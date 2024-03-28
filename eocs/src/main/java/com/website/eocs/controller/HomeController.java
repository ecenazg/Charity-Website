package com.website.eocs.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.website.eocs.dto.PaginationDto;
import com.website.eocs.entity.Account;
import com.website.eocs.entity.Categories;
import com.website.eocs.entity.Foundation;
import com.website.eocs.entity.Fund;
import com.website.eocs.service.AccountServiceImpl;
import com.website.eocs.service.AdminServiceImpl;
import com.website.eocs.service.EmailService;
import com.website.eocs.service.PaginationServiceImpl;
import com.website.eocs.service.UserService;

@Validated
@Controller
public class HomeController {
	
	@Autowired
	AdminServiceImpl adminServiceImpl;
	@Autowired
	PaginationServiceImpl paginationService;
	@Autowired
	AccountServiceImpl accountServiceImpl;
	@Autowired
	EmailService emailService;
	@Autowired
	UserService userService;
	
	@RequestMapping(value = { "/home", "/" })
	public ModelAndView userHome(@RequestParam(name = "page", defaultValue = "1") String page,
			Authentication authentication, HttpSession session) {
		int pageSize = 6;
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(page);
		} catch (Exception e) {
			currentPage = 1;
		}
		int totalData = userService.getDataFundActive().size();
		PaginationDto paginationInfo = paginationService.getDataPagination(totalData, currentPage, pageSize);
		ModelAndView mv = new ModelAndView();
		if (authentication != null) {
			Account user = accountServiceImpl.getDataAccountByEmail(authentication.getName());
			session.setAttribute("user", user);
		}

		List<Fund> funds = userService.getDataFundsPaginationActive(paginationInfo.getStart(), paginationInfo.getEnd());
		for (Fund fund : funds) {
			fund.setCurrentAmount(userService.getCurrentMoneyByFund(fund.getId()));
		}
		
