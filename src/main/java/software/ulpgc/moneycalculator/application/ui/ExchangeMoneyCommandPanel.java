package software.ulpgc.moneycalculator.application.ui;

import software.ulpgc.moneycalculator.architecture.control.ExchangeMoneyCommand;
import software.ulpgc.moneycalculator.architecture.io.Storer;
import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.Money;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;


public class ExchangeMoneyCommandPanel extends CommandPanel {
    private final SupplierBox<Currency> fromSupplierBox;
    private final JTextField moneyInputField;
    private final JTextField moneyOutputField;
    private final ExchangeMoneyCommand command;

    public ExchangeMoneyCommandPanel(Storer storer) {
        super(storer);
        List<Currency> currencies = storer.loadAll();
        
        this.setLayout(new FlowLayout());

        fromSupplierBox = SupplierBox.with(currencies, new Font("Segos UI", Font.PLAIN, 12));
        this.add(fromSupplierBox);
        moneyInputField = moneyInputField();
        this.add(moneyInputField);
        DateSupplierCalendar dateSupplierCalendar = new DateSupplierCalendar();
        this.add(dateSupplierCalendar);
        SupplierBox<Currency> toSupplierBox = SupplierBox.with(currencies, new Font("Segos UI", Font.PLAIN, 12));
        this.add(toSupplierBox);
        moneyOutputField = moneyOutputField();
        this.add(moneyOutputField);

        command = new ExchangeMoneyCommand(this::inputMoney,
                dateSupplierCalendar,
                toSupplierBox,
                this::showOutputMoney,
                storer);

        command.execute();
    }

    private void showOutputMoney(Money money) {
        moneyOutputField.setText(Double.toString(money.amount()));
    }

    private Money inputMoney() {
        return new Money(Double.parseDouble(moneyInputField.getText()), fromSupplierBox.get());
    }

    private JTextField moneyOutputField() {
        JTextField field = new JTextField("0");
        field.setFont(new Font("Segoe UI", Font.BOLD, 14));
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        field.setEditable(false);
        return field;
    }

    private JTextField moneyInputField() {
        JTextField field = new JTextField("100");
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                command.execute();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                command.execute();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                command.execute();
            }
        });
        return field;
    }
}
