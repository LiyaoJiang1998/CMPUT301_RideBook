package com.example.liyao1_ridebook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


/** Adopted part of the code from my codee in Lab 3
 *  purpose:
 *      Fragment class that can be reused for viewing detail of a Ride.
 *      Shows the detailed attributes for the Ride object
 *
 *  design rationale:
 *      - it shows the detailed attributes name and their values
 *      - it gives the AlertDialog for options to go back or Delete the Ride
 *      - it asks for validation when deleting to avoid accidental touch.
 */
public class DetailDeleteRideFragment extends DialogFragment {
    private TextView dateText;
    private TextView timeText;
    private TextView distanceText;
    private TextView avgSpeedText;
    private TextView avgCadenceText;
    private TextView commentText;

    private DetailDeleteRideFragment.OnFragmentInteractionListener listener;
    private Ride selectedRide;
    public interface OnFragmentInteractionListener {
        void onConfirmPressed(Ride ride);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DetailDeleteRideFragment.OnFragmentInteractionListener) {
            listener = (DetailDeleteRideFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.detail_delete_ride_fragment_layout, null);
        dateText = view.findViewById(R.id.date_textView);
        timeText = view.findViewById(R.id.time_textView);
        distanceText = view.findViewById(R.id.distance_textView);
        avgSpeedText = view.findViewById(R.id.avgSpeed_textView);
        avgCadenceText = view.findViewById(R.id.avgCadence_name_textView);
        commentText = view.findViewById(R.id.comment_textView);

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

        /* Delete or Back dialog */
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setView(view)
                    .setTitle("Ride Detail")
                    .setNegativeButton("Back", null)
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            listener.onConfirmPressed(selectedRide);
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setMessage("Are you sure that you want to delete?")
                                    .setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener)
                                    .show();
                        }
                    }).create();
    }

    static DetailDeleteRideFragment newInstance(Ride ride) {
        Bundle args = new Bundle();
        args.putSerializable("ride", ride);

        DetailDeleteRideFragment fragment = new DetailDeleteRideFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
