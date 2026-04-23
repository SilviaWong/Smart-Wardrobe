# 智能衣橱

Smart Wardrobe 现已升级为一个全栈智能电子衣橱系统。它包含基于 Spring Boot 3 的后端服务和基于 Flutter 的移动端应用。系统通过 RESTful API 提供用户认证、衣物管理、智能穿搭画布、数据统计以及衣物生命周期管理（断舍离挑战）等功能。

## 技术栈

**后端：**
- Spring Boot 3
- MyBatis Plus
- Spring Security + JWT
- MySQL 8
- Maven
- Docker / Nginx（可选部署）

**移动端：**
- Flutter (Dart)
- Provider (状态管理)
- Dio (网络请求)
- Flutter Card Swiper (卡片滑动断舍离交互)
- FL Chart (数据可视化图表)

## 快速开始

### 后端运行指南
1. 在 `src/main/resources/application.yml` 中配置 MySQL 账号和 Base64 编码的 JWT 密钥。
2. 在数据库中创建相应的表结构。
3. 编译并运行服务：

```bash
mvn clean install
java -jar target/smart-wardrobe-0.0.1-SNAPSHOT.jar
```

### 移动端 App 运行指南
1. 确保后端已在本地 8080 端口运行。
2. 进入 `smart_wardrobe_app` 目录。
3. 运行 `flutter pub get` 获取所有依赖。
4. 运行 `flutter run` 在 iOS/Android 模拟器或真机上启动应用。

## 核心功能

1. **单品管理**：添加衣物，支持录入图片、分类、颜色、适用季节及价格。
2. **穿搭画布**：在自由画布上对衣物进行拖拽、缩放、旋转，创造专属穿搭方案并保存布局。
3. **断舍离挑战**：自动筛选超过 180 天未穿的闲置衣物，通过左滑（舍弃）或右滑（保留）轻松管理衣橱。
4. **数据统计分析**：自动追踪单次穿着成本（CPW），并直观呈现不同分类和季节衣物的占比情况。

## API 概览

| 方法 | 路径 | 描述 |
| ---- | ---- | ---- |
| POST | `/api/auth/login` | 用户登录（返回 JWT） |
| POST | `/api/auth/register` | 用户注册 |
| GET | `/api/clothes` | 获取衣物列表 |
| GET | `/api/clothes/{id}` | 获取单个衣物详情 |
| POST | `/api/clothes` | 新增衣物 |
| PUT | `/api/clothes/{id}` | 更新衣物 |
| DELETE | `/api/clothes/{id}` | 删除衣物 |
| GET | `/api/stats` | 获取衣橱统计信息（分类、季节占比等） |
| POST | `/api/files` | 上传衣物图片 |
| POST | `/api/outfits` | 保存穿搭画布布局 |
| POST | `/api/wears` | 记录衣物今日已穿 |
| GET | `/api/locations` | 获取可用储物位置列表 |

*注：为了开发阶段调试方便，部分接口暂时放行。生产环境部署前请修改 `SecurityConfig.java` 开启 JWT 校验。*

## 文件上传

上传的文件默认保存到服务端的 `uploads/` 目录，通过 `/uploads/**` 对外暴露静态访问。

## Docker 与 Nginx

可将项目封装为 Docker 镜像并通过 Nginx 提供统一入口，镜像需暴露 `8080` 端口。请确保在 Nginx 配置中增大 `client_max_body_size` 以支持图片文件上传。

