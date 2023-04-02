/**
 * Lab6op_RegistMemberControllerTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod4.control;

import static jp.co.flm.common.util.MessageList.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jp.co.flm.common.exception.BusinessException;
import jp.co.flm.mod4.entity.Member;
import jp.co.flm.mod4.form.EmailForm;
import jp.co.flm.mod4.form.NewMemberForm;
import jp.co.flm.mod4.service.Lab6op_RegistMemberService;
import jp.co.flm.test.util.DummyDataAccessException;

/**
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Lab6op_RegistMemberControllerのテスト")
public class Lab6op_RegistMemberControllerTest {

	@InjectMocks
	Lab6op_RegistMemberController sut;

	@Mock
	Lab6op_RegistMemberService mock;

	@Autowired
	HandlerExceptionResolver handlerExceptionResolver;

	MockMvc mvc;

	MockHttpSession session;

	MockHttpServletRequest request;

	EmailForm emailForm;

	NewMemberForm newMemberForm;

	@BeforeEach
	void setUp() throws Exception {
		mvc = standaloneSetup(sut)
				.setHandlerExceptionResolvers(handlerExceptionResolver)
				.build();
	}

	@BeforeEach
	void setUpForm() throws Exception {
		emailForm = new EmailForm();
		newMemberForm = new NewMemberForm(
				"abc@fj.co.jp", null, null, null, "M", null, null);
		request = new MockHttpServletRequest();
		session = new MockHttpSession();
	}

	@Test
	@DisplayName("メールアドレス入力画面を表示する")
	void testReturnInputEmail() throws Exception {
		// assert
		mvc.perform(get("/lab6op/inputEmail"))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab6op/email-input"))
				.andExpect(model().attribute("emailForm", emailForm));
	}

	@Test
	@DisplayName("会員登録画面を表示する")
	void testValidateEmailForRegiter() throws Exception {
		// setup
		String email = "abc@fj.co.jp";
		emailForm.setEmail(email);

		// assert
		mvc.perform(post("/lab6op/inputMember")
				.param("email", email))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab6op/registration-input"))
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute("emailForm", emailForm))
				.andExpect(request().sessionAttribute("newMember", newMemberForm));
	}

	@Test
	@DisplayName("会員登録確認画面を表示する")
	void testConfirmRegistration() throws Exception {
		// setup
		String email = "abc@fj.co.jp";
		String password = "flm123";
		String confirmPassword = "flm123";
		String memberName = "富士通 太郎";
		String gender = "M";
		String address = "東京都港区港南2-13-34";
		String phone = "012-3456-7890";
		newMemberForm.setPassword(password);
		newMemberForm.setConfirmPassword(confirmPassword);
		newMemberForm.setMemberName(memberName);
		newMemberForm.setGender(gender);
		newMemberForm.setAddress(address);
		newMemberForm.setPhone(phone);

		// when
		session = (MockHttpSession) mvc.perform(post("/lab6op/inputMember")
				.param("email", email)
				.sessionAttr("newMember", newMemberForm))
				.andReturn().getRequest().getSession();

		// assert
		mvc.perform(post("/lab6op/confirmMember")
				.param("password", password)
				.param("confirmPassword", confirmPassword)
				.param("memberName", memberName)
				.param("gender", gender)
				.param("address", address)
				.param("phone", phone)
				.session(session))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab6op/registration-confirm"))
				.andExpect(model().hasNoErrors())
				.andExpect(request().sessionAttribute("newMember", newMemberForm));
	}

	@Test
	@DisplayName("会員登録完了画面を表示する")
	void testCommitRegistration() throws Exception {
		// setup
		String memberId = "abc@fj.co.jp";
		String password = "flm123";
		String confirmPassword = "flm123";
		String memberName = "富士通 太郎";
		String gender = "M";
		String address = "東京都港区港南2-13-34";
		String phone = "012-3456-7890";
		newMemberForm.setPassword(password);
		newMemberForm.setConfirmPassword(confirmPassword);
		newMemberForm.setMemberName(memberName);
		newMemberForm.setGender(gender);
		newMemberForm.setAddress(address);
		newMemberForm.setPhone(phone);
		Member returnVal = new Member(
				memberId, password, memberName, gender, address, phone, 0);

		// when
		session = (MockHttpSession) mvc.perform(post("/lab6op/confirmMember")
						.param("password", password)
						.param("confirmPassword", confirmPassword)
						.param("memberName", memberName)
						.param("gender", gender)
						.param("address", address)
						.param("phone", phone)
						.sessionAttr("newMember", newMemberForm))
						.andReturn().getRequest().getSession();

		// assert
		mvc.perform(post("/lab6op/commitMember")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab6op/registration-complete"))
				.andExpect(model().attribute("member", returnVal));
	}

	@Test
	@DisplayName("会員登録画面に戻る")
	void testReviseInput() throws Exception {
		// setup
		String password = "flm123";
		String confirmPassword = "flm123";
		String memberName = "富士通 太郎";
		String gender = "M";
		String address = "東京都港区港南2-13-34";
		String phone = "012-3456-7890";
		newMemberForm.setPassword(password);
		newMemberForm.setConfirmPassword(confirmPassword);
		newMemberForm.setMemberName(memberName);
		newMemberForm.setGender(gender);
		newMemberForm.setAddress(address);
		newMemberForm.setPhone(phone);

		// when
		session = (MockHttpSession) mvc.perform(post("/lab6op/confirmMember")
						.param("password", password)
						.param("confirmPassword", confirmPassword)
						.param("memberName", memberName)
						.param("gender", gender)
						.param("address", address)
						.param("phone", phone)
						.sessionAttr("newMember", newMemberForm))
						.andReturn().getRequest().getSession();

		// assert
		mvc.perform(post("/lab6op/reviseInput")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab6op/registration-input"))
				.andExpect(model().hasNoErrors())
				.andExpect(request().sessionAttribute("newMember", newMemberForm));
	}
	@Nested
	@SpringBootTest
	@DisplayName("EmailFormのテスト")
	class EmailFormTest{

		final Validator validator
				= Validation.buildDefaultValidatorFactory().getValidator();

		@Test
//		@Disabled
		@DisplayName("EmailFormの入力チェックエラー(@Size)を表示する")
		void testValidateEmailForRegiter_SizeValidationError() throws Exception {
			// setup
			String email = new String("a").repeat(42) + "@fj.co.jp";
			emailForm.setEmail(email);

			// assert
			Set<ConstraintViolation<EmailForm>> violations
					= validator.validate(emailForm);
			assertThat(violations).hasSize(1);
			assertThat(violations.iterator().next().getMessageTemplate())
					.isEqualTo("{javax.validation.constraints.Size.message}");

			mvc.perform(post("/lab6op/inputMember")
					.param("email", email))
					.andExpect(view().name("/lab6op/email-input"))
					.andExpect(model().hasErrors());
		}

		@Test
		@DisplayName("EmailFormの入力チェックエラー(@Email)を表示する")
		void testValidateEmailForRegiter_EmailValidationError() throws Exception {
			// setup
			String email = "abc";
			emailForm.setEmail(email);

			// assert
			Set<ConstraintViolation<EmailForm>> violations
					= validator.validate(emailForm);
			assertThat(violations.iterator().next().getMessageTemplate())
					.isEqualTo("{javax.validation.constraints.Email.message}");

			mvc.perform(post("/lab6op/inputMember")
					.param("email", email))
					.andExpect(view().name("/lab6op/email-input"))
					.andExpect(model().hasErrors());
		}

		@Test
		@DisplayName("EmailFormの入力チェックエラー(@NotBlank)を表示する")
		void testValidateEmailForRegiter_NotBlankValidationError() throws Exception {
			// setup
			String email = null;
			emailForm.setEmail(email);

			// assert
			Set<ConstraintViolation<EmailForm>> violations
					= validator.validate(emailForm);
			assertThat(violations).hasSize(1);
			assertThat(violations.iterator().next().getMessageTemplate())
					.isEqualTo("{javax.validation.constraints.NotBlank.message}");

			mvc.perform(post("/lab6op/inputMember")
					.param("email", email))
					.andExpect(view().name("/lab6op/email-input"))
					.andExpect(model().hasErrors());
		}
	}

	@Nested
	@SpringBootTest
	@DisplayName("NewMemberFormのテスト")
	class NewMemberFormTest{

		final Validator validator
				= Validation.buildDefaultValidatorFactory().getValidator();

		@Test
		@DisplayName("NewMemberFormの入力チェックエラー(@Size)を表示する")
		void testConfirmRegistration_SizeValidationError() throws Exception {
			// setup
			String password = new String("a").repeat(9);
			String confirmPassword = new String("a").repeat(3);
			String memberName = new String("a").repeat(41);
			String gender = "12";
			String address = new String("a").repeat(81);
			String phone = "012-3456-7890";
			newMemberForm.setPassword(password);
			newMemberForm.setConfirmPassword(confirmPassword);
			newMemberForm.setMemberName(memberName);
			newMemberForm.setGender(gender);
			newMemberForm.setAddress(address);
			newMemberForm.setPhone(phone);

			// when
			session.setAttribute("newMember", newMemberForm);

			// assert
			Set<ConstraintViolation<NewMemberForm>> violations
					= validator.validate(newMemberForm);
			assertThat(violations).hasSize(5);
			for (ConstraintViolation<NewMemberForm> constraintViolation : violations) {
				assertThat(constraintViolation.getMessageTemplate())
						.isEqualTo("{javax.validation.constraints.Size.message}");
			}

			mvc.perform(post("/lab6op/confirmMember")
					.param("password", password)
					.param("confirmPassword", confirmPassword)
					.param("memberName", memberName)
					.param("gender", gender)
					.param("address", address)
					.param("phone", phone)
					.session(session))
					.andExpect(view().name("/lab6op/registration-input"))
					.andExpect(model().hasErrors());
		}

		@Test
		@DisplayName("NewMemberFormの入力チェックエラー(@NotBlank)を表示する")
		void testConfirmRegistration_NotBlankValidationError() throws Exception {
			// setup
			String password = null;
			String confirmPassword = null;
			String memberName = null;
			String gender = null;
			String address = null;
			String phone = "012-3456-7890";
			newMemberForm.setPassword(password);
			newMemberForm.setConfirmPassword(confirmPassword);
			newMemberForm.setMemberName(memberName);
			newMemberForm.setAddress(address);
			newMemberForm.setGender(gender);

			// when
			session.setAttribute("newMember", newMemberForm);

			// assert
			Set<ConstraintViolation<NewMemberForm>> violations
					= validator.validate(newMemberForm);
			assertThat(violations).hasSize(5);
			for (ConstraintViolation<NewMemberForm> constraintViolation : violations) {
				assertThat(constraintViolation.getMessageTemplate())
						.isEqualTo("{javax.validation.constraints.NotBlank.message}");
			}

			mvc.perform(post("/lab6op/confirmMember")
					.param("password", password)
					.param("confirmPassword", confirmPassword)
					.param("memberName", memberName)
					.param("gender", gender)
					.param("address", address)
					.param("phone", phone)
					.session(session))
					.andExpect(view().name("/lab6op/registration-input"))
					.andExpect(model().hasErrors());
		}

		@Test
		@DisplayName("NewMemberFormの入力チェックエラー(@Pattern)を表示する")
		void testConfirmRegistration_PatternValidationError() throws Exception {
			// setup
			String password = "flm123";
			String confirmPassword = "flm123";
			String memberName = "富士通 太郎";
			String gender = "M";
			String address = "東京都港区港南2-13-34";
			String phone = "012-3456-78901";
			newMemberForm.setPassword(password);
			newMemberForm.setConfirmPassword(confirmPassword);
			newMemberForm.setMemberName(memberName);
			newMemberForm.setAddress(address);
			newMemberForm.setPhone(phone);

			// when
			session.setAttribute("newMember", newMemberForm);

			// assert
			Set<ConstraintViolation<NewMemberForm>> violations
					= validator.validate(newMemberForm);
			assertThat(violations).hasSize(1);
			assertThat(violations.iterator().next().getMessageTemplate())
					.isEqualTo("{javax.validation.constraints.Pattern.message}");

			mvc.perform(post("/lab6op/confirmMember")
					.param("password", password)
					.param("confirmPassword", confirmPassword)
					.param("memberName", memberName)
					.param("gender", gender)
					.param("address", address)
					.param("phone", phone)
					.session(session))
					.andExpect(view().name("/lab6op/registration-input"))
					.andExpect(model().hasErrors());
		}

		@Test
		@DisplayName("NewMemberFormの入力チェックエラー(パスワードと確認用パスワードが一致しない)を表示する")
		void testConfirmRegistration_PwValidationError() throws Exception {
			// setup
			String password = "flm123";
			String confirmPassword = "flm321";
			String memberName = "富士通 太郎";
			String gender = "M";
			String address = "東京都港区港南2-13-34";
			String phone = "012-3456-7890";
			newMemberForm.setPassword(password);
			newMemberForm.setConfirmPassword(confirmPassword);
			newMemberForm.setMemberName(memberName);
			newMemberForm.setGender(gender);
			newMemberForm.setAddress(address);
			newMemberForm.setPhone(phone);

			// when
			session.setAttribute("newMember", newMemberForm);

			// assert
			mvc.perform(post("/lab6op/confirmMember")
					.param("password", password)
					.param("confirmPassword", confirmPassword)
					.param("memberName", memberName)
					.param("gender", gender)
					.param("address", address)
					.param("phone", phone)
					.session(session))
					.andExpect(view().name("/lab6op/registration-input"))
					.andExpect(model().attribute("message", BIZERR602));
		}
	}

	@Nested
	@SpringBootTest
	@DisplayName("例外処理のテスト")
	class ExceptionHandlerTest{

		@Test
		@DisplayName("会員登録確認画面表示時にセッションが無効になる")
		void testConfirmRegistration_SessionExpired() throws Exception {
			// setup
			String password = "flm123";
			String confirmPassword = "flm123";
			String memberName = "富士通 太郎";
			String gender = "M";
			String address = "東京都港区港南2-13-34";
			String phone = "012-3456-7890";

			// assert
			mvc.perform(post("/lab6op/confirmMember")
					.param("password", password)
					.param("confirmPassword", confirmPassword)
					.param("memberName", memberName)
					.param("gender", gender)
					.param("address", address)
					.param("phone", phone))
					.andExpect(status().isBadGateway())
					.andExpect(view().name("/lab6op/email-input"))
					.andExpect(model().attribute("emailForm", emailForm))
					.andExpect(model().attribute("message", BIZERR302));
		}

		@Test
		@DisplayName("会員登録完了画面表示時にセッションが無効になる")
		void testCommitRegistration_SessionExpired() throws Exception {
			// assert
			mvc.perform(post("/lab6op/commitMember"))
					.andExpect(status().isBadGateway())
					.andExpect(view().name("/lab6op/email-input"))
					.andExpect(model().attribute("emailForm", emailForm))
					.andExpect(model().attribute("message", BIZERR302));
		}

		@Test
		@DisplayName("会員登録画面に戻る際にセッションが無効になる")
		void testReviseInput_SessionExpired() throws Exception {
			// assert
			mvc.perform(post("/lab6op/reviseInput"))
					.andExpect(status().isBadGateway())
					.andExpect(view().name("/lab6op/email-input"))
					.andExpect(model().attribute("emailForm", emailForm))
					.andExpect(model().attribute("message", BIZERR302));
		}

		@Test
		@DisplayName("会員登録画面表示時に業務例外が発生時する")
		void testValidateEmailForRegiter_BizExOccured() throws Exception {
			// setup
			String email = "iida@fj.co.jp";
			emailForm.setEmail(email);

			// when
			doThrow(new BusinessException(BIZERR601))
					.when(mock).validateEmail(email);

			// assert
			mvc.perform(post("/lab6op/inputMember")
					.param("email", email))
					.andExpect(status().isBadRequest())
					.andExpect(view().name("/lab6op/email-input"))
					.andExpect(model().attribute("emailForm", new EmailForm()))
					.andExpect(model().attribute("message", BIZERR601));
		}

		@Test
		@DisplayName("会員登録完了画面表示時に業務例外が発生する")
		void testCommitRegistration_BizExOccured() throws Exception {
			// setup
			String memberId = "abc@fj.co.jp";
			String password = "flm123";
			String confirmPassword = "flm123";
			String memberName = "富士通 太郎";
			String gender = "M";
			String address = "東京都港区港南2-13-34";
			String phone = "012-3456-7890";
			newMemberForm.setPassword(password);
			newMemberForm.setConfirmPassword(confirmPassword);
			newMemberForm.setMemberName(memberName);
			newMemberForm.setGender(gender);
			newMemberForm.setAddress(address);
			newMemberForm.setPhone(phone);
			Member returnVal
					= new Member(memberId, password, memberName, gender, address, phone, 0);

			// when
			doThrow(new BusinessException(BIZERR601))
					.when(mock).registMember(returnVal);
			session = (MockHttpSession) mvc.perform(post("/lab6op/confirmMember")
							.param("password", password)
							.param("confirmPassword", confirmPassword)
							.param("memberName", memberName)
							.param("gender", gender)
							.param("address", address)
							.param("phone", phone)
							.sessionAttr("newMember", newMemberForm))
							.andReturn().getRequest().getSession();

			// assert
			mvc.perform(
					post("/lab6op/commitMember")
					.session(session))
					.andExpect(status().isBadRequest())
					.andExpect(view().name("/lab6op/email-input"))
					.andExpect(model().attribute("emailForm", new EmailForm()))
					.andExpect(model().attribute("message", BIZERR601));
		}

		@Test
		@DisplayName("会員登録画面表示時にシステムエラーが発生時する")
		void testValidateEmailForRegiter_SysExOccured() throws Exception {
			// setup
			String email = "abc@fj.co.jp";
			emailForm.setEmail(email);

			// when
			doThrow(new DummyDataAccessException())
					.when(mock).validateEmail(email);

			// assert
			mvc.perform(post("/lab6op/inputMember")
					.param("email", email))
					.andExpect(status().isInternalServerError())
					.andExpect(view().name("/error"))
					.andExpect(model().attribute("message", SYSERR000));
		}

		@Test
		@DisplayName("会員登録完了画面表示時にシステムエラーが発生する")
		void testCommitRegistration_SysExOccured() throws Exception {
			// setup
			String memberId = "abc@fj.co.jp";
			String password = "flm123";
			String confirmPassword = "flm123";
			String memberName = "富士通 太郎";
			String gender = "M";
			String address = "東京都港区港南2-13-34";
			String phone = "012-3456-7890";
			newMemberForm.setPassword(password);
			newMemberForm.setConfirmPassword(confirmPassword);
			newMemberForm.setMemberName(memberName);
			newMemberForm.setGender(gender);
			newMemberForm.setAddress(address);
			newMemberForm.setPhone(phone);
			Member returnVal = new Member(
					memberId, password, memberName, gender, address, phone, 0);

			// when
			doThrow(new DummyDataAccessException())
					.when(mock).registMember(returnVal);
			session = (MockHttpSession) mvc.perform(post("/lab6op/confirmMember")
							.param("password", password)
							.param("confirmPassword", confirmPassword)
							.param("memberName", memberName)
							.param("gender", gender)
							.param("address", address)
							.param("phone", phone)
							.sessionAttr("newMember", newMemberForm))
							.andReturn().getRequest().getSession();

			// assert
			mvc.perform(
					post("/lab6op/commitMember")
					.session(session))
					.andExpect(status().isInternalServerError())
					.andExpect(view().name("/error"))
					.andExpect(model().attribute("message", SYSERR000));
		}
	}
}
