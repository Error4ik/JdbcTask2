package org.example.jdbc.services;

import org.example.jdbc.dao.StudentDao;
import org.example.jdbc.entity.Student;

import java.util.List;
import java.util.UUID;

public class StudentService {

    private final StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public Student saveStudent(Student student) {
        return studentDao.saveStudent(student);
    }

    public Student getStudentById(UUID studentId) {
        return studentDao.getStudentById(studentId);
    }

    public List<Student> getStudents() {
        return studentDao.getStudents();
    }

    public void updateStudent(Student student) {
        studentDao.updateStudent(student);
    }

    public void deleteStudentById(UUID studentId) {
        studentDao.deleteStudentById(studentId);
    }
}
