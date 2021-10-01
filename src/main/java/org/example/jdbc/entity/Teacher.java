package org.example.jdbc.entity;

import java.util.UUID;

public class Teacher {

    private UUID teacherId;
    private String name;
    private String surname;

    public Teacher() {
    }

    public Teacher(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public UUID getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(UUID teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return String.format("Teacher {id = %s, name = %s, surname = %s}", teacherId, name, surname);
    }
}
