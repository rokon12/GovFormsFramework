package bd.gov.forms.web.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;

public class PdfGenerator {
	private static final Logger log = Logger.getLogger(PdfGenerator.class);
	private OutputStream outputStream;

	public PdfGenerator(OutputStream os) {
		this.outputStream = os;
	}

	public void renderPdf(String htmlContent, HttpServletRequest request)
			throws Exception {
		log.debug("renderPdf");

		htmlContent = cleanUpHtml(htmlContent);
		htmlContent = addHeadBodyTag(htmlContent);

		htmlContent = addCss(htmlContent);

		log.debug("convertToXhtml-e:");
		OutputStream xhtmlOs = new ByteArrayOutputStream();

		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setCharEncoding(Configuration.UTF8);
		tidy.parse(new ByteArrayInputStream(htmlContent.getBytes()), xhtmlOs);

		xhtmlOs.flush();
		xhtmlOs.close();
		log.debug(xhtmlOs.toString());
		StringReader contentReader = new StringReader(xhtmlOs.toString());

		InputSource source = new InputSource(contentReader);

		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		// documentBuilder.setEntityResolver(new MyResolver(request));
		documentBuilder.setEntityResolver(new NoOpEntityResolver());

		Document xhtmlContent = documentBuilder.parse(source);
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(xhtmlContent, null);
		renderer.layout();

		// renderer.createPDF(outputStream);

		renderer.createPDF(outputStream);
		outputStream.close();
	}

	private String cleanUpHtml(String data) {
		log.debug("cleanUpHtml-e:");

		data = HtmlUtils.removeAllTags(data, "<script", "</script>", null);
		data = HtmlUtils.replaceAll(data, "<a .*?>|</a>", "");
		// data = HtmlUtils.replaceAll(data, "<img .*?>|</img>", "");
		data = HtmlUtils.replaceAll(data, "<link .*?>", "");
		// remove hidden field
		data = HtmlUtils.replaceAll(data, "<input.*?type=['\"]hidden['\"].*?>",
				"");
		// remove pagination from lister
		data = HtmlUtils
				.replaceAll(
						data,
						"(<div style=\"top: .*?px; position: absolute;\" id=\"pager\" class=\"pager\".*?</div>)",
						"");

		return data;
	}

	private String addHeadBodyTag(String data) {
		// data = "<html><head></head><body>" + data + "</body></html>";
		return data;
	}

	private String addCss(String data) {
		log.debug("addCss-e:");

		return data;
	}

	private String addHead(String data, HttpServletRequest request) {
		log.debug("addHeader-e:");
		StringBuilder sb = new StringBuilder(500);
		String header = sb.toString();
		data = HtmlUtils.replaceAll(data, "(<body.*?>)", "$1" + header);

		return data;

	}

	private String addFooter(String data) {
		log.debug("addFooter-e:");

		String footer = "iTax";

		data = HtmlUtils.replaceAll(data, "(<body.*?>)", "$1" + footer);
		return data;
	}
}