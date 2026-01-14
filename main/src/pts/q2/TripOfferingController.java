package pts.q2;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import model.TripDisplay;
import pts.BackToHome;
import pts.DBConnect;
import pts.q6.AddBusController;

public class TripOfferingController implements Initializable {
	@FXML
	protected TextField tripNumberInput;
	@FXML
	protected DatePicker datePicker;
	@FXML
	protected TextField scheduledStartTimeInput;

	@FXML
	protected TableView<TripDisplay> table;
	@FXML
	protected TableColumn<TripDisplay, String> tripNumberCol;
	@FXML
	protected TableColumn<TripDisplay, String> dateCol;
	@FXML
	protected TableColumn<TripDisplay, String> scheduledStartTimeCol;
	@FXML
	protected TableColumn<TripDisplay, String> scheduledArrivalTimeCol;
	@FXML
	protected TableColumn<TripDisplay, String> driverNameCol;
	@FXML
	protected TableColumn<TripDisplay, String> busIdCol;

	ObservableList<TripDisplay> tripOfferingList = FXCollections.observableArrayList();

	String query = null;
	Connection connection = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		loadTripOffering();
	}

	@FXML
	protected void backAction(ActionEvent event) throws SQLException, IOException {
		BackToHome home = new BackToHome();
		home.backToHome(event);
	}

	// Get the data from the database
	private void refreshTable() {
		try {
			System.out.println("");
			tripOfferingList.clear();

			query = "SELECT * FROM tripoffering";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				tripOfferingList.add(new TripDisplay(resultSet.getInt("TripNumber"), resultSet.getString("Date"),
						resultSet.getString("ScheduledStartTime"), resultSet.getString("ScheduledArrivalTime"),
						resultSet.getString("DriverName"), resultSet.getString("BusID")));
			}
			table.setItems(tripOfferingList);
		} catch (SQLException ex) { // Handle exception
			Logger.getLogger(AddBusController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// Display trip schedule on the table
	protected void loadTripOffering() {
		connection = DBConnect.getConnect(); // Connect the database
		refreshTable();
		// Display data on the columns
		tripNumberCol.setCellValueFactory(new PropertyValueFactory<>("tripNumber"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		scheduledStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("scheduledStartTime"));
		scheduledArrivalTimeCol.setCellValueFactory(new PropertyValueFactory<>("scheduledArrivalTime"));
		driverNameCol.setCellValueFactory(new PropertyValueFactory<>("driverName"));
		busIdCol.setCellValueFactory(new PropertyValueFactory<>("busId"));

		// Apply the custom row factory
		table.setRowFactory(tv -> new TableRow<TripDisplay>() {
			@Override
			protected void updateItem(TripDisplay item, boolean empty) {
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
}
