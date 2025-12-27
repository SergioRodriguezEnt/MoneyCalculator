package software.ulpgc.moneycalculator.application;

import software.ulpgc.moneycalculator.architecture.io.Storer;
import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.ExchangeRate;

import java.time.LocalDate;
import java.util.List;

public class MockStore extends Storer {
    @Override
    public List<Currency> loadAll() {
        return List.of(new Currency("AA", "AAA"));
    }

    @Override
    public void saveAll(List<Currency> currencies) {

    }

    @Override
    public boolean hasCurrencyData() {
        return false;
    }

    @Override
    public ExchangeRate load(Currency from, Currency to, LocalDate date) {
        return new ExchangeRate(date, from, to, 1);
    }

    @Override
    public void save(ExchangeRate exchangeRate) {

    }

    @Override
    public boolean hasExchangeRate(Currency from, Currency to, LocalDate date) {
        return false;
    }
}
