package com.website.eocs.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.website.eocs.dto.PaginationDto;
import com.website.eocs.entity.Account;
import com.website.eocs.service.AccountServiceImpl;
import com.website.eocs.service.AdminServiceImpl;
import com.website.eocs.service.EmailService;
import com.website.eocs.service.PaginationServiceImpl;


@Validated
@Controller
@RequestMapping(value = { "/admin" })
public class AccountAdminController {
	@Autowired
	AdminServiceImpl adminServiceImpl;
	@Autowired
	PaginationServiceImpl paginationService;
	@Autowired
	AccountServiceImpl accountServiceImpl;
	@Autowired
	EmailService emailService;
	// ACCOUNT
	@RequestMapping(value = "/account_management")
	public ModelAndView accountManagement(@RequestParam(name = "page", defaultValue = "1") String page) {
		int pageSize = 6;
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(page);
		} catch (Exception e) {
			currentPage = 1;
		}
		int totalData = adminServiceImpl.getDataAccount().size();
		PaginationDto paginationInfo = paginationService.getDataPagination(totalData, currentPage, pageSize);
		ModelAndView mv = new ModelAndView();
		mv.addObject("accountsPagination",
				adminServiceImpl.getDataAccountPagination(paginationInfo.getStart(), paginationInfo.getEnd()));
		mv.addObject("pagination", paginationInfo);
		mv.setViewName("admin/account_management");
		return mv;
	}

	@RequestMapping(value = "/search_account")
	public ModelAndView searchAccount(@RequestParam(name = "username", required = false) String username,
			@RequestParam(name = "phone", required = false) String phone,
			@RequestParam(name = "page", defaultValue = "1") String page) {

		ModelAndView mv = new ModelAndView();

		int pageSize = 6;
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(page);
		} catch (Exception e) {
			currentPage = 1;
		}

		int totalData = adminServiceImpl.searchAccount(username, phone).size();
		PaginationDto paginationInfo = paginationService.getDataPagination(totalData, currentPage, pageSize);
		mv.addObject("searchAccountPagination", adminServiceImpl.searchAccountPagination(username, phone,
				paginationInfo.getStart(), paginationInfo.getEnd()));
		mv.addObject("pagination", paginationInfo);
		mv.setViewName("admin/search_account");
		return mv;
	}

	@RequestMapping(value = "/account_management/delete/{id}")
	public String deleteAccount(@PathVariable int id, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		String referer = request.getHeader("Referer");
		if (adminServiceImpl.getDataAccountById(id).getRole() == 1) {
			model.addAttribute("deleteMessage", "The administrator account cannot be deleted");
			redirectAttributes.addFlashAttribute("deleteMessage", "Cannot delete Admin account");
			return "redirect:" + referer;
		}
		adminServiceImpl.deleteAccount(id);
		model.addAttribute("deleteMessage", "The element was successfully deleted");
		redirectAttributes.addFlashAttribute("deleteMessage", "The element was successfully deleted");
		return "redirect:" + referer;
	}

	@RequestMapping(value = "/account_management/undelete/{id}")
	public String unDeleteAccount(@PathVariable int id, HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		adminServiceImpl.unDeleteAccount(id);
		return "redirect:" + referer;
	}

	@RequestMapping(value = "/account_management/delete_multiple")
	public String deleteMultipleAccount(@RequestParam(name = "ids") List<Integer> ids, HttpServletRequest request,
			Model model, RedirectAttributes redirectAttributes) {

		String referer = request.getHeader("Referer");
		for (int id : ids) {
			if (adminServiceImpl.getDataAccountById(id).getRole() == 1) {
				model.addAttribute("deleteMessage", "Cannot delete Admin account");
				redirectAttributes.addFlashAttribute("deleteMessage", "Cannot delete Admin account");
				return "redirect:" + referer;
			}
		}

		adminServiceImpl.deleteMultipleAccount(ids);
		model.addAttribute("deleteMessage", "The element was successfully deleted");
		redirectAttributes.addFlashAttribute("deleteMessage", "The element was successfully deleted");
		return "redirect:" + referer;
	}

	@RequestMapping(value = "/account_management/edit_account/{id}")
	public ModelAndView editAccounts(@PathVariable int id) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("account", adminServiceImpl.getDataAccountById(id));
		mv.addObject("statusList", adminServiceImpl.statusList());
		mv.setViewName("admin/edit_account");
		return mv;
	}

	@RequestMapping(value = "/account_management/update_password/{id}")
	public String updateAccountPassword(@PathVariable int id, HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		
		Account account = adminServiceImpl.getDataAccountById(id);
		
		String newPassword = accountServiceImpl.newRandomPassword();
		String subject = "Update account password: " + account.getEmail();
		String text = "Your new password is: " + newPassword;
		//emailService.sendEmail("ff7a8c9a2c-0643d2@inbox.mailtrap.io", subject, text);
		//String password = passwordEncoder.encode(newPassword) ;
		
		accountServiceImpl.updateAccountPassword(id, newPassword);
		return "redirect:" + referer;
	}

	@RequestMapping(value = "/update_account", method = RequestMethod.POST)
	public String upDateAccount(@javax.validation.Valid @ModelAttribute("account") Account account,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			if (!adminServiceImpl.isUniqueAccount(account.getEmail(), account.getId())) {
				FieldError error = new FieldError("account", "email", "Email already exists");
				bindingResult.addError(error);
			}

			model.addAttribute("email", account.getEmail());
			model.addAttribute("statusList", adminServiceImpl.statusList());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "admin/edit_account";
		}
		if (!adminServiceImpl.isUniqueAccount(account.getEmail(), account.getId())) {
			FieldError error = new FieldError("account", "email", "Email already exists");
			bindingResult.addError(error);
			model.addAttribute("email", account.getEmail());
			model.addAttribute("statusList", adminServiceImpl.statusList());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "admin/edit_account";
		}
		Account checkAccount = adminServiceImpl.getDataAccountById(account.getId());
		if(checkAccount.getRole()==1) {
			account.setRole(1);
			account.setStatus("Active");
		}
		
		adminServiceImpl.updateAccount(account);
		return "redirect: account_management";
	}

	@RequestMapping(value = "/account_management/add_account")
	public ModelAndView addAccounts() {
		Account account = new Account();
		ModelAndView mv = new ModelAndView();
		mv.addObject("account", account);
		mv.addObject("statusList", adminServiceImpl.statusList());
		mv.setViewName("admin/add_account");
		return mv;
	}

	@RequestMapping(value = "/insert_account", method = RequestMethod.POST)
	public String insertAccount(@javax.validation.Valid @ModelAttribute("account") Account account,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			if (!adminServiceImpl.isUniqueAccount(account.getEmail(), account.getId())) {
				FieldError error = new FieldError("account", "email", "Email already exists");
				bindingResult.addError(error);
			}
			model.addAttribute("email", account.getEmail());
			model.addAttribute("statusList", adminServiceImpl.statusList());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "admin/add_account";
		}
		if (!adminServiceImpl.isUniqueAccount(account.getEmail(), account.getId())) {
			FieldError error = new FieldError("account", "email", "Email already exists");
			bindingResult.addError(error);
			model.addAttribute("email", account.getEmail());
			model.addAttribute("statusList", adminServiceImpl.statusList());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "admin/add_account";
		}
		account.setPassword(account.getPassword());
		adminServiceImpl.insertAccount(account);
		return "redirect: account_management";
	}

}
