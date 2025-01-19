/**
 * 
 */
package org.dimigo.billboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * <pre>
 * org.dimigo.billboard
 *   |_ BillboardMain
 *
 * 1. 개요 :
 * 2. 작성일 : 2017. 6. 23.
 * </pre>
 *
 * @author      : 공경배
 * @version     : 1.0
 */
public class BillboardMain extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("billboard.fxml"));
		
		stage.setScene(new Scene(root));
		stage.setTitle("Billboard Chart");
		stage.setResizable(false);
		stage.centerOnScreen();
		stage.show();
	}
}
