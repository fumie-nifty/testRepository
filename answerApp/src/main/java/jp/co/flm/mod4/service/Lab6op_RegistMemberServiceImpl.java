/**
 * Lab6_RegistMemberServiceImpl.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.service;

import static jp.co.flm.common.util.MessageList.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Member;
import jp.co.flm.mod4.mapper.MemberMapper;

/**
 * 会員登録Serviceの実装クラス
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Service
public class Lab6op_RegistMemberServiceImpl
		implements Lab6op_RegistMemberService {

	/** Mapper */
	@Autowired
	private MemberMapper mapper;

	/**
	 * @see Lab6op_RegistMemberService#validateEmail(String)
	 */
	@Override
	public void validateEmail(String email) {
		// Mapperのexistメソッドを呼び出し
		// 戻り値のboolean値を取得する
		boolean exist = mapper.exist(email);

		// メールアドレスが重複している場合
		if (exist) {

			// 業務エラーを明示的に発生させる（エラーメッセージ[BIZERR601]）
			throw new BusinessException(BIZERR601);
		}
	}

	/**
	 * @see Lab6op_RegistMemberService#registMember(Member)
	 */
	@Override
	public void registMember(Member member) {
		try {

			// Mapperのsaveメソッドを呼び出す
			mapper.save(member);

			// メールアドレスが重複している場合
		} catch (DuplicateKeyException e) {

			// 業務エラーを明示的に発生させる（エラーメッセージ[BIZERR601]）
			throw new BusinessException(BIZERR601, e);
		}
	}
}
