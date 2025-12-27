package software.ulpgc.moneycalculator.application;

import software.ulpgc.moneycalculator.architecture.ui.CommandPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Desktop extends JFrame {
    private final JPanel mainPanel;
    private final JPanel subMenuPanel;


    public Desktop() {
        this.setTitle("Money Calculator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 600);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        this.subMenuPanel = subMenuPanel();
        this.getContentPane().add(sidePanel(), BorderLayout.WEST);
        mainPanel = new JPanel(new CardLayout());
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    private JPanel subMenuPanel() {
        JPanel subMenuPanel = new JPanel(new GridLayout());
        subMenuPanel.setBackground(Color.GRAY);
        return subMenuPanel;
    }

    private JPanel sidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(250,600));
        sidePanel.setBackground(Color.GRAY);
        sidePanel.add(titlePanel());
        sidePanel.add(this.subMenuPanel);
        return sidePanel;
    }

    private JPanel titlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.GRAY);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(80,0,20,0));
        titlePanel.add(titleLabel());
        return titlePanel;
    }

    private JLabel titleLabel() {
        JLabel titleLabel = new JLabel("Money Calculator");
        titleLabel.setFont(new Font("Segos UI", Font.PLAIN, 22));
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
        button.setBackground(Color.GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(null);
        button.setFont(new Font("Segos UI", Font.BOLD, 16));

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.DARK_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.GRAY);
            }
        });

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
