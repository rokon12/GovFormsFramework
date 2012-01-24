package bd.gov.forms.web.pdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PdfFilter implements Filter, ApplicationContextAware {
	private static final Logger log = LoggerFactory.getLogger(PdfFilter.class);
	protected ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		// Check to see if this filter should apply.
		if (!"pdf".equals(request.getParameter("reportViewType"))) {
			filterChain.doFilter(request, response);
			return;
		}

		// Capture the content for this request
		ContentCaptureServletResponse capContent = new ContentCaptureServletResponse(
				response);
		filterChain.doFilter(request, capContent);

		response.setContentType("application/pdf; charset=UTF-8");
		response.setHeader("Content-disposition",
				"attachment; filename=\"report.pdf\"");
		response.addHeader("Cache-Control", "-1");

		try {
			// Get the html content
			String htmlContent = capContent.getHtlmContent();

			PdfGenerator pdfGenerator = new PdfGenerator(
					response.getOutputStream());
			pdfGenerator.renderPdf(htmlContent, request);

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {

	}
}
