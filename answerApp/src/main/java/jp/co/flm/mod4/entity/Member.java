/**
 * Member.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会員情報を管理するエンティティクラス
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member implements Serializable {

	/** 会員ID */
	private String memberId;

	/** パスワード */
	private String password;

	/** 会員名 */
	private String memberName;

	/** 性別 */
	private String gender;

	/** 住所 */
	private String address;

	/** 電話番号 */
	private String phone;

	/** ポイント */
	private int memberPoint;

}
