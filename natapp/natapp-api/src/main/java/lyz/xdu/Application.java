package lyz.xdu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
@RestController()
@RequestMapping("/api/")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    /**
     * <a href="http://localhost:8080/api/test">http://localhost:8080/api/test</a>
     * <a href="http://y2fafa.natappfree.cc/api/test">http://y2fafa.natappfree.cc/api/test</a>
     * <a href="http://fersbbvwofks.nat100.top/api/test">http://fersbbvwofks.nat100.top/api/test</a>
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseBodyEmitter test(HttpServletResponse response) {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        String[] words = new String[]{"嗨，臭宝。\r\n", "恭喜💐 ", "你的", " NatApp 内网穿透 ", "部", "署", "测", "试", "成", "功", "了啦🌶！", "\r\nBy lyz"};
        new Thread(() -> {
            for (String word : words) {
                try {
                    emitter.send(word);
                    Thread.sleep(250);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        return emitter;
    }

    /**
     * <a href="http://localhost:8080/api/query">http://localhost:8080/api/query</a>
     * <a href="http://192.168.1.111:8080/api/query">http://192.168.1.111:8080/api/query</a>
     * <a href="http://117.72.37.243:8080/api/query">http://117.72.37.243:8080/api/query</a>
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public String query() {
        return "hi!";
    }
}
