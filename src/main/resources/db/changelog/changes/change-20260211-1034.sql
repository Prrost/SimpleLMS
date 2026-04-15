-- liquibase formatted sql

-- changeset rostislav:add_table_student context:h2
CREATE TABLE student
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255),
    last_name  VARCHAR(255),
    email      VARCHAR(255),
    phone      VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_deleted BOOLEAN,
    CONSTRAINT pk_student PRIMARY KEY (id)
);


-- changeset rostislav:student_student_group_table context:h2
CREATE TABLE student_student_group
(
    student_group_id   BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    CONSTRAINT pk_student_student_group PRIMARY KEY (student_group_id, student_id)
);

-- changeset rostislav:student_group_student_fk context:h2
ALTER TABLE student_student_group
    ADD CONSTRAINT fk_stugro_on_group FOREIGN KEY (student_group_id) REFERENCES student_group (id);

-- changeset rostislav:student_student_group_fk context:h2
ALTER TABLE student_student_group
    ADD CONSTRAINT fk_stugro_on_student FOREIGN KEY (student_id) REFERENCES student (id);


-- ================================================================

-- changeset rostislav:add_table_student context:postgres
CREATE TABLE student
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255),
    last_name  VARCHAR(255),
    email      VARCHAR(255),
    phone      VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_deleted BOOLEAN
);

-- changeset rostislav:student_student_group_table context:postgres
CREATE TABLE student_student_group
(
    student_group_id BIGINT NOT NULL,
    student_id       BIGINT NOT NULL,
    CONSTRAINT pk_student_student_group PRIMARY KEY (student_group_id, student_id)
);

-- changeset rostislav:student_student_group_group_fk context:postgres
ALTER TABLE student_student_group
    ADD CONSTRAINT fk_stugro_on_group
        FOREIGN KEY (student_group_id) REFERENCES student_group (id);

-- changeset rostislav:student_student_group_student_fk context:postgres
ALTER TABLE student_student_group
    ADD CONSTRAINT fk_stugro_on_student
        FOREIGN KEY (student_id) REFERENCES student (id);