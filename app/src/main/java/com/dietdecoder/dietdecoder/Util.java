package com.dietdecoder.dietdecoder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

import com.dietdecoder.dietdecoder.activity.foodlog.EditFoodLogActivity;
import com.dietdecoder.dietdecoder.activity.foodlog.NewFoodLogActivity;
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

    public static final ZoneId defaultZoneId = ZoneId.systemDefault();

    public static final StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
    public static final StyleSpan italicStyle = new StyleSpan(Typeface.ITALIC);

    ///////////////////////////////////////////////////////
    //////// Set the arguments to pass between fragments///
    ///////////////////////////////////////////////////////

    // action to take given this argument
    public static final String ARGUMENT_ACTION = "action";
    // actions to take
    public static final String ARGUMENT_DUPLICATE = "duplicate";


    // which activity or fragment we are from
    public static final String ARGUMENT_FROM = "from";
    // fragment or activity to go to given this argument
    public static final String ARGUMENT_GO_TO = "go_to";

    // which activity we are from
    public static final String ARGUMENT_ACTIVITY_FROM = "activity_from";
    // activities we might be from
    public static final String ARGUMENT_ACTIVITY_FROM_EDIT_FOOD_LOG = "activity_from_edit_food_log";
    public static final String ARGUMENT_ACTIVITY_FROM_NEW_FOOD_LOG = "activity_from_new_food_log";
    public static final String ARGUMENT_ACTIVITY_FROM_ARE_YOU_SURE = "from_are_you_sure";




    // fragment to go to given this argument
    public static final String ARGUMENT_FRAGMENT_GO_TO = "fragment_go_to";
    // fragments to go to
    public static final String ARGUMENT_GO_TO_NAME = "go_to_name";
    public static final String ARGUMENT_GO_TO_DATE_TIME_CHOICES = "go_to_date_time_choices";
    public static final String ARGUMENT_GO_TO_PART_OF_DAY = "go_to_part_of_day";
    public static final String ARGUMENT_GO_TO_BRAND = "go_to_brand";
    public static final String ARGUMENT_GO_TO_SPECIFIC_DATE_FRAGMENT = "go_to_specific_date";
    public static final String ARGUMENT_GO_TO_SPECIFIC_TIME_FRAGMENT = "go_to_specific_time";

    public static final String ARGUMENT_GO_TO_DELETE_FOOD_LOG = "go_to_delete_food_log";
    public static final String ARGUMENT_GO_TO_EDIT_FOOD_LOG_ACTIVITY =
            "go_to_edit_food_log_activity";
    public static final String ARGUMENT_GO_TO_EDIT_FOOD_LOG_FRAGMENT =
            "go_to_edit_food_log_fragment";
    public static final String ARGUMENT_GO_TO_FOOD_LOG_ACTIVITY = "go_to_food_log";



    // which fragment we're from
    public static final String ARGUMENT_FRAGMENT_FROM = "fragment_from";
    // places we might be from options
    public static final String ARGUMENT_FROM_INGREDIENT_NAME = "from_ingredient_name";
    public static final String ARGUMENT_FROM_SPECIFIC_TIME = "from_specific_time";
    public static final String ARGUMENT_FROM_SPECIFIC_DATE = "from_specific_date";
    public static final String ARGUMENT_FROM_DATE_TIME_CHOICES = "from_date_time_choices";
    public static final String ARGUMENT_FROM_INGREDIENT_BRAND = "from_ingredient_brand";
    public static final String ARGUMENT_FROM_PART_OF_DAY = "from_part_of_day";
    public static final String ARGUMENT_FROM_EDIT_FOOD_LOG = "from_edit_food_log";
    public static final String ARGUMENT_FRAGMENT_FROM_DELETE_FOOD_LOG =
            "from_fragment_delete_food_log";



    // argument for what the UUID of the food log is
    public static final String ARGUMENT_FOOD_LOG_ID = "food_log_id";


    // which value do we want to change
    public static final String ARGUMENT_CHANGE = "change";
    // values as options to change
    public static final String ARGUMENT_CHANGE_CONSUMED = "change_consumed";
    public static final String ARGUMENT_CHANGE_COOKED = "change_cooked";
    public static final String ARGUMENT_CHANGE_ACQUIRED = "change_acquired";

    public static final String ARGUMENT_HOUR = "hour";
    public static final String ARGUMENT_MINUTE = "minute";
    public static final String ARGUMENT_DAY = "day";
    public static final String ARGUMENT_MONTH = "month";
    public static final String ARGUMENT_YEAR = "year";
    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////
    // for displaying values////////////////////////////////
    ////////////////////////////////////////////////////////

    // make substring bold and italic and regular font
    public static SpannableStringBuilder setBoldItalicSpan(String boldString, String notBoldString,
                                       String italicString, String notItalicString) {

        // first string is bold
            // so its length must be its own length
        // second string is plain
            // must be its length plus first string
        // third string is italic
        // fourth string is plain
        int firstStringLength = boldString.length();
        int secondStringLength = firstStringLength + notBoldString.length();
        int thirdStringLength = secondStringLength + italicString.length();
        int fourthStringLength = thirdStringLength + notItalicString.length();

        Log.d(TAG, "boldstring: " + boldString + boldString.length());
        Log.d(TAG, "notboldstring: " + notBoldString+ notBoldString.length());
        Log.d(TAG, "italic: " + italicString+ italicString.length());
        Log.d(TAG, "notital: " + notItalicString+ notItalicString.length());
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(boldString);
        // this is the location of the substring that you want to make bold
        int start = 0;
        int end = start + boldString.length();
        // use exclusive to say we want this substring bold
        spannableStringBuilder.setSpan(Util.boldStyle, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // append whatever else you want
        spannableStringBuilder.append(notBoldString);

        Log.d(TAG, "spannableStringBuilder: " + spannableStringBuilder.length());
        // if we have more strings to use, add them
        if ( !TextUtils.isEmpty(italicString) ) {

            int italicStart = firstStringLength + secondStringLength;
            int italicEnd = italicStart + italicString.length()-1;
            spannableStringBuilder.append(italicString,
                    Util.italicStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (!TextUtils.isEmpty(notItalicString)){
                spannableStringBuilder.append(notItalicString);
            }
        }

        Log.d(TAG, "spannableStringBuilder: " + spannableStringBuilder.length());
        return spannableStringBuilder;

    }


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

    public static String stringRelativeTimeFromInstant(Instant printThisInstant,
                                                       Instant printRelativeToThisInstant){

        // the answer string for displaying the difference pretty
        String relativityToInstant;

        // find the number of days between the two instants
        int totalDaysApart = integerRelativeTimeFromInstant(printThisInstant,
                printRelativeToThisInstant);


        // if the year and month and day are the same, today
        // if the year and month and day is one day before, yesterday
        // then it's day before yesterday
        // then it's by number of days before yesterday
        // TODO fix this to get printed dates from strings, not here, can't get translated
        if ( totalDaysApart == 0 ){
            relativityToInstant = "Same day";
        } else if ( totalDaysApart == 1 ){
            relativityToInstant = "Day before";
        } else if ( totalDaysApart == -1 )  {
            relativityToInstant = "Predicted tomorrow";
        } else if ( totalDaysApart < 0 ) {
            relativityToInstant =
                        "Predicted " + Math.abs(totalDaysApart) + " days from now";
        }
        else {
            relativityToInstant = totalDaysApart + " days before";
        }

        return relativityToInstant;
    }
    public static Integer integerRelativeTimeFromInstant(Instant printThisInstant,
                                                       Instant printRelativeToThisInstant){

        Integer relativityToInstant;

        Calendar printThisCalendar = GregorianCalendar.from(
                printThisInstant.atZone( defaultZoneId )
        ) ;
        Calendar printRelativeToThisCalendar = GregorianCalendar.from(
                printRelativeToThisInstant.atZone( defaultZoneId )
        ) ;


        // calculate number of days before
        int yearsApart;
        int totalDaysApart;

        // start with which day of the year and which year they happened
        int olderDayOfYear = printRelativeToThisCalendar.get(Calendar.DAY_OF_YEAR);
        int newerDayOfYear = printThisCalendar.get(Calendar.DAY_OF_YEAR);
        int olderYear = printRelativeToThisCalendar.get(Calendar.YEAR);
        int newerYear = printThisCalendar.get(Calendar.YEAR);

        int daysInYearApart = newerDayOfYear - olderDayOfYear;
        // if its the same year, newer's day of year minus older's day of year
        if ( olderYear == newerYear ) {
            totalDaysApart = daysInYearApart;
        }
        // else add 365 times number of years difference, minus day of years
        else {
            yearsApart = newerYear - olderYear;
            // forget leap year for now TODO make work for leap years
            totalDaysApart = yearsApart*365 + daysInYearApart;
        }


        return totalDaysApart;
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
    public static Intent intentChoicesButton(Activity activity, String whereGoTo,
                                             String whereFrom) {
        // start the intent to pass data along
        Intent mIntent = null;
        // if it doesn't exist, make it
        if ( activity.getIntent() == null ) {
            // if we want to go to edit or add
            if ( TextUtils.equals(whereGoTo, Util.ARGUMENT_GO_TO_EDIT_FOOD_LOG_ACTIVITY)) {
                mIntent = new Intent(activity, EditFoodLogActivity.class);
                // where we want to go next
                mIntent.putExtra(Util.ARGUMENT_GO_TO, whereGoTo);
                // which fragment we came from
                mIntent.putExtra(Util.ARGUMENT_FROM, whereFrom);
            } else {
                // default to guessing we're making a new food log
                mIntent = new Intent(activity, NewFoodLogActivity.class);
                // where we want to go next
                mIntent.putExtra(Util.ARGUMENT_FRAGMENT_GO_TO, whereGoTo);
                // which fragment we came from
                mIntent.putExtra(Util.ARGUMENT_FRAGMENT_FROM, whereFrom);
            }
        } else {
            // activity already has an intent so use that one
            mIntent = activity.getIntent();
        }

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
    // Buttons to be used from different fragments or activities//////////
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

    // same as above but for activities

    public static Intent intentWithFoodLogIdStringButtonActivity(Activity activity,
                                                         String foodLogIdString,
                                                         String whereGoTo, String whereFrom) {
        // start the intent to pass data along
        // and set which fragment we came from and where we want to go next
        Intent mIntent = intentChoicesButton(activity, whereGoTo, whereFrom);

        // set the id of the food log
        // and put it in the intent so we can access it from future fragments
        mIntent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, foodLogIdString);

        // now we're ready to go back to activity this fragment is in
        return mIntent;
    }



    // takes in food id, fragment from, fragment to go to, and what we want to change
    public static Intent intentChangeWithFoodLogIdStringButton(FragmentActivity activity,
                                                         String foodLogIdString,
                                                         String fragmentGoTo,
                                                               String fragmentFrom,
                                                               String whatToChange) {
        // start the intent to pass data along
        // and set which fragment we came from and where we want to go next
        Intent mIntent = intentChoicesButton(activity, fragmentGoTo, fragmentFrom);

        // set the id of the food log
        // and put it in the intent so we can access it from future fragments
        mIntent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, foodLogIdString);
        mIntent.putExtra(Util.ARGUMENT_CHANGE, whatToChange);

        // now we're ready to go back to activity this fragment is in
        return mIntent;
    }

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////
    // Change defaults //////////
    ////////////////////////////////////////////////////////

    // set the edit text given to use word wrap up to 6 lines but not allow the enter key
    public static EditText setEditTextWordWrapNoEnter(EditText editText) {
/*
What setSingleLine(true) does is calling setHorizontallyScrolling(true) and setLines(1) implicitly,
 alongside with altering some IME keyboard settings to disable the enter key.
In turn, the call to setLines(1) is like calling setMinLines(1) and setMaxLines(1) in one call.
Some input types (i.e., the constants from InputType.TYPE_*) calls setSingleLine(true) implicitly,
or at least achieves the same effect.
 */
// IMPORTANT, do this before any of the code following it
        editText.setSingleLine(true);
// IMPORTANT, to allow wrapping
        editText.setHorizontallyScrolling(false);
// IMPORTANT, or else your edit text would wrap but not expand to multiple lines
        editText.setMaxLines(6);

        return editText;
    }

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////
    // Boolean Checks//////////
    ////////////////////////////////////////////////////////

    public static Boolean isEdited(String old, String current){
        Boolean isEditedBoolean = null;

        // if the old string is not equal to the current string
        // it was edited, so return true
        if (!TextUtils.equals(old, current)) {
            isEditedBoolean = Boolean.TRUE;
        } else {
            isEditedBoolean = Boolean.FALSE;
        }

        return isEditedBoolean;
    }

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////
    // set which part of food log to change //////////
    ////////////////////////////////////////////////////////

    public static LocalDateTime getDateTimeConsumedAcquiredCooked(String whatToChange,
                                                           FoodLog foodLog) {

        Instant mInstant = null;

        if (TextUtils.equals(whatToChange, Util.ARGUMENT_CHANGE_CONSUMED)) {
            mInstant = foodLog.getMDateTimeConsumed();
        } else if (TextUtils.equals(whatToChange,
                Util.ARGUMENT_CHANGE_COOKED)) {
            mInstant = foodLog.getMDateTimeCooked();
        } else if (TextUtils.equals(whatToChange,
                Util.ARGUMENT_CHANGE_ACQUIRED)) {
            mInstant = foodLog.getMDateTimeAcquired();
        } else {
            // default to consumed time
            Log.d(TAG, "Weren't given a argument for what to change, defaulting to consumed...");
            mInstant = foodLog.getMDateTimeConsumed();
        }

        return Util.localDateTimeFromInstant(mInstant);
    }

    // return a food log with the instant of when consumed or acquired or cooked happened
    public static FoodLog setFoodLogConsumedAcquiredCooked(String whatToChange,
                                                           FoodLog foodLog,
                                                           LocalDateTime localDateTime) {

        Instant instant  = Util.instantFromLocalDateTime(localDateTime);

        if (TextUtils.equals(whatToChange, Util.ARGUMENT_CHANGE_CONSUMED)) {
            foodLog.setMDateTimeConsumed(instant);
        } else if (TextUtils.equals(whatToChange,
                Util.ARGUMENT_CHANGE_COOKED)) {
            foodLog.setMDateTimeCooked(instant);
        } else if (TextUtils.equals(whatToChange,
                Util.ARGUMENT_CHANGE_ACQUIRED)) {
            foodLog.setMDateTimeAcquired(instant);
        }
        return foodLog;
    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////

}