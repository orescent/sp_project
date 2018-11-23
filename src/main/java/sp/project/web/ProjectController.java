package sp.project.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sp.project.model.Department;
import sp.project.model.Employee;
import sp.project.repository.DepartmentRepository;
import sp.project.repository.EmployeeRepository;

@Controller
public class ProjectController {
	
	@Autowired
	private EmployeeRepository erepository;
	@Autowired
	private DepartmentRepository drepository;
	
	//DEFAULT LANDING PAGES
	
	@RequestMapping(value="/login")
    public String login() {
        return "login";
    }
	
	@RequestMapping(value= {"/index", "/", ""})								//maps all default landing pages to the site
	public String index() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.isAuthenticated() == true) {						//if user is authenticated
			return "redirect:welcome";										//landing page redirects to the appropriate one for the user
		} else {															//otherwise
			return "redirect:login";										//it redirects to login
		}
	/*	Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		List<String> roles = new ArrayList<String>();
		for (GrantedAuthority a : authorities) {
			roles.add(a.getAuthority());
		}	*/
	}
	
	@RequestMapping(value="/welcome")
	public String welcome() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if (username.equals("admin")) {
			return "redirect:userlist";										//user management is the homepage for admin, employee list is the homepage for other users
		} else {
			return "redirect:employeelist";
		}
	}
	
	//EMPLOYEE MANAGEMENT
	
	@RequestMapping("/employeelist")										//homepage for employee overview
	public String employeeList(Model model) {
		model.addAttribute("employees", erepository.findAll());
		model.addAttribute("departments", drepository.findAll());
		return "employeelist";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/employees", method=RequestMethod.GET) 			//Rest list for all employees
	public @ResponseBody Iterable<Employee> employeeListRest() {
		return erepository.findAll();
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/employee/{id}", method=RequestMethod.GET) 		//Rest detail for a specific employee
	public @ResponseBody Optional<Employee> findEmployeeRest(@PathVariable("id") Long employeeId) {
		return erepository.findById(employeeId);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/addemployee")									//creates a new employee
	public String addEmployee(Model model) {
		model.addAttribute("employee", new Employee());
		model.addAttribute("departments", drepository.findAll());
		return "addemployee";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/addemployee/{department}") 						//loads the new employee page with a specific department pre-selected
	public String addEmployee(@PathVariable("department") String departmentName, Model model) {
		Department currentDepartment = drepository.findByName(departmentName);
		model.addAttribute("employee", new Employee(currentDepartment));
		model.addAttribute("departments", drepository.findAll());
		return "addemployee";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/saveemployee", method=RequestMethod.POST)		//saves the employee
	public String saveEmployee(Employee employee) {
		if (employee.getBirthdate() == "") {
			employee.setBirthdate(null);									//default empty birthdate is null for simpler future handling
		}
		erepository.save(employee);
		return "redirect:employeelist";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/saveandadd", method=RequestMethod.POST)			//saves the employee and starts the creation of a new department
	public String saveAndAdd(Employee employee) {
		if (employee.getBirthdate() == "") {
			employee.setBirthdate(null);
		}
		erepository.save(employee);
		return "redirect:/adddepartment";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/editemployee/{id}")								//edits the employee
	public String editEmployee(@PathVariable("id") Long employeeId, Model model){
		Optional<Employee> currentEmployee = erepository.findById(employeeId);
		model.addAttribute("employee", currentEmployee);
		model.addAttribute("departments", drepository.findAll());
		return "editemployee";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")			
	@RequestMapping(value="/deleteemployee/{id}")							//deletes employee
	public String deleteEmployee(@PathVariable("id") Long employeeId, Model model){
		erepository.deleteById(employeeId);
		return "redirect:../employeelist";
	}
	
	@RequestMapping(value="/cancel/{form}")									//cancels the form, multipurpose, redirects to relevant page
	public String cancel(@PathVariable("form") String form) {
		switch (form) {
		case "employee": return "redirect:employeelist";
		case "user": return "redirect:userlist";
		case "department": return "redirect:departmentlist";
		default: return "redirect:index";
		}
		
	}
	
	//DEPARTMENT MANAGEMENT
	
	@RequestMapping("/departmentlist")										//global list
	public String departmentList(Model model) {
		model.addAttribute("employees", erepository.findAll());
		model.addAttribute("departments", drepository.findAll());
		List<Employee> directors = erepository.findByPosition("Director");	//lists all directors and adds it to the model
		for (int i = 0; i < directors.size(); i++) {
			if (directors.get(i).getDepartment() == null) {					//removes directors with no department
				directors.remove(i);
			}
		}
		model.addAttribute("directors", directors);
		return "departmentlist";
	}
	
	@RequestMapping(value="/departmentview/{id}") 							//management page for a specific department
	public String departmentView(@PathVariable("id") Long departmentId, Model model){
		Optional<Department> currentDepartment = drepository.findById(departmentId);
		model.addAttribute("department", currentDepartment);
		model.addAttribute("employees", erepository.findByDepartmentId(departmentId));	//saves all the employees of the department in the model
		String director = "";
		List<Employee> employees = erepository.findByDepartmentId(departmentId);
		for (Employee employee : employees) {
			if (employee.getPosition() == "Director") {
				director = employee.getName();								//finds the name of the director of the department if there is one, saves it in the model
			}
		}
		if (director == "") {												//if there is no director for the department, will return the string "None"
			director = "None";
		}
		model.addAttribute("director", director);
		return "departmentview";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")	
	@RequestMapping(value="/adddepartment") 								//creates a new department
	public String addDepartment(Model model) {
		model.addAttribute("department", new Department());
		return "adddepartment";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")	
	@RequestMapping(value="/savedepartment", method=RequestMethod.POST)		//saves the department
	public String saveDepartment(Department department) {
		drepository.save(department);
		return "redirect:departmentlist";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")			
	@RequestMapping(value="/deletedepartment/{id}")							//deletes department
	public String deleteDepartment(@PathVariable("id") Long departmentId, Model model){
		List<Employee> employees = erepository.findByDepartmentId(departmentId);
		for (int i = 0; i < employees.size(); i++) {
			employees.get(i).setDepartment(null);
		}
		erepository.saveAll(employees);
		drepository.deleteById(departmentId);
		return "redirect:../departmentlist";
	}
	
}