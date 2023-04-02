/**
 * AddProductMapper.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.flm.mod4.entity.Product;

/**
 * Productテーブルを利用するMapperインターフェイス
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Mapper
public interface AddProductMapper {

	/**
	 * カテゴリー内の最後の商品情報を取得する
	 * @param categoryId カテゴリーID
	 * @return {@link Product}オブジェクト
	 */
	public Product getLast(String categoryId);

	/**
	 * 商品情報をDBに登録する
	 * @param product {@link Product}オブジェクト
	 */
	public void saveProduct(Product product);

	/**
	 * 在庫情報をDBに登録する
	 * @param productId 商品ID
	 * @param quantity 在庫数
	 */
	public void saveStock(@Param("productId") String productId,
			@Param("quantity") int quantity);

}
