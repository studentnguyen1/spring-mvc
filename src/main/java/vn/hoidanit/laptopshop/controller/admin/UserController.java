package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {
    // // DI: Dependency Injection
    private final UserService userService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UploadService uploadService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;

    }

    @RequestMapping("/")

    public String getHomePage(Model model) {
        List<User> arrayUser = this.userService.getAllUserByEmail("nguyendeptrai123@gmail.com");
        String controlService = "Hello from Controller";
        model.addAttribute("test", "test");
        model.addAttribute("controller", controlService);
        return "hello";
    }

    @RequestMapping("/admin/user")

    public String getUser(Model model) {
        model.addAttribute("newUser", new User());
        List<User> users = this.userService.getAllUser();
        model.addAttribute("users1", users);

        return "admin/user/show";
    }

    @RequestMapping("/admin/user/{id}")

    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("id", id);
        model.addAttribute("user", user);
        return "admin/user/detail";

    }

    @RequestMapping("/admin/user/update/{id}")

    public String updateUser(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("newUser", currentUser);
        return "admin/user/update";

    }

    @PostMapping("/admin/user/update")

    public String postUpdateUser(Model model, @ModelAttribute("newUser") User hoidanit) {
        User currentUser = this.userService.getUserById((hoidanit.getId()));
        if (currentUser != null) {
            currentUser.setAddress(hoidanit.getAddress());
            currentUser.setFullName(hoidanit.getFullName());
            currentUser.setPhone(hoidanit.getPhone());
            this.userService.handleSaveUser(currentUser);
        }
        return "redirect:/admin/user";

    }

    @RequestMapping("/admin/user/delete/{id}")

    public String getDeleteUserPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newUser", new User());
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete")

    public String postDeleteUser(Model model, @ModelAttribute("newUser") User eric) {
        this.userService.deleteById((eric.getId()));
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/create")

    public String getCreateUser(Model model) {

        model.addAttribute("newUser", new User());
        return "admin/user/create";

    }

    @PostMapping(value = "/admin/user/create")

    public String createUserPage(Model model, @ModelAttribute("newUser") @Valid User hoidanit,
            BindingResult newUserbindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {

        List<FieldError> errors = newUserbindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getField() + " - " + error.getDefaultMessage());
        }

        // Validate

        if (newUserbindingResult.hasErrors()) {
            return "/admin/user/create";
        }

        String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(hoidanit.getPassword());
        hoidanit.setAvatar(avatar);
        hoidanit.setPassword(hashPassword);
        hoidanit.setRole(this.userService.getRoleByName(hoidanit.getRole().getName()));
        this.userService.handleSaveUser(hoidanit);

        return "redirect:/admin/user";

    }

}
