package com.pentagon.test6;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button buttnUpload;
    Button btnSend;
    Bitmap bitmap;
    String filename;
    ImageView imageViewUpload;
    ProgressBar spinner;

    ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        buttnUpload = findViewById(R.id.buttonUpload);
        imageViewUpload = findViewById(R.id.imageViewUpload);
        btnSend = findViewById(R.id.btnSend);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        buttnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 1);

            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Please Wait For Response",Toast.LENGTH_LONG).show();
                btnSend.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.VISIBLE);
                Call<String> result = apiInterface.getResult();
                result.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        spinner.setVisibility(View.GONE);
                        String res = response.body();

                        StringBuilder sbBuilder = new StringBuilder();
                        sbBuilder.append("Your Average Consumption is ");
                        sbBuilder.append(res);
                        sbBuilder.append("\n");
                        sbBuilder.append("You are Eligible to grant GEOP! ");
                        sbBuilder.append("\n");
                        sbBuilder.append("The Admin will Contact you right away");

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(sbBuilder.toString())
                                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.setTitle("Congratulations");
                        builder.show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
                        spinner.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode== 1 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                String urlString = selectedImage.toString();
                String[] urlStringArr = urlString.split("/");
                filename =  urlStringArr[urlStringArr.length - 1] + ".jpg";
                buttnUpload.setText(filename);
                imageViewUpload.setImageBitmap(bitmap);
                btnSend.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
}

