/**
 * MemberMapperMock.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod3.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jp.co.flm.common.exception.SystemException;
import jp.co.flm.mod3.entity.Member;

/**
 * MemberMapperのモック
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Component
public class MemberMapperMock implements MemberMapper {

	/** 従業員情報を保持するリスト */
	private List<Member> list = new ArrayList<>();

	/** コンストラクター */
	public MemberMapperMock() {
		list.add(new Member("iida@fj.co.jp", "flm123", "飯田 哲夫", "M",
				"東京都 大田区 池上0-0-0", "03-1111-2222", 652));
		list.add(new Member("aaa@fj.co.jp", "flm123", "渡邉 準", "M",
				"東京都 大田区 蒲田0-0-0", "03-2222-3333", 0));
	}

	/**
	 * @see MemberMapper#findOne(String, String)
	 */
	@Override
	public Member findOne(String memberId, String password) {
		// 引数チェック
		if (memberId == null || password == null) {
			return null;
		}

		for (Member memberInList : list) {
			if (memberId.equals(memberInList.getMemberId())
					&& password.equals(memberInList.getPassword())) {
				return memberInList;
			}
		}
		return null;
	}

	/**
	 * @see MemberMapper#update(Member)
	 */
	@Override
	public boolean update(Member member) {
		// 引数チェック
		if (member == null) {
			return false;
		}

		// システムエラーを明示的に発生させる
		if (member.getMemberName().equals("FLM")) {
			throw new SystemException();
		}

		// 業務エラーを明示的に発生させる
		if (member.getAddress().equals("FLM")) {
			return false;
		}

		for (int i = 0; i < list.size(); i++) {
			if (member.getMemberId().equals(list.get(i).getMemberId())) {
				list.set(i, member);
				return true;
			}
		}
		return false;
	}
}
