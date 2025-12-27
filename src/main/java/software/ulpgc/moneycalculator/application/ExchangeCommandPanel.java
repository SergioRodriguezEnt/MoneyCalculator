package software.ulpgc.moneycalculator.application;

import com.toedter.calendar.JCalendar;
import software.ulpgc.moneycalculator.architecture.control.ExchangeMoneyCommand;
import software.ulpgc.moneycalculator.architecture.io.Storer;
import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.Money;
import software.ulpgc.moneycalculator.architecture.ui.CommandPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;


public class ExchangeCommandPanel extends CommandPanel {
    private final JComboBox<Currency> fromCurrencyBox;
    private final JComboBox<Currency> toCurrencyBox;
    private final JTextField moneyInputField;
    private final JCalendar dateSelectorCalendar;
    private final JTextField moneyOutputField;
    private final List<Currency> currencies;

    public ExchangeCommandPanel(Storer storer) {
        super(storer);
        currencies = storer.loadAll();
        
        this.setLayout(new FlowLayout());

        fromCurrencyBox = currencyComboBox();
        this.add(fromCurrencyBox);
        moneyInputField = moneyInputField();
        this.add(moneyInputField);
        dateSelectorCalendar = dateSelector();
        this.add(dateSelectorCalendar);
        toCurrencyBox = currencyComboBox();
        this.add(toCurrencyBox);
        moneyOutputField = moneyOutputField();
        this.add(moneyOutputField);

        ExchangeMoneyCommand command = new ExchangeMoneyCommand(this::inputMoney,
                this::selectedDate,
                this::outputCurrency,
                this::showOutputMoney,
                storer);

        command.execute();
    }

    private void showOutputMoney(Money money) {
        moneyOutputField.setText(Double.toString(money.amount()));
    }

    private Currency outputCurrency() {
        return (Currency) toCurrencyBox.getSelectedItem();
    }

    private LocalDate selectedDate() {
        return LocalDate.ofInstant(dateSelectorCalendar.getDate().toInstant(), ZoneOffset.UTC);
    }

    private Money inputMoney() {
        return new Money(Double.parseDouble(moneyInputField.getText()), inputCurrency());
    }

    private Currency inputCurrency() {
        return (Currency) fromCurrencyBox.getSelectedItem();
    }

    private JTextField moneyOutputField() {
        JTextField field = new JTextField("0");
        field.setFont(new Font("Segoe UI", Font.BOLD, 14));
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        field.setEditable(false);
        return field;
    }

    private JCalendar dateSelector() {
        return new JCalendar(Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)));
    }

    private JTextField moneyInputField() {
        JTextField field = new JTextField("100");
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return field;
    }

    private JComboBox<Currency> currencyComboBox() {
        JComboBox<Currency> comboBox = new JComboBox<>(currencies.toArray(new Currency[0]));
        comboBox.setFont(new Font("Segos UI", Font.PLAIN, 12));
        return comboBox;
    }
}
