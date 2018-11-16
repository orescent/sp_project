package sp.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import sp.project.model.User;

public interface UserRepository extends CrudRepository<User,Long> {
	Optional<User> findById(Long Id);
	User findByUsername(String username);
}