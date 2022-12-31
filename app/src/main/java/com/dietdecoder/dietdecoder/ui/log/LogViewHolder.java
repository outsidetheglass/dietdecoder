package com.dietdecoder.dietdecoder.ui.log;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.log.DeleteLogActivity;
import com.dietdecoder.dietdecoder.activity.log.DetailLogActivity;
import com.dietdecoder.dietdecoder.activity.log.EditLogActivity;
import com.dietdecoder.dietdecoder.activity.log.NewLogActivity;
import com.dietdecoder.dietdecoder.database.log.Log;

import java.time.Instant;

public class LogViewHolder extends RecyclerView.ViewHolder {



  // make a TAG to use to log errors
  private final String TAG = getClass().getSimpleName();

  // to set the text for what shows up in the UI
  public TextView logItemView;
  public ImageButton logItemOptionButton;
  private Context logContext;

  private LogViewHolder(View itemView) {
    super(itemView);
    logContext = itemView.getContext();
    logItemView = itemView.findViewById(R.id.textview_log_item);

//    //TODO get edit working and uncomment this and figure out how to place it right
//    logItemOptionButton = itemView.findViewById(R.id.imagebutton_log_option);
  }


  public void bind(Log log) {

    String logDateTime = log.getLogDateTimeString();
    String logIngredientName = log.getLogIngredientName();
    String logString = log.toString();

    logItemView.setText(logIngredientName + "\n(" + logDateTime + ")");

    // change this to OptionButton when that's working, or not
    logItemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        // Initializing the popup menu and giving the reference as current logContext
        PopupMenu popupMenu = new PopupMenu(logContext, logItemView);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.item_options_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem logMenuItem) {

            // if edit clicked
            if ( logMenuItem.getTitle().toString() == logContext.getString(R.string.edit))
            {
              Toast.makeText(logContext, "Edit was clicked", Toast.LENGTH_SHORT).show();
              //TODO turn this into a fragment, or just a popup
//              Intent editLogIntent = new Intent(logContext, EditLogActivity.class);
//              editLogIntent.putExtra("log_datetime", logDateTime);
//              //TODO add other properties of log type here
//              //editLogIntent.putExtra("log_concern", logConcern);
//              logContext.startActivity( editLogIntent );
            }
            // if delete clicked
            else if ( logMenuItem.getTitle().toString()  == logContext.getString(R.string.delete ))
            {
              Toast.makeText(logContext, "Delete was clicked", Toast.LENGTH_SHORT).show();
//              logContext.startActivity(
//                new Intent(logContext, DeleteLogActivity.class)
//              );
            }
            // if delete clicked
            else if ( logMenuItem.getTitle().toString()  == logContext.getString(R.string.duplicate ))
            {
              Toast.makeText(logContext, "Duplicate was clicked", Toast.LENGTH_SHORT).show();

              logContext.startActivity(
                new Intent(logContext, NewLogActivity.class).putExtra(
                  "ingredientName", logIngredientName).putExtra("ingredientDateTime", logDateTime)
              );
            }
            // if more details clicked
            else if ( logMenuItem.getTitle().toString() == logContext.getString(R.string.detail) )
            {
              Intent detailIntent = new Intent(logContext, DetailLogActivity.class);
              detailIntent.putExtra("log_detail", logString);
              logContext.startActivity(detailIntent);
            }

            return true;
          }
        });
        // Showing the popup menu
        popupMenu.show();
      }
    });

  }


  static com.dietdecoder.dietdecoder.ui.log.LogViewHolder create(ViewGroup logParent) {

    Context logContext = logParent.getContext();
    LayoutInflater logInflater = LayoutInflater.from(logContext);
    View logView = logInflater.inflate(
      R.layout.recyclerview_log_item,
      logParent,
      false
    );

    return new com.dietdecoder.dietdecoder.ui.log.LogViewHolder(logView);
  }


}//end log view holder class
