/**
 * Lab5_RetrieveProductController.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Product;
import jp.co.flm.mod4.service.Lab5_RetrieveProductService;

/**
 * 商品検索Controller
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@Controller
public class Lab5_RetrieveProductController {

	/** Service */
	@Autowired
	private Lab5_RetrieveProductService service;

	/**
	 * TOP画面の[実習5]、商品カテゴリー画面の[カテゴリー画像]に対応するHandlerメソッド
	 * マッピングするURL： /lab5/retrieveProductsByCategory/{カテゴリーID}
	 * @param categoryId カテゴリーID
	 * @param model Modelオブジェクト
	 * @return 商品検索画面（/lab5/retrieve-product）
	 */
	@RequestMapping("/lab5/retrieveProductsByCategory/{categoryId}")
	public String retrieveProductsByCategory(@PathVariable String categoryId,
			Model model) {
		// ServiceのgetAllProductByCategoryIdメソッドを呼び出し
		// 戻り値のList<Product>オブジェクトを取得する
		List<Product> productList
				= service.getAllProductByCategoryId(categoryId);

		// List<Product>オブジェクトをキー名"productList"でModelに格納
		model.addAttribute("productList", productList);

		return "/lab5/retrieve-product";
	}

	/**
	 * 商品検索画面の[詳細]に対応するHandlerメソッド
	 * マッピングするURL： /lab5/retrieveProduct/{商品ID}
	 * @param productId 商品ID
	 * @param model Modelオブジェクト
	 * @return 商品詳細画面（/lab5/retrieve-detail）
	 */
	@RequestMapping("/lab5/retrieveProduct/{productId}")
	public String retrieveProduct(@PathVariable String productId, Model model) {
		// ServiceのgetProductメソッドを呼び出し
		// 戻り値のProductオブジェクトを取得する
		Product product = service.getProduct(productId);

		// Productオブジェクトをキー名"product"でModelに格納
		model.addAttribute("product", product);

		return "/lab5/retrieve-detail";
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
