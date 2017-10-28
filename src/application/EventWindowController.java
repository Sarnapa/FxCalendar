package application;

import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EventWindowController 
{
	private CalendarModel calendarModel;
	private LocalDate currentDate;
	private Stage stage;
	
	@FXML
	private TextField eventTitleTextField;
	@FXML
	private TextField startTimeTextField;
	@FXML
	private TextField endTimeTextField;
	@FXML
	private Button removeEventButton;
	@FXML
	private Button addEventButton;
	
	public CalendarModel getCalendarModel() 
	{
		return calendarModel;
	}

	public void setCalendarModel(CalendarModel calendarModel) 
	{
		this.calendarModel = calendarModel;
	}
	
	public LocalDate getCurrentDate() 
	{
		return currentDate;
	}

	public void setCurrentDate(LocalDate currentDate) 
	{
		this.currentDate = currentDate;
	}
	
	
	public EventWindowController()
	{}
	
	void init(Parent root)
	{
		stage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().
				getResource("EventWindow.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("Event");
		stage.setMinWidth(400);
		stage.setMinHeight(250);
		stage.setMaxHeight(250);
		stage.show();
	}
	
	@FXML
	void close()
	{
		stage.close();
	}
		
}
