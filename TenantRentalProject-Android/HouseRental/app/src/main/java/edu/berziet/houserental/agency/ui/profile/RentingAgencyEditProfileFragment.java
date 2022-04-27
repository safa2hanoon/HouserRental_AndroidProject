package edu.berziet.houserental.agency.ui.profile;

import static edu.berziet.houserental.ApiUrl.AGENCY_PROFILE_URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.berziet.houserental.R;
import edu.berziet.houserental.agency.RentingAgencyMainActivity;
import edu.berziet.houserental.dbhelpers.RentalSqliteOpenHelper;
import edu.berziet.houserental.models.AgencyProfileResponseModel;
import edu.berziet.houserental.models.CityModel;
import edu.berziet.houserental.models.CountryModel;
import edu.berziet.houserental.models.RentingAgencyModel;
import edu.berziet.houserental.models.SimpleSuccessResponseModel;
import edu.berziet.houserental.parsers.RentingAgencyResponseJsonParser;
import edu.berziet.houserental.parsers.SimpleSuccessResponseJsonParser;
import edu.berziet.houserental.tools.HttpManager;
import edu.berziet.houserental.tools.SharedPrefManager;

public class RentingAgencyEditProfileFragment extends Fragment {

    private SharedPrefManager sharedPrefManager;
    private EditText emailET,nameET,phoneNumberET;
    private Spinner countrySpinner, citySpinner;
    private TextView zipCodeTextView;
    private RentalSqliteOpenHelper sqliteOpenHelper;
    private CountryModel selectedCountry;
    private CityModel selectedCityModel;
    private List<CityModel> citiesList = new ArrayList<>();
    private List<CountryModel> countriesList = new ArrayList<>();

    public RentingAgencyEditProfileFragment() {
        // Required empty public constructor
    }

