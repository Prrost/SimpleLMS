-- liquibase formatted sql

-- changeset rostislav:add_table_attendance context:h2
CREATE TABLE attendance
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT,
    lesson_id  BIGINT,
    mark       VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_deleted BOOLEAN,
    CONSTRAINT pk_attendance PRIMARY KEY (id)
);

-- changeset rostislav:add_constraint_attendance_to_student context:h2
ALTER TABLE attendance
    ADD CONSTRAINT fk_attendance_on_student FOREIGN KEY (student_id) REFERENCES student (id);

-- changeset rostislav:add_constraint_attendance_to_lesson context:h2
ALTER TABLE attendance
    ADD CONSTRAINT fk_attendance_on_lesson FOREIGN KEY (lesson_id) REFERENCES lesson (id);

-- ================================================================

-- changeset rostislav:add_table_attendance context:postgres
CREATE TABLE attendance
(
    id         BIGSERIAL PRIMARY KEY,
    student_id BIGINT,
    lesson_id  BIGINT,
    mark       VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_deleted BOOLEAN
);

-- changeset rostislav:add_constraint_attendance_to_student context:postgres
ALTER TABLE attendance
    ADD CONSTRAINT fk_attendance_on_student
        FOREIGN KEY (student_id) REFERENCES student (id);

-- changeset rostislav:add_constraint_attendance_to_lesson context:postgres
ALTER TABLE attendance
    ADD CONSTRAINT fk_attendance_on_lesson
        FOREIGN KEY (lesson_id) REFERENCES lesson (id);