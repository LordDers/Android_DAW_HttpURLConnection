package com.example.ruben.applr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button register, log_in;
    EditText Username, Password ;
    String Username_Holder, PasswordHolder;
    String finalResult ;
    String HttpURL = "http://allord.pcriot.com/register.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    // La respuesta del JSON es
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign Id'S
        Username = (EditText)findViewById(R.id.editTextUsername);
        Password = (EditText)findViewById(R.id.editTextPassword);

        register = (Button)findViewById(R.id.Submit);
        log_in = (Button)findViewById(R.id.Login);

        //Adding Click Listener on button.
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checking whether EditText is Empty or Not
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){
                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    UserRegisterFunction(Username_Holder, PasswordHolder);
                }
                else {
                    // If EditText is empty then this block will execute .
                    Toast.makeText(MainActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }
            }
        });

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,UserLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void CheckEditTextIsEmptyOrNot(){

        Username_Holder = Username.getText().toString();
        PasswordHolder = Password.getText().toString();

        if(TextUtils.isEmpty(Username_Holder) ||  TextUtils.isEmpty(PasswordHolder)) {
            CheckEditText = false;
        } else {
            CheckEditText = true ;
        }
    }

    public void UserRegisterFunction(final String user, final String password){

        class UserRegisterFunctionClass extends AsyncTask<String,Void,String> {

            /*
            onPreExecute(), invoked on the UI thread before the task is executed. Here We are displaying loading message.
            doInBackground(Params…), invoked on the background thread immediately after
            onPreExecute() finishes executing. The sending and recieving data from and to php file using HttpURLConnection class has done in this function.
            onPostExecute(Result), invoked on the UI thread after the background computation finishes. Here we are checking for recieved result.
             */

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(MainActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();

                //Toast.makeText(MainActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();


                int success = 0;
                String message = "";
                try {
                    JSONObject reader = new JSONObject(httpResponseMsg);
                    success = reader.getInt(TAG_SUCCESS);
                    message = reader.getString(TAG_MESSAGE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (success == 1) {
                    // Creamos el mensaje
                    Log.d("Usuario creado!", message);
                    //finish();
                    // Mostramos el mensaje
                    //return json.getString(TAG_MESSAGE);
                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
                }else{
                    // Creamos el mensaje
                    Log.d("Fallo en el registro!", message);
                    // Mostramos el mensaje
                    //return json.getString(TAG_MESSAGE);
                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("username",params[0]);
                hashMap.put("password",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();
        userRegisterFunctionClass.execute(user,password);
    }
}
