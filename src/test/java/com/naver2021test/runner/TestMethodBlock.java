/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.naver2021test.runner;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.MethodRule;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.List;

/**
 * {@link org.junit.runners.BlockJUnit4ClassRunner}에서 테스트 대상을 실행하기 위해 {@link Statement}를 구축하는 과정을
 * 복사해 재정리한 코드. 기존 코드가 반드시 {@link org.junit.runner.Runner}를 상속 받아야 하는 구조 때문에
 * 단일 {@link FrameworkMethod} 만을 테스트 대상으로 처리할 때에는 활용하기가 어려워 분리
 *
 * @author M.Ryan (sh.ryan.park@dreamus.io)
 * @since 2021-05-04
 */
public class TestMethodBlock extends TestClassBlock {
	
	private final FrameworkMethod method;
	
	private final Description description;
	
	public TestMethodBlock(TestClass testClass, FrameworkMethod method) {
		super(testClass);
		this.method = method;
		this.description = Description.createTestDescription(testClass.getJavaClass(), method.getName());
	}
	
	
	@Override
	public Description getDescription() {
		return description;
	}
	
	@Override
	public void evaluate() throws Throwable {
		methodBlock().evaluate();
	}
	
	
	private Statement methodBlock() {
		final Object test = createTest();
		Statement statement = methodInvoker(method, test);
		statement = possiblyExpectingExceptions(method, statement);
		statement = withBefores(test, statement);
		statement = withAfters(test, statement);
		statement = withRules(method, test, statement);
		statement = withBeforeClasses(statement);
		statement = withAfterClasses(statement);
		statement = withClassRules(statement);
		return statement;
	}
	
	
	protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
		return new InvokeMethod(method, test);
	}
	
	
	private Statement possiblyExpectingExceptions(FrameworkMethod method, Statement next) {
		Test annotation = method.getAnnotation(Test.class);
		return expectsException(annotation) ? new ExpectException(next, getExpectedException(annotation)) : next;
	}
	
	
	private Statement withBefores(Object target, Statement statement) {
		List<FrameworkMethod> befores = getTestClass().getAnnotatedMethods(Before.class);
		return befores.isEmpty() ? statement : new RunBefores(statement, befores, target);
	}
	
	
	private Statement withAfters(Object target, Statement statement) {
		List<FrameworkMethod> afters = getTestClass().getAnnotatedMethods(After.class);
		return afters.isEmpty() ? statement : new RunAfters(statement, afters, target);
	}
	
	
	private Statement withRules(FrameworkMethod method, Object target, Statement statement) {
		List<TestRule> testRules = getTestRules(getTestClass(), target);
		Statement result = statement;
		result = withMethodRules(getTestClass(), method, testRules, target, result);
		result = withTestRules(testRules, result, getDescription());
		return result;
	}
	
	private static boolean expectsException(Test annotation) {
		return getExpectedException(annotation) != null;
	}
	
	private static Class<? extends Throwable> getExpectedException(Test annotation) {
		if (annotation == null || annotation.expected() == Test.None.class) {
			return null;
		} else {
			return annotation.expected();
		}
	}
	
	
	private static Statement withMethodRules(TestClass testClass, FrameworkMethod method,
	                                         List<TestRule> testRules, Object target, Statement result) {
		for (MethodRule each : getMethodRules(testClass, target)) {
			//noinspection SuspiciousMethodCalls
			if (!testRules.contains(each)) {
				result = each.apply(result, method, target);
			}
		}
		return result;
	}
	
	
	private static List<MethodRule> getMethodRules(TestClass testClass, Object target) {
		return rules(testClass, target);
	}
	
	
	private static List<MethodRule> rules(TestClass testClass, Object target) {
		List<MethodRule> rules = testClass.getAnnotatedMethodValues(target, Rule.class, MethodRule.class);
		rules.addAll(testClass.getAnnotatedFieldValues(target, Rule.class, MethodRule.class));
		return rules;
	}
	
	
	private static Statement withTestRules(List<TestRule> testRules, Statement statement, Description description) {
		return testRules.isEmpty() ? statement : new RunRules(statement, testRules, description);
	}
	
	
	private static List<TestRule> getTestRules(TestClass testClass, Object target) {
		List<TestRule> result = testClass.getAnnotatedMethodValues(target, Rule.class, TestRule.class);
		result.addAll(testClass.getAnnotatedFieldValues(target, Rule.class, TestRule.class));
		return result;
	}
	
}
