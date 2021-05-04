package com.naver2021test.test;


import com.naver2021test.runner.Repeat;
import com.naver2021test.runner.RepeatRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * {@link Repeat} 을 이용하여 지정한 횟수 만큼 동작 하고, 결과과 그룹화 되어 표시되는 지 확인한다
 *
 * @author M.Ryan (sh.ryan.park@dreamus.io)
 * @since 2021-05-02
 */
@RunWith(RepeatRunner.class)
public class BasicTest {
	
	@Test
	public void testNonRepeated() {
		System.out.println("testNonRepeated");
	}
	
	@Ignore
	public void testIgnored() {
		System.out.println("testNonRepeated");
	}
	
	@Repeat(5)
	@Test
	public void testMyCode5Times() {
		System.out.println("testMyCode5Times");
	}
	
	@Repeat(10)
	@Test
	public void testMyCode10Times() {
		System.out.println("testMyCode10Times");
	}
	
	@Repeat(500)
	@Test
	public void testMyCode500Times() {
		System.out.println("testMyCode5Times");
	}
}
