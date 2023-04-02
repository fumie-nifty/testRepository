/**
 * Lab4_RetrieveCategoryService.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.service;

import java.util.List;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Category;

/**
 * カテゴリー検索Service
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
public interface Lab4_RetrieveCategoryService {

	/**
	 * 全カテゴリー情報を検索する
	 * @return {@link List}オブジェクト
	 * @throws BusinessException 検索結果が存在しない場合
	 */
	public List<Category> getAllCategories() throws BusinessException;

}
