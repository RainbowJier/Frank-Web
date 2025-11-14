-- ----------------------------
-- 用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user
(
    user_id      bigint generated always as identity,
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
    create_by    bigint       default null,
    create_time  timestamp    default now(),
    update_by    bigint       default null,
    update_time  timestamp,
    remark       varchar(500) default null,
    primary key (user_id)
);

comment on table sys_user is '用户信息表';
comment on column sys_user.user_id is '用户ID';
comment on column sys_user.user_name is '用户账号';
comment on column sys_user.nick_name is '用户昵称';
comment on column sys_user.user_type is '用户类型（00系统用户）';
comment on column sys_user.email is '用户邮箱';
comment on column sys_user.phone_number is '手机号码';
comment on column sys_user.sex is '用户性别（1男 0女 2未知）';
comment on column sys_user.avatar is '头像地址';
comment on column sys_user.password is '密码';
comment on column sys_user.status is '帐号状态（1正常 0停用）';
comment on column sys_user.del_flag is '删除标志（1-存在 0-删除）';
comment on column sys_user.create_time is '创建时间';
comment on column sys_user.update_time is '更新时间';
comment on column sys_user.create_by is '创建者';
comment on column sys_user.update_by is '更新者';
comment on column sys_user.remark is '备注';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into sys_user
(user_id, user_name, nick_name, email, sex, password, create_time, update_time) overriding system value
values (1, 'admin', 'administrator', 'frank@163.com', 1,
        '$2a$12$HgMqFBFOt1rys5iMT8ShN.1/I6woV2jgaWV3DWcM5ffDzGiyZNsIa', now(), now());

-- 更新序列，确保自动插入时，id值从当前最大值开始
SELECT setval(pg_get_serial_sequence('sys_user', 'user_id'),
              (SELECT MAX(user_id) FROM sys_user));


-- ----------------------------
-- 用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists sys_user_rel_role;
create table sys_user_rel_role
(
    user_id     bigint not null,
    role_id     bigint not null,
    del_flag    int          default 1,
    create_by   bigint       default null,
    create_time timestamp    default now(),
    update_by   bigint       default null,
    update_time timestamp,
    remark      varchar(500) default null,
    primary key (user_id, role_id)
);

comment on table sys_user_rel_role is '用户和角色关联表';
comment on column sys_user_rel_role.user_id is '用户ID';
comment on column sys_user_rel_role.role_id is '角色ID';
comment on column sys_user_rel_role.del_flag is '删除标志（1-存在 0-删除）';
comment on column sys_user_rel_role.create_time is '创建时间';
comment on column sys_user_rel_role.create_by is '创建者';
comment on column sys_user_rel_role.update_time is '更新时间';
comment on column sys_user_rel_role.update_by is '更新者';
comment on column sys_user_rel_role.remark is '备注';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
insert into sys_user_rel_role
values ('1', '1');

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
    menu_check_strictly smallint     default 1,
    status              int          default 1,
    del_flag            int          default 1,
    create_by           bigint       default null,
    create_time         timestamp    default now(),
    update_by           bigint       default null,
    update_time         timestamp,
    remark              varchar(500) default null
);

comment on table sys_role is '角色信息表';
comment on column sys_role.role_id is '角色ID';
comment on column sys_role.role_name is '角色名称';
comment on column sys_role.role_key is '角色权限字符串';
comment on column sys_role.role_sort is '显示顺序';
comment on column sys_role.menu_check_strictly is '菜单树选择项是否关联显示';
comment on column sys_role.status is '角色状态（1正常 0停用）';
comment on column sys_role.del_flag is '删除标志（1-存在 0-删除）';
comment on column sys_role.create_by is '创建者';
comment on column sys_role.create_time is '创建时间';
comment on column sys_role.update_by is '更新者';
comment on column sys_role.update_time is '更新时间';
comment on column sys_role.remark is '备注';

-- 初始化-角色信息表数据
insert into sys_role (role_id, role_name, role_key, role_sort, update_time, remark) overriding system value
values (1, '超级管理员', 'admin', 1, now(), '超级管理员');