    public static RentingAgencyEditProfileFragment newInstance() {
        RentingAgencyEditProfileFragment fragment = new RentingAgencyEditProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefManager = SharedPrefManager.getInstance(getContext());
        sqliteOpenHelper = new RentalSqliteOpenHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_renting_agency_edit_profile, container, false);
        emailET = root.findViewById(R.id.agencyEditProfile_emailAddressET);
        nameET = root.findViewById(R.id.agencyEditProfile_agencyNameET);
        countrySpinner = root.findViewById(R.id.agencyEditProfile_countrySpinner);
        citySpinner = root.findViewById(R.id.agencyEditProfile_citySpinner);
        phoneNumberET = root.findViewById(R.id.agencyEditProfile_phoneNumberET);
        zipCodeTextView = root.findViewById(R.id.agencyEditProfile_countryZipCodeTextView);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        countriesList = sqliteOpenHelper.getCountriesList();
        List<String> countriesNames = new ArrayList<>();
        for (CountryModel country :
                countriesList) {
            countriesNames.add(country.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, countriesNames);
        countrySpinner.setAdapter(adapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    CountryModel country = countriesList.get(i);
                    selectedCountry = country;
                    zipCodeTextView.setText(country.getZipCode());
                    citiesList = sqliteOpenHelper.getCountryCities(country.getCountryId());
                    if(selectedCityModel!=null){
                        fillCitiesSpinner(selectedCityModel.getId());
                    }else {
                        fillCitiesSpinner(0);
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    selectedCityModel = null;
                    return;
                }
                CityModel cityModel = citiesList.get(i-1);
                    selectedCityModel = cityModel;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        String url = String.format(Locale.ENGLISH, AGENCY_PROFILE_URL, sharedPrefManager.getEmail());
        AgencyProfileAsyncTask profileAsyncTask = new AgencyProfileAsyncTask();
        profileAsyncTask.execute(url);

        view.findViewById(R.id.agencyEditProfile_saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInputs();
            }
        });
    }

    private void showSnackbar(int msgResourceId){
        Snackbar.make(
                getView(),
                getResources().getString(msgResourceId),
                BaseTransientBottomBar.LENGTH_SHORT)
                .show();
    }

    private void checkInputs() {
        String name = nameET.getText().toString();
        if(name.isEmpty()){
            nameET.setError(getResources().getString(R.string.enter_name));
            nameET.requestFocus();
            showSnackbar(R.string.enter_name);
            return;
        }else{
            nameET.setError(null);
        }
        if(selectedCountry==null){
            showSnackbar(R.string.select_country);
            return;
        }
        if(selectedCityModel==null){
            showSnackbar(R.string.select_city);
            return;
        }

        String phoneNumber = phoneNumberET.getText().toString();
        if(phoneNumber.isEmpty()){
            phoneNumberET.setError(getResources().getString(R.string.enter_phone_number));
            showSnackbar(R.string.enter_phone_number);
            phoneNumberET.requestFocus();
            return;
        }else{
            phoneNumberET.setError(null);
        }

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("name",name);
            requestJsonObject.put("countryId",selectedCountry.getCountryId());
            requestJsonObject.put("cityId",selectedCityModel.getId());
            phoneNumber = selectedCountry.getZipCode()+phoneNumber;
            requestJsonObject.put("phoneNumber",phoneNumber);
            EditAgencyProfileAsyncTask editAsyncTask = new EditAgencyProfileAsyncTask();
            String url = String.format(AGENCY_PROFILE_URL,emailET.getText().toString());
            editAsyncTask.execute(url,requestJsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void fillCitiesSpinner(int cityId){
        List<String> citiesNames = new ArrayList<>();
        citiesNames.add(getResources().getString(R.string.select_city));
        int selectedPosition = 0;
        for(int i = 0 ; i<citiesList.size();i++){
            CityModel city = citiesList.get(i);
            citiesNames.add(city.getName());
            if(city.getId()==cityId){
                selectedCityModel = city;
                selectedPosition = i+1;
            }
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, citiesNames);
        citySpinner.setAdapter(adapter2);
        citySpinner.setSelection(selectedPosition);
    }

    public void setProgress(boolean progress) {
        ProgressBar progressBar = getView().findViewById(R.id.agencyEditProfile_progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public class AgencyProfileAsyncTask extends AsyncTask<String, String,
            String> {
        @Override
        protected void onPreExecute() {
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

            if (s == null) {
                Toast.makeText(getContext(), "Error connecting to server", Toast.LENGTH_SHORT).show();
                return;
            }

            AgencyProfileResponseModel agencyProfileResponseModel =
                    RentingAgencyResponseJsonParser.getObjectFromJson(s);
            if (agencyProfileResponseModel == null) {
                Toast.makeText(getContext(), "Error in server response", Toast.LENGTH_SHORT).show();
                return;
            }
            if (agencyProfileResponseModel.getExist()) {
                RentingAgencyModel agencyModel = agencyProfileResponseModel.getAgency();
                setProfileInfo(agencyModel);
            }else{
                Toast.makeText(getContext(), "Profile Does Not exist", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setProfileInfo(RentingAgencyModel agencyModel) {
        emailET.setText(agencyModel.getEmailAddress());
        nameET.setText(agencyModel.getName());
        CountryModel agencyCountry = sqliteOpenHelper.getCountryById(agencyModel.getCountryId());
        selectedCountry = agencyCountry;
        zipCodeTextView.setText(agencyCountry.getZipCode());
        for(int i = 0 ; i<countriesList.size();i++){
            CountryModel countryModel = countriesList.get(i);
            if(countryModel.getCountryId()==agencyCountry.getCountryId()){
                countrySpinner.setSelection(i);
                break;
            }
        }
        citiesList = sqliteOpenHelper.getCountryCities(agencyCountry.getCountryId());
        CityModel agencyCity = sqliteOpenHelper.getCityById(agencyModel.getCityId());
        selectedCityModel = agencyCity;
        fillCitiesSpinner(agencyCity.getId());

        String phoneNumber = agencyModel.getPhoneNumber().substring(agencyCountry.getZipCode().length());
        phoneNumberET.setText(phoneNumber);
    }

    public class EditAgencyProfileAsyncTask extends AsyncTask<String, String,
            String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpManager.sendRequest(params[0],"PATCH",params[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setProgress(false);

            if (s == null) {
                Toast.makeText(getContext(), "Error connecting to server", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleSuccessResponseModel responseModel = SimpleSuccessResponseJsonParser
                    .getObjectFromJson(s);
            if (responseModel == null) {
                Toast.makeText(getContext(), "Error in server response", Toast.LENGTH_SHORT).show();
                return;
            }
            if (responseModel.isSuccess()) {
                SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getContext());
                sharedPrefManager.setCountryId(selectedCountry.getCountryId());
                sharedPrefManager.setCountryName(selectedCountry.getName());
                sharedPrefManager.setCityId(selectedCityModel.getId());
                sharedPrefManager.setCityName(selectedCityModel.getName());
                sharedPrefManager.setName(nameET.getText().toString());
                Snackbar.make(
                        getView(),
                        getResources().getString(R.string.edited_successfully),
                        BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
                RentingAgencyMainActivity activity = (RentingAgencyMainActivity) getActivity();
                activity.setHeaderNameAndEmail();
            }else{
                Toast.makeText(getContext(), responseModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}