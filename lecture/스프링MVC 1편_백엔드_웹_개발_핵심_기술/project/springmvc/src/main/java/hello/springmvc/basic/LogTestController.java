package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j // private final Logger log = LoggerFactory.getLogger(LogTestController.class); 한줄 추가해주는 것임.
@RestController // REST API + Controller => 응답값을 그대로 리턴함. (@Controller는 뷰 이름으로 인식)
public class LogTestController {

//    private final Logger log = LoggerFactory.getLogger(LogTestController.class);

    @RequestMapping("/log-test")
    public String logTest() {

        String name = "Spring";

        System.out.println("name = " + name);

        log.trace("trace log = {}", name);
        log.debug("debug log = {}", name);
        log.info("info log = {}", name);
        log.warn("warn log = {}", name);
        log.error("error log = {}", name);

        return "ok";
    }
}
