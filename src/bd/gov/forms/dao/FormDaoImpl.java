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
package bd.gov.forms.dao;

import bd.gov.forms.domain.Field;
import bd.gov.forms.domain.Form;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author asif
 * @version $Revision: 1.0 $
 */
@Repository("formDao")
@Transactional 
@SuppressWarnings("unchecked")
public class FormDaoImpl implements FormDao {

	private static final Logger log = LoggerFactory
			.getLogger(FormDaoImpl.class);

	private static final int FORM_ENTRY_RESULTS_PER_PAGE = 5;
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	DefaultLobHandler lobHandler;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Method saveForm.
	 * @param form Form
	 * @see bd.gov.forms.dao.FormDao#saveForm(Form)
	 */
	public void saveForm(final Form form) {
		String sql = "INSERT INTO form";
		sql += " (form_id, title, subtitle, detail, status, ministry";

		if (formHasTemplate(form)) {
			sql += ", template_file, template_file_name";
		}

		if (formHasLogo(form)) {
			sql += ", logo, logo_name";
		}

		sql += ")";
		sql += " VALUES (?, ?, ?, ?, ?, ?";

		if (formHasTemplate(form)) {
			sql += " , ?, ?";
		}

		if (formHasLogo(form)) {
			sql += " , ?, ?";
		}

		sql += ")";

		log.debug("SQL INSERT query: {}" + sql);

		jdbcTemplate.execute(sql,
				new AbstractLobCreatingPreparedStatementCallback(lobHandler) {

					@Override
					protected void setValues(PreparedStatement ps,
							LobCreator lobCreator) throws SQLException {
						int i = 1;

						ps.setString(i++, form.getFormId());
						ps.setString(i++, form.getTitle());
						ps.setString(i++, form.getSubTitle());
						ps.setString(i++, form.getDetail());
						ps.setInt(i++, form.getStatus());
						ps.setInt(i++, form.getMinistry());

						if (formHasTemplate(form)) {
							lobCreator.setBlobAsBinaryStream(
									ps,
									i++,
									new ByteArrayInputStream(form
											.getPdfTemplate()), form
											.getPdfTemplate().length);
							ps.setString(i, form.getTemplateFileName());
						}
						if (formHasLogo(form)) {
							lobCreator.setBlobAsBinaryStream(ps, i++,
									new ByteArrayInputStream(form.getLogo()),
									form.getLogo().length);
							ps.setString(i, form.getLogoName());
						}

					}
				});
	}

	/**
	 * Method formHasTemplate.
	 * @param form Form
	 * @return boolean
	 */
	private boolean formHasTemplate(Form form) {
		return form.getPdfTemplate() != null
				&& form.getPdfTemplate().length > 0;
	}

	/**
	 * Method formHasLogo.
	 * @param form Form
	 * @return boolean
	 */
	private boolean formHasLogo(Form form) {
		return form.getLogo() != null && form.getLogo().length > 0;
	}

