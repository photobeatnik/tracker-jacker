package at.fhj.mobcomp.trackerjacker.commons;

public class Constants {

    public static final String PREFIX = "tj";
    public static final String SEPARATOR = ":";

    public static final String LOCATION_MESSAGE = PREFIX + SEPARATOR + "loc";

    public static final String SMS_SENT = "SMS_SENT";
    public static final String SMS_DELIVERED = "SMS_DELIVERED";
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public static final int HIGHEST_PRIORITY = 999;

    public static final String KEY_ADDRESS = "address";
    public static final String KEY_LATITUDE = "lat";
    public static final String KEY_LONGITUDE = "long";
    public static final String KEY_METHOD = "method";
    public static final String KEY_PROVIDER = "provider";

    /** Key for getting PDUs from bundle object. */
    public static final String PDUS_KEY = "pdus";

    private Constants() {
        // Util class. No instance needed.
    }
}
