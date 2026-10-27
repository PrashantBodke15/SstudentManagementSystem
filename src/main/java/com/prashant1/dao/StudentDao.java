package com.student.dao;

import com.student.database.DBConnection;
import com.student.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // CREATE: Register a student
    public boolean addStudent(Student student) throws SQLException {

        String sql = """
                INSERT INTO students
                (full_name, email, phone, gender, course, address)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.getFullName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPhone());
            ps.setString(4, student.getGender());
            ps.setString(5, student.getCourse());
            ps.setString(6, student.getAddress());

            return ps.executeUpdate() > 0;
        }
    }

    // READ: Get all students
    public List<Student> getAllStudents() throws SQLException {

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                students.add(mapStudent(rs));
            }
        }

        return students;
    }

    // READ: Get one student by ID
    public Student getStudentById(int id) throws SQLException {

        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapStudent(rs);
                }
            }
        }

        return null;
    }

    // UPDATE: Edit student details
    public boolean updateStudent(Student student) throws SQLException {

        String sql = """
                UPDATE students
                SET full_name = ?,
                    email = ?,
                    phone = ?,
                    gender = ?,
                    course = ?,
                    address = ?
                WHERE id = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.getFullName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPhone());
            ps.setString(4, student.getGender());
            ps.setString(5, student.getCourse());
            ps.setString(6, student.getAddress());
            ps.setInt(7, student.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // DELETE: Delete a student
    public boolean deleteStudent(int id) throws SQLException {

        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    // Convert a database row into a Student object
    private Student mapStudent(ResultSet rs) throws SQLException {

        return new Student(
                rs.getInt("id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("gender"),
                rs.getString("course"),
                rs.getString("address")
        );
    }
}