package pts;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BackToHome {
	FXMLLoader loader;
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	public void backToHome(ActionEvent event) throws SQLException, IOException {
		loader = new FXMLLoader(getClass().getResource("Home.fxml"));
		root = loader.load(); 
		
    	scene = new Scene(root);
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.setTitle("Home");
		stage.show();
	}
}
