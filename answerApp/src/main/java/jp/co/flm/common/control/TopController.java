/**
 * TopController.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.common.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 実習TOP画面表示Controller
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Controller
public class TopController {

	/**
	 * 実習TOP画面を表示するHandlerメソッド
	 * マッピングするURL： /(コンテキストルート)
	 * @return トップ画面（/top）
	 */
	@RequestMapping("/")
	public String handler() {

		System.out.println("sss11");
		System.out.println("sss11");

		return "/top";
	}
}
