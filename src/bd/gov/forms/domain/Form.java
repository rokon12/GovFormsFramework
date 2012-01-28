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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author asif
 * 
 */
@XmlRootElement(name = "form")
public class Form {

	private int id;
	private String formId;
	private String title;
	private String subTitle;
	private String detail;
	private String tableName;
	private int status;
	private byte[] pdfTemplate;
	private String templateFileName;
	private List<Field> fields;

	private String logoName; // added for logo
	private byte[] logo;

	private String entryId;
	private String entryStatus;
	private int totalPages;
	private int ministry;

	public Form() {
	}

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

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getPdfTemplate() {
		return pdfTemplate;
	}

	public void setPdfTemplate(byte[] pdfTemplate) {
		this.pdfTemplate = pdfTemplate;
	}

	public String getTemplateFileName() {
		return templateFileName;
	}

	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public String getEntryStatus() {
		return entryStatus;
	}

	public void setEntryStatus(String entryStatus) {
		this.entryStatus = entryStatus;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

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

	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public int getMinistry() {
		return ministry;
	}

	public void setMinistry(int ministry) {
		this.ministry = ministry;
	}
}
