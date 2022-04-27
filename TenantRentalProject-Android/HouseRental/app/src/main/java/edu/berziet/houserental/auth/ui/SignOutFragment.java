package edu.berziet.houserental.auth.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.berziet.houserental.R;
import edu.berziet.houserental.auth.AuthActivity;
import edu.berziet.houserental.tools.SharedPrefManager;

public class SignOutFragment extends Fragment {

    public SignOutFragment() {
        // Required empty public constructor
    }

    public static SignOutFragment newInstance() {
        SignOutFragment fragment = new SignOutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.goBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigateUp();
            }
        });
        view.findViewById(R.id.signOutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager prefs = SharedPrefManager.getInstance(getContext());
                prefs.setCountryName("");
                prefs.setCountryId(0);
                prefs.setCityName("");
                prefs.setCityId(0);
                prefs.setRole("");
                prefs.setName("");
                Intent i = new Intent(getContext(), AuthActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
    }
}