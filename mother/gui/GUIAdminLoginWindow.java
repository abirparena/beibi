package mother.gui;

import javax.swing.*;
import java.awt.*;

public class GUIAdminLoginWindow extends JFrame {
    public GUIAdminLoginWindow() {
        setTitle("Admin Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            if (usernameField.getText().equals("admin") &&
                    new String(passwordField.getPassword()).equals("admin123")) {
                new GUIAdminPanel().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }
        });

        panel.add(new JLabel());
        panel.add(loginButton);
        add(panel);
    }
}
