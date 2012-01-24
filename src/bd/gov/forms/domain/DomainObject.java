package bd.gov.forms.domain;

import java.util.Date;

/**
 * @author A. N. M. Bazlur Rahman
 * 
 * */

public abstract class DomainObject {

	private Date dateCreated;
	private Date dateLastUpdated;
	private long version;

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateLastUpdated() {
		return dateLastUpdated;
	}

	public void setDateLastUpdated(Date dateLastUpdated) {
		this.dateLastUpdated = dateLastUpdated;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
}
