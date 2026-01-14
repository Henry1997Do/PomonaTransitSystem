package pts.q1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//import helper.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import pts.BackToHome;
import pts.DBConnect;

public class DisplayTripScheduleController {
	@FXML
	private TextField startLocationNameInput;
	@FXML
	private TextField destinationNameInput;
	@FXML
	private DatePicker datePicker;

	FXMLLoader loader;
	private Stage stage;
	private Scene scene;
	private Parent root;

	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private String query;

	@FXML
	public void backAction(ActionEvent event) throws SQLException, IOException {
		BackToHome home = new BackToHome();
		home.backToHome(event);
	}

	public void displayAction(ActionEvent event) throws SQLException, IOException {
		String startLoc = startLocationNameInput.getText();
		String destLoc = destinationNameInput.getText();
		LocalDate myDate = datePicker.getValue();

		if (startLoc.equals("") || myDate == null || destLoc.equals("")) {
//			System.out.println("Missing");
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText(null);
			alert.setContentText("Missing data field(s). Please fill out all the fields");
			alert.showAndWait();
		} else {
			String date = myDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			if (isNoRecordsReturned(startLoc, destLoc, date)) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setHeaderText(null);
				alert.setContentText("No records match the specified data");
				alert.showAndWait();
				return; // Exit the method if no records found
			} else {
				loader = new FXMLLoader(getClass().getResource("DisplayTripScheduleResult.fxml"));
				root = loader.load();

				DisplayTripScheduleResultController tripController = loader.getController();
				tripController.setData(startLoc, destLoc, date);
				tripController.loadTrip();

				scene = new Scene(root);
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.setScene(scene);
				stage.setTitle("Trip Schedule Result");
				stage.show();
			}
		}
	}

	private boolean isNoRecordsReturned(String startLoc, String destLoc, String date) {
		connection = DBConnect.getConnect();
		query = "SELECT 1 FROM tripoffering AS T1 JOIN trip AS T2 ON T1.TripNumber = T2.TripNumber "
				+ "WHERE T2.StartLocationName = ? AND T2.DestinationName = ? AND T1.Date = ?;";
		boolean isNoRecordsReturned = false;
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, startLoc);
			preparedStatement.setString(2, destLoc);
			preparedStatement.setString(3, date);
			resultSet = preparedStatement.executeQuery();

			isNoRecordsReturned = !resultSet.next();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return isNoRecordsReturned;
	}
}
