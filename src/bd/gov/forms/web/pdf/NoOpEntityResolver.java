package bd.gov.forms.web.pdf;

import java.io.IOException;
import java.io.StringBufferInputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 */
public class NoOpEntityResolver implements EntityResolver {

	/**
	 * Method resolveEntity.
	 * @param publicId String
	 * @param systemId String
	 * @return InputSource
	 * @throws SAXException
	 * @throws IOException
	 * @see org.xml.sax.EntityResolver#resolveEntity(String, String)
	 */
	@Override
	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {
		return new InputSource(new StringBufferInputStream(""));
	}
}
