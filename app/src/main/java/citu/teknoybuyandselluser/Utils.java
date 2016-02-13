package citu.teknoybuyandselluser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class Utils {

    public static String capitalize (String string) {
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    public static String parseDate(long unix){
        Date date = new Date(unix*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("E, MMM d 'at' h:mm:ss a"); // the format of the date
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Manila")); // timezone reference for formating
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public static String parseToDateOnly(long unix){
        Date date = new Date(unix*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // the format of the date
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Manila")); // timezone reference for formating
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public static String formatFloat(float price){
        //return String.format("%.2f", price);
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.US);
        numberFormatter.setMinimumFractionDigits(2);
        numberFormatter.setMaximumFractionDigits(2);
        return numberFormatter.format(price);
    }

    public static String formatDouble(double price){
        return String.format("%.2f", price);
    }

    public static void alertInfo(Context context,String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    public interface Callbacks {
        void ok();
    }
}
