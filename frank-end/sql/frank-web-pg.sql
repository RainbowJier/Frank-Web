-- ----------------------------
-- 用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user
(
    user_id      bigint generated always as identity,
    dept_id      bigint       default null,
    user_name    varchar(30) not null,
    nick_name    varchar(30) not null,
    user_type    varchar(2)   default '00',
    email        varchar(50)  default '',
    phone_number varchar(11)  default '',
    sex          int          default null,
    avatar       varchar(100) default '',
    password     varchar(100) default '',
    status       int          default 1,
    del_flag     int          default 1,
    create_by    varchar(64)  default '',
    create_time  timestamp    default now(),
    update_by    varchar(64)  default '',
    update_time  timestamp,
    remark       varchar(500) default null,
    primary key (user_id)
);

comment on table sys_user is '用户信息表';
comment on column sys_user.user_id is '用户ID';
comment on column sys_user.dept_id is '部门ID';
comment on column sys_user.user_name is '用户账号';
comment on column sys_user.nick_name is '用户昵称';
comment on column sys_user.user_type is '用户类型（00系统用户）';
comment on column sys_user.email is '用户邮箱';
comment on column sys_user.phone_number is '手机号码';
comment on column sys_user.sex is '用户性别（1男 0女 2未知）';
comment on column sys_user.avatar is '头像地址';
comment on column sys_user.password is '密码';
comment on column sys_user.status is '账号状态（1正常 -1停用）';
comment on column sys_user.del_flag is '删除标志（1代表存在 -1代表删除）';
comment on column sys_user.create_by is '创建者';
comment on column sys_user.create_time is '创建时间';
comment on column sys_user.update_by is '更新者';
comment on column sys_user.update_time is '更新时间';
comment on column sys_user.remark is '备注';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into sys_user overriding system value
values (1, 103, 'admin', 'administrator', '00', 'frank@163.com', '', 1, '',
        'euLUpj0cPhoYeh/Yn0ce9Q==', 1, 1, now(), now(),
        'admin', now(), '管理员');


-- ----------------------------
-- 用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists sys_user_role;
create table sys_user_role
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id)
);

comment on table sys_user_role is '用户和角色关联表';
comment on column sys_user_role.user_id is '用户ID';
comment on column sys_user_role.role_id is '角色ID';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
insert into sys_user_role
values ('1', '1');
insert into sys_user_role
values ('2', '2');


-- ----------------------------
-- 角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role
(
    role_id             bigint generated always as identity primary key,
    role_name           varchar(30)  not null,
    role_key            varchar(100) not null,
    role_sort           integer      not null,
    data_scope          char(1)      default '1',
    menu_check_strictly smallint     default 1,
    dept_check_strictly smallint     default 1,
    status              int          default 1,
    del_flag            int          default 1,
    create_by           varchar(64)  default '',
    create_time         timestamp    default now(),
    update_by           varchar(64)  default '',
    update_time         timestamp,
    remark              varchar(500) default null
);

comment on table sys_role is '角色信息表';
comment on column sys_role.role_id is '角色ID';
comment on column sys_role.role_name is '角色名称';
comment on column sys_role.role_key is '角色权限字符串';
comment on column sys_role.role_sort is '显示顺序';
comment on column sys_role.data_scope is '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）';
comment on column sys_role.menu_check_strictly is '菜单树选择项是否关联显示';
comment on column sys_role.dept_check_strictly is '部门树选择项是否关联显示';
comment on column sys_role.status is '角色状态（1正常 -1停用）';
comment on column sys_user.del_flag is '删除标志（1代表存在 -1代表删除）';
comment on column sys_role.create_by is '创建者';
comment on column sys_role.create_time is '创建时间';
comment on column sys_role.update_by is '更新者';
comment on column sys_role.update_time is '更新时间';
comment on column sys_role.remark is '备注';

-- 初始化-角色信息表数据
insert into sys_role(role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status,
                     del_flag, create_by, create_time, update_by, update_time, remark)
values ('超级管理员', 'admin', 1, '1', 1, 1, 1, 1, 'admin', now(), '', null, '超级管理员');
insert into sys_role(role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status,
                     del_flag, create_by, create_time, update_by, update_time, remark)
values ('普通角色', 'common', 2, '2', 1, 1, 1, 1, 'admin', now(), '', null, '普通角色');



-- ----------------------------
-- 角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists sys_role_menu;
create table sys_role_menu
(
    role_id bigint not null,
    menu_id bigint not null,
    primary key (role_id, menu_id)
);

comment on table sys_role_menu is '角色和菜单关联表';
comment on column sys_role_menu.role_id is '角色ID';
comment on column sys_role_menu.menu_id is '菜单ID';

