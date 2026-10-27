package com.student;

import com.student.database.DBConnection;
import com.student.ui.Dashboard;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try (Connection con = DBConnection.getConnection()) {

                if (con.isValid(3)) {
                    System.out.println(
                            "MySQL database connected successfully!"
                    );
                }

            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                        null,
                        "Database connection failed.\n"
                                + "Check MySQL service, database name, "
                                + "username and password.\n\n"
                                + ex.getMessage(),
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            new Dashboard().setVisible(true);
        });
    }
}