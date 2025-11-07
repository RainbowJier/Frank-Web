# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在此代码库中工作时提供指导。

## 项目概述

这是一个基于若依-Vue3 框架的 Frank-Web前端应用程序，采用现代 Vue 3 技术栈构建的综合管理后台系统。

## 技术栈

- **前端框架**: Vue 3.4.31 (Composition API)
- **UI 组件库**: Element Plus 2.7.6
- **构建工具**: Vite 5.3.2
- **状态管理**: Pinia 2.1.7
- **路由**: Vue Router 4.4.0
- **HTTP 客户端**: Axios 0.28.1
- **样式预处理**: SCSS/Sass 1.77.5

## 开发命令

```bash
# 安装依赖
yarn --registry=https://registry.npmmirror.com

# 启动开发服务器 (运行在 80 端口)
yarn dev

# 生产环境构建
yarn build:prod

# 预发布环境构建
yarn build:stage

# 预览生产构建
yarn preview
```

## 项目架构

### 目录结构

- `src/api/` - 按功能模块组织的 API 服务 (system, monitor, tool)
- `src/assets/` - 静态资源，包括 SVG 图标和样式文件
- `src/components/` - 可复用的 Vue 组件 (分页、文件上传等)
- `src/router/` - Vue Router 路由配置
- `src/store/` - Pinia 状态管理模块
- `src/utils/` - 工具函数和辅助方法
- `src/views/` - 按功能模块组织的页面组件
- `src/directive/` - 自定义 Vue 指令
- `src/plugins/` - 插件配置

### 核心架构模式

1. **模块化 API 组织**: API 调用按业务域组织在 `src/api/` 中
2. **组件化架构**: `src/components/` 中的可复用组件，支持全局注册
3. **状态管理**: `src/store/modules/` 中的 Pinia 存储模块，管理不同关注点 (用户、权限、应用设置)
4. **权限系统**: 基于路由和按钮级别的权限控制
5. **请求拦截器**: 集中式 HTTP 请求/响应处理，包含认证和错误处理

### 核心功能

系统包含完整的管理模块:
- 用户管理和基于角色的访问控制
- 部门和岗位管理
- 菜单和权限管理
- 系统配置的数据字典管理
- 系统监控 (日志、缓存、服务器指标)
- 任务调度和作业管理
- 代码生成工具
- 文件上传和图片处理

### 全局组件

以下组件在 `main.js` 中全局注册:
- `Pagination` - 标准化分页组件
- `FileUpload` / `ImageUpload` - 文件处理组件
- `RightToolbar` - 表格操作工具栏
- `Editor` - 富文本编辑器
- `DictTag` - 字典值显示组件

### 请求架构

应用使用集中式 HTTP 客户端 (`src/utils/request.js`)，包含:
- JWT token 认证
- 请求/响应拦截器
- 自动 token 刷新处理
- 文件下载功能
- 防重复请求机制

### 环境配置

应用通过 Vite 环境变量支持多环境:
- 开发环境: 本地开发，支持热重载
- 预发布环境: `yarn build:stage`
- 生产环境: `yarn build:prod`

## 开发注意事项

- 应用默认使用中文语言环境
- 开发服务器使用 80 端口 (可能需要管理员权限)
- SVG 图标通过自定义插件处理
- 使用 SCSS 进行样式处理，全局样式导入在 `src/assets/styles/index.scss`
- 权限系统基于路由，在 `src/permission.js` 中进行权限检查