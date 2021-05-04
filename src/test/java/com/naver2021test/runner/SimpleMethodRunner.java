/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.naver2021test.runner;

import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

/**
 * {@link org.junit.runners.BlockJUnit4ClassRunner}과 동일하게 {@link TestMethodBlock}을
 * 구축해서 {@code Before}, {@code After} 등도 제대로 동작할 수 있게 구성해준다.
 *
 * @author M.Ryan (sh.ryan.park@dreamus.io)
 * @since 2021-05-04
 */
class SimpleMethodRunner extends Runner {
	final FrameworkMethod method;
	final TestClass testClass;
	
	public SimpleMethodRunner(FrameworkMethod method, TestClass testClass) {
		this.method = method;
		this.testClass = testClass;
	}
	
	@Override
	public Description getDescription() {
		return newDescription(method, testClass);
	}
	
	@Override
	public void run(RunNotifier notifier) {
		final Description description = getDescription();
		final EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
		eachNotifier.fireTestStarted();
		try {
			new TestMethodBlock(testClass, method).evaluate();
		} catch (AssumptionViolatedException e) {
			eachNotifier.addFailedAssumption(e);
		} catch (Throwable e) {
			eachNotifier.addFailure(e);
		} finally {
			eachNotifier.fireTestFinished();
		}
	}
	
	static Description newDescription(FrameworkMethod method, TestClass testClass) {
		return Description.createTestDescription(testClass.getJavaClass(), method.getName());
	}
}
