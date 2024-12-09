package com.example.lab6mobile;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    private TimePickerDialog.OnTimeSetListener listener;

    public static TimePickerFragment newInstance(Calendar calendar, TimePickerDialog.OnTimeSetListener listener) {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.listener = listener;
        Bundle args = new Bundle();
        args.putInt("hour", calendar.get(Calendar.HOUR_OF_DAY));
        args.putInt("minute", calendar.get(Calendar.MINUTE));
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int hour = getArguments().getInt("hour");
        int minute = getArguments().getInt("minute");
        return new TimePickerDialog(requireActivity(), listener, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }
}
