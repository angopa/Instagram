package com.paezand.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.paezand.instagram.util.Contants.PARSE_COLUMN_IMAGE_IMAGE;
import static com.paezand.instagram.util.Contants.PARSE_COLUMN_IMAGE_USERNAME;
import static com.paezand.instagram.util.Contants.PARSE_COLUMN_USER_USERNAME;
import static com.paezand.instagram.util.Contants.PARSE_OBJECT_IMAGE;
import static com.paezand.instagram.util.Contants.PARSE_OBJECT_USER;

public class UserImagesActivity extends AppCompatActivity {

    @BindView(R.id.images_container)
    protected LinearLayout imageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_images);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String selectedUserName = intent.getStringExtra(PARSE_COLUMN_IMAGE_USERNAME);
        setTitle(selectedUserName +"\'s images");

        getUserImages(selectedUserName);
    }

    private void getUserImages(final String selectedUserName) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_OBJECT_IMAGE);

        query.whereEqualTo(PARSE_COLUMN_USER_USERNAME, selectedUserName);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0){
                        for (ParseObject o : objects) {
                            ParseFile file = (ParseFile) o.get(PARSE_COLUMN_IMAGE_IMAGE);

                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        addNewImageIntoLinearLayout(bitmap);
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(UserImagesActivity.this, "No images for this user", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void addNewImageIntoLinearLayout(final Bitmap bitmap) {
        ImageView newImage = new ImageView(getApplicationContext());
        newImage.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        newImage.setImageBitmap(bitmap);
        imageContainer.addView(newImage);
    }
}
