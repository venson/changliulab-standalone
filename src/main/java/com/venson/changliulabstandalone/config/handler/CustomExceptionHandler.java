package com.venson.changliulabstandalone.config.handler;

import com.venson.changliulabstandalone.entity.vo.ErrorMessage;
import com.venson.changliulabstandalone.utils.ResUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
        import org.springframework.web.bind.annotation.ControllerAdvice;
        import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorMessage> handleAuthenticationException(AuthenticationException ex) {
        // handle the exception and return an appropriate response
        logger.info("response handler");
        logger.info(ex.getMessage());
        logger.info(ex.getCause());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("Access-Control-Allow-Methods", "*")
                .header("Access-Control-Allow-Origin", "*")
//                .header("Access-Control-Allow-", "*")
                .body(new ErrorMessage("Authentication failed at controller advice"));
    }
    @ExceptionHandler({ InsufficientAuthenticationException.class })
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleInsuffException(AuthenticationException ex) {
        logger.info("response handler: InsufficientAuthenticationException exception");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).
                header("Access-Control-Allow-Methods", "*")
                .header("Access-Control-Allow-Origin", "*")
                .body(new ErrorMessage("Not enough auth"));
    }
    @ExceptionHandler({ ResponseStatusException.class })
    @ResponseBody
    public ResponseEntity<ErrorMessage> handle401(ResponseStatusException ex) {
        logger.info("response handler: ResponseStatus Exception exception");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                header("Access-Control-Allow-Methods", "*")
                .header("Access-Control-Allow-Origin", "*")
                .body(new ErrorMessage("Not enough auth"));
    }
}
