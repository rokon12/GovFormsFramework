package bd.gov.forms.web.pdf;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PdfFilter implements Filter, ApplicationContextAware {

	@SuppressWarnings({ "UnusedDeclaration" })
	private static final Logger log = Logger.getLogger(PdfFilter.class);
	protected ApplicationContext applicationContext;

	public void init(FilterConfig config) {
		/* do nothing */
	}

	public void destroy() {
		/* do nothing */
	}

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
			
			pdfGenerator.renderPdf(htmlContent, request, response);

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}