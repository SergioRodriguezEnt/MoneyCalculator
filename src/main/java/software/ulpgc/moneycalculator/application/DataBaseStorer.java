package software.ulpgc.moneycalculator.application;

import software.ulpgc.moneycalculator.architecture.io.Storer;
import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.ExchangeRate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class DataBaseStorer extends Storer {
    private final Connection connection;

    public DataBaseStorer(Connection connection) throws SQLException {
        this.connection = connection;
        this.createTablesIfNotExists();
    }

    private void createTablesIfNotExists() throws SQLException {
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS currencies (code TEXT PRIMARY KEY, name TEXT)");
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS " +
                "exchangeRates (date DATE, fromCode TEXT, toCode TEXT, rate REAL, " +
                "FOREIGN KEY (fromCode) REFERENCES currencies(code), FOREIGN KEY (toCode) REFERENCES currencies(code))");
    }

    @Override
    public List<Currency> loadAll() {
        try {
            return currenciesIn(currencyResultSet());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return List.of();
        }
    }

    private List<Currency> currenciesIn(ResultSet resultSet) {
        return Stream.generate(() -> nextCurrencyIn(resultSet))
                .onClose(() -> close(resultSet))
                .takeWhile(Objects::nonNull)
                .toList();
    }

    private static void close(ResultSet resultSet) {
        try {
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Currency nextCurrencyIn(ResultSet resultSet) {
        try {
            return resultSet.next() ? currencyIn(resultSet) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Currency currencyIn(ResultSet resultSet) throws SQLException {
        return new Currency(resultSet.getString(1), resultSet.getString(2));
    }

    private ResultSet currencyResultSet() throws SQLException {
        return connection.createStatement().executeQuery("SELECT * FROM currencies");
    }

    @Override
    public void saveAll(List<Currency> currencies) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO currencies (code, name) VALUES (?, ?)");
            currencies.forEach(currency -> {
                try {
                    statement.setString(1, currency.code());
                    statement.setString(2, currency.name());
                    statement.addBatch();
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            });
            statement.executeLargeBatch();
            connection.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public boolean hasCurrencyData() {
        try (ResultSet result = currencyResultSet()) {
            return result.next();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public ExchangeRate load(Currency from, Currency to, LocalDate date) {
        return null;
    }

    @Override
    public void save(ExchangeRate exchangeRate) {

    }

    @Override
    public boolean hasExchangeRate(Currency from, Currency to, LocalDate date) {
        return false;
    }
}
