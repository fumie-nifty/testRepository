/**
 * BusinessException.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.common.exception;

/**
 * 業務例外クラス
 * 本例外は各Controllerの＠ExceptionHandlerでハンドリングされる
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
public class BusinessException extends RuntimeException {

	/**
	 * 業務例外を発生させる
	 * @param message エラーメッセージ
	 * @see RuntimeException#RuntimeException(String message)
	 */
	public BusinessException(String message) {
		super(message);
	}

	/**
	 * 業務例外を発生させる
	 * @param message エラーメッセージ
	 * @param cause 原因
	 * @see RuntimeException#RuntimeException(String message, Throwable cause)
	 */
	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}
}
