/**
 * MemberMapper.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod3.mapper;

import jp.co.flm.mod3.entity.Member;

/**
 * Memberテーブルを利用するMapperインターフェイス
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
public interface MemberMapper {

	/**
	 * 会員情報を照会する
	 * @param memberId 会員ID
	 * @param password パスワード
	 * @return {@link Member}オブジェクト
	 */
	public Member findOne(String memberId, String password);

	/**
	 * 会員情報を更新する
	 * @param member {@link Member}オブジェクト
	 * @return 更新結果を表すboolean値（成功の場合{@code true} を返す）
	 */
	public boolean update(Member member);

}
