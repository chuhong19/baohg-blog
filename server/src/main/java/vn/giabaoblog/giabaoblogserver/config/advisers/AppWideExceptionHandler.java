package vn.giabaoblog.giabaoblogserver.config.advisers;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.giabaoblog.giabaoblogserver.config.exception.*;
import vn.giabaoblog.giabaoblogserver.data.dto.response.StandardResponse;

import java.util.List;

@Slf4j
@RestControllerAdvice
@CrossOrigin
public class AppWideExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("handleException", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StandardResponse.create("500", "ServerError", e.getLocalizedMessage()));
    }

    @ExceptionHandler(PaymentDuplicateException.class)
    public ResponseEntity<?> handlePaymentNotFoundException(PaymentDuplicateException e) {
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(StandardResponse.create("402", "PaymentDuplicateException", e.getMessage()));
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<?> handlePaymentNotFoundException(PaymentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(StandardResponse.create("402", "PaymentNotFoundException", e.getMessage()));
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<?> handleInsufficientBalanceException(InsufficientBalanceException e) {
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(StandardResponse.create("402", "InsufficientBalanceException", e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StandardResponse.create("404", "NotFound", e.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenException(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(StandardResponse.create("403", "Forbidden", e.getMessage()));
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(org.springframework.security.access.AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(StandardResponse.create("403", "Forbidden", e.getMessage()));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleAuthorizedException(JwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(StandardResponse.create("401", "Unauthorized", e.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleAuthorizedException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(StandardResponse.create("401", "Unauthorized", e.getMessage()));
    }

    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<?> handleValidationException(ValidateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StandardResponse.create("400", "BadRequest", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        List<String> _messages = e.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StandardResponse.create("400", "BadRequest", _messages));
    }

    @ExceptionHandler(InvalidUserDataException.class)
    public ResponseEntity<?> handleInvalidUserDataException(InvalidUserDataException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StandardResponse.create("404", "NotFound", e.getMessage()));
    }

    @ExceptionHandler(InvalidPermissionDataException.class)
    public ResponseEntity<?> handleInvalidPermissionDataException(InvalidPermissionDataException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StandardResponse.create("404", "Invalid permission data", e.getMessage()));
    }

    @ExceptionHandler(AccessException.class)
    public ResponseEntity<?> handleAccessException(AccessException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StandardResponse.create("400", "Cannot access this action", e.getMessage()));
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<?> handleDuplicateException(DuplicateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StandardResponse.create("400", "Duplicate record", e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StandardResponse.create("400", "Illegal state exception", e.getMessage()));
    }

    @ExceptionHandler(NotEnoughException.class)
    public ResponseEntity<?> handleNotEnoughException(NotEnoughException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StandardResponse.create("400", "Not enough exception", e.getMessage()));
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<?> handleTokenRefreshException(TokenRefreshException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StandardResponse.create("400", "Refresh token not found", e.getMessage()));
    }
}