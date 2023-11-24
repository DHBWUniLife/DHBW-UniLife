package com.dhbw.unilife.rnv;

import static com.dhbw.unilife.rnv.network.RnvRetrofitClient.RNV_BASE_URL;
import static java.text.DateFormat.getDateInstance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dhbw.unilife.R;
import com.dhbw.unilife.rnv.network.MethodsRnvDepartures;
import com.dhbw.unilife.rnv.network.ModelRnvDepartures;
import com.dhbw.unilife.rnv.network.RnvRetrofitClient;
import com.dhbw.unilife.rnv.network.classes.DateTime;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RnvActivity extends AppCompatActivity {
    final String destination_hd_hbf = "de:08221:1160"; // rnv Haltestellen Adresse
    final String destination_ma_hbf = "de:08222:2417"; // rnv Haltestellen Adresse
    final String set_rnv_request_attributes = "mngvrn/XML_TRIP_REQUEST2?changeSpeed=normal&coordOutputFormat=EPSG:4326&cycleSpeed=14&deleteITPTWalk=0&exclMOT_15=1&exclMOT_16=1&excludedMeans=checkbox&itOptionsActive=1&itPathListActive=1&lineRestriction=0400&locationServerActive=1&name_origin=de:08222:2521&outputFormat=json&ptMacro=true&ptOptionsActive=1&routeType=leasttime&strictMode=0&trITMOT=100&trITMOTvalue=15&type_destination=any&type_origin=any&useElevationData=1&useRealtime=1&useUT=1&useUnifiedTickets=1&wheelchairSpaceStop=0";

    TextView textViewRichtungMannheimAbfahrtZeit, textViewRichtungMannheimAbfahrtZeitEcht,textViewRichtungHeidelbergAbfahrtZeit, textViewRichtungHeidelbergAbfahrtZeitEcht;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rnv);
        findViews(); // Die TextViews des Layouts finden
        get_next_departures(destination_hd_hbf); // Für Heidelberg
        get_next_departures(destination_ma_hbf); // Für Mannheim
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_next_departures(destination_hd_hbf); // Für Heidelberg
        get_next_departures(destination_ma_hbf); // Für Mannheim
    }

    private void get_next_departures(String direction) {
        // Anfrage an RNV async senden durch Retrofit
        progressBar.setVisibility(View.VISIBLE);
        MethodsRnvDepartures methods = RnvRetrofitClient.getRetrofitInstance().create(MethodsRnvDepartures.class);
        // Total Url enthält den BASE URL der rnv sowie die Standardatrrtibute und die Richtungsaddresse sowie die derzeitige Zeit
        String total_url = RNV_BASE_URL + set_rnv_request_attributes + "&name_destination="+direction+"&itdTime="+getFormattedTime();

        Call<ModelRnvDepartures> call = methods.getAllData(total_url);
        call.enqueue(new Callback<ModelRnvDepartures>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ModelRnvDepartures> call, @NonNull Response<ModelRnvDepartures> response) {
                if (response.isSuccessful()) {
                    // Do awesome stuff
                    assert response.body() != null;

                    DateTime dateTime = response.body().getTrips().get(0).getLegs().get(0).getPoints().get(0).getDateTime();
                    if (direction.equals(destination_ma_hbf)) {
                        textViewRichtungMannheimAbfahrtZeit.setText(dateTime.getTime());
                        textViewRichtungMannheimAbfahrtZeitEcht.setText(dateTime.getRtTime());
                    } else {
                        textViewRichtungHeidelbergAbfahrtZeit.setText(dateTime.getTime());
                        textViewRichtungHeidelbergAbfahrtZeitEcht.setText(dateTime.getRtTime());
                    }

                } else {
                    // Error
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<ModelRnvDepartures> call, @NonNull Throwable t) {
                Log.d("error_contact_rnv", t.toString());
                textViewRichtungMannheimAbfahrtZeitEcht.setText(t.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void findViews() {
        textViewRichtungMannheimAbfahrtZeit = findViewById(R.id.textViewRichtungMannheimAbfahrtZeit);
        textViewRichtungMannheimAbfahrtZeitEcht = findViewById(R.id.textViewRichtungMannheimAbfahrtZeitEcht);
        textViewRichtungHeidelbergAbfahrtZeit = findViewById(R.id.textViewRichtungHeidelbergAbfahrtZeit);
        textViewRichtungHeidelbergAbfahrtZeitEcht = findViewById(R.id.textViewRichtungHeidelbergAbfahrtZeitEcht);
        progressBar = findViewById(R.id.progressBar);
    }

    private String getFormattedTime() {
        // RNV API braucht die Uhrzeit in dem Format STUNDENMINUTEN Daher muss es von uns umgeformt werden
        String formattedTime;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalTime now = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
            formattedTime = now.format(formatter);
        } else {
            Date now = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
            formattedTime = sdf.format(now);
        }
        return formattedTime; // gibt Uhrzeit in dem korrektem Format zurück
    }
}