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
package bd.gov.forms.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Bazlur Rahman
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@Column(name = "sys_id")
	private String sysId;
	@Column(name = "user_name")
	private String userName;
	@Column(name = "password")
	private String password;

	@Column(name = "name")
	private String name;
	@Column(name = "email")
	private String email;
	@Column(name = "admin")
	private int admin;
	@Column(name = "mobile")
	private String mobile;
	@Column(name = "active")
	private int active;
	@Column(name = "title")
	private String title;
	@Transient
	private String confirmPassword;

	private int ministry;
	@Transient
	private String oldPassword;

	/**
	 * Method getActive.
	 * 
	 * @return int
	 */
	public int getActive() {
		return active;
	}

	/**
	 * Method setActive.
	 * 
	 * @param active
	 *            int
	 */
	public void setActive(int active) {
		this.active = active;
	}

	/**
	 * Method getAdmin.
	 * 
	 * @return int
	 */
	public int getAdmin() {
		return admin;
	}

	/**
	 * Method setAdmin.
	 * 
	 * @param admin
	 *            int
	 */
	public void setAdmin(int admin) {
		this.admin = admin;
	}

	/**
	 * Method getEmail.
	 * 
	 * @return String
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Method setEmail.
	 * 
	 * @param email
	 *            String
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Method getId.
	 * 
	 * @return int
	 */
	public int getId() {
		return id;
	}

	/**
	 * Method setId.
	 * 
	 * @param id
	 *            int
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Method getMobile.
	 * 
	 * @return String
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * Method setMobile.
	 * 
	 * @param mobile
	 *            String
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * Method getName.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method setName.
	 * 
	 * @param name
	 *            String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Method getPassword.
	 * 
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Method setPassword.
	 * 
	 * @param password
	 *            String
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Method getSysId.
	 * 
	 * @return String
	 */
	public String getSysId() {
		return sysId;
	}

	/**
	 * Method setSysId.
	 * 
	 * @param sysId
	 *            String
	 */
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	/**
	 * Method getUserName.
	 * 
	 * @return String
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Method setUserName.
	 * 
	 * @param userName
	 *            String
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Method getConfirmPassword.
	 * 
	 * @return String
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * Method setConfirmPassword.
	 * 
	 * @param confirmPassword
	 *            String
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	/**
	 * Method getOldPassword.
	 * 
	 * @return String
	 */
	public String getOldPassword() {
		return oldPassword;
	}

	/**
	 * Method setOldPassword.
	 * 
	 * @param oldPassword
	 *            String
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * Method getTitle.
	 * 
	 * @return String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Method setTitle.
	 * 
	 * @param title
	 *            String
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Method getMinistry.
	 * 
	 * @return int
	 */
	public int getMinistry() {
		return ministry;
	}

	/**
	 * Method setMinistry.
	 * 
	 * @param ministry
	 *            int
	 */
	public void setMinistry(int ministry) {
		this.ministry = ministry;
	}

}
