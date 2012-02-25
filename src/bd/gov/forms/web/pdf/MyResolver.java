package bd.gov.forms.web.pdf;

import org.apache.log4j.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;

/**
 */
public class MyResolver implements EntityResolver {
    private static final Logger log = Logger.getLogger(MyResolver.class);
    private HttpServletRequest request;

    /**
     * Constructor for MyResolver.
     * @param request HttpServletRequest
     */
    public MyResolver(HttpServletRequest request) {
        this.request = request;
    }

    // This method is called whenever an external entity is accessed
    // for the first time.
    /**
     * Method resolveEntity.
     * @param publicId String
     * @param systemId String
     * @return InputSource
     * @see org.xml.sax.EntityResolver#resolveEntity(String, String)
     */
    public InputSource resolveEntity(String publicId, String systemId) {
        try {
            log.debug("publicId: " + publicId + " systemId: " + systemId);
            String dtd = "";
            if (systemId.contains("strict.dtd")) {
                dtd = (request).getSession().getServletContext().getRealPath("/xhtml_validation_dtd_ent/DTD/xhtml1-strict.dtd");
            } else if (systemId.contains("transitional.dtd")) {
                dtd = (request).getSession().getServletContext().getRealPath("/xhtml_validation_dtd_ent/DTD/xhtml1-transitional.dtd");
            } else if (systemId.contains("xhtml-lat1.ent")) {
                dtd = (request).getSession().getServletContext().getRealPath("/xhtml_validation_dtd_ent/ent/xhtml-lat1.ent");
            } else if (systemId.contains("xhtml-symbol.ent")) {
                dtd = (request).getSession().getServletContext().getRealPath("/xhtml_validation_dtd_ent/ent/xhtml-symbol.ent");
            } else if (systemId.contains("xhtml-special.ent")) {
                dtd = (request).getSession().getServletContext().getRealPath("/xhtml_validation_dtd_ent/ent/xhtml-special.ent");
            }
            return new InputSource(new FileInputStream(dtd));
        } catch (IOException e) {
            log.debug(e);
        }
        // Returning null causes the caller to try accessing the systemid
        return null;
    }
}