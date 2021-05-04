package com.naver2021test.utils;

import com.naver2021test.runner.Repeat;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Test 기능 중 annotation과 관련된 Reflecection class에 대하여 유용한 기능을 제공하는 Util class
 *
 * @author M.Ryan (sh.ryan.park@dreamus.io)
 * @since 2021-05-02
 */
public class AnnotationUtils {
	
	/**
	 * {@code method}에 {@link Repeat} annotation이 존재할 경우 value 횟수를 참조하여 반복 횟수를 계산하여 반환 한다
	 *
	 * @param method 메소드
	 * @return 반복 횟수
	 */
	public static int getRepeatCount(Method method) {
		return findAnnotation(method, Repeat.class)
				.map(repeat -> Math.max(1, repeat.value()))
				.orElse(1);
	}
	
	/**
	 * {@link AnnotatedElement} 구현체가 {@code annotationType} 가지고 있을 경우 해당 annotation을 Optional로 wrapping 하여 반환 한다
	 * @param annotatedElement AnnotatedElement 구현체
	 * @param annotationType annotation type
	 * @param <T> annotation type class
	 * @return Optional annotation type
	 */
	public static <T extends Annotation> Optional<T> findAnnotation(AnnotatedElement annotatedElement, Class<T> annotationType) {
		return Optional.ofNullable(annotatedElement.getAnnotation(annotationType));
	}
}
