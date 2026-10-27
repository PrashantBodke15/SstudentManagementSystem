package com.student.ui;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard() {

        setTitle("Student Management System");
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel heading = new JLabel(
                "STUDENT MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 24));

        JButton registerButton = new JButton("Student Registration");
        JButton listButton = new JButton("Student List");
        JButton detailsButton = new JButton("Student Details");
        JButton editButton = new JButton("Edit Student");
        JButton exitButton = new JButton("Exit");

        JPanel menu = new JPanel(new GridLayout(5, 1, 15, 15));

        menu.setBorder(
                BorderFactory.createEmptyBorder(20, 100, 30, 100)
        );

        menu.add(registerButton);
        menu.add(listButton);
        menu.add(detailsButton);
        menu.add(editButton);
        menu.add(exitButton);

        setLayout(new BorderLayout(10, 10));

        add(heading, BorderLayout.NORTH);
        add(menu, BorderLayout.CENTER);

        registerButton.addActionListener(e ->
                new StudentForm().setVisible(true)
        );

        listButton.addActionListener(e ->
                new StudentList().setVisible(true)
        );

        detailsButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(
                    this, "Enter Student ID:"
            );

            if (input != null && !input.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(input.trim());
                    new StudentDetails(id).setVisible(true);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            this, "Enter a valid numeric ID."
                    );
                }
            }
        });

        editButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(
                    this, "Enter Student ID to edit:"
            );

            if (input != null && !input.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(input.trim());
                    new StudentForm(id).setVisible(true);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            this, "Enter a valid numeric ID."
                    );
                }
            }
        });

        exitButton.addActionListener(e -> dispose());
    }
}