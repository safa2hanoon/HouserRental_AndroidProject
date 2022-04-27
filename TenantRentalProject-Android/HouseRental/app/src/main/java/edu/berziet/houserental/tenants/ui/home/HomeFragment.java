package edu.berziet.houserental.tenants.ui.home;

import static edu.berziet.houserental.ApiUrl.PROPERTIES_URL;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import edu.berziet.houserental.R;
import edu.berziet.houserental.dbhelpers.RentalSqliteOpenHelper;
import edu.berziet.houserental.models.CityModel;
import edu.berziet.houserental.models.PropertyModel;
import edu.berziet.houserental.parsers.PropertiesListJsonParser;
import edu.berziet.houserental.tools.HttpManager;

public class HomeFragment extends Fragment {

    private RentalSqliteOpenHelper sqliteHelper;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqliteHelper = new RentalSqliteOpenHelper(getContext());
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tenant_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       getTopFiveProperties();
    }

    private void getTopFiveProperties(){
        RentalSqliteOpenHelper sqliteHelper = new RentalSqliteOpenHelper(getContext());
        List<PropertyModel> propertiesList = sqliteHelper.getTopFivePropertiesList();
        RecyclerView recyclerView = getView().findViewById(R.id.tenantHome_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(new PropertyRecyclerAdapter(propertiesList));
        loadAnimation(recyclerView);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.tenant_home_option_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.refresh_button){
            GetPropertiesAsyncTask asyncTask = new GetPropertiesAsyncTask();
            asyncTask.execute(PROPERTIES_URL);
        }
        return super.onOptionsItemSelected(item);
    }
    private void loadAnimation(RecyclerView recycler) {
        Context context = recycler.getContext();
        LayoutAnimationController layoutAnimationController =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down);
        recycler.setLayoutAnimation(layoutAnimationController);
        recycler.getAdapter().notifyDataSetChanged();
        recycler.scheduleLayoutAnimation();
    }
    private class PropertyViewHolder extends RecyclerView.ViewHolder{
        private EditText statusET,cityET,surfaceAreaET,rentalPriceET;
        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            statusET = itemView.findViewById(R.id.guestPropertyLayout_statusET);
            cityET = itemView.findViewById(R.id.guestPropertyLayout_cityET);
            surfaceAreaET = itemView.findViewById(R.id.guestPropertyLayout_surfaceAreaET);
            rentalPriceET = itemView.findViewById(R.id.guestPropertyLayout_rentalPriceET);
        }

        public void setData(PropertyModel propertyModel){
            statusET.setText(propertyModel.getStatus());
            int cityId = propertyModel.getCityId();
            CityModel city = sqliteHelper.getCityById(cityId);
            cityET.setText(city.getName());
            surfaceAreaET.setText(String.valueOf(propertyModel.getSurfaceArea()));
            rentalPriceET.setText(String.valueOf(propertyModel.getRentalPrice()));

            View.OnClickListener ocl = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putInt("propertyId",propertyModel.getId());
                    Navigation.findNavController(getView())
                            .navigate(R.id.nav_property_info,args);
                }
            };
            statusET.setOnClickListener(ocl);
            cityET.setOnClickListener(ocl);
            surfaceAreaET.setOnClickListener(ocl);
            rentalPriceET.setOnClickListener(ocl);
        }
    }

    private class PropertyRecyclerAdapter extends RecyclerView.Adapter<PropertyViewHolder>{
        List<PropertyModel> propertiesList;

        public PropertyRecyclerAdapter(List<PropertyModel> propertiesList) {
            this.propertiesList = propertiesList;
        }

        @NonNull
        @Override
        public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PropertyViewHolder(
                    View.inflate(parent.getContext(),R.layout.property_guest_layout,null)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
            PropertyModel property = propertiesList.get(position);
            holder.setData(property);
        }

        @Override
        public int getItemCount() {
            return propertiesList.size();
        }
    }

    public class GetPropertiesAsyncTask extends AsyncTask<String, String,
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

            if(s==null){
                Toast.makeText(getContext(), "Error connecting to server", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<PropertyModel> propertiesList =
                    PropertiesListJsonParser.getListFromJsonArray(s);
            if (propertiesList == null) {
                Toast.makeText(getContext(), "Error in server response", Toast.LENGTH_SHORT).show();
                return;
            }
            sqliteHelper.deleteAllProperties();
            for (PropertyModel property :
                    propertiesList) {
                sqliteHelper.insertProperty(property);
            }
            getTopFiveProperties();
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
}