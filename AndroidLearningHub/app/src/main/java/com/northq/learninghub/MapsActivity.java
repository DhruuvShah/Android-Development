package com.northq.learninghub;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Unit 4 Practical: "Integrate Google Maps and display the current location."
 *
 * Requires a Google Maps API key in AndroidManifest.xml (see README) —
 * without one, the map tiles will fail to load, but the code below still
 * demonstrates the full pattern: SupportMapFragment + FusedLocationProviderClient
 * + runtime location permission.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationClient;

    private final ActivityResultLauncher<String> requestLocationPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) showCurrentLocation();
                else Toast.makeText(this, "Location permission is required to show your position", Toast.LENGTH_SHORT).show();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mapsRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (PermissionUtils.isGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            showCurrentLocation();
        } else {
            requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @SuppressLint("MissingPermission") // Guarded by PermissionUtils.isGranted() / the launcher callback above.
    private void showCurrentLocation() {
        map.setMyLocationEnabled(true);
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                LatLng here = new LatLng(location.getLatitude(), location.getLongitude());
                map.addMarker(new MarkerOptions().position(here).title("You are here"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(here, 15f));
            } else {
                Toast.makeText(this, "Could not determine current location — try again outdoors", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
