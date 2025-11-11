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
CREATE TABLE `user` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    gender VARCHAR(10),
    region VARCHAR(50),
    style_preference VARCHAR(100),
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE clothes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    color VARCHAR(50),
    season VARCHAR(50),
    tags VARCHAR(100),
    brand VARCHAR(50),
    price DECIMAL(10, 2),
    purchase_date DATE,
    image_url VARCHAR(255),
    description TEXT,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_clothes_user FOREIGN KEY (user_id) REFERENCES `user`(id)
);
```

> Optional extensions: `tag`, `clothes_tag`, `outfit`, `outfit_item`, `wear_log`, and `image_resource` tables can be introduced later to support tagging, outfit curation, usage tracking, and centralized asset management.

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

## Front-End Integration Guide

To connect a front-end client (for example, a React/Vue/Angular single-page application) with the Smart Wardrobe backend, follow the steps below:

1. **Configure the API base URL**
   - Determine the backend origin (e.g., `http://localhost:8080` for local development or the deployed domain behind Nginx).
   - Create a reusable API client in the front-end that prefixes all requests with `/api`, for example:
     ```js
     // src/services/http.js
     import axios from 'axios';

     const http = axios.create({
       baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
     });

     export default http;
     ```
   - Expose the base URL through an environment variable such as `VITE_API_BASE_URL` (Vite) or `REACT_APP_API_BASE_URL` (Create React App).

2. **Handle authentication tokens**
   - Call `POST /api/auth/login` with the username and password collected from the login form.
   - Store the returned JWT (e.g., in memory or `localStorage`).
   - Attach `Authorization: Bearer <token>` headers to all protected requests:
     ```js
     http.interceptors.request.use((config) => {
       const token = localStorage.getItem('smartWardrobeToken');
       if (token) {
         config.headers.Authorization = `Bearer ${token}`;
       }
       return config;
     });
     ```
   - On logout, remove the stored token to invalidate the session client-side.

3. **Support CORS during development**
   - The backend allows cross-origin requests by default via `WebConfig`. If you deploy behind Nginx, ensure the proxy forwards the `Authorization` header and handles HTTPS termination.
   - When running the front-end dev server, proxy API requests to avoid additional CORS configuration. For Vite:
     ```js
     // vite.config.js
     export default defineConfig({
       server: {
         proxy: {
           '/api': {
             target: process.env.VITE_API_BASE_URL || 'http://localhost:8080',
             changeOrigin: true,
           },
         },
       },
     });
     ```

4. **Consume API responses**
   - All responses follow the unified structure `{ code, message, data }`. Handle success by reading from `response.data.data` and process validation errors using `response.data.message`.
   - For file uploads, use multipart form submissions against `POST /api/files` and read the returned `imageUrl` to display previews.

5. **Synchronize environments**
   - Align the front-end `.env` (e.g., `VITE_API_BASE_URL=https://wardrobe.example.com/api`) with the backend deployment URLs.
   - When using Docker Compose, add the front-end service and set `API_BASE_URL` via environment variables to reference the backend container name (e.g., `http://smart-wardrobe-backend:8080/api`).

## Docker & Nginx

The project can be containerized with Docker and served behind Nginx. Configure the image to expose port `8080` and proxy requests from Nginx.
