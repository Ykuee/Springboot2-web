create table sys_log
(
    biz_type varchar,
    module_code varchar,
    method varchar,
    operate_type varchar,
    module_name varchar,
    description varchar,
    request_ip varchar,
    request_uri varchar,
    ua varchar,
    biz_type_description varchar,
    params varchar,
    http_method varchar,
    user_id varchar,
    result varchar,
    operate_result varchar,
    operate_type_description varchar,
    start_time timestamp(6) default NULL::timestamp without time zone,
    user_login_name varchar,
    end_time timestamp(6) default NULL::timestamp without time zone,
    clazz varchar,
    execute_time integer,
    ex_desc varchar,
    ex_detail varchar
);

create table sys_menu
(
    id varchar(32) default NULL::character varying not null
        constraint sys_menu_pk
        primary key,
    code varchar(32) default NULL::character varying,
    title varchar(50) default NULL::character varying,
    parent_id varchar(32) default NULL::character varying,
    level_type integer,
    path varchar(32) default NULL::character varying,
    component varchar(100) default NULL::character varying,
    sort integer,
    icon varchar,
    created_by varchar(32) default NULL::character varying,
    created_date timestamp(6) default NULL::timestamp without time zone,
    updated_by varchar(32) default NULL::character varying,
    updated_date timestamp(6) default NULL::timestamp without time zone,
    del_flag varchar(2) default NULL::character varying,
    disable_flag varchar(2) default NULL::character varying,
    name varchar(50) default NULL::character varying
);

comment on column sys_menu.code is '菜单编码';

comment on column sys_menu.title is '菜单名称';

comment on column sys_menu.parent_id is '父菜单ID';

comment on column sys_menu.level_type is '级别';

comment on column sys_menu.path is '路径';

comment on column sys_menu.component is '前端路由';

comment on column sys_menu.sort is '排序';

comment on column sys_menu.icon is '图标';

comment on column sys_menu.created_by is '创建人';

comment on column sys_menu.created_date is '创建日期';

comment on column sys_menu.updated_by is '更新日期';

comment on column sys_menu.updated_date is '更新日期';

comment on column sys_menu.del_flag is '删除标志 0否 1是';

create unique index sys_menu_id_uindex
    on sys_menu (id);

create table sys_role
(
    id varchar(32) default NULL::character varying not null
        constraint sys_role_pk
        primary key,
    name varchar(32) default NULL::character varying,
    code varchar(32) default NULL::character varying,
    remark varchar(50) default NULL::character varying,
    created_by varchar(32) default NULL::character varying,
    created_date timestamp(6) default NULL::timestamp without time zone,
    updated_by varchar(32) default NULL::character varying,
    updated_date timestamp(6) default NULL::timestamp without time zone,
    del_flag varchar(2) default NULL::character varying,
    disable_flag varchar(2) default NULL::character varying
);

comment on column sys_role.name is '权限名称';

comment on column sys_role.code is '权限代码';

comment on column sys_role.remark is '备注';

comment on column sys_role.created_by is '创建人';

comment on column sys_role.created_date is '创建日期';

comment on column sys_role.updated_by is '更新日期';

comment on column sys_role.updated_date is '更新日期';

comment on column sys_role.del_flag is '删除标志';

create unique index sys_role_id_uindex
    on sys_role (id);

create table sys_role_menu
(
    id varchar(32) default NULL::character varying not null
        constraint sys_role_menu_pk
        primary key,
    menu_id varchar(32) default NULL::character varying not null,
    role_id varchar(32) default NULL::character varying not null
);

create unique index sys_role_menu_id_uindex
    on sys_role_menu (id);

create unique index sys_role_menu_rid_mid_un
    on sys_role_menu (role_id, menu_id);

create table sys_user
(
    id varchar(50) default NULL::character varying,
    code varchar(32) default NULL::character varying,
    name varchar(32) default NULL::character varying,
    login_name varchar(32) default NULL::character varying,
    password varchar(100) default NULL::character varying,
    created_by varchar(32) default NULL::character varying,
    created_date timestamp(6) default NULL::timestamp without time zone,
    updated_by varchar(32) default NULL::character varying,
    updated_date timestamp(6) default NULL::timestamp without time zone,
    del_flag varchar(2) default NULL::character varying,
    disable_flag varchar(2) default NULL::character varying
);

comment on column sys_user.code is '用户编码';

comment on column sys_user.name is '用户昵称';

comment on column sys_user.login_name is '登录名';

comment on column sys_user.password is '密码';

create table sys_user_role
(
    id varchar(32) default NULL::character varying not null
        constraint sys_user_role_pk
        primary key,
    role_id varchar(32) default NULL::character varying not null,
    user_id varchar(32) default NULL::character varying not null
);

create unique index sys_user_role_id_uindex
    on sys_user_role (id);

create unique index sys_user_role_uid_rid_un
    on sys_user_role (user_id, role_id);

-- auto-generated definition
create table approval_recard
(
    id           varchar(32),
    trans_type   varchar(50),
    recard_id    varchar(32),
    dto_data     varchar,
    entity_data  varchar,
    dto_class    varchar,
    entity_class varchar,
    status       varchar(5),
    del_flag     varchar(2),
    submit_date  timestamp,
    submit_by    varchar(32),
    approve_date timestamp,
    approve_by   varchar(32)
);

comment on table approval_recard is '发布流程表';

comment on column approval_recard.trans_type is '业务类型';

comment on column approval_recard.recard_id is '业务Id';

comment on column approval_recard.dto_data is 'dto数据';

comment on column approval_recard.entity_data is '实体类数据';

comment on column approval_recard.dto_class is 'dto全类名';

comment on column approval_recard.entity_class is '实体类全类名';

comment on column approval_recard.status is '审批状态';

comment on column approval_recard.del_flag is '删除标志';

comment on column approval_recard.submit_date is '创建日期';

comment on column approval_recard.submit_by is '创建人';

comment on column approval_recard.approve_date is '更新日期';

comment on column approval_recard.approve_by is '更新人';


