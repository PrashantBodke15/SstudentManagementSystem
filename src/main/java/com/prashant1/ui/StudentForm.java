package com.student.ui;

import com.student.dao.StudentDAO;
import com.student.model.Student;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class StudentForm extends JFrame {

    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JComboBox<String> genderBox =
            new JComboBox<>(new String[]{"Male", "Female", "Other"});

    private final JComboBox<String> courseBox =
            new JComboBox<>(new String[]{
                    "MCA", "BCA", "BSc", "BTech", "MBA", "Other"
            });

    private final JTextArea addressArea = new JTextArea(3, 20);

    private final StudentDAO dao = new StudentDAO();

    private final Integer studentId;

    public StudentForm() {
        this(null);
    }

    public StudentForm(Integer studentId) {

        this.studentId = studentId;

        setTitle(studentId == null
                ? "Student Registration"
                : "Edit Student");

        setSize(500, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 12));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        form.add(new JLabel("Full Name:"));
        form.add(nameField);

        form.add(new JLabel("Email:"));
        form.add(emailField);

        form.add(new JLabel("Phone:"));
        form.add(phoneField);

        form.add(new JLabel("Gender:"));
        form.add(genderBox);

        form.add(new JLabel("Course:"));
        form.add(courseBox);

        form.add(new JLabel("Address:"));
        form.add(new JScrollPane(addressArea));

        JButton saveButton = new JButton(
                studentId == null ? "Register Student" : "Update Student"
        );

        JButton cancelButton = new JButton("Cancel");

        form.add(saveButton);
        form.add(cancelButton);

        add(form);

        saveButton.addActionListener(e -> saveStudent());
        cancelButton.addActionListener(e -> dispose());

        if (studentId != null) {
            loadStudent();
        }
    }

    private void loadStudent() {

        try {
            Student student = dao.getStudentById(studentId);

            if (student == null) {
                JOptionPane.showMessageDialog(
                        this, "Student not found."
                );
                dispose();
                return;
            }

            nameField.setText(student.getFullName());
            emailField.setText(student.getEmail());
            phoneField.setText(student.getPhone());
            genderBox.setSelectedItem(student.getGender());
            courseBox.setSelectedItem(student.getCourse());
            addressArea.setText(student.getAddress());

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void saveStudent() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String gender = (String) genderBox.getSelectedItem();
        String course = (String) courseBox.getSelectedItem();
        String address = addressArea.getText().trim();

        if (name.isEmpty() || email.isEmpty()
                || phone.isEmpty() || course == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all required fields."
            );
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(
                    this, "Enter a valid email address."
            );
            return;
        }

        if (!phone.matches("[0-9]{10,15}")) {
            JOptionPane.showMessageDialog(
                    this, "Enter a valid phone number."
            );
            return;
        }

        Student student = new Student(
                studentId == null ? 0 : studentId,
                name,
                email,
                phone,
                gender,
                course,
                address
        );

        try {
            boolean success;

            if (studentId == null) {
                success = dao.addStudent(student);
            } else {
                success = dao.updateStudent(student);
            }

            if (success) {
                JOptionPane.showMessageDialog(
                        this,
                        studentId == null
                                ? "Student registered successfully!"
                                : "Student updated successfully!"
                );

                dispose();

            } else {
                JOptionPane.showMessageDialog(
                        this, "No changes were saved."
                );
            }

        } catch (SQLException ex) {

            if ("23000".equals(ex.getSQLState())) {
                JOptionPane.showMessageDialog(
                        this,
                        "This email may already be registered."
                );
            } else {
                showError(ex);
            }
        }
    }

    private void showError(Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}