/**
 * Lab5_RetrieveProductService.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.service;

import java.util.List;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Product;

/**
 * 商品検索Service
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
public interface Lab5_RetrieveProductService {

	/**
	 * カテゴリーIDをもとに商品情報を検索する
	 * @param categoryId カテゴリーID
	 * @return {@link List}オブジェクト
	 * @throws BusinessException 検索結果が存在しない場合
	 */
	public List<Product> getAllProductByCategoryId(String categoryId)
			throws BusinessException;

	/**
	 * 商品IDをもとに商品情報を検索する
	 * @param productId 商品ID
	 * @return {@link Product}オブジェクト
	 * @throws BusinessException 検索結果が存在しない場合
	 */
	public Product getProduct(String productId) throws BusinessException;

}
