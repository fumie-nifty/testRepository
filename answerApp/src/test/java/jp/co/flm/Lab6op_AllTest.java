/**
 * Lab6op_AllTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import jp.co.flm.mod4.control.Lab6op_RegistMemberControllerTest;
import jp.co.flm.mod4.mapper.MemberMapperTest;
import jp.co.flm.mod4.service.Lab6op_RegistMemberServiceImplTest;

/**
 *
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Suite
@SelectClasses({
	Lab6op_RegistMemberControllerTest.class,
	Lab6op_RegistMemberServiceImplTest.class,
	MemberMapperTest.class })
public class Lab6op_AllTest {
}
