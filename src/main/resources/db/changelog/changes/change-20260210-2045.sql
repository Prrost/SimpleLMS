-- liquibase formatted sql

-- changeset rostislav:add_table_lesson
CREATE TABLE lesson
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    name         VARCHAR(255),
    classroom_id BIGINT,
    student_group_id BIGINT,
    starts_at    TIMESTAMP,
    ends_at      TIMESTAMP,
    expires_at   TIMESTAMP,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP,
    created_by   VARCHAR(255),
    updated_by   VARCHAR(255),
    is_deleted   BOOLEAN,
    CONSTRAINT pk_lesson PRIMARY KEY (id)
);

-- changeset rostislav:add_constraint_lesson_to_classroom
ALTER TABLE lesson
    ADD CONSTRAINT FK_LESSON_ON_CLASSROOM FOREIGN KEY (classroom_id) REFERENCES classroom (id);

