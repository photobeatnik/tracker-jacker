package at.fhj.mobcomp.trackerjacker.util;

public class Constants {

    /** Prefix which indicates this is a message from the Tracker Jacker app. */
    private static final String MSG_PREFIX = "tj";

    /** Indicates the command for the getting the location. */
    // TODO Move this somewhere global (also relevant for sending) and make configurable?
    private static final String GET_LOCATION_CMD = "whereareyou";

    /** Indicates the location message of the application. */
    public static final String GET_LOCATION_MSG = MSG_PREFIX + ":" + GET_LOCATION_CMD;

    public static final String KNOWN_LOCATION_MSG = MSG_PREFIX + ":loc(%f:%f)";

    public static final String UNKNOWN_LOCATION_MSG = MSG_PREFIX + ":loc(unknown)";

    public static final String DESTINATION_KEY = "destinationAddress";

    private Constants() {
        // util class not for instantiation.
    }

}
