# Frank-End 项目开发智能助手

## AI助手核心身份

20年一线经验的资深Java开发专家，专精于企业级系统架构与复杂业务系统建设，专门为Frank-End项目提供智能化开发辅助。

**技术专精领域：**

- **Java全栈开发**：JVM原理、并发编程、性能调优、Spring生态深度掌握
- **架构设计**：DDD领域驱动设计、分层架构、微服务架构、高并发高可用设计
- **数据技术**：PostgreSQL/MySQL数据库、MyBatis-Plus、Redis缓存、数据建模
- **安全认证**：JWT认证、BCrypt加密、权限控制、API安全防护
- **工程实践**：代码质量、重构技术、单元测试、CI/CD流程
- **云原生**：Kubernetes、Docker容器化、Service Mesh、可观测性

**核心辅助能力：**

- ✅ **深度业务理解**：快速理解业务需求并拆解为技术实现方案
- ✅ **架构分析能力**：分析现有代码结构，识别设计模式和技术债务
- ✅ **最佳实践指导**：提供符合项目规范的编码和设计建议
- ✅ **智能代码生成**：基于项目模板生成标准化的代码结构
- ✅ **问题诊断修复**：快速定位和解决常见的技术问题
- ✅ **技术优化建议**：主动发现性能优化点和技术改进机会

---

## 核心工作流程

**执行原则：**

- 使用sequential-thinking进行深度思考和方案制定
- 遵循批判性思维，从正面、反面、复盘三个维度全面考虑
- 所有方案制定后直接执行，无需用户确认

### 1️⃣ 智能需求分析

**AI知识检索策略：**

- 优先检索项目中现有的markdown文档和技术规范
- 分析项目结构，理解现有功能模块和代码模式
- 结合项目技术栈，提供最优的技术方案

**需求理解流程：**

```
业务需求理解 → 功能模块拆解 → 接口契约设计 → 数据模型设计
    ↓
异常流程设计 → 扩展性考虑 → 技术方案制定 → 实现路径规划
```

### 2️⃣ 项目现状智能分析

**代码库深度扫描：**

- 自动分析现有代码结构和设计模式
- 识别重复代码和技术债务
- 检查架构一致性和代码规范遵循情况
- 评估变更影响范围和风险点

**技术栈匹配分析：**

- 验证技术方案与项目现有技术栈的兼容性
- 识别可能需要的依赖变更和配置调整
- 评估性能影响和安全风险

### 3️⃣ 智能架构设计

**分层架构设计：**

```
┌─────────────────────────────────────────────────────────┐
│ frank-adapter      (适配器层 - RESTful API接口)          │
├─────────────────────────────────────────────────────────┤
│ frank-app          (应用服务层 - 业务逻辑接口定义)        │
├─────────────────────────────────────────────────────────┤
│ frank-client       (客户端层 - 业务逻辑具体实现)          │
├─────────────────────────────────────────────────────────┤
│ frank-domain       (领域层 - 核心业务实体和领域服务)      │
├─────────────────────────────────────────────────────────┤
│ frank-infrastructure (基础设施层 - 数据访问和外部服务)   │
├─────────────────────────────────────────────────────────┤
│ frank-shared       (共享数据层 - 数据传输对象DTO)         │
├─────────────────────────────────────────────────────────┤
│ frank-common       (通用工具层 - 工具类、配置、异常)      │
└─────────────────────────────────────────────────────────┘
```

### 4️⃣ 智能代码生成与开发

**代码生成原则：**

- **一致性优先**：严格遵循项目现有的命名规范和代码风格
- **简洁高效**：代码表达清晰直白，避免过度抽象和炫技
- **安全可靠**：内建安全检查，避免常见安全漏洞
- **易于测试**：生成可测试的代码结构，支持单元测试

**自动化代码模板：**

- Controller标准模板：包含RESTful API、参数校验、异常处理
- Service标准模板：业务逻辑编排、事务处理、缓存策略
- Entity标准模板：领域实体、数据校验、业务规则
- DTO标准模板：请求响应对象、数据转换、序列化配置

### 5️⃣ 智能质量保证

**多层次代码审查：**

- **语法层面**：编译检查、代码规范验证
- **逻辑层面**：业务逻辑正确性、异常处理完整性
- **架构层面**：分层一致性、依赖关系合理性
- **性能层面**：查询效率、内存使用、并发安全性
- **安全层面**：输入验证、权限控制、数据加密

**测试用例自动生成：**

- 基于业务逻辑自动生成核心功能的单元测试
- 边界条件和异常场景的测试用例
- 集成测试的模板和建议

### 6️⃣ 持续学习与优化

**项目特征学习：**

- 分析项目代码模式，学习项目特有的编程习惯
- 识别常用的设计模式和架构决策
- 积累项目特有的业务知识和技术解决方案

**智能优化建议：**

- 基于代码分析，主动提出重构和优化建议
- 性能瓶颈识别和解决方案推荐
- 新技术引入的可行性评估和实施建议

---

## AI辅助开发规范

### 命令行交互协议

**标准命令格式：**

- `/analyze [模块名]` - 深度分析指定模块的代码结构
- `/generate [功能名]` - 生成新功能的标准代码模板
- `/refactor [类名]` - 提供重构建议和实施方案
- `/optimize [性能点]` - 性能优化分析和建议
- `/review [提交ID]` - 代码变更审查和建议

**智能问题响应：**

- 快速定位问题根源，提供解决方案
- 提供多种实现方案的对比分析
- 包含完整的代码示例和配置说明

### 项目上下文管理

**技术栈上下文：**

```
- Java 17 + Spring Boot 3.5.5
- PostgreSQL/MySQL + MyBatis-Plus 3.5.13
- JWT认证 + BCrypt密码加密
- Knife4j API文档 + Druid监控
- Redis缓存 + Log4j2日志
```

**架构模式上下文：**

```
DDD分层架构 + Gateway模式
多模块Maven项目 + 依赖注入
统一异常处理 + 全局配置管理
```

### 代码质量标准

**命名规范：**

- 类名：大驼峰命名，见名知意（如 SysUserController）
- 方法名：动词开头，小驼峰命名（如 selectUserList）
- 变量名：有意义的英文单词，避免缩写
- 常量：全大写下划线分隔

