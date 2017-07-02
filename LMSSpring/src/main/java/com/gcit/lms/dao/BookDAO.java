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

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Branch;
import com.mysql.jdbc.Statement;

public class BookDAO extends BaseDAO  implements ResultSetExtractor<List<Book>>{

	public void addBook(Book book) throws SQLException{
		template.update("insert into tbl_book(title) values (?)", new Object[] {book.getTitle()});
	}
	
	public void resetBookAuthors(Integer bookId) throws SQLException{
		template.update("delete from tbl_book_authors where bookId = ?", new Object[] {bookId});
	}
	
	public void resetBookGenres(Integer bookId) throws SQLException{
		template.update("delete from tbl_book_genres where bookId = ?", new Object[] {bookId});
	}
	
	public void resetBookPublisher(Integer bookId) throws SQLException{
		template.update("update tbl_book set pubId = null where bookId = ?", new Object[] {bookId});
	}
	
	public void addBookAuthor(Book book, Integer authorId) throws SQLException{
		template.update("insert into tbl_book_authors(bookId, authorId) values (?, ?)", new Object[] {book.getBookId(), authorId});
	}
	
	public Integer addBookWithID(Book book) throws SQLException {
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "insert into tbl_book(title) values (?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, book.getTitle());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}
	
	public void updateBook(Book book) throws SQLException{
		template.update("update tbl_book set title =? where bookId = ?", new Object[] {book.getTitle(), book.getBookId()});
	}
	
	public void deleteBook(Book book) throws SQLException{
		template.update("delete from tbl_book where bookId = ?", new Object[] {book.getBookId()});
	}
	
	public List<Book> readAllBooks() throws SQLException{
		return template.query("select * from tbl_book", this);
	}
	
	public List<Book> readAllBooksByTitle(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		return (List<Book>) template.query("select * from tbl_book where title like ?", new Object[]{searchString}, this);
	}
	
	public List<Book> readAllBooksByBranch(Branch branch) throws SQLException{
		return template.query("select * from tbl_book where bookId IN (Select bookId from tbl_book_copies where branchId = ?)", new Object[]{branch.getBranchId()}, this);
	}
	
	public List<Book> readAllBooksByBranchId(Integer branchId) throws SQLException{
		return template.query("select * from tbl_book where bookId IN (Select bookId from tbl_book_copies where branchId = ?)", new Object[]{branchId}, this);
	}
	
	public List<Book> readAllBooksByAuthorId(Integer authorId) throws SQLException {
		return template.query("select * from tbl_book where bookId in (select bookId from tbl_book_authors where authorId = ?)", new Object[]{authorId}, this);
	}
	
	public List<Book> readAllBooksByGenreId(Integer genreId) throws SQLException {
		return template.query("select * from tbl_book where bookId in (select bookId from tbl_book_genres where genre_id = ?)", new Object[]{genreId}, this);
	}
	
	public List<Book> readAllBooksByPublisherId(Integer publisherId) throws SQLException {
		return template.query("select * from tbl_book where pubId = ?", new Object[]{publisherId}, this);
	}
	
	public Book readBookByPK(Integer bookId) throws SQLException{
		List<Book> books =  template.query("select * from tbl_book where bookId = ?", new Object[]{bookId}, this);
		if(books!=null){
			return books.get(0);
		}
		return null;
	}
	
	public Integer getBooksCount() throws SQLException{
		return template.queryForObject("select count(*) as COUNT from tbl_book",Integer.class);
	}
	
	public Integer getBooksCount(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		return template.queryForObject("select count(*) as COUNT from tbl_book where title like ?",new Object[]{searchString},Integer.class);
	}
	
	public List<Book> readAllBooks(Integer pageNo) throws SQLException{
		setPageNo(pageNo);
		if (pageNo>0){
			int index = (getPageNo() - 1) * 10;
			return  template.query("select * from tbl_book" + " LIMIT " + index + " , " + getPageSize(), this);
		}else{
			return  template.query("select * from tbl_book", this);	
		}
	}
	
	public List<Book> readAllBooksByName(Integer pageNo, String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		setPageNo(pageNo);
		if (pageNo>0){
			int index = (getPageNo() - 1) * 10;
			return template.query("select * from tbl_book where title like ?" + " LIMIT " + index + " , " + getPageSize(), new Object[]{searchString}, this);
		}else{
			return template.query("select * from tbl_book where title like ?", new Object[]{searchString}, this);
		}
	}

	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<>();
		while(rs.next()){
			Book b = new Book();
			b.setBookId(rs.getInt("bookId"));
			b.setTitle(rs.getString("title"));
			books.add(b);
		}
		return books;
	}
}
