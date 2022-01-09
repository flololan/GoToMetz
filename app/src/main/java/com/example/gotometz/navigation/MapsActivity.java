package com.example.gotometz.navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gotometz.R;
import com.example.gotometz.listeners.AddOrEditSiteFormListener;
import com.example.gotometz.dao.CategoryService;
import com.example.gotometz.dao.SiteService;
import com.example.gotometz.map.MarkerDescription;
import com.example.gotometz.listeners.MyMapListener;
import com.example.gotometz.map.SearchSiteDialog;
import com.example.gotometz.listeners.SearchSiteListener;
import com.example.gotometz.dbmodels.Category;
import com.example.gotometz.dbmodels.Site;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, SearchSiteDialog.SearchModalDialogListener, LocationListener {

    public GoogleMap mMap;

    private Location location;

    private static int permissions = 0;

    private CategoryService categoryDao;
    private SiteService siteDao;

    LocationManager locationManager = null;
    private String provider;

    // Used when doing a site research
    private Category searchedCategory;
    private int searchRadius;
    public MyMapListener myMapListener;
    public FloatingActionButton mapSearchBTN;
    public FloatingActionButton mapAddBTN;
    public FloatingActionButton cornerFloatingBTN;
    boolean isAllFabsVisible;

    //TO-DO: refactor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Verifies permissions
        permissions = 0;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission of localisation
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            permissions++;

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //Permission pour écrire dans le fichier
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            } else {
                permissions++;

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //Permission pour lire le fichier
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                }
                else
                    permissions++;
            }
        }

        //Once all permissions are granted, launch the app
        if (permissions == 3)
            run();
    }

    //TO-DO: refactor
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Show message in case of user not granting permission
        Toast toast = Toast.makeText(this, this.getResources().getString(R.string.noPermissions), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED)
            toast.show();

        //If permissions have been given
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            MapsActivity.permissions++;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //If permission to write in file system has been given
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        } else
        if (requestCode != 3)
            MapsActivity.permissions++;

        if (MapsActivity.permissions == 2) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //If read access has been given
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            } else
                MapsActivity.permissions++;
        }

        if (MapsActivity.permissions == 3)
            run();
    }

    public void run() {
        setContentView(R.layout.activity_maps);

        //initialise menu
        BottomNavigationView bottomNavigationView = this.findViewById(R.id.menu);
        bottomNavigationView.setSelectedItemId(R.id.mapMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new Menu(this));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        //Get data from DB via DAO
        categoryDao = CategoryService.getInstance(this);
        siteDao = SiteService.getInstance(this);

        // Button listeners
        FloatingActionButton personSearchBTN = findViewById(R.id.personSearchBTN);
        personSearchBTN.setOnClickListener(new SearchSiteListener(this));

        FloatingActionButton personAddBTN = findViewById(R.id.personAddBTN);
        personAddBTN.setOnClickListener(new AddOrEditSiteFormListener(this, null));

        mapSearchBTN= findViewById(R.id.mapSearchBTN);
        mapAddBTN = findViewById(R.id.mapAddBTN);
        cornerFloatingBTN = findViewById(R.id.cornerFloatingBTN);

        //Floating Icon L-Menu logic
        personSearchBTN.hide();
        personAddBTN.hide();
        mapSearchBTN.hide();
        mapAddBTN.hide();

        isAllFabsVisible = false;

        cornerFloatingBTN.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {
                            personSearchBTN.show();
                            personAddBTN.show();
                            mapSearchBTN.show();
                            mapAddBTN.show();

                            isAllFabsVisible = true;
                        } else {
                            personSearchBTN.hide();
                            personAddBTN.hide();
                            mapSearchBTN.hide();
                            mapAddBTN.hide();

                            isAllFabsVisible = false;
                        }
                    }
                }
        );


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Get user localisation
        locationInitialization();

        mMap.setMyLocationEnabled(true);

        //Zoom on user's position
        if (location != null) {
            LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLatLng, 18);
            mMap.animateCamera(cameraUpdate);
        }

        mMap.setInfoWindowAdapter(new MarkerDescription(getLayoutInflater()));

        this.myMapListener = new MyMapListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Add a site/POI
        if (data != null && data.getExtras() != null)
            if (data.getLongExtra("id", -1) == -1) {
                Site site = new Site(data.getStringExtra("label"), data.getDoubleExtra("latitude", 0),
                        data.getDoubleExtra("longitude", 0), data.getStringExtra("postalAddress"),
                        categoryDao.findById(data.getLongExtra("categoryId", -1)), data.getStringExtra("summary"));

                //Add site to DB
                site.setId(siteDao.create(site));
            }
    }

    @Override
    public void onDialogPositiveClick(Location location, Category category, int radius) {
        searchedCategory = category;
        searchRadius = radius;

        searchSites(location);
    }

    @SuppressLint("NewApi")
    public void searchSites(Location location) {
        if (searchedCategory != null) {
            mMap.clear();

            //Add circle on map
            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(location.getLatitude(), location.getLongitude()))
                    .radius(searchRadius)
                    .strokeColor(Color.TRANSPARENT)
                    .strokeWidth(0.0f)
                    .fillColor(Color.argb(0.01f, 120, 158, 165)));

            //Add markers to the map
            Location siteLocation = new Location("");
            LatLng siteLatLng;
            double siteDistance;
            for (Site site : siteDao.findByCategory(searchedCategory)) {
                siteLocation.setLatitude(site.getLatitude());
                siteLocation.setLongitude(site.getLongitude());

                siteLatLng = new LatLng(site.getLatitude(), site.getLongitude());

                siteDistance = location.distanceTo(siteLocation);
                siteDistance = Math.round(siteDistance * 100) / 100.0;

                //Check if marker is inside of search radius
                if (siteDistance <= searchRadius)
                    mMap.addMarker(new MarkerOptions()
                            .position(siteLatLng)
                            .title(site.getLabel())
                            .snippet(site.getSummary() + "\n" + getString(R.string.distance) + " " + siteDistance + " m"));
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void locationInitialization() {
            try {
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                provider = LocationManager.NETWORK_PROVIDER;


                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(true);
                criteria.setBearingRequired(true);
                criteria.setSpeedRequired(true);
                criteria.setCostAllowed(true);
                criteria.setPowerRequirement(Criteria.POWER_HIGH);


                setUserLocation(locationManager.getLastKnownLocation(provider));

                if (location != null)
                    this.onLocationChanged(location);
                else{
                    locationManager.requestLocationUpdates(provider, 1000, 0, this);
                    //Log.e("Erreur Localisation","Entrer dans else de requestLocationUpdates");
                    if (location != null)
                        this.onLocationChanged(location);
                }

            }catch (Exception e){
                Toast.makeText(this, R.string.localisationNotPossible, Toast.LENGTH_LONG).show();

            }


    }

    public Location getUserLocation() {
        return location;
    }

    public void setUserLocation(Location location) {
        this.location = location;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        //Log.e("Erreur Localisation","onLocationChanged has been called");
        this.setUserLocation(location);
        this.searchSites(location);

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void searchNearestPlace(String v2txt) {
        //.....
    }
}