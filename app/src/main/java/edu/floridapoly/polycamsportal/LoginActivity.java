package edu.floridapoly.polycamsportal;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import edu.floridapoly.polycamsportal.util.PolyAuthenticator;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    public static final String SHARED_PREFS = "FLPOLY";
    public static final String ACTION = "ACTION";
    public static final int LOGOUT = 1;
    private static final String UNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle extras = getIntent().getExtras();
        int action;

        if (extras != null) {
            action = extras.getInt(ACTION);
            if (action == LOGOUT)
            {
                clearUsernameAndPass();
            }

        }

        loadUsernameAndPass();

        if (!username.isEmpty() && !password.isEmpty())
        {
            authenticate(username, password);
            launchMainActivity();
        }

        Button login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(this);
    }

    public void onClick(View v) {
        String mUsername, mPassword;
        TextView username_input = (TextView) findViewById(R.id
            .username_input);
        TextView password_input = (TextView) findViewById(R.id
            .password_input);

        mUsername = username_input.getText().toString();
        mPassword = password_input.getText().toString();

        if (!mUsername.isEmpty() && !mPassword.isEmpty()) {
            authenticate(mUsername, mPassword);
            saveUsernameAndPass(mUsername, mPassword);
            launchMainActivity();
        }
    }

    public void loadUsernameAndPass()
    {
        SharedPreferences sprefs = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sprefs.getString(UNAME, "");
        password = sprefs.getString(PASSWORD, "");
    }

    public void saveUsernameAndPass(String username, String password)
    {
        SharedPreferences sprefs = LoginActivity.this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sprefs.edit();
        editor.putString(UNAME, username);
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public void clearUsernameAndPass()
    {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, 0);
        preferences.edit().remove(UNAME).commit();
        preferences.edit().remove(PASSWORD).commit();

    }

    private void authenticate(String username, String password)
    {
        this.username = username;
        this.password = password;
        PolyAuthenticator.setAsDefault();
        PolyAuthenticator pAuth = PolyAuthenticator.getInstance();
        pAuth.setApiAuth(username, password);
    }

    private void launchMainActivity()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.USERNAME, this.username);
        startActivity(intent);
    }



}
