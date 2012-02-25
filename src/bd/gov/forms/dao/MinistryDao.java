package bd.gov.forms.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import bd.gov.forms.domain.Ministry;

/**
 * @author Bazlur Rahman Rokon

 * * @version $Revision: 1.0 $
 */
@Component
public interface MinistryDao {
	/**
	 * Method save.
	 * @param ministry Ministry
	 */
	public void save(final Ministry ministry);

	/**
	 * Method update.
	 * @param ministry Ministry
	 */
	public void update(Ministry ministry);

	/**
	 * Method delete.
	 * @param id int
	 */
	public void delete(int id);

	/**
	 * Method getAll.
	 * @return List<Ministry>
	 */
	public List<Ministry> getAll();

	/**
	 * Method find.
	 * @param id int
	 * @return Ministry
	 */
	public Ministry find(int id);

}
