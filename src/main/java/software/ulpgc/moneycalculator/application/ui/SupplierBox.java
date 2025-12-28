package software.ulpgc.moneycalculator.application.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

public class SupplierBox<T> extends JComboBox<T> implements Supplier<T> {
    private SupplierBox(T[] items) {
        super(items);
    }

    public static <T> SupplierBox<T> with(List<T> items, Font font) {
        @SuppressWarnings("unchecked") SupplierBox<T> box = new SupplierBox<>((T[])items.toArray());
        box.setFont(font);
        return box;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get() {
        return (T) this.getSelectedItem();
    }
}
