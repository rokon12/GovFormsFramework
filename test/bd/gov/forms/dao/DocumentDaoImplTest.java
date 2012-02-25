package bd.gov.forms.dao;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import bd.gov.forms.domain.Document;

/**
 */
public class DocumentDaoImplTest {

	private DocumentDao mockDao;

	/**
	 * Method setUp.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		mockDao = createStrictMock(DocumentDao.class);
	}

	@Test
	public void testFileUPload() {
		Document document = new Document();
		List<Document> documents = new ArrayList<>();

		expect(mockDao.listAll()).andReturn(documents);

		replay(mockDao);
		verify(mockDao);
	}
}