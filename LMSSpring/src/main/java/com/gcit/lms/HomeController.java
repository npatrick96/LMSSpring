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

@Controller
public class HomeController {
	
	@Autowired
	AdminService adminService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "welcome";
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String admin() {
		return "admin";
	}
	
	@RequestMapping(value = "/a_author", method = RequestMethod.GET)
	public String author() {
		return "a_author";
	}
	
	@RequestMapping(value = "/a_viewauthors", method = RequestMethod.GET)
	public String viewAuthors(Model model) throws SQLException {
		model.addAttribute("authors", adminService.getAllAuthors(1, null));
		Integer authCount = adminService.getAuthorsCount();
		int pages = 0;
		if (authCount % 10 > 0) {
			pages = authCount / 10 + 1;
		} else {
			pages = authCount / 10;
		}
		model.addAttribute("pages", pages);
		return "a_viewauthors";
	}
	
	@RequestMapping(value = "/a_viewbooks", method = RequestMethod.GET)
	public String viewBooks(Model model) throws SQLException {
		model.addAttribute("books", adminService.getAllBooks(1, null));
		Integer authCount = adminService.getBooksCount();
		int pages = 0;
		if (authCount % 10 > 0) {
			pages = authCount / 10 + 1;
		} else {
			pages = authCount / 10;
		}
		model.addAttribute("pages", pages);
		return "a_viewbooks";
	}
	
	@RequestMapping(value = "/a_addauthor", method = RequestMethod.GET)
	public String addAuthor(Model model) throws SQLException {
		Integer authorId = Integer.parseInt(model.asMap().get("authorId").toString());
		AdminService service = new AdminService();
		Author author = service.getAuthorByPK(authorId);
		//TODO: add books
		adminService.saveAuthor(author);
		return "a_addauthor";
	}
	
}
