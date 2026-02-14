-- liquibase formatted sql

-- changeset rostislav:student_group_table
CREATE TABLE student_group
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255),
    is_virtual BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_deleted BOOLEAN,
    CONSTRAINT pk_student_group PRIMARY KEY (id)
);



