/**
 * Lab4_RetrieveCategoryControllerTest.java
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
import jp.co.flm.mod4.entity.Category;
import jp.co.flm.mod4.service.Lab4_RetrieveCategoryService;
import jp.co.flm.test.util.DummyDataAccessException;

/**
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Lab4_RetrieveCategoryControllerのテスト")
public class Lab4_RetrieveCategoryControllerTest {

	@InjectMocks
	Lab4_RetrieveCategoryController sut;

	@Mock
	Lab4_RetrieveCategoryService mock;

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
	@DisplayName("商品カテゴリー画面を表示する")
	void testRetrieveAllCategories() throws Exception {
		// setup
		List<Category> returnVal = List.of(
				new Category("a", "スポーツシューズ", "a.gif"),
				new Category("b", "	スニーカー", "b.gif"),
				new Category("c", "サンダル", "c.gif"));

		// when
		when(mock.getAllCategories()).thenReturn(returnVal);

		// assert
		mvc.perform(get("/lab4/retrieveAllCategories"))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab4/retrieve-category"))
				.andExpect(model().attribute("categoryList", returnVal));
	}
	@Nested
	@SpringBootTest
	@DisplayName("例外処理のテスト")
	class ExceptionHandlerTest{

		@Test
		@DisplayName("業務例外が発生する")
		void testRetrieveAllCategories_BizExOccured() throws Exception {
			// when
			when(mock.getAllCategories())
					.thenThrow(new BusinessException(BIZERR401));

			// assert
			mvc.perform(get("/lab4/retrieveAllCategories"))
					.andExpect(status().isBadRequest())
					.andExpect(view().name("/error"))
					.andExpect(model().attribute("message", BIZERR401));
		}

		@Test
		@DisplayName("システムエラーが発生する")
		void testRetrieveAllCategories_SysExOccured() throws Exception {
			// when
			when(mock.getAllCategories())
					.thenThrow(new DummyDataAccessException());

			// assert
			mvc.perform(get("/lab4/retrieveAllCategories"))
					.andExpect(status().isInternalServerError())
					.andExpect(view().name("/error"))
					.andExpect(model().attribute("message", SYSERR000));
		}
	}
}
