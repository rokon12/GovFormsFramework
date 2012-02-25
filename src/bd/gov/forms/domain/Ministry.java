package bd.gov.forms.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 */
@Entity(name = "minstry")
public class Ministry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@Column(name = "ministry_name")
	private String ministryName;
	@Column(name = "ministry_short_name")
	private String ministryShortName;

	public Ministry() {
	}

	/**
	 * Method getId.
	 * 
	 * @return int
	 */
	public int getId() {
		return id;
	}

	/**
	 * Method setId.
	 * 
	 * @param id
	 *            int
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Method getMinistryName.
	 * 
	 * @return String
	 */
	public String getMinistryName() {
		return ministryName;
	}

	/**
	 * Method setMinistryName.
	 * 
	 * @param ministryName
	 *            String
	 */
	public void setMinistryName(String ministryName) {
		this.ministryName = ministryName;
	}

	/**
	 * Method getMinistryShortName.
	 * 
	 * @return String
	 */
	public String getMinistryShortName() {
		return ministryShortName;
	}

	/**
	 * Method setMinistryShortName.
	 * 
	 * @param ministryShortName
	 *            String
	 */
	public void setMinistryShortName(String ministryShortName) {
		this.ministryShortName = ministryShortName;
	}

}
