package org.example.jdbc.services;


import org.example.jdbc.dao.CourseDao;
import org.example.jdbc.entity.Course;
import org.example.jdbc.entity.Student;

import java.util.List;
import java.util.UUID;

public class CourseService {

    private final CourseDao courseDao;

    public CourseService(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public Course saveCourse(Course course) {
        return courseDao.saveCourse(course);
    }

    public Course getCourseById(UUID courseId) {
        return courseDao.getCourseById(courseId);
    }

    public List<Course> getAllCourses() {
        return courseDao.getAllCourses();
    }

    public void updateCourse(Course course) {
        courseDao.updateCourse(course);
    }

    public void deleteCourseById(UUID courseId) {
        courseDao.deleteCourseById(courseId);
    }

    public void addStudentToTheCourse(UUID courseId, UUID studentId) {
        courseDao.addStudentToTheCourse(courseId, studentId);
    }

    public void removeStudentFromCourse(UUID courseId, UUID studentId) {
        courseDao.removeStudentFromCourse(courseId, studentId);
    }

    public List<Student> getStudentsFromCourse(UUID courseId) {
        return courseDao.getStudentsFromCourse(courseId);
    }

    public List<Course> getCoursesByStudent(UUID studentId) {
        return courseDao.getCoursesByStudent(studentId);
    }
}
