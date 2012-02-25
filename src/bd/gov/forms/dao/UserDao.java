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

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * @author asif
 * @version $Revision: 1.0 $
 */
@Component
public interface UserDao {

	/**
	 * Method saveUser.
	 * 
	 * @param user
	 *            User
	 */
	public void saveUser(final User user);

	/**
	 * Method getUser.
	 * 
	 * @param sysId
	 *            String
	 * @return User
	 */
	public User getUser(String sysId);

	/**
	 * Method updateUser.
	 * 
	 * @param user
	 *            User
	 */
	public void updateUser(final User user);

	/**
	 * Method getUserList.
	 * 
	 * @return List
	 */
	public List getUserList();

	/**
	 * Method getUser.
	 * 
	 * @param userName
	 *            String
	 * @param password
	 *            String
	 * @return User
	 */
	public User getUser(String userName, String password);

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
	 */
	public User getUser(String userName, String password, int ministryId);

	/**
	 * Method getUserWithEmail.
	 * 
	 * @param userName
	 *            String
	 * @param email
	 *            String
	 * @return User
	 */
	public User getUserWithEmail(String userName, String email);

	/**
	 * Method changePassword.
	 * 
	 * @param userName
	 *            String
	 * @param password
	 *            String
	 * @return int
	 */
	public int changePassword(String userName, String password);

	/**
	 * Method getCountWithUserName.
	 * 
	 * @param userName
	 *            String
	 * @return int
	 */
	public int getCountWithUserName(String userName);

}
