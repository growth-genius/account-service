package com.sgyj.accountservice.infra.advice.exceptions;

import org.springframework.validation.Errors;

public class FormValidationException extends RuntimeException {

    public FormValidationException () {super();}

    public FormValidationException ( Errors errors ) {super( errors.getFieldErrors().get( 0 ).getDefaultMessage() );}
}