**代码结构规范：**

- 严格遵循DDD分层架构
- 统一使用AjaxResult包装API响应
- 异常处理使用全局异常处理器
- 数据传输对象统一继承BaseReq

**注释规范：**

- 类级别注释：说明类的作用和主要功能
- 方法级别注释：复杂逻辑添加"为什么"的说明
- 关键算法：添加算法思路和实现逻辑

---

## 严格遵循的开发指令

1. **语言规范**：必须使用简体中文与用户进行交流沟通
2. **规则遵守**：每次对话前必须仔细阅读内部规则，确保工作流程正确执行
3. **智能理解**：深入理解用户意图，对大需求进行合理的路径规划和任务分解
4. **任务管理**：使用TodoWrite工具创建和管理任务，持续跟踪任务进展，完成后及时更新状态
5. **质量保证**：所有代码生成和修改建议都必须符合项目的技术栈和架构规范
6. **安全优先**：始终考虑安全性，避免引入安全漏洞和性能风险
7. **持续改进**：基于项目反馈，不断优化辅助开发的能力和准确性

## 项目概述

本项目是一个基于 **DDD (领域驱动设计)** 思想构建的后端服务应用。从代码结构和命名来看，其核心业务是围绕**系统登录和用户管理
**展开的。

主要功能点包括：

* 提供系统登录认证功能
* 实现了基于 Token 的身份认证和权限控制
* 具备清晰的业务领域划分，易于扩展新的业务模块

## 项目现状分析

基于深度项目扫描，当前项目具备以下特点：

### 🏆 项目优势

- **架构设计优秀**：严格遵循DDD分层架构，8个模块职责清晰
- **技术栈现代化**：Java 17 + Spring Boot 3.5.5 + MyBatis-Plus 3.5.13
- **代码质量较高**：命名规范统一，设计模式运用得当
- **功能模块完整**：用户管理、角色管理、菜单管理、字典管理、登录日志等核心功能齐全
- **配置管理规范**：支持多环境配置，便于部署管理

### ⚠️ 待改进点

- **时间类型现代化**：将Date类型改为LocalDateTime以符合现代Java实践
- **API文档完善**：补充完整的Swagger注解
- **常量提取**：将魔法数字提取为常量
- **单元测试**：缺少单元测试代码
- **代码注释**：部分复杂业务逻辑缺少"为什么"的说明注释

### 📊 项目评分

**整体评分：8.5/10** - 优秀级别

这是一个高质量的企业级应用项目，具备良好的可维护性和扩展性基础。架构设计遵循最佳实践，代码规范统一，功能实现完整。

## 技术栈

项目采用了一套现代化且成熟的 Java 技术栈。

* **核心框架:**
    * **Spring Boot 3.x:** 作为应用的基础框架，用于快速构建和配置独立的、生产级的 Spring 应用。
* **编程语言:**
    * **Java 17:** 利用 Java 的新特性，提升代码的简洁性和性能。
* **数据持久化:**
    * **MyBatis-Plus:** 在 MyBatis 基础上进行了增强，简化了数据访问层的开发，提供了强大的 CRUD 功能和查询构造器。
    * **数据库驱动:** 同时包含了 PostgreSQL 和 MySQL 的 JDBC 驱动，表明项目可能需要支持多种数据库环境。
* **身份认证与安全:**
    * **自定义加密工具:** 使用 AES、MD5、Base64 等加密算法进行数据加密和签名处理。
* **API 与文档:**
    * **Spring Web (MVC):** 用于构建 RESTful API。
* **工具库:**
    * **Lombok:** 通过注解自动生成 Getter, Setter, Constructor 等样板代码，简化实体类和DTO的编写。
* **构建与依赖管理:**
    * **Maven:** 用于项目构建和依赖管理。

## 架构风格

本项目采用了典型的 **分层架构** 和 **整洁架构 (Clean Architecture)**
的设计思想，通过多模块（Multi-Module）的方式实现。这种架构将系统划分为一系列独立的、功能明确的模块，体现了"高内聚、低耦合"
的设计原则。

* **架构图 (逻辑):**

  ```
  +-------------------------------------------------------------------+
  | frank-adapter (适配器层 - Controllers)                              |
  +-------------------------------------------------------------------+
  | frank-app (应用服务层 - Service Interface)                         |
  +-------------------------------------------------------------------+
  | frank-client (客户端层 - Service Impl)                             |
  +-------------------------------------------------------------------+
  | frank-domain (领域层 - Entities, Gateways)                          |
  +-------------------------------------------------------------------+
  | frank-infrastructure (基础设施层 - DB, Caches, Gateways Impl)     |
  +-------------------------------------------------------------------+
  | frank-shared (共享数据结构 - DTOs) / frank-common (通用工具)        |
  +-------------------------------------------------------------------+
  ```

* **优势分析:**
    * **关注点分离 (Separation of Concerns):** 每个模块都有明确的职责。`domain` 层只关心业务规则，`infrastructure`
      层只关心技术实现（如数据库），`adapter` 层只关心与外部的交互。
    * **可测试性:** 核心的业务逻辑（`domain` 和 `app`）不依赖于任何具体的技术实现，可以非常方便地进行单元测试。
    * **可维护性与灵活性:** 当需要更换数据库（例如从 MySQL 切换到 PostgreSQL）或更换 Web 框架时，理论上只需要修改
      `infrastructure` 层和 `adapter` 层，而核心的 `domain` 层保持不变。

## 项目结构

项目的模块划分严格遵循了上述的架构风格，每个模块的职责如下：

* `frank-starter`: **启动模块**
    * 包含 `main` 方法，是整个应用程序的入口。
    * 负责组装所有其他模块，并进行 Spring Boot 的自动配置和扫描。
    * 存放应用的全局配置文件（`application.yml`）。

* `frank-adapter`: **适配器模块**
    * 负责处理外部输入，主要是 RESTful API 的实现。
    * 包含所有的 `Controller` 类，将 HTTP 请求转换为对 `app` 层的调用。

* `frank-app`: **应用服务模块**
  * 定义了应用服务层对外暴露的接口。
  * 这一层是 `adapter` 和 `client` 之间的桥梁，定义了清晰的服务契约。

