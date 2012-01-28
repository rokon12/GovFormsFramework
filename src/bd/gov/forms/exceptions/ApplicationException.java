package bd.gov.forms.exceptions;

public class ApplicationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String customMsg;

	public ApplicationException(String customMsg) {
		this.customMsg = customMsg;
	}

	public ApplicationException() {
	}

	public String getCustomMsg() {
		return customMsg;
	}

	public void setCustomMsg(String customMsg) {
		this.customMsg = customMsg;
	}
}
