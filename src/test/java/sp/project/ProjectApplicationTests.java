package sp.project;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import sp.project.web.ProjectController;
import sp.project.web.UserController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectApplicationTests {

	@Autowired
	private ProjectController controller;
	
	@Autowired
	private UserController ucontroller;
	
	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
		assertThat(ucontroller).isNotNull();
	}
}