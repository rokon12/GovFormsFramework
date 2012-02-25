package bd.gov.forms.domain;

import java.sql.Blob;
import java.util.Date;

/**
 * @author A. N. M. Bazlur Rahman
 * * @version $Revision: 1.0 $
 */

public class Document {

	private Integer id;

	private String name;

	private String description;

	private String filename;

	private byte[] content;

	private String contentType;

	private Date created;

	/**
	 * Method getId.
	 * @return Integer
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Method setId.
	 * @param id Integer
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Method getName.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method setName.
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Method getDescription.
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Method setDescription.
	 * @param description String
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Method getFilename.
	 * @return String
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Method setFilename.
	 * @param filename String
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * Method getContent.
	 * @return byte[]
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * Method setContent.
	 * @param content byte[]
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * Method getContentType.
	 * @return String
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Method setContentType.
	 * @param contentType String
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Method getCreated.
	 * @return Date
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * Method setCreated.
	 * @param created Date
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
}