	/**
	 * Method getForm.
	 * @param formId String
	 * @return Form
	 * @see bd.gov.forms.dao.FormDao#getForm(String)
	 */
	@Override
	public Form getForm(String formId) {

		// return (Form) jdbcTemplate.queryForObject(
		// "SELECT * FROM form WHERE form_id = ?",
		// new Object[] { formId }, new RowMapper() {
		//
		// public Object mapRow(ResultSet resultSet, int rowNum)
		// throws SQLException {
		// Form form = new Form();
		//
		// form.setId(resultSet.getInt("id"));
		// form.setFormId(resultSet.getString("form_id"));
		// form.setTitle(resultSet.getString("title"));
		// form.setSubTitle(resultSet.getString("subtitle"));
		// form.setDetail(resultSet.getString("detail"));
		// form.setTableName(resultSet.getString("table_name"));
		// form.setStatus(resultSet.getInt("status"));
		// form.setTemplateFileName(resultSet
		// .getString("template_file_name"));
		// form.setLogoName(resultSet.getString("logo_name"));
		// return form;
		// }
		// });

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Form.class);
		criteria.add(Restrictions.eq("form_id", formId));
		List<Form> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * Method updateForm.
	 * @param form Form
	 * @see bd.gov.forms.dao.FormDao#updateForm(Form)
	 */
	public void updateForm(final Form form) {
		String sql = "UPDATE form";
		sql += " set title = ?, subtitle = ?, detail = ?, status = ?";

		if (formHasTemplate(form)) {
			sql += ", template_file = ?, template_file_name = ?";
		}

		if (formHasLogo(form)) {
			sql += ", logo = ?, logo_name = ?";
		}

		sql += " WHERE form_id = ?";

		log.debug("SQL UPDATE query: {}", sql);

		jdbcTemplate.execute(sql,
				new AbstractLobCreatingPreparedStatementCallback(lobHandler) {

					@Override
					protected void setValues(PreparedStatement ps,
							LobCreator lobCreator) throws SQLException {
						int i = 1;

						ps.setString(i++, form.getTitle());
						ps.setString(i++, form.getSubTitle());
						ps.setString(i++, form.getDetail());
						ps.setInt(i++, form.getStatus());

						if (formHasTemplate(form)) {
							lobCreator.setBlobAsBinaryStream(
									ps,
									i++,
									new ByteArrayInputStream(form
											.getPdfTemplate()), form
											.getPdfTemplate().length);
							ps.setString(i++, form.getTemplateFileName());
						}

						if (formHasLogo(form)) {
							lobCreator.setBlobAsBinaryStream(ps, i++,
									new ByteArrayInputStream(form.getLogo()),
									form.getLogo().length);
							ps.setString(i++, form.getLogoName());
						}

						ps.setString(i, form.getFormId());
					}
				});
	}

	/**
	 * Method getTemplateContent.
	 * @param formId String
	 * @return byte[]
	 * @see bd.gov.forms.dao.FormDao#getTemplateContent(String)
	 */
	public byte[] getTemplateContent(String formId) {
		Form form = (Form) jdbcTemplate.queryForObject(
				"SELECT template_file FROM form WHERE form_id = ?",
				new Object[] { formId }, new RowMapper() {

					public Object mapRow(ResultSet resultSet, int rowNum)
							throws SQLException {
						Form form = new Form();

						form.setPdfTemplate(resultSet.getBytes("template_file"));

						return form;
					}
				});

		return form.getPdfTemplate();
	}

	/**
	 * Method getLogoContent.
	 * @param formId String
	 * @return byte[]
	 * @see bd.gov.forms.dao.FormDao#getLogoContent(String)
	 */
	public byte[] getLogoContent(String formId) {

		Form form = (Form) jdbcTemplate.queryForObject(
				"SELECT logo FROM form WHERE form_id = ?",
				new Object[] { formId }, new RowMapper() {

					public Object mapRow(ResultSet resultSet, int rowNum)
							throws SQLException {
						Form form = new Form();

						form.setPdfTemplate(resultSet.getBytes("logo"));

						return form;
					}
				});

		return form.getPdfTemplate();
	}

	/**
	 * Method getFormWithFields.
	 * @param formId String
	 * @return Form
	 * @see bd.gov.forms.dao.FormDao#getFormWithFields(String)
	 */
	public Form getFormWithFields(String formId) {
		Form form = getForm(formId);

		log.debug("formId: {}", form != null ? form.getId() : "form is null");

		if (form != null) {
			List fields = jdbcTemplate
					.query("SELECT * FROM field WHERE form_id = ? order by field_order",
							new Object[] { form.getId() }, new RowMapper() {

								public Object mapRow(ResultSet rs, int rowNum)
										throws SQLException {
									Field fld = new Field();

									fld.setId(rs.getInt("id"));
									fld.setFormId(rs.getInt("form_id"));
									fld.setFieldId(rs.getString("field_id"));
									fld.setType(rs.getString("type"));
									fld.setColName(rs.getString("col_name"));
									fld.setLabel(rs.getString("label"));
									fld.setHelpText(rs.getString("help_text"));
									fld.setOptions(rs.getString("options"));
									fld.setListDataId(rs.getInt("list_data_id"));
									fld.setDefaultValue(rs
											.getString("def_value"));
									fld.setFieldOrder(rs.getInt("field_order"));
									fld.setRequired(rs.getInt("required"));
									fld.setInputType(rs.getString("input_type"));

									return fld;
								}
							});

			form.setFields(fields);
		}

		return form;
	}

