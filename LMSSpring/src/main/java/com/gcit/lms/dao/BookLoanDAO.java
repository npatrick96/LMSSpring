package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.mysql.jdbc.Statement;

public class BookLoanDAO extends BaseDAO implements ResultSetExtractor<List<BookLoan>>{
	
	@Autowired
	BookDAO bdao;
	
	@Autowired
	BranchDAO brdao;
	
	@Autowired
	BorrowerDAO bodao;

	public void addBookLoan(BookLoan bookLoan) throws SQLException{
		template.update("insert into tbl_book_loans(bookId, branchId, cardNo, dateOut, dueDate) values (?, ?, ?,?,?)", new Object[] {
				bookLoan.getBook().getBookId(), bookLoan.getBranch().getBranchId(), 
				bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut(), bookLoan.getDueDate()});
	}
	
	public Integer addBookLoanWithID(BookLoan bookLoan) throws SQLException{
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "insert into tbl_book_loans(bookId, branchId, cardNo, dateOut) values (?,?,?,?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, bookLoan.getBook().getBookId());
				ps.setInt(1,  bookLoan.getBranch().getBranchId());
				ps.setInt(1, bookLoan.getBorrower().getCardNo());
				ps.setString(5, bookLoan.getDateOut());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}
	
	public void updateBookLoan(BookLoan bookLoan) throws SQLException{
		template.update("update tbl_book_loans set dueDate = ?, dateIn = ? where  bookId = ? and branchId = ? and cardNo = ? and dateOut = ?",
				new Object[] {bookLoan.getDueDate(), bookLoan.getDateIn(), 
				bookLoan.getBook().getBookId(), bookLoan.getBranch().getBranchId(), 
				bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut()});
	}
	
	public void updateBookLoanDueDate(BookLoan bookLoan) throws SQLException{
		template.update("update tbl_book_loans set dueDate = ? where  bookId = ? and branchId = ? and cardNo = ? and dateOut = ?",
				new Object[] {bookLoan.getDueDate(), 
				bookLoan.getBook().getBookId(), bookLoan.getBranch().getBranchId(), 
				bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut()});	
		}
	
	public void updateBookLoanDateIn(BookLoan bookLoan) throws SQLException{
		template.update("update tbl_book_loans set dateIn = ? where  bookId = ? and branchId = ? and cardNo = ? and dateOut = ?",
				new Object[] {bookLoan.getDateIn(), 
				bookLoan.getBook().getBookId(), bookLoan.getBranch().getBranchId(), 
				bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut()});	
		}
	
	public void returnBook(BookLoan bookLoan) throws SQLException{
		template.update("update tbl_book_loans set dateIn = NOW() where  bookId = ? and branchId = ? and cardNo = ? and dateOut = ?",
				new Object[] {
				bookLoan.getBook().getBookId(), bookLoan.getBranch().getBranchId(), 
				bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut()});	
		}
	
	public void deleteBookLoan(BookLoan bookLoan) throws SQLException{
		template.update("delete from tbl_book_loans where bookId = ? and branchId = ? and cardNo = ? and dateOut = ?", 
				new Object[] {bookLoan.getBook().getBookId(), bookLoan.getBranch().getBranchId(), 
				bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut()});
	}
	
	public Integer getBookLoansCount() throws SQLException{
		return template.queryForObject("select count(*) as COUNT from tbl_book_loans", Integer.class);
	}

	
	public Integer getBookLoansCount(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		return template.queryForObject("select count(*) as COUNT from tbl_book_loans where dateOut like ?",new Object[]{searchString}, Integer.class);
	}
	
	public Integer getBookLoansCount(Integer cardNo) throws SQLException{
		return template.queryForObject("select count(*) as COUNT from tbl_book_loans where cardNo = ?", new Object[]{cardNo}, Integer.class);
	}
	
	public Integer getDueBookLoansCount(Integer cardNo) throws SQLException{
		return template.queryForObject("select count(*) as COUNT from tbl_book_loans where cardNo = ? and dateIn IS NULL", new Object[]{cardNo}, Integer.class);
	}
	
	public BookLoan readBookLoanByDateOut(String dateout) throws SQLException{
		List<BookLoan> bookloans =  template.query("select * from tbl_book_loans where dateOut = ?", new Object[]{dateout}, this);
		if(bookloans!=null){
			return bookloans.get(0);
		}
		return null;
	}
	
	public BookLoan readBookLoanBy4Pks(Integer bookId, Integer branchId, Integer cardNo,String dateout) throws SQLException{
		List<BookLoan> bookloans = (List<BookLoan>) template.query("select * from tbl_book_loans where bookId=? and branchId=? and cardNo=? and dateOut = ?", new Object[]{
				bookId, branchId, cardNo, dateout}, this);
		if(bookloans!=null){
			return bookloans.get(0);
		}
		return null;
	}
	
	public List<BookLoan> readAllBookLoans(Integer pageNo) throws SQLException{
		setPageNo(pageNo);
		return template.query("select * from tbl_book_loans", this);
	}
	
	
	public List<BookLoan> readAllBookLoansByDateOut(Integer pageNo, String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		setPageNo(pageNo);
		return template.query("select * from tbl_book_loans where dateOut like ?", new Object[]{searchString}, this);
	}
	
	public List<BookLoan> readAllBookLoans() throws SQLException{
		return template.query("select * from tbl_book_loans", this);
	}
	
	public List<BookLoan> readAllBookLoansByBorrower(Borrower borrower) throws SQLException{
		return template.query("select * from tbl_book_loans where cardNo = ?", new Object[]{borrower.getCardNo()}, this);
	}
	
	public List<BookLoan> readOutBookLoansByBorrowerCardNo(Integer cardNo) throws SQLException{
		List<BookLoan> bookLoans =  template.query("select * from tbl_book_loans where cardNo = ? and dateIn IS NULL", new Object[]{cardNo}, this);
		return bookLoans;
	}
	
	public List<BookLoan> readAllBookLoansByBranchId(Integer branchId) throws SQLException{
		return template.query("select * from tbl_book_loans where branchId = ?", new Object[]{branchId}, this);
	}
	
	public List<BookLoan> readAllBookLoansByCardNo(Integer cardNo) throws SQLException{
		return template.query("select * from tbl_book_loans where cardNo = ?", new Object[]{cardNo}, this);
	}
	
	public List<BookLoan> readAllBookLoansByBookId(Integer bookId) throws SQLException{
		return template.query("select * from tbl_book_loans where bookId = ?", new Object[]{bookId}, this);
	}
	
	public List<BookLoan> readBookLoansByBorrowerCardNo(Integer pageNo, Integer cardNo) throws SQLException{
		setPageNo(pageNo);
		List<BookLoan> bookLoans =  template.query("select * from tbl_book_loans where cardNo = ? and dateIn IS NULL", new Object[]{cardNo}, this);
		return bookLoans;
	}

	@Override
	public List<BookLoan> extractData(ResultSet rs) throws SQLException {
		List<BookLoan> bookLoans = new ArrayList<>();
		while(rs.next()){
			BookLoan b = new BookLoan();
			// TODO my_implementation: verification and testing needed
			b.setBook(bdao.readBookByPK(rs.getInt("bookId")));
			b.setBranch(brdao.readBranchByPK(rs.getInt("branchId")));
			b.setBorrower(bodao.readBorrowerByPK(rs.getInt("cardNo")));
			b.setDateOut(rs.getString("dateOut"));
			b.setDueDate(rs.getString("dueDate"));
			b.setDateIn(rs.getString("dateIn"));
			// end my_implementation
			bookLoans.add(b);
		}
		return bookLoans;
	}
}
