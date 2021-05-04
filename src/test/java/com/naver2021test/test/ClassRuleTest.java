package com.naver2021test.test;

import com.naver2021test.runner.RepeatRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * description
 *
 * @author M.Ryan (sh.ryan.park@dreamus.io)
 * @since 2021-05-05
 */
@RunWith(RepeatRunner.class)
public class ClassRuleTest {
	@Rule
	public TestName name = new TestName();
	
	@Test
	public void test_class_rule_name1() {
		assertEquals("test_class_rule_name1", name.getMethodName());
	}
	
	@Test
	public void test_class_rule_name2() {
		assertEquals("test_class_rule_name2", name.getMethodName());
	}
}
