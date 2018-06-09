package sample.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	/**
	 * start
	 */
    @Override
    public void start(Stage primaryStage) throws Exception{
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("graphicalEditor.fxml"));
			Scene scene = new Scene((Parent)loader.load());
            primaryStage.setTitle("Graphical Editor");
			primaryStage.setScene(scene);
			primaryStage.show();			
		} catch(Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
