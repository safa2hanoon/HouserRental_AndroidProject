package edu.berziet.houserental.auth.ui.agencysignup;

import static edu.berziet.houserental.ApiUrl.AGENCIES_URL;

import android.content.Intent;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.berziet.houserental.R;
import edu.berziet.houserental.agency.RentingAgencyMainActivity;
import edu.berziet.houserental.dbhelpers.RentalSqliteOpenHelper;
import edu.berziet.houserental.models.CityModel;
import edu.berziet.houserental.models.CountryModel;
import edu.berziet.houserental.models.SimpleSuccessResponseModel;
import edu.berziet.houserental.parsers.SimpleSuccessResponseJsonParser;
import edu.berziet.houserental.tools.HttpManager;
import edu.berziet.houserental.tools.SharedPrefManager;

public class RentingAgencySignUpFragment extends Fragment {

    List<CityModel> citiesList = new ArrayList<>();
    private EditText emailET, nameET, passwordET, confirmPasswordET, phoneNumberET;
    private TextView countryZipCodeTextView;
    private Spinner countrySpinner, citySpinner;
    private RentalSqliteOpenHelper sqlHelper;
    private CountryModel selectedCountry;
    private CityModel selectedCityModel;
    private SharedPrefManager prefs;

    public RentingAgencySignUpFragment() {
    }

    public static RentingAgencySignUpFragment newInstance() {
        RentingAgencySignUpFragment fragment = new RentingAgencySignUpFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqlHelper = new RentalSqliteOpenHelper(getContext());
        prefs = SharedPrefManager.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_renting_agency_sign_up, container, false);

        emailET = root.findViewById(R.id.emailAddressET);
        nameET = root.findViewById(R.id.agencyNameET);
        passwordET = root.findViewById(R.id.passwordET);
        confirmPasswordET = root.findViewById(R.id.confirmPasswordET);
        phoneNumberET = root.findViewById(R.id.phoneNumberET);
        countryZipCodeTextView = root.findViewById(R.id.countryZipCodeTextView);

        countrySpinner = root.findViewById(R.id.countrySpinner);
        citySpinner = root.findViewById(R.id.citySpinner);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<CountryModel> countriesList = sqlHelper.getCountriesList();
        List<String> countriesNames = new ArrayList<>();
        countriesNames.add(getResources().getString(R.string.select_country));
        for (CountryModel country :
                countriesList) {
            countriesNames.add(country.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, countriesNames);
        countrySpinner.setAdapter(adapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                if (index == 0) {
                    emptyCitiesSpinner();
                    selectedCountry = null;
                    countryZipCodeTextView.setText("");
                } else {
                    CountryModel country = countriesList.get(index - 1);
                    selectedCountry = country;
                    countryZipCodeTextView.setText(country.getZipCode());
                    citiesList = sqlHelper.getCountryCities(country.getCountryId());
                    fillCitiesSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    selectedCityModel = null;
                } else {
                    CityModel cityModel = citiesList.get(i - 1);
                    selectedCityModel = cityModel;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        view.findViewById(R.id.signupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInputs();
            }
        });

    }

