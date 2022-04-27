package edu.berziet.houserental.agency.ui.property;

import static edu.berziet.houserental.ApiUrl.AGENCY_PROPERTIES_LIST_URL;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.berziet.houserental.R;
import edu.berziet.houserental.dbhelpers.RentalSqliteOpenHelper;
import edu.berziet.houserental.models.CityModel;
import edu.berziet.houserental.models.PropertyModel;
import edu.berziet.houserental.parsers.PropertiesListJsonParser;
import edu.berziet.houserental.tools.HttpManager;
import edu.berziet.houserental.tools.SharedPrefManager;

public class RentingAgencyPropertiesListFragment extends Fragment {
    private SharedPrefManager sharedPrefManager;
    private RentalSqliteOpenHelper sqliteHelper;
    private RecyclerView recyclerView;
    public RentingAgencyPropertiesListFragment() {
    }
    public static RentingAgencyPropertiesListFragment newInstance() {
        RentingAgencyPropertiesListFragment fragment = new RentingAgencyPropertiesListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefManager = SharedPrefManager.getInstance(getContext());
        sqliteHelper = new RentalSqliteOpenHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_renting_agency_properties_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.agencyPropertiesList_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        GetPropertiesListAsyncTask asyncTask = new GetPropertiesListAsyncTask();
        String url = String.format(AGENCY_PROPERTIES_LIST_URL,sharedPrefManager.getUserId());
        asyncTask.execute(url);
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
                    Navigation.findNavController(requireView())
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

    public void setProgress(boolean progress) {
        ProgressBar progressBar = getView().findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public class GetPropertiesListAsyncTask extends AsyncTask<String,String,String> {
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

            ArrayList<PropertyModel> propertiesList =
                    PropertiesListJsonParser.getListFromJsonArray(s);
            if (propertiesList == null) {
                Toast.makeText(getContext(), "Error in server response", Toast.LENGTH_SHORT).show();
                return;
            }
            PropertyRecyclerAdapter adapter = new PropertyRecyclerAdapter(propertiesList);
            recyclerView.setAdapter(adapter);
        }
    }

}