package ro.mpp.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ro.mpp.Flight;
import ro.mpp.ValidationException;
import ro.mpp.observer.Observer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class MainViewController extends Controller implements Observer {
    @FXML
    TableView<Flight> flightsTable;
    @FXML
    TableColumn<Flight, String> destinationColumn;
    @FXML
    TableColumn<Flight, String> dateColumn;
    @FXML
    TableColumn<Flight, String> timeColumn;
    @FXML
    TableColumn<Flight, String> airportColumn;
    @FXML
    TableColumn<Flight, Integer> seatsColumn;
    @FXML
    TableView<Flight> filteredFlightsTable;
    @FXML
    TableColumn<Flight, String> filteredTimeColumn;
    @FXML
    TableColumn<Flight, String> filteredAirportColumn;
    @FXML
    TableColumn<Flight, Integer> filteredSeatsColumn;
    @FXML
    TextField destinationField;
    @FXML
    DatePicker datePicker;
    @FXML
    TextField clientField;
    @FXML
    TextField noOfSeatsField;
    @FXML
    Button buyButton;

    private final ObservableList<Flight> flights = FXCollections.observableArrayList();
    private final ObservableList<Flight> filteredFlights = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initFlightsTable();
        initFilteredFlightsTable();

        Platform.runLater(() -> {
            loadAllFlights();
            datePicker.setValue(LocalDate.now());
            loadFilteredFlights();
        });

        Platform.runLater(this::loadAllFlights);
        Platform.runLater(this::loadFilteredFlights);

        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> loadFilteredFlights());
        destinationField.textProperty().addListener((obs, oldText, newText) -> loadFilteredFlights());
    }

    private void loadAllFlights() {
        try {
            flights.clear();
            flights.addAll(StreamSupport.stream(appService.getAllFlights().spliterator(), false).toList());
        } catch(ValidationException e) {
            showException(e);
        }
    }

    private void loadFilteredFlights() {
        try {
            filteredFlights.clear();
            //filteredFlights.addAll(StreamSupport.stream(appService.findByDestinationAndDepartureDate(datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), destinationField.getText()).spliterator(), false).toList());
            filteredFlights.addAll(StreamSupport.stream(appService.getAllFlights().spliterator(), false).filter(flight -> flight.getDestination().equals(destinationField.getText()) && flight.getDepartureDate().equals(datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))).toList());
        } catch (ValidationException e) {
            showException(e);
        }
    }

    private void initFlightsTable() {
        flightsTable.setRowFactory(t -> new TableRow<Flight>() {
            @Override
            protected void updateItem(Flight item, boolean empty) {
                super.updateItem(item, empty);
                /*
                if(item == null || empty)
                    setVisible(true);
                else {
                    if(item.getAvailableSeats() == 0) {
                        setVisible(false);
                    } else {
                        setVisible(true);
                    }
                }
                 */
            }
        });

        destinationColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("destination"));
        dateColumn.setCellValueFactory(p -> {
            Flight f = p.getValue();
            var date = f.getDepartureDate();
            return new SimpleStringProperty(date);
        });
        timeColumn.setCellValueFactory(p -> {
            Flight f = p.getValue();
            var time = f.getDepartureTime();
            return new SimpleStringProperty(time.toString());
        });
        airportColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("airport"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<Flight, Integer>("availableSeats"));

        flightsTable.setItems(flights);
    }

    public void initFilteredFlightsTable() {
        filteredFlightsTable.setRowFactory(t -> new TableRow<Flight>() {
            @Override
            protected void updateItem(Flight item, boolean empty) {
                super.updateItem(item, empty);
                /*
                if(item == null || empty)
                    setVisible(true);
                else {
                    if(item.getAvailableSeats() == 0) {
                        setVisible(false);
                    } else {
                        setVisible(true);
                    }
                }
                 */
            }
        });

        filteredTimeColumn.setCellValueFactory(p -> {
            Flight f = p.getValue();
            var time = f.getDepartureTime();
            return new SimpleStringProperty(time);
        });
        filteredAirportColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("airport"));
        filteredSeatsColumn.setCellValueFactory(new PropertyValueFactory<Flight, Integer>("availableSeats"));

        filteredFlightsTable.setItems(filteredFlights);
    }


    @FXML
    public void datePickerInputChanged() {
        loadFilteredFlights();
    }

    @FXML
    public void destinationFieldInputChanged() {
        loadFilteredFlights();
    }

    @FXML
    public void buyButtonClicked() {
        try {
            var flight = filteredFlightsTable.getSelectionModel().getSelectedItem();
            if(flight == null) {
                throw new Exception("Choose a flight");
            }

            var clientName = clientField.getText();
            var noOfSeats = Integer.parseInt(noOfSeatsField.getText());

            if(Objects.equals(clientName, "")) {
                throw new Exception("Client field cannot be empty");
            }
            if(noOfSeats <= 0 || noOfSeats > flight.getAvailableSeats()) {
                throw new Exception("Invalid number of seats");
            }

            appService.buyTicket(clientName, flight, noOfSeats);
            Utils.showErrorBox("Succes!");
            //loadAllFlights();
            //loadFilteredFlights();
        } catch (NumberFormatException e) {
            Utils.showErrorBox("Invalid number of seats");
        } catch (ValidationException e) {
            showException(e);
        } catch (Exception e) {
            showException(e);
            //throw new RuntimeException(e);
        }
    }

    @Override
    public void updatedFlight(Flight flight) {
        System.out.println("Received update for flight: " + flight);
        Platform.runLater(() -> {
            for(Flight f : flights) {
                if(Objects.equals(f.getId(), flight.getId())) {
                    flights.remove(f);
                    if(flight.getAvailableSeats() > 0) {
                        flights.add(flight);
                    }
                    break;
                }
            }

            for(Flight f : filteredFlights) {
                if(Objects.equals(f.getId(), flight.getId())) {
                    filteredFlights.remove(f);
                    if(flight.getAvailableSeats() > 0) {
                        filteredFlights.add(flight);
                    }
                    break;
                }
            }
        });
    }
}