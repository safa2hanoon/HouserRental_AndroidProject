package edu.berziet.houserental.agency.ui.profile;

import static edu.berziet.houserental.ApiUrl.AGENCY_PROFILE_URL;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.Locale;
import edu.berziet.houserental.R;
import edu.berziet.houserental.auth.ui.signupmain.SignUpMainFragment;
import edu.berziet.houserental.models.AgencyProfileResponseModel;
import edu.berziet.houserental.models.RentingAgencyModel;
import edu.berziet.houserental.parsers.RentingAgencyResponseJsonParser;
import edu.berziet.houserental.tools.HttpManager;
import edu.berziet.houserental.tools.SharedPrefManager;

public class RentingAgencyProfileFragment extends Fragment {

    private SharedPrefManager sharedPrefManager;
    private EditText emailET,nameET,countryET,cityET,phoneNumberET;
    public RentingAgencyProfileFragment() {
        // Required empty public constructor
    }

    public static RentingAgencyProfileFragment newInstance() {
        RentingAgencyProfileFragment fragment = new RentingAgencyProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefManager = SharedPrefManager.getInstance(getContext());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_renting_agency_profile, container, false);
        emailET = root.findViewById(R.id.agencyProfile_emailAddressET);
        nameET = root.findViewById(R.id.agencyProfile_agencyNameET);
        countryET = root.findViewById(R.id.agencyProfile_countryNameET);
        cityET = root.findViewById(R.id.agencyProfile_cityNameET);
        phoneNumberET = root.findViewById(R.id.agencyProfile_phoneNumberET);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String url = String.format(Locale.ENGLISH, AGENCY_PROFILE_URL, sharedPrefManager.getUserId());
        AgencyProfileAsyncTask profileAsyncTask = new AgencyProfileAsyncTask();
        profileAsyncTask.execute(url);
        view.findViewById(R.id.agencyProfile_editButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.nav_edit_agency_profile);
            }
        });
    }

    public void setProgress(boolean progress) {
        ProgressBar progressBar = getView().findViewById(R.id.agencyProfile_progressBar);
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
        countryET.setText(agencyModel.getCountryName());
        cityET.setText(agencyModel.getCityName());
        phoneNumberET.setText(agencyModel.getPhoneNumber());
    }

}