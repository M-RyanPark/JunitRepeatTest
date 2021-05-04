package com.naver2021test.runner;

import com.naver2021test.utils.AnnotationUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link Repeat} annotation을 확인하여 메소드 반복실행을 처리할 {@link Runner} 구현체
 * 테스트 결과를 메소드 별로 그룹화 하여 보여주기 위해 {@link RepeatMethodGroupingRunner}로 구성하여 처리한다.
 *
 * @author M.Ryan (sh.ryan.park@dreamus.io)
 * @since 2021-05-04
 */
public class RepeatRunner extends Suite {
	
	private final ArrayList<Runner> runners = new ArrayList<>();
	
	public RepeatRunner(Class<?> testClass) throws InitializationError {
		super(testClass, Collections.emptyList());
		
		for (final FrameworkMethod method : getTestClass().getAnnotatedMethods(Test.class)) {
			if (method.getAnnotation(Ignore.class) != null) {
				continue;
			}
			
			if (AnnotationUtils.findAnnotation(method.getMethod(), Repeat.class).isPresent()) {
				runners.add(new RepeatMethodGroupingRunner(getTestClass(), method));
			} else {
				runners.add(new SimpleMethodRunner(method, getTestClass()));
			}
		}
	}
	
	@Override
	protected List<Runner> getChildren() {
		return this.runners;
	}
	
	/**
	 * {@link FrameworkMethod}를 입력받아 동작하는 {@link BlockJUnit4ClassRunner}
	 * 반복 처리를 위해 입력받은 {@link FrameworkMethod}를 {@link FrameworkMethodWithIndexed}로 복사하여 {@code duplicateIndexedChildren}에 저장한다.
	 */
	public static class RepeatMethodGroupingRunner extends BlockJUnit4ClassRunner {
		
		private final FrameworkMethod parent;
		private final ArrayList<FrameworkMethod> duplicateIndexedChildren = new ArrayList<>();
		
		protected RepeatMethodGroupingRunner(TestClass testClass, FrameworkMethod parent) throws InitializationError {
			super(testClass);
			this.parent = parent;
			
			final int repeatCount = AnnotationUtils.getRepeatCount(parent.getMethod());
			if (repeatCount > 1) {
				for (int i = 0; i < AnnotationUtils.getRepeatCount(parent.getMethod()); i++) {
					duplicateIndexedChildren.add(new FrameworkMethodWithIndexed(parent.getMethod(), i));
				}
			}
		}
		
		@Override
		protected String testName(FrameworkMethod method) {
			return indexedTestName(method);
		}
		
		@Override
		protected List<FrameworkMethod> getChildren() {
			return duplicateIndexedChildren;
		}
		
		/**
		 * method 명으로 grouping 하여 보여줄 수 있게 {@code getName}을 parent의 메소드 명을 반환하도록 override 한다
		 */
		@Override
		protected String getName() {
			return parent.getName();
		}
		
		private static String indexedTestName(FrameworkMethod method) {
			if (method instanceof FrameworkMethodWithIndexed) {
				return ((FrameworkMethodWithIndexed) method).getIndexedName();
			} else {
				return method.getName();
			}
		}
	}
	
	/**
	 * {@link Repeat} 에 의해 반복될 경우 해당 {@link FrameworkMethod}의 반복 index를 가질 수 있게 확장한 class
	 */
	static class FrameworkMethodWithIndexed extends FrameworkMethod {
		private final Integer index;
		
		public FrameworkMethodWithIndexed(Method method, int index) {
			super(method);
			this.index = index;
		}
		
		@Override
		public String getName() {
			return super.getName();
		}
		
		public String getIndexedName() {
			return String.format("%s[%s]", getMethod().getName(), index);
		}
		
		/**
		 * {@code BlockJUnit4ClassRunner.describeChild}는 동일한 {@link FrameworkMethod}일 경우 {@code methodDescriptions} map에
		 * {@link Description}을 저장하고 재사용한다. 이 때 key값이 되는 hasCode는 생성자로 입력 받은 {@code method}의 hasCode이므로,
		 * {@link FrameworkMethodWithIndexed}를 생성할 때 동일한 method를 반복하여 사용하므로 hasCode를 바꿔주지 않으면
		 * {@link Description} 재사용 되어 0번 index만 찍히는 문제가 발생하므로 {@code hashCode}를 override 하여 모든 index에 대하여 표시할 수 있게 한다.
		 */
		@Override
		public int hashCode() {
			return super.hashCode() + index;
		}
	}
}
