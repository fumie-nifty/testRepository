/**
 * Lab6_AllTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import jp.co.flm.mod4.control.Lab6_AddProductControllerTest;
import jp.co.flm.mod4.mapper.AddProductMapperTest;
import jp.co.flm.mod4.service.Lab6_AddProductServiceImplTest;

/**
 *
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Suite
@SelectClasses({
	Lab6_AddProductControllerTest.class,
	Lab6_AddProductServiceImplTest.class,
	AddProductMapperTest.class })
public class Lab6_AllTest {
}
