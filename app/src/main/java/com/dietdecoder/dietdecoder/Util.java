package com.dietdecoder.dietdecoder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.dietdecoder.dietdecoder.activity.DetailActivity;
import com.dietdecoder.dietdecoder.activity.MainActivity;
import com.dietdecoder.dietdecoder.activity.OtherActivity;
import com.dietdecoder.dietdecoder.activity.EditActivity;
import com.dietdecoder.dietdecoder.activity.ingredientlog.ChooseIngredientActivity;
import com.dietdecoder.dietdecoder.activity.ingredientlog.ListIngredientLogActivity;
import com.dietdecoder.dietdecoder.activity.ingredientlog.NewIngredientLogActivity;
import com.dietdecoder.dietdecoder.activity.symptomlog.ChooseSymptomActivity;
import com.dietdecoder.dietdecoder.activity.symptomlog.ListSymptomLogActivity;
import com.dietdecoder.dietdecoder.activity.symptomlog.NewSymptomLogActivity;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewModel;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.UUID;

public class Util {

    private final static String TAG = "TAG: " + Util.class.getSimpleName();

    public static String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
            "Aug",
            "Sept", "Oct", "Nov", "Dec" };


    public static final ZoneId defaultZoneId = ZoneId.systemDefault();

    public static final StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
    public static final StyleSpan italicStyle = new StyleSpan(Typeface.ITALIC);

    public static final int fragmentContainerViewEdit = R.id.fragment_container_view_edit;

    public static final int fragmentContainerViewAddIngredientLog =
            R.id.fragment_container_view_new_ingredient_log;

    public static final int fragmentContainerViewAddSymptomLog =
            R.id.fragment_container_view_new_symptom_log;


    //public static final int fragmentContainerViewEditSymptomLog = R.id
    // .fragment_container_view_edit_symptom_log;
    public static final int fragmentContainerViewChooseSymptomLog =
            R.id.recyclerview_symptom_name_choices;
    ///////////////////////////////////////////////////////
    //////// Set the arguments to pass between fragments///
    ///////////////////////////////////////////////////////


    //TODO set this to be preferences a different time
    public static final Integer early_hour = 8;
    // personally 8am and 11pm work for me, but preferences should be set in case they want 1am
    // or 9pm or something else
    public static final Integer earliest_hour = early_hour;
    public static final Integer midday_hour = 12;
    public static final Integer afternoon_hour = 15;
    public static final Integer evening_hour = 19;
    public static final Integer midnight_hour = 23;
    public static final Integer latest_hour = midnight_hour;

    // confirmed textutils.equals does not see things as same unless they're EXACTLY the same
    // also confirmed if an argument is given twice it replaces the first one, it won't hold on
    // to both. i.e. if ARGUMENT_CHANGE goes from CHANGE_BEGIN to CHANGE_END it won't have it
    // twice for both options, it'll only say CHANGE_END.
    public static final String ARGUMENT_INGREDIENT_ID = "ingredient_id";
    public static final String ARGUMENT_INGREDIENT_NAME = "ingredient_name";
    public static final String ARGUMENT_SYMPTOM_ID = "symptom_id";
    public static final String ARGUMENT_SYMPTOM_NAME = "symptom_name";

    public static final String ARGUMENT_INGREDIENT_LOG_ID_ARRAY = "ingredient_id_array";
    public static final String ARGUMENT_HOW_MANY_ID_IN_ARRAY =
            "how_many_ids";

    public static final String ARGUMENT_SYMPTOM_ID_ARRAY = "symptom_id_array";
    public static final String ARGUMENT_CURRENT_INDEX_IN_ARRAY = "current_index_in_array";
    public static final String ARGUMENT_SYMPTOM_LOG_ID_ARRAY = "symptom_log_id_array";
    public static final String ARGUMENT_SYMPTOM_LOG_ID_ARRAY_ORIGINAL =
            "symptom_log_id_array_original";
    public static final String ARGUMENT_SYMPTOM_ID_ARRAY_ORIGINAL =
            "symptom_log_id_array_original";

    public static final String ARGUMENT_INGREDIENT_LOG_ID_ARRAY_ORIGINAL =
            "ingredient_log_id_array_original";
    public static final String ARGUMENT_FOOD_LOG_ID_ARRAY = "food_log_id_array";
    public static final String ARGUMENT_FOOD_LOG_ID_ARRAY_ORIGINAL =
            "food_log_id_array_original";
    public static final String ARGUMENT_INGREDIENT_ID_ARRAY = "ingredient_id_array";
    public static final String ARGUMENT_RECIPE_ID_ARRAY = "recipe_id_array";

    public static final String ARGUMENT_CHANGE_SYMPTOM_LOG_ALL_INSTANTS_ALL_DAY = "change_symptom_log_all_instants_all_day";


    public static final String ARGUMENT_NEXT = "next";


    // check if we want to export as a csv or pdf or what
    public static final String ARGUMENT_CSV = "csv";
    public static final String ARGUMENT_PDF = "pdf";


    // action to take given this argument
    public static final String ARGUMENT_ACTION = "action";
    // actions to take
    public static final String ARGUMENT_ACTION_DUPLICATE = "action_duplicate";
    public static final String ARGUMENT_ACTION_EDIT = "action_edit";
    public static final String ARGUMENT_ACTION_ADD = "action_new";
    public static final String ARGUMENT_ACTION_DELETE = "action_delete";
    public static final String ARGUMENT_ACTION_DETAIL = "action_detail";



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
    public static final String ARGUMENT_ACTIVITY_FROM_NEW_SYMPTOM_LOG = "from_new_symptom_log";





    // fragments to go to
    public static final String ARGUMENT_GO_TO_NAME = "go_to_name";
    public static final String ARGUMENT_GO_TO_DATE_TIME_CHOICES = "go_to_date_time_choices";
    public static final String ARGUMENT_GO_TO_PART_OF_DAY = "go_to_part_of_day";
    public static final String ARGUMENT_GO_TO_BRAND = "go_to_brand";
    public static final String ARGUMENT_GO_TO_SPECIFIC_DATE = "go_to_specific_date";
    public static final String ARGUMENT_GO_TO_SPECIFIC_TIME = "go_to_specific_time";
    public static final String ARGUMENT_GO_TO_LIST_SYMPTOM_LOG =
            "go_to_list_symptom_log";
    public static final String ARGUMENT_GO_TO_LIST_INGREDIENT_LOG =
            "go_to_list_ingredient_log";
    public static final String ARGUMENT_GO_TO_CHOOSE_SYMPTOM =
            "go_to_choose_symptom";
    public static final String ARGUMENT_GO_TO_CHOOSE_INGREDIENT =
            "go_to_choose_ingredient";
    public static final String ARGUMENT_GO_TO_ADD_SYMPTOM =
            "go_to_add_symptom";
    public static final String ARGUMENT_GO_TO_ADD_INGREDIENT =
            "go_to_add_ingredient";
    public static final String ARGUMENT_GO_TO_ADD_SYMPTOM_LOG =
            "go_to_add_symptom_log";
    public static final String ARGUMENT_GO_TO_ADD_INGREDIENT_LOG =
            "go_to_add_ingredient_log";



    public static final String ARGUMENT_GO_TO_DELETE_FOOD_LOG = "go_to_delete_food_log";
    public static final String ARGUMENT_GO_TO_DETAIL_FOOD_LOG = "go_to_delete_food_log";

    public static final String ARGUMENT_GO_TO_EDIT_FOOD_LOG =
            "go_to_edit_food_log";
    public static final String ARGUMENT_GO_TO_EDIT_SYMPTOM_LOG =
            "go_to_edit_symptom_log";
    public static final String ARGUMENT_GO_TO_EDIT_SYMPTOM =
            "go_to_edit_symptom";
    public static final String ARGUMENT_GO_TO_EDIT_RECIPE =
            "go_to_edit_recipe";
    public static final String ARGUMENT_GO_TO_EDIT_INGREDIENT =
            "go_to_edit_ingredient";
    public static final String ARGUMENT_GO_TO_SYMPTOM_INTENSITY = "go_to_symptom_intensity";
    public static final String ARGUMENT_GO_TO_INGREDIENT_AMOUNT = "go_to_ingredient_amount";
    public static final String ARGUMENT_GO_TO_INGREDIENT_BRAND = "go_to_ingredient_amount";


    // which fragment we're from, places we might be from options
    public static final String ARGUMENT_FROM_INGREDIENT_NAME = "from_ingredient_name";
    public static final String ARGUMENT_FROM_SPECIFIC_TIME = "from_specific_time";
    public static final String ARGUMENT_FROM_SPECIFIC_DATE = "from_specific_date";
    public static final String ARGUMENT_FROM_DATE_TIME_CHOICES = "from_date_time_choices";
    public static final String ARGUMENT_FROM_INGREDIENT_BRAND = "from_ingredient_brand";
    public static final String ARGUMENT_FROM_PART_OF_DAY = "from_part_of_day";
    public static final String ARGUMENT_FROM_EDIT = "from_edit";
    public static final String ARGUMENT_FROM_INTENSITY_SYMPTOM = "from_intensity_symptom";



    // argument for what the UUID of the food log is
    public static final String ARGUMENT_FOOD_LOG_ID = "food_log_id";


    // which value do we want to change
    public static final String ARGUMENT_CHANGE = "change";
    // values as options to change
    public static final String ARGUMENT_CHANGE_INGREDIENT_CONSUMED = "change_ingredient_consumed";
    public static final String ARGUMENT_CHANGE_INGREDIENT_COOKED = "change_ingredient_cooked";
    public static final String ARGUMENT_CHANGE_INGREDIENT_ACQUIRED = "change_ingredient_acquired";
    public static final String ARGUMENT_CHANGE_INGREDIENT_ALL_INSTANTS =
            "change_ingredient_all_instants";
    public static final String ARGUMENT_CHANGE_INGREDIENT_BRAND = "change_ingredient_brand";
    public static final String ARGUMENT_CHANGE_INGREDIENT_AMOUNT = "change_ingredient_amount";


    public static final String ARGUMENT_CHANGE_SYMPTOM_ALL_INSTANTS =
            "change_symptom_all_instants";
    public static final String ARGUMENT_CHANGE_SYMPTOM_BEGIN = "change_symptom_begin";
    public static final String ARGUMENT_CHANGE_SYMPTOM_CHANGED = "change_symptom_changed";
    public static final String ARGUMENT_CHANGE_SYMPTOM_INTENSITY = "change_symptom_intensity";


    public static final String ARGUMENT_DONE_OR_UNFINISHED = "done_or_unfinished";
    public static final String ARGUMENT_DONE = "done";
    public static final String ARGUMENT_UNFINISHED = "unfinished";

    public static final String ARGUMENT_ONLY_SET = "only_set";
    public static final String ARGUMENT_ONLY_SET_TIME = "only_set_time";
    public static final String ARGUMENT_ONLY_SET_DATE = "only_set_date";

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


    // clean a string that was turned from an ArrayList into a string
    // for bundling between activities
    public static String cleanArrayString(String paramString){
        String mString = paramString;
        // remove first and last brackets if it has them
        mString = mString.replaceAll("\\[", "");
        mString = mString.replaceAll("\\]", "");
        // and remove any whitespace
        mString = mString.replaceAll("\\s", "");
        return mString;
    }

    // clean the bundled string and return an Array List
    public static ArrayList<String> cleanBundledStringIntoArrayList(String paramStringToCleanIntoArrayList){

        ArrayList<String> mStringArray = new ArrayList<String>();

        // remove brackets and spaces
        String mCleanedSplitString =
                Util.cleanArrayString(paramStringToCleanIntoArrayList);

        // make it into an array for each ID
        // double checked, this works fine even if there is only one value and no comma
        for (String cleanString : mCleanedSplitString.split(",")) {
            mStringArray.add(cleanString);
        }

        return mStringArray;

    }

    // set whether the bundle contains ingredient log id or symptom log id, etc
    public static String[] setLogIdArrayTypeAndStringFromBundle(Bundle paramBundle) {
        String stringFromBundle = null;
        String stringTypeFromBundle = null;

        if ( paramBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY) != null ) {
            stringTypeFromBundle = Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY;
        } else if (paramBundle.getString(Util.ARGUMENT_FOOD_LOG_ID_ARRAY) != null){
            stringTypeFromBundle = Util.ARGUMENT_FOOD_LOG_ID_ARRAY;
        } else if (paramBundle.getString(Util.ARGUMENT_INGREDIENT_ID_ARRAY) != null){
            stringTypeFromBundle = Util.ARGUMENT_INGREDIENT_ID_ARRAY;
        } else if (paramBundle.getString(Util.ARGUMENT_RECIPE_ID_ARRAY) != null){
            stringTypeFromBundle = Util.ARGUMENT_RECIPE_ID_ARRAY;
        } else if (paramBundle.getString(Util.ARGUMENT_SYMPTOM_ID_ARRAY) != null){
            stringTypeFromBundle = Util.ARGUMENT_SYMPTOM_ID_ARRAY;
        }
        stringFromBundle = paramBundle.getString(stringTypeFromBundle);

        String[] strings = new String[]{};
        strings[0] = stringTypeFromBundle;
        strings[1] = stringFromBundle;
        return strings;
    }
    // clean the bundled string and return an Array List
    public static ArrayList<String> setLogIdArrayFromBundle(Bundle paramBundle){

        ArrayList<String> mIdStringArray = new ArrayList<String>();
        String stringFromBundle = setLogIdArrayTypeAndStringFromBundle(paramBundle)[1];
        // remove brackets and spaces
        mIdStringArray =
                Util.cleanBundledStringIntoArrayList(stringFromBundle);

        return mIdStringArray;

    }



    // make substring bold and italic and regular font
    public static SpannableStringBuilder setBoldItalicPlainSpan(String boldString,
                                                             String notBoldString,
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

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(boldString);
        // this is the location of the substring that you want to make bold
        int start = 0;
        int end = start + boldString.length();
        // use exclusive to say we want this substring bold
        spannableStringBuilder.setSpan(Util.boldStyle, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // append whatever else you want
        spannableStringBuilder.append(notBoldString);

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

        return spannableStringBuilder;

    }

    public static SpannableStringBuilder setBoldSpan(String boldString, String notBoldString) {

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(boldString);
        // this is the location of the substring that you want to make bold
        int start = 0;
        int end = start + boldString.length();
        // use exclusive to say we want this substring bold
        spannableStringBuilder.setSpan(Util.boldStyle, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // append whatever else you want
        spannableStringBuilder.append(notBoldString);

        return spannableStringBuilder;

    }


    public static SpannableStringBuilder setBoldItalicSpan(String boldString, String notBoldString,
                                                           String italicString) {

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(boldString);
        // this is the location of the substring that you want to make bold
        int start = 0;
        int end = start + boldString.length();
        // use exclusive to say we want this substring bold
        spannableStringBuilder.setSpan(Util.boldStyle, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // append whatever else you want
        spannableStringBuilder.append(notBoldString);

        // if we have more strings to use, add them
        if ( !TextUtils.isEmpty(italicString) ) {
            spannableStringBuilder.append(italicString,
                    Util.italicStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

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
    // given integers of hours and minutes return string pretty
    public static String setTimeString(int hour, int min) {
        // Append in a StringBuilder
        String aTime = new StringBuilder()
                .append(hour)
                .append(':')
                .append( Util.setMinutesString(
                        min ) )
                .toString();
        return aTime;
    }
    // given integers of hours and minutes return string pretty
    public static String setTimeStringWithUnderscore(int hour, int min) {
        // Append in a StringBuilder
        String aTime = new StringBuilder()
                .append(hour)
                .append('_')
                .append( Util.setMinutesString(
                        min ) )
                .toString();
        return aTime;
    }
    public static String setDateString(int day, int month, int year) {

        // turn a zero indexed number for month into the name of that month
        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                "Sept", "Oct", "Nov", "Dec"};

        String aDate = new StringBuilder()
                .append(months[month])
                .append(" ")
                .append(day)
                .append(", ")
                .append(year)
                .toString();
        return aDate;
    }
    public static String setDateStringWithUnderscore(int day, int month, int year) {

        // turn a zero indexed number for month into the name of that month
        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                "Sept", "Oct", "Nov", "Dec"};

        String aDate = new StringBuilder()
                .append(months[month])
                .append("_")
                .append(day)
                .append("_")
                .append(year)
                .toString();
        return aDate;
    }

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

        Calendar mCalendar = GregorianCalendar.from(instant.atZone( defaultZoneId )) ;

        // the day of the week for readability
        String[] days = new String[] { "Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat" };
        String mDayOfWeekString = days[mCalendar.get(Calendar.DAY_OF_WEEK) - 1];

        // set the date to be with a space and a comma and with the short month name
        String mDateString = stringDateFromCalendar(mCalendar);

        // set time to have a colon and a zero if the minute is < 10
        String mTimeString = stringTimeFromCalendar(mCalendar);


        return(mTimeString + " " + mDayOfWeekString + ", " + mDateString);
    }

    //given an instant in time, display it pretty
    public static String stringForFileNameFromInstant(Instant instant){

        Calendar mCalendar = GregorianCalendar.from(instant.atZone( defaultZoneId )) ;

        // set the date to be with a space and a comma and with the short month name
        String mDateString = stringDateFromCalendarWithUnderscore(mCalendar);

        // set time to have a colon and a zero if the minute is < 10
        String mTimeString = stringTimeFromCalendarWithUnderscore(mCalendar);

        return(mTimeString + "_" + mDateString);
    }

    //given calendar return a string with only the date and time set
    public static String stringDateFromCalendar(Calendar calendar){

        // date related
        int mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int mMonth = calendar.get(Calendar.MONTH);
        int mYear = calendar.get(Calendar.YEAR);
        // set the date to be with a space and a comma and with the short month name
        String mDateString = setDateString(mDayOfMonth, mMonth, mYear);

        return mDateString;
    }
    //given calendar return a string with only the date and time set
    public static String stringDateFromCalendarWithUnderscore(Calendar calendar){

        // date related
        int mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int mMonth = calendar.get(Calendar.MONTH);
        int mYear = calendar.get(Calendar.YEAR);
        // set the date to be with a space and a comma and with the short month name
        String mDateString = setDateStringWithUnderscore(mDayOfMonth, mMonth, mYear);

        return mDateString;
    }
    public static String stringTimeFromCalendar(Calendar calendar){
        // get the hour and minute
        Integer mHour = calendar.get(Calendar.HOUR_OF_DAY);
        Integer mMinute = calendar.get(Calendar.MINUTE);
        // and set them to have a colon and a zero if the minute is < 10
        String mTimeString = setTimeString(mHour, mMinute);

        return mTimeString;
    }
    public static String stringTimeFromCalendarWithUnderscore(Calendar calendar){
        // get the hour and minute
        Integer mHour = calendar.get(Calendar.HOUR_OF_DAY);
        Integer mMinute = calendar.get(Calendar.MINUTE);
        // and set them to have a colon and a zero if the minute is < 10
        String mTimeString = setTimeStringWithUnderscore(mHour, mMinute);

        return mTimeString;
    }

    // calculate how recently the parameter instant was from the second instant parameter
    // and make it a pretty string
    public static String stringRelativeTimeFromInstant(Instant printThisInstant,
                                                       Instant printRelativeToThisInstant){

        // the answer string for displaying the difference pretty
        String relativityToInstant;

        // find the number of days between the two instants
        int totalDaysApart = integerRelativeDateFromInstant(printThisInstant,
                printRelativeToThisInstant);
        // now the hours
        int totalHoursApart = integerRelativeTimeFromInstant(printThisInstant,
                printRelativeToThisInstant);


        // if the year and month and day are the same, today
        // if the year and month and day is one day before, yesterday
        // then it's day before yesterday
        // then it's by number of days before yesterday
        // TODO fix this to get printed dates from strings, not here, can't get translated
        if ( totalDaysApart == 0 ){
            relativityToInstant = "Same day";
            if (Math.abs(totalHoursApart) == 1){
                relativityToInstant = "Lasted " + Math.abs(totalHoursApart) + " hour";
            } else if (totalHoursApart != 0){
                relativityToInstant = "Lasted " + Math.abs(totalHoursApart) + " hours";
            }
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
    public static Integer integerRelativeDateFromInstant(Instant printThisInstant,
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

    public static Integer integerRelativeTimeFromInstant(Instant printThisInstant,
                                                         Instant printRelativeToThisInstant){

        Integer totalHoursApart = 0;

        Calendar printThisCalendar = GregorianCalendar.from(
                printThisInstant.atZone( defaultZoneId )
        ) ;
        Calendar printRelativeToThisCalendar = GregorianCalendar.from(
                printRelativeToThisInstant.atZone( defaultZoneId )
        ) ;

        Integer totalDaysApart = Util.integerRelativeDateFromInstant(printThisInstant,
                printRelativeToThisInstant);


        int olderHour = printRelativeToThisCalendar.get(Calendar.HOUR_OF_DAY);
        int newerHour = printThisCalendar.get(Calendar.HOUR_OF_DAY);

        // if it happened only on day of
        if (totalDaysApart==0){
            // get the later in day time minus earlier in day hour
            totalHoursApart = newerHour - olderHour;
        } else {
            // it lasted longer than a day, so add the hours for number of days minus what time
            // of day
            totalHoursApart = totalDaysApart*24 + newerHour - olderHour;
        }

        return totalHoursApart;
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
    public static Instant instantFromDurationAndStartInstant(Instant instantStart,
                                                             Duration duration){

        return instantStart.plus(duration);
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
            if ( TextUtils.equals(whereGoTo, Util.ARGUMENT_GO_TO_EDIT_FOOD_LOG)) {
                mIntent = new Intent(activity, EditActivity.class);
                // where we want to go next
                mIntent.putExtra(Util.ARGUMENT_GO_TO, whereGoTo);
                // which fragment we came from
                mIntent.putExtra(Util.ARGUMENT_FROM, whereFrom);
            } else {
                // default to guessing we're making a new food log
               // mIntent = new Intent(activity, NewFoodLogActivity.class);
                // where we want to go next
                mIntent.putExtra(Util.ARGUMENT_GO_TO, whereGoTo);
                // which fragment we came from
                mIntent.putExtra(Util.ARGUMENT_FROM, whereFrom);
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

    public static Bundle setBundleFromArrayInfo(ArrayList<String> array, String stringType,
                                                String originalStringType){
        Bundle bundle = new Bundle();

        // put info about the array to add
        String arrayString = array.toString();
        bundle.putString(stringType, arrayString);

        // also how big the array is for ease of coding,
        // TODO remove this and replace with .size
        //  () in the activity or something more efficient
        Integer arraySize = array.size();
        String stringArraySize = String.valueOf(arraySize);
        bundle.putString(Util.ARGUMENT_HOW_MANY_ID_IN_ARRAY, stringArraySize);
        // to calculate when we've done all the elements in the array
        String arrayIndexString = String.valueOf(arraySize - 1);
        bundle.putString(ARGUMENT_CURRENT_INDEX_IN_ARRAY, arrayIndexString);

        // original array, TODO also replace this inefficiency with better array usage or something
        bundle.putString(originalStringType, arrayString);

        return bundle;
    }

    public static Bundle setNewIngredientLogBundle(ArrayList<String> newLogIdStringArray){

        Bundle bundle = new Bundle();

        // set our new bundle to be an ingredient log
        bundle = setNewBundle(newLogIdStringArray, ARGUMENT_GO_TO_INGREDIENT_AMOUNT,
                ARGUMENT_INGREDIENT_LOG_ID_ARRAY, ARGUMENT_INGREDIENT_LOG_ID_ARRAY_ORIGINAL);

        return bundle;
    }


    // bundle the relevant information between fragments for adding new symptom log
    public static Bundle setNewSymptomLogBundle(String idTypeString,
                                                ArrayList<String> newLogIdStringArray){

        // set our new bundle to be a symptom log
        Bundle bundle = setNewBundle(newLogIdStringArray, ARGUMENT_GO_TO_SYMPTOM_INTENSITY,
                idTypeString, ARGUMENT_SYMPTOM_ID_ARRAY_ORIGINAL);

        return bundle;
    }

    // bundle the relevant information between fragments for adding something new
    public static Bundle setNewBundle(ArrayList<String> newStringArray, String whereToGo,
                                      String arrayType, String arrayTypeOriginal){

        Bundle bundle = new Bundle();

        // set that we still have things to set
        bundle.putString(ARGUMENT_DONE_OR_UNFINISHED, ARGUMENT_UNFINISHED);
        bundle.putString(ARGUMENT_ACTION, ARGUMENT_ACTION_ADD);
        // where we want to go next
        bundle.putString(Util.ARGUMENT_GO_TO, whereToGo);

        // set information about the array into the bundle
        bundle = setBundleFromArrayInfo(newStringArray,
                arrayType, arrayTypeOriginal);

        return bundle;
    }

    // bundle the relevant information between fragments for editing something already existing
    public static Bundle setEditBundle(ArrayList<String> array, String whereToGo,
                                      String arrayType, String arrayTypeOriginal){

        Bundle bundle = new Bundle();

        // set the basics
        bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_EDIT);
        bundle.putString(ARGUMENT_ACTION, ARGUMENT_ACTION_EDIT);
        // where we want to go next
        bundle.putString(Util.ARGUMENT_GO_TO, whereToGo);
        // set that we still have things to set
        bundle.putString(ARGUMENT_DONE_OR_UNFINISHED, ARGUMENT_UNFINISHED);

        // set information about the array into the bundle
        bundle = setBundleFromArrayInfo(array, arrayType, arrayTypeOriginal);

        return bundle;
    }

    // if we're from edit, then set to done, else we're not done yet so change nothing
    public static Bundle setDoneIfFromEdit( Bundle bundle ){

        // we've only changed the intensity or amount or one of the middle (not last) of
        // ingredient log or symptom log etc, so if we're adding a new object, we
        // don't want to be done yet but to go to the next fragment. But if we're from
        // edit, we do want to go back to edit now. Set the next bundle to that info
        // given where we're from
        // if it's from edit then we also only had one to set so it's fine this is before
        // current id

        if ( TextUtils.equals(bundle.getString(ARGUMENT_FROM), ARGUMENT_FROM_EDIT ) ) {

            // can consume something that has only just now been cooked or acquired
            //TODO make logic for invalid edit if the instant is a wrong time
            // but can't consume something that was acquired later
            // if from edit, don't allow to set cooked or consumed to be an earlier time than
            // acquired
            // also can't edit a symptom to change or end before it's begun

            bundle.putString(ARGUMENT_DONE_OR_UNFINISHED, ARGUMENT_DONE);
            // could also add which we're going to now

        }


        return bundle;
    }

    // set to done
    public static Bundle setDone( Bundle bundle ){

        bundle.putString(ARGUMENT_DONE_OR_UNFINISHED, ARGUMENT_DONE);

        return bundle;
    }


    // bundle the relevant information between fragments for adding new symptom log
    public static Bundle setEditSymptomLogBundle(String logIdString,
                                                 String whatToEdit){

        Bundle bundle = new Bundle();

        // where we want to go next
        String whereNext = null;
        bundle.putString(ARGUMENT_CHANGE, whatToEdit);
        // if we're changing one of the time based parameters
        if ( TextUtils.equals(whatToEdit, ARGUMENT_CHANGE_SYMPTOM_BEGIN)
                || TextUtils.equals(whatToEdit, ARGUMENT_CHANGE_SYMPTOM_CHANGED) ){
            whereNext = ARGUMENT_GO_TO_DATE_TIME_CHOICES;
        } else if ( TextUtils.equals(whatToEdit, ARGUMENT_CHANGE_SYMPTOM_INTENSITY)) {
            // we're changing intensity or symptom if it's not a time
            whereNext = ARGUMENT_GO_TO_SYMPTOM_INTENSITY;
        } else {
            // only symptom is left
            whereNext = ARGUMENT_GO_TO_CHOOSE_SYMPTOM;
        }

        // in edit we only have one id in our array, so make it now and pass it in
        ArrayList<String> array = new ArrayList<>();
        array.add(logIdString);
        bundle = setEditBundle(array, whereNext,
                ARGUMENT_SYMPTOM_LOG_ID_ARRAY,
                ARGUMENT_SYMPTOM_LOG_ID_ARRAY_ORIGINAL);


        return bundle;
    }

    // bundle the relevant information between fragments for editing an ingredient log
    public static Bundle setEditIngredientLogBundle(String logIdString,
                                                 String whatToEdit){

        Bundle bundle = new Bundle();

        // where we want to go next
        String whereNext = null;
        bundle.putString(ARGUMENT_CHANGE, whatToEdit);
        // if we're changing one of the time based parameters
        if ( TextUtils.equals(whatToEdit, ARGUMENT_CHANGE_INGREDIENT_CONSUMED)
                || TextUtils.equals(whatToEdit, ARGUMENT_CHANGE_INGREDIENT_COOKED)
                || TextUtils.equals(whatToEdit, ARGUMENT_CHANGE_INGREDIENT_ACQUIRED)
        ){
            whereNext = ARGUMENT_GO_TO_DATE_TIME_CHOICES;
        }
        else if ( TextUtils.equals(whatToEdit, ARGUMENT_CHANGE_INGREDIENT_AMOUNT)) {
            // we're changing amount or brand or ingredient if it's not a time
            whereNext = ARGUMENT_GO_TO_INGREDIENT_AMOUNT;
        }
        else if ( TextUtils.equals(whatToEdit, ARGUMENT_CHANGE_INGREDIENT_BRAND)) {
            // we're changing brand if it's not a time or amount
            whereNext = ARGUMENT_GO_TO_INGREDIENT_BRAND;
        }
        else {
            // only ingredient is left
            whereNext = ARGUMENT_GO_TO_CHOOSE_INGREDIENT;
        }

        // in edit we only have one id in our array, so make it now and pass it in
        ArrayList<String> array = new ArrayList<>();
        array.add(logIdString);
        bundle = setEditBundle(array, whereNext,
                ARGUMENT_INGREDIENT_LOG_ID_ARRAY,
                ARGUMENT_INGREDIENT_LOG_ID_ARRAY_ORIGINAL);


        return bundle;
    }
    // bundle the relevant information between fragments for adding new symptom log
    public static Bundle setNewIngredientLogBundleFromArray(ArrayList<String> newLogIdStringArray){

        Bundle bundle = new Bundle();
        // where we want to go next
        bundle.putString(Util.ARGUMENT_GO_TO, Util.ARGUMENT_GO_TO_INGREDIENT_AMOUNT);
        // set that we still have things to set
        bundle.putString(ARGUMENT_DONE_OR_UNFINISHED, ARGUMENT_UNFINISHED);

        //set the array string of symptom log Ids to the bundle
        String newLogIdString = newLogIdStringArray.toString();
        bundle.putString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY, newLogIdString );

        // also how big the array is for ease of coding, TODO remove this and replace with .size
        //  () in the activity or something more efficient
        Integer logIdStringArraySize = newLogIdStringArray.size();
        String logIdStringArraySizeIndexString = String.valueOf(logIdStringArraySize - 1);
        String newLogIdStringArraySize = String.valueOf(logIdStringArraySize);
        bundle.putString(Util.ARGUMENT_HOW_MANY_ID_IN_ARRAY,
                newLogIdStringArraySize);

        // original array, TODO also replace this inefficiency with better array usage or something
        bundle.putString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY_ORIGINAL, newLogIdString);

        return bundle;
    }



    // bundle the relevant information between fragments for log instant
    public static Bundle setBundleGivenIntegersOfDateTime ( Integer minute, Integer hour,
                                                            Integer day, Integer month,
                                                            Integer year){
        // get the instant of the log
        // which here should be the instant the name was saved
        // or a specific time if they chose that first and then came here
        Bundle integerBundleToSetDateTime = new Bundle();
        integerBundleToSetDateTime.putInt(Util.ARGUMENT_MONTH, month);
        integerBundleToSetDateTime.putInt(Util.ARGUMENT_YEAR, year);
        integerBundleToSetDateTime.putInt(Util.ARGUMENT_DAY, day);
        integerBundleToSetDateTime.putInt(Util.ARGUMENT_HOUR, hour);
        integerBundleToSetDateTime.putInt(Util.ARGUMENT_MINUTE, minute);

        return integerBundleToSetDateTime;
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

    // set numberpicker values
    public static NumberPicker setNumberPicker(NumberPicker numberPicker,
                                               String[] displayStringList, Integer defaultValue,
                                               Color defaultValueColor){
        // the lowest number in the list, then minus one
        // to give us the index to set the numberpicker with
        Integer minimumIntegerIndex = Integer.parseInt(displayStringList[0]) - 1;
        // get the last in the list by getting the length, minus one to make an index
        Integer indexForLastValueInStringList = displayStringList.length - 1;
        // parse the last number in the list, then minus one to give an index
        Integer maximumIntegerIndex =
                Integer.parseInt(displayStringList[indexForLastValueInStringList]) - 1;

        // make a number picker for the intensity of the symptom
        numberPicker.setDisplayedValues(displayStringList);
        numberPicker.scrollBy(minimumIntegerIndex,maximumIntegerIndex);
        numberPicker.setMinValue(minimumIntegerIndex);
        numberPicker.setMaxValue(maximumIntegerIndex);
        numberPicker.setValue(defaultValue-1);
        numberPicker.setWrapSelectorWheel(false);
        // TODO this is a lot of parsing and converting, is colorstatelist really better than color?
        //numberPicker.setBackgroundColor(Color.parseColor(String.valueOf(defaultValueColor)));
        return numberPicker;
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

    public static Boolean isIntegerBetween1to10(Integer paramInteger) {
        Boolean answer = Boolean.FALSE;

        if (paramInteger != 1 ||
                paramInteger != 2 ||
                paramInteger != 3 ||
                paramInteger != 4 ||
                paramInteger!= 5 ||
                paramInteger != 6 ||
                paramInteger != 7 ||
                paramInteger != 8 ||
                paramInteger != 9 ||
                paramInteger != 10 ) {
            answer = Boolean.TRUE;
        } else {
            answer = Boolean.FALSE;
        }

        return answer;
    }



















    ////////////////////////////////////////////////////////
    // start commonly used functions //////////
    ////////////////////////////////////////////////////////

    public static void startNextFragmentBundleChange(Activity paramThisActivity,
                                                     FragmentTransaction paramFragmentTransaction,
                                               int paramFragmentContainerView,
                                               Fragment paramNextFragment, Bundle paramBundle,
                                               String whatToChange ) {

        paramBundle.putString(Util.ARGUMENT_CHANGE, whatToChange);

        startNextFragmentBundle(paramThisActivity, paramFragmentTransaction,
                paramFragmentContainerView,
                paramNextFragment, paramBundle);
    }
    public static void startNextFragmentBundle(Activity paramThisActivity,
                                               FragmentTransaction paramFragmentTransaction,
                                         int paramFragmentContainerView,
                                         Fragment paramNextFragment, Bundle bundle) {

        // set the data to pass along info
        // given from the previous fragment
        // or the duplicated ID if we did that
        paramNextFragment.setArguments(bundle);


        // actually go to the next place now
        // based on from edit or not and if we're done
        if ( TextUtils.equals(
                    bundle.getString(ARGUMENT_ACTION), ARGUMENT_ACTION_EDIT)
                    && TextUtils.equals(ARGUMENT_DONE_OR_UNFINISHED, ARGUMENT_DONE)
            ) {
                String[] logIdArrayStringAndType = setLogIdArrayTypeAndStringFromBundle(bundle);
                String logIdArrayType = logIdArrayStringAndType[0];
                String logIdArrayString = logIdArrayStringAndType[1];

                Util.goToEditActivityActionTypeId(null, paramThisActivity,
                        Util.ARGUMENT_ACTION_EDIT,
                        logIdArrayType, logIdArrayString);

        }
        else if ( TextUtils.equals(ARGUMENT_DONE_OR_UNFINISHED, ARGUMENT_DONE) ){
            // we're done and not from edit, so go to list
            String[] logIdArrayStringAndType = setLogIdArrayTypeAndStringFromBundle(bundle);
            String logIdArrayType = logIdArrayStringAndType[0];
            String logIdArrayString = logIdArrayStringAndType[1];

            Util.goToListActivityTypeId(null, paramThisActivity,
                    logIdArrayType, logIdArrayString);

        } else {

            // we weren't from edit or done so go to the next fragment
            startNextFragment(paramFragmentTransaction, paramFragmentContainerView, paramNextFragment);
        }
    }
    public static void startNextFragment(FragmentTransaction paramFragmentTransaction,
                                         int paramFragmentContainerView,
                                         Fragment paramNextFragment ) {

        paramFragmentTransaction
                .replace(paramFragmentContainerView,
                        paramNextFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    public static void startNextFragmentActionChangeIdArray(FragmentTransaction paramFragmentTransaction,
                                         int paramFragmentContainerView, Fragment paramNextFragment,
                                                            String paramActionString,
                                                            String paramChangeString,
                                                            String paramIdTypeString,
                                                            String paramIdString ) {
        Bundle bundle = new Bundle();
        // set action, duplicate or edit or delete
        bundle.putString(ARGUMENT_ACTION, paramActionString);
        // set what we want to change, acquired, consumed, began, changed etc
        bundle.putString(ARGUMENT_CHANGE, paramChangeString);
        // set whether we're food log or symptom log etc type, and then also the id array itself
        bundle.putString(paramIdTypeString, paramIdString);

        // put the arguments into where we're going next
        paramNextFragment.setArguments(bundle);
        // go there
        startNextFragment(paramFragmentTransaction, paramFragmentContainerView, paramNextFragment);
    }
    public static void startNextActivityActionChangeIdArray(Activity paramThisActivity,
                                                            Class paramNextActivityClass,
                                                            String paramActionString,
                                                            String paramChangeString,
                                                            String paramIdTypeString,
                                                            String paramIdString ) throws ClassNotFoundException {

        Intent intent = new Intent(paramThisActivity.getApplicationContext(),
                paramNextActivityClass);
        // set action, duplicate or edit or delete
        intent.putExtra(ARGUMENT_ACTION, paramActionString);
        // set what we want to change, acquired, consumed, began, changed etc
        intent.putExtra(ARGUMENT_CHANGE, paramChangeString);
        // set whether we're food log or symptom log etc type, and then also the id array itself
        intent.putExtra(paramIdTypeString, paramIdString);

        // go there
        paramThisActivity.startActivity(intent);
    }

    //make edit or next fragment/activity for add new


    // check if we were from edit or not, go back to edit screen or to list activity
    public static void goToListOrEditActivity(Context context, Activity activity, Bundle bundle){

        // if there is an action in the bundle
        if ( bundle.containsKey(ARGUMENT_ACTION)){
            String whatAction = bundle.getString(ARGUMENT_ACTION);
            // check if that action is edit
            if ( TextUtils.equals(whatAction, ARGUMENT_ACTION_EDIT ) ) {
                // check if we're done
                if ( TextUtils.equals(bundle.getString(ARGUMENT_DONE_OR_UNFINISHED),
                        ARGUMENT_DONE ) ) {
                    goToEditSymptomLogOrIngredientLogActivity(context, activity, bundle);
                } else {
                    // we're not finished yet, so go to the next activity or fragment
                }
            } else {

            }
        } else {
            // if no action was given it can't be edit, so go to list
            goToListSymptomLogOrIngredientLogActivity(context, activity, bundle);
        }

    }

    // check the bundle to get the log id array key and then go to the right list activity
    public static void goToListSymptomLogOrIngredientLogActivity(Context context, Activity
                                                                 activity, Bundle bundle){

        //if it's symptom log go to that
        if ( bundle.containsKey(bundle.getString(ARGUMENT_SYMPTOM_LOG_ID_ARRAY))) {
            goToListSymptomLogActivity(context, activity, bundle.getString(ARGUMENT_SYMPTOM_LOG_ID_ARRAY));
        } else {
            // ingredient log then
            goToListIngredientLogActivity(context, activity, bundle.getString(ARGUMENT_INGREDIENT_LOG_ID_ARRAY));
        }
    }

    // check the bundle to get the log id array key and then go to the right list activity
    public static void goToEditSymptomLogOrIngredientLogActivity(Context context, Activity
            activity, Bundle bundle){
        //if it's symptom log go to that
        if ( bundle.containsKey(bundle.getString(ARGUMENT_SYMPTOM_LOG_ID_ARRAY))) {
            goToEditSymptomLogActivity(context, activity,
                    bundle.getString(ARGUMENT_SYMPTOM_LOG_ID_ARRAY));
        } else {
            // ingredient log then
            goToEditIngredientLogActivity(context, activity,
                    bundle.getString(ARGUMENT_INGREDIENT_LOG_ID_ARRAY));
        }
    }

    // make going to each of the activities easier
    public static void goToListSymptomLogActivity(Context context, Activity activity,
                                                  String stringId){
            goToActivityTypeIdClass(context, activity, ARGUMENT_SYMPTOM_LOG_ID_ARRAY, stringId,
                    ListSymptomLogActivity.class, null, null, null);

    }
    public static void goToMainActivity(Context context, Activity activity){
        goToActivityTypeIdClass(context, activity, null, null, MainActivity.class, null, null, null);
    }

    // go to the list activity given the type (symptom, etc) to deal with and its id string
    public static void goToListActivityTypeId(Context context, Activity activity,
                                              String idStringType,
                                              String idString){
        Class whereGoTo = null;

        if ( TextUtils.equals(idStringType, Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY) ) {
            whereGoTo = ListSymptomLogActivity.class;
        }
        else if ( TextUtils.equals(idStringType, Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY) ) {
            whereGoTo = ListIngredientLogActivity.class;
        }

        goToActivityTypeIdClass(context, activity, idStringType, idString, whereGoTo, null, null, null);

    }



    // go to the choose ingredient activity
    public static void goToChooseIngredientActivity(Context context, Activity activity) {
        goToActivityTypeIdClass(context, activity, null, null, ChooseIngredientActivity.class,
                null, null, null);

    }

    public static String setWhatToChangeFromBundle(Bundle bundle){
        String whatToChange = null;

        if (bundle.containsKey(ARGUMENT_CHANGE)){
            whatToChange = bundle.getString(ARGUMENT_CHANGE);
        }

        return whatToChange;
    }

    public static Boolean setGoToEditFromBundle(Bundle bundle){
        Boolean goBackToEdit = Boolean.FALSE;
        // if there was an action given in the bundle
        if ( bundle.containsKey(ARGUMENT_ACTION) ) {
            // and if that action was edit
            if ( TextUtils.equals(bundle.getString(Util.ARGUMENT_ACTION), ARGUMENT_ACTION_EDIT)){
                // then we were from edit, so we'll check this to only set one value and go back
                // to edit
                goBackToEdit = Boolean.TRUE;
            }
        }
        return goBackToEdit;
    }

    // if any of the values given aren't null, set them in the intent given and return intent
    public static Intent setIntent(Intent intent, String fragmentGoToString, String idStringType,
                                   String idString, String actionString, Bundle bundle){

        if ( bundle != null ) {
            //we were given a bundle so set that
            intent.putExtras(bundle);
        }

        // if there's a fragment to go to, like we're going to edit and then on to edit symptom
        if (fragmentGoToString != null) {
            intent.putExtra(Util.ARGUMENT_GO_TO,
                    fragmentGoToString);
        }

        // type is symptom or food log or symptom log or ingredient or recipe
        // not necessarily going to list with any specific logs in mind
        if (idString != null && idStringType != null) {
            intent.putExtra(idStringType, idString);
        }

        // needed for duplicate or edit
        if (actionString != null) {
            intent.putExtra(Util.ARGUMENT_ACTION, actionString);
        }


        return intent;
    }

    // shenanigans to make it work no matter if coming from a fragment, activity, or view holder
    public static void goToActivityTypeIdClass(Context context, Activity activity,
                                               String idStringType,
                                               String idString, Class activityClassToGoTo,
                                               String fragmentGoToString, String actionString,
                                               Bundle bundle){

        if ( activity != null ) {

            context = activity.getApplicationContext();
            Activity useForIntent = activity;
            Activity useForStartActivity = activity;

            Intent intent = new Intent(useForIntent, activityClassToGoTo);
            // if any of our given values aren't null, set them in the intent
            intent = setIntent(intent, fragmentGoToString, idStringType, idString, actionString,
                    bundle);

            useForStartActivity.startActivity(intent);
        } else {
            // this half appears to work between:
            // MainActivity to List Ing or List Sym

           // Class activityClassToGoTo = setClassActivity(idStringType, whereTo);
            Context useForIntent = context;
            Context useForStartActivity = context;

            Intent intent = new Intent(useForIntent, activityClassToGoTo);
            // if any of our given values aren't null, set them in the intent
            intent = setIntent(intent, fragmentGoToString, idStringType, idString, actionString,
                    bundle);

            useForStartActivity.startActivity(intent);
        }


    }


    public static void goToChooseActivityType(Context context,
                                              String idStringType){

        Intent intent = null;
        String whereGoTo = null;

        // go to the right list activity based on what type of id string we have
        if ( TextUtils.equals(idStringType, Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY) ) {
            intent = new Intent(context, ChooseSymptomActivity.class);
        }
        else if ( TextUtils.equals(idStringType, Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY) ) {
           intent = new Intent(context, ChooseIngredientActivity.class);
        }
        else if ( TextUtils.equals(idStringType,
                Util.ARGUMENT_INGREDIENT_ID_ARRAY) ) {
            //TODO make the list activities
//            intent = new Intent(context, ListIngredientActivity.class);
        }
        else if ( TextUtils.equals(idStringType,
                Util.ARGUMENT_SYMPTOM_ID_ARRAY) ) {
//            intent = new Intent(context, ListSymptomActivity.class);
        }
        else if ( TextUtils.equals(idStringType,
                Util.ARGUMENT_RECIPE_ID_ARRAY) ) {
//            intent = new Intent(context, ListRecipeActivity.class);
        }

        // type is symptom or food log or symptom log or ingredient or recipe
        context.startActivity(intent);
    }

    public static void goToEditIngredientLogActivity(Context context, Activity activity,
                                                     String idString){

        goToActivityTypeIdClass(context, activity, ARGUMENT_INGREDIENT_LOG_ID_ARRAY, idString,
                EditActivity.class, null, null, null);
    }

    public static void goToEditSymptomLogActivity(Context context, Activity activity,
                                                     String idString){

        goToActivityTypeIdClass(context, activity, ARGUMENT_SYMPTOM_LOG_ID_ARRAY, idString,
                EditActivity.class, null, null, null);
    }

    public static void goToChooseSymptomActivity(Context context, Activity activity) {
        goToActivityTypeIdClass(context, activity, null, null, ChooseSymptomActivity.class, null,
                null, null);
    }

    public static void goToOtherActivity(Context context, Activity activity) {
        goToActivityTypeIdClass(context, activity, null, null, OtherActivity.class, null, null, null);
    }

    //pass in null for string id if there's none to highlight or do anything with
    public static void goToListIngredientLogActivity(Context context, Activity activity,
                                                     String stringId) {
        goToActivityTypeIdClass(context, activity, ARGUMENT_INGREDIENT_LOG_ID_ARRAY, stringId,
                ListIngredientLogActivity.class, null, null, null);
    }

    // go to the edit activity with our id and what type of action and array we're using
    public static void goToEditActivityActionTypeId(Context context, Activity activity,
                                                    String actionString,
                                        String idStringType,
                                        String idString){
        goToActivityTypeIdClass(context, activity, idStringType, idString, EditActivity.class,
                null, actionString, null);

    }
    
    public static void goToDetailActivity(Context context, String whatAction,
                                          String idStringType,
                                          String idString){

        goToActivityTypeIdClass(context, null, idStringType, idString, DetailActivity.class,
                null, whatAction, null);
    }


    // make going to each of the activities easier
    public static void goToAddSymptomLogActivity(Context context, Activity activity,
                                                  Bundle bundle){
        goToActivityTypeIdClass(context, activity, ARGUMENT_SYMPTOM_ID_ARRAY, null,
                NewSymptomLogActivity.class, null, null, bundle);

    }

    public static void goToAddIngredientLog(Activity paramActivity,
                                            String idString, Bundle bundle){

        goToActivityTypeIdClass(null, paramActivity, ARGUMENT_INGREDIENT_ID_ARRAY,
                idString, NewIngredientLogActivity.class,
                ARGUMENT_GO_TO_ADD_INGREDIENT_LOG, ARGUMENT_ACTION_ADD, bundle);
    }

















    ////////////////////////////////////////////////////////
    // set which part of food log to change //////////
    ////////////////////////////////////////////////////////

    public static LocalDateTime getDateTimeFromChange(String whatToChange,
                                                      IngredientLog ingredientLog,
                                                      SymptomLog symptomLog) {

        Instant mInstant = null;

        if ( ingredientLog != null ) {
            if (TextUtils.equals(whatToChange, Util.ARGUMENT_CHANGE_INGREDIENT_CONSUMED)) {
                mInstant = ingredientLog.getInstantConsumed();
            } else if (TextUtils.equals(whatToChange,
                    Util.ARGUMENT_CHANGE_INGREDIENT_COOKED)) {
                mInstant = ingredientLog.getInstantCooked();
            } else if (TextUtils.equals(whatToChange,
                    Util.ARGUMENT_CHANGE_INGREDIENT_ACQUIRED)) {
                mInstant = ingredientLog.getInstantAcquired();
            } else {
                // this is for all the changed instants, leaving as an else in case I add more
                // types
                mInstant = ingredientLog.getInstantConsumed();
            }
        }
        else if ( symptomLog != null ) {
            if (TextUtils.equals(whatToChange,
                    Util.ARGUMENT_CHANGE_SYMPTOM_BEGIN)) {
                mInstant = symptomLog.getInstantBegan();
            } else if (TextUtils.equals(whatToChange,
                    Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED)) {
                mInstant = symptomLog.getInstantChanged();
            } else {
                mInstant = symptomLog.getInstantBegan();
            }
        }

        return Util.localDateTimeFromInstant(mInstant);
    }

    public static Bundle setBundleFromLocalDateTime(LocalDateTime localDateTime){
        Bundle bundle = new Bundle();

        bundle.putInt(ARGUMENT_MINUTE, localDateTime.getMinute());
        bundle.putInt(ARGUMENT_HOUR, localDateTime.getHour());
        bundle.putInt(ARGUMENT_DAY, localDateTime.getDayOfMonth());
        bundle.putInt(ARGUMENT_MONTH, localDateTime.getMonthValue()-1);
        bundle.putInt(ARGUMENT_YEAR, localDateTime.getYear());

        return bundle;
    }

    public static Instant setFirstLogInstant(String whatToChange,
                                             IngredientLogViewModel ingredientLogViewModel,
                                             SymptomLogViewModel SymptomLogViewModel,
                                             Bundle bundle ){
        Instant firstInstant = null;
        if ( TextUtils.equals(whatToChange, Util.ARGUMENT_CHANGE_SYMPTOM_BEGIN) ) {

            firstInstant =
                    Util.getFirstInstant(null,
                            SymptomLogViewModel.viewModelGetSymptomLogFromLogId(
                                    UUID.fromString(Util.cleanBundledStringIntoArrayList(
                                            bundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY)).get(0)
                                    )
                            ));
        } else if ( TextUtils.equals(whatToChange, Util.ARGUMENT_CHANGE_INGREDIENT_COOKED) ) {
            // get the new first instant corresponding to our newly set ingredient log instant
            firstInstant = ingredientLogViewModel.viewModelGetIngredientLogFromLogId(
                    UUID.fromString( Util.cleanBundledStringIntoArrayList(bundle.getString(
                            Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY)).get(0)
                    )
            ).getInstantCooked();
        } else {
            // get the new first instant corresponding to our newly set ingredient log instant
            firstInstant =
                    Util.getFirstInstant(ingredientLogViewModel.viewModelGetIngredientLogFromLogId(
                            UUID.fromString(Util.cleanBundledStringIntoArrayList(
                                    bundle.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY)).get(0)
                            )
                    ), null);
        }
        return firstInstant;
    }

    // pass in null for values you want to keep same as instant
    public static Instant setInstantFromLogAndIntegers(SymptomLogViewModel symptomLogViewModel,
                                                        SymptomLog symptomLog,
                                                        IngredientLogViewModel ingredientLogViewModel,
                                                        IngredientLog ingredientLog,
                                                        String whatToChange,
                                                        Integer minute,
                                                        Integer hour,
                                                        Integer day,
                                                        Integer month, Integer year) {

        Instant instant = null;
        if (symptomLog != null) {
            if ( TextUtils.equals(whatToChange, ARGUMENT_CHANGE_SYMPTOM_BEGIN) ) {
                instant = symptomLog.getInstantBegan();
            } else if ( TextUtils.equals(whatToChange, ARGUMENT_CHANGE_SYMPTOM_CHANGED)){
                instant = symptomLog.getInstantChanged();
            } else {
                instant = symptomLog.getInstantBegan();
            }
        } else if ( ingredientLog != null ){
            if ( TextUtils.equals(whatToChange, ARGUMENT_CHANGE_INGREDIENT_CONSUMED) ) {
                instant = ingredientLog.getInstantConsumed();
            } else if ( TextUtils.equals(whatToChange, ARGUMENT_CHANGE_INGREDIENT_COOKED)){
                instant = ingredientLog.getInstantCooked();
            } else if ( TextUtils.equals(whatToChange, ARGUMENT_CHANGE_INGREDIENT_ACQUIRED)){
                instant = ingredientLog.getInstantAcquired();
            } else {
                instant = ingredientLog.getInstantConsumed();
            }

        }
        instant = setInstantFromIntegers(instant, minute, hour, day, month, year);
        return instant;
    }

    // pass in null for values you want to keep same as instant
    public static Instant setInstantFromIntegers( Instant instant,
                                                        Integer minute,
                                                        Integer hour,
                                                        Integer day,
                                                        Integer month, Integer year) {

        if ( minute != null ) {
            instant.with(ChronoField.MINUTE_OF_DAY, minute);
        }
        if ( hour != null ) {
            instant.with(ChronoField.HOUR_OF_DAY, hour);
        }
        if ( day != null ) {
            instant.with(ChronoField.DAY_OF_MONTH, day);
        }
        if ( month != null ) {
            instant.with(ChronoField.MONTH_OF_YEAR, month-1);
        }
        if ( year != null ) {
            instant.with(ChronoField.YEAR, year);
        }
            return instant;
    }
    // update the instant to match what was given in the bundle
    public static Instant setInstantFromBundle (Instant logInstant, Bundle
    integerBundleToSetDateTime){

        // given an instant and a bundle with at least some integers of minute, hour, month, day, year
        LocalDateTime localDateTime = null;
        localDateTime = Util.localDateTimeFromInstant(logInstant);
        Integer monthInteger = localDateTime.getMonthValue()-1;
        Integer yearInteger = localDateTime.getYear();
        Integer minuteInteger = localDateTime.getMinute();
        Integer dayInteger = localDateTime.getDayOfMonth();
        Integer hourInteger = localDateTime.getHour();


        // check in the bundle for each of the date time values
        // if it contains the value
        if ( integerBundleToSetDateTime.containsKey(Util.ARGUMENT_MONTH) ) {
            // set the value from the bundle
            monthInteger = integerBundleToSetDateTime.getInt(Util.ARGUMENT_MONTH);
        }
        if ( integerBundleToSetDateTime.containsKey(Util.ARGUMENT_YEAR) ) {
            yearInteger = integerBundleToSetDateTime.getInt(Util.ARGUMENT_YEAR);
        }
        if ( integerBundleToSetDateTime.containsKey(Util.ARGUMENT_MINUTE) ) {
            minuteInteger = integerBundleToSetDateTime.getInt(Util.ARGUMENT_MINUTE);
        }
        if ( integerBundleToSetDateTime.containsKey(Util.ARGUMENT_DAY) ) {
            dayInteger = integerBundleToSetDateTime.getInt(Util.ARGUMENT_DAY);
        }
        if ( integerBundleToSetDateTime.containsKey(Util.ARGUMENT_HOUR) ) {
            hourInteger = integerBundleToSetDateTime.getInt(Util.ARGUMENT_HOUR);
        }

        localDateTime =
                localDateTime.withDayOfMonth(dayInteger).withMonth(monthInteger+1).withYear(yearInteger)
                        .withMinute(minuteInteger).withHour(hourInteger);

        return Util.instantFromLocalDateTime(localDateTime);
    }


    public static Bundle setBundleLogToNextInstant(Bundle bundle){
        String whatToChange = null;

        // if there is a key with something to change
        if ( bundle.containsKey(ARGUMENT_CHANGE) ) {
            // set what we need to change to that
            whatToChange = bundle.getString(ARGUMENT_CHANGE);
        } else {
            // if no key was given, set to the first thing to change for that object
            if ( bundle.containsKey(ARGUMENT_INGREDIENT_LOG_ID_ARRAY)){
                whatToChange = ARGUMENT_CHANGE_INGREDIENT_CONSUMED;
            } else {
                whatToChange = ARGUMENT_CHANGE_SYMPTOM_BEGIN;
            }
        }

        // start with consumed, then ask for cooked, then ask for acquired for ingredient log
        // or start with begin and then changed for symptom log
        if (whatToChange == ARGUMENT_CHANGE_INGREDIENT_CONSUMED) {
            bundle.putString(ARGUMENT_CHANGE, ARGUMENT_CHANGE_INGREDIENT_COOKED);
        }
        else if (whatToChange == ARGUMENT_CHANGE_INGREDIENT_COOKED) {
            bundle.putString(ARGUMENT_CHANGE, ARGUMENT_CHANGE_INGREDIENT_ACQUIRED);
        }
        else if (whatToChange == Util.ARGUMENT_CHANGE_INGREDIENT_ACQUIRED) {
            bundle.putString(ARGUMENT_CHANGE, ARGUMENT_CHANGE_INGREDIENT_BRAND);
        }
        else if (whatToChange == ARGUMENT_CHANGE_INGREDIENT_BRAND) {
            bundle.putString(ARGUMENT_CHANGE, ARGUMENT_CHANGE_INGREDIENT_AMOUNT);
        }
        else if (whatToChange == ARGUMENT_CHANGE_INGREDIENT_AMOUNT) {
            bundle.remove(ARGUMENT_CHANGE);

        }
        else if (whatToChange == ARGUMENT_CHANGE_SYMPTOM_INTENSITY) {
            bundle.putString(ARGUMENT_CHANGE, ARGUMENT_CHANGE_SYMPTOM_BEGIN);
        }
        else if (whatToChange == ARGUMENT_CHANGE_SYMPTOM_BEGIN) {
            bundle.putString(ARGUMENT_CHANGE, ARGUMENT_CHANGE_SYMPTOM_CHANGED);
        }
        else if (whatToChange == ARGUMENT_CHANGE_SYMPTOM_CHANGED) {
            bundle.remove(ARGUMENT_CHANGE);
        }

        return bundle;
    }

    // return a symptom log with the new began or changed instants set
    public static SymptomLog setSymptomLogBeganChanged(String whatToChange,
                                                       SymptomLog symptomLog,
                                                       Bundle integerBundleToSetDateTime){

        if (TextUtils.equals(whatToChange, Util.ARGUMENT_CHANGE_SYMPTOM_ALL_INSTANTS)) {
            // get the instant we have already so we have correct date
            // change began at time
            Instant instantToSet = Util.setInstantFromBundle(symptomLog.getInstantBegan(),
                    integerBundleToSetDateTime);
            symptomLog.setInstantBegan(instantToSet);
            // then change when the symptom changed/ended instant
            instantToSet = Util.setInstantFromBundle(symptomLog.getInstantChanged(),
                    integerBundleToSetDateTime);
            symptomLog.setInstantChanged(instantToSet);
        } else if (TextUtils.equals(whatToChange, Util.ARGUMENT_CHANGE_SYMPTOM_BEGIN)) {
            // get the instant we have already so we have correct date
            Instant instantToSet = Util.setInstantFromBundle(symptomLog.getInstantBegan(),
                    integerBundleToSetDateTime);
            symptomLog.setInstantBegan(instantToSet);
        }else if (TextUtils.equals(whatToChange, Util.ARGUMENT_CHANGE_SYMPTOM_LOG_ALL_INSTANTS_ALL_DAY)) {
            // get the instant we have already so we have correct date
            Instant instantToSet = Util.setInstantFromBundle(symptomLog.getInstantBegan(),
                    integerBundleToSetDateTime);
            symptomLog.setInstantBegan(instantToSet);
            // using the begin instant and our preset latest hour, set changed/ended to that
            Instant latestInstantInDay = Util.setInstantFromIntegers(instantToSet, null,
                    latest_hour, null, null, null);
            symptomLog.setInstantChanged(latestInstantInDay);
        }
        else {
            Instant instantToSet = Util.setInstantFromBundle(symptomLog.getInstantChanged(),
                    integerBundleToSetDateTime);
            symptomLog.setInstantChanged(instantToSet);
        }

        //then set the values from the symptom log
        //then set the values from the food log
        return symptomLog;
    }

    // return a food log with the instant of when consumed or acquired or cooked happened
    public static IngredientLog setIngredientLogInstants(String whatToChange,
                                                         IngredientLog ingredientLog,
                                                         Bundle integerBundleToSetDateTime) {

        Instant instant;

        if (TextUtils.equals(whatToChange, Util.ARGUMENT_CHANGE_INGREDIENT_ALL_INSTANTS)) {

            // using the bundle and instant we want to change, set the localdatetime to that
            instant = Util.setInstantFromBundle(ingredientLog.getInstantConsumed(),
                    integerBundleToSetDateTime);
            // and set that fixed value back in the food log
            ingredientLog.setInstantConsumed(instant);

            // now the next one
            instant = Util.setInstantFromBundle(ingredientLog.getInstantCooked(),
                    integerBundleToSetDateTime);
            ingredientLog.setInstantCooked(instant);

            instant = Util.setInstantFromBundle(ingredientLog.getInstantAcquired(),
                    integerBundleToSetDateTime);
            ingredientLog.setInstantAcquired(instant);
        }
        else if (TextUtils.equals(whatToChange, Util.ARGUMENT_CHANGE_INGREDIENT_CONSUMED)) {

            // using the bundle and instant we want to change, set the localdatetime to that
            instant = Util.setInstantFromBundle(ingredientLog.getInstantConsumed(),
                    integerBundleToSetDateTime);

            // and set that fixed value back in the food log
            ingredientLog.setInstantConsumed(instant);
        }
        else if (TextUtils.equals(whatToChange, Util.ARGUMENT_CHANGE_INGREDIENT_COOKED)) {
            instant = Util.setInstantFromBundle(ingredientLog.getInstantCooked(),
                    integerBundleToSetDateTime);
            ingredientLog.setInstantCooked(instant);
        }
        else if (TextUtils.equals(whatToChange, Util.ARGUMENT_CHANGE_INGREDIENT_ACQUIRED)) {
            instant = Util.setInstantFromBundle(ingredientLog.getInstantAcquired(),
                    integerBundleToSetDateTime);
            ingredientLog.setInstantAcquired(instant);
        }
        return ingredientLog;
    }


    public static Bundle setSymptomLogInstants(String whatToChange,
                                       ArrayList<SymptomLog> symptomLogArray,
                                               SymptomViewModel symptomViewModel,
                                             SymptomLogViewModel symptomLogViewModel,
                                             Bundle integerBundleToSetDateTime, Bundle bundleNext){

        // for each string in array update that log's instant began
        for (SymptomLog symptomLog: symptomLogArray){

            // TODO have logic to check if it's a type of symptom that can't be
            //  instantaneous, like a migraine, or must be instantaneous, like vomiting

            // set that date and time to either began or changed (determined by whatToChange)
            symptomLog = Util.setSymptomLogBeganChanged(whatToChange, symptomLog,
                    integerBundleToSetDateTime);
            // put our updated log into the database
            symptomLogViewModel.viewModelUpdateSymptomLog(symptomLog);
        }

        // return the bundle that now has been reset to get the next instant
        return Util.setBundleLogToNextInstant(bundleNext);

    }

    public static Instant getFirstInstant(IngredientLog ingredientLog, SymptomLog symptomLog){
        Instant firstInstant = null;
        if ( ingredientLog != null ){
            firstInstant = ingredientLog.getInstantConsumed();
        }
        else {
            firstInstant = symptomLog.getInstantBegan();
        }
        return firstInstant;
    }

    public static Bundle setLogInstants(String whatToChange,
                                        ArrayList<IngredientLog> ingredientLogArray,
                                        ArrayList<SymptomLog> symptomLogArray,
                                        IngredientLogViewModel ingredientLogViewModel,
                                        SymptomLogViewModel symptomLogViewModel,
                                        IngredientViewModel ingredientViewModel,
                                        SymptomViewModel symptomViewModel,
                                        LocalDateTime dateTime, Bundle bundleNext){

        // only one will be given in to set its instants
        if ( ingredientLogViewModel != null ){
            // if we're setting ingredient logs
            bundleNext = Util.setIngredientLogInstants(whatToChange, ingredientLogArray,
                    ingredientLogViewModel, Util.setBundleFromLocalDateTime(dateTime),
                    bundleNext);

        } else if ( symptomLogViewModel != null ) {
            // if setting symptom logs
            bundleNext = Util.setSymptomLogInstants(whatToChange, symptomLogArray,
                    symptomViewModel, symptomLogViewModel,
                    Util.setBundleFromLocalDateTime(dateTime),
                    bundleNext);
        }

        return bundleNext;
    }


    public static Bundle setIngredientLogInstants(String whatToChange,
                                                           ArrayList<IngredientLog> ingredientLogArray,
                                                           IngredientLogViewModel ingredientLogViewModel,
                                                           Bundle integerBundleToSetDateTime,
                                                   Bundle bundleNext){
        // for each string in array update that log's instant began
        for (IngredientLog ingredientLog: ingredientLogArray){
            //then set the values from the food log
            ingredientLog = setIngredientLogInstants(whatToChange, ingredientLog,
                    integerBundleToSetDateTime);
            ingredientLogViewModel.viewModelUpdateIngredientLog(ingredientLog);
        }
        //done with for loop, set that we've changed what we needed to


        //TODO either here or don't go in to set bundle, ask user if food was cooked and acquired
        // at same time as most recent food log
        // (i.e. if they're putting sushi in and this is setting fish,
        // set rice acquired and cooked to same as fish food log)
        return Util.setBundleLogToNextInstant(bundleNext);

    }

    public static String setFileName(String fileType) {
        // set name of file
        StringBuilder myFileName = new StringBuilder("diet_decoder_database_");
        // include the current instant of time
        Instant nowInstant = Instant.now();
        String nowDayString = Util.stringForFileNameFromInstant(nowInstant);
        myFileName.append(nowDayString);
        // TODO append on file name with their name

        // set the end of the file name to be for csv or pdf
        if (fileType == ARGUMENT_CSV) {
            myFileName.append(".txt");
        } else if (fileType == ARGUMENT_PDF) {
            myFileName.append(".pdf");
        }

        return myFileName.toString();
    }

    public static SpannableStringBuilder setViewHolderRecyclerViewStringNameDescription(String title,
                                                                         String description) {

        String boldString = title;
        String notBoldString = description;

        return Util.setBoldSpan(boldString, notBoldString);
    }

    public static SpannableStringBuilder setViewHolderRecyclerViewString(String title, String subtitle,
                                                                         String severity,
                                                                         String unImportantString) {

        String boldString = "";
        String notBoldString = "";

        // bind the name and the time the food was eaten to the recyclerview item
        // leave out brand if it isn't named
        if ( TextUtils.isEmpty(subtitle)) {
            boldString = title;
            if (!TextUtils.isEmpty(severity)) {
                notBoldString = notBoldString
                        .concat("\n").concat(severity).concat("/10");
            }
        }
        else {
            boldString = title;
            notBoldString = notBoldString.concat("\n").concat(subtitle);
            if (!TextUtils.isEmpty(severity)) {
                            notBoldString = notBoldString.concat("\n").concat(severity).concat(
                                    "/10");
            }
        }

        return Util.setBoldItalicSpan(boldString, notBoldString, unImportantString);
    }

    public static String setAcquiredString(String string){
        String italicString = "\n";
        return italicString.concat("Acquired: ").concat(
                string);
    }

    public static String setCookedString(String string){
        String notItalicString = "\n";
        return notItalicString.concat("Cooked: ").concat(
                string);
    }

    public static String setDescriptionString(String string){
        // only display if not empty
        String notItalicString = "";
        if (!TextUtils.isEmpty(string)) {
            notItalicString = "\n";
            notItalicString.concat("Description: ").concat(
                    string);
        }
        return notItalicString;
    }
    public static String setPlainDescriptionString(String description){
        // nothing at first so if we don't have a description there's not a random new line
        String plainString = description;

        // only display if not empty
        if (!TextUtils.isEmpty(description)) {

            // we have some description, so let's make a newline after the title
            plainString = "\n";

            // then add our description
            plainString = plainString.concat(description);
        }
        return plainString;
    }
    public static String setSeverityString(String string){
        // set the string out of 5 on the scale
        String severityString = "";

        if (string != null) {
            severityString = severityString.concat("Severity: ").concat(
                    string).concat("/10");
        }
        return severityString;
    }

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////




}