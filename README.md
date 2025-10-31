# 农产品溯源系统 (Farm Search System)

## 项目简介

农产品溯源系统是一个基于前后端分离架构的现代化Web应用，旨在实现农产品从种植/养殖、屠宰、批发到零售的全链条溯源管理。系统包含三个主要模块：管理员后台管理系统、企业端管理系统和消费者查询系统。

## 项目结构

```
├── FarmSystem-master/
│   ├── farm-search-admin-frontend/  # 管理员前端系统
│   └── farm-search-backend/         # 后端服务
├── farm-search-consumer-frontend-master/  # 消费者前端系统
└── farm-search-enterprise-frontend-master/ # 企业前端系统
```

## 技术栈

### 后端技术栈
- **框架**: Spring Boot 3.5.6
- **ORM**: MyBatis-Plus
- **数据库**: MySQL
- **缓存**: Redis
- **安全认证**: JWT
- **API文档**: Knife4j
- **分布式锁**: Redisson
- **WebSocket**: 实时通信支持
- **AOP**: 切面编程（用于限流等）

### 前端技术栈

#### 管理员前端
- **框架**: Vue 3 + TypeScript
- **构建工具**: Vue CLI
- **UI组件库**: Ant Design Vue 4.x
- **状态管理**: Vuex 4.x
- **路由管理**: Vue Router 4.x
- **图表库**: ECharts
- **富文本编辑器**: WangEditor

#### 消费者前端
- **框架**: Vue 3 + TypeScript
- **构建工具**: Vite 7.x
- **UI组件库**: Ant Design Vue 4.x
- **状态管理**: Pinia
- **路由管理**: Vue Router 4.x

#### 企业前端
- **框架**: Vue 3 + TypeScript
- **构建工具**: Vite 7.x
- **UI组件库**: Ant Design Vue 4.x
- **状态管理**: Pinia
- **路由管理**: Vue Router 4.x

## 系统功能

### 1. 管理员系统
- **系统管理**
  - 管理员账号管理（高级管理员可创建、更新、删除管理员账号）
  - 管理员权限控制（基于角色的访问控制）
  - 登录认证与令牌刷新
- **企业管理**
  - 节点企业注册信息管理
  - 企业数据统计分析
- **数据监控**
  - 溯源数据统计与展示

### 2. 企业系统
- **企业信息管理**
  - 企业基本信息维护
  - 企业资质管理
- **批次管理**
  - 养殖批次管理
  - 屠宰批次管理
  - 批发批次管理
  - 零售批次管理
- **溯源码生成与管理**

### 3. 消费者系统
- **溯源码查询**
  - 输入溯源码获取产品全链条溯源信息
  - 溯源信息可视化展示

## 核心业务流程

1. **企业注册与认证**：节点企业提交注册信息，管理员审核通过后成为系统用户
2. **产品批次管理**：各节点企业管理各自环节的产品批次信息
3. **溯源码生成**：在产品流转过程中生成唯一的溯源码
4. **信息上传与共享**：各环节企业上传产品相关信息
5. **消费者查询**：消费者通过溯源码查询产品全链条信息

## 系统特点

1. **全链条覆盖**：覆盖农产品从生产到消费的完整流程
2. **权限控制**：基于JWT和角色的精细化权限管理
3. **性能优化**：使用Redis缓存和分布式锁提升系统性能
4. **安全保障**：密码加密、请求限流等多重安全措施
5. **用户友好**：现代化UI设计，良好的用户体验

## 安装部署

### 环境要求

#### 后端
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

#### 前端
- Node.js 20.19.0 或 22.12.0+
- npm 9.0+

### 后端部署

1. 克隆代码仓库
2. 配置MySQL数据库连接信息（application.properties/application.yml）
3. 配置Redis连接信息
4. 执行Maven构建：`mvn clean package`
5. 运行jar包：`java -jar farm-search-backend-0.0.1-SNAPSHOT.jar`

### 前端部署

#### 管理员前端
1. 进入管理员前端目录：`cd FarmSystem-master/farm-search-admin-frontend`
2. 安装依赖：`npm install`
3. 开发环境运行：`npm run serve-dev`
4. 生产环境构建：`npm run build-prod`

#### 消费者前端
1. 进入消费者前端目录：`cd farm-search-consumer-frontend-master`
2. 安装依赖：`npm install`
3. 开发环境运行：`npm run dev`
4. 生产环境构建：`npm run build`

#### 企业前端
1. 进入企业前端目录：`cd farm-search-enterprise-frontend-master`
2. 安装依赖：`npm install`
3. 开发环境运行：`npm run dev`
4. 生产环境构建：`npm run build`

## API文档

系统集成了Knife4j API文档，启动后端服务后可访问：`http://localhost:8080/doc.html`

## 系统访问

- 管理员系统：`http://localhost:8081`
- 消费者系统：`http://localhost:8082`
- 企业系统：`http://localhost:8083`

