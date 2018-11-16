package sp.project.repository;

import org.springframework.data.repository.CrudRepository;

import sp.project.model.Department;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
	
	Department findById(long id);
	Department findByName(String name);
}