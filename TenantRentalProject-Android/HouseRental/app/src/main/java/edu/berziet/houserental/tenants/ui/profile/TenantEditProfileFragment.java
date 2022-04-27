package edu.berziet.houserental.tenants.ui.profile;

import static edu.berziet.houserental.ApiUrl.TENANT_PROFILE_URL;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.berziet.houserental.R;
import edu.berziet.houserental.dbhelpers.RentalSqliteOpenHelper;
import edu.berziet.houserental.models.CityModel;
import edu.berziet.houserental.models.CountryModel;
import edu.berziet.houserental.models.NationalityModel;
import edu.berziet.houserental.models.TenantModel;
import edu.berziet.houserental.models.TenantProfileResponseModel;
import edu.berziet.houserental.parsers.TenantProfileJsonParser;
import edu.berziet.houserental.tools.HttpManager;
import edu.berziet.houserental.tools.SharedPrefManager;

public class TenantEditProfileFragment extends Fragment {

    private List<NationalityModel> nationalitiesList = new ArrayList<>();
    private RentalSqliteOpenHelper sqlHelper;
    private CountryModel selectedCountry;
    private CityModel selectedCityModel;
    private NationalityModel selectedNationality;
    private SharedPrefManager sharedPrefManager;
    private EditText emailET, firstNameET, lastNameET,
            monthlySalaryET, occupationET, familySizeET, phoneNumberET;
    private Spinner countrySpinner,citySpinner,genderSpinner,nationalitySpinner;
    private TextView countryZipCodeTextView;
    private List<CityModel> citiesList = new ArrayList<>();
    private List<CountryModel> countriesList = new ArrayList<>();

    public TenantEditProfileFragment() {
    }


    public static TenantEditProfileFragment newInstance() {
        TenantEditProfileFragment fragment = new TenantEditProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefManager = SharedPrefManager.getInstance(getContext());
        sqlHelper = new RentalSqliteOpenHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tenant_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailET = view.findViewById(R.id.tenantEditProfile_emailAddressET);
        firstNameET = view.findViewById(R.id.tenantEditProfile_firstNameET);
        lastNameET = view.findViewById(R.id.tenantEditProfile_lastNameET);
        nationalitySpinner = view.findViewById(R.id.tenantEditProfile_nationalitySpinner);
        countrySpinner = view.findViewById(R.id.tenantEditProfile_countrySpinner);
        citySpinner = view.findViewById(R.id.tenantEditProfile_citySpinner);
        genderSpinner = view.findViewById(R.id.tenantEditProfile_genderSpinner);
        monthlySalaryET = view.findViewById(R.id.tenantEditProfile_monthlySalaryET);
        occupationET = view.findViewById(R.id.tenantEditProfile_occupationET);
        familySizeET = view.findViewById(R.id.tenantEditProfile_familySizeET);
        phoneNumberET = view.findViewById(R.id.tenantEditProfile_phoneNumberET);
        countryZipCodeTextView = view.findViewById(R.id.countryZipCodeTextView);

        countriesList = sqlHelper.getCountriesList();
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
                    if(selectedCityModel!=null){
                        fillCitiesSpinner(selectedCityModel.getId());
                    }else {
                        fillCitiesSpinner(0);
                    }
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

        String url = String.format(Locale.ENGLISH, TENANT_PROFILE_URL, sharedPrefManager.getEmail());
        TenantProfileAsyncTask profileAsyncTask = new TenantProfileAsyncTask();
        profileAsyncTask.execute(url);

        view.findViewById(R.id.tenantEditProfile_saveButton)
                .setOnClickListener(new View.OnClickListener() {
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

    private void showSnackbar(int msgResourceId) {
        View rootLayout = getView();
        Snackbar.make(rootLayout, getResources().getString(msgResourceId), BaseTransientBottomBar.LENGTH_SHORT).show();
    }
    public void setProgress(boolean progress) {
        ProgressBar progressBar = getView().findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
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

        TenantUpdateProfileAsyncTask updateAsyncTask = new TenantUpdateProfileAsyncTask();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("emailAddress", email);
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
            String url = String.format(TENANT_PROFILE_URL,sharedPrefManager.getUserId());
            updateAsyncTask.execute(url, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class TenantProfileAsyncTask extends AsyncTask<String, String,
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

            TenantProfileResponseModel tenantProfileResponseModel =
                    TenantProfileJsonParser.getObjectFromJson(s);
            if (tenantProfileResponseModel == null) {
                Toast.makeText(getContext(), "Error in server response", Toast.LENGTH_SHORT).show();
                return;
            }
            if (tenantProfileResponseModel.getExist()) {
                TenantModel tenantModel = tenantProfileResponseModel.getTenant();
                setProfileInfo(tenantModel);
            }else{
                Toast.makeText(getContext(), "Profile Does Not exist", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void setProfileInfo(TenantModel tenantModel) {
        emailET.setText(tenantModel.getEmailAddress());
        firstNameET.setText(tenantModel.getFirstName());
         lastNameET.setText(tenantModel.getLastName());
        monthlySalaryET.setText(String.valueOf(tenantModel.getMonthlySalary()));
         occupationET.setText(tenantModel.getOccupation());
         familySizeET.setText(String.valueOf(tenantModel.getFamilySize()));

        CountryModel tenantCountry = sqlHelper.getCountryById(tenantModel.getCountryId());
        selectedCountry = tenantCountry;
        countryZipCodeTextView.setText(tenantCountry.getZipCode());
        for(int i = 0 ; i<countriesList.size();i++){
            CountryModel countryModel = countriesList.get(i);
            if(countryModel.getCountryId()==tenantCountry.getCountryId()){
                countrySpinner.setSelection(i+1);
                break;
            }
        }
        citiesList = sqlHelper.getCountryCities(tenantCountry.getCountryId());
        CityModel tenantCity = sqlHelper.getCityById(tenantModel.getCityId());
        selectedCityModel = tenantCity;
        fillCitiesSpinner(tenantCity.getId());

        String phoneNumber = tenantModel.getPhoneNumber().substring(tenantCountry.getZipCode().length());
        phoneNumberET.setText(phoneNumber);

        // gender
        genderSpinner.setSelection(tenantModel.getGender().equalsIgnoreCase("male")?1:2);

        for(int i = 0; i<nationalitiesList.size();i++){
            NationalityModel nationalityModel = nationalitiesList.get(i);
            if(nationalityModel.getNationalityId()==tenantModel.getNationalityId()){
                nationalitySpinner.setSelection(i+1);
                break;
            }
        }
    }

    public class TenantUpdateProfileAsyncTask extends AsyncTask<String, String,
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

//            TenantProfileResponseModel tenantProfileResponseModel =
//                    TenantProfileJsonParser.getObjectFromJson(s);
//            if (tenantProfileResponseModel == null) {
//                Toast.makeText(getContext(), "Error in server response", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (tenantProfileResponseModel.getExist()) {
//                TenantModel tenantModel = tenantProfileResponseModel.getTenant();
//                setProfileInfo(tenantModel);
//            }else{
//                Toast.makeText(getContext(), "Profile Does Not exist", Toast.LENGTH_SHORT).show();
//                return;
//            }
        }
    }

}