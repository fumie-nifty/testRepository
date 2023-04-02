/**
 * SystemExceptionHandler.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.common.exception;

import static jp.co.flm.common.util.MessageList.*;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * システム例外をハンドリングするクラス
 * すべてのControllerクラスで発生するシステム例外をハンドリングする
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@ControllerAdvice
public class SystemExceptionHandler {

	/**
	 * システム例外（SystemException・DataAccessException）をハンドリングする
	 * レスポンスステータスコード： HttpStatus.INTERNAL_SERVER_ERROR
	 * ハンドリングする例外クラス： { SystemException.class, DataAccessException.class }
	 * @param model モデル
	 * @return エラー画面（/error）
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ SystemException.class, DataAccessException.class })
	public String handleError(Model model) {
		model.addAttribute("message", SYSERR000);
		return "/error";
	}
}