		mv.addObject("fundsPagination", funds);
		mv.addObject("categoriesList", userService.getDataCategoriesActive());
		mv.addObject("foundationList", userService.getDataFoundationActive());
		mv.addObject("pagination", paginationInfo);
		mv.setViewName("user/index");
		return mv;
	}
	
	@RequestMapping(value = "/search_fund")
	public ModelAndView searchFund(@RequestParam(name = "page", defaultValue = "1") String page,
			@RequestParam(value = "fundName", required = false)String fundName,
			@RequestParam(value = "categoryId", required = false)int categoryId,
			@RequestParam(value = "foundationId", required = false)int foundationId,		
			Authentication authentication, HttpSession session) {
		int pageSize = 6;
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(page);
		} catch (Exception e) {
			currentPage = 1;
		}
		int totalData = userService.userSearchFunds(fundName, categoryId, foundationId).size();
		PaginationDto paginationInfo = paginationService.getDataPagination(totalData, currentPage, pageSize);
		ModelAndView mv = new ModelAndView();
		if (authentication != null) {
			Account user = accountServiceImpl.getDataAccountByEmail(authentication.getName());
			session.setAttribute("user", user);
		}

		List<Fund> funds = userService.userSearchFundsPagination(fundName, categoryId, foundationId, paginationInfo.getStart(), paginationInfo.getEnd());
		for (Fund fund : funds) {
			fund.setCurrentAmount(userService.getCurrentMoneyByFund(fund.getId()));
		}
		
		mv.addObject("fundsPagination", funds);
		mv.addObject("categoriesList", userService.getDataCategoriesActive());
		mv.addObject("foundationList", userService.getDataFoundationActive());
		mv.addObject("pagination", paginationInfo);
		mv.setViewName("user/search_fund");
		return mv;
	}

	@RequestMapping(value = "/foundation")
	public ModelAndView foundation(@RequestParam(name = "page", defaultValue = "1") String page,
			Authentication authentication, HttpSession session) {
		int pageSize = 6;
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(page);
		} catch (Exception e) {
			currentPage = 1;
		}
		int totalData = userService.getDataFoundationActive().size();
		PaginationDto paginationInfo = paginationService.getDataPagination(totalData, currentPage, pageSize);
		ModelAndView mv = new ModelAndView();
		if (authentication != null) {
			Account user = accountServiceImpl.getDataAccountByEmail(authentication.getName());
			session.setAttribute("user", user);
		}
		mv.addObject("foundationsPagination",
				userService.getDataFoundationPaginationActive(paginationInfo.getStart(), paginationInfo.getEnd()));
		mv.addObject("pagination", paginationInfo);
		mv.setViewName("user/foundation");
		return mv;
	}

	@RequestMapping(value = "/categories")
	public ModelAndView categories(@RequestParam(name = "page", defaultValue = "1") String page,
			Authentication authentication, HttpSession session) {
		int pageSize = 9;
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(page);
		} catch (Exception e) {
			currentPage = 1;
		}
		int totalData = userService.getDataCategoriesActive().size();
		PaginationDto paginationInfo = paginationService.getDataPagination(totalData, currentPage, pageSize);
		ModelAndView mv = new ModelAndView();
		if (authentication != null) {
			Account user = accountServiceImpl.getDataAccountByEmail(authentication.getName());
			session.setAttribute("user", user);
		}
		mv.addObject("categoriesPagination",
				userService.getDataCategoriesPaginationActive(paginationInfo.getStart(), paginationInfo.getEnd()));
		mv.addObject("pagination", paginationInfo);
		mv.setViewName("user/categories");
		return mv;
	}

	@RequestMapping(value = "/admin")
	public String admin(Authentication authentication, HttpSession session) {
		if (authentication != null) {
			Account user = accountServiceImpl.getDataAccountByEmail(authentication.getName());
			session.setAttribute("user", user);
		}
		return "admin/home";
	}

	@RequestMapping(value = "/error_page")
	public String test() {
		return "user/403_page";
	}

	@GetMapping(value = "/register")
	public String register(@ModelAttribute("account") Account account) {

		return "user/register";
	}

	@PostMapping(value = "/registerAccount")
	public String registerAccount(@Valid @ModelAttribute("account") Account account, BindingResult bindingResult,
			Model model, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			if (!adminServiceImpl.isUniqueAccount(account.getEmail())) {
				FieldError error = new FieldError("account", "email", "Email already exists");
				bindingResult.addError(error);
			}
			model.addAttribute("email", account.getEmail());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "user/register";
		}
		if (!adminServiceImpl.isUniqueAccount(account.getEmail())) {
			FieldError error = new FieldError("account", "email", "Email already exists");
			bindingResult.addError(error);
			model.addAttribute("email", account.getEmail());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "user/register";
		}
		String newPassword = accountServiceImpl.newRandomPassword();
		String subject = "Account registration: " + account.getEmail();
		String text = "Your login password is: " + newPassword;
		//emailService.sendEmail("ff7a8c9a2c-0643d2@inbox.mailtrap.io", subject, text);

		//String password = passwordEncoder.encode(newPassword);
		account.setPassword(newPassword);
		account.setRole(2);
		account.setStatus("Active");
			
		adminServiceImpl.insertAccount(account);
		model.addAttribute("message", "Successful registration");
		redirectAttributes.addFlashAttribute("message", "Successful registration");
		redirectAttributes.addFlashAttribute("message", "Successful registration");
		return "user/login"	;
	}

	@RequestMapping(value = "/fund/{id}")
	public ModelAndView fundDetails(@PathVariable int id) {
		ModelAndView mv = new ModelAndView();
		Fund fund = adminServiceImpl.getDataFundById(id);
		fund.setCurrentAmount(userService.getCurrentMoneyByFund(fund.getId()));
		
		int foundationId = adminServiceImpl.getFoundationIdByName(fund.getFoundationName());
		Foundation foundation = adminServiceImpl.getDataFoundationById(foundationId);
		mv.addObject("fund", fund);
		mv.addObject("foundation", foundation);
		mv.setViewName("user/fund_detail");
		return mv;
	}

	@RequestMapping(value = "/categories/{id}")
	public ModelAndView categoriesDetails(@PathVariable int id) {
		ModelAndView mv = new ModelAndView();
		Categories categories = adminServiceImpl.getDataCategoriesById(id);
		List<Fund> funds = userService.getDataFundByCategoriesActive(id);
		
		for (Fund fund : funds) {
			fund.setCurrentAmount(userService.getCurrentMoneyByFund(fund.getId()));
		}
		
		mv.addObject("categories", categories);
		mv.addObject("funds", funds);
		mv.setViewName("user/categories_detail");
		return mv;
	}
	
	@RequestMapping(value = "/foundation/{id}")
	public ModelAndView foundationDetails(@PathVariable int id) {
		ModelAndView mv = new ModelAndView();
		Foundation foundation = adminServiceImpl.getDataFoundationById(id);
		List<Fund> funds = userService.getDataFundByFoundationActive(id);
		
		for (Fund fund : funds) {
			fund.setCurrentAmount(userService.getCurrentMoneyByFund(fund.getId()));
		}
		
		mv.addObject("foundation", foundation);
		mv.addObject("funds", funds);
		mv.setViewName("user/foundation_detail");
		return mv;
	}
	
	@RequestMapping(value = "/about_us")
	public String aboutUs() {
		return "user/about_us";
	}

}
