package com.gcit.lms;


//import java.util.Date;
import java.sql.SQLException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gcit.lms.entity.Author;
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
	public String author() {
		return "a_author";
	}
	
	@RequestMapping(value = "/a_addauthor", method = RequestMethod.GET)
	public String addAuthor(Model model) throws SQLException {
		Integer authorId = Integer.parseInt(model.asMap().get("authorId").toString());
		Author author = adminService.getAuthorByPK(authorId);
		//TODO: add books
		adminService.saveAuthor(author);
		return "a_addauthor";
	}
	
	@RequestMapping(value = "/a_viewauthors", method = RequestMethod.GET)
	public String a_viewAuthors(Model model) throws SQLException {
		model.addAttribute("authors", adminService.getAllAuthors(1, null));
		Integer authCount = adminService.getAuthorsCount(""); 
		// TODO: add searchstring later
		int pages = getPagesNumber(authCount);
		model.addAttribute("pages", pages);
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
