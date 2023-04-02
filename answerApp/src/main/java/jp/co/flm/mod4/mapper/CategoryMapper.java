/**
 * CategoryMapper.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import jp.co.flm.mod4.entity.Category;

/**
 * Categoryテーブルを利用するMapperインターフェイス
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Mapper
public interface CategoryMapper {

	/**
	 * カテゴリー情報のリストを取得する
	 * @return {@link List}オブジェクト
	 */
	public List<Category> findAll();

}
