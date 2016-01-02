package com.locaces.locaces;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class HomePageActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // UI references
    private EditText statusView;

    private GoogleApiClient client;

    private UserModel user;

    private Location currentLocation;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            if (m.what == 1) {
                Toast.makeText(HomePageActivity.this,
                        "Posted successfully!",
                        Toast.LENGTH_LONG).show();
                statusView.setText("");
                statusView.clearFocus();
                refresh(new View(HomePageActivity.this));
                return;
            }

            String[] list = (String[])m.obj;
            ListAdapter adapter = new ArrayAdapter<String>(
                    HomePageActivity.this, android.R.layout.simple_list_item_1, list);
            ListView postsView = (ListView) findViewById(R.id.posts);
            postsView.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        user = (UserModel) getIntent().getExtras().getSerializable("user");

        statusView = (EditText) findViewById(R.id.checkInStatus);

        Toast.makeText(this, "Welcome, " + user.getName() + "!", Toast.LENGTH_LONG).show();

        if (client == null) {
            client = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        refresh(new View(this));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(menu.findItem(R.id.contactus).getOrder()).setVisible(false);
        menu.getItem(menu.findItem(R.id.back_button).getOrder()).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                startActivity(new Intent(this, LoginActivity.class));
                Toast.makeText(this, "Be back soon!", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.about:
                startActivity(new Intent(this, About.class));
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Posts a check in with the text attached.
     */
    public void checkIn(View view) {

        Thread checkInThread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        PlaceModel place = PlaceController.getNearestPlace(currentLocation);
                        String status = statusView.getText().toString();
                        Date time = new Date(Calendar.getInstance().getTimeInMillis());
                        try {
                            CheckInController.doCheckIn(status, place, time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(1);
                }
            }
        });

        checkInThread.start();

    }


    public void refresh(View view) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    ArrayList<Post> posts = null;
                    try {
                        posts = CheckInModel.getAllCheckInPosts();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    String[] postsText = new String[posts.size()];
                    for (int i = 0; i < posts.size(); ++i) {
                        postsText[posts.size() - i - 1] = posts.get(i).toString();
                    }
                    handler.sendMessage(Message.obtain(handler, 0, postsText));
                }
            }
        });

        thread.start();
    }

    @Override
    public void onStart() {
        client.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        client.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        int timer = 0;
        do {
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(client);
            ++timer;
            if (timer > 1000)
                break;
        } while (currentLocation == null);

        if (currentLocation == null) {
            Toast.makeText(this, "Error getting your location. Try again later.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
