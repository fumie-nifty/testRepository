/**
 * Lab5_RetrieveProductServiceImplTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.service;

import static jp.co.flm.common.util.MessageList.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
import jp.co.flm.mod4.entity.Product;
import jp.co.flm.mod4.mapper.RetrieveProductMapper;
import jp.co.flm.test.util.DummyDataAccessException;

/**
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Lab5_RetrieveProductServiceImplのテスト")
public class Lab5_RetrieveProductServiceImplTest {

	@InjectMocks
	Lab5_RetrieveProductServiceImpl sut;

	@Mock
	RetrieveProductMapper mock;

	@Test
	@DisplayName("カテゴリーIDをもとに商品情報を検索する")
	void testGetAllProductByCategoryId() throws Exception {
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

		// when
		when(mock.findAllBy("a")).thenReturn(expected);

		// assert
		assertThat(sut.getAllProductByCategoryId("a")).isEqualTo(expected);
	}

	@Test
	@DisplayName("カテゴリーID検索で業務例外が発生する")
	void testGetAllProductByCategoryId_ThrowsBizEx() throws Exception {
		// when
		when(mock.findAllBy(anyString())).thenReturn(new ArrayList<>());

		// assert
		assertThatThrownBy(() -> sut.getAllProductByCategoryId("z"))
				.isInstanceOf(BusinessException.class).hasMessage(BIZERR401);
	}

	@Test
	@DisplayName("カテゴリーID検索でシステムエラーが発生する")
	void testGetAllProductByCategoryId_ThrowsSysEx() throws Exception {
		// when
		when(mock.findAllBy(anyString())).thenThrow(DummyDataAccessException.class);

		// assert
		assertThatThrownBy(() -> sut.getAllProductByCategoryId("a"))
				.isInstanceOf(DataAccessException.class);
	}

	@Test
	@DisplayName("商品IDをもとに商品情報を検索する")
	void testGetProduct() throws Exception {
		// setup
		Product expected = new Product(
				"a01", "ALL STAR one", "a", "スポーツシューズ", 15000, "a01.gif", 150, 10);

		// when
		when(mock.findOne("a01")).thenReturn(expected);

		// assert
		assertThat(sut.getProduct("a01")).isEqualTo(expected);
	}

	@Test
	@DisplayName("商品ID検索で業務例外が発生する")
	void testGetProduct_ThrowsBizEx() throws Exception {
		// when
		when(mock.findOne(anyString())).thenReturn(null);

		// assert
		assertThatThrownBy(() -> sut.getProduct("z01"))
				.isInstanceOf(BusinessException.class).hasMessage(BIZERR401);
	}

	@Test
	@DisplayName("商品ID検索でシステムエラーが発生する")
	void testGetProduct_ThrowsSysEx() throws Exception {
		// when
		when(mock.findOne(anyString())).thenThrow(DummyDataAccessException.class);

		// assert
		assertThatThrownBy(() -> sut.getProduct("a01"))
				.isInstanceOf(DataAccessException.class);
	}
}
