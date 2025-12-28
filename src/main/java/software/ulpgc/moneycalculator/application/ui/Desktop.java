package software.ulpgc.moneycalculator.application.ui;

import javax.swing.*;
import java.awt.*;

public class Desktop extends JFrame {
    private final JPanel mainPanel;
    private final JPanel subMenuPanel;


    public Desktop() {
        this.setTitle("Money Calculator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        this.subMenuPanel = subMenuPanel();
        this.getContentPane().add(sidePanel(), BorderLayout.WEST);
        mainPanel = new JPanel(new CardLayout());
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    private JPanel subMenuPanel() {
        GridLayout layout = new GridLayout();
        layout.setColumns(1);
        layout.setRows(4);
        layout.setVgap(50);
        JPanel subMenuPanel = new JPanel(layout);
        subMenuPanel.setBackground(Color.GRAY);
        return subMenuPanel;
    }

    private JPanel sidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(250,1800));
        sidePanel.setBackground(Color.GRAY);
        sidePanel.add(titlePanel(), BorderLayout.NORTH);
        sidePanel.add(this.subMenuPanel,  BorderLayout.SOUTH);
        return sidePanel;
    }

    private JPanel titlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.GRAY);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(80,0,50,0));
        titlePanel.add(titleLabel());
        return titlePanel;
    }

    private JLabel titleLabel() {
        JLabel titleLabel = new JLabel("Money Calculator");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        return titleLabel;
    }

    public void addCommand(String name, CommandPanel commandPanel, boolean show) {
        this.mainPanel.add(commandPanel, name);
        this.subMenuPanel.add(commandPanelButton(name));
        if (show) ((CardLayout) this.mainPanel.getLayout()).show(this.mainPanel, name);
        this.revalidate();
        this.repaint();
    }

    private JButton commandPanelButton(String name) {
        JButton button = new JButton(name);
        button.setFocusPainted(false);
        button.setBorder(null);
        button.setFont(new Font("Inter", Font.BOLD, 16));
        button.setBackground(Color.GRAY);
        button.setForeground(Color.DARK_GRAY);
        button.putClientProperty("JButton.hoverBackground", Color.LIGHT_GRAY);

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(_ -> {
            ((CardLayout) this.mainPanel.getLayout()).show(this.mainPanel, name);
            revalidate();
            repaint();
        });
        return button;
    }

    private Runnable onClose;
    public void onClose(Runnable func) {
        this.onClose = func;
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        onClose.run();
    }
}
