package com.gcit.lms.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;

public class AdminService {
	@Autowired
	AuthorDAO adao;

	@Autowired
	BookDAO bdao;

	@Autowired
	GenreDAO gdao;

	@Autowired
	PublisherDAO pdao;

	@Autowired
	BranchDAO brdao;

	@Autowired
	BorrowerDAO bodao;

	@Autowired
	BookLoanDAO bldao;

	@Transactional
	public void saveAuthor(Author author) throws SQLException {
		if (author.getAuthorId() != null) {
			adao.updateAuthor(author);
		} else {
			int authId = adao.addAuthorWithID(author);
			if (author.getBooks() != null) {
				for (Book b : author.getBooks()) {
					bdao.addBookAuthor(b, authId);
				}
			}
		}
	}

	@Transactional
	public void deleteAuthor(Author author) throws SQLException {
		adao.deleteAuthor(author);
	}

	public Author getAuthorByPK(Integer authorId) throws SQLException {
		Author a = adao.readAuthorsByPK(authorId);
		a.setBooks(bdao.readAllBooksByAuthorId(a.getAuthorId()));
		return a;
	}

	public Integer getAuthorsCount() throws SQLException {
		return adao.getAuthorsCount();
	}

	public Integer getAuthorsCount(String searchString) throws SQLException {
		return adao.getAuthorsCount(searchString);
	}

	public List<Author> getAllAuthors() throws SQLException {
		List<Author> authors = adao.readAllAuthors();
		for(Author a: authors){
			a.setBooks(bdao.readAllBooksByAuthorId(a.getAuthorId()));
		}
		return authors;
	}

	public List<Author> getAllAuthors(Integer pageNo, String searchString) throws SQLException {
		List<Author> authors = new ArrayList<>();
		if (searchString != null) {
			authors = adao.readAllAuthorsByName(pageNo, searchString);
		} else {
			authors = adao.readAllAuthors(pageNo);
		}
		for(Author a: authors){
			a.setBooks(bdao.readAllBooksByAuthorId(a.getAuthorId()));
		}
		return authors;
	}

	public List<Book> getAllBooks() throws SQLException {
		List<Book> books = bdao.readAllBooks();
		for(Book b: books){
			b.setAuthors(adao.readAllAuthorsByBookId(b.getBookId()));
			b.setGenres(gdao.readAllGenresByBookId(b.getBookId()));
			b.setPublisher(pdao.readPublisherByBookId(b.getBookId()));
		}
		return books;
	}

	public Book getBookByPK(Integer bookId) throws SQLException {
		Book b = bdao.readBookByPK(bookId);
		b.setAuthors(adao.readAllAuthorsByBookId(b.getBookId()));
		b.setGenres(gdao.readAllGenresByBookId(b.getBookId()));
		b.setPublisher(pdao.readPublisherByBookId(b.getBookId()));
		return b;
		}

	public List<Book> getAllBooks(Integer pageNo, String searchString) throws SQLException {
		List<Book> books = new ArrayList<>();
		if (searchString != null) {
			books = bdao.readAllBooksByName(pageNo, searchString);
		} else {
			books = bdao.readAllBooks(pageNo);
		}
		for (Book b: books){
			b.setAuthors(adao.readAllAuthorsByBookId(b.getBookId()));
			b.setGenres(gdao.readAllGenresByBookId(b.getBookId()));
			b.setPublisher(pdao.readPublisherByBookId(b.getBookId()));
		}
		return books;
	}
	
	public Integer getBooksCount() throws SQLException {
		return bdao.getBooksCount();
	}

	public Integer getBooksCount(String searchString) throws SQLException {
		System.out.println(bdao.getBooksCount(searchString));
		return bdao.getBooksCount(searchString);
	}

	@Transactional
	public void saveBook(Book book) throws SQLException {
		int bookId;
		if (book.getBookId() != null) {

			bookId = book.getBookId();
			bdao.resetBookAuthors(bookId);
			bdao.resetBookGenres(bookId);
			bdao.resetBookPublisher(bookId);

			if (book.getAuthors() != null) {
				for (Author a : book.getAuthors()) {
					adao.addAuthorBook(a, bookId);
				}
			}
			if (book.getGenres() != null) {
				for (Genre g : book.getGenres()) {
					gdao.addGenreBook(g, bookId);
				}
			}
			if (book.getPublisher() != null) {
				pdao.addPublisherBook(book.getPublisher(), bookId);
			}
			bdao.updateBook(book);
		} else {
			bookId = bdao.addBookWithID(book);
			if (book.getAuthors() != null) {
				for (Author a : book.getAuthors()) {
					adao.addAuthorBook(a, bookId);
				}
			}

			if (book.getGenres() != null) {
				for (Genre g : book.getGenres()) {
					gdao.addGenreBook(g, bookId);
				}
			}

			if (book.getPublisher() != null) {
				pdao.addPublisherBook(book.getPublisher(), bookId);
			}
		}
	}

