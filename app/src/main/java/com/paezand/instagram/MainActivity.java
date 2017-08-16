package com.paezand.instagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.user_name_input_text)
    protected EditText userName;

    @BindView(R.id.passwor_input_text)
    protected EditText password;

    @BindView(R.id.action_button)
    protected Button actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    @OnClick(R.id.action_button)
    protected void onActionButtonTapped() {
        if (userName.getText().toString().matches("") || password.getText().toString().matches("")) {
            Toast.makeText(this, "A User Name an Password are required", Toast.LENGTH_SHORT).show();
        } else {
            ParseUser user = new ParseUser();

            user.setUsername(userName.getText().toString());
            user.setPassword(password.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(final ParseException e) {
                    if (e == null) {
                        Toast.makeText(MainActivity.this, "User Create!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
