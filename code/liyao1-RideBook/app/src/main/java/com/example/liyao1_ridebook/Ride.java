package com.example.liyao1_ridebook;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/** purpose:
 *      the Ride object that holds information about a ride
 *
 *  design rationale:
 *      - keep the attributes private (encapsulation), access the attributes through
 *       getters and setters methods
 *      - implement get attribute String methods, so users classes can get String representation of
 *        the attributes easily, and still have access to the underlying attribute in
 *        their actual type (i.e. float, int, Date ...)
 */
public class Ride implements Serializable {

    private Date date;
    private Date time;
    private float distance;
    private float avgSpeed;
    private int avgCadence;
    private String comment;

    /** Static method that returns the DateFormat object with expected Date String format
     */
    public static DateFormat dateFormatter( ){

        return new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
    }

    /** Static method that returns the DateFormat object with expected Time String format
     */
    public static DateFormat timeFormatter(){
        return new SimpleDateFormat("hh:mm", Locale.getDefault());
    }

    public Ride(Date date, Date time, float distance, float avgSpeed, int avgCadence, String comment) {
        this.date = date;
        this.time = time;
        this.distance = distance;
        this.avgSpeed = avgSpeed;
        this.avgCadence = avgCadence;
        this.comment = comment;
    }

    /** get the String Representation of Date in the required Date format of this ride
     */
    public String getDateString(){
        return Ride.dateFormatter().format(this.getDate());
    }

    /** get the String Representation of Time in the required Date format of this ride
     */
    public String getTimeString(){
        return Ride.timeFormatter().format(this.getTime());
    }

    /** get the String Representation of Distance in the two different formats
     *  shortFormat = true, is for displaying in the list of rides screen
     *  shortFormat = false, is for String representation of the float Distance raw value
     */
    public String getDistanceString(boolean shortFormat){
        if (shortFormat) {
            return String.format(Locale.getDefault(), "%.2f km",this.getDistance());
        }
        else{
            return String.format(Locale.getDefault(), "%f",this.getDistance());
        }
    }

    /** get the String Representation of float Average Speed raw value
     */
    public String getAvgSpeedString(){
        return String.format(Locale.getDefault(), "%f", this.getAvgSpeed());
    }

    /** get the String Representation of int Average Cadence raw value
     */
    public String getAvgCadenceString(){
        return String.format(Locale.getDefault(), "%d", this.getAvgCadence());
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public int getAvgCadence() {
        return avgCadence;
    }

    public void setAvgCadence(int avgCadence) {
        this.avgCadence = avgCadence;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
