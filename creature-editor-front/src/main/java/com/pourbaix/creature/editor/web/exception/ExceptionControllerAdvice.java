package com.pourbaix.creature.editor.web.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.pourbaix.creature.editor.web.parameter.ParameterController;
import com.pourbaix.infinity.service.ServiceException;

@ControllerAdvice(annotations = RestController.class)
public class ExceptionControllerAdvice {

	private static final Logger logger = LoggerFactory.getLogger(ParameterController.class);

	@ExceptionHandler
	ResponseEntity<String> httpMessageConversionException(HttpMessageConversionException ex) {
		logger.error(ExceptionUtils.getStackTrace(ex));
		HttpHeaders headers = new HttpHeaders();
		headers.set("HeaderKey", "HeaderDetails");
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Conversion error", headers, HttpStatus.BAD_REQUEST);
		return responseEntity;
	}

	@ExceptionHandler
	ResponseEntity<String> ServiceException(ServiceException ex) {
		logger.error(ExceptionUtils.getStackTrace(ex));
		HttpHeaders headers = new HttpHeaders();
		headers.set("HeaderKey", "HeaderDetails");
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("ServiceException error", headers, HttpStatus.INTERNAL_SERVER_ERROR);
		return responseEntity;
	}

}
