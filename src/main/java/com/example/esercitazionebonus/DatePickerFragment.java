package com.example.esercitazionebonus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.example.esercitazionebonus.ui.login.LoginActivity;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    private Calendar calendar;
    private DatePicker datePicker;

    public static DatePickerFragment newInstance() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        Bundle args = new Bundle();

        datePickerFragment.setArguments(args);

        return datePickerFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        datePicker = new DatePicker(getActivity());
        calendar = Calendar.getInstance();

        return new AlertDialog.Builder(getActivity()).setView(datePicker).setPositiveButton(R.string.alert_dialog_ok, (DialogInterface.OnClickListener) (dialog, whichButton) -> {
            calendar.set(Calendar.YEAR, datePicker.getYear());
            calendar.set(Calendar.MONTH, datePicker.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
            ((LoginActivity) getActivity()).doPositiveClick(calendar);
        }).create();
    }
}
