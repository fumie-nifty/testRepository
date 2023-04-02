/**
 * NewProductForm.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.form;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新商品情報入力フォーム
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewProductForm implements Serializable {

	/** 商品ID */
	private String productId;

	/** 商品名 */
	@Size(max = 40)
	@NotBlank
	private String productName;

	/** カテゴリーID */
	private String categoryId;

	/** カテゴリー名 */
	private String categoryName;

	/** 単価 */
	@Min(value = 1)
	@Max(value = 9999999)
	@NotNull
	private Integer price;

	/** 商品画像 */
	private String picture;

	/** ポイント */
	private int point;

	/** 在庫 */
	@Min(value = 1)
	@Max(value = 100)
	@NotNull
	private Integer quantity;

}
