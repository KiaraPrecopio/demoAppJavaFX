package org.prueba.demoappjavafx;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HelloController {

    @FXML
    private GridPane gridPane;

    public void initialize() {
        if (gridPane != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate currentDate = LocalDate.now();

            LocalDate startOfWeek = getFirstDayOfWeek(currentDate);

            for (int i = 0; i < 5; i++) {
                Label label = createFormattedLabel(startOfWeek, formatter);
                gridPane.add(label, i + 1, 0);
                startOfWeek = startOfWeek.plusDays(1);
            }
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

    private void handleNullGridPane() {
        System.err.println("gridPane is null. Make sure it is properly initialized or injected.");
    }
}