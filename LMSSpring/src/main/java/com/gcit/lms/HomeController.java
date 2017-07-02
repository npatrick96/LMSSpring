package com.gcit.lms;


//import java.util.Date;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;
import com.gcit.lms.service.AdminService;
import com.gcit.lms.service.BorrowerService;
import com.gcit.lms.service.LibrarianService;

@Controller
public class HomeController {
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	LibrarianService librarianService;
	
	@Autowired
	BorrowerService borrowerService;
	
	//================================================================================
    // Welcome page && Home menu
    //================================================================================
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "welcome";
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String admin() {
		return "admin";
	}
	
	@RequestMapping(value = "/librarian", method = RequestMethod.GET)
	public String librarian(Model model) throws SQLException {
		model.addAttribute("branches", librarianService.getAllBranches());
		return "librarian";
	}
	
	@RequestMapping(value = "/borrower", method = RequestMethod.GET)
	public String borrower() {
		return "borrower";
	}
	
	//================================================================================
    // Books pages
    //================================================================================
	
	@RequestMapping(value = "/a_book", method = RequestMethod.GET)
	public String a_book() {
		return "a_book";
	}
	
	@RequestMapping(value = "/a_addbook", method = RequestMethod.GET)
	public String a_addBook(Model model) throws SQLException {
		List<Author> authors = adminService.getAllAuthors();
		List<Genre> genres = adminService.getAllGenres();
		List<Publisher> publishers = adminService.getAllPublishers();
		model.addAttribute("authors", authors);
		model.addAttribute("genres", genres);
		model.addAttribute("publishers", publishers);
		return "a_addbook";
	}

	@RequestMapping(value = "/addBook", method = RequestMethod.POST)
	public String addBook(Model model, @RequestParam("title") String title, 
			@RequestParam(value="authorId", required=false) String[] authorIds,
			@RequestParam(value="genreId", required=false) String[] genreIds, 
			@RequestParam(value="publisherId", required=false) String publisherId) throws SQLException {
		Book book = new Book();
		book.setTitle(title);
		if (authorIds != null && authorIds.length > 0) {
			ArrayList<Author> authors = new ArrayList<Author>();
			for (String a : authorIds) {
				Author author = adminService.getAuthorByPK(Integer.parseInt(a));
				authors.add(author);
			}
			book.setAuthors(authors);
		}
		if (genreIds != null && genreIds.length > 0) {
			ArrayList<Genre> genres = new ArrayList<Genre>();
			for (String g : genreIds) {
				Genre genre = adminService.getGenreByPK(Integer.parseInt(g));
				genres.add(genre);
			}
			book.setGenres(genres);
		}
		if (publisherId != null  && publisherId != "") {
			Publisher publisher = adminService.getPublisherByPK(Integer.parseInt(publisherId));
			book.setPublisher(publisher);
		}
		adminService.saveBook(book);
		Integer booksCount = adminService.getBooksCount("");
		Integer pages = getPagesNumber(booksCount);
		model.addAttribute("pages", pages);
		model.addAttribute("books", adminService.getAllBooks(1, null));
		return "a_viewbooks";
	}
	
	@RequestMapping(value = "/a_editbook", method = RequestMethod.GET)
	public String a_editBook(Model model, 
			@RequestParam("bookId") Integer bookId, Integer pageNo, String searchString) throws SQLException {
		Book book = adminService.getBookByPK(bookId);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("searchString", searchString);
		model.addAttribute("book", book);
		model.addAttribute("authors", adminService.getAllAuthors());
		model.addAttribute("genres", adminService.getAllGenres());
		model.addAttribute("publishers", adminService.getAllPublishers());
		return "a_editbook";
	}
	
