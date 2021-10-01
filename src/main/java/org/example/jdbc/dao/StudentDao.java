package org.example.jdbc.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.jdbc.entity.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StudentDao {
    private static final Logger logger = LogManager.getLogger();

    private final static String SAVE_STUDENT = "INSERT INTO students (name, surname, email) VALUES(?, ?, ?);";
    private final static String GET_STUDENT_BY_ID = "SELECT * FROM students WHERE student_id = ?;";
    private final static String GET_ALL_STUDENTS = "SELECT * FROM students;";
    private final static String UPDATE_STUDENT = "UPDATE students SET name = ?, surname = ? WHERE student_id = ?;";
    private final static String DELETE_STUDENT_BY_ID = "DELETE from students WHERE student_id = ?;";

    private final ComboPooledDataSource dataSource;

    public StudentDao(ComboPooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Student saveStudent(Student student) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pr = con.prepareStatement(SAVE_STUDENT, Statement.RETURN_GENERATED_KEYS)) {
            pr.setString(1, student.getName());
            pr.setString(2, student.getSurname());
            pr.setString(3, student.getEmail());
            pr.executeUpdate();
            try (ResultSet resultSet = pr.getGeneratedKeys()) {
                while (resultSet.next()) {
                    student.setStudentId(UUID.fromString(resultSet.getString(1)));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return student;
    }

    public Student getStudentById(UUID studentId) {
        Student student = new Student();
        try (Connection conn = dataSource.getConnection(); PreparedStatement pr = conn.prepareStatement(GET_STUDENT_BY_ID)) {
            pr.setObject(1, studentId, Types.OTHER);
            try (ResultSet rs = pr.executeQuery()) {
                while (rs.next()) {
                    student.setStudentId(UUID.fromString(rs.getString(1)));
                    student.setName(rs.getString(2));
                    student.setSurname(rs.getString(3));
                    student.setEmail(rs.getString(4));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return student;
    }

    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pr = conn.prepareStatement(GET_ALL_STUDENTS);
             ResultSet rs = pr.executeQuery()) {
            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(UUID.fromString(rs.getString(1)));
                student.setName(rs.getString(2));
                student.setSurname(rs.getString(3));
                student.setEmail(rs.getString(4));
                students.add(student);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return students;
    }

    public void updateStudent(Student student) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pr = con.prepareStatement(UPDATE_STUDENT)) {
            pr.setString(1, student.getName());
            pr.setString(2, student.getSurname());
            pr.setObject(3, student.getStudentId(), Types.OTHER);
            pr.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void deleteStudentById(UUID studentId) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pr = con.prepareStatement(DELETE_STUDENT_BY_ID)) {
            pr.setObject(1, studentId, Types.OTHER);
            pr.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
