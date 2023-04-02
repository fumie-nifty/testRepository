/**
 * Lab6_AddProductService.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.service;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Product;

/**
 * 商品追加Service
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
public interface Lab6_AddProductService {

	/**
	 * 商品情報を追加する商品カテゴリーの情報を取得する
	 * @param categoryId カテゴリー
	 * @return {@link Product}オブジェクト
	 */
	public Product getInfoForNewProduct(String categoryId);

	/**
	 * 商品情報をDBに格納する
	 * @param product {@link Product}オブジェクト
	 * @throws BusinessException DBに登録できなかった場合
	 */
	public void addProduct(Product product) throws BusinessException;

}
