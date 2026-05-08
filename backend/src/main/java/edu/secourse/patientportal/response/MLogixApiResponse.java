package edu.secourse.patientportal.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Generic API Response Wrapper for MacLogix REST API
 *
 * This class wraps all API responses to provide consistent response format across all endpoints.
 *
 * Response Structure:
 * {
 *   "success": true/false,
 *   "message": "Human readable message",
 *   "timestamp": "2026-01-18T10:30:00",
 *   "statusCode": 200,
 *   "data": { ... }
 * }
 *
 * Benefits:
 * - Consistent response format across all endpoints
 * - Easy for client applications to parse
 * - Includes timestamp for audit/debugging
 * - Supports both single object and paginated data
 *
 * Usage Examples:
 * - Success: ApiResponse.success("Login successful", loginData, 200)
 * - Error: ApiResponse.error("Invalid credentials", 401)
 * - Paginated: ApiResponse.success("Patients retrieved", pageData, 200)
 *
 * @author MacLogix Development Team
 * @param <T> Generic type for response data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MLogixApiResponse<T> {

    /** Indicates whether the request was successful (true) or failed (false) */
    private boolean success;

    /** Human-readable message describing the response */
    private String message;

    /** Timestamp when the response was generated (ISO format) */
    private LocalDateTime timestamp;

    /** HTTP status code (200, 201, 400, 401, 404, 500, etc.) */
    private int statusCode;

    /**
     * Response data (included only if applicable)
     * - For GET requests: the retrieved object or list
     * - For POST requests: the created object
     * - For PUT requests: the updated object
     * - For DELETE requests: null or empty
     *
     * Note: This field is excluded from JSON if null (@JsonInclude annotation)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    /**
     * Creates a successful API response
     *
     * Used when:
     * - GET request returns data
     * - POST request creates resource (201)
     * - PUT request updates resource
     * - Data is found/processed successfully
     *
     * @param message the response message
     * @param data the response data
     * @param statusCode the HTTP status code (usually 200 or 201)
     * @param <T> the type of response data
     * @return ApiResponse with success=true
     */
    public static <T> MLogixApiResponse<T> success(String message, T data, int statusCode) {
        return MLogixApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .statusCode(statusCode)
                .data(data)
                .build();
    }

    /**
     * Creates a successful API response without data
     *
     * Used when:
     * - Resource is deleted successfully (204)
     * - Logout is successful
     * - No data needs to be returned
     *
     * @param message the response message
     * @param statusCode the HTTP status code (usually 200 or 204)
     * @return ApiResponse with success=true and data=null
     */
    public static <T> MLogixApiResponse<T> success(String message, int statusCode) {
        return MLogixApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .statusCode(statusCode)
                .data(null)
                .build();
    }

    /**
     * Creates an error API response
     *
     * Used when:
     * - Invalid input (400)
     * - Unauthorized access (401)
     * - Forbidden access (403)
     * - Resource not found (404)
     * - Resource already exists (409)
     * - Server errors (500, 503)
     *
     * @param message the error message
     * @param statusCode the HTTP error code
     * @param <T> the type of response data
     * @return ApiResponse with success=false and data=null
     */
    public static <T> MLogixApiResponse<T> error(String message, int statusCode) {
        return MLogixApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .statusCode(statusCode)
                .data(null)
                .build();
    }

    /**
     * Creates an error API response with optional data
     *
     * Used when error response needs to include additional information
     * Example: Validation errors list
     *
     * @param message the error message
     * @param statusCode the HTTP error code
     * @param data additional error information (e.g., validation errors)
     * @param <T> the type of response data
     * @return ApiResponse with success=false
     */
    public static <T> MLogixApiResponse<T> error(String message, int statusCode, T data) {
        return MLogixApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .statusCode(statusCode)
                .data(data)
                .build();
    }
}
