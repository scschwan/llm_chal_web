package kr.co.dimillion.lcapp.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.dimillion.lcapp.application.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public String loginForm(Model model) {
        model.addAttribute("role", "");
        model.addAttribute("username", "");
        model.addAttribute("password", "");
        return "login/index";
    }

    @PostMapping
    public String login(@RequestParam String username, @RequestParam String password, @RequestParam("role") String roleStr, HttpServletRequest request) {
        Role role = Role.valueOf(roleStr.toUpperCase());
        if (Role.WORKER == role && username.equals("worker") && password.equals("worker")) {
            authenticateUser(username, role, request);
            return "redirect:/";
        }

        else if (Role.ADMIN == role && username.equals("admin") && password.equals("admin")) {
            authenticateUser(username, role, request);
            return "redirect:/admin";
        }

        else
            return "redirect:/login?error";
    }

    private void authenticateUser(String username, Role role, HttpServletRequest request) {
        var authority = new SimpleGrantedAuthority(role.getAuthority());
        var authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                List.of(authority)
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", context);
    }


}
