package org.example.jdbc.services;

import org.example.jdbc.dao.TeacherDao;
import org.example.jdbc.entity.Course;
import org.example.jdbc.entity.Student;
import org.example.jdbc.entity.Teacher;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TeacherServiceTest {

    private final TeacherDao teacherDao = mock(TeacherDao.class);
    private final UUID id = UUID.randomUUID();
    private final Teacher teacher = new Teacher("name", "surname");
    private final TeacherService teacherService = new TeacherService(teacherDao);

    @Before
    public void init() {
        teacher.setTeacherId(id);
    }

    @Test
    public void saveTeacher() {
        when(teacherDao.saveTeacher(any(Teacher.class))).thenReturn(teacher);

        Teacher actualTeacher = teacherService.saveTeacher(teacher);

        assertEquals(actualTeacher, teacher);
        verify(teacherDao, times(1)).saveTeacher(teacher);
    }

    @Test
    public void getTeacherById() {
        when(teacherDao.getTeacherById(any(UUID.class))).thenReturn(teacher);

        Teacher actualTeacher = teacherService.getTeacherById(id);

        assertEquals(actualTeacher, teacher);
        verify(teacherDao, times(1)).getTeacherById(id);
    }

    @Test
    public void getTeachers() {
        when(teacherDao.getTeachers()).thenReturn(Arrays.asList(teacher));

        List<Teacher> teachers = teacherService.getTeachers();

        assertEquals(teachers.get(0), teacher);
        verify(teacherDao, times(1)).getTeachers();
    }

    @Test
    public void updateTeacher() {
        teacherService.updateTeacher(teacher);

        verify(teacherDao, times(1)).updateTeacher(teacher);
    }

    @Test
    public void deleteTeacherById() {
        teacherService.deleteTeacherById(id);

        verify(teacherDao, times(1)).deleteTeacherById(id);
    }
}