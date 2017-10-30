package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;



public class Main extends Application 
{
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
		    Parent root = FXMLLoader.load(getClass()
                    .getResource("Calendar.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().
					getResource("Calendar.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Calendar");
			primaryStage.setMinWidth(725);
			primaryStage.setMinHeight(560);
			primaryStage.show();
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}

}
