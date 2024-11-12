package com.example.mymessenger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import java.util.ArrayList;

public class MovieTrendsActivity extends AppCompatActivity {
    private static final String TAG = "MovieTrendsActivity";
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 123;
    private static final int REQUEST_CHECK_SETTINGS = 1000;
    private boolean locationPermissionGranted = false;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trends);

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)        // 10 segundos
                .setFastestInterval(5000); // 5 segundos

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    handleLocation(locationResult.getLastLocation());
                    fusedLocationClient.removeLocationUpdates(locationCallback);
                } else {
                    Toast.makeText(MovieTrendsActivity.this,
                            "Por favor, asegúrate de que la ubicación está activada",
                            Toast.LENGTH_LONG).show();
                }
            }
        };

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermission();
        setupRecyclerView();
        setupButtons();
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            locationPermissionGranted = true;
            handleLocationFeatures();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                handleLocationFeatures();
            } else {
                locationPermissionGranted = false;
                showLocationPermissionDeniedMessage();
            }
        }
    }

    private void handleLocationFeatures() {
        checkLocationSettings();
    }

    private void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> {
            getCurrentLocation();
        });

        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (Exception sendEx) {
                    Log.e(TAG, "Error al mostrar el diálogo de configuración", sendEx);
                }
            }
        });
    }

    private void getCurrentLocation() {
        if (!locationPermissionGranted) {
            showLocationPermissionDeniedMessage();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            handleLocation(location);
                        } else {
                            requestNewLocation();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error al obtener la ubicación", e);
                        requestNewLocation();
                    });
        }
    }

    private void requestNewLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,
                    getMainLooper());
        }
    }

    private void handleLocation(android.location.Location location) {
        openMapsForTheaters(location);
    }

    private void searchNearbyTheaters() {
        if (locationPermissionGranted) {
            getCurrentLocation();
        } else {
            showLocationPermissionDeniedMessage();
        }
    }

    private void openMapsForTheaters(android.location.Location location) {
        Uri gmmIntentUri = Uri.parse("geo:" + location.getLatitude() + "," +
                location.getLongitude() + "?q=cines");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Por favor instala Google Maps", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMyLocation() {
        if (locationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, location -> {
                            if (location != null) {
                                Uri gmmIntentUri = Uri.parse("geo:" + location.getLatitude() + "," +
                                        location.getLongitude() + "?z=16");
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");

                                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(mapIntent);
                                } else {
                                    Toast.makeText(this, "Por favor instala Google Maps",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                requestNewLocation();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error al acceder a la ubicación", e);
                            requestNewLocation();
                        });
            } else {
                showLocationPermissionDeniedMessage();
            }
        } else {
            showLocationPermissionDeniedMessage();
        }
    }

    private void getDirections() {
        if (locationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, location -> {
                            if (location != null) {
                                // Construir la URI para navegar al cine más cercano automáticamente
                                Uri gmmIntentUri = Uri.parse("google.navigation:q=cinema+movie+theater&mode=d");
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");

                                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(mapIntent);
                                } else {
                                    Toast.makeText(this,
                                            "Por favor instala Google Maps",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                requestNewLocation();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error al acceder a la ubicación", e);
                            Toast.makeText(this,
                                    "No se pudo obtener la ubicación",
                                    Toast.LENGTH_SHORT).show();
                            requestNewLocation();
                        });
            } else {
                showLocationPermissionDeniedMessage();
            }
        } else {
            showLocationPermissionDeniedMessage();
        }
    }

    private void showLocationPermissionDeniedMessage() {
        Toast.makeText(this, "Se requiere permiso de ubicación para esta función",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                getCurrentLocation();
            } else {
                Toast.makeText(this,
                        "La configuración de ubicación debe estar habilitada para usar esta función",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private void setupRecyclerView() {
        ArrayList<String> movieTitles = getIntent().getStringArrayListExtra("movie_titles");
        ArrayList<String> moviePosters = getIntent().getStringArrayListExtra("movie_posters");
        ArrayList<String> movieOverviews = getIntent().getStringArrayListExtra("movie_overviews");

        if (movieTitles == null || moviePosters == null || movieOverviews == null) {
            Log.e(TAG, "onCreate: Faltan datos de películas");
            finish();
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MovieAdapter(movieTitles, moviePosters, movieOverviews));
    }

    private void setupButtons() {
        findViewById(R.id.btn_nearby_theaters).setOnClickListener(v -> searchNearbyTheaters());
        findViewById(R.id.btn_my_location).setOnClickListener(v -> showMyLocation());
        findViewById(R.id.btn_get_directions).setOnClickListener(v -> getDirections());
    }

    private static class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
        private final ArrayList<String> titles;
        private final ArrayList<String> posters;
        private final ArrayList<String> overviews;

        public MovieAdapter(ArrayList<String> titles, ArrayList<String> posters,
                            ArrayList<String> overviews) {
            this.titles = titles;
            this.posters = posters;
            this.overviews = overviews;
        }

        @NonNull
        @Override
        public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
            holder.titleView.setText(titles.get(position));
            holder.overviewView.setText(overviews.get(position));

            Glide.with(holder.itemView.getContext())
                    .load(posters.get(position))
                    .into(holder.posterView);
        }

        @Override
        public int getItemCount() {
            return titles.size();
        }

        static class MovieViewHolder extends RecyclerView.ViewHolder {
            final ImageView posterView;
            final TextView titleView;
            final TextView overviewView;

            MovieViewHolder(View view) {
                super(view);
                posterView = view.findViewById(R.id.image_movie_poster);
                titleView = view.findViewById(R.id.text_movie_title);
                overviewView = view.findViewById(R.id.text_movie_overview);
            }
        }
    }
}