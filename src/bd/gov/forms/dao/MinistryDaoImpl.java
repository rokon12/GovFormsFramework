package bd.gov.forms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import bd.gov.forms.domain.Ministry;

/**
 * @author Bazlur Rahman Rokon
 * 
 *         * @version $Revision: 1.0 $
 */

@Repository("ministryDao")
@Transactional 
public class MinistryDaoImpl implements MinistryDao {

	private static final Logger log = LoggerFactory
			.getLogger(MinistryDaoImpl.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	String query = null;

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * Method save.
	 * 
	 * @param ministry
	 *            Ministry
	 * @see bd.gov.forms.dao.MinistryDao#save(Ministry)
	 */
	@Override
	public void save(final Ministry ministry) {
		sessionFactory.getCurrentSession().save(ministry);
		// query =
		// "INSERT INTO ministry( ministry_name, ministry_short_name) VALUES ( ?, ?)";
		// log.debug("query", "insert ministry: " + query);
		// try {
		// synchronized (this) {
		// jdbcTemplate.update(new PreparedStatementCreator() {
		//
		// @Override
		// public PreparedStatement createPreparedStatement(
		// Connection conn) throws SQLException {
		//
		// PreparedStatement statement = conn
		// .prepareStatement(query);
		// statement.setString(1, ministry.getMinistryName());
		// statement.setString(2, ministry.getMinistryShortName());
		// return statement;
		// }
		// });
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// log.trace("from MinistryDaoImpl() : " + e.toString());
		// }
	}

	/**
	 * Method update.
	 * 
	 * @param ministry
	 *            Ministry
	 * @see bd.gov.forms.dao.MinistryDao#update(Ministry)
	 */
	@Override
	public void update(Ministry ministry) {
		sessionFactory.getCurrentSession().update(ministry);
	}

	/**
	 * Method delete.
	 * 
	 * @param id
	 *            int
	 * @see bd.gov.forms.dao.MinistryDao#delete(int)
	 */
	@Override
	public void delete(int id) {
		sessionFactory.getCurrentSession().delete(find(id));

		// query = "DELETE from ministry WHERE id = ?";
		//
		// log.debug("delete query: " + query);
		// try {
		// jdbcTemplate.update(query, new Object[] { id });
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// log.trace(ex.toString());
		// }
	}

	/**
	 * Method getAll.
	 * 
	 * @return List<Ministry>
	 * @see bd.gov.forms.dao.MinistryDao#getAll()
	 */
	@Override
	public List<Ministry> getAll() {
		return sessionFactory.getCurrentSession()
				.createCriteria(Ministry.class).list();

		// query = "SELECT * from ministry";
		// try {
		// log.debug("get all ministry query: " + query);
		//
		// List<Ministry> list = jdbcTemplate.query(query,
		// new BeanPropertyRowMapper<Ministry>(Ministry.class));
		//
		// return list;
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// log.trace(e.toString());
		// }
		// return null;
	}

	/**
	 * Method find.
	 * 
	 * @param id
	 *            int
	 * @return Ministry
	 * @see bd.gov.forms.dao.MinistryDao#find(int)
	 */
	@Override
	public Ministry find(int id) {
		return (Ministry) sessionFactory.getCurrentSession().get(
				Ministry.class, id);
		// query = "SELECT * FROM ministry WHERE id = ?";
		// log.debug("get ministry by id query: " + query);
		//
		// try {
		// Ministry ministry = (Ministry) jdbcTemplate.queryForObject(query,
		// new Object[] { id }, new RowMapper<Object>() {
		// Ministry ministry;
		//
		// @Override
		// public Object mapRow(ResultSet rs, int i)
		// throws SQLException {
		// ministry = new Ministry();
		// ministry.setId(rs.getInt("id"));
		// ministry.setMinistryName(rs
		// .getString("ministry_name"));
		// ministry.setMinistryShortName(rs
		// .getString("ministry_short_name"));
		// return ministry;
		// }
		// });
		// return ministry;
		// } catch (Exception e) {
		// e.printStackTrace();
		// log.trace(e.toString());
		// }

		// return null;
	}

}
