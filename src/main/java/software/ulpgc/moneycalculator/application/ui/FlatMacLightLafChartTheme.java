package software.ulpgc.moneycalculator.application.ui;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.axis.ValueAxis;

import java.awt.*;

public class FlatMacLightLafChartTheme {
    private static final Color ACCENT_BLUE = Color.decode("#0A84FF");
    private static final Color ACCENT_GREEN = Color.decode("#34C759");
    private static final Color ACCENT_ORANGE = Color.decode("#FF9F0A");

    // Neutral colors
    private static final Color BACKGROUND_LIGHT_GRAY = Color.decode("#F5F5F7");
    private static final Color PLOT_BACKGROUND_WHITE = Color.WHITE;
    private static final Color GRIDLINE_GRAY = Color.decode("#D1D1D6");
    private static final Color AXIS_LINE_GRAY = Color.decode("#8E8E93");
    private static final Color TEXT_PRIMARY = Color.decode("#1D1D1F");
    private static final Color TEXT_SECONDARY = Color.decode("#6E6E73");

    private static final Font DEFAULT_FONT = new Font("Inter", Font.PLAIN, 12);
    private static final Font TITLE_FONT = new Font("Inter", Font.BOLD, 14);

    /**
     * Apply FlatMacLightLaf theme to the given chart.
     *
     * @param chart JFreeChart instance
     */
    public static void apply(JFreeChart chart) {
        if (chart == null) return;

        // Chart background
        chart.setBackgroundPaint(BACKGROUND_LIGHT_GRAY);
        if (chart.getTitle() != null) {
            chart.getTitle().setPaint(TEXT_PRIMARY);
            chart.getTitle().setFont(TITLE_FONT);
        }

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(PLOT_BACKGROUND_WHITE);
        plot.setDomainGridlinePaint(GRIDLINE_GRAY);
        plot.setRangeGridlinePaint(GRIDLINE_GRAY);
        plot.setOutlinePaint(AXIS_LINE_GRAY);

        // Axes
        ValueAxis domainAxis = plot.getDomainAxis();
        ValueAxis rangeAxis = plot.getRangeAxis();
        if (domainAxis != null) {
            domainAxis.setLabelPaint(TEXT_PRIMARY);
            domainAxis.setTickLabelPaint(TEXT_SECONDARY);
            domainAxis.setLabelFont(DEFAULT_FONT);
            domainAxis.setTickLabelFont(DEFAULT_FONT);
        }
        if (rangeAxis != null) {
            rangeAxis.setLabelPaint(TEXT_PRIMARY);
            rangeAxis.setTickLabelPaint(TEXT_SECONDARY);
            rangeAxis.setLabelFont(DEFAULT_FONT);
            rangeAxis.setTickLabelFont(DEFAULT_FONT);
        }

        // Renderer / series colors
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        int seriesCount = plot.getDataset() != null ? plot.getDataset().getSeriesCount() : 0;
        for (int i = 0; i < seriesCount; i++) {
            switch (i % 3) {
                case 0 -> renderer.setSeriesPaint(i, ACCENT_BLUE);
                case 1 -> renderer.setSeriesPaint(i, ACCENT_GREEN);
                case 2 -> renderer.setSeriesPaint(i, ACCENT_ORANGE);
            }
            renderer.setSeriesStroke(i, new BasicStroke(2.0f));
        }
        plot.setRenderer(renderer);
    }
}
