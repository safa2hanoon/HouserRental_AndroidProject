package edu.berziet.houserental.shared;

import static edu.berziet.houserental.ApiUrl.IMAGE_URL;
import static edu.berziet.houserental.ApiUrl.PROPERTY_URL;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.berziet.houserental.ApiUrl;
import edu.berziet.houserental.R;
import edu.berziet.houserental.auth.AuthActivity;
import edu.berziet.houserental.dbhelpers.RentalSqliteOpenHelper;
import edu.berziet.houserental.models.CityModel;
import edu.berziet.houserental.models.CountryModel;
import edu.berziet.houserental.models.PropertyImage;
import edu.berziet.houserental.models.PropertyModel;
import edu.berziet.houserental.models.RentalRequestModel;
import edu.berziet.houserental.models.SimpleSuccessResponseModel;
import edu.berziet.houserental.parsers.PropertyJsonParser;
import edu.berziet.houserental.parsers.RentalRequestModelJsonParser;
import edu.berziet.houserental.parsers.SimpleSuccessResponseJsonParser;
import edu.berziet.houserental.tools.HttpManager;
import edu.berziet.houserental.tools.SharedPrefManager;

public class PropertyInfoFragment extends Fragment {

    private static final String ARG_PROPERTY_ID = "propertyId";
    private EditText surfaceAreaET, bedroomsCountET, rentalPriceET, constructionYearET,
            availabilityDateET, countryET, descriptionET;
    private EditText cityET, hasBalconyET, hasGardenET, statusET,isRentedET;
    private int propertyId;
    private RentalSqliteOpenHelper sqliteOpenHelper;
    private SharedPrefManager sharedPrefManager;
    private AppCompatButton applyButton;
    private AdapterViewFlipper viewFlipper;

    public PropertyInfoFragment() {
        // Required empty public constructor
    }

    public static PropertyInfoFragment newInstance(int propertyId) {
        PropertyInfoFragment fragment = new PropertyInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROPERTY_ID, propertyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            propertyId = getArguments().getInt(ARG_PROPERTY_ID);
        }
        sqliteOpenHelper = new RentalSqliteOpenHelper(getContext());
        sharedPrefManager = SharedPrefManager.getInstance(getContext());
        requireActivity().setTitle(R.string.property_info);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_property_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewFlipper = view.findViewById(R.id.propertyInfo_imageViewFlipper);
        surfaceAreaET=view.findViewById(R.id.propertyInfo_surfaceArea);
        bedroomsCountET=view.findViewById(R.id.propertyInfo_bedroomsCount);
        rentalPriceET=view.findViewById(R.id.propertyInfo_rentalPrice);
        constructionYearET=view.findViewById(R.id.propertyInfo_constructionYear);
        availabilityDateET=view.findViewById(R.id.propertyInfo_availabilityDate);
        countryET=view.findViewById(R.id.propertyInfo_country);
        descriptionET=view.findViewById(R.id.propertyInfo_description);
        cityET=view.findViewById(R.id.propertyInfo_city);
        hasBalconyET=view.findViewById(R.id.propertyInfo_hasBalcony);
        hasGardenET=view.findViewById(R.id.propertyInfo_hasGarden);
        statusET=view.findViewById(R.id.propertyInfo_propertyStatus);
        isRentedET=view.findViewById(R.id.propertyInfo_isRented);