    public void emptyCitiesSpinner() {
        citiesList = new ArrayList<>();
        List<String> citiesNames = new ArrayList<>();
        citiesNames.add(getResources().getString(R.string.select_country));
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, citiesNames);
        citySpinner.setAdapter(adapter2);
    }

    public void fillCitiesSpinner() {
        List<String> citiesNames = new ArrayList<>();
        citiesNames.add(getResources().getString(R.string.select_city));
        for (CityModel city :
                citiesList) {
            citiesNames.add(city.getName());
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, citiesNames);
        citySpinner.setAdapter(adapter2);
    }

    private void showSnackbar(int msgResourceId) {
        View rootLayout = getView();
        Snackbar.make(rootLayout, getResources().getString(msgResourceId), BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    private void checkInputs() {
        String email = emailET.getText().toString();
        if (email.isEmpty()) {
            emailET.setError(getResources().getString(R.string.enter_email));
            showSnackbar(R.string.enter_email);
            emailET.requestFocus();
            return;
        } else {
            // validate email as valid email format
            Pattern pattern = Pattern.compile("^(.+)@([^@]+[^.])$");
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                emailET.setError(getResources().getString(R.string.invalid_email_address));
                showSnackbar(R.string.invalid_email_address);
                emailET.requestFocus();
                return;
            }
            emailET.setError(null);
        }
        String name = nameET.getText().toString();
        if (name.isEmpty()) {
            nameET.setError(getResources().getString(R.string.enter_name));
            showSnackbar(R.string.enter_name);
            nameET.requestFocus();
            return;
        } else {
            if (nameET.length() > 20) {
                nameET.setError(getResources().getString(R.string.agency_name_length_error));
                showSnackbar(R.string.agency_name_length_error);
                nameET.requestFocus();
                return;
            }
            nameET.setError(null);
        }
        String password = passwordET.getText().toString();
        if (password.isEmpty()) {
            passwordET.setError(getResources().getString(R.string.enter_password));
            showSnackbar(R.string.enter_password);
            passwordET.requestFocus();
            return;
        } else {
            String passwordPolicy = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!{}])(?=\\S+$).{8,15}$";
            Pattern passRegex = Pattern.compile(passwordPolicy);
            if (!passRegex.matcher(password).matches()) {
                passwordET.setError(getResources().getString(R.string.invalid_password));
                showSnackbar(R.string.invalid_password);
                passwordET.requestFocus();
                return;
            }
            passwordET.setError(null);
        }
        String confirmPassword = confirmPasswordET.getText().toString();
        if (confirmPassword.isEmpty()) {
            confirmPasswordET.setError(getResources().getString(R.string.enter_confirm_password));
            showSnackbar(R.string.enter_confirm_password);
            confirmPasswordET.requestFocus();
            return;
        } else if (!confirmPassword.equals(password)) {
            confirmPasswordET.setError(getResources().getString(R.string.confirm_password_not_match));
            showSnackbar(R.string.confirm_password_not_match);
            confirmPasswordET.requestFocus();
            return;
        } else {
            confirmPasswordET.setError(null);
        }

        if (countrySpinner.getSelectedItemPosition() == 0) {
            showSnackbar(R.string.select_country);
            return;
        }
        if (citySpinner.getSelectedItemPosition() == 0) {
            showSnackbar(R.string.select_city);
            return;
        }
        String phoneNumber = phoneNumberET.getText().toString();
        if (phoneNumber.isEmpty()) {
            phoneNumberET.setError(getResources().getString(R.string.enter_phone_number));
            showSnackbar(R.string.enter_phone_number);
            phoneNumberET.requestFocus();
            return;
        } else {
            phoneNumberET.setError(null);
        }

        SignUpAsyncTask signUpAsyncTask = new SignUpAsyncTask();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("emailAddress", email);
            jsonObject.put("password", password);
            jsonObject.put("countryId", selectedCountry.getCountryId());
            jsonObject.put("cityId", selectedCityModel.getId());
            jsonObject.put("name", name);
            phoneNumber = selectedCountry.getZipCode().concat(phoneNumber);
            jsonObject.put("phoneNumber", phoneNumber);
            signUpAsyncTask.execute(AGENCIES_URL, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setProgress(boolean progress) {
        ProgressBar progressBar = getView().findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public class SignUpAsyncTask extends AsyncTask<String, String,
            String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpManager.sendRequest(params[0], "POST", params[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setProgress(false);
            SimpleSuccessResponseModel responseModel = SimpleSuccessResponseJsonParser
                    .getObjectFromJson(s);
            if (responseModel == null) {
                Toast.makeText(getContext(), getResources().getString(R.string.error_sign_up), Toast.LENGTH_SHORT).show();
                return;
            }
            if (responseModel.isSuccess()) {
                prefs.setCountryName(selectedCountry.getName());
                prefs.setCountryId(selectedCountry.getCountryId());
                prefs.setCityName(selectedCityModel.getName());
                prefs.setCityId(selectedCityModel.getId());
                prefs.setRole("RentingAgency");
                String name = nameET.getText().toString();
                prefs.setName(name);
                String email = emailET.getText().toString();
                prefs.setUserId(email);
                prefs.setEmail(email);
                Intent agencyIntent = new Intent(getContext(), RentingAgencyMainActivity.class);
                startActivity(agencyIntent);
                requireActivity().finish();
            }else{
                Toast.makeText(getContext(), responseModel.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}