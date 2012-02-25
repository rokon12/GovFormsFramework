package bd.gov.forms.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import bd.gov.forms.domain.User;
import bd.gov.forms.web.AbstractFormTest;

public class UserDaoTest extends AbstractFormTest {

	@Autowired
	private UserDao dao;

	@Test
	public void userLoginTest() {
		User user = dao.getUser("1");

		assertNull(user);
	}
}
