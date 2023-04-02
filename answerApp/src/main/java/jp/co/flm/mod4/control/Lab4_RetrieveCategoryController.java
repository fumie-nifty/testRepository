/**
 * Lab4_RetrieveCategoryController.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Category;
import jp.co.flm.mod4.service.Lab4_RetrieveCategoryService;

/**
 * カテゴリー検索Controller
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Controller
public class Lab4_RetrieveCategoryController {

	/** Service */
	@Autowired
	private Lab4_RetrieveCategoryService service;

	/**
	 * TOP画面の[実習4]に対応するHandlerメソッド.
	 * マッピングするURL： /lab4/retrieveAllCategories
	 * @param model Modelオブジェクト
	 * @return 商品カテゴリー画面（/lab4/retrieve-category）
	 */
	@RequestMapping("/lab4/retrieveAllCategories")
	public String retrieveAllCategories(Model model) {
		// ServiceのgetAllCategoriesメソッドを呼び出し
		// 戻り値のList<Category>オブジェクトを取得する
		List<Category> categoryList = service.getAllCategories();

		// List<Category>オブジェクトをキー名"categoryList"でModelに格納
		model.addAttribute("categoryList", categoryList);

		return "/lab4/retrieve-category";
	}

	/**
	 * 業務例外（検索結果が存在しない場合）のハンドリング
	 * レスポンスステータスコード： HttpStatus.BAD_REQUEST
	 * ハンドリングする例外クラス： BusinessException.class
	 * @param model Modelオブジェクト
	 * @param e 例外オブジェクト
	 * @return エラー画面（/error）
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BusinessException.class)
	public String catchBizException(Model model, Exception e) {
		// エラーメッセージをキー名"message"でModelに格納
		model.addAttribute("message", e.getMessage());

		return "/error";
	}
}
