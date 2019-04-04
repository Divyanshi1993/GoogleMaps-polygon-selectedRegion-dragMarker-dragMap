package com.parkman.View.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.parkman.R;
import com.parkman.View.utility.Util;
import com.parkman.controller.DataController;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveStartedListener , View.OnClickListener {

    private GoogleMap mMap;
    private DataController dataController;
    private Util util;
    private Marker marker;
    private int zoneId;
    private Button parkinButton;
    private View zonedetaillayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        parkinButton = findViewById(R.id.parkinButton);
        dataController = new DataController(this);
        util = new Util(dataController);
        zonedetaillayout = findViewById(R.id.zonedetail);


    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker to current location.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String[] latlong = dataController.getJsonData().getCurrent_location().split(",");

        // Add a marker in place and move the camera
        LatLng currentLocation = new LatLng(Double.parseDouble(latlong[0]), Double.parseDouble(latlong[1]));

        marker=   mMap.addMarker(new MarkerOptions().position(currentLocation).draggable(true)
                .icon(BitmapDescriptorFactory.fromBitmap(util.makeBitmap(MapsActivity.this))));

        setBounds();

        //Creating Polygons
        for (int zoneIndex = 0; zoneIndex < dataController.getZoneData().length; zoneIndex++) {
            util.createPolygons(mMap,zoneIndex);
        }

        initClickListner();


    }

    private void setBounds() {
        // Itializing Bounds
        LatLngBounds bounds = new LatLngBounds(
                new LatLng(util.parseToLatLang( getString(R.string.south), MapsActivity.this), util.parseToLatLang(getString(R.string.west), MapsActivity.this)),
                new LatLng(util.parseToLatLang( getString(R.string.north), MapsActivity.this), util.parseToLatLang(getString(R.string.east), MapsActivity.this)));

        mMap.setLatLngBoundsForCameraTarget(bounds);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 14));

    }


    private void initClickListner() {
        parkinButton.setOnClickListener(this);
        mMap.setOnPolylineClickListener(this);
        mMap.setOnPolygonClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
    }


    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    @Override
    public void onMapLongClick(LatLng point) {

        mMap.addMarker(new MarkerOptions()
                .position(point)
                .draggable(true));

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng midLatLng = marker.getPosition();
        marker.setPosition(midLatLng);
        util.hilightPolygonwithUpdatedMarker(MapsActivity.this,midLatLng,marker,zonedetaillayout);
    }

    @Override
    public void onCameraIdle() {
        LatLng midLatLng = mMap.getCameraPosition().target;
        marker.setPosition(midLatLng);
        util.hilightPolygonwithUpdatedMarker(MapsActivity.this,midLatLng,marker,zonedetaillayout);

    }

    @Override
    public void onCameraMoveStarted(int i) {
        marker.setVisible(false);
    }

    @Override
    public void onClick(View v) {
        util.showSnackbar(this);

    }
}
