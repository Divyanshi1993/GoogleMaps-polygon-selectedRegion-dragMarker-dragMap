package com.parkman.model;

public class Location_data
{
    private Bounds bounds;

    private Zones[] zones;

    public Bounds getBounds ()
    {
        return bounds;
    }

    public void setBounds (Bounds bounds)
    {
        this.bounds = bounds;
    }

    public Zones[] getZones ()
    {
        return zones;
    }

    public void setZones (Zones[] zones)
    {
        this.zones = zones;
    }

}

