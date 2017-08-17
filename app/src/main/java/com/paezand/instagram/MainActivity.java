package com.paezand.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

    @BindView(R.id.login_user_name)
    protected EditText userName;

    @BindView(R.id.login_password)
    protected EditText password;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (ParseUser.getCurrentUser() != null) {
            displayUserList();
        }

        password.setOnKeyListener(this);

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
                        displayUserList();
                    } else {
                        Toast.makeText(MainActivity.this, parseException.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onKey(View dialogInterface, int codeKey, KeyEvent keyEvent) {
        if (codeKey == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            onActionButtonTapped();
        }
        return false;
    }

    @OnClick(R.id.create_account)
    protected void onCreateAccountTapped() {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    private void displayUserList() {
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
    }
}
