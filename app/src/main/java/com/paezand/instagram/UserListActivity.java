package com.paezand.instagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends AppCompatActivity {

    @BindView(R.id.user_list)
    protected ListView userNamesListView;

    ArrayList<String> userNames = new ArrayList<>();

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ButterKnife.bind(this);

        obtainUserNamesFromServer();

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, userNames);

        userNamesListView.setAdapter(adapter);
    }

    private void obtainUserNamesFromServer() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> objects, ParseException parseException) {
                if (parseException == null) {
                    if (objects.size() > 0) {
                        for (ParseUser user : objects) {
                            userNames.add(user.getUsername());
                        }
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    parseException.printStackTrace();
                }
            }
        });
    }
}
