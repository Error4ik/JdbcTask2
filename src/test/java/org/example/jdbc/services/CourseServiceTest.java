package org.example.jdbc.services;

import org.example.jdbc.dao.CourseDao;
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
import static org.mockito.Mockito.times;

public class CourseServiceTest {

    private final CourseDao courseDao = mock(CourseDao.class);
    private final UUID id = UUID.randomUUID();
    private final Course course = new Course();
    private final Teacher teacher = new Teacher();
    private final Student student = new Student("name", "surname", "email");
    private final CourseService courseService = new CourseService(courseDao);

    @Before
    public void init() {
        teacher.setTeacherId(id);
        teacher.setName("Name");
        teacher.setSurname("Surname");
        course.setCourseId(id);
        course.setTeacher(teacher);
        course.setTitle("Course");
        student.setStudentId(id);
    }

    @Test
    public void saveCourse() {
        when(courseDao.saveCourse(any(Course.class))).thenReturn(course);

        Course actualCourse = this.courseService.saveCourse(course);

        assertEquals(actualCourse, course);
        verify(courseDao, times(1)).saveCourse(course);
    }

    @Test
    public void getCourseById() {
        when(courseDao.getCourseById(any(UUID.class))).thenReturn(course);

        Course actualCourse = this.courseService.getCourseById(id);

        assertEquals(actualCourse, course);
        verify(courseDao, times(1)).getCourseById(id);
    }

    @Test
    public void getAllCourses() {
        when(courseDao.getAllCourses()).thenReturn(Arrays.asList(course));

        List<Course> actualCourses = this.courseService.getAllCourses();

        assertEquals(actualCourses.get(0), course);
        verify(courseDao, times(1)).getAllCourses();
    }

    @Test
    public void updateCourse() {
        this.courseService.updateCourse(course);

        verify(courseDao, times(1)).updateCourse(course);
    }

    @Test
    public void deleteCourseById() {
        this.courseService.deleteCourseById(id);

        verify(courseDao, times(1)).deleteCourseById(id);
    }

    @Test
    public void addStudentToTheCourse() {
        this.courseService.addStudentToTheCourse(id, id);

        verify(courseDao, times(1)).addStudentToTheCourse(id, id);
    }

    @Test
    public void removeStudentFromCourse() {
        this.courseService.removeStudentFromCourse(id, id);

        verify(courseDao, times(1)).removeStudentFromCourse(id, id);
    }

    @Test
    public void getStudentsFromCourse() {
        when(courseDao.getStudentsFromCourse(any(UUID.class))).thenReturn(Arrays.asList(student));

        List<Student> studentsFromCourse = this.courseService.getStudentsFromCourse(id);

        assertEquals(studentsFromCourse.get(0), student);
        verify(courseDao, times(1)).getStudentsFromCourse(id);
    }

    @Test
    public void getCoursesByStudent() {
        when(courseDao.getCoursesByStudent(any(UUID.class))).thenReturn(Arrays.asList(course));

        List<Course> coursesByStudent = this.courseService.getCoursesByStudent(id);

        assertEquals(coursesByStudent.get(0), course);
        verify(courseDao, times(1)).getCoursesByStudent(id);
    }
}
