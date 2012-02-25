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

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import javax.persistence.Entity;

/**
 * @author asif
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "FIELD")
public class Field implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fId")
	private int id;
	@Column(name = "field_id")
	private String fieldId;
	@Column(name = "col_name")
	private String colName;
	@Column(name = "type")
	private String type;
	@Column(name = "label")
	private String label;
	@Column(name = "required")
	private int required;
	@Column(name = "multi_check_enable")
	private boolean multiCheckEnable; // for multiple/single check box
	@Column(name = "help_text")
	private String helpText;
	@Transient
	private String options; // not used
	@Column(name = "list_data_id")
	private int listDataId;
	@Column(name = "default_value")
	private String defaultValue;
	@Column(name = "field_order")
	private int fieldOrder;
	@Column(name = "input_type")
	private String inputType;
	@Column(name = "css_class")
	private String cssClass; // Transient
	@Column(name = "line_break")
	private boolean lineBreak;

	@Column(name = "str_val")
	private String strVal;
	@Column(name = "byte_val")
	@Lob
	private byte[] byteVal;
	@Column(name = "form_id")
	private int formId;
	@Column(name = "form_id_str")
	private String formIdStr;

	// @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	// @JoinColumn(name = "list_data_id", insertable = true, updatable = true)
	@Transient
	private List list; // generated from options

	// private List<ListData> lis

	/**
	 * Method isMultiCheckEnable.
	 * 
	 * @return boolean
	 */
	public boolean isMultiCheckEnable() {
		return multiCheckEnable;
	}

	/**
	 * Method setMultiCheckEnable.
	 * 
	 * @param multiCheckEnable
	 *            boolean
	 */
	public void setMultiCheckEnable(boolean multiCheckEnable) {
		this.multiCheckEnable = multiCheckEnable;
	}

	/**
	 * Method getByteVal.
	 * 
	 * @return byte[]
	 */
	public byte[] getByteVal() {
		return byteVal;
	}

	/**
	 * Method setByteVal.
	 * 
	 * @param byteVal
	 *            byte[]
	 */
	public void setByteVal(byte[] byteVal) {
		this.byteVal = byteVal;
	}

	/**
	 * Method getColName.
	 * 
	 * @return String
	 */
	public String getColName() {
		return colName;
	}

	/**
	 * Method setColName.
	 * 
	 * @param colName
	 *            String
	 */
	public void setColName(String colName) {
		this.colName = colName;
	}

	/**
	 * Method getCssClass.
	 * 
	 * @return String
	 */
	public String getCssClass() {
		return cssClass;
	}

	/**
	 * Method setCssClass.
	 * 
	 * @param cssClass
	 *            String
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	/**
	 * Method getDefaultValue.
	 * 
	 * @return String
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Method setDefaultValue.
	 * 
	 * @param defaultValue
	 *            String
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Method getFieldId.
	 * 
	 * @return String
	 */
	public String getFieldId() {
		return fieldId;
	}

	/**
	 * Method setFieldId.
	 * 
	 * @param fieldId
	 *            String
	 */
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	/**
	 * Method getFieldOrder.
	 * 
	 * @return int
	 */
	public int getFieldOrder() {
		return fieldOrder;
	}

	/**
	 * Method setFieldOrder.
	 * 
	 * @param fieldOrder
	 *            int
	 */
	public void setFieldOrder(int fieldOrder) {
		this.fieldOrder = fieldOrder;
	}

	/**
	 * Method getHelpText.
	 * 
	 * @return String
	 */
	public String getHelpText() {
		return helpText;
	}

	/**
	 * Method setHelpText.
	 * 
	 * @param helpText
	 *            String
	 */
	public void setHelpText(String helpText) {
		this.helpText = helpText;
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
	 * Method getLabel.
	 * 
	 * @return String
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Method setLabel.
	 * 
	 * @param label
	 *            String
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Method isLineBreak.
	 * 
	 * @return boolean
	 */
	public boolean isLineBreak() {
		return lineBreak;
	}

	/**
	 * Method setLineBreak.
	 * 
	 * @param lineBreak
	 *            boolean
	 */
	public void setLineBreak(boolean lineBreak) {
		this.lineBreak = lineBreak;
	}

	/**
	 * Method getList.
	 * 
	 * @return List
	 */
	public List getList() {
		return list;
	}

	/**
	 * Method setList.
	 * 
	 * @param list
	 *            List
	 */
	public void setList(List list) {
		this.list = list;
	}

	/**
	 * Method getListDataId.
	 * 
	 * @return int
	 */
	public int getListDataId() {
		return listDataId;
	}

	/**
	 * Method setListDataId.
	 * 
	 * @param listDataId
	 *            int
	 */
	public void setListDataId(int listDataId) {
		this.listDataId = listDataId;
	}

	/**
	 * Method getOptions.
	 * 
	 * @return String
	 */
	public String getOptions() {
		return options;
	}

	/**
	 * Method setOptions.
	 * 
	 * @param options
	 *            String
	 */
	public void setOptions(String options) {
		this.options = options;
	}

	/**
	 * Method getRequired.
	 * 
	 * @return int
	 */
	public int getRequired() {
		return required;
	}

	/**
	 * Method setRequired.
	 * 
	 * @param required
	 *            int
	 */
	public void setRequired(int required) {
		this.required = required;
	}

	/**
	 * Method getStrVal.
	 * 
	 * @return String
	 */
	public String getStrVal() {
		return strVal;
	}

	/**
	 * Method setStrVal.
	 * 
	 * @param strVal
	 *            String
	 */
	public void setStrVal(String strVal) {
		this.strVal = strVal;
	}

	/**
	 * Method getType.
	 * 
	 * @return String
	 */
	public String getType() {
		return type;
	}

	/**
	 * Method setType.
	 * 
	 * @param type
	 *            String
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Method getFormId.
	 * 
	 * @return int
	 */
	public int getFormId() {
		return formId;
	}

	/**
	 * Method setFormId.
	 * 
	 * @param formId
	 *            int
	 */
	public void setFormId(int formId) {
		this.formId = formId;
	}

	/**
	 * Method getFormIdStr.
	 * 
	 * @return String
	 */
	public String getFormIdStr() {
		return formIdStr;
	}

	/**
	 * Method setFormIdStr.
	 * 
	 * @param formIdStr
	 *            String
	 */
	public void setFormIdStr(String formIdStr) {
		this.formIdStr = formIdStr;
	}

	/**
	 * Method getInputType.
	 * 
	 * @return String
	 */
	public String getInputType() {
		return inputType;
	}

	/**
	 * Method setInputType.
	 * 
	 * @param inputType
	 *            String
	 */
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
}
