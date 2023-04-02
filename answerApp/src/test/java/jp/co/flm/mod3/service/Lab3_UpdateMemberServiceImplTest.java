/**
 * Lab3_UpdateMemberServiceImplTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod3.service;

import static jp.co.flm.common.util.MessageList.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.common.exception.SystemException;
import jp.co.flm.mod3.entity.Member;
import jp.co.flm.mod3.mapper.MemberMapper;

/**
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Lab3_UpdateMemberServiceImplのテスト")
public class Lab3_UpdateMemberServiceImplTest {

	@InjectMocks
	Lab3_UpdateMemberServiceImpl sut;

	@Mock
	MemberMapper mock;

	@Test
	@DisplayName("会員情報の認証を行う")
	void testAuthenticateMember() throws Exception {
		// setup
		String testArg_memberId = "iida@fj.co.jp";
		String testArg_password = "flm123";
		Member expected = new Member(
				"iida@fj.co.jp", "flm123", "飯田 哲夫", "M", "東京都 大田区 池上0-0-0",
				"03-1111-2222", 652);

		// when
		when(mock.findOne(testArg_memberId, testArg_password)).thenReturn(expected);

		// assert
		assertThat(sut.authenticateMember(testArg_memberId, testArg_password))
				.isEqualTo(expected);
	}

	@Test
	@DisplayName("該当する会員情報が存在しない場合")
	void testAuthenticateMember_ReturnsNull() throws Exception {
		// setup
		String testArg_memberId = "iida@fj.co.jp";
		String testArg_password = "flm";

		// when
		when(mock.findOne(testArg_memberId, testArg_password)).thenReturn(null);

		// assert
		assertThat(sut.authenticateMember(testArg_memberId, testArg_password))
				.isNull();
	}

	@Test
	@DisplayName("会員情報の更新を行う")
	void testSetData() throws Exception {
		// setup
		Member testArg_member = new Member(
				"iida@fj.co.jp", "flm123", "富士通 太郎", "M", "東京都 港区 港南0-0-0",
				"03-1111-2222", 652);

		// when
		when(mock.update(testArg_member)).thenReturn(true);

		// assert
		sut.setData(testArg_member);
	}

	@Test
	@DisplayName("会員情報更新で業務例外が発生する")
	void testSetData_None2Update() throws Exception {
		// setup
		Member testArg_member = new Member(
				"annoymous@fj.co.jp", "flm123", "富士通 太郎", "M", "東京都 港区 港南0-0-0",
				"03-1111-2222", 652);

		// when
		when(mock.update(testArg_member)).thenReturn(false);

		// assert
		assertThatThrownBy(() -> sut.setData(testArg_member))
				.isInstanceOf(BusinessException.class).hasMessage(BIZERR301);
	}

	@Test
	@DisplayName("住所がFLMのため業務例外が発生する")
	void testSetData_ThrowsBizEx() throws Exception {
		// setup
		Member testArg_member = new Member(
				"iida@fj.co.jp", "flm123", "富士通 太郎", "M", "FLM", "03-1111-2222",
				652);

		// when
		when(mock.update(testArg_member)).thenReturn(false);

		// assert
		assertThatThrownBy(() -> sut.setData(testArg_member))
				.isInstanceOf(BusinessException.class).hasMessage(BIZERR301);
	}

	@Test
	@DisplayName("氏名がFLMのためシステムエラーが発生する")
	void testSetData_ThrowsSysEx() throws Exception {
		// setup
		Member testArg_member = new Member(
				"iida@fj.co.jp", "flm123", "FLM", "M", "東京都 港区 港南0-0-0",
				"03-1111-2222", 652);

		// when
		when(mock.update(testArg_member)).thenThrow(SystemException.class);

		// assert
		assertThatThrownBy(() -> sut.setData(testArg_member))
				.isInstanceOf(SystemException.class);
	}
}
