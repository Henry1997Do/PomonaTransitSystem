package pts.q6;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import model.Bus;
import model.Driver;
import pts.BackToHome;
import pts.DBConnect;
import pts.q3.DisplayStopsController;

public class AddBusController implements Initializable {
	@FXML
	private TextField busIdInput;
	@FXML
	private TextField modelInput;
	@FXML
	private TextField yearInput;

	@FXML
	private TableView<Bus> table;
	@FXML
	private TableColumn<Bus, String> busIDCol;
	@FXML
	private TableColumn<Bus, String> modelCol;
	@FXML
	private TableColumn<Bus, String> yearCol;

	ObservableList<Bus> busList = FXCollections.observableArrayList();

	String query = null;
	Connection connection = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	private String busID;
	private String model;
	private String year;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		loadBus();
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
			busList.clear();

			query = "SELECT * FROM bus";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				busList.add(new Bus(resultSet.getString("BusID"), resultSet.getString("Model"),
						resultSet.getString("Year")));
			}
			table.setItems(busList);
		} catch (SQLException ex) { // Handle exception
			Logger.getLogger(AddBusController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// Display trip schedule on the table
	private void loadBus() {
		connection = DBConnect.getConnect(); // Connect the database
		refreshTable();
		// Display data on the columns
		busIDCol.setCellValueFactory(new PropertyValueFactory<>("busId"));
		modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
		yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

		// Apply the custom row factory
		table.setRowFactory(tv -> new TableRow<Bus>() {
			@Override
			protected void updateItem(Bus item, boolean empty) {
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

	public void addBusAction(ActionEvent event) throws SQLException, IOException {
		try {
			busID = busIdInput.getText();
			model = modelInput.getText();
			year = yearInput.getText();

			if (busID.equals("") || model.equals("") || year.equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setHeaderText(null);
				alert.setContentText("Missing data field(s). Please fill out all the fields");
				alert.showAndWait();
			} else {
				connection = DBConnect.getConnect();
				query = "INSERT INTO `lab4`.`bus` (`BusID`, `Model`, `Year`) VALUES (?, ?, ?);";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, busID);
				preparedStatement.setString(2, model);
				preparedStatement.setString(3, year);
				preparedStatement.executeUpdate();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Add Bus");
				alert.setHeaderText(null);
				alert.setContentText("Bus ID added successfully!");
				alert.showAndWait();

				busIdInput.clear();
				modelInput.clear();
				yearInput.clear();

				loadBus();
				for (Bus bus : busList) {
					if (bus.getBusId().equals(busID) && bus.getModel().equals(model) && bus.getYear().equals(year)) {
						bus.setUpdated(true);
						break;
					}
				}
				table.refresh();
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
