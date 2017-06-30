package com.gcit.lms.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gcit.lms.dao.BookCopyDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopy;
import com.gcit.lms.entity.Branch;

public class LibrarianService {
	@Autowired
	BranchDAO brdao;
	
	@Autowired
	BookCopyDAO bcdao;
	
	@Autowired
	BookDAO bdao;
	
	public List<BookCopy> getAllBookCopiesOwnedBy(Branch branch) throws SQLException {
			return bcdao.readAllBookCopiesByBranch(branch);
	}
	
	public Branch getBranchByPk(Integer branchId) throws SQLException {
			return brdao.readBranchByPK(branchId);
			}

	public BookCopy getBookCopyByPks(Integer branchId, Integer bookId) throws SQLException {
			return bcdao.readBookCopyByPks(branchId, bookId);
	}

	public List<Branch> getAllBranches() throws SQLException {
			return brdao.readAllBranchs();
	}

	@Transactional
	public void addBookCopiesToBranch(Book book, Branch branch, Integer noOfCopies) throws SQLException {
		if (book.getBookId() != null) {
			bdao.updateBook(book);
		} else {
			bdao.addBook(book);
		}
		saveBranch(branch);
		// TODO: Not sure the below implementation is the best way to do it
		int currentNoOfCopies = bcdao.getBookCopyNoOfCopies(book, branch);
		BookCopy bookcopy = new BookCopy();
		bookcopy.setBook(book);
		bookcopy.setBranch(branch);
		bookcopy.setNoOfCopies(noOfCopies + currentNoOfCopies);
		bcdao.addBookCopy(bookcopy);
	}

	@Transactional
	public void updateBookCopiesOfBranch(Book book, Branch branch, Integer noOfCopies) throws SQLException {
		if (book.getBookId() != null) {
			bdao.updateBook(book);
		} else {
			bdao.addBook(book);
		}
		saveBranch(branch);
		BookCopy bookcopy = new BookCopy();
		bookcopy.setBook(book);
		bookcopy.setBranch(branch);
		bookcopy.setNoOfCopies(noOfCopies);
		bcdao.addBookCopy(bookcopy);
	}
	
	@Transactional
	public void deleteBookCopy(Integer bookId, Integer branchId) throws SQLException {
		bcdao.deleteBookCopy(bookId, branchId);
	}

	@Transactional
	public void saveBranch(Branch branch) throws SQLException {
		if (branch.getBranchId() != null) {
			brdao.updateBranchName(branch);
			if (branch.getBranchAddress() != null) {
				brdao.updateBranchNameAndAddress(branch);
			} else {
				brdao.updateBranchName(branch);
			}
		} else {
			int branchId = brdao.addBranchWithID(branch);
			if (branch.getBranchAddress() != null) {
				brdao.updateBranchAddress(branchId, branch.getBranchAddress());
			}
		}
	}
	
	@Transactional
	public void saveBookCopy(BookCopy copy) throws SQLException {
		if (copy.getBranch() != null && copy.getBook() != null) {
			bcdao.updateNoOfCopies(copy);
		} else {
			bcdao.addBookCopy(copy);
		}
	}
}