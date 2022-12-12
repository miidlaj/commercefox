package com.ecommerce.admin.user.controller;

import com.ecommerce.admin.AmazonS3Util;
import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.admin.paging.PagingAndSortingArgumentResolver;
import com.ecommerce.admin.paging.PagingAndSortingHelper;
import com.ecommerce.admin.paging.PagingAndSortingParam;
import com.ecommerce.admin.user.*;
import com.ecommerce.admin.user.export.UserCsvExporter;
import com.ecommerce.admin.user.export.UserExcelExporter;
import com.ecommerce.admin.user.export.UserPdfExporter;
import com.ecommerce.common.entity.Role;
import com.ecommerce.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/users")
    public String listFirstPage(){
        return "redirect:/users/page/1?sortField=firstName&sortDir=asc";
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "listUsers", moduleURL = "/users") PagingAndSortingHelper helper,
                             @PathVariable(name = "pageNum")int pageNum
                             ){
        userService.listByPage(pageNum,helper);

        return "users/users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model){
        List<Role> listRoles = userService.listRoles();

        User user  = new User();
        user.setEnabled(true);

        model.addAttribute("user",user);
        model.addAttribute("listRoles",listRoles);
        model.addAttribute("pageTitle","New User");



        return "users/user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user,
                           RedirectAttributes redirectAttributes,
                           @RequestParam("image")MultipartFile multipartFile
                           ) throws IOException {
        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.save(user);
            String uploadDir = "user-photos/"+savedUser.getId();

            AmazonS3Util.removeFolder(uploadDir);
            AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
        }else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.save(user);
        }

        redirectAttributes.addFlashAttribute("message","The user has saved Successfully.");

        return getRedirectURLtoAffectedUser(user);
    }
    
    private String getRedirectURLtoAffectedUser(User user) {
    	String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id,
                           RedirectAttributes redirectAttributes,
                           Model model
    ){
        try {
            User user = userService.get(id);
            List<Role> listRoles = userService.listRoles();

            model.addAttribute("user",user);
            model.addAttribute("listRoles",listRoles);
            model.addAttribute("pageTitle","Edit User (ID: "+id+" )");

            return "users/user_form";
        }catch (UserNotFoundException e){
            redirectAttributes.addFlashAttribute("message",e.getMessage());

            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id,
                             RedirectAttributes redirectAttributes
                             ){
        try {
            userService.delete(id);
            String userPhotosDir = "user-photos/" + id;
            AmazonS3Util.removeFolder(userPhotosDir);

            redirectAttributes.addFlashAttribute("message","The user with ID " + id + " deleted Successfully." );
            return "redirect:/users";
        }catch (UserNotFoundException e){
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") boolean enabled,
                                          RedirectAttributes redirectAttributes
                                          ){
        userService.updateUserEnabledStatus(id,enabled);
        String status = enabled ? "Enabled" : "Disabled";
        String message = "The user Id " + id + " has been " + status;



        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/users";
    }


    @GetMapping("/users/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.listAll();
    	UserCsvExporter exporter = new UserCsvExporter();
        exporter.export(listUsers,response);
    	
    }

    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.listAll();
        UserExcelExporter exporter = new UserExcelExporter();
        exporter.export(listUsers,response);
    }

    @GetMapping("/users/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.listAll();
        UserPdfExporter exporter = new UserPdfExporter();
        exporter.export(listUsers,response);
    }



}
