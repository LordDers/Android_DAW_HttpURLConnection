package com.example.ruben.applr;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Insertar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Declaramos las variables
    private EditText name, lastname, note;
    String nameHolder, lastnameHolder, noteHolder;
    private Button mEnviar;
    private Spinner spinner;

    String opcionSeleccionada;

    String finalResult ;
    String HttpURL = "http://allord.pcriot.com/insertar.php";
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
        setContentView(R.layout.insertar);

        // Enlazamos a las variables los campos que se introducirán (editText)
        name = (EditText)findViewById(R.id.editText);
        lastname = (EditText)findViewById(R.id.editText2);
        note = (EditText)findViewById(R.id.editText3);
        spinner = (Spinner) findViewById(R.id.spinner);

        mEnviar = (Button)findViewById(R.id.enviar);

        mEnviar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){
                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    UserInsertFunction(nameHolder, lastnameHolder, opcionSeleccionada, noteHolder);
                }
                else {
                    // If EditText is empty then this block will execute .
                    Toast.makeText(Insertar.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        // ArrayList de las asignaturas
        ArrayList<String> asignaturas = new ArrayList<String>();
        asignaturas.add("programacion");
        asignaturas.add("entornos");
        asignaturas.add("bd");
        asignaturas.add("sistemas");
        asignaturas.add("lenguaje");
        asignaturas.add("ingles");
        asignaturas.add("fol");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, asignaturas);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        opcionSeleccionada = item;

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void CheckEditTextIsEmptyOrNot(){

        nameHolder = name.getText().toString();
        lastnameHolder = lastname.getText().toString();
        noteHolder = note.getText().toString();

        if(TextUtils.isEmpty(nameHolder) ||  TextUtils.isEmpty(lastnameHolder) || TextUtils.isEmpty(noteHolder)) {
            CheckEditText = false;
        } else {
            CheckEditText = true ;
        }
    }

    public void UserInsertFunction(final String nombre, final String apellido, final String asignatura, final String nota){

        class UserRegisterFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(Insertar.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                //Toast.makeText(Insertar.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();


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
                    Log.d("Alumno a\u00f1adido", message);
                    //finish();
                    // Mostramos el mensaje
                    //return json.getString(TAG_MESSAGE);
                    Toast.makeText(Insertar.this,message,Toast.LENGTH_LONG).show();
                }else {
                    Log.d("No se ha podido a\u00f1adir", message);
                    // Mostramos el mensaje
                    //return json.getString(TAG_MESSAGE);
                    Toast.makeText(Insertar.this,message,Toast.LENGTH_LONG).show();

                }
            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("nombre",params[0]);
                hashMap.put("apellido",params[1]);
                hashMap.put("asignatura",params[2]);
                hashMap.put("nota",params[3]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();
        userRegisterFunctionClass.execute(nombre, apellido, asignatura, nota);
    }
}