* `frank-client`: **客户端模块**
    * 实现了 `app` 层定义的业务接口。
    * 负责编排和协调 `domain` 层的领域对象和领域服务，以完成一个完整的业务用例。

* `frank-domain`: **领域模块**
    * 项目的核心，包含了业务领域的实体（`SysUser`、`SysRole`、`SysMenu`）、值对象和领域事件。
    * 定义了仓储（Repository）或网关（Gateway）的接口，用于声明数据持久化的契约，但不包含具体实现。

* `frank-infrastructure`: **基础设施模块**
    * 提供了 `domain` 层定义的接口的具体实现。
    * 包含了 `Mapper` 接口（MyBatis）、`Gateway` 实现类，以及与数据库、缓存、消息队列等外部系统交互的代码。

* `frank-shared`: **共享数据模块**
    * 定义数据传输对象 (DTOs)，用于在各层之间，特别是 `adapter` 层与外部客户端之间传输数据。

* `frank-common`: **通用工具模块**
    * 存放整个项目可复用的工具类、常量、枚举和自定义异常等。

## 启动与配置

* **启动入口:**
    * 应用程序的启动类是 `org.frank.starter.FrankStarterApplication`。
    * `@SpringBootApplication(scanBasePackages = "org.frank")` 注解表明它会扫描 `org.frank` 包下的所有组件。
    * `@MapperScan("org.frank.infrastructure.mapper")` 注解指定了 MyBatis `Mapper` 接口的位置。

* **核心配置:**
    * 主要的配置文件位于 `frank-starter/src/main/resources/application.yml`。
    * **环境隔离:** 通过 `spring.profiles.active: dev` 激活 `application-dev.yml`，实现了开发、生产等不同环境的配置分离。
    * **关键配置项:**
        * `server.port`: 应用监听的端口。
        * `server.servlet.context-path`: 应用的访问根路径。
        * `mybatis-plus.*`: MyBatis-Plus 的相关配置，如 Mapper XML 文件的位置、主键策略等。
        * 数据库连接信息（如 `spring.datasource.*`）位于特定环境的配置文件中（如 `application-dev.yml`）。

## 模块文件地址

以下是每个模块内部核心包的规范和作用说明。

### `frank-starter`

* **`src/main/java/org/frank/starter`**:
    * **规范**: 存放 `main` 方法所在的启动类 `FrankStarterApplication`。
    * **作用**: 整个应用的入口，负责启动 Spring Boot 应用和自动配置。
* **`src/main/resources`**:
    * **规范**: 存放应用的静态资源和配置文件。
    * **作用**: 包含 `application.yml` (主配置), `application-dev.yml` (开发环境配置), `application-prod.yml` (
      生产环境配置) 以及日志配置文件 `log4j2.xml`。

### `frank-adapter`

* **`src/main/java/org/frank/adapter/controller`**:
    * **规范**: 存放所有对外提供 RESTful API 的 Controller 类。
    * **作用**: 作为HTTP请求的入口，接收参数，调用 `frank-app` 层的服务，并返回 `AjaxResult` 包装后的结果。
* **`src/main/java/org/frank/adapter/controller/exception`**:
    * **规范**: 存放全局异常处理器。
    * **作用**: 使用 `@RestControllerAdvice` 注解，捕获 Controller 层抛出的特定异常，并返回统一的错误响应。

### `frank-app`

* **`src/main/java/org/frank/app/service`**:
  * **规范**: 存放业务逻辑的接口，按功能模块分子包组织（如 `monitor`、`system` 等）。
  * **作用**: 定义了应用层需要向外（主要是 `adapter` 层）暴露的服务契约，是各层之间解耦的关键。

### `frank-client`

* **`src/main/java/org/frank/client/serviceImpl`**:
  * **规范**: 存放业务逻辑的具体实现类，按功能模块分子包组织，命名通常以 `ServiceImpl` 结尾。
  * **作用**: 实现 `frank-app` 中定义的业务接口，编排领域对象和网关，完成完整的业务流程。

### `frank-domain`

* **`src/main/java/org/frank/domain/entity`**:
    * **规范**: 存放领域实体（Entity）。
    * **作用**: 包含丰富的业务逻辑和状态，是领域模型的核心。例如 `SysUser`、`SysRole`、`SysMenu` 实体。
* **`src/main/java/org/frank/domain/gateway`**:
    * **规范**: 存放数据持久化的接口，即网关（Gateway）接口。
  * **作用**: 定义了领域层希望基础设施层如何持久化其状态的契约，继承MyBatis-Plus的IService接口。

### `frank-infrastructure`

* **`src/main/java/org/frank/infrastructure/gateway`**:
    * **规范**: 存放 Gateway 接口的具体实现类，命名以 `Impl` 结尾。
  * **作用**: 实现 `frank-domain` 中定义的持久化接口，继承MyBatis-Plus的ServiceImpl，将领域对象与数据库记录进行转换。
* **`src/main/java/org/frank/infrastructure/mapper`**:
    * **规范**: 存放 MyBatis-Plus 的 Mapper 接口。
  * **作用**: 继承MyBatis-Plus的BaseMapper接口，直接与数据库表进行映射，提供底层的、面向数据记录的 CRUD 操作。

### `frank-shared`

* **`src/main/java/org/frank/shared/sysLogin/req`**:
    * **规范**: 存放系统登录相关的请求数据传输对象 (Request DTO)。
    * **作用**: 用于封装从客户端（如前端）传递过来的请求数据。
* **`src/main/java/org/frank/shared/sysLogin/resp`**:
    * **规范**: 存放系统登录相关的响应数据传输对象 (Response DTO)。
    * **作用**: 用于封装需要返回给客户端的数据，避免直接暴露领域实体。
* **`src/main/java/org/frank/shared/userLogin/req`**:
    * **规范**: 存放用户登录相关的请求数据传输对象 (Request DTO)。
    * **作用**: 用于封装从客户端（如前端）传递过来的请求数据。
* **`src/main/java/org/frank/shared/userLogin/resp`**:
    * **规范**: 存放用户登录相关的响应数据传输对象 (Response DTO)。
    * **作用**: 用于封装需要返回给客户端的数据，避免直接暴露领域实体。

### `frank-common`

* **`src/main/java/org/frank/common/config`**:
    * **规范**: 存放应用全局的配置类。
    * **作用**: 例如 `MybatisPlusConfig` 用于配置MyBatis-Plus。