	@Transactional
	public void deleteBook(Book Book) throws SQLException {
		bdao.deleteBook(Book);
	}

	@Transactional
	public void saveGenre(Genre genre) throws SQLException {
		if (genre.getGenreId() != null) {
			gdao.updateGenre(genre);
		} else {
			gdao.addGenre(genre);
		}
	}

	public List<Genre> getAllGenres() throws SQLException {
		List<Genre> genres = gdao.readAllGenres();
		for (Genre g:genres){
			g.setBooks(bdao.readAllBooksByGenreId(g.getGenreId()));
		}
		return genres;
	}

	public Genre getGenreByPK(Integer authorId) throws SQLException {
		Genre g = gdao.readGenresByPK(authorId);
		g.setBooks(bdao.readAllBooksByGenreId(g.getGenreId()));
		return g;
	}

	@Transactional
	public void deleteGenre(Genre genre) throws SQLException {
		gdao.deleteGenre(genre);
	}

	public List<Publisher> getAllPublishers() throws SQLException {
		List<Publisher> publishers = pdao.readAllPublishers();
		for (Publisher p:publishers){
			p.setBooks(bdao.readAllBooksByPublisherId(p.getPublisherId()));
		}
		return publishers;
	}

	public Publisher getPublisherByPK(Integer authorId) throws SQLException {
		Publisher p =pdao.readPublisherByPK(authorId);
		p.setBooks(bdao.readAllBooksByPublisherId(p.getPublisherId()));
		return p;
	}

	public List<Publisher> getAllPublishers(Integer pageNo, String searchString) throws SQLException {
		List<Publisher> publishers = new ArrayList<>();
		if (searchString != null) {
			publishers = pdao.readAllPublishersByName(pageNo, searchString);
		} else {
			publishers = pdao.readAllPublishers(pageNo);
		}
		for (Publisher p:publishers){
			p.setBooks(bdao.readAllBooksByPublisherId(p.getPublisherId()));
		}
		return publishers;
	}

	public Integer getPublishersCount(String searchString) throws SQLException {
		return pdao.getPublishersCount(searchString);
	}

	@Transactional
	public void deletePublisher(Publisher publisher) throws SQLException {
		pdao.deletePublisher(publisher);
	}

	@Transactional
	public void savePublisher(Publisher publisher) throws SQLException {
		if (publisher.getPublisherId() != null) {
			pdao.updatePublisherName(publisher);
			if (publisher.getPublisherAddress() != null) {
				pdao.updatePublisherAddress(publisher);
				if (publisher.getPublisherPhone() != null) {
					pdao.updatePublisherPhone(publisher);
				}
			} else {
				int pubId = pdao.addPublisherWithID(publisher);
				if (publisher.getPublisherAddress() != null) {
					pdao.updatePublisherAddress(pubId,
							publisher.getPublisherAddress());
				}
				if (publisher.getPublisherPhone() != null) {
					pdao.updatePublisherPhone(pubId,
							publisher.getPublisherPhone());
				}
			}
		}
	}

	public Branch getBranchByPK(Integer branchId) throws SQLException {
		Branch br = brdao.readBranchByPK(branchId);
		br.setBooks(bdao.readAllBooksByBranchId(br.getBranchId()));
		br.setBookLoans(bldao.readAllBookLoansByBranchId(br.getBranchId()));
		return br;
	}

	public List<Branch> getAllBranches() throws SQLException {
		List<Branch> branches= brdao.readAllBranchs();
		for (Branch br: branches){
			br.setBooks(bdao.readAllBooksByBranchId(br.getBranchId()));
			br.setBookLoans(bldao.readAllBookLoansByBranchId(br.getBranchId()));
		}
		return branches;
	}

	public List<Branch> getAllBranches(Integer pageNo, String searchString) throws SQLException {
		List<Branch> branches = new ArrayList<>();
		if (searchString != null) {
			branches = brdao.readAllBranchesByName(pageNo, searchString);
		} else {
			branches = brdao.readAllBranches(pageNo);
		}
		for (Branch br: branches){
			br.setBooks(bdao.readAllBooksByBranchId(br.getBranchId()));
			br.setBookLoans(bldao.readAllBookLoansByBranchId(br.getBranchId()));
		}
		return branches;
	}

	public Integer getBranchesCount(String searchString) throws SQLException {
		return brdao.getBranchesCount(searchString);
	}

	@Transactional
	public void deleteBranch(Branch branch) throws SQLException {
		brdao.deleteBranch(branch);
	}

