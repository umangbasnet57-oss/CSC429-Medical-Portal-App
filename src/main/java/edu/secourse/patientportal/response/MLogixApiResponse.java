package edu.secourse.patientportal.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Generic response wrapper used to standardize API responses.
 *
 * <p>This class provides a consistent JSON response structure for both
 * successful and failed API operations.
 *
 * <p><b>Response Format:</b>
 * <pre>
 * {
 *   "success": true,
 *   "message": "Operation completed successfully",
 *   "timestamp": "2026-01-18T10:30:00",
 *   "statusCode": 200,
 *   "data": { ... }
 * }
 * </pre>
 *
 * <p><b>Benefits:</b>
 * <ul>
 *     <li>Provides consistent response structure across all controllers</li>
 *     <li>Includes timestamps for auditing and debugging</li>
 *     <li>Supports success and error responses</li>
 *     <li>Supports generic response data of any type</li>
 * </ul>
 *
 * @param <T> type of data included in the response body
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MLogixApiResponse<T> {

    /** Indicates whether the request was processed successfully. */
    private boolean success;

    /** Human-readable response message. */
    private String message;

    /** Time at which the response was created. */
    private LocalDateTime timestamp;

    /** HTTP status code associated with the response. */
    private int statusCode;

    /**
     * Optional response payload.
     *
     * <p>This field is excluded from the JSON response when it is {@code null}.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    /**
     * Creates a successful response with data.
     *
     * @param message response message
     * @param data response payload
     * @param statusCode HTTP status code
     * @param <T> response payload type
     * @return success response containing data
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
     * Creates a successful response without data.
     *
     * @param message response message
     * @param statusCode HTTP status code
     * @param <T> response payload type
     * @return success response with no data payload
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
     * Creates an error response without additional data.
     *
     * @param message error message
     * @param statusCode HTTP error status code
     * @param <T> response payload type
     * @return error response with no data payload
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
     * Creates an error response with additional details.
     *
     * <p>This is useful for returning validation errors or other structured
     * error information.
     *
     * @param message error message
     * @param statusCode HTTP error status code
     * @param data additional error details
     * @param <T> response payload type
     * @return error response containing additional details
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