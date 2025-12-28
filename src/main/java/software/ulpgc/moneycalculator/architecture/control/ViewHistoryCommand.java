package software.ulpgc.moneycalculator.architecture.control;

import software.ulpgc.moneycalculator.architecture.io.ExchangeRateLoader;
import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.DateGranularity;
import software.ulpgc.moneycalculator.architecture.model.ExchangeRate;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public record ViewHistoryCommand(Supplier<Currency> fromCurrencySupplier,
                                 Supplier<LocalDate> dateSupplier,
                                 Supplier<DateGranularity> dateGranularitySupplier,
                                 Supplier<Currency> toCurrencySupplier,
                                 BiConsumer<List<ExchangeRate>, DateGranularity> historicConsumer,
                                 ExchangeRateLoader exchangeRateLoader) implements Command {

    @Override
    public void execute() {
        Stream<LocalDate> dates = dateRangeStream(dateSupplier.get(), LocalDate.now().minusDays(1), dateGranularitySupplier.get());
        historicConsumer.accept(dates.map(this::exchangeRateFrom).toList(), dateGranularitySupplier.get());
    }

    private Stream<LocalDate> dateRangeStream(LocalDate from, LocalDate to, DateGranularity granularity) {
        from = normalize(from, granularity);
        LocalDate finalTo = normalize(to, granularity);

        if (from.isAfter(finalTo)) {
            return Stream.empty();
        }
        return Stream.iterate(from,
                date -> !date.isAfter(finalTo),
                date -> increment(date, granularity));
    }

    private LocalDate normalize(LocalDate date, DateGranularity granularity) {
        return switch (granularity) {
            case DAY    -> date;
            case MONTH  -> date.withDayOfMonth(1);
            case YEAR   -> date.withDayOfYear(1);
            case DECADE -> date.withYear((date.getYear()/10) * 10).withDayOfYear(1);
        };
    }

    private LocalDate increment(LocalDate date, DateGranularity granularity) {
        return switch (granularity) {
            case DAY    -> date.plusDays(1);
            case MONTH  -> date.plusMonths(1);
            case YEAR   -> date.plusYears(1);
            case DECADE -> date.plusYears(10);
        };
    }

    private ExchangeRate exchangeRateFrom(LocalDate date) {
        return exchangeRateLoader.load(fromCurrencySupplier.get(), toCurrencySupplier.get(), date);
    }
}
