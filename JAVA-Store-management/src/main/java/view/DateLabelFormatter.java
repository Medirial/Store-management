package view;
import java.text.ParseException;
import javax.swing.text.DateFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateLabelFormatter extends DateFormatter {
    public DateLabelFormatter() {
        super(new SimpleDateFormat("dd/MM/yyyy"));
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value instanceof Date) {
            return super.valueToString(value);
        }
        return "";
    }
}
