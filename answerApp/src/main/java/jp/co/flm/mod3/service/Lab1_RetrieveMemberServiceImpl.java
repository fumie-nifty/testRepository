/**
 * Lab1_RetrieveMemberServiceImpl.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.flm.mod3.entity.Member;
import jp.co.flm.mod3.mapper.MemberMapper;

/**
 * 会員情報照会Serviceの実装クラス
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Service
public class Lab1_RetrieveMemberServiceImpl
		implements Lab1_RetrieveMemberService {

	/** Mapper */
	@Autowired
	private MemberMapper mapper;

	/**
	 * @see Lab1_RetrieveMemberService#authenticateMember(String, String)
	 */
	@Override
	public Member authenticateMember(String memberId, String password) {
		// MapperのfindOneメソッドを呼び出し
		// 戻り値のMemberオブジェクトを取得する
		Member member = mapper.findOne(memberId, password);

		return member;
	}
}
