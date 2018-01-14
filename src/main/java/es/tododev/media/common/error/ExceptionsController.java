package es.tododev.media.common.error;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsController {

	private final static Logger log = LogManager.getLogger();
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleError(HttpServletRequest req, Exception ex) {
		log.error("Request: " + req.getRequestURL() + " raised ", ex);

		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		return ResponseEntity.status(apiError.getStatus()).body(apiError);
	}

}
