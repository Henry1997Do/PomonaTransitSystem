package pts.q2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import pts.DBConnect;

public class DeleteTripOfferingController extends TripOfferingController {
	String query = null;
	Connection connection = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	private String tripNumber;
	private String date;
	private String scheduledStartTime;

	public void deleteTripOfferingAction(ActionEvent event) throws SQLException, IOException {
		try {
			tripNumber = tripNumberInput.getText();
			LocalDate myDate = datePicker.getValue();
			scheduledStartTime = scheduledStartTimeInput.getText();

			if (tripNumber.equals("") || myDate == null || scheduledStartTime.equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setHeaderText(null);
				alert.setContentText("Missing data field(s). Please fill out all the fields");
				alert.showAndWait();
			} else {
				date = myDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				connection = DBConnect.getConnect();
				query = "DELETE FROM `lab4`.`tripoffering`\r\n"
						+ "WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, tripNumber);
				preparedStatement.setString(2, date);
				preparedStatement.setString(3, scheduledStartTime);
				preparedStatement.executeUpdate();
				preparedStatement.close();
				connection.close();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Remove Trip Offering");
				alert.setHeaderText(null);
				alert.setContentText("Trip offering removed successfully!");
				alert.showAndWait();

				tripNumberInput.clear();
				datePicker.setValue(null);
				scheduledStartTimeInput.clear();
				loadTripOffering();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to remove trip offering");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}
}
