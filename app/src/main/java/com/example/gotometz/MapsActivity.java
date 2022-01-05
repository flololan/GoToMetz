package com.example.gotometz;

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
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gotometz.administration.DisplaySiteFormListener;
import com.example.gotometz.dao.CategoryService;
import com.example.gotometz.dao.SiteService;
import com.example.gotometz.map.MarkerDescription;
import com.example.gotometz.map.MyMapListener;
import com.example.gotometz.map.SearchSiteDialog;
import com.example.gotometz.map.SearchSiteListener;
import com.example.gotometz.model.Category;
import com.example.gotometz.model.Site;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, SearchSiteDialog.SearchModalDialogListener, LocationListener {

    public GoogleMap mMap;

    private Location userLocation;

    private static int permissions = 0;

    private CategoryService categoryDao;
    private SiteService siteDao;

    LocationManager locationManager = null;
    private String provider;
    //MyLocationListener myLocationListener; // location listener

    // Used when doing a site research
    private Category searchedCategory;
    private int searchedRadius;

    public MyMapListener myMapListener;
    public Button searchSiteBTN;
    public Button addSiteBTN;

    public String bestProvider;
    public Criteria criteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Vérifie les permissions
        permissions = 0;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission de localisation
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

        //Lance l'application quand toutes les permissions sont accordées
        if (permissions == 3)
            run();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Affiche un message si la permission n'est pas accordée
        Toast toast = Toast.makeText(this, this.getResources().getString(R.string.noPermissions), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED)
            toast.show();

        //Si la permission de localisation est accordée
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            MapsActivity.permissions++;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Si la permissionj d'écrire dans le fichier est accordée
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        } else
        if (requestCode != 3)
            MapsActivity.permissions++;

        if (MapsActivity.permissions == 2) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //Si la permission de lire dans le fichier est accordée
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

        //initialisation du menu
        BottomNavigationView bottomNavigationView = this.findViewById(R.id.menu);
        bottomNavigationView.setSelectedItemId(R.id.mapMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new Menu(this));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        //récupérer les données bdd via la DAO
        categoryDao = CategoryService.getInstance(this);
        siteDao = SiteService.getInstance(this);

        // Bouton listeners

        Button searchSiteFromUserBTN = findViewById(R.id.searchSiteFromUserBTN);
        searchSiteFromUserBTN.setOnClickListener(new SearchSiteListener(this));

        Button addSiteFromUserBTN = findViewById(R.id.addSiteFromUserBTN);
        addSiteFromUserBTN.setOnClickListener(new DisplaySiteFormListener(this, null));

        searchSiteBTN = findViewById(R.id.searchSiteBTN);
        addSiteBTN = findViewById(R.id.addSiteBTN);
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

        //Récupère la position de l'utilisateur
        locationInitialization();

        mMap.setMyLocationEnabled(true);

        //Zoomer sur la position de l'utilisateur
        if (userLocation != null) {
            LatLng userLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLatLng, 12);
            mMap.animateCamera(cameraUpdate);
        }

        mMap.setInfoWindowAdapter(new MarkerDescription(getLayoutInflater()));

        this.myMapListener = new MyMapListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Ajouter un site
        if (data != null && data.getExtras() != null)
            if (data.getLongExtra("id", -1) == -1) {
                Site site = new Site(data.getStringExtra("label"), data.getDoubleExtra("latitude", 0),
                        data.getDoubleExtra("longitude", 0), data.getStringExtra("postalAddress"),
                        categoryDao.findById(data.getLongExtra("categoryId", -1)), data.getStringExtra("summary"));

                //Création du site dans la bdd
                site.setId(siteDao.create(site));
            }
    }

    @Override
    public void onDialogPositiveClick(Location location, Category category, int radius) {
        searchedCategory = category;
        searchedRadius = radius;

        searchSites(location);
    }

    @SuppressLint("NewApi")
    public void searchSites(Location location) {
        if (searchedCategory != null) {
            mMap.clear();

            //Ajouter le cercle sur la map
            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(location.getLatitude(), location.getLongitude()))
                    .radius(searchedRadius)
                    .strokeColor(Color.BLACK)
                    .fillColor(Color.argb(0.3f, 0, 0, 255)));

            //Ajouter les marqueurs sur la map
            Location siteLocation = new Location("");
            LatLng siteLatLng;
            double siteDistance;
            for (Site site : siteDao.findByCategory(searchedCategory)) {
                siteLocation.setLatitude(site.getLatitude());
                siteLocation.setLongitude(site.getLongitude());

                siteLatLng = new LatLng(site.getLatitude(), site.getLongitude());

                siteDistance = location.distanceTo(siteLocation);
                siteDistance = Math.round(siteDistance * 100) / 100.0;

                //Vérifier si le marqueurs du site se situe dans la rayon de recherche
                if (siteDistance <= searchedRadius)
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
            Criteria criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
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

    public void searchNearestPlace(String v2txt) {
        //.....
    }
}