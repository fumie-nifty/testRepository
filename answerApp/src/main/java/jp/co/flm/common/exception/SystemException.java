/**
 * SystemException.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.common.exception;

/**
 * システム例外クラス
 * 本例外は@ControllerAdviceによりハンドリングされる
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
public class SystemException extends RuntimeException {

	/**
	 * システム例外を発生させる
	 * @see RuntimeException#RuntimeException()
	 */
	public SystemException() {
		super();
	}
}
