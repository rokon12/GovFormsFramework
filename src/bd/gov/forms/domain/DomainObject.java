package bd.gov.forms.domain;

import java.util.Date;

/**
 * @author A. N. M. Bazlur Rahman
 * 
 *         * @version $Revision: 1.0 $
 */

public abstract class DomainObject {

	private Date dateCreated;
	private Date dateLastUpdated;
	private long version;

	/**
	 * Method getDateCreated.
	 * 
	 * @return Date
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * Method setDateCreated.
	 * 
	 * @param dateCreated
	 *            Date
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * Method getDateLastUpdated.
	 * 
	 * @return Date
	 */
	public Date getDateLastUpdated() {
		return dateLastUpdated;
	}

	/**
	 * Method setDateLastUpdated.
	 * 
	 * @param dateLastUpdated
	 *            Date
	 */
	public void setDateLastUpdated(Date dateLastUpdated) {
		this.dateLastUpdated = dateLastUpdated;
	}

	/**
	 * Method getVersion.
	 * 
	 * @return long
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * Method setVersion.
	 * 
	 * @param version
	 *            long
	 */
	public void setVersion(long version) {
		this.version = version;
	}
}
