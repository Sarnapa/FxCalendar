package application;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CalendarController 
{
	private CalendarModel calendarModel;
	
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
			day = day.plusDays(1);
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
