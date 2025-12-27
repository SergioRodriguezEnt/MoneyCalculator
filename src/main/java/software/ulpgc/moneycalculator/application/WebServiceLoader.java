package software.ulpgc.moneycalculator.application;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.ulpgc.moneycalculator.architecture.io.CurrencyLoader;
import software.ulpgc.moneycalculator.architecture.io.ExchangeRateLoader;
import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.ExchangeRate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WebServiceLoader implements CurrencyLoader, ExchangeRateLoader {
    private final String ApiCurrenciesUrl;
    private final String ApiHistoricalUrl;

    public WebServiceLoader() {
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(".keys")) {
            String ApiKey = new String(Objects.requireNonNull(is).readAllBytes());
            ApiCurrenciesUrl = "https://api.freecurrencyapi.com/v1/currencies?apikey=API-KEY".replace("API-KEY", ApiKey);
            ApiHistoricalUrl = "https://api.freecurrencyapi.com/v1/historical?apikey=API-KEY&date=FECHA&base_currency=FROM&currencies=TO".replace("API-KEY", ApiKey);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Currency> loadAll() {
        try {
            return readCurrenciesFrom(jsonFrom(ApiCurrenciesUrl));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return List.of();
        }
    }

    private List<Currency> readCurrenciesFrom(JsonObject jsonObject) {
        return jsonObject.get("data").getAsJsonObject().entrySet().stream().map(this::currencyFrom).toList();
    }

    private Currency currencyFrom(Map.Entry<String, JsonElement> entry) {
        return new Currency(entry.getKey(), entry.getValue().getAsJsonObject().get("name").getAsString());
    }

    private JsonObject jsonFrom(String urlString) throws IOException {
        try(InputStream is = URI.create(urlString).toURL().openStream()) {
            return new Gson().fromJson(new String(is.readAllBytes()), JsonObject.class);
        }
    }

    @Override
    public ExchangeRate load(Currency from, Currency to, LocalDate date) {
        try {
            return readExchangeRateFrom(jsonFrom(exchangeRateUrlFor(from, to, date)), date, from, to);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return new ExchangeRate(LocalDate.now(),from, to, 1);
        }
    }

    private String exchangeRateUrlFor(Currency from, Currency to, LocalDate date) {
        return ApiHistoricalUrl.replace("FECHA", date.toString()).replace("FROM", from.code()).replace("TO", to.code());
    }

    private ExchangeRate readExchangeRateFrom(JsonObject jsonObject, LocalDate date, Currency from, Currency to) {
        return new ExchangeRate(date,
                from,
                to,
                jsonObject.get("data").getAsJsonObject().get(date.toString()).getAsJsonObject().get(to.code()).getAsDouble());
    }
}
