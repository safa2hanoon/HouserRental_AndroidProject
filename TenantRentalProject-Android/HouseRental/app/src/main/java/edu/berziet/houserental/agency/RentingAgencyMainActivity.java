package edu.berziet.houserental.agency;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import edu.berziet.houserental.R;
import edu.berziet.houserental.databinding.RentingAgencyMainActivityBinding;
import edu.berziet.houserental.tools.SharedPrefManager;


public class RentingAgencyMainActivity extends AppCompatActivity {
    private RentingAgencyMainActivityBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_down_to_up, R.anim.hide_from_top_to_down);
        super.onCreate(savedInstanceState);

        binding = RentingAgencyMainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarAgencyMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_profile,R.id.nav_property_add,
                R.id.nav_rental_history, R.id.nav_properties_list,
                R.id.nav_rental_requests,R.id.nav_log_out)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_agency_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setHeaderNameAndEmail();
    }

    public void setHeaderNameAndEmail() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
        String email = sharedPrefManager.getEmail();
        TextView emailTextView = headerView.findViewById(R.id.agencyNavHeader_agencyEmailTV);
        emailTextView.setText(email);
        String name = sharedPrefManager.getName();
        TextView nameTextView = headerView.findViewById(R.id.agencyNavHeader_agencyNameTV);
        nameTextView.setText(name);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_agency_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}