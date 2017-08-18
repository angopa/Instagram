package com.paezand.instagram;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.paezand.instagram.util.Contants.PARSE_COLUMN_IMAGE_IMAGE;
import static com.paezand.instagram.util.Contants.PARSE_COLUMN_IMAGE_USERNAME;
import static com.paezand.instagram.util.Contants.PARSE_OBJECT_IMAGE;

public class SelectImagesActivity extends AppCompatActivity {

    private static final int MEDIA_STORES_RESULT_INTENT = 983;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_RESULT = 746;

    @BindView(R.id.selected_image)
    protected ImageView selectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_images);
        ButterKnife.bind(this);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST_RESULT);
        } else {
            getPhoto();
        }
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
            getPhoto();
        } else if (item.getItemId() == R.id.log_out) {
            ParseUser.logOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MEDIA_STORES_RESULT_INTENT
                && resultCode == RESULT_OK
                && data != null) {
            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                saveImage(bitmap);
                setSelectImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_RESULT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            } else {
                this.finish();
            }
        }
    }

    public void setSelectImageBitmap(final Bitmap selectImage) {
        this.selectImage.setImageBitmap(selectImage);
    }

    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, MEDIA_STORES_RESULT_INTENT);
    }

    private void saveImage(final Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile file = new ParseFile(PARSE_COLUMN_IMAGE_IMAGE + System.currentTimeMillis() + ".png", byteArray);
        ParseObject object = new ParseObject(PARSE_OBJECT_IMAGE);
        object.put(PARSE_COLUMN_IMAGE_IMAGE, file);
        object.put(PARSE_COLUMN_IMAGE_USERNAME, ParseUser.getCurrentUser().getUsername());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException parseException) {
                if (parseException == null){
                    Toast.makeText(SelectImagesActivity.this, "Image Shared!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SelectImagesActivity.this, "Image could not be shared - please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
