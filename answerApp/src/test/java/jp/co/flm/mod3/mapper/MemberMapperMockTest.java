/**
 * jp.co.flm.mod3.mapper.MemberMapperMockTest
 *
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod3.mapper;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jp.co.flm.common.exception.SystemException;
import jp.co.flm.mod3.entity.Member;

/**
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@DisplayName("MemberMapperMockのテスト")
public class MemberMapperMockTest {

	MemberMapper sut;

	@BeforeEach
	void setUp() throws Exception {
		sut = new MemberMapperMock();
	}

	@Test
	@DisplayName("会員情報を照会する")
	void testFindOne_test1() {
		// setup
		String testArg_memberId = "iida@fj.co.jp";
		String testArg_password = "flm123";
		Member expected = new Member(
				"iida@fj.co.jp", "flm123", "飯田 哲夫", "M", "東京都 大田区 池上0-0-0",
				"03-1111-2222", 652);

		// assert
		assertThat(sut.findOne(testArg_memberId, testArg_password))
				.isEqualTo(expected);
	}

	@Test
	@DisplayName("該当する会員が存在しないため会員情報の取得に失敗する")
	void testFindOne_test2() {
		// setup
		String testArg_memberId = "iida@fj.co.jp";
		String testArg_password = "flm";

		// assert
		assertThat(sut.findOne(testArg_memberId, testArg_password)).isNull();
	}

	@Test
	@DisplayName("引数が不正なため会員情報の取得に失敗する")
	void testFindOne_test3() {
		// setup
		String testArg_memberId = "iida@fj.co.jp";
		String testArg_password = "flm123";

		// assert
		assertAll(() -> {
			assertThat(sut.findOne(null, testArg_password)).isNull();
			assertThat(sut.findOne(testArg_memberId, null)).isNull();
			assertThat(sut.findOne(null, null)).isNull();
		});
	}

	@Test
	@DisplayName("会員情報の更新を行う")
	void testSave_test1() {
		// setup
		Member testArg_member = new Member(
				"iida@fj.co.jp", "flm123", "富士通 太郎", "M", "東京都 港区 港南0-0-0",
				"03-1111-2222", 652);

		// assert
		assertThat(sut.update(testArg_member)).isTrue();
	}

	@Test
	@DisplayName("更新対象が存在しない")
	void testSave_test2() {
		// setup
		Member testArg_member = new Member(
				"annoymous@fj.co.jp", "flm123", "富士通 太郎", "M", "東京都 港区 港南0-0-0",
				"03-1111-2222", 652);

		// assert
		assertThat(sut.update(testArg_member)).isFalse();
	}

	@Test
	@DisplayName("住所がFLMのためFALSEを返却する")
	void testSave_test3() {
		// setup
		Member testArg_member = new Member(
				"iida@fj.co.jp", "flm123", "富士通 太郎", "M", "FLM", "03-1111-2222",
				652);

		// assert
		assertThat(sut.update(testArg_member)).isFalse();
	}

	@Test
	@DisplayName("引数が不正なためFALSEを返却する")
	void testSave_test4() {
		// setup
		Member testArg_member = null;

		// assert
		assertThat(sut.update(testArg_member)).isFalse();
	}

	@Test
	@DisplayName("氏名がFLMのためシステムエラーが発生する")
	void testSave_test5() {
		// setup
		Member testArg_member = new Member(
				"iida@fj.co.jp", "flm123", "FLM", "M", "東京都 港区 港南0-0-0",
				"03-1111-2222", 652);

		// assert
		assertThatThrownBy(() -> sut.update(testArg_member))
				.isInstanceOf(SystemException.class);
	}
}