	@RequestMapping(value = "/editBook", method = RequestMethod.POST)
	public String editBook(Model model, @RequestParam("bookId") Integer bookId,
			@RequestParam("title") String title, 
			@RequestParam(value="authorId", required=false) String[] authorIds,
			@RequestParam(value="genreId", required=false) String[] genreIds, 
			@RequestParam(value="publisherId", required=false) String publisherId, 
			Integer pageNo, String searchString) throws SQLException {
		
		Book book = adminService.getBookByPK(bookId);
		book.setTitle(title);
		
		if (authorIds != null && authorIds.length > 0){
			ArrayList<Author> authors = new ArrayList<Author>();
			for (String a: authorIds){
				Author author = adminService.getAuthorByPK(Integer.parseInt(a));
				authors.add(author);
			}
			book.setAuthors(authors);
		}else{book.setAuthors(null);}
		if (genreIds != null && genreIds.length > 0){
			ArrayList<Genre> genres = new ArrayList<Genre>();
			for (String g: genreIds){
				Genre genre = adminService.getGenreByPK(Integer.parseInt(g));
				genres.add(genre);
			}
			book.setGenres(genres);
		}else{book.setGenres(null);}
		if (publisherId != null && publisherId != ""){
			if (Integer.parseInt(publisherId) != 0){
			Publisher publisher = adminService.getPublisherByPK(Integer.parseInt(publisherId));
			book.setPublisher(publisher);
			}else{
				book.setPublisher(null);
			}
		}else{}
		
		adminService.saveBook(book);
		model.addAttribute("books", adminService.getAllBooks(pageNo, searchString));
		Integer booksCount = adminService.getBooksCount(searchString);
		Integer pages = getPagesNumber(booksCount);
		model.addAttribute("pages", pages);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("searchString", searchString);
		return "a_viewbooks";
	}
	
//	@RequestMapping(value = "/a_viewbooks", method = RequestMethod.GET)
//	public String a_viewBooks(Model model) throws SQLException { 
//		//GENERIC FOR VIEW PAGES WITH NO SEARCH OR PAGINATION
//		model.addAttribute("books", adminService.getAllBooks(1, null));
//		Integer booksCount = adminService.getBooksCount("");
//		Integer pages = getPagesNumber(booksCount);
//		model.addAttribute("pages", pages);
//		return "a_viewbooks";
//		}
	
	@RequestMapping(value = "/deleteBook", method = RequestMethod.GET)
	public String deleteBook(Model model, 
			@RequestParam("bookId") Integer bookId, Integer pageNo, String searchString) throws SQLException {
		Book book = adminService.getBookByPK(bookId);
		adminService.deleteBook(book);
		System.out.println(searchString + searchString.length());
		model.addAttribute("books", adminService.getAllBooks(pageNo, searchString));
		Integer booksCount = adminService.getBooksCount(searchString);
		Integer pages = getPagesNumber(booksCount);
		model.addAttribute("pages", pages);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("searchString", searchString);
		model.addAttribute("pages", pages);
		return "a_viewbooks";
	}
	
	
	@RequestMapping(value = "/a_viewbooks", method = RequestMethod.GET)
	public String a_viewBooks(Model model, Integer pageNo, String searchString) throws SQLException { 
		Integer booksCount = adminService.getBooksCount("");
		List<Book> books = new ArrayList<>();
		if (searchString == null){
			searchString = "";}
		if(pageNo == null){
			pageNo = 1;}
		books = adminService.getAllBooks(pageNo, searchString);
		booksCount = adminService.getBooksCount(searchString); 
		Integer pages = getPagesNumber(booksCount);
		model.addAttribute("books", books);
		model.addAttribute("pages", pages);
		model.addAttribute("pageNo", pageNo);
		System.out.println(searchString + "searchString");
		model.addAttribute("searchString", searchString);
		return "a_viewbooks";
	}
	
	@RequestMapping(value = "/searchBooks", method = RequestMethod.GET) 
	public void searchBooks(Model model, 
			@RequestParam("searchString") String searchString, 
			HttpServletResponse response) throws SQLException, IOException {
		if (searchString == null) {
			searchString = "";}
		Integer pageNo = 1;
		Integer booksCount = adminService.getBooksCount(searchString);
		Integer pages = getPagesNumber(booksCount);
		List<Book> books = adminService.getAllBooks(pageNo, searchString);
		StringBuffer strBuf = new StringBuffer();
		
		strBuf.append("<nav aria-label='Page navigation'><ul class='pagination'><li><a href='#' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>");
		for (int i = 1; i<= pages; i++){
			strBuf.append("<li><a href='a_viewbooks?pageNo="+i+"&searchString="+searchString+"'>"+i+"</a></li>");
		}
		strBuf.append("<li><a href='#' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li></ul></nav>");
		strBuf.append("<table class='table' id='authorsTable'>");
		strBuf.append("<tr><th>No</th><th>Book Name</th><th>Authors</th><th>Genres</th><th>Publisher</th><th>Edit</th><th>Delete</th></tr>");
		for (Book bk : books) {
			int idx = books.indexOf(bk) + 1;
			strBuf.append("<tr><td>" + idx
					+ "</td><td>" + bk.getTitle() + "</td><td>");
			for (Author a : bk.getAuthors()) {
				strBuf.append(" '"+a.getAuthorName() + "' ");
			}
			strBuf.append("</td><td>");
			for (Genre g : bk.getGenres()) {
				strBuf.append(" '"+g.getGenreName() + "' ");
			}
			strBuf.append("</td><td>");
			if (bk.getPublisher()!=null) {
				strBuf.append(bk.getPublisher().getPublisherName());
			}else{
				strBuf.append("");
			}
			strBuf.append("</td><td><button type='button' class='btn btn-sm btn-primary'data-toggle='modal' data-target='#editBookModal' href='a_editbook.jsp?bookId="
					+ bk.getBookId() + "'>Edit!</button></td>");
			strBuf.append("<td><button type='button' class='btn btn-sm btn-danger' onclick='javascript:location.href='deleteBook?bookId="
					+ bk.getBookId()
					+ "''>Delete!</button></td></tr>");
		}
		strBuf.append("</table>");
		response.getWriter().write(strBuf.toString());
	}
	
	
	//================================================================================
    // Authors pages
    //================================================================================

	
	@RequestMapping(value = "/a_author", method = RequestMethod.GET)
	public String a_author() {
		return "a_author";
	}
	
