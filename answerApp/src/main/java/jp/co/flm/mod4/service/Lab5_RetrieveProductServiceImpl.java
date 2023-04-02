/**
 * Lab5_RetrieveProductServiceImpl.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.service;

import static jp.co.flm.common.util.MessageList.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Product;
import jp.co.flm.mod4.mapper.RetrieveProductMapper;

/**
 * 商品検索Serviceの実装クラス
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Service
public class Lab5_RetrieveProductServiceImpl
		implements Lab5_RetrieveProductService {

	/** Mapper */
	@Autowired
	private RetrieveProductMapper mapper;

	/**
	 * @see Lab5_RetrieveProductService#getAllProductByCategoryId(String)
	 */
	@Override
	public List<Product> getAllProductByCategoryId(String categoryId) {
		// MapperのfindAllByメソッドを呼び出し
		// 戻り値のList<Product>オブジェクトを取得する
		List<Product> productList = mapper.findAllBy(categoryId);

		// 検索結果が存在しない場合
		if (productList.isEmpty()) {

			// 業務エラーを明示的に発生させる（エラーメッセージ[BIZERR401]）
			throw new BusinessException(BIZERR401);
		}

		return productList;
	}

	/**
	 * @see Lab5_RetrieveProductService#getProduct(String)
	 */
	@Override
	public Product getProduct(String productId) {
		// MapperのfindOneメソッドを呼び出し
		// 戻り値のProductオブジェクトを取得する
		Product product = mapper.findOne(productId);

		// 検索結果が存在しない場合
		if (product == null) {

			// 業務エラーを明示的に発生させる（エラーメッセージ[BIZERR401]）
			throw new BusinessException(BIZERR401);
		}

		return product;
	}
}
