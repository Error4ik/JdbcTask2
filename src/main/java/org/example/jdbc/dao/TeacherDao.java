package org.example.jdbc.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.jdbc.entity.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeacherDao {

    private static final Logger logger = LogManager.getLogger();

    private final static String SAVE_TEACHER = "INSERT INTO teachers (name, surname) VALUES(?, ?);";
    private final static String GET_TEACHER_BY_ID = "SELECT * FROM teachers WHERE teacher_id = ?;";
    private final static String GET_ALL_TEACHERS = "SELECT * FROM teachers;";
    private final static String UPDATE_TEACHER = "UPDATE teachers SET name = ?, surname = ? WHERE teacher_id = ?;";
    private final static String DELETE_TEACHER_BY_ID = "DELETE from teachers WHERE teacher_id = ?;";

    private final ComboPooledDataSource dataSource;

    public TeacherDao(ComboPooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Teacher saveTeacher(Teacher teacher) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pr = con.prepareStatement(SAVE_TEACHER, Statement.RETURN_GENERATED_KEYS)) {
            pr.setString(1, teacher.getName());
            pr.setString(2, teacher.getSurname());
            pr.executeUpdate();
            try (ResultSet resultSet = pr.getGeneratedKeys()) {
                while (resultSet.next()) {
                    teacher.setTeacherId(UUID.fromString(resultSet.getString(1)));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return teacher;
    }

    public Teacher getTeacherById(UUID teacherId) {
        Teacher teacher = new Teacher();
        try (Connection conn = dataSource.getConnection(); PreparedStatement pr = conn.prepareStatement(GET_TEACHER_BY_ID)) {
            pr.setObject(1, teacherId, Types.OTHER);
            try (ResultSet rs = pr.executeQuery()) {
                while (rs.next()) {
                    teacher.setTeacherId(UUID.fromString(rs.getString(1)));
                    teacher.setName(rs.getString(2));
                    teacher.setSurname(rs.getString(3));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return teacher;
    }

    public List<Teacher> getTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pr = conn.prepareStatement(GET_ALL_TEACHERS);
             ResultSet rs = pr.executeQuery()) {
            while (rs.next()) {
                Teacher teacher = new Teacher();
                teacher.setTeacherId(UUID.fromString(rs.getString(1)));
                teacher.setName(rs.getString(2));
                teacher.setSurname(rs.getString(3));
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return teachers;
    }

    public void updateTeacher(Teacher teacher) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pr = con.prepareStatement(UPDATE_TEACHER)) {
            pr.setString(1, teacher.getName());
            pr.setString(2, teacher.getSurname());
            pr.setObject(3, teacher.getTeacherId(), Types.OTHER);
            pr.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void deleteTeacherById(UUID teacherId) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pr = con.prepareStatement(DELETE_TEACHER_BY_ID)) {
            pr.setObject(1, teacherId, Types.OTHER);
            pr.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
