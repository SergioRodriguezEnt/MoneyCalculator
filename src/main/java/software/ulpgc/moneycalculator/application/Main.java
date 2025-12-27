package software.ulpgc.moneycalculator.application;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    static void main() {
        Desktop desktop = new Desktop();
        WebServiceLoader webServiceLoader = new WebServiceLoader();
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:calculator.db")) {
            Store store = new Store(webServiceLoader, webServiceLoader, new DataBaseStorer(connection));
            desktop.addCommand("Exchange", new ExchangeCommandPanel(store), true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
