/**
 * CategoryMapperTest.java
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

import jp.co.flm.mod4.entity.Category;
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
@DisplayName("CategoryMapperのテスト")
public class CategoryMapperTest {

	@Autowired
	CategoryMapper sut;

	@Autowired
	ExecuteQueryForTestService renameTable4TestService;

	@Test
	@DisplayName("カテゴリー情報のリストを取得する")
	void testFindAll_test1() throws Exception {
		// setup
		List<Category> expected = List.of(
				new Category("a", "スポーツシューズ", "a.gif"),
				new Category("b", "スニーカー", "b.gif"),
				new Category("c", "サンダル", "c.gif"),
				new Category("d", "ウォーキングシューズ", "d.gif"),
				new Category("e", "シャツ", "e.png"),
				new Category("f", "グッズ", "f.png"));

		// assert
		assertThat(sut.findAll()).isEqualTo(expected);
	}

	@Test
	@DatabaseSetup(type = DELETE_ALL)
	@DisplayName("レコードが存在しないためカテゴリー情報の取得に失敗する")
	void testFindAll_test2() throws Exception {
		// assert
		assertThat(sut.findAll()).isEmpty();
	}

	@Nested
	@SpringBootTest
	@DisplayName("Categoryテーブルが存在しない場合")
	class CategoryTableRenamed {

		@BeforeEach
		void setUp() throws Exception {
			renameTable4TestService.renameTable("category", "category2");
		}

		@AfterEach
		void tearDown() throws Exception {
			renameTable4TestService.renameTable("category2", "category");
		}

		@Test
		@DisplayName("カテゴリー情報の取得に失敗する")
		void testFindAll_test3() throws Exception {
			// assert
			assertThatThrownBy(() -> sut.findAll())
					.isInstanceOf(DataAccessException.class);
		}
	}
}
