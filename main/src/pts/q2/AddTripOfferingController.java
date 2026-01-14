package pts.q2;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;

import javafx.event.ActionEvent;

public class AddTripOfferingController extends TripOfferingController {
	@FXML
	public void openAddTripOfferingForm(ActionEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/pts/q2/AddTripOfferingFormFX.fxml"));
			Stage stage = new Stage();
			stage.setScene(new Scene(loader.load()));
			stage.setTitle("Add Trip Offering");
//			stage.initModality(Modality.APPLICATION_MODAL);

			// Get the controller and pass the main controller instance
			AddTripOfferingFormController controller = loader.getController();
			controller.setMainController(this);

			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