-- ----------------------------
-- 初始化-角色和菜单关联表数据
-- ----------------------------
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES ('2', '1'),
       ('2', '2'),
       ('2', '3'),
       ('2', '4'),
       ('2', '100'),
       ('2', '101'),
       ('2', '102'),
       ('2', '103'),
       ('2', '104'),
       ('2', '105'),
       ('2', '106'),
       ('2', '107'),
       ('2', '108'),
       ('2', '109'),
       ('2', '110'),
       ('2', '111'),
       ('2', '112'),
       ('2', '113'),
       ('2', '114'),
       ('2', '115'),
       ('2', '116'),
       ('2', '117'),
       ('2', '500'),
       ('2', '501'),
       ('2', '1000'),
       ('2', '1001'),
       ('2', '1002'),
       ('2', '1003'),
       ('2', '1004'),
       ('2', '1005'),
       ('2', '1006'),
       ('2', '1007'),
       ('2', '1008'),
       ('2', '1009'),
       ('2', '1010'),
       ('2', '1011'),
       ('2', '1012'),
       ('2', '1013'),
       ('2', '1014'),
       ('2', '1015'),
       ('2', '1016'),
       ('2', '1017'),
       ('2', '1018'),
       ('2', '1019'),
       ('2', '1020'),
       ('2', '1021'),
       ('2', '1022'),
       ('2', '1023'),
       ('2', '1024'),
       ('2', '1025'),
       ('2', '1026'),
       ('2', '1027'),
       ('2', '1028'),
       ('2', '1029'),
       ('2', '1030'),
       ('2', '1031'),
       ('2', '1032'),
       ('2', '1033'),
       ('2', '1034'),
       ('2', '1035'),
       ('2', '1036'),
       ('2', '1037'),
       ('2', '1038'),
       ('2', '1039'),
       ('2', '1040'),
       ('2', '1041'),
       ('2', '1042'),
       ('2', '1043'),
       ('2', '1044'),
       ('2', '1045'),
       ('2', '1046'),
       ('2', '1047'),
       ('2', '1048'),
       ('2', '1049'),
       ('2', '1050'),
       ('2', '1051'),
       ('2', '1052'),
       ('2', '1053'),
       ('2', '1054'),
       ('2', '1055'),
       ('2', '1056'),
       ('2', '1057'),
       ('2', '1058'),
       ('2', '1059'),
       ('2', '1060');

-- ----------------------------
-- 2、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu
(
    menu_id     bigserial   not null primary key,
    menu_name   varchar(50) not null,
    parent_id   bigint       default 0,
    order_num   integer      default 0,
    path        varchar(200) default '',
    component   varchar(255) default null,
    query       varchar(255) default null,
    route_name  varchar(50)  default '',
    is_frame    integer      default 1,
    is_cache    integer      default 0,
    menu_type   char(1)      default '',
    visible     int          default 1,
    status      int          default 1,
    perms       varchar(100) default null,
    icon        varchar(100) default '#',
    create_by   varchar(64)  default '',
    create_time timestamp    default now(),
    update_by   varchar(64)  default '',
    update_time timestamp,
    remark      varchar(500) default ''
);

COMMENT ON TABLE sys_menu IS '菜单权限表';
COMMENT ON COLUMN sys_menu.menu_id IS '菜单ID';
COMMENT ON COLUMN sys_menu.menu_name IS '菜单名称';
COMMENT ON COLUMN sys_menu.parent_id IS '父菜单ID';
COMMENT ON COLUMN sys_menu.order_num IS '显示顺序';
COMMENT ON COLUMN sys_menu.path IS '路由地址';
COMMENT ON COLUMN sys_menu.component IS '组件路径';
COMMENT ON COLUMN sys_menu.query IS '路由参数';
COMMENT ON COLUMN sys_menu.route_name IS '路由名称';
COMMENT ON COLUMN sys_menu.is_frame IS '是否为外链（0是 1否）';
COMMENT ON COLUMN sys_menu.is_cache IS '是否缓存（0缓存 1不缓存）';
COMMENT ON COLUMN sys_menu.menu_type IS '菜单类型（M目录 C菜单 F按钮）';
COMMENT ON COLUMN sys_menu.visible IS '菜单状态（1显示 -1隐藏）';
COMMENT ON COLUMN sys_menu.status IS '菜单状态（1正常 -1停用）';
COMMENT ON COLUMN sys_menu.perms IS '权限标识';
COMMENT ON COLUMN sys_menu.icon IS '菜单图标';

