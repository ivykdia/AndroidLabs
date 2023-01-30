package ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import algonquin.cst2335.li000793.databinding.ActivityMainBinding;
import data.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding variableBinding;

   private MainViewModel model;

    @Override  // this starts the application
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(MainViewModel.class);
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());

        variableBinding.mySwitch.setChecked(true);
        setContentView(variableBinding.getRoot());

        variableBinding.myimagebutton.setOnClickListener(imageBtn ->{

        });




        variableBinding.button.setOnClickListener(click -> {
            model.editString.postValue(variableBinding.myedittext.getText().toString());
            model.editString.observe(this, s -> {
                variableBinding.textview.setText("your edit text has" + s);


            });
        });

        variableBinding.myCheckBox.setOnCheckedChangeListener( (btn, isChecked) -> {
            model.isSelected.postValue(variableBinding.myCheckBox.isChecked());
            model.isSelected.observe(this, selected -> {
                variableBinding.myCheckBox.setChecked(selected);
            variableBinding.myRadioButton.setChecked(selected);
            variableBinding.mySwitch.setChecked(selected);

        });
        });

        variableBinding.mySwitch.setOnCheckedChangeListener( (btn, isChecked) -> {
            model.isSelected.postValue(variableBinding.mySwitch.isChecked());
            model.isSelected.observe(this, selected -> {
                variableBinding.myCheckBox.setChecked(selected);
                variableBinding.myRadioButton.setChecked(selected);
                variableBinding.mySwitch.setChecked(selected);

            });
        });


        variableBinding.myRadioButton.setOnCheckedChangeListener( (btn, isChecked) -> {
            model.isSelected.postValue(variableBinding.myRadioButton.isChecked());
            model.isSelected.observe(this, selected -> {
                variableBinding.myCheckBox.setChecked(selected);
                variableBinding.myRadioButton.setChecked(selected);
                variableBinding.mySwitch.setChecked(selected);

            });
        });



    }


}

