package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import hello.aop.proxyvs.code.ProxyDIAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"}) //JDK 동적 프록시, DI 예외 발생.
// Impl를 대상으로 하는 포인트컷이 포함된 Advisor가 존재하기 때문에 프록시가 생성되고 빈 정보에 등록되었으나, 해당 프록시는 JDK 동적 프록시에 의해 생성되었기 때문에 구현체로 형변환이 불가능하여 DI시에 에러가 터진다.
// org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'hello.aop.proxyvs.ProxyDITest': Unsatisfied dependency expressed through field 'memberServiceImpl'; nested exception is org.springframework.beans.factory.BeanNotOfRequiredTypeException: Bean named 'memberServiceImpl' is expected to be of type 'hello.aop.member.MemberServiceImpl' but was actually of type 'com.sun.proxy.$Proxy50'
// 에러를 해석해보면 impl의 DI를 하는 과정에서 MemberServiceImpl타입의 클래스를 원하였으나, com.sun.proxy.$Proxy -> (JDK 동적 프록시 구현체) 타입이 제공되었다는 뜻이다. 해당 구현체는 구현체로 형변환이 불가능하기에 에러가 발생하게된다.
@SpringBootTest(properties = {"spring.aop.proxy-target-class=true"}) //CGLIB 프록시, 성공
// CGLIB로 생성된 프록시는 구현체로도 형변환이 가능하기 때문에 에러가 터지지 않는다.
// 결론 : CGLIB 쓰자 ^^
@Import(ProxyDIAspect.class)
public class ProxyDITest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberServiceImpl memberServiceImpl;

    @Test
    void go() {
        log.info("memberService class={}", memberService.getClass());
        log.info("memberServiceImpl class={}", memberServiceImpl.getClass());
        memberServiceImpl.hello("hello");
    }
}
