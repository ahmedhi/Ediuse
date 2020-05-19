package org.sid.exception;

public class RessourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public RessourceNotFoundException(String message) {
		super(message);
	}
	
	public RessourceNotFoundException (String message , Throwable throwable) {
		super(message,throwable);
	}

}
