package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(CallLogAspect.class)
class CallServiceV0Test {

    @Autowired
    private CallServiceV0 callServiceV0;

    @Test
    void external() {
        callServiceV0.external();
        System.out.println("callServiceV0 = " + callServiceV0);
    }
    @Test
    void internal() {
        callServiceV0.internal();
    }

}