package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopy;
import com.gcit.lms.entity.Branch;

public class BookCopyDAO extends BaseDAO implements ResultSetExtractor<List<BookCopy>>{
	
	@Autowired
	BookDAO bdao;
	
	@Autowired
	BranchDAO brdao;
	
	public void addBookCopy(BookCopy bookCopy) throws SQLException{
		template.update("insert into tbl_book_copies(bookId, branchId, noOfCopies) values (?,?,?)", new Object[] {
				bookCopy.getBook().getBookId(), bookCopy.getBranch().getBranchId(), 
				bookCopy.getNoOfCopies()});
	}
	
	public void decrementNoOfCopies(Integer bookId, Integer branchId) throws SQLException{
		template.update("update tbl_book_copies set noOfCopies = noOfCopies -1 where bookId = ? and branchId = ? ", new Object[] {bookId, branchId});
	}
	
	public void decrementNoOfCopiesToZero(Integer bookId, Integer branchId) throws SQLException{
		template.update("delete from tbl_book_copies where noOfCopies = 1 and bookId = ? and branchId = ? ", new Object[] {bookId, branchId});
	}
	
	public Integer getBookCopiesCount(BookCopy copy) throws SQLException{
		return template.queryForObject("select count(*) as COUNT from tbl_book_copies where bookId = ? and branchId = ?",new Object[]{
				copy.getBook().getBookId(), copy.getBranch().getBranchId()},Integer.class);
	}
	
	public void updateNoOfCopies(BookCopy bookCopy) throws SQLException{
		template.update("update tbl_book_copies set noOfCopies =  ? where bookId = ? and branchId = ? ", new Object[] {
				bookCopy.getNoOfCopies(),bookCopy.getBook().getBookId(), bookCopy.getBranch().getBranchId()});
	}
	
	public void incrementNoOfCopies(Integer bookId, Integer branchId) throws SQLException{
		template.update("update tbl_book_copies set noOfCopies =  noOfCopies + 1 where bookId = ? and branchId = ? ", new Object[] {bookId, branchId});
	}
	
	
	public BookCopy readBookCopyByPks(Integer branchId, Integer bookId) throws SQLException{
		List<BookCopy> copies = template.query("select * from tbl_book_copies where branchId = ? and bookId =?", new Object[]{branchId, bookId}, this);
		if(copies!=null){
			return copies.get(0);
		}
		return null;
	}
	
	public void updateBookCopy(BookCopy bookCopy) throws SQLException{
		template.update("update tbl_book_copies set noOfCopies = ? where  bookId = ? and branchId = ?",
				new Object[] {bookCopy.getNoOfCopies(),  
				bookCopy.getBook().getBookId(), bookCopy.getBranch().getBranchId()});
	}
	
	public Integer getBookCopyNoOfCopies(Book book, Branch branch) throws SQLException{
		List<BookCopy> bookCopies =   template.query("select noOfCopies from tbl_book_copies where bookId=? and branchId=?", new Object[]{book.getBookId(),branch.getBranchId()}, this);
		if (bookCopies!=null){
			return bookCopies.get(0).getNoOfCopies();
		}
		return null;
	}
	
	public void deleteBookCopy(BookCopy bookCopy) throws SQLException{
		template.update("delete from tbl_book_copies where bookId = ? and branchId = ?", 
				new Object[] {bookCopy.getBook().getBookId(), bookCopy.getBranch().getBranchId()});
	}
	
	public void deleteBookCopy(Integer bookId, Integer branchId) throws SQLException{
		template.update("delete from tbl_book_copies where bookId = ? and branchId = ?", 
				new Object[] {bookId, branchId});
	}
	
	public List<BookCopy> readAllBookCopies() throws SQLException{
		return  template.query("select * from tbl_book_copies", this);
	}
	
	public List<BookCopy> readAllBookCopiesByBook(Book book) throws SQLException{
		return template.query("select * from tbl_book_copies where bookId = ?", new Object[]{book.getBookId()}, this);
	}
	
	public List<BookCopy> readAllBookCopiesByBranch(Branch branch) throws SQLException{
		return  template.query("select * from tbl_book_copies where BranchId = ?", new Object[]{branch.getBranchId()}, this);
	}
	
	public List<BookCopy> readBookCopiesByBorrowerCardNo(Integer cardNo) throws SQLException{
		List<BookCopy> bookCopies =  template.query("select * from tbl_book_copies where cardNo = ?", new Object[]{cardNo}, this);
		return bookCopies;
	}

	@Override
	public List<BookCopy> extractData(ResultSet rs) throws SQLException {
		List<BookCopy> bookCopies = new ArrayList<>();
		
		while(rs.next()){
			BookCopy b = new BookCopy();
			b.setBook(bdao.readBookByPK(rs.getInt("bookId")));
			b.setBranch(brdao.readBranchByPK(rs.getInt("branchId")));
			b.setNoOfCopies(rs.getInt("noOfCopies"));
			bookCopies.add(b);
		}
		return bookCopies;
	}
}
