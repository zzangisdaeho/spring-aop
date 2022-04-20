package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy() {
        MemberServiceImpl target = new MemberServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false);//JDK dynamic proxy

        // 구현체를 주어도 proxyFactory에서 구현체가 있음을 감지하고 JDK dynamic proxy를 생성한다.
        // 알아서 구현체의 부모를 따라가서 인터페이스를 기반으로 생성된게 나온다.
        // 물론 구현체가 아닌 인터페이스를 주어도 인터페이스를 기반으로 생성된다.
        // 프록시를 인터페이스로 캐스팅 성공
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        log.info("proxy class={}", memberServiceProxy.getClass());

        //JDK 동적 프록시를 구현 클래스로 캐스팅 시도 실패, ClassCastException 예외 발생
        //JDK 동적 프록시의 결과물은 인터페이스를 바라보는 객체이기 때문에, 인터페이스의 다른 구현체로 바꾸는것은 불가능
        //구현체 A를 다른 구현체 B로 캐스팅하는것과 같음
        Assertions.assertThrows(ClassCastException.class, () -> {
            MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
        });
    }

    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true);//CGLIB 프록시

        //프록시를 인터페이스로 캐스팅 성공
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();
        log.info("proxy class={}", memberServiceProxy.getClass());

        //CGLIB 프록시를 구현 클래스로 캐스팅 시도 성공
        //CGLIB는 구현체를 기반으로 프록시를 생성. JDK가 인터페이스를 기반으로 생성하는것과 다름
        //고로 구현체로 다운케스팅하는것도 가능하다.
        MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
    }
}
