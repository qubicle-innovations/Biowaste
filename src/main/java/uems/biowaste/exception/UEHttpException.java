package uems.biowaste.exception;

public class UEHttpException extends Exception {
	
	private static final long serialVersionUID = 1L;
	protected int httpStatusCode;
	protected String message;
	
	public UEHttpException(int httpStatusCode, String message) {
		super(message);
		this.message = message;
		this.httpStatusCode = httpStatusCode;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public String getMessage() {
		return message;
	}

}
