package org.marno.drugsafetyreport.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DrugSafetyReportNotFoundExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(DrugSafetyReportNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse drugSafetyReportNotFoundHandler(DrugSafetyReportNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}

