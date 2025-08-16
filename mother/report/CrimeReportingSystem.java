package mother.report;

import mother.model.*;
import mother.exceptions.*;
import mother.util.*;
import java.util.*;
import java.io.*;


public class CrimeReportingSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    private static ICasePriorityCalculator priorityCalculator = new BasicCasePriorityCalculator();

    // Helper method to find a zone by name
    private static CrimeZone findZone(ArrayList<CrimeZone> zones, String name) {
        for (CrimeZone zone : zones) {
            if (zone.getZoneName().equals(name)) {
                return zone;
            }
        }
        return null;
    }

    public static void start() {
        showMainMenu();
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== Crime Reporting System ===");
            System.out.println("1. Crime Rate Reports");
            System.out.println("2. User Case File");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        showCrimeRates();
                        break;
                    case 2:
                        userLogin();
                        break;
                    case 3:
                        adminLogin();
                        break;
                    case 4:
                        System.out.println("Exiting system...");
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private static void showCrimeRates() {
        ArrayList<CrimeZone> zones = FileHandler.loadCrimeData();
        System.out.println("\n--- Crime Rates by Zone ---");
        for (CrimeZone zone : zones) {
            System.out.printf("%-20s: %d incidents%n",
                    zone.getZoneName(), zone.getCrimeCount());
        }
    }

    private static void userLogin() {
        System.out.println("\n=== User Login ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter NID (10 digits): ");
        String nid = scanner.nextLine();

        try {
            if (nid.length() != 10 || !nid.matches("\\d+")) {
                throw new InvalidNIDException("NID must be 10 digits");
            }

            currentUser = new Reporter(username, nid);
            showUserMenu();
        } catch (InvalidNIDException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void showUserMenu() {
        while (true) {
            System.out.println("\n=== User Menu ===");
            System.out.println("Logged in as: " + currentUser.getUsername());
            System.out.println("1. Check My Cases");
            System.out.println("2. File New Case");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    checkUserCases();
                    break;
                case 2:
                    fileNewCase();
                    break;
                case 3:
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void checkUserCases() {
        ArrayList<Case> cases = FileHandler.loadCases();
        boolean found = false;

        System.out.println("\n=== Your Cases ===");
        for (Case caseObj : cases) {
            if (caseObj.getReporterNid().equals(currentUser.getNid())) {
                System.out.println("Case ID: " + caseObj.getCaseId());
                System.out.println("Victim: " + caseObj.getVictimName());
                System.out.println("Status: " + caseObj.getStatus());
                System.out.println("Submitted: " + caseObj.getSubmissionTime());
                System.out.println("-------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No cases found for this user.");
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static void fileNewCase() {
        System.out.println("\n=== File New Case ===");

        System.out.print("Victim Name: ");
        String victimName = scanner.nextLine();

        System.out.print("Victim Age: ");
        String victimAge = scanner.nextLine();

        System.out.println("Contact info(Email/ Phone number etc.): ");
        String contactinfo = scanner.nextLine();


        System.out.print("Platform (Facebook/Twitter/etc): ");
        String platform = scanner.nextLine();

        System.out.println("Incident Details (press Enter twice to finish):");
        String incidentDetails = "";
        String line;
        while (!(line = scanner.nextLine()).isEmpty()) {
            incidentDetails += line + ("\n");
        }

        System.out.println("Evidence Details (press Enter twice to finish):");
        String evidence = "";
        String line1;
        while (!(line1 = scanner.nextLine()).isEmpty()) {
            evidence += line1 += ("\n");
        }

        Case newCase = new Case(currentUser.getUsername(), currentUser.getNid(), victimName,
                victimAge, contactinfo, platform, incidentDetails.toString(), evidence.toString()
        );

        ArrayList<Case> cases = FileHandler.loadCases();
        cases.add(newCase);

        try {
            FileHandler.saveCases(cases);
            System.out.println("\nCase submitted successfully!");
            System.out.println("Your Case ID: " + newCase.getCaseId());
        } catch (IOException e) {
            System.out.println("Error saving case: " + e.getMessage());
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static void adminLogin() {
        System.out.println("\n=== Admin Login ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (username.equals("boltu") && password.equals("boltuboltu")) {
            currentUser = new Admin(username, "ADMIN001");
            showAdminMenu();
        } else {
            System.out.println("Invalid credentials!");
        }
    }

    private static void showAdminMenu() {
        while (true) {
            System.out.println("\n=== Admin Panel ===");
            System.out.println("1. Manage Crime Data");
            System.out.println("2. Manage Cases");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    manageCrimeData();
                    break;
                case 2:
                    manageCases();
                    break;
                case 3:
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void manageCrimeData() {
        ArrayList<CrimeZone> crimeData = FileHandler.loadCrimeData();

        while (true) {
            System.out.println("\n=== Crime Data Management ===");
            System.out.println("Current Crime Rates:");

            for (int i = 0; i < crimeData.size(); i++) {
                CrimeZone zone = crimeData.get(i);
                System.out.printf("%d. %-20s: %d%%\n", i+1, zone.getZoneName(), zone.getCrimeCount());
            }

            System.out.println("\n1. Add New Area");
            System.out.println("2. Update Existing Area");
            System.out.println("3. Back to Admin Menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.print("Enter new area name: ");
                String area = scanner.nextLine();

                System.out.print("Enter crime rate: ");
                int rate = scanner.nextInt();
                scanner.nextLine();

                crimeData.add(new CrimeZone(area, rate));
                System.out.println("Area added successfully!");
            }
            else if (choice == 2) {
                System.out.print("Enter area number to update: ");
                int areaNum = scanner.nextInt();
                scanner.nextLine();

                if (areaNum < 1 || areaNum > crimeData.size()) {
                    System.out.println("Invalid area number!");
                    continue;
                }

                CrimeZone zone = crimeData.get(areaNum-1);
                System.out.print("Enter new crime rate for " + zone.getZoneName() + ": ");
                int newRate = scanner.nextInt();
                scanner.nextLine();

                zone.setCrimeCount(newRate);
                System.out.println("Crime rate updated!");
            }
            else if (choice == 3) {
                try {
                    FileHandler.saveCrimeData(crimeData);
                } catch (IOException e) {
                    System.out.println("Error saving crime data: " + e.getMessage());
                }
                return;
            }
        }
    }

    private static void manageCases() {
        ArrayList<Case> cases = FileHandler.loadCases();

        while (true) {
            System.out.println("\n=== Case Management ===");
            System.out.println("ID\tStatus\t\tVictim");
            System.out.println("--------------------------------");

            for (int i = 0; i < cases.size(); i++) {
                Case c = cases.get(i);
                System.out.printf("%d. %s\t%-12s\t%s\n",
                        i+1, c.getCaseId(), c.getStatus(), c.getVictimName());
            }

            System.out.println("\n1. Update Case Status");
            System.out.println("2. Back to Admin Menu");
            System.out.println("3. View Case Details");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.print("Enter case number to update: ");
                int caseNum = scanner.nextInt();
                scanner.nextLine();

                if (caseNum < 1 || caseNum > cases.size()) {
                    System.out.println("Invalid case number!");
                    continue;
                }

                Case selectedCase = cases.get(caseNum-1);
                System.out.println("\nCurrent status: " + selectedCase.getStatus());
                System.out.println("1. Pending");
                System.out.println("2. Investigating");
                System.out.println("3. Resolved");
                System.out.print("Select new status: ");

                int statusChoice = scanner.nextInt();
                scanner.nextLine();

                switch (statusChoice) {
                    case 1: selectedCase.setStatus("Pending"); break;
                    case 2: selectedCase.setStatus("Investigating"); break;
                    case 3: selectedCase.setStatus("Resolved"); break;
                    default: System.out.println("Invalid choice!");
                }

                try {
                    FileHandler.saveCases(cases);
                    System.out.println("Case status updated!");
                } catch (IOException e) {
                    System.out.println("Error saving cases: " + e.getMessage());
                }
            }
            else if (choice == 2) {
                return;
            }

            else if (choice == 3){
                System.out.print("Enter case number to view: ");
                int caseNum = scanner.nextInt();
                scanner.nextLine();

                if (caseNum < 1 || caseNum > cases.size()) {
                    System.out.println("Invalid case number!");
                    continue;
                }

                Case selectedCase = cases.get(caseNum-1);
                printCaseDetails(selectedCase); // Helper method
            }
            else if (choice == 2) {
                // Existing status update code...
            }
            else if (choice == 3) {
                return;
            }
        }
    }

    private static void printCaseDetails(Case caseObj) {
        System.out.println("\n=== Case Details ===");
        System.out.println("Case ID: " + caseObj.getCaseId());
        System.out.println("Reporter: " + caseObj.getReporterName());
        System.out.println("Victim: " + caseObj.getVictimName() + " (Age: " + caseObj.getVictimAge() + ")");
        System.out.println("Conract info: " + caseObj.getContactinfo());
        System.out.println("Platform: " + caseObj.getPlatform());
        System.out.println("Status: " + caseObj.getStatus());
        System.out.println("Submitted: " + caseObj.getSubmissionTime());
        System.out.println("\n-- Incident Details --");
        System.out.println(caseObj.getIncidentDetails());
        System.out.println("\n-- Evidence --");
        System.out.println(caseObj.getEvidence());
        System.out.println("----------------------");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
}

