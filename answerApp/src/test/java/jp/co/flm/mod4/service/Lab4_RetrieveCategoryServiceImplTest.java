/**
 * Lab4_RetrieveCategoryServiceImplTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.service;

import static jp.co.flm.common.util.MessageList.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Category;
import jp.co.flm.mod4.mapper.CategoryMapper;
import jp.co.flm.test.util.DummyDataAccessException;

/**
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Lab4_RetrieveCategoryServiceImplのテスト")
public class Lab4_RetrieveCategoryServiceImplTest {

	@InjectMocks
	Lab4_RetrieveCategoryServiceImpl sut;

	@Mock
	CategoryMapper mock;

	@Test
	@DisplayName("全ての商品カテゴリー情報を取得する")
	void testGetAllCategories() throws Exception {
		// setup
		List<Category> expected = List.of(
				new Category("a", "スポーツシューズ", "a.gif"),
				new Category("b", "	スニーカー", "b.gif"),
				new Category("c", "サンダル", "c.gif"));

		// when
		when(mock.findAll()).thenReturn(expected);

		// assert
		assertThat(sut.getAllCategories()).isEqualTo(expected);
	}

	@Test
	@DisplayName("商品カテゴリー検索で業務例外が発生する")
	void testGetAllCategories_ThrowsBixEx() throws Exception {
		// when
		when(mock.findAll()).thenReturn(new ArrayList<>());

		// assert
		assertThatThrownBy(() -> sut.getAllCategories())
				.isInstanceOf(BusinessException.class).hasMessage(BIZERR401);
	}

	@Test
	@DisplayName("商品カテゴリー検索でシステムエラーが発生する")
	void testGetAllCategories_ThrowsSysEx() throws Exception {
		// when
		when(mock.findAll()).thenThrow(DummyDataAccessException.class);

		// assert
		assertThatThrownBy(() -> sut.getAllCategories())
				.isInstanceOf(DataAccessException.class);
	}
}
