package com.gcit.lms.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gcit.lms.dao.BookCopyDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopy;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;

public class BorrowerService {
	@Autowired
	BorrowerDAO bodao;
	
	@Autowired
	BookDAO bdao;
	
	@Autowired
	BranchDAO brdao;
	
	@Autowired
	BookLoanDAO bldao;
	
	@Autowired
	BookCopyDAO bcdao;
	
	public Borrower getBorrowerByPK(Integer authorId) throws SQLException {
		return bodao.readBorrowerByPK(authorId);
	}
	
	public Boolean isValidCardNo(Integer cardNo) throws SQLException {
		Integer num =  bodao.getBorrowersCountByPk(cardNo);
		if(num > 0){
			return true;
		}else{
			return false;
		}
	}
	
	@Transactional
	public void checkOutBook(Integer bookId, Integer branchId, Integer cardNo) throws SQLException {
		Branch branch = new Branch();
		Book book = new Book();
		Borrower borrower = new Borrower();

		branch.setBranchId(branchId);
		book.setBookId(bookId);
		borrower.setCardNo(cardNo);

		BookLoan bookloan = new BookLoan();

		bookloan.setBook(book);
		bookloan.setBorrower(borrower);
		bookloan.setBranch(branch);

		LocalDateTime todayDateTime = LocalDateTime.now();
		bookloan.setDateOut(todayDateTime + "");
		bookloan.setDueDate(todayDateTime.plusWeeks(1) + "");

		bcdao.decrementNoOfCopiesToZero(bookId, branchId);
		bcdao.decrementNoOfCopies(bookId, branchId);
		bldao.addBookLoan(bookloan);
		// TODO: Test it and make sure it works
	}
	
	@Transactional
	public void returnBook(BookLoan bookLoan) throws SQLException {
		bldao.returnBook(bookLoan);
		BookCopy bookCopy = new BookCopy();
		bookCopy.setBook(bookLoan.getBook());
		bookCopy.setBranch(bookLoan.getBranch());
		if (bcdao.getBookCopiesCount(bookCopy) > 0) {
			bcdao.incrementNoOfCopies(bookLoan.getBook().getBookId(), bookLoan.getBranch().getBranchId());
		} else {
			bookCopy.setNoOfCopies(1);
			bcdao.addBookCopy(bookCopy);
		}
	}
	
	public List<Branch> getAllBranches() throws SQLException {
		return brdao.readAllBranchs();
	}
	
	public List<Book> getAllBooksOwned(Branch branch) throws SQLException {
		return bdao.readAllBooksByBranch(branch);
	}
}
