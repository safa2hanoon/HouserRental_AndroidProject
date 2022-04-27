package edu.berziet.houserental.auth.ui.signin;

import static edu.berziet.houserental.ApiUrl.SIGN_IN_URL;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.berziet.houserental.R;
import edu.berziet.houserental.agency.RentingAgencyMainActivity;
import edu.berziet.houserental.auth.ui.signupmain.SignUpMainFragment;
import edu.berziet.houserental.guest.GuestActivity;
import edu.berziet.houserental.models.SingInResponseModel;
import edu.berziet.houserental.parsers.SignInResponseJsonParser;
import edu.berziet.houserental.tenants.TenantActivity;
import edu.berziet.houserental.tools.HttpManager;
import edu.berziet.houserental.tools.SharedPrefManager;

public class SignInFragment extends Fragment {

    private EditText emailAddressET;
    private EditText passwordET;
    private SharedPrefManager prefs;

    CheckBox rememberCheckBox;
    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailAddressET = view.findViewById(R.id.emailAddressET);
        passwordET = view.findViewById(R.id.passwordET);
        prefs = SharedPrefManager.getInstance(getContext());
        rememberCheckBox = view.findViewById(R.id.rememberEmailCheckBox);
        String savedEmail = prefs.getEmail();
        if(savedEmail!=null && !savedEmail.isEmpty() ){
            emailAddressET.setText(savedEmail);
            rememberCheckBox.setChecked(true);
        }

        view.findViewById(R.id.guestButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prefs.setCountryName("");
                        prefs.setCountryId(0);
                        prefs.setCityName("");
                        prefs.setCityId(0);
                        prefs.setRole("Guest");
                        prefs.setUserId("");
                        Intent guestIntent = new Intent(getContext(), GuestActivity.class);
                        startActivity(guestIntent);
                        requireActivity().finish();
                    }
                }
        );
        view.findViewById(R.id.signinButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkLoginInput();
                    }
                }
        );
        view.findViewById(R.id.signupButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        FragmentTransaction fragmentTransaction =
                                requireActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.slide_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.fade_out  // popExit
                        );
                        fragmentTransaction.replace(R.id.container, SignUpMainFragment.newInstance());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
        );
    }

    private void checkLoginInput() {
        boolean valid = true;
        // check email address
        String email = emailAddressET.getText().toString();
        if(email.isEmpty()){
            emailAddressET.setError(getResources().getString(R.string.enter_email));
            valid = false;
        }else{
            Pattern pattern = Pattern.compile("^(.+)@([^@]+[^.])$");
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                emailAddressET.setError(getResources().getString(R.string.invalid_email_address));
                valid = false;
            }else {
                emailAddressET.setError(null);
            }
        }

        // check password
        String password = passwordET.getText().toString();
        if(password.isEmpty()){
            passwordET.setError(getResources().getString(R.string.enter_password));
            valid = false;
        }else{
            passwordET.setError(null);
        }
        if(!valid){
            Snackbar.make(requireView(),R.string.enter_login_credentials,Snackbar.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("emailAddress",email);
            jsonObject.put("password",password);
            String requestBody = jsonObject.toString();
            SignInAsyncTask signinAsyncTask = new SignInAsyncTask();
            signinAsyncTask.execute(SIGN_IN_URL,requestBody);
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

    public class SignInAsyncTask extends AsyncTask<String, String,
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
            SingInResponseModel singInResponseModel = SignInResponseJsonParser.getObjectFromJson(s);
            if(singInResponseModel==null){
                Toast.makeText(getContext(), "Error Sign in", Toast.LENGTH_SHORT).show();
                return;
            }
            if(singInResponseModel.isSuccess()){
                Toast.makeText(getContext(), getResources().getString(R.string.sign_in_success), Toast.LENGTH_SHORT).show();
                prefs.setCountryName(singInResponseModel.getCountryName());
                prefs.setCountryId(singInResponseModel.getCountryId());
                prefs.setCityName(singInResponseModel.getCityName());
                prefs.setCityId(singInResponseModel.getCityId());
                prefs.setRole(singInResponseModel.getRole());
                prefs.setName(singInResponseModel.getName());
                String email = emailAddressET.getText().toString();
                prefs.setEmail(email);
                prefs.setUserId(email);
                prefs.setRememberEmail(rememberCheckBox.isChecked());
                if(singInResponseModel.getRole().equalsIgnoreCase("RentingAgency")){
                    Intent agencyIntent = new Intent(getContext(), RentingAgencyMainActivity.class);
                    startActivity(agencyIntent);
                    requireActivity().finish();
                }else{
                    Intent tenantIntent = new Intent(getContext(), TenantActivity.class);
                    startActivity(tenantIntent);
                    requireActivity().finish();
                }
            }else{
                String message = singInResponseModel.getErrorMessage();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}