        AppCompatButton editButton = view.findViewById(R.id.propertyInfo_editButton);
        AppCompatButton editImagesButton = view.findViewById(R.id.propertyInfo_editImagesButton);
        AppCompatButton deleteButton = view.findViewById(R.id.propertyInfo_deleteButton);
        applyButton = view.findViewById(R.id.propertyInfo_applyButton);
        String role = sharedPrefManager.getRole();
        if(role.equalsIgnoreCase("RentingAgency")){
            editButton.setVisibility(View.VISIBLE);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putInt("propertyId",propertyId);
                    Navigation.findNavController(requireView())
                            .navigate(R.id.nav_edit_property,args);
                }
            });
            editImagesButton.setVisibility(View.VISIBLE);
            editImagesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putInt("propertyId",propertyId);
                    Navigation.findNavController(requireView())
                            .navigate(R.id.nav_property_images,args);
                }
            });
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(getContext());
                    myAlert.setMessage(getResources().getString(R.string.confirm_delete_property))
                            .setTitle(getResources().getString(R.string.confirm_delete))
                            .setNegativeButton(getResources().getString(R.string.no), null)
                            .setIcon(ResourcesCompat.getDrawable(getResources(),
                                    android.R.drawable.ic_dialog_alert,null))
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DeletePropertyAsyncTask asyncTask = new DeletePropertyAsyncTask();
                                    String url = String.format(PROPERTY_URL,propertyId);
                                    asyncTask.execute(url);
                                }
                            });
                    AlertDialog alert = myAlert.create();
                    alert.show();
                }
            });
        }else if(role.equalsIgnoreCase("Tenant")){
            CheckHasRequestAsyncTask checkHasRequestAsyncTask = new CheckHasRequestAsyncTask();
            String url = String.format(
                    ApiUrl.TENANT_RENTAL_REQUESTS_FOR_PROPERTY_CHECK_URL,
                    sharedPrefManager.getUserId(),
                    propertyId
            );
            checkHasRequestAsyncTask.execute(url);
        }else{
            // guest
            applyButton.setVisibility(View.VISIBLE);
            applyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), AuthActivity.class);
                    startActivity(i);
                    requireActivity().finish();
                }
            });
        }
        GetPropertyAsyncTask asyncTask = new GetPropertyAsyncTask();
        String url = String.format(PROPERTY_URL,propertyId);
        asyncTask.execute(url);
    }

    public void setProgress(boolean progress) {
        ProgressBar progressBar = requireView().findViewById(R.id.propertyInfo_progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public class GetPropertyAsyncTask extends AsyncTask<String,String,String> {
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

            PropertyModel propertyModel =
                    PropertyJsonParser.getObjectFromJson(s);
            if (propertyModel == null) {
                Toast.makeText(getContext(), "Error in server response", Toast.LENGTH_SHORT).show();
                return;
            }
            setPropertyInfo(propertyModel);
        }
    }

    public class DeletePropertyAsyncTask extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgress(true);
        }
        @Override
        protected String doInBackground(String... params) {
            return HttpManager.sendRequest(params[0],"DELETE","");
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setProgress(false);

            if (s == null) {
                Toast.makeText(getContext(), "Error connecting to server", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleSuccessResponseModel deleteResponseModel =
                    SimpleSuccessResponseJsonParser.getObjectFromJson(s);
            if (deleteResponseModel == null) {
                Toast.makeText(getContext(), "Error in server response", Toast.LENGTH_SHORT).show();
            }else if(deleteResponseModel.isSuccess()){
                Toast.makeText(getContext(),
                        getResources().getString(R.string.property_deleted_successfully),
                        Toast.LENGTH_SHORT)
                        .show();
                Navigation.findNavController(requireView()).navigateUp();
            }else{
                Toast.makeText(getContext(), deleteResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void setPropertyInfo(PropertyModel propertyModel) {
        surfaceAreaET.setText(String.valueOf(propertyModel.getSurfaceArea()));
        bedroomsCountET.setText(String.valueOf(propertyModel.getBedroomsCount()));
        rentalPriceET.setText(String.valueOf(propertyModel.getRentalPrice()));
        constructionYearET.setText(String.valueOf(propertyModel.getConstructionYear()));
        availabilityDateET.setText(propertyModel.getAvailabilityDate());
        descriptionET.setText(propertyModel.getDescription());
        String yes = getResources().getString(R.string.yes);
        String no = getResources().getString(R.string.no);
        hasBalconyET.setText(propertyModel.isHasBalcony()?yes:no);
        hasGardenET.setText(propertyModel.isHasGarden()?yes:no);
        statusET.setText(propertyModel.getStatus());
        CityModel city = sqliteOpenHelper.getCityById(propertyModel.getCityId());
        CountryModel country = sqliteOpenHelper.getCountryById(city.getCountryId());
        cityET.setText(city.getName());
        countryET.setText(country.getName());
        isRentedET.setText(propertyModel.isRented()?yes:no);
        int imagesCount = propertyModel.getImagesList().size();
        if(imagesCount>0){
            CustomImageAdapter imageAdapter = new CustomImageAdapter(propertyModel.getImagesList());
            viewFlipper.setAdapter(imageAdapter);
            viewFlipper.setAutoStart(true);
            viewFlipper.setFlipInterval(3000);
            viewFlipper.startFlipping();
        }else{
            viewFlipper.setVisibility(View.GONE);
        }
    }

    public class CustomImageAdapter extends BaseAdapter {
        private final List<PropertyImage> images;
        LayoutInflater inflater;

        public CustomImageAdapter(List<PropertyImage> images) {
            this.images = images;
            inflater = (LayoutInflater.from(getContext()));
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int i) {
            return images.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.property_image_layout, null);
            ImageView iv = view.findViewById(R.id.propertyImageView);
            PropertyImage propertyImage = (PropertyImage) getItem(i);
            String url = String.format(IMAGE_URL,propertyImage.getFileName());
            Picasso.get().load(url).into(iv);
            return view;
        }
    }

    public class SendRequestAsyncTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgress(true);
        }
        @Override
        protected String doInBackground(String... params) {
            return  HttpManager.sendRequest(params[0],"POST",params[1]);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setProgress(false);

            if (s == null) {
                Toast.makeText(getContext(), "Error connecting to server", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleSuccessResponseModel responseModel =
                    SimpleSuccessResponseJsonParser.getObjectFromJson(s);
            if (responseModel == null) {
                Toast.makeText(getContext(), "Error in server response", Toast.LENGTH_SHORT).show();
                return;
            }
            if(responseModel.isSuccess()){
                applyButton.setEnabled(false);
                applyButton.setText(
                        getResources().getString(R.string.awaiting_approval)
                );
            }

            Toast.makeText(requireContext(), responseModel.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public class CheckHasRequestAsyncTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgress(true);
        }
        @Override
        protected String doInBackground(String... params) {
            return  HttpManager.getData(params[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setProgress(false);

            if (s == null) {
                Toast.makeText(getContext(), "Error connecting to server", Toast.LENGTH_SHORT).show();
                return;
            }
            if(s.isEmpty()){
                // means there is no current requests
                applyButton.setVisibility(View.VISIBLE);
                applyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SendRequestAsyncTask sendRequestAsyncTask = new SendRequestAsyncTask();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("tenantId",sharedPrefManager.getUserId());
                            jsonObject.put("propertyId",propertyId);
                            sendRequestAsyncTask.execute(ApiUrl.RENTAL_REQUESTS_URL,jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                return;
            }

            RentalRequestModel requestModel =
                    RentalRequestModelJsonParser.getObjectFromJson(s);
            if (requestModel == null) {
                Toast.makeText(getContext(), "Error in server response", Toast.LENGTH_SHORT).show();
                return;
            }
            applyButton.setVisibility(View.VISIBLE);
            applyButton.setEnabled(false);
            applyButton.setText(requestModel.getResult());

        }
    }
}