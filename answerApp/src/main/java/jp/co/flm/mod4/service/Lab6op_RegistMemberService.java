/**
 * Lab6_RegistMemberService.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.service;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Member;

/**
 * 会員登録Service
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
public interface Lab6op_RegistMemberService {

	/**
	 * Eメールアドレスの重複チェックを行う
	 * @param email メールアドレス
	 * @throws BusinessException メールアドレスが重複している場合
	 */
	public void validateEmail(String email) throws BusinessException;

	/**
	 * 会員情報をDBに登録する
	 * @param member {@link Member}オブジェクト
	 * @throws BusinessException DBに登録できなかった場合
	 */
	public void registMember(Member member) throws BusinessException;

}
