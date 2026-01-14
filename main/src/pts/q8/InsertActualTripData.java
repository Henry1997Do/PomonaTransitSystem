package pts.q8;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.ActualTripStopInfo;
import model.Bus;
import model.Stop;
import model.TripDisplay;
import pts.BackToHome;
import pts.DBConnect;

public class InsertActualTripData implements Initializable {
	@FXML
	Label tripNumberLabel;
	@FXML
	Label dateLabel;
	@FXML
	Label scheduledStartTimeLabel;
	@FXML
	Label stopNumberLabel;

	@FXML
	private TextField actualStartTimeInput;
	@FXML
	private TextField actualArrivalTimeInput;
	@FXML
	private TextField numPassengersInInput;
	@FXML
	private TextField numPassengersOutInput;

	@FXML
	private TableView<ActualTripStopInfo> actualTripTable;
	@FXML
	private TableColumn<ActualTripStopInfo, String> tripNumberCol;
	@FXML
	private TableColumn<ActualTripStopInfo, String> dateCol;
	@FXML
	private TableColumn<ActualTripStopInfo, String> scheduledStartTimeCol;
	@FXML
	private TableColumn<ActualTripStopInfo, String> scheduledArrivalTimeCol;
	@FXML
	private TableColumn<ActualTripStopInfo, String> stopNumberCol;
	@FXML
	private TableColumn<ActualTripStopInfo, String> actualStartTimeCol;
	@FXML
	private TableColumn<ActualTripStopInfo, String> actualArrivalTimeCol;
	@FXML
	private TableColumn<ActualTripStopInfo, String> numPassengersInCol;
	@FXML
	private TableColumn<ActualTripStopInfo, String> numPassengersOutCol;

	ObservableList<ActualTripStopInfo> actualTripStopList = FXCollections.observableArrayList();

