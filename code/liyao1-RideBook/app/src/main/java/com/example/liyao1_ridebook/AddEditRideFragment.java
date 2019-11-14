package com.example.liyao1_ridebook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.DecimalMin;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.text.ParseException;
import java.util.Date;
import java.util.List;


/**
 * ---------------------------------------------------------------------------------------------
 * Using Library by Ragunath Jawahar from https://github.com/ragunathjawahar/android-saripaar
 * Android Saripaar - a UI validation library
 * License
 * Copyright 2012 - 2015 Mobs & Geeks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ---------------------------------------------------------------------------------------------
 *  Adopted part of the code from my codee in Lab 3
 *  purpose:
 *      A DialogFragment class for Edit/Add Ride,
 *
 *  design rationale:
 *      - shows the current values of the Ride attributes when editing and
 *        uses EditText to let user edit the values.
 *      - When Fragment triggered by the Add Button, blank fields of the new Ride
 *        will be available for the user to input.
 *      - validate the data validity of the Ride attributes when Edit/Add,
 *        validation implemented using Android Saripaar, Annotation Rules are
 *        added to each EditText View based the requirements
 */
public class AddEditRideFragment extends DialogFragment implements Validator.ValidationListener{
    @NotEmpty
    @Pattern(regex = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))",
            message = "Input must be valid date in yyyy-mm-dd format")
    private EditText dateText;
    @NotEmpty
    @Pattern(regex = "^(0[0-9]|1[0-9]|2[0-3]|[0-9]):[0-5][0-9]$",
            message = "Input must be valid time in hh:mm format")
    private EditText timeText;
    @NotEmpty
    @DecimalMin(value=0, message = "Input must be non-negative decimal")
    private EditText distanceText;
    @NotEmpty
    @DecimalMin(value=0, message = "Input must be non-negative decimal")
    private EditText avgSpeedText;
    @NotEmpty
    @Min(value=0, message = "Input must be non-negative integer")
    private EditText avgCadenceText;
    @Length(max = 20, message = "Input must be text within 20 characters")
    private EditText commentText;

    private Validator validator;

    private OnFragmentInteractionListener listener;
    private Ride selectedRide;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Ride newRide);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }


    /* Will be called when validator all rules passed, so read in all the
       EditText View input values, uses the date and time formatter to validate the Date and Time
       */
    @Override
    public void onValidationSucceeded() {
        boolean valid = true;
        Date date = null;
        try {
            date = Ride.dateFormatter().parse(dateText.getText().toString());
        } catch (ParseException e) {
            valid = false;
            dateText.setError("Input must be valid date in yyyy-mm-dd format");
        }
        Date time = null;
        try {
            time = Ride.timeFormatter().parse(timeText.getText().toString());
        } catch (ParseException e) {
            valid = false;
            timeText.setError("Input must be valid time in hh:mm format");
        }

        float distance = Float.valueOf(distanceText.getText().toString());
        float avgSpeed = Float.valueOf(avgSpeedText.getText().toString());
        int avgCadence = Integer.valueOf(avgCadenceText.getText().toString());
        String comment = commentText.getText().toString();
        if (valid){
            listener.onOkPressed(new Ride(date, time, distance, avgSpeed, avgCadence, comment));
            // only dismiss the dialog when everything is valid.
            getDialog().dismiss();
        }

    }

    /* Will be called when validator of some rule(s) failed, display all the EditText Error prompts */
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
        }
    }

    /** Dialog to handle Add/Edit of Ride,
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_edit_ride_fragment_layout, null);
        dateText = view.findViewById(R.id.date_editText);
        timeText = view.findViewById(R.id.time_editText);
        distanceText = view.findViewById(R.id.distance_editText);
        avgSpeedText = view.findViewById(R.id.avgSpeed_editText);
        avgCadenceText = view.findViewById(R.id.avgCadence_name_editText);
        commentText = view.findViewById(R.id.comment_editText);


        Bundle args = getArguments();
        if (args != null){
            selectedRide = (Ride) args.getSerializable("ride");
        }

        if (selectedRide != null) {
            dateText.setText(selectedRide.getDateString());
            timeText.setText(selectedRide.getTimeString());
            distanceText.setText(selectedRide.getDistanceString(false));
            avgSpeedText.setText(selectedRide.getAvgSpeedString());
            avgCadenceText.setText(selectedRide.getAvgCadenceString());
            commentText.setText(selectedRide.getComment());
        }

        validator = new Validator(this);
        validator.setValidationListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog d = builder.setView(view)
                .setTitle("Add/Edit Ride")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", null).create();

        /* Use View.OnclickListener to get manual control of Dialog dismiss, only dismiss
           after all validation passed and value updated , use validator 's
           onValidationFailed and onValidationSucceeded to handle input requirement validation*/
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        validator.validate();
                    }
                });
            }
        });

        return d;

    }

    static AddEditRideFragment newInstance(Ride ride) {
        Bundle args = new Bundle();
        args.putSerializable("ride", ride);

        AddEditRideFragment fragment = new AddEditRideFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
