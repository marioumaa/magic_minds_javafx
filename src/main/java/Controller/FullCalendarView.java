package Controller;

import Entity.Evenement;
import Service.ServiceEvenement;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;




public class FullCalendarView {

    private ArrayList<AnchorPaneNode> allCalendarDays = new ArrayList<>(35);
    private VBox view;
    private Text calendarTitle;
    private YearMonth currentYearMonth;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    /**
     * Create a calendar view
     * @param yearMonth year month to create the calendar of
     */
    public FullCalendarView(YearMonth yearMonth) {
        currentYearMonth = yearMonth;
        // Create the calendar grid pane
        GridPane calendar = new GridPane();
        calendar.setPrefSize(1095, 670);
        calendar.setGridLinesVisible(true);
        // Create rows and columns with anchor panes for the calendar
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                AnchorPaneNode ap = new AnchorPaneNode();
                ap.setPrefSize(700,700);
                calendar.add(ap,j,i);
                allCalendarDays.add(ap);
            }
        }
        // Days of the week labels
        Text[] dayNames = new Text[]{
                new Text("Sunday"),
                new Text("Monday"),
                new Text("Tuesday"),
                new Text("Wednesday"),
                new Text("Thursday"),
                new Text("Friday"),
                new Text("Saturday")
        };

// Appliquer le style gras à chaque objet Text
        for (Text dayName : dayNames) {
            dayName.setStyle("-fx-font-weight: bold;");
        }

        GridPane dayLabels = new GridPane();
        dayLabels.setPrefWidth(1095);
        Integer col = 0;
        for (Text txt : dayNames) {
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(700, 10);
            ap.setBottomAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            dayLabels.add(ap, col++, 0);
        }
        // Create calendarTitle and buttons to change current month
        calendarTitle = new Text();
        Button previousMonth = new Button("<<");
        previousMonth.setStyle("-fx-font-weight: bold;");

        previousMonth.setOnAction(e -> previousMonth());
        Button nextMonth = new Button(">>");
        nextMonth.setStyle("-fx-font-weight: bold;");
        nextMonth.setOnAction(e -> nextMonth());
        HBox titleBar = new HBox(previousMonth, calendarTitle, nextMonth);
        titleBar.setAlignment(Pos.BASELINE_CENTER);
        // Populate calendar with the appropriate day numbers
        populateCalendar(yearMonth);
        // Create the calendar view
        view = new VBox(titleBar, dayLabels, calendar);
    }

    /**
     * Set the days of the calendar to correspond to the appropriate date
     * @param yearMonth year and month of month to render
     */
    public void populateCalendar(YearMonth yearMonth) {
        // Get the date we want to start with on the calendar
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        // Dial back the day until it is SUNDAY (unless the month starts on a sunday)
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY") ) {
            calendarDate = calendarDate.minusDays(1);
        }
        // Populate the calendar with day numbers
        for (AnchorPaneNode ap : allCalendarDays) {
            if (ap.getChildren().size() != 0) {
                ap.getChildren().remove(0);
            }
            if (ap.getChildren().size() != 0) {
                ap.getChildren().remove(0);
            }
            Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            ap.setDate(calendarDate);
            ap.setTopAnchor(txt, 5.0);
            ap.setLeftAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            VBox vbox=attacherTache(ap.getDate());

            if( vbox !=null){
                ap.getChildren().add(vbox);
            }
            calendarDate = calendarDate.plusDays(1);
        }
        // Change the title of the calendar
        calendarTitle.setText(yearMonth.getMonth().toString() + " " + String.valueOf(yearMonth.getYear()));
    }

    /**
     * Move the month back by one. Repopulate the calendar with the correct dates.
     */
    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        populateCalendar(currentYearMonth);
    }

    /**
     * Move the month forward by one. Repopulate the calendar with the correct dates.
     */
    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        populateCalendar(currentYearMonth);
    }

    public VBox getView() {
        return view;
    }

    public ArrayList<AnchorPaneNode> getAllCalendarDays() {
        return allCalendarDays;
    }

    public void setAllCalendarDays(ArrayList<AnchorPaneNode> allCalendarDays) {
        this.allCalendarDays = allCalendarDays;
    }



    public VBox attacherTache(LocalDate ld) {
        VBox eventContainer = new VBox();
        eventContainer.getStyleClass().add("event-container"); // Ajoute la classe CSS pour le conteneur d'événements
        eventContainer.setAlignment(Pos.CENTER);
        ServiceEvenement serviceEvenement = new ServiceEvenement();
        List<Evenement> allEvents = serviceEvenement.getAll();
        int colorIndex = 1; // Variable pour alterner entre les couleurs d'événements
        for (Evenement evenement : allEvents) {
            LocalDate eventStartDate = LocalDate.of(evenement.getDate_debut().getYear() + 1900, evenement.getDate_debut().getMonth() + 1, evenement.getDate_debut().getDate());
            LocalDate eventEndDate = LocalDate.of(evenement.getDate_fin().getYear() + 1900, evenement.getDate_fin().getMonth() + 1, evenement.getDate_fin().getDate());

            // Vérifie si la date passée en paramètre se situe entre la date de début et de fin de l'événement
            if ((ld.isEqual(eventStartDate) || ld.isAfter(eventStartDate)) &&
                    (ld.isEqual(eventEndDate) || ld.isBefore(eventEndDate))) {
                Text titleText = new Text("Event Title : " + evenement.getNom());
                titleText.getStyleClass().add("event-title");


                VBox eventDetails = new VBox(titleText);
                eventDetails.getStyleClass().add("event-color-" + colorIndex);
                eventContainer.getChildren().add(eventDetails);

                colorIndex = colorIndex % 2 + 1;
            }
        }

        if (eventContainer.getChildren().isEmpty()) {
            return null;
        } else {
            return eventContainer;
        }
    }
}