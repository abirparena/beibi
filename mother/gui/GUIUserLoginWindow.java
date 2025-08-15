package mother.gui;

import mother.exceptions.InvalidNIDException;
import javax.swing.*;
import java.awt.*;

public class GUIUserLoginWindow extends JFrame {
    public GUIUserLoginWindow() {
        setTitle("User Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("NID (10 digits):"));
        JTextField nidField = new JTextField();
        panel.add(nidField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            try {
                if (nidField.getText().length() != 10 || !nidField.getText().matches("\\d+")) {
                    throw new InvalidNIDException("NID must be 10 digits");
                }
                new GUIUserMenu(usernameField.getText(), nidField.getText()).setVisible(true);
                dispose();
            } catch (InvalidNIDException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        panel.add(new JLabel());
        panel.add(loginButton);
        add(panel);
    }
}