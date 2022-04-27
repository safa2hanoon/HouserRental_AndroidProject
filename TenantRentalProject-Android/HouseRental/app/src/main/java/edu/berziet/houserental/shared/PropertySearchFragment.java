package edu.berziet.houserental.shared;

import static edu.berziet.houserental.ApiUrl.IMAGE_URL;
import static edu.berziet.houserental.ApiUrl.PROPERTIES_SEARCH_URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import edu.berziet.houserental.R;
import edu.berziet.houserental.dbhelpers.RentalSqliteOpenHelper;
import edu.berziet.houserental.models.CityModel;
import edu.berziet.houserental.models.CountryModel;
import edu.berziet.houserental.models.PropertyImage;
import edu.berziet.houserental.models.PropertyModel;
import edu.berziet.houserental.parsers.PropertiesListJsonParser;
import edu.berziet.houserental.tools.HttpManager;
import edu.berziet.houserental.tools.SharedPrefManager;

public class PropertySearchFragment extends Fragment {

    private Spinner countrySpinner,citySpinner, statusSpinner;
    private EditText minSurfaceET, maxSurfaceET, minBedroomsET, maxBedroomsET, minPriceET, maxPriceET;
    private CheckBox hasBalconyCheckBox, hasGardenCheckBox;
    private RentalSqliteOpenHelper sqliteOpenHelper;
    private SharedPrefManager sharedPrefManager;
    private int selectedCountryId;
    private int selectedCityId;
    private RecyclerView recyclerView;
    private List<CityModel> citiesList;
    private String role;
    public PropertySearchFragment() {
    }

    public static PropertySearchFragment newInstance() {
        return new PropertySearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqliteOpenHelper = new RentalSqliteOpenHelper(getContext());
        sharedPrefManager = SharedPrefManager.getInstance(getContext());
        requireActivity().setTitle(R.string.property_search);
    }


    public void setProgress(boolean progress) {
        ProgressBar progressBar = requireView().findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_property_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.propertySearch_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        minSurfaceET = view.findViewById(R.id.propertySearch__minSurfaceArea);
        maxSurfaceET = view.findViewById(R.id.propertySearch_maxSurfaceArea);
        minBedroomsET = view.findViewById(R.id.propertySearch_minBedroomsCount);
        maxBedroomsET = view.findViewById(R.id.propertySearch_maxBedroomsCount);
        minPriceET = view.findViewById(R.id.propertySearch_minRentalPrice);
        maxPriceET = view.findViewById(R.id.propertySearch_maxRentalPrice);

        hasBalconyCheckBox = view.findViewById(R.id.propertySearch_hasBalconyCheckBox);
        hasGardenCheckBox = view.findViewById(R.id.propertySearch_hasGardenCheckBox);

        countrySpinner = view.findViewById(R.id.propertySearch_countrySpinner);
        List<CountryModel> countriesList = sqliteOpenHelper.getCountriesList();
        List<String> countriesNames = new ArrayList<>();
        countriesNames.add(getResources().getString(R.string.select_country));
        for (CountryModel country :
                countriesList) {
            countriesNames.add(country.getName());
        }
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    emptyCitiesSpinner();
                    selectedCountryId=0;
                } else {
                    CountryModel country = countriesList.get(i - 1);
                    selectedCountryId=country.getCountryId();
                    citiesList = sqliteOpenHelper.getCountryCities(country.getCountryId());
                    fillCitiesSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, countriesNames);
        countrySpinner.setAdapter(adapter);

        citySpinner = view.findViewById(R.id.propertySearch_citySpinner);
        role = sharedPrefManager.getRole();
        if(role.equalsIgnoreCase("guest")){
            countrySpinner.setEnabled(true);
        }else if (role.equalsIgnoreCase("tenant")){
            int countryId = sharedPrefManager.getCountryId();
            for(int i = 0 ; i < countriesList.size();i++){
                CountryModel country = countriesList.get(i);
                if(country.getCountryId()==countryId){
                    countrySpinner.setSelection(i+1);
                }
            }
            countrySpinner.setEnabled(false);
            citiesList = sqliteOpenHelper.getCountryCities(countryId);
            emptyCitiesSpinner();
            fillCitiesSpinner();
        }

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
        statusSpinner = view.findViewById(R.id.propertySearch_statusSpinner);

