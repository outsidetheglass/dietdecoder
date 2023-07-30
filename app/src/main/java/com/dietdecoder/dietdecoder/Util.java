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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dietdecoder.dietdecoder.activity.DetailActivity;
import com.dietdecoder.dietdecoder.activity.DateTimeChoicesFragment;
import com.dietdecoder.dietdecoder.activity.PartOfDayFragment;
import com.dietdecoder.dietdecoder.activity.SpecificDateTimeFragment;
import com.dietdecoder.dietdecoder.activity.MainActivity;
import com.dietdecoder.dietdecoder.activity.OtherActivity;
import com.dietdecoder.dietdecoder.activity.EditActivity;
import com.dietdecoder.dietdecoder.activity.ingredient.AddIngredientActivity;
import com.dietdecoder.dietdecoder.activity.ingredientlog.ChooseIngredientActivity;
import com.dietdecoder.dietdecoder.activity.ingredientlog.IngredientAmountFragment;
import com.dietdecoder.dietdecoder.activity.ingredientlog.ListIngredientLogActivity;
import com.dietdecoder.dietdecoder.activity.ingredientlog.AddIngredientLogActivity;
import com.dietdecoder.dietdecoder.activity.symptomlog.ChooseSymptomActivity;
import com.dietdecoder.dietdecoder.activity.symptomlog.ListSymptomLogActivity;
import com.dietdecoder.dietdecoder.activity.symptomlog.AddSymptomLogActivity;
import com.dietdecoder.dietdecoder.activity.symptomlog.SymptomIntensityFragment;
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
    public static final String ARGUMENT_BEFORE = "before";
    public static final String ARGUMENT_AFTER = "after";

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

    public static final String ARGUMENT_INGREDIENT_ID_ARRAY = "ingredient_id_array";
    public static final String ARGUMENT_RECIPE_ID_ARRAY = "recipe_id_array";

    public static final String ARGUMENT_CHANGE_SYMPTOM_LOG_ALL_INSTANTS_ALL_DAY = "change_symptom_log_all_instants_all_day";

    public static final String ARGUMENT_QUESTION = "question";
    public static final String ARGUMENT_CHANGE_JUST_NOW = "change_just_now";

    public static final String ARGUMENT_FILTER = "filter";

    public static final String ARGUMENT_NEXT = "next";


    // check if we want to export as a csv or pdf or what
    public static final String ARGUMENT_CSV = "csv";
    public static final String ARGUMENT_PDF = "pdf";


    // action to take given this argument
    public static final String ARGUMENT_ACTION = "action";
    // actions to take
    public static final String ARGUMENT_ACTION_DUPLICATE = "action_duplicate";
    public static final String ARGUMENT_ACTION_EDIT = "action_edit";
    public static final String ARGUMENT_ACTION_ADD = "action_add";
    public static final String ARGUMENT_ACTION_DELETE = "action_delete";
    public static final String ARGUMENT_ACTION_DETAIL = "action_detail";



    // which activity or fragment we are from
    public static final String ARGUMENT_FROM = "from";
    // fragment or activity to go to given this argument
    public static final String ARGUMENT_GO_TO = "go_to";

    public static final String ARGUMENT_FROM_ADD_INGREDIENT_ACTIVITY =
            "from_add_ingredient_activity";
    public static final String ARGUMENT_FROM_DELETE_INGREDIENT_ACTIVITY =
            "from_delete_ingredient_activity";
    public static final String ARGUMENT_FROM_DETAIL_INGREDIENT_ACTIVITY =
            "from_detail_ingredient_activity";
    public static final String ARGUMENT_FROM_EDIT_INGREDIENT_ACTIVITY =
            "from_edit_ingredient_activity";
    public static final String ARGUMENT_FROM_INGREDIENT_ACTIVITY = "from_ingredient_activity";

    public static final String ARGUMENT_FROM_ADD_INGREDIENT_LOG_ACTIVITY =
            "from_add_ingredient_log_activity";
    public static final String ARGUMENT_FROM_CHOOSE_INGREDIENT_ACTIVITY =
            "from_choose_ingredient_activity";
    public static final String ARGUMENT_FROM_INGREDIENT_AMOUNT_FRAGMENT = "from_ingredient_amount_fragment";
    public static final String ARGUMENT_FROM_EDIT_INGREDIENT_LOG_ACTIVITY =
            "from_edit_ingredient_log_activity";

    public static final String ARGUMENT_FROM_ADD_RECIPE_ACTIVITY = "from_add_recipe_activity";
    public static final String ARGUMENT_FROM_EDIT_RECIPE_ACTIVITY = "from_new_recipe_activity";
    public static final String ARGUMENT_FROM_RECIPE_ACTIVITY = "from_recipe_activity";


    public static final String ARGUMENT_FROM_DETAIL_SYMPTOM_ACTIVITY =
            "from_detail_symptom_activity";
    public static final String ARGUMENT_FROM_SYMPTOM_ACTIVITY =
            "from_symptom_activity";


    public static final String ARGUMENT_FROM_ADD_SYMPTOM_LOG_ACTIVITY =
 "from_add_symptom_log_activity";
    public static final String ARGUMENT_FROM_CHOOSE_SYMPTOM_ACTIVITY =
            "from_choose_symptom_activity";
    public static final String ARGUMENT_FROM_EDIT_SYMPTOM_LOG_ACTIVITY =
            "from_edit_symptom_log_activity";
    public static final String ARGUMENT_FROM_LIST_SYMPTOM_LOG_ACTIVITY =
            "from_list_symptom_log_activity";
    public static final String ARGUMENT_FROM_SYMPTOM_INTENSITY_FRAGMENT = "from_intensity_symptom";


    public static final String ARGUMENT_FROM_DATE_TIME_CHOICES_FRAGMENT =
            "from_date_time_choices_fragment";
    public static final String ARGUMENT_FROM_DETAIL_ACTIVITY = "from_detail_activity";
    public static final String ARGUMENT_FROM_EDIT_ACTIVITY = "from_edit_activity";
    public static final String ARGUMENT_FROM_EXPORT_ACTIVITY = "from_export_activity";
    public static final String ARGUMENT_FROM_FIRST_START_WIZARD_ACTIVITY =
            "from_first_start_wizard_activity";
    public static final String ARGUMENT_FROM_MAIN_ACTIVITY = "from_main_activity";
    public static final String ARGUMENT_FROM_OTHER_ACTIVITY = "from_other_activity";
    public static final String ARGUMENT_FROM_PART_OF_DAY_FRAGMENT = "from_part_of_day";
    public static final String ARGUMENT_FROM_RESOURCES_ACTIVITY = "from_resources_activity";
    public static final String ARGUMENT_FROM_SPECIFIC_DATE_TIME_FRAGMENT =
            "from_specific_date_time_fragment";

    public static final String ARGUMENT_FROM_SPECIFIC_TIME = "from_specific_time";
    public static final String ARGUMENT_FROM_SPECIFIC_DATE = "from_specific_date";
    public static final String ARGUMENT_FROM_INGREDIENT_BRAND = "from_ingredient_brand";
    public static final String ARGUMENT_FROM_EDIT = "from_edit";




    public static final String ARGUMENT_GO_TO_INGREDIENT_AMOUNT_ACTIVITY = "go_to_ingredient_amount_activity";
    public static final String ARGUMENT_GO_TO_INGREDIENT_BRAND_ACTIVITY = "go_to_ingredient_brand_activity";
    public static final String ARGUMENT_GO_TO_ADD_INGREDIENT_ACTIVITY =
            "go_to_add_ingredient_activity";
    public static final String ARGUMENT_GO_TO_DELETE_INGREDIENT_ACTIVITY =
            "go_to_delete_ingredient_activity";
    public static final String ARGUMENT_GO_TO_DETAIL_INGREDIENT_ACTIVITY =
            "go_to_detail_ingredient_activity";
    public static final String ARGUMENT_GO_TO_EDIT_INGREDIENT_ACTIVITY =
            "go_to_edit_ingredient_activity";
    public static final String ARGUMENT_GO_TO_INGREDIENT_ACTIVITY = "go_to_ingredient_activity";

    public static final String ARGUMENT_GO_TO_ADD_INGREDIENT_LOG_ACTIVITY =
            "go_to_add_ingredient_log_activity";
    public static final String ARGUMENT_GO_TO_CHOOSE_INGREDIENT_ACTIVITY =
            "go_to_choose_ingredient_activity";
    public static final String ARGUMENT_GO_TO_INGREDIENT_AMOUNT_FRAGMENT = "go_to_ingredient_amount_fragment";
    public static final String ARGUMENT_GO_TO_EDIT_INGREDIENT_LOG_ACTIVITY =
            "go_to_edit_ingredient_log_activity";


    public static final String ARGUMENT_GO_TO_ADD_RECIPE_ACTIVITY = "go_to_add_recipe_activity";
    public static final String ARGUMENT_GO_TO_EDIT_RECIPE_ACTIVITY = "go_to_ew_recipe_activity";
    public static final String ARGUMENT_GO_TO_RECIPE_ACTIVITY = "go_to_recipe_activity";


    public static final String ARGUMENT_GO_TO_DETAIL_SYMPTOM_ACTIVITY =
            "go_to_detail_symptom_activity";
    public static final String ARGUMENT_GO_TO_SYMPTOM_ACTIVITY =
            "go_to_symptom_activity";


    public static final String ARGUMENT_GO_TO_ADD_SYMPTOM_LOG_ACTIVITY =
            "go_to_add_symptom_log_activity";
    public static final String ARGUMENT_GO_TO_CHOOSE_SYMPTOM_ACTIVITY =
            "go_to_choose_symptom_activity";
    public static final String ARGUMENT_GO_TO_EDIT_SYMPTOM_LOG_ACTIVITY =
            "go_to_edit_symptom_log_activity";
    public static final String ARGUMENT_GO_TO_LIST_SYMPTOM_LOG_ACTIVITY =
            "go_to_list_symptom_log_activity";
    public static final String ARGUMENT_GO_TO_SYMPTOM_INTENSITY_FRAGMENT =
            "go_to_symptom_intensity_fragment";


    public static final String ARGUMENT_GO_TO_DATE_TIME_CHOICES_FRAGMENT =
            "go_to_date_time_choices_fragment";
    public static final String ARGUMENT_GO_TO_DETAIL_ACTIVITY = "go_to_detail_activity";
    public static final String ARGUMENT_GO_TO_EDIT_ACTIVITY = "go_to_edit_activity";
    public static final String ARGUMENT_GO_TO_EXPORT_ACTIVITY = "go_to_export_activity";
    public static final String ARGUMENT_GO_TO_FIRST_START_WIZARD_ACTIVITY =
            "go_to_first_start_wizard_activity";
    public static final String ARGUMENT_GO_TO_MAIN_ACTIVITY =
            "go_to_main_activity";
    public static final String ARGUMENT_GO_TO_OTHER_ACTIVITY = "go_to_other_activity";
    public static final String ARGUMENT_GO_TO_PART_OF_DAY_FRAGMENT = "go_to_part_of_day";
    public static final String ARGUMENT_GO_TO_RESOURCES_ACTIVITY = "go_to_resources_activity";
    public static final String ARGUMENT_GO_TO_SPECIFIC_DATE_TIME_FRAGMENT =
            "go_to_specific_date_time_fragment";





    // which value do we want to change
    public static final String ARGUMENT_CHANGE = "change";
    // values as options to change
    public static final String ARGUMENT_CHANGE_INGREDIENT_LOG_CONSUMED = "change_ingredient_log_consumed";
    public static final String ARGUMENT_CHANGE_INGREDIENT_LOG_COOKED = "change_ingredient_log_cooked";
    public static final String ARGUMENT_CHANGE_INGREDIENT_LOG_ACQUIRED = "change_ingredient_log_acquired";
    public static final String ARGUMENT_CHANGE_INGREDIENT_LOG_ALL_INSTANTS =
            "change_ingredient_all_instants";
    public static final String ARGUMENT_CHANGE_INGREDIENT_LOG_BRAND = "change_ingredient_log_brand";
    public static final String ARGUMENT_CHANGE_INGREDIENT_LOG_AMOUNT = "change_ingredient_log_amount";


    public static final String ARGUMENT_CHANGE_SYMPTOM_LOG_ALL_INSTANTS =
            "change_symptom_all_instants";
    public static final String ARGUMENT_CHANGE_SYMPTOM_LOG_BEGIN = "change_symptom_log_begin";
    public static final String ARGUMENT_CHANGE_SYMPTOM_LOG_CHANGED = "change_symptom_log_changed";
    public static final String ARGUMENT_CHANGE_SYMPTOM_LOG_INTENSITY =
            "change_symptom_log_intensity";


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
                cleanArrayString(paramStringToCleanIntoArrayList);

        if ( paramStringToCleanIntoArrayList.contains(",")) {
            // make it into an array for each ID
            // double checked, this works fine even if there is only one value and no comma
            for (String cleanString : mCleanedSplitString.split(",")) {
                mStringArray.add(cleanString);
            }
        }

        return mStringArray;
    }

    // set whether the bundle contains ingredient log id or symptom log id, etc
    public static String[] setLogIdArrayTypeAndStringFromBundle(Bundle paramBundle) {
        String stringTypeFromBundle = setStringTypeBundle(paramBundle);
        String stringFromBundle = null;

        if ( paramBundle.containsKey(stringTypeFromBundle) ){
            stringFromBundle = paramBundle.getString(stringTypeFromBundle);
        } else if ( stringTypeFromBundle == null){
            Log.d(TAG, "Error, failed to have valid type.");
        } else {
            Log.d(TAG, "Error, failed to call to get strings from type.");
        }


        String[] strings = new String[]{stringTypeFromBundle, stringFromBundle};
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

    public static String setBeforeOrAfterFromArgument(String argument){

        String beforeOrAfterString = null;

        if (TextUtils.equals(Util.ARGUMENT_BEFORE, argument) ){
            beforeOrAfterString = Util.ARGUMENT_BEFORE;
        }

        return beforeOrAfterString;

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

    public static Integer hourFromInstant(Instant instant){
        return localDateTimeFromInstant(instant).getHour();
    }
    public static Integer yearFromInstant(Instant instant){
        return localDateTimeFromInstant(instant).getYear();
    }
    public static Integer dayOfYearFromInstant(Instant instant){
        return localDateTimeFromInstant(instant).getDayOfYear();
    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////
    // Base of making intent with fragment from and go to //
    ////////////////////////////////////////////////////////
    // start the intent to pass data along
    // and set which fragment we came from and where we want to go next
//    public static Intent intentChoicesButton(Activity activity, String whereGoTo,
//                                             String whereFrom) {
//        // start the intent to pass data along
//        Intent mIntent = null;
//        // if it doesn't exist, make it
//        if ( activity.getIntent() == null ) {
//            // if we want to go to edit or add
//            if ( isEditGoTo(whereGoTo) ) {
//                mIntent = new Intent(activity, EditActivity.class);
//                // where we want to go next
//                mIntent.putExtra(Util.ARGUMENT_GO_TO, whereGoTo);
//                // which fragment we came from
//                mIntent.putExtra(Util.ARGUMENT_FROM, whereFrom);
//            } else {
//                // default to guessing we're making a new food log
//               // mIntent = new Intent(activity, NewFoodLogActivity.class);
//                // where we want to go next
//                mIntent.putExtra(Util.ARGUMENT_GO_TO, whereGoTo);
//                // which fragment we came from
//                mIntent.putExtra(Util.ARGUMENT_FROM, whereFrom);
//            }
//        } else {
//            // activity already has an intent so use that one
//            mIntent = activity.getIntent();
//        }
//
//        return mIntent;
//    }

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
//    public static Intent intentWithFoodLogIdStringButton(FragmentActivity activity,
//                                                         String foodLogIdString,
//                                                         String fragmentGoTo, String fragmentFrom) {
//        // start the intent to pass data along
//        // and set which fragment we came from and where we want to go next
//        Intent mIntent = intentChoicesButton(activity, fragmentGoTo, fragmentFrom);
//
//        // set the id of the food log
//        // and put it in the intent so we can access it from future fragments
//        mIntent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, foodLogIdString);
//
//        // now we're ready to go back to activity this fragment is in
//        return mIntent;
//    }

    // same as above but for activities
//
//    public static Intent intentWithFoodLogIdStringButtonActivity(Activity activity,
//                                                         String foodLogIdString,
//                                                         String whereGoTo, String whereFrom) {
//        // start the intent to pass data along
//        // and set which fragment we came from and where we want to go next
//        Intent mIntent = intentChoicesButton(activity, whereGoTo, whereFrom);
//
//        // set the id of the food log
//        // and put it in the intent so we can access it from future fragments
//        mIntent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, foodLogIdString);
//
//        // now we're ready to go back to activity this fragment is in
//        return mIntent;
//    }
//
//

    // takes in food id, fragment from, fragment to go to, and what we want to change
//    public static Intent intentChangeWithFoodLogIdStringButton(FragmentActivity activity,
//                                                         String foodLogIdString,
//                                                         String fragmentGoTo,
//                                                               String fragmentFrom,
//                                                               String whatToChange) {
//        // start the intent to pass data along
//        // and set which fragment we came from and where we want to go next
//        Intent mIntent = intentChoicesButton(activity, fragmentGoTo, fragmentFrom);
//
//        // set the id of the food log
//        // and put it in the intent so we can access it from future fragments
//        mIntent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, foodLogIdString);
//        mIntent.putExtra(Util.ARGUMENT_CHANGE, whatToChange);
//
//        // now we're ready to go back to activity this fragment is in
//        return mIntent;
//    }

    //set a string and the id type string
    // given a bundle and the strings to set to based on which id type
    public static String[] setStringAndTypeByIdArrayFromBundleElseIfStrings(Bundle bundle, String ifSymptomLogString,
                                                                            String ifIngredientLogString, String ifSymptomString,
                                                                            String ifIngredientString, String ifRecipeString){

        String stringTypeFromBundle = null;
        String ifString = null;
        String[] typeAndIfString = null;

        if ( isSymptomLogBundle(bundle) ) {
            ifString = ifSymptomLogString;
            stringTypeFromBundle = ARGUMENT_SYMPTOM_LOG_ID_ARRAY;
        } else if ( isIngredientLogBundle(bundle) ){
            stringTypeFromBundle = ifIngredientLogString;
            ifString = ifSymptomLogString;
            stringTypeFromBundle = ARGUMENT_INGREDIENT_LOG_ID_ARRAY;

        } else if ( isIngredientBundle(bundle) ){
            ifString = ifIngredientString;
            stringTypeFromBundle = ARGUMENT_INGREDIENT_ID_ARRAY;

        } else if ( isRecipeBundle(bundle) ){
            ifString = ifRecipeString;
            stringTypeFromBundle = ARGUMENT_RECIPE_ID_ARRAY;

        } else if ( isSymptomBundle(bundle) ){
            ifString = ifSymptomString;
            stringTypeFromBundle = ARGUMENT_SYMPTOM_ID_ARRAY;
        }

        typeAndIfString[0] = stringTypeFromBundle;
        typeAndIfString[1] = ifString;
        return typeAndIfString;
    }

    public static String setStringByIdArrayFromBundleElseIfStrings(Bundle bundle, String ifSymptomLogString,
                                                                   String ifIngredientLogString, String ifSymptomString,
                                                                   String ifIngredientString, String ifRecipeString){
        setLogIdArrayFromBundle(bundle);

        String stringTypeFromBundle = null;

        if ( isSymptomLogBundle(bundle) ) {
            stringTypeFromBundle = ifSymptomLogString;

        } else if ( isIngredientLogBundle(bundle) ){
            stringTypeFromBundle = ifIngredientLogString;

        } else if ( isIngredientBundle(bundle) ){
            stringTypeFromBundle = ifIngredientString;

        } else if ( isRecipeBundle(bundle) ){
            stringTypeFromBundle = ifRecipeString;

        } else if ( isSymptomBundle(bundle) ){
            stringTypeFromBundle = ifSymptomString;
        }

        return stringTypeFromBundle;
    }
    public static Bundle setBundleFromArrayInfo(ArrayList<String> array, String stringType){
        Bundle bundle = new Bundle();

        // put info about the array to add
        String arrayString = array.toString();
        bundle.putString(stringType, arrayString);

        // how big the array is for ease of coding
        Integer arraySize = array.size();
        String stringArraySize = String.valueOf(arraySize);
        bundle.putString(Util.ARGUMENT_HOW_MANY_ID_IN_ARRAY, stringArraySize);

        // to calculate when we've done all the elements in the array
        String arrayIndexString = String.valueOf(arraySize - 1);
        bundle.putString(ARGUMENT_CURRENT_INDEX_IN_ARRAY, arrayIndexString);


        return bundle;
    }

    public static Bundle setNewIngredientLogBundle(ArrayList<String> newLogIdStringArray){

        // set our new bundle to be an ingredient log
        Bundle bundle = setNewBundle(newLogIdStringArray, ARGUMENT_GO_TO_INGREDIENT_AMOUNT_ACTIVITY,
                ARGUMENT_INGREDIENT_LOG_ID_ARRAY);

        return bundle;
    }


    // bundle the relevant information between fragments for adding new symptom log
    public static Bundle setNewSymptomLogFromSymptomIdBundle(
                                                ArrayList<String> newLogIdStringArray){

        // set our new bundle to be a symptom log
        Bundle bundle = setNewBundle(newLogIdStringArray, ARGUMENT_GO_TO_SYMPTOM_INTENSITY_FRAGMENT,
                ARGUMENT_SYMPTOM_ID_ARRAY);

        return bundle;
    }

    // bundle the relevant information between fragments for adding new symptom log
    public static Bundle setNewSymptomLogFromSymptomLogIdBundle(
                                                ArrayList<String> newLogIdStringArray){

        // set our new bundle to be a symptom log
        Bundle bundle = setNewBundle(newLogIdStringArray, ARGUMENT_GO_TO_SYMPTOM_INTENSITY_FRAGMENT,
                ARGUMENT_SYMPTOM_LOG_ID_ARRAY);

        return bundle;
    }

    // bundle the relevant information between fragments for adding something new
    public static Bundle setNewBundle(ArrayList<String> newStringArray, String whereToGo,
                                      String arrayType){
        Bundle bundle = new Bundle();

        // if we were given an array add it to bundle
        if ( newStringArray != null ) {
            // set information about the array into the bundle
            bundle = setBundleFromArrayInfo(newStringArray,
                    arrayType);
        }

        // set that we still have things to set
        bundle.putString(ARGUMENT_DONE_OR_UNFINISHED, ARGUMENT_UNFINISHED);
        bundle.putString(ARGUMENT_ACTION, ARGUMENT_ACTION_ADD);
        // where we want to go next
        bundle.putString(Util.ARGUMENT_GO_TO, whereToGo);


        return bundle;
    }
    public static Bundle setSearchBundle(String searchString){

        Bundle searchBundle = new Bundle();
        searchBundle.putString(Util.ARGUMENT_FILTER, searchString);

        return searchBundle;
    }

    // bundle the relevant information between fragments for editing something already existing
    public static Bundle setEditBundle(ArrayList<String> array, String whereToGo,
                                      String arrayType, String whatToEdit){

        // set information about the array into the bundle
        Bundle bundle = setBundleFromArrayInfo(array, arrayType);

        // set the basics
        bundle.putString(ARGUMENT_CHANGE, whatToEdit);
        bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_EDIT);
        bundle.putString(ARGUMENT_ACTION, ARGUMENT_ACTION_EDIT);
        // where we want to go next
        bundle.putString(Util.ARGUMENT_GO_TO, whereToGo);
        // set that we still have things to set
        bundle.putString(ARGUMENT_DONE_OR_UNFINISHED, ARGUMENT_UNFINISHED);


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

        if ( isFromEdit(bundle.getString(ARGUMENT_FROM)) ) {

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
        bundle.remove(ARGUMENT_CHANGE);

        return bundle;
    }


    // bundle the relevant information between fragments for adding new symptom log
    public static Bundle setEditSymptomLogBundle(String logIdString,
                                                 String whatToEdit){


        // where we want to go next
        String whereNext = null;
        // if we're changing one of the time based parameters
        if ( isSymptomLogTimeChange(whatToEdit) ){
            whereNext = ARGUMENT_GO_TO_DATE_TIME_CHOICES_FRAGMENT;
        } else if ( isSymptomLogIntensity(whatToEdit) ) {
            // we're changing intensity or symptom if it's not a time
            whereNext = ARGUMENT_GO_TO_SYMPTOM_INTENSITY_FRAGMENT;
        } else {
            // only symptom is left
            whereNext = ARGUMENT_GO_TO_CHOOSE_SYMPTOM_ACTIVITY;
        }

        // in edit we only have one id in our array, so make it now and pass it in
        ArrayList<String> array = new ArrayList<>();
        array.add(logIdString);
        Bundle bundle = setEditBundle(array, whereNext,
                ARGUMENT_SYMPTOM_LOG_ID_ARRAY, whatToEdit);


        return bundle;
    }

    // bundle the relevant information between fragments for editing an ingredient log
    public static Bundle setEditIngredientLogBundle(String logIdString,
                                                 String whatToEdit){

        // where we want to go next
        String whereNext = null;
        // if we're changing one of the time based parameters
        if ( isIngredientLogTimeChange(whatToEdit) ){
            whereNext = ARGUMENT_GO_TO_DATE_TIME_CHOICES_FRAGMENT;
        }
        else if ( isIngredientLogAmount(whatToEdit) ) {
            // we're changing amount or brand or ingredient if it's not a time
            whereNext = ARGUMENT_GO_TO_INGREDIENT_AMOUNT_ACTIVITY;
        }
        else if ( isIngredientLogBrand(whatToEdit) ) {
            // we're changing brand if it's not a time or amount
            whereNext = ARGUMENT_GO_TO_INGREDIENT_BRAND_ACTIVITY;
        }
        else {
            // only ingredient is left
            whereNext = ARGUMENT_GO_TO_CHOOSE_INGREDIENT_ACTIVITY;
        }

        // in edit we only have one id in our array, so make it now and pass it in
        ArrayList<String> array = new ArrayList<>();
        array.add(logIdString);
        Bundle bundle = setEditBundle(array, whereNext,
                ARGUMENT_INGREDIENT_LOG_ID_ARRAY,
                whatToEdit);


        return bundle;
    }
    // bundle the relevant information between fragments for adding new symptom log
    public static Bundle setNewIngredientLogBundleFromArray(ArrayList<String> newLogIdStringArray){

        Bundle bundle = new Bundle();
        // where we want to go next
        bundle.putString(Util.ARGUMENT_GO_TO, Util.ARGUMENT_GO_TO_INGREDIENT_AMOUNT_ACTIVITY);
        // set that we still have things to set
        bundle.putString(ARGUMENT_DONE_OR_UNFINISHED, ARGUMENT_UNFINISHED);

        //set the array string of symptom log Ids to the bundle
        String newLogIdString = newLogIdStringArray.toString();
        bundle.putString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY, newLogIdString );

        // also how big the array is for ease of coding
        Integer logIdStringArraySize = newLogIdStringArray.size();
        String logIdStringArraySizeIndexString = String.valueOf(logIdStringArraySize - 1);
        String newLogIdStringArraySize = String.valueOf(logIdStringArraySize);
        bundle.putString(Util.ARGUMENT_HOW_MANY_ID_IN_ARRAY,
                newLogIdStringArraySize);
        bundle.putString(Util.ARGUMENT_CURRENT_INDEX_IN_ARRAY,
                logIdStringArraySizeIndexString);


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

    public static void toastInvalidBeforeAfter(String firstTypeString,String secondTypeString,
                                                                   String beforeOrAfterString,
                                                                   Activity thisActivity ){
        String invalidString = "Invalid date/times. It can't have been " +
                firstTypeString +
                beforeOrAfterString + " it was " +
                secondTypeString +
                ". Reset one of the two.";
        Toast.makeText(thisActivity,
                invalidString,
                Toast.LENGTH_SHORT).show();
    }
    public static void toastInvalidBeforeAfterGoToSpecificDateTime(String firstTypeString,
                                                                   String secondTypeString,  String beforeOrAfterString,
                                                                   Activity thisActivity,
                                                                   FragmentTransaction fragmentTransaction,
                                                                   int fragmentContainer, Bundle bundle){
        String invalidString = "Invalid date/times. An ingredient can't have been " +
                        secondTypeString +
                        beforeOrAfterString + " it was " +
                        firstTypeString +
                        ". Reset one of the two.";

        //TODO go to list or edit or more specific datetime, make more buttons that
        // only become visible now, or a popup and make the user pick where to go
        //for now just go to specific datetime
        toastInvalidGoToSpecificDateTime(invalidString, thisActivity,
                fragmentTransaction,
                fragmentContainer, bundle);
    }

    public static void toastInvalidGoToSpecificDateTime(String invalidString,  Activity thisActivity,
                                                        FragmentTransaction fragmentTransaction,
                                                        int fragmentContainer, Bundle bundle){

        Toast.makeText(thisActivity,
                invalidString,
                Toast.LENGTH_SHORT).show();

        Util.goToLogSpecificDateTimeFragment(thisActivity,
                fragmentTransaction,
                fragmentContainer, bundle);
    }

    // update the "from this fragment/activity" using the "go to" from the bundle
    public static Bundle updateBundleToBundleNext(Bundle bundle){

        if ( bundle.containsKey(ARGUMENT_GO_TO)){
            // if we meant to go to this fragment
            if ( isGoToAddIngredientActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_ADD_INGREDIENT_ACTIVITY);
            }
            else if ( isGoToDeleteIngredientActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_DELETE_INGREDIENT_ACTIVITY);
            }
            else if ( isGoToDetailIngredientActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_DETAIL_INGREDIENT_ACTIVITY);
            }
            else if ( isGoToEditIngredientActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_EDIT_INGREDIENT_ACTIVITY);
            }
            else if ( isGoToIngredientActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_INGREDIENT_ACTIVITY);
            }
            else if ( isGoToAddIngredientLogActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_ADD_INGREDIENT_LOG_ACTIVITY);
            }
            else if ( isGoToChooseIngredientActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_CHOOSE_INGREDIENT_ACTIVITY);
            }
            else if ( isGoToIngredientAmountFragment(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_INGREDIENT_AMOUNT_FRAGMENT);
            }
            else if ( isGoToEditIngredientLogActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_EDIT_INGREDIENT_LOG_ACTIVITY);
            }
            else if ( isGoToAddRecipeActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_ADD_RECIPE_ACTIVITY);
            }
            else if ( isGoToEditRecipeActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_EDIT_RECIPE_ACTIVITY);
            }
            else if ( isGoToRecipeActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_RECIPE_ACTIVITY);
            }
            else if ( isGoToDetailSymptomActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_DETAIL_SYMPTOM_ACTIVITY);
            }
            else if ( isGoToSymptomActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_SYMPTOM_ACTIVITY);
            }
            else if ( isGoToAddSymptomLogActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_ADD_SYMPTOM_LOG_ACTIVITY);
            }
            else if ( isGoToChooseSymptomActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_CHOOSE_SYMPTOM_ACTIVITY);
            }
            else if ( isGoToEditSymptomLogActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_EDIT_SYMPTOM_LOG_ACTIVITY);
            }
            else if ( isGoToListSymptomLogActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_LIST_SYMPTOM_LOG_ACTIVITY);
            }
            else if ( isGoToSymptomIntensityActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_SYMPTOM_INTENSITY_FRAGMENT);
            }
            else if ( isGoToDateTimeChoicesFragment(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_DATE_TIME_CHOICES_FRAGMENT);
            }
            else if ( isGoToDetailActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_DETAIL_ACTIVITY);
            }
            else if ( isGoToEditActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_EDIT_ACTIVITY);
            }
            else if ( isGoToExportActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_EXPORT_ACTIVITY);
            }
            else if ( isGoToFirstStartWizardActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_FIRST_START_WIZARD_ACTIVITY);
            }
            else if ( isGoToMainActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_MAIN_ACTIVITY);
            }
            else if ( isGoToOtherActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_OTHER_ACTIVITY);
            }
            else if ( isGoToPartOfDayFragment(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_PART_OF_DAY_FRAGMENT);
            }
            else if ( isGoToResourcesActivity(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_RESOURCES_ACTIVITY);
            }
            else if ( isGoToSpecificDateTimeFragment(bundle) ){
                bundle.putString(ARGUMENT_FROM, ARGUMENT_FROM_SPECIFIC_DATE_TIME_FRAGMENT);
            }
        }

        return bundle;

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
    public static NumberPicker setNumberPickerIntegers(NumberPicker numberPicker,
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
    public static NumberPicker setNumberPickerStrings(NumberPicker picker,
                                               String[] displayStringList, Integer defaultValueIndex,
                                               Color defaultValueColor){
        // the lowest number in the list, then minus one
        // to give us the index to set the numberpicker with
        String minimumIndexString = displayStringList[0];
        // get the last in the list by getting the length, minus one to make an index
        Integer indexForLastValueInStringList = displayStringList.length - 1;
        // parse the last number in the list, then minus one to give an index
//        Integer maximumIntegerIndex =
//                Integer.parseInt(displayStringList[indexForLastValueInStringList]) - 1;

        // make a number picker for the intensity of the symptom
        picker.setDisplayedValues(displayStringList);
        picker.scrollBy(0,indexForLastValueInStringList);
        picker.setMinValue(0);
        picker.setMaxValue(indexForLastValueInStringList);
        picker.setValue(defaultValueIndex);
        picker.setWrapSelectorWheel(false);
        // TODO this is a lot of parsing and converting, is colorstatelist really better than color?
        //numberPicker.setBackgroundColor(Color.parseColor(String.valueOf(defaultValueColor)));
        return picker;
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

    public static Boolean isSameDayYear(Instant longestAgoInstant, Instant mostRecentInstant){
        Boolean isTrueOrNot = Boolean.FALSE;

        // if either value is null, obviously they can't be the same day and year
        // only compute same day year calculation if neither is null
        if ( longestAgoInstant != null || mostRecentInstant != null){
            Integer longestAgoDay = Util.dayOfYearFromInstant(longestAgoInstant);
            Integer longestAgoYear = Util.yearFromInstant(longestAgoInstant);

            Integer mostRecentYear = Util.yearFromInstant(mostRecentInstant);
            Integer mostRecentDay = Util.dayOfYearFromInstant(mostRecentInstant);

            if (longestAgoDay == mostRecentDay && longestAgoYear == mostRecentYear) {
                isTrueOrNot = Boolean.TRUE;
            }
        }

        return isTrueOrNot;
    }



    // boolean for if the first given instant should happen earlier in time than the second instant
    public static Boolean isEarlierDayYear(Instant shouldBeEarlierInstant,
                                           Instant shouldHappenLaterInstant){
        Boolean happenedBeforeItShouldHave = Boolean.FALSE;

        // only compute day year calculation if neither is null
        if ( shouldBeEarlierInstant != null || shouldHappenLaterInstant != null){
            Integer shouldBeEarlierDay = Util.dayOfYearFromInstant(shouldBeEarlierInstant);
            Integer shouldBeEarlierYear = Util.yearFromInstant(shouldBeEarlierInstant);

            Integer shouldHappenLaterYear = Util.yearFromInstant(shouldHappenLaterInstant);
            Integer shouldHappenLaterDay = Util.dayOfYearFromInstant(shouldHappenLaterInstant);

            // if what's supposed to be more recent is actually before what's supposed to be
            // longer ago
            if (shouldHappenLaterDay < shouldBeEarlierDay &&
                    shouldHappenLaterYear <= shouldBeEarlierYear ) {
                happenedBeforeItShouldHave = Boolean.TRUE;
            }
        }

        return happenedBeforeItShouldHave;
    }














    ////////////////////////////////////////////////////////
    // start commonly used functions //////////
    ////////////////////////////////////////////////////////

    public static Boolean isSymptomLogTimeChange(String whatToChange){
        Boolean isTimeChange = Boolean.FALSE;

        if ( isSymptomLogWhatToChangeSetToBegin(whatToChange)
                || isSymptomLogWhatToChangeSetToChanged(whatToChange)
                || isSymptomLogAllInstants(whatToChange)
                || isSymptomLogAllInstantsAllDay(whatToChange)
        ) {
            isTimeChange = Boolean.TRUE;
        }
        return isTimeChange;
    }

    public static Boolean isGoToMainActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_PART_OF_DAY_FRAGMENT)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToIngredientBrandActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_INGREDIENT_BRAND_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToAddIngredientActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_ADD_INGREDIENT_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToDeleteIngredientActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_DELETE_INGREDIENT_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToDetailIngredientActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_DETAIL_INGREDIENT_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToEditIngredientActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_EDIT_INGREDIENT_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToIngredientActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_INGREDIENT_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToAddIngredientLogActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_ADD_INGREDIENT_LOG_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToChooseIngredientActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_CHOOSE_INGREDIENT_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToIngredientAmountFragment(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_INGREDIENT_AMOUNT_FRAGMENT)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToEditIngredientLogActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_EDIT_INGREDIENT_LOG_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToAddRecipeActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_ADD_RECIPE_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToEditRecipeActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_EDIT_RECIPE_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToRecipeActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_RECIPE_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToDetailSymptomActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_DETAIL_SYMPTOM_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToSymptomActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_SYMPTOM_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToAddSymptomLogActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_ADD_SYMPTOM_LOG_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToChooseSymptomActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_CHOOSE_SYMPTOM_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToEditSymptomLogActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_EDIT_SYMPTOM_LOG_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToListSymptomLogActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_LIST_SYMPTOM_LOG_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToSymptomIntensityActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_SYMPTOM_INTENSITY_FRAGMENT)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToDateTimeChoicesFragment(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_DATE_TIME_CHOICES_FRAGMENT)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToDetailActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_DETAIL_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToEditActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_EDIT_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToExportActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_EXPORT_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToFirstStartWizardActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_FIRST_START_WIZARD_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToOtherActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_OTHER_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToResourcesActivity(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_RESOURCES_ACTIVITY)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToSpecificDateTimeFragment(Bundle bundle) {
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_SPECIFIC_DATE_TIME_FRAGMENT)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isGoToPartOfDayFragment(Bundle bundle){
        Boolean isTrueOrFalse = Boolean.FALSE;
        if (TextUtils.equals(bundle.getString(ARGUMENT_GO_TO), ARGUMENT_GO_TO_PART_OF_DAY_FRAGMENT)) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }




    public static Boolean isIngredientLogTimeChange(String whatToChange){
        Boolean isTimeChange = Boolean.FALSE;

        if ( isIngredientLogAllInstants(whatToChange)
                || isIngredientLogConsumed(whatToChange)
                || isIngredientLogCooked(whatToChange)
                || isIngredientLogAcquired(whatToChange)
        ) {
            isTimeChange = Boolean.TRUE;
        }
        return isTimeChange;
    }

    public static Boolean isLogTimeChange(String whatToChange){
        Boolean isTimeChange = Boolean.FALSE;

        if ( isSymptomLogTimeChange(whatToChange)
                || isIngredientLogTimeChange(whatToChange)
        ) {
            isTimeChange = Boolean.TRUE;
        }
        return isTimeChange;
    }
    public static Boolean isFromEdit(String whatToChange){
        Boolean isTrueOrFalse = Boolean.FALSE;

        if (TextUtils.equals(
                whatToChange,
                ARGUMENT_FROM_EDIT
        )) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }
    public static Boolean isIngredientLogBrand(String whatToChange){
        Boolean isTrueOrFalse = Boolean.FALSE;

        if (TextUtils.equals(
                whatToChange,
                ARGUMENT_CHANGE_INGREDIENT_LOG_BRAND
        )) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isTodayFromInstant(Instant instantToCheck){
        Boolean mIsToday = Boolean.TRUE;

        LocalDateTime localDateTime = localDateTimeFromInstant(instantToCheck);
        Integer dayToCheck = localDateTime.getDayOfYear();
        Integer yearToCheck = localDateTime.getYear();
        LocalDateTime nowLocalDateTime = localDateTimeFromInstant(Instant.now());
        Integer thisDay = nowLocalDateTime.getDayOfYear();
        Integer thisYear = nowLocalDateTime.getYear();
        Log.d(TAG, " \n\ninstant: \n" + instantToCheck.toString());
        Log.d(TAG, " \n\nyearToCheck: \n" + yearToCheck.toString());
        Log.d(TAG, " \n\ndayToCheck: \n" + dayToCheck.toString());
//        Integer yearToCheck = instantToCheck.get(ChronoField.YEAR);
//        Log.d(TAG,  " \n\nyear: \n" +yearToCheck.toString());
//        Integer yearToCheck = instantToCheck.get(ChronoField.YEAR);
//        Integer dayToCheck = instantToCheck.get(ChronoField.DAY_OF_YEAR);
        if ( dayToCheck == thisDay &&
                yearToCheck == thisYear
        ) {
            // if the date is today time based buttons can vanish
            mIsToday = Boolean.TRUE;
        } else {
            // it is not today so the buttons can't go invisible
            //TODO add logic for if we're only setting time from edit
            mIsToday = Boolean.FALSE;
        }

        return mIsToday;
    }

    public static Boolean isIngredientLogAmount(String whatToChange){
        Boolean isTrueOrFalse = Boolean.FALSE;

        if (TextUtils.equals(
                whatToChange,
                ARGUMENT_CHANGE_INGREDIENT_LOG_AMOUNT)
        ) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }
    public static Boolean isSymptomLogIntensity(String whatToChange){
        Boolean isIntensity = Boolean.FALSE;

        if (TextUtils.equals(
                        whatToChange,
                        ARGUMENT_CHANGE_SYMPTOM_LOG_INTENSITY)
        ) {
            isIntensity = Boolean.TRUE;
        }
        return isIntensity;
    }

    public static Boolean isActionEditBundle(Bundle bundle){
        Boolean isTrueOrFalse = Boolean.FALSE;

        if (
                TextUtils.equals(
                        bundle.getString(ARGUMENT_ACTION),
                        ARGUMENT_ACTION_EDIT
                )
        ) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }
    public static Boolean isActionAddBundle(Bundle bundle){
        Boolean isTrueOrFalse = Boolean.FALSE;

        if (
                TextUtils.equals(
                        bundle.getString(ARGUMENT_ACTION),
                        ARGUMENT_ACTION_ADD
                )
        ) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }
    public static Boolean isEditAndDone(Bundle bundle){
        Boolean isTrueOrFalse = Boolean.FALSE;

        if (
                TextUtils.equals(
                        bundle.getString(ARGUMENT_ACTION),
                        ARGUMENT_ACTION_EDIT
                )
                        &&
                        TextUtils.equals(
                                bundle.getString(ARGUMENT_DONE_OR_UNFINISHED),
                                ARGUMENT_DONE
                        )
        ) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }

    public static Boolean isDone(Bundle bundle){
        Boolean isTrueOrFalse = Boolean.FALSE;

        if (TextUtils.equals(
                bundle.getString(ARGUMENT_DONE_OR_UNFINISHED),
                ARGUMENT_DONE
        )) {
            isTrueOrFalse = Boolean.TRUE;
        }
        return isTrueOrFalse;
    }


    public static Bundle setBundleChangeGoTo(Bundle bundle, String whatToChange){

        bundle.putString(Util.ARGUMENT_CHANGE, whatToChange);

        if ( isLogTimeChange(whatToChange) ){
            bundle.putString(ARGUMENT_GO_TO, ARGUMENT_GO_TO_DATE_TIME_CHOICES_FRAGMENT);
        } else if ( isSymptomLogIntensity(whatToChange) ){
            bundle.putString(ARGUMENT_GO_TO, ARGUMENT_GO_TO_SYMPTOM_INTENSITY_FRAGMENT);

        } else if ( isIngredientLogBrand(whatToChange) ){
            bundle.putString(ARGUMENT_GO_TO, ARGUMENT_GO_TO_INGREDIENT_BRAND_ACTIVITY);

        }
        return bundle;
    }

    public static void goToLogSpecificDateTimeFragment(Activity paramThisActivity,
                                               FragmentTransaction paramFragmentTransaction,
                                               int paramFragmentContainerView, Bundle bundle) {

        // go to the next place now
        Util.startNextFragmentBundle(paramThisActivity, paramFragmentTransaction,
                paramFragmentContainerView, new SpecificDateTimeFragment(), bundle);
    }

    public static void startNextFragmentBundleChange(Activity paramThisActivity,
                                                     FragmentTransaction paramFragmentTransaction,
                                               int paramFragmentContainerView,
                                               Fragment paramNextFragment, Bundle paramBundle,
                                               String whatToChange ) {

        paramBundle = setBundleChangeGoTo(paramBundle, whatToChange);
        paramBundle.putString(Util.ARGUMENT_CHANGE, whatToChange);
        int howManyInArray = 0;
        String idArrayString = setStringTypeBundle(paramBundle);
        Log.d(TAG, paramBundle.toString());
        Log.d(TAG, idArrayString);
        for ( String hasComma:
                paramBundle.getString(idArrayString).split(",") ){
            howManyInArray++;
        }
        paramBundle.putString(ARGUMENT_HOW_MANY_ID_IN_ARRAY, String.valueOf(howManyInArray));
        paramBundle.putString(Util.ARGUMENT_CURRENT_INDEX_IN_ARRAY,
                String.valueOf(howManyInArray-1));
        Log.d(TAG, paramBundle.toString());

        startNextFragmentBundle(paramThisActivity, paramFragmentTransaction,
                paramFragmentContainerView,
                paramNextFragment, paramBundle);
    }
    public static void startNextFragmentBundle(Activity paramThisActivity,
                                               FragmentTransaction paramFragmentTransaction,
                                         int paramFragmentContainerView,
                                         Fragment paramNextFragment, Bundle bundle) {

        // actually go to the next place now
        // based on from edit or not and if we're done
        if ( isEditAndDone(bundle)  ) {
                String[] logIdArrayStringAndType = setLogIdArrayTypeAndStringFromBundle(bundle);
                String logIdArrayType = logIdArrayStringAndType[0];
                String logIdArrayString = logIdArrayStringAndType[1];

                Util.goToEditActivityActionTypeId(null, paramThisActivity,
                        Util.ARGUMENT_ACTION_EDIT,
                        logIdArrayType, logIdArrayString);

        }
        else if ( isDone(bundle) ){
            // we're done and not from edit, so go to list
            String[] logIdArrayStringAndType = setLogIdArrayTypeAndStringFromBundle(bundle);
            String logIdArrayType = logIdArrayStringAndType[0];
            String logIdArrayString = logIdArrayStringAndType[1];

            Util.goToListActivityTypeId(null, paramThisActivity,
                    logIdArrayType, logIdArrayString);

        } else {
            // set our bundle to say it's going to the fragment we're about to go to
            bundle = setGoToFromNextFragment(bundle, paramNextFragment);

            // set the data to pass along info
            // given from the previous fragment
            // or the duplicated ID if we did that
            paramNextFragment.setArguments(bundle);

            // we weren't from edit or done so go to the next fragment
            startNextFragment(paramFragmentTransaction, paramFragmentContainerView, paramNextFragment);
        }
    }
    public static Bundle setGoToFromNextFragment(Bundle bundle, Fragment nextFragment){
        //TODO make this also set where from
        Class nextFragmentClass = nextFragment.getClass();
        String goToString = null;
        if (DateTimeChoicesFragment.class == nextFragmentClass){
            goToString = ARGUMENT_GO_TO_DATE_TIME_CHOICES_FRAGMENT;
        } else if ( PartOfDayFragment.class == nextFragmentClass ){
            goToString = ARGUMENT_GO_TO_PART_OF_DAY_FRAGMENT;
        } else if ( SpecificDateTimeFragment.class == nextFragmentClass ){
            goToString = ARGUMENT_GO_TO_SPECIFIC_DATE_TIME_FRAGMENT;
        } else if ( SymptomIntensityFragment.class == nextFragmentClass ){
            goToString = ARGUMENT_GO_TO_SYMPTOM_INTENSITY_FRAGMENT;
        } else if (IngredientAmountFragment.class == nextFragmentClass ){
            goToString = ARGUMENT_GO_TO_INGREDIENT_AMOUNT_ACTIVITY;
        } else if ( nextFragmentClass == null ){
            goToString = ARGUMENT_GO_TO_MAIN_ACTIVITY;
        }

        bundle.putString(ARGUMENT_GO_TO, goToString);
        return bundle;
    }
    public static void startNextFragment(FragmentTransaction paramFragmentTransaction,
                                         int paramFragmentContainerView,
                                         Fragment paramNextFragment ) {

        Log.d(TAG, "\n\n in starting fragment next fragment \n" + paramNextFragment.toString());
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

    // check the arguments for if we have a bundle and if we do, check it for valid values
    // go back to list or edit if anything required is invalid
    public static Bundle checkValidFragment(Bundle bundle, Activity activity){

        // if we have no bundle at all
        if ( bundle == null ){
            // go back to home screen
            Util.goToMainActivity(null, activity);
        } else {
            // check the bundle has required values
            if ( !hasValidWhatToChange(bundle) && !hasValidId(bundle) ){
                // one of the required values was invalid so go to list or edit
                Util.goToListOrEditActivity(null, activity, bundle);
            }
        }
        // got here because valid bundle
        Log.d(TAG, "Valid bundle here: " + bundle.toString());
        return bundle;
    }


    public static Boolean hasValidWhatToChange(Bundle bundle){
        // default is that the given value is not valid
        Boolean isValidValue = Boolean.FALSE;
        // if we find any value for what to change
        if ( bundle.getString(Util.ARGUMENT_CHANGE) != null ){
            // then set that the value is valid to true
            // TODO also put in logic for here for different fragments, like if the ingredient
            //  what to change is set to symptom's set invalid, that sort of thing, unit testing
            isValidValue = Boolean.TRUE;
        }
        return isValidValue;
    }
    public static Boolean hasValidId(Bundle bundle){
        Boolean isValidValue = Boolean.FALSE;
        // does it contain any of the id arrays
        if ( isIngredientLogBundle(bundle) || isSymptomLogBundle(bundle)
                || isSymptomBundle(bundle) || isIngredientBundle(bundle)
                //TODO add recipe id here or remove it if stop using recipe
        ){
            isValidValue = Boolean.TRUE;
        }
        return isValidValue;
    }

    // check if we were from edit or not, go back to edit screen or to list activity
    public static void goToListOrEditActivity(Context context, Activity activity, Bundle bundle){

        // if it's edit and done has been set, or if the bundle does not have an action set
        // not having an action set means it's from main activity so then go to list
         if ( isEditAndDone(bundle) || !bundle.containsKey(ARGUMENT_ACTION)  ) {
             // if from edit and we're done setting the value we'd clicked go to that screen
                    goToEditSymptomLogOrIngredientLogActivity(context, activity, bundle);
            } else {
                // if no it can't be edit, so go to list
                goToListSymptomLogOrIngredientLogActivity(context, activity, bundle);
            }
    }

    // check the bundle to get the log id array key and then go to the right list activity
    public static void goToListSymptomLogOrIngredientLogActivity(Context context, Activity
                                                                 activity, Bundle bundle){

        //if it's symptom log go to that
        if ( isSymptomLogBundle(bundle) ) {
            goToListSymptomLogActivity(context, activity, bundle.getString(ARGUMENT_SYMPTOM_LOG_ID_ARRAY));
        } else {
            // ingredient log then
            goToListIngredientLogActivity(context, activity, bundle.getString(ARGUMENT_INGREDIENT_LOG_ID_ARRAY));
        }
    }

    public static Boolean isSymptomLogBundle(Bundle bundle){

        Boolean isSymptomLogOrNot = Boolean.FALSE;

        if ( bundle.containsKey(ARGUMENT_SYMPTOM_LOG_ID_ARRAY)
        ) {
            isSymptomLogOrNot = Boolean.TRUE;
        }
        return isSymptomLogOrNot;
    }

    public static Boolean isRecipeBundle(Bundle bundle){

        Boolean isSymptomLogOrNot = Boolean.FALSE;

        if ( bundle.containsKey(ARGUMENT_RECIPE_ID_ARRAY)
        ) {
            isSymptomLogOrNot = Boolean.TRUE;
        }
        return isSymptomLogOrNot;
    }

    public static Boolean isSymptomBundle(Bundle bundle){

        Boolean isSymptomLogOrNot = Boolean.FALSE;

        if ( bundle.containsKey(ARGUMENT_SYMPTOM_ID_ARRAY)
        ) {
            isSymptomLogOrNot = Boolean.TRUE;
        }
        return isSymptomLogOrNot;
    }

    public static Boolean isSymptomLogTypeString(String typeString){

        Boolean isSymptomLogOrNot = Boolean.FALSE;

        if ( TextUtils.equals(ARGUMENT_SYMPTOM_LOG_ID_ARRAY, typeString)
        ) {
            isSymptomLogOrNot = Boolean.TRUE;
        }
        return isSymptomLogOrNot;
    }
    public static Boolean isSymptomLogWhatToChangeSetToBegin(String typeString){

        Boolean isSymptomLogOrNot = Boolean.FALSE;

        if ( TextUtils.equals(ARGUMENT_CHANGE_SYMPTOM_LOG_BEGIN, typeString)
        ) {
            isSymptomLogOrNot = Boolean.TRUE;
        }
        return isSymptomLogOrNot;
    }
    public static Boolean isSymptomLogAllInstants(String typeString){

        Boolean isSymptomLogOrNot = Boolean.FALSE;

        if ( TextUtils.equals(ARGUMENT_CHANGE_SYMPTOM_LOG_ALL_INSTANTS, typeString)
        ) {
            isSymptomLogOrNot = Boolean.TRUE;
        }
        return isSymptomLogOrNot;
    }
    public static Boolean isSymptomLogAllInstantsAllDay(String typeString){

        Boolean isSymptomLogOrNot = Boolean.FALSE;

        if ( TextUtils.equals(ARGUMENT_CHANGE_SYMPTOM_LOG_ALL_INSTANTS_ALL_DAY, typeString)
        ) {
            isSymptomLogOrNot = Boolean.TRUE;
        }
        return isSymptomLogOrNot;
    }
    public static Boolean isSymptomLogWhatToChangeSetToChanged(String typeString){

        Boolean isSymptomLogOrNot = Boolean.FALSE;

        if ( TextUtils.equals(ARGUMENT_CHANGE_SYMPTOM_LOG_CHANGED, typeString)
        ) {
            isSymptomLogOrNot = Boolean.TRUE;
        }
        return isSymptomLogOrNot;
    }
    public static Boolean isIngredientLogTypeString(String typeString){

        Boolean isIngredientLogOrNot = Boolean.FALSE;

        if ( TextUtils.equals(ARGUMENT_INGREDIENT_LOG_ID_ARRAY, typeString)
        ) {
            isIngredientLogOrNot = Boolean.TRUE;
        }
        return isIngredientLogOrNot;
    }


    public static Boolean isIngredientLogAllInstants(String string){

        Boolean isIngredientLogOrNot = Boolean.FALSE;

        if ( TextUtils.equals(ARGUMENT_CHANGE_INGREDIENT_LOG_ALL_INSTANTS, string)
        ) {
            isIngredientLogOrNot = Boolean.TRUE;
        }
        return isIngredientLogOrNot;
    }
    public static Boolean isIngredientLogConsumed(String string){

        Boolean isIngredientLogOrNot = Boolean.FALSE;

        if ( TextUtils.equals(ARGUMENT_CHANGE_INGREDIENT_LOG_CONSUMED, string)
        ) {
            isIngredientLogOrNot = Boolean.TRUE;
        }
        return isIngredientLogOrNot;
    }
    public static Boolean isIngredientLogCooked(String string){

        Boolean isIngredientLogOrNot = Boolean.FALSE;

        if ( TextUtils.equals(ARGUMENT_CHANGE_INGREDIENT_LOG_COOKED, string)
        ) {
            isIngredientLogOrNot = Boolean.TRUE;
        }
        return isIngredientLogOrNot;
    }
    public static Boolean isIngredientLogAcquired(String string){

        Boolean isIngredientLogOrNot = Boolean.FALSE;

        if ( TextUtils.equals(ARGUMENT_CHANGE_INGREDIENT_LOG_ACQUIRED, string)
        ) {
            isIngredientLogOrNot = Boolean.TRUE;
        }
        return isIngredientLogOrNot;
    }
    public static Boolean isIngredientLogBundle(Bundle bundle){

        Boolean isIngredientLogOrNot = Boolean.FALSE;

        if ( bundle.containsKey(ARGUMENT_INGREDIENT_LOG_ID_ARRAY)
        ) {
            isIngredientLogOrNot = Boolean.TRUE;
        }
        return isIngredientLogOrNot;
    }
    public static Boolean isIngredientBundle(Bundle bundle){

        Boolean isIngredientLogOrNot = Boolean.FALSE;

        if ( bundle.containsKey(ARGUMENT_INGREDIENT_ID_ARRAY)
        ) {
            isIngredientLogOrNot = Boolean.TRUE;
        }
        return isIngredientLogOrNot;
    }

    // check the bundle to get the log id array key and then go to the right list activity
    public static void goToEditSymptomLogOrIngredientLogActivity(Context context, Activity
            activity, Bundle bundle){
        //if it's symptom log go to that
        if ( isSymptomLogBundle(bundle) ) {
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

    public static void searchThisActivity(Context context, Activity activity, String filterString){

        Bundle searchBundle = Util.setSearchBundle(filterString);
        goToActivityTypeIdClass(context, activity, null, null, activity.getClass(), null, null, searchBundle);
    }
    // go to the list activity given the type (symptom, etc) to deal with and its id string
    public static void goToListActivityTypeId(Context context, Activity activity,
                                              String idStringType,
                                              String idString){
        Class whereGoTo = null;

        if ( isSymptomLogTypeString(idStringType) ) {
            whereGoTo = ListSymptomLogActivity.class;
        }
        else if ( isIngredientLogTypeString(idStringType) ) {
            whereGoTo = ListIngredientLogActivity.class;
        }

        goToActivityTypeIdClass(context, activity, idStringType, idString, whereGoTo, null, null, null);

    }



    // go to the choose ingredient activity
    public static void goToChooseIngredientActivity(Context context, Activity activity) {
        goToActivityTypeIdClass(context, activity, null, null, ChooseIngredientActivity.class,
                null, null, null);

//        Intent intent = new Intent(thisContext, ChooseIngredientActivity.class);
//        // if any of our given values aren't null, set them in the intent
//        // if there's a fragment to go to, like we're going to edit and then on to edit symptom
//          intent.putExtra(Util.ARGUMENT_GO_TO,
//                  Util.ARGUMENT_GO_TO_CHOOSE_INGREDIENT);
//          intent.putExtra(Util.ARGUMENT_ACTION, Util.ARGUMENT_ACTION_ADD);
//        startActivity(intent);
    }

    public static String setWhatToChangeFromBundle(Bundle bundle){
        String whatToChange = null;

        if (bundle.containsKey(ARGUMENT_CHANGE)){
            whatToChange = bundle.getString(ARGUMENT_CHANGE);
        }

        return whatToChange;
    }

    // if what to change is this or that, then return our value defaults for that
    // basically if then else if etc
    public static Fragment setIngredientLogFragmentFromChangeInstant(String whatToChange,
                                                                     Fragment setIfConsumed,
                                                                     Fragment setIfCooked,
                                                                     Fragment setIfAcquired){
        Fragment setToThis = null;

        if ( isIngredientLogConsumed(whatToChange) ){
            setToThis = setIfConsumed;
        } else if ( isIngredientLogCooked(whatToChange) ){
            setToThis = setIfCooked;
        } else if ( isIngredientLogAcquired(whatToChange) ){
            setToThis = setIfAcquired;
        }

        return setToThis;
    }



    // given a string arraylist of the UUID of ingredient logs,
    // return the ingredient logs in an array
    public static ArrayList<SymptomLog> setSymptomLogArrayFromStringArray(ArrayList<String> logIdStringArray,
                                                                          SymptomLogViewModel symptomLogViewModel){
        ArrayList<SymptomLog> symptomLogArray = new ArrayList<>();

        // for each string in array update that log's instant began
        for (String logIdString: logIdStringArray) {
            // now get the log associated with each UUID
            symptomLogArray.add(
                    symptomLogViewModel.viewModelGetSymptomLogFromLogId(UUID.fromString(logIdString))
                );
        }
        return symptomLogArray;
    }

    // return a bundle that contains the question string and what to change next
    // given what values to set to if it's symptom log begin or changed etc
    // basically this is an if else
    public static Bundle setQuestionStringWhatToChangeJustNow(String whatToChange,
            String firstStringIfBegin, String firstStringIfChanged,
    String secondStringIfBegin, String secondStringIfChanged){

        String questionString = setSymptomLogStringFromChangeInstant(whatToChange,
                firstStringIfBegin, firstStringIfChanged);
        String whatToChangeString = setSymptomLogStringFromChangeInstant(whatToChange,
                secondStringIfBegin, secondStringIfChanged);

        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_QUESTION, questionString);
        bundle.putString(ARGUMENT_CHANGE_JUST_NOW, whatToChangeString);

        return bundle;
    }

    public static String setStringTypeBundle(Bundle bundle){

        String stringTypeFromBundle = null;

        if ( isSymptomLogBundle(bundle) ) {
            stringTypeFromBundle = Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY;

        } else if ( isIngredientLogBundle(bundle) ){
            stringTypeFromBundle = Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY;

        } else if ( isIngredientBundle(bundle) ){
            stringTypeFromBundle = Util.ARGUMENT_INGREDIENT_ID_ARRAY;

        } else if ( isRecipeBundle(bundle) ){
            stringTypeFromBundle = Util.ARGUMENT_RECIPE_ID_ARRAY;

        } else if ( isSymptomBundle(bundle) ){
            stringTypeFromBundle = Util.ARGUMENT_SYMPTOM_ID_ARRAY;
        }

        return stringTypeFromBundle;
    }
    // given a string arraylist of the UUID of ingredient logs,
    // return the ingredient logs in an array
    public static ArrayList<IngredientLog> setIngredientLogArrayFromStringArray(ArrayList<String> logIdStringArray,
                                                                                IngredientLogViewModel ingredientLogViewModel){
        ArrayList<IngredientLog> ingredientLogArray = new ArrayList<>();
        // for each string in array update that log's instant began

        for (String ingredientLogIdString: logIdStringArray) {
            // now get the food log associated with each UUID
            ingredientLogArray.add(
                    ingredientLogViewModel.viewModelGetIngredientLogFromLogId(
                            UUID.fromString(ingredientLogIdString)
                    ));
        }
        return ingredientLogArray;
    }

    public static String setIngredientLogStringFromChangeInstant(String whatToChange,
                                                                 String setIfConsumed,
                                                                 String setIfCooked,
                                                                 String setIfAcquired){
        String setToThis = null;

        if ( isIngredientLogConsumed(whatToChange) ){
            setToThis = setIfConsumed;
        } else if ( isIngredientLogCooked(whatToChange) ){
            setToThis = setIfCooked;
        } else if ( isIngredientLogAcquired(whatToChange) ){
            setToThis = setIfAcquired;
        }

        return setToThis;
    }
    public static String setSymptomLogStringFromChangeInstant(String whatToChange,
                                                                 String setIfBegin,
                                                                 String setIfChanged){
        String setToThis = null;

        if ( isSymptomLogWhatToChangeSetToBegin(whatToChange) ){
            setToThis = setIfBegin;
        } else if ( isSymptomLogWhatToChangeSetToChanged(whatToChange) ){
            setToThis = setIfChanged;
        }

        return setToThis;
    }


    public static Boolean setGoToEditFromBundle(Bundle bundle){
        Boolean goBackToEdit = Boolean.FALSE;
        // if there was an action given in the bundle
        if ( bundle.containsKey(ARGUMENT_ACTION) ) {
            // and if that action was edit
            if ( isActionEditBundle(bundle) ){
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
                AddSymptomLogActivity.class, null, null, bundle);

    }
    public static void goToAddIngredientActivityMakeAddBundle(Context context, Activity activity){
        Bundle bundle = setNewBundle(null, ARGUMENT_GO_TO_ADD_INGREDIENT_ACTIVITY,
                ARGUMENT_INGREDIENT_ID_ARRAY);
        Log.d(TAG, bundle.toString());
        goToActivityTypeIdClass(context, activity, ARGUMENT_INGREDIENT_ID_ARRAY, null,
                AddIngredientActivity.class, null, null, bundle);

    }


    public static void goToAddIngredientLogActivity(Activity paramActivity,
                                                    String idString, Bundle bundle){

        goToActivityTypeIdClass(null, paramActivity, ARGUMENT_INGREDIENT_ID_ARRAY,
                idString, AddIngredientLogActivity.class,
                ARGUMENT_GO_TO_ADD_INGREDIENT_LOG_ACTIVITY, ARGUMENT_ACTION_ADD, bundle);
    }

















    ////////////////////////////////////////////////////////
    // set which part of food log to change //////////
    ////////////////////////////////////////////////////////

    public static LocalDateTime getDateTimeFromChange(String whatToChange,
                                                      IngredientLog ingredientLog,
                                                      SymptomLog symptomLog) {

        Instant mInstant = null;

        if ( ingredientLog != null ) {
            if (  isIngredientLogConsumed(whatToChange) ) {
                mInstant = ingredientLog.getInstantConsumed();
            } else if ( isIngredientLogCooked(whatToChange) ) {
                mInstant = ingredientLog.getInstantCooked();
            } else if (isIngredientLogAcquired(whatToChange) ) {
                mInstant = ingredientLog.getInstantAcquired();
            } else {
                // this is for all the changed instants, leaving as an else in case I add more
                // types
                mInstant = ingredientLog.getInstantConsumed();
            }
        }
        else if ( symptomLog != null ) {
            if ( isSymptomLogWhatToChangeSetToBegin(whatToChange) ) {
                mInstant = symptomLog.getInstantBegan();
            } else if ( isSymptomLogWhatToChangeSetToChanged(whatToChange) ) {
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

    public static Integer setIntegerCurrentIndexFromBundle(Bundle bundle){

        // default index is first in the array
        Integer currentLogIdIndex = 0;
        //TODO put this in startnextfragmentbundle properly set finished previous fragment
        if (!Objects.isNull(bundle.getString(Util.ARGUMENT_CURRENT_INDEX_IN_ARRAY))) {
            // if there is an index in the bundle, set to that
            currentLogIdIndex =
                    Integer.parseInt(bundle.getString(Util.ARGUMENT_CURRENT_INDEX_IN_ARRAY));
        }
        return currentLogIdIndex;
    }
    // return the instants that came before and after the one we're changing
    public static ArrayList<Instant> setOrderOfLogInstants(String whatToChange,
                                             IngredientLogViewModel ingredientLogViewModel,
                                             SymptomLogViewModel SymptomLogViewModel,
                                             Bundle bundle ){
        ArrayList<Instant> arrayListInstants = new ArrayList<>();
        //TODO all this logic should also return the second instant so it can calculate if it's
        // the correct date or not
        Instant mostRecentInstant = null;
        Instant middleInstant = null;
        Instant longestAgoInstant = null;
        // if it's an ingredient log
        if ( isIngredientLogBundle(bundle) ){
            // then get the ingredient log from the bundle
            String ingredientLogIdString =
                    cleanArrayString(bundle.getString(
                            Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY));
            UUID uuid = UUID.fromString(ingredientLogIdString);
            IngredientLog ingredientLog = ingredientLogViewModel.viewModelGetIngredientLogFromLogId(uuid);

            // get the instant that comes before the one we're setting
            // if it's consumed, then it had to have been acquired and cooked before that
            // so if it's consumed, then any time after cooked is invalid
            // if it's cooked, anything after acquired is invalid
            if ( Util.isIngredientLogCooked(whatToChange) ) {
                // get the new first instant corresponding to our newly set ingredient log instant
                mostRecentInstant = ingredientLog.getInstantCooked();
                longestAgoInstant = ingredientLog.getInstantAcquired();
            }
            else if ( Util.isIngredientLogConsumed(whatToChange) ) {
                // get the new first instant corresponding to our newly set ingredient log instant
                mostRecentInstant = ingredientLog.getInstantConsumed();
                middleInstant = ingredientLog.getInstantCooked();
                longestAgoInstant = ingredientLog.getInstantAcquired();

                // is longestAgoInstant and not today, then valid

                // if is mostRecentInstant, then mostRecentInstant after longestAgoInstant, and make
                // specific date button visible

                // if it's middleInstant, after longestAgoInstant and before mostRecentInstant
            }
            else {
                // get the new first instant corresponding to our newly set ingredient log instant
                // if we're setting acquired, it can't be done in the future, so set it to the
                // default date
                mostRecentInstant = ingredientLog.getInstantAcquired();
            }
        }
        else if (isSymptomLogBundle(bundle)) {
            //if (Util.isSymptomLogTimeChange(whatToChange)) {

            String symptomLogIdString =
                    cleanArrayString(bundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY));
            UUID uuid = UUID.fromString(symptomLogIdString);
            SymptomLog symptomLog = SymptomLogViewModel.viewModelGetSymptomLogFromLogId(uuid);
            // if it's begin we want to use the default time set
            // if it's changed we want to use begin to set what times are valid, after that instant
            mostRecentInstant = symptomLog.getInstantChanged();
            longestAgoInstant = symptomLog.getInstantBegan();
        }

        arrayListInstants.add(mostRecentInstant);
        arrayListInstants.add(middleInstant);
        arrayListInstants.add(longestAgoInstant);
        return arrayListInstants;
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
            if ( TextUtils.equals(whatToChange, ARGUMENT_CHANGE_SYMPTOM_LOG_BEGIN) ) {
                instant = symptomLog.getInstantBegan();
            } else if ( TextUtils.equals(whatToChange, ARGUMENT_CHANGE_SYMPTOM_LOG_CHANGED)){
                instant = symptomLog.getInstantChanged();
            } else {
                instant = symptomLog.getInstantBegan();
            }
        } else if ( ingredientLog != null ){
            if ( TextUtils.equals(whatToChange, ARGUMENT_CHANGE_INGREDIENT_LOG_CONSUMED) ) {
                instant = ingredientLog.getInstantConsumed();
            } else if ( TextUtils.equals(whatToChange, ARGUMENT_CHANGE_INGREDIENT_LOG_COOKED)){
                instant = ingredientLog.getInstantCooked();
            } else if ( TextUtils.equals(whatToChange, ARGUMENT_CHANGE_INGREDIENT_LOG_ACQUIRED)){
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

        // have to convert to set the new time, with chronofield didn't work on instant
        // TODO figure out how to do all these time conversions more efficiently
        LocalDateTime localDateTime = localDateTimeFromInstant(instant);
        if ( minute != null ) {
            localDateTime.with(ChronoField.MINUTE_OF_DAY, minute);
        }
        if ( hour != null ) {
            localDateTime.with(ChronoField.HOUR_OF_DAY, hour);
        }
        if ( day != null ) {
            localDateTime.with(ChronoField.DAY_OF_MONTH, day);
        }
        if ( month != null ) {
            localDateTime.with(ChronoField.MONTH_OF_YEAR, month-1);
        }
        if ( year != null ) {
            localDateTime.with(ChronoField.YEAR, year);
        }
            return instantFromLocalDateTime(localDateTime);
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
            if ( isIngredientLogBundle(bundle) ){
                whatToChange = ARGUMENT_CHANGE_INGREDIENT_LOG_CONSUMED;
            } else {
                whatToChange = ARGUMENT_CHANGE_SYMPTOM_LOG_BEGIN;
            }
        }

        if ( isIngredientLogBundle(bundle)) {
            // start with consumed, then ask for cooked, then ask for acquired for ingredient log
            // or start with begin and then changed for symptom log
            if (isIngredientLogConsumed(whatToChange)) {
                bundle.putString(ARGUMENT_CHANGE, ARGUMENT_CHANGE_INGREDIENT_LOG_COOKED);
            } else if (isIngredientLogCooked(whatToChange)) {
                bundle.putString(ARGUMENT_CHANGE, ARGUMENT_CHANGE_INGREDIENT_LOG_ACQUIRED);
            } else if (isIngredientLogAcquired(whatToChange)) {
                bundle.putString(ARGUMENT_CHANGE, ARGUMENT_CHANGE_INGREDIENT_LOG_BRAND);
            } else if (isIngredientLogBrand(whatToChange)) {
                bundle.putString(ARGUMENT_CHANGE, ARGUMENT_CHANGE_INGREDIENT_LOG_AMOUNT);
            } else if (isIngredientLogAmount(whatToChange)) {
                bundle = setDone(bundle);
            }
        }
        // is it symptom log
        else if (isSymptomLogBundle(bundle)) {
            if (isSymptomLogIntensity(whatToChange)) {
                bundle.putString(ARGUMENT_CHANGE, ARGUMENT_CHANGE_SYMPTOM_LOG_BEGIN);
            } else if (isSymptomLogWhatToChangeSetToBegin(whatToChange)) {
                bundle.putString(ARGUMENT_CHANGE, ARGUMENT_CHANGE_SYMPTOM_LOG_CHANGED);
            } else if (isSymptomLogWhatToChangeSetToChanged(whatToChange)) {
                bundle = setDone(bundle);
            }
        }
        //TODO else if ingredient or symptom themselves

        return bundle;
    }

    // return a symptom log with the new began or changed instants set
    public static SymptomLog setSymptomLogBeganChanged(String whatToChange,
                                                       SymptomLog symptomLog,
                                                       Bundle integerBundleToSetDateTime){

        if (isSymptomLogAllInstants(whatToChange)) {
            // get the instant we have already so we have correct date
            // change began at time
            Instant instantToSet = Util.setInstantFromBundle(symptomLog.getInstantBegan(),
                    integerBundleToSetDateTime);
            symptomLog.setInstantBegan(instantToSet);
            // then change when the symptom changed/ended instant
            instantToSet = Util.setInstantFromBundle(symptomLog.getInstantChanged(),
                    integerBundleToSetDateTime);
            symptomLog.setInstantChanged(instantToSet);
        } else if (isSymptomLogWhatToChangeSetToBegin(whatToChange)) {
            // get the instant we have already so we have correct date
            Instant instantToSet = Util.setInstantFromBundle(symptomLog.getInstantBegan(),
                    integerBundleToSetDateTime);
            symptomLog.setInstantBegan(instantToSet);
        } else if (isSymptomLogAllInstantsAllDay(whatToChange)) {
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

        if (isIngredientLogAllInstants(whatToChange)) {

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
        else if (isIngredientLogConsumed(whatToChange)) {

            // using the bundle and instant we want to change, set the localdatetime to that
            instant = Util.setInstantFromBundle(ingredientLog.getInstantConsumed(),
                    integerBundleToSetDateTime);

            // and set that fixed value back in the food log
            ingredientLog.setInstantConsumed(instant);
        }
        else if (isIngredientLogCooked(whatToChange)) {
            instant = Util.setInstantFromBundle(ingredientLog.getInstantCooked(),
                    integerBundleToSetDateTime);
            ingredientLog.setInstantCooked(instant);
        }
        else if (isIngredientLogAcquired(whatToChange)) {
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
                                             Bundle integerBundleToSetDateTime, Bundle bundleNext
            , Boolean moveToNextWhatToChange){

        Bundle bundle = null;

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

        // only if we were told to move to next change should we reset what to change
        if ( moveToNextWhatToChange ){
            // return the bundle that now has been reset to get the next instant
            bundle = Util.setBundleLogToNextInstant(bundleNext);
        } else {
            // leave the bundle the way we got it
            bundle = bundleNext;
        }

        return bundle;

    }


    public static Bundle setLogInstants(String whatToChange,
                                        ArrayList<IngredientLog> ingredientLogArray,
                                        ArrayList<SymptomLog> symptomLogArray,
                                        IngredientLogViewModel ingredientLogViewModel,
                                        SymptomLogViewModel symptomLogViewModel,
                                        IngredientViewModel ingredientViewModel,
                                        SymptomViewModel symptomViewModel,
                                        LocalDateTime dateTime, Bundle bundleNext, Boolean
                                                moveToNextWhatToChange){

        // only one will be given in to set its instants
        if ( ingredientLogViewModel != null ){
            // if we're setting ingredient logs
            bundleNext = Util.setIngredientLogInstants(whatToChange, ingredientLogArray,
                    ingredientLogViewModel, Util.setBundleFromLocalDateTime(dateTime),
                    bundleNext, moveToNextWhatToChange);

        } else if ( symptomLogViewModel != null ) {
            // if setting symptom logs
            bundleNext = Util.setSymptomLogInstants(whatToChange, symptomLogArray,
                    symptomViewModel, symptomLogViewModel,
                    Util.setBundleFromLocalDateTime(dateTime),
                    bundleNext, moveToNextWhatToChange);
        }

        return bundleNext;
    }


    public static Bundle setIngredientLogInstants(String whatToChange,
                                                           ArrayList<IngredientLog> ingredientLogArray,
                                                           IngredientLogViewModel ingredientLogViewModel,
                                                           Bundle integerBundleToSetDateTime,
                                                   Bundle bundleNext, Boolean moveToNextWhatToChange){


        // TODO add check for if acquired and cooked are same as an existing food log
        // TODO then use whatToChange or a new argument
        //  that's for set them all the same as another food log

        Bundle bundle = null;

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

        // only if we were told to move to next change should we reset what to change
        if ( moveToNextWhatToChange ){
            // return the bundle that now has been reset to get the next instant
            bundle = Util.setBundleLogToNextInstant(bundleNext);
        } else {
            // leave the bundle the way we got it
            bundle = bundleNext;
        }

        return bundle;
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