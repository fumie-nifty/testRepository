/**
 * Lab6_AddProductServiceImplTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.service;

import static jp.co.flm.common.util.MessageList.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Product;
import jp.co.flm.mod4.mapper.AddProductMapper;
import jp.co.flm.test.util.DummyDataAccessException;

/**
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Lab6_AddProductServiceImplのテスト")
public class Lab6_AddProductServiceImplTest {

	@InjectMocks
	Lab6_AddProductServiceImpl sut;

	@Mock
	AddProductMapper mock;

	@Test
	@DisplayName("商品情報を追加する商品カテゴリーの情報を取得する（商品IDが09以下）")
	void testGetInfoForNewProduct_NewIdUnder9() throws Exception {
		// setup
		Product expected
				= new Product("a09", null, "a", "スポーツシューズ", 0, "a.gif", 0, 0);
		Product lastIdProduct
				= new Product("a08", null, "a", "スポーツシューズ", 0, "a.gif", 0, 0);

		// when
//		fail("Mapper実装後下の行のコメントを外してください");
		when(mock.getLast("a")).thenReturn(lastIdProduct);

		// assert
		assertThat(sut.getInfoForNewProduct("a")).isEqualTo(expected);
	}

	@Test
	@DisplayName("商品情報を追加する商品カテゴリーの情報を取得する（商品IDが10以上）")
	void testGetInfoForNewProduct_NewIdOver10() throws Exception {
		// setup
		Product expected
				= new Product("a10", null, "a", "スポーツシューズ", 0, "a.gif", 0, 0);
		Product lastIdProduct
				= new Product("a09", null, "a", "スポーツシューズ", 0, "a.gif", 0, 0);

		// when
//		fail("Mapper実装後下の行のコメントを外してください");
		when(mock.getLast("a")).thenReturn(lastIdProduct);

		// assert
		assertThat(sut.getInfoForNewProduct("a")).isEqualTo(expected);
	}

	@Test
	@DisplayName("商品情報をDBに格納する")
	void testAddProduct() throws Exception {
		// setup
		Product testArg_product = new Product(
				"a13", "新商品", "a", "スポーツシューズ", 10000, "a.gif", 100, 100);

//		fail("Mapper実装後この行を消してください");

		// assert
		sut.addProduct(testArg_product);
	}

	@Test
	@DisplayName("Productテーブルの更新に失敗した場合")
	void testAddProduct_ThrowsBizExDueToFailureInSaveProduct() throws Exception {
		// setup
		Product testArg_product = new Product(
				"a12", "新商品", "a", "スポーツシューズ", 10000, "a.gif", 100, 100);

		// when
//		fail("Mapper実装後下の行のコメントを外してください");
		doThrow(DuplicateKeyException.class).when(mock).saveProduct(any());

		// assert
		assertThatThrownBy(() -> sut.addProduct(testArg_product))
				.isInstanceOf(BusinessException.class).hasMessage(BIZERR611);
	}

	@Test
	@DisplayName("Stockテーブルの更新に失敗した場合")
	void testAddProduct_ThrowsBizExDueToFailureInSaveStock() throws Exception {
		// setup
		Product testArg_product = new Product(
				"a12", "新商品", "a", "スポーツシューズ", 10000, "a.gif", 100, 100);

		// when
//		fail("Mapper実装後下の行のコメントを外してください");
		doThrow(DuplicateKeyException.class)
				.when(mock).saveStock(anyString(), anyInt());

		// assert
		assertThatThrownBy(() -> sut.addProduct(testArg_product))
				.isInstanceOf(BusinessException.class).hasMessage(BIZERR611);
	}

	@Test
	@DisplayName("商品情報登録でシステムエラーが発生する")
	void testAddProduct_ThrowsSysEx() throws Exception {
		// setup
		Product testArg_product = new Product(
				"a13", "新商品", "a", "スポーツシューズ", 10000, "a.gif", 100, 100);

		// when
//		fail("Mapper実装後下の行のコメントを外してください");
		doThrow(DummyDataAccessException.class)
				.when(mock).saveProduct(testArg_product);

		// assert
		assertThatThrownBy(() -> sut.addProduct(testArg_product))
				.isInstanceOf(DataAccessException.class);
	}
}
