/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wizut.tpsi.ogloszenia;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.validation.Valid;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import wizut.tpsi.ogloszenia.component.UserSession;
import wizut.tpsi.ogloszenia.jpa.User;
import wizut.tpsi.ogloszenia.services.UserService;
import wizut.tpsi.ogloszenia.web.UserLoginForm;

/**
 *
 * @author Pioterk
 */
@Controller
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSession currentUser;

    @GetMapping("/loginForm")
    public String loginForm(UserLoginForm user) {
        return "loginForm";
    }

    @GetMapping("/login")
    public String login(Model model, @Valid UserLoginForm userForm, BindingResult binding) {
        if (binding.hasErrors()) {
            model.addAttribute("user", "Wypełnij formularz");
        } else {

            User user = userService.getUserByName(userForm.getUsername());

            if (user != null) {
                String hash = DigestUtils.sha256Hex(userForm.getPassword());
                
                if(hash.equals(user.getPassword())){
                    currentUser.setId(user.getId());
                    currentUser.setUsername(user.getUsername());
                    return "redirect:/";
                }
                model.addAttribute("user", "Niepoprawne dane");
            }
        }
        return "loginForm";
    }

    
    @GetMapping("/logout")
    public String logout(){
        currentUser.destroy();
        return "redirect:/";
    }
}