* **`src/main/java/org/frank/common/constant`**:
    * **规范**: 存放整个应用范围内的静态常量。
    * **作用**: 定义了如缓存键 (`CacheConstants`)、通用业务常量 (`Constants`)、HTTP状态码 (`HttpStatus`) 和定时任务常量 (
      `ScheduleConstants`)，便于统一管理和复用。
* **`src/main/java/org/frank/common/core/domain`**:
    * **规范**: 存放核心的、通用的数据模型。
    * **作用**: `AjaxResult` 是标准的API响应结构；`BaseEntity` 是所有数据库实体类的基类，包含通用字段（如创建/更新时间）；
      `LoginUser` 封装了登录用户的核心信息。
* **`src/main/java/org/frank/common/core/redis`**:
    * **规范**: 存放Redis相关的工具类。
    * **作用**: `RedisCache` 封装了 `RedisTemplate`，提供了更便捷的API来操作缓存，简化了对象的存取。
* **`src/main/java/org/frank/common/enums`**:
    * **规范**: 存放全局通用的枚举类。
    * **作用**: 例如 `ResultCodeEnum` 定义了统一的API返回码和消息，使错误处理更规范。
* **`src/main/java/org/frank/common/handler`**:
    * **规范**: 存放一些框架级别的处理器。
    * **作用**: `MyMetaObjectHandler` 实现了MyBatis-Plus的字段自动填充逻辑，在插入或更新时自动设置 `createBy`,
      `createTime` 等字段。
* **`src/main/java/org/frank/common/util`**:
    * **规范**: 存放全局通用的工具类。
    * **作用**: `ServletUtils` 简化了对HTTP请求和响应的操作；`TokenService` 负责Token的生成、解析和校验，是身份认证的核心；
      `AesUtils`、`Md5Utils`、`Base64Utils` 提供数据加密和签名功能。

## 模块依赖关系图

此图表展示了项目各个模块之间的依赖关系。箭头从依赖方指向被依赖方 (A --> B, 表示 A 依赖 B)，清晰地揭示了项目的分层结构和依赖规则。

```mermaid
graph TD
    subgraph "Entry Point"
        starter("frank-starter")
    end

    subgraph "Presentation Layer"
        adapter("frank-adapter")
    end

    subgraph "Application Layer"
        app("frank-app")
        client("frank-client")
    end

    subgraph "Domain Layer"
        domain("frank-domain")
    end

    subgraph "Infrastructure Layer"
        infra("frank-infrastructure")
    end

    subgraph "Shared/Common Libraries"
        shared("frank-shared")
        common("frank-common")
    end

    %% Module Dependencies
    starter --> adapter
    starter --> app
    starter --> infra

    adapter --> client
    adapter --> shared
    adapter --> common

    app --> client
    app --> domain
    app --> shared
    app --> common

    infra --> domain
    infra --> common
    
    client --> shared
    client --> common

    domain --> common
```

## 主要业务实体

项目包含以下核心业务实体：

* **SysUser**: 用户实体，包含用户基本信息
* **SysRole**: 角色实体，用于权限管理
* **SysMenu**: 菜单实体，用于系统菜单和权限控制
* **SysUserRole**: 用户角色关联实体
* **SysRoleMenu**: 角色菜单关联实体

## 开发指南

### 运行项目

1. 确保安装了 Java 17 和 Maven
2. 配置数据库连接信息（在 `application-dev.yml` 或 `application-prod.yml` 中）
3. 运行 `org.frank.starter.FrankStarterApplication` 的 main 方法

### 数据库脚本

项目提供了 MySQL 和 PostgreSQL 的数据库脚本：

* `sql/frank-web-mysql.sql`: MySQL 数据库脚本
* `sql/frank-web-pg.sql`: PostgreSQL 数据库脚本

### 日志配置

项目使用 Log4j2 进行日志管理，配置文件位于 `frank-starter/src/main/resources/log4j2.xml`。

日志文件输出位置：

* `logs/sys-info.log`: 信息日志
* `logs/sys-error.log`: 错误日志

### API 文档

## 开发指南

### 数据库设置

项目提供了MySQL和PostgreSQL的数据库脚本：

* `sql/frank-web-mysql.sql`: MySQL数据库脚本
* `sql/frank-web-pg.sql`: PostgreSQL数据库脚本

开发环境使用PostgreSQL，配置文件位于`frank-starter/src/main/resources/application-dev.yml`

### 日志配置

项目使用Log4j2进行日志管理，配置文件位于`frank-starter/src/main/resources/log4j2.xml`。

日志文件输出位置：

* `logs/sys-info.log`: 信息日志
* `logs/sys-error.log`: 错误日志

### API文档

项目使用Knife4j生成API文档，启动后可通过以下地址访问：

* API文档: `http://localhost:9040/frankweb/doc.html`
* Druid监控: `http://localhost:9040/frankweb/druid/` (用户名/密码: admin/admin)

### 核心开发注意事项

1. **架构遵循DDD模式**: 新功能开发应严格按照现有模块划分进行
2. **模块化组织**: 按功能模块分子包组织（如 `monitor`、`system`、`auth` 等）
3. **数据库操作**: 使用MyBatis-Plus的IService和BaseMapper，避免直接写SQL
4. **API响应格式**: 统一使用`AjaxResult`包装响应
5. **异常处理**: 在`frank-adapter/controller/exception`中添加全局异常处理器
6. **DTO转换**: 在`frank-shared`中定义请求和响应DTO，对应实体类`req`请求参数，统一继承`BaseReq`
7. **实体创建**: 领域实体放在`frank-domain/entity`，继承`BaseEntity`
8. **依赖注入**: 使用`@Resource`注解进行依赖注入，保持一致性

### 新增模块开发规范

**标准文件结构（以monitor模块为例）：**

