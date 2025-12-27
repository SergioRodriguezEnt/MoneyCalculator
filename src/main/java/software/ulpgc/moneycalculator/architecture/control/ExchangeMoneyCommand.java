package software.ulpgc.moneycalculator.architecture.control;

import software.ulpgc.moneycalculator.architecture.io.ExchangeRateLoader;
import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.ExchangeRate;
import software.ulpgc.moneycalculator.architecture.model.Money;

import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record ExchangeMoneyCommand(Supplier<Money> fromMoneySupplier,
                                   Supplier<LocalDate> dateSupplier,
                                   Supplier<Currency> toCurrencySupplier,
                                   Consumer<Money> toMoneyConsumer,
                                   ExchangeRateLoader exchangeRateLoader) implements Command {

    @Override
    public void execute() {
        Money fromMoney = fromMoneySupplier.get();
        Currency toCurrency = toCurrencySupplier.get();

        ExchangeRate exchangeRate = exchangeRateLoader.load(fromMoney.currency(), toCurrency, dateSupplier.get());

        toMoneyConsumer.accept(new Money(fromMoney.amount() * exchangeRate.rate(), toCurrency));
    }
}
