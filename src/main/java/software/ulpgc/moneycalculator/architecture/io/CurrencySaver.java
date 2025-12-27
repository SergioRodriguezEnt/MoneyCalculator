package software.ulpgc.moneycalculator.architecture.io;

import software.ulpgc.moneycalculator.architecture.model.Currency;

import java.util.List;

public interface CurrencySaver {
    void saveAll(List<Currency> currencies);
    boolean hasCurrencyData();
}
