/**
 * Lab2_RetrieveMemberControllerTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod3.control;

import static jp.co.flm.common.util.MessageList.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import jp.co.flm.mod3.entity.Member;
import jp.co.flm.mod3.form.LoginForm;
import jp.co.flm.mod3.service.Lab2_RetrieveMemberService;

/**
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Lab2_RetrieveMemberControllerのテスト")
public class Lab2_RetrieveMemberControllerTest {

	@InjectMocks
	Lab2_RetrieveMemberController sut;

	@Mock
	Lab2_RetrieveMemberService mock;

	MockMvc mvc;

	LoginForm loginForm;

	@BeforeEach
	void setUp() throws Exception {
		mvc = standaloneSetup(sut).build();
	}

	@BeforeEach
	void setUpForm() throws Exception {
		loginForm = new LoginForm();
	}

	@Test
	@DisplayName("ログイン画面を表示する")
	void testReturnLogin() throws Exception {
		// assert
		mvc.perform(get("/lab2/login"))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab2/retrieve-login"))
				.andExpect(model().attribute("loginForm", loginForm));
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
		mvc.perform(post("/lab2/retrieveMember")
				.param("memberId", testArg_memberId)
				.param("password", testArg_password))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab2/retrieve-member"))
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute("member", returnVal));
	}

	@Test
	@DisplayName("認証失敗時にログイン画面を表示する")
	void testRetrieveMember_BizExOccured() throws Exception {
		// setup
		String testArg_memberId = "iida@fj.co.jp";
		String testArg_password = "flm321";

		// when
		when(mock.authenticateMember(testArg_memberId, testArg_password))
				.thenReturn(null);

		// assert
		mvc.perform(post("/lab2/retrieveMember")
				.param("memberId", testArg_memberId)
				.param("password", testArg_password))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab2/retrieve-login"))
				.andExpect(model().attribute("message", BIZERR101));
	}

	@Test
	@DisplayName("ログアウトしログイン画面を表示する")
	void testLogoutMember() throws Exception {
		// assert
		mvc.perform(get("/lab2/logout"))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab2/retrieve-login"))
				.andExpect(model().attribute("message", BIZMSG101));
	}

	@Nested
	@SpringBootTest
	@DisplayName("LoginFormのテスト")
	class LoginFormTest{

		final Validator validator
				= Validation.buildDefaultValidatorFactory().getValidator();

		@Test
		@DisplayName("入力チェックエラー(@Email)を表示する")
		void testRetrieveMember_EmailValidationError() throws Exception {
			// setup
			String testArg_memberId = "iida";
			String testArg_password = "flm123";
			loginForm.setMemberId(testArg_memberId);
			loginForm.setPassword(testArg_password);

			// assert
			Set<ConstraintViolation<LoginForm>> violations
					= validator.validate(loginForm);
			assertThat(violations).hasSize(1);
			assertThat(violations.iterator().next().getMessageTemplate())
					.isEqualTo("{javax.validation.constraints.Email.message}");

			mvc.perform(post("/lab2/retrieveMember")
					.param("memberId", testArg_memberId)
					.param("password", testArg_password))
					.andExpect(view().name("/lab2/retrieve-login"))
					.andExpect(model().hasErrors());
		}

		@Test
//		@Disabled
		@DisplayName("入力チェックエラー(@Size)を表示する")
		void testRetrieveMember_SizeValidationError() throws Exception {
			// setup
			String testArg_memberId = new String("a").repeat(42) + "@fj.co.jp";
			String testArg_password = "flm";
			loginForm.setMemberId(testArg_memberId);
			loginForm.setPassword(testArg_password);

			// assert
			Set<ConstraintViolation<LoginForm>> violations
					= validator.validate(loginForm);
			assertThat(violations).hasSize(2);
			for (ConstraintViolation<LoginForm> constraintViolation : violations) {
				assertThat(constraintViolation.getMessageTemplate())
						.isEqualTo("{javax.validation.constraints.Size.message}");
			}

			mvc.perform(post("/lab2/retrieveMember")
					.param("memberId", testArg_memberId)
					.param("password", testArg_password))
					.andExpect(view().name("/lab2/retrieve-login"))
					.andExpect(model().hasErrors());
		}

		@Test
		@DisplayName("入力チェックエラー(@NotBlank)を表示する")
		void testRetrieveMember_NotBlankValidationError() throws Exception {
			// setup
			String testArg_memberId = null;
			String testArg_password = null;
			loginForm.setMemberId(testArg_memberId);
			loginForm.setPassword(testArg_password);

			// assert
			Set<ConstraintViolation<LoginForm>> violations
					= validator.validate(loginForm);
			assertThat(violations).hasSize(2);
			for (ConstraintViolation<LoginForm> constraintViolation : violations) {
				assertThat(constraintViolation.getMessageTemplate())
						.isEqualTo("{javax.validation.constraints.NotBlank.message}");
			}

			mvc.perform(post("/lab2/retrieveMember")
					.param("memberId", testArg_memberId)
					.param("password", testArg_password))
					.andExpect(view().name("/lab2/retrieve-login"))
					.andExpect(model().hasErrors());
		}
	}
}
