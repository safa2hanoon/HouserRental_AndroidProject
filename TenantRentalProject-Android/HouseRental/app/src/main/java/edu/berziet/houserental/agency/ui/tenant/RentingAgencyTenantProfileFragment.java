package edu.berziet.houserental.agency.ui.tenant;

import static edu.berziet.houserental.ApiUrl.TENANT_PROFILE_URL;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Locale;

import edu.berziet.houserental.R;
import edu.berziet.houserental.models.TenantModel;
import edu.berziet.houserental.models.TenantProfileResponseModel;
import edu.berziet.houserental.parsers.TenantProfileJsonParser;
import edu.berziet.houserental.tenants.ui.profile.TenantProfileFragment;
import edu.berziet.houserental.tools.HttpManager;
import edu.berziet.houserental.tools.SharedPrefManager;

public class RentingAgencyTenantProfileFragment extends Fragment {

    private static final String ARG_TENANT_ID = "tenantId";
    private EditText emailET, firstNameET, lastNameET, nationalityET, countryET, cityET, genderET,
            monthlySalaryET, occupationET, familySizeET, phoneNumberET;
    private String tenantId;

    public RentingAgencyTenantProfileFragment() {
        // Required empty public constructor
    }

    public static RentingAgencyTenantProfileFragment newInstance(String tenantId) {
        RentingAgencyTenantProfileFragment fragment = new RentingAgencyTenantProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TENANT_ID, tenantId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tenantId = getArguments().getString(ARG_TENANT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_renting_agency_tenant_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailET = view.findViewById(R.id.tenantProfile_emailAddressET);
        firstNameET = view.findViewById(R.id.tenantProfile_firstNameET);
        lastNameET = view.findViewById(R.id.tenantProfile_lastNameET);
        nationalityET = view.findViewById(R.id.tenantProfile_nationalityET);
        countryET = view.findViewById(R.id.tenantProfile_countryET);
        cityET = view.findViewById(R.id.tenantProfile_cityET);
        genderET = view.findViewById(R.id.tenantProfile_genderET);
        monthlySalaryET = view.findViewById(R.id.tenantProfile_monthlySalaryET);
        occupationET = view.findViewById(R.id.tenantProfile_occupationET);
        familySizeET = view.findViewById(R.id.tenantProfile_familySizeET);
        phoneNumberET = view.findViewById(R.id.tenantProfile_phoneNumberET);

        String url = String.format(Locale.ENGLISH, TENANT_PROFILE_URL, tenantId);
        TenantProfileAsyncTask profileAsyncTask = new TenantProfileAsyncTask();
        profileAsyncTask.execute(url);

        view.findViewById(R.id.tenantProfile_viewRentalHistoryButton)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle args = new Bundle();
                        args.putString("tenantId",tenantId);
                        Navigation.findNavController(requireView())
                                .navigate(R.id.nav_tenant_rental_history,args);
                    }
                });
    }

    public void setProgress(boolean progress) {
        ProgressBar progressBar = requireView().findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
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
                Navigation.findNavController(requireView())
                    .navigateUp();
            }
        }
    }

    private void setProfileInfo(TenantModel tenantModel) {
        emailET.setText(tenantModel.getEmailAddress());
        firstNameET.setText(tenantModel.getFirstName());
        lastNameET.setText(tenantModel.getLastName());
        nationalityET.setText(tenantModel.getTenantNationality());
        countryET.setText(tenantModel.getCountryName());
        cityET.setText(tenantModel.getCityName());
        genderET.setText(tenantModel.getGender());
        monthlySalaryET.setText(String.valueOf(tenantModel.getMonthlySalary()));
        occupationET.setText(tenantModel.getOccupation());
        familySizeET.setText(String.valueOf(tenantModel.getFamilySize()));
        phoneNumberET.setText(tenantModel.getPhoneNumber());
    }
}