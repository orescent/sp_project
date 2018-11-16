package sp.project.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import sp.project.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
	
	Employee findById(long id);
	List<Employee> findByDepartmentId(long departmentId);
	List<Employee> findByPosition(String position);
}