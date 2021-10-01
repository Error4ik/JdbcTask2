CREATE DATABASE university;

CREATE EXTENSION pgcrypto;

CREATE TABLE students
(
    student_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name       VARCHAR(255)        NOT NULL,
    surname    VARCHAR(255)        NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE teachers
(
    teacher_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    surname    VARCHAR(255) NOT NULL
);

CREATE TABLE courses
(
    course_id  UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    teacher_id UUID DEFAULT NULL,

    FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id)
);

CREATE TABLE course_students
(
    course_id  UUID NOT NULL,
    student_id UUID NOT NULL,

    PRIMARY KEY (course_id, student_id),

    FOREIGN KEY (course_id) REFERENCES courses (course_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE
);