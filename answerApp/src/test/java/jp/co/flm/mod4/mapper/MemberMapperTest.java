/**
 * MemberMapperTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.mapper;

import static com.github.springtestdbunit.annotation.DatabaseOperation.*;
import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.*;
import static org.assertj.core.api.Assertions.*;

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
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

import jp.co.flm.mod4.entity.Member;
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
@DisplayName("MemberMapperのテスト")
public class MemberMapperTest {

	@Autowired
	MemberMapper sut;

	@Autowired
	ExecuteQueryForTestService renameTable4TestService;

	@Test
	@DisplayName("メールアドレスの重複チェックを行う")
	void testExist_test1() throws Exception {
		// assert
		assertThat(sut.exist("abc@def.com")).isFalse();
	}

	@Test
	@DisplayName("メールアドレスの重複により真を返す")
	void testExist_test2() throws Exception {
		// assert
		assertThat(sut.exist("iida@fj.co.jp")).isTrue();
	}

	@Test
	@DatabaseSetup(type = DELETE_ALL)
	@DisplayName("レコードが存在しないため真を返す")
	void testExist_test3() throws Exception {
		// assert
		assertThat(sut.exist("abc@def.com")).isFalse();
	}

	@Test
	@ExpectedDatabase(value = "classpath:MemberMapper/expectedDB.xml",
			assertionMode = NON_STRICT_UNORDERED)
	@DisplayName("会員情報をDBに登録する")
	void testSave_test1() throws Exception {
		// setup
		Member testArg_member = new Member(
				"abc@def.com", "flm123", "富士通 太郎", "M", "東京都港区港南2-13-34",
				"012-3456-7890", 0);

		// assert
		sut.save(testArg_member);
	}

	@Test
	@Transactional
	@ExpectedDatabase(value = "classpath:MemberMapper/expectedDB_Unchanged.xml",
			assertionMode = NON_STRICT_UNORDERED)
	@DisplayName("メールアドレスの重複により会員情報の登録に失敗する")
	void testSave_test2() throws Exception {
		// setup
		Member testArg_member = new Member(
				"iida@fj.co.jp", "flm123", "富士通 太郎", "M", "東京都港区港南2-13-34",
				"012-3456-7890", 0);

		// assert
		assertThatThrownBy(() -> sut.save(testArg_member))
				.isInstanceOf(DataAccessException.class);
	}

	@Test
	@DatabaseSetup(type = DELETE_ALL)
	@ExpectedDatabase(value = "classpath:MemberMapper/expectedDB_withSingleRecord.xml",
			assertionMode = NON_STRICT_UNORDERED)
	@DisplayName("レコードが存在しない場合DBに登録できる")
	void testSave_test3() throws Exception {
		// setup
		Member testArg_member = new Member(
				"abc@def.com", "flm123", "富士通 太郎", "M", "東京都港区港南2-13-34",
				"012-3456-7890", 0);

		// assert
		sut.save(testArg_member);
	}

	@Nested
	@SpringBootTest
	@DisplayName("Memberテーブルが存在しない場合")
	class MemberTableRenamed {

		@BeforeEach
		void setUp() throws Exception {
			renameTable4TestService.renameTable("member", "member2");
		}

		@AfterEach
		void tearDown() throws Exception {
			renameTable4TestService.renameTable("member2", "member");
		}

		@Test
		@DisplayName("重複チェックに失敗する")
		void testExist_test4() throws Exception {
			// assert
			assertThatThrownBy(() -> sut.exist("abc@def.com"))
					.isInstanceOf(DataAccessException.class);
		}

		@Test
		@ExpectedDatabase(value = "classpath:MemberMapper/expectedDB_Unchanged.xml",
				assertionMode = NON_STRICT_UNORDERED)
		@DisplayName("会員情報の登録に失敗する")
		void testSave_test4() throws Exception {
			// setup
			Member testArg_member = new Member(
					"iida@fj.co.jp", "flm123", "富士通 太郎", "M", "東京都港区港南2-13-34",
					"012-3456-7890", 0);

			// assert
			assertThatThrownBy(() -> sut.save(testArg_member))
					.isInstanceOf(DataAccessException.class);
		}
	}
}
