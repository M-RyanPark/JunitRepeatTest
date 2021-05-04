package com.naver2021test.test;

import com.naver2021test.runner.Repeat;
import com.naver2021test.runner.RepeatRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.isA;

/**
 * {@link RepeatRunner} 사용 시 {@link Rule} annotation 이 정상 동작 하는 지 테스트 한다.
 *
 * @author M.Ryan (sh.ryan.park@dreamus.io)
 * @since 2021-05-05
 */
@RunWith(RepeatRunner.class)
public class RuleTest {
	@Rule
	public final ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void expect_throw_exception() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectCause(isA(NullPointerException.class));
		thrown.expectMessage("error message");
		
		throw new IllegalArgumentException("error message", new NullPointerException());
	}
	
	@Repeat(5)
	@Test
	public void expect_throw_exception_5times() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectCause(isA(NullPointerException.class));
		thrown.expectMessage("error message");
		
		throw new IllegalArgumentException("error message", new NullPointerException());
	}
}
