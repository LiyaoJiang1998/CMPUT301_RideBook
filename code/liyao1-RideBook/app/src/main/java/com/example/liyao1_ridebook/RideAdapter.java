package com.example.liyao1_ridebook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;


/** purpose:
 *      Because the app needs to manage and show a list of Rides, a custom class that holds
 *      rides inheriting the ArrayAdapter class is needed.
 *  design rationale:
 *      - need a ArrayList<Ride> to hold the ride objects
 *      - need to implement a method that aggregates the distance from rides
 *      - for each row of the Ride ListView, show only the necessary minimal info
 *        (i.e. Date, Time, Distance), and have a delete button at each row,
 *        for easy and fast delete
 *      - have a validation dialog when deleting to avoid accidental touch
 */
public class RideAdapter extends ArrayAdapter<Ride> {
    private ArrayList<Ride> rides;
    private Context context;

    public RideAdapter(Context context, ArrayList<Ride> rides) {
        super(context, 0, rides);
        this.rides = rides;
        this.context = context;
    }

    /** sum the distance of all the rides in the ArrayList
     */
    public float totalDistance(){
        float totalDistance = 0;
        for (Ride ride : rides) {
            totalDistance += ride.getDistance();
        }
        return totalDistance;
    }

    /** get a String of the total distance to be displayed at the all rides ListView
     */
    public String totalDistanceString(){
        return String.format(Locale.getDefault(), "Total Distance: %.2f km", this.totalDistance());
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content, parent, false);
        }

        final Ride ride = rides.get(position);
        TextView dateTextView = view.findViewById(R.id.date_text);
        TextView timeTextView = view.findViewById(R.id.time_text);
        TextView distanceTextView = view.findViewById(R.id.distance_text);
        dateTextView.setText(ride.getDateString());
        timeTextView.setText(ride.getTimeString());
        distanceTextView.setText(ride.getDistanceString(true));

        /* Make the delete button have a validation step dialog */
        Button deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                ((MainActivity)context).onConfirmPressed(ride);
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
        });
        return view;
    }
}
