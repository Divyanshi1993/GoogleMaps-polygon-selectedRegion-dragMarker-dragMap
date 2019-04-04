package com.parkman.View.utility;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class ReadJsonFile {
    public String readJSONFromAsset( Context c)  {
        String json = null;
        try {
            InputStream is = c.getAssets().open("mapData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
