package com.parkman.model;

public class MapData
{
    private Location_data location_data;

    private String current_location;

    public Location_data getLocation_data ()
    {
        return location_data;
    }

    public void setLocation_data (Location_data location_data)
    {
        this.location_data = location_data;
    }

    public String getCurrent_location ()
    {
        return current_location;
    }

    public void setCurrent_location (String current_location)
    {
        this.current_location = current_location;
    }

}

