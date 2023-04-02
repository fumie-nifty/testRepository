/**
 * Lab5_RetrieveProductControllerTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.control;

import static jp.co.flm.common.util.MessageList.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Product;
import jp.co.flm.mod4.service.Lab5_RetrieveProductService;
import jp.co.flm.test.util.DummyDataAccessException;

/**
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Lab5_RetrieveProductControllerのテスト")
public class Lab5_RetrieveProductControllerTest {

	@InjectMocks
	Lab5_RetrieveProductController sut;

	@Mock
	Lab5_RetrieveProductService mock;

	@Autowired
	HandlerExceptionResolver handlerExceptionResolver;

	MockMvc mvc;

	@BeforeEach
	void setUp() throws Exception {
		mvc = standaloneSetup(sut)
				.setHandlerExceptionResolvers(handlerExceptionResolver)
				.build();
	}

	@Test
	@DisplayName("商品検索結果画面を表示する")
	void testRetrieveProductsByCategory() throws Exception {
		// setup
		String testArg_categoryId = "a";
		List<Product> returnVal = List.of(
				new Product("a01", "ALL STAR one", "a", "スポーツシューズ", 15000, "a01.gif", 150, 10),
				new Product("a02", "Star Speeder HI", "a", "スポーツシューズ", 13800, "a02.gif", 138, 10),
				new Product("a03", "Scuderia-α", "a", "スポーツシューズ", 15000, "a03.gif", 150, 10));

		// when
		when(mock.getAllProductByCategoryId(testArg_categoryId))
				.thenReturn(returnVal);

		// assert
		mvc.perform(get("/lab5/retrieveProductsByCategory/" + testArg_categoryId))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab5/retrieve-product"))
				.andExpect(model().attribute("productList", returnVal));
	}

	@Test
	@DisplayName("商品詳細画面を表示する")
	void testRetrieveProduct() throws Exception {
		// setup
		String testArg_productId = "a01";
		Product returnVal = new Product(
				"a01", "ALL STAR one", "a", "スポーツシューズ", 15000, "a01.gif", 150, 10);

		// when
		when(mock.getProduct(testArg_productId)).thenReturn(returnVal);

		// assert
		mvc.perform(get("/lab5/retrieveProduct/" + testArg_productId))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab5/retrieve-detail"))
				.andExpect(model().attribute("product", returnVal));
	}

	@Nested
	@SpringBootTest
	@DisplayName("例外処理のテスト")
	class ExceptionHandlerTest{

		@Test
		@DisplayName("商品検索結果画面表示で業務例外が発生する")
		void testRetrieveProductsByCategory_BizExOccured() throws Exception {
			// setup
			String testArg_categoryId = "z";

			// when
			when(mock.getAllProductByCategoryId(testArg_categoryId))
					.thenThrow(new BusinessException(BIZERR401));

			// assert
			mvc.perform(get("/lab5/retrieveProductsByCategory/" + testArg_categoryId))
					.andExpect(status().isBadRequest())
					.andExpect(view().name("/error"))
					.andExpect(model().attribute("message", BIZERR401));
		}

		@Test
		@DisplayName("商品検索結果画面表示でシステムエラーが発生する")
		void testRetrieveProductByCategory_SysExOccured() throws Exception {
			// setup
			String testArg_categoryId = "a";

			// when
			when(mock.getAllProductByCategoryId(testArg_categoryId))
					.thenThrow(new DummyDataAccessException());

			// assert
			mvc.perform(get("/lab5/retrieveProductsByCategory/" + testArg_categoryId))
					.andExpect(status().isInternalServerError())
					.andExpect(view().name("/error"))
					.andExpect(model().attribute("message", SYSERR000));
		}

		@Test
		@DisplayName("商品詳細画面表示で業務例外が発生する")
		void testRetrieveProduct_BizExOccured() throws Exception {
			// setup
			String testArg_productId = "z01";

			// when
			when(mock.getProduct(testArg_productId))
					.thenThrow(new BusinessException(BIZERR401));

			// assert
			mvc.perform(get("/lab5/retrieveProduct/" + testArg_productId))
					.andExpect(status().isBadRequest())
					.andExpect(view().name("/error"))
					.andExpect(model().attribute("message", BIZERR401));
		}

		@Test
		@DisplayName("商品詳細画面表示でシステムエラーが発生する")
		void testRetrieveProduct_SysExOccured() throws Exception {
			// setup
			String testArg_productId = "a01";

			// when
			when(mock.getProduct(testArg_productId))
					.thenThrow(new DummyDataAccessException());

			// assert
			mvc.perform(get("/lab5/retrieveProduct/" + testArg_productId))
					.andExpect(status().isInternalServerError())
					.andExpect(view().name("/error"))
					.andExpect(model().attribute("message", SYSERR000));
		}
	}
}
