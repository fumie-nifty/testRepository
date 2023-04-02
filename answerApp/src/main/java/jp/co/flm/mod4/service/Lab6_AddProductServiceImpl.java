/**
 * Lab6_AddProductServiceImpl.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.service;

import static jp.co.flm.common.util.MessageList.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Product;
import jp.co.flm.mod4.mapper.AddProductMapper;

/**
 * 商品追加Serviceの実装クラス
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Service
public class Lab6_AddProductServiceImpl implements Lab6_AddProductService {

	/** Mapper */
	@Autowired
	private AddProductMapper mapper;

	/**
	 * @see Lab6_AddProductService#getInfoForNewProduct(String)
	 */
	@Override
	public Product getInfoForNewProduct(String categoryId) {
		// MapperのgetLastメソッドを呼び出し
		// 戻り値のProductオブジェクトを取得する
		Product product = mapper.getLast(categoryId);

		// 新商品用の商品IDを設定
		int lastIndex = Integer.parseInt(product.getProductId().substring(1));
		int newIndex = 1 + lastIndex;
		product.setProductId(categoryId + String.format("%02d", newIndex));
		return product;
	}

	/**
	 * @see Lab6_AddProductService#addProduct(Product)
	 */
	@Transactional
	@Override
	public void addProduct(Product product) {
		try {

			// MapperのsaveProductメソッドを呼び出す
			mapper.saveProduct(product);

			// MapperのsaveStockメソッドを呼び出す
			mapper.saveStock(product.getProductId(), product.getQuantity());

			// 商品IDが重複している場合
		} catch (DuplicateKeyException e) {

			// 業務エラーを明示的に発生させる（エラーメッセージ[BIZERR611]）
			throw new BusinessException(BIZERR611, e);
		}
	}
}
