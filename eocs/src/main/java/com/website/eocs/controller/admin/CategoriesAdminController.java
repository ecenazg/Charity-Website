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

import com.website.eocs.entity.Categories;
import com.website.eocs.dto.PaginationDto;
import com.website.eocs.service.AdminServiceImpl;
import com.website.eocs.service.PaginationServiceImpl;


@Validated
@Controller
@RequestMapping(value = { "/admin" })
public class CategoriesAdminController {
	@Autowired
	AdminServiceImpl adminServiceImpl;
	@Autowired
	PaginationServiceImpl paginationService;

	
	// CATEGORIES
	@RequestMapping(value = "/categories_management")
	public ModelAndView categoriesManagement(@RequestParam(name = "page", defaultValue = "1") String page) {
		int pageSize = 6;
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(page);
		} catch (Exception e) {
			currentPage = 1;
		}
		int totalData = adminServiceImpl.getDataCategories().size();
		PaginationDto paginationInfo = paginationService.getDataPagination(totalData, currentPage, pageSize);
		ModelAndView mv = new ModelAndView();
		mv.addObject("categoriesPagination",
				adminServiceImpl.getDataCategoriesPagination(paginationInfo.getStart(), paginationInfo.getEnd()));
		mv.addObject("pagination", paginationInfo);
		mv.setViewName("admin/categories_management");
		return mv;
	}

	@RequestMapping(value = "/search_categories")
	public ModelAndView searchcategories(@RequestParam(name = "id", required = false) String id,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "page", defaultValue = "1") String page) {

		ModelAndView mv = new ModelAndView();
		int categoriesId;
		try {
			categoriesId = Integer.parseInt(id);
		} catch (Exception e) {
			categoriesId = -1;
		}
		int pageSize = 6;
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(page);
		} catch (Exception e) {
			currentPage = 1;
		}

		int totalData = adminServiceImpl.searchCategories(categoriesId, name).size();
		PaginationDto paginationInfo = paginationService.getDataPagination(totalData, currentPage, pageSize);
		mv.addObject("searchCategoriesPagination", adminServiceImpl.searchCategoriesPagination(categoriesId, name,
				paginationInfo.getStart(), paginationInfo.getEnd()));
		mv.addObject("pagination", paginationInfo);
		mv.setViewName("admin/search_categories");
		return mv;
	}

	@RequestMapping(value = "/categories_management/delete/{id}")
	public String deleteCategories(@PathVariable int id, HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		adminServiceImpl.deleteCategories(id);
		return "redirect:" + referer;
	}

	@RequestMapping(value = "/categories_management/undelete/{id}")
	public String unDeleteCategories(@PathVariable int id, HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		adminServiceImpl.unDeleteCategories(id);
		return "redirect:" + referer;
	}

	@RequestMapping(value = "/categories_management/delete_multiple")
	public String deleteMultipleCategories(@RequestParam(name = "ids") List<Integer> ids, HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		adminServiceImpl.deleteMultipleCategories(ids);
		return "redirect:" + referer;
	}

	@RequestMapping(value = "/categories_management/edit_categories/{id}")
	public ModelAndView editCategories(@PathVariable int id) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("categories", adminServiceImpl.getDataCategoriesById(id));
		mv.addObject("statusList", adminServiceImpl.statusList());
		mv.setViewName("admin/edit_categories");
		return mv;
	}
	

	@RequestMapping(value = "/update_categories", method = RequestMethod.POST)
	public String upDateCategories(@javax.validation.Valid @ModelAttribute("categories") Categories categories, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			if (!adminServiceImpl.isUniqueCategories(categories.getName(), categories.getId())) {
				FieldError error = new FieldError("categories", "name", "Tên đã tồn tại");
				bindingResult.addError(error);
			}

			model.addAttribute("name", categories.getName());
			model.addAttribute("statusList", adminServiceImpl.statusList());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "admin/edit_categories";
		}
		if (!adminServiceImpl.isUniqueCategories(categories.getName(), categories.getId())) {
			FieldError error = new FieldError("categories", "name", "Tên đã tồn tại");
			bindingResult.addError(error);
			model.addAttribute("name", categories.getName());
			model.addAttribute("statusList", adminServiceImpl.statusList());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "admin/edit_categories";
		}
		adminServiceImpl.updateCategories(categories);
		return "redirect: categories_management";
	}

	@RequestMapping(value = "/categories_management/add_categories")
	public ModelAndView addCategories() {
		Categories categories = new Categories();
		ModelAndView mv = new ModelAndView();
		mv.addObject("categories", categories);
		mv.addObject("statusList", adminServiceImpl.statusList());
		mv.addObject("categoryList", adminServiceImpl.getDataCategories());
		mv.addObject("foundationList", adminServiceImpl.getDataFoundation());
		mv.setViewName("admin/add_categories");
		return mv;
	}

	@RequestMapping(value = "/insert_categories", method = RequestMethod.POST)
	public String insertCategories(@javax.validation.Valid @ModelAttribute("categories") Categories categories, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			if (!adminServiceImpl.isUniqueCategories(categories.getName(), categories.getId())) {
				FieldError error = new FieldError("categories", "name", "Tên đã tồn tại");
				bindingResult.addError(error);
			}
			model.addAttribute("name", categories.getName());
			model.addAttribute("statusList", adminServiceImpl.statusList());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "admin/add_categories";
		}
		if (!adminServiceImpl.isUniqueCategories(categories.getName(), categories.getId())) {
			FieldError error = new FieldError("categories", "name", "Tên đã tồn tại");
			bindingResult.addError(error);
			model.addAttribute("name", categories.getName());
			model.addAttribute("statusList", adminServiceImpl.statusList());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "admin/add_categories";
		}
		adminServiceImpl.insertCategories(categories);
		return "redirect: categories_management";
	}

	

}
