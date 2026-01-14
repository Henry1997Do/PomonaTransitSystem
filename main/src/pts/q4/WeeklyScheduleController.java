package pts.q4;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.TripDisplay;
import model.TripStopInfo;
import pts.BackToHome;
import pts.DBConnect;
import pts.q1.DisplayTripScheduleResultController;

public class WeeklyScheduleController {
	@FXML
	Label driverNameLabel;
	@FXML
	TextField textField;
	@FXML
	DatePicker datePicker;
	@FXML
	private TableView<TripDisplay> table;
	@FXML
	private TableColumn<TripDisplay, String> tripNumberCol;
	@FXML
	private TableColumn<TripDisplay, String> dateCol;
	@FXML
	private TableColumn<TripDisplay, String> scheduledStartTimeCol;
	@FXML
	private TableColumn<TripDisplay, String> scheduledArrivalTimeCol;
	@FXML
	private TableColumn<TripDisplay, String> busIDCol;

	ObservableList<TripDisplay> tripScheduleList = FXCollections.observableArrayList();

	String query = null;
	Connection connection = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	@FXML
	public void viewScheduleAction(ActionEvent event) throws SQLException, IOException {
		String driverName = textField.getText();
		LocalDate myDate = datePicker.getValue();
		if (driverName.equals("") || myDate == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText(null);
			alert.setContentText("Missing data field(s). Please fill out all the fields");
			alert.showAndWait();
		} else {
			String myFormattedDate = myDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			loadTrip(driverName, myFormattedDate);
		}
	}

	@FXML
	public void backAction(ActionEvent event) throws SQLException, IOException {
		BackToHome home = new BackToHome();
		home.backToHome(event);
	}

	// Get the data from the database
	private void refreshTable(String driverName, String date) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate monday = LocalDate.parse(date, formatter).with(DayOfWeek.MONDAY);
			LocalDate sunday = LocalDate.parse(date, formatter).with(DayOfWeek.SUNDAY);

			// Convert LocalDate back to String
			String mondayString = monday.format(formatter);
			String sundayString = sunday.format(formatter);
			driverNameLabel.setText("Weekly Schedule for: " + driverName);
			tripScheduleList.clear();

			query = "SELECT TripNumber, `Date`, ScheduledStartTime, ScheduledArrivalTime, BusID\r\n"
					+ "FROM tripoffering \r\n" + "WHERE DriverName = ? AND (`Date` BETWEEN ? AND ?);";
			preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			preparedStatement.setString(1, driverName);
			preparedStatement.setString(2, mondayString);
			preparedStatement.setString(3, sundayString);
			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setHeaderText(null);
				alert.setContentText("No records match the specified data");
				alert.showAndWait();
			}

			// Move the cursor back to the start
			resultSet.beforeFirst();

			while (resultSet.next()) {
				Date myDate = resultSet.getDate("Date");
				String date_str = myDate.toString();
				tripScheduleList.add(new TripDisplay(resultSet.getInt("TripNumber"), date_str,
						resultSet.getString("ScheduledStartTime"), resultSet.getString("ScheduledArrivalTime"), "",
						resultSet.getString("BusID")));
			}
			table.setItems(tripScheduleList);
		} catch (SQLException ex) { // Handle exception
			Logger.getLogger(DisplayTripScheduleResultController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// Display trip schedule on the table
	public void loadTrip(String driverName, String date) {
		connection = DBConnect.getConnect(); // Connect the database
		refreshTable(driverName, date);
		// Display data on the columns
		tripNumberCol.setCellValueFactory(new PropertyValueFactory<>("tripNumber"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		scheduledStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("scheduledStartTime"));
		scheduledArrivalTimeCol.setCellValueFactory(new PropertyValueFactory<>("scheduledArrivalTime"));
		busIDCol.setCellValueFactory(new PropertyValueFactory<>("busId"));
	}
}
