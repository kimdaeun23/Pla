package com.example.plantapp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class EventData
{

     String name="";
     String date="";
     String eventCellplant="";
     String event_type="";

    public EventData(String name, String date, String eventCellplant, String event_type)
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

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

}
