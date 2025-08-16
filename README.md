# Task App Backend

A comprehensive task management REST API backend built with Spring Boot, designed for both individual and team collaboration. The application implements Kanban board functionality with role-based access control, JWT authentication, and real-time activity tracking.


## Features

### 🔐 Authentication & Authorization
- **JWT Authentication**: Secure token-based authentication
- **Role-based Access Control**: Project managers and members with different permissions
- **User Registration & Login**: Complete user account management
- **Password Management**: Secure password change functionality

### 📊 Project Management
- **Project Creation & Management**: Create and organize projects with descriptions
- **Team Collaboration**: Add/remove team members with role assignments
- **Project Roles**: MANAGER and MEMBER roles with different permissions
- **Activity Streams**: Real-time project activity tracking and notifications

### ✅ Task Management
- **Kanban Board**: Four-column board (TODO, IN_PROGRESS, DONE, FAILED)
- **Task Assignment**: Assign tasks to team members
- **Priority Levels**: LOW, MEDIUM, HIGH priority classification
- **Due Date Tracking**: Automatic status updates based on deadlines
- **Task Status Management**: Dynamic status changes with activity logging

### 🔔 Notifications & Messaging
- **Real-time Notifications**: Alert users about task assignments and deadlines
- **Activity Tracking**: Complete audit trail of all project and task activities
- **Deadline Alerts**: Automatic notifications for approaching and overdue tasks

## Technology Stack

### Backend Framework
- **Spring Boot 3.x.x**: Main application framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database interaction layer
- **Hibernate**: ORM implementation

### Database & Persistence
- **MySQL 8**: Primary database
- **JPA/Hibernate**: Object-relational mapping
- **Connection Pooling**: Optimized database connections

### Security
- **JWT (JSON Web Tokens)**: Stateless authentication
- **BCrypt**: Password encryption
- **CORS Configuration**: Cross-origin resource sharing setup

### Build & Deployment
- **Gradle 8.2.1**: Build automation and dependency management
- **Java 17**: Target runtime environment
- **Heroku**: Cloud deployment platform

## Project Structure

```
src/main/java/com/taskapp/be/
├── TaskAppBeApplication.java           # Main application entry point
├── controller/                         # REST API endpoints
│   ├── AuthController.java            # Authentication endpoints
│   ├── ProjectController.java         # Project management APIs
│   ├── TaskController.java            # Task management APIs
│   └── UserController.java            # User profile management
├── dto/                               # Data Transfer Objects
│   ├── request/                       # Request DTOs
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   ├── ProjectRequest.java
│   │   ├── TaskRequest.java
│   │   ├── TaskDto.java
│   │   ├── StatusDto.java
│   │   ├── UpdateRequest.java
│   │   └── ChangePasswordRequest.java
│   └── response/                      # Response DTOs
│       ├── LoginResponse.java
│       ├── InfoResponse.java
│       └── ProjectResponse.java
├── model/                             # JPA Entity classes
│   ├── User.java                      # User entity
│   ├── Project.java                   # Project entity
│   ├── Task.java                      # Task entity
│   ├── UserProject.java              # User-Project relationship
│   └── ActivityStream.java           # Activity logging
├── repository/                        # Data access layer
│   ├── UserRepository.java
│   ├── ProjectRepository.java
│   ├── TaskRepository.java
│   ├── UserProjectRepository.java
│   └── ActivityStreamRepo.java
├── security/                          # Security configuration
│   ├── SecurityConfig.java           # Main security setup
│   ├── custom/
│   │   ├── CustomUserDetails.java
│   │   └── CustomUserDetailsService.java
│   └── jwt/
│       ├── JwtTokenProvider.java
│       └── JwtAuthenticationFilter.java
├── service/                          # Business logic layer
│   ├── UserService.java
│   ├── ProjectService.java
│   ├── TaskService.java
│   └── impl/                        # Service implementations
│       ├── UserServiceImpl.java
│       ├── ProjectServiceImpl.java
│       ├── TaskServiceImpl.java
│       └── MessageService.java
├── util/                            # Utility enums and classes
│   ├── Status.java                  # Task status enum
│   ├── Priority.java               # Task priority enum
│   ├── ProjectRole.java            # Project role enum
│   └── Type.java                   # Activity type enum
└── exception/                       # Exception handling
    └── GlobalExceptionHandler.java
```

## API Endpoints

### Authentication Endpoints
```
POST /api/v1/auth/register     # User registration
POST /api/v1/auth/login        # User login
```

### User Management
```
GET    /api/v1/user/{username}                    # Get user profile
PUT    /api/v1/user/update/{username}             # Update user info
PUT    /api/v1/user/change-password/{username}    # Change password
GET    /api/v1/user/{username}/message            # Get notifications
```

### Project Management
```
POST   /api/v1/project/{username}                 # Create project
GET    /api/v1/project/get-all/{username}         # Get user's projects
GET    /api/v1/project/{id}                       # Get project by ID
PUT    /api/v1/project/update/{id}                # Update project
DELETE /api/v1/project/delete/{id}/{username}     # Delete project
GET    /api/v1/project/{id}/members               # Get project members
GET    /api/v1/project/{id}/managers              # Get project managers
POST   /api/v1/project/{id}/add/{username}        # Add member to project
POST   /api/v1/project/{id}/out/{username}        # Leave project
```

