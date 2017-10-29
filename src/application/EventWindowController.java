package application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.function.Predicate;
import java.util.logging.Filter;

import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EventWindowController 
{
	private CalendarModel calendarModel;
	private LocalDate currentDate;
	private Stage stage;
	private boolean isEventTitleValid;
	private boolean isStartTimeValid;
	private boolean isEndTimeValid;
	
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
		stage.initStyle(StageStyle.UTILITY);
		stage.setMinWidth(400);
		stage.setMinHeight(250);
		stage.setMaxHeight(250);
	}
	
	public void showWindow()
	{
		stage.show();
	}
	
	public boolean isShowing()
	{
		return stage.isShowing();
	}
	
	@FXML
	void close()
	{
		stage.close();
	}
	
	public void loadEventData(EventModel event, LocalDate currentDate)
	{
		if(event != null)
		{
			eventTitleTextField.setText(event.getTitle());
			startTimeTextField.setText(event.getStart().toString());
			endTimeTextField.setText(event.getEnd().toString());
			removeEventButton.setVisible(true);
			addEventButton.setText("Modify event");
			isEventTitleValid = true;
			isStartTimeValid = true;
			isEndTimeValid = true;
		}
		else
		{
			addEventButton.setText("Add event");
			isEventTitleValid = false;
			isStartTimeValid = false;
			isEndTimeValid = false;
		}
		eventTitleTextField.focusedProperty().addListener(e ->{
			titleValidation(e);
		});
		startTimeTextField.focusedProperty().addListener(e ->{
			timeValidation(e, startTimeTextField);
		});
		endTimeTextField.focusedProperty().addListener(e ->{
			timeValidation(e, endTimeTextField);
		});
	}
	
	private void titleValidation(Observable e)
	{
		String title = eventTitleTextField.getText();
		Tooltip tooltip = new Tooltip();
		if(title.isEmpty())
		{
			tooltip.setText("Title must not be empty!");
			eventTitleTextField.setTooltip(tooltip);
			eventTitleTextField.getStyleClass().add("invalidTextField");
			isEventTitleValid = false;
		}
		else if(title.length() > 30)
		{
			tooltip.setText("Title must be at most 30 characters in length!");
			eventTitleTextField.setTooltip(tooltip);
			eventTitleTextField.getStyleClass().add("invalidTextField");
			isEventTitleValid = false;
		}
		else
		{
			eventTitleTextField.getStyleClass().removeIf(style -> style == "invalidTextField");
			eventTitleTextField.setTooltip(null);
			isEventTitleValid = true;
		}
	}
	
	private void timeValidation(Observable e, TextField timeField)
	{
		String timeString = timeField.getText(); 
		Tooltip tooltip = new Tooltip();
		if(timeString.isEmpty())
		{
			tooltip.setText("Time field must not be empty!");
			timeField.setTooltip(tooltip);
			timeField.getStyleClass().add("invalidTextField");
			setIsTimeValid(timeField, false);
		}
		else
		{
			try
			{
				LocalTime.parse(timeString);
			}
			catch(DateTimeParseException e1)
			{
				tooltip.setText("Wrong format!");
				timeField.setTooltip(tooltip);
				timeField.getStyleClass().add("invalidTextField");
				setIsTimeValid(timeField, false);
				return;
			}
			timeField.getStyleClass().removeIf(style -> style == "invalidTextField");
			timeField.setTooltip(null);
			setIsTimeValid(timeField, true);	
		}
	}
	
	private void setIsTimeValid(TextField timeField, boolean value)
	{
		if(timeField.equals(startTimeTextField))
			isStartTimeValid = value;
		else
			isEndTimeValid = value;
	}
	
}
