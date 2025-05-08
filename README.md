# CloudVault - Banking Management System

CloudVault is a modern, full-stack banking management system built with Spring Boot and Angular. It provides a comprehensive solution for managing banking operations with a focus on security, user experience, and modern web technologies.

## 🚀 Features

- **User Authentication & Authorization**
  - Secure JWT-based authentication
  - Role-based access control
  - Session management

- **Account Management**
  - Create and manage bank accounts
  - Multiple account types support
  - Account balance tracking

- **Fixed Deposits**
  - Create and manage fixed deposits
  - Track FD status and maturity
  - Automated interest calculations

- **Transaction Management**
  - Secure fund transfers
  - Transaction history
  - Statement generation (PDF)

- **Dashboard**
  - Real-time account overview
  - Transaction summaries
  - Quick access to common operations

## 🛠️ Technology Stack

### Backend
- Java 17
- Spring Boot 3.2.3
- Spring Security
- Spring Data JPA
- MySQL Database
- JWT Authentication
- Swagger/OpenAPI Documentation
- iText7 for PDF generation

### Frontend
- Angular 19
- Angular Material
- Bootstrap 5
- TypeScript
- RxJS
- NgBootstrap

## 📋 Prerequisites

- Java 17 or higher
- Node.js (LTS version)
- MySQL 8.0 or higher
- Maven
- Angular CLI

## 🚀 Getting Started

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Configure the database in `src/main/resources/application.properties`

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   ng serve
   ```

4. Access the application at `http://localhost:4200`

## 🔒 Security Features

- JWT-based authentication
- Password encryption
- Role-based access control
- Secure session management
- Input validation
- CORS configuration
- XSS protection

## 📚 API Documentation

The API documentation is available through Swagger UI when the backend is running:
- URL: `http://localhost:8080/swagger-ui.html`

## 🧪 Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
ng test
```

## 📦 Project Structure

```
CloudVault/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/bms/
│   │   │   │       ├── config/
│   │   │   │       ├── controller/
│   │   │   │       ├── dto/
│   │   │   │       ├── entity/
│   │   │   │       ├── exception/
│   │   │   │       ├── repository/
│   │   │   │       ├── service/
│   │   │   │       └── util/
│   │   │   └── resources/
│   │   └── test/
│   └── pom.xml
└── frontend/
    ├── src/
    │   ├── app/
    │   ├── assets/
    │   └── environments/
    ├── package.json
    └── angular.json
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- FocusedMS Team

## 🙏 Acknowledgments

- Spring Boot Team
- Angular Team
- All contributors and supporters 