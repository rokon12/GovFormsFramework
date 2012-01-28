package bd.gov.forms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import bd.gov.forms.domain.Ministry;

/**
 * @author Bazlur Rahman Rokon
 * @date 28 jan 2012
 * */

@Repository("ministryDao")
public class MinistryDaoImpl implements MinistryDao {

	private static final Logger log = LoggerFactory
			.getLogger(MinistryDaoImpl.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	String query = null;

	@Override
	public void save(final Ministry ministry) {
		query = "INSERT INTO ministry( ministry_name, ministry_short_name) VALUES ( ?, ?)";
		log.debug("query", "insert ministry: " + query);
		try {
			synchronized (this) {
				jdbcTemplate.update(new PreparedStatementCreator() {

					@Override
					public PreparedStatement createPreparedStatement(
							Connection conn) throws SQLException {

						PreparedStatement statement = conn
								.prepareStatement(query);
						statement.setString(1, ministry.getMinistryName());
						statement.setString(2, ministry.getMinistryShortName());
						return statement;
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.trace("from MinistryDaoImpl() : " + e.toString());
		}
	}

	@Override
	public void update(Ministry ministry) {

	}

	@Override
	public void delete(int id) {
		query = "DELETE from ministry WHERE id = ?";

		log.debug("delete query: " + query);
		try {
			jdbcTemplate.update(query, new Object[] { id });
		} catch (Exception ex) {
			ex.printStackTrace();
			log.trace(ex.toString());
		}
	}

	@Override
	public List<Ministry> getAll() {
		query = "SELECT * from ministry";
		try {
			log.debug("get all ministry query: " + query);

			List<Ministry> list = jdbcTemplate.query(query,
					new BeanPropertyRowMapper<Ministry>(Ministry.class));

			return list;

		} catch (Exception e) {
			e.printStackTrace();
			log.trace(e.toString());
		}
		return null;
	}

	@Override
	public Ministry find(int id) {
		query = "SELECT * FROM ministry WHERE id = ?";
		log.debug("get ministry by id query: " + query);

		try {
			Ministry ministry = (Ministry) jdbcTemplate.queryForObject(query,
					new Object[] { id }, new RowMapper<Object>() {
						Ministry ministry;

						@Override
						public Object mapRow(ResultSet rs, int i)
								throws SQLException {
							ministry = new Ministry();
							ministry.setId(rs.getInt("id"));
							ministry.setMinistryName(rs
									.getString("ministry_name"));
							ministry.setMinistryShortName(rs
									.getString("ministry_short_name"));
							return ministry;
						}
					});
			return ministry;
		} catch (Exception e) {
			e.printStackTrace();
			log.trace(e.toString());
		}

		return null;
	}

}
