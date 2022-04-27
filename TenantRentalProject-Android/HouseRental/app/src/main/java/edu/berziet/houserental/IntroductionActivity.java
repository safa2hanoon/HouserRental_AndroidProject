package edu.berziet.houserental;

import static edu.berziet.houserental.ApiUrl.CONNECT_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import edu.berziet.houserental.auth.AuthActivity;
import edu.berziet.houserental.dbhelpers.RentalSqliteOpenHelper;
import edu.berziet.houserental.models.CityModel;
import edu.berziet.houserental.models.ConnectModel;
import edu.berziet.houserental.models.CountryModel;
import edu.berziet.houserental.models.NationalityModel;
import edu.berziet.houserental.models.PropertyModel;
import edu.berziet.houserental.parsers.ConnectJsonParser;
import edu.berziet.houserental.tools.HttpManager;

public class IntroductionActivity extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        button = findViewById(R.id.connectButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask();
                connectionAsyncTask.execute(CONNECT_URL);
            }
        });
    }

    public void setButtonText(String text){
        button.setText(text);
    }

    public class ConnectionAsyncTask extends AsyncTask<String, String,
                String> {
        @Override
        protected void onPreExecute() {
            setButtonText("connecting");
            super.onPreExecute();
            setProgress(true);
        }
        @Override
        protected String doInBackground(String... params) {
            return HttpManager.getData(params[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setProgress(false);
            setButtonText("connect");

            if(s==null){
                Toast.makeText(IntroductionActivity.this, "Error connecting to server", Toast.LENGTH_SHORT).show();
                return;
            }

            ConnectModel connectModel =
                    ConnectJsonParser.getObjectFromJson(s);

            RentalSqliteOpenHelper sqlHelper = new RentalSqliteOpenHelper(IntroductionActivity.this);

            sqlHelper.deleteAllProperties();
            for (PropertyModel property :
                    connectModel.getPropertiesList()) {
                sqlHelper.insertProperty(property);
            }

            sqlHelper.deleteAllNationalities();
            for (NationalityModel nationality :
                    connectModel.getNationalitiesList()) {
                sqlHelper.insertNationality(nationality);
            }

            sqlHelper.deleteAllCountries();
            for (CountryModel country : connectModel.getCountriesList()){
                sqlHelper.addCountry(country);
            }

            sqlHelper.deleteAllCities();
            for(CityModel city : connectModel.getCitiesList()){
                sqlHelper.insertCity(city);
            }

            Intent i = new Intent(IntroductionActivity.this, AuthActivity.class);
            startActivity(i);
            finish();
        }
    }
    public void setProgress(boolean progress) {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}