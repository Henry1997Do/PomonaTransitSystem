package pts.q2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.TripDisplay;
import pts.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddTripOfferingFormController {
	@FXML
	private TextField tripNumberInput;
	@FXML
	DatePicker datePicker;
	@FXML
	private TextField scheduledStartTimeInput;
	@FXML
	private TextField scheduledArrivalTimeInput;
	@FXML
	private TextField driverNameInput;
	@FXML
	private TextField busIdInput;
	@FXML
	private TextArea addedDataTextArea;

	private List<TripDisplay> addedTripOfferings = new ArrayList<>();

	private AddTripOfferingController mainController; // Reference to the main controller

	private String tripNumber;
	private String date;
	private String scheduledStartTime;
	private String scheduledArrivalTime;
	private String driverName;
	private String busId;

	public void setMainController(AddTripOfferingController mainController) {
		this.mainController = mainController;
	}

	@FXML
	private void addMore(ActionEvent event) {
		tripNumber = tripNumberInput.getText();
		LocalDate myDate = datePicker.getValue();
		scheduledStartTime = scheduledStartTimeInput.getText();
		scheduledArrivalTime = scheduledArrivalTimeInput.getText();
		driverName = driverNameInput.getText();
		busId = busIdInput.getText();

		if (tripNumber.equals("") || myDate == null || scheduledStartTime.equals("") || scheduledArrivalTime.equals("")
				|| driverName.equals("") || busId.equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText(null);
			alert.setContentText("Missing data field(s). Please fill out all the fields");
			alert.showAndWait();
		} else {
			date = myDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			TripDisplay tripOffering = new TripDisplay(Integer.parseInt(tripNumber), date, scheduledStartTime,
					scheduledArrivalTime, driverName, busId);
			addedTripOfferings.add(tripOffering);

			addedDataTextArea.appendText("\nAdded: " + tripOffering.toString());

			tripNumberInput.clear();
			datePicker.setValue(null);
			scheduledStartTimeInput.clear();
			scheduledArrivalTimeInput.clear();
			driverNameInput.clear();
			busIdInput.clear();
		}
	}

	@FXML
	private void submit(ActionEvent event) {
		Connection connection = DBConnect.getConnect();
		String query = "INSERT INTO tripoffering (TripNumber, Date, ScheduledStartTime, ScheduledArrivalTime, DriverName, BusID) VALUES (?, ?, ?, ?, ?, ?)";

		try {
			for (TripDisplay tripOffering : addedTripOfferings) {
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, Integer.toString(tripOffering.getTripNumber()));
				preparedStatement.setString(2, tripOffering.getDate());
				preparedStatement.setString(3, tripOffering.getScheduledStartTime());
				preparedStatement.setString(4, tripOffering.getScheduledArrivalTime());
				preparedStatement.setString(5, tripOffering.getDriverName());
				preparedStatement.setString(6, tripOffering.getBusId());
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		stage.close();

		// Refresh the table in the main window
		if (mainController != null) {
			mainController.loadTripOffering();
		}
	}

}
