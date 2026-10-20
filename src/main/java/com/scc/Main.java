
package com.scc;

import com.scc.ui.ScenarioMenu;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try {
            ScenarioMenu menu = new ScenarioMenu();
            menu.start();

        } catch (SQLException e) {
            System.err.println(
                    "ERROR: Unable to initialize the dental clinic system."
            );
            System.err.println("Database error: " + e.getMessage());
        }
    }
}
