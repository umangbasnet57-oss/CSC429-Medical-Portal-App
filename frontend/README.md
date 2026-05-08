# CSC 429 – Medical Portal Application

**Course:** CSC 429 – Object Oriented Software Development  
**Level:** Undergraduate  
**Project Type:** Agile Team Software Engineering Project  

---

## Project Overview

This repository contains the collaborative **Medical Portal Application** developed for **CSC 429**.

The goal of this project is to design and implement a **desktop-based patient management system** for a hypothetical primary care medical practice using modern **software engineering principles**, including:

- Modular architecture (MVC)
- RESTful service design
- Database integration
- Agile teamwork & version control
- Automated testing and documentation

The system supports **three user roles**:

- **Admin/Clerk** – manages users and appointments  
- **Patient** – views and cancels personal appointments  
- **Doctor** – views assigned schedule (day/week/month)  

---

## System Architecture

The application is designed as **two loosely-coupled components**:

### Backend – Spring Boot REST API
- Provides secure endpoints for:
  - User management
  - Appointment scheduling
  - Search functionality
- Connects to a relational database (**MySQL/MariaDB**)
- Tested using **Postman** and **JUnit**

### Frontend – JavaFX Desktop Client
- Single **login interface** for all roles  
- Role-based **dashboards**:
  - Admin dashboard → CRUD users & appointments + search  
  - Patient dashboard → view & cancel appointments  
  - Doctor dashboard → view schedule by time range  
- Communicates with backend via **HTTP REST calls**

---

## 🗄️ Data Model

### User Entity
- `id`
- `firstName`
- `lastName`
- `username`
- `passwordHash`
- `role` (admin, patient, doctor)
- `lastLogin`
- `lastPasswordChange`

### Appointment Entity
- `id`
- `date`
- `startTime`
- `endTime`
- `patientId`
- `doctorId`
- `lastUpdated`

---

## ⚙️ Core Functionalities

- Common **login & authentication system**
- **Role-based dashboards**
- Admin can:
  - Create, update, delete users
  - Schedule and modify appointments
  - Search appointments by patient or doctor
- Patients can:
  - View personal appointments
  - Cancel future appointments
- Doctors can:
  - View assigned appointments
  - Filter by **day, week, or month**
- Fully functional **REST API**
- Persistent **database storage**
- **Password change** capability for all users

---

## 🧪 Testing & Quality Assurance

- Unit and integration tests implemented using **JUnit**
- REST endpoints verified using **Postman**
- Target **≥ 80% test coverage**
- Validation rules include:
  - No overlapping appointments for the same doctor
  - Patients can cancel only their own appointments
  - Role-based access enforcement

## 📂 Repository Structure
---
## 👥 Team Collaboration

Development followed **Agile practices**:

- GitHub for **version control & branching**
- Feature development on **individual branches**
- **Kanban board** for backlog tracking
- Peer reviews and frequent merges
- Sprint demos aligned with course **Epic milestones**

---

## 📑 Documentation Deliverables

- UML diagrams (architecture, data flow, use cases)
- Design rationale and planning artifacts
- Test plans and coverage reports
- Sprint retrospectives
- AI usage log documenting prompt assistance and evaluation

---

## 🚀 How to Run the Project

### 1️⃣ Start Database
Configure and run **MySQL/MariaDB** with the schema provided in `/docs`.

### 2️⃣ Run Backend API
### 3️⃣ Run JavaFX Client
### 4️⃣ Test API
Import the **Postman collection** from `/docs/postman` and verify endpoints.

---

## **Team Members**
- Soyesh Subbha
- Madhav Sharma
- Umang Basnet
- Lamine
- Fredrick

## 🤖 Use of AI Tools

AI tools (including ChatGPT) were used to:

- Clarify architectural design decisions  
- Generate documentation drafts  
- Assist with debugging and testing strategies  

All AI assistance was **reviewed, validated, and modified** by the development team to ensure correctness and academic integrity.

---

## 📅 Course Timeline Alignment

- **Epic 1:** Core architecture & login  
- **Epic 2:** Admin CRUD functionality  
- **Epic 3:** Patient & doctor dashboards  
- **Epic 4:** Search, filtering, UI polish  
- **Epic 5:** Testing, documentation, final demo  

---
## Architecture Design Overview

---

  <img width="1396" height="786" alt="image" src="https://github.com/user-attachments/assets/cc6154c8-e00c-4435-b468-bd8617542a8f" />



## Flat Design Overview

---

<img width="1297" height="726" alt="image" src="https://github.com/user-attachments/assets/dc4b5e00-7730-49e8-9ce6-59291a60e7cf" />


## Data Flow Diagram

---

<img width="1371" height="523" alt="image" src="https://github.com/user-attachments/assets/abb66609-5fbb-4ea8-8e27-bfdc33b311f6" />

## Use Case Diagram

<img width="1112" height="779" alt="image" src="https://github.com/user-attachments/assets/3bf9891d-0c0f-4374-8a1b-fb1598fe3d23" />




