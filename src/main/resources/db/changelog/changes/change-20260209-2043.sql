--liquibase formatted sql
--changeset RPrettser:create_test_table_for_liquibase_chck

create table if not exists public.test
(
    id           bigint primary key,
    some_text    text,
    some_boolean boolean,
    some_date    timestamp,
    some_number  integer,
    some_varchar varchar(100)
);