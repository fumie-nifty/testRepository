/**
 * jp.co.flm.AllTests
 *
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Suite
@SelectPackages({"jp.co.flm.mod3.control",
	"jp.co.flm.mod3.mapper",
	"jp.co.flm.mod3.service",
	"jp.co.flm.mod4.control",
	"jp.co.flm.mod4.mapper",
	"jp.co.flm.mod4.service" })
public class AllTests {
}
