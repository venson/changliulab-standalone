package com.venson.changliulabstandalone.config.handler;

import com.venson.changliulabstandalone.entity.vo.ErrorMessage;
import com.venson.changliulabstandalone.exception.CustomizedException;
import com.venson.changliulabstandalone.utils.ResUtils;
import com.venson.changliulabstandalone.utils.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static com.venson.changliulabstandalone.utils.ResultCode.ERROR;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
//    public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> AccessDeniedError(Exception e){
//            return ResUtils.forbidden();
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .header("Access-Control-Allow-Methods", "*")
                .body(new ErrorMessage("Not enough authorization"));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorMessage> status(ResponseStatusException e){
        log.info("response exception" + e.getReason());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("Access-Control-Allow-Methods", "*")
                .body(new ErrorMessage("Authentication failed at controller advice"));
//        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @ExceptionHandler({ AuthenticationException.class })
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleAuthenticationException(AuthenticationException ex) {
        log.info("global authentication exception");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("Access-Control-Allow-Methods", "*")
                .body(new ErrorMessage("Authentication failed at controller advice"));
    }
    @ExceptionHandler({ InsufficientAuthenticationException.class })
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleInsuffException(AuthenticationException ex) {
        log.info("InsufficientAuthenticationException exception");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).
                header("Access-Control-Allow-Methods", "*")
                .body(new ErrorMessage("Not enough auth"));
    }
    @ExceptionHandler(CustomizedException.class)
    public ResponseEntity<ErrorMessage> CustomizedExceptionError(CustomizedException e){
        if(ERROR.getValue() == e.getCode()){
            return ResUtils.internalError(e.getMessage());
        }else{
            return ResUtils.internalError();
        }
    }
    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public Result<String> IllegalExceptionError(Exception e){
        return Result.error(e.getMessage());
    }
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorMessage> methodArgumentError(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder message = new StringBuilder();

        for (FieldError error :
                fieldErrors) {
            message.append(error.getDefaultMessage()).append(". ");
        }
        return ResUtils.internalError(message.toString());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> error(Exception e){
        log.info("all" + e.getMessage());
        return ResUtils.internalError();
    }
}