-- 更新序列，确保自动插入时，id值从当前最大值开始
SELECT setval(pg_get_serial_sequence('sys_role', 'role_id'),
              (SELECT MAX(role_id) FROM sys_role));


-- ----------------------------
-- 角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists sys_role_rel_menu;
create table sys_role_rel_menu
(
    role_id     bigint not null,
    menu_id     bigint not null,
    del_flag    int          default 1,
    create_by   bigint       default null,
    create_time timestamp    default now(),
    update_by   bigint       default null,
    update_time timestamp,
    remark      varchar(500) default null,
    primary key (role_id, menu_id)
);

comment on table sys_role_rel_menu is '角色和菜单关联表';
comment on column sys_role_rel_menu.role_id is '角色ID';
comment on column sys_role_rel_menu.menu_id is '菜单ID';
comment on column sys_role_rel_menu.del_flag is '删除标志（1-存在 0-删除）';
comment on column sys_role_rel_menu.create_time is '创建时间';
comment on column sys_role_rel_menu.create_by is '创建者';
comment on column sys_role_rel_menu.update_time is '更新时间';
comment on column sys_role_rel_menu.update_by is '更新者';
comment on column sys_role_rel_menu.remark is '备注';

-- ----------------------------
-- 初始化-角色和菜单关联表数据
-- ----------------------------
INSERT INTO sys_role_rel_menu (role_id, menu_id)
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
    is_frame    integer      default 0,
    is_cache    integer      default 1,
    menu_type   char(1)      default '',
    visible     int          default 1,
    status      int          default 1,
    perms       varchar(100) default null,
    icon        varchar(100) default '#',
    create_by   bigint       default null,
    create_time timestamp    default now(),
    update_by   bigint       default null,
    update_time timestamp,
    remark      varchar(500) default '',
    del_flag    int          default 1
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
COMMENT ON COLUMN sys_menu.is_frame IS '是否为外链（1-是 0-否）';
COMMENT ON COLUMN sys_menu.is_cache IS '是否缓存（1-缓存 0-不缓存）';
COMMENT ON COLUMN sys_menu.menu_type IS '菜单类型（M目录 C菜单 F按钮）';
COMMENT ON COLUMN sys_menu.visible IS '菜单状态（1-显示 0-隐藏）';
COMMENT ON COLUMN sys_menu.status IS '菜单状态（1-显示 0-隐藏）';
COMMENT ON COLUMN sys_menu.perms IS '权限标识';
COMMENT ON COLUMN sys_menu.icon IS '菜单图标';
COMMENT ON COLUMN sys_menu.create_by IS '创建者';
COMMENT ON COLUMN sys_menu.create_time IS '创建时间';
COMMENT ON COLUMN sys_menu.update_by IS '更新者';
COMMENT ON COLUMN sys_menu.update_time IS '更新时间';
COMMENT ON COLUMN sys_menu.remark IS '备注';
comment on column sys_menu.del_flag is '删除标志（1-存在 0-删除）';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame,
                      is_cache, menu_type, visible, perms, icon, create_time, update_time, remark)
