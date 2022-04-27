package edu.berziet.houserental.auth.ui.signupmain;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.berziet.houserental.R;
import edu.berziet.houserental.auth.ui.agencysignup.RentingAgencySignUpFragment;
import edu.berziet.houserental.auth.ui.tenantsignup.TenantSignUpFragment;

public class SignUpMainFragment extends Fragment {


    public static SignUpMainFragment newInstance() {
        return new SignUpMainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_up_main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.tenantButton).setOnClickListener(new View.OnClickListener() {
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
                fragmentTransaction.replace(R.id.container, TenantSignUpFragment.newInstance());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        view.findViewById(R.id.agencyButton).setOnClickListener(new View.OnClickListener() {
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
                fragmentTransaction.replace(R.id.container, RentingAgencySignUpFragment.newInstance());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
}