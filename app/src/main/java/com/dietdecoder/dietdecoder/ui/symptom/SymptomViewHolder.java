package com.dietdecoder.dietdecoder.ui.symptom;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.symptom.DetailSymptomActivity;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;

public class SymptomViewHolder extends RecyclerView.ViewHolder {


  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  // to set the text for what shows up in the UI
  public TextView symptomItemView;
  public ImageButton symptomItemOptionButton;
  private Context symptomViewHolderContext;

  private SymptomViewHolder(View itemView) {
    super(itemView);
    symptomViewHolderContext = itemView.getContext();
    symptomItemView = itemView.findViewById(R.id.textview_symptom_item);
    symptomItemOptionButton = itemView.findViewById(R.id.imagebutton_symptom_option);
  }


  public void bind(Symptom symptom) {

    String symptomName = symptom.getSymptomName();
    String symptomCategory = symptom.getSymptomCategory();
    String symptomDescription = symptom.getSymptomDescription();
    String symptomSufferType = symptom.getSymptomSufferType();

    symptomItemView.setText( "Name:\n" + symptomName + "\n\nDescription:\n" + symptomDescription);

    symptomItemOptionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        // Initializing the popup menu and giving the reference as current symptomViewHolderContext
        PopupMenu popupMenu = new PopupMenu(symptomViewHolderContext, symptomItemOptionButton);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.item_option_detail_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem symptomMenuItem) {

            Intent detailSymptomIntent = new Intent(symptomViewHolderContext, DetailSymptomActivity.class);
            detailSymptomIntent.putExtra("symptom_name", symptomName);
            detailSymptomIntent.putExtra("symptom_description", symptomDescription);
            detailSymptomIntent.putExtra("symptom_suffertype", symptomSufferType);
            detailSymptomIntent.putExtra("symptom_category", symptomCategory);

            // if more details clicked
            if ( symptomMenuItem.getTitle().toString() == symptomViewHolderContext.getString(R.string.detail) )
            {
              symptomViewHolderContext.startActivity(detailSymptomIntent);
            }

            return true;
          }
        });
        // Showing the popup menu
        popupMenu.show();
      }
    });

  }


  static SymptomViewHolder create(ViewGroup symptomParent) {

    Context symptomViewHolderContext = symptomParent.getContext();
    LayoutInflater symptomInflater = LayoutInflater.from(symptomViewHolderContext);
    View symptomView = symptomInflater.inflate(
      R.layout.recyclerview_symptom_item,
      symptomParent,
      false
    );

    return new SymptomViewHolder(symptomView);
  }


}//end symptom view holder class
