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
package bd.gov.forms.web;

import bd.gov.forms.dao.DocumentDao;
import bd.gov.forms.dao.FormDao;
import bd.gov.forms.dao.ListDao;
import bd.gov.forms.domain.Document;
import bd.gov.forms.domain.Field;
import bd.gov.forms.domain.Form;
import bd.gov.forms.domain.ListData;
import bd.gov.forms.domain.User;
import bd.gov.forms.utils.ContentType;
import bd.gov.forms.utils.FormUtil;
import bd.gov.forms.utils.ListUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SeekableByteChannel;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bd.gov.forms.utils.UserAccessChecker;
import bd.gov.forms.web.pdf.ContentCaptureServletResponse;
import bd.gov.forms.web.pdf.PdfGenerator;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

/**
 * @author asif
 * @version $Revision: 1.0 $
 */
@Controller
@RequestMapping("/formBuilder")
public class FormBuilder {
	private static final Logger log = LoggerFactory
			.getLogger(FormBuilder.class);

	@Autowired
	private FormDao formDao;
	@Autowired
	private ListDao listDao;
	@Autowired
	MessageSource messageSource;

	@Autowired
	private DocumentDao documentDao;

	/**
	 * Method initBinder.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param binder
	 *            ServletRequestDataBinder
	 * @throws ServletException
	 */
	@InitBinder
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}

	/**
	 * Method newForm.
	 * 
	 * @param model
	 *            ModelMap
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	@RequestMapping(value = "/newForm", method = RequestMethod.GET)
	public String newForm(ModelMap model, HttpServletRequest request) {
		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}
		Form form = new Form();
		model.put("formDetailsCmd", form);
		model.put("formAction", "saveForm");

		return "forms/formDetails";
	}

	/**
	 * Method saveForm.
	 * 
	 * @param form
	 *            Form
	 * @param result
	 *            BindingResult
	 * @param request
	 *            HttpServletRequest
	 * @param model
	 *            ModelMap
	 * @return String
	 */
	@RequestMapping(value = "/saveForm", method = RequestMethod.POST)
	public String saveForm(@ModelAttribute("formDetailsCmd") Form form,
			BindingResult result, HttpServletRequest request, ModelMap model) {

		model.put("formDetailsCmd", form);
		model.put("formAction", "saveForm");

		if (result.hasFieldErrors() || result.hasErrors()) {
			return "forms/formDetails";
		}

		log.debug("form->save");

		form.setFormId(Long.toString(System.nanoTime())
				+ new Long(new Random().nextLong()));
		form.setStatus(1);

		User user = (User) request.getSession().getAttribute("user");

		form.setMinistry(user.getMinistry());

		formDao.saveForm(form);

		model.put("message", "msg.form.submitted");
		model.put("msgType", "success");

		return "redirect:formList.htm";
	}

	/**
	 * Method editForm.
	 * 
	 * @param formId
	 *            String
	 * @param model
	 *            ModelMap
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	@RequestMapping(value = "/editForm", method = RequestMethod.GET)
	public String editForm(
			@RequestParam(value = "formId", required = true) String formId,
			ModelMap model, HttpServletRequest request) {
		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}

		Form form = null;

		if (formId != null) {
			form = formDao.getForm(formId);
		}

		if (form != null) {
			if (form.getTemplateFileName() != null
					&& "".equals(form.getTemplateFileName().trim())) {
				form.setTemplateFileName(null);
			}

			if (form.getLogoName() != null
					&& "".equals(form.getLogoName().trim())) {
				form.setLogoName(null);
			}
		}

		model.put("formDetailsCmd", form);
		model.put("formAction", "updateForm");

		return "forms/formDetails";
	}

	/**
	 * Method updateForm.
	 * 
	 * @param form
	 *            Form
	 * @param result
	 *            BindingResult
	 * @param request
	 *            HttpServletRequest
	 * @param model
	 *            ModelMap
	 * @return String
	 */
	@RequestMapping(value = "/updateForm", method = RequestMethod.POST)
	public String updateForm(@ModelAttribute("formDetailsCmd") Form form,
			BindingResult result, HttpServletRequest request, ModelMap model) {

		model.put("formDetailsCmd", form);
		model.put("formAction", "updateForm");

		if (result.hasFieldErrors() || result.hasErrors()) {
			return "forms/formDetails";
		}

		log.debug("form->update");
		log.debug("file size: {}", form.getPdfTemplate().length);

		formDao.updateForm(form);

		model.put("message", "msg.form.updated");
		model.put("msgType", "success");

		return "redirect:formList.htm";
	}

	/**
	 * Method pdfTemplate.
	 * 
	 * @param formId
	 *            String
	 * @param model
	 *            ModelMap
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException
	 */
	@RequestMapping(value = "/dloadTemplate", method = RequestMethod.GET)
	public void pdfTemplate(
			@RequestParam(value = "formId", required = true) String formId,
			ModelMap model, HttpServletResponse response) throws IOException {
		// response.setContentType("application/pdf");
		// TODO: file name

		byte[] fileContent = formDao.getTemplateContent(formId);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(fileContent);

		OutputStream os = response.getOutputStream();

		out.writeTo(os);
		os.flush();
	}

	/**
	 * Method logo.
	 * 
	 * @param formId
	 *            String
	 * @param model
	 *            ModelMap
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException
	 */
	@RequestMapping(value = "/dloadLogo", method = RequestMethod.GET)
	public void logo(
			@RequestParam(value = "formId", required = true) String formId,
			ModelMap model, HttpServletResponse response) throws IOException {
		byte[] fileContent = formDao.getLogoContent(formId);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(fileContent);

		OutputStream os = response.getOutputStream();

		out.writeTo(os);
		os.flush();
	}

	/**
	 * Method removeTemplate.
	 * 
	 * @param formId
	 *            String
	 * @param model
	 *            ModelMap
	 * @param response
	 *            HttpServletResponse
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/removeTemplate", method = RequestMethod.GET)
	public String removeTemplate(
			@RequestParam(value = "formId", required = true) String formId,
			ModelMap model, HttpServletResponse response) throws IOException {
		formDao.removeTemplate(formId);
		return "redirect:editForm.htm?formId=" + formId;
	}

	/**
	 * Method removeLogo.
	 * 
	 * @param formId
	 *            String
	 * @param model
	 *            ModelMap
	 * @param response
	 *            HttpServletResponse
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/removeLogo", method = RequestMethod.GET)
	public String removeLogo(
			@RequestParam(value = "formId", required = true) String formId,
			ModelMap model, HttpServletResponse response) throws IOException {
		formDao.removeLogo(formId);
		return "redirect:editForm.htm?formId=" + formId;
	}

	/**
	 * Method markChecked.
	 * 
	 * @param formId
	 *            String
	 * @param entryId
	 *            String
	 * @param page
	 *            Integer
	 * @param colName
	 *            String
	 * @param colVal
	 *            String
	 * @param sortCol
	 *            String
	 * @param sortDir
	 *            String
	 * @param checked
	 *            boolean
	 * @param model
	 *            ModelMap
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/markChecked", method = RequestMethod.GET)
	public String markChecked(
			@RequestParam(value = "formId", required = true) String formId,
			@RequestParam(value = "entryId", required = true) String entryId,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "colName", required = true) String colName,
			@RequestParam(value = "colVal", required = true) String colVal,
			@RequestParam(value = "sortCol", required = false) String sortCol,
			@RequestParam(value = "sortDir", required = false) String sortDir,
			@RequestParam(value = "checked", required = true) boolean checked,
			ModelMap model, HttpServletRequest request) throws IOException {

		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}

		Form role = formDao.getForm(formId);
		String status = checked ? "Checked" : "Submitted";
		formDao.updateEntryStatus(role, entryId, status);

		return "redirect:entryList.htm?formId=" + FormUtil.formatValue(formId)
				+ "&page=" + FormUtil.formatValue(page) + "&colName="
				+ FormUtil.formatValue(colName) + "&colVal="
				+ FormUtil.formatValue(colVal) + "&sortCol="
				+ FormUtil.formatValue(sortCol) + "&sortDir="
				+ FormUtil.formatValue(sortDir);
	}

	/**
	 * Method getEntryListHeaders.
	 * 
	 * @param form
	 *            Form
	 * @return List
	 */
	public List getEntryListHeaders(Form form) {
		List list = new ArrayList();

		list.add("Date");
		list.add("Time");
		list.add("Status");
		for (Field field : form.getFields()) {
			list.add(field.getLabel());
		}

		return list;
	}

	/**
	 * Method excelExport.
	 * 
	 * @param formId
	 *            String
	 * @param page
	 *            Integer
	 * @param colName
	 *            String
	 * @param colVal
	 *            String
	 * @param sortCol
	 *            String
	 * @param sortDir
	 *            String
	 * @param model
	 *            ModelMap
	 * @param response
	 *            HttpServletResponse
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/excelExport", method = RequestMethod.GET)
	public String excelExport(
			@RequestParam(value = "formId", required = true) String formId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "colName", required = false) String colName,
			@RequestParam(value = "colVal", required = false) String colVal,
			@RequestParam(value = "sortCol", required = false) String sortCol,
			@RequestParam(value = "sortDir", required = false) String sortDir,
			ModelMap model, HttpServletResponse response,
			HttpServletRequest request) throws IOException {

		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}

		if (page == null) {
			page = 1;
		}

		Form form = formDao.getFormWithFields(formId);

		List<HashMap> list = formDao.getEntryList(form, page, colName, colVal,
				sortCol, sortDir, false);
		List<String> headers = getEntryListHeaders(form);

		response.setContentType("application/vnd.ms-excel");
		// TODO: file name

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Excel Report");

		int r = 0;
		HSSFRow row = sheet.createRow((short) r++);
		int count = 0;
		for (String header : headers) {
			HSSFCell cell = row.createCell(count++);
			cell.setCellValue(header);
		}

		for (HashMap hashmap : list) {
			row = sheet.createRow((short) r++);
			count = 0;

			HSSFCell cell = row.createCell(count++);
			cell.setCellValue((String) hashmap.get("entry_date"));

			cell = row.createCell(count++);
			cell.setCellValue((String) hashmap.get("entry_time"));

			cell = row.createCell(count++);
			cell.setCellValue((String) hashmap.get("entry_status"));

			for (Field field : form.getFields()) {
				cell = row.createCell(count++);
				cell.setCellValue((String) hashmap.get(field.getColName()));
			}
		}

		String fileName = "Report-" + formId + ".xls";
		response.setHeader("Content-Disposition", "inline; filename="
				+ fileName);
		response.setContentType("application/vnd.ms-excel");

		ServletOutputStream outputStream = response.getOutputStream();
		sheet.getWorkbook().write(outputStream);
		outputStream.flush();

		return null;
	}

	/**
	 * Method printHtml.
	 * 
	 * @param formId
	 *            String
	 * @param entryId
	 *            String
	 * @param model
	 *            ModelMap
	 * @param response
	 *            HttpServletResponse
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/printHtml", method = RequestMethod.GET)
	public String printHtml(
			@RequestParam(value = "formId", required = true) String formId,
			@RequestParam(value = "entryId", required = true) String entryId,
			ModelMap model, HttpServletResponse response) throws IOException {

		byte[] fileContent = formDao.getTemplateContent(formId);
		Form form = formDao.getFormWithFields(formId);
		form.setEntryId(entryId);

		form = formDao.getEntry(form);
		String report = "";

		if (fileContent != null) {
			report = new String(fileContent, "UTF8");

			for (Field field : form.getFields()) {
				if (fieldTypeIsNotOfFileOrNoteOrSection(field)) {
					report = report.replaceAll("#" + field.getColName()
							+ ":label#", field.getLabel());
					report = report.replaceAll("#" + field.getColName()
							+ ":value#", field.getStrVal());
				}
			}
		} else {
			report += "<table cellspacing='0' cellpadding='0' style='border:1px solid #aaa;width:98%;'>";

			for (Field field : form.getFields()) {
				if (fieldTypeIsNotOfFileOrNoteOrSection(field)) {
					report += "<tr>";
					report += "<td>";
					report += field.getLabel();
					report += "</td>";
					report += "<td>";
					report += field.getStrVal();
					report += "</td>";

					report += "</tr>";

				}
			}

			report += "</table>";
		}

		model.put("report", report);

		return "reports/formReport";
	}

	/**
	 * Method fieldTypeIsNotOfFileOrNoteOrSection.
	 * 
	 * @param field
	 *            Field
	 * @return boolean
	 */
	private boolean fieldTypeIsNotOfFileOrNoteOrSection(Field field) {
		return !"file".equals(field.getType())
				&& !"note".equals(field.getType())
				&& !"section".equals(field.getType());
	}

	/**
	 * Method activate.
	 * 
	 * @param formId
	 *            String
	 * @param model
	 *            ModelMap
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/activate", method = RequestMethod.GET)
	public String activate(
			@RequestParam(value = "formId", required = true) String formId,
			ModelMap model, HttpServletRequest request) throws IOException {

		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}

		Form form = formDao.getForm(formId);

		if (form.getStatus() == 1) {
			formDao.initDbIdentifiers(form.getId());
			form = formDao.getFormWithFields(formId);
			formDao.createTable(form);
		}

		formDao.updateStatus(formId, 2);

		return "redirect:formList.htm";
	}

	/**
	 * Method deactivate.
	 * 
	 * @param formId
	 *            String
	 * @param model
	 *            ModelMap
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/deactivate", method = RequestMethod.GET)
	public String deactivate(
			@RequestParam(value = "formId", required = true) String formId,
			ModelMap model, HttpServletRequest request) throws IOException {

		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}

		formDao.updateStatus(formId, 3);

		return "redirect:formList.htm";
	}

	/**
	 * Method deleteForm.
	 * 
	 * @param formId
	 *            String
	 * @param model
	 *            ModelMap
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/deleteForm", method = RequestMethod.GET)
	public String deleteForm(
			@RequestParam(value = "formId", required = true) String formId,
			ModelMap model, HttpServletRequest request) throws IOException {

		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}

		formDao.deleteForm(formId);

		return "redirect:formList.htm";
	}

	/**
	 * Method deleteField.
	 * 
	 * @param fieldId
	 *            String
	 * @param formId
	 *            String
	 * @param model
	 *            ModelMap
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/deleteField", method = RequestMethod.GET)
	public String deleteField(
			@RequestParam(value = "fieldId", required = true) String fieldId,
			@RequestParam(value = "formId", required = true) String formId,
			ModelMap model, HttpServletRequest request) throws IOException {

		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}

		log.debug("formBuilder->deleteField");

		Form form = formDao.getForm(formId);

		if (form.getStatus() > 1) {
			model.put("message", "field.delete.failed");
			model.put("msgType", "failed");
			return "redirect:formList.htm";
		}

		Field field = formDao.getField(fieldId);
		formDao.deleteField(fieldId, form.getId());
		formDao.updateOrder(field.getFormId(), field.getFieldOrder(), "-");

		return "redirect:design.htm?formId=" + formId;
	}

	/**
	 * Method moveField.
	 * 
	 * @param fieldId
	 *            String
	 * @param formId
	 *            String
	 * @param order
	 *            int
	 * @param model
	 *            ModelMap
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/moveField", method = RequestMethod.GET)
	public String moveField(
			@RequestParam(value = "fieldId", required = true) String fieldId,
			@RequestParam(value = "formId", required = true) String formId,
			@RequestParam(value = "order", required = true) int order,
			ModelMap model, HttpServletRequest request) throws IOException {

		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}

		log.debug("formBuilder->moveField. fieldId: {}", fieldId);

		Field field = formDao.getField(fieldId);

		formDao.moveField(field.getFormId(), field.getFieldId(),
				field.getFieldOrder(), order);

		return "redirect:design.htm?formId=" + formId;
	}

	/**
	 * Method newField.
	 * 
	 * @param formId
	 *            String
	 * @param type
	 *            String
	 * @param order
	 *            int
	 * @param model
	 *            ModelMap
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/newField", method = RequestMethod.GET)
	public String newField(
			@RequestParam(value = "formId", required = true) String formId,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "order", required = true) int order,
			ModelMap model, HttpServletRequest request) throws IOException {

		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}

		Form form = formDao.getForm(formId);
		if (form.getStatus() > 1) {
			model.put("message", "field.add.failed");
			model.put("msgType", "failed");

			return "redirect:formList.htm";
		}

		Field field = new Field();
		field.setFieldOrder(order);
		field.setType(type); // TODO: invalid type
		field.setFormIdStr(formId);
		field.setRequired(0);

		model.put("fieldCmd", field);
		model.put("formAction", "saveField");

		return "forms/field";
	}

	/**
	 * Method saveField.
	 * 
	 * @param field
	 *            Field
	 * @param result
	 *            BindingResult
	 * @param request
	 *            HttpServletRequest
	 * @param model
	 *            ModelMap
	 * @return String
	 */
	@RequestMapping(value = "/saveField", method = RequestMethod.POST)
	public String saveField(@ModelAttribute("fieldCmd") Field field,
			BindingResult result, HttpServletRequest request, ModelMap model) {

		model.put("fieldCmd", field);
		model.put("formAction", "saveField");

		if (result.hasFieldErrors() || result.hasErrors()) {
			return "field";
		}

		log.debug("field->save");

		Form frm = formDao.getForm(field.getFormIdStr());
		field.setFormId(frm.getId());
		field.setFieldId(Long.toString(System.nanoTime())
				+ new Long(new Random().nextLong()));

		formDao.updateOrder(field.getFormId(), field.getFieldOrder(), "+");
		formDao.saveField(field);

		model.put("message", "msg.field.submitted");
		model.put("msgType", "success");

		return "redirect:design.htm?formId=" + field.getFormIdStr();
	}

	/**
	 * Method editField.
	 * 
	 * @param formId
	 *            String
	 * @param fieldId
	 *            String
	 * @param model
	 *            ModelMap
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	@RequestMapping(value = "/editField", method = RequestMethod.GET)
	public String editField(
			@RequestParam(value = "formId", required = true) String formId,
			@RequestParam(value = "fieldId", required = true) String fieldId,
			ModelMap model, HttpServletRequest request) {

		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}

		Field field = null;

		if (fieldId != null) {
			field = formDao.getField(fieldId);
		}

		if (field != null) {
			field.setFormIdStr(formId);
		}

		model.put("fieldCmd", field);
		model.put("formAction", "updateField");

		return "forms/field";
	}

	/**
	 * Method updateField.
	 * 
	 * @param field
	 *            Field
	 * @param result
	 *            BindingResult
	 * @param request
	 *            HttpServletRequest
	 * @param model
	 *            ModelMap
	 * @return String
	 */
	@RequestMapping(value = "/updateField", method = RequestMethod.POST)
	public String updateField(@ModelAttribute("fieldCmd") Field field,
			BindingResult result, HttpServletRequest request, ModelMap model) {

		model.put("fieldCmd", field);
		model.put("formAction", "updateField");

		if (result.hasFieldErrors() || result.hasErrors()) {
			return "forms/field";
		}

		log.debug("field->update");

		Form form = formDao.getForm(field.getFormIdStr());
		field.setFormId(form.getId());
		formDao.updateField(field);

		model.put("message", "msg.field.updated");
		model.put("msgType", "success");

		return "redirect:design.htm?formId=" + field.getFormIdStr();
	}

	/**
	 * Method formList.
	 * 
	 * @param page
	 *            Integer
	 * @param model
	 *            ModelMap
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/formList", method = RequestMethod.GET)
	public String formList(
			@RequestParam(value = "page", required = false) Integer page,
			ModelMap model, HttpServletRequest request) throws IOException {

		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}

		if (page == null) {
			page = 1;
		}

		List list = formDao.getFormList(page);
		model.put("forms", list);

		return "forms/formList";
	}

	/**
	 * Method formInfo.
	 * 
	 * @param formId
	 *            String
	 * @param model
	 *            ModelMap
	 * @param response
	 *            HttpServletResponse
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/formInfo", method = RequestMethod.GET)
	public String formInfo(
			@RequestParam(value = "formId", required = true) String formId,
			ModelMap model, HttpServletResponse response) throws IOException {

		Form form = formDao.getFormWithFields(formId);
		model.put("form", form);

		return "forms/formInfo";
	}

	/**
	 * Method design.
	 * 
	 * @param formId
	 *            String
	 * @param model
	 *            ModelMap
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/design", method = RequestMethod.GET)
	public String design(
			@RequestParam(value = "formId", required = true) String formId,
			ModelMap model, HttpServletRequest request) throws Exception {

		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}

		Form frm = null;

		if (formId != null) {
			frm = formDao.getFormWithFields(formId);
		}

		if (frm != null) {
			if (frm.getStatus() > 1) {
				model.put("doneMessage", "msg.access.denied");
				model.put("doneMsgType", "failed");

				return "redirect:done.htm";
			}
			initForm(frm);
		}

		model.put("formCmd", frm);
		model.put("formAction", "formDesign");

		return "forms/form";
	}

	/**
	 * Method formDesign.
	 * 
	 * @param form
	 *            Form
	 * @param result
	 *            BindingResult
	 * @param request
	 *            HttpServletRequest
	 * @param model
	 *            ModelMap
	 * @return String
	 */
	@RequestMapping(value = "/formDesign", method = RequestMethod.POST)
	public String formDesign(@ModelAttribute("fieldCmd") Form form,
			BindingResult result, HttpServletRequest request, ModelMap model) {

		model.put("message", "msg.form.updated"); // TODO: Update msg
		model.put("msgType", "success"); // TODO: Update msg

		return "redirect:design.htm?formId=" + form.getFormId();
	}

	/**
	 * Method newEntry.
	 * 
	 * @param formId
	 *            String
	 * @param model
	 *            ModelMap
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/newEntry", method = RequestMethod.GET)
	public String newEntry(
			@RequestParam(value = "formId", required = true) String formId,
			ModelMap model) throws Exception {

		Form form = null;

		if (formId != null) {
			form = formDao.getFormWithFields(formId);
		}

		if (form != null) {
			if (form.getStatus() != 2) {
				model.put("doneMessage", "msg.access.denied");
				model.put("doneMsgType", "failed");

				return "redirect:done.htm";
			}
			initForm(form);
		}

		model.put("formCmd", form);
		model.put("formAction", "saveEntry");

		return "forms/form";
	}

	/**
	 * Method saveEntry.
	 * 
	 * @param formCmd
	 *            Form
	 * @param result
	 *            BindingResult
	 * @param request
	 *            HttpServletRequest
	 * @param model
	 *            ModelMap
	 * @return String
	 */
	@RequestMapping(value = "/saveEntry", method = RequestMethod.POST)
	public String saveEntry(@ModelAttribute("formCmd") Form formCmd,
			BindingResult result, HttpServletRequest request, ModelMap model) {

		log.debug("form->save. formId: {}", formCmd.getFormId());

		Form formDb = formDao.getFormWithFields(formCmd.getFormId());

		// formDb.setEntryId(Long.toString(System.nanoTime()) + new Long(new
		// Random().nextLong()));
		formDb.setEntryId(Long.toString(System.nanoTime()));
		formDb.setEntryStatus("Submitted");

		if (formDb.getFields() != null && formDb.getFields().size() > 0) {
			for (int i = 0; i < formDb.getFields().size(); i++) {
				Field fldCmd = formCmd.getFields().get(i);
				Field fldDb = formDb.getFields().get(i);

				if (fldCmd.getFieldId().equals(fldDb.getFieldId())) {
					fldDb.setByteVal(fldCmd.getByteVal());
					fldDb.setStrVal(fldCmd.getStrVal());
				} else {
					throw new RuntimeException("Field IDs do not match");
				}

				if (fldDb.getRequired() == 1 && fldDb.getByteVal() == null
						&& fldDb.getStrVal() == null) {
					throw new RuntimeException("Required value not found.");
				}
			}
		}

		formDao.saveEntry(formDb);

		model.put("doneMessage", "msg.form.submitted");
		model.put("doneMsgType", "success");
		model.put("trackId", formDb.getEntryId());

		return "redirect:done.htm";
	}

	/**
	 * Method done.
	 * 
	 * @param message
	 *            String
	 * @param msgType
	 *            String
	 * @param trackId
	 *            String
	 * @param model
	 *            ModelMap
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/done", method = RequestMethod.GET)
	public String done(
			@RequestParam(value = "doneMessage", required = true) String message,
			@RequestParam(value = "doneMsgType", required = true) String msgType,
			@RequestParam(value = "trackId", required = false) String trackId,
			ModelMap model) throws Exception {

		model.put("doneMessage", message);
		model.put("doneMsgType", msgType);
		model.put("trackId", trackId);

		return "forms/done";
	}

	/**
	 * Method index.
	 * 
	 * @param model
	 *            ModelMap
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) throws Exception {
		List list = formDao.getPublicForms();
		model.put("forms", list);

		return "index";
	}

	/**
	 * Method entryList.
	 * 
	 * @param page
	 *            Integer
	 * @param formId
	 *            String
	 * @param colName
	 *            String
	 * @param colVal
	 *            String
	 * @param sortCol
	 *            String
	 * @param sortDir
	 *            String
	 * @param model
	 *            ModelMap
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 * @throws IOException
	 */
	@RequestMapping(value = "/entryList", method = RequestMethod.GET)
	public String entryList(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "formId", required = true) String formId,
			@RequestParam(value = "colName", required = false) String colName,
			@RequestParam(value = "colVal", required = false) String colVal,
			@RequestParam(value = "sortCol", required = false) String sortCol,
			@RequestParam(value = "sortDir", required = false) String sortDir,
			ModelMap model, HttpServletRequest request) throws IOException {

		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}

		if (page == null) {
			page = 1;
		}

		if (sortDir == null || "".equals(sortDir)) {
			sortDir = "DESC";
		}
		if (!"ASC".equals(sortDir) && !"DESC".equals(sortDir)) {
			sortDir = "DESC";
		}
		log.debug("sort order: {}", sortDir);

		Form form = formDao.getFormWithFields(formId);
		int totalCount = formDao.getFormEntryCount(form, null);
		int checkedCount = formDao.getFormEntryCount(form, "Checked");
		int uncheckedCount = totalCount - checkedCount;
		model.put("totalCount", totalCount);
		model.put("checkedCount", checkedCount);
		model.put("uncheckedCount", uncheckedCount);

		List list = formDao.getEntryList(form, page, colName, colVal, sortCol,
				sortDir, true);

		model.put("entries", list);
		model.put("headers", getEntryListHeaders(form));
		model.put("form", form);
		model.put("formId", formId);
		model.put("page", page);
		model.put("colName", colName);
		model.put("colVal", colVal);
		model.put("sortCol", sortCol);
		model.put("sortDirX", sortDir);

		if ("ASC".equals(sortDir)) {
			sortDir = "DESC";
		} else {
			sortDir = "ASC";
		}
		log.debug("sort order: {}", sortDir);

		model.put("sortDir", sortDir);
		model.put("totalPages", form.getTotalPages());

		return "forms/entryList";
	}

	/**
	 * Method initForm.
	 * 
	 * @param form
	 *            Form
	 * @throws Exception
	 */
	private void initForm(Form form) throws Exception {
		for (Field field : form.getFields()) {
			String css = "";
			if (field.getRequired() == 1) {
				css += "required";
			}
			if (field.getInputType() != null) {
				css += " " + field.getInputType();
			}
			field.setCssClass(css);

			if (field.getListDataId() != 0) {
				String sysId = listDao.getListDataSysId(field.getListDataId());
				ListData lst = listDao.getListData(sysId);
				List list = ListUtil.getList(field.getType(), lst);
				field.setList(list);
			}
		}
	}

	/**
	 * Method getInputType.
	 * 
	 * @return Map
	 */
	@ModelAttribute("inputType")
	public Map getInputType() {
		Map fieldType = new HashMap();
		fieldType.put("", "");
		fieldType.put("number", "Number");
		fieldType.put("currency", "Currency");
		fieldType.put("date", "Date");
		fieldType.put("email", "E-mail");

		return fieldType;
	}

	/**
	 * Method getYesNoOption.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param locale
	 *            Locale
	 * @return Map
	 */
	@ModelAttribute("yesNoOption")
	public Map getYesNoOption(HttpServletRequest request, Locale locale) {
		Map options = new HashMap();

		options.put("1", messageSource.getMessage("yes", null, locale));
		options.put("0", messageSource.getMessage("no", null, locale));

		return options;
	}

	/**
	 * Method getListSrc.
	 * 
	 * @return Map
	 */
	@ModelAttribute("listSrc")
	public Map getListSrc() {
		Map map = new LinkedHashMap();

		List<ListData> list = listDao.getListDataList();

		for (ListData listData : list) {
			map.put(listData.getId(), listData.getName());
		}
		return map;
	}

	/**
	 * Method downloadForm.
	 * 
	 * @param formId
	 *            String
	 */
	@RequestMapping(value = "/downloadForm", method = RequestMethod.GET)
	public void downloadForm(
			@RequestParam(value = "formId", required = true) String formId) {

		Form form = formDao.getForm(formId);
		List<Field> fields = form.getFields();
		Field field = new Field();

	}

	/**
	 * 
	 * * @param model ModelMap
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 **/
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String sayHello(ModelMap model, HttpServletRequest request) {

		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}

		Document document = new Document();
		model.put("document", document);
		return "doc/form";
	}

	/**
	 * Method addDocument.
	 * 
	 * @param document
	 *            Document
	 * @param file
	 *            MultipartFile
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String addDocument(@ModelAttribute("document") Document document,
			@RequestParam("file") MultipartFile file, HttpServletRequest request) {

		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}
		try {
			document.setFilename(file.getOriginalFilename());
			document.setContent(file.getBytes());
			document.setContentType(file.getContentType());
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			documentDao.save(document);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:list.htm";
	}

	/**
	 * Method listOfDoc.
	 * 
	 * @param model
	 *            ModelMap
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String listOfDoc(ModelMap model, HttpServletRequest request) {
		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}

		List<Document> list = documentDao.listAll();
		model.put("documentList", list);

		return "doc/list";
	}

	/**
	 * Method download.
	 * 
	 * @param documentId
	 *            Integer
	 * @param response
	 *            HttpServletResponse
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	@RequestMapping(value = "/download/{documentId}")
	public String download(@PathVariable("documentId") Integer documentId,
			HttpServletResponse response, HttpServletRequest request) {

		log.debug("download->id: " + documentId);

		String access = UserAccessChecker.check(request);
		if (access != null) {
			return access;
		}

		Document doc = documentDao.find(documentId);
		try {
			response.setHeader("Content-Disposition", "inline;filename=\""
					+ doc.getFilename() + "\"");

			OutputStream out = response.getOutputStream();
			response.setContentType(doc.getContentType());

			InputStream stream = new ByteArrayInputStream(doc.getContent());
			IOUtils.copy(stream, out);
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Method remove.
	 * 
	 * @param documentId
	 *            Integer
	 * @return String
	 */
	@RequestMapping(value = "/remove/{documentId}")
	public String remove(@PathVariable("documentId") Integer documentId) {
		documentDao.delete(documentId);
		return "redirect:/list.htm";
	}

	/**
	 * Restful web service
	 * 
	 * * @param id String
	 * 
	 * @return Form
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/entry/{id}", headers = "Accept=application/json")
	public @ResponseBody
	Form entryListWebservice(@PathVariable("id") String id) {

		// String access = UserAccessChecker.check(request);
		// if (access != null) {
		// return access;
		// }

		Form form = formDao.getForm(id);
		return form;
	}

	/**
	 * Method printPdf.
	 * 
	 * @param formId
	 *            String
	 * @param entryId
	 *            String
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 */
	@RequestMapping(value = "printPdf", method = RequestMethod.GET)
	public void printPdf(
			@RequestParam(value = "formId", required = true) String formId,
			@RequestParam(value = "entryId", required = true) String entryId,
			HttpServletRequest request, HttpServletResponse response) {

		byte[] fileContent = formDao.getTemplateContent(formId);
		Form form = formDao.getFormWithFields(formId);
		form.setEntryId(entryId);

		response.setContentType("application/pdf; charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename=\""
				+ formId + ".pdf\"");
		response.addHeader("Cache-Control", "-1");

		form = formDao.getEntry(form);
		String report = "";

		if (fileContent != null) {
			try {
				report = new String(fileContent, "UTF8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			for (Field field : form.getFields()) {
				if (fieldTypeIsNotOfFileOrNoteOrSection(field)) {
					report = report.replaceAll("#" + field.getColName()
							+ ":label#", field.getLabel());
					report = report.replaceAll("#" + field.getColName()
							+ ":value#", field.getStrVal());
				}
			}
		} else {
			report += "<table cellspacing='0' cellpadding='0' style='border:1px solid #aaa;width:98%;'>";

			for (Field field : form.getFields()) {
				if (fieldTypeIsNotOfFileOrNoteOrSection(field)) {
					report += "<tr>";
					report += "<td>";
					report += field.getLabel();
					report += "</td>";
					report += "<td>";
					report += field.getStrVal();
					report += "</td>";

					report += "</tr>";

				}
			}

			report += "</table>";
		}

		try {
			PdfGenerator pdfGenerator = new PdfGenerator(
					response.getOutputStream());
			pdfGenerator.renderPdf(report, request);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method downloadAttachemnt.
	 * 
	 * @param entryId
	 *            String
	 * @param columName
	 *            String
	 * @param tableName
	 *            String
	 * @param response
	 *            HttpServletResponse
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	@RequestMapping(value = "downloadAttachment", method = RequestMethod.GET)
	public String downloadAttachemnt(
			@RequestParam(value = "entryId", required = true) String entryId,
			@RequestParam(value = "columName", required = true) String columName,
			@RequestParam(value = "tableName", required = true) String tableName,
			HttpServletResponse response, HttpServletRequest request) {
		String access = UserAccessChecker.check(request);

		if (access != null) {
			return access;
		}

		List<HashMap> list = formDao.getAttachment(entryId, columName,
				tableName);
		if (list != null && list.size() > 0) {

			Map map = list.get(0);

			try {
				String fileName = (String) map.get("name");
				String nameOnly = (String) fileName.subSequence(0,
						fileName.indexOf('.'));
				response.setHeader("Content-Disposition", "inline;filename=\""
						+ nameOnly + "\"");

				OutputStream out = response.getOutputStream();

				String contentType = (String) fileName.subSequence(
						fileName.indexOf('.'), fileName.length());
				System.out.println("ContentType: " + contentType);
				System.out
						.println("return Type:"
								+ ContentType.getInstance().getContentType(
										contentType));

				response.setContentType(ContentType.getInstance()
						.getContentType(contentType));

				InputStream stream = new ByteArrayInputStream(
						(byte[]) map.get("file"));
				IOUtils.copy(stream, out);
				out.flush();
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
