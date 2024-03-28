package com.website.eocs.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.website.eocs.entity.Foundation;
import com.website.eocs.dto.PaginationDto;
import com.website.eocs.service.AdminServiceImpl;
import com.website.eocs.service.PaginationServiceImpl;

@Validated
@Controller
@RequestMapping(value = { "/admin" })
public class FoundationAdminController {
	@Autowired
	AdminServiceImpl adminServiceImpl;
	@Autowired
	PaginationServiceImpl paginationService;

	// FOUNDATION
	@RequestMapping(value = "/foundation_management")
	public ModelAndView foundationManagement(@RequestParam(name = "page", defaultValue = "1") String page) {
		int pageSize = 6;
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(page);
		} catch (Exception e) {
			currentPage = 1;
		}
		int totalData = adminServiceImpl.getDataFoundation().size();
		PaginationDto paginationInfo = paginationService.getDataPagination(totalData, currentPage, pageSize);
		ModelAndView mv = new ModelAndView();
		mv.addObject("foundationPagination",
				adminServiceImpl.getDataFoundationPagination(paginationInfo.getStart(), paginationInfo.getEnd()));
		mv.addObject("pagination", paginationInfo);
		mv.setViewName("admin/foundation_management");
		return mv;
	}

	@RequestMapping(value = "/search_foundation")
	public ModelAndView searchFoundation(@RequestParam(name = "id", required = false) String id,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "page", defaultValue = "1") String page) {

		ModelAndView mv = new ModelAndView();
		int foundationId;
		try {
			foundationId = Integer.parseInt(id);
		} catch (Exception e) {
			foundationId = -1;
		}
		int pageSize = 6;
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(page);
		} catch (Exception e) {
			currentPage = 1;
		}

		int totalData = adminServiceImpl.searchFoundation(foundationId, name).size();
		PaginationDto paginationInfo = paginationService.getDataPagination(totalData, currentPage, pageSize);
		mv.addObject("searchFoundationPagination", adminServiceImpl.searchFoundationPagination(foundationId, name,
				paginationInfo.getStart(), paginationInfo.getEnd()));
		mv.addObject("pagination", paginationInfo);
		mv.setViewName("admin/search_foundation");
		return mv;
	}

	@RequestMapping(value = "/foundation_management/delete/{id}")
	public String deleteFoundation(@PathVariable int id, HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		adminServiceImpl.deleteFoundation(id);
		return "redirect:" + referer;
	}

	@RequestMapping(value = "/foundation_management/undelete/{id}")
	public String unDeleteFoundation(@PathVariable int id, HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		adminServiceImpl.unDeleteFoundation(id);
		return "redirect:" + referer;
	}

	@RequestMapping(value = "/foundation_management/delete_multiple")
	public String deleteMultipleFoundation(@RequestParam(name = "ids") List<Integer> ids, HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		adminServiceImpl.deleteMultipleFoundation(ids);
		return "redirect:" + referer;
	}

	@RequestMapping(value = "/foundation_management/edit_foundation/{id}")
	public ModelAndView editFoundations(@PathVariable int id) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("foundation", adminServiceImpl.getDataFoundationById(id));
		mv.addObject("statusList", adminServiceImpl.statusList());
		mv.setViewName("admin/edit_foundation");
		return mv;
	}
	

	@RequestMapping(value = "/update_foundation", method = RequestMethod.POST)
	public String upDateFoundation(@javax.validation.Valid @ModelAttribute("foundation") Foundation foundation, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			if (!adminServiceImpl.isUniqueFoundation(foundation.getName(), foundation.getId())) {
				FieldError error = new FieldError("foundation", "name", "Tên đã tồn tại");
				bindingResult.addError(error);
			}

			model.addAttribute("name", foundation.getName());
			model.addAttribute("statusList", adminServiceImpl.statusList());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "admin/edit_foundation";
		}
		if (!adminServiceImpl.isUniqueFoundation(foundation.getName(), foundation.getId())) {
			FieldError error = new FieldError("foundation", "name", "Tên đã tồn tại");
			bindingResult.addError(error);
			model.addAttribute("name", foundation.getName());
			model.addAttribute("statusList", adminServiceImpl.statusList());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "admin/edit_foundation";
		}
		adminServiceImpl.updateFoundation(foundation);
		return "redirect: foundation_management";
	}

	@RequestMapping(value = "/foundation_management/add_foundation")
	public ModelAndView addFoundations() {
		Foundation foundation = new Foundation();
		ModelAndView mv = new ModelAndView();
		mv.addObject("foundation", foundation);
		mv.addObject("statusList", adminServiceImpl.statusList());
		mv.setViewName("admin/add_foundation");
		return mv;
	}

	@RequestMapping(value = "/insert_foundation", method = RequestMethod.POST)
	public String insertFoundation(@javax.validation.Valid @ModelAttribute("foundation") Foundation foundation, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			if (!adminServiceImpl.isUniqueFoundation(foundation.getName(), foundation.getId())) {
				FieldError error = new FieldError("foundation", "name", "Tên đã tồn tại");
				bindingResult.addError(error);
			}
			model.addAttribute("name", foundation.getName());
			model.addAttribute("statusList", adminServiceImpl.statusList());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "admin/add_foundation";
		}
		if (!adminServiceImpl.isUniqueFoundation(foundation.getName(), foundation.getId())) {
			FieldError error = new FieldError("foundation", "name", "Tên đã tồn tại");
			bindingResult.addError(error);
			model.addAttribute("name", foundation.getName());
			model.addAttribute("statusList", adminServiceImpl.statusList());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "admin/add_foundation";
		}
		
		adminServiceImpl.insertFoundation(foundation);
		return "redirect: foundation_management";
	}


}
