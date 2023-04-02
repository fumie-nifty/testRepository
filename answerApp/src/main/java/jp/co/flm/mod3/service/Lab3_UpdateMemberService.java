/**
 * Lab3_UpdateMemberService.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod3.service;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod3.entity.Member;

/**
 * 会員情報更新Service
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
public interface Lab3_UpdateMemberService {

	/**
	 * 会員情報の認証を行う
	 * @param memberId 会員ID
	 * @param password パスワード
	 * @return {@link Member}オブジェクト
	 */
	public Member authenticateMember(String memberId, String password);

	/**
	 * 会員情報の更新を行う
	 * @param member {@link Member}オブジェクト
	 * @throws BusinessException 会員情報の更新ができなかった場合
	 */
	public void setData(Member member) throws BusinessException;

}
