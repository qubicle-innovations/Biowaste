package uems.biowaste.utils;

import android.content.Context;

/**
 * Created by aswin on 08/12/16.
 */

public class Constants {

    public static final String ARABIC_LOCALE = "ar";
    public static final String ENGLISH_LOCALE = "en";
    public static final String NAMESPACE = "http://Coleague.com/";
    public static final String SOAP_ACTION = "http://Coleague.com/";

    public static String SERVER_URL = "http://uemssqlvm.cloudapp.net/HPBSBiowaste_WebService/api/Service";

    // public static String USER_LOGIN = "http://uemssqlvm.cloudapp.net/HPBSV3-MasterAppService/HPBSMasterAppService.asmx";
    public static String USER_LOGIN = "https://www.uetracksg.com/hpbs/MasterAppWS/HPBSMasterAppService.asmx";

    public static String FETCH_COUNT = SERVER_URL + "/GetMonthlyDetails";

    public static String GET_GWASTE_LIST = SERVER_URL + "/GetFoodandGeneralwaste";
    public static String GET_GWASTE_DETAILS = SERVER_URL + "/GetFoodandGeneralwasteList";
    public static String CREATE_GWASTE_DETAILS = SERVER_URL + "/SaveFoodandGeneralwaste";
    public static String CREATE_RECYCLED_DETAILS = SERVER_URL + "/SaveRecycleditems";
    public static String GET_BIOWASTE_LIST = SERVER_URL + "/GetBiowaste";
    public static String GET_BIOWASTE_DETAILS = SERVER_URL + "/GetBiowasteList";
    public static String GET_BIOWASTE_CREATE = SERVER_URL + "/SaveBiowaste";
    public static String GET_BIOWASTE_VALUE = SERVER_URL + "/GetBioWasteValue";
    public static String GET_RECYCLED_LIST = SERVER_URL + "/GetRecycleditems";
    public static String GET_RECYCLED_DETAILS = SERVER_URL + "/GetRecycleditemsList";
    public static String GET_PATIENT_LIST = SERVER_URL + "/GetMonthlypatients";
    public static String CREATE_PATIENTS_DETAILS = SERVER_URL + "/SaveMonthlypatients";
    public static String METHOD_VALIDATE_USER = "ValidateUser";
    public static String DELETE_ITEM = SERVER_URL + "/DeleteRecords";

    public static final String FRAGMENT_LOGIN = "FRAGMENT_LOGIN";
    public static final String FRAGMENT_DASHBOARD = "FRAGMENT_DASHBOARD";
    public static final String FRAGMENT_FOOD_AND_GENERAL_WASTE = "FRAGMENT_FOOD_AND_GENERAL_WASTE";
    public static final String FRAGMENT_BIOWASTE = "FRAGMENT_BIOWASTE";
    public static final String FRAGMENT_RECYCLED_ITEMS = "FRAGMENT_RECYCLED_ITEMS";
    public static final String FRAGMENT_MONTHLY_PATIENTS = "FRAGMENT_MONTHLY_PATIENTS";
    public static final String FRAGMENT_FOOD_AND_GENERAL_WASTE_CREATE = "FRAGMENT_FOOD_AND_GENERAL_WASTE_CREATE";
    public static final String FRAGMENT_BIOWASTE_CREATE = "FRAGMENT_BIOWASTE_CREATE";
    public static final String FRAGMENT_RECYCLED_ITEMS_CREATE = "FRAGMENT_RECYCLED_ITEMS_CREATE";
    public static final String FRAGMENT_MONTHLY_PATIENTS_CREATE = "FRAGMENT_MONTHLY_PATIENTS_CREATE";
    public static final String FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS = "FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS";
    public static final String FRAGMENT_BIOWASTE_DETAILS = "FRAGMENT_BIOWASTE_DETAILS";
    public static final String FRAGMENT_RECYCLED_ITEMS_DETAILS = "FRAGMENT_RECYCLED_ITEMS_DETAILS";
    public static final String FRAGMENT_MONTHLSY_PATIENTS_DETAILS = "FRAGMENT_MONTHLSY_PATIENTS_DETAILS";

    public static final String FRAGMENT_FOOD_AND_GENERAL_WASTE_TYPE_ID = "1";
    public static final String FRAGMENT_BIOWASTE_TYPE_ID = "2";
    public static final String FRAGMENT_RECYCLED_ITEMS_TYPE_ID = "3";
    public static final String FRAGMENT_MONTHLY_PATIENTS_TYPE_ID = "4";


    public Constants(Context context) {

        String url = Utils.getSharedPreference(context, "url");
        if (url != null) {
            SERVER_URL = url;
            FETCH_COUNT = SERVER_URL + "/GetMonthlyDetails";
            GET_GWASTE_LIST = SERVER_URL + "/GetFoodandGeneralwaste";
            GET_GWASTE_DETAILS = SERVER_URL + "/GetFoodandGeneralwasteList";
            CREATE_GWASTE_DETAILS = SERVER_URL + "/SaveFoodandGeneralwaste";
            CREATE_RECYCLED_DETAILS = SERVER_URL + "/SaveRecycleditems";
            GET_BIOWASTE_LIST = SERVER_URL + "/GetBiowaste";
            GET_BIOWASTE_DETAILS = SERVER_URL + "/GetBiowasteList";
            GET_BIOWASTE_CREATE = SERVER_URL + "/SaveBiowaste";
            GET_RECYCLED_LIST = SERVER_URL + "/GetRecycleditems";
            GET_RECYCLED_DETAILS = SERVER_URL + "/GetRecycleditemsList";
            GET_PATIENT_LIST = SERVER_URL + "/GetMonthlypatients";
            CREATE_PATIENTS_DETAILS = SERVER_URL + "/SaveMonthlypatients";
            METHOD_VALIDATE_USER = "ValidateUser";
            DELETE_ITEM = SERVER_URL + "/DeleteRecords";
            GET_BIOWASTE_VALUE = SERVER_URL + "/GetBioWasteValue";
        }

    }
}
