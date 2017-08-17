package com.paezand.instagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateAccountActivity extends AppCompatActivity implements View.OnKeyListener{

    @BindView(R.id.sign_up_user_name)
    protected TextView userName;

    @BindView(R.id.sign_up_password)
    protected TextView password;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        ButterKnife.bind(this);

    }

    @OnClick(R.id.sign_in_button)
    protected void onSignInButtonTapped() {
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
                        Toast.makeText(CreateAccountActivity.this, "User Create!", Toast.LENGTH_SHORT).show();
                        returnMainActivity();
                    } else {
                        Toast.makeText(CreateAccountActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onKey(View dialogInterface, int codeKey, KeyEvent keyEvent) {
        if (codeKey == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            onSignInButtonTapped();
        }
        return false;
    }

    private void returnMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
