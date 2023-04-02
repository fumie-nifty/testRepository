/**
 * Lab3_UpdateMemberController.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod3.control;

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
import jp.co.flm.mod3.entity.Member;
import jp.co.flm.mod3.form.UpdateMemberForm;
import jp.co.flm.mod3.service.Lab3_UpdateMemberService;

/**
 * 会員更新Controller
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SessionAttributes(types = UpdateMemberForm.class)
@Controller
public class Lab3_UpdateMemberController {

	/** Service */
	@Autowired
	private Lab3_UpdateMemberService service;

	/**
	 * TOP画面の[実習3]に対応するHandlerメソッド
	 * マッピングするURL： /lab3/inputUpdate
	 * @param model Modelオブジェクト
	 * @return 会員更新画面（/lab3/update-input）
	 */
	@RequestMapping("/lab3/inputUpdate")
	public String inputUpdate(Model model) {
		// 更新前の情報をフォームオブジェクトの初期値に設定する
		Member member = service.authenticateMember("iida@fj.co.jp", "flm123");
		UpdateMemberForm updateMember = new UpdateMemberForm();
		updateMember.setMemberId(member.getMemberId());
		updateMember.setPassword(member.getPassword());
		updateMember.setMemberName(member.getMemberName());
		updateMember.setPhone(member.getPhone());
		updateMember.setAddress(member.getAddress());
		updateMember.setGender(member.getGender());
		updateMember.setMemberPoint(member.getMemberPoint());

		// フォームオブジェクトをキー名"updateMember"でModelに格納
		model.addAttribute("updateMember", updateMember);

		return "/lab3/update-input";
	}

	/**
	 * 会員更新画面の[確認]に対応するHandlerメソッド
	 * マッピングするURL： /lab3/confirmUpdate
	 * マッピングするHTTPメソッド： POST
	 * @param updateMember 会員更新情報入力フォームオブジェクト
	 * @param result 入力値検証結果オブジェクト
	 * @param model Modelオブジェクト
	 * @return 会員更新確認画面（/lab3/update-confirm）
	 */
	@RequestMapping(value = "/lab3/confirmUpdate", method = RequestMethod.POST)
	public String confirmUpdate(
			@ModelAttribute("updateMember") @Validated UpdateMemberForm updateMember,
			BindingResult result, Model model) {
		// 入力チェック
		if (result.hasErrors()) {

			// 会員更新画面（/lab3/update-input）を返却する
			return "/lab3/update-input";
		}

		// フォームオブジェクトをキー名"updateMember"でModelに格納
		model.addAttribute("updateMember", updateMember);

		return "/lab3/update-confirm";
	}

	/**
	 * 会員更新画面の[更新確定]に対応するHandlerメソッド
	 * マッピングするURL： /lab3/commitUpdate
	 * マッピングするHTTPメソッド： POST
	 * @param updateMember 会員更新情報入力フォームオブジェクト
	 * @param model Modelオブジェクト
	 * @param status セッションステータス
	 * @return 会員更新結果画面（/lab3/update-complete）
	 */
	@RequestMapping(value = "/lab3/commitUpdate", method = RequestMethod.POST)
	public String commitUpdate(
			@ModelAttribute("updateMember") UpdateMemberForm updateMember,
			Model model, SessionStatus status) {
		// フォームオブジェクトに格納された情報をMemberオブジェクトに設定する
		Member update = new Member(updateMember.getMemberId(),
				updateMember.getPassword(), updateMember.getMemberName(),
				updateMember.getGender(), updateMember.getAddress(),
				updateMember.getPhone(), updateMember.getMemberPoint());

		// ServiceのsetDataメソッドを呼び出す
		service.setData(update);

		// Memberオブジェクトをキー名"member"でModelに格納
		model.addAttribute("member", update);

		// セッションからフォームオブジェクトを削除
		status.setComplete();

		return "/lab3/update-complete";
	}

	/**
	 * 会員更新確認画面の[修正]に対応するHandlerメソッド
	 * マッピングするURL： /lab3/reviseInput
	 * @param updateMember 会員更新情報入力フォームオブジェクト
	 * @return 会員更新画面（/lab3/update-input）
	 */
	@RequestMapping("/lab3/reviseInput")
	public String reviseInput(
			@ModelAttribute("updateMember") UpdateMemberForm updateMember) {
		return "/lab3/update-input";
	}

	/**
	 * 業務例外のハンドリング
	 * レスポンスステータスコード： HttpStatus.BAD_REQUEST
	 * ハンドリングする例外クラス： BusinessException.class
	 * @param model Modelオブジェクト
	 * @param e 例外オブジェクト
	 * @return 会員更新画面（/lab3/update-input）
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BusinessException.class)
	public String catchBizException(Model model, Exception e) {
		// エラーメッセージをキー名"message"でModelに格納
		model.addAttribute("message", e.getMessage());

		// 更新前の情報をフォームオブジェクトの初期値に設定する
		Member member = service.authenticateMember("iida@fj.co.jp", "flm123");
		UpdateMemberForm updateMember = new UpdateMemberForm();
		updateMember.setMemberId(member.getMemberId());
		updateMember.setPassword(member.getPassword());
		updateMember.setMemberName(member.getMemberName());
		updateMember.setPhone(member.getPhone());
		updateMember.setAddress(member.getAddress());
		updateMember.setGender(member.getGender());
		updateMember.setMemberPoint(member.getMemberPoint());

		// フォームオブジェクトをキー名"updateMember"でModelに格納
		model.addAttribute("updateMember", updateMember);

		return "/lab3/update-input";
	}

	/**
	 * セッションが無効になった場合のハンドリング
	 * レスポンスステータスコード： HttpStatus.BAD_GATEWAY
	 * ハンドリングする例外クラス： HttpSessionRequiredException.class
	 * @param model Modelオブジェクト
	 * @return 会員更新画面（/lab3/update-input）
	 */
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	@ExceptionHandler(HttpSessionRequiredException.class)
	public String sessionExpired(Model model) {
		// エラーメッセージ[BIZERR302]をキー名"message"でModelに格納
		model.addAttribute("message", BIZERR302);

		// 更新前の情報をフォームオブジェクトの初期値に設定する
		Member member = service.authenticateMember("iida@fj.co.jp", "flm123");
		UpdateMemberForm updateMember = new UpdateMemberForm();
		updateMember.setMemberId(member.getMemberId());
		updateMember.setPassword(member.getPassword());
		updateMember.setMemberName(member.getMemberName());
		updateMember.setPhone(member.getPhone());
		updateMember.setAddress(member.getAddress());
		updateMember.setGender(member.getGender());
		updateMember.setMemberPoint(member.getMemberPoint());

		// フォームオブジェクトをキー名"updateMember"でModelに格納
		model.addAttribute("updateMember", updateMember);

		return "/lab3/update-input";
	}
}
