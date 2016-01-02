package com.locaces.locaces;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Message;
import android.os.Handler;

import java.sql.SQLException;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText emailView;
    private EditText passwordView;
    private TextView invalidView;

    UserController currentUser;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            String msg = (String) m.obj;
            if (msg == "success") {
                Intent homepage = new Intent(LoginActivity.this, HomePageActivity.class);
                homepage.putExtra("user", currentUser.getUser());
                startActivity(homepage);
                try {
                    DataBaseManager.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                finish();
            } else {
                invalidView.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // TODO: This is for debugging only, remove it!
        //{currentUser = new UserController();final String email = "m@m";final String password = "asdfasdf";Thread loginThread = new Thread(new Runnable() {@Override public void run() {synchronized (this) {try {if (currentUser.login(email, password)) handler.sendMessage(Message.obtain(handler, 0, "success"));else handler.sendEmptyMessage(0);} catch (Exception e) {e.printStackTrace();return;}}}});loginThread.start();finish();}

        // Set up the login form.
        emailView = (EditText) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        invalidView = (TextView) findViewById(R.id.invalid_email_password);

        currentUser = new UserController();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(menu.findItem(R.id.contactus).getOrder()).setVisible(false);
        menu.getItem(menu.findItem(R.id.logout).getOrder()).setVisible(false);
        menu.getItem(menu.findItem(R.id.back_button).getOrder()).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id != R.id.about)
            return false;
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin(View view) {

        // Store values at the time of the login attempt.
        final String email = emailView.getText().toString();
        final String password = passwordView.getText().toString();

        if (!checkFields(email, password))
            return;

        Thread loginThread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        if (currentUser.login(email, password))
                            handler.sendMessage(Message.obtain(handler, 0, "success"));
                        else
                            handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        });

        loginThread.start();

    }

    public void attemptSignUp(View view) {

        // Store values at the time of the login attempt.
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        Intent signup = new Intent(this, SignUpActivity.class);
        signup.putExtra("email", email);
        signup.putExtra("password", password);
        startActivity(signup);

    }

    private boolean checkFields(String email, String password) {

        // Reset errors.
        emailView.setError(null);
        passwordView.setError(null);
        invalidView.setVisibility(View.INVISIBLE);

        // Check if the user entered email.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            emailView.requestFocus();
            return false;
        }

        // Check if the user entered password.
        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            passwordView.requestFocus();
            return false;
        }

        return true;
    }
}

