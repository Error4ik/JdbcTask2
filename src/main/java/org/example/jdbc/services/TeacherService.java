package org.example.jdbc.services;


import org.example.jdbc.dao.TeacherDao;
import org.example.jdbc.entity.Teacher;

import java.util.List;
import java.util.UUID;

public class TeacherService {

    private final TeacherDao teacherDao;

    public TeacherService(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    public Teacher saveTeacher(Teacher teacher) {
        return teacherDao.saveTeacher(teacher);
    }

    public Teacher getTeacherById(UUID teacherId) {
        return teacherDao.getTeacherById(teacherId);
    }

    public List<Teacher> getTeachers() {
        return teacherDao.getTeachers();
    }

    public void updateTeacher(Teacher teacher) {
        teacherDao.updateTeacher(teacher);
    }

    public void deleteTeacherById(UUID teacherId) {
        teacherDao.deleteTeacherById(teacherId);
    }
}