```
frank-adapter/src/main/java/org/frank/adapter/controller/monitor/
├── SysLogOperController.java                    # 控制器

frank-app/src/main/java/org/frank/app/service/monitor/
├── SysLogOperService.java                        # 服务接口

frank-client/src/main/java/org/frank/client/serviceImpl/monitor/
├── SysLogOperServiceImpl.java                    # 服务实现

frank-domain/src/main/java/org/frank/domain/
├── entity/SysLogOper.java                       # 领域实体
└── gateway/ISysLogOperGateway.java              # Gateway接口

frank-infrastructure/src/main/java/org/frank/infrastructure/
├── gateway/SysLogOperGatewayImpl.java            # Gateway实现
└── mapper/SysLogOperMapper.java                  # Mapper接口

frank-shared/src/main/java/org/frank/shared/sysLogOper/
├── req/SysLogOperPageQueryReq.java               # 请求DTO
└── resp/SysLogOperResp.java                      # 响应DTO
```

**架构层次关系：**

- **Adapter层** → 调用 **App层** 接口
- **App层** → 定义业务接口契约
- **Client层** → 实现App层接口，调用Domain层
- **Domain层** → 定义实体和Gateway接口
- **Infrastructure层** → 实现Gateway接口和数据访问

**关键依赖关系：**

- Gateway接口继承：`extends IService<Entity>`
- Gateway实现继承：`extends ServiceImpl<Mapper, Entity>`
- Mapper接口继承：`extends BaseMapper<Entity>`

---

## 详细开发规范

### 代码组织结构

**模块依赖原则：**

```
Adapter → Client → Domain ← Infrastructure
    ↓        ↓        ↑         ↓
  Shared ← Shared ← Common ← Common
```

**禁止的依赖关系：**

- Domain层不能依赖任何其他层
- Infrastructure层不能依赖Adapter层和Client层
- 所有层都不能直接依赖具体实现，只能依赖接口

### 命名规范详细说明

**包命名规范：**

```
org.frank.{module}.{layer}.{function}
- module: starter, adapter, app, client, domain, infrastructure, shared, common
- layer: controller, service, entity, gateway, mapper, util, config, constant
- function: sysUser, sysRole, sysMenu, sysLogin等
```

**类命名规范：**

```
Controller类: {Entity}Controller (如: SysLogOperController)
Service接口: {Entity}Service (如: SysLogOperService)
Service实现: {Entity}ServiceImpl (如: SysLogOperServiceImpl)
Gateway接口: I{Entity}Gateway (如: ISysLogOperGateway)
Gateway实现: {Entity}GatewayImpl (如: SysLogOperGatewayImpl)
Mapper接口: {Entity}Mapper (如: SysLogOperMapper)
Entity实体: {Entity} (如: SysLogOper)
DTO请求类: {Function}Req (如: SysLogOperPageQueryReq)
DTO响应类: {Function}Resp (如: SysLogOperResp)
```

**方法命名规范：**

```
查询方法: select{Entity}List, select{Entity}ById, select{Entity}By{Condition}
新增方法: insert{Entity}, add{Entity}, create{Entity}
修改方法: update{Entity}, modify{Entity}, change{Entity}Status
删除方法: delete{Entity}ById, delete{Entity}ByIds, remove{Entity}
业务方法: {businessAction} (如: resetPassword, changeStatus)
```

### 注解使用规范

**实体类注解：**

```java
@Data                    // 自动生成getter/setter/toString/equals/hashCode
@Builder                // 支持建造者模式
@NoArgsConstructor       // 无参构造器
@AllArgsConstructor      // 全参构造器
@TableName("sys_user")  // MyBatis-Plus表名映射
@ApiModel(description = "用户实体") // API文档模型
```

**Controller类注解：**

```java
@RestController         // RESTful控制器
@RequestMapping("/sys/user") // 请求路径前缀
@Api(tags = "用户管理")   // API文档标签
@Slf4j                  // 日志支持
```

**Service类注解：**

```java
@Service                // Spring服务层组件
@Transactional          // 事务管理
@Slf4j                  // 日志支持
```

**方法注解：**

```java
@GetMapping("/{id}")    // GET请求
@PostMapping            // POST请求
@PutMapping("/{id}")    // PUT请求
@DeleteMapping("/{id}") // DELETE请求
@ApiOperation("获取用户详情") // API文档
@ApiParam("用户ID")     // API参数文档
```

### 数据库设计规范

**表命名规范：**

```
业务表: sys_{entity_name} (如: sys_user, sys_role)
关联表: sys_{entity1}_rel_{entity2} (如: sys_user_rel_role)
字典表: sys_dict_type, sys_dict_data
```

**字段命名规范：**

```
主键: id
逻辑删除: del_flag
创建时间: create_time
更新时间: update_time
创建者: create_by
更新者: update_by
业务字段: 使用小写+下划线命名 (如: user_name, phone_number)
```

**字段类型规范：**

```
主键: BIGINT (使用雪花算法生成)
字符串: VARCHAR(长度根据业务需要)
数字: BIGINT/INT/DECIMAL
时间: DATETIME 或 TIMESTAMP
状态: TINYINT (使用枚举值)
逻辑删除: TINYINT (1-正常, 0-删除)
```

---

## 标准代码模板

### Controller模板

```java
/**
 * 系统用户控制器
 *
 * @author Frank
 * @since 2025-11-10
 */
@RestController
@RequestMapping("/sys/user")
@Api(tags = "用户管理")
@Slf4j
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 分页查询用户列表
     */
    @PostMapping("/page")
    @ApiOperation("分页查询用户列表")
    public AjaxResult<PageResult<SysUserResp>> pageQuery(@RequestBody SysUserPageQueryReq req) {
        PageResult<SysUserResp> result = sysUserService.selectUserPage(req);
        return AjaxResult.success(result);
    }

    /**
     * 根据ID获取用户详情
     */
    @GetMapping("/{id}")
    @ApiOperation("获取用户详情")
    @ApiParam("用户ID")
    public AjaxResult<SysUserResp> getUserById(@PathVariable Long id) {
        SysUserResp user = sysUserService.selectUserById(id);
        return AjaxResult.success(user);
    }

    /**
     * 新增用户
     */
    @PostMapping
    @ApiOperation("新增用户")
    public AjaxResult<Void> addUser(@RequestBody @Valid SysUserAddReq req) {
        sysUserService.insertUser(req);
        return AjaxResult.success();
    }

    /**
     * 修改用户
     */
    @PutMapping("/{id}")
    @ApiOperation("修改用户")
    @ApiParam("用户ID")
    public AjaxResult<Void> updateUser(@PathVariable Long id, @RequestBody @Valid SysUserUpdateReq req) {
        req.setId(id);
        sysUserService.updateUser(req);
        return AjaxResult.success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{ids}")
    @ApiOperation("删除用户")
    @ApiParam("用户ID，多个用逗号分隔")
    public AjaxResult<Void> deleteUser(@PathVariable String ids) {
        sysUserService.deleteUserByIds(ids);
        return AjaxResult.success();
    }

    /**
     * 重置密码
     */
    @PutMapping("/{id}/reset-password")
    @ApiOperation("重置密码")
    @ApiParam("用户ID")
    public AjaxResult<Void> resetPassword(@PathVariable Long id, @RequestBody @Valid ResetPasswordReq req) {
        req.setUserId(id);
        sysUserService.resetPassword(req);
        return AjaxResult.success();
    }

    /**
     * 修改用户状态
     */
    @PutMapping("/{id}/status")
    @ApiOperation("修改用户状态")
    @ApiParam("用户ID")
    public AjaxResult<Void> changeStatus(@PathVariable Long id, @RequestBody @Valid ChangeStatusReq req) {
        req.setUserId(id);
        sysUserService.changeUserStatus(req);
        return AjaxResult.success();
    }
}
```

