package bd.gov.forms.dao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;

import bd.gov.forms.domain.Document;
import bd.gov.forms.utils.DateUtil;

/**
 * @author A. N. M. Bazlur Rahman
 * */

@Repository("documentDao")
public class DocumentDaoImpl implements DocumentDao {
	private static final Logger log = LoggerFactory
			.getLogger(DocumentDaoImpl.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	String query = null;

	@Override
	public Document find(int id) {

		query = "select * from documents where id = ?";

		log.debug("get documents by id query: " + query);

		try {
			Document document = (Document) jdbcTemplate.queryForObject(query,
					new Object[] { id }, new RowMapper<Object>() {
						Document doc;

						@Override
						public Object mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							doc = new Document();
							doc.setId(rs.getInt("id"));
							doc.setName(rs.getString("name"));
							doc.setDescription(rs.getString("description"));
							doc.setFilename(rs.getString("filename"));
							doc.setContent(rs.getBytes("content"));
							doc.setContentType(rs.getString("content_type"));
							doc.setCreated(rs.getTimestamp("created"));
							return doc;
						}
					});
			return document;

		} catch (Exception e) {
			e.printStackTrace();
			log.trace(e.toString());
		}
		return null;
	}

	@Override
	public List<Document> listAll() {
		query = "select * from documents";
		try {
			log.debug("get all documetnts query: " + query);

			List<Document> list = jdbcTemplate.query(query,
					new BeanPropertyRowMapper<Document>(Document.class));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			log.trace(e.toString());
		}
		return null;
	}

	@Override
	public void save(final Document document) {
		query = "insert into documents (name, description, filename, content, content_type, created) values (?, ?, ?, ?, ?, ?)";

		log.debug("query", "insert query: " + query);

		try {
			synchronized (this) {
				jdbcTemplate.update(new PreparedStatementCreator() {

					@Override
					public PreparedStatement createPreparedStatement(
							Connection conn) throws SQLException {

						PreparedStatement statement = conn
								.prepareStatement(query);

						statement.setString(1, document.getName());
						statement.setString(2, document.getDescription());
						statement.setString(3, document.getFilename());
						// statement.setBlob(4, document.getContent());
						InputStream stream = new ByteArrayInputStream(document
								.getContent());
						statement.setBinaryStream(4, stream);
						statement.setString(5, document.getContentType());
						statement.setTimestamp(6,
								DateUtil.convertDateToTimestamp(new Date()));

						return statement;
					}
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.trace(e.toString());
		}
	}

	@Override
	public void delete(int id) {
		query = "delete from documents WHERE id = ?";

		log.debug("delete query: " + query);
		try {
			jdbcTemplate.update(query, new Object[] { id });
		} catch (Exception ex) {
			ex.printStackTrace();
			log.trace(ex.toString());
		}
	}
}
