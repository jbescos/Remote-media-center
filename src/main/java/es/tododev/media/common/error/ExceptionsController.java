package es.tododev.media.common.error;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsController {

	private final Logger logger = LoggerFactory.getLogger(ExceptionsController.class);
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleError(HttpServletRequest req, Exception ex) {
		logger.error("Request: " + req.getRequestURL() + " raised ", ex);

		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		return ResponseEntity.status(apiError.getStatus()).body(apiError);
	}

}
