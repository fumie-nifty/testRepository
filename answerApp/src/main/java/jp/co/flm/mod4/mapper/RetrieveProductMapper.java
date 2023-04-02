/**
 * RetrieveProductMapper.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import jp.co.flm.mod4.entity.Product;

/**
 * Productテーブルを利用するMapperインターフェイス
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Mapper
public interface RetrieveProductMapper {

	/**
	 * 商品情報のリストを取得する
	 * @param categoryId カテゴリーID
	 * @return {@link List}オブジェクト
	 */
	public List<Product> findAllBy(String categoryId);

	/**
	 * 商品情報を取得する
	 * @param productId 商品ID
	 * @return {@link Product}オブジェクト
	 */
	public Product findOne(String productId);

}
