/**
 * Lab6_AddProductController.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.control;

import static jp.co.flm.common.util.MessageList.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Product;
import jp.co.flm.mod4.form.NewProductForm;
import jp.co.flm.mod4.service.Lab6_AddProductService;

/**
 * 商品追加Controller
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SessionAttributes(types = NewProductForm.class)
@Controller
public class Lab6_AddProductController {

	/** Service */
	@Autowired
	private Lab6_AddProductService service;

	/**
	 * TOP画面の[実習6]、商品検索画面の[商品追加]に対応するHandlerメソッド
	 * マッピングするURL： /lab6/inputNewProduct
	 * マッピングするパラメーター： "categoryId"
	 * @param categoryId カテゴリーID
	 * @param model Modelオブジェクト
	 * @return 新商品追加画面（/lab6/addition-input）
	 */
	@RequestMapping(value = "/lab6/inputNewProduct", params = "categoryId")
	public String inputNewProduct(@RequestParam String categoryId,
			Model model) {
		// ServiceのgetInfoForNewProductメソッドを呼び出し
		// 戻り値のProductオブジェクトを取得する
		Product product = service.getInfoForNewProduct(categoryId);

		// 新商品情報入力フォームに初期値を設定する
		NewProductForm newProduct = new NewProductForm();
		newProduct.setProductId(product.getProductId());
		newProduct.setCategoryId(product.getCategoryId());
		newProduct.setCategoryName(product.getCategoryName());
		newProduct.setPicture(product.getPicture());

		// Productオブジェクトをキー名"newProduct"でModelに格納
		model.addAttribute("newProduct", newProduct);

		return "/lab6/addition-input";
	}

	/**
	 * 新商品追加画面の[確認]に対応するHandlerメソッド
	 * マッピングするURL： /lab6/confirmAddition
	 * マッピングするHTTPメソッド： POST
	 * @param newProduct 新商品情報入力フォームオブジェクト
	 * @param result 入力値検証結果オブジェクト
	 * @param model Modelオブジェクト
	 * @return 新商品追加確認画面（/lab6/addition-confirm）
	 */
	@RequestMapping(value = "/lab6/confirmAddition",
			method = RequestMethod.POST)
	public String confirmAddition(
			@ModelAttribute("newProduct") @Validated NewProductForm newProduct,
			BindingResult result, Model model) {
		// 入力チェック
		if (result.hasErrors()) {

			// 新商品追加画面（/lab6/addition-input）を返却する
			return "/lab6/addition-input";
		}

		// 単価の1％をポイントに設定
		newProduct.setPoint((int) (newProduct.getPrice() * 0.01));

		// フォームオブジェクトをキー名"newProduct"でModelに格納
		model.addAttribute("newProduct", newProduct);

		return "/lab6/addition-confirm";
	}

	/**
	 * 新商品追加確認画面の[更新確定]に対応するHandlerメソッド
	 * マッピングするURL： /lab6/commitAddition
	 * マッピングするHTTPメソッド： POST
	 * @param newProduct 新商品情報入力フォームオブジェクト
	 * @param status セッションステータス
	 * @return 新商品追加完了画面（/lab6/addition-complete）
	 */
	@RequestMapping(value = "/lab6/commitAddition", method = RequestMethod.POST)
	public String commitAddition(
			@ModelAttribute("newProduct") NewProductForm newProduct,
			SessionStatus status) {
		// セッションに格納した商品情報から登録するProductオブジェクトを作成する
		Product product = new Product(newProduct.getProductId(),
				newProduct.getProductName(), newProduct.getCategoryId(),
				newProduct.getCategoryName(), newProduct.getPrice(),
				newProduct.getPicture(), newProduct.getPoint(),
				newProduct.getQuantity());

		// ServiceのaddProductメソッドを呼び出す
		service.addProduct(product);

		// セッションからフォームオブジェクトを削除
		status.setComplete();

		return "/lab6/addition-complete";
	}

	/**
	 * 新商品追加確認画面の[修正]に対応するHandlerメソッド
	 * マッピングするURL： /lab6/reviseInput
	 * @param newProduct 新商品情報入力フォームオブジェクト
	 * @return 新商品追加画面（/lab6/addition-input）
	 */
	@RequestMapping("/lab6/reviseInput")
	public String reviseInput(
			@ModelAttribute("newProduct") NewProductForm newProduct) {
		return "/lab6/addition-input";
	}

	/**
	 * 業務例外（商品IDが重複した場合）のハンドリング
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

	/**
	 * セッションが無効になった場合のハンドリング
	 * レスポンスステータスコード： HttpStatus.BAD_GATEWAY
	 * ハンドリングする例外クラス： HttpSessionRequiredException.class
	 * @param model Modelオブジェクト
	 * @return エラー画面（/error）
	 */
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	@ExceptionHandler(HttpSessionRequiredException.class)
	public String sessionExpired(Model model) {
		// エラーメッセージ[BIZERR302]をキー名"message"でModelに格納
		model.addAttribute("message", BIZERR302);

		return "/error";
	}
}
