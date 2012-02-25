/*
 * Copyright (C) 2011 Therap (BD) Ltd.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bd.gov.forms.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author asif
 * 
 * @version $Revision: 1.0 $
 */

@Entity
@Table(name = "FORM")
public class Form {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@Column(name = "form_id")
	private String formId;
	@Column(name = "title")
	private String title;
	@Column(name = "sub_title")
	private String subTitle;
	@Column(name = "detail")
	private String detail;
	@Column(name = "table_name")
	private String tableName;
	@Column(name = "status")
	private int status;
	@Column(name = "pdf_template")
	@Lob
	private byte[] pdfTemplate;
	@Column(name = "template_file_name")
	private String templateFileName;

	// @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	// @JoinTable(name = "FORM_FIELD", joinColumns = { @JoinColumn(name = "id")
	// }, inverseJoinColumns = { @JoinColumn(name = "fId") })
	// // @JoinColumn(name = "fId", insertable = true, updatable = true)
	@Transient
	private List<Field> fields;

	@Column(name = "logo_name")
	private String logoName; // added for logo
	@Column(name = "logo")
	@Lob
	private byte[] logo;

	@Column(name = "entry_id")
	private String entryId;
	@Column(name = "entry_status")
	private String entryStatus;

	@Column(name = "total_pages")
	private int totalPages;

	private int ministry;

	public Form() {
	}

	/**
	 * Constructor for Form.
	 * 
	 * @param id
	 *            int
	 * @param formId
	 *            String
	 * @param title
	 *            String
	 * @param subTitle
	 *            String
	 * @param detail
	 *            String
	 * @param tableName
	 *            String
	 * @param status
	 *            int
	 * @param pdfTemplate
	 *            byte[]
	 * @param templateFileName
	 *            String
	 * @param fields
	 *            List<Field>
	 * @param entryId
	 *            String
	 * @param entryStatus
	 *            String
	 * @param totalPages
	 *            int
	 */
	public Form(int id, String formId, String title, String subTitle,
			String detail, String tableName, int status, byte[] pdfTemplate,
			String templateFileName, List<Field> fields, String entryId,
			String entryStatus, int totalPages) {
		super();
		this.id = id;
		this.formId = formId;
		this.title = title;
		this.subTitle = subTitle;
		this.detail = detail;
		this.tableName = tableName;
		this.status = status;
		this.pdfTemplate = pdfTemplate;
		this.templateFileName = templateFileName;
		this.fields = fields;
		this.entryId = entryId;
		this.entryStatus = entryStatus;
		this.totalPages = totalPages;
	}

	/**
	 * Method getDetail.
	 * 
	 * @return String
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * Method setDetail.
	 * 
	 * @param detail
	 *            String
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * Method getFields.
	 * 
	 * @return List<Field>
	 */
	public List<Field> getFields() {
		return fields;
	}

	/**
	 * Method setFields.
	 * 
	 * @param fields
	 *            List<Field>
	 */
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	/**
	 * Method getFormId.
	 * 
	 * @return String
	 */
	public String getFormId() {
		return formId;
	}

	/**
	 * Method setFormId.
	 * 
	 * @param formId
	 *            String
	 */
	public void setFormId(String formId) {
		this.formId = formId;
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
	 * Method getPdfTemplate.
	 * 
	 * @return byte[]
	 */
	public byte[] getPdfTemplate() {
		return pdfTemplate;
	}

	/**
	 * Method setPdfTemplate.
	 * 
	 * @param pdfTemplate
	 *            byte[]
	 */
	public void setPdfTemplate(byte[] pdfTemplate) {
		this.pdfTemplate = pdfTemplate;
	}

	/**
	 * Method getTemplateFileName.
	 * 
	 * @return String
	 */
	public String getTemplateFileName() {
		return templateFileName;
	}

	/**
	 * Method setTemplateFileName.
	 * 
	 * @param templateFileName
	 *            String
	 */
	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
	}

	/**
	 * Method getStatus.
	 * 
	 * @return int
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Method setStatus.
	 * 
	 * @param status
	 *            int
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Method getSubTitle.
	 * 
	 * @return String
	 */
	public String getSubTitle() {
		return subTitle;
	}

	/**
	 * Method setSubTitle.
	 * 
	 * @param subTitle
	 *            String
	 */
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	/**
	 * Method getTableName.
	 * 
	 * @return String
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Method setTableName.
	 * 
	 * @param tableName
	 *            String
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Method getTitle.
	 * 
	 * @return String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Method setTitle.
	 * 
	 * @param title
	 *            String
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Method getEntryId.
	 * 
	 * @return String
	 */
	public String getEntryId() {
		return entryId;
	}

	/**
	 * Method setEntryId.
	 * 
	 * @param entryId
	 *            String
	 */
	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	/**
	 * Method getEntryStatus.
	 * 
	 * @return String
	 */
	public String getEntryStatus() {
		return entryStatus;
	}

	/**
	 * Method setEntryStatus.
	 * 
	 * @param entryStatus
	 *            String
	 */
	public void setEntryStatus(String entryStatus) {
		this.entryStatus = entryStatus;
	}

	/**
	 * Method getTotalPages.
	 * 
	 * @return int
	 */
	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * Method setTotalPages.
	 * 
	 * @param totalPages
	 *            int
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * Method getStatusStr.
	 * 
	 * @return String
	 */
	public String getStatusStr() {
		switch (getStatus()) {
		case 1:
			return "Draft";
		case 2:
			return "Activated";
		case 3:
			return "Deactivated";
		default:
			return "";
		}
	}

	/**
	 * Method getLogoName.
	 * 
	 * @return String
	 */
	public String getLogoName() {
		return logoName;
	}

	/**
	 * Method setLogoName.
	 * 
	 * @param logoName
	 *            String
	 */
	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	/**
	 * Method getLogo.
	 * 
	 * @return byte[]
	 */
	public byte[] getLogo() {
		return logo;
	}

	/**
	 * Method setLogo.
	 * 
	 * @param logo
	 *            byte[]
	 */
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	/**
	 * Method getMinistry.
	 * 
	 * @return int
	 */
	public int getMinistry() {
		return ministry;
	}

	/**
	 * Method setMinistry.
	 * 
	 * @param ministry
	 *            int
	 */
	public void setMinistry(int ministry) {
		this.ministry = ministry;
	}
}
