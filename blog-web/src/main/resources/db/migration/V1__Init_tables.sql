-- 用户表
create table user (
    id bigint unsigned primary key auto_increment comment 'ID',
    username varchar(20) not null comment '用户名',
    password varchar(255) not null comment '密码',
    nickname varchar(20) default '' comment '昵称',
    email varchar(128) default '' comment '邮箱',
    user_pic varchar(255) default '' comment '用户头像',
    create_time datetime not null default current_timestamp comment '创建时间',
    update_time datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 唯一约束
    constraint uk_username unique (username),
    constraint uk_email unique (email)
) ENGINE = InnoDB charset = utf8mb4 collate = utf8mb4_unicode_ci auto_increment = 100001
  comment '用户表';

-- 文章分类表
create table article_category (
    id bigint unsigned primary key auto_increment comment 'ID',
    category_name varchar(32) not null comment '分类名称',
    category_alias varchar(32) not null comment '分类别名',
    create_user bigint unsigned not null comment '创建人id',
    create_time datetime not null default current_timestamp comment '创建时间',
    update_time datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 唯一约束:确保create_user和category_name的组合唯一
    constraint uk_user_category unique (create_user,category_name),
    -- 唯一约束：确保category_alias唯一
    constraint uk_category_alias unique (category_alias)
) ENGINE = InnoDB charset = utf8mb4 collate = utf8mb4_unicode_ci auto_increment = 100001
  comment '文章分类表';

-- 文章表
create table article (
    id bigint unsigned primary key auto_increment comment 'ID',
    title varchar(30) not null comment '文章标题',
    content varchar(10000) not null comment '文章内容',
    cover_img varchar(255) not null comment '文章封面图片',
    state varchar(3) default '草稿' comment '发布状态：只能是[已发布]或[草稿]',
    category_id bigint unsigned comment '文章分类id',
    create_user bigint unsigned not null comment '创建人id',
    create_time datetime not null default current_timestamp comment '创建时间',
    update_time datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 检查约束：state只能是 '已发布' 或 '草稿'
    constraint chk_article_state check (state in ('草稿', '已发布'))
) ENGINE = InnoDB charset = utf8mb4 collate = utf8mb4_unicode_ci auto_increment = 100001
  comment '文章表';

-- 角色表
CREATE TABLE `role` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL -- 如 'ROLE_ADMIN', 'ROLE_USER'
);

-- 权限表
CREATE TABLE `permission` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `code` VARCHAR(100) NOT NULL, -- 如 'user:read', 'order:delete'
  `description` VARCHAR(200)
);

-- 用户-角色关联
CREATE TABLE `user_role` (
  `user_id` BIGINT,
  `role_id` BIGINT,
  PRIMARY KEY (`user_id`, `role_id`)
);

-- 角色-权限关联
CREATE TABLE `role_permission` (
  `role_id` BIGINT,
  `permission_id` BIGINT,
  PRIMARY KEY (`role_id`, `permission_id`)
);