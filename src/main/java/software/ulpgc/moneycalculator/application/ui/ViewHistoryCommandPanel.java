package software.ulpgc.moneycalculator.application.ui;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import software.ulpgc.moneycalculator.architecture.control.ViewHistoryCommand;
import software.ulpgc.moneycalculator.architecture.io.Storer;
import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.DateGranularity;
import software.ulpgc.moneycalculator.architecture.model.ExchangeRate;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToIntFunction;

public class ViewHistoryCommandPanel extends CommandPanel {
    private final JPanel chartPanel;
    private final ViewHistoryCommand command;

    public ViewHistoryCommandPanel(Storer storer) {
        super(storer);
        List<Currency> currencies = storer.loadAll();

        this.setLayout(new BorderLayout());
        DateSupplierCalendar dateSupplierCalendar = new DateSupplierCalendar();
        this.add(dateSupplierCalendar,  BorderLayout.NORTH);

        JPanel bufferPanel = new JPanel(new BorderLayout());
        this.add(bufferPanel, BorderLayout.CENTER);

        JPanel midPanel = new JPanel(new GridBagLayout());
        bufferPanel.add(midPanel, BorderLayout.NORTH);
        SupplierBox<Currency> fromSupplierBox = SupplierBox.with(currencies, new Font("Segos UI", Font.PLAIN, 12));
        midPanel.add(fromSupplierBox);
        SupplierBox<Currency> toSupplierBox = SupplierBox.with(currencies, new Font("Segos UI", Font.PLAIN, 12));
        midPanel.add(toSupplierBox);
        SupplierBox<DateGranularity> dateGranularityBox = SupplierBox.with(Arrays.asList(DateGranularity.values()), new Font("Segos UI", Font.PLAIN, 12));
        midPanel.add(dateGranularityBox);

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapper.add(viewChartButton());
        bufferPanel.add(buttonWrapper, BorderLayout.CENTER);

        chartPanel = new JPanel(new BorderLayout());
        bufferPanel.add(chartPanel, BorderLayout.SOUTH);

        command = new ViewHistoryCommand(fromSupplierBox,
                dateSupplierCalendar,
                dateGranularityBox,
                toSupplierBox,
                this::showHistory,
                storer);

        showChart();
    }

    public void showChart() {
        try {
            command.execute();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(chartPanel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton viewChartButton() {
        JButton button = new JButton("View");
        button.addActionListener(_ -> showChart());
        return button;
    }

    private void showHistory(List<ExchangeRate> exchangeRates, DateGranularity granularity) {
        JFreeChart chart = ChartFactory.createXYLineChart("Chart", "time", "rate", collectionFrom(exchangeRates, granularity));
        FlatMacLightLafChartTheme.apply(chart);
        NumberAxis domainAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        domainAxis.setNumberFormatOverride(DecimalFormat.getIntegerInstance());
        NumberAxis rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        rangeAxis.setAutoRange(true);
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setNumberFormatOverride(new DecimalFormat("0.####"));
        chartPanel.removeAll();
        ChartPanel cPanel = new ChartPanel(chart);
        cPanel.setPreferredSize(new Dimension(400, 400));
        chartPanel.add(cPanel, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    private XYSeriesCollection collectionFrom(List<ExchangeRate> exchangeRates, DateGranularity granularity) {
        XYSeriesCollection collection = new XYSeriesCollection();
        collection.addSeries(seriesFrom(exchangeRates, granularity));
        return collection;
    }

    private XYSeries seriesFrom(List<ExchangeRate> exchangeRates, DateGranularity granularity) {
        XYSeries series = new XYSeries("x");
        ToIntFunction<ExchangeRate> timeOf = serializer(granularity);
        exchangeRates.forEach(exchangeRate -> {
            series.add(timeOf.applyAsInt(exchangeRate), exchangeRate.rate());
            System.out.print(timeOf.applyAsInt(exchangeRate));
            System.out.print(" - ");
            System.out.println(exchangeRate.rate());
        });
        return series;
    }

    private ToIntFunction<ExchangeRate> serializer(DateGranularity granularity) {
        return switch (granularity) {
            case DAY -> (exRate) -> exRate.date().getDayOfYear();
            case MONTH -> (exRate) -> exRate.date().getMonthValue();
            case YEAR -> (exRate) -> exRate.date().getYear();
            case DECADE -> (exRate) -> (exRate.date().getYear() / 10) * 10;
        };
    }
}
