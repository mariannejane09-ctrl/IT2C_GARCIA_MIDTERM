
package com.scc.ui;

import com.scc.dao.ScenarioDao;
import com.scc.model.ScenarioEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ScenarioMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final ScenarioDao dao;

    public ScenarioMenu() throws SQLException {
        dao = new ScenarioDao();
    }

    public void start() {

        boolean running = true;

        while (running) {
            displayMenu();

            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> recordPatientTreatment();
                case "2" -> viewPatientRecords();
                case "3" -> {
                    System.out.println("\nExiting system... Thank you!");
                    running = false;
                }
                default -> System.out.println(
                        "\nInvalid choice! Please select 1, 2, or 3.\n"
                );
            }
        }

        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n========================================================");
        System.out.println("             DENTAL CLINIC TREATMENT LOG");
        System.out.println("========================================================");
        System.out.println();
        System.out.println("[1] Record Patient Treatment");
        System.out.println("[2] View Patient Records");
        System.out.println("[3] Exit System");
        System.out.println();
        System.out.println("========================================================");
    }

    private void recordPatientTreatment() {

        System.out.println("\n--- RECORD PATIENT TREATMENT ---\n");

        System.out.print("Enter Patient Name: ");
        String patientName = scanner.nextLine().trim();

        while (patientName.isEmpty()) {
            System.out.print("Patient name cannot be empty. Enter Patient Name: ");
            patientName = scanner.nextLine().trim();
        }

        String treatment;

        while (true) {
            System.out.print(
                    "Enter Treatment (CLEANING / EXTRACTION / FILLING): "
            );

            treatment = scanner.nextLine().trim().toUpperCase();

            if (treatment.equals("CLEANING")
                    || treatment.equals("EXTRACTION")
                    || treatment.equals("FILLING")) {
                break;
            }

            System.out.println(
                    "Invalid treatment! Please enter CLEANING, EXTRACTION, or FILLING."
            );
        }

        double amountDue;

        while (true) {
            System.out.print("Enter Amount Due: ");
            String amountInput = scanner.nextLine().trim();

            try {
                amountDue = Double.parseDouble(amountInput);

                if (Double.isFinite(amountDue) && amountDue >= 0) {
                    break;
                }

                System.out.println(
                        "Invalid amount! Enter a non-negative amount."
                );

            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid input! Please enter a numeric amount."
                );
            }
        }

        String payment;

        while (true) {
            System.out.print(
                    "Enter Payment Status (PAID / PENDING): "
            );

            payment = scanner.nextLine().trim().toUpperCase();

            if (payment.equals("PAID") || payment.equals("PENDING")) {
                break;
            }

            System.out.println(
                    "Invalid payment status! Enter PAID or PENDING."
            );
        }

        try {
            dao.addPatientTreatment(
                    patientName, treatment, amountDue, payment
            );

            System.out.println("\nSUCCESS: Patient treatment recorded!\n");

        } catch (SQLException e) {
            System.out.println(
                    "\nERROR: Unable to record patient treatment."
            );
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private void viewPatientRecords() {

        try {
            List<ScenarioEntity> records = dao.getAllPatientTreatments();

            System.out.println("\n========================================================");
            System.out.println("                  DENTAL PATIENT RECORDS");
            System.out.println("========================================================");

            int idWidth = 4;
            int nameWidth = 14;
            int treatmentWidth = 10;
            int amountWidth = 10;
            int paymentWidth = 7;

            for (ScenarioEntity record : records) {
                idWidth = Math.max(
                        idWidth, String.valueOf(record.id()).length()
                );
                nameWidth = Math.max(
                        nameWidth, record.patientName().length()
                );
                treatmentWidth = Math.max(
                        treatmentWidth, record.treatment().length()
                );
                amountWidth = Math.max(
                        amountWidth,
                        String.format("%.2f", record.amountDue()).length()
                );
                paymentWidth = Math.max(
                        paymentWidth, record.payment().length()
                );
            }

            String border = "+"
                    + "-".repeat(idWidth + 2) + "+"
                    + "-".repeat(nameWidth + 2) + "+"
                    + "-".repeat(treatmentWidth + 2) + "+"
                    + "-".repeat(amountWidth + 2) + "+"
                    + "-".repeat(paymentWidth + 2) + "+";

            System.out.println();
            System.out.println(border);

            System.out.printf(
                    "| %-" + idWidth + "s | %-" + nameWidth + "s | %-" +
                            treatmentWidth + "s | %" + amountWidth +
                            "s | %-" + paymentWidth + "s |%n",
                    "ID", "PATIENT NAME", "TREATMENT",
                    "AMOUNT DUE", "PAYMENT"
            );

            System.out.println(border);

            for (ScenarioEntity record : records) {
                System.out.printf(
                        "| %" + idWidth + "d | %-" + nameWidth + "s | %-" +
                                treatmentWidth + "s | %" + amountWidth +
                                ".2f | %-" + paymentWidth + "s |%n",
                        record.id(),
                        record.patientName(),
                        record.treatment(),
                        record.amountDue(),
                        record.payment()
                );
            }

            System.out.println(border);
            System.out.println(
                    "\nTotal Records Displayed: " + records.size()
            );
            System.out.println();

        } catch (SQLException e) {
            System.out.println(
                    "ERROR: Unable to retrieve patient records."
            );
            System.err.println("Database error: " + e.getMessage());
        }
    }
}
