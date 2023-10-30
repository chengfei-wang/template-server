drop database `template-shop`;
create database if not exists `template-shop` character set utf8mb4 collate utf8mb4_bin;

use `template-shop`;

create table user
(
    uid      varchar(64)  not null primary key,
    name     varchar(32)  not null,
    email    varchar(64)  not null unique,
    password varchar(128) not null
);

create table template
(
    tid         varchar(64) not null primary key,
    creator     varchar(64) not null,
    title       varchar(64) not null,
    content     text        not null,
    update_time timestamp   not null default current_timestamp
);

create table template_share
(
    tid        varchar(64) not null primary key,
    creator    varchar(64) not null,
    title      varchar(64) not null,
    content    text        not null,
    price      int         not null default 0,
    share_time timestamp   not null default current_timestamp
);

create table user_info
(
    uid     varchar(64) not null primary key,
    balance int         not null default 0
);

create table template_order
(
    order_id   varchar(64) not null primary key,
    template   varchar(64) not null,
    buyer      varchar(64) not null,
    price      int         not null,
    order_time timestamp   not null default current_timestamp,
    unique key (template, buyer)
);


create table image_bed
(
    image_id    varchar(64) not null primary key,
    image_owner varchar(64) not null,
    upload_time timestamp   not null default current_timestamp
);

create table template_deploy
(
    deploy_id       varchar(64) not null primary key,
    uid             varchar(64) not null,
    deploy_template varchar(64) not null,
    deploy_type     varchar(64) not null,
    user_verify     varchar(64) not null,
    deploy_addition varchar(128) not null,
    price           int         not null default 0,
    page_path       varchar(64) not null,
    deploy_time     timestamp   not null default current_timestamp
);