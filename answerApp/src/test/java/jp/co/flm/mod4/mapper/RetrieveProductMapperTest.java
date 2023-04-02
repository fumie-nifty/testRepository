/**
 * RetrieveProductMapperTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.mapper;

import static com.github.springtestdbunit.annotation.DatabaseOperation.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

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
@DisplayName("RetrieveProductMapperのテスト")
public class RetrieveProductMapperTest {

	@Autowired
	RetrieveProductMapper sut;

	@Autowired
	ExecuteQueryForTestService renameTable4TestService;

	@Test
	@DisplayName("商品情報のリストを取得する")
	void testFindAllBy_test1() throws Exception {
		// setup
		List<Product> expected = List.of(
				new Product("a01", "ALL STAR one", "a", "スポーツシューズ", 15000, null, 0, 100),
				new Product("a02", "Star Speeder HI", "a", "スポーツシューズ", 13800, null, 0, 100),
				new Product("a03", "Scuderia-α", "a", "スポーツシューズ", 15000, null, 0, 100),
				new Product("a04", "エアロセプシー", "a", "スポーツシューズ", 12000, null, 0, 100),
				new Product("a05", "Squadra Nova", "a", "スポーツシューズ", 12000, null, 0, 100),
				new Product("a06", "Squadra Stellar", "a", "スポーツシューズ", 9800, null, 0, 100),
				new Product("a07", "Squadra Estoile", "a", "スポーツシューズ", 8500, null, 0, 100),
				new Product("a08", "Squadra Hobit", "a", "スポーツシューズ", 6800, null, 0, 100),
				new Product("a09", "ヒュプノクラウン2000", "a", "スポーツシューズ", 16000, null, 0, 100),
				new Product("a10", "SLIP STREAM", "a", "スポーツシューズ", 12500, null, 0, 100),
				new Product("a11", "ストラーダ ラップリーダー", "a", "スポーツシューズ", 25000, null, 0, 100),
				new Product("a12", "Star Speeder Kids", "a", "スポーツシューズ", 6700, null, 0, 100));

		// assert
		assertThat(sut.findAllBy("a")).isEqualTo(expected);
	}

	@Test
	@DisplayName("該当するカテゴリーが存在しないため商品情報の取得に失敗する")
	void testFindAllBy_test2() throws Exception {
		// assert
		assertThat(sut.findAllBy("z")).isEmpty();
	}

	@Test
	@DatabaseSetup(type = DELETE_ALL)
	@DisplayName("レコードが存在しないため商品情報の取得に失敗する")
	void testFindAllBy_test3() throws Exception {
		// assert
		assertThat(sut.findAllBy("a")).isEmpty();
	}

	@Test
	@DisplayName("商品情報を取得する")
	void testFindOne_test1() throws Exception {
		// setup
		Product expected = new Product(
				"a01", "ALL STAR one", "a", "スポーツシューズ", 15000, "a01.gif", 150, 100);

		// assert
		assertThat(sut.findOne("a01")).isEqualTo(expected);
	}

	@Test
	@DisplayName("該当する商品が存在しないため商品情報の取得に失敗する")
	void testFindOne_test2() throws Exception {
		// assert
		assertThat(sut.findOne("z01")).isNull();
	}

	@Test
	@DatabaseSetup(type = DELETE_ALL)
	@DisplayName("レコードが存在しないため商品情報の取得に失敗する")
	void testFindOne_test3() throws Exception {
		// assert
		assertThat(sut.findOne("a01")).isNull();
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
		@DisplayName("商品情報のリストの取得に失敗する")
		void testFindAllBy_test4() throws Exception {
			// assert
			assertThatThrownBy(() -> sut.findAllBy("a"))
					.isInstanceOf(DataAccessException.class);
		}

		@Test
		@DisplayName("商品情報の取得に失敗する")
		void testFindOne_test4() throws Exception {
			// assert
			assertThatThrownBy(() -> sut.findOne("a01"))
					.isInstanceOf(DataAccessException.class);
		}
	}
}
