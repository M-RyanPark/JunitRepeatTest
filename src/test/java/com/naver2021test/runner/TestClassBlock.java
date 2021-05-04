/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.naver2021test.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.List;

/**
 * {@link org.junit.runners.ParentRunner}에서 테스트 대상을 실행하기 위해 {@link Statement}를 구축하는 과정 중
 * {@link TestClass}와 관련된 작업을 담당하는 부분으로 {@link TestMethodBlock}과의 계증 구조를 맞추기 위해 분리했다.
 *
 * @author M.Ryan (sh.ryan.park@dreamus.io)
 * @since 2021-05-04
 */
public abstract class TestClassBlock implements TestBlock {
	
	private final TestClass testClass;
	
	public TestClassBlock(TestClass testClass) {
		this.testClass = testClass;
	}
	
	protected TestClass getTestClass() {
		return testClass;
	}
	
	
	protected Object createTest() {
		try {
			return new ReflectiveCallable() {
				@Override
				protected Object runReflectiveCall() throws Throwable {
					return testClass.getOnlyConstructor().newInstance();
				}
			}.run();
		} catch (Throwable e) {
			return new Fail(e);
		}
	}
	
	
	protected Statement withBeforeClasses(Statement statement) {
		List<FrameworkMethod> befores = testClass.getAnnotatedMethods(BeforeClass.class);
		return befores.isEmpty() ? statement : new RunBefores(statement, befores, null);
	}
	
	
	protected Statement withAfterClasses(Statement statement) {
		List<FrameworkMethod> afters = testClass.getAnnotatedMethods(AfterClass.class);
		return afters.isEmpty() ? statement : new RunAfters(statement, afters, null);
	}
	
	
	protected Statement withClassRules(Statement statement) {
		List<TestRule> classRules = classRules(testClass);
		return classRules.isEmpty() ? statement : new RunRules(statement, classRules, getDescription());
	}
	
	
	private static List<TestRule> classRules(TestClass testClass) {
		List<TestRule> result = testClass.getAnnotatedMethodValues(null, ClassRule.class, TestRule.class);
		result.addAll(testClass.getAnnotatedFieldValues(null, ClassRule.class, TestRule.class));
		return result;
	}
}
