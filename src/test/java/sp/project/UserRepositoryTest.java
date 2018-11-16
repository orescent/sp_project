
package sp.project;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import sp.project.model.User;
import sp.project.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void findByIdShouldReturnUser() {
        Optional<User> user = repository.findById((long) 1);
        
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getUsername()).isEqualTo("user");
    }
    
    @Test
    public void findByUsernameShouldReturnUser() {
        User user = repository.findByUsername("admin");
        
        assertThat(user.getId()).isEqualTo(2);
    }
    
    @Test
    public void createUser() {
    	assertThat((List<User>) repository.findAll()).hasSize(2);
    	User user = new User("orescent", "password", "USER");
    	BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
    	String hashPwd = bc.encode(user.getPasswordHash());
    	user.setPasswordHash(hashPwd);
    	repository.save(user);
    	assertThat(repository.findByUsername("orescent")).isNotNull();
    	User repoUser = repository.findByUsername("orescent");
    	assertThat(repoUser.getId()).isNotNull();
    	assertThat((List<User>) repository.findAll()).hasSize(3);
    }
    
    @Test
    public void deleteUser() {
    	assertThat(repository.findByUsername("user")).isNotNull();
    	assertThat((List<User>) repository.findAll()).hasSize(2);
    	User user = repository.findByUsername("user");
    	repository.deleteById(user.getId());
    	assertThat((List<User>) repository.findAll()).hasSize(1);
    }
}
