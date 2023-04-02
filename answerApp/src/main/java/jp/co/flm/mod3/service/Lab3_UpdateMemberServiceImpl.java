/**
 * Lab3_UpdateMemberServiceImpl.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod3.service;

import static jp.co.flm.common.util.MessageList.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod3.entity.Member;
import jp.co.flm.mod3.mapper.MemberMapper;

/**
 * 会員情報更新Serviceの実装クラス
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Service
public class Lab3_UpdateMemberServiceImpl implements Lab3_UpdateMemberService {

	/** Mapper */
	@Autowired
	private MemberMapper mapper;

	/**
	 * @see Lab3_UpdateMemberService#authenticateMember(String, String)
	 */
	@Override
	public Member authenticateMember(String memberId, String password) {
		// MapperのfindOneメソッドを呼び出し
		// 戻り値のMemberオブジェクトを取得する
		Member member = mapper.findOne(memberId, password);

		return member;
	}

	/**
	 * @see Lab3_UpdateMemberService#setData(Member)
	 */
	@Override
	public void setData(Member member) {
		// Mapperのupdateメソッドを呼び出し
		// 戻り値のboolean値を取得する
		boolean result = mapper.update(member);

		// 更新に失敗した場合
		if (!result) {

			// 業務エラーを明示的に発生させる（エラーメッセージ[BIZERR301]）
			throw new BusinessException(BIZERR301);
		}
	}
}
