package edu.secourse.patientportal.util;

public class Constants {
    // ==================== HTTP Status Code Messages ====================

    /** 200 OK - Request successful */
    public static final String HTTP_OK = "OK";

    /** 201 Created - Resource successfully created */
    public static final String HTTP_CREATED = "CREATED";

    /** 204 No Content - Successful deletion */
    public static final String HTTP_NO_CONTENT = "NO_CONTENT";

    /** 400 Bad Request - Invalid input validation error */
    public static final String HTTP_BAD_REQUEST = "Bad Request - Invalid input";

    /** 401 Unauthorized - Missing or invalid JWT token */
    public static final String HTTP_UNAUTHORIZED = "Unauthorized - Invalid or missing token";

    /** 403 Forbidden - User lacks permission */
    public static final String HTTP_FORBIDDEN = "Forbidden - Access denied";

    /** 404 Not Found - Resource doesn't exist */
    public static final String HTTP_NOT_FOUND = "Not Found - Resource does not exist";

    /** 409 Conflict - Resource already exists (e.g., duplicate username) */
    public static final String HTTP_CONFLICT = "Conflict - Resource already exists";

    /** 500 Internal Server Error - Unexpected server error */
    public static final String HTTP_INTERNAL_ERROR = "Internal Server Error";

    /** 503 Service Unavailable - Service temporarily down */
    public static final String HTTP_SERVICE_UNAVAILABLE = "Service Unavailable";

    // ==================== Business Constants ====================

    /** AquaWorld Store Name */
    public static final String COMPANY_NAME = "MACLOGIX";

    /** Store Description - Guppy specialization */
    public static final String PATIENT_APPOINTMENT_APP_DESCRIPTION = "Premium medical services for you!";

//    /** Default page size for pagination */
//    public static final int DEFAULT_PAGE_SIZE = 10;
//
//    /** Maximum page size */
//    public static final int MAX_PAGE_SIZE = 100;

    /** JWT token validity period in milliseconds (1 hour) */
    public static final long JWT_EXPIRATION_TIME = 3600000;

    // ==================== Product Categories ====================

    /** Guppy fish category */
    public static final String CATEGORY_GUPPIES = "guppies";

    /** Fish food category */
    public static final String CATEGORY_FISH_FOOD = "fish_food";

    /** Aquarium equipment category */
    public static final String CATEGORY_EQUIPMENT = "equipment";

    /** Decorations category */
    public static final String CATEGORY_DECORATIONS = "decorations";

    /** Medicines category */
    public static final String CATEGORY_MEDICINES = "medicines";

    // ==================== Appointment Status ====================

    /** Order is pending (awaiting confirmation) */
    public static final String ORDER_STATUS_PENDING = "PENDING";

    /** Order confirmed by customer */
    public static final String ORDER_STATUS_CONFIRMED = "CONFIRMED";


    /** Appointment has been cancelled */
    public static final String APPOINTMENT_STATUS_CANCELLED = "CANCELLED";


    // ==================== User Roles ====================

    /** Regular customer role */
    public static final String ROLE_DOCTOR = "DOCTOR";

    /** Administrator role */
    public static final String ROLE_ADMIN = "ADMIN";

    /** Vendor role */
    public static final String ROLE_PATIENT = "PATIENT";

    // ==================== Validation Messages ====================

    public static final String MSG_USERNAME_REQUIRED = "Username is required";
    public static final String MSG_EMAIL_REQUIRED = "Email is required";
    public static final String MSG_PASSWORD_REQUIRED = "Password is required and must be at least 8 characters";
    public static final String MSG_FIRSTNAME_REQUIRED = "First name is required";
    public static final String MSG_LASTNAME_REQUIRED = "Last name is required";

    public static final String MSG_USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String MSG_EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String MSG_USER_NOT_FOUND = "User not found";
    public static final String MSG_INVALID_CREDENTIALS = "Invalid username or password";

    public static final String MSG_APPOINTMENT_CANNOT_CANCEL = "Cannot cancel appointment in current status";
    public static final String MSG_APPOINTMENT_CANNOT_UPDATE = "Cannot update appointment in current status";

    public static final String MSG_UNAUTHORIZED_ACCESS = "You are not authorized to access this resource";
    public static final String MSG_TOKEN_INVALID = "Invalid or expired token";

    public static final String MSG_REQUEST_INVALID = "Invalid request - please check your input";
    public static final String MSG_INTERNAL_ERROR = "An unexpected error occurred";

    // ==================== Success Messages ====================

    public static final String MSG_REGISTRATION_SUCCESS = "Registration successful";
    public static final String MSG_LOGIN_SUCCESS = "Login successful";
    public static final String MSG_LOGOUT_SUCCESS = "Logout successful";
    public static final String MSG_PROFILE_UPDATED = "Profile updated successfully";
    public static final String MSG_APPOINTMENT_CREATED = "Appointment created successfully";
    public static final String MSG_APPOINTMENT_UPDATED = "Appointment updated successfully";
    public static final String MSG_APPOINTMENT_CANCELLED = "Appointment cancelled successfully";

    // ==================== API Paths ====================

    public static final String API_VERSION = "/maclogixapi/v1";
    public static final String AUTH_ENDPOINT = API_VERSION + "/auth";
    public static final String AUTH_LOGIN_ENDPOINT = API_VERSION + "/auth/login";
    public static final String PATIENTS_ENDPOINT = API_VERSION + "/patients";
    public static final String APPOINTMENT_ENDPOINT = API_VERSION + "/appointments";
    public static final String USERS_ENDPOINT = API_VERSION + "/users";
}
