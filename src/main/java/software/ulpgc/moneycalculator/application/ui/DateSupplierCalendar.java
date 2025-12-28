package software.ulpgc.moneycalculator.application.ui;

import com.toedter.calendar.JCalendar;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.function.Supplier;

public class DateSupplierCalendar extends JCalendar implements Supplier<LocalDate> {
    public DateSupplierCalendar() {
        super(Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)));
    }

    @Override
    public LocalDate get() {
        LocalDate selectedDate =  LocalDate.ofInstant(this.getDate().toInstant(), ZoneOffset.UTC);
        if (!selectedDate.isBefore(LocalDate.now())) {
            selectedDate = LocalDate.now().minusDays(1);
        }
        if (selectedDate.isBefore(LocalDate.ofYearDay(2000, 1))) {
            selectedDate = LocalDate.ofYearDay(2000, 1);
        }
        return selectedDate;
    }
}
