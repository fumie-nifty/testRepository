/**
 * Lab1_RetrieveMemberServiceImplTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod3.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import jp.co.flm.mod3.entity.Member;
import jp.co.flm.mod3.mapper.MemberMapper;

/**
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Lab1_RetrieveMemberServiceImplのテスト")
public class Lab1_RetrieveMemberServiceImplTest {

	@InjectMocks
	Lab1_RetrieveMemberServiceImpl sut;

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
}