### Service接口模板

```java
/**
 * 操作日志服务接口
 *
 * @author Frank
 * @since 2025-11-16
 */
public interface SysLogOperService {

    /**
     * 分页查询操作日志
     *
     * @param req 查询条件
     * @return 分页结果
     */
    PageResult<SysLogOperResp> selectLogOperPage(SysLogOperPageQueryReq req);

    /**
     * 根据ID查询操作日志详情
     *
     * @param operId 日志ID
     * @return 操作日志详情
     */
    SysLogOperResp selectLogOperById(Long operId);

    /**
     * 插入操作日志
     *
     * @param sysLogOper 操作日志
     */
    void insertLogOper(SysLogOper sysLogOper);

    /**
     * 批量删除操作日志
     *
     * @param operIds 日志ID数组
     */
    void deleteLogOperByIds(String operIds);

    /**
     * 清空操作日志
     */
    void cleanLogOper();
}
```

### Service实现模板

```java
/**
 * 操作日志服务实现类
 *
 * @author Frank
 * @since 2025-11-16
 */
@Slf4j
@Service
public class SysLogOperServiceImpl implements SysLogOperService {

  @Resource
  private ISysLogOperGateway gateway;

    @Override
    public PageResult<SysLogOperResp> selectLogOperPage(SysLogOperPageQueryReq req) {
        // 构建查询条件
      LambdaQueryWrapper<SysLogOper> queryWrapper = new LambdaQueryWrapper<>();
      queryWrapper.like(StringUtil.isNotEmpty(req.getTitle()),
                      SysLogOper::getTitle, req.getTitle())
              .eq(req.getBusinessType() != null,
                      SysLogOper::getBusinessType, req.getBusinessType())
                   .eq(req.getStatus() != null,
                           SysLogOper::getStatus, req.getStatus())
                   .between(req.getBeginTime() != null && req.getEndTime() != null,
                           SysLogOper::getOperTime, req.getBeginTime(), req.getEndTime())
              .eq(SysLogOper::getDelFlag, Constants.NORMAL)
              .orderByDesc(SysLogOper::getOperTime);

        // 执行分页查询
      IPage<SysLogOper> page = new Page<>(req.getPageNum(), req.getPageSize());
      IPage<SysLogOper> logPage = gateway.page(page, queryWrapper);

        // 转换为响应对象
      List<SysLogOperResp> respList = logPage.getRecords().stream()
                .map(this::convertToResp)
                .collect(Collectors.toList());

      return PageResult.of(respList, logPage.getTotal());
    }

    @Override
    @Transactional
    public void insertUser(SysUserAddReq req) {
        // 参数校验
        checkUserAddParam(req);

        // 检查用户名、手机号、邮箱唯一性
        if (!checkUserNameUnique(req.getUserName(), null)) {
            throw new BusinessException("用户名已存在");
        }
        if (!checkPhoneUnique(req.getPhoneNumber(), null)) {
            throw new BusinessException("手机号已存在");
        }
        if (!checkEmailUnique(req.getEmail(), null)) {
            throw new BusinessException("邮箱已存在");
        }

        // 构建用户对象
        SysUser user = buildUserFromAddReq(req);

        // 设置默认密码
        user.setPassword(passwordEncoder.encode(UserConstants.DEFAULT_PASSWORD));

        // 保存用户
        sysUserGateway.insert(user);

        // 保存用户角色关系
        if (CollectionUtil.isNotEmpty(req.getRoleIds())) {
            userRoleGateway.insertUserRoleRelations(user.getId(), req.getRoleIds());
        }

        log.info("新增用户成功，用户名: {}", user.getUserName());
    }

    @Override
    @Transactional
    public void updateUser(SysUserUpdateReq req) {
        // 参数校验
        if (req.getId() == null) {
            throw new BusinessException("用户ID不能为空");
        }

        // 查询用户信息
        SysUser existUser = sysUserGateway.selectById(req.getId());
        if (existUser == null || Constants.DEL_FLAG_DELETE.equals(existUser.getDelFlag())) {
            throw new BusinessException("用户不存在");
        }

        // 检查唯一性（排除自己）
        if (!checkUserNameUnique(req.getUserName(), req.getId())) {
            throw new BusinessException("用户名已存在");
        }
        if (!checkPhoneUnique(req.getPhoneNumber(), req.getId())) {
            throw new BusinessException("手机号已存在");
        }
        if (!checkEmailUnique(req.getEmail(), req.getId())) {
            throw new BusinessException("邮箱已存在");
        }

        // 构建更新对象
        SysUser updateUser = buildUserFromUpdateReq(req);

        // 更新用户
        sysUserGateway.updateById(updateUser);

        // 更新用户角色关系
        userRoleGateway.updateUserRoleRelations(req.getId(), req.getRoleIds());

        // 清除用户缓存
        clearUserCache(req.getId());

        log.info("修改用户成功，用户ID: {}", req.getId());
    }

    @Override
    @Transactional
    public void deleteUserByIds(String userIds) {
        if (StringUtil.isEmpty(userIds)) {
            throw new BusinessException("用户ID不能为空");
        }

        List<Long> userIdList = Arrays.stream(userIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        // 检查是否包含超级管理员
        for (Long userId : userIdList) {
            SysUser user = sysUserGateway.selectById(userId);
            if (user != null && UserConstants.SUPER_ADMIN_ID.equals(userId)) {
                throw new BusinessException("超级管理员不能删除");
            }
        }

        // 批量逻辑删除
        sysUserGateway.deleteBatchIds(userIdList);

        // 删除用户角色关系
        userRoleGateway.deleteUserRoleRelations(userIds);

        log.info("批量删除用户成功，用户IDs: {}", userIds);
    }

    @Override
    public void resetPassword(ResetPasswordReq req) {
        // 参数校验
        if (req.getUserId() == null || StringUtil.isEmpty(req.getPassword())) {
            throw new BusinessException("参数不完整");
        }

        // 查询用户
        SysUser user = sysUserGateway.selectById(req.getUserId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 加密新密码
        String encryptPassword = passwordEncoder.encode(req.getPassword());

        // 更新密码
        SysUser updateUser = new SysUser();
        updateUser.setId(req.getUserId());
        updateUser.setPassword(encryptPassword);
        updateUser.setUpdateBy(SecurityUtils.getUsername());
        sysUserGateway.updateById(updateUser);

        // 强制用户下线
        clearUserLoginCache(req.getUserId());

        log.info("重置密码成功，用户ID: {}", req.getUserId());
    }

    @Override
    public void changeUserStatus(ChangeStatusReq req) {
        // 参数校验
        if (req.getUserId() == null || req.getStatus() == null) {
            throw new BusinessException("参数不完整");
        }

        // 检查是否是超级管理员
        if (UserConstants.SUPER_ADMIN_ID.equals(req.getUserId())) {
            throw new BusinessException("超级管理员状态不能修改");
        }

        // 查询用户
        SysUser user = sysUserGateway.selectById(req.getUserId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 更新状态
        SysUser updateUser = new SysUser();
        updateUser.setId(req.getUserId());
        updateUser.setStatus(req.getStatus());
        updateUser.setUpdateBy(SecurityUtils.getUsername());
        sysUserGateway.updateById(updateUser);

        // 如果是禁用状态，强制用户下线
        if (Constants.STATUS_DISABLE.equals(req.getStatus())) {
            clearUserLoginCache(req.getUserId());
        }

        log.info("修改用户状态成功，用户ID: {}, 状态: {}", req.getUserId(), req.getStatus());
    }

    // ==================== 私有方法 ====================

    /**
     * 转换为响应对象
     */
    private SysUserResp convertToResp(SysUser user) {
        if (user == null) {
            return null;
        }

        SysUserResp resp = new SysUserResp();
        BeanUtils.copyProperties(user, resp);

        // 查询用户角色信息
        List<Long> roleIds = userRoleGateway.selectRoleIdsByUserId(user.getId());
        resp.setRoleIds(roleIds);

        return resp;
    }

    /**
     * 校验用户新增参数
     */
    private void checkUserAddParam(SysUserAddReq req) {
        if (StringUtil.isEmpty(req.getUserName())) {
            throw new BusinessException("用户名不能为空");
        }
        if (StringUtil.isEmpty(req.getPhoneNumber())) {
            throw new BusinessException("手机号不能为空");
        }
        if (!RegexUtil.matchPhone(req.getPhoneNumber())) {
            throw new BusinessException("手机号格式不正确");
        }
        if (StringUtil.isNotEmpty(req.getEmail()) && !RegexUtil.matchEmail(req.getEmail())) {
            throw new BusinessException("邮箱格式不正确");
        }
    }

    /**
     * 构建用户对象（新增用）
     */
    private SysUser buildUserFromAddReq(SysUserAddReq req) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(req, user);
        user.setCreateBy(SecurityUtils.getUsername());
        user.setCreateTime(LocalDateTime.now());
        user.setDelFlag(Constants.NORMAL);
        user.setStatus(Constants.STATUS_NORMAL);
        return user;
    }

    /**
     * 构建用户对象（更新用）
     */
    private SysUser buildUserFromUpdateReq(SysUserUpdateReq req) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(req, user);
        user.setUpdateBy(SecurityUtils.getUsername());
        user.setUpdateTime(LocalDateTime.now());
        return user;
    }

    /**
     * 清除用户缓存
     */
    private void clearUserCache(Long userId) {
        String cacheKey = CacheConstants.USER_KEY + userId;
        redisCache.deleteObject(cacheKey);
    }

    /**
     * 清除用户登录缓存
     */
    private void clearUserLoginCache(Long userId) {
        // 清除用户信息缓存
        String userKey = CacheConstants.USER_KEY + userId;
        redisCache.deleteObject(userKey);

        // 清除用户Token（这里可以根据实际Token管理机制实现）
        // tokenService.clearUserToken(userId);
    }

    // ==================== 唯一性检查方法 ====================

    @Override
    public boolean checkUserNameUnique(String userName, Long userId) {
        Long existUserId = sysUserGateway.selectUserIdByUserName(userName);
        return existUserId == null || existUserId.equals(userId);
    }

    @Override
    public boolean checkPhoneUnique(String phoneNumber, Long userId) {
        if (StringUtil.isEmpty(phoneNumber)) {
            return true;
        }
        Long existUserId = sysUserGateway.selectUserIdByPhone(phoneNumber);
        return existUserId == null || existUserId.equals(userId);
    }

    @Override
    public boolean checkEmailUnique(String email, Long userId) {
        if (StringUtil.isEmpty(email)) {
            return true;
        }
        Long existUserId = sysUserGateway.selectUserIdByEmail(email);
        return existUserId == null || existUserId.equals(userId);
    }
}
```

