package bd.gov.forms.web.pdf;

import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

public class PdfGenerator {
	private static final Logger log = LoggerFactory.getLogger(PdfFilter.class);
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
		// htmlContent = addFooter(htmlContent);

		OutputStream xhtmlOs = new ByteArrayOutputStream();

		// byte[] byteArray = htmlContent.getBytes("UTF-8");
		// xhtmlOs.write(byteArray);
		// xhtmlOs.flush();
		// xhtmlOs.close();

		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setCharEncoding(Configuration.UTF8);
		tidy.parse(new ByteArrayInputStream(htmlContent.getBytes()), xhtmlOs);

		xhtmlOs.flush();
		xhtmlOs.close();

		// log.debug(xhtmlOs.toString());

		StringReader contentReader = new StringReader(xhtmlOs.toString());

		InputSource source = new InputSource(contentReader);

		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();

		documentBuilder.setEntityResolver(new NoOpEntityResolver());
		Document xhtmlContent = documentBuilder.parse(source);

		try {
			ITextRenderer renderer = new ITextRenderer();
			ITextFontResolver resolver = renderer.getFontResolver();
			addFonts(resolver);
			renderer.setDocument(xhtmlContent, null);
			renderer.layout();

			renderer.createPDF(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		outputStream.close();
	}

	private void addFonts(ITextFontResolver resolver) {
		try {
			String fonts[] = { "Godhuli_03-09-2005.ttf",
					"Lohit_14-04-2007.ttf", "Puja-17-06-2006.ttf",
					"Punarbhaba_27-02-2006.ttf", "Rupali_01-02-2007.ttf",
					"Saraswatii_03-09-2005.ttf", "Sharifa_03-09-2005.ttf",
					"shibani.ttf", "Siyamrupali_1_01.ttf",
					"SolaimanLipi_20-04-07.ttf" };

			for (int i = 0; i < fonts.length; i++) {
				String path = "D:\\Codes\\Java\\Java\\spirng-mvc\\govForms\\resource\\fonts\\"
						+ fonts[i];
				// /String path =
				// this.getClass().getResource(fonts[i]).toString();

				resolver.addFont((path), BaseFont.IDENTITY_H,
						BaseFont.NOT_EMBEDDED);
				// System.out.println(getFontFilePath(path));
			}
		} catch (DocumentException | IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	private String getFontFilePath(String classpathRelativePath) {
		Resource rsrc = new ClassPathResource(classpathRelativePath);
		try {
			return rsrc.getFile().getAbsolutePath();

		} catch (IOException e) {
			System.err.println(e.toString());
			e.printStackTrace();
		}
		return classpathRelativePath;
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

		String footer = "iit lone gunmen 2.0";

		data = HtmlUtils.replaceAll(data, "(<body.*?>)", "$1" + footer);
		return data;
	}
}