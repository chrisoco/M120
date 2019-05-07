package rcas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import rcas.controller.RCASMainController;

/**
 * Main Class
 *
 * @author Christopher O'Connor, Umut Savas
 * @version 1.0
 * @since 2019-04-16
 */

public class RCASMain extends Application {

	public static void main(String[] args) {
		launch(RCASMain.class);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(RCASMain.class.getResource("view/RCASMainView.fxml"));

		BorderPane mainPane = (BorderPane) fxmlLoader.load();

		primaryStage.setScene(new Scene(mainPane));
		primaryStage.setTitle("M120 Race Car Analysis Studio");
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image(RCASMain.class.getResourceAsStream("view/imgs/icon.png")));

		primaryStage.centerOnScreen();

		KeyCombination save   = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
		KeyCombination delete = new KeyCodeCombination(KeyCode.DELETE);
		KeyCombination newCar = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
		RCASMainController controller = fxmlLoader.getController();

		//Create save shortcut (CTRL + s)
		Runnable rn = controller::saveRaceCar;
		primaryStage.getScene().getAccelerators().put(save, rn);

		//Create delete shortcut (Delete)
		rn = controller::deleteRaceCar;
		primaryStage.getScene().getAccelerators().put(delete, rn);

		//Create new car shortcut (CTRL + n)
		rn = controller::moveToNewCar;
		primaryStage.getScene().getAccelerators().put(newCar, rn);


		primaryStage.show();

	}

}