VALUES (1, '系统管理', 0, 1, 'system', null, '', '', 0, 1, 'M', 1, '', 'system', now(), now(), '系统管理目录'),
       (2, '系统监控', 0, 2, 'monitor', null, '', '', 0, 1, 'M', 1, '', 'monitor', now(), now(), '系统监控目录'),
       (3, '系统工具', 0, 3, 'tool', null, '', '', 0, 1, 'M', 1, '', 'tool', now(), now(), '系统工具目录'),
       (4, '若依官网', 0, 4, 'http://ruoyi.vip', null, '', '', 1, 1, 'M', 1, '', 'guide', now(), now(), '若依官网地址'),
       (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 0, 1, 'C', 1, 'system:user:list', 'user', now(),
        now(), '用户管理菜单'),
       (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 0, 1, 'C', 1, 'system:role:list', 'peoples', now(),
        now(), '角色管理菜单'),
       (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 0, 1, 'C', 1, 'system:menu:list', 'tree-table',
        now(), now(), '菜单管理菜单'),
       (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 0, 1, 'C', 1, 'system:dept:list', 'tree', now(),
        now(), '部门管理菜单'),
       (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', 0, 1, 'C', 1, 'system:post:list', 'post', now(),
        now(), '岗位管理菜单'),
       (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 0, 1, 'C', 1, 'system:dict:list', 'dict', now(),
        now(), '字典管理菜单'),
       (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 0, 1, 'C', 1, 'system:config:list', 'edit',
        now(), now(), '参数设置菜单'),
       (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', 0, 1, 'C', 1, 'system:notice:list', 'message',
        now(), now(), '通知公告菜单'),
       (108, '日志管理', 1, 9, 'log', '', '', '', 0, 1, 'M', 1, '', 'log', now(), now(), '日志管理菜单'),
       (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 0, 1, 'C', 1, 'monitor:online:list', 'online',
        now(), now(), '在线用户菜单'),
       (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 0, 1, 'C', 1, 'monitor:job:list', 'job', now(),
        now(), '定时任务菜单'),
       (111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', '', 0, 1, 'C', 1, 'monitor:druid:list', 'druid',
        now(), now(), '数据监控菜单'),
       (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', '', 0, 1, 'C', 1, 'monitor:server:list', 'server',
        now(), now(), '服务监控菜单'),
       (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', '', 0, 1, 'C', 1, 'monitor:cache:list', 'redis',
        now(), now(), '缓存监控菜单'),
       (114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', '', 0, 1, 'C', 1, 'monitor:cache:list',
        'redis-list', now(), now(), '缓存列表菜单'),
       (115, '表单构建', 3, 1, 'build', 'tool/build/index', '', '', 0, 1, 'C', 1, 'tool:build:list', 'build', now(),
        now(), '表单构建菜单'),
       (116, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', '', 0, 1, 'C', 1, 'tool:gen:list', 'code', now(), now(),
        '代码生成菜单'),
       (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', '', 0, 1, 'C', 1, 'monitor:operlog:list',
        'form', now(), now(), '操作日志菜单'),
       (501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', '', 0, 1, 'C', 1,
        'monitor:logininfor:list', 'logininfor', now(), now(), '登录日志菜单'),
       (1000, '用户查询', 100, 1, '', '', '', '', 0, 1, 'F', 1, 'system:user:query', '#', now(), now(), ''),
       (1001, '用户新增', 100, 2, '', '', '', '', 0, 1, 'F', 1, 'system:user:add', '#', now(), now(), ''),
       (1002, '用户修改', 100, 3, '', '', '', '', 0, 1, 'F', 1, 'system:user:edit', '#', now(), now(), ''),
       (1003, '用户删除', 100, 4, '', '', '', '', 0, 1, 'F', 1, 'system:user:remove', '#', now(), now(), ''),
       (1004, '用户导出', 100, 5, '', '', '', '', 0, 1, 'F', 1, 'system:user:export', '#', now(), now(), ''),
       (1005, '用户导入', 100, 6, '', '', '', '', 0, 1, 'F', 1, 'system:user:import', '#', now(), now(), ''),
       (1006, '重置密码', 100, 7, '', '', '', '', 0, 1, 'F', 1, 'system:user:resetPwd', '#', now(), now(), ''),
       (1007, '角色查询', 101, 1, '', '', '', '', 0, 1, 'F', 1, 'system:role:query', '#', now(), now(), ''),
       (1008, '角色新增', 101, 2, '', '', '', '', 0, 1, 'F', 1, 'system:role:add', '#', now(), now(), ''),
       (1009, '角色修改', 101, 3, '', '', '', '', 0, 1, 'F', 1, 'system:role:edit', '#', now(), now(), ''),
       (1010, '角色删除', 101, 4, '', '', '', '', 0, 1, 'F', 1, 'system:role:remove', '#', now(), now(), ''),
       (1011, '角色导出', 101, 5, '', '', '', '', 0, 1, 'F', 1, 'system:role:export', '#', now(), now(), ''),
       (1012, '菜单查询', 102, 1, '', '', '', '', 0, 1, 'F', 1, 'system:menu:query', '#', now(), now(), ''),
       (1013, '菜单新增', 102, 2, '', '', '', '', 0, 1, 'F', 1, 'system:menu:add', '#', now(), now(), ''),
       (1014, '菜单修改', 102, 3, '', '', '', '', 0, 1, 'F', 1, 'system:menu:edit', '#', now(), now(), ''),
       (1015, '菜单删除', 102, 4, '', '', '', '', 0, 1, 'F', 1, 'system:menu:remove', '#', now(), now(), ''),
       (1016, '部门查询', 103, 1, '', '', '', '', 0, 1, 'F', 1, 'system:dept:query', '#', now(), now(), ''),
       (1017, '部门新增', 103, 2, '', '', '', '', 0, 1, 'F', 1, 'system:dept:add', '#', now(), now(), ''),
       (1018, '部门修改', 103, 3, '', '', '', '', 0, 1, 'F', 1, 'system:dept:edit', '#', now(), now(), ''),
       (1019, '部门删除', 103, 4, '', '', '', '', 0, 1, 'F', 1, 'system:dept:remove', '#', now(), now(), ''),
       (1020, '岗位查询', 104, 1, '', '', '', '', 0, 1, 'F', 1, 'system:post:query', '#', now(), now(), ''),
       (1021, '岗位新增', 104, 2, '', '', '', '', 0, 1, 'F', 1, 'system:post:add', '#', now(), now(), ''),
       (1022, '岗位修改', 104, 3, '', '', '', '', 0, 1, 'F', 1, 'system:post:edit', '#', now(), now(), ''),
       (1023, '岗位删除', 104, 4, '', '', '', '', 0, 1, 'F', 1, 'system:post:remove', '#', now(), now(), ''),
       (1024, '岗位导出', 104, 5, '', '', '', '', 0, 1, 'F', 1, 'system:post:export', '#', now(), now(), ''),
       (1025, '字典查询', 105, 1, '#', '', '', '', 0, 1, 'F', 1, 'system:dict:query', '#', now(), now(), ''),
       (1026, '字典新增', 105, 2, '#', '', '', '', 0, 1, 'F', 1, 'system:dict:add', '#', now(), now(), ''),
       (1027, '字典修改', 105, 3, '#', '', '', '', 0, 1, 'F', 1, 'system:dict:edit', '#', now(), now(), ''),
       (1028, '字典删除', 105, 4, '#', '', '', '', 0, 1, 'F', 1, 'system:dict:remove', '#', now(), now(), ''),
       (1029, '字典导出', 105, 5, '#', '', '', '', 0, 1, 'F', 1, 'system:dict:export', '#', now(), now(), ''),
       (1030, '参数查询', 106, 1, '#', '', '', '', 0, 1, 'F', 1, 'system:config:query', '#', now(), now(), ''),
       (1031, '参数新增', 106, 2, '#', '', '', '', 0, 1, 'F', 1, 'system:config:add', '#', now(), now(), ''),
       (1032, '参数修改', 106, 3, '#', '', '', '', 0, 1, 'F', 1, 'system:config:edit', '#', now(), now(), ''),
       (1033, '参数删除', 106, 4, '#', '', '', '', 0, 1, 'F', 1, 'system:config:remove', '#', now(), now(), ''),
       (1034, '参数导出', 106, 5, '#', '', '', '', 0, 1, 'F', 1, 'system:config:export', '#', now(), now(), ''),
       (1035, '公告查询', 107, 1, '#', '', '', '', 0, 1, 'F', 1, 'system:notice:query', '#', now(), now(), ''),
       (1036, '公告新增', 107, 2, '#', '', '', '', 0, 1, 'F', 1, 'system:notice:add', '#', now(), now(), ''),
       (1037, '公告修改', 107, 3, '#', '', '', '', 0, 1, 'F', 1, 'system:notice:edit', '#', now(), now(), ''),
       (1038, '公告删除', 107, 4, '#', '', '', '', 0, 1, 'F', 1, 'system:notice:remove', '#', now(), now(), ''),
       (1039, '操作查询', 500, 1, '#', '', '', '', 0, 1, 'F', 1, 'monitor:operlog:query', '#', now(), now(), ''),
       (1040, '操作删除', 500, 2, '#', '', '', '', 0, 1, 'F', 1, 'monitor:operlog:remove', '#', now(), now(), ''),
       (1041, '日志导出', 500, 3, '#', '', '', '', 0, 1, 'F', 1, 'monitor:operlog:export', '#', now(), now(), ''),
       (1042, '登录查询', 501, 1, '#', '', '', '', 0, 1, 'F', 1, 'monitor:logininfor:query', '#', now(), now(), ''),
       (1043, '登录删除', 501, 2, '#', '', '', '', 0, 1, 'F', 1, 'monitor:logininfor:remove', '#', now(), now(), ''),
       (1044, '日志导出', 501, 3, '#', '', '', '', 0, 1, 'F', 1, 'monitor:logininfor:export', '#', now(), now(), ''),
       (1045, '账户解锁', 501, 4, '#', '', '', '', 0, 1, 'F', 1, 'monitor:logininfor:unlock', '#', now(), now(), ''),
       (1046, '在线查询', 109, 1, '#', '', '', '', 0, 1, 'F', 1, 'monitor:online:query', '#', now(), now(), ''),
       (1047, '批量强退', 109, 2, '#', '', '', '', 0, 1, 'F', 1, 'monitor:online:batchLogout', '#', now(), now(), ''),
       (1048, '单条强退', 109, 3, '#', '', '', '', 0, 1, 'F', 1, 'monitor:online:forceLogout', '#', now(), now(), ''),
       (1049, '任务查询', 110, 1, '#', '', '', '', 0, 1, 'F', 1, 'monitor:job:query', '#', now(), now(), ''),
       (1050, '任务新增', 110, 2, '#', '', '', '', 0, 1, 'F', 1, 'monitor:job:add', '#', now(), now(), ''),
       (1051, '任务修改', 110, 3, '#', '', '', '', 0, 1, 'F', 1, 'monitor:job:edit', '#', now(), now(), ''),
       (1052, '任务删除', 110, 4, '#', '', '', '', 0, 1, 'F', 1, 'monitor:job:remove', '#', now(), now(), ''),
       (1053, '状态修改', 110, 5, '#', '', '', '', 0, 1, 'F', 1, 'monitor:job:changeStatus', '#', now(), now(), ''),
       (1054, '任务导出', 110, 6, '#', '', '', '', 0, 1, 'F', 1, 'monitor:job:export', '#', now(), now(), ''),
       (1055, '生成查询', 116, 1, '#', '', '', '', 0, 1, 'F', 1, 'tool:gen:query', '#', now(), now(), ''),
       (1056, '生成修改', 116, 2, '#', '', '', '', 0, 1, 'F', 1, 'tool:gen:edit', '#', now(), now(), ''),
       (1057, '生成删除', 116, 3, '#', '', '', '', 0, 1, 'F', 1, 'tool:gen:remove', '#', now(), now(), ''),
       (1058, '导入代码', 116, 4, '#', '', '', '', 0, 1, 'F', 1, 'tool:gen:import', '#', now(), now(), ''),
       (1059, '预览代码', 116, 5, '#', '', '', '', 0, 1, 'F', 1, 'tool:gen:preview', '#', now(), now(), ''),
       (1060, '生成代码', 116, 6, '#', '', '', '', 0, 1, 'F', 1, 'tool:gen:code', '#', now(), now(), '');

