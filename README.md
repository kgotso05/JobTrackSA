# JobTrack SA

**Track opportunities. Build your future.**

JobTrack SA is an Android application designed to help students, graduates and job seekers organise and track job applications, internships, learnerships and graduate programme opportunities.

The application allows users to securely create an account, log in and manually track opportunities through the different stages of the application process.

## Features

- User registration and login
- Secure password hashing using bcrypt
- JWT-based authentication
- Add job applications
- View saved applications
- View individual application details
- Edit applications
- Delete applications
- Track application status
- Dashboard application statistics
- User settings
- Input validation and error handling
- Hosted REST API
- Hosted PostgreSQL database
- Automated Android unit testing
- Automated backend API testing
- Continuous Integration using GitHub Actions

## Application Statuses

JobTrack SA allows an opportunity to move through the following stages:

1. Saved
2. Applied
3. Assessment
4. Interview
5. Offer
6. Accepted
7. Rejected

## Technology Stack

### Android Application

- Kotlin
- XML Views
- Android Studio
- Retrofit
- Gson
- OkHttp
- SharedPreferences

### Backend

- Node.js
- Express.js
- PostgreSQL
- bcrypt
- JSON Web Tokens (JWT)
- node-postgres (`pg`)
- CORS
- dotenv

### Testing and DevOps

- JUnit
- Jest
- Supertest
- Git
- GitHub
- GitHub Actions

### Hosting

- Render Web Service
- Render PostgreSQL
- HTTPS REST API

## System Architecture

The application follows a client-server architecture:

Android Application  
↓ HTTPS / Retrofit  
Node.js + Express REST API  
↓  
PostgreSQL Database

The Android application communicates with the hosted REST API using Retrofit. The REST API handles authentication, application management and database operations.

## Security

JobTrack SA implements several security measures:

- Passwords are never stored in plaintext.
- Passwords are salted and hashed using bcrypt.
- Authentication is performed using JSON Web Tokens.
- Protected application endpoints require a valid JWT.
- User-specific application queries are restricted using the authenticated user's ID.
- Environment variables are used for sensitive backend configuration.
- The `.env` file is excluded from Git.

## REST API

### Authentication

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/register` | Register a user |
| POST | `/api/auth/login` | Authenticate a user |

### Applications

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/applications` | Create an application |
| GET | `/api/applications` | Retrieve user's applications |
| GET | `/api/applications/:id` | Retrieve an application |
| PUT | `/api/applications/:id` | Update an application |
| DELETE | `/api/applications/:id` | Delete an application |

Application endpoints require JWT authentication.

## Database

The prototype uses two primary PostgreSQL tables:

### users

Stores:

- User ID
- Full name
- Email
- Password hash
- Creation timestamp

### applications

Stores:

- Application ID
- User ID
- Company
- Job title
- Location
- Source URL
- Application status
- Closing date
- Date applied
- Notes
- Creation and update timestamps

A user can have multiple applications. Applications are linked to users through a foreign key.

## Automated Testing

The project contains automated tests for both the Android application and backend.

Android unit tests cover reusable validation logic such as:

- Email validation
- Password validation
- Date validation
- URL validation
- Company and job-title validation

Backend tests cover API behaviour including:

- API availability
- Database health
- Authentication validation
- Protection of authenticated endpoints

## Continuous Integration

GitHub Actions automatically runs the Android and backend test suites when changes are pushed to configured branches.

The CI workflow contains two jobs:

- Android Unit Tests
- Backend API Tests with a temporary PostgreSQL service

Both jobs must pass before changes are considered ready for integration.

## Running the Android Application

1. Clone the repository.
2. Open the project in Android Studio.
3. Allow Gradle to synchronize.
4. Build the project.
5. Start an Android emulator or connect a compatible Android device.
6. Run the application.

The current application is configured to communicate with the hosted JobTrack SA REST API over HTTPS.

## Running the Backend Locally

Navigate to the backend directory:

```bash
cd backend
