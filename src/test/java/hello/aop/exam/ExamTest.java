package hello.aop.exam;

import hello.aop.exam.aop.RetryAspect;
import hello.aop.exam.aop.TraceAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
@Slf4j
//@Import(TraceAspect.class)
@Import({RetryAspect.class, TraceAspect.class})
public class ExamTest {

    @Autowired
    private ExamService examService;

    @Test
    void test() {
        for (int i = 0; i < 5; i++) {
            log.info("client request i = {}", i);
            examService.request("data" + i);
        } }

    @Test
    void testAsync() {
        List<CompletableFuture<String>> completableFutureList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            log.info("client request i = {}", i);
            completableFutureList.add(examService.requestAsync("data" + i));
        }

        CompletableFuture.allOf(completableFutureList.toArray(CompletableFuture[]::new)).join();

    }

    @Test
    void testAsync2(){
        log.info("client request i = {}", 100);
        CompletableFuture<String> request = examService.requestAsync("data" + 100);
        String join = request.join();
        System.out.println("join = " + join);
    }
}
