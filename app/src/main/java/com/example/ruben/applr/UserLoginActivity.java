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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UserLoginActivity extends AppCompatActivity {

    EditText Username, Password;
    Button LogIn ;
    String PasswordHolder, UsernameHolder;
    String finalResult ;
    String HttpURL = "http://allord.pcriot.com/login.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    public static final String UsernameF = "";

    // La respuesta del JSON es
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        Username = (EditText)findViewById(R.id.username);
        Password = (EditText)findViewById(R.id.password);
        LogIn = (Button)findViewById(R.id.Login);

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){
                    UserLoginFunction(UsernameHolder, PasswordHolder);
                } else {
                    Toast.makeText(UserLoginActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    public void CheckEditTextIsEmptyOrNot(){

        UsernameHolder = Username.getText().toString();
        PasswordHolder = Password.getText().toString();

        if(TextUtils.isEmpty(UsernameHolder) || TextUtils.isEmpty(PasswordHolder)) {
            CheckEditText = false;
        } else {
            CheckEditText = true ;
        }
    }

    public void UserLoginFunction(final String user, final String password){

        class UserLoginClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(UserLoginActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                
                int success = 0;
                String message = "";
                try {
                    JSONObject reader = new JSONObject(httpResponseMsg);
                    success = reader.getInt(TAG_SUCCESS);
                    message = reader.getString(TAG_MESSAGE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(success == 2){
                    // Creamos el mensaje de login correcto
                    Log.d("Login correcto!", user);

                    // Le dirigimos al layout de "Busqueda"
                    Intent i = new Intent(UserLoginActivity.this, Insertar.class);
                    finish();
                    startActivity(i);
                } else if(success == 1){
                    Log.d("Login correcto!", user);

                    // Le dirigimos al layout de "Busqueda"
                    Intent i = new Intent(UserLoginActivity.this, Busqueda.class);
                    finish();
                    startActivity(i);
                } else {
                    Toast.makeText(UserLoginActivity.this,message,Toast.LENGTH_LONG).show();
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

        UserLoginClass userLoginClass = new UserLoginClass();
        userLoginClass.execute(user,password);
    }
}
