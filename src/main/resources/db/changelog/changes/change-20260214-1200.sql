-- liquibase formatted sql

-- changeset rostislav:add_table_attendance
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

-- changeset rostislav:add_constraint_attendance_to_student
ALTER TABLE attendance
    ADD CONSTRAINT fk_attendance_on_student FOREIGN KEY (student_id) REFERENCES student (id);

-- changeset rostislav:add_constraint_attendance_to_lesson
ALTER TABLE attendance
    ADD CONSTRAINT fk_attendance_on_lesson FOREIGN KEY (lesson_id) REFERENCES lesson (id);
