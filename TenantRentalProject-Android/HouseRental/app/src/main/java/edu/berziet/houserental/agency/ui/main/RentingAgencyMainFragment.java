package edu.berziet.houserental.agency.ui.main;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.berziet.houserental.R;

public class RentingAgencyMainFragment extends Fragment {

    public static RentingAgencyMainFragment newInstance() {
        return new RentingAgencyMainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_renting_agency_main, container, false);
    }

}