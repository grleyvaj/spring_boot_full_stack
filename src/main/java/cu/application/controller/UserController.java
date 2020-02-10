package cu.application.controller;

import cu.application.model.User;
import cu.application.service.IRoleService;
import cu.application.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    IUserService userService;
    @Autowired
    IRoleService roleService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/userForm")
    public String getUserForm(Model model){
        model.addAttribute("userForm", new User());
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("roles",roleService.getAllRole());
        model.addAttribute("listTab", "active");
        return "user-form/user-view";
    }

    @PostMapping("/userForm")
    public String createUser(@Validated @ModelAttribute("userForm")User user, BindingResult result, ModelMap model){
        if (result.hasErrors()){
            model.addAttribute("userForm", user);
            model.addAttribute("formTab", "active");
        }
        else {
            try {
                userService.createUser(user);
                model.addAttribute("userForm", new User());
                model.addAttribute("listTab", "active");
            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("userForm", user);
                model.addAttribute("formTab", "active");
                model.addAttribute("userList", userService.getAllUsers());
                model.addAttribute("roles",roleService.getAllRole());
            }
        }
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("roles",roleService.getAllRole());
        return "user-form/user-view";
    }
}
