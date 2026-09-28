package com.smartcomplaint.smartcomplaintsystem.controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.smartcomplaint.smartcomplaintsystem.model.User;
import com.smartcomplaint.smartcomplaintsystem.repository.UserRepository;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password) {

        User user = new User();

        user.setFullName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);

        userRepository.save(user);

        return "redirect:/index.html";
    }
    @PostMapping("/login")
public String loginUser(
        @RequestParam String email,
        @RequestParam String password,
        HttpSession session) {

    User user = userRepository.findByEmail(email);

    if (user != null && user.getPassword().equals(password)) {

        session.setAttribute("userId", user.getId());
        session.setAttribute("useremail",user.getemail());

        return "redirect:/dashboard.html";
    }

    return "redirect:/login.html?error=true";
}
@GetMapping("/api/profile")
@ResponseBody
public java.util.Map<String, String> getProfile(HttpSession session) {

    Long userId = (Long) session.getAttribute("userId");

    if (userId == null) {
        return null;
    }

    User user = userRepository.findById(userId).orElse(null);

    if (user == null) {
        return null;
    }

    java.util.Map<String, String> profile = new java.util.HashMap<>();

    profile.put("fullName", user.getFullName());
    profile.put("email", user.getemail());
    profile.put("phone", user.getPhone());

    return profile;
}
@PostMapping("/admin-login")
public String adminLogin(
        @RequestParam String email,
        @RequestParam String password,
        HttpSession session) {

    if (email.equals("admin@civicfix.com")
            && password.equals("Admin@123")) {

        session.setAttribute("adminLoggedIn", true);

        return "redirect:/dashboard.html";
    }

    return "redirect:/admin-login.html?error=true";
}
@GetMapping("/admin")
public String openAdminPanel(HttpSession session) {

    Boolean adminLoggedIn =
            (Boolean) session.getAttribute("adminLoggedIn");

    if (Boolean.TRUE.equals(adminLoggedIn)) {
        return "redirect:/admin.html";
    }

    return "redirect:/admin-login.html";
}


}
