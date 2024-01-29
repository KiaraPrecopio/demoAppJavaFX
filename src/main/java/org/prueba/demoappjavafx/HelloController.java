package org.prueba.demoappjavafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

public class HelloController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField hsDiariasField;

    @FXML
    private TextField hsExtrasField;

    @FXML
    private ComboBox<String> proyectoComboBox;


    private LocalDate selectedDate;
    private String selectedProject;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void initialize() {
        datePicker.setDayCellFactory(createDayCellFactory());
        setupDatePickerListener();
    }

    private void setupDatePickerListener() {
        datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                selectedDate = newValue;
                updateGridPaneWithSelectedDate(newValue);
            }
        });
    }

    private void updateGridPaneWithSelectedDate(LocalDate newDate) {
        int columnIndex = 1;
        int rowIndex = 1;

        Label labelToUpdate = (Label) gridPane.getChildren().stream()
                .filter(node -> GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == columnIndex
                        && GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == rowIndex)
                .findFirst()
                .orElse(null);

        if (labelToUpdate != null) {
            String formattedDate = newDate.format(dateFormatter);

            String dayOfWeek = newDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());

            String labelText = dayOfWeek + " - " + formattedDate;

            labelToUpdate.setText(labelText);
        }

    }

    private Callback<DatePicker, DateCell> createDayCellFactory() {
        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    LocalDate currentDate = LocalDate.now();
                    LocalDate startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                    LocalDate endOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

                    if (item.isAfter(startOfWeek.minusDays(1)) && item.isBefore(endOfWeek.plusDays(1))) {
                        setStyle("-fx-background-color: lightblue;");
                    }
                }
            }
        };
    }

    @FXML
    private void confirmarHoras() {

        LocalDate selectedDate = datePicker.getValue();
        String selectedProyecto = proyectoComboBox.getValue();

        String horasDiarias = hsDiariasField.getText();
        String horasExtras = hsExtrasField.getText();

        if (horasDiarias.isEmpty() || horasExtras.isEmpty()) {
            System.out.println("Por favor, ingrese valores para horas diarias y extras.");
            return;
        }

        System.out.println("Fecha seleccionada: " + selectedDate);
        System.out.println("Proyecto seleccionado: " + selectedProyecto);
        System.out.println("Horas diarias (antes de convertir): " + horasDiarias);
        System.out.println("Horas extras (antes de convertir): " + horasExtras);

        try {
            int horasDiariasInt = Integer.parseInt(horasDiarias);
            int horasExtrasInt = Integer.parseInt(horasExtras);

            System.out.println("Horas diarias (después de convertir): " + horasDiariasInt);
            System.out.println("Horas extras (después de convertir): " + horasExtrasInt);
        } catch (NumberFormatException e) {
            System.out.println("Error al convertir a enteros: " + e.getMessage());
        }
    }

}






/*
    private static final int NUM_COLUMNS = 5;
    private static final int NUM_ROWS = 4;
    private static final int COL_TOTAL_WEEKLY = 6;
    private static final int ROW_TOTAL_WEEKLY = 4;
    private static final int ROW_TOTAL = 4;

    @FXML
    private GridPane gridPane;

    public void initialize() {
        if (gridPane != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate currentDate = LocalDate.now();

            LocalDate startOfWeek = getFirstDayOfWeek(currentDate);

            for (int i = 0; i < NUM_COLUMNS; i++) {
                Label label = createFormattedLabel(startOfWeek, formatter);
                gridPane.add(label, i + 1, 0);
                startOfWeek = startOfWeek.plusDays(1);
            }

            // Añadir TextFields vacíos en la fila "TOTAL"
            for (int i = 1; i <= NUM_COLUMNS; i++) {
                gridPane.add(createEmptyTextField(), i, ROW_TOTAL);
            }

            // Añadir TextField vacío para "TOTAL SEMANAL"
            gridPane.add(createEmptyTextField(), COL_TOTAL_WEEKLY, ROW_TOTAL_WEEKLY);

        } else {
            handleNullGridPane();
        }
    }

    private LocalDate getFirstDayOfWeek(LocalDate currentDate) {
        LocalDate startOfWeek = currentDate;
        while (startOfWeek.getDayOfWeek() != DayOfWeek.MONDAY) {
            startOfWeek = startOfWeek.minusDays(1);
        }
        return startOfWeek;
    }

    private Label createFormattedLabel(LocalDate date, DateTimeFormatter formatter) {
        Label label = new Label(date.format(formatter));
        label.setAlignment(Pos.CENTER);
        return label;
    }

    // Método para crear un TextField vacío
    private TextField createEmptyTextField() {
        TextField textField = new TextField();
        textField.setAlignment(Pos.CENTER);
        return textField;
    }

    @FXML
    private void onConfirmButtonClicked() {
        // Sumar columnas y actualizar los valores en "TOTAL" y "TOTAL SEMANAL"
        sumColumnsAndUpdateTotals();
    }

    private void sumColumnsAndUpdateTotals() {
        double totalWeekly = 0;

        for (int col = 1; col <= NUM_COLUMNS; col++) {
            double totalColumn = sumColumn(col, 1, NUM_ROWS - 1);
            setTextFieldValue(col, ROW_TOTAL, String.valueOf(totalColumn));

            // Acumular el total semanal
            totalWeekly += totalColumn;
        }

        // Actualizar el valor en "TOTAL SEMANAL"
        setTextFieldValue(COL_TOTAL_WEEKLY, ROW_TOTAL_WEEKLY, String.valueOf(totalWeekly));
    }

    private void setTextFieldValue(int col, int row, String value) {
        TextField textField = getTextField(col, row);
        if (textField != null) {
            textField.setText(value);
        }
    }

    private double sumColumn(int col, int startRow, int endRow) {
        double total = 0;

        for (int row = startRow; row <= endRow; row++) {
            TextField textField = getTextField(col, row);

            if (textField != null && !textField.getText().isEmpty()) {
                try {
                    total += Double.parseDouble(textField.getText());
                } catch (NumberFormatException e) {
                    handleNumberFormatError(col, row);
                }
            }
        }

        return total;
    }

    private TextField getTextField(int col, int row) {
        List<Node> textFields = gridPane.getChildren().filtered(node ->
                GridPane.getColumnIndex(node) != null &&
                        GridPane.getRowIndex(node) != null &&
                        GridPane.getColumnIndex(node) == col &&
                        GridPane.getRowIndex(node) == row &&
                        node instanceof TextField);

        return textFields.isEmpty() ? null : (TextField) textFields.get(0);
    }

    private void handleNullGridPane() {
        throw new IllegalStateException("gridPane is null. Make sure it is properly initialized or injected.");
    }

    private void handleNumberFormatError(int col, int row) {
        throw new NumberFormatException("Error de formato en el TextField en la columna " + col + ", fila " + row);
    }
*/
