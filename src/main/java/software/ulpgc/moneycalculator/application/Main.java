package software.ulpgc.moneycalculator.application;

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
            desktop.addCommand("Exchange", new ExchangeCommandPanel(store), true);
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
