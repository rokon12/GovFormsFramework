package bd.gov.forms.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import bd.gov.forms.domain.Document;

/***
 * @author A. N. M. Bazlur Rahman
 * @version $Revision: 1.0 $
 */
@Component
public interface DocumentDao {
	/**
	 * Method find.
	 * @param id int
	 * @return Document
	 */
	public Document find(int id);

	/**
	 * Method listAll.
	 * @return List<Document>
	 */
	public List<Document> listAll();

	/**
	 * Method save.
	 * @param document Document
	 */
	public void save(final Document document);

	/**
	 * Method delete.
	 * @param id int
	 */
	public void delete(int id);
}
