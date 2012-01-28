package bd.gov.forms.dao;

import java.util.List;

import bd.gov.forms.domain.Ministry;

/**
 * @author Bazlur Rahman Rokon
 * @date 28 jan 2012
 * */
public interface MinistryDao {
	public void save(final Ministry ministry);

	public void update(Ministry ministry);

	public void delete(int id);

	public List<Ministry> getAll();

	public Ministry find(int id);

}
