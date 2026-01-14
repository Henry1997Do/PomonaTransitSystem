package pts.q8;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import model.Stop;
import model.TripDisplay;
import pts.BackToHome;
import pts.DBConnect;

public class DisplayTripOfferingController implements Initializable {
	@FXML
	private TextField tripNumberInput;
	@FXML
	DatePicker datePicker;
	@FXML
	private TextField scheduledStartTimeInput;
	@FXML
	private TextField stopNumberInput;

	@FXML
	private TableView<TripDisplay> tripTable;
	@FXML
	private TableView<Stop> stopTable;
	@FXML
	private TableColumn<TripDisplay, String> tripNumberCol;
	@FXML
	private TableColumn<TripDisplay, String> dateCol;
	@FXML
	private TableColumn<TripDisplay, String> scheduledStartTimeCol;
	@FXML
	private TableColumn<Stop, String> stopNumberCol;
	@FXML
	private TableColumn<Stop, String> stopAddressCol;

	ObservableList<TripDisplay> tripOfferingList = FXCollections.observableArrayList();
	ObservableList<Stop> stopList = FXCollections.observableArrayList();

	String query = null;
	Connection connection = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		loadTripOfferingsAndStop();
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
			tripOfferingList.clear();
			stopList.clear();

			// Run SQL query to display the table TripOffering
			query = "SELECT TripNumber, Date, ScheduledStartTime FROM tripoffering\r\n" + "ORDER BY TripNumber";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				tripOfferingList.add(new TripDisplay(resultSet.getInt("TripNumber"), resultSet.getString("Date"),
						resultSet.getString("ScheduledStartTime")));
			}
			tripTable.setItems(tripOfferingList);

			// Run SQL query to display the table Stop
			query = "SELECT * FROM `lab4`.`stop` ORDER BY StopNumber";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				stopList.add(new Stop(resultSet.getInt("StopNumber"), resultSet.getString("StopAddress")));
			}
			stopTable.setItems(stopList);
		} catch (SQLException ex) { // Handle exception
			Logger.getLogger(DisplayTripOfferingController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// Display trip schedule on the table
	private void loadTripOfferingsAndStop() {
		connection = DBConnect.getConnect(); // Connect the database
		refreshTable();
		// Display trip offering data on the columns
		tripNumberCol.setCellValueFactory(new PropertyValueFactory<>("tripNumber"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		scheduledStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("scheduledStartTime"));

		// Display stop data on the columns
		stopNumberCol.setCellValueFactory(new PropertyValueFactory<>("stopNumber"));
		stopAddressCol.setCellValueFactory(new PropertyValueFactory<>("stopAddress"));
	}

//	private

	public void displayAddActualTripData(ActionEvent event) throws IOException {
		try {
			String tripNumberStr = tripNumberInput.getText();
			LocalDate myDate = datePicker.getValue();
			String scheduledStartTime = scheduledStartTimeInput.getText();
			String stopNumberStr = stopNumberInput.getText();
			if (tripNumberStr.equals("") || myDate == null || scheduledStartTime.equals("") || stopNumberStr.equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setHeaderText(null);
				alert.setContentText("Missing data field(s). Please fill out all the fields");
				alert.showAndWait();
			} else {
				int tripNumber = Integer.parseInt(tripNumberStr);
				int stopNumber = Integer.parseInt(stopNumberStr);
				String date = myDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/pts/q8/InsertActualDataFX.fxml"));
				Parent root = loader.load();

				InsertActualTripData actualTripDataController = loader.getController();
				actualTripDataController.setData(tripNumber, date, scheduledStartTime, stopNumber);

				Scene scene = new Scene(root);
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.setScene(scene);
				stage.setTitle("Insert Actual Trip Data");
				stage.show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
