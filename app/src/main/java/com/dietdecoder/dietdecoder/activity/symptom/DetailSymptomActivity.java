package com.dietdecoder.dietdecoder.activity.symptom;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.symptom.SymptomActivity;

public class DetailSymptomActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  public static String mSymptomName;
  public static String mSymptomDescription;
  public static String mSymptomSufferType;
  public static String mSymptomCategory;

  private TextView mSymptomNameView;
  private TextView mSymptomDescriptionView;
  private TextView mSymptomSufferTypeView;
  private TextView mSymptomCategoryView;

  private Button goToViewAllSymptomsButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_symptom_detail);

    mSymptomNameView = findViewById(R.id.textview_symptom_name_value);
    mSymptomSufferTypeView = findViewById(R.id.textview_symptom_suffertype_value);
    mSymptomDescriptionView = findViewById(R.id.textview_symptom_description_value);
    mSymptomCategoryView = findViewById(R.id.textview_symptom_category_value);


    Intent detailSymptomIntent = getIntent();


    // Null check the intent worked
    if (null == detailSymptomIntent) {
      // go back if it was null
      Toast.makeText(this, "Can't view an invalid symptom", Toast.LENGTH_SHORT).show();
      this.startActivity( new Intent(this, SymptomActivity.class));
    }

    // get values from the intent for symptom we're displaying details for
    mSymptomName = detailSymptomIntent.getStringExtra("symptom_name");
    mSymptomNameView.setText(mSymptomName);
    mSymptomCategory = detailSymptomIntent.getStringExtra("symptom_category");
    mSymptomCategoryView.setText(mSymptomCategory);
    mSymptomDescription = detailSymptomIntent.getStringExtra("symptom_description");
    mSymptomDescriptionView.setText(mSymptomDescription);
    mSymptomSufferType = detailSymptomIntent.getStringExtra("symptom_suffertype");
    mSymptomSufferTypeView.setText(mSymptomSufferType);

    goToViewAllSymptomsButton = findViewById(R.id.button_from_detail_to_all_symptom);
    goToViewAllSymptomsButton.setOnClickListener(view -> {

      this.startActivity( new Intent(this, SymptomActivity.class) );

      finish();
    });
  }//end OnCreate

}//end Activity
