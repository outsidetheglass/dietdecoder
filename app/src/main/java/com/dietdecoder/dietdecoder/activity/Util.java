package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.foodlog.NewFoodLogChoicesActivity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Objects;

public class Util {

    private final static String TAG = "TAG: " + Util.class.getSimpleName();

    public static String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
            "Aug",
            "Sept", "Oct", "Nov", "Dec" };

    //set the arguments to pass between fragments
    public static final String ARGUMENT_HOUR = "hour";
    public static final String ARGUMENT_MINUTE = "minute";
    public static final String ARGUMENT_DAY = "day";
    public static final String ARGUMENT_MONTH = "month";
    public static final String ARGUMENT_YEAR = "year";

    public static final String ARGUMENT_WHICH_BUTTON = "which_button";

    public static final String ARGUMENT_INGREDIENT_NAME = "ingredient_name";
    public static final String ARGUMENT_INGREDIENT_BRAND = "ingredient_brand";

    public static final String ARGUMENT_NEW_NAME = "new_name";
    public static final String ARGUMENT_OLD_NAME = "old_name";
    public static final String ARGUMENT_LOG_NAME = "log_name";
    public static final String ARGUMENT_OLD_INGREDIENT = "old_ingredient";

    public static final String ARGUMENT_JUST_NOW = "just_now";
    public static final String ARGUMENT_YESTERDAY = "yesterday";
    public static final String ARGUMENT_EARLIER_TODAY = "earlier_today";
    public static final String ARGUMENT_ANOTHER_TIME = "another_time";

    public static final String ARGUMENT_MIDDAY = "midday";
    public static final String ARGUMENT_EARLY = "early";
    public static final String ARGUMENT_MIDNIGHT = "midnight";
    public static final String ARGUMENT_SPECIFIC_TIME = "specific_time";
    public static final String ARGUMENT_AFTERNOON = "afternoon";


    // given an integer of minute of datetime
    // return a string to set for displaying
    // with a 0 in front if the minute is less than 10
    public static String setMinutesString(Integer minute){

        String mMinutesString;

        if ( minute < 10 ) {
            mMinutesString = "0" + minute.toString();
        } else {
            mMinutesString = minute.toString();
        }

        return mMinutesString;
    }//end setMinutesString function

    // set month from zero indexed value to a string of the month name
    public static String setMonthString(Integer monthIndex){

        // if they're invalid values less than Jan or more than Dec, set to those
        if (monthIndex < 0) {
            monthIndex = 0;
        }
        else if (monthIndex > 11) {
            monthIndex = 11;
        }
        String month = months[monthIndex];

        return month;
    }

    // get the integer zero indexed from a three char string of the month
    // if it's anything but the given three char strings, return 0 for January
    public static Integer getMonthInteger(String monthString){
        Integer monthInt;

        if (Objects.equals(monthString, "Jan")) {
            monthInt = 0;
        } else if (Objects.equals(monthString, "Feb")) {
            monthInt = 1;
        } else if (Objects.equals(monthString, "Mar")) {
            monthInt = 2;
        } else if (Objects.equals(monthString, "Apr")) {
            monthInt = 3;
        } else if (Objects.equals(monthString, "May")) {
            monthInt = 4;
        } else if (Objects.equals(monthString, "Jun")) {
            monthInt = 5;
        } else if (Objects.equals(monthString, "Jul")) {
            monthInt = 6;
        } else if (Objects.equals(monthString, "Aug")) {
            monthInt = 7;
        } else if (Objects.equals(monthString, "Sep")) {
            monthInt = 8;
        } else if (Objects.equals(monthString, "Oct")) {
            monthInt = 9;
        } else if (Objects.equals(monthString, "Nov")) {
            monthInt = 10;
        } else if (Objects.equals(monthString, "Dec")) {
            monthInt = 11;
        } else {
            monthInt = 0;
        }

        Log.d(TAG, "newFoodLogActivity monthString=" + monthString );
        Log.d(TAG, "newFoodLogActivity monthInt=" + monthInt.toString() );
        return monthInt;
    }

    public static Instant instantFromValues(Integer mHour, Integer mMinute, Integer mDay, Integer mMonth, Integer mYear) {

        // save it directly from here
        Instant logInstant;
        Calendar logCalendar = Calendar.getInstance();
        logCalendar.set(Calendar.MONTH, mMonth);
        logCalendar.set(Calendar.YEAR, mYear);
        logCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        logCalendar.set(Calendar.MINUTE, mMinute);
        logCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        logInstant = logCalendar.toInstant();

        return logInstant;
    }


    public static Intent intentWithDateTimeButton (Activity activity, LocalDateTime mLocalDateTime,
                                                  String mWhichButton) {

        Intent mIntent = new Intent(activity, NewFoodLogChoicesActivity.class);

        // set the times
        Integer mHour = mLocalDateTime.getHour();
        Integer mMinute = mLocalDateTime.getMinute();
        Integer mDay = mLocalDateTime.getDayOfMonth();
        // LocalDateTime saves it not zero indexed
        // and I'm using Calendar which is zero indexed
        // to save to database minus one
        Integer mMonth = mLocalDateTime.getMonthValue()-1;
        Integer mYear = mLocalDateTime.getYear();

        mIntent.putExtra(Util.ARGUMENT_HOUR, mHour);
        mIntent.putExtra(Util.ARGUMENT_MINUTE, mMinute);
        mIntent.putExtra(Util.ARGUMENT_DAY, mDay);
        mIntent.putExtra(Util.ARGUMENT_MONTH, mMonth);
        mIntent.putExtra(Util.ARGUMENT_YEAR, mYear);
        mIntent.putExtra(Util.ARGUMENT_WHICH_BUTTON, mWhichButton);

        // go back to activity this fragment is in
        return mIntent;
    }
    //
    //
    //
    //
    //
    //
    //
    //

//
//  public class CalendarPickerFragment extends DialogFragment implements  DatePickerDialog.OnDateSetListener {
//
//    public CalendarPickerFragment.OnDateReceiveCallBack mListener;
//    public Context context;
//
//    public static CalendarPickerFragment.OnDateReceiveCallBack mListenerForAlertDialog;
//
//    public interface OnDateReceiveCallBack {
//      public void onDateReceive(int dd, int mm, int yy);
//    }
//
//    public static CalendarPickerFragment getInstance(CalendarPickerFragment.OnDateReceiveCallBack callback) {
//      mListenerForAlertDialog = callback;
//      return new CalendarPickerFragment();
//    }
//
//    @Override
//    public void onAttach(Context context) {
//      super.onAttach(context);
//      this.context = context;
//
//      try {
//        mListener = (CalendarPickerFragment.OnDateReceiveCallBack) context;
//      } catch (ClassCastException e) {
//        throw new ClassCastException(context.toString() + " must implement OnDateSetListener");
//      }
//    }
//
//    public CalendarPickerFragment() {
//    }
//
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//      final Calendar cal = Calendar.getInstance();
//      int year = cal.get(Calendar.YEAR);
//      int month = cal.get(Calendar.MONTH);
//      int day = cal.get(Calendar.DAY_OF_MONTH);
//
//      return new DatePickerDialog(getActivity(), this, year, month, day);
//    }
//
//    @Override
//    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//      Log.d("tag", i + "/" + "/" + i1 + "/" + i2);
//      mListener.onDateReceive(i, i1, i2);
//      mListenerForAlertDialog.onDateReceive(i, i1, i2);
//    }
//
//  }



}