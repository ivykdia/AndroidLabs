package algonquin.cst2335.li000793;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import algonquin.cst2335.li000793.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        ActivitySecondBinding binding = ActivitySecondBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot()); //+Binding
        Intent dataFromPage1 = getIntent();
        String emailAddress = dataFromPage1.getStringExtra("EmailAddress");
        Log.d("Second Activity", "EmailAddress is " + emailAddress);
        binding.textView2.setText("Welcome back " + emailAddress);

        Log.d("Second Activity", "EmailAddress is " + emailAddress);
        binding.textView2.setText("Welcome back " + emailAddress);
        //create a table named MyTele.xml
        SharedPreferences prefs = getSharedPreferences("MyTele", Context.MODE_PRIVATE);
        //if the user has never use the callpad before, just use the empty string: ""
        String telephone = prefs.getString("PhoneNumber", "");
        binding.editTextPhone.setText(telephone);

        binding.button2.setOnClickListener( clk -> {
            //go back:

            //send data to first page:
            Intent dial = new Intent(Intent.ACTION_DIAL);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("PhoneNumber", binding.editTextPhone.getText().toString());
            editor.apply();

            String phoneNumber = binding.editTextPhone.getText().toString();


            dial.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(dial);

        });



        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override //the camera has disappeared
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData(); // our picture is in here

                            Bitmap thumbnail = data.getParcelableExtra("data");
                            binding.imageView.setImageBitmap(thumbnail);

                            FileOutputStream fOut = null;


                            try {
                                fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);

                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);

                                fOut.flush();

                                fOut.close();

                            } catch (IOException ioe) {
                            }

                        }


                    }
                });
        File file = new File(getFilesDir(), "Picture.png");
        if (file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            binding.imageView.setImageBitmap(theImage);
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        binding.button3.setOnClickListener( click -> {
            //use apps on the phone
            cameraResult.launch(cameraIntent);
            //should return a picture
        });


    }
}

