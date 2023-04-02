/**
 * Lab6op_RegistMemberServiceImplTest.java
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
import jp.co.flm.mod4.entity.Member;
import jp.co.flm.mod4.mapper.MemberMapper;
import jp.co.flm.test.util.DummyDataAccessException;

/**
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Lab6op_RegistMemberServiceImplのテスト")
public class Lab6op_RegistMemberServiceImplTest {

	@InjectMocks
	Lab6op_RegistMemberServiceImpl sut;

	@Mock
	MemberMapper mock;

	@Test
	@DisplayName("Eメールアドレスの重複チェックを行う")
	void testValidateEmail() throws Exception {
		// setup
		String testArg_email = "abc@def.com";

		// when
		when(mock.exist(testArg_email)).thenReturn(false);

		// assert
		sut.validateEmail(testArg_email);
	}

	@Test
	@DisplayName("重複チェックで業務例外が発生する")
	void testValidateEmail_ThrowsBizEx() throws Exception {
		// setup
		String testArg_email = "iida@fj.co.jp";

		// when
		when(mock.exist(anyString())).thenReturn(true);

		// assert
		assertThatThrownBy(() -> sut.validateEmail(testArg_email))
				.isInstanceOf(BusinessException.class).hasMessage(BIZERR601);
	}

	@Test
	@DisplayName("重複チェックでシステムエラーが発生する")
	void testValidateEmail_ThrowsSysEx() throws Exception {
		// setup
		String testArg_email = "abc@def.com";

		// when
		when(mock.exist(testArg_email)).thenThrow(DummyDataAccessException.class);

		// assert
		assertThatThrownBy(() -> sut.validateEmail(testArg_email))
				.isInstanceOf(DataAccessException.class);
	}

	@Test
	@DisplayName("会員情報をDBに登録する")
	void testRegistMember() throws Exception {
		// setup
		Member testArg_member = new Member(
				"abc@def.com", "flm123", "富士通 太郎", "M", "東京都港区港南2-13-34",
				"012-3456-7890", 0);

		// when
		doNothing().when(mock).save(testArg_member);

		// assert
		sut.registMember(testArg_member);
	}

	@Test
	@DisplayName("会員情報登録で業務例外が発生する")
	void testRegistMember_ThrowsBizEx() throws Exception {
		// setup
		Member testArg_member = new Member(
				"iida@fj.co.jp", "flm123", "富士通 太郎", "M", "東京都港区港南2-13-34",
				"012-3456-7890", 0);

		// when
		doThrow(DuplicateKeyException.class).when(mock).save(any());

		// assert
		assertThatThrownBy(() -> sut.registMember(testArg_member))
				.isInstanceOf(BusinessException.class).hasMessage(BIZERR601);
	}

	@Test
	@DisplayName("会員情報登録でシステムエラーが発生する")
	void testRegistMember_ThrowsSysEx() throws Exception {
		// setup
		Member testArg_member = new Member(
				"abc@def.com", "flm123", "富士通 太郎", "M", "東京都港区港南2-13-34",
				"012-3456-7890", 0);

		// when
		doThrow(DummyDataAccessException.class).when(mock).save(testArg_member);

		// assert
		assertThatThrownBy(() -> sut.registMember(testArg_member))
				.isInstanceOf(DataAccessException.class);
	}
}