	@RequestMapping(value = "/a_addauthor", method = RequestMethod.GET)
	public String a_addAuthor(Model model) throws SQLException {
		List<Book> books = adminService.getAllBooks();
		model.addAttribute("books", books);
		return "a_addauthor";
	}
	
	@RequestMapping(value = "/addAuthor", method = RequestMethod.POST)
	public String addAuthor(Model model, 
			@RequestParam("authorName") String authorName,
			@RequestParam(value ="bookId", required = false) String[] bookIds) throws SQLException {
		Author author = new Author();
		author.setAuthorName(authorName);
		if (bookIds != null && bookIds.length > 0) {
			ArrayList<Book> books = new ArrayList<Book>();
			for (String b: bookIds){
				Book book = adminService.getBookByPK(Integer.parseInt(b));
				books.add(book);
			}
		author.setBooks(books);
		}
		adminService.saveAuthor(author);
		Integer authorsCount = adminService.getAuthorsCount("");
		Integer pages = getPagesNumber(authorsCount);
		model.addAttribute("pages", pages);
		model.addAttribute("authors", adminService.getAllAuthors(1, null));
		return "a_viewauthors";
	}
	
	@RequestMapping(value = "/a_editauthor", method = RequestMethod.GET)
	public String a_editAuthor(Model model, 
			@RequestParam("authorId") Integer authorId, Integer pageNo, String searchString) throws SQLException {
		Author author = adminService.getAuthorByPK(authorId);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("searchString", searchString);
		model.addAttribute("author", author);
		return "a_editauthor";
	}
	
	@RequestMapping(value = "/editAuthor", method = RequestMethod.POST)
	public String editAuthor(Model model, 
			@RequestParam("authorId") Integer authorId,
			@RequestParam("authorName") String authorName, Integer pageNo, String searchString) throws SQLException {
		Author author = adminService.getAuthorByPK(authorId);
		author.setAuthorName(authorName);
		adminService.saveAuthor(author);
		model.addAttribute("authors", adminService.getAllAuthors(pageNo, searchString));
		Integer authorsCount = adminService.getAuthorsCount(searchString);
		Integer pages = getPagesNumber(authorsCount);
		model.addAttribute("pages", pages);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("searchString", searchString);
		return "a_viewauthors";
	}	
	
	@RequestMapping(value = "/deleteAuthor", method = RequestMethod.GET)
	public String deleteAuthor(Model model, 
			@RequestParam("authorId") Integer authorId, Integer pageNo, String searchString) throws SQLException {
		Author author = adminService.getAuthorByPK(authorId);
		adminService.deleteAuthor(author);
		model.addAttribute("authors", adminService.getAllAuthors(pageNo, searchString));
		Integer authorsCount = adminService.getAuthorsCount(searchString);
		Integer pages = getPagesNumber(authorsCount);
		model.addAttribute("pages", pages);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("searchString", searchString);
		model.addAttribute("pages", pages);
		return "a_viewauthors";
	}
	
	@RequestMapping(value = "/a_viewauthors", method = RequestMethod.GET)
	public String a_viewAuthors(Model model, Integer pageNo, String searchString) throws SQLException {
		List<Author> authors = new ArrayList<>();
		Integer authorsCount = 0;
		if (searchString == null){
			searchString = "";}
		if(pageNo == null){
			pageNo = 1;}
		authors = adminService.getAllAuthors(pageNo, searchString);
		authorsCount = adminService.getAuthorsCount(searchString);
		Integer pages = getPagesNumber(authorsCount);
		model.addAttribute("authors", authors);
		model.addAttribute("pages", pages);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("searchString", searchString);
		return "a_viewauthors";
	}
	
