package cu.application.controller;

import cu.application.dto.ChangePasswordForm;
import cu.application.model.User;
import cu.application.service.IRoleService;
import cu.application.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    IUserService userService;
    @Autowired
    IRoleService roleService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/userForm")
    public String userForm(Model model) {
        model.addAttribute("userForm", new User());
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRole());
        model.addAttribute("listTab", "active");
        return "user-form/user-view";
    }

    @PostMapping("/userForm")
    public String createUser(@Validated @ModelAttribute("userForm") User user, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("userForm", user);
            model.addAttribute("formTab", "active");
        } else {
            try {
                userService.createUser(user);
                //refrescar y dejar como estaba antes
                model.addAttribute("userForm", new User());
                model.addAttribute("listTab", "active");
            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("userForm", user);
                model.addAttribute("formTab", "active");
                model.addAttribute("userList", userService.getAllUsers());
                model.addAttribute("roles", roleService.getAllRole());
            }
        }
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRole());
        return "user-form/user-view";
    }

    @GetMapping("/editUser/{id}")
    public String getEditUserForm(Model model, @PathVariable(name = "id") Long id) throws Exception {
        User userToEdit = userService.getUserById(id);

        model.addAttribute("userForm", userToEdit);
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRole());
        model.addAttribute("formTab", "active");
        //comportamiento de esta página, de sus botones, etc
        model.addAttribute("editMode", "true");
        //para cambiar contraseña
        model.addAttribute("passwordForm", new ChangePasswordForm(id));
        return "user-form/user-view";
    }

    @PostMapping("/editUser")
    public String postEditUserForm(@Validated @ModelAttribute("userForm") User user, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("userForm", user);
            model.addAttribute("formTab", "active");
            model.addAttribute("editMode", "true");
            //para cambiar contraseña
            model.addAttribute("passwordForm", new ChangePasswordForm(user.getId()));
        } else {
            try {
                userService.updateUser(user);
                //refrescar y dejar como estaba antes
                model.addAttribute("userForm", new User());
                model.addAttribute("listTab", "active");
//                se comentarea porque por defecto es falso, se pone en falso porque si está bien debe regresar al list
//                model.addAttribute("editMode", "falso");
            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("userForm", user);
                model.addAttribute("formTab", "active");
                model.addAttribute("userList", userService.getAllUsers());
                model.addAttribute("roles", roleService.getAllRole());
//                si se lanza una exception, se debe quedar en la misma pantalla con los campos del user llenos
                model.addAttribute("editMode", "true");
                //para cambiar contraseña
                model.addAttribute("passwordForm", new ChangePasswordForm(user.getId()));

            }
        }
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRole());
        return "user-form/user-view";
    }

    @GetMapping("/userForm/cancel")
    public String cancelEditUser(ModelMap map) {
        return "redirect:/userForm";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(Model model, @PathVariable(name = "id") Long id){
        try {
            userService.deleteUser(id);
        } catch (Exception e) {
            model.addAttribute("listErrorMessage", e.getMessage());
        }
        return userForm(model);
    }

    @PostMapping("/editUser/changePassword")
    public ResponseEntity postEditUseChangePassword(@Valid @RequestBody ChangePasswordForm form, Errors errors){
        //se hace de esta forma y no como antes a través dell HTML porque no se va a recargar la página
        try {
            //If error, just return a 400 bad request, along with the error message
            if (errors.hasErrors()) {
                String result = errors.getAllErrors()
                        .stream().map(x -> x.getDefaultMessage())
                        .collect(Collectors.joining(""));

                throw new Exception(result);
            }
            userService.changePassword(form);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("success");
    }

}
