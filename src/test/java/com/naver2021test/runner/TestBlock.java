/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.naver2021test.runner;

import org.junit.runner.Description;

/**
 * {@link TestClassBlock}과 {@link TestMethodBlock}에서 사용하기 위한 기본적인 함수들을 정의한 interface.
 *
 * @author M.Ryan (sh.ryan.park@dreamus.io)
 * @since 2021-05-04
 */
public interface TestBlock {
	
	/**
	 * {@link org.junit.runners.model.Statement#evaluate()}와 동일한 interface를 유지하기 위해 추가된 함수.
	 * 실제 테스트 대상을 수행할 때 사용되는 함수이다.
	 */
	void evaluate() throws Throwable;
	
	/**
	 * {@link org.junit.Rule} 등에 의해 {@link Description} 정보를 다루어야 할 일이 있으므로
	 * 현재 수행되는 TestBlock에 해당하는 Description을 받을 수 있어야 한다.
	 */
	Description getDescription();
}
