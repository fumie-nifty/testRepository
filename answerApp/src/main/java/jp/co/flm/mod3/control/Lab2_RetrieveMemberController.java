/**
 * Lab2_RetrieveMemberController.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod3.control;

import static jp.co.flm.common.util.MessageList.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.flm.mod3.entity.Member;
import jp.co.flm.mod3.form.LoginForm;
import jp.co.flm.mod3.service.Lab2_RetrieveMemberService;

/**
 * 会員情報照会Controller
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Controller
public class Lab2_RetrieveMemberController {

	/** Service */
	@Autowired
	private Lab2_RetrieveMemberService service;

	/**
	 * TOP画面の[実習2]に対応するHandlerメソッド
	 * マッピングするURL： /lab2/login
	 * @param model Modelオブジェクト
	 * @return ログイン画面（/lab2/retrieve-login）
	 */
	@RequestMapping("/lab2/login")
	public String login(Model model) {
		// フォームオブジェクトをキー名"loginForm"でModelに格納
		model.addAttribute("loginForm", new LoginForm());

		return "/lab2/retrieve-login";
	}

	/**
	 * ログイン画面の[ログイン]に対応するHandlerメソッド
	 * マッピングするURL： /lab2/retrieveMember
	 * マッピングするHTTPメソッド： POST
	 * @param loginForm ログイン情報入力フォームオブジェクト
	 * @param result 入力値検証結果オブジェクト
	 * @param model Modelオブジェクト
	 * @return 会員情報照会画面（/lab2/retrieve-member）
	 */
	@RequestMapping(value = "/lab2/retrieveMember",
			method = RequestMethod.POST)
	public String retrieveMember(@Validated LoginForm loginForm,
			BindingResult result, Model model) {
		// 入力チェック
		if (result.hasErrors()) {

			// ログイン画面（/lab2/retrieve-login）を返却する
			return "/lab2/retrieve-login";
		}

		// ServiceのauthenticateMemberメソッドを呼び出し
		// 戻り値のMemberオブジェクトを取得する
		Member member = service.authenticateMember(loginForm.getMemberId(),
				loginForm.getPassword());

		// 該当する会員情報が存在しない場合
		if (member == null) {

			// エラーメッセージ[BIZERR101]をキー名"message"でModelに格納
			model.addAttribute("message", BIZERR101);

			// ログイン画面（/lab2/retrieve-login）を返却する
			return "/lab2/retrieve-login";
		}

		// Memberオブジェクトをキー名"member"でModelに格納
		model.addAttribute("member", member);

		return "/lab2/retrieve-member";
	}

	/**
	 * 会員情報照会画面の[ログアウト]に対応するHandlerメソッド
	 * マッピングするURL： /lab2/logout
	 * @param model Modelオブジェクト
	 * @return ログイン画面（/lab2/retrieve-login）
	 */
	@RequestMapping("/lab2/logout")
	public String logout(Model model) {
		// メッセージ[BIZMSG101]をキー名"message"でModelに格納
		model.addAttribute("message", BIZMSG101);

		// フォームオブジェクトをキー名"loginForm"でModelに格納
		model.addAttribute("loginForm", new LoginForm());

		return "/lab2/retrieve-login";
	}
}