### Task Management
```
POST /api/v1/task/{projectId}/project/{username}/creator  # Create task
GET  /api/v1/task/{taskId}                                # Get task by ID
GET  /api/v1/task/todo/{projectId}/project                # Get TODO tasks
GET  /api/v1/task/in-progress/{projectId}/project         # Get IN_PROGRESS tasks
GET  /api/v1/task/done/{projectId}/project                # Get DONE tasks
GET  /api/v1/task/failed/{projectId}/project              # Get FAILED tasks
POST /api/v1/task/{taskId}/assignee/{username}            # Assign task
POST /api/v1/task/{taskId}/status                         # Change task status
POST /api/v1/task/{taskId}/update                         # Update task
POST /api/v1/task/{taskId}/delete                         # Delete task
GET  /api/v1/task/{username}/assignee                     # Get user's assigned tasks
```

## Installation & Setup

### Prerequisites
- Java 17 or higher
- MySQL 8.0 or higher
- Gradle 8.2.1 or higher (or use provided wrapper)

### Local Development Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/baodt278/task-app-backend.git
   cd task-app-backend
   ```

2. **Database Setup**:
   ```sql
   CREATE DATABASE taskapp;
   CREATE USER 'taskapp_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON taskapp.* TO 'taskapp_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Configure Database Connection**:
   Update `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/taskapp
       username: taskapp_user
       password: your_password
   ```

4. **Set Environment Variables** (for production):
   ```bash
   export SPRING_DATASOURCE_USERNAME=your_db_username
   export SPRING_DATASOURCE_PASSWORD=your_db_password
   ```

5. **Build the application**:
   ```bash
   ./gradlew build
   ```

6. **Run the application**:
   ```bash
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8080`

### Docker Setup (Optional)

Create a `docker-compose.yml`:
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: taskapp
      MYSQL_USER: taskapp_user
      MYSQL_PASSWORD: password
      MYSQL_ROOT_PASSWORD: root_password
    ports:
      - "3306:3306"
  
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - mysql
    environment:
      SPRING_DATASOURCE_USERNAME: taskapp_user
      SPRING_DATASOURCE_PASSWORD: password
```

## Configuration

### Key Configuration Files

**`application.yml`**: Main configuration
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/taskapp
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect
    hibernate:
      ddl-auto: update
    show-sql: true
```

### Security Configuration
- JWT tokens expire after 90 days
- Password encryption using BCrypt
- Role-based access control for all endpoints
- CORS enabled for cross-origin requests

## Database Schema

### Core Entities

**Users**: User account information
- `id`, `username`, `full_name`, `email`, `password`

**Projects**: Project information and activity streams
- `id`, `name`, `description`, `streams`

**Tasks**: Task details and relationships
- `id`, `name`, `description`, `start_date`, `end_date`, `status`, `priority`
- Foreign keys to `creator_user`, `assignee_user`, `project`

**UserProject**: Many-to-many relationship with roles
- `id`, `user_id`, `project_id`, `project_role`

**ActivityStream**: Activity logging
- `id`, `message`, `time`

## Business Logic

### Task Status Management
- **TODO**: Initial task state
- **IN_PROGRESS**: Task being worked on
- **DONE**: Completed task
- **FAILED**: Automatically set when past due date

### Automatic Status Updates
Tasks are automatically marked as FAILED when:
- Current date > end date AND status != DONE

### Role Permissions
- **MANAGER**: Can create, update, delete projects and all tasks
- **MEMBER**: Can create tasks, update assigned tasks, change task status

### Activity Stream
All actions are logged with timestamps:
- Project creation/updates
- User joining/leaving projects
- Task creation/updates/assignments
- Status changes

## Notification System

### Message Types
1. **Assignment**: "Assigned to you - Please check the description before start {task_name}"
2. **Deadline**: "Deadline - Today is the end of {task_name}"
3. **Failed**: "Failed - {task_name} is marked as FAILED"

### Notification Logic
- Users receive notifications for tasks assigned to them
- Deadline warnings for tasks due today
- Failed task notifications for overdue items

## Frontend Integration

This backend is designed to work with the [Task App Frontend](https://github.com/baodt278/task-app-frontend).

### Mobile App Support
- RESTful API compatible with mobile applications
- JWT token-based authentication for stateless sessions
- Structured JSON responses for easy parsing

## Testing

Run tests using:
```bash
./gradlew test
```

## Deployment

### Heroku Deployment
The application is configured for Heroku deployment:

1. **Set Java runtime** (already configured in `system.properties`):
   ```
   java.runtime.version=17
   ```

2. **Configure Heroku environment variables**:
   ```bash
   heroku config:set SPRING_DATASOURCE_USERNAME=your_username
   heroku config:set SPRING_DATASOURCE_PASSWORD=your_password
   ```

3. **Deploy**:
   ```bash
   git push heroku main
   ```

### Production Considerations
- Use environment variables for sensitive configuration
- Configure connection pooling for database
- Set up monitoring and logging
- Enable HTTPS in production
- Configure CORS for your domain

## API Usage Examples

### Authentication
```bash
# Register
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","email":"john@example.com","password":"password123","rePassword":"password123"}'

# Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","password":"password123"}'
```

### Project Management
```bash
# Create project (requires JWT token)
curl -X POST http://localhost:8080/api/v1/project/john_doe \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"name":"My Project","description":"Project description","username":"john_doe"}'
```

### Task Management
```bash
# Create task
curl -X POST http://localhost:8080/api/v1/task/1/project/john_doe/creator \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"name":"Task 1","description":"Task description","startDate":"2024-01-01","endDate":"2024-01-31","priority":"HIGH"}'
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

If you encounter any issues or have questions:
1. Check the existing issues on GitHub
2. Create a new issue with detailed description
3. For urgent matters, contact the development team

## Related Projects

- [Task App Frontend](https://github.com/baodt278/task-app-frontend) - React Native mobile application
- Mobile APK download available through Expo

---

**Built with ❤️ using Spring Boot and modern Java technologies**
