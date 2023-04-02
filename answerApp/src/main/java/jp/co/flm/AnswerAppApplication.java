/**
 * AnswerAppApplication.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * アプリケーションのエントリーポイント
 * @author FLM
 * @version 1.0.0
 */
@SpringBootApplication
public class AnswerAppApplication {

	/**
	 * Springアプリケーションを起動する
	 * @param args コマンドライン引数
	 */
	public static void main(String[] args) {
		SpringApplication.run(AnswerAppApplication.class, args);
	}
}
