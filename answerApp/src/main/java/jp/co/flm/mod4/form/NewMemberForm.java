/**
 * NewMemberForm.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.form;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会員登録情報入力フォーム
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewMemberForm implements Serializable {

	/** 会員ID（Eメールアドレス） */
	private String memberId;

	/** パスワード */
	@Size(min = 4, max = 8)
	@NotBlank
	private String password;

	/** 確認用パスワード */
	@Size(min = 4, max = 8)
	@NotBlank
	private String confirmPassword;

	/** 会員名 */
	@Size(max = 40)
	@NotBlank
	private String memberName;

	/** 性別 */
	@Size(max = 1)
	@NotBlank
	private String gender;

	/** 住所 */
	@Size(max = 80)
	@NotBlank
	private String address;

	/** 電話番号 */
	@Size(max = 15)
	@Pattern(regexp = "\\d{2,3}-\\d{3,4}-\\d{4}")
	private String phone;

}
