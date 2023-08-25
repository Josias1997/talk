package com.eidogs.talk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;


public class DashboardActivity extends AppCompatActivity {
    EditText secretCode;
    Button shareBtn, joinBtn;

    BottomNavigationView bottomNavigationView;

    FloatingActionButton floatingActionButton;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        secretCode = findViewById(R.id.secret_code);
        shareBtn = findViewById(R.id.share);
        joinBtn = findViewById(R.id.join);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        floatingActionButton = findViewById(R.id.fab);

        firebaseAuth = FirebaseAuth.getInstance();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home) {
                    Toast.makeText(DashboardActivity.this, "Home", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.logout) {
                    firebaseAuth.signOut();
                    startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                    finish();
                }
                return true;
            }
        });

        URL serverURL;

        try {
            serverURL = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverURL)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(secretCode.getText().toString())
                        .build();
                JitsiMeetActivity.launch(DashboardActivity.this, options);
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = secretCode.getText().toString();
                if(!code.equals("")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Here is th secret code use it to connect to the meeting: " + secretCode.getText().toString());
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                } else {
                    Toast.makeText(DashboardActivity.this, "Create a meeting first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = "talk-meet-" + UUID.randomUUID().toString().substring(0, 5);
                secretCode.setText(code);
            }
        });

    }
}