package com.student.ui;

import com.student.dao.StudentDAO;
import com.student.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StudentList extends JFrame {

    private final StudentDAO dao = new StudentDAO();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Name", "Email", "Phone", "Course"},
            0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);

    public StudentList() {

        setTitle("Student List");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton refreshButton = new JButton("Refresh");
        JButton detailsButton = new JButton("View Details");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        JPanel buttons = new JPanel();

        buttons.add(refreshButton);
        buttons.add(detailsButton);
        buttons.add(editButton);
        buttons.add(deleteButton);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> loadStudents());

        detailsButton.addActionListener(e -> {
            Integer id = getSelectedId();

            if (id != null) {
                new StudentDetails(id).setVisible(true);
            }
        });

        editButton.addActionListener(e -> {
            Integer id = getSelectedId();

            if (id != null) {
                StudentForm form = new StudentForm(id);

                form.addWindowListener(
                        new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosed(
                                    java.awt.event.WindowEvent e) {
                                loadStudents();
                            }
                        }
                );

                form.setVisible(true);
            }
        });

        deleteButton.addActionListener(e -> deleteStudent());

        loadStudents();
    }

    private Integer getSelectedId() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a student first."
            );
            return null;
        }

        return Integer.parseInt(
                model.getValueAt(row, 0).toString()
        );
    }

    private void loadStudents() {

        model.setRowCount(0);

        try {
            List<Student> students = dao.getAllStudents();

            for (Student s : students) {

                model.addRow(new Object[]{
                        s.getId(),
                        s.getFullName(),
                        s.getEmail(),
                        s.getPhone(),
                        s.getCourse()
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load students: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteStudent() {

        Integer id = getSelectedId();

        if (id == null) {
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete student ID " + id + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (dao.deleteStudent(id)) {
                JOptionPane.showMessageDialog(
                        this, "Student deleted successfully."
                );
            } else {
                JOptionPane.showMessageDialog(
                        this, "Student record was not found."
                );
            }

            loadStudents();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Delete failed: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}