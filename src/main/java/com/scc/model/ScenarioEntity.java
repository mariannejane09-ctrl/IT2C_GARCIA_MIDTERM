
package com.scc.model;

public record ScenarioEntity(
        int id,
        String patientName,
        String treatment,
        double amountDue,
        String payment
) {
}
