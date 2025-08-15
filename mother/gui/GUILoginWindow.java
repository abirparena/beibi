package mother.gui;

import mother.model.*;
import mother.exceptions.*;
import javax.swing.*;
import java.awt.*;

public class GUILoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public GUILoginWindow() {
        setTitle("Crime Reporting System - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals("admin") && password.equals("admin123")) {
                new GUIAdminPanel().setVisible(true);
                dispose();
            } else {
                try {
                    // Reuse existing validation from console version
                    if (username.isEmpty() || password.isEmpty()) {
                        throw new InvalidNIDException("Fields cannot be empty");
                    }
                    new GUIUserMenu(username, password).setVisible(true);
                    dispose();
                } catch (InvalidNIDException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(new JLabel());
        panel.add(loginButton);
        add(panel);
    }
}