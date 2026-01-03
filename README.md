# Paid Parking Management System

A real-world **backend MVP** for a paid parking system built with modern Java stack.  
Designed as a strong portfolio project for **Backend / Full-Stack Java positions**.

## Features (MVP)

- Parking spots management (CRUD)
- User registration and login with **JWT authentication**
- Rent a parking spot with immediate (fake) payment
- Cancel rent (frees the spot)
- **Real-time updates** of available spots via **WebSocket (STOMP)** — all connected clients see changes instantly
- Automatic rent expiration (scheduled task)
- REST API with **Swagger UI** (OpenAPI 3)
- PostgreSQL database
- Containerized with **Docker + docker-compose**
- Unit and integration tests
- **GitHub Actions CI/CD** (build + test on push)
- Code quality with **Qodana** (JetBrains static analysis)

## Tech Stack

- **Backend**: Spring Boot 4.0.1, Java 25
- **Database**: PostgreSQL 16
- **Security**: JWT (JJWT), Spring Security
- **API Documentation**: springdoc-openapi (Swagger UI)
- **Real-time**: Spring WebSocket + STOMP
- **ORM**: Spring Data JPA (Hibernate)
- **Build**: Maven
- **Containerization**: Docker + docker-compose
- **CI/CD**: GitHub Actions
- **Code Quality**: Qodana

## How to Run

### With Docker (recommended)

### Clone the repository
   ```bash
   git clone https://github.com/AntonMuzhytskyi/paid-parking-system.git
   cd paid-parking-system 
   ```
### Start PostgreSQL and backend
docker-compose up -d

### Application will be available at http://localhost:8080

### Swagger UI

Open: http://localhost:8080/swagger-ui/index.html

**Testing the API:**

1. Register a new user: `POST /api/v1/auth/register`
2. Login to get JWT token: `POST /api/v1/auth/login`
3. Click the **Authorize** button (top right) → enter `Bearer <your-token>`
4. Test the flow:
   - Create parking spots (`POST /api/v1/parking-spots`)
   - View available spots (`GET /api/v1/parking-spots/available`)
   - Book a spot (`POST /api/v1/rents/book/{spotId}`)
   - Cancel booking (`POST /api/v1/rents/cancel/{rentId}`)

### Real-time Demo

Open Swagger UI in **two browser tabs** (or use multiple devices):

- Book a spot in one tab
- Watch the list of available spots update **instantly** in the other tab via WebSocket

### Frontend Integration Example
This backend is already connected to a React frontend (separate repository) that:

- Displays a beautiful interactive parking map (200 spots)
- Allows users to book and cancel spots with modals
- Uses JWT authentication
- Updates the map after booking/cancellation

### Why This Project?

This project was developed step-by-step to showcase modern backend development skills. It demonstrates:

- Clean architecture and best practices in Spring Boot
- Secure stateless authentication with JWT
- Real-time communication using WebSocket/STOMP
- Production concerns: testing, Docker, CI/CD, code quality
- Focus on readable, maintainable, and explainable code (perfect for interviews)

Feel free to explore, star, fork, or use as inspiration!

---

**Author**: Anton Muzhytskyi  
**Looking for**: Java Backend Developer / Full-Stack roles (remote or relocation)  
**Contact**: muzhytskyianton@gmail.com  | https://www.linkedin.com/in/anton-muzhytskyi-5728b9383/ | https://github.com/AntonMuzhytskyi