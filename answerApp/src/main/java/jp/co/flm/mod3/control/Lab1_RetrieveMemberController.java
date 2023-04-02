/**
 * Lab1_RetrieveMemberController.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod3.control;

import static jp.co.flm.common.util.MessageList.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.flm.mod3.entity.Member;
import jp.co.flm.mod3.service.Lab1_RetrieveMemberService;

/**
 * 会員情報照会Controller
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Controller
public class Lab1_RetrieveMemberController {

	/** Service */
	@Autowired
	private Lab1_RetrieveMemberService service;

	/**
	 * TOP画面の[実習1]に対応するHandlerメソッド
	 * マッピングするURL： /lab1/menu
	 * @return メニュー画面（/lab1/retrieve-menu）
	 */
	@RequestMapping("/lab1/menu")
	public String menu() {
		return "/lab1/retrieve-menu";
	}

	/**
	 * メニュー画面の[会員情報照会]に対応するHandlerメソッド
	 * マッピングするURL： /lab1/login
	 * マッピングするHTTPメソッド : GET
	 * @return ログイン画面（/lab1/retrieve-login）
	 */
	@RequestMapping(value = "/lab1/login", method = RequestMethod.GET)
	public String login() {
		return "/lab1/retrieve-login";
	}

	/**
	 * ログイン画面の[ログイン]に対応するHandlerメソッド
	 * マッピングするURL： /lab1/retrieveMember
	 * マッピングするパラメーター： { "memberId", "password" }
	 * @param memberId 会員ID
	 * @param password パスワード
	 * @param model Modelオブジェクト
	 * @return 会員情報照会画面（/lab1/retrieve-member）
	 */
	@RequestMapping(value = "/lab1/retrieveMember",
			params = { "memberId", "password" })
	public String retrieveMember(@RequestParam String memberId,
			@RequestParam String password, Model model) {
		// ServiceのauthenticateMemberメソッドを呼び出し
		// 戻り値のMemberオブジェクトを取得する
		Member member = service.authenticateMember(memberId, password);

		// 該当する会員情報が存在しない場合
		if (member == null) {

			// エラーメッセージ[BIZERR101]をキー名"message"でModelに格納
			model.addAttribute("message", BIZERR101);

			// ログイン画面（/lab1/retrieve-login）を返却する
			return "/lab1/retrieve-login";
		}

		// Memberオブジェクトをキー名"member"でModelに格納
		model.addAttribute("member", member);

		return "/lab1/retrieve-member";
	}

	/**
	 * 会員情報照会画面の[ログアウト]に対応するHandlerメソッド
	 * マッピングするURL： /lab1/logout
	 * @param model Modelオブジェクト
	 * @return ログイン画面（/lab1/retrieve-login）
	 */
	@RequestMapping("/lab1/logout")
	public String logout(Model model) {
		// メッセージ[BIZMSG101]をキー名"message"でModelに格納
		model.addAttribute("message", BIZMSG101);

		return "/lab1/retrieve-login";
	}
}
