# Attendance Management System - Checkin using Face Recognition (Backend Core)

The Core API Service built with Spring Boot. It acts as the central orchestrator connecting the Web Client (Angular), the Database (SQL Server), and the AI Engine (Python).

## Project Ecosystem
This backend serves the Frontend Web Application.
* Frontend Repository: [frontend-attendance-management-system](https://github.com/bunrieu2005/frontend-attendance-management-system)

---

## Overview
This backend system handles the complex business logic for the Attendance System, including JWT Authentication, Salary Calculation algorithms, and integration with a Python Microservice for Computer Vision tasks.

## Tech Stack
* Framework: Java Spring Boot (Spring MVC, Spring Security, Spring Data JPA)
* Database: SQL Server (Auto-schema update via Hibernate)
* AI Integration: REST Client communicating with Python Flask Service

## Python AI Service Integration
The system offloads heavy image processing tasks to a specialized Python Service.
* Core Library: Built on top of the face_recognition library (utilizing dlib's state-of-the-art face recognition).
* Technologies: Python 3.x, Flask, OpenCV, Numpy.
* Mechanism:
    1. Receives image from Spring Boot.
    2. Detects faces and generates 128-d encodings.
    3. Calculates Euclidean distance to verify identity.

## Core Functionality

### Comprehensive HR Management
* Employee Lifecycle: Full CRUD capabilities to manage staff profiles, contract details, and uploaded avatars.
* Organizational Structure: Manage departments and assign employees to specific units.
* Face Registration: API endpoints to register and update Face Embeddings for the AI engine.

### Face Recognition Middleware
* Orchestration: Acts as the secure bridge between the User (Angular) and the AI (Python), ensuring no direct access to the AI service from the public internet.
* Validation: Verifies identity based on vector distance (< 0.5) and logs attendance records.

### Payroll Engine
* Automated Calculation: Complex algorithm that aggregates working hours, tracks late arrivals/early departures, and processes approved leave requests to generate monthly payslips.

### Security
* Authentication: Secure Login via JWT (JSON Web Token).
* Authorization: Role-based access control (RBAC) ensuring only Admins can access sensitive HR and Payroll data.

## Setup & Run
1. Database: Create an SQL Server database named 'attendance'.
2. Configuration: Update 'src/main/resources/application.properties' with your DB credentials.
3. Run: 'mvn spring-boot:run'

## Roadmap & TODO
* [ ] Real-time Notifications: Implement Push Notifications for check-in success and leave request approvals.
* [ ] Dockerization: Containerize the full stack for easier deployment.
* [ ] CI/CD: Setup GitHub Actions pipeline.

## Author
* Tran Vu Tan (bunrieu)
* University of Sciences – Hue (HUSC)