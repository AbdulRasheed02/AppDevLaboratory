package com.example.lab10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // creating variables for our edittext, 
    // button, textview and progressbar.
    private EditText nameEdt, passEdt,passReEdt,randomEdt,random;
    private Button postDataBtn,loginPage;
    private TextView responseTV, randomNumberText;
    private ProgressBar loadingPB;

    private String myRandom;
    private Random rand = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing our views
        nameEdt = findViewById(R.id.idEdtName);
        passEdt = findViewById(R.id.idEdtPass);
        passReEdt = findViewById(R.id.idEdtRePass);
        randomNumberText = findViewById(R.id.idRandom);
        randomEdt = findViewById(R.id.idEdtRandom);
        postDataBtn = findViewById(R.id.idBtnPost);
        loginPage = findViewById(R.id.idBtnLogin);
        responseTV = findViewById(R.id.idTVResponse);
        loadingPB = findViewById(R.id.idLoadingPB);

        myRandom = String.valueOf(rand.nextInt(100) + 1);
        randomNumberText.setText("Random Number : "+myRandom);

        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEdt.setText("");
                passEdt.setText("");
                passReEdt.setText("");
                randomEdt.setText("");
                responseTV.setText("Response");
                myRandom = String.valueOf(rand.nextInt(100) + 1);
                randomNumberText.setText("Random Number : "+myRandom);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // adding on click listener to our button.
        postDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the text field is empty or not.
                String user = nameEdt.getText().toString();
                String pass = passEdt.getText().toString();
                String repass = passReEdt.getText().toString();
                String randomNum = randomEdt.getText().toString();

                if (user.isEmpty() || pass.isEmpty() || repass.isEmpty() || randomNum.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all the values", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.equals(repass)==false) {
                    Toast.makeText(MainActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (randomNum.equals(myRandom)==false) {
                    Toast.makeText(MainActivity.this, "Random Number does not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                // calling a method to post the data and passing our name and job.
                registerAPI(nameEdt.getText().toString(), passEdt.getText().toString());
            }
        });
    }

    private void registerAPI(String name, String pass) {

        // below line is for displaying our progress bar.
        loadingPB.setVisibility(View.VISIBLE);

        // on below line we are creating a retrofit 
        // builder and passing our base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.76.1:3000")
                // as we are sending data in json format so 
                // we have to add Gson converter factory
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();
        // below line is to create an instance for our retrofit api class.
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        // passing data from our text fields to our modal class.
        DataModal modal = new DataModal(name, pass);

        // calling a method to create a post and passing our modal class.
        Call<DataModal> call = retrofitAPI.register(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<DataModal>() {
            @Override
            // this method is called when we get response from our api.
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {

                // below line is for hiding our progress bar.
                loadingPB.setVisibility(View.GONE);

                // on below line we are setting empty text
                // to our both edit text.
                nameEdt.setText("");
                passEdt.setText("");
                passReEdt.setText("");
                randomEdt.setText("");
                myRandom = String.valueOf(rand.nextInt(100) + 1);
                randomNumberText.setText("Random Number : "+myRandom);
                // on below line we are getting our data from modal class and adding it to our string.
                String responseString = "Response Code - " + response.code() ;
                // below line we are setting our 123
                // string to our text view.


                //Response code between 200-300
                if(response.isSuccessful()){
                    responseTV.setText("Response");
                    Toast.makeText(MainActivity.this, "User Registered", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                }
                else{
                    if(response.code()==400){
                        responseTV.setText(responseString+" : User Already Exists");
                    }
                }

            }

            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                // setting text to our text view when 
                // we get error response from API.
                responseTV.setText("Error found is : " + t.getMessage());
            }
        });
    }
}