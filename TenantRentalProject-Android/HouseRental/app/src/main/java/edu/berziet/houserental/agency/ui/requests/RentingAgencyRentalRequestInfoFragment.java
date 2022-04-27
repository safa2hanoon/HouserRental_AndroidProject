package edu.berziet.houserental.agency.ui.requests;

import static edu.berziet.houserental.ApiUrl.IMAGE_URL;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.berziet.houserental.ApiUrl;
import edu.berziet.houserental.R;
import edu.berziet.houserental.dbhelpers.RentalSqliteOpenHelper;
import edu.berziet.houserental.models.CityModel;
import edu.berziet.houserental.models.PropertyImage;
import edu.berziet.houserental.models.RentalRequestModel;
import edu.berziet.houserental.models.SimpleSuccessResponseModel;
import edu.berziet.houserental.parsers.RentalRequestListJsonParser;
import edu.berziet.houserental.parsers.SimpleSuccessResponseJsonParser;
import edu.berziet.houserental.tools.HttpManager;

public class RentingAgencyRentalRequestInfoFragment extends Fragment {

    private EditText statusET;
    private EditText cityET;
    private EditText surfaceAreaET;
    private EditText rentalPriceET;
    private ImageView imageView;
    private EditText resultET;
    private RentalSqliteOpenHelper sqliteOpenHelper;
    private Button viewTenantButton;
    private Button approveButton,rejectButton;

    RentalRequestModel rentalRequestModel;
    public RentingAgencyRentalRequestInfoFragment() {
        // Required empty public constructor
    }

    public static RentingAgencyRentalRequestInfoFragment newInstance(RentalRequestModel rentalRequestModel) {
        RentingAgencyRentalRequestInfoFragment fragment = new RentingAgencyRentalRequestInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("rentalRequest", rentalRequestModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqliteOpenHelper = new RentalSqliteOpenHelper(requireContext());
        if (getArguments() != null) {
            rentalRequestModel = (RentalRequestModel)
                    getArguments().getSerializable("rentalRequest");
        }else{
            Toast.makeText(requireContext(), "Null args", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView())
                    .navigateUp();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_renting_agency_rental_request_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        statusET = view.findViewById(R.id.rentalRequestInfo_statusET);
        cityET = view.findViewById(R.id.rentalRequestInfo_cityET);
        surfaceAreaET = view.findViewById(R.id.rentalRequestInfo_surfaceAreaET);
        rentalPriceET = view.findViewById(R.id.rentalRequestInfo_rentalPriceET);
        resultET = view.findViewById(R.id.rentalRequestInfo_resultET);
        imageView = view.findViewById(R.id.rentalRequestInfo_imageView);

        viewTenantButton = view.findViewById(R.id.rentalRequestInfo_viewTenantButton);
        viewTenantButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle args = new Bundle();
                        args.putString("tenantId",rentalRequestModel.getTenantId());
                        Navigation.findNavController(requireView())
                                .navigate(R.id.nav_tenant_profile,args);
                    }
                }
        );
        rejectButton = view.findViewById(R.id.rentalRequestInfo_rejectButton);
        approveButton = view.findViewById(R.id.rentalRequestInfo_approveButton);
        if(rentalRequestModel.getResultId()>0){
            approveButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.GONE);
        }else {
            rejectButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = String.format(ApiUrl.REJECT_RENTAL_REQUESTS_URL,
                                    rentalRequestModel.getRequestId());
                            RejectRequestsAsyncTask rejectRequestsAsyncTask =
                                    new RejectRequestsAsyncTask();
                            rejectRequestsAsyncTask.execute(url);
                        }
                    }
            );

            approveButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = String.format(ApiUrl.APPROVE_RENTAL_REQUESTS_URL,
                                    rentalRequestModel.getRequestId());
                            ApproveRequestsAsyncTask approveRequestsAsyncTask =
                                    new ApproveRequestsAsyncTask();
                            approveRequestsAsyncTask.execute(url);
                        }
                    }
            );
        }
            statusET.setText(rentalRequestModel.getProperty().getStatus());
            int cityId = rentalRequestModel.getProperty().getCityId();
            CityModel city = sqliteOpenHelper.getCityById(cityId);
            cityET.setText(city.getName());
            surfaceAreaET.setText(String.valueOf(rentalRequestModel.getProperty().getSurfaceArea()));
            rentalPriceET.setText(String.valueOf(rentalRequestModel.getProperty().getRentalPrice()));
            resultET.setText(rentalRequestModel.getResult());
            if (rentalRequestModel.getProperty().getImagesList().size() > 0) {
                PropertyImage propertyImage = rentalRequestModel.getProperty().getImagesList().get(0);
                String imageUrl = String.format(IMAGE_URL, propertyImage.getFileName());
                Picasso.get()
                        .load(imageUrl)
                        .into(imageView);
            }
    }

    public void setProgress(boolean progress) {
        ProgressBar progressBar = requireView().findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
    private class ApproveRequestsAsyncTask extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgress(true);
        }
        @Override
        protected String doInBackground(String... params) {
            return HttpManager.sendRequest(params[0],"POST","");
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
            if(responseModel.isSuccess()){
                rentalRequestModel.setResultId(1);
                rentalRequestModel.setResult("Approved");
                resultET.setText("Approved");
                approveButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
            }
            Toast.makeText(requireContext(), responseModel.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private class RejectRequestsAsyncTask extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgress(true);
        }
        @Override
        protected String doInBackground(String... params) {
            return HttpManager.sendRequest(params[0],"POST","");
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
            if(responseModel.isSuccess()){
                rentalRequestModel.setResultId(2);
                rentalRequestModel.setResult("Rejected");
                resultET.setText("Rejected");
                approveButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
            }
            Toast.makeText(requireContext(), responseModel.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}