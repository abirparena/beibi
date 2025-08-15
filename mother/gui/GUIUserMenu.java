package mother.gui;

import mother.model.*;
import mother.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUIUserMenu extends JFrame {
    public GUIUserMenu(String username, String nid) {
        setTitle("User Dashboard - " + username);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Reuse existing FileHandler to load cases
        ArrayList<Case> cases = FileHandler.loadCases();
        DefaultListModel<String> caseListModel = new DefaultListModel<>();

        for (Case c : cases) {
            if (c.getReporterNid().equals(nid)) {
                caseListModel.addElement(c.getCaseId() + " - " + c.getStatus());
            }
        }

        JList<String> caseList = new JList<>(caseListModel);
        panel.add(new JScrollPane(caseList), BorderLayout.CENTER);

        JButton newCaseButton = new JButton("File New Case");
        newCaseButton.addActionListener(e -> {
            new GUIFileCaseWindow(username, nid).setVisible(true);
            dispose();
        });

        panel.add(newCaseButton, BorderLayout.SOUTH);
        add(panel);
    }
}