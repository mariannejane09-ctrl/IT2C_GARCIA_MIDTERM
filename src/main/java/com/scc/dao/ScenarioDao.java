
package com.scc.dao;

import com.scc.model.ScenarioEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ScenarioDao {

    public ScenarioDao() throws SQLException {
        createTable();
    }

    // Create the patient treatment table if it does not exist
    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS patient_treatments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patient_name TEXT NOT NULL, " +
                "treatment TEXT NOT NULL CHECK " +
                "(treatment IN ('CLEANING', 'EXTRACTION', 'FILLING')), " +
                "amount_due REAL NOT NULL CHECK (amount_due >= 0), " +
                "payment TEXT NOT NULL CHECK " +
                "(payment IN ('PAID', 'PENDING'))" +
                ")";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
        }
    }

    // Record a new patient treatment
    public void addPatientTreatment(
            String patientName,
            String treatment,
            double amountDue,
            String payment
    ) throws SQLException {

        String sql = "INSERT INTO patient_treatments " +
                "(patient_name, treatment, amount_due, payment) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patientName);
            pstmt.setString(2, treatment);
            pstmt.setDouble(3, amountDue);
            pstmt.setString(4, payment);

            pstmt.executeUpdate();
        }
    }

    // Retrieve all patient treatment records
    public List<ScenarioEntity> getAllPatientTreatments()
            throws SQLException {

        List<ScenarioEntity> records = new ArrayList<>();

        String sql = "SELECT id, patient_name, treatment, " +
                "amount_due, payment " +
                "FROM patient_treatments ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ScenarioEntity record = new ScenarioEntity(
                        rs.getInt("id"),
                        rs.getString("patient_name"),
                        rs.getString("treatment"),
                        rs.getDouble("amount_due"),
                        rs.getString("payment")
                );

                records.add(record);
            }
        }

        return records;
    }
}
