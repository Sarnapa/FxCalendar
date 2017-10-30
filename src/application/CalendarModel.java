package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarModel 
{
	private static final String sourcePath = "EventsDB";
	private List<EventModel> eventsList;
	private LocalDate firstDateOfWeek = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);
	private LocalDate today = LocalDate.now();
	
	public LocalDate getFirstDateOfWeek() 
	{
		return firstDateOfWeek;
	}
	
	public void setFirstDateOfWeek(LocalDate firstDateOfWeek)
	{
		this.firstDateOfWeek = firstDateOfWeek;
	}
	
	public LocalDate getToday() 
	{
		return today;
	}
	
	public void setToday(LocalDate today)
	{
		this.today = today;
	}
	
	public CalendarModel()
	{
		eventsList = new ArrayList<>();
		readSource();
	}
	
	private void readSource()
	{
		try 
		{
			FileInputStream fileIn = new FileInputStream(sourcePath);
			ObjectInputStream  in = new ObjectInputStream(fileIn);
			eventsList = (List<EventModel>)in.readObject();
			in.close();
			fileIn.close();
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("File not found");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}		
	}
	
	private void writeToSource()
	{
		try
		{
			FileOutputStream fileOut = new FileOutputStream(sourcePath);
			fileOut.flush();
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(eventsList);
	        out.close();
	        fileOut.close();
	    }
		catch (FileNotFoundException e) 
		{
			System.out.println("File not found");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public List<EventModel> getEvents(LocalDate date)
	{
		List<EventModel> currentEventsList = eventsList.stream().filter(e -> date.isEqual(e.getDay())).collect(Collectors.toList());
		currentEventsList.sort(Comparator.comparing(EventModel::getStart));
		return currentEventsList;
	}

	public void addEvent(EventModel event)
	{
		eventsList.add(event);
		writeToSource();
	}
	
	public void removeEvent(EventModel event)
	{
		eventsList.remove(event);
		writeToSource();
	}
	
	public void modifyEvent()
	{
		writeToSource();
	}

}
