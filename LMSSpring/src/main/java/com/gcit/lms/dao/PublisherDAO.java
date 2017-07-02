package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.lms.entity.Publisher;
import com.mysql.jdbc.Statement;

public class PublisherDAO extends BaseDAO implements ResultSetExtractor<List<Publisher>>{

	public void addPublisher(Publisher publisher) throws SQLException{
		template.update("insert into tbl_publisher(publisherName) values (?)", new Object[] {publisher.getPublisherName()});
	}
	
	public void addPublisherBook(Publisher publisher, Integer bookId) throws SQLException{
		template.update("update tbl_book set pubId=? where bookId=?", new Object[] {publisher.getPublisherId(),bookId});
	}
	
	public Integer addPublisherWithID(Publisher publisher) throws SQLException{
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "insert into tbl_publisher(publisherName) values (?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, publisher.getPublisherName());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
		}
	
	public void updatePublisherName(Publisher publisher) throws SQLException{
		template.update("update tbl_publisher set publisherName =? where publisherId = ?", new Object[] {publisher.getPublisherName(), publisher.getPublisherId()});
	}
	
	public void updatePublisherName(Integer publisherId, String publisherName) throws SQLException{
		template.update("update tbl_publisher set publisherName =? where publisherId = ?", new Object[] {publisherName, publisherId});
	}
	
	public void updatePublisherPhone(Publisher publisher) throws SQLException{
		template.update("update tbl_publisher set publisherPhone =? where publisherId = ?", new Object[] {publisher.getPublisherPhone(), publisher.getPublisherId()});
	}
	
	public void updatePublisherPhone(Integer publisherId, String publisherPhone) throws SQLException{
		template.update("update tbl_publisher set publisherPhone =? where publisherId = ?", new Object[] {publisherPhone,publisherId});
	}
	
	public void updatePublisherAddress(Publisher publisher) throws SQLException{
		template.update("update tbl_publisher set publisherAddress =? where publisherId = ?", new Object[] {publisher.getPublisherAddress(), publisher.getPublisherId()});
	}
	
	public void updatePublisherAddress(Integer publisherId, String publisherAddress) throws SQLException{
		template.update("update tbl_publisher set publisherAddress =? where publisherId = ?", new Object[] {publisherAddress,publisherId});
	}
	
	public void deletePublisher(Publisher publisher) throws SQLException{
		template.update("delete from tbl_publisher where publisherId = ?", new Object[] {publisher.getPublisherId()});
	}
	
	public List<Publisher> readAllPublishers() throws SQLException{
		return template.query("select * from tbl_publisher", this);
	}
	
	public Integer getPublishersCount() throws SQLException{
		return template.queryForObject("select count(*) as COUNT from tbl_publisher",Integer.class);
	}
	
	public Integer getPublishersCount(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		return template.queryForObject("select count(*) as COUNT from tbl_publisher where publisherName like ?",new Object[]{searchString},Integer.class);		
	}
	
	public List<Publisher> readAllPublishers(Integer pageNo) throws SQLException{
		setPageNo(pageNo);
		if (pageNo>0){
			int index = (getPageNo() - 1) * 10;
			return (List<Publisher>) template.query("select * from tbl_publisher"+" LIMIT " + index + " , " + getPageSize(), this);
		}else{
		return (List<Publisher>) template.query("select * from tbl_publisher", this);
		}
	}
	
	public List<Publisher> readAllPublishersByName(Integer pageNo, String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		setPageNo(pageNo);
		if (pageNo>0){
			int index = (getPageNo() - 1) * 10;
			return (List<Publisher>) template.query("select * from tbl_publisher where publisherName like ?" +  " LIMIT " + index + " , " + getPageSize(), new Object[]{searchString}, this);
		}else{
			return (List<Publisher>) template.query("select * from tbl_publisher where publisherName like ?", new Object[]{searchString}, this);
		}
		
	}
	
	public List<Publisher> readAllPublishersByPublisherName(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		return (List<Publisher>) template.query("select * from tbl_publisher where publisherName like ?", new Object[]{searchString}, this);
	}
	
	public Publisher readPublisherByPK(Integer publisherId) throws SQLException{
		List<Publisher> publishers = template.query("select * from tbl_publisher where publisherId = ?", new Object[]{publisherId} , this);
		if(publishers!=null && publishers.size() > 0){
			return publishers.get(0);
		}
		return null;
	}
	
	public Publisher readPublisherByBookId(Integer bookId) throws SQLException {
		List<Publisher> publishers = template.query("select * from tbl_publisher where publisherId IN (Select pubId from tbl_book where bookId = ?)", new Object[]{bookId} , this);
		if(publishers!=null && publishers.size() > 0){
			return publishers.get(0);
		}
		return null;
	}
	

	@Override
	public List<Publisher> extractData(ResultSet rs) throws SQLException {
		List<Publisher> publishers = new ArrayList<>();
		while(rs.next()){
			Publisher p = new Publisher();
			p.setPublisherId(rs.getInt("publisherId"));
			p.setPublisherName(rs.getString("publisherName"));
			p.setPublisherAddress(rs.getString("publisherAddress"));
			p.setPublisherPhone(rs.getString("publisherPhone"));
			publishers.add(p);
		}
		return publishers;
	}
}
