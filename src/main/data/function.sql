CREATE OR REPLACE FUNCTION addStudent(name text, surname text, email text)
    RETURNS void AS
'
INSERT INTO students (name, surname, email) VALUES (name, surname, email);
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION deleteStudentById(studentId uuid)
    RETURNS void AS
'
DELETE FROM students WHERE student_id = studentId;
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION allStudents()
    RETURNS SETOF students AS
'
SELECT * FROM students;
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION updateStudent(_name text, _surname text, studentId uuid)
    RETURNS void AS
'
UPDATE students SET name = _name, surname = _surname WHERE student_id = studentId;
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION getStudentById(studentId uuid)
    RETURNS students AS
    '
SELECT * FROM students WHERE student_id = studentId;
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION allTeachers()
    RETURNS SETOF teachers AS
'
SELECT *
    FROM teachers;
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION getTeacherById(teacherId uuid)
    RETURNS teachers AS
    '
SELECT * FROM teachers WHERE teacher_id = teacherId;
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION addTeacher(name text, surname text)
    RETURNS void AS
'
INSERT INTO teachers (name, surname) VALUES (name, surname);
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION updateTeacher(_name text, _surname text, teacherId uuid)
    RETURNS void AS
'
UPDATE teachers SET name = _name, surname = _surname WHERE teacher_id = teacherId;
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION deleteTeacherById(teacherId uuid)
    RETURNS void AS
'
DELETE FROM teachers WHERE teacher_id = teacherId;
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION allCourses()
    RETURNS table
        (course_id  uuid,
            title      text,
            teacher_id uuid,
            name       text,
            surname    text
        )
AS
'
SELECT c.course_id, c.title, t.teacher_id, t.name, t.surname
         FROM courses AS c
         JOIN teachers as t ON (c.teacher_id = t.teacher_id);
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION getCourseById(courseId uuid)
    RETURNS table
        (course_id uuid,
        title text,
        teacher_id uuid,
        name text,
        surname text
        ) AS
'
SELECT c.course_id, c.title, t.teacher_id, t.name, t.surname
         FROM courses c
         JOIN teachers as t ON (c.teacher_id = t.teacher_id)
WHERE course_id = courseId;
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION addCourse(title text, teacherId uuid)
    RETURNS void AS
'
INSERT INTO courses (title, teacher_id) VALUES (title, teacherId);
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION updateCourse(_title text, teacherId uuid, courseId uuid)
    RETURNS void AS
'
UPDATE courses SET title = _title, teacher_id = teacherId WHERE course_id = courseId;
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION deleteCourseById(courseId uuid)
    RETURNS void AS
'
DELETE FROM courses where course_id = courseId;
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION addStudentToTheCourse(courseId uuid, studentId uuid)
    RETURNS void AS
'
INSERT INTO course_students (course_id, student_id) VALUES (courseId, studentId);
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION removeStudentFromCourse(courseId uuid, studentId uuid)
    RETURNS void AS
'
DELETE FROM course_students WHERE course_id = courseId AND student_id = studentId;
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION getStudentsFromCourse(courseId uuid)
    RETURNS SETOF students AS
'
SELECT * FROM students WHERE student_id
          IN (select cs.student_id FROM course_students cs WHERE cs.course_id = courseId);
'
LANGUAGE sql;

CREATE OR REPLACE FUNCTION getCoursesByStudent(studentId uuid)
    RETURNS table
        (
        course_id  uuid,
        title      text,
        teacher_id uuid,
        name       text,
        surname    text
        )
    AS
'
SELECT c.course_id, c.title, t.teacher_id, t.name, t.surname FROM courses c
         JOIN teachers t
              ON c.teacher_id = t.teacher_id
        WHERE course_id
          IN (select cs.course_id FROM course_students cs WHERE cs.student_id = studentId);
'
LANGUAGE sql;
