package at.fhj.mobcomp.trackerjacker.util;

public class Constants {

    /** Prefix which indicates this is a message from the Tracker Jacker app. */
    private static final String MSG_PREFIX = "tj";

    public static final String SEPARATOR = ":";

    /** Indicates the command for the getting the location. */
    // TODO make configurable?
    private static final String GET_LOCATION_CMD = "whereareyou";

    /** Indicates the location message of the application. */
    public static final String GET_LOCATION_MSG = MSG_PREFIX + SEPARATOR + GET_LOCATION_CMD;

    public static final String SEND_LOCATION_MSG = MSG_PREFIX + SEPARATOR + "loc(%s)";

    public static final String ACTUAL_LOCATION_METHOD = "al";

    public static final String LAST_KNOWN_LOCATION_METHOD = "lkl";

    public static final String UNKOWN_LOCATION = "unknown";

    public static final String DESTINATION_KEY = "destinationAddress";

    private Constants() {
        // util class not for instantiation.
    }

}
