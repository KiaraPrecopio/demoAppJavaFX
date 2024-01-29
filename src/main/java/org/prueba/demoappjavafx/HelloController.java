package org.prueba.demoappjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.function.Consumer;

public class HelloController {

    @FXML
    private TextField maxHours;
    @FXML
    private TextField fHours;
    @FXML
    private DatePicker datePicker;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label countdownHours;
    private int fHoursValue;
    private int maxHoursValue;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void initialize() {
        maxHoursValue = 8;
        initializeCountdownHours();
        maxDailyHours();
        fHoursTotal();
        datePicker.setDayCellFactory(createDayCellFactory());
        setupDatePickerListener();


    }



    private void setupDatePickerListener() {
        datePicker.valueProperty().addListener((observable, oldValue, newValue)
                -> updateGridPaneWithSelectedDate(newValue));
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

    // Método para crear un DateCell que pinte de color los días de la semana actual
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

    private void maxDailyHours() {
        TextFormatter<Integer> formatter = createTextFormatter(maxHoursValue -> {
            if (maxHoursValue >= 0 && maxHoursValue <= 8) {
                this.maxHoursValue = maxHoursValue;
                updateCountdownHours();
            }
        });

        maxHours.setTextFormatter(formatter);
    }

    private void fHoursTotal() {
        TextFormatter<Integer> formatter = createTextFormatter(fHoursValue -> {
            this.fHoursValue = fHoursValue;
            updateCountdownHours();
        });

        fHours.setTextFormatter(formatter);
    }

    private void initializeCountdownHours() {
        updateCountdownHours();
    }

    private void updateCountdownHours() {
        int remainingHours = maxHoursValue - fHoursValue;
        countdownHours.setText(String.valueOf(Math.max(remainingHours, 0)));
    }

    private TextFormatter<Integer> createTextFormatter(Consumer<Integer> valueChangedCallback) {
        return new TextFormatter<>(new IntegerStringConverter(), 0, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                int newValue = newText.isEmpty() ? 0 : Integer.parseInt(newText);
                valueChangedCallback.accept(newValue);
                return change;
            }
            return null;
        });
    }







}