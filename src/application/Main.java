package application;
	
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try 
		{
		    Parent root = FXMLLoader.load(getClass()
                    .getResource("Calendar.fxml"));
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass()
			 //.getResource("fxcalendar.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Scene Buildered");
			primaryStage.show();
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
