package application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
	
	public void loadEventData(EventModel event, CalendarController calendarController, LocalDate currentDate)
	{
		if(event != null)
		{
			isEventTitleValid = true;
			isStartTimeValid = true;
			isEndTimeValid = true;
			eventTitleTextField.setText(event.getTitle());
			startTimeTextField.setText(event.getStart().toString());
			endTimeTextField.setText(event.getEnd().toString());
			addEventButton.setText("Modify event");
			addEventButton.setOnAction(null);
			addEventButton.setOnAction(e -> {
				modifyEvent(e, calendarController, event);
				close();
			});
			removeEventButton.setVisible(true);
			removeEventButton.setOnAction(null);
			removeEventButton.setOnAction(e -> {
				removeEvent(e, calendarController, event);
				close();
			});
		}
		else
		{
			isEventTitleValid = false;
			isStartTimeValid = false;
			isEndTimeValid = false;
			addEventButton.setText("Add event");
			addEventButton.setDisable(true);
			addEventButton.setOnAction(null);
			addEventButton.setOnAction(e -> {
				addEvent(e, calendarController, currentDate);
				close();
			});
		}
		eventTitleTextField.focusedProperty().addListener(e ->{
			titleValidation(e);
			changeAddEventButtonDisable();
		});
		startTimeTextField.focusedProperty().addListener(e ->{
			timeValidation(e, startTimeTextField);
			changeAddEventButtonDisable();
		});
		endTimeTextField.focusedProperty().addListener(e ->{
			timeValidation(e, endTimeTextField);
			changeAddEventButtonDisable();
		});
	}
	

	private void addEvent(ActionEvent e, CalendarController controller, LocalDate currentDate)
	{
		String title = eventTitleTextField.getText();
		LocalTime startTime = LocalTime.parse(startTimeTextField.getText());
		LocalTime endTime = LocalTime.parse(endTimeTextField.getText());
		EventModel event = new EventModel(currentDate, startTime, endTime, title);
		calendarModel.addEvent(event);
		controller.loadView();
	}
	
	private void removeEvent(ActionEvent e, CalendarController controller, EventModel event) 
	{
		calendarModel.removeEvent(event);
		controller.loadView();
	}
	
	private void modifyEvent(ActionEvent e, CalendarController controller, EventModel event) 
	{
		String title = eventTitleTextField.getText();
		LocalTime startTime = LocalTime.parse(startTimeTextField.getText());
		LocalTime endTime = LocalTime.parse(endTimeTextField.getText());
		event.setTitle(title);
		event.setStart(startTime);
		event.setEnd(endTime);
		calendarModel.modifyEvent();
		controller.loadView();
	}
	
	private void changeAddEventButtonDisable()
	{
		if(isValid())
			addEventButton.setDisable(false);
		else
			addEventButton.setDisable(true);
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
	
	private boolean isValid()
	{
		return isEventTitleValid && isStartTimeValid && isEndTimeValid;
	}
		
}
