package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CalendarController 
{
	private CalendarModel calendarModel;
	private EventWindowController eventWindowController;
	
	@FXML
	private GridPane leftWeeksPanel;
	@FXML
	private GridPane rightWeeksPanel;
	@FXML
	private GridPane calendarGrid;
	
	
	public CalendarController()
	{
		calendarModel = new CalendarModel();
		//calendarModel.addEvent(new EventModel(LocalDate.now(), LocalTime.MIDNIGHT, LocalTime.NOON, "xD"));
	} 
	
	@FXML
	void initialize()
	{
		loadView();
	}
	
	
	public void prevWeek()
	{
		LocalDate firstDateOfWeek = calendarModel.getFirstDateOfWeek();
		calendarModel.setFirstDateOfWeek(firstDateOfWeek.minusDays(7));
		loadView();
	}
	
	public void nextWeek()
	{
		LocalDate firstDateOfWeek = calendarModel.getFirstDateOfWeek();
		calendarModel.setFirstDateOfWeek(firstDateOfWeek.plusDays(7));
		loadView();
	}
	
	public void addDoubleClick(Node node, LocalDate currentDate)
	{
		node.setOnMouseClicked(e -> {
			if(e.getClickCount() == 2)
			{
				FXMLLoader EventWindowLoader = new FXMLLoader(getClass().getResource("EventWindow.fxml"));
				Parent root;
				try 
				{
					root = EventWindowLoader.load();
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
					return;
				}
				eventWindowController = EventWindowLoader.getController();
				eventWindowController.setCalendarModel(calendarModel);
				eventWindowController.init(root);
			}
		});
	}
	
	private void loadView()
	{
		calendarModel.setToday(LocalDate.now());
		LocalDate firstDateOfWeek = calendarModel.getFirstDateOfWeek();
		loadCalendarGrid(firstDateOfWeek);
		loadWeeksPanel(firstDateOfWeek);
	}
	
	private void loadCalendarGrid(LocalDate day)
	{
		for(Node node: calendarGrid.getChildren())
		{
			addDoubleClick(node, day);
			Label label = getCalendarGridCellLabel(node);
			label.setText(day.getMonth().toString() + " " + day.getDayOfMonth());
			if(day.isEqual((calendarModel.getToday())))
			{
				node.getStyleClass().clear();
				node.getStyleClass().add("todayCell");
				label.getStyleClass().clear();
				label.getStyleClass().add("todayLabel");
			}
			else
			{
				node.getStyleClass().clear();
				node.getStyleClass().add("dayCell");
				label.getStyleClass().clear();
				label.getStyleClass().add("date");
			}
			loadEvents(node, day);
			day = day.plusDays(1);
		}
	}
	
	private void loadEvents(Node node, LocalDate day) 
	{
		VBox vBox = (VBox)node;
		ObservableList<Node> children = vBox.getChildren();
		if(children.size() > 1)
			children.remove(1, children.size());
		List<EventModel> events = calendarModel.getEvents(day);
		for(EventModel event: events)
		{
			Label eventLabel = new Label();
			eventLabel.setMaxWidth(Double.MAX_VALUE);
			if(day.isEqual((calendarModel.getToday())))
			{
				eventLabel.getStyleClass().add("eventLabelToday");
			}
			else
			{
				eventLabel.getStyleClass().add("eventLabel");
			}
			eventLabel.setText(event.getStart() + " - " + event.getEnd() + " " + event.getTitle());
			children.add(eventLabel);
		}
	}

	private void loadWeeksPanel(LocalDate day)
	{
		loadPanel(leftWeeksPanel, day);
		loadPanel(rightWeeksPanel, day);
	}
	
	private void loadPanel(GridPane panel, LocalDate day)
	{
		WeekFields weekFields = WeekFields.of(Locale.getDefault()); 
		for(Node node: panel.getChildren())
		{
			Label label = (Label)node;
			int weekNumber = day.get(weekFields.weekOfWeekBasedYear());
			if(weekNumber > 9)
				label.setText("W" + weekNumber + '\n' + day.getYear());
			else
				label.setText("W0" + weekNumber + '\n' + day.getYear());
			day = day.plusDays(7);
		}
	}
	
	private Label getCalendarGridCellLabel(Node node)
	{
		return (Label)((VBox) node).getChildren().get(0);
	}
		
}