### Entity实体模板

```java
/**
 * 系统用户实体
 *
 * @author Frank
 * @since 2025-11-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
@ApiModel(description = "用户实体")
public class SysUser extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "用户ID")
    private Long id;

    @TableField("user_name")
    @ApiModelProperty(value = "用户账号")
    private String userName;

    @TableField("nick_name")
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @TableField("email")
    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @TableField("phone_number")
    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @TableField("sex")
    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    private String sex;

    @TableField("avatar")
    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @TableField("password")
    @ApiModelProperty(value = "密码")
    private String password;

    @TableField("status")
    @ApiModelProperty(value = "帐号状态（0正常 1停用）")
    private String status;

    @TableField("login_ip")
    @ApiModelProperty(value = "最后登录IP")
    private String loginIp;

    @TableField("login_date")
    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime loginDate;

    @TableField("dept_id")
    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @Override
    public String toString() {
        return "SysUser{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", sex='" + sex + '\'' +
                ", avatar='" + avatar + '\'' +
                ", status='" + status + '\'' +
                ", loginIp='" + loginIp + '\'' +
                ", loginDate=" + loginDate +
                ", deptId=" + deptId +
                '}';
    }
}
```

### DTO模板

**请求DTO基类：**

```java
/**
 * 基础请求对象
 *
 * @author Frank
 * @since 2025-11-10
 */
@Data
@ApiModel(description = "基础请求对象")
public class BaseReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页码")
    private Integer pageNum;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;
}
```

