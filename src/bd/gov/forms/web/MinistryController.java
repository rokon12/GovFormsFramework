package bd.gov.forms.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import bd.gov.forms.dao.MinistryDao;
import bd.gov.forms.domain.Ministry;
import bd.gov.forms.domain.Role;
import bd.gov.forms.domain.User;

import bd.gov.forms.utils.FormUtil;

@Controller
@RequestMapping("/ministry")
public class MinistryController {
	@Autowired
	MinistryDao ministryDao;

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		List<Ministry> list = ministryDao.getAll();

		model.put("ministryList", list);

		return "ministry/index";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String insertForm(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		map.put("ministry", new Ministry());

		return "ministry/create";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String submitForm(@ModelAttribute("ministry") Ministry ministry,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {

		if (FormUtil.isEmpty(ministry.getMinistryName())
				|| FormUtil.isEmpty(ministry.getMinistryShortName())) {
			throw new RuntimeException("Required value not found.");
		}

		model.put("ministry", ministry);
		model.put("formAction", "ministry/create");

		ministryDao.save(ministry);

		model.put("message", "msg.form.submitted");
		model.put("msgType", "success");

		return "redirect:index.htm";
	}

	@RequestMapping(value = "dashboard", method = RequestMethod.GET)
	public String ministryDashBoard(
			@RequestParam(value = "ministry", required = true) int ministryId,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {

		String access = Role.checkRole(Role.ROLE_USER, (User) request
				.getSession().getAttribute("user"));
		if (access != null) {
			return access;
		}

		Ministry ministry = ministryDao.find(ministryId);
		model.put("ministryName", ministry.getMinistryName());

		return "ministry/dashboard";
	}
}
