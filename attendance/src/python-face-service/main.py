from fastapi import FastAPI, UploadFile, File, HTTPException
import cv2
import numpy as np
import insightface
from insightface.app import FaceAnalysis
from sklearn.metrics.pairwise import cosine_similarity
import json
import pyodbc # conect SQL Server
app = FastAPI()


DB_CONFIG = {
    'server': 'localhost',
    'database': 'attendance', #  Database
    'username': 'tàikhoanSQL(sa)',           # Tài khoản SQL
    'password': 'nhập mk của bạn vô'           # Mật khẩu SQL (Sửa lại cho đúng)
}

# --- 1. KHỞI TẠO MODEL AI ---
# Load model InsightFace (Lần đầu chạy sẽ mất vài giây để load)
print("Dang khoi tao model AI...")
model = FaceAnalysis(name='buffalo_l', providers=['CPUExecutionProvider'])
model.prepare(ctx_id=0, det_size=(640, 640))
print("Model Al nhan dien da san sang !")

def get_db_connection():
   # Bỏ chữ "ODBC Driver 17 for", chỉ để lại "SQL Server"
    conn_str = f'DRIVER={{SQL Server}};SERVER={DB_CONFIG["server"]};DATABASE={DB_CONFIG["database"]};UID={DB_CONFIG["username"]};PWD={DB_CONFIG["password"]}'
    return pyodbc.connect(conn_str)

#  LẤY VECTOR TỪ ẢNH :dùng cho Java gọi khi train khuon mat nhan vien
@app.post("/face/embedding")
async def extract_face_embedding(file: UploadFile = File(...)):
    # đọc ảnh từ request
    file_bytes = await file.read()
    nparr = np.frombuffer(file_bytes, np.uint8)
    img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    # Detect
    faces = model.get(img)
    if len(faces) == 0:
        return {"success": False, "message": "Khong tim thay khuon mat"}

    # lấy mặt to nhất (nguoi dung chinh dien)
    face = sorted(faces, key=lambda x: (x.bbox[2]-x.bbox[0]) * (x.bbox[3]-x.bbox[1]))[-1]

    # trả về vector
    return {
        "success": True,
        "embedding": face.embedding.tolist()
    }

# --- API 2: SO SÁNH KHUÔN MẶT :nhân viên vào checkin/checlout
@app.post("/face/verify")
async def verify_face(file: UploadFile = File(...)):
    # 1. Đọc ảnh gửi lên
    file_bytes = await file.read()
    nparr = np.frombuffer(file_bytes, np.uint8)
    img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    # 2. Detect khuôn mặt  ảnh gửi lên
    faces = model.get(img)
    if len(faces) == 0:
        return {"success": False, "message": "Khong tim thay khuon mat"}

    # Lấy vector của người đang chấm công
    current_face = sorted(faces, key=lambda x: (x.bbox[2]-x.bbox[0]) * (x.bbox[3]-x.bbox[1]))[-1]
    current_embedding = current_face.embedding.reshape(1, -1)

    # 3. so sánh databasse
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        cursor.execute("SELECT id, face_embedding FROM employees WHERE face_embedding IS NOT NULL")

        best_score = 0
        best_employee_id = None

        rows = cursor.fetchall()
        for row in rows:
            emp_id = row[0]
            emb_str = row[1] # json on db

            if not emb_str: continue

            try:
                # Convert chuỗi JSON(DB)->  numpy array
                saved_emb = np.array(json.loads(emb_str)).reshape(1, -1)

                #  độ giống nhau :Cosine Similarity
                score = cosine_similarity(current_embedding, saved_emb)[0][0]

                if score > best_score:
                    best_score = score
                    best_employee_id = emp_id
            except:
                continue

        conn.close()

        # 4.  ngưỡng (Threshold)
        # 0.5 là ngưỡng trung bình, chinh theo độ khắt khe
        THRESHOLD = 0.5

        if best_score > THRESHOLD:
            return {
                "success": True,
                "employeeId": best_employee_id,
                "score": float(best_score)
            }
        else:
            return {
                "success": False,
                "message": "Nguoi la (Khong khop voi du lieu)",
                "score": float(best_score)
            }

    except Exception as e:
        return {"success": False, "message": "Loi Server: " + str(e)}

# option: python main.py nếu không dùng uvicorn command
if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)