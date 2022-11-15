package com.dietdecoder.dietdecoder.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ResourcesActivity extends AppCompatActivity {

  TextView glossarySubTitleTextView;
  TextView dizzinessTextView;
  TextView allSymptomDescriptionsTextView;
  TextView heartburnDescriptionTextView;

  TextView helpfulSuggestions;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Descriptions of symptoms
    glossarySubTitleTextView.setText("Descriptions of Symptoms");
    dizzinessTextView.setText("https://vertigodetective.com/glossary/dizziness/");
    allSymptomDescriptionsTextView.setText("https://www.factvsfitness.com/blogs/news/histamine-intolerance-symptoms");

    // if symptom is light sensitivity
    helpfulSuggestions.setText("Use polarized sunglasses");

    heartburnDescriptionTextView.setText("https://www.healthline.com/health/gerd/heartburn-vs-acid-reflux");

  }
}
