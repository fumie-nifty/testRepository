/**
 * LoggingAspect.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AOPを利用したログ出力コンポーネント
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Aspect
public class LoggingAspect {

	/** ロガー */
	private static final Logger LOGGER
			= LoggerFactory.getLogger(LoggingAspect.class);

	/**
	 * Serviceのメソッド開始時にログを出力する
	 * @param point 織り込み箇所
	 */
	@Before(value = "within(jp.co.flm.*.service.*)")
	public void logBeforeService(JoinPoint point) {
		LOGGER.debug(getSignature(point) + " : START");
	}

	/**
	 * Serviceのメソッド終了時にログを出力する
	 * @param point 織り込み箇所
	 * @param returnValue 戻り値
	 */
	@AfterReturning(value = "within(jp.co.flm.*.service.*)",
			returning = "returnValue")
	public void logAfterReturnService(JoinPoint point, Object returnValue) {
		LOGGER.debug(getSignature(point) + " : END");
		LOGGER.debug(" ->> RETURNS : " + returnValue);
	}

	/**
	 * Serviceのメソッドで例外発生時にログを出力する
	 * @param exception 例外
	 */
	@AfterThrowing(value = "within(jp.co.flm.*.service.*)",
			throwing = "exception")
	public void logAfterThrowService(Exception exception) {
		LOGGER.error(exception.toString());
		StackTraceElement[] trace = exception.getStackTrace();
		for (StackTraceElement stackTraceElement : trace) {
			LOGGER.error(stackTraceElement.toString());
		}
	}

	/**
	 * Mapperのメソッド開始/終了時にログを出力する
	 * @param point 織り込み箇所
	 * @return SQL実行結果
	 * @throws Throwable 例外
	 */
	@Around(value = "execution(* *..*Mapper.*(..))")
	public Object logAroundMapper(ProceedingJoinPoint point) throws Throwable {
		String signature = getSignature(point);
		LOGGER.debug(signature + " : START");
		Object returnValue = point.proceed();
		LOGGER.debug(signature + " : END");
		LOGGER.debug(" ->> RETURNS : " + returnValue);
		return returnValue;
	}

	/**
	 * Controllerのメソッド開始/終了時にログを出力する
	 * @param point 織り込み箇所
	 * @return 次画面名
	 * @throws Throwable 例外
	 */
	@Around(value = "execution(* *..*Controller.*(..))")
	public Object logAroundController(ProceedingJoinPoint point)
			throws Throwable {
		String signature = getSignature(point);
		LOGGER.debug(signature + " : START");
		Object returnValue = point.proceed();
		LOGGER.debug(signature + " : END");
		LOGGER.debug(" ->> RETURNS : " + returnValue);
		return returnValue;
	}

	/**
	 * 織り込み対象のシグニチャを取得する
	 * @param point 織り込み箇所
	 * @return 織り込み対象のシグニチャ
	 */
	private String getSignature(JoinPoint point) {
		return point.getSignature().getDeclaringType().getSimpleName() + "#"
				+ point.getSignature().getName();
	}
}
