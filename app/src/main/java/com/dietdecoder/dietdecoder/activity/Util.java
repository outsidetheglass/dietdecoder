package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.dietdecoder.dietdecoder.activity.foodlog.NewFoodLogChoicesActivity;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class Util {

    private final static String TAG = "TAG: " + Util.class.getSimpleName();

    public static String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
            "Aug",
            "Sept", "Oct", "Nov", "Dec" };

    //set the arguments to pass between fragments

    // which fragment to go to
    public static final String ARGUMENT_FRAGMENT_GO_TO = "fragment_go_to";
    public static final String ARGUMENT_FRAGMENT_FROM = "fragment_from";

    public static final String ARGUMENT_GO_TO_NAME = "go_to_name";
    public static final String ARGUMENT_GO_TO_DATE_TIME_CHOICES = "go_to_date_time_choices";
    public static final String ARGUMENT_GO_TO_PART_OF_DAY = "go_to_part_of_day";
    public static final String ARGUMENT_GO_TO_COOKED_TIME = "go_to_cooked_time";
    public static final String ARGUMENT_GO_TO_ACQUIRED_TIME = "go_to_acquired_time";
    public static final String ARGUMENT_GO_TO_BRAND = "go_to_brand";
    public static final String ARGUMENT_GO_TO_SPECIFIC_DATE = "go_to_specific_date";
    public static final String ARGUMENT_GO_TO_SPECIFIC_TIME = "go_to_specific_time";

    public static final String ARGUMENT_DONE = "done";

    public static final String ARGUMENT_FROM_INGREDIENT_NAME = "from_ingredient_name";
    public static final String ARGUMENT_FROM_SPECIFIC_TIME = "from_specific_time";
    public static final String ARGUMENT_FROM_SPECIFIC_DATE = "from_specific_date";
    public static final String ARGUMENT_FROM_DATE_TIME_CHOICES = "from_date_time_choices";
    public static final String ARGUMENT_FROM_INGREDIENT_BRAND = "from_ingredient_brand";
    public static final String ARGUMENT_FROM_PART_OF_DAY = "from_part_of_day";





    public static final String ARGUMENT_FOOD_LOG_ID = "food_log_id";

    public static final String ARGUMENT_HOUR = "hour";
    public static final String ARGUMENT_MINUTE = "minute";
    public static final String ARGUMENT_DAY = "day";
    public static final String ARGUMENT_MONTH = "month";
    public static final String ARGUMENT_YEAR = "year";

    public static final String ARGUMENT_INGREDIENT_NAME = "ingredient_name";
    public static final String ARGUMENT_INGREDIENT_BRAND = "ingredient_brand";

    public static final String ARGUMENT_NEW_NAME = "new_name";
    public static final String ARGUMENT_OLD_NAME = "old_name";
    public static final String ARGUMENT_LOG_NAME = "log_name";
    public static final String ARGUMENT_OLD_INGREDIENT = "old_ingredient";

    public static final String ARGUMENT_JUST_NOW = "just_now";
    public static final String ARGUMENT_YESTERDAY = "yesterday";
    public static final String ARGUMENT_EARLIER_TODAY = "earlier_today";

    public static final String ARGUMENT_MIDDAY = "midday";
    public static final String ARGUMENT_EARLY = "early";
    public static final String ARGUMENT_MIDNIGHT = "midnight";
    public static final String ARGUMENT_AFTERNOON = "afternoon";
    public static final String ARGUMENT_EVENING = "evening";

    public static final ZoneId defaultZoneId = ZoneId.systemDefault();


    ////////////////////////////////////////////////////////
    // for displaying values////////////////////////////////
    ////////////////////////////////////////////////////////
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
    //given an instant in time, display it pretty
    public static String stringFromInstant(Instant instant){

        Calendar logCalendar = GregorianCalendar.from(instant.atZone( defaultZoneId )) ;
        String fullLogTime = logCalendar.getTime().toString();


        String logNumberDayOfMonth = String.valueOf(logCalendar.get(Calendar.DAY_OF_MONTH));
        String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                "Sept", "Oct", "Nov", "Dec" };
        String logMonth = months[logCalendar.get(Calendar.MONTH)];

        String logYear = String.valueOf(logCalendar.get(Calendar.YEAR));
        String logShortYear = logYear.substring(2);

        String logDate =  logMonth + "/" + logNumberDayOfMonth + "/" + logShortYear;


        String[] days = new String[] { "Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat" };
        String logDayOfWeek = days[logCalendar.get(Calendar.DAY_OF_WEEK) - 1];


        String logHour = String.valueOf(logCalendar.get(Calendar.HOUR_OF_DAY));
        // if minute is less than 10, add a 0 to the beginning to display pretty
        Integer logMinuteInteger = logCalendar.get(Calendar.MINUTE);
        String logMinuteString;
        if (logMinuteInteger < 10) {
            logMinuteString =
                    "0" + String.valueOf(logCalendar.get(Calendar.MINUTE));
        } else {
            logMinuteString =
                    String.valueOf(logCalendar.get(Calendar.MINUTE));
        }



        String logTime = logHour + ":" + logMinuteString;


        return(logTime + " " + logDayOfWeek + ", " + logMonth + " " + logNumberDayOfMonth + " " + logYear);

    }
     ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////
    // conversions between data types///////////////////////
    ////////////////////////////////////////////////////////
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
    public static Calendar calendarFromInstant(Instant instant){
        return GregorianCalendar.from( zonedDateTimeFromInstant( instant ) );
    }
    public static ZonedDateTime zonedDateTimeFromInstant(Instant instant){
        return instant.atZone( defaultZoneId );
    }
    public static LocalDateTime localDateTimeFromInstant(Instant instant){
        return LocalDateTime.ofInstant(instant, defaultZoneId);
    }
    public static Instant instantFromLocalDateTime(LocalDateTime localDateTime){
        return localDateTime.toInstant( defaultZoneId.getRules().getOffset(localDateTime) );
    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////
    // Base of making intent with fragment from and go to //
    ////////////////////////////////////////////////////////
    // start the intent to pass data along
    // and set which fragment we came from and where we want to go next
    public static Intent intentChoicesButton(Activity activity, String fragmentGoTo,
                                             String fragmentFrom) {
        // start the intent to pass data along
        Intent mIntent = new Intent(activity, NewFoodLogChoicesActivity.class);
        // where we want to go next
        mIntent.putExtra(Util.ARGUMENT_FRAGMENT_GO_TO, fragmentGoTo);
        // which fragment we came from
        mIntent.putExtra(Util.ARGUMENT_FRAGMENT_FROM, fragmentFrom);

        return mIntent;
    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////
    // put values into intents//////////////////////////////
    ////////////////////////////////////////////////////////
    // save as integers from localdatetime
    public static Intent intentPutDateFromLocalDateTime(Intent intent, LocalDateTime localDateTime) {

        Integer mDay = localDateTime.getDayOfMonth();
        // LocalDateTime saves it not zero indexed
        // and I'm using Calendar which is zero indexed
        // to save to database minus one
        Integer mMonth = localDateTime.getMonthValue()-1;
        Integer mYear = localDateTime.getYear();

        // put the ints into the intent
        intent = intentPutDateFromValues(intent, mDay, mMonth, mYear);

        return intent;
    }

    public static Intent intentPutDateFromValues(Intent intent, Integer day, Integer month,
                                                       Integer year) {

        intent.putExtra(Util.ARGUMENT_DAY, day);
        intent.putExtra(Util.ARGUMENT_MONTH, month);
        intent.putExtra(Util.ARGUMENT_YEAR, year);

        return intent;
    }

    public static Intent intentPutTimeFromLocalDateTime(Intent intent, LocalDateTime localDateTime) {

        // set the times
        Integer mHour = localDateTime.getHour();
        Integer mMinute = localDateTime.getMinute();

        intent.putExtra(Util.ARGUMENT_HOUR, mHour);
        intent.putExtra(Util.ARGUMENT_MINUTE, mMinute);

        return intent;
    }

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////
    // Buttons to be used from different fragments//////////
    ////////////////////////////////////////////////////////

    // go back to new food log activity
    // with the ID of foodlog set
    // and the fact we came from this fragment
    // and which fragment to go to next
    public static Intent intentWithFoodLogIdStringButton(FragmentActivity activity,
                                                         String foodLogIdString,
                                                         String fragmentGoTo, String fragmentFrom) {
        // start the intent to pass data along
        // and set which fragment we came from and where we want to go next
        Intent mIntent = intentChoicesButton(activity, fragmentGoTo, fragmentFrom);

        // set the id of the food log
        // and put it in the intent so we can access it from future fragments
        mIntent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, foodLogIdString);

        // now we're ready to go back to activity this fragment is in
        return mIntent;
    }

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////

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