package knowledgehunters.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import knowledgehunters.service.UserService;

@Controller
@SpringBootApplication
public class MainController {
	
	@Autowired
	UserService userService;
	 
	@RequestMapping("/")
     public String index(ModelMap map) {
        System.out.println("Looking in the index controller.........");

         return "index.html";
     }
	
	@RequestMapping("/login")
    public String login(ModelMap map) {
       System.out.println("Looking in the login controller.........");

        return "login.html";
    }
	
	@RequestMapping("/register")
    public String register(ModelMap map) {
       System.out.println("Looking in the register controller.........");

        return "register.html";
    }
	
	@RequestMapping("/welcome")
    public String welcome(ModelMap map) {
       System.out.println("Looking in the welcome controller.........");

        return "welcome.html";
    }
	
//	
//	@RequestMapping(value = "users-list")
//	    public ModelAndView messages() {
//	        ModelAndView mav = new ModelAndView("users-list.html");
//	        mav.addObject("users", userService.getAllUsers());
//	        return mav;
//	}
	
	@GetMapping("/users-list")
	public String listStudent(Model model) {
	    model.addAttribute("users", userService.getAllUsers());
	    return "users-list.html";
	}
	
//	@RequestMapping("/users-list")
//    public String usersList(ModelMap map) {
//       System.out.println("Looking in the users-list controller.........");
//       
//
//        return "users-list.html";
//    }
}
