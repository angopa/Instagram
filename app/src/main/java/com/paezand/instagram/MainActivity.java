package com.paezand.instagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.login_user_name)
    protected EditText userName;

    @BindView(R.id.login_passwor)
    protected EditText password;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    @OnClick(R.id.login_button)
    protected void onActionButtonTapped() {
        if (userName.getText().toString().matches("") || password.getText().toString().matches("")) {
            Toast.makeText(this, "A User Name an Password are required", Toast.LENGTH_SHORT).show();
        } else {
            ParseUser user = new ParseUser();

            user.logInInBackground(userName.getText().toString(), password.getText().toString(), new LogInCallback() {

                @Override
                public void done(final ParseUser user, final ParseException parseException) {
                    if (parseException == null) {
                        Toast.makeText(MainActivity.this, "Welcome " + userName.getText().toString() + "!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, parseException.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @OnClick(R.id.create_account)
    protected void onCreateAccountTapped() {
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);
    }
}
