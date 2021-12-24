package com.ketul.demo.exception;

public class MultipleExceptionHandler {
	private String errorField;
	private String errorMessage;
	
	public MultipleExceptionHandler() {
		super();
	}

	public MultipleExceptionHandler(String errorField, String errorMessage) {
		super();
		this.errorField = errorField;
		this.errorMessage = errorMessage;
	}

	public String getErrorField() {
		return errorField;
	}

	public void setErrorField(String errorField) {
		this.errorField = errorField;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "MultipleExceptionHandler [errorField=" + errorField + ", errorMessage=" + errorMessage + "]";
	}
}
