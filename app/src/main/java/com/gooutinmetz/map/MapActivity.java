package com.gooutinmetz.map;

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
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gooutinmetz.menu.Menu;
import com.gooutinmetz.site.update.DisplaySiteFormListener;
import com.gooutinmetz.category.CategoryDaoService;
import com.gooutinmetz.site.SiteDaoService;
import com.gooutinmetz.R;
import com.gooutinmetz.category.CategoryModel;
import com.gooutinmetz.site.SiteModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, SearchSiteDialog.SearchModalDialogListener, LocationListener {

    public GoogleMap mMap;

    private Location userLocation;

    private static int permissions = 0;

    private CategoryDaoService categoryDao;
    private SiteDaoService siteDao;

    LocationManager locationManager = null;

    // Used when doing a site research
    private CategoryModel searchedCategory;
    private int searchRadius;

    public MapClickListener mapClickListener;
    public Button searchSiteBTN;
    public Button addSiteBTN;

    public String bestProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check permissions
        permissions = 0;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Location perm
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            permissions++;

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // File perm
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            } else {
                permissions++;

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Read file perm
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                }
                else
                    permissions++;
            }
        }

        // Run app only if the needed perms are set
        if (permissions == 3)
            run();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Display toast message if perm is not granted
        Toast toast = Toast.makeText(this, this.getResources().getString(R.string.noPermissions), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED)
            toast.show();

        boolean isLocationPermissionGranted = requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        if (isLocationPermissionGranted)
            MapActivity.permissions++;

        boolean isWriteInFilePermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
        if (isWriteInFilePermissionGranted) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        } else
        if (requestCode != 3)
            MapActivity.permissions++;

        if (MapActivity.permissions == 2) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //Si la permission de lire dans le fichier est accordée
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            } else
                MapActivity.permissions++;
        }

        if (MapActivity.permissions == 3)
            run();
    }

    public void run() {
        setContentView(R.layout.activity_maps);

        //initialisation du menu
        BottomNavigationView bottomNavigationView = this.findViewById(R.id.menu);
        bottomNavigationView.setSelectedItemId(R.id.mapMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new Menu(this));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        // Retrieve data from DAO
        categoryDao = CategoryDaoService.getInstance(this);
        siteDao = SiteDaoService.getInstance(this);

        // Button listeners
        Button searchSiteFromUserBTN = findViewById(R.id.searchSiteFromUserBTN);
        searchSiteFromUserBTN.setOnClickListener(new SearchSiteListener(this));
        searchSiteBTN = findViewById(R.id.searchSiteBTN);

        Button addSiteFromUserBTN = findViewById(R.id.addSiteFromUserBTN);
        addSiteFromUserBTN.setOnClickListener(new DisplaySiteFormListener(this, null));
        addSiteBTN = findViewById(R.id.addSiteBTN);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Retrieve the position of the user
        locationInitialization();

        mMap.setMyLocationEnabled(true);

        // Zoom on the user location
        if (userLocation != null) {
            LatLng userLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLatLng, 12);
            mMap.animateCamera(cameraUpdate);
        }

        mMap.setInfoWindowAdapter(new MarkerDescription(getLayoutInflater()));

        this.mapClickListener = new MapClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        boolean isAddingSiteOperation = data != null && data.getExtras() != null;
        if (isAddingSiteOperation)
            if (data.getLongExtra("id", -1) == -1) {
                SiteModel site = new SiteModel(data.getStringExtra("label"), data.getDoubleExtra("latitude", 0),
                        data.getDoubleExtra("longitude", 0), data.getStringExtra("postalAddress"),
                        categoryDao.findById(data.getLongExtra("categoryId", -1)), data.getStringExtra("summary"));

                long siteId = siteDao.create(site);
                site.setId(siteId);
            }
    }

    @Override
    public void onDialogPositiveClick(Location location, CategoryModel category, int radius) {
        searchedCategory = category;
        searchRadius = radius;

        searchSites(location);
    }

    @SuppressLint("NewApi")
    public void searchSites(Location location) {
        if (searchedCategory != null) {
            mMap.clear();

            // Add a circle on the map
            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(location.getLatitude(), location.getLongitude()))
                    .radius(searchRadius)
                    .strokeColor(Color.BLACK)
                    .fillColor(Color.argb(0.3f, 0, 0, 255)));

            // Add a marker on the map
            Location siteLocation = new Location("");
            LatLng siteLatLng;
            double siteDistance;
            for (SiteModel site : siteDao.findByCategory(searchedCategory)) {
                siteLocation.setLatitude(site.getLatitude());
                siteLocation.setLongitude(site.getLongitude());

                siteLatLng = new LatLng(site.getLatitude(), site.getLongitude());

                siteDistance = location.distanceTo(siteLocation);
                siteDistance = Math.round(siteDistance * 100) / 100.0;

                // Check the marker is located in the search radius
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
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if(locationManager == null) {
                return;
            }
            Criteria criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));
            locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);


            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(true);
            criteria.setBearingRequired(true);
            criteria.setSpeedRequired(true);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_HIGH);


            setUserLocation(locationManager.getLastKnownLocation(bestProvider));

            if (userLocation != null)
               this.onLocationChanged(userLocation);
            else{
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                Log.e("Localisation Error","Enter in the else of requestLocationUpdates");
                if (userLocation != null)
                    this.onLocationChanged(userLocation);
            }
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
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
}