/**
 * Category.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品カテゴリー情報を管理するエンティティクラス
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

	/** カテゴリーID */
	private String categoryId;

	/** カテゴリー名 */
	private String categoryName;

	/** カテゴリー画像 */
	private String picture;

}
