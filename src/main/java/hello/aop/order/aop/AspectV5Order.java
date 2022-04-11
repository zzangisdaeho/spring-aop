package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

@Slf4j
@Import({AspectV5Order.LogAspect.class, AspectV5Order.TxAspect.class})
public class AspectV5Order {

    /**
     * 어드바이스는 기본적으로 순서를 보장하지 않는다.
     * 순서를 지정하고 싶으면 @Aspect 적용 단위로 org.springframework.core.annotation.@Order 애노테이션을 적용해야 한다.
     * 문제는 이것을 어드바이스 단위가 아니라 클래스 단위로 적용할 수 있다는 점이다.
     * 그래서 지금처럼 하나의 애스펙트에 여러 어드바이스가 있으면 순서를 보장 받을 수 없다.
     * 따라서 Aspect를 별도의 클래스로 분리해야 한다.
     * Aspect가 bean으로 등록되어야 프록시 자동생성기가 비교하기에 각 내부클래스를 Import를 통해 빈등록한다 (당연히 다르게 등록해도 상관없다)
  다  */

    @Aspect
    @Order(2)
    public static class LogAspect{
        @Around("hello.aop.order.aop.Pointcuts.allOrder()")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[log] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

    @Aspect
    @Order(1)
    public static class TxAspect{
        @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
        public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
            try {
                log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
                Object result = joinPoint.proceed();
                log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
                return result;
            } catch (Exception e) {
                log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
                throw e;
            } finally {
                log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
            }
        }
    }
}

