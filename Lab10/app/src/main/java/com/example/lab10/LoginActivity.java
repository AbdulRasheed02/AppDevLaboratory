package com.example.lab10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    // creating variables for our edittext,
    // button, textview and progressbar.
    private EditText nameEdtLogin, passEdtLogin;
    private Button postDataBtnLogin;
    private TextView responseTVLogin;
    private ProgressBar loadingPBLogin;

    private String myRandom;
    private Random rand = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initializing our views
        nameEdtLogin = findViewById(R.id.idEdtNameLogin);
        passEdtLogin = findViewById(R.id.idEdtPassLogin);
        postDataBtnLogin = findViewById(R.id.idBtnPostLogin);
        responseTVLogin = findViewById(R.id.idTVResponseLogin);
        loadingPBLogin = findViewById(R.id.idLoadingPBLogin);

        // adding on click listener to our button.
        postDataBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the text field is empty or not.
                String user = nameEdtLogin.getText().toString();
                String pass = passEdtLogin.getText().toString();


                if (user.isEmpty() || pass.isEmpty() ) {
                    Toast.makeText(LoginActivity.this, "Please enter all the values", Toast.LENGTH_SHORT).show();
                    return;
                }

                // calling a method to post the data and passing our name and job.
                loginAPI(nameEdtLogin.getText().toString(), passEdtLogin.getText().toString());
            }
        });
    }

    private void loginAPI(String name, String pass) {

        // below line is for displaying our progress bar.
        loadingPBLogin.setVisibility(View.VISIBLE);

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
        Call<DataModal> call = retrofitAPI.login(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<DataModal>() {
            @Override
            // this method is called when we get response from our api.
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {

                // below line is for hiding our progress bar.
                loadingPBLogin.setVisibility(View.GONE);

                // on below line we are setting empty text
                // to our both edit text.
                nameEdtLogin.setText("");
                passEdtLogin.setText("");

                // on below line we are getting our data from modal class and adding it to our string.
                String responseString = "Response Code - " + response.code() ;
                // below line we are setting our 123
                // string to our text view.

                //Response code between 200-300
                if(response.isSuccessful()){
                    responseTVLogin.setText("Response");
                    Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }
                else{
                    if(response.code()==401){
                        responseTVLogin.setText(responseString+" : Wrong Credentials");
                    }
                    else if(response.code()==404){
                        responseTVLogin.setText(responseString+" : User not found!");
                    }
                }

            }

            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                responseTVLogin.setText("Error found is : " + t.getMessage());
            }
        });
    }
}