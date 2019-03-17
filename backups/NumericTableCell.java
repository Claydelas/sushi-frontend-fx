package comp1206.sushi.server.components;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

import java.lang.reflect.Field;

/**
 * TextFieldTableCell that validates input with a TextFormatter.
 * <p>
 * Extends TextFieldTableCell, accesses super's private field reflectively.
 */
public class NumericTableCell<S, T> extends TextFieldTableCell<S, T> {

    private TextFormatter<T> formatter;
    private TextField textAlias;

    public NumericTableCell() {
        this((StringConverter<T>) null);
    }

    public NumericTableCell(StringConverter<T> converter) {
        super(converter);
    }

    public NumericTableCell(TextFormatter<T> formatter) {
        super(formatter.getValueConverter());
        this.formatter = formatter;
    }

    /**
     * Overridden to install the formatter. <p>
     * <p>
     * Beware: implementation detail! super creates and configures
     * the textField lazy on first access, so have to install after
     * calling super.
     */
    @Override
    public void startEdit() {
        super.startEdit();
        installFormatter();
    }

    private void installFormatter() {
        if (formatter != null && isEditing() && textAlias == null) {
            textAlias = invokeTextField();
            if (textAlias != null) {
                textAlias.setTextFormatter(formatter);
            }
        }
    }

    private TextField invokeTextField() {
        Class<?> clazz = TextFieldTableCell.class;
        try {
            Field field = clazz.getDeclaredField("textField");
            field.setAccessible(true);
            return (TextField) field.get(this);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}