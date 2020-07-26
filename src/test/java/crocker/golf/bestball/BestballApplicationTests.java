package crocker.golf.bestball;

import crocker.golf.bestball.core.controllers.UserController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BestballApplicationTests {

    @Autowired
    private UserController userController;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(userController);
    }

}
