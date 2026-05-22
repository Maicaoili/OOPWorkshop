package com.example.test_student_management.repository;

import com.example.test_student_management.model.Course;
import com.example.test_student_management.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseRepository {
    public List<Course> findAll() throws SQLException {
        String sql = "SELECT course, course_name FROM courses ORDER BY course";
        List<Course> courses = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                courses.add(new Course(
                        rs.getString("course"),
                        rs.getString("course_name")
                ));
            }
        }
        return courses;
    }
}
