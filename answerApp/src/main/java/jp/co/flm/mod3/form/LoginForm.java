/**
 * LoginForm.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod3.form;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ログイン情報入力フォーム
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm implements Serializable {

	/** 会員ID */
	@Email
	@Size(max = 50)
	@NotBlank
	private String memberId;

	/** パスワード */
	@Size(min = 4, max = 8)
	@NotBlank
	private String password;

}