-- 更新序列，确保自动插入时，id值从当前最大值开始
SELECT setval(pg_get_serial_sequence('sys_menu', 'menu_id'),
              (SELECT MAX(menu_id) FROM sys_menu));

-- ----------------------------
-- 字典数据表
-- ----------------------------
drop table if exists sys_dict_data;
create table sys_dict_data
(
    dict_code   bigint generated by default as identity primary key,
    dict_sort   integer      default 0,
    dict_label  varchar(100) default '',
    dict_value  varchar(100) default '',
    dict_type   varchar(100) default '',
    css_class   varchar(100) default null,
    list_class  varchar(100) default null,
    is_default  char(1)      default 'N',
    status      int          default 1,
    del_flag    int          default 1,
    create_by   bigint       default null,
    create_time timestamp    default now(),
    update_by   bigint       default null,
    update_time timestamp,
    remark      varchar(500) default null
);


-- 表注释
comment on table sys_dict_data is '字典数据表';
comment on column sys_dict_data.dict_code is '字典编码';
comment on column sys_dict_data.dict_sort is '字典排序';
comment on column sys_dict_data.dict_label is '字典标签';
comment on column sys_dict_data.dict_value is '字典键值';
comment on column sys_dict_data.dict_type is '字典类型';
comment on column sys_dict_data.css_class is '样式属性（其他样式扩展）';
comment on column sys_dict_data.list_class is '表格回显样式';
comment on column sys_dict_data.is_default is '是否默认（Y是 N否）';
comment on column sys_dict_data.status is '状态（1正常，0停用）';
comment on column sys_dict_data.del_flag is '删除标志（1代表存在 0代表删除）';
comment on column sys_dict_data.create_by is '创建者';
comment on column sys_dict_data.create_time is '创建时间';
comment on column sys_dict_data.update_by is '更新者';
comment on column sys_dict_data.update_time is '更新时间';
comment on column sys_dict_data.remark is '备注';


