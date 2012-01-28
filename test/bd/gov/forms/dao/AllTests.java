package bd.gov.forms.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import bd.gov.forms.web.MinistryController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/dispatcher-servlet.xml")
public class AllTests {
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private MinistryController controller;
	private AnnotationMethodHandlerAdapter adapter;

	@Autowired
	private ApplicationContext applicationContext;

	@Before
	public void setUp() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		response.setOutputStreamAccessAllowed(true);
		controller = new MinistryController();
		adapter = new AnnotationMethodHandlerAdapter();
	}

	@Test
	public void findRelatedVideosTest() throws Exception {
		request.setRequestURI("/ministry/index.htm");
		request.setMethod("GET");
		// request.addParameter("myParam", "myValue");
		adapter.handle(request, response, controller);

		System.out.println(response.getContentAsString());
	}

}
