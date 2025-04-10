# User Management System

A Spring Boot-based application for managing user-related operations, including authentication, email notifications, and password resets.

## Features

- User registration and authentication
- Email notifications for registration, password reset, and welcome messages
- Password reset functionality
- User management (CRUD operations)
- Integration with PostgreSQL and Redis
- JWT-based authentication

## Technologies Used

- **Java**: Programming language
- **Spring Boot**: Framework for building the application
- **PostgreSQL**: Database for storing user data
- **Redis**: In-memory data store for caching
- **Gradle**: Build tool
- **Jakarta Mail**: For sending emails
- **JWT**: For secure authentication

## Configuration

The application uses the following configuration properties (defined in `application.properties`):

- **Database**:
  - `spring.datasource.url`: URL of the PostgreSQL database
  - `spring.datasource.username`: Database username
  - `spring.datasource.password`: Database password
- **JWT**:
  - `jwt.secret`: Secret key for signing tokens
  - `jwt.expiration`: Token expiration time in milliseconds
- **Redis**:
  - `spring.data.redis.host`: Redis host
  - `spring.data.redis.port`: Redis port
- **Email**:
  - `spring.mail.host`: SMTP server host
  - `spring.mail.port`: SMTP server port
  - `spring.mail.username`: Email username
  - `spring.mail.password`: Email password

## Endpoints

The application provides the following RESTful endpoints:

- **Authentication**:
  - `POST /auth/register`: Register a new user
  - `POST /auth/login`: Authenticate a user
  - `GET /auth/confirm`: Confirm user registration
- **User Management**:
  - `GET /users/{id}`: Retrieve user by ID
  - `GET /users/email/{email}`: Retrieve user by email
  - `GET /users`: Retrieve all users
  - `PUT /users/{id}`: Update user details
  - `DELETE /users/{id}`: Delete user by ID
  - `DELETE /users`: Delete all users
- **Password Reset**:
  - `POST /password/forgot`: Initiate password reset
  - `POST /password/confirm`: Confirm reset code
  - `POST /password/change`: Change password