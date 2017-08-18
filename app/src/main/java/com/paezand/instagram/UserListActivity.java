package com.paezand.instagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import static com.paezand.instagram.util.Contants.PARSE_COLUMN_USER_USERNAME;

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

        userNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), UserImagesActivity.class);
                intent.putExtra(PARSE_COLUMN_USER_USERNAME, userNames.get(i));
                startActivity(intent);
            }
        });
    }

    private void obtainUserNamesFromServer() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.whereNotEqualTo(PARSE_COLUMN_USER_USERNAME, ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException parseException) {
                if (parseException == null) {
                    if (objects.size() > 0) {
                        for (ParseUser user : objects) {
                            userNames.add(user.getUsername());
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    parseException.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share_image) {
            Intent intent = new Intent(this, SelectImagesActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.log_out) {
            ParseUser.logOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
