package com.lifecare.main.Activities;

import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lifecare.main.GPSTracker;
import com.lifecare.main.R;

import java.util.ArrayList;

import static com.lifecare.main.R.id;
import static com.lifecare.main.R.layout;

public class MainActivity extends AppCompatActivity {

    DatabaseReference dbContacts;
    boolean torchOn = false;
    private ArrayList<String> phoneNumbers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        String uid = FirebaseAuth.getInstance().getUid();
        dbContacts = FirebaseDatabase.getInstance().getReference("Contacts");
        Query query = dbContacts.orderByChild("uid").equalTo(uid);

        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String child = postSnapshot.child("phone").getValue(String.class);
                            phoneNumbers.add(child);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        Button sendMessagesBtn = findViewById(id.sendMessagesBtn);

        sendMessagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSend();
            }
        });

        Button alarmBtn = findViewById(id.alarmBtn);

        final MediaPlayer alarmSound = MediaPlayer.create(this, R.raw.alarm);

        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
                try {
                    String cameraID = cameraManager.getCameraIdList()[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (torchOn) {
                            alarmSound.pause();
                            cameraManager.setTorchMode(cameraID, false);
                            torchOn = false;
                        } else {
                            alarmSound.isLooping();
                            alarmSound.start();
                            cameraManager.setTorchMode(cameraID, true);
                            torchOn = true;
                        }
                    }
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        Button openInformationBtn = findViewById(id.openInformationBtn);

        openInformationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainBlocked.class);
                startActivity(intent);
            }
        });
    }

    private void onClickSend() {
        GPSTracker gps = new GPSTracker(getApplicationContext());
        if (gps.isGPSEnabled) {
            if (gps.getLatitude() != 0.0 && gps.getLongitude() != 0.0) {
                Location location = new Location("");
                location.setLatitude(gps.getLatitude());
                location.setLongitude(gps.getLongitude());
                for (int i = 0; i < phoneNumbers.size(); i++) {
                    sendLocationSMS(phoneNumbers.get(i), location);
                    Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();
                }
            } else {
                for (int i = 0; i < phoneNumbers.size(); i++) {
                    sendLocationSMSWithoutLocation(phoneNumbers.get(i));
                    Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            for (int i = 0; i < phoneNumbers.size(); i++) {
                sendLocationSMSWithoutLocation(phoneNumbers.get(i));
                Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();
            }
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    public void sendLocationSMS(String phoneNumber, Location currentLocation) {
        SmsManager smsManager = SmsManager.getDefault();
        StringBuffer smsBody = new StringBuffer();
        smsBody.append("http://maps.google.com?q=");
        smsBody.append(currentLocation.getLatitude());
        smsBody.append(",");
        smsBody.append(currentLocation.getLongitude());
        smsManager.sendTextMessage(phoneNumber, null, smsBody.toString(), null, null);
    }

    public void sendLocationSMSWithoutLocation(String phoneNumber) {
        SmsManager smsManager = SmsManager.getDefault();
        StringBuffer smsBody = new StringBuffer("help");
        smsManager.sendTextMessage(phoneNumber, null, smsBody.toString(), null, null);
    }
}
