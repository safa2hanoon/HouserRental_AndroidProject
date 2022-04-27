package edu.berziet.houserental.auth.ui.tenantsignup;

import static edu.berziet.houserental.ApiUrl.TENANTS_URL;

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
import edu.berziet.houserental.dbhelpers.RentalSqliteOpenHelper;
import edu.berziet.houserental.models.CityModel;
import edu.berziet.houserental.models.CountryModel;
import edu.berziet.houserental.models.NationalityModel;
import edu.berziet.houserental.models.SimpleSuccessResponseModel;
import edu.berziet.houserental.parsers.SimpleSuccessResponseJsonParser;
import edu.berziet.houserental.tenants.TenantActivity;
import edu.berziet.houserental.tools.HttpManager;
import edu.berziet.houserental.tools.SharedPrefManager;

public class TenantSignUpFragment extends Fragment {
    List<CityModel> citiesList = new ArrayList<>();
    List<NationalityModel> nationalitiesList = new ArrayList<>();
    private EditText emailET, firstNameET, lastNameET, passwordET, confirmPasswordET, phoneNumberET,
            monthlySalaryET, familySizeET, occupationET;
    private TextView countryZipCodeTextView;
    private Spinner countrySpinner, citySpinner, nationalitySpinner, genderSpinner;
    private RentalSqliteOpenHelper sqlHelper;
    private CountryModel selectedCountry;
    private CityModel selectedCityModel;
    private NationalityModel selectedNationality;
    private SharedPrefManager prefs;

    public TenantSignUpFragment() {
    }

    public static TenantSignUpFragment newInstance() {
        TenantSignUpFragment fragment = new TenantSignUpFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqlHelper = new RentalSqliteOpenHelper(getContext());
        prefs = SharedPrefManager.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tenant_sign_up, container, false);
        emailET = root.findViewById(R.id.emailAddressET);
        firstNameET = root.findViewById(R.id.firstNameET);
        lastNameET = root.findViewById(R.id.lastNameET);
        passwordET = root.findViewById(R.id.passwordET);
        confirmPasswordET = root.findViewById(R.id.confirmPasswordET);
        monthlySalaryET = root.findViewById(R.id.monthlySalaryET);
        familySizeET = root.findViewById(R.id.familySizeET);
        occupationET = root.findViewById(R.id.occupationET);
        phoneNumberET = root.findViewById(R.id.phoneNumberET);

        countryZipCodeTextView = root.findViewById(R.id.countryZipCodeTextView);

