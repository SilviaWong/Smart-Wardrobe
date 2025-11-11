# 智能衣橱后端服务

Smart Wardrobe 是一个基于 Spring Boot 3 的智能电子衣橱后端服务，提供认证、衣物管理、统计以及文件上传等 RESTful API。

## 技术栈

- Spring Boot 3
- MyBatis Plus
- Spring Security + JWT
- MySQL 8
- Maven
- Logback
- Docker / Nginx（可选部署）

## 快速开始

1. 在 `src/main/resources/application.yml` 中配置 MySQL 账号和 Base64 编码的 JWT 密钥。
2. 创建数据库表（见下方建表 SQL）。
3. 编译并运行服务：

```bash
mvn clean install
java -jar target/smart-wardrobe-0.0.1-SNAPSHOT.jar
```

## 数据库结构

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

> 可选扩展：后续可以加入 `tag`、`clothes_tag`、`outfit`、`outfit_item`、`wear_log` 与 `image_resource` 等表，用于标签体系、搭配管理、穿搭记录和统一的资源存储。

## API 概览

| 方法 | 路径 | 描述 |
| ---- | ---- | ---- |
| POST | `/api/auth/login` | 用户登录（返回 JWT） |
| POST | `/api/auth/register` | 用户注册 |
| GET | `/api/clothes` | 获取衣物列表（分页） |
| POST | `/api/clothes` | 新增衣物 |
| PUT | `/api/clothes/{id}` | 更新衣物 |
| DELETE | `/api/clothes/{id}` | 删除衣物 |
| GET | `/api/stats` | 衣橱统计信息 |
| POST | `/api/files` | 上传衣物图片 |

所有衣橱相关接口需要携带 `Authorization: Bearer <token>` 请求头。

## 文件上传

上传的文件默认保存到 `uploads/` 目录，通过 `/uploads/**` 对外暴露静态访问。

## 前端集成指南

若需要将 Smart Wardrobe 后端对接到前端项目（如 React / Vue / Angular），可参考以下步骤：

1. **配置 API 基础地址**
   - 确认后端地址（本地开发可为 `http://localhost:8080`，部署时可由 Nginx 代理）。
   - 在前端创建可复用的 API 客户端，并统一在 `/api` 前缀下发送请求，例如：
     ```js
     // src/services/http.js
     import axios from 'axios';

     const http = axios.create({
       baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
     });

     export default http;
     ```
   - 通过环境变量（如 `VITE_API_BASE_URL` 或 `REACT_APP_API_BASE_URL`）管理不同环境的地址。

2. **处理认证 Token**
   - 登录时调用 `POST /api/auth/login`，获取 JWT。
   - 将 Token 存储在内存或 `localStorage` 中。
   - 对受保护的接口统一添加 `Authorization: Bearer <token>` 请求头：
     ```js
     http.interceptors.request.use((config) => {
       const token = localStorage.getItem('smartWardrobeToken');
       if (token) {
         config.headers.Authorization = `Bearer ${token}`;
       }
       return config;
     });
     ```
   - 退出登录时清理 Token。

3. **开发环境下的 CORS 处理**
   - 后端默认开启跨域（见 `WebConfig`），部署到 Nginx 时需确保代理转发 `Authorization` 头并处理 HTTPS。
   - 前端开发服务器可通过代理避免额外的 CORS 设置。以 Vite 为例：
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

4. **统一响应结构处理**
   - 后端统一返回 `{ code, message, data }`，前端可在 `response.data.data` 中获取业务数据，并使用 `response.data.message` 处理错误。
   - 文件上传使用 `POST /api/files` 的 multipart 请求，响应中的 `imageUrl` 可用于显示预览。

5. **环境配置同步**
   - 在前端 `.env` 中同步配置（如 `VITE_API_BASE_URL=https://wardrobe.example.com/api`）。
   - 若通过 Docker Compose 部署，可在前端容器中设置 `API_BASE_URL=http://smart-wardrobe-backend:8080/api` 指向后端容器。

## Docker 与 Nginx

可将项目封装为 Docker 镜像并通过 Nginx 提供统一入口，镜像需暴露 `8080` 端口，Nginx 负责转发 `/api` 请求并处理静态资源。

