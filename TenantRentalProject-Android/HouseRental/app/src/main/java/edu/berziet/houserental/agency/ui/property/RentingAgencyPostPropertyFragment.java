package edu.berziet.houserental.agency.ui.property;

import static edu.berziet.houserental.ApiUrl.PROPERTIES_URL;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.berziet.houserental.R;
import edu.berziet.houserental.dbhelpers.RentalSqliteOpenHelper;
import edu.berziet.houserental.models.CityModel;
import edu.berziet.houserental.models.PropertyPostResponseModel;
import edu.berziet.houserental.parsers.PropertyPostResponseJsonParser;
import edu.berziet.houserental.shared.MyDatePicker;
import edu.berziet.houserental.tools.HttpManager;
import edu.berziet.houserental.tools.SharedPrefManager;

public class RentingAgencyPostPropertyFragment extends Fragment {

    private EditText surfaceAreaET, bedroomsCountET, rentalPriceET, constructionYearET,
            availabilityDateET, countryET, descriptionET;
    private Spinner citySpinner, hasBalconySpinner, hasGardenSpinner, statusSpinner;
    private int selectedCityId = 0;
    SharedPrefManager sharedPrefManager;
    private Calendar dateCalendar;

    public RentingAgencyPostPropertyFragment() {
    }

