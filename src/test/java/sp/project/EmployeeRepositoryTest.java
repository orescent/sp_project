package sp.project;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import sp.project.model.Employee;
import sp.project.repository.EmployeeRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;
    
    @Test
    public void findByDepartmentIdShouldReturnEmployees() {
    	assertThat(repository.findByDepartmentId(3)).isNotEmpty();
        List<Employee> employees = repository.findByDepartmentId(3);
        assertThat(employees.size()).isEqualTo(3);
    }
    
    @Test
    public void findByPositionShouldReturnEmployees() {
    	assertThat(repository.findByPosition("Director")).isNotEmpty();
        List<Employee> employees = repository.findByPosition("Director");
        assertThat(employees.size()).isEqualTo(2);
    }
    
    @Test
    public void findByIdShouldReturnEmployee() {
    	assertThat(repository.findByDepartmentId(1)).isNotEmpty();
    	List<Employee> employees = repository.findByDepartmentId(1);
    	Employee employee = employees.get(0);
    	assertThat(repository.findById(employee.getId())).isNotNull();
    	Employee employee2 = repository.findById(employee.getId());
        assertThat(employee2.getFirstName()).isEqualTo("John");
    }
    
    @Test
    public void createEmployee() {
    	Employee employee = new Employee("Orescent", "Guthert", "20-05-1988", "Manager", 5000);
    	assertThat(repository.findByPosition("Manager")).isEmpty();
    	repository.save(employee);
    	assertThat(repository.findByPosition("Manager")).isNotEmpty();
    	List<Employee> repoEmps = repository.findByPosition("Manager");
    	assertThat(repoEmps.size()).isEqualTo(1);
    	assertThat(repoEmps.get(0).getId()).isNotNull();
    	assertThat(repoEmps.get(0).getFirstName()).isEqualTo("Orescent");
    }
    
    @Test
    public void deleteEmployee() {
    	List<Employee> employees = repository.findByPosition("Director");
    	assertThat(employees.get(0)).isNotNull();
    	Employee employee = employees.get(0);
    	assertThat(repository.findById(employee.getId())).isNotNull();
    	assertThat((List<Employee>) repository.findAll()).hasSize(7);
    	repository.deleteById(employee.getId());
    	assertThat((List<Employee>) repository.findAll()).hasSize(6);
    	assertThat(repository.findById(employee.getId())).isNull();
    }
}
