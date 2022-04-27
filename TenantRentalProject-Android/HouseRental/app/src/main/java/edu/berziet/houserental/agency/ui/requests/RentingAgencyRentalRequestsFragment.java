package edu.berziet.houserental.agency.ui.requests;

import static edu.berziet.houserental.ApiUrl.IMAGE_URL;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.berziet.houserental.ApiUrl;
import edu.berziet.houserental.R;
import edu.berziet.houserental.dbhelpers.RentalSqliteOpenHelper;
import edu.berziet.houserental.models.CityModel;
import edu.berziet.houserental.models.PropertyImage;
import edu.berziet.houserental.models.RentalRequestModel;
import edu.berziet.houserental.parsers.RentalRequestListJsonParser;
import edu.berziet.houserental.tenants.ui.requests.TenantRentalRequestsFragment;
import edu.berziet.houserental.tools.HttpManager;
import edu.berziet.houserental.tools.SharedPrefManager;


public class RentingAgencyRentalRequestsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RentalSqliteOpenHelper sqliteOpenHelper;

    public RentingAgencyRentalRequestsFragment() {
        // Required empty public constructor
    }

    public static RentingAgencyRentalRequestsFragment newInstance() {
        return new RentingAgencyRentalRequestsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqliteOpenHelper = new RentalSqliteOpenHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_renting_agency_rental_requests, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.agencyRequests_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),1));
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(requireContext());
        String url = String.format(ApiUrl.AGENCY_RENTAL_REQUESTS_URL,sharedPrefManager.getUserId());
        GetRequestsAsyncTask getRequestsAsyncTask = new GetRequestsAsyncTask();
        getRequestsAsyncTask.execute(url);
    }


    public void setProgress(boolean progress) {
        ProgressBar progressBar = requireView().findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private class GetRequestsAsyncTask extends AsyncTask<String,String,String> {
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
            ArrayList<RentalRequestModel> requestsList =
                    RentalRequestListJsonParser.getArrayFromJson(s);
            PropertyRecyclerAdapter adapter = new PropertyRecyclerAdapter(requestsList);
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

    private class RequestViewHolder extends RecyclerView.ViewHolder {
        private final EditText statusET;
        private final EditText cityET;
        private final EditText surfaceAreaET;
        private final EditText rentalPriceET;
        private final ImageView imageView;
        private final EditText resultET;
        private final Button viewButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            statusET = itemView.findViewById(R.id.rentalRequestItem_statusET);
            cityET = itemView.findViewById(R.id.rentalRequestItem_cityET);
            surfaceAreaET = itemView.findViewById(R.id.rentalRequestItem_surfaceAreaET);
            rentalPriceET = itemView.findViewById(R.id.rentalRequestItem_rentalPriceET);
            resultET = itemView.findViewById(R.id.rentalRequestItem_resultET);
            imageView = itemView.findViewById(R.id.rentalRequestItem_imageView);
            viewButton = itemView.findViewById(R.id.rentalRequestItem_viewButton);
        }

        public void setData(RentalRequestModel request) {
            statusET.setText(request.getProperty().getStatus());
            int cityId = request.getProperty().getCityId();
            CityModel city = sqliteOpenHelper.getCityById(cityId);
            cityET.setText(city.getName());
            surfaceAreaET.setText(String.valueOf(request.getProperty().getSurfaceArea()));
            rentalPriceET.setText(String.valueOf(request.getProperty().getRentalPrice()));
            resultET.setText(request.getResult());
            if(request.getProperty().getImagesList().size()>0){
                PropertyImage propertyImage = request.getProperty().getImagesList().get(0);
                String imageUrl = String.format(IMAGE_URL,propertyImage.getFileName());
                Picasso.get()
                        .load(imageUrl)
                        .into(imageView);
            }
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putSerializable("rentalRequest",request);
                    NavOptions navOptions = new NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_in)
                            .setExitAnim(R.anim.slide_out)
                            .setPopEnterAnim(R.anim.fade_in)
                            .setPopExitAnim(R.anim.fade_out)
                            .build();
                    Navigation.findNavController(requireView())
                            .navigate(R.id.nav_request_info,args,navOptions);
                }
            });
        }
    }

    private class PropertyRecyclerAdapter extends RecyclerView.Adapter<RequestViewHolder> {
        List<RentalRequestModel> requestsList;

        public PropertyRecyclerAdapter(List<RentalRequestModel> requestsList) {
            this.requestsList = requestsList;
        }

        @NonNull
        @Override
        public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RequestViewHolder(
                    View.inflate(parent.getContext(), R.layout.rental_request_item_layout_w_view, null)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
            RentalRequestModel request = requestsList.get(position);
            holder.setData(request);
        }

        @Override
        public int getItemCount() {
            return requestsList.size();
        }
    }


}