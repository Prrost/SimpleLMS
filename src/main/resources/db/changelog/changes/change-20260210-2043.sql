-- liquibase formatted sql

-- changeset rprettser:add_table_classroom
CREATE TABLE classroom
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_deleted BOOLEAN,
    CONSTRAINT pk_classroom PRIMARY KEY (id)
);

