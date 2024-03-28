package com.website.eocs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.website.eocs.dto.PaginationDto;
import com.website.eocs.entity.Account;
import com.website.eocs.entity.Donation;
import com.website.eocs.entity.Fund;
import com.website.eocs.service.AccountServiceImpl;
import com.website.eocs.service.AdminServiceImpl;
import com.website.eocs.service.EmailService;
import com.website.eocs.service.PaginationServiceImpl;
import com.website.eocs.service.UserService;

@Validated
@Controller
public class UserController {
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

	
	@RequestMapping(value = "/user")
	public ModelAndView user(Authentication authentication, @RequestParam(name = "page", defaultValue = "1") String page) {
		Account user = accountServiceImpl.getDataAccountByEmail(authentication.getName());
		ModelAndView mv = new ModelAndView();
		
		int pageSize = 4;
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(page);
		} catch (Exception e) {
			currentPage = 1;
		}
		int totalData = userService.searchDonationByAccount(user.getUsername()).size();
		PaginationDto paginationInfo = paginationService.getDataPagination(totalData, currentPage, pageSize);
	
		mv.addObject("donationsPagination",
				userService.searchDonationByAccountPagination(user.getUsername() ,paginationInfo.getStart(), paginationInfo.getEnd()));
		mv.addObject("pagination", paginationInfo);
		mv.addObject("user", user);
		mv.setViewName("user/user");
		return mv;
	}

	@RequestMapping(value = "/user/change_password")
	public ModelAndView changePassword(Authentication authentication) {
		Account user = accountServiceImpl.getDataAccountByEmail(authentication.getName());
		ModelAndView mv = new ModelAndView();
		mv.addObject("user", user);

		mv.setViewName("user/change_password");
		return mv;
	}

	@RequestMapping(value = "/user/updatepassword")
	public String updatePasswordUser(@RequestParam(value = "password") String password,
			@RequestParam(value = "newPassword") String newPassword,@RequestParam(value = "confirmPassword") String confirmPassword,
			Authentication authentication, Model model,
			RedirectAttributes redirectAttributes) {
		Account user = accountServiceImpl.getDataAccountByEmail(authentication.getName());
		if (password.equalsIgnoreCase(user.getPassword())) {
			model.addAttribute("wrongPassword", "Wrong password");
			redirectAttributes.addFlashAttribute("wrongPassword", "Wrong password");
			return "redirect:change_password";
		}

		if (newPassword.length() < 8) {
			model.addAttribute("invalidPassword", "Password has a minimum length of 8 characters");
			redirectAttributes.addFlashAttribute("invalidPassword", "Password has a minimum length of 8 characters");
			return "redirect:change_password";
		}
		
		if(!newPassword.equals(confirmPassword) ) {
			model.addAttribute("confirmFail", "Incorrect confirmation password, please enter the correct new password to confirm");
			redirectAttributes.addFlashAttribute("confirmFail", "Incorrect confirmation password, please enter the correct new password to confirm");
			return "redirect:change_password";
		}
		model.addAttribute("message", "Change Password successfully");
		redirectAttributes.addFlashAttribute("message", "Change Password successfully");
		//String updatepassword = passwordEncoder.encode(newPassword);
		accountServiceImpl.updateAccountPassword(user.getId(), newPassword);
		return "redirect:/home";
	}

	@RequestMapping(value = "/forget_password")
	public String forgetPassword() {
		return "user/forget_password";
	}

	@RequestMapping(value = "/update_forget_password")
	public String updateForgetPassword(@RequestParam(value = "email") String email, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Account account = accountServiceImpl.getDataAccountByEmail(email);
			String newPassword = accountServiceImpl.newRandomPassword();

			String subject = "New account password: " + account.getEmail();
			String text = "Your new login password is: " + newPassword;
			//emailService.sendEmail("ff7a8c9a2c-0643d2@inbox.mailtrap.io", subject, text);
			//String password = passwordEncoder.encode(newPassword);
			accountServiceImpl.updateAccountPassword(account.getId(), newPassword);
			//model.addAttribute("message", "Mật khẩu mới đã được gửi tới email của bạn");
			//redirectAttributes.addFlashAttribute("message", "Mật khẩu mới đã được gửi tới email của bạn");
			return "user/login";
		} catch (EmptyResultDataAccessException e) {
			return "redirect:forget_password?error";
		}
	}

	@PostMapping(value = "/user/donation")
	public String createDonation(HttpServletRequest request, @RequestParam(value = "amount") Integer amount,
			@RequestParam(value = "message") String message, @RequestParam(value = "fundId") int id, Model model,
			RedirectAttributes redirectAttributes, Authentication authentication) {
		String referer = request.getHeader("Referer");
		Donation donation = new Donation();
		if(amount == null || amount == 0 ) {
			model.addAttribute("message", "Unsuccessful donation, the amount must not be left empty and must be greater than 0");
			redirectAttributes.addFlashAttribute("message", "Unsuccessful donation, the amount must not be left empty and must be greater than 0");
			return "redirect:" + referer;
		}
		donation.setAmount(amount);
		donation.setMessage(message);
		int fundId = id;		
		Fund fund = adminServiceImpl.getDataFundById(fundId);
		if(fund.getStatus().equals("Finish")) {
			model.addAttribute("message", "The donation fund has been completed. Thank you to the philanthropists who have accompanied you");
			redirectAttributes.addFlashAttribute("message", "The donation fund has been completed. Thank you to the philanthropists who have accompanied you");
			return "redirect:" + referer;
		}
		Account account = accountServiceImpl.getDataAccountByEmail(authentication.getName());
		int accountId = account.getId();
		
		userService.createDonation(donation, accountId, fundId);
		int currentAmount = userService.getCurrentMoneyByFund(fundId);
		if(currentAmount >= fund.getExpectedAmount()) {
			adminServiceImpl.finishFund(fundId);
		}
		model.addAttribute("message", "Thank you for contributing with us for the difficult circumstances");
		redirectAttributes.addFlashAttribute("message", "Thank you for contributing with us for the difficult circumstances");
		
		return "redirect:" + referer;
	}
	
	@PostMapping(value = "/user/update_user")
	public String updateUser(@ModelAttribute(value = "user") Account user, BindingResult bindingResult, RedirectAttributes redirectAttributes,
			HttpServletRequest request, Model model, Authentication authentication) {
		String referer = request.getHeader("Referer");
		
		if(user.getUsername().length() < 5) {
			model.addAttribute("error", "Name and surname with a minimum length of 5 characters");
			redirectAttributes.addFlashAttribute("error", "Name and surname with a minimum length of 5 characters");
			return "redirect:" + referer;
		}
		Account account = accountServiceImpl.getDataAccountByEmail(authentication.getName());
		account.setUsername(user.getUsername());
		account.setPhone(user.getPhone());
		account.setAddress(user.getAddress());
		userService.updateUser(account);
		return "redirect:" + referer;
	}
}
