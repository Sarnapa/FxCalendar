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
import javafx.scene.control.ScrollPane;
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
	
	public void addDoubleClick(EventModel event, Node node, LocalDate currentDate)
	{
		node.setOnMousePressed(e -> {
			if(e.getClickCount() == 2 && (eventWindowController == null || !eventWindowController.isShowing()))
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
				eventWindowController.loadEventData(event, this, currentDate);
				eventWindowController.showWindow();
			}
		});
	}
	
	public void loadView()
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
			Label label = getCalendarGridCellLabel(node);
			ScrollPane eventsScrollPane = getEventsScrollPane(node);
			label.setText(day.getMonth().toString() + " " + day.getDayOfMonth());
			if(day.isEqual((calendarModel.getToday())))
			{
				node.getStyleClass().clear();
				node.getStyleClass().add("todayCell");
				label.getStyleClass().clear();
				label.getStyleClass().add("todayLabel");
				eventsScrollPane.getStyleClass().clear();
				eventsScrollPane.getStyleClass().add("eventsScrollPaneToday");
			}
			else
			{
				node.getStyleClass().clear();
				node.getStyleClass().add("dayCell");
				label.getStyleClass().clear();
				label.getStyleClass().add("date");
				eventsScrollPane.getStyleClass().clear();
			}
			loadEvents(node, day);	
			addDoubleClick(null, node, day);
			day = day.plusDays(1);
		}
	}
	
	private void loadEvents(Node node, LocalDate day) 
	{
		VBox eventsVBox = getEventsVBox(node);
		ObservableList<Node> children = eventsVBox.getChildren();
		if(children.size() > 0)
			children.remove(0, children.size());
		List<EventModel> events = calendarModel.getEvents(day);
		for(EventModel event: events)
		{
			Label eventLabel = new Label();
			eventLabel.setMaxWidth(Double.MAX_VALUE);
			if(day.isEqual((calendarModel.getToday())))
			{
				eventLabel.getStyleClass().clear();
				eventLabel.getStyleClass().add("eventLabelToday");
			}
			else
			{
				eventLabel.getStyleClass().clear();
				eventLabel.getStyleClass().add("eventLabel");
			}
			eventLabel.setText(event.getStart() + " - " + event.getEnd() + " " + event.getTitle());
			addDoubleClick(event, eventLabel, day);
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
	
	private ScrollPane getEventsScrollPane(Node node)
	{
		VBox vBox = (VBox) node;
		return (ScrollPane) vBox.getChildren().get(1);
	}
	
	private VBox getEventsVBox(Node node)
	{
		ScrollPane scrollPane = getEventsScrollPane(node);
		return (VBox) scrollPane.getContent();
	}
		
}
