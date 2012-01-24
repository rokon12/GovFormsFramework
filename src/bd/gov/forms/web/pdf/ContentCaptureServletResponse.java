package bd.gov.forms.web.pdf;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class ContentCaptureServletResponse extends HttpServletResponseWrapper {

	private ByteArrayOutputStream contentBuffer;
	private PrintWriter writer;

	public ContentCaptureServletResponse(HttpServletResponse originalResponse) {
		super(originalResponse);
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (writer == null) {
			contentBuffer = new ByteArrayOutputStream();
			writer = new PrintWriter(contentBuffer);
		}
		return writer;
	}

	public String getHtlmContent() throws IOException {
		getWriter().flush();
		return new String(contentBuffer.toByteArray());
	}
}