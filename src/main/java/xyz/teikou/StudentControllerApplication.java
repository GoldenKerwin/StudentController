package xyz.teikou;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {ShiroAutoConfiguration.class, ShiroWebAutoConfiguration.class})
@MapperScan("xyz.teikou.mapper")
public class StudentControllerApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentControllerApplication.class, args);
    }
}