# CloudVault - Banking Management System

CloudVault is a modern, full-stack banking management system built with Spring Boot and Angular. It provides a comprehensive, secure, and user-friendly solution for managing banking operations, with a strong focus on robust authentication, seamless session renewal, and modern web technologies.

## 🚀 Features

- **User Authentication & Authorization**
  - Secure JWT-based authentication
  - Refresh token-based session renewal (auto-refresh)
  - Role-based access control (pluggable for admin features)
  - Auto-logout on token expiry/invalidity
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
- JWT Authentication & Refresh Token Flow
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
2. Configure the database in `src/main/resources/application.properties`.
3. **Run database migrations:**
   - Ensure the `refresh_token` table is created (for refresh token support). If using JPA/Hibernate, this is automatic. For production, use a migration tool (e.g., Flyway/Liquibase).
4. Build the project:
   ```bash
   mvn clean install
   ```
5. Run the application:
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

- JWT-based authentication (short-lived access tokens)
- Refresh token-based session renewal (long-lived, stored in DB)
- Auto-refresh and auto-logout for seamless and secure sessions
- Password encryption (BCrypt)
- Role-based access control
- Secure session management
- Input validation (backend & frontend)
- CORS configuration (allows Authorization header)
- XSS protection

## 🛡️ Authentication & Security Deep Dive

### Overview
CloudVault uses a robust, modern authentication system based on JWT access tokens and long-lived refresh tokens. This ensures both security and a seamless user experience.

### 🔑 Token-Based Authentication Flow
- **Login:**
  - User submits credentials to `/api/auth/login`.
  - Backend issues:
    - **Access Token (JWT):** Short-lived (e.g., 1 hour), used for API requests.
    - **Refresh Token:** Long-lived (e.g., 7 days), random, stored in the database.
  - Both tokens are returned to the frontend.
- **Accessing Protected Resources:**
  - Frontend attaches the access token to every API request via the `Authorization: Bearer <token>` header.
  - Backend validates the token and grants/denies access.
- **Token Expiry & Refresh:**
  - If the access token expires (401 error), the frontend automatically calls `/api/auth/refresh-token` with the refresh token and account number.
  - If valid, backend issues a new access token (and optionally a new refresh token).
  - If invalid/expired, user is logged out.
- **Logout:**
  - Frontend calls `/api/auth/logout` with the refresh token.
  - Backend deletes the refresh token from the database, invalidating the session.

### 🧩 Backend Security Architecture
- **JWT:**
  - Signed with a secret key, includes user/account info, short expiry.
  - Validated on every request to protected endpoints.
- **Refresh Token:**
  - Random, stored in DB with expiry and user association.
  - Only used to obtain new access tokens.
- **Endpoints:**
  - `POST /api/auth/login` — Login, returns both tokens.
  - `POST /api/auth/refresh-token` — Accepts refresh token & account number, returns new access token.
  - `POST /api/auth/logout` — Invalidates refresh token.
- **CORS:**
  - Configured to allow frontend origins and `Authorization` header.
- **Password Security:**
  - Passwords hashed with BCrypt.
- **Role-Based Access:**
  - (Pluggable) for future admin features.

### 🧩 Frontend Auth Flow
- **Token Storage:**
  - Access token and refresh token stored in `localStorage`.
- **HTTP Interceptor:**
  - Attaches access token to all requests.
  - On 401, attempts to refresh token and retry request.
  - Logs out if refresh fails.
- **Route Guards:**
  - Prevents navigation to protected pages without a valid token.
- **Logout:**
  - Clears all tokens and session data.

### 📦 Other Major Features & Security
- **Account Management:**
  - Account creation, balance tracking, and updates are all protected by authentication.
- **Fixed Deposits:**
  - Only authenticated users can create/view FDs. All FD operations are logged.
- **Transactions:**
  - Fund transfers require authentication and are validated for sufficient balance and account existence.
  - Transaction history is only accessible to the account owner.
- **Dashboard:**
  - Shows only the logged-in user's data, never leaks other users' info.

### ✅ Security Checklist
| Feature                        | Status | Notes |
|-------------------------------|--------|-------|
| JWT access token (short-lived) | ✅     | 1 hour expiry |
| Refresh token (long-lived)     | ✅     | 7 days, stored in DB |
| Refresh endpoint               | ✅     | Validates token, issues new access token |
| Logout invalidates refresh     | ✅     | Deletes from DB |
| HTTP interceptor auto-refresh  | ✅     | Retries failed requests |
| Route guards                   | ✅     | Protects all sensitive routes |
| Password hashing               | ✅     | BCrypt |
| CORS for Authorization         | ✅     | Configured |
| Input validation               | ✅     | Backend & frontend |
| (Optional) HttpOnly cookies    | ❓     | For refresh token, future improvement |

### 🧪 Auth Testing Quickstart
1. **Login:**
   - Use the login form. Check that both tokens are stored in localStorage.
2. **Access protected pages:**
   - Should work while access token is valid.
3. **Let access token expire:**
   - Remove it from localStorage or wait for expiry. Trigger an API call; the app should refresh the token and continue.
4. **Logout:**
   - Both tokens are cleared, and you are redirected to login.
5. **Try using an invalid/expired refresh token:**
   - Should be logged out and required to log in again.

### 🛡️ Recommendations
- For even better security, store the refresh token in an HttpOnly cookie (requires backend changes).
- Add automated tests for login, refresh, and logout flows.
- Regularly monitor logs for unauthorized access attempts.

## 📚 API Documentation

The API documentation is available through Swagger UI when the backend is running:
- URL: `http://localhost:8080/swagger-ui.html`
- Key endpoints:
  - `POST /api/auth/login`
  - `POST /api/auth/refresh-token`
  - `POST /api/auth/logout`
  - (plus all account, transaction, FD, and dashboard endpoints)

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

### Auth Flow Testing
- See the "Auth Testing Quickstart" in the security deep dive above for step-by-step instructions.

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