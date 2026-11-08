# JobTrack SA

**Track opportunities. Build your future.**

## 1. Project Overview
JobTrack SA is an Android application designed to help South African job seekers manage and track their job applications in one place.

Users can register, log in, save job opportunities, monitor application statuses, and update or delete application records.

## 2. Main Features
- User registration and login.
- Secure password hashing on the backend.
- JWT-based authentication.
- Add and save job applications.
- View application records.
- View application details.
- Edit and delete applications.
- Track statuses such as Saved, Applied, Interview and Offer.
- Settings for application preferences.

## 3. Technology Stack
| Component | Technology |
|---|---|
| Mobile application | Kotlin |
| Development environment | Android Studio |
| Backend | Node.js and Express |
| Database | PostgreSQL |
| API communication | REST API |
| Authentication | JWT and bcrypt |
| API deployment | Render |
| Version control | Git and GitHub |

## 4. System Architecture
The Android application communicates with the Express backend through REST API requests. The backend processes requests, authenticates users and communicates with the PostgreSQL database.

**Architecture flow:**

Android Application → REST API → Express Backend → PostgreSQL Database

## 5. API Endpoints
| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/register` | Register a user |
| POST | `/api/auth/login` | Authenticate a user |
| POST | `/api/auth/google` | Google authentication |
| POST | `/api/applications` | Create an application |
| GET | `/api/applications` | Retrieve applications |
| GET | `/api/applications/:id` | Retrieve application details |
| PUT | `/api/applications/:id` | Update an application |
| DELETE | `/api/applications/:id` | Delete an application |

## 6. Backend Deployment
The backend API is deployed on Render:

https://jobtracksa.onrender.com/

## 7. Database
The application uses PostgreSQL with two main tables:
- `users`
- `applications`

The applications table is linked to users through a foreign key.

## 8. Testing
The following application functions were tested:
- User registration.
- User login.
- Creating job applications.
- Retrieving job applications.
- Viewing application details.
- Updating application records.
- Deleting application records.
- Settings screen functionality.

The backend API test suite also passed during development.

## 9. Screenshots
Application screenshots are stored in:

`submission/screenshots/`

## 10. Running the Project
To run the Android application, open the project in Android Studio, allow Gradle synchronisation and run the app on an Android emulator or compatible device.

To run the backend locally:
1. Navigate to the backend directory.
2. Install dependencies using `npm install`.
3. Configure the required environment variables in a local `.env` file.
4. Start the backend using `npm run dev`.

Do not commit `.env` files, database passwords, JWT secrets or private Firebase service-account credentials.

## 11. Limitations and Future Improvements
- Complete end-to-end testing of Google Sign-In.
- Add job-search integration with external job platforms.
- Add application reminders and notifications.
- Improve offline support and synchronisation.
- Expand automated testing.

## 12. Conclusion
JobTrack SA provides a centralised platform for managing job applications. The project demonstrates Android development, REST API integration, database management, authentication, testing and deployment.