package pts.q1;

import model.TripDisplay;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import pts.BackToHome;
import pts.DBConnect;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DisplayTripScheduleResultController {
	@FXML
	Label startLocationNameLabel;
	@FXML
	Label destLocationNameLabel;
	@FXML
	Label dateLabel;
	@FXML
	private TableView<TripDisplay> table;
	@FXML
	private TableColumn<TripDisplay, String> scheduledStartTimeCol;
	@FXML
	private TableColumn<TripDisplay, String> scheduledArrivalTimeCol;
	@FXML
	private TableColumn<TripDisplay, String> driverNameCol;
	@FXML
	private TableColumn<TripDisplay, String> busIDCol;

	ObservableList<TripDisplay> tripScheduleList = FXCollections.observableArrayList();

	String query = null;
	Connection connection = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	private String startLoc;
	private String destLoc;
	private String date;

	public void setData(String startLoc, String destLoc, String date) {
		this.startLoc = startLoc;
		this.destLoc = destLoc;
		this.date = date;

		// Append the new text to the labels
		startLocationNameLabel.setText(startLocationNameLabel.getText() + startLoc);
		destLocationNameLabel.setText(destLocationNameLabel.getText() + destLoc);
		dateLabel.setText(dateLabel.getText() + " " + date);
	}

	@FXML
	public void backAction(ActionEvent event) throws SQLException, IOException {
		BackToHome home = new BackToHome();
		home.backToHome(event);
	}

	// Get the data from the database
	@FXML
	private void refreshTable() {
		try {
			System.out.println("");
			tripScheduleList.clear();

			query = "SELECT T1.ScheduledStartTime, T1.ScheduledArrivalTime, T1.DriverName, T1.BusID\r\n"
					+ "FROM tripoffering AS T1\r\n" + "JOIN trip AS T2 ON T1.TripNumber = T2.TripNumber\r\n"
					+ "WHERE T2.StartLocationName = ? AND T2.DestinationName = ? AND T1.Date = ?;";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, this.startLoc);
			preparedStatement.setString(2, this.destLoc);
			preparedStatement.setString(3, this.date);
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				tripScheduleList.add(new TripDisplay(resultSet.getString("ScheduledStartTime"),
						resultSet.getString("ScheduledArrivalTime"), resultSet.getString("DriverName"),
						resultSet.getString("BusID")));
			}
			table.setItems(tripScheduleList);
		} catch (SQLException ex) { // Handle exception
			Logger.getLogger(DisplayTripScheduleResultController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// Display trip schedule on the table
	public void loadTrip() {
		connection = DBConnect.getConnect(); // Connect the database
		refreshTable();
		// Display data on the columns
		scheduledStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("scheduledStartTime"));
		scheduledArrivalTimeCol.setCellValueFactory(new PropertyValueFactory<>("scheduledArrivalTime"));
		driverNameCol.setCellValueFactory(new PropertyValueFactory<>("driverName"));
		busIDCol.setCellValueFactory(new PropertyValueFactory<>("busId"));
	}
}
