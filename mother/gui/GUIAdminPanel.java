package mother.gui;

import mother.model.*;
import mother.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.IOException;

public class GUIAdminPanel extends JFrame {
    private JTable casesTable;
    private JComboBox<String> statusComboBox;
    private JButton refreshButton;
    private JButton updateButton;
    private JButton logoutButton;
    private JButton statsButton;

    public GUIAdminPanel() {
        setTitle("Admin Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title label
        JLabel titleLabel = new JLabel("Case Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table to display cases
        casesTable = new JTable();
        refreshCaseTable();

        JScrollPane scrollPane = new JScrollPane(casesTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Control panel for actions
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Status update components
        JPanel statusPanel = new JPanel();
        statusPanel.add(new JLabel("Update Status:"));
        statusComboBox = new JComboBox<>(new String[]{"Pending", "Investigating", "Resolved"});
        statusPanel.add(statusComboBox);

        updateButton = new JButton("Update Selected");
        updateButton.addActionListener(this::updateCaseStatus);

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshCaseTable());

        statsButton = new JButton("View Statistics");
        statsButton.addActionListener(this::showStatistics);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this::logoutAdmin);

        controlPanel.add(statusPanel);
        controlPanel.add(updateButton);
        controlPanel.add(refreshButton);
        controlPanel.add(statsButton);
        controlPanel.add(logoutButton);

        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void refreshCaseTable() {
        try {
            ArrayList<Case> cases = FileHandler.loadCases();
            String[] columnNames = {"Case ID", "Reporter", "Victim", "Age", "Platform", "Status", "Submitted"};

            Object[][] data = new Object[cases.size()][7];
            for (int i = 0; i < cases.size(); i++) {
                Case c = cases.get(i);
                data[i][0] = c.getCaseId();
                data[i][1] = c.getReporterName();
                data[i][2] = c.getVictimName();
                data[i][3] = c.getVictimAge();
                data[i][4] = c.getPlatform();
                data[i][5] = c.getStatus();
                data[i][6] = c.getSubmissionTime().toString();
            }

            casesTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table non-editable
                }
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading cases: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCaseStatus(ActionEvent e) {
        int selectedRow = casesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a case first!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String caseId = (String) casesTable.getValueAt(selectedRow, 0);
            String newStatus = (String) statusComboBox.getSelectedItem();

            ArrayList<Case> cases = FileHandler.loadCases();
            for (Case c : cases) {
                if (c.getCaseId().equals(caseId)) {
                    c.setStatus(newStatus);
                    break;
                }
            }

            FileHandler.saveCases(cases);
            refreshCaseTable();
            JOptionPane.showMessageDialog(this, "Status updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error updating case: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showStatistics(ActionEvent e) {
        try {
            ArrayList<Case> cases = FileHandler.loadCases();
            int pending = 0, investigating = 0, resolved = 0;

            for (Case c : cases) {
                switch (c.getStatus()) {
                    case "Pending": pending++; break;
                    case "Investigating": investigating++; break;
                    case "Resolved": resolved++; break;
                }
            }

            // Create statistics dialog
            JDialog statsDialog = new JDialog(this, "Case Statistics", true);
            statsDialog.setSize(400, 300);
            statsDialog.setLayout(new BorderLayout());

            JPanel statsPanel = new JPanel(new GridLayout(4, 1));
            statsPanel.add(new JLabel("Total Cases: " + cases.size(), JLabel.CENTER));
            statsPanel.add(new JLabel("Pending: " + pending, JLabel.CENTER));
            statsPanel.add(new JLabel("Investigating: " + investigating, JLabel.CENTER));
            statsPanel.add(new JLabel("Resolved: " + resolved, JLabel.CENTER));

            statsDialog.add(statsPanel, BorderLayout.CENTER);

            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(ev -> statsDialog.dispose());
            statsDialog.add(closeButton, BorderLayout.SOUTH);

            statsDialog.setLocationRelativeTo(this);
            statsDialog.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading statistics: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logoutAdmin(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?", "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new GUILoginWindow().setVisible(true);
        }
    }
/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUIAdminPanel adminPanel = new GUIAdminPanel();
            adminPanel.setVisible(true);
        });
    }*/
}