COMMENT ON COLUMN sys_menu.create_by IS '创建者';
COMMENT ON COLUMN sys_menu.create_time IS '创建时间';
COMMENT ON COLUMN sys_menu.update_by IS '更新者';
COMMENT ON COLUMN sys_menu.update_time IS '更新时间';
COMMENT ON COLUMN sys_menu.remark IS '备注';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
INSERT INTO sys_menu
VALUES ('1', '系统管理', '0', '1', 'system', null, '', '', 1, 0, 'M', '0', '0', '', 'system', 'admin', NOW(), '', null,
        '系统管理目录'),
       ('2', '系统监控', '0', '2', 'monitor', null, '', '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', NOW(), '',
        null, '系统监控目录'),
       ('3', '系统工具', '0', '3', 'tool', null, '', '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', NOW(), '', null,
        '系统工具目录'),
       ('4', '若依官网', '0', '4', 'http://ruoyi.vip', null, '', '', 0, 0, 'M', '0', '0', '', 'guide', 'admin', NOW(),
        '', null, '若依官网地址'),
       ('100', '用户管理', '1', '1', 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list',
        'user', 'admin', NOW(), '', null, '用户管理菜单'),
       ('101', '角色管理', '1', '2', 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list',
        'peoples', 'admin', NOW(), '', null, '角色管理菜单'),
       ('102', '菜单管理', '1', '3', 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list',
        'tree-table', 'admin', NOW(), '', null, '菜单管理菜单'),
       ('103', '部门管理', '1', '4', 'dept', 'system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list',
        'tree', 'admin', NOW(), '', null, '部门管理菜单'),
       ('104', '岗位管理', '1', '5', 'post', 'system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list',
        'post', 'admin', NOW(), '', null, '岗位管理菜单'),
       ('105', '字典管理', '1', '6', 'dict', 'system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list',
        'dict', 'admin', NOW(), '', null, '字典管理菜单'),
       ('106', '参数设置', '1', '7', 'config', 'system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list',
        'edit', 'admin', NOW(), '', null, '参数设置菜单'),
       ('107', '通知公告', '1', '8', 'notice', 'system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list',
        'message', 'admin', NOW(), '', null, '通知公告菜单'),
       ('108', '日志管理', '1', '9', 'log', '', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', NOW(), '', null,
        '日志管理菜单'),
       ('109', '在线用户', '2', '1', 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0',
        'monitor:online:list', 'online', 'admin', NOW(), '', null, '在线用户菜单'),
       ('110', '定时任务', '2', '2', 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job',
        'admin', NOW(), '', null, '定时任务菜单'),
       ('111', '数据监控', '2', '3', 'druid', 'monitor/druid/index', '', '', 1, 0, 'C', '0', '0', 'monitor:druid:list',
        'druid', 'admin', NOW(), '', null, '数据监控菜单'),
       ('112', '服务监控', '2', '4', 'server', 'monitor/server/index', '', '', 1, 0, 'C', '0', '0',
        'monitor:server:list', 'server', 'admin', NOW(), '', null, '服务监控菜单'),
       ('113', '缓存监控', '2', '5', 'cache', 'monitor/cache/index', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list',
        'redis', 'admin', NOW(), '', null, '缓存监控菜单'),
       ('114', '缓存列表', '2', '6', 'cacheList', 'monitor/cache/list', '', '', 1, 0, 'C', '0', '0',
        'monitor:cache:list', 'redis-list', 'admin', NOW(), '', null, '缓存列表菜单'),
       ('115', '表单构建', '3', '1', 'build', 'tool/build/index', '', '', 1, 0, 'C', '0', '0', 'tool:build:list',
        'build', 'admin', NOW(), '', null, '表单构建菜单'),
       ('116', '代码生成', '3', '2', 'gen', 'tool/gen/index', '', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code',
        'admin', NOW(), '', null, '代码生成菜单'),
       ('500', '操作日志', '108', '1', 'operlog', 'monitor/operlog/index', '', '', 1, 0, 'C', '0', '0',
        'monitor:operlog:list', 'form', 'admin', NOW(), '', null, '操作日志菜单'),
       ('501', '登录日志', '108', '2', 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0',
        'monitor:logininfor:list', 'logininfor', 'admin', NOW(), '', null, '登录日志菜单'),
       ('1000', '用户查询', '100', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', NOW(),
        '', null, ''),
       ('1001', '用户新增', '100', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', NOW(), '',
        null, ''),
       ('1002', '用户修改', '100', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', NOW(),
        '', null, ''),
       ('1003', '用户删除', '100', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', NOW(),
        '', null, ''),
       ('1004', '用户导出', '100', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', NOW(),
        '', null, ''),
       ('1005', '用户导入', '100', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', NOW(),
        '', null, ''),
       ('1006', '重置密码', '100', '7', '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin',
        NOW(), '', null, ''),
       ('1007', '角色查询', '101', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', NOW(),
        '', null, ''),
       ('1008', '角色新增', '101', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', NOW(), '',
        null, ''),
       ('1009', '角色修改', '101', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', NOW(),
        '', null, ''),
       ('1010', '角色删除', '101', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', NOW(),
        '', null, ''),
       ('1011', '角色导出', '101', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', NOW(),
        '', null, ''),
       ('1012', '菜单查询', '102', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', NOW(),
        '', null, ''),
       ('1013', '菜单新增', '102', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', NOW(), '',
        null, ''),
       ('1014', '菜单修改', '102', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', NOW(),
        '', null, ''),
       ('1015', '菜单删除', '102', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', NOW(),
        '', null, ''),
       ('1016', '部门查询', '103', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', NOW(),
        '', null, ''),
       ('1017', '部门新增', '103', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', NOW(), '',
        null, ''),
       ('1018', '部门修改', '103', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', NOW(),
        '', null, ''),
       ('1019', '部门删除', '103', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', NOW(),
        '', null, ''),
       ('1020', '岗位查询', '104', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', NOW(),
        '', null, ''),
       ('1021', '岗位新增', '104', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', NOW(), '',
        null, ''),
       ('1022', '岗位修改', '104', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', NOW(),
        '', null, ''),
       ('1023', '岗位删除', '104', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', NOW(),
        '', null, ''),
       ('1024', '岗位导出', '104', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', NOW(),
        '', null, ''),
       ('1025', '字典查询', '105', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', NOW(),
        '', null, ''),
       ('1026', '字典新增', '105', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', NOW(),
        '', null, ''),
       ('1027', '字典修改', '105', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', NOW(),
        '', null, ''),
       ('1028', '字典删除', '105', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', NOW(),
        '', null, ''),
       ('1029', '字典导出', '105', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', NOW(),
        '', null, ''),
       ('1030', '参数查询', '106', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin',
        NOW(), '', null, ''),
       ('1031', '参数新增', '106', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', NOW(),
        '', null, ''),
       ('1032', '参数修改', '106', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', NOW(),
        '', null, ''),
       ('1033', '参数删除', '106', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin',
        NOW(), '', null, ''),
       ('1034', '参数导出', '106', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin',
        NOW(), '', null, ''),
       ('1035', '公告查询', '107', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin',
        NOW(), '', null, ''),
       ('1036', '公告新增', '107', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', NOW(),
        '', null, ''),
       ('1037', '公告修改', '107', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', NOW(),
        '', null, ''),
       ('1038', '公告删除', '107', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin',
        NOW(), '', null, ''),
       ('1039', '操作查询', '500', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin',
        NOW(), '', null, ''),
       ('1040', '操作删除', '500', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin',
        NOW(), '', null, ''),
       ('1041', '日志导出', '500', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin',
        NOW(), '', null, ''),
       ('1042', '登录查询', '501', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin',
        NOW(), '', null, ''),
       ('1043', '登录删除', '501', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin',
        NOW(), '', null, ''),
       ('1044', '日志导出', '501', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin',
        NOW(), '', null, ''),
       ('1045', '账户解锁', '501', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin',
        NOW(), '', null, ''),
       ('1046', '在线查询', '109', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin',
        NOW(), '', null, ''),
       ('1047', '批量强退', '109', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#',
        'admin', NOW(), '', null, ''),
       ('1048', '单条强退', '109', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#',
        'admin', NOW(), '', null, ''),
       ('1049', '任务查询', '110', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', NOW(),
        '', null, ''),
       ('1050', '任务新增', '110', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', NOW(),
        '', null, ''),
       ('1051', '任务修改', '110', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', NOW(),
        '', null, ''),
       ('1052', '任务删除', '110', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', NOW(),
        '', null, ''),
       ('1053', '状态修改', '110', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin',
        NOW(), '', null, ''),
       ('1054', '任务导出', '110', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', NOW(),
        '', null, ''),
       ('1055', '生成查询', '116', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', NOW(), '',
        null, ''),
       ('1056', '生成修改', '116', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', NOW(), '',
        null, ''),
       ('1057', '生成删除', '116', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', NOW(),
        '', null, ''),
       ('1058', '导入代码', '116', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', NOW(),
        '', null, ''),
       ('1059', '预览代码', '116', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', NOW(),
        '', null, ''),
       ('1060', '生成代码', '116', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', NOW(), '',
        null, '');