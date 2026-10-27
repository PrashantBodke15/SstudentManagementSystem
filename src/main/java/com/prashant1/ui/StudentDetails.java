package com.student.ui;

import com.student.dao.StudentDAO;
import com.student.model.Student;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class StudentDetails extends JFrame {

    private final StudentDAO dao = new StudentDAO();

    public StudentDetails(int studentId) {

        setTitle("Student Details");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));

        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        add(panel);

        try {
            Student student = dao.getStudentById(studentId);

            if (student == null) {
                panel.add(new JLabel("Student not found."));
                return;
            }

            panel.add(new JLabel("Student ID: " + student.getId()));
            panel.add(new JLabel("Name: " + student.getFullName()));
            panel.add(new JLabel("Email: " + student.getEmail()));
            panel.add(new JLabel("Phone: " + student.getPhone()));
            panel.add(new JLabel("Gender: " + student.getGender()));
            panel.add(new JLabel("Course: " + student.getCourse()));
            panel.add(new JLabel("Address: " + student.getAddress()));

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to retrieve student details: "
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}