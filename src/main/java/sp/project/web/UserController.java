package sp.project.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sp.project.model.SignupForm;
import sp.project.model.User;
import sp.project.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class UserController {
	
	@Autowired
    private UserRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//NEW USERS - SIGNUP SYSTEM
	
    @RequestMapping(value = "signup")
    public String addStudent(Model model){
    	model.addAttribute("signupform", new SignupForm());
        return "signup";
    }	
    
    @RequestMapping(value = "saveuser", method = RequestMethod.POST)
    public String saveUser(@Valid @ModelAttribute("signupform") SignupForm signupForm, BindingResult bindingResult, HttpServletRequest request) {
    	if (!bindingResult.hasErrors()) { 									// validation errors
    		if (signupForm.getPassword().equals(signupForm.getPasswordCheck())) { // checks password match		
	    		String pwd = signupForm.getPassword();
		    	BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		    	String hashPwd = bc.encode(pwd);
	
		    	User newUser = new User();
		    	newUser.setPasswordHash(hashPwd);
		    	newUser.setUsername(signupForm.getUsername());
		    	newUser.setRole("USER");
		    	if (repository.findByUsername(signupForm.getUsername()) == null) { // checks if user exists
		    		repository.save(newUser);
		    		try {
		    	        request.login(signupForm.getUsername(), pwd);		//automatically logs the new user in with the new credentials
		    	    } catch (ServletException e) {
		    	    }
		    	}
		    	else {
	    			bindingResult.rejectValue("username", "err.username", "Username already exists");    	
	    			return "signup";		    		
		    	}
    		}
    		else {
    			bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords does not match");    	
    			return "signup";
    		}
    	}
    	else {
    		return "signup";
    	}
    	return "redirect:/index";    	
    }
    
    //USER MANAGEMENT
    
    @PreAuthorize("hasAuthority('ADMIN')")		
	@RequestMapping("/userlist")											//default user management page
	public String userList(Model model) {
		model.addAttribute("users", repository.findAll());
		return "userlist";
	}
	
	@PreAuthorize("authentication.principal.username == 'admin'")			//only the admin login can enact changes in user management
	@RequestMapping(value="/promote/{id}", method=RequestMethod.POST)		//promotes a user to admin rights
	public String promote(@PathVariable("id") Long userId){
		User modifiedUser = repository.findById(userId).get();
		modifiedUser.setRole("ADMIN");
		repository.save(modifiedUser);
		return "redirect:/userlist";
	}
	
	@PreAuthorize("authentication.principal.username == 'admin'")
	@RequestMapping(value="/demote/{id}", method=RequestMethod.POST)		//demotes an admin to simple user rights
	public String demote(@PathVariable("id") Long userId){
		User modifiedUser = repository.findById(userId).get();
		modifiedUser.setRole("USER");
		repository.save(modifiedUser);
		return "redirect:/userlist";
	}
	
	@PreAuthorize("authentication.principal.username == 'admin'")
	@RequestMapping(value="/deleteuser/{id}")								//deletes the user
	public String deleteUser(@PathVariable("id") Long userId){
		repository.deleteById(userId);
		return "redirect:../userlist";
	}
	
	@PreAuthorize("authentication.principal.username == 'admin'")
	@RequestMapping(value="/adduser")										//admin creates manually a new user who may have admin rights from the start
	public String addUser(Model model) {
		model.addAttribute("user", new User());
		return "adduser";
	}
	
	@PreAuthorize("authentication.principal.username == 'admin'")
	@RequestMapping(value="/saveadmin", method=RequestMethod.POST)			//saves the user created by the admin
	public String saveAdmin(User user) {
		user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));	//hashes the password entered to store securely in the repository
		repository.save(user);
		return "redirect:userlist";
	}
    
}
