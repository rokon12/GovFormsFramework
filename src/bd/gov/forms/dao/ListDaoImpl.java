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

import bd.gov.forms.domain.ListData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sound.sampled.DataLine;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
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
@Transactional 
@Repository("listDao")
public class ListDaoImpl implements ListDao {

	private static final Logger log = LoggerFactory
			.getLogger(ListDaoImpl.class);

	@Autowired
	private DefaultLobHandler lobHandler;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * Method getListDataSysId.
	 * 
	 * @param id
	 *            int
	 * @return String
	 * @see bd.gov.forms.dao.ListDao#getListDataSysId(int)
	 */
	public String getListDataSysId(int id) {
		return (String) jdbcTemplate.queryForObject(
				"SELECT sys_id FROM list_data WHERE id = ?",
				new Object[] { id }, new RowMapper() {

					public Object mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString("sys_id");
					}
				});
	}

	/**
	 * Method getListData.
	 * 
	 * @param sysId
	 *            String
	 * @return ListData
	 * @see bd.gov.forms.dao.ListDao#getListData(String)
	 */
	public ListData getListData(String sysId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ListData.class);

		criteria.add(Restrictions.eq("sysId", sysId));

		List<ListData> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}

		return null;

		// return (ListData) jdbcTemplate.queryForObject(
		// "SELECT * FROM list_data WHERE sys_id = ?",
		// new Object[]{sysId},
		// new RowMapper() {
		//
		// public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		// ListData list = new ListData();
		//
		// list.setId(rs.getInt("id"));
		// list.setSysId(rs.getString("sys_id"));
		// list.setName(rs.getString("name"));
		// list.setDetail(rs.getString("detail"));
		// list.setValues(rs.getString("list_values"));
		//
		// return list;
		// }
		// });
	}

	/**
	 * Method getListDataList.
	 * 
	 * @return List
	 * @see bd.gov.forms.dao.ListDao#getListDataList()
	 */
	public List getListDataList() {

//		return sessionFactory.getCurrentSession()
//				.createCriteria(ListData.class).list();

		return jdbcTemplate.query("SELECT * FROM list_data ", new Object[] {},
				new RowMapper() {

					public Object mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						ListData list = new ListData();

						list.setId(rs.getInt("id"));
						list.setSysId(rs.getString("sys_id"));
						list.setName(rs.getString("name"));
						list.setDetail(rs.getString("detail"));

						return list;
					}
				});
	}

	/**
	 * Method saveListData.
	 * 
	 * @param listData
	 *            ListData
	 * @see bd.gov.forms.dao.ListDao#saveListData(ListData)
	 */
	public void saveListData(final ListData listData) {
		sessionFactory.getCurrentSession().save(listData);

		// String sql = "INSERT INTO list_data";
		// sql += " (sys_id, name, detail, list_values";
		// sql += ")";
		// sql += " VALUES (?, ?, ?, ?";
		// sql += ")";
		//
		// log.debug("SQL INSERT query: {}", sql);
		//
		// jdbcTemplate.execute(sql,
		// new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
		//
		// @Override
		// protected void setValues(PreparedStatement ps,
		// LobCreator lobCreator) throws SQLException {
		// int i = 1;
		//
		// ps.setString(i++, listData.getSysId());
		// ps.setString(i++, listData.getName());
		// ps.setString(i++, listData.getDetail());
		// ps.setString(i, listData.getValues());
		// }
		// });
	}

	/**
	 * Method updateListData.
	 * 
	 * @param lst
	 *            ListData
	 * @see bd.gov.forms.dao.ListDao#updateListData(ListData)
	 */
	public void updateListData(final ListData lst) {

		sessionFactory.getCurrentSession().update(lst);

		// String sql = "UPDATE list_data";
		// sql += " set name = ?, detail = ?, list_values = ?";
		// sql += " WHERE sys_id = ?";
		//
		// log.debug("SQL UPDATE query: {}", sql);
		//
		// jdbcTemplate.execute(sql,
		// new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
		//
		// @Override
		// protected void setValues(PreparedStatement ps,
		// LobCreator lobCreator) throws SQLException {
		// int i = 1;
		//
		// ps.setString(i++, lst.getName());
		// ps.setString(i++, lst.getDetail());
		// ps.setString(i++, lst.getValues());
		// ps.setString(i, lst.getSysId());
		// }
		// });
	}
}
