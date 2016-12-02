package br.com.gabrielacolares.localizarmeusclientes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by gabrielacolares on 26/11/16.
 */
public class DatePickerFragmentAgendamento extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);    }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            // Do something with the date chosen by the user
            ((TextView) getActivity().findViewById(R.id.datanasc)).setText(dayOfMonth+"/"+month+"/"+year);
        }
    }