        view.findViewById(R.id.propertySearch_searchButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(countrySpinner.getSelectedItemPosition()==0){
                            Snackbar.make(requireView(),
                                    getResources().getString(R.string.select_country),
                                    BaseTransientBottomBar.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        try {
                            JSONObject requestJsonObject = new JSONObject();
                            requestJsonObject.put("countryId", selectedCountryId);
                            requestJsonObject.put("cityId", selectedCityId);
                            requestJsonObject.put("minSurfaceArea", minSurfaceET.getText().toString());
                            requestJsonObject.put("maxSurfaceArea", maxSurfaceET.getText().toString());
                            requestJsonObject.put("minBedroomsCount", minBedroomsET.getText().toString());
                            requestJsonObject.put("maxBedroomsCount", maxBedroomsET.getText().toString());
                            requestJsonObject.put("minRentalPrice", minPriceET.getText().toString());
                            requestJsonObject.put("maxRentalPrice", maxPriceET.getText().toString());
                            if (statusSpinner.getSelectedItemPosition() == 0) {
                                requestJsonObject.put("status", "");
                            } else {
                                requestJsonObject.put("status", statusSpinner.getSelectedItem().toString());
                            }
                            requestJsonObject.put("hasBalcony", hasBalconyCheckBox.isChecked());
                            requestJsonObject.put("hasGarden", hasGardenCheckBox.isChecked());
                            SearchAsyncTask searchAsyncTask = new SearchAsyncTask();
                            searchAsyncTask.execute(PROPERTIES_SEARCH_URL, requestJsonObject.toString());
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                });
        requireView().setFocusableInTouchMode(true);
        requireView().requestFocus();
        requireView().setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    if(recyclerView.getVisibility()==View.VISIBLE){
                        Animation hideAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.hide_from_top_to_down);
                        hideAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                recyclerView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        recyclerView.startAnimation(hideAnimation);
                        return true;
                    }
                    return false;
                }
                return false;
            }
        } );
    }


    public void emptyCitiesSpinner() {
        citiesList = new ArrayList<>();
        List<String> citiesNames = new ArrayList<>();
        citiesNames.add(getResources().getString(R.string.select_country));
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
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
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, citiesNames);
        citySpinner.setAdapter(adapter2);
    }

    public class SearchAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpManager.sendRequest(params[0], "GET", params[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setProgress(false);
            System.out.println("Properties Search : " + s);
            if (s == null) {
                Toast.makeText(getContext(), "Error connecting to server", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<PropertyModel> propertiesList =
                    PropertiesListJsonParser.getListFromJsonArray(s);
            if (propertiesList == null) {
                Toast.makeText(getContext(), "Error in server response", Toast.LENGTH_SHORT).show();
                return;
            }
            PropertyRecyclerAdapter adapter = new PropertyRecyclerAdapter(propertiesList);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.enter_from_down_to_up));
        }
    }

    private class PropertyViewHolder extends RecyclerView.ViewHolder {
        private final EditText statusET;
        private final EditText cityET;
        private final EditText surfaceAreaET;
        private final EditText rentalPriceET;
        private final Button viewButton;
        private final ImageView imageView;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            statusET = itemView.findViewById(R.id.propertySearchItem_statusET);
            cityET = itemView.findViewById(R.id.propertySearchItem_cityET);
            surfaceAreaET = itemView.findViewById(R.id.propertySearchItem_surfaceAreaET);
            rentalPriceET = itemView.findViewById(R.id.propertySearchItem_rentalPriceET);
            viewButton = itemView.findViewById(R.id.propertySearchItem_viewButton);
            imageView = itemView.findViewById(R.id.propertySearchItem_imageView);
        }

        public void setData(PropertyModel propertyModel) {
            statusET.setText(propertyModel.getStatus());
            int cityId = propertyModel.getCityId();
            CityModel city = sqliteOpenHelper.getCityById(cityId);
            cityET.setText(city.getName());
            surfaceAreaET.setText(String.valueOf(propertyModel.getSurfaceArea()));
            rentalPriceET.setText(String.valueOf(propertyModel.getRentalPrice()));

            viewButton.setOnClickListener(
                new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              if(role.equalsIgnoreCase("guest")){
                                  FragmentTransaction fragmentTransaction =
                                          requireActivity().getSupportFragmentManager().beginTransaction();
                                  fragmentTransaction.setCustomAnimations(
                                          R.anim.slide_in,  // enter
                                          R.anim.fade_out,  // exit
                                          R.anim.fade_in,   // popEnter
                                          R.anim.slide_out  // popExit
                                  );
                                  fragmentTransaction.replace(R.id.container,
                                          PropertyInfoFragment.newInstance(propertyModel.getId()));
                                  fragmentTransaction.addToBackStack(null);
                                  fragmentTransaction.commit();

                              }else{
                                  Bundle args = new Bundle();
                                  args.putInt("propertyId", propertyModel.getId());
                                  Navigation.findNavController(requireView())
                                          .navigate(R.id.nav_property_info, args);
                              }
                          }
                      }
                );
            if(propertyModel.getImagesList().size()>0){
                PropertyImage propertyImage = propertyModel.getImagesList().get(0);
                String imageUrl = String.format(IMAGE_URL,propertyImage.getFileName());
                Picasso.get()
                        .load(imageUrl)
                        .into(imageView);
            }
        }
    }

    private class PropertyRecyclerAdapter extends RecyclerView.Adapter<PropertyViewHolder> {
        List<PropertyModel> propertiesList;

        public PropertyRecyclerAdapter(List<PropertyModel> propertiesList) {
            this.propertiesList = propertiesList;
        }

        @NonNull
        @Override
        public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PropertyViewHolder(
                    View.inflate(parent.getContext(), R.layout.property_search_item_layout, null)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
            PropertyModel property = propertiesList.get(position);
            holder.setData(property);
        }

        @Override
        public int getItemCount() {
            return propertiesList.size();
        }
    }



}