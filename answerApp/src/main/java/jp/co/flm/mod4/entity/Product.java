/**
 * Product.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品情報を管理するエンティティクラス
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

	/** 商品ID */
	private String productId;

	/** 商品名 */
	private String productName;

	/** カテゴリーID */
	private String categoryId;

	/** カテゴリー名 */
	private String categoryName;

	/** 単価 */
	private int price;

	/** 商品画像 */
	private String picture;

	/** ポイント */
	private int point;

	/** 在庫 */
	private int quantity;

}
