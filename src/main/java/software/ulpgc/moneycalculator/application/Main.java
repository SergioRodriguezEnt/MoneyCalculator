package software.ulpgc.moneycalculator.application;

import software.ulpgc.moneycalculator.application.io.DataBaseStorer;
import software.ulpgc.moneycalculator.application.ui.Desktop;
import software.ulpgc.moneycalculator.application.ui.ExchangeMoneyCommandPanel;
import software.ulpgc.moneycalculator.application.ui.ViewHistoryCommandPanel;
import software.ulpgc.moneycalculator.architecture.io.Store;
import software.ulpgc.moneycalculator.application.io.WebServiceLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    static void main() {
        Desktop desktop = new Desktop();
        WebServiceLoader webServiceLoader = new WebServiceLoader();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:calculator.db");
            Store store = new Store(webServiceLoader, webServiceLoader, new DataBaseStorer(connection));
            desktop.addCommand("Exchange money", new ExchangeMoneyCommandPanel(store), true);
            desktop.addCommand("View history", new ViewHistoryCommandPanel(store), false);
            desktop.onClose(() -> {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
