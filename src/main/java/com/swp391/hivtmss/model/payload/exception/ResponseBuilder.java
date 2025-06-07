package com.swp391.hivtmss.model.payload.exception;

import com.swp391.hivtmss.model.payload.response.ListResponse;
import com.swp391.hivtmss.util.DateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseBuilder {

    public static ResponseEntity<Object> returnData(HttpStatus httpStatus, String message, Object responseObject) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("http_status", httpStatus.value());
        response.put("time_stamp", DateUtil.formatTimestamp(new Date(), DateUtil.DATE_TIME_FORMAT));
        response.put("message", message);
        response.put("data", responseObject);

        return new ResponseEntity<>(response, httpStatus);

    }

    public static ResponseEntity<Object> returnMessage(HttpStatus httpStatus, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("http_status", httpStatus.value());
        response.put("time_stamp", DateUtil.formatTimestamp(new Date(), DateUtil.DATE_TIME_FORMAT));
        response.put("message", message);
        return new ResponseEntity<>(response, httpStatus);
    }

    public static ResponseEntity<Object> returnPagination(HttpStatus httpStatus, String message, ListResponse listResponse) {
        return returnData(httpStatus, message, listResponse);

    }

}
