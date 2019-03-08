package uems.biowaste.utils;

/**
 * Created by aswin on 08/12/16.
 */

public class Constants {

    public static final String ARABIC_LOCALE = "ar";
    public static final String ENGLISH_LOCALE = "en";
    public static final String NAMESPACE = "http://Coleague.com/";
    public static final String SOAP_ACTION = "http://Coleague.com/";

    public static String SERVER_URL ="http://119.82.97.221/HPBSBiowaste/api/Service";

   public static String USER_LOGIN ="http://119.82.97.221/HPBSMasterAppWS/HPBSMasterAppService.asmx";
   public static String FETCH_COUNT=SERVER_URL+"/GetMonthlyDetails";;
    public static String GET_TROLLEY_NO =SERVER_URL+"GetTrolleyNo";
    public static String GET_RETURN_LIST =SERVER_URL+"GetReturnList";
    public static String GET_GWASTE_LIST =SERVER_URL+"/GetFoodandGeneralwaste";
    public static String GET_GWASTE_DETAILS =SERVER_URL+"/GetFoodandGeneralwasteList";
    public static String CREATE_GWASTE_DETAILS =SERVER_URL+"/SaveFoodandGeneralwaste";
    public static String CREATE_RECYCLED_DETAILS =SERVER_URL+"/SaveRecycleditems";
    public static String GET_BIOWASTE_LIST =SERVER_URL+"/GetBiowaste";
    public static String GET_BIOWASTE_DETAILS =SERVER_URL+"/GetBiowasteList";
    public static String GET_BIOWASTE_CREATE =SERVER_URL+"/SaveBiowaste";
    public static String GET_RECYCLED_LIST =SERVER_URL+"/GetRecycleditems";
    public static String GET_RECYCLED_DETAILS =SERVER_URL+"/GetRecycleditemsList";
    public static String GET_PATIENT_LIST =SERVER_URL+"/GetMonthlypatients";
    public static String CREATE_PATIENTS_DETAILS =SERVER_URL+"/SaveMonthlypatients";
    public static String SAVE_RETURN_ITEMS =SERVER_URL+"SaveReturnItems";
    public static String SAVE_ISSUE_ITEMS =SERVER_URL+"SaveIssueItems";
    public static String METHOD_VALIDATE_USER = "ValidateUser";


}
