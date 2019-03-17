package comp1206.sushi.server.components;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.function.UnaryOperator;

public class NumericFilter extends TextFormatter<Number> {

    public NumericFilter(NumberFormat format){
        this(new NumberStringConverter(), 0, getFilter(format));
    }

    public NumericFilter(StringConverter<Number> stringConverter, Number number, UnaryOperator<Change> unaryOperator) {
        super(stringConverter, number, unaryOperator);
    }

    private static UnaryOperator<TextFormatter.Change> getFilter(NumberFormat format) {
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (c.isContentChange()) {
                ParsePosition pos = new ParsePosition(0);
                format.parse(c.getControlNewText(), pos);
                if (pos.getIndex() == 0 || pos.getIndex() < c.getControlNewText().length())
                    return null;
            }
            return c;
        };
        return filter;
    }
}
