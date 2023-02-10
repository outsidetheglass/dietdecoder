//package com.dietdecoder.dietdecoder.activity;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TimePicker;
//
//import androidx.annotation.NonNull;
//
//import com.dietdecoder.dietdecoder.R;
//
//public class TimePickerFragment {
//
//
//    private TimePicker timePicker1;
//
//
//    public TimePickerFragment() {
//        super(R.layout.fragment_time_picker);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_time_picker, container, false);
//
//    }//end onCreateView
//
//    @Override
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        int hour = timePicker1.getCurrentHour();
//        int min = timePicker1.getCurrentMinute();
//        timePicker1 = (TimePicker) view.findViewById(R.id.timepicker_log_specific_time);
//    }
//
//}
