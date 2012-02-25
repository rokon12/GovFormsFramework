/*
 * Copyright (C) 2011 Therap (BD) Ltd.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bd.gov.forms.dao;

import bd.gov.forms.domain.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.cache.OptimisticTreeCache.CircumventChecksDataVersion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author asif
 * @version $Revision: 1.0 $
 */
@Repository("userDao")
@Transactional
@SuppressWarnings("unchecked")
public class UserDaoImpl implements UserDao {

	private static final Logger log = LoggerFactory
			.getLogger(UserDaoImpl.class);

	@Autowired
	DefaultLobHandler lobHandler;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * Method getUserRowMapper.
	 * 
	 * @return RowMapper
	 */
	private RowMapper getUserRowMapper() {
		return new RowMapper<Object>() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();

				user.setId(rs.getInt("id"));
				user.setSysId(rs.getString("sys_id"));
				user.setName(rs.getString("name"));
				user.setTitle(rs.getString("title"));
				user.setUserName(rs.getString("user"));

				user.setMobile(rs.getString("mobile"));
				user.setEmail(rs.getString("email"));
				user.setAdmin(rs.getInt("admin"));
				user.setActive(rs.getInt("active"));
				user.setMinistry(rs.getInt("ministry"));

				return user;
			}
		};
	}

	/**
	 * Method getUser.
	 * 
	 * @param sysId
	 *            String
	 * @return User
	 * @see bd.gov.forms.dao.UserDao#getUser(String)
	 */
	public User getUser(String sysId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				User.class);
		criteria.add(Restrictions.eq("sysId", sysId));

		List<User> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list.get(0);
		}

		// return (User) jdbcTemplate.queryForObject(
		// "SELECT * FROM user WHERE sys_id = ?", new Object[] { sysId },
		// getUserRowMapper());

		return null;
		// new RowMapper() {
		//
		// public Object mapRow(ResultSet rs, int rowNum)
		// throws SQLException {
		// User user = new User();
		//
		// user.setId(rs.getInt("id"));
		// user.setSysId(rs.getString("sys_id"));
		// user.setName(rs.getString("name"));
		// user.setTitle(rs.getString("title"));
		// user.setUserName(rs.getString("user"));
		// user.setPassword(rs.getString("password"));
		//
		// user.setMobile(rs.getString("mobile"));
		// user.setEmail(rs.getString("email"));
		// user.setAdmin(rs.getInt("admin"));
		// user.setActive(rs.getInt("active"));
		//
		// return user;
		// }
		// });
	}

	/**
	 * Method getUser.
	 * 
	 * @param userName
	 *            String
	 * @param password
	 *            String
	 * @return User
	 * @see bd.gov.forms.dao.UserDao#getUser(String, String)
	 */
	public User getUser(String userName, String password) {
		// Criteria criteria =
		// sessionFactory.getCurrentSession().createCriteria(
		// User.class);
		// criteria.add(Restrictions.eq("userName", userName));
		// criteria.add(Restrictions.eq("password", password));
		//
		// List<User> list = criteria.list();
		//
		// if (list != null && list.size() > 0) {
		// return list.get(0);
		// }
		// return null;

		try {
			return (User) jdbcTemplate.queryForObject(
					"SELECT * FROM user WHERE user_name = ? and password=?",
					new Object[] { userName, password }, getUserRowMapper());

			// new RowMapper() {
			//
			// public Object mapRow(ResultSet rs, int rowNum)
			// throws SQLException {
			// User user = new User();
			//
			// user.setId(rs.getInt("id"));
			// user.setSysId(rs.getString("sys_id"));
			// user.setName(rs.getString("name"));
			// user.setTitle(rs.getString("title"));
			// user.setUserName(rs.getString("user"));
			//
			// user.setMobile(rs.getString("mobile"));
			// user.setEmail(rs.getString("email"));
			// user.setAdmin(rs.getInt("admin"));
			// user.setActive(rs.getInt("active"));
			// user.setMinistry(rs.getInt("ministry"));
			//
			// return user;
			// }
			// });
		} catch (Exception ex) {
			log.debug("Exception in getUser().", ex);
			return null;
		}
	}

	/**
	 * Method getUserWithEmail.
	 * 
	 * @param userName
	 *            String
	 * @param email
	 *            String
	 * @return User
	 * @see bd.gov.forms.dao.UserDao#getUserWithEmail(String, String)
	 */
	public User getUserWithEmail(String userName, String email) {
		try {
			return (User) jdbcTemplate.queryForObject(
					"SELECT * FROM user WHERE user = ? and email=?",
					new Object[] { userName, email }, getUserRowMapper());
			// new RowMapper() {
			//
			// public Object mapRow(ResultSet rs, int rowNum)
			// throws SQLException {
			// User user = new User();
			//
			// user.setId(rs.getInt("id"));
			// user.setSysId(rs.getString("sys_id"));
			// user.setName(rs.getString("name"));
			// user.setTitle(rs.getString("title"));
			// user.setUserName(rs.getString("user"));
			//
			// user.setMobile(rs.getString("mobile"));
			// user.setEmail(rs.getString("email"));
			// user.setAdmin(rs.getInt("admin"));
			// user.setActive(rs.getInt("active"));
			//
			// return user;
			// }
			// });
		} catch (Exception ex) {
			log.debug("Exception in getUserWIthEmail().", ex);
			return null;
		}
	}

	/**
	 * Method getUserList.
	 * 
	 * @return List
	 * @see bd.gov.forms.dao.UserDao#getUserList()
	 */
	public List getUserList() {
		return jdbcTemplate.query("SELECT * FROM user ", new Object[] {},
				getUserRowMapper());
		// new RowMapper() {
		//
		// public Object mapRow(ResultSet rs, int rowNum)
		// throws SQLException {
		// User user = new User();
		//
		// user.setSysId(rs.getString("sys_id"));
		// user.setName(rs.getString("name"));
		// user.setTitle(rs.getString("title"));
		// user.setUserName(rs.getString("user"));
		// user.setAdmin(rs.getInt("admin"));
		// user.setActive(rs.getInt("active"));
		//
		// return user;
		// }
		// });
	}

	/**
	 * Method saveUser.
	 * 
	 * @param user
	 *            User
	 * @see bd.gov.forms.dao.UserDao#saveUser(User)
	 */
	public void saveUser(final User user) {
		String sql = "INSERT INTO user";
		sql += " (sys_id, name, title, user, password, mobile, email, admin, active, ministry";
		sql += ")";
		sql += " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?";
		sql += ")";

		log.debug("SQL INSERT query: {}", sql);

		jdbcTemplate.execute(sql,
				new AbstractLobCreatingPreparedStatementCallback(lobHandler) {

					@Override
					protected void setValues(PreparedStatement ps,
							LobCreator lobCreator) throws SQLException {
						int i = 1;

						ps.setString(i++, user.getSysId());
						ps.setString(i++, user.getName());
						ps.setString(i++, user.getTitle());
						ps.setString(i++, user.getUserName());
						ps.setString(i++, user.getPassword());
						ps.setString(i++, user.getMobile());
						ps.setString(i++, user.getEmail());
						ps.setInt(i++, user.getAdmin());
						ps.setInt(i++, user.getActive());
						ps.setInt(i, user.getMinistry());
					}
				});
	}

	/**
	 * Method updateUser.
	 * 
	 * @param user
	 *            User
	 * @see bd.gov.forms.dao.UserDao#updateUser(User)
	 */
	public void updateUser(final User user) {
		String sql = "UPDATE user";
		sql += " set name = ?, title = ?, mobile = ?, email = ?, admin = ?, active = ?";
		sql += " WHERE sys_id = ?";

		log.debug("SQL UPDATE query: {}", sql);

		jdbcTemplate.execute(sql,
				new AbstractLobCreatingPreparedStatementCallback(lobHandler) {

					@Override
					protected void setValues(PreparedStatement ps,
							LobCreator lobCreator) throws SQLException {
						int i = 1;

						ps.setString(i++, user.getName());
						ps.setString(i++, user.getTitle());
						ps.setString(i++, user.getMobile());
						ps.setString(i++, user.getEmail());
						ps.setInt(i++, user.getAdmin());
						ps.setInt(i++, user.getActive());
						ps.setString(i, user.getSysId());
					}
				});
	}

	/**
	 * Method changePassword.
	 * 
	 * @param userName
	 *            String
	 * @param password
	 *            String
	 * @return int
	 * @see bd.gov.forms.dao.UserDao#changePassword(String, String)
	 */
	public int changePassword(String userName, String password) {
		return jdbcTemplate.update(
				"UPDATE user SET password = ? WHERE user = ?", password,
				userName);
	}

	/**
	 * Method getCountWithUserName.
	 * 
	 * @param userName
	 *            String
	 * @return int
	 * @see bd.gov.forms.dao.UserDao#getCountWithUserName(String)
	 */
	public int getCountWithUserName(String userName) {
		return jdbcTemplate.queryForInt(
				"select count(*) from user where user = ?", userName);
	}

	/**
	 * Method getUser.
	 * 
	 * @param userName
	 *            String
	 * @param password
	 *            String
	 * @param ministryId
	 *            int
	 * @return User
	 * @see bd.gov.forms.dao.UserDao#getUser(String, String, int)
	 */
	@Override
	public User getUser(String userName, String password, int ministryId) {

		return (User) jdbcTemplate
				.queryForObject(
						"SELECT * FROM user WHERE user = ? and password=? and 	ministry=?",
						new Object[] { userName, password, ministryId },
						getUserRowMapper());
		// new RowMapper() {
		//
		// public Object mapRow(ResultSet rs, int rowNum)
		// throws SQLException {
		// User user = new User();
		//
		// user.setId(rs.getInt("id"));
		// user.setSysId(rs.getString("sys_id"));
		// user.setName(rs.getString("name"));
		// user.setTitle(rs.getString("title"));
		// user.setUserName(rs.getString("user"));
		//
		// user.setMobile(rs.getString("mobile"));
		// user.setEmail(rs.getString("email"));
		// user.setAdmin(rs.getInt("admin"));
		// user.setActive(rs.getInt("active"));
		// user.setActive(rs.getInt("ministryId"));
		//
		// return user;
		// }
		// });

	}
}
