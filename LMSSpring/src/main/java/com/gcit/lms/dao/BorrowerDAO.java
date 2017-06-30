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

import com.gcit.lms.entity.Borrower;
import com.mysql.jdbc.Statement;

public class BorrowerDAO extends BaseDAO implements ResultSetExtractor<List<Borrower>>{

	public void addBorrower(Borrower borrower) throws SQLException{
		template.update("insert into tbl_borrower(name) values (?)", new Object[] {borrower.getName()});
	}
	
	public Integer addBorrowerWithID(Borrower borrower) throws SQLException{
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "insert into tbl_borrower(name) values (?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, borrower.getName());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
		}
	
	public void updateBorrowerName(Borrower borrower) throws SQLException{
		template.update("update tbl_borrower set name =? where cardNo = ?", new Object[] {borrower.getName(), borrower.getCardNo()});
	}
	
	public void updateBorrowerAddress(Borrower borrower) throws SQLException{
		template.update("update tbl_borrower set address =? where cardNo = ?", new Object[] {borrower.getAddress(), borrower.getCardNo()});
	}
	
	public void updateBorrowerAddress(Integer cardNo, String address) throws SQLException{
		template.update("update tbl_borrower set address =? where cardNo = ?", new Object[] {address,cardNo});
	}
	
	public void updateBorrowerPhone(Borrower borrower) throws SQLException{
		template.update("update tbl_borrower set phone =? where cardNo = ?", new Object[] {borrower.getPhone(), borrower.getCardNo()});
	}
	
	public void updateBorrowerPhone(Integer cardNo, String phone) throws SQLException{
		template.update("update tbl_borrower set phone =? where cardNo = ?", new Object[] {phone,cardNo});
	}
	
	public Integer getBorrowersCount() throws SQLException{
		return template.queryForObject("select count(*) as COUNT from tbl_borrower",Integer.class);
	}
	
	public Integer getBorrowersCountByPk(Integer cardNo) throws SQLException{
		return template.queryForObject("select count(*) as COUNT from tbl_borrower where cardNo = ?",new Object[]{cardNo},Integer.class);
	}
	
	public Integer getBorrowersCount(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		return template.queryForObject("select count(*) as COUNT from tbl_borrower where name like ?",new Object[]{searchString},Integer.class);
	}
	
	public List<Borrower> readAllBorrowers(Integer pageNo) throws SQLException{
		setPageNo(pageNo);
		return (List<Borrower>) template.query("select * from tbl_borrower", this);
	}
	
	public List<Borrower> readAllBorrowersByName(Integer pageNo, String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		setPageNo(pageNo);
		return (List<Borrower>) template.query("select * from tbl_borrower where name like ?", new Object[]{searchString}, this);
	}
	
	
	public void deleteBorrower(Borrower borrower) throws SQLException{
		template.update("delete from tbl_borrower where cardNo = ?", new Object[] {borrower.getCardNo()});
	}
	
	public List<Borrower> readAllBorrowers() throws SQLException{
		return (List<Borrower>) template.query("select * from tbl_borrower", this);
	}
	
	public List<Borrower> readAllBorrowersByName(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		return (List<Borrower>) template.query("select * from tbl_borrower where name like ?", new Object[]{searchString}, this);
	}
	
	public Borrower readBorrowerByPK(Integer cardNo) throws SQLException{
		List<Borrower> borrowers = (List<Borrower>) template.query("select * from tbl_borrower where cardNo = ?", new Object[]{cardNo}, this);
		if(borrowers!=null){
			return borrowers.get(0);
		}
		return null;
	}

	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException {
		List<Borrower> borrowers = new ArrayList<>();
		while(rs.next()){
			Borrower b = new Borrower();
			b.setCardNo(rs.getInt("cardNo"));
			b.setName(rs.getString("name"));
			b.setAddress(rs.getString("address"));
			b.setPhone(rs.getString("phone"));
			borrowers.add(b);
		}
		return borrowers;
	}
}
