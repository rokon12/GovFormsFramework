package bd.gov.forms.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import bd.gov.forms.domain.Document;

/***
 * @author A. N. M. Bazlur Rahman
 */
@Component
public interface DocumentDao {
	public Document find(int id);

	public List<Document> listAll();

	public void save(final Document document);

	public void delete(int id);
}
