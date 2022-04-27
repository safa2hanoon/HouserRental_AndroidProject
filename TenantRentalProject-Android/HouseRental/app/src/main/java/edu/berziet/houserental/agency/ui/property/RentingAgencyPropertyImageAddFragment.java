package edu.berziet.houserental.agency.ui.property;

import static edu.berziet.houserental.ApiUrl.PROPERTY_IMAGES_URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import edu.berziet.houserental.R;
import edu.berziet.houserental.models.SimpleSuccessResponseModel;
import edu.berziet.houserental.parsers.SimpleSuccessResponseJsonParser;
import kotlin.Unit;

public class RentingAgencyPropertyImageAddFragment extends Fragment {

    private static final String ARG_PROPERTY_ID = "propertyId";

    private int propertyId;
    private ImageView imageView;
    private Uri imageUri;

    public RentingAgencyPropertyImageAddFragment() {
    }

    public static RentingAgencyPropertyImageAddFragment newInstance(int propertyId) {
        RentingAgencyPropertyImageAddFragment fragment = new RentingAgencyPropertyImageAddFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROPERTY_ID, propertyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            propertyId = getArguments().getInt(ARG_PROPERTY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renting_agency_property_image_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.propertyAddImage_imageView);
        imageView.setOnClickListener(v -> pickImage());

        view.findViewById(R.id.propertyAddImage_addButton)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(imageUri!=null) {
                            PostPropertyAsyncTask asyncTask = new PostPropertyAsyncTask();
                            String url = String.format(PROPERTY_IMAGES_URL, propertyId);
                            asyncTask.execute(url);
                        }else{
                            pickImage();
                            Snackbar.make(getView(),
                                    getResources().getString(R.string.select_image),
                                    BaseTransientBottomBar.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
        pickImage();
    }

    private void pickImage() {
        ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(512, 512)    //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent(intent -> {
                    pickImageActivityResultLauncher.launch(intent);
                    return Unit.INSTANCE;
                });
    }
    ActivityResultLauncher<Intent> pickImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && imageView!=null) {
                        imageView.setImageURI(data.getData());
                        imageUri=data.getData();
                    }
                }
            });

    public void setProgress(boolean progress) {
        ProgressBar progressBar = getView().findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public class PostPropertyAsyncTask extends AsyncTask<String, String,
            String> {
        String response="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            String attachmentName = "bitmap";
            String attachmentFileName = "bitmap.bmp";
            String crlf = "\r\n";
            String twoHyphens = "--";
            String boundary =  "*****";
            HttpURLConnection httpUrlConnection = null;
            URL url = null;
            try {
                url = new URL(params[0]);
                httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setDoInput(true); // Allow Inputs
                httpUrlConnection.setDoOutput(true); // Allow Outputs
                httpUrlConnection.setUseCaches(false); // Don't use a Cached Copy

                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
                httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
                httpUrlConnection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + boundary);
                DataOutputStream request = new DataOutputStream(
                        httpUrlConnection.getOutputStream());

                request.writeBytes(twoHyphens + boundary + crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"" +
                        attachmentName + "\";filename=\"" +
                        attachmentFileName + "\"" + crlf);
                request.writeBytes(crlf);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                InputStream imageStream = null;
                    imageStream = getContext().getContentResolver().openInputStream(imageUri);
                Bitmap selectedBitmap = BitmapFactory.decodeStream(imageStream);
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                InputStream in = new ByteArrayInputStream(bos.toByteArray());

                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                // create a buffer of maximum size
                //bytesAvailable = fileInputStream.available();
                bytesAvailable = in.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = in.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    request.write(buffer, 0, bufferSize);
                    bytesAvailable = in.available();
                    bufferSize = Math
                            .min(bytesAvailable, maxBufferSize);
                    bytesRead = in.read(buffer, 0,
                            bufferSize);

                }

                // send multipart form data necesssary after file
                // data...
                request.writeBytes(crlf);
                request.writeBytes(twoHyphens + boundary + twoHyphens
                        + crlf);

                // Responses from the server (code and message)
                //int serverResponseCode = conn.getResponseCode();
                String responseMessage = httpUrlConnection
                        .getResponseMessage();

                //System.out.println("Respone Code : " + serverResponseCode);
                System.out.println("Respone Message : " + responseMessage);
                if(httpUrlConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader((httpUrlConnection.getInputStream())));
                    String line;
                    InputStream responseStream = new
                            BufferedInputStream(httpUrlConnection.getInputStream());
                    StringBuilder stringBuilder = new StringBuilder();
                    BufferedReader responseStreamReader =
                            new BufferedReader(new InputStreamReader(responseStream));
                    while ((line = responseStreamReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    responseStreamReader.close();

                    response = stringBuilder.toString();
                    responseStream.close();

                }
                // close the streams //
                //fileInputStream.close();
                in.close();
                request.flush();
                request.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setProgress(false);

            if (s == null) {
                Toast.makeText(getContext(), "Error connecting to server", Toast.LENGTH_SHORT).show();
                return;
            }
            SimpleSuccessResponseModel responseModel =
                    SimpleSuccessResponseJsonParser.getObjectFromJson(s);
            if(responseModel.isSuccess()){
                Toast.makeText(getContext(), getResources().getString(R.string.added_successfully), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigateUp();
            }else{
                Toast.makeText(getContext(), responseModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}