/**
 * Lab6op_RegistMemberController.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.control;

import static jp.co.flm.common.util.MessageList.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Member;
import jp.co.flm.mod4.form.EmailForm;
import jp.co.flm.mod4.form.NewMemberForm;
import jp.co.flm.mod4.service.Lab6op_RegistMemberService;

/**
 * 会員登録Controller
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SessionAttributes(types = NewMemberForm.class)
@Controller
public class Lab6op_RegistMemberController {

	/** Service */
	@Autowired
	private Lab6op_RegistMemberService service;

	/**
	 * TOP画面の[実習6（オプション）]に対応するHandlerメソッド
	 * マッピングするURL： /lab6op/inputEmail
	 * @param model Modelオブジェクト
	 * @return メールアドレス入力画面（/lab6op/email-input）
	 */
	@RequestMapping("/lab6op/inputEmail")
	public String inputEmail(Model model) {
		// フォームオブジェクトをキー名"emailForm"でModelに格納
		model.addAttribute("emailForm", new EmailForm());

		return "/lab6op/email-input";
	}

	/**
	 * メールアドレス入力画面の[次へ]に対応するHandlerメソッド
	 * マッピングするURL： /lab6op/inputMember
	 * マッピングするHTTPメソッド： POST
	 * @param emailForm メールアドレス入力フォームオブジェクト
	 * @param result 入力値検証結果オブジェクト
	 * @param model Modelオブジェクト
	 * @return 会員登録画面（/lab6op/registration-input）
	 */
	@RequestMapping(value = "/lab6op/inputMember", method = RequestMethod.POST)
	public String inputMember(@Validated EmailForm emailForm,
			BindingResult result, Model model) {
		// 入力チェック
		if (result.hasErrors()) {

			// メールアドレス入力画面（/lab6op/email-input）を返却する
			return "/lab6op/email-input";
		}

		// ServiceのvalidateEmailメソッドを呼び出す
		String email = emailForm.getEmail();
		service.validateEmail(email);

		// 会員情報入力フォームに初期値を設定する
		NewMemberForm newMember = new NewMemberForm();
		newMember.setMemberId(email);
		newMember.setGender("M");

		// フォームオブジェクトをキー名"newMember"でModelに格納
		model.addAttribute("newMember", newMember);

		return "/lab6op/registration-input";
	}

	/**
	 * 会員登録画面の[確認]に対応するHandlerメソッド
	 * マッピングするURL： /lab6op/confirmMember
	 * マッピングするHTTPメソッド： POST
	 * @param newMember 会員情報入力フォームオブジェクト
	 * @param result 入力値検証結果オブジェクト
	 * @param model Modelオブジェクト
	 * @return 会員登録確認画面（/lab6op/registration-confirm）
	 */
	@RequestMapping(value = "/lab6op/confirmMember",
			method = RequestMethod.POST)
	public String confirmMember(
			@ModelAttribute("newMember") @Validated NewMemberForm newMember,
			BindingResult result, Model model) {
		// 入力チェック
		if (result.hasErrors()) {

			// 会員登録画面（/lab6op/registration-input）を返却する
			return "/lab6op/registration-input";
		}

		// パスワードと確認用パスワードのチェック
		if (!newMember.getPassword().equals(newMember.getConfirmPassword())) {

			// エラーメッセージ[BIZERR602]をキー名"message"でModelに格納
			model.addAttribute("message", BIZERR602);

			// 会員登録画面（/lab6op/registration-input）を返却する
			return "/lab6op/registration-input";
		}

		// フォームオブジェクトをキー名"newMember"でModelに格納
		model.addAttribute("newMember", newMember);

		return "/lab6op/registration-confirm";
	}

	/**
	 * 会員登録確認画面の[登録確定]に対応するHandlerメソッド
	 * マッピングするURL： /lab6op/commitMember
	 * マッピングするHTTPメソッド： POST
	 * @param newMember 会員情報入力フォームオブジェクト
	 * @param model Modelオブジェクト
	 * @param status セッションステータス
	 * @return 会員登録完了画面（/lab6op/registration-complete）
	 */
	@RequestMapping(value = "/lab6op/commitMember",
			method = RequestMethod.POST)
	public String commitMember(
			@ModelAttribute("newMember") NewMemberForm newMember, Model model,
			SessionStatus status) {
		// フォームオブジェクトに格納された情報を会員情報オブジェクトに設定する
		Member member
				= new Member(newMember.getMemberId(), newMember.getPassword(),
						newMember.getMemberName(), newMember.getGender(),
						newMember.getAddress(), newMember.getPhone(), 0);

		// ServiceのregistMemberメソッドを呼び出す
		service.registMember(member);

		// セッションからフォームオブジェクトを削除
		status.setComplete();

		// 会員情報オブジェクトをキー名"member"でModelに格納
		model.addAttribute("member", member);

		return "/lab6op/registration-complete";
	}

	/**
	 * 会員登録確認画面の[修正]に対応するHandlerメソッド
	 * マッピングするURL： /lab6op/reviseInput
	 * @param newMember 会員情報入力フォームオブジェクト
	 * @return 会員登録画面（/lab6op/registration-input）
	 */
	@RequestMapping("/lab6op/reviseInput")
	public String reviseInput(
			@ModelAttribute("newMember") NewMemberForm newMember) {
		return "/lab6op/registration-input";
	}

	/**
	 * 業務例外（メールアドレスが重複している場合）のハンドリング
	 * レスポンスステータスコード： HttpStatus.BAD_REQUEST
	 * ハンドリングする例外クラス： BusinessException.class
	 * @param model Modelオブジェクト
	 * @param e 例外オブジェクト
	 * @return メールアドレス入力画面（/lab6op/email-input）
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BusinessException.class)
	public String catchBizException(Model model, Exception e) {
		// エラーメッセージをキー名"message"でModelに格納
		model.addAttribute("message", e.getMessage());

		// フォームオブジェクトをキー名"emailForm"でModelに格納
		model.addAttribute("emailForm", new EmailForm());

		return "/lab6op/email-input";
	}

	/**
	 * セッションが無効になった場合のハンドリング
	 * レスポンスステータスコード： HttpStatus.BAD_GATEWAY
	 * ハンドリングする例外クラス： HttpSessionRequiredException.class
	 * @param model Modelオブジェクト
	 * @return メールアドレス入力画面（/lab6op/email-input）
	 */
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	@ExceptionHandler(HttpSessionRequiredException.class)
	public String sessionExpired(Model model) {
		// エラーメッセージ[BIZERR302]をキー名"message"でModelに格納
		model.addAttribute("message", BIZERR302);

		// フォームオブジェクトをキー名"emailForm"でModelに格納
		model.addAttribute("emailForm", new EmailForm());

		return "/lab6op/email-input";
	}
}
