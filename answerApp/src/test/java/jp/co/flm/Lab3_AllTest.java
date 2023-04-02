/**
 * Lab3_AllTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import jp.co.flm.mod3.control.Lab3_UpdateMemberControllerTest;
import jp.co.flm.mod3.service.Lab3_UpdateMemberServiceImplTest;

/**
 *
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Suite
@SelectClasses({
	Lab3_UpdateMemberControllerTest.class,
	Lab3_UpdateMemberServiceImplTest.class })
public class Lab3_AllTest {
}
