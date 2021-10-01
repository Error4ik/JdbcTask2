package org.example.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.jdbc.dao.CourseDao;
import org.example.jdbc.dao.StudentDao;
import org.example.jdbc.dao.TeacherDao;
import org.example.jdbc.entity.Course;
import org.example.jdbc.entity.Student;
import org.example.jdbc.entity.Teacher;
import org.example.jdbc.services.CourseService;
import org.example.jdbc.services.StudentService;
import org.example.jdbc.services.TeacherService;
import org.example.jdbc.util.ComboPooledDS;
import org.example.jdbc.util.ReadingAFunctionFromDatabase;
import org.example.jdbc.util.ReadingFunctionFromFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Starter {

    private static final Logger logger = LogManager.getLogger();

    private final static CourseService courseService = new CourseService(new CourseDao(ComboPooledDS.getDatasource()));
    private final static TeacherService teacherService = new TeacherService(new TeacherDao(ComboPooledDS.getDatasource()));
    private final static StudentService studentService = new StudentService(new StudentDao(ComboPooledDS.getDatasource()));
    private final static ReadingFunctionFromFile readingFunctionFromFile = new ReadingFunctionFromFile();
    private final static ReadingAFunctionFromDatabase functionsFromDatabase = new ReadingAFunctionFromDatabase();

    private final static String DROP_COURSE_STUDENTS = "DROP TABLE IF EXISTS course_students;";
    private final static String DROP_COURSES = "DROP TABLE IF EXISTS courses;";
    private final static String DROP_TEACHERS = "DROP TABLE IF EXISTS teachers;";
    private final static String DROP_STUDENTS = "DROP TABLE IF EXISTS students;";

    private final static String CREATE_STUDENTS = String.format("CREATE TABLE IF NOT EXISTS students(%s, %s, %s, %s);",
            "student_id UUID DEFAULT gen_random_uuid() PRIMARY KEY",
            "name VARCHAR(255) NOT NULL",
            "surname VARCHAR(255) NOT NULL",
            "email VARCHAR(255) UNIQUE NOT NULL");

    private final static String CREATE_TEACHERS = String.format("CREATE TABLE IF NOT EXISTS teachers(%s, %s, %s);",
            "teacher_id UUID DEFAULT gen_random_uuid() PRIMARY KEY",
            "name VARCHAR(255) NOT NULL",
            "surname VARCHAR(255) NOT NULL");

    private final static String CREATE_COURSES = String.format("CREATE TABLE IF NOT EXISTS courses(%s, %s, %s, %s);",
            "course_id  UUID DEFAULT gen_random_uuid() PRIMARY KEY",
            "title VARCHAR(255) NOT NULL",
            "teacher_id UUID DEFAULT NULL",
            "FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id)");

    private final static String CREATE_COURSE_STUDENTS = String.format("CREATE TABLE IF NOT EXISTS course_students(%s, %s, %s, %s, %s);",
            "course_id  UUID NOT NULL",
            "student_id UUID NOT NULL",
            "PRIMARY KEY (course_id, student_id)",
            "FOREIGN KEY (course_id) REFERENCES courses (course_id) ON DELETE CASCADE",
            "FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE");

    public static void main(String[] args) {
        removeAllFunctions();
        dropAndCreateTables();
        creatingFunction();
        fillingTables();
        printAllFunctions();
    }

    private static void removeAllFunctions() {
        List<String> functionList = functionsFromDatabase.getFunctionList();
        try (Connection conn = ComboPooledDS.getDatasource().getConnection()) {
            for (String s : functionList) {
                String dropFunction = String.format("drop function %s", s);
                try (PreparedStatement pr = conn.prepareStatement(dropFunction)) {
                    pr.executeUpdate();
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    private static void dropAndCreateTables() {
        try (Connection con = ComboPooledDS.getDatasource().getConnection();
             Statement statement = con.createStatement()) {
            statement.execute(DROP_COURSE_STUDENTS);
            statement.execute(DROP_COURSES);
            statement.execute(DROP_TEACHERS);
            statement.execute(DROP_STUDENTS);

            statement.execute(CREATE_STUDENTS);
            statement.execute(CREATE_TEACHERS);
            statement.execute(CREATE_COURSES);
            statement.execute(CREATE_COURSE_STUDENTS);
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    private static void creatingFunction() {
        List<String> strings = readingFunctionFromFile.readFunctionFromFile();
        try (Connection conn = ComboPooledDS.getDatasource().getConnection()) {
            for (String s : strings) {
                try (PreparedStatement pr = conn.prepareStatement(s)) {
                    pr.executeUpdate();
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    private static void fillingTables() {
        Student alex = new Student("Alex", "Gavrilov", "alex@mail.ru");
        Student garry = new Student("Garry", "Mixeev", "garry@mail.ru");
        Student max = new Student("Max", "Dobrinin", "max@mail.ru");
        Student oleg = new Student("Oleg", "Ivanov", "oleg@mail.ru");
        Student petr = new Student("Petr", "Sidorov", "petr@mail.ru");
        Student irina = new Student("Irina", "Smirnova", "irina@mail.ru");

        studentService.saveStudent(alex);
        studentService.saveStudent(garry);
        studentService.saveStudent(max);
        studentService.saveStudent(oleg);
        studentService.saveStudent(petr);
        studentService.saveStudent(irina);

        Teacher firstTeacher = new Teacher("Dmitry", "Gavrilov");
        Teacher secondTeacher = new Teacher("Marina", "Semenova");
        Teacher thirdTeacher = new Teacher("Ilya", "Ivanov");

        teacherService.saveTeacher(firstTeacher);
        teacherService.saveTeacher(secondTeacher);
        teacherService.saveTeacher(thirdTeacher);

        Course firstCourse = new Course("History", firstTeacher);
        Course secondCourse = new Course("Math", secondTeacher);
        Course thirdCourse = new Course("Electronics", secondTeacher);
        Course fourthCourse = new Course("Biology", thirdTeacher);

        courseService.saveCourse(firstCourse);
        courseService.saveCourse(secondCourse);
        courseService.saveCourse(thirdCourse);
        courseService.saveCourse(fourthCourse);

        List<Course> courses = courseService.getAllCourses();
        List<Student> students = studentService.getStudents();
        int studentSize = students.size();

        Set<Integer> randomStudent = new HashSet<>();
        Random random = new Random();

        for (Course course : courses) {
            if (random.nextBoolean()) {
                for (Student student : students) {
                    courseService.addStudentToTheCourse(course.getCourseId(), student.getStudentId());
                }
            } else {
                fillingSet(studentSize, randomStudent);
                for (Integer num : randomStudent) {
                    courseService.addStudentToTheCourse(course.getCourseId(), students.get(num).getStudentId());
                }
                randomStudent.clear();
            }
        }
    }

    private static void fillingSet(int studentSize, Set<Integer> randomStudent) {
        for (int i = 0; i < studentSize; i++) {
            randomStudent.add((int) (Math.random() * studentSize));
        }
    }

    private static void printAllFunctions() {
        ReadingAFunctionFromDatabase functionsFromDatabase = new ReadingAFunctionFromDatabase();
        List<String> functionList = functionsFromDatabase.getFunctionList();
        functionList.forEach(logger::info);
    }
}
