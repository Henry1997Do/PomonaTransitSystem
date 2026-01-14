module PomonaTransitSystem {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires java.logging;
	requires java.sql;
	
	opens pts to javafx.graphics, javafx.fxml;
	opens pts.q1 to javafx.graphics, javafx.fxml;
	opens pts.q2 to javafx.graphics, javafx.fxml;
	opens pts.q3 to javafx.graphics, javafx.fxml;
	opens pts.q4 to javafx.graphics, javafx.fxml;
	opens pts.q5 to javafx.graphics, javafx.fxml;
	opens pts.q6 to javafx.graphics, javafx.fxml;
	opens pts.q7 to javafx.graphics, javafx.fxml;
	opens pts.q8 to javafx.graphics, javafx.fxml;
	opens model to javafx.graphics, javafx.fxml, javafx.base;
}
