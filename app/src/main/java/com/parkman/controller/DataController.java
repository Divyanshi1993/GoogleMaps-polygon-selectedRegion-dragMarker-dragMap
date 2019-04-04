package com.parkman.controller;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.parkman.View.utility.ReadJsonFile;
import com.parkman.model.Bounds;
import com.parkman.model.MapData;
import com.parkman.model.Zones;

import java.util.ArrayList;
import java.util.List;


public class DataController {
    private ReadJsonFile readJsonFile;
    private Context context;

    public DataController(Context context) {
        readJsonFile = new ReadJsonFile();
        this.context = context;

    }

    public MapData getJsonData() {
        MapData mapDataobj = new Gson().fromJson(readJsonFile.readJSONFromAsset(context), MapData.class);
        return mapDataobj;
    }


    public List<LatLng> getPolyPoints(int zoneIndex) {
        List<LatLng> polygonPoints = new ArrayList<LatLng>();
        String latlngs[] = (getJsonData().getLocation_data().getZones()[zoneIndex].getPolygon()).split(",");
        for (String latlng: latlngs) {
            String[] polySpacesplit = latlng.trim().split("\\s+");
            polygonPoints.add(new LatLng(Double.parseDouble(polySpacesplit[0]), Double.parseDouble(polySpacesplit[1])));
        }
        return polygonPoints;

    }

    public Zones[] getZoneData(){
        return getJsonData().getLocation_data().getZones();

    }
    public Bounds getBoundData(){
        return getJsonData().getLocation_data().getBounds();

    }

}