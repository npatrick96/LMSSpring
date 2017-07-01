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
    // Authors pages
    //================================================================================

	
	@RequestMapping(value = "/a_author", method = RequestMethod.GET)
	public String a_author() {
		return "a_author";
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
//		model.addAttribute("authors", adminService.getAllAuthors(1, null));
//		Integer authorsCount = adminService.getAuthorsCount("");
		model.addAttribute("authors", adminService.getAllAuthors(pageNo, searchString));
		Integer authorsCount = adminService.getAuthorsCount(searchString);
		Integer pages = getPagesNumber(authorsCount);
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
		//if (pageNo == null) {
		//	pageNo = 1;}    ,@RequestParam("pageNo") Integer pageNo
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
	
	@RequestMapping(value = "/deleteAuthor", method = RequestMethod.GET)
	public String deleteAuthor(Model model, 
			@RequestParam("authorId") Integer authorId, Integer pageNo, String searchString) throws SQLException {
		Author author = adminService.getAuthorByPK(authorId);
		adminService.deleteAuthor(author);
//		model.addAttribute("authors", adminService.getAllAuthors(1, null));
//		Integer authorsCount = adminService.getAuthorsCount("");
		model.addAttribute("authors", adminService.getAllAuthors(pageNo, searchString));
		Integer authorsCount = adminService.getAuthorsCount(searchString);
		Integer pages = getPagesNumber(authorsCount);
		model.addAttribute("pages", pages);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("searchString", searchString);
		model.addAttribute("pages", pages);
		return "a_viewauthors";
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
			@RequestParam("bookId") String[] bookIds) throws SQLException {
		Author author = new Author();
		author.setAuthorName(authorName);
		ArrayList<Book> books = new ArrayList<Book>();
		for (String b: bookIds){
			Book book = adminService.getBookByPK(Integer.parseInt(b));
			books.add(book);
		}
		author.setBooks(books);
		adminService.saveAuthor(author);
		Integer authorsCount = adminService.getAuthorsCount("");
		Integer pages = getPagesNumber(authorsCount);
		model.addAttribute("pages", pages);
		model.addAttribute("authors", adminService.getAllAuthors(1, null));
		return "a_viewauthors";
	}
	
	@RequestMapping(value = "/a_viewauthors", method = RequestMethod.GET)
	public String a_viewAuthors(Model model, Integer pageNo, String searchString) throws SQLException {
		List<Author> authors = new ArrayList<>();
		Integer authorsCount = 0;
		if (pageNo != null && searchString != null){
			authors = adminService.getAllAuthors(pageNo, searchString);
			authorsCount = adminService.getAuthorsCount(searchString); 
		}else if(searchString != null){
			authors = adminService.getAllAuthors(1, searchString);
			authorsCount = adminService.getAuthorsCount(searchString); 
		}else{
			authors = adminService.getAllAuthors(1, "");
			authorsCount = adminService.getAuthorsCount(""); 
		}
		Integer pages = getPagesNumber(authorsCount);
		model.addAttribute("authors", authors);
		model.addAttribute("pages", pages);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("searchString", searchString);
		return "a_viewauthors";
	}
	
	//================================================================================
    // Books pages
    //================================================================================
	
	@RequestMapping(value = "/a_viewbooks", method = RequestMethod.GET)
	public String a_viewBooks(Model model) throws SQLException {
		model.addAttribute("books", adminService.getAllBooks(1, null));
		Integer bookCount = adminService.getBooksCount("");
		Integer pages = getPagesNumber(bookCount);
		model.addAttribute("pages", pages);
		return "a_viewbooks";
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
