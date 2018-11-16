package edu.floridapoly.polycamsportal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import edu.floridapoly.polycamsportal.util.PolyAuthenticator;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    //private static final String ACCOUNT_TYPE = "edu.floridapoly.polycamsportal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //TODO: Check for credentials in AccountManager

        Button login_button = (Button) findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String mUsername, mPassword;
                    TextView username_input = (TextView) findViewById(R.id
                        .username_input);
                    TextView password_input = (TextView) findViewById(R.id
                        .password_input);

                    mUsername = username_input.getText().toString();
                    mPassword = password_input.getText().toString();

                    if (!mUsername.isEmpty() && !mPassword.isEmpty()) {
                        PolyAuthenticator pAuth = PolyAuthenticator.getInstance();
                        pAuth.setApiAuth(mUsername, mPassword);
                        Log.v(TAG, "Success Authenticator'");

                        //TODO: store username and password in AccountManager
                        //https://developer.android.com/training/id-auth/custom_auth
                        //AccountManager am = AccountManager.get(LoginActivity.this);
                        /*
                        final Account account = new Account(mUsername, ACCOUNT_TYPE);
                        am.addAccountExplicitly(account, mPassword, null);
                         */

                        // Open Schedule for User
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("username", mUsername);
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    Log.v(TAG, "We have failed.'");
                    e.printStackTrace();
                }
            }
        });
    }


}