	@Transactional
	public void saveBranch(Branch branch) throws SQLException {
			if (branch.getBranchId() != null) {
				brdao.updateBranchName(branch);
				if (branch.getBranchAddress() != null){
					brdao.updateBranchNameAndAddress(branch);
					}else{
						brdao.updateBranchName(branch);
					}
			} else {
				int branchId = brdao.addBranchWithID(branch);
				if (branch.getBranchAddress() != null){
					brdao.updateBranchAddress(branchId,branch.getBranchAddress());
					}
			}
	}

	public List<Borrower> getAllBorrowers() throws SQLException {
		List<Borrower> borrowers = bodao.readAllBorrowers();
		for (Borrower bo: borrowers){
			bo.setBookLoans(bldao.readAllBookLoansByCardNo(bo.getCardNo()));
		}
		return borrowers;
	}

	public Borrower getBorrowerByPK(Integer cardNo) throws SQLException {
		Borrower bo = bodao.readBorrowerByPK(cardNo);
		bo.setBookLoans(bldao.readAllBookLoansByCardNo(bo.getCardNo()));
		return bo;
	}

	public List<Borrower> getAllBorrowers(Integer pageNo, String searchString) throws SQLException {
		List<Borrower> borrowers = new ArrayList<>();
		if (searchString != null) {
			borrowers = bodao.readAllBorrowersByName(pageNo, searchString);
		} else {
			borrowers = bodao.readAllBorrowers(pageNo);
		}
		for (Borrower bo: borrowers){
			bo.setBookLoans(bldao.readAllBookLoansByCardNo(bo.getCardNo()));
		}
		return borrowers;
	}

	public Integer getBorrowersCount(String searchString) throws SQLException {
		return bodao.getBorrowersCount(searchString);
	}
	
	public Integer getBorrowersCount() throws SQLException {
		return bodao.getBorrowersCount();
	}

	@Transactional
	public void deleteBorrower(Borrower borrower) throws SQLException {
		bodao.deleteBorrower(borrower);
	}

	@Transactional
	public void saveBorrower(Borrower borrower) throws SQLException {
		if (borrower.getCardNo() != null) {
			bodao.updateBorrowerName(borrower);
			if (borrower.getAddress() != null) {
				bodao.updateBorrowerAddress(borrower);
			}
			if (borrower.getPhone() != null) {
				bodao.updateBorrowerPhone(borrower);
			}
		} else {
			int userId = bodao.addBorrowerWithID(borrower);
			if (borrower.getAddress() != null) {
				bodao.updateBorrowerAddress(userId, borrower.getAddress());
			}
			if (borrower.getPhone() != null) {
				bodao.updateBorrowerPhone(userId, borrower.getPhone());
			}
		}
	}

	public BookLoan getBookLoanByDateOut(String dateout) throws SQLException {
		BookLoan bookloan =  bldao.readBookLoanByDateOut(dateout);
		// TODO: talk to PRAMOD about this
//			bookloan.setBook(bdao.readBookByLoanDateOut(bl.bookloans()));
//			bookloan.setBranch(brdao.readBranchbyLoanDateOut(bl.getDateOut()));
//			bookloan.setBorrower(bodao.readBorrowerByLoanDateOut(bl.getDateOut()));
		return bookloan;
	}

	public BookLoan getBookLoanBy4Pks(Integer bookId, Integer branchId,
			Integer cardNo, String dateout) throws SQLException {
		return bldao.readBookLoanBy4Pks(bookId, branchId, cardNo, dateout);
	}

	public List<BookLoan> getAllBookLoans() throws SQLException {
		return bldao.readAllBookLoans();
	}

	public List<BookLoan> getAllBookLoans(Integer pageNo, String searchString)
			throws SQLException {
		if (searchString != null) {
			return bldao.readAllBookLoansByDateOut(pageNo, searchString);
		} else {
			return bldao.readAllBookLoans(pageNo);
		}
	}

	public List<BookLoan> getAllDueBookLoans(Integer pageNo, Integer cardNo)
			throws SQLException {
		return bldao.readBookLoansByBorrowerCardNo(pageNo, cardNo);
	}

	public Integer getBookLoansCount(String searchString) throws SQLException {
		return bldao.getBookLoansCount(searchString);
	}

	public Integer getBookLoansCount(Integer cardNo) throws SQLException {
		return bldao.getBookLoansCount(cardNo);
	}

	public Integer getDueBookLoansCount(Integer cardNo) throws SQLException {
		return bldao.getDueBookLoansCount(cardNo);
	}

	@Transactional
	public void deleteBookLoan(BookLoan bookloan) throws SQLException {
		bldao.deleteBookLoan(bookloan);

	}

	@Transactional
	public void overrideDueDate(BookLoan bookloan, String newDueDate)
			throws SQLException {
		bookloan.setDueDate(newDueDate);
		bldao.updateBookLoanDueDate(bookloan);

	}
}