	/**
	 * Method getField.
	 * @param fieldId String
	 * @return Field
	 * @see bd.gov.forms.dao.FormDao#getField(String)
	 */
	public Field getField(String fieldId) {
		return (Field) jdbcTemplate.queryForObject(
				"SELECT * FROM field WHERE field_id = ? ",
				new Object[] { fieldId }, new RowMapper() {

					public Object mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Field fld = new Field();

						fld.setId(rs.getInt("id"));
						fld.setFormId(rs.getInt("form_id"));
						fld.setFieldId(rs.getString("field_id"));
						fld.setType(rs.getString("type"));
						fld.setColName(rs.getString("col_name"));
						fld.setLabel(rs.getString("label"));
						fld.setHelpText(rs.getString("help_text"));
						fld.setOptions(rs.getString("options"));
						fld.setListDataId(rs.getInt("list_data_id"));
						fld.setDefaultValue(rs.getString("def_value"));
						fld.setFieldOrder(rs.getInt("field_order"));
						fld.setRequired(rs.getInt("required"));
						fld.setInputType(rs.getString("input_type"));

						return fld;
					}
				});
	}

	/**
	 * Method saveField.
	 * @param field Field
	 * @see bd.gov.forms.dao.FormDao#saveField(Field)
	 */
	public void saveField(final Field field) {
		String sql = "INSERT INTO field";
		sql += " (field_id, form_id, type, input_type, label, required, help_text, options, list_data_id, def_value, "
				+ "field_order";
		sql += ")";
		sql += " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
		sql += ")";

		log.debug("SQL INSERT query: {}", sql);

		jdbcTemplate.execute(sql,
				new AbstractLobCreatingPreparedStatementCallback(lobHandler) {

					@Override
					protected void setValues(PreparedStatement ps,
							LobCreator lobCreator) throws SQLException {
						int i = 1;

						ps.setString(i++, field.getFieldId());
						ps.setInt(i++, field.getFormId());
						ps.setString(i++, field.getType());
						ps.setString(i++, field.getInputType());
						ps.setString(i++, field.getLabel());
						ps.setInt(i++, field.getRequired());
						ps.setString(i++, field.getHelpText());
						ps.setString(i++, field.getOptions());
						ps.setInt(i++, field.getListDataId());
						ps.setString(i++, field.getDefaultValue());
						ps.setInt(i, field.getFieldOrder());
					}
				});
	}

	/**
	 * Method updateField.
	 * @param field Field
	 * @see bd.gov.forms.dao.FormDao#updateField(Field)
	 */
	public void updateField(final Field field) {
		String sql = "UPDATE field";
		sql += " set type = ?, input_type = ?, label = ?, required = ?, help_text = ?, options = ?, list_data_id = ?, "
				+ "def_value = ?, field_order = ?";
		sql += " WHERE field_id = ? and form_id = ?";

		log.debug("SQL UPDATE query: {}", sql);
		log.debug("formId: {}, fieldId: {}", field.getFormId(),
				field.getFieldId());

		jdbcTemplate.execute(sql,
				new AbstractLobCreatingPreparedStatementCallback(lobHandler) {

					@Override
					protected void setValues(PreparedStatement ps,
							LobCreator lobCreator) throws SQLException {
						int i = 1;

						ps.setString(i++, field.getType());// *
						ps.setString(i++, field.getInputType());
						ps.setString(i++, field.getLabel());
						ps.setInt(i++, field.getRequired());
						ps.setString(i++, field.getHelpText());
						ps.setString(i++, field.getOptions());
						ps.setInt(i++, field.getListDataId());
						ps.setString(i++, field.getDefaultValue());
						ps.setInt(i++, field.getFieldOrder());// *

						ps.setString(i++, field.getFieldId());
						ps.setInt(i, field.getFormId());
					}
				});
	}

	/**
	 * Method moveField.
	 * @param formId int
	 * @param fieldId String
	 * @param fieldOrder int
	 * @param order int
	 * @see bd.gov.forms.dao.FormDao#moveField(int, String, int, int)
	 */
	public void moveField(int formId, String fieldId, int fieldOrder, int order) {
		jdbcTemplate
				.update("UPDATE field SET field_order = ? WHERE form_id = ? and field_order = ?",
						fieldOrder, formId, order);
		jdbcTemplate
				.update("UPDATE field SET field_order = ? WHERE form_id = ? and field_id = ?",
						order, formId, fieldId);
	}

	/**
	 * Method updateOrder.
	 * @param formId int
	 * @param fieldOrder int
	 * @param operator String
	 * @see bd.gov.forms.dao.FormDao#updateOrder(int, int, String)
	 */
	public void updateOrder(int formId, int fieldOrder, String operator) {
		jdbcTemplate.update("UPDATE field SET field_order = field_order"
				+ operator + "1 WHERE form_id = ? and field_order >= ?",
				formId, fieldOrder);
	}

	/**
	 * Method deleteField.
	 * @param fieldId String
	 * @param formId int
	 * @see bd.gov.forms.dao.FormDao#deleteField(String, int)
	 */
	public void deleteField(String fieldId, int formId) {
		jdbcTemplate.update(
				"DELETE FROM field WHERE field_id = ? and form_id = ?",
				fieldId, formId);
	}

	/**
	 * Method deleteForm.
	 * @param formId String
	 * @see bd.gov.forms.dao.FormDao#deleteForm(String)
	 */
	public void deleteForm(String formId) {
		jdbcTemplate.update(
				"DELETE FROM form WHERE form_id = ? and status = 1", formId);
	}

	/**
	 * Method createTable.
	 * @param frm Form
	 * @see bd.gov.forms.dao.FormDao#createTable(Form)
	 */
	public void createTable(Form frm) {
		String cols = "";

		for (Field f : frm.getFields()) {
			if (!"note".equals(f.getType()) && !"section".equals(f.getType())) {
				String type = "LONGTEXT";

				if ("file".equals(f.getType())) {
					type = "LONGBLOB";
				}

				cols += f.getColName() + " " + type + ", ";

				if ("file".equals(f.getType())) {
					cols += f.getColName() + "_fname" + " LONGTEXT, ";
				}
			}
		}

		cols = cols.substring(0, cols.length() - 2);

		String sql = "CREATE TABLE " + frm.getTableName();
		sql += " ("
				+ "id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, entry_id TEXT NOT NULL, entry_date DATE NOT NULL, "
				+ "entry_time TIME NOT NULL, entry_status TEXT NOT NULL, "
				+ cols + ") DEFAULT CHARSET=utf8 ";

		log.debug("CREATE TABLE sql: {}", sql);

		jdbcTemplate.update(sql);
	}

	/**
	 * Method saveEntry.
	 * @param form Form
	 * @see bd.gov.forms.dao.FormDao#saveEntry(Form)
	 */
	public void saveEntry(final Form form) {
		String cols = "entry_date, entry_time, entry_id, entry_status, ";
		String val = "CURDATE(), CURTIME(), ?, ?, ";

		for (Field field : form.getFields()) {
			if (fieldTypeIsNotFileOrNoteOrSection(field.getType())) {
				cols += field.getColName() + ", ";
				val += "? , ";
			}

			if ("file".equals(field.getType())) {
				if (notEmpty(field.getByteVal())) {
					cols += field.getColName() + ", " + field.getColName()
							+ "_fname, ";
					val += "? , ?, ";
				}
			}
		}

		cols = cols.substring(0, cols.length() - 2);
		val = val.substring(0, val.length() - 2);

		String sql = "INSERT INTO " + form.getTableName();
		sql += " (" + cols + ")";
		sql += " VALUES (" + val + ")";

		log.debug("SQL INSERT query: {}", sql);

		jdbcTemplate.execute(sql,
				new AbstractLobCreatingPreparedStatementCallback(lobHandler) {

					protected void setValues(PreparedStatement ps,
							LobCreator lobCreator) throws SQLException {
						int i = 1;

						ps.setString(i++, form.getEntryId());
						ps.setString(i++, form.getEntryStatus());

						for (Field field : form.getFields()) {
							if (fieldTypeIsNotFileOrNoteOrSection(field
									.getType())) {
								ps.setString(i++, field.getStrVal());
							}

							if ("file".equals(field.getType())) {
								if (notEmpty(field.getByteVal())) {
									lobCreator.setBlobAsBinaryStream(
											ps,
											i++,
											new ByteArrayInputStream(field
													.getByteVal()), field
													.getByteVal().length);
									ps.setString(i++, field.getStrVal());
									log.debug("File Name: {}",
											field.getStrVal());
								}
							}
						}
					}
				});
	}

	/**
	 * Method notEmpty.
	 * @param content byte[]
	 * @return boolean
	 */
	private boolean notEmpty(byte[] content) {
		return content != null && content.length > 0;
	}

	/**
	 * Method fieldTypeIsNotFileOrNoteOrSection.
	 * @param fieldType String
	 * @return boolean
	 */
	private boolean fieldTypeIsNotFileOrNoteOrSection(String fieldType) {
		return !"file".equals(fieldType) && !"note".equals(fieldType)
				&& !"section".equals(fieldType);
	}

	/**
	 * Method getFormList.
	 * @param page int
	 * @return List
	 * @see bd.gov.forms.dao.FormDao#getFormList(int)
	 */
	public List getFormList(int page) {
		String sql = "select * from form";

		return jdbcTemplate.query(sql, new RowMapper() {

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Form form = new Form();

				populateFormFromResultSet(rs, form);

				return form;
			}
		});
	}

	/**
	 * Method populateFormFromResultSet.
	 * @param rs ResultSet
	 * @param form Form
	 * @throws SQLException
	 */
	private void populateFormFromResultSet(ResultSet rs, Form form)
			throws SQLException {
		form.setFormId(rs.getString("form_id"));
		form.setStatus(rs.getInt("status"));
		form.setTitle(rs.getString("title"));
		form.setDetail(rs.getString("detail"));
	}

	/**
	 * Method getPublicForms.
	 * @return List
	 * @see bd.gov.forms.dao.FormDao#getPublicForms()
	 */
	public List getPublicForms() {
		String sql = "select * from form where status=2";

		return this.jdbcTemplate.query(sql, new RowMapper() {

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Form form = new Form();

				populateFormFromResultSet(rs, form);

				return form;
			}
		});
	}

	/**
	 * Method updateStatus.
	 * @param formId String
	 * @param status int
	 * @see bd.gov.forms.dao.FormDao#updateStatus(String, int)
	 */
	public void updateStatus(String formId, int status) {
		jdbcTemplate.update("UPDATE form set status = ? WHERE form_id = ?",
				status, formId);
	}

	/**
	 * Method initDbIdentifiers.
	 * @param id int
	 * @see bd.gov.forms.dao.FormDao#initDbIdentifiers(int)
	 */
	public void initDbIdentifiers(int id) {
		jdbcTemplate
				.update("UPDATE form set table_name = concat('table', id) WHERE id = ?",
						id);
		jdbcTemplate
				.update("UPDATE field set col_name = concat('col', id) WHERE form_id = ?",
						id);
	}

	/**
	 * Method getEntryList.
	 * @param form Form
	 * @param page Integer
	 * @param colName String
	 * @param colVal String
	 * @param sortCol String
	 * @param sortOrder String
	 * @param limit boolean
	 * @return List
	 * @see bd.gov.forms.dao.FormDao#getEntryList(Form, Integer, String, String, String, String, boolean)
	 */
	public List getEntryList(final Form form, Integer page, String colName,
			String colVal, String sortCol, String sortOrder, boolean limit) {
		String where;
		if (colName != null && !"".equals(colName) && colVal != null
				&& !"".equals(colVal)) {
			where = " WHERE " + colName + " LIKE ? ";
		} else {
			where = " WHERE 1 = ? ";
			colVal = "1";
		}

		String order = " ORDER BY ";
		if (sortCol != null && !"".equals(sortCol)) {
			order += sortCol + " ";
		} else {
			order += " entry_date ";
		}
		order += sortOrder + " ";

		int totalRows = jdbcTemplate.queryForInt(
				"SELECT COUNT(*) FROM " + form.getTableName() + where, colVal);

		int resultsPerPage = totalRows;
		if (limit) {
			resultsPerPage = FORM_ENTRY_RESULTS_PER_PAGE;
		}

		int totalPages = 0;
		try {
			totalPages = totalRows / resultsPerPage;
			if (totalRows % resultsPerPage > 0) {
				totalPages++;
			}
		} catch (ArithmeticException ignore) {
		}

		if (page > totalPages || page < 1) {
			page = 1;
		}

		int start = (page - 1) * resultsPerPage;

		form.setTotalPages(totalPages);

		String sql = "SELECT * FROM " + form.getTableName() + where + order
				+ " LIMIT ?, ?";
		log.debug("getEntryList SQL query: {}", sql);
		final String columName = colName;
		// final String columVal = colVal;

		return this.jdbcTemplate.query(sql, new Object[] { colVal, start,
				resultsPerPage }, new RowMapper() {

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map map = new HashMap();

				for (Field field : form.getFields()) {
					map.put("entry_id", rs.getString("entry_id"));
					map.put("entry_date", rs.getString("entry_date"));
					map.put("entry_time", rs.getString("entry_time"));
					map.put("entry_status", rs.getString("entry_status"));
					map.put("entry_id", rs.getString("entry_id"));
					map.put("table_name", form.getTableName());
					map.put("colum_name", field.getColName());

					if (fieldTypeIsNotFileOrNoteOrSection(field.getType())) {
						map.put(field.getColName(),
								rs.getString(field.getColName()));
					}
				}

				return map;
			}
		});
	}

	/**
	 * Method getEntry.
	 * @param form Form
	 * @return Form
	 * @see bd.gov.forms.dao.FormDao#getEntry(Form)
	 */
	public Form getEntry(final Form form) {
		String sql = "SELECT * FROM " + form.getTableName()
				+ " WHERE entry_id = ?";

		log.debug("entryId: {}", form.getEntryId());
		log.debug("sql: {}", sql);

		return (Form) this.jdbcTemplate.queryForObject(sql,
				new Object[] { form.getEntryId() }, new RowMapper() {

					public Object mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						for (Field field : form.getFields()) {
							if (fieldTypeIsNotFileOrNoteOrSection(field
									.getType())) {
								field.setStrVal(rs.getString(field.getColName()));
							}
						}

						return form;
					}
				});
	}

	/**
	 * Method updateEntryStatus.
	 * @param form Form
	 * @param entryId String
	 * @param status String
	 * @see bd.gov.forms.dao.FormDao#updateEntryStatus(Form, String, String)
	 */
	public void updateEntryStatus(Form form, String entryId, String status) {
		jdbcTemplate.update("UPDATE " + form.getTableName()
				+ " set entry_status = ? WHERE entry_id = ?", status, entryId);
	}

	/**
	 * Method removeTemplate.
	 * @param formId String
	 * @see bd.gov.forms.dao.FormDao#removeTemplate(String)
	 */
	public void removeTemplate(String formId) {
		jdbcTemplate
				.update("UPDATE form set  template_file = null, template_file_name = null WHERE form_id = ?",
						formId);
	}

	/**
	 * Method removeLogo.
	 * @param formId String
	 * @see bd.gov.forms.dao.FormDao#removeLogo(String)
	 */
	public void removeLogo(String formId) {
		jdbcTemplate
				.update("UPDATE form set  logo = null, logo_name = null WHERE form_id = ?",
						formId);
	}

	/**
	 * Method getFormEntryCount.
	 * @param frm Form
	 * @param status String
	 * @return int
	 * @see bd.gov.forms.dao.FormDao#getFormEntryCount(Form, String)
	 */
	public int getFormEntryCount(Form frm, String status) {

		String sql = "SELECT COUNT(*) FROM " + frm.getTableName();
		if (status != null) {
			sql += " WHERE entry_status='" + status + "'";
		}

		return jdbcTemplate.queryForInt(sql);
	}

	/**
	 * Method getAttachment.
	 * @param entryId String
	 * @param columName String
	 * @param tableName String
	 * @return List
	 * @see bd.gov.forms.dao.FormDao#getAttachment(String, String, String)
	 */
	@Override
	public List getAttachment(String entryId, String columName, String tableName) {

		String query = "SELECT * From " + tableName + " WHERE entry_id=?";

		final String colName = columName;

		return this.jdbcTemplate.query(query, new Object[] { entryId },
				new RowMapper() {

					public Object mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Map map = new HashMap();

						map.put("file", rs.getBytes(colName));
						map.put("name", rs.getString(colName + "_fname"));
						return map;
					}
				});
	}
}
