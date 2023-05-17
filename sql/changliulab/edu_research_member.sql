create table changliulab.edu_research_member
(
    id           bigint unsigned  not null
        primary key,
    research_id  bigint unsigned  not null,
    member_id    bigint unsigned  not null,
    member_name  varchar(20)      null,
    gmt_create   datetime         null,
    gmt_modified datetime         null,
    is_deleted   bit default b'0' not null
);

