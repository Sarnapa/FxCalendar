package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(eventsList);
	        out.close();
	        fileOut.close();
	        System.out.println("Liczba eventow: " + eventsList.size());
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
	
	void addEvent(EventModel event)
	{
		eventsList.add(event);
		writeToSource();
	}
	
	void removeEvent(EventModel event)
	{
		eventsList.remove(event);
		writeToSource();
	}

}
