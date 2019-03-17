package comp1206.sushi.server.components;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.util.converter.NumberStringConverter;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class NumericTableCell<T> extends TableCell<T, Number> {

    private TextField textField;
    private int minDecimals, maxDecimals;

    public NumericTableCell() {
        minDecimals = 2;
        maxDecimals = 2;
    }

    public NumericTableCell(int min, int max) {
        minDecimals = min;
        maxDecimals = max;
    }

    @Override
    public void startEdit() {
        if (editableProperty().get()) {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.requestFocus();
            }
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem() != null ? getItem().toString() : null);
        setGraphic(null);
    }

    @Override
    public void updateItem(Number item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                    textField.selectAll();
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(null);
            }
        }
    }

    private void createTextField() {
        textField = new TextField();
        textField.setTextFormatter(new NumericTextFormatter(minDecimals, maxDecimals));
        textField.setText(getString());

        textField.setOnAction(evt -> {
            if (textField.getText() != null && !textField.getText().isEmpty()) {
                NumberStringConverter nsc = new NumberStringConverter();
                Number n = nsc.fromString(textField.getText());
                commitEdit(BigDecimal.valueOf(n.doubleValue()));
            }
        });
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

        textField.setOnKeyPressed((ke) -> {
            if (ke.getCode().equals(KeyCode.ESCAPE)) {
                cancelEdit();
            }
        });
        textField.setAlignment(Pos.CENTER);
        this.setAlignment(Pos.CENTER);
    }

    private String getString() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(minDecimals);
        nf.setMaximumFractionDigits(maxDecimals);
        return getItem() == null ? "" : nf.format(getItem());
    }

    @Override
    public void commitEdit(Number item) {
        if (isEditing()) {
            super.commitEdit(item);
        } else {
            final TableView<T> table = getTableView();
            if (table != null) {
                TablePosition<T, Number> position = new TablePosition<>(getTableView(),
                        getTableRow().getIndex(), getTableColumn());
                TableColumn.CellEditEvent<T, Number> editEvent = new TableColumn.CellEditEvent<>(table, position,
                        TableColumn.editCommitEvent(), item);
                Event.fireEvent(getTableColumn(), editEvent);
            }
            updateItem(item, false);
            if (table != null) {
                table.edit(-1, null);
            }
        }
    }
}
