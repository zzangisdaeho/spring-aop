package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(CallLogAspect.class)
class CallServiceV1Test {

    @Autowired
    private CallServiceV1 callServiceV1;

    @Test
    void external() {
        callServiceV1.external();
        System.out.println("callServiceV1 = " + callServiceV1);
    }
    @Test
    void internal() {
        callServiceV1.internal();
    }

}