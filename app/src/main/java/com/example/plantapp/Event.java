package com.example.plantapp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event
{
    public static ArrayList<Event> eventsList = new ArrayList<>();


    public static ArrayList<Event> eventsForDate(LocalDate date)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }


    private String name;
    private LocalDate date;
    private String eventCellplant;
    private String event_type;

    public Event(String name, LocalDate date, String eventCellplant, String event_type)
    {
        this.name = name;
        this.date = date;
        this.eventCellplant=eventCellplant;
        this.event_type=event_type;
    }

    public String getEventCellplant() {
        return eventCellplant;
    }

    public void setEventCellplant(String eventCellplant) {
        this.eventCellplant = eventCellplant;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

}
