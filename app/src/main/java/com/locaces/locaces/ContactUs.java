package com.locaces.locaces;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ContactUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(menu.findItem(R.id.back_button).getOrder()).setVisible(true);
        menu.getItem(menu.findItem(R.id.contactus).getOrder()).setVisible(false);
        menu.getItem(menu.findItem(R.id.logout).getOrder()).setVisible(false);
        menu.getItem(menu.findItem(R.id.about).getOrder()).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.back_button) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
