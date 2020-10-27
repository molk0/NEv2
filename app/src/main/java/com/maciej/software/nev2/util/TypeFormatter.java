package com.maciej.software.nev2.util;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TypeFormatter {

    /**
     * Displays decimal points if needed, otherwise just displays an integer
     *
     * @param weight - numeric weight that we want to display
     * @return String type of the weight
     */
    public static String getFormattedWeight(double weight) {
        if (weight%1==0) return String.valueOf((int)weight);
        else return String.valueOf(weight);
    }

    /**
     * Checks whether input is empty, and assigns it a 0.
     *  Method used to avoid null pointer exception when trying to convert no text input into a double.
     *
     * @param weight - User input from a weight input text field
     * @return Double type of given weight, or 0 if weight wasn't typed in
     */
    public static double parseWeight(@NonNull String weight) {
        if (weight.equals("")) weight = "0";
        return Double.parseDouble(weight);
    }

    /**
     * Returns a formatted String that represents a date as:
     *  Mon, Jan 1, 2000
     *
     * @param date - Date object to be formatted
     * @return Formatted date String to be displayed on the screen
     */
    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMMM d, yyyy");
        return formatter.format(date);
    }

}
