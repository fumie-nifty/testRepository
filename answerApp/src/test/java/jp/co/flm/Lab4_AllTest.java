/**
 * Lab4_AllTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import jp.co.flm.mod4.control.Lab4_RetrieveCategoryControllerTest;
import jp.co.flm.mod4.mapper.CategoryMapperTest;
import jp.co.flm.mod4.service.Lab4_RetrieveCategoryServiceImplTest;

/**
 *
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Suite
@SelectClasses({
	Lab4_RetrieveCategoryControllerTest.class,
	Lab4_RetrieveCategoryServiceImplTest.class,
	CategoryMapperTest.class })
public class Lab4_AllTest {
}
