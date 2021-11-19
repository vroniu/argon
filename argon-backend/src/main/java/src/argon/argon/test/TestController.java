package src.argon.argon.test;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static Logger logger = LogManager.getLogger(TestController.class);

    @GetMapping("/test")
    public String test() {
        logger.info("logging test");
        return "Hello world!";
    }

}