-- 插入数据
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
                          status, create_time, update_time, remark)
VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', 1, now(), now(), '性别男'),
       (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', 1, now(), now(), '性别女'),
       (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', 1, now(), now(), '性别未知'),
       (4, 1, '显示', '1', 'sys_show_hide', '', 'primary', 'Y', 1, now(), now(), '显示菜单'),
       (5, 2, '隐藏', '0', 'sys_show_hide', '', 'danger', 'N', 1, now(), now(), '隐藏菜单'),
       (6, 1, '正常', '1', 'sys_normal_disable', '', 'primary', 'Y', 1, now(), now(), '正常状态'),
       (7, 2, '停用', '0', 'sys_normal_disable', '', 'danger', 'N', 1, now(), now(), '停用状态'),
       (8, 1, '正常', '1', 'sys_job_status', '', 'primary', 'Y', 1, now(), now(), '正常状态'),
       (9, 2, '暂停', '0', 'sys_job_status', '', 'danger', 'N', 1, now(), now(), '停用状态'),
       (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', 1, now(), now(), '默认分组'),
       (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', 1, now(), now(), '系统分组'),
       (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', 1, now(), now(), '系统默认是'),
       (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', 1, now(), now(), '系统默认否'),
       (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', 1, now(), now(), '通知'),
       (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', 1, now(), now(), '公告'),
       (16, 1, '正常', '1', 'sys_notice_status', '', 'primary', 'Y', 1, now(), now(), '正常状态'),
       (17, 2, '关闭', '0', 'sys_notice_status', '', 'danger', 'N', 1, now(), now(), '关闭状态'),
       (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', 1, now(), now(), '其他操作'),
       (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', 1, now(), now(), '新增操作'),
       (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', 1, now(), now(), '修改操作'),
       (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', 1, now(), now(), '删除操作'),
       (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', 1, now(), now(), '授权操作'),
       (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', 1, now(), now(), '导出操作'),
       (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', 1, now(), now(), '导入操作'),
       (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', 1, now(), now(), '强退操作'),
       (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', 1, now(), now(), '生成操作'),
       (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', 1, now(), now(), '清空操作'),
       (28, 1, '成功', '1', 'sys_common_status', '', 'primary', 'N', 1, now(), now(), '正常状态'),
       (29, 2, '失败', '0', 'sys_common_status', '', 'danger', 'N', 1, now(), now(), '停用状态');

SELECT setval(pg_get_serial_sequence('sys_dict_data', 'dict_code'),
              (SELECT MAX(dict_code) FROM sys_dict_data));


-- ----------------------------
-- 字典类型表
-- ----------------------------
drop table if exists sys_dict_type;
create table sys_dict_type
(
    dict_id     bigint generated by default as identity primary key,
    dict_name   varchar(100) default '',
    dict_type   varchar(100) default '',
    status      int          default 1,
    del_flag    int          default 1,
    create_by   bigint       default null,
    create_time timestamp    default now(),
    update_by   bigint       default null,
    update_time timestamp,
    remark      varchar(500) default null,
    unique (dict_type)
);

-- 表注释
comment on table sys_dict_type is '字典类型表';
comment on column sys_dict_type.dict_id is '字典主键';
comment on column sys_dict_type.dict_name is '字典名称';
comment on column sys_dict_type.dict_type is '字典类型';
comment on column sys_dict_type.status is '状态（1正常，-1停用）';
comment on column sys_dict_type.create_by is '创建者';
comment on column sys_dict_type.create_time is '创建时间';
comment on column sys_dict_type.update_by is '更新者';
comment on column sys_dict_type.update_time is '更新时间';
comment on column sys_dict_type.remark is '备注';
comment on column sys_dict_type.del_flag is '删除标志（1-存在 0-删除）';

INSERT INTO sys_dict_type(dict_id, dict_name, dict_type, status, create_time, update_time, remark)
VALUES (1, '用户性别', 'sys_user_sex', 1, now(), now(), '用户性别列表'),
       (2, '菜单状态', 'sys_show_hide', 1, now(), now(), '菜单状态列表'),
       (3, '系统开关', 'sys_normal_disable', 1, now(), now(), '系统开关列表'),
       (4, '任务状态', 'sys_job_status', 1, now(), now(), '任务状态列表'),
       (5, '任务分组', 'sys_job_group', 1, now(), now(), '任务分组列表'),
       (6, '系统是否', 'sys_yes_no', 1, now(), now(), '系统是否列表'),
       (7, '通知类型', 'sys_notice_type', 1, now(), now(), '通知类型列表'),
       (8, '通知状态', 'sys_notice_status', 1, now(), now(), '通知状态列表'),
       (9, '操作类型', 'sys_oper_type', 1, now(), now(), '操作类型列表'),
       (10, '系统状态', 'sys_common_status', 1, now(), now(), '登录状态列表');

-- 更新序列，确保自动插入时，id值从当前最大值开始
SELECT setval(pg_get_serial_sequence('sys_dict_type', 'dict_id'),
              (SELECT MAX(dict_id) FROM sys_dict_type));


-- ----------------------------
-- sys_log_login
-- ----------------------------
DROP TABLE sys_log_login;
CREATE TABLE sys_log_login
(
    id             BIGSERIAL PRIMARY KEY,
    user_name      VARCHAR(50)  DEFAULT ''  NOT NULL,
    ipaddr         VARCHAR(128) DEFAULT ''  NOT NULL,
    login_location VARCHAR(255) DEFAULT ''  NOT NULL,
    browser        VARCHAR(50)  DEFAULT ''  NOT NULL,
    os             VARCHAR(50)  DEFAULT ''  NOT NULL,
    status         CHAR(1)      DEFAULT '0' NOT NULL,
    msg            VARCHAR(255) DEFAULT ''  NOT NULL,
    login_time     TIMESTAMP    DEFAULT now(),
    del_flag       int          default 1,
    create_by      bigint       default null,
    create_time    timestamp    default now(),
    update_by      bigint       default null,
    update_time    timestamp,
    remark         varchar(500) default null
);

COMMENT ON TABLE sys_log_login IS '系统访问记录';
COMMENT ON COLUMN sys_log_login.id IS '访问ID';
COMMENT ON COLUMN sys_log_login.user_name IS '用户账号';
COMMENT ON COLUMN sys_log_login.ipaddr IS '登录IP地址';
COMMENT ON COLUMN sys_log_login.login_location IS '登录地点';
COMMENT ON COLUMN sys_log_login.browser IS '浏览器类型';
COMMENT ON COLUMN sys_log_login.os IS '操作系统';
COMMENT ON COLUMN sys_log_login.status IS '登录状态1成功 0失败）';
COMMENT ON COLUMN sys_log_login.msg IS '提示消息';
COMMENT ON COLUMN sys_log_login.login_time IS '访问时间';
comment on column sys_dict_type.del_flag is '删除标志（1-存在 0-删除）';
comment on column sys_dict_type.create_by is '创建者';
comment on column sys_dict_type.create_time is '创建时间';
comment on column sys_dict_type.update_by is '更新者';
comment on column sys_dict_type.update_time is '更新时间';
comment on column sys_dict_type.remark is '备注';


CREATE INDEX idx_sys_log_login_status ON sys_log_login (status);
CREATE INDEX idx_sys_log_login_lt ON sys_log_login (login_time);




