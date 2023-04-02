/**
 * Lab5_AllTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import jp.co.flm.mod4.control.Lab5_RetrieveProductControllerTest;
import jp.co.flm.mod4.mapper.RetrieveProductMapperTest;
import jp.co.flm.mod4.service.Lab5_RetrieveProductServiceImplTest;

/**
 *
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Suite
@SelectClasses({
	Lab5_RetrieveProductControllerTest.class,
	Lab5_RetrieveProductServiceImplTest.class,
	RetrieveProductMapperTest.class })
public class Lab5_AllTest {
}
