package sp.project.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Department {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String name;
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "department")
	private List<Employee> employees;
	
	public Department() {
		
	}
	
	public Department(String name) {
		this.name = name;
	}
	
	public int size(List<Employee> employees) {			//returns the number of employees in a given department
		int counter = 0;
		for(Employee employee : employees) {
			if (employee.getDepartment() != null && employee.getDepartment().getName() == name) {
				counter++;
			}
		}
		return counter;
	}
	
	public double budget(List<Employee> employees) {	//returns the total salary budget for a given department
		double budget = 0;
		for(Employee employee : employees) {
			if (employee.getDepartment() != null && employee.getDepartment().getName() == name) {
				budget += employee.getSalary();
			}
		}
		return budget;
	}
	
	public boolean hasDirector(List<Employee> employees) {	//answers whether the department has a director
		boolean answer = false;
		for (Employee employee : employees) {
			if (employee.getDepartment() != null && employee.getDepartment().getName() == name && employee.getPosition() == "Director") {
				answer = true;
			}
		}
		return answer;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
	
}