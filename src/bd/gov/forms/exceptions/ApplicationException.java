package bd.gov.forms.exceptions;

/**
 */
public class ApplicationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String customMsg;

	/**
	 * Constructor for ApplicationException.
	 * @param customMsg String
	 */
	public ApplicationException(String customMsg) {
		this.customMsg = customMsg;
	}

	public ApplicationException() {
	}

	/**
	 * Method getCustomMsg.
	 * @return String
	 */
	public String getCustomMsg() {
		return customMsg;
	}

	/**
	 * Method setCustomMsg.
	 * @param customMsg String
	 */
	public void setCustomMsg(String customMsg) {
		this.customMsg = customMsg;
	}
}
