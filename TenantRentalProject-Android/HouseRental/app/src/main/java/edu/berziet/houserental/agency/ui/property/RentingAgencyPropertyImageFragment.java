package edu.berziet.houserental.agency.ui.property;

import static edu.berziet.houserental.ApiUrl.IMAGE_URL;
import static edu.berziet.houserental.ApiUrl.PROPERTY_IMAGES_URL;
import static edu.berziet.houserental.ApiUrl.PROPERTY_IMAGE_ID;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.berziet.houserental.R;
import edu.berziet.houserental.models.PropertyImage;
import edu.berziet.houserental.parsers.PropertyImagesListJsonParser;
import edu.berziet.houserental.tools.HttpManager;

public class RentingAgencyPropertyImageFragment extends Fragment {

    private static final String PROPERTY_ID_PARAM = "propertyId";
    private RecyclerView recyclerView;
    private int propertyId;

    public RentingAgencyPropertyImageFragment() {
    }

    public static RentingAgencyPropertyImageFragment newInstance(int propertyId) {
        RentingAgencyPropertyImageFragment fragment = new RentingAgencyPropertyImageFragment();
        Bundle args = new Bundle();
        args.putInt(PROPERTY_ID_PARAM, propertyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            propertyId = getArguments().getInt(PROPERTY_ID_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_renting_agency_property_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.propertyImages_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        view.findViewById(R.id.propertyImages_addImageButton)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putInt("propertyId",propertyId);
                Navigation.findNavController(getView()).navigate(R.id.nav_property_add_image,args);
            }
        });
        GetPropertyImagesAsyncTask asyncTask = new GetPropertyImagesAsyncTask();
        String url = String.format(PROPERTY_IMAGES_URL,propertyId);
        asyncTask.execute(url);
    }
    public void setProgress(boolean progress) {
        ProgressBar progressBar = getView().findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
    public class GetPropertyImagesAsyncTask extends AsyncTask<String,String,String> {
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

            ArrayList<PropertyImage> imagesArrayList =
                    PropertyImagesListJsonParser.getObjectFromJson(s);
            if (imagesArrayList == null) {
                Toast.makeText(getContext(), "Error in server response", Toast.LENGTH_SHORT).show();
                return;
            }
            ImageRecyclerAdapter imageRecyclerAdapter = new ImageRecyclerAdapter(imagesArrayList);
            recyclerView.setAdapter(imageRecyclerAdapter);
        }
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private ImageButton deleteButton;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.propertyImage_imageView);
            deleteButton = itemView.findViewById(R.id.propertyImage_deleteButton);
        }

        public void setData(PropertyImage propertyImage) {
            String url = String.format(IMAGE_URL,propertyImage.getFileName());
            Picasso.get()
                    .load(url)
                    .into(imageView);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(getContext());
                    myAlert.setMessage(getResources().getString(R.string.confirm_delete_image))
                            .setTitle(getResources().getString(R.string.confirm_delete))
                            .setNegativeButton(getResources().getString(R.string.no), null)
                            .setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DeletePropertyImagesAsyncTask deleteAsyncTask =
                                            new DeletePropertyImagesAsyncTask();
                                    String url = String.format(PROPERTY_IMAGE_ID,propertyImage.getImageId());
                                    deleteAsyncTask.execute(url);
                                }
                            });
                    AlertDialog alert = myAlert.create();
                    alert.show();
                }
            });
        }
    }
    private class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageViewHolder>{
        ArrayList<PropertyImage> imagesArrayList;

        public ImageRecyclerAdapter(ArrayList<PropertyImage> imagesArrayList) {
            this.imagesArrayList = imagesArrayList;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.property_image_layout_w_delete,null);
            return new ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            PropertyImage propertyImage = imagesArrayList.get(position);
            holder.setData(propertyImage);
        }

        @Override
        public int getItemCount() {
            return imagesArrayList.size();
        }
    }

    public class DeletePropertyImagesAsyncTask extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgress(true);
        }
        @Override
        protected String doInBackground(String... params) {
            return HttpManager.sendRequest(params[0],"DELETE","");
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setProgress(false);
            if (s == null) {
                Toast.makeText(getContext(), "Error connecting to server", Toast.LENGTH_SHORT).show();
                return;
            }
            GetPropertyImagesAsyncTask asyncTask = new GetPropertyImagesAsyncTask();
            String url = String.format(PROPERTY_IMAGES_URL,propertyId);
            asyncTask.execute(url);

        }
    }

}