package com.offlineprogrammer.amplifyinsta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.StorageItem;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    private ImageView imageView;
    private Button camera_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        camera_button = findViewById(R.id.camera_button);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        ArrayList imageUrlList = prepareData();
        ImageUrlsAdapter dataAdapter = new ImageUrlsAdapter(getApplicationContext(), imageUrlList);
        recyclerView.setAdapter(dataAdapter);


        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(MainActivity.this).returnMode(ReturnMode.ALL)
                        .folderMode(true).includeVideo(false).limit(1).theme(R.style.AppTheme_NoActionBar).single().start();
            }
        });


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getData() != null && "amplifyinsta".equals(intent.getData().getScheme())) {
            Amplify.Auth.handleWebUISignInResponse(intent);
        }
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (ImagePicker.shouldHandle(i, i2, intent)) {
            Image firstImageOrNull = ImagePicker.getFirstImageOrNull(intent);
            if (firstImageOrNull != null) {
                uploadImage(firstImageOrNull.getPath());

            }
        }


    }

    private void uploadImage(String path) {
        if (path != null) {


            File exampleFile = new File(path);


            Amplify.Storage.uploadFile(
                    UUID.randomUUID().toString(),
                    exampleFile,
                    result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                    storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
            );


            // Code for showing progressDialog while uploading
          /*  progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();*/


        } else {


        }

    }


    private ArrayList prepareData() {

        Amplify.Storage.list(
                "/",
                result -> {
                    for (StorageItem item : result.getItems()) {
                        Log.i("MyAmplifyApp", "Item: " + item.getKey());

                    }
                },
                error -> Log.e("MyAmplifyApp", "List failure", error)
        );

// here you should give your image URLs and that can be a link from the Internet
        String[] imageUrls = {
                "https://picsum.photos/id/1/5616/3744",
                "https://picsum.photos/id/1000/5626/3635",
                "https://picsum.photos/id/1006/3000/2000",
                "https://picsum.photos/id/1016/3844/2563"};

        ArrayList imageUrlList = new ArrayList<>();
        for (int i = 0; i < imageUrls.length; i++) {
            ImageUrl imageUrl = new ImageUrl();
            imageUrl.setImageUrl(imageUrls[i]);
            imageUrlList.add(imageUrl);
        }
        Log.d("MainActivity", "List count: " + imageUrlList.size());
        return imageUrlList;
    }
}