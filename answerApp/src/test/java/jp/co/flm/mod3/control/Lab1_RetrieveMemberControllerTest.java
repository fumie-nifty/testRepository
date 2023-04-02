/**
 * Lab1_RetrieveMemberControllerTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod3.control;

import static jp.co.flm.common.util.MessageList.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import jp.co.flm.mod3.entity.Member;
import jp.co.flm.mod3.service.Lab1_RetrieveMemberService;

/**
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Lab1_RetrieveMemberControllerのテスト")
public class Lab1_RetrieveMemberControllerTest {

	@InjectMocks
	Lab1_RetrieveMemberController sut;

	@Mock
	Lab1_RetrieveMemberService mock;

	MockMvc mvc;

	@BeforeEach
	void setUp() throws Exception {
		mvc = standaloneSetup(sut).build();
	}

	@Test
	@DisplayName("メニュー画面を表示する")
	void testReturnMenu() throws Exception {
		// assert
		mvc.perform(get("/lab1/menu"))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab1/retrieve-menu"));
	}

	@Test
	@DisplayName("ログイン画面を表示する")
	void testReturnLogin() throws Exception {
		// assert
		mvc.perform(get("/lab1/login"))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab1/retrieve-login"));
	}

	@Test
	@DisplayName("会員情報照会画面を表示する")
	void testRetrieveMember() throws Exception {
		// setup
		String testArg_memberId = "iida@fj.co.jp";
		String testArg_password = "flm123";
		Member returnVal = new Member(
				"iida@fj.co.jp", "flm123", "飯田 哲夫", "M", "東京都 大田区 池上0-0-0",
				"03-1111-2222", 652);

		// when
		when(mock.authenticateMember(testArg_memberId, testArg_password))
				.thenReturn(returnVal);

		// assert
		mvc.perform(get("/lab1/retrieveMember")
				.param("memberId", testArg_memberId)
				.param("password", testArg_password))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab1/retrieve-member"))
				.andExpect(model().attribute("member", returnVal));
	}

	@Test
	@DisplayName("認証失敗時にログイン画面を表示する")
	void testRetrieveMember_BizExOccured() throws Exception {
		// setup
		String testArg_memberId = "iida@fj.co.jp";
		String testArg_password = "flm";

		// when
		when(mock.authenticateMember(testArg_memberId, testArg_password))
				.thenReturn(null);

		// assert
		mvc.perform(get("/lab1/retrieveMember")
				.param("memberId", testArg_memberId)
				.param("password", testArg_password))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab1/retrieve-login"))
				.andExpect(model().attribute("message", BIZERR101));
	}

	@Test
	@DisplayName("ログアウトしログイン画面を表示する")
	void testLogoutMember() throws Exception {
		// assert
		mvc.perform(get("/lab1/logout"))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab1/retrieve-login"))
				.andExpect(model().attribute("message", BIZMSG101));
	}
}
