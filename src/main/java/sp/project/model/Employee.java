package sp.project.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
public class Employee {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String firstName;
	private String lastName;
	private String birthdate;
	private String position;
	private double salary;
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "departmentid")
	private Department department;
	
	public Employee() {

	}
	
	public Employee(String firstName, String lastName, String birthdate, String position, double salary) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate;
		this.position = position;
		this.salary = salary;
	}
	
	public Employee(String firstName, String lastName, String position, double salary) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.position = position;
		this.salary = salary;
	}
	
	public Employee(String firstName, String lastName, String birthdate, String position, double salary, Department department) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate;
		this.position = position;
		this.salary = salary;
		this.department = department;
	}
	
	public Employee(String firstName, String lastName, String position, double salary, Department department) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.position = position;
		this.salary = salary;
		this.department = department;
	}
	
	public Employee(Department department) {
		this.department = department;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getName() {
		return firstName + " " + lastName;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	
}