        countrySpinner = root.findViewById(R.id.countrySpinner);
        citySpinner = root.findViewById(R.id.citySpinner);
        nationalitySpinner = root.findViewById(R.id.nationalitySpinner);
        genderSpinner = root.findViewById(R.id.genderSpinner);

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
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    emptyCitiesSpinner();
                    selectedCountry = null;
                    countryZipCodeTextView.setText("");
                } else {
                    CountryModel country = countriesList.get(i - 1);
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

        nationalitiesList = sqlHelper.getNationalityList();
        List<String> nationalitiesStringList = new ArrayList<>();
        nationalitiesStringList.add("Select Nationality");
        for (NationalityModel nationality :
                nationalitiesList) {
            nationalitiesStringList.add(nationality.getNationality());
        }

        ArrayAdapter<String> nationalityAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, nationalitiesStringList);
        nationalitySpinner.setAdapter(nationalityAdapter);
        nationalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    selectedNationality = null;
                } else {
                    selectedNationality = nationalitiesList.get(i - 1);
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
        String firstName = firstNameET.getText().toString();
        if (firstName.isEmpty()) {
            firstNameET.setError(getResources().getString(R.string.enter_first_name));
            showSnackbar(R.string.enter_first_name);
            firstNameET.requestFocus();
            return;
        } else {
            if (firstName.length() > 20 || firstName.length() < 3) {
                firstNameET.setError(getResources().getString(R.string.first_name_lenght_error));
                showSnackbar(R.string.first_name_lenght_error);
                firstNameET.requestFocus();
                return;
            }
            firstNameET.setError(null);
        }
        String lastName = lastNameET.getText().toString();
        if (lastName.isEmpty()) {
            lastNameET.setError(getResources().getString(R.string.enter_last_name));
            showSnackbar(R.string.enter_last_name);
            lastNameET.requestFocus();
            return;
        } else {
            if (lastName.length() > 20 || lastName.length() < 3) {
                firstNameET.setError(getResources().getString(R.string.lasst_name_lenght_error));
                showSnackbar(R.string.first_name_lenght_error);
                firstNameET.requestFocus();
                return;
            }
            lastNameET.setError(null);
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
        if (nationalitySpinner.getSelectedItemPosition() == 0) {
            showSnackbar(R.string.select_nationality);
            return;
        }
        if (countrySpinner.getSelectedItemPosition() == 0) {
            showSnackbar(R.string.select_country);
            return;
        }
        if (citySpinner.getSelectedItemPosition() == 0) {
            showSnackbar(R.string.select_city);
            return;
        }
        if (genderSpinner.getSelectedItemPosition() == 0) {
            showSnackbar(R.string.select_gender);
            return;
        }

        String monthlySalaryString = monthlySalaryET.getText().toString();
        if (monthlySalaryString.isEmpty()) {
            monthlySalaryET.setError(getResources().getString(R.string.enter_monthly_salary));
            showSnackbar(R.string.enter_confirm_password);
            monthlySalaryET.requestFocus();
            return;
        } else {
            try {
                int salary = Integer.parseInt(monthlySalaryString);
                if (salary <= 0) {
                    monthlySalaryET.setError(getResources().getString(R.string.invalid_monthly_salary));
                    showSnackbar(R.string.invalid_monthly_salary);
                    monthlySalaryET.requestFocus();
                    return;
                } else {
                    monthlySalaryET.setError(null);
                }
            } catch (NumberFormatException nfe) {
                monthlySalaryET.setError(getResources().getString(R.string.invalid_monthly_salary));
                showSnackbar(R.string.invalid_monthly_salary);
                monthlySalaryET.requestFocus();
            }
        }
        String occupation = occupationET.getText().toString();
        if (occupation.isEmpty()) {
            occupationET.setError(getResources().getString(R.string.enter_occupation));
            showSnackbar(R.string.enter_occupation);
            occupationET.requestFocus();
        } else {
            if (occupation.length() > 20) {
                occupationET.setError(getResources().getString(R.string.occupation_lenght_error));
                showSnackbar(R.string.occupation_lenght_error);
                occupationET.requestFocus();
                return;
            }
            occupationET.setError(null);
        }

        String familySizeString = familySizeET.getText().toString();
        if (familySizeString.isEmpty()) {
            familySizeET.setError(getResources().getString(R.string.enter_family_size));
            showSnackbar(R.string.enter_family_size);
            familySizeET.requestFocus();
            return;
        } else {
            try {
                int familySize = Integer.parseInt(familySizeString);
                if (familySize <= 0) {
                    familySizeET.setError(getResources().getString(R.string.invalid_family_size));
                    showSnackbar(R.string.invalid_family_size);
                    familySizeET.requestFocus();
                    return;
                } else {
                    familySizeET.setError(null);
                }
            } catch (NumberFormatException nfe) {
                familySizeET.setError(getResources().getString(R.string.invalid_family_size));
                showSnackbar(R.string.invalid_family_size);
                familySizeET.requestFocus();
            }
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
            jsonObject.put("residenceCountryId", selectedCountry.getCountryId());
            jsonObject.put("residenceCityId", selectedCityModel.getId());
            jsonObject.put("firstName", firstName);
            jsonObject.put("lastName", lastName);
            jsonObject.put("gender", genderSpinner.getSelectedItem().toString());
            jsonObject.put("nationalityId", selectedNationality.getNationalityId());
            jsonObject.put("monthlySalary", monthlySalaryString);
            jsonObject.put("occupation", occupation);
            jsonObject.put("familySize", familySizeString);
            phoneNumber = selectedCountry.getZipCode().concat(phoneNumber);
            jsonObject.put("phoneNumber", phoneNumber);
            signUpAsyncTask.execute(TENANTS_URL, jsonObject.toString());
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
            System.out.println("Tenant Sign up : " + s);
            SimpleSuccessResponseModel responseModel = SimpleSuccessResponseJsonParser
                    .getObjectFromJson(s);
            if (responseModel == null) {
                Toast.makeText(getContext(), getResources().getString(R.string.error_sign_up), Toast.LENGTH_SHORT).show();
                return;
            }
            if (responseModel.isSuccess()) {
                Toast.makeText(getContext(), getResources().getString(R.string.sign_up_success), Toast.LENGTH_SHORT).show();
                prefs.setCountryName(selectedCountry.getName());
                prefs.setCountryId(selectedCountry.getCountryId());
                prefs.setCityName(selectedCityModel.getName());
                prefs.setCityId(selectedCityModel.getId());
                prefs.setRole("Tenant");
                String email = emailET.getText().toString();
                prefs.setUserId(email);
                prefs.setEmail(email);
                String name = firstNameET.getText().toString() + " " +
                        lastNameET.getText().toString();
                prefs.setName(name);
                Intent agencyIntent = new Intent(getContext(), TenantActivity.class);
                startActivity(agencyIntent);
                getActivity().finish();
            }else{
                Toast.makeText(requireContext(), responseModel.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}