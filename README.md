# Smart Wardrobe

Smart Wardrobe is a full-stack intelligent electronic wardrobe system. It consists of a Spring Boot 3 backend service and a Flutter mobile application. It offers authentication, wardrobe management, intelligent outfit canvas creation, statistics, and item lifecycle management (decluttering) via RESTful APIs.

> Looking for the Chinese version? See [readme_zh.md](readme_zh.md).

## Tech Stack

**Backend:**
- Spring Boot 3
- MyBatis Plus
- Spring Security + JWT
- MySQL 8
- Maven
- Docker/Nginx (deployment ready)

**Mobile Client:**
- Flutter (Dart)
- Provider (State Management)
- Dio (Networking)
- Flutter Card Swiper (Tinder-style decluttering)
- FL Chart (Data visualization)

## Getting Started

### Backend Setup
1. Update `src/main/resources/application.yml` with your MySQL credentials and a Base64 encoded JWT secret.
2. Create the required tables in your database.
3. Build and run the service:

```bash
mvn clean install
java -jar target/smart-wardrobe-0.0.1-SNAPSHOT.jar
```

### Mobile App Setup
1. Ensure the backend is running locally on port 8080.
2. Navigate to the `smart_wardrobe_app` directory.
3. Run `flutter pub get` to fetch dependencies.
4. Run `flutter run` on an iOS/Android simulator or physical device.

## Core Features

1. **Item Management**: Add clothing items with images, category, color, season, and price.
2. **Outfit Canvas**: Drag, scale, and rotate clothing items on a freeform canvas to create outfits and save their spatial configurations.
3. **Declutter Challenge**: Swipe left/right on items that haven't been worn in over 180 days to effortlessly decide whether to keep or discard them.
4. **Usage Analytics**: Automatically tracks cost-per-wear (CPW) and visualizes your wardrobe breakdown by category and season.

## API Overview

| Method | Path | Description |
| ------ | ---- | ----------- |
| POST | `/api/auth/login` | User login (returns JWT) |
| POST | `/api/auth/register` | User registration |
| GET | `/api/clothes` | List clothes |
| GET | `/api/clothes/{id}` | Get clothing item details |
| POST | `/api/clothes` | Create clothing item |
| PUT | `/api/clothes/{id}` | Update clothing item |
| DELETE | `/api/clothes/{id}` | Delete clothing item |
| GET | `/api/stats` | Aggregated wardrobe statistics (by category/season) |
| POST | `/api/files` | Upload clothing images |
| POST | `/api/outfits` | Save outfit canvas configuration |
| POST | `/api/wears` | Log an item as worn today |
| GET | `/api/locations` | List available wardrobe storage locations |

*Note: For development convenience, some endpoints currently permit unauthorized access. To secure them for production, update `SecurityConfig.java` to require JWT authentication.*

## File Uploads

Uploaded files are stored under the `uploads/` directory on the server. Static access is exposed via `/uploads/**`.

## Docker & Nginx

The project can be containerized with Docker and served behind Nginx. Configure the image to expose port `8080` and proxy requests from Nginx. Ensure that the proxy handles multipart file uploads correctly by increasing `client_max_body_size`.
