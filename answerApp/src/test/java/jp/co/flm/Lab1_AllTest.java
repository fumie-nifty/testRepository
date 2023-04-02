/**
 * Lab1_AllTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import jp.co.flm.mod3.control.Lab1_RetrieveMemberControllerTest;
import jp.co.flm.mod3.mapper.MemberMapperMockTest;
import jp.co.flm.mod3.service.Lab1_RetrieveMemberServiceImplTest;

/**
 *
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Suite
@SelectClasses({
	Lab1_RetrieveMemberControllerTest.class,
	Lab1_RetrieveMemberServiceImplTest.class,
	MemberMapperMockTest.class })
public class Lab1_AllTest {
}
