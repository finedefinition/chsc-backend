package ua.dlc.chscbackend.constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApplicationConstants {
    private ApplicationConstants() {
        // restrict instantiation
    }

    public static final LocalDateTime START_DATE = LocalDateTime
            .parse("2023-01-12T14:30:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    public static final LocalDateTime END_DATE = LocalDateTime.now();

//    public static final String  STATUS_201 = "201";
//    public static final String  MESSAGE_201 = "Account created successfully";
//    public static final String  STATUS_200 = "200";
//    public static final String  MESSAGE_200 = "Request processed successfully";
//    public static final String  STATUS_417 = "417";
//    public static final String  MESSAGE_417_UPDATE= "Update operation failed. Please try again or contact Dev team";
//    public static final String  MESSAGE_417_DELETE= "Delete operation failed. Please try again or contact Dev team";
    // public static final String  STATUS_500 = "500";
    // public static final String  MESSAGE_500 = "An error occurred. Please try again or contact Dev team";

}
