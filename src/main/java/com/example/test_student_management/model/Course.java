package com.example.test_student_management.model;

public class Course {
    private String course;
    private String courseName;

    public Course() {
    }

    public Course(String course, String courseName) {
        this.course = course;
        this.courseName = courseName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return course + " - " + courseName;
    }
}
