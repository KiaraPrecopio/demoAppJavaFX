package org.prueba.demoappjavafx;

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
    private Spinner<Integer> dailyHours;
    @FXML
    private Spinner<Integer> extraHours;
    @FXML
    private Spinner<Integer> fHours;
    @FXML
    private DatePicker datePicker;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label dateLabel;

    @FXML
    private Spinner<Integer> hsDiariasField;

    @FXML
    private Spinner<Integer> hsExtrasField;

    @FXML
    private Spinner<Integer> forloughField;

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

        spinnerDailyHours();
        spinnerExtraHours();
        spinnerFHours();
    }



    private void setupDatePickerListener() {
        datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                updateGridPaneWithSelectedDate(newValue);
            }
        });
    }

    private void updateGridPaneWithSelectedDate(LocalDate newDate) {
        int dateLabelColumnIndex = 1;
        int dateLabelRowIndex = 1;

        Label dayLabel = (Label) gridPane.getChildren().stream()
                .filter(node -> GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == dateLabelColumnIndex
                        && GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == dateLabelRowIndex)
                .findFirst()
                .orElse(null);

        /*Label dateLabel = (Label) gridPane.getChildren().stream()
                .filter(node -> GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == dateLabelColumnIndex
                        && GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == dateLabelRowIndex)
                .findFirst()
                .orElse(null);*/

        if (dayLabel != null && dateLabel != null) {
            String date = newDate.format(dateFormatter);
            String day = newDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());

            dayLabel.setText(day);
            dateLabel.setText(date);
        }
    }


    /*private void updateGridPaneWithSelectedDate(LocalDate newDate) {
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
    }*/

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
    private void spinnerDailyHours() {
        SpinnerValueFactory<Integer> valueDailyHours =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 8, 0);
        dailyHours.setValueFactory(valueDailyHours);

        dailyHours.editorProperty().getValue().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                dailyHours.getValueFactory().setValue(0);
            }
        });
    }
    private void spinnerExtraHours() {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
        extraHours.setValueFactory(valueFactory);

        extraHours.editorProperty().getValue().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                extraHours.getValueFactory().setValue(0);
            }
        });
    @FXML
    private void confirmHours() {
        LocalDate selectedDate = datePicker.getValue();
        String selectedProyecto = proyectComboBox.getValue();
        String selectedRange = rangeField.getValue();

        Integer horasDiarias = hsDiariasField.getValue();
        Integer horasExtras = hsExtrasField.getValue();
        Integer forloughHours = forloughField.getValue();
        String commentsHours = commentField.getText();

        System.out.println("Fecha seleccionada: " + selectedDate);
        System.out.println("Proyecto seleccionado: " + selectedProyecto);
        System.out.println("Horas diarias: " + horasDiarias);
        System.out.println("Horas extras: " + horasExtras);
        System.out.println("Rango de horas extra: " + selectedRange);
        System.out.println("Horas forlough: " + forloughHours);
        System.out.println("Comentarios: " + commentsHours);
        System.out.println();
    }
    private void spinnerFHours() {
        SpinnerValueFactory<Integer> valueFHours =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 8, 0);
        fHours.setValueFactory(valueFHours);

        fHours.editorProperty().getValue().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                fHours.getValueFactory().setValue(0);
            }
        });
    }



}