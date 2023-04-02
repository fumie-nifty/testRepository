/**
 * MemberMapper.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.mapper;

import org.apache.ibatis.annotations.Mapper;

import jp.co.flm.mod4.entity.Member;

/**
 * Memberテーブルを利用するMapperインターフェイス
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Mapper
public interface MemberMapper {

	/**
	 * メールアドレスの重複チェックを行う
	 * @param email メールアドレス
	 * @return 重複を表すboolean値（重複の場合{@code true} を返す）
	 */
	public boolean exist(String email);

	/**
	 * 会員情報をDBに登録する
	 * @param member {@link Member}オブジェクト
	 */
	public void save(Member member);

}
