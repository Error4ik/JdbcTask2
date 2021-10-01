package org.example.jdbc.services;

import org.example.jdbc.dao.StudentDao;
import org.example.jdbc.entity.Student;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class StudentServiceTest {

    private final StudentDao studentDao = mock(StudentDao.class);
    private final UUID id = UUID.randomUUID();
    private final Student student = new Student("name", "surname", "email");
    private final StudentService studentService = new StudentService(studentDao);

    @Before
    public void init() {
        student.setStudentId(id);
    }

    @Test
    public void saveStudent() {
        when(studentDao.saveStudent(any(Student.class))).thenReturn(student);

        Student actualStudent = studentService.saveStudent(student);

        assertEquals(actualStudent, student);
        verify(studentDao, times(1)).saveStudent(student);
    }

    @Test
    public void getStudentById() {
        when(studentDao.getStudentById(any(UUID.class))).thenReturn(student);

        Student actualStudent = studentService.getStudentById(id);

        assertEquals(actualStudent, student);
        verify(studentDao, times(1)).getStudentById(id);
    }

    @Test
    public void getStudents() {
        when(studentDao.getStudents()).thenReturn(Arrays.asList(student));

        List<Student> students = studentService.getStudents();

        assertEquals(students.get(0), student);
        verify(studentDao, times(1)).getStudents();
    }

    @Test
    public void updateStudent() {
        studentService.updateStudent(student);

        verify(studentDao, times(1)).updateStudent(student);
    }

    @Test
    public void deleteStudentById() {
        studentService.deleteStudentById(id);

        verify(studentDao, times(1)).deleteStudentById(id);
    }
}
