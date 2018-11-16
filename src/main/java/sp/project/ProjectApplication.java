package sp.project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import sp.project.model.Department;
import sp.project.model.Employee;
import sp.project.model.User;
import sp.project.repository.DepartmentRepository;
import sp.project.repository.EmployeeRepository;
import sp.project.repository.UserRepository;

@SpringBootApplication
public class ProjectApplication {
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ProjectApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(EmployeeRepository erepository, DepartmentRepository drepository, UserRepository urepository) {
		return(args) -> {
			
			//We create some departments
			
			Department department1 = new Department ("Finance");
			Department department2 = new Department("Sales");
			Department department3 = new Department("HR");
			Department department4 = new Department("IT");
			
			drepository.save(department1);
			drepository.save(department2);
			drepository.save(department3);
			drepository.save(department4);
			
			//We create some employees
			
			Employee employee1 = new Employee ("John", "Smith", "Director", 16000, department1);
			Employee employee2 = new Employee ("Liam", "Taylor", "Sales representative", 6000, department2);
			Employee employee3 = new Employee ("Erin", "Adams", "15-02-1977", "Sales manager", 9000, department2);
			Employee employee4 = new Employee ("Kate", "Perry", "04-03-1986", "Intern", 2000, department3);
			Employee employee5 = new Employee ("Addison", "Melton", "Director", 11000, department3);
			Employee employee6 = new Employee ("Ashley", "Jordan", "27-08-1981", "Talent specialist", 5500, department3);
			Employee employee7 = new Employee ("Bennie", "Marsh", "Sales representative", 5000, department2);
			
			erepository.save(employee1);
			erepository.save(employee2);
			erepository.save(employee3);
			erepository.save(employee4);
			erepository.save(employee5);
			erepository.save(employee6);
			erepository.save(employee7);
			
			// We create two users: admin/admin user/user
			
			User user1 = new User("user", "$2a$10$PmWNlpsjKibZqg3.rWhts.ma/OJC8d9Bv/U3rcIhL.jQGFHBloSTi", "USER");
			User user2 = new User("admin", "$2a$10$KtOoz491q3niRGIqGrZ4KeQI4bCOrix8CFuRSmdfslhKitoSF5wam", "ADMIN");
			
			urepository.save(user1);
			urepository.save(user2);
		};
	}
}
