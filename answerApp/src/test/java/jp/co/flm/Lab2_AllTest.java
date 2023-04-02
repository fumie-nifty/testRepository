/**
 * Lab2_AllTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import jp.co.flm.mod3.control.Lab2_RetrieveMemberControllerTest;
import jp.co.flm.mod3.service.Lab2_RetrieveMemberServiceImplTest;

/**
 *
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Suite
@SelectClasses({
	Lab2_RetrieveMemberControllerTest.class,
	Lab2_RetrieveMemberServiceImplTest.class })
public class Lab2_AllTest {
}
