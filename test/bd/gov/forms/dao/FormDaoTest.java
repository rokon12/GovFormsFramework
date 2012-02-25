package bd.gov.forms.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import bd.gov.forms.domain.Field;
import bd.gov.forms.domain.Form;
import bd.gov.forms.web.AbstractFormTest;

/**
 */
public class FormDaoTest extends AbstractFormTest {
	@Autowired
	private FormDao dao;

	/**
	 * Method saveForm.
	 * @param frm Form
	 */
	@Test
	public void saveForm(final Form frm) {
		Form form = new Form();
		form.setDetail("adfasdfasdf");

	}

	/**
	 * Method getForm.
	 * @param formId String
	 * @return Form
	 */
	public Form getForm(String formId) {
		return null;
	}

	/**
	 * Method updateForm.
	 * @param frm Form
	 */
	public void updateForm(final Form frm) {
	}

	/**
	 * Method deleteForm.
	 * @param formId String
	 */
	public void deleteForm(String formId) {
	}

	/**
	 * Method getTemplateContent.
	 * @param formId String
	 * @return byte[]
	 */
	public byte[] getTemplateContent(String formId) {
		return null;
	}

	/**
	 * Method getLogoContent.
	 * @param formId String
	 * @return byte[]
	 */
	public byte[] getLogoContent(String formId) {
		return null;
	}

	/**
	 * Method getFormWithFields.
	 * @param formId String
	 * @return Form
	 */
	public Form getFormWithFields(String formId) {
		return null;
	}

	/**
	 * Method getField.
	 * @param fieldId String
	 * @return Field
	 */
	public Field getField(String fieldId) {
		return null;
	}

	/**
	 * Method saveField.
	 * @param fld Field
	 */
	public void saveField(final Field fld) {
	}

	/**
	 * Method updateField.
	 * @param fld Field
	 */
	public void updateField(final Field fld) {
	}

	/**
	 * Method deleteField.
	 * @param fieldId String
	 * @param formId int
	 */
	public void deleteField(String fieldId, int formId) {
	}

	/**
	 * Method updateOrder.
	 * @param formId int
	 * @param fieldOrder int
	 * @param operator String
	 */
	public void updateOrder(int formId, int fieldOrder, String operator) {
	}

	/**
	 * Method moveField.
	 * @param formId int
	 * @param fieldId String
	 * @param fieldOrder int
	 * @param order int
	 */
	public void moveField(int formId, String fieldId, int fieldOrder, int order) {
	}

	/**
	 * Method getFormList.
	 * @param page int
	 * @return List
	 */
	public List getFormList(int page) {
		return null;
	}

	/**
	 * Method updateStatus.
	 * @param formId String
	 * @param i int
	 */
	public void updateStatus(String formId, int i) {
	}

	/**
	 * Method createTable.
	 * @param frm Form
	 */
	public void createTable(Form frm) {
	}

	/**
	 * Method saveEntry.
	 * @param frm2 Form
	 */
	public void saveEntry(final Form frm2) {
	}

	/**
	 * Method initDbIdentifiers.
	 * @param id int
	 */
	public void initDbIdentifiers(int id) {
	}

	/**
	 * Method getEntryList.
	 * @param frm Form
	 * @param page Integer
	 * @param colName String
	 * @param colVal String
	 * @param sortCol String
	 * @param sortDir String
	 * @param limit boolean
	 * @return List
	 */
	public List getEntryList(final Form frm, Integer page, String colName,
			String colVal, String sortCol, String sortDir, boolean limit) {
		return null;
	}

	/**
	 * Method getEntry.
	 * @param frm Form
	 * @return Form
	 */
	public Form getEntry(final Form frm) {
		return frm;
	}

	/**
	 * Method updateEntryStatus.
	 * @param frm Form
	 * @param entryId String
	 * @param string String
	 */
	public void updateEntryStatus(Form frm, String entryId, String string) {
	}

	/**
	 * Method removeTemplate.
	 * @param formId String
	 */
	public void removeTemplate(String formId) {
	}

	/**
	 * Method removeLogo.
	 * @param formId String
	 */
	public void removeLogo(String formId) {
	}

	/**
	 * Method getPublicForms.
	 * @return List
	 */
	public List getPublicForms() {
		return null;
	}

	/**
	 * Method getFormEntryCount.
	 * @param frm Form
	 * @param status String
	 * @return int
	 */
	public int getFormEntryCount(Form frm, String status) {
		return 0;
	}

	/**
	 * Method getAttachment.
	 * @param entryId String
	 * @param columName String
	 * @param tableName String
	 * @return List
	 */
	public List getAttachment(String entryId, String columName, String tableName) {
		return null;
	}
}
