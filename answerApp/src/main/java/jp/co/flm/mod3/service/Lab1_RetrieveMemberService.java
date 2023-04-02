/**
 * Lab1_RetrieveMemberService.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod3.service;

import jp.co.flm.mod3.entity.Member;

/**
 * 会員情報照会Service
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
public interface Lab1_RetrieveMemberService {

	/**
	 * 会員情報の認証を行う
	 * @param memberId 会員ID
	 * @param password パスワード
	 * @return {@link Member}オブジェクト
	 */
	public Member authenticateMember(String memberId, String password);

}
