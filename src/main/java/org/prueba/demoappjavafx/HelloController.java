package org.prueba.demoappjavafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private Spinner<Integer> hsDiariasField;

    @FXML
    private TextField hsExtrasField;

    @FXML
    private TextField forloughField;

    @FXML
    private TextField commentField;

    @FXML
    private ComboBox<String> proyectComboBox;

    @FXML
    private ComboBox<String> rangeField;

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
    private void confirmHours() {
        LocalDate selectedDate = datePicker.getValue();
        String selectedProyecto = proyectComboBox.getValue();
        String selectedRange = rangeField.getValue();

        Integer horasDiarias = hsDiariasField.getValue();
        String horasExtras = hsExtrasField.getText();
        String forloughHours = forloughField.getText();
        String commentsHours = commentField.getText();

        /** TODO
         * Si está dispuesto por defecto el 0 no habría que hacer estas validaciones
         */

        if (horasExtras.isEmpty()) {
            System.out.println("Por favor, ingrese valores para horas extras.");
            return;
        }

        if (isIntegerNumber(horasExtras)) {
            System.out.println("Por favor, ingrese valores numéricos para horas diarias y extras.");
            return;
        }

        System.out.println("Fecha seleccionada: " + selectedDate);
        System.out.println("Proyecto seleccionado: " + selectedProyecto);
        System.out.println("Horas diarias: " + horasDiarias);
        System.out.println("Horas extras: " + horasExtras);
        System.out.println("Rango de horas extra: " + selectedRange);
        System.out.println("Horas forlough: " + forloughHours);
        System.out.println("Comentarios: " + commentsHours);
        System.out.println();
    }

    private boolean isIntegerNumber(String texto) {
        try {
            Integer.parseInt(texto);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}