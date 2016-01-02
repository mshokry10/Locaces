package com.locaces.locaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A sign up screen that offers to sign up an account.
 */
public class SignUpActivity extends AppCompatActivity {

    // UI references.
    private EditText nameView;
    private EditText emailView;
    private EditText passwordView;
    private EditText passwordConfirmView;
    private TextView emailExistsView;

    UserController currentUser;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            String msg = (String) m.obj;
            if (msg == "success") {
                final AlertDialog.Builder successDialog = new AlertDialog.Builder(SignUpActivity.this);
                successDialog.setMessage(R.string.account_successful).setTitle(R.string.success_dialog_title);
                successDialog.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                successDialog.create().show();
            } else {
                emailExistsView.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Set up the signup form.
        nameView = (EditText) findViewById(R.id.name);
        emailView = (EditText) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        passwordConfirmView = (EditText) findViewById(R.id.password_confirm);
        emailExistsView = (TextView) findViewById(R.id.email_exists);

        currentUser = new UserController();

        emailView.setText(getIntent().getStringExtra("email"));
        passwordView.setText(getIntent().getStringExtra("password"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(menu.findItem(R.id.contactus).getOrder()).setVisible(false);
        menu.getItem(menu.findItem(R.id.logout).getOrder()).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.back_button) {
            finish();
            return true;
        }
        if (id != R.id.about)
            return false;
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Attempts to register the account specified by the sign up form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual sign up attempt is made.
     */
    public void attemptSignUp(View view) {
        // Store values at the time of the login attempt.
        final String name = nameView.getText().toString();
        final String email = emailView.getText().toString();
        final String password = passwordView.getText().toString();
        final String passwordConfirm = passwordConfirmView.getText().toString();

        if (!checkFields(name, email, password, passwordConfirm))
            return;

        Thread signUpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        if (currentUser.signup(name, password, email))
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

        signUpThread.start();
    }

    private boolean checkFields(String name, String email, String password, String passwordConfirm) {
        // Reset errors.
        nameView.setError(null);
        emailView.setError(null);
        passwordView.setError(null);
        passwordConfirmView.setError(null);
        emailExistsView.setVisibility(View.INVISIBLE);

        // Check if the user entered name.
        if (TextUtils.isEmpty(name)) {
            nameView.setError(getString(R.string.error_field_required));
            nameView.requestFocus();
            return false;
        }

        // Check if the user entered email.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            emailView.requestFocus();
            return false;
        }

        // Check if the user entered a valid email
        if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            emailView.requestFocus();
            return false;
        }

        // Check if the user entered password.
        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            passwordView.requestFocus();
            return false;
        }

        // Check if the user entered valid password.
        if (!isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_incorrect_password));
            passwordView.requestFocus();
            return false;
        }

        // Check if the user entered password confirmation.
        if (TextUtils.isEmpty(password)) {
            passwordConfirmView.setError(getString(R.string.error_field_required));
            passwordConfirmView.requestFocus();
            return false;
        }

        // Check if password confirmation matches password.
        if (!password.equals(passwordConfirm)) {
            passwordConfirmView.setError(getString(R.string.error_password_not_match));
            passwordConfirmView.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

}