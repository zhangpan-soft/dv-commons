package com.dv.commons.base;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;

public enum DefaultStatus implements BaseStatus{
    OK(0, "OK"),

    CONTINUE(100,  "Continue"),
    SWITCHING_PROTOCOLS(101,  "Switching Protocols"),
    PROCESSING(102,  "Processing"),
    EARLY_HINTS(103,  "Early Hints"),

    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
    NO_CONTENT(204, "No Content"),
    RESET_CONTENT(205, "Reset Content"),
    PARTIAL_CONTENT(206, "Partial Content"),
    MULTI_STATUS(207, "Multi-Status"),
    ALREADY_REPORTED(208, "Already Reported"),
    IM_USED(226, "IM Used"),
    MULTIPLE_CHOICES(300,  "Multiple Choices"),
    MOVED_PERMANENTLY(301,  "Moved Permanently"),
    FOUND(302,  "Found"),
    SEE_OTHER(303,  "See Other"),
    NOT_MODIFIED(304,  "Not Modified"),
    TEMPORARY_REDIRECT(307,  "Temporary Redirect"),
    PERMANENT_REDIRECT(308,  "Permanent Redirect"),
    BAD_REQUEST(400,  "Bad Request"),
    UNAUTHORIZED(401,  "Unauthorized"),
    PAYMENT_REQUIRED(402,  "Payment Required"),
    FORBIDDEN(403,  "Forbidden"),
    NOT_FOUND(404,  "Not Found"),
    METHOD_NOT_ALLOWED(405,  "Method Not Allowed"),
    NOT_ACCEPTABLE(406,  "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407,  "Proxy Authentication Required"),
    REQUEST_TIMEOUT(408,  "Request Timeout"),
    CONFLICT(409,  "Conflict"),
    GONE(410,  "Gone"),
    LENGTH_REQUIRED(411,  "Length Required"),
    PRECONDITION_FAILED(412,  "Precondition Failed"),
    PAYLOAD_TOO_LARGE(413,  "Payload Too Large"),
    /** @deprecated */
    @Deprecated
    REQUEST_ENTITY_TOO_LARGE(413,  "Request Entity Too Large"),
    URI_TOO_LONG(414,  "URI Too Long"),
    /** @deprecated */
    @Deprecated
    REQUEST_URI_TOO_LONG(414,  "Request-URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE(415,  "Unsupported Media Type"),
    REQUESTED_RANGE_NOT_SATISFIABLE(416,  "Requested range not satisfiable"),
    EXPECTATION_FAILED(417,  "Expectation Failed"),
    I_AM_A_TEAPOT(418,  "I'm a teapot"),
    /** @deprecated */
    @Deprecated
    INSUFFICIENT_SPACE_ON_RESOURCE(419,  "Insufficient Space On Resource"),
    /** @deprecated */
    @Deprecated
    METHOD_FAILURE(420,  "Method Failure"),
    /** @deprecated */
    @Deprecated
    DESTINATION_LOCKED(421,  "Destination Locked"),
    UNPROCESSABLE_ENTITY(422,  "Unprocessable Entity"),
    LOCKED(423,  "Locked"),
    FAILED_DEPENDENCY(424,  "Failed Dependency"),
    TOO_EARLY(425,  "Too Early"),
    UPGRADE_REQUIRED(426,  "Upgrade Required"),
    PRECONDITION_REQUIRED(428,  "Precondition Required"),
    TOO_MANY_REQUESTS(429,  "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431,  "Request Header Fields Too Large"),
    UNAVAILABLE_FOR_LEGAL_REASONS(451,  "Unavailable For Legal Reasons"),
    INTERNAL_SERVER_ERROR(500,  "Internal Server Error"),
    NOT_IMPLEMENTED(501,  "Not Implemented"),
    BAD_GATEWAY(502,  "Bad Gateway"),
    SERVICE_UNAVAILABLE(503,  "Service Unavailable"),
    GATEWAY_TIMEOUT(504,  "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED(505,  "HTTP Version not supported"),
    VARIANT_ALSO_NEGOTIATES(506,  "Variant Also Negotiates"),
    INSUFFICIENT_STORAGE(507,  "Insufficient Storage"),
    LOOP_DETECTED(508,  "Loop Detected"),
    BANDWIDTH_LIMIT_EXCEEDED(509,  "Bandwidth Limit Exceeded"),
    NOT_EXTENDED(510,  "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED(511,  "Network Authentication Required"),
    ;
    private final int code;
    private final String message;
    private String key;
    DefaultStatus(int code, String message, String key){
        this.code = code;
        this.message = message;
        this.key = key;
        if (this.key == null||this.key.isBlank()) {
            this.key = "status."+code;
        }
    }

    private static final ResourceBundleMessageSource MESSAGE_SOURCE;

    static {
        MESSAGE_SOURCE = new ResourceBundleMessageSource();
        MESSAGE_SOURCE.setBundleClassLoader(DefaultStatus.class.getClassLoader());
        MESSAGE_SOURCE.setBasename("messages/status");
//        MESSAGE_SOURCE.setDefaultEncoding("utf-8");
    }
    
    DefaultStatus(int code,String defaultMessage){
        this(code,defaultMessage,null);
    }

    @Override
    public Serializable code() {
        return this.code;
    }

    @Override
    public String desc() {
        return this.message;
    }


    @Override
    public String message(Locale locale, Object... args) {
        String message1 = MESSAGE_SOURCE.getMessage(this.key, args, "", locale);
        if (message1==null||message1.isBlank()){
            message1 = new MessageFormat(this.message).format(args);
            return message1;
        }
        return MESSAGE_SOURCE.getMessage(this.key,args,this.message,locale);
    }

    @Override
    public String message() {
        return this.message(Locale.getDefault());
    }
}
