package software.ulpgc.moneycalculator.application.ui;

import software.ulpgc.moneycalculator.architecture.io.Storer;

import javax.swing.*;

public abstract class CommandPanel extends JPanel {
    protected final Storer storer;
    public CommandPanel(Storer storer) {
        this.storer = storer;
    }
}
