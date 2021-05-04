package com.naver2021test.runner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Test 메소드에 지정할 경우 해당 메소드를 {@code value} 횟수 만큼 반복하여 실행할 Test annotation
 *
 * @author M.Ryan (sh.ryan.park@dreamus.io)
 * @since 2021-05-02
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Repeat {
	int value() default 1;
}
