/**
 * AddProductMapperTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.mapper;

import static com.github.springtestdbunit.annotation.DatabaseOperation.*;
import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.*;
import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

import jp.co.flm.mod4.entity.Product;
import jp.co.flm.test.util.ExecuteQueryForTestService;

/**
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("classpath:AllTest/setupDB.xml")
@DatabaseTearDown("classpath:AllTest/setupDB.xml")
@DisplayName("AddProductMapperのテスト")
public class AddProductMapperTest {

	@Autowired
	AddProductMapper sut;

	@Autowired
	ExecuteQueryForTestService renameTable4TestService;

	@Test
	@DisplayName("カテゴリー内の最後の商品情報を取得する")
	void testGetLast_test1() throws Exception {
		// setup
		Product expected
				= new Product("a12", null, "a", "スポーツシューズ", 0, "a.gif", 0, 0);

		try {
			Method target = AddProductMapper.class.getMethod("getLast", String.class);
			// assert
			assertThat(target.invoke(sut, "a")).isEqualTo(expected);
		} catch (NoSuchMethodException e) {
			fail("The target method is not yet implemented.");
		}
	}

	@Test
	@DisplayName("該当するカテゴリーが存在しないため商品情報を取得できない")
	void testGetLast_test2() throws Exception {
		try {
			Method target = AddProductMapper.class.getMethod("getLast", String.class);
			// assert
			assertThat(target.invoke(sut, "z")).isNull();
		} catch (NoSuchMethodException e) {
			fail("The target method is not yet implemented.");
		}
	}

	@Test
	@DatabaseSetup(type = DELETE_ALL)
	@DisplayName("レコードが存在しないため商品情報を取得できない")
	void testGetLast_test3() throws Exception {
		// assert
		try {
			Method target = AddProductMapper.class.getMethod("getLast", String.class);
			// assert
			assertThat(target.invoke(sut, "a")).isNull();
		} catch (NoSuchMethodException e) {
			fail("The target method is not yet implemented.");
		}
	}

	@Test
	@ExpectedDatabase(
			value = "classpath:AddProductMapper/expectedDB_forSaveProduct.xml",
			assertionMode = NON_STRICT_UNORDERED)
	@DisplayName("商品情報をDBに登録する")
	void testSaveProduct_test1() throws Exception {
		// setup
		Product testArg_product = new Product(
				"a13", "新商品", "a", "スポーツシューズ", 10000, "a.gif", 100, 100);
		try {
			Method target = AddProductMapper.class.getMethod("saveProduct", Product.class);
			// assert
			target.invoke(sut, testArg_product);
		} catch (NoSuchMethodException e) {
			fail("The target method is not yet implemented.");
		}
	}

	@Test
	@Transactional
	@ExpectedDatabase(value = "classpath:AddProductMapper/expectedDB_Unchanged.xml",
			assertionMode = NON_STRICT_UNORDERED)
	@DisplayName("Productテーブルの一意性制約違反により商品情報の登録に失敗する")
	void testSaveProduct_test2() throws Exception {
		// setup
		Product testArg_product = new Product(
				"a12", "新商品", "a", "スポーツシューズ", 10000, "a.gif", 100, 100);
		try {
			Method target = AddProductMapper.class.getMethod("saveProduct", Product.class);
			target.invoke(sut, testArg_product);
		}catch(InvocationTargetException e) {
			// assert
			assertThat(e.getCause()).isInstanceOf(DuplicateKeyException.class);
		}catch (NoSuchMethodException e) {
			fail("The target method is not yet implemented.");
		}
	}

	@Test
	@DatabaseSetup("classpath:AddProductMapper/setupDB_forSaveStock.xml")
	@ExpectedDatabase(value = "classpath:AddProductMapper/expectedDB_forSaveStock.xml",
			assertionMode = NON_STRICT_UNORDERED)
	@DisplayName("在庫情報をDBに登録する")
	void testSaveStock_test1() throws Exception {
		// setup
		Product testArg_product = new Product(
				"a13", "新商品", "a", "スポーツシューズ", 10000, "a.gif", 100, 100);
		try {
			Method target = AddProductMapper.class.getMethod("saveStock", String.class, int.class);
			// assert
			target.invoke(sut, testArg_product.getProductId(), testArg_product.getQuantity());
		} catch (NoSuchMethodException e) {
			fail("The target method is not yet implemented.");
		}
	}

	@Test
	@Transactional
	@DatabaseSetup("classpath:AddProductMapper/setupDB_forSaveStock.xml")
	@ExpectedDatabase(value = "classpath:AddProductMapper/setupDB_forSaveStock.xml",
			assertionMode = NON_STRICT_UNORDERED)
	@DisplayName("Stockテーブルの一意性制約違反により在庫情報の登録に失敗する")
	void testSaveStock_test2() throws Exception {
		// setup
		Product testArg_product = new Product(
				"a12", "新商品", "a", "スポーツシューズ", 10000, "a.gif", 100, 100);
		try {
			Method target = AddProductMapper.class.getMethod("saveStock", String.class, int.class);
			target.invoke(sut, testArg_product.getProductId(),
					testArg_product.getQuantity());
		}catch(InvocationTargetException e) {
			// assert
			assertThat(e.getCause()).isInstanceOf(DataAccessException.class);
		}catch (NoSuchMethodException e) {
			fail("The target method is not yet implemented.");
		}
	}

	@Test
	@Transactional
	@DatabaseSetup("classpath:AddProductMapper/setupDB_forSaveStock.xml")
	@ExpectedDatabase(value = "classpath:AddProductMapper/setupDB_forSaveStock.xml",
			assertionMode = NON_STRICT_UNORDERED)
	@DisplayName("外部キー制約違反のため在庫情報の登録に失敗する")
	void testSaveStock_test3() throws Exception {
		// setup
		Product testArg_product = new Product(
				"a14", "新商品", "a", "スポーツシューズ", 10000, "a.gif", 100, 100);
		try {
			Method target = AddProductMapper.class.getMethod("saveStock", String.class, int.class);
			target.invoke(sut, testArg_product.getProductId(),
					testArg_product.getQuantity());
		}catch(InvocationTargetException e) {
			// assert
			assertThat(e.getCause()).isInstanceOf(DataAccessException.class);
		}catch (NoSuchMethodException e) {
			fail("The target method is not yet implemented.");
		}
	}

	@Nested
	@SpringBootTest
	@DisplayName("Productテーブルが存在しない場合")
	class ProductTableRenamed {

		@BeforeEach
		void setUp() throws Exception {
			renameTable4TestService.renameTable("product", "product2");
		}

		@AfterEach
		void tearDown() throws Exception {
			renameTable4TestService.renameTable("product2", "product");
		}

		@Test
		@DisplayName("商品情報の取得に失敗する")
		void testGetLast_test4() throws Exception {
			try {
				Method target = AddProductMapper.class.getMethod("getLast", String.class);
				target.invoke(sut, "a");
			}catch(InvocationTargetException e) {
				// assert
				assertThat(e.getCause()).isInstanceOf(DataAccessException.class);
			}catch (NoSuchMethodException e) {
				fail("The target method is not yet implemented.");
			}
		}

		@Test
		@ExpectedDatabase(value = "classpath:AddProductMapper/expectedDB_Unchanged.xml",
				assertionMode = NON_STRICT_UNORDERED)
		@DisplayName("商品情報の登録に失敗する")
		void testSaveProduct_test3() throws Exception {
			// setup
			Product testArg_product = new Product(
					"a13", "新商品", "a", "スポーツシューズ", 10000, "a.gif", 100, 100);
			try {
				Method target = AddProductMapper.class.getMethod("saveProduct", Product.class);
				target.invoke(sut, testArg_product);
			}catch(InvocationTargetException e) {
				// assert
				assertThat(e.getCause()).isInstanceOf(DataAccessException.class);
			}catch (NoSuchMethodException e) {
				fail("The target method is not yet implemented.");
			}
		}
	}

	@Nested
	@SpringBootTest
	@DisplayName("Stockテーブルが存在しない場合")
	class StockTableRenamed {

		@BeforeEach
		void setUp() throws Exception {
			renameTable4TestService.renameTable("stock", "stock2");
		}

		@AfterEach
		void tearDown() throws Exception {
			renameTable4TestService.renameTable("stock2", "stock");
		}

		@Test
		@DisplayName("在庫情報の登録に失敗する")
		void testSaveStock_test4() throws Exception {
			// setup
			Product testArg_product = new Product(
					"a12", "新商品", "a", "スポーツシューズ", 10000, "a.gif", 100, 100);
			try {
				Method target = AddProductMapper.class.getMethod("saveStock", String.class, int.class);
				target.invoke(sut, testArg_product.getProductId(),
						testArg_product.getQuantity());
			}catch(InvocationTargetException e) {
				// assert
				assertThat(e.getCause()).isInstanceOf(DataAccessException.class);
			}catch (NoSuchMethodException e) {
				fail("The target method is not yet implemented.");
			}
		}
	}
}
