/**
 * Lab4_RetrieveCategoryServiceImpl.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.service;

import static jp.co.flm.common.util.MessageList.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Category;
import jp.co.flm.mod4.mapper.CategoryMapper;

/**
 * カテゴリー検索Serviceの実装クラス
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Service
public class Lab4_RetrieveCategoryServiceImpl
		implements Lab4_RetrieveCategoryService {

	/** Mapper */
	@Autowired
	private CategoryMapper mapper;

	/**
	 * @see Lab4_RetrieveCategoryService#getAllCategories()
	 */
	@Override
	public List<Category> getAllCategories() {
		// MapperのfindAllメソッドを呼び出し
		// 戻り値のList<Category>オブジェクトを取得する
		List<Category> categoryList = mapper.findAll();

		// 検索結果が存在しない場合
		if (categoryList.isEmpty()) {

			// 業務エラーを明示的に発生させる（エラーメッセージ[BIZERR401]）
			throw new BusinessException(BIZERR401);
		}

		return categoryList;
	}
}
