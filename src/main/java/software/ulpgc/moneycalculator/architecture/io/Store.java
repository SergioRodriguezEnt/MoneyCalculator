package software.ulpgc.moneycalculator.architecture.io;

import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.ExchangeRate;

import java.time.LocalDate;
import java.util.List;

public class Store extends Storer {
    private final CurrencyLoader externalCurrencyLoader;
    private final ExchangeRateLoader externalExchangeRateLoader;
    private final Storer internalStorer;

    public Store(CurrencyLoader externalCurrencyLoader, ExchangeRateLoader externalExchangeRateLoader, Storer internalStorer) {
        this.externalCurrencyLoader = externalCurrencyLoader;
        this.externalExchangeRateLoader = externalExchangeRateLoader;
        this.internalStorer = internalStorer;
    }

    @Override
    public List<Currency> loadAll() {
        if (hasCurrencyData()) {
            return internalStorer.loadAll();
        }
        List<Currency> currencies = externalCurrencyLoader.loadAll();
        saveAll(currencies);
        return currencies;
    }

    @Override
    public void saveAll(List<Currency> currencies) {
        internalStorer.saveAll(currencies);
    }

    @Override
    public boolean hasCurrencyData() {
        return internalStorer.hasCurrencyData();
    }

    @Override
    public ExchangeRate load(Currency from, Currency to, LocalDate date) {
        if (hasExchangeRate(from, to, date)) {
            return internalStorer.load(from, to, date);
        }
        ExchangeRate exchangeRate = externalExchangeRateLoader.load(from, to, date);
        save(exchangeRate);
        return exchangeRate;
    }

    @Override
    public void save(ExchangeRate exchangeRate) {
        internalStorer.save(exchangeRate);
    }

    @Override
    public boolean hasExchangeRate(Currency from, Currency to, LocalDate date) {
        return internalStorer.hasExchangeRate(from, to, date);
    }
}
