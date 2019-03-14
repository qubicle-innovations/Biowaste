package uems.biowaste.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import uems.biowaste.vo.UserVo;

;


/**
 * Created by aswin on 09/12/16.
 */

public class Utils {

    public static int THUMBNAIL_SIZE = 300;
    public static final int APP_VERSION = 0;
    public static int MINS_FACTOR = 60000;
    public static int REFRESH_FACTOR = 30000;
    public static int MESSAGE_REFRESH_TIME = 5000;
    public static int msgNotifyID = 1001;
    public static boolean refreshData = false;
    public static final String MESSAGE_KEY = "message";
    public static final String GOOGLE_PROJECT_ID = "1038406780031";
    public static final String SUPPORT_NUMBER = "+97470003200";


    public static Bitmap cropCircleBitmap(Bitmap bitmapimg) {
        Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(),
                bitmapimg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapimg.getWidth(),
                bitmapimg.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmapimg.getWidth() / 2,
                bitmapimg.getHeight() / 2, bitmapimg.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);
        return output;
    }

    public static void showError(String msg, View view) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static Bitmap getThumbnail(Uri uri, Context context) throws FileNotFoundException, IOException {
        InputStream input = context.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    public static String getText(EditText et) {
        try {
            if (et.getText().length() > 0)
                return et.getText().toString();

        } catch (Exception e) {

        }
        return null;
    }

    public static String getText(TextView et) {
        try {
            if (et.getText().length() > 0)
                return et.getText().toString();

        } catch (Exception e) {

        }
        return null;
    }
    public static UserVo getUser(Context context){
        try{
            String user = getSharedPreference(context, "userObject");
            Log.v("user", user);
            ObjectMapper mapper = new ObjectMapper();
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jp = jsonFactory.createJsonParser(user);
            UserVo bean = mapper.readValue(jp, UserVo.class);
            return bean;
        }catch(Exception e){

        }
        return null;
    }
    public static void setUser(Context context, UserVo user){

        try{
            if(user==null){
                deleteSharedPreference(context,"userObject");
            }else{
                ObjectMapper mapper = new ObjectMapper();
                String result =	mapper.writeValueAsString(user);
                setSharedPreference(context, "userObject", result);
                Log.v("user", result);
            }

        }catch(Exception e){

        }

    }

    public static void setSharedPreference(Context ctx, String key, String value) {
        SharedPreferences pref = ctx.getSharedPreferences("NanyMarya", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSharedPreference(Context ctx, String key) {
        SharedPreferences pref = ctx.getSharedPreferences("NanyMarya", Context.MODE_PRIVATE);
        if (pref.contains(key)) {
            return pref.getString(key, null);
        } else {
            return null;
        }
    }

    public static void deleteSharedPreference(Context ctx, String key) {
        SharedPreferences pref = ctx.getSharedPreferences("NanyMarya", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (pref.contains(key)) {
            editor.remove(key);
            editor.commit();
        }
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static String roundOff(String value) {
        try {
            double amount = Double.parseDouble(value);
            NumberFormat formatter = new DecimalFormat("#0.00");
            return formatter.format(amount);

        } catch (Exception e) {

        }
        return null;


    }
    public static String roundOff(Double amount) {
        try {
             NumberFormat formatter = new DecimalFormat("#0.00");
            return formatter.format(amount);

        } catch (Exception e) {

        }
        return null;


    }

    public static String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }

    public static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    public static void launchGoogleMaps(Context context, String latitude, String longitude) {
        try{
            String format = "geo:0,0?q=" + latitude + "," + longitude;
            Uri uri = Uri.parse(format);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }catch (Exception e) {
            Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show();
        }
    }

    public static void call(Context context, String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        context.startActivity(intent);
    }
   /* public static ArrayList<ProductVO> getCart(Context context) {
        ArrayList<ProductVO> temp = new ArrayList<>();
        try{
            String cart = getSharedPreference(context, "cartObject");
             ObjectMapper mapper = new ObjectMapper();
            ResponsePojo<ArrayList<ProductVO>> ad = mapper.readValue(cart, new TypeReference<ResponsePojo<ArrayList<ProductVO>>>() { });
            if(ad.getData()==null) {
                return temp;
            }
            return ad.getData();
        }catch(Exception e){
            e.printStackTrace();
        }

        return temp;

    }


    public static void setCart(Context context, ArrayList<ProductVO> aList){

        try{
            ResponsePojo<ArrayList<ProductVO>> ad = new ResponsePojo<>();
            ad.setData(aList);
            ObjectMapper mapper = new ObjectMapper();
            String result=	mapper.writeValueAsString(ad);
            setSharedPreference(context, "cartObject", result);
            Log.v("user", result);
        }catch(Exception e){

            e.printStackTrace();
        }

    }
    public static long addEvent(Context context, OrderVO event) {
        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        Date eventDate = DateUtil.stringToDate(event.getEventdate(), DateUtil.DATE_SQL);
        Calendar temp = Calendar.getInstance();
        //  temp.setTime(eventStartTime);
        //	event.gets
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(eventDate);
        beginTime.ic_add(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
        beginTime.ic_add(Calendar.MINUTE, temp.get(Calendar.MINUTE));
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(eventDate);
        // temp.setTime(eventEndTime);
        endTime.ic_add(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
        endTime.ic_add(Calendar.MINUTE, temp.get(Calendar.MINUTE));
        endMillis = endTime.getTimeInMillis();


        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events
                .DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, event.getName() + ": " + event.getFunctionname());
        values.put(CalendarContract.Events.DESCRIPTION, event.getMessage());
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, beginTime.getTimeZone().getID());
        Uri uri;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context,"Permission denied by user", Toast.LENGTH_SHORT).show();
            return 1;
        }else {
            uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        }

// get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
            Toast.makeText(context,"Event added to device calender", Toast.LENGTH_SHORT).show();

        return  eventID;
    }

    public static String writeListToJsonArray(ArrayList<ProductVO> list ) throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        String data =mapper.writeValueAsString(list);

        System.out.println(new String(data));
        return data;
    }*/

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }
    public static File persistImage(Context context, Bitmap bitmap) {
        String name = "talaat_img";
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }

    public static Bitmap getRoundedCornerBitmap(Context context, Bitmap input, int pixels , int w , int h , boolean squareTL, boolean squareTR, boolean squareBL, boolean squareBR  ) {

        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);

        //make sure that our rounded corner is scaled appropriately
        final float roundPx = pixels*densityMultiplier;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);


        //draw rectangles over the corners we want to be square
        if (squareTL ){
            canvas.drawRect(0, h/2, w/2, h, paint);
        }
        if (squareTR ){
            canvas.drawRect(w/2, h/2, w, h, paint);
        }
        if (squareBL ){
            canvas.drawRect(0, 0, w/2, h/2, paint);
        }
        if (squareBR ){
            canvas.drawRect(w/2, 0, w, h/2, paint);
        }


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(input, 0,0, paint);

        return output;
    }

    public static String getLocationURL(String lat, String lon){

        String location ="https://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lon+"&zoom=14&size=400x200&markers="+lat+","+lon+"&path=color:0x0000FF80";
        return location;
    }

    public static void setBackground(View view){
        int color = Color.WHITE; //red for example
        float radius = 18f; //radius will be 5px
        int strokeWidth = 2;

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setStroke(strokeWidth, color);
        view.setBackground(gradientDrawable);
    }

    public static boolean haveNetworkConnection(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public static String getMonths(int pos){

        ArrayList<String> popup = new ArrayList<>();
        popup.add("January");
        popup.add("February");
        popup.add("March");
        popup.add("April");
        popup.add("May");
        popup.add("June");
        popup.add("July");
        popup.add("August");
        popup.add("September");
        popup.add("October");
        popup.add("November");
        popup.add("December");
        return popup.get(pos);
    }
}
