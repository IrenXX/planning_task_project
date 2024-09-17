package ru.kemova.task_planning.exception.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.kemova.task_planning.exception.ConfirmationNotSuccessfullyException;
import ru.kemova.task_planning.exception.PasswordsNotSameException;
import ru.kemova.task_planning.exception.UserAlreadyExistException;
import ru.kemova.task_planning.exception.UserNotAuthenticatedException;

@RestControllerAdvice
public class HandlerError extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ProjectError> handleAuthenticationException(Exception ex) {
        return new ResponseEntity<>(new ProjectError(HttpStatus.UNAUTHORIZED.value(), MessageError.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ProjectError> handleUserAlreadyExist() {
        return new ResponseEntity<>(new ProjectError(HttpStatus.CONFLICT.value(), MessageError.USER_ALREADY_EXIST), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PasswordsNotSameException.class)
    public ResponseEntity<ProjectError> handlePasswordsNotSame() {
        return new ResponseEntity<>(new ProjectError(HttpStatus.BAD_REQUEST.value(), MessageError.PASSWORDS_ARE_NOT_EQUAL), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ProjectError> badCredentialsException() {
        return new ResponseEntity<>(new ProjectError(HttpStatus.UNAUTHORIZED.value(), MessageError.CREDENTIALS_IS_NOT_VALID), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ProjectError> notAuthenticated() {
        return new ResponseEntity<>(new ProjectError(HttpStatus.UNAUTHORIZED.value(), MessageError.JWT_TOKEN_NOT_VALID), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ConfirmationNotSuccessfullyException.class)
    public ResponseEntity<ProjectError> notConfirmed() {
        return new ResponseEntity<>(new ProjectError(HttpStatus.BAD_REQUEST.value(), MessageError.CONFIRMATION_TOKEN_NOT_VALID), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<ProjectError> notFound() {
        return new ResponseEntity<>(new ProjectError(HttpStatus.NOT_FOUND.value(), MessageError.USER_NOT_FOUND), HttpStatus.NOT_FOUND);
    }
}
