package org.example.jdbc.entity;

import java.util.UUID;

public class Course {

    private UUID courseId;
    private String title;
    private Teacher teacher;

    public Course() {
    }

    public Course(String title, Teacher teacher) {
        this.title = title;
        this.teacher = teacher;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return String.format(
                "Course {id = %s, title = %s, teacher = %s}", courseId, title, teacher);
    }
}
