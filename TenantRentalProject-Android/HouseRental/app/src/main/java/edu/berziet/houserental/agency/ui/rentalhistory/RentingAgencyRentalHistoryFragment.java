package edu.berziet.houserental.agency.ui.rentalhistory;

import static edu.berziet.houserental.ApiUrl.IMAGE_URL;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.berziet.houserental.ApiUrl;
import edu.berziet.houserental.R;
import edu.berziet.houserental.dbhelpers.RentalSqliteOpenHelper;
import edu.berziet.houserental.models.CityModel;
import edu.berziet.houserental.models.PropertyImage;
import edu.berziet.houserental.models.RentalHistoryModel;
import edu.berziet.houserental.parsers.RentalHistoryModelJsonParser;
import edu.berziet.houserental.tools.HttpManager;
import edu.berziet.houserental.tools.SharedPrefManager;

public class RentingAgencyRentalHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private RentalSqliteOpenHelper sqliteOpenHelper;

    public RentingAgencyRentalHistoryFragment() {
    }
    public static RentingAgencyRentalHistoryFragment newInstance() {
        return new RentingAgencyRentalHistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqliteOpenHelper = new RentalSqliteOpenHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agency_rental_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.agencyRentalHistory_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),1));
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(requireContext());
        String url = String.format(ApiUrl.AGENCY_RENTAL_HISTORY_URL,sharedPrefManager.getUserId());
        GetRentalHistoryAsyncTask getRentalHistoryAsyncTask = new GetRentalHistoryAsyncTask();
        getRentalHistoryAsyncTask.execute(url);
    }

    public void setProgress(boolean progress) {
        ProgressBar progressBar = requireView().findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private class GetRentalHistoryAsyncTask extends AsyncTask<String,String,String> {
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
            ArrayList<RentalHistoryModel> historyList =
                    RentalHistoryModelJsonParser.getArrayFromJson(s);
            RentalHistoryAdapter adapter = new RentalHistoryAdapter(historyList);
            recyclerView.setAdapter(adapter);
            loadAnimation(recyclerView);
        }
    }
    private void loadAnimation(RecyclerView recycler) {
        Context context = recycler.getContext();
        LayoutAnimationController layoutAnimationController =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down);
        recycler.setLayoutAnimation(layoutAnimationController);
        recycler.getAdapter().notifyDataSetChanged();
        recycler.scheduleLayoutAnimation();
    }

    private class RentalHistoryViewHolder extends RecyclerView.ViewHolder {
        private final EditText statusET;
        private final EditText cityET;
        private final EditText surfaceAreaET;
        private final EditText rentalPriceET;
        private final EditText tenantNameET;
        private final ImageView imageView;

        public RentalHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            statusET = itemView.findViewById(R.id.rentalHistoryItem_statusET);
            cityET = itemView.findViewById(R.id.rentalHistoryItem_cityET);
            surfaceAreaET = itemView.findViewById(R.id.rentalHistoryItem_surfaceAreaET);
            rentalPriceET = itemView.findViewById(R.id.rentalHistoryItem_rentalPriceET);
            tenantNameET = itemView.findViewById(R.id.rentalHistoryItem_tenantName);
            imageView = itemView.findViewById(R.id.rentalHistoryItem_imageView);
        }

        public void setData(RentalHistoryModel rentalHistoryModel) {
            tenantNameET.setText(rentalHistoryModel.getTenantName());
            statusET.setText(rentalHistoryModel.getProperty().getStatus());
            int cityId = rentalHistoryModel.getProperty().getCityId();
            CityModel city = sqliteOpenHelper.getCityById(cityId);
            cityET.setText(city.getName());
            surfaceAreaET.setText(String.valueOf(rentalHistoryModel.getProperty().getSurfaceArea()));
            rentalPriceET.setText(String.valueOf(rentalHistoryModel.getProperty().getRentalPrice()));
            if(rentalHistoryModel.getProperty().getImagesList().size()>0){
                PropertyImage propertyImage = rentalHistoryModel.getProperty().getImagesList().get(0);
                String imageUrl = String.format(IMAGE_URL,propertyImage.getFileName());
                Picasso.get()
                        .load(imageUrl)
                        .into(imageView);
            }
        }
    }

    private class RentalHistoryAdapter extends RecyclerView.Adapter<RentalHistoryViewHolder> {
        List<RentalHistoryModel> rentalList;

        public RentalHistoryAdapter(List<RentalHistoryModel> rentalList) {
            this.rentalList = rentalList;
        }

        @NonNull
        @Override
        public RentalHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RentalHistoryViewHolder(
                    View.inflate(parent.getContext(), R.layout.rental_history_item_layout_w_tenant_name, null)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull RentalHistoryViewHolder holder, int position) {
            RentalHistoryModel rentalHistoryModel = rentalList.get(position);
            holder.setData(rentalHistoryModel);
        }

        @Override
        public int getItemCount() {
            return rentalList.size();
        }
    }


}