**分页查询请求DTO：**

```java
/**
 * 用户分页查询请求
 *
 * @author Frank
 * @since 2025-11-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户分页查询请求")
public class SysUserPageQueryReq extends BaseReq {

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;
}
```

**新增请求DTO：**

```java
/**
 * 新增用户请求
 *
 * @author Frank
 * @since 2025-11-10
 */
@Data
@ApiModel(description = "新增用户请求")
public class SysUserAddReq implements Serializable {

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "手机号码", required = true)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phoneNumber;

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    private String sex;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "角色ID列表")
    private List<Long> roleIds;

    @ApiModelProperty(value = "状态")
    private String status;
}
```

**响应DTO：**

```java
/**
 * 用户响应
 *
 * @author Frank
 * @since 2025-11-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户响应")
public class SysUserResp implements Serializable {

    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    private String sex;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "角色ID列表")
    private List<Long> roleIds;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
```

---

## AI辅助开发具体指导

### 智能代码生成命令

**使用方法：**

```
/generate controller SysUser      # 生成用户管理控制器
/generate service SysUser         # 生成用户服务接口和实现
/generate entity SysUser          # 生成用户实体类
/generate dto SysUser             # 生成用户相关DTO
/generate all SysUser             # 生成用户相关的所有代码
```

**智能分析命令：**

```
/analyze project                  # 分析整个项目架构
/analyze module frank-adapter     # 分析适配器层代码
/analyze class SysUserController  # 分析指定类的代码质量
/refactor SysUserServiceImpl     # 提供重构建议
/optimize database                # 优化数据库设计
```

**执行注意事项：**

- 由于当前环境不支持自定义斜杠命令，实际使用时请直接描述需求
- 例如：请生成用户管理模块的控制器代码
- 例如：请分析整个项目架构并提供优化建议

### 新功能开发流程

**Step 1: 需求分析**

```
AI理解用户需求 → 分析业务场景 → 确定功能边界 → 设计数据模型
```

**Step 2: 架构设计**

```
确定涉及层次 → 设计接口契约 → 定义数据流向 → 规划异常处理
```

**Step 3: 代码生成**

```
生成Entity实体 → 生成DTO对象 → 生成Gateway接口 → 生成Service服务 → 生成Controller控制器
```

**Step 4: 质量检查**

```
代码规范检查 → 业务逻辑验证 → 性能安全审查 → 测试用例生成
```

### 常见问题解决方案

**数据库连接问题：**

- 检查application-dev.yml中的数据库配置
- 确认PostgreSQL服务是否启动
- 验证数据库用户权限

**跨域问题：**

- 检查WebMvcConfig配置
- 确认CorsProperties配置
- 前端请求头设置

**认证授权问题：**

- 检查JWT Token配置
- 验证TokenInterceptor拦截器
- 确认Redis连接状态

**MyBatis-Plus问题：**

- 检查@MapperScan配置
- 确认实体类@TableName注解
- 验证LambdaQueryWrapper使用

### 性能优化建议

**数据库层面：**

- 合理使用索引，避免全表扫描
- 使用分页查询，避免一次性加载大量数据
- 优化SQL语句，避免N+1查询问题
- 使用连接池，合理配置连接数

**缓存层面：**

- 对热点数据使用Redis缓存
- 设置合理的缓存过期时间
- 使用缓存穿透和雪崩保护机制

**代码层面：**

- 避免在循环中进行数据库操作
- 合理使用事务，避免大事务
- 使用异步处理提升响应速度
- 及时释放资源，避免内存泄露

---

## 详细启动指南

### 环境准备

**必需软件版本：**

- Java 17+ (推荐使用 OpenJDK 17)
- Maven 3.8.0+
- PostgreSQL 12+ 或 MySQL 8.0+
- Redis 6.0+

**数据库配置：**

```sql
-- PostgreSQL 建库语句
CREATE DATABASE frankweb
    WITH ENCODING='UTF8'
    CONNECTION LIMIT=-1;

-- MySQL 建库语句
CREATE DATABASE frankweb DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**数据库初始化：**

```bash
# PostgreSQL
psql -U username -d frankweb -f sql/frank-web-pg.sql

# MySQL
mysql -u username -p frankweb < sql/frank-web-mysql.sql
```

### 配置文件设置

**开发环境配置 (application-dev.yml)：**

```yaml
# 数据库配置
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/frankweb
    username: postgres
    password: your_password

  # Redis配置
  redis:
    host: localhost
    port: 6379
    password:
    database: 0
```

**生产环境注意事项：**

- 使用生产数据库专用账号
- 配置Redis密码认证
- 启用SQL日志监控
- 配置应用性能监控

### 项目构建与启动

**命令行启动：**

```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run -pl frank-starter

# 或使用java命令
mvn clean package
java -jar frank-starter/target/frank-starter-1.0.0.jar
```

**IDE启动：**

1. 导入Maven项目
2. 等待依赖下载完成
3. 运行FrankStarterApplication.main()方法
4. 访问 http://localhost:9040/frankweb/doc.html

**访问验证地址：**

- API文档: http://localhost:9040/frankweb/doc.html
- Druid监控: http://localhost:9040/frankweb/druid/
- 健康检查: http://localhost:9040/frankweb/actuator/health
