
package sp.project;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import sp.project.model.Department;
import sp.project.repository.DepartmentRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository repository;
    
    @Test
    public void findByIdShouldReturnDepartment() {
    	assertThat(repository.findById(3)).isNotNull();
        Department department = repository.findById(3);
        assertThat(department.getName()).isEqualTo("HR");
    }
    
    @Test
    public void findByNameShouldReturnDepartment() {
    	assertThat(repository.findByName("IT")).isNotNull();
    	Department department = repository.findByName("IT");
        assertThat(department.getId()).isNotNull();
    }
    
    @Test
    public void createDepartment() {
    	Department department = new Department("R&D");
    	assertThat(repository.findAll()).hasSize(4);
    	repository.save(department);
    	assertThat(repository.findByName("R&D")).isNotNull();
    	assertThat(repository.findAll()).hasSize(5);
    }
    
    @Test
    public void deleteDepartment() {
    	assertThat(repository.findAll()).hasSize(4);
    	Department department = repository.findByName("IT");
    	repository.deleteById(department.getId());
    	assertThat(repository.findAll()).hasSize(3);
    	assertThat(repository.findById(department.getId())).isNull();
    }
}