	String query = null;
	Connection connection = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	private int tripNumber;
	private String date;
	private String scheduledStartTime;
	private int stopNumber;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		loadActualTripInfo();
	}

	public void setData(int tripNumber, String date, String scheduledStartTime, int stopNumber) {
		this.tripNumber = tripNumber;
		this.date = date;
		this.scheduledStartTime = scheduledStartTime;
		this.stopNumber = stopNumber;

		// Append the new text to the labels
		tripNumberLabel.setText(tripNumberLabel.getText() + tripNumber);
		dateLabel.setText(dateLabel.getText() + date);
		scheduledStartTimeLabel.setText(scheduledStartTimeLabel.getText() + scheduledStartTime);
		stopNumberLabel.setText(stopNumberLabel.getText() + stopNumber);
	}

	@FXML
	public void backAction(ActionEvent event) throws SQLException, IOException {
		BackToHome home = new BackToHome();
		home.backToHome(event);
	}

	// Get the data from the database
	private void refreshTable() {
		try {
			System.out.println("");
			actualTripStopList.clear();

			// Run SQL query to display the table ActualTripStopInfo
			query = "SELECT * FROM actualtripstopinfo ORDER BY TripNumber";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				actualTripStopList.add(new ActualTripStopInfo(resultSet.getInt("TripNumber"),
						resultSet.getString("Date"), resultSet.getString("ScheduledStartTime"),
						resultSet.getInt("StopNumber"), resultSet.getString("ScheduledArrivalTime"),
						resultSet.getString("ActualStartTime"), resultSet.getString("ActualArrivalTime"),
						resultSet.getInt("NumberOfPassengerIn"), resultSet.getInt("NumberOfPassengerOut")));
			}
			actualTripTable.setItems(actualTripStopList);
		} catch (SQLException ex) { // Handle exception
			Logger.getLogger(InsertActualTripData.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// Display trip schedule on the table
	private void loadActualTripInfo() {
		connection = DBConnect.getConnect(); // Connect the database
		refreshTable();
		// Display trip offering data on the columns
		tripNumberCol.setCellValueFactory(new PropertyValueFactory<>("tripNumber"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		scheduledStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("scheduledStartTime"));
		scheduledArrivalTimeCol.setCellValueFactory(new PropertyValueFactory<>("scheduledArrivalTime"));
		stopNumberCol.setCellValueFactory(new PropertyValueFactory<>("stopNumber"));
		actualStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("actualStartTime"));
		actualArrivalTimeCol.setCellValueFactory(new PropertyValueFactory<>("actualArrivalTime"));
		numPassengersInCol.setCellValueFactory(new PropertyValueFactory<>("numPassengersIn"));
		numPassengersOutCol.setCellValueFactory(new PropertyValueFactory<>("numPassengersOut"));

		// Apply the custom row factory
		actualTripTable.setRowFactory(tv -> new TableRow<ActualTripStopInfo>() {
			@Override
			protected void updateItem(ActualTripStopInfo item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setStyle("");
				} else {
					if (item.isUpdated()) {
						setStyle("-fx-background-color: yellow;");
					} else {
						setStyle("");
					}
				}
			}
		});
	}

	public void addActualTripData(ActionEvent event) throws IOException {
		try {
			String actualStartTime = actualStartTimeInput.getText();
			String actualArrivalTime = actualArrivalTimeInput.getText();
			String numPassengersInStr = numPassengersInInput.getText();
			String numPassengersOutStr = numPassengersOutInput.getText();
			
			if (actualStartTime.equals("") || actualArrivalTime.equals("") || numPassengersInStr.equals("")
					|| numPassengersOutStr.equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setHeaderText(null);
				alert.setContentText("Missing data field(s). Please fill out all the fields");
				alert.showAndWait();
			} else {
				int numPassengersIn = Integer.parseInt(numPassengersInStr);
				int numPassengersOut = Integer.parseInt(numPassengersOutStr);
				
				connection = DBConnect.getConnect();
				query = "SELECT ScheduledArrivalTime FROM tripoffering\r\n"
						+ "WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1, tripNumber);
				preparedStatement.setString(2, date);
				preparedStatement.setString(3, scheduledStartTime);
				resultSet = preparedStatement.executeQuery();
				resultSet.next();
				String scheduledArrivalTime = resultSet.getString("ScheduledArrivalTime");

				query = "INSERT INTO `lab4`.`actualtripstopinfo` (`TripNumber`, `Date`, `ScheduledStartTime`, `StopNumber`, `ScheduledArrivalTime`, `ActualStartTime`, `ActualArrivalTime`, `NumberOfPassengerIn`, `NumberOfPassengerOut`)\r\n"
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1, tripNumber);
				preparedStatement.setString(2, date);
				preparedStatement.setString(3, scheduledStartTime);
				preparedStatement.setInt(4, stopNumber);
				preparedStatement.setString(5, scheduledArrivalTime);
				preparedStatement.setString(6, actualStartTime);
				preparedStatement.setString(7, actualArrivalTime);
				preparedStatement.setInt(8, numPassengersIn);
				preparedStatement.setInt(9, numPassengersOut);

				preparedStatement.executeUpdate();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Add Driver");
				alert.setHeaderText(null);
				alert.setContentText("Actual trip data added successfully!");
				alert.showAndWait();

				actualStartTimeInput.clear();
				actualArrivalTimeInput.clear();
				numPassengersInInput.clear();
				numPassengersOutInput.clear();

				loadActualTripInfo();
				for (ActualTripStopInfo trip : actualTripStopList) {
					if (trip.getTripNumber() == tripNumber && trip.getDate().equals(date)
							&& trip.getScheduledStartTime().equals(scheduledStartTime)
							&& trip.getStopNumber() == stopNumber) {
						trip.setUpdated(true);
						break;
					}
				}
				actualTripTable.refresh();
			}	
		} catch (SQLException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to add bus ID");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}
}
