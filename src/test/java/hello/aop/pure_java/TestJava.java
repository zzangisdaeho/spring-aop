package hello.aop.pure_java;

import org.junit.jupiter.api.Test;

public class TestJava {

    @Test
    void test1(){
        int a = 20;
        int b = a;

        b = 30;

        System.out.println("b = " + b);
    }
}
