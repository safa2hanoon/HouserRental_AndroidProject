package edu.berziet.houserental.guest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import edu.berziet.houserental.R;
import edu.berziet.houserental.shared.PropertySearchFragment;

public class GuestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter_from_down_to_up, R.anim.hide_from_top_to_down);
        setContentView(R.layout.guest_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            R.anim.enter_from_down_to_up,  // enter
                            R.anim.hide_from_top_to_down,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.fade_out  // popExit
                    )
                    .replace(R.id.container, PropertySearchFragment.newInstance())
                    .commitNow();
        }
    }
}