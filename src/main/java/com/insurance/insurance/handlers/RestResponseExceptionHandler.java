package com.insurance.insurance.handlers;
import com.insurance.insurance.exceptions.RiskTypeNotFoundException;
import com.insurance.insurance.services.PremiumCalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseExceptionHandler {
    Logger logger = LoggerFactory.getLogger(RestResponseExceptionHandler.class);
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        String errorMessage = cutValidationMessage(ex);
        logger.info("");
        return new ResponseEntity(prepareResponseBody(errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RiskTypeNotFoundException.class)
    public ResponseEntity<Object> handleRiskTypeNotFoundExceptionException(
            RiskTypeNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(prepareResponseBody(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, WebRequest request) {
        return new ResponseEntity<>(prepareResponseBody(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private String cutValidationMessage(MethodArgumentNotValidException ex) {
        String errorMessagePointer = "default message [";
        String cut1 = ex.getMessage().substring(ex.getMessage().indexOf(errorMessagePointer) + 18);
        String start = cut1.substring(cut1.indexOf(errorMessagePointer) + 17);
        String errorMessage = start.substring(0, start.indexOf(']'));
        return errorMessage;
    }

    private Map<String, Object> prepareResponseBody(String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        return body;
    }

}
