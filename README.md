# Smart Wardrobe Backend

Smart Wardrobe is a Spring Boot 3 service that powers an intelligent electronic wardrobe. It offers authentication, wardrobe management, statistics and file uploads via RESTful APIs.

## Tech Stack

- Spring Boot 3
- MyBatis Plus
- Spring Security + JWT
- MySQL 8
- Maven
- Logback
- Docker/Nginx (deployment ready)

## Getting Started

1. Update `src/main/resources/application.yml` with your MySQL credentials and a Base64 encoded JWT secret.
2. Create the required tables (see below for schema).
3. Build and run the service:

```bash
mvn clean install
java -jar target/smart-wardrobe-0.0.1-SNAPSHOT.jar
```

## Database Schema

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    create_time DATETIME NOT NULL
);

CREATE TABLE clothes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    color VARCHAR(50),
    season VARCHAR(50),
    tags VARCHAR(100),
    image_url VARCHAR(255),
    create_time DATETIME NOT NULL,
    CONSTRAINT fk_clothes_user FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## API Overview

| Method | Path | Description |
| ------ | ---- | ----------- |
| POST | `/api/auth/login` | User login (returns JWT) |
| POST | `/api/auth/register` | User registration |
| GET | `/api/clothes` | List clothes (paginated) |
| POST | `/api/clothes` | Create clothing item |
| PUT | `/api/clothes/{id}` | Update clothing item |
| DELETE | `/api/clothes/{id}` | Delete clothing item |
| GET | `/api/stats` | Aggregated wardrobe statistics |
| POST | `/api/files` | Upload clothing images |

All wardrobe endpoints require `Authorization: Bearer <token>` header.

## File Uploads

Uploaded files are stored under the `uploads/` directory. Static access is exposed via `/uploads/**`.

## Docker & Nginx

The project can be containerized with Docker and served behind Nginx. Configure the image to expose port `8080` and proxy requests from Nginx.
