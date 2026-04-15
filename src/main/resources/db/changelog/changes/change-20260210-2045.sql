-- liquibase formatted sql

-- changeset rostislav:add_table_lesson context:h2
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

-- changeset rostislav:add_constraint_lesson_to_classroom context:h2
ALTER TABLE lesson
    ADD CONSTRAINT FK_LESSON_ON_CLASSROOM FOREIGN KEY (classroom_id) REFERENCES classroom (id);

-- ================================================================

-- changeset rostislav:add_table_lesson_pg context:postgres
CREATE TABLE lesson
(
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(255),
    classroom_id     BIGINT,
    student_group_id BIGINT,
    starts_at        TIMESTAMP,
    ends_at          TIMESTAMP,
    expires_at       TIMESTAMP,
    created_at       TIMESTAMP,
    updated_at       TIMESTAMP,
    created_by       VARCHAR(255),
    updated_by       VARCHAR(255),
    is_deleted       BOOLEAN
);

-- changeset rostislav:add_constraint_lesson_to_classroom_pg context:postgres
ALTER TABLE lesson
    ADD CONSTRAINT fk_lesson_on_classroom
        FOREIGN KEY (classroom_id) REFERENCES classroom (id);