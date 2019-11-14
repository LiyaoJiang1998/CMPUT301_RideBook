package com.example.liyao1_ridebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


/**
 *  Adopted part of the code from my codee in Lab 3
 *  purpose:
 *      the MainActivity Class of the app consists of a ListView of rides, a floating button
 *      for adding rides, and a textView showing total Distance.
 *      View detail, Add, Edit, Delete of a ride are implemented through several Fragment Classes.
 *
 *  design rationale:
 *      - the RideAdapter is used to implement the Ride List
 *      - the Floating Button is used to add new Ride, which goes to
 *        the AddEdit Fragment after click
 *      - LongClickOnItem is used to trigger to go to the Add/Edit Fragment
 *      - OnItemClick is used to trigger to go to the Detail/Delete Fragment
 *      - this class implements the two OnFragmentInteractionListener interfaces
 *        the Overrided methods are used to update the ride list
 *
 */
public class MainActivity extends AppCompatActivity
                        implements AddEditRideFragment.OnFragmentInteractionListener,
                                    DetailDeleteRideFragment.OnFragmentInteractionListener {
    private ListView rideList;
    private TextView totalDistanceText;
    private RideAdapter rideAdapter;
    private ArrayList<Ride> rideDataList;
    private Ride selectedRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Use the Custom RideAdapter to get the ListView of Rides */
        rideList = findViewById(R.id.ride_list);
        rideDataList = new ArrayList<>();

        rideAdapter = new RideAdapter(this, rideDataList);
        rideList.setAdapter(rideAdapter);

        totalDistanceText = findViewById(R.id.total_distance_text);
        totalDistanceText.setText(rideAdapter.totalDistanceString());

        /* Assign the add button to the Fragment*/
        final FloatingActionButton addCityButton = findViewById(R.id.add_ride_button);
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRide = null;
                new AddEditRideFragment().show(getSupportFragmentManager(), "ADD_RIDE");
            }
        });

        /* Assign the on item long click to the Fragment*/
        rideList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View v, int position, long id) {
                selectedRide = (Ride) adapter.getItemAtPosition(position);
                AddEditRideFragment.newInstance(selectedRide).show(getSupportFragmentManager(), "EDIT_RIDE");
                return true;
            }
        });

        /* Assign the click on item to the Fragment*/
        rideList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                selectedRide = (Ride) adapter.getItemAtPosition(position);
                DetailDeleteRideFragment.newInstance(selectedRide).show(getSupportFragmentManager(), "DETAIL_DELETE_RIDE");
            }
        });

    }

    /** When ok is pressed after Add/Edit, update the selected right when Editing,
     *  add a new ride when Adding.
    */
    @Override
    public void onOkPressed(Ride ride) {
        if (selectedRide != null){
            selectedRide.setDate(ride.getDate());
            selectedRide.setTime(ride.getTime());
            selectedRide.setDistance(ride.getDistance());
            selectedRide.setAvgSpeed(ride.getAvgSpeed());
            selectedRide.setAvgCadence(ride.getAvgCadence());
            selectedRide.setComment(ride.getComment());
            selectedRide = null;
            rideAdapter.notifyDataSetChanged();
            totalDistanceText.setText(rideAdapter.totalDistanceString());
        }
        else{
            rideAdapter.add(ride);
            rideAdapter.notifyDataSetChanged();
            totalDistanceText.setText(rideAdapter.totalDistanceString());
        }
    }

    /** When delete is confirmed, remove the selectedRide from the list
     */
    @Override
    public void onConfirmPressed(Ride selectedRide) {
         rideAdapter.remove(selectedRide);
         rideAdapter.notifyDataSetChanged();
         totalDistanceText.setText(rideAdapter.totalDistanceString());
    }
}
