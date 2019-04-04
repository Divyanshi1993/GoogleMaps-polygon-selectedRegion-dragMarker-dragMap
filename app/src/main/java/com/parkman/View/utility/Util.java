package com.parkman.View.utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.ui.IconGenerator;
import com.parkman.R;
import com.parkman.View.ui.MapsActivity;
import com.parkman.controller.DataController;
import com.parkman.model.Zones;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.Bitmap.Config.ARGB_8888;

public class Util {
    private List<Polygon> polygonList = new ArrayList<>();
    private Polygon polygon;
    private DataController dataController;
    private int payment_is_allowed_color = 0xfff8bbd0;
    private int payment_is_not_allowed_color = 0xfffff9c4;
    private int color_polygon_selected = 0xffe6ee9c;
    private int strokeColor_pay_alwd = 0xfff48fb1;
    private int strokeColor_pay_nt_alwd = 0xfffff176;
    private int markerIconColor = 0xffF44336;
    private String parkinZoneName;

    public Util() {

    }

    public Util(DataController dataController) {
        this();
        this.dataController = dataController;
    }

    private static final int POLYGON_STROKE_WIDTH_PX = 3;
    private List<PatternItem> pattern = null;


    public void createPolygons(GoogleMap mMap, int zoneIndex) {
        polygon = mMap.addPolygon(new PolygonOptions()
                .addAll(dataController.getPolyPoints(zoneIndex)));
        polygon.setStrokePattern(pattern);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setTag(zoneIndex);
        polygonList.add(polygon);
    }

    public Double parseToLatLang(String direction, Context context) {
        Double latLng = null;
        if (direction.equals(context.getString(R.string.north))) {
            latLng = Double.parseDouble(dataController.getBoundData().getNorth());
        } else if (direction.equals(context.getString(R.string.south))) {
            latLng = Double.parseDouble(dataController.getBoundData().getSouth());
        } else if (direction.equals(context.getString(R.string.west))) {
            latLng = Double.parseDouble(dataController.getBoundData().getWest());
        } else if (direction.equals(context.getString(R.string.east))) {
            latLng = Double.parseDouble(dataController.getBoundData().getEast());
        }
        return latLng;
    }

    ;

    public void hilightPolygonwithUpdatedMarker(MapsActivity context, LatLng midLatLng, Marker marker, View zodetaillayout) {
        parkinZoneName = context.getString(R.string.not_in_zone);
        zodetaillayout.setVisibility(View.GONE);

        marker.setIcon(BitmapDescriptorFactory.fromBitmap(makeBitmap(context)));

        for (Polygon polygon : polygonList) {
            marker.setVisible(true);

            int polygonTag = Integer.parseInt(polygon.getTag().toString());

            Zones[] zoneData = dataController.getZoneData();

            String service_price = zoneData[polygonTag].getService_price();

            String payment_is_allowed = zoneData[polygonTag].getPayment_is_allowed();

            if (PolyUtil.containsLocation(midLatLng, polygon.getPoints(), false)) {

                zodetaillayout.setVisibility(View.VISIBLE);

                updateZoneDetail(context, zoneData[polygonTag]);

                parkinZoneName = context.getString(R.string.parkingMsg)+" " + zoneData[polygonTag].getName();

                polygon.setFillColor(color_polygon_selected);

                IconGenerator icg = new IconGenerator(context);
                icg.setColor(markerIconColor);
                icg.setTextAppearance(R.style.MarkerTextstyle);// green background
                Bitmap bm = icg.makeIcon(service_price);

                // Bitmap bitmap = makeBitmap(context,service_price);

                marker.setIcon(BitmapDescriptorFactory.fromBitmap(bm));
                marker.showInfoWindow();
            } else {
                if (payment_is_allowed.equals("1")) {
                    polygon.setFillColor(payment_is_allowed_color);
                    polygon.setStrokeColor(strokeColor_pay_alwd);
                } else {
                    polygon.setFillColor(payment_is_not_allowed_color);
                    polygon.setStrokeColor(strokeColor_pay_nt_alwd);
                }

            }
        }
    }

    public void showSnackbar(MapsActivity mapsActivity) {
        Snackbar.make(mapsActivity.findViewById(android.R.id.content), parkinZoneName, Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE)
                .show();
    }
    public Bitmap makeBitmap(Context context) {
        Resources resources = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_location_on);
        return bitmap;
    }

    private Bitmap makeBitmap1(Context context, String text) {
        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_location_on);
        bitmap = bitmap.copy(ARGB_8888, true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE); // Text color
        paint.setTextSize(18 * scale); // Text size
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE); // Text shadow
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        int x = bitmap.getWidth() - bounds.width(); // 10 for padding from right
        int y = bounds.height();
        canvas.drawText(text, 0, 50, paint);

        Bitmap bitmapResize = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
        return bitmapResize;
    }

    private void updateZoneDetail(MapsActivity context, Zones zoneData) {
        TextView parking_name = context.findViewById(R.id.parking_name);
        TextView payment_is_alwd = context.findViewById(R.id.payment_is_alwd);
        TextView max_duration = context.findViewById(R.id.max_duration);
        TextView service_price = context.findViewById(R.id.service_price);
        TextView depth = context.findViewById(R.id.depth);
        TextView draw = context.findViewById(R.id.draw);
        TextView sticker = context.findViewById(R.id.sticker);
        TextView currency = context.findViewById(R.id.currency);
        TextView country = context.findViewById(R.id.country);
        TextView contact_email = context.findViewById(R.id.contact_email);
        TextView provider_id = context.findViewById(R.id.provider_id);
        TextView provider_name = context.findViewById(R.id.provider_name);

        parking_name.setText(zoneData.getName());

        String payment_is_alwd_data = zoneData.getPayment_is_allowed();
        payment_is_alwd.setText(payment_is_alwd_data.equals("0") ? "Yes" : "No");

        max_duration.setText(zoneData.getMax_duration());
        service_price.setText(zoneData.getService_price());
        depth.setText(zoneData.getDepth());
        draw.setText(zoneData.getDraw());

        String sticker_data = zoneData.getSticker_required();
        sticker.setText(sticker_data.equals("0") ? context.getString(R.string.not_required) : context.getString(R.string.required));

        currency.setText(zoneData.getCurrency());
        country.setText(zoneData.getCountry());
        contact_email.setText(zoneData.getContact_email());
        provider_id.setText(zoneData.getProvider_id());
        provider_name.setText(zoneData.getProvider_name());

    }
}
