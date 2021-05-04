# JUNIT4 Repeat Test

JUNIT 테스트 시 다음과 같이 `@Repeat` annotation이 테스트 메소드에 지정 되었을 경우 `@Repeat.value` 만큼 반복 실행할 수 있는 Runner를 작성합니다. 

```
@RunWith(RepeatRunner.class)
public class SampleTest {
	
	@Repeat(10)
	@Test
	public void testMyCode10Times() {
		...
	}
	
	
	@Repeat(5)
	@Test
	public void testMyCode5Times() {
		...
	}
}
```

테스트 결과를 리포팅 할 때, 메소드 별로 그룹화 하고 테스트 결과 메소드 명에 해당 회차 정보를 배열과 같이 `methodName[count]`로 표현합니다.


## RepeatRunner
- 메소드 별로 그룹화 하기 위하여 Suite를 extend 하여 작
- 메소드를 반복할 때 회차 정보를 표시하기 위해 index 갖는 `FrameworkMethodWithIndexed`를 `FrameworkMethod`를 확장하여 작성
- `@Test` 메소드별로 N회 만큼 그룹하 하여 실행하기 위한 `RepeatMethodGroupingRunner` 작성
- `RepeatMethodGroupingRunner`는 `BlockJUnit4ClassRunner` 기반으로 작성하였으며, 그룹화를 바탕이 되는 parent `FrameworkMethod`와 반복 실행할 `FrameworkMethodWithIndexed` List 를 children으로 가짐 
