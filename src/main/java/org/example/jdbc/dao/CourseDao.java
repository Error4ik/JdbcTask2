package org.example.jdbc.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.jdbc.entity.Course;
import org.example.jdbc.entity.Student;
import org.example.jdbc.entity.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CourseDao {

    private static final Logger logger = LogManager.getLogger();

    private final static String SAVE_COURSE = "INSERT INTO courses (title, teacher_id) VALUES(?, ?);";
    private final static String GET_COURSE_BY_ID = "SELECT c.course_id, c.title, t.teacher_id, t.name, t.surname " +
            "FROM courses c JOIN teachers t ON(c.teacher_id = t.teacher_id) WHERE course_id = ?;";
    private final static String GET_ALL_COURSES = "SELECT c.course_id, c.title, t.teacher_id, t.name, t.surname " +
            "FROM courses c JOIN teachers t ON(c.teacher_id = t.teacher_id);";
    private final static String UPDATE_COURSE = "UPDATE courses SET title = ?, teacher_id = ? WHERE course_id = ?;";
    private final static String DELETE_COURSE_BY_ID = "DELETE from courses WHERE course_id = ?;";
    private final static String ADD_STUDENT_TO_THE_COURSE = "INSERT INTO course_students (course_id, student_id) VALUES(?, ?);";
    private final static String REMOVE_STUDENT_FROM_COURSE = "DELETE FROM course_students WHERE course_id = ? AND student_id = ?;";
    private final static String GET_ALL_STUDENTS_FROM_THE_COURSE = "SELECT * FROM students WHERE student_id " +
            "IN (select cs.student_id FROM course_students cs WHERE cs.course_id = ?);";
    private final static String GET_ALL_COURSES_BY_STUDENT =
            "SELECT c.course_id, c.title, t.teacher_id, t.name, t.surname FROM courses c " +
                    "JOIN teachers t ON c.teacher_id = t.teacher_id WHERE course_id " +
            "IN (select cs.course_id FROM course_students cs WHERE cs.student_id = ?);";

    private final ComboPooledDataSource dataSource;

    public CourseDao(ComboPooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Course saveCourse(Course course) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pr = con.prepareStatement(SAVE_COURSE, Statement.RETURN_GENERATED_KEYS)) {
            pr.setString(1, course.getTitle());
            pr.setObject(2, course.getTeacher().getTeacherId(), Types.OTHER);
            pr.executeUpdate();
            try (ResultSet resultSet = pr.getGeneratedKeys()) {
                while (resultSet.next()) {
                    course.setCourseId(UUID.fromString(resultSet.getString(1)));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return course;
    }

    public Course getCourseById(UUID courseId) {
        Course course = null;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement coursePreparedStatement = conn.prepareStatement(GET_COURSE_BY_ID)) {
            coursePreparedStatement.setObject(1, courseId, Types.OTHER);
            try (ResultSet rs = coursePreparedStatement.executeQuery()) {
                while (rs.next()) {
                    Teacher teacher = new Teacher(rs.getString(4), rs.getString(5));
                    teacher.setTeacherId(UUID.fromString(rs.getString(3)));

                    course = new Course(rs.getString(2), teacher);
                    course.setCourseId(UUID.fromString(rs.getString(1)));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return course;
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pr = conn.prepareStatement(GET_ALL_COURSES);
             ResultSet rs = pr.executeQuery()) {
            while (rs.next()) {
                Teacher teacher = new Teacher(rs.getString(4), rs.getString(5));
                teacher.setTeacherId(UUID.fromString(rs.getString(3)));

                Course course = new Course(rs.getString(2), teacher);
                course.setCourseId(UUID.fromString(rs.getString(1)));
                courses.add(course);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return courses;
    }

    public void updateCourse(Course course) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pr = con.prepareStatement(UPDATE_COURSE)) {
            pr.setString(1, course.getTitle());
            pr.setObject(2, course.getTeacher().getTeacherId(), Types.OTHER);
            pr.setObject(3, course.getCourseId(), Types.OTHER);
            pr.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void deleteCourseById(UUID courseId) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pr = con.prepareStatement(DELETE_COURSE_BY_ID)) {
            pr.setObject(1, courseId, Types.OTHER);
            pr.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void addStudentToTheCourse(UUID courseId, UUID studentId) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pr = con.prepareStatement(ADD_STUDENT_TO_THE_COURSE)) {
            pr.setObject(1, courseId, Types.OTHER);
            pr.setObject(2, studentId, Types.OTHER);
            pr.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void removeStudentFromCourse(UUID courseId, UUID studentId) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pr = con.prepareStatement(REMOVE_STUDENT_FROM_COURSE)) {
            pr.setObject(1, courseId, Types.OTHER);
            pr.setObject(2, studentId, Types.OTHER);
            pr.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public List<Student> getStudentsFromCourse(UUID courseId) {
        List<Student> students = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement pr = con.prepareStatement(GET_ALL_STUDENTS_FROM_THE_COURSE)) {
            pr.setObject(1, courseId, Types.OTHER);
            try (ResultSet rs = pr.executeQuery()) {
                while (rs.next()) {
                    Student student =
                            new Student(rs.getString(2), rs.getString(3), rs.getString(4));
                    student.setStudentId(UUID.fromString(rs.getString(1)));
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return students;
    }

    public List<Course> getCoursesByStudent(UUID studentId) {
        List<Course> courses = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement pr = con.prepareStatement(GET_ALL_COURSES_BY_STUDENT)) {
            pr.setObject(1, studentId, Types.OTHER);
            try (ResultSet rs = pr.executeQuery()) {
                while (rs.next()) {
                    Teacher teacher = new Teacher(rs.getString(4), rs.getString(5));
                    teacher.setTeacherId(UUID.fromString(rs.getString(3)));

                    Course course = new Course(rs.getString(2), teacher);
                    course.setCourseId(UUID.fromString(rs.getString(1)));
                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return courses;
    }
}
