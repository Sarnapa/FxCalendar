package application;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventModel implements Serializable 
{
	
	private static final long serialVersionUID = -5196396121852716915L;
	private LocalDate day;
	private LocalTime start;
	private LocalTime end;
	private String title;
	
	public LocalDate getDay() 
	{
		return day;
	}
	public void setDay(LocalDate day) 
	{
		this.day = day;
	}
	
	public LocalTime getStart() 
	{
		return start;
	}
	public void setStart(LocalTime start) 
	{
		this.start = start;
	}
	
	public LocalTime getEnd() 
	{
		return end;
	}
	public void setEnd(LocalTime end) 
	{
		this.end = end;
	}
	
	public String getTitle() 
	{
		return title;
	}
	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	public EventModel(LocalDate day, LocalTime start, LocalTime end, String title)
	{
		this.day = day;
		this.start = start;
		this.end = end;
		this.title = title;
	}
	
}