    public static RentingAgencyPostPropertyFragment newInstance() {
        RentingAgencyPostPropertyFragment fragment = new RentingAgencyPostPropertyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_renting_agency_post_property, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPrefManager = SharedPrefManager.getInstance(getContext());
        surfaceAreaET = view.findViewById(R.id.agencyPostProperty_surfaceArea);
        bedroomsCountET = view.findViewById(R.id.agencyPostProperty_bedroomsCount);
        rentalPriceET = view.findViewById(R.id.agencyPostProperty_rentalPrice);
        constructionYearET = view.findViewById(R.id.agencyPostProperty_constructionYear);
        availabilityDateET = view.findViewById(R.id.agencyPostProperty_availabilityDate);
        dateCalendar = Calendar.getInstance();
        availabilityDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyDatePicker(requireContext(),R.string.select_date)
                        .selectDate(dateCalendar,availabilityDateET);
            }
        });
        descriptionET = view.findViewById(R.id.agencyPostProperty_description);

        citySpinner= view.findViewById(R.id.agencyPostProperty_citySpinner);
        countryET = view.findViewById(R.id.agencyPostProperty_country);
        countryET.setText(sharedPrefManager.getCountryName());
        RentalSqliteOpenHelper sqliteOpenHelper = new RentalSqliteOpenHelper(getContext());
        List<CityModel> citiesList = sqliteOpenHelper.getCountryCities(sharedPrefManager.getCountryId());
        List<String> citiesNames = new ArrayList<>();
        citiesNames.add(getResources().getString(R.string.select_city));
        for (int i = 0; i < citiesList.size(); i++) {
            CityModel city = citiesList.get(i);
            citiesNames.add(city.getName());
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, citiesNames);
        citySpinner.setAdapter(adapter2);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    selectedCityId = 0;
                    return;
                }
                CityModel cityModel = citiesList.get(i - 1);
                selectedCityId = cityModel.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        hasBalconySpinner = view.findViewById(R.id.agencyPostProperty_hasBalconySpinner);
        hasGardenSpinner = view.findViewById(R.id.agencyPostProperty_hasGardenSpinner);
        statusSpinner = view.findViewById(R.id.agencyPostProperty_statusSpinner);

        view.findViewById(R.id.agencyPostProperty_saveButton).setOnClickListener(
                new View.OnClickListener() {
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
        String surfaceAreaString = surfaceAreaET.getText().toString();
        if(surfaceAreaString.isEmpty()){
            surfaceAreaET.setError(getResources().getString(R.string.enter_surface_area));
            showSnackbar(R.string.enter_surface_area);
            surfaceAreaET.requestFocus();
            return;
        }else{
            try {
                int surfaceArea = Integer.parseInt(surfaceAreaString);
                if(surfaceArea<=0){
                    surfaceAreaET.setError(getResources().getString(R.string.invalid_surface_area_value));
                    showSnackbar(R.string.invalid_surface_area_value);
                    surfaceAreaET.requestFocus();
                    return;
                }else{
                    surfaceAreaET.setError(null);
                }
            }catch (NumberFormatException nfe){
                surfaceAreaET.setError(getResources().getString(R.string.invalid_surface_area_value));
                showSnackbar(R.string.invalid_surface_area_value);
                surfaceAreaET.requestFocus();
                return;
            }
        }

        String bedroomsCountString = bedroomsCountET.getText().toString();
        if(bedroomsCountString.isEmpty()){
            bedroomsCountET.setError(getResources().getString(R.string.enter_bedrooms_count));
            showSnackbar(R.string.enter_bedrooms_count);
            bedroomsCountET.requestFocus();
            return;
        }else{
            try {
                int bedroomsCount = Integer.parseInt(bedroomsCountString);
                if(bedroomsCount<=0){
                    bedroomsCountET.setError(getResources().getString(R.string.invalid_bedrooms_count_value));
                    showSnackbar(R.string.invalid_bedrooms_count_value);
                    bedroomsCountET.requestFocus();
                    return;
                }else{
                    bedroomsCountET.setError(null);
                }
            }catch (NumberFormatException nfe){
                bedroomsCountET.setError(getResources().getString(R.string.invalid_bedrooms_count_value));
                showSnackbar(R.string.invalid_bedrooms_count_value);
                bedroomsCountET.requestFocus();
                return;
            }
        }


        String rentalPriceString = rentalPriceET.getText().toString();
        if(rentalPriceString.isEmpty()){
            rentalPriceET.setError(getResources().getString(R.string.enter_rental_price));
            showSnackbar(R.string.enter_rental_price);
            rentalPriceET.requestFocus();
            return;
        }else{
            try {
                int rentalPrice = Integer.parseInt(rentalPriceString);
                if(rentalPrice<=0){
                    rentalPriceET.setError(getResources().getString(R.string.invalid_rental_price_value));
                    showSnackbar(R.string.invalid_rental_price_value);
                    rentalPriceET.requestFocus();
                    return;
                }else{
                    rentalPriceET.setError(null);
                }
            }catch (NumberFormatException nfe){
                rentalPriceET.setError(getResources().getString(R.string.invalid_rental_price_value));
                showSnackbar(R.string.invalid_rental_price_value);
                rentalPriceET.requestFocus();
                return;
            }
        }

        String constructionYearString = constructionYearET.getText().toString();
        if(constructionYearString.isEmpty()){
            constructionYearET.setError(getResources().getString(R.string.enter_construction_year));
            showSnackbar(R.string.enter_construction_year);
            constructionYearET.requestFocus();
            return;
        }else{
            try {
                int rentalPrice = Integer.parseInt(constructionYearString);
                if(rentalPrice<=1900){
                    constructionYearET.setError(getResources().getString(R.string.invalid_construction_year_value));
                    showSnackbar(R.string.invalid_construction_year_value);
                    constructionYearET.requestFocus();
                    return;
                }else{
                    constructionYearET.setError(null);
                }
            }catch (NumberFormatException nfe){
                constructionYearET.setError(getResources().getString(R.string.invalid_construction_year_value));
                showSnackbar(R.string.invalid_construction_year_value);
                constructionYearET.requestFocus();
                return;
            }
        }
        String availabilityDateString = availabilityDateET.getText().toString();
        if(availabilityDateString.isEmpty()){
            availabilityDateET.setError(getResources().getString(R.string.enter_availability_date));
            showSnackbar(R.string.enter_availability_date);
            availabilityDateET.requestFocus();
            return;
        }else{
            availabilityDateET.setError(null);
        }

        if(selectedCityId==0){
            showSnackbar(R.string.select_city);
            return;
        }
        if(statusSpinner.getSelectedItemPosition()==0){
            showSnackbar(R.string.select_property_status);
            return;
        }
        if(hasBalconySpinner.getSelectedItemPosition()==0){
            showSnackbar(R.string.select_if_has_balcony);
            return;
        }
        if(hasGardenSpinner.getSelectedItemPosition()==0){
            showSnackbar(R.string.select_if_has_garden);
            return;
        }
        String description = descriptionET.getText().toString();
        if(description.isEmpty()){
            descriptionET.setError(getResources().getString(R.string.enter_property_description));
            showSnackbar(R.string.enter_property_description);
            descriptionET.requestFocus();
            return;
        }else{
            String[] words = description.split(" ");
            if(words.length>200 || words.length<20){
                descriptionET.setError(getResources().getString(R.string.property_description_length_error));
                showSnackbar(R.string.property_description_length_error);
                descriptionET.requestFocus();
                return;
            }
            descriptionET.setError(null);
        }

        try{
            JSONObject requestJsonObject = new JSONObject();
            requestJsonObject.put("cityId",selectedCityId);
            requestJsonObject.put("agencyId",sharedPrefManager.getUserId());
            requestJsonObject.put("surfaceArea",surfaceAreaString);
            requestJsonObject.put("bedroomsCount",bedroomsCountString);
            requestJsonObject.put("rentalPrice",rentalPriceString);
            requestJsonObject.put("status",statusSpinner.getSelectedItem().toString());
            requestJsonObject.put("hasBalcony",hasBalconySpinner.getSelectedItem().toString());
            requestJsonObject.put("hasGarden",hasGardenSpinner.getSelectedItem().toString());
            requestJsonObject.put("constructionYear",constructionYearString);
            requestJsonObject.put("availabilityDate",availabilityDateString);
            requestJsonObject.put("description",description);
            PostPropertyAsyncTask postPropertyAsyncTask = new PostPropertyAsyncTask();
            postPropertyAsyncTask.execute(PROPERTIES_URL,requestJsonObject.toString());
        }catch (Exception exc){
            exc.printStackTrace();
        }
    }

    public void setProgress(boolean progress) {
        ProgressBar progressBar = getView().findViewById(R.id.agencyPostProperty_progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public class PostPropertyAsyncTask extends AsyncTask<String, String,
            String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpManager.sendRequest(params[0],"POST",params[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setProgress(false);

            if (s == null) {
                Toast.makeText(getContext(), "Error connecting to server", Toast.LENGTH_SHORT).show();
                return;
            }

            PropertyPostResponseModel agencyProfileResponseModel =
                    PropertyPostResponseJsonParser.getObjectFromJson(s);
            if (agencyProfileResponseModel == null) {
                Toast.makeText(getContext(), "Error in server response", Toast.LENGTH_SHORT).show();
                return;
            }
            if (agencyProfileResponseModel.isSuccess()) {
                Toast.makeText(getContext(), getResources().getString(R.string.added_successfully), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigateUp();
                Bundle args = new Bundle();
                args.putInt("propertyId",agencyProfileResponseModel.getPropertyId());
                Navigation.findNavController(getView()).navigate(R.id.nav_property_info,args);
                Navigation.findNavController(getView()).navigate(R.id.nav_property_add_image,args);
            }else{
                Toast.makeText(getContext(), agencyProfileResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}