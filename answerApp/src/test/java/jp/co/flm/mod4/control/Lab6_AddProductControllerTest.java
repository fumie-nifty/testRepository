/**
 * Lab6_AddProductControllerTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.control;

import static jp.co.flm.common.util.MessageList.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Product;
import jp.co.flm.mod4.form.NewProductForm;
import jp.co.flm.mod4.service.Lab6_AddProductService;
import jp.co.flm.test.util.DummyDataAccessException;

/**
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Lab6_AddProductControllerのテスト")
public class Lab6_AddProductControllerTest {

	@InjectMocks
	Lab6_AddProductController sut;

	@Mock
	Lab6_AddProductService mock;

	@Autowired
	Validator validator;

	@Autowired
	HandlerExceptionResolver handlerExceptionResolver;

	MockMvc mvc;

	MockHttpSession session;

	MockHttpServletRequest request;

	NewProductForm newProductForm;

	@BeforeEach
	void setUp() throws Exception {
		mvc = standaloneSetup(sut)
				.setHandlerExceptionResolvers(handlerExceptionResolver)
				.build();
	}

	@BeforeEach
	void setUpEntity() throws Exception {
		newProductForm = new NewProductForm(
				"a13", null, "a", "スポーツシューズ", null, "a.gif", 0, null);
		request = new MockHttpServletRequest();
		session = new MockHttpSession();
	}

	@Test
	@DisplayName("新商品追加画面を表示する")
	void testInputNewProduct() throws Exception {
		// setup
		String testArg_categoryId = "a";
		Product returnVal
				= new Product("a13", null, "a", "スポーツシューズ", 0, "a.gif", 0, 0);

		// when
		when(mock.getInfoForNewProduct(testArg_categoryId)).thenReturn(returnVal);

		// assert
		mvc.perform(get("/lab6/inputNewProduct")
				.param("categoryId", testArg_categoryId))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab6/addition-input"))
				.andExpect(request().sessionAttribute("newProduct", newProductForm));
	}

	@Test
	@DisplayName("新商品追加確認画面を表示する")
	void testConfirmAddition() throws Exception {
		// setup
		String testArg_categoryId = "a";
		Product returnVal
				= new Product("a13", null, "a", "スポーツシューズ", 0, "a.gif", 0, 0);
		String newProductName = "新商品";
		String newProductPrice = "10000";
		String newProductQuantity = "100";
		newProductForm.setProductName(newProductName);
		newProductForm.setPrice(Integer.parseInt(newProductPrice));
		newProductForm.setPoint((int) (newProductForm.getPrice() * 0.01));
		newProductForm.setQuantity(Integer.parseInt(newProductQuantity));

		// when
		when(mock.getInfoForNewProduct(testArg_categoryId)).thenReturn(returnVal);
		session = (MockHttpSession) mvc.perform(get("/lab6/inputNewProduct")
				.param("categoryId", testArg_categoryId))
				.andReturn().getRequest().getSession();

		// assert
		mvc.perform(post("/lab6/confirmAddition")
				.param("productName", newProductName)
				.param("price", newProductPrice)
				.param("quantity", newProductQuantity)
				.session(session))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab6/addition-confirm"))
				.andExpect(model().hasNoErrors())
				.andExpect(request().sessionAttribute("newProduct", newProductForm));
	}
	@Test
	@DisplayName("新商品追加完了画面を表示する")
	void testCommitAddition() throws Exception {
		// setup
		String newProductName = "新商品";
		String newProductPrice = "10000";
		String newProductQuantity = "100";
		newProductForm.setProductId("a13");
		newProductForm.setProductName(newProductName);
		newProductForm.setPrice(Integer.parseInt(newProductPrice));
		newProductForm.setQuantity(Integer.parseInt(newProductQuantity));

		// when
		doNothing().when(mock).addProduct(any());
		session = (MockHttpSession) mvc.perform(post("/lab6/confirmAddition")
				.param("productName", newProductName)
				.param("price", newProductPrice)
				.param("quantity", newProductQuantity)
				.sessionAttr("newProduct", newProductForm))
				.andReturn().getRequest().getSession();

		// assert
		mvc.perform(post("/lab6/commitAddition")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab6/addition-complete"))
				.andExpect(model().attribute("newProduct", newProductForm));
	}

	@Test
	@DisplayName("新商品追加画面に戻る")
	void testReviseInput() throws Exception {
		// setup
		String newProductName = "新商品";
		String newProductPrice = "10000";
		String newProductQuantity = "100";
		newProductForm.setProductId("a13");
		newProductForm.setProductName(newProductName);
		newProductForm.setPrice(Integer.parseInt(newProductPrice));
		newProductForm.setQuantity(Integer.parseInt(newProductQuantity));

		// when
		session = (MockHttpSession) mvc.perform(post("/lab6/confirmAddition")
						.param("productName", newProductName)
						.param("price", newProductPrice)
						.param("quantity", newProductQuantity)
						.sessionAttr("newProduct", newProductForm))
						.andReturn().getRequest().getSession();

		// assert
		mvc.perform(get("/lab6/reviseInput")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab6/addition-input"))
				.andExpect(model().hasNoErrors())
				.andExpect(request().sessionAttribute("newProduct", newProductForm));
	}

	@Nested
	@SpringBootTest
	@DisplayName("NewProductFormのテスト")
	class NewProductFormTest{

		final Validator validator
				= Validation.buildDefaultValidatorFactory().getValidator();

		@Test
		@DisplayName("入力チェックエラー(@Size)を表示する")
		void testConfirmAddition_SizeValidationError() throws Exception {
			// setup
			String testArg_categoryId = "a";
			Product returnVal
					= new Product("a13", null, "a", "スポーツシューズ", 0, "a.gif", 0, 0);
			String newProductName = new String("a").repeat(41);
			String newProductPrice = "10000";
			String newProductQuantity = "100";
			newProductForm.setProductId("a13");
			newProductForm.setProductName(newProductName);
			newProductForm.setPrice(Integer.parseInt(newProductPrice));
			newProductForm.setQuantity(Integer.parseInt(newProductQuantity));

			// when
			when(mock.getInfoForNewProduct(testArg_categoryId))
					.thenReturn(returnVal);
			session = (MockHttpSession) mvc.perform(get("/lab6/inputNewProduct")
					.param("categoryId", testArg_categoryId))
					.andReturn().getRequest().getSession();

			// assert
			Set<ConstraintViolation<NewProductForm>> violations
					= validator.validate(newProductForm);
			assertThat(violations).hasSize(1);
			assertThat(violations.iterator().next().getMessageTemplate())
					.isEqualTo("{javax.validation.constraints.Size.message}");

			mvc.perform(post("/lab6/confirmAddition")
					.param("productName", newProductName)
					.param("price", newProductPrice)
					.param("quantity", newProductQuantity)
					.session(session))
					.andExpect(view().name("/lab6/addition-input"))
					.andExpect(model().hasErrors());
		}

		@Test
		@DisplayName("入力チェックエラー(@NotBlank)を表示する")
		void testConfirmAddition_NotBlankValidationError() throws Exception {
			// setup
			String testArg_categoryId = "a";
			Product returnVal
					= new Product("a13", null, "a", "スポーツシューズ", 0, "a.gif", 0, 0);
			String newProductName = null;
			String newProductPrice = "10000";
			String newProductQuantity = "100";
			newProductForm.setProductId("a13");
			newProductForm.setProductName(newProductName);
			newProductForm.setPrice(Integer.parseInt(newProductPrice));
			newProductForm.setQuantity(Integer.parseInt(newProductQuantity));

			// when
			when(mock.getInfoForNewProduct(testArg_categoryId))
					.thenReturn(returnVal);
			session = (MockHttpSession) mvc.perform(get("/lab6/inputNewProduct")
					.param("categoryId", testArg_categoryId))
					.andReturn().getRequest().getSession();

			// assert
			Set<ConstraintViolation<NewProductForm>> violations
					= validator.validate(newProductForm);
			assertThat(violations).hasSize(1);
			assertThat(violations.iterator().next().getMessageTemplate())
					.isEqualTo("{javax.validation.constraints.NotBlank.message}");

			mvc.perform(post("/lab6/confirmAddition")
					.param("productName", newProductName)
					.param("price", newProductPrice)
					.param("quantity", newProductQuantity)
					.session(session))
					.andExpect(view().name("/lab6/addition-input"))
					.andExpect(model().hasErrors());
		}

		@Test
		@DisplayName("入力チェックエラー(@NotNull)を表示する")
		void testConfirmAddition_NotNullValidationError() throws Exception {
			// setup
			String testArg_categoryId = "a";
			Product returnVal
					= new Product("a13", null, "a", "スポーツシューズ", 0, "a.gif", 0, 0);
			String newProductName = "新商品";
			String newProductPrice = null;
			String newProductQuantity = null;
			newProductForm.setProductId("a13");
			newProductForm.setProductName(newProductName);
			newProductForm.setPrice(null);
			newProductForm.setQuantity(null);

			// when
			when(mock.getInfoForNewProduct(testArg_categoryId))
					.thenReturn(returnVal);
			session = (MockHttpSession) mvc.perform(get("/lab6/inputNewProduct")
					.param("categoryId", testArg_categoryId))
					.andReturn().getRequest().getSession();

			// assert
			Set<ConstraintViolation<NewProductForm>> violations
					= validator.validate(newProductForm);
			assertThat(violations).hasSize(2);
			for (ConstraintViolation<NewProductForm> constraintViolation : violations) {
				assertThat(constraintViolation.getMessageTemplate())
						.isEqualTo("{javax.validation.constraints.NotNull.message}");
			}

			mvc.perform(post("/lab6/confirmAddition")
					.param("productName", newProductName)
					.param("price", newProductPrice)
					.param("quantity", newProductQuantity)
					.session(session))
					.andExpect(view().name("/lab6/addition-input"))
					.andExpect(model().hasErrors());
		}

		@Test
		@DisplayName("入力チェックエラー(@Min)を表示する")
		void testConfirmAddition_MinValidationError() throws Exception {
			// setup
			String testArg_categoryId = "a";
			Product returnVal
					= new Product("a13", null, "a", "スポーツシューズ", 0, "a.gif", 0, 0);
			String newProductName = "新商品";
			String newProductPrice = "0";
			String newProductQuantity = "0";
			newProductForm.setProductId("a13");
			newProductForm.setProductName(newProductName);
			newProductForm.setPrice(Integer.parseInt(newProductPrice));
			newProductForm.setQuantity(Integer.parseInt(newProductQuantity));

			// when
			when(mock.getInfoForNewProduct(testArg_categoryId))
					.thenReturn(returnVal);
			session = (MockHttpSession) mvc.perform(get("/lab6/inputNewProduct")
					.param("categoryId", testArg_categoryId))
					.andReturn().getRequest().getSession();

			// assert
			Set<ConstraintViolation<NewProductForm>> violations
					= validator.validate(newProductForm);
			assertThat(violations).hasSize(2);
			for (ConstraintViolation<NewProductForm> constraintViolation : violations) {
				assertThat(constraintViolation.getMessageTemplate())
						.isEqualTo("{javax.validation.constraints.Min.message}");
			}

			mvc.perform(post("/lab6/confirmAddition")
					.param("productName", newProductName)
					.param("price", newProductPrice)
					.param("quantity", newProductQuantity)
					.session(session))
					.andExpect(view().name("/lab6/addition-input"))
					.andExpect(model().hasErrors());
		}

		@Test
		@DisplayName("入力チェックエラー(@Max)を表示する")
		void testConfirmAddition_MaxValidationError() throws Exception {
			// setup
			String testArg_categoryId = "a";
			Product returnVal
					= new Product("a13", null, "a", "スポーツシューズ", 0, "a.gif", 0, 0);
			String newProductName = "新商品";
			String newProductPrice = "10000000";
			String newProductQuantity = "101";
			newProductForm.setProductId("a13");
			newProductForm.setProductName(newProductName);
			newProductForm.setPrice(Integer.parseInt(newProductPrice));
			newProductForm.setQuantity(Integer.parseInt(newProductQuantity));

			// when
			when(mock.getInfoForNewProduct(testArg_categoryId))
					.thenReturn(returnVal);
			session = (MockHttpSession) mvc.perform(get("/lab6/inputNewProduct")
					.param("categoryId", testArg_categoryId))
					.andReturn().getRequest().getSession();

			// assert
			Set<ConstraintViolation<NewProductForm>> violations
					= validator.validate(newProductForm);
			assertThat(violations).hasSize(2);
			for (ConstraintViolation<NewProductForm> constraintViolation : violations) {
				assertThat(constraintViolation.getMessageTemplate())
						.isEqualTo("{javax.validation.constraints.Max.message}");
			}

			mvc.perform(post("/lab6/confirmAddition")
					.param("productName", newProductName)
					.param("price", newProductPrice)
					.param("quantity", newProductQuantity)
					.session(session))
					.andExpect(view().name("/lab6/addition-input"))
					.andExpect(model().hasErrors());
		}
	}

	@Nested
	@SpringBootTest
	@DisplayName("例外処理のテスト")
	class ExceptionHandlerTest{

		@Test
		@DisplayName("新商品追加確認画面表示時にセッションが無効になる")
		void testConfirmAddition_SessionExpired() throws Exception {
			// setup
			String newProductName = "新商品";
			String newProductPrice = "10000";
			String newProductQuantity = "100";
			newProductForm.setProductId("a13");
			newProductForm.setProductName(newProductName);
			newProductForm.setPrice(Integer.parseInt(newProductPrice));
			newProductForm.setQuantity(Integer.parseInt(newProductQuantity));

			// assert
			mvc.perform(post("/lab6/confirmAddition")
					.param("productName", newProductName)
					.param("price", newProductPrice)
					.param("quantity", newProductQuantity))
					.andExpect(status().isBadGateway())
					.andExpect(view().name("/error"))
					.andExpect(model().attribute("message", BIZERR302));
		}

		@Test
		@DisplayName("新商品追加完了画面表示時にセッションが無効になる")
		void testCommitAddition_SessionExpired() throws Exception {
			// assert
			mvc.perform(post("/lab6/commitAddition"))
					.andExpect(status().isBadGateway())
					.andExpect(view().name("/error"))
					.andExpect(model().attribute("message", BIZERR302));
		}

		@Test
		@DisplayName("新商品追加画面に戻る際にセッションが無効になる")
		void testReviseInput_SessionExpired() throws Exception {
			// assert
			mvc.perform(get("/lab6/reviseInput"))
					.andExpect(status().isBadGateway())
					.andExpect(view().name("/error"))
					.andExpect(model().attribute("message", BIZERR302));
		}

		@Test
		@DisplayName("新商品追加完了画面表示時に業務例外が発生時する")
		void testCommitAddition_BizExOccured() throws Exception {
			// setup
			String newProductName = "新商品";
			String newProductPrice = "10000";
			String newProductQuantity = "100";
			newProductForm.setProductId("a12");
			newProductForm.setProductName(newProductName);
			newProductForm.setPrice(Integer.parseInt(newProductPrice));
			newProductForm.setQuantity(Integer.parseInt(newProductQuantity));

			// when
			doThrow(new BusinessException(BIZERR611))
					.when(mock).addProduct(any());
			session = (MockHttpSession) mvc.perform(post("/lab6/confirmAddition")
					.param("productName", newProductName)
					.param("price", newProductPrice)
					.param("quantity", newProductQuantity)
					.sessionAttr("newProduct", newProductForm))
					.andReturn().getRequest().getSession();

			// assert
			mvc.perform(post("/lab6/commitAddition")
					.session(session))
					.andExpect(status().isBadRequest())
					.andExpect(view().name("/error"))
					.andExpect(model().attribute("message", BIZERR611));
		}

		@Test
		@DisplayName("新商品追加画面表示時にシステムエラーが発生する")
		void testInputNewProduct_SysExOccured() throws Exception {
			// setup
			String testArg_categoryId = "a";

			// when
			when(mock.getInfoForNewProduct(testArg_categoryId))
				.thenThrow(new DummyDataAccessException());

			// assert
			mvc.perform(get("/lab6/inputNewProduct")
					.param("categoryId", testArg_categoryId))
					.andExpect(status().isInternalServerError())
					.andExpect(view().name("/error"))
					.andExpect(model().attribute("message", SYSERR000));
		}

		@Test
		@DisplayName("新商品追加完了画面表示時にシステムエラーが発生時する")
		void testCommitAddition_SysExOccured() throws Exception {
			// setup
			String newProductName = "新商品";
			String newProductPrice = "10000";
			String newProductQuantity = "100";
			newProductForm.setProductId("a13");
			newProductForm.setProductName(newProductName);
			newProductForm.setPrice(Integer.parseInt(newProductPrice));
			newProductForm.setQuantity(Integer.parseInt(newProductQuantity));

			// when
			doThrow(new DummyDataAccessException())
					.when(mock).addProduct(any());
			session = (MockHttpSession) mvc.perform(post("/lab6/confirmAddition")
							.param("productName", newProductName)
							.param("price", newProductPrice)
							.param("quantity", newProductQuantity)
							.sessionAttr("newProduct", newProductForm))
							.andReturn().getRequest().getSession();

			// assert
			mvc.perform(post("/lab6/commitAddition")
					.session(session))
					.andExpect(status().isInternalServerError())
					.andExpect(view().name("/error"))
					.andExpect(model().attribute("message", SYSERR000));
		}
	}
}
