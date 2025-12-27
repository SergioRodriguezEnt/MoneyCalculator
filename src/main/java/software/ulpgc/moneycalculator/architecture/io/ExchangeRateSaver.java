package software.ulpgc.moneycalculator.architecture.io;

import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.ExchangeRate;

import java.time.LocalDate;

public interface ExchangeRateSaver {
    void save(ExchangeRate exchangeRate);
    boolean hasExchangeRate(Currency from, Currency to, LocalDate date);
}
