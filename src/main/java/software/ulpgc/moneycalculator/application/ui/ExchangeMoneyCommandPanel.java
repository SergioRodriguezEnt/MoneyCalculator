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

        setLayout(new BorderLayout());
        DateSupplierCalendar dateSupplierCalendar = new DateSupplierCalendar();
        this.add(dateSupplierCalendar, BorderLayout.NORTH);
        JPanel midPanel = new JPanel(new GridBagLayout());
        this.add(new JPanel(new BorderLayout()).add(midPanel), BorderLayout.CENTER);
        fromSupplierBox = SupplierBox.with(currencies, new Font("Segos UI", Font.PLAIN, 12));
        midPanel.add(fromSupplierBox);
        moneyInputField = moneyInputField();
        midPanel.add(moneyInputField);
        SupplierBox<Currency> toSupplierBox = SupplierBox.with(currencies, new Font("Segos UI", Font.PLAIN, 12));
        midPanel.add(toSupplierBox);
        moneyOutputField = moneyOutputField();
        midPanel.add(moneyOutputField);

        command = new ExchangeMoneyCommand(this::inputMoney,
                dateSupplierCalendar,
                toSupplierBox,
                this::showOutputMoney,
                storer);

        command.execute();
    }

    private void showOutputMoney(Money money) {
        moneyOutputField.setText(Double.toString(Math.round(money.amount()*1000)/1000.0));
        updateSize(moneyOutputField);
    }

    private void updateSize(JTextField textField) {
        int newWidth = (textField.getText().length() * 10)+10;
        textField.setPreferredSize(new Dimension(Math.min(newWidth, 300), textField.getPreferredSize().height));
        revalidate();
    }

    private Money inputMoney() {
        String moneyInput = moneyInputField.getText().replace(" ", "").replace(",", ".");
        if (moneyInput.isBlank()) {return new Money(0.0, fromSupplierBox.get());}
        return new Money(Double.parseDouble(moneyInput), fromSupplierBox.get());
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
