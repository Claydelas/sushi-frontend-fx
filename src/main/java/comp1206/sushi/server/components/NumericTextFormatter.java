package comp1206.sushi.server.components;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;

/*based on https://stackoverflow.com/a/45201446/11214781
 * note: it is not essential for the functionality of the GUI, merely
 * enhances it by providing in-place validation.*/
public class NumericTextFormatter extends TextFormatter<Number> {

    private static DecimalFormat format = new DecimalFormat("#.0;-#.0");

    public NumericTextFormatter(int minDecimals, int maxDecimals) {
        super(
                new StringConverter<>() {
                    @Override
                    public String toString(Number object) {
                        if (object == null) {
                            return "";
                        }
                        String format;
                        if (maxDecimals > 0) {
                            format = "0.";
                        } else {
                            format = "0";
                        }
                        for (int i = 0; i < maxDecimals; i++) {
                            if (i < minDecimals) {
                                format = format + "0";
                            } else {
                                format = format + "#";
                            }
                        }
                        format = format + ";-" + format;
                        DecimalFormat df = new DecimalFormat(format);
                        String formatted = df.format(object);
                        return formatted;
                    }

                    @Override
                    public Number fromString(String string) {
                        try {
                            return format.parse(string);
                        } catch (ParseException e) {
                            return null;
                        }
                    }
                },
                0,
                change -> {
                    if (change.getControlNewText().isEmpty()) {
                        return change;
                    }

                    ParsePosition parsePosition = new ParsePosition(0);
                    Object object = format.parse(change.getControlNewText(), parsePosition);

                    if (change.getControlNewText().equals("-")) {
                        return change;
                    }

                    if (change.getCaretPosition() == 1) {
                        if (change.getControlNewText().equals(".")) {
                            return change;
                        }
                    }

                    if (object == null || parsePosition.getIndex() < change.getControlNewText().length()) {
                        return null;
                    } else {
                        int decPos = change.getControlNewText().indexOf(".");
                        if (decPos > 0) {
                            int numberOfDecimals = change.getControlNewText().substring(decPos + 1).length();
                            if (numberOfDecimals > maxDecimals) {
                                return null;
                            }
                        }
                        return change;
                    }
                }
        );
    }
}