	@RequestMapping(value = "/searchAuthors", method = RequestMethod.GET) 
	public void searchAuthors(Model model, 
			@RequestParam("searchString") String searchString, 
			HttpServletResponse response) throws SQLException, IOException {
		if (searchString == null) {
			searchString = "";
		}
		
		Integer pageNo = 1;
		Integer authorsCount = adminService.getAuthorsCount(searchString);
		Integer pages = getPagesNumber(authorsCount);
		
		List<Author> authors = adminService.getAllAuthors(pageNo, searchString);
		StringBuffer strBuf = new StringBuffer();
		
		strBuf.append("<nav aria-label='Page navigation'><ul class='pagination'><li><a href='#' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>");
		for (int i = 1; i<= pages; i++){
			strBuf.append("<li><a href='a_viewauthors?pageNo="+i+"&searchString="+searchString+"'>"+i+"</a></li>");
		}
		strBuf.append("<li><a href='#' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li></ul></nav>");
		strBuf.append("<table class='table' id='authorsTable'>");
		
		strBuf.append("<tr><th>Author ID</th><th>Author Name</th><th>Books by Author</th><th>Edit</th><th>Delete</th></tr>");
		for (Author a : authors) {
			int idx = authors.indexOf(a) + 1;
			strBuf.append("<tr><td>" + idx
					+ "</td><td>" + a.getAuthorName() + "</td><td>");
			for (Book b : a.getBooks()) {
				strBuf.append(" '"+b.getTitle() + "' ");
			}
			strBuf.append("</td><td><button type='button' class='btn btn-sm btn-primary'data-toggle='modal' data-target='#editAuthorModal' href='a_editauthor.jsp?authorId="
					+ a.getAuthorId() + "'>Edit!</button></td>");
			strBuf.append("<td><button type='button' class='btn btn-sm btn-danger' onclick='javascript:location.href='deleteAuthor?authorId="
					+ a.getAuthorId()
					+ "''>Delete!</button></td></tr>");
		}
		strBuf.append("</table>");
		response.getWriter().write(strBuf.toString());
	}
	
	//================================================================================
    // Borrowers pages
    //================================================================================
	
	@RequestMapping(value = "/a_viewborrowers", method = RequestMethod.GET)
	public String a_viewBorrowers(Model model) throws SQLException {
		model.addAttribute("borrowers", adminService.getAllBorrowers(1, null));
		Integer borrowersCount = adminService.getBorrowersCount("");
		Integer pages = getPagesNumber(borrowersCount);
		model.addAttribute("pages", pages);
		return "a_viewborrowers";
	}
	
	//================================================================================
    // Loans pages
    //================================================================================
	
	@RequestMapping(value = "/a_viewboookloans", method = RequestMethod.GET)
	public String a_viewBookLoans(Model model) throws SQLException {
		model.addAttribute("bookloans", adminService.getAllBookLoans(1, null));
		Integer loansCount = adminService.getBookLoansCount("");
		Integer pages = getPagesNumber(loansCount);
		model.addAttribute("pages", pages);
		return "a_viewbookloans";
	}
	
	//================================================================================
    // Branches pages
    //================================================================================
	
	@RequestMapping(value = "/a_viewbranches", method = RequestMethod.GET)
	public String a_viewBranches(Model model) throws SQLException {
		model.addAttribute("branches", adminService.getAllBranches(1, null));
		Integer branchesCount = adminService.getBranchesCount("");
		Integer pages = getPagesNumber(branchesCount);
		model.addAttribute("pages", pages);
		return "a_viewbranches";
	}
	
	//================================================================================
    // Publishers pages
    //================================================================================
	
	@RequestMapping(value = "/a_viewpublishers", method = RequestMethod.GET)
	public String a_viewPublishers(Model model) throws SQLException {
		model.addAttribute("publishers", adminService.getAllPublishers(1, null));
		Integer publishersCount = adminService.getPublishersCount("");
		Integer pages = getPagesNumber(publishersCount);
		model.addAttribute("pages", pages);
		return "a_viewpublishers";
	}
	
	//================================================================================
    // Helpers Methods
    //================================================================================
	
	public Integer getPagesNumber(Integer entityCount){
		int pages = 0;
		if (entityCount % 10 > 0) {
			pages = entityCount / 10 + 1;
		} else if (entityCount > 1) {
			pages = 1;
		} else {
			pages = 0;
		}
		return pages;
	}
	
}
