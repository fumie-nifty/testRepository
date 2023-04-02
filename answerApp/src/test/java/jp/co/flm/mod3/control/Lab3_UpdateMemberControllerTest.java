/**
 * Lab3_UpdateMemberControllerTest.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.mod3.control;

import static jp.co.flm.common.util.MessageList.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
import jp.co.flm.common.exception.SystemException;
import jp.co.flm.mod3.entity.Member;
import jp.co.flm.mod3.form.UpdateMemberForm;
import jp.co.flm.mod3.service.Lab3_UpdateMemberService;

/**
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Lab3_UpdateMemberControllerのテスト")
public class Lab3_UpdateMemberControllerTest {

	@InjectMocks
	Lab3_UpdateMemberController sut;

	@Mock
	Lab3_UpdateMemberService mock;

	@Autowired
	HandlerExceptionResolver handlerExceptionResolver;

	MockMvc mvc;

	MockHttpSession session;

	MockHttpServletRequest request;

	UpdateMemberForm updateMemberForm;

	@BeforeEach
	void setUp() throws Exception {
		mvc = standaloneSetup(sut)
				.setHandlerExceptionResolvers(handlerExceptionResolver)
				.build();
	}

	@BeforeEach
	void setUpForm() throws Exception {
		updateMemberForm = new UpdateMemberForm(
				"iida@fj.co.jp", "flm123", "飯田 哲夫", "M", "東京都 大田区 池上0-0-0",
				"03-1111-2222", 652);
		request = new MockHttpServletRequest();
		session = new MockHttpSession();
	}

	@Test
	@DisplayName("会員更新画面を表示する")
	void testInputUpdate() throws Exception {
		// setup
		Member testArg_member = new Member(
				"iida@fj.co.jp", "flm123", "飯田 哲夫", "M", "東京都 大田区 池上0-0-0",
				"03-1111-2222", 652);

		// when
		when(mock.authenticateMember(anyString(), anyString()))
				.thenReturn(testArg_member);

		// assert
		mvc.perform(get("/lab3/inputUpdate"))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab3/update-input"))
				.andExpect(request().sessionAttribute("updateMember", updateMemberForm));
	}

	@Test
	@DisplayName("会員更新確認画面を表示する")
	void testConfirmUpdate() throws Exception {
		// setup
		String testArg_memberName = "富士通 太郎";
		String testArg_gender = "M";
		String testArg_address = "東京都港区港南2-13-34";
		String testArg_phone = "012-3456-7890";
		updateMemberForm.setMemberName(testArg_memberName);
		updateMemberForm.setGender(testArg_gender);
		updateMemberForm.setAddress(testArg_address);
		updateMemberForm.setPhone(testArg_phone);

		// when
		session.setAttribute("updateMember", updateMemberForm);
		request.setSession(session);

		// assert
		mvc.perform(post("/lab3/confirmUpdate")
				.param("memberName", testArg_memberName)
				.param("gender", testArg_gender)
				.param("address", testArg_address)
				.param("phone", testArg_phone)
				.session(session))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab3/update-confirm"))
				.andExpect(model().hasNoErrors())
				.andExpect(request().sessionAttribute("updateMember", updateMemberForm));
	}

	@Test
	@DisplayName("会員更新結果画面を表示する")
	void testCommitUpdate() throws Exception {
		// setup
		String testArg_memberName = "富士通 太郎";
		String testArg_gender = "M";
		String testArg_address = "東京都港区港南2-13-34";
		String testArg_phone = "012-3456-7890";
		Member returnVal = new Member(
				"iida@fj.co.jp", "flm123", testArg_memberName, testArg_gender,
				testArg_address, testArg_phone, 652);

		// when
		session = (MockHttpSession) mvc.perform(post("/lab3/confirmUpdate")
						.param("memberName", testArg_memberName)
						.param("gender", testArg_gender)
						.param("address", testArg_address)
						.param("phone", testArg_phone)
						.sessionAttr("updateMember", updateMemberForm))
						.andReturn().getRequest().getSession();

		// assert
		mvc.perform(post("/lab3/commitUpdate")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab3/update-complete"))
				.andExpect(model().attribute("member", returnVal));
	}

	@Test
	@DisplayName("会員更新画面に戻る")
	void testReviseInput() throws Exception {
		// setup
		String testArg_memberName = "富士通 太郎";
		String testArg_gender = "M";
		String testArg_address = "東京都港区港南2-13-34";
		String testArg_phone = "012-3456-7890";
		updateMemberForm.setMemberName(testArg_memberName);
		updateMemberForm.setGender(testArg_gender);
		updateMemberForm.setAddress(testArg_address);
		updateMemberForm.setPhone(testArg_phone);

		// when
		session = (MockHttpSession) mvc.perform(post("/lab3/confirmUpdate")
						.param("memberName", testArg_memberName)
						.param("gender", testArg_gender)
						.param("address", testArg_address)
						.param("phone", testArg_phone)
						.sessionAttr("updateMember", updateMemberForm))
						.andReturn().getRequest().getSession();

		// assert
		mvc.perform(get("/lab3/reviseInput")
				.session(session))
				.andExpect(status().isOk())
				.andExpect(view().name("/lab3/update-input"))
				.andExpect(model().hasNoErrors())
				.andExpect(request().sessionAttribute("updateMember", updateMemberForm));
	}

	@Nested
	@SpringBootTest
	@DisplayName("UpdateMemberFormのテスト")
	class UpdateMemberFormTest{

		final Validator validator
				= Validation.buildDefaultValidatorFactory().getValidator();

		@Test
		@DisplayName("入力チェックエラー(@Size)を表示する")
		void testConfirmUpdate_SizeValidationError() throws Exception {
			// setup
			String testArg_memberName = new String("a").repeat(41);
			String testArg_gender = "12";
			String testArg_address = new String("a").repeat(81);
			String testArg_phone = "012-3456-7890";
			updateMemberForm.setMemberName(testArg_memberName);
			updateMemberForm.setGender(testArg_gender);
			updateMemberForm.setAddress(testArg_address);
			updateMemberForm.setPhone(testArg_phone);

			// when
			session.setAttribute("updateMember", updateMemberForm);

			// assert
			Set<ConstraintViolation<UpdateMemberForm>> violations
					= validator.validate(updateMemberForm);
			assertThat(violations).hasSize(3);
			for (ConstraintViolation<UpdateMemberForm> constraintViolation : violations) {
				assertThat(constraintViolation.getMessageTemplate())
						.isEqualTo("{javax.validation.constraints.Size.message}");
			}

			mvc.perform(post("/lab3/confirmUpdate")
					.param("memberName", testArg_memberName)
					.param("gender", testArg_gender)
					.param("address", testArg_address)
					.param("phone", testArg_phone)
					.session(session))
					.andExpect(view().name("/lab3/update-input"))
					.andExpect(model().hasErrors());
		}

		@Test
		@DisplayName("入力チェックエラー(@NotBlank)を表示する")
		void testConfirmUpdate_NotBlankValidationError() throws Exception {
			// setup
			String testArg_memberName = null;
			String testArg_gender = null;
			String testArg_address = null;
			String testArg_phone = null;
			updateMemberForm.setMemberName(testArg_memberName);
			updateMemberForm.setGender(testArg_gender);
			updateMemberForm.setAddress(testArg_address);
			updateMemberForm.setPhone(testArg_phone);

			// when
			session.setAttribute("updateMember", updateMemberForm);

			// assert
			Set<ConstraintViolation<UpdateMemberForm>> violations
					= validator.validate(updateMemberForm);
			assertThat(violations).hasSize(4);
			for (ConstraintViolation<UpdateMemberForm> constraintViolation : violations) {
				assertThat(constraintViolation.getMessageTemplate())
						.isEqualTo("{javax.validation.constraints.NotBlank.message}");
			}

			mvc.perform(post("/lab3/confirmUpdate")
					.param("memberName", testArg_memberName)
					.param("gender", testArg_gender)
					.param("address", testArg_address)
					.param("phone", testArg_phone)
					.session(session))
					.andExpect(view().name("/lab3/update-input"))
					.andExpect(model().hasErrors());
		}
	}

	@Nested
	@SpringBootTest
	@DisplayName("例外処理のテスト")
	class ExceptionHandlerTest{

		@Test
		@DisplayName("会員更新確認画面表示時にセッションが無効になる")
		void testConfirmUpdate_SessionExpired() throws Exception {
			// setup
			String testArg_memberName = "富士通 太郎";
			String testArg_gender = "M";
			String testArg_address = "東京都港区港南2-13-34";
			String testArg_phone = "012-3456-7890";
			Member testArg_member = new Member(
					"iida@fj.co.jp", "flm123", "飯田 哲夫", "M", "東京都 大田区 池上0-0-0",
					"03-1111-2222", 652);

			// when
			when(mock.authenticateMember(anyString(), anyString()))
					.thenReturn(testArg_member);

			// assert
			mvc.perform(post("/lab3/confirmUpdate")
					.param("memberName", testArg_memberName)
					.param("gender", testArg_gender)
					.param("address", testArg_address)
					.param("phone", testArg_phone))
					.andExpect(status().isBadGateway())
					.andExpect(view().name("/lab3/update-input"))
					.andExpect(model().attribute("message", BIZERR302))
					.andExpect(model().attribute("updateMember", updateMemberForm));
		}

		@Test
		@DisplayName("会員更新結果画面表示時にセッションが無効になる")
		void testCommitUpdate_SessionExpired() throws Exception {
			// setup
			Member testArg_member = new Member(
					"iida@fj.co.jp", "flm123", "飯田 哲夫", "M", "東京都 大田区 池上0-0-0",
					"03-1111-2222", 652);

			// when
			when(mock.authenticateMember(anyString(), anyString()))
					.thenReturn(testArg_member);

			// assert
			mvc.perform(post("/lab3/commitUpdate"))
					.andExpect(status().isBadGateway())
					.andExpect(view().name("/lab3/update-input"))
					.andExpect(model().attribute("message", BIZERR302))
					.andExpect(model().attribute("updateMember", updateMemberForm));
		}

		@Test
		@DisplayName("会員更新画面へ戻る際にセッションが無効になる")
		void testReviseInput_SessionExpired() throws Exception {
			// setup
			Member testArg_member = new Member(
					"iida@fj.co.jp", "flm123", "飯田 哲夫", "M", "東京都 大田区 池上0-0-0",
					"03-1111-2222", 652);

			// when
			when(mock.authenticateMember(anyString(), anyString()))
					.thenReturn(testArg_member);

			// assert
			mvc.perform(get("/lab3/reviseInput"))
					.andExpect(status().isBadGateway())
					.andExpect(view().name("/lab3/update-input"))
					.andExpect(model().attribute("message", BIZERR302))
					.andExpect(model().attribute("updateMember", updateMemberForm));
		}

		@Test
		@DisplayName("会員更新結果画面表示時に業務例外が発生時する")
		void testCommitUpdate_BizExOccured() throws Exception {
			// setup
			String testArg_memberName = "富士通 太郎";
			String testArg_gender = "M";
			String testArg_address = "東京都港区港南2-13-34";
			String testArg_phone = "012-3456-7890";
			Member testArg_member = new Member(
					"iida@fj.co.jp", "flm123", "飯田 哲夫", "M", "東京都 大田区 池上0-0-0",
					"03-1111-2222", 652);

			// when
			doThrow(new BusinessException(BIZERR301))
					.when(mock).setData(any());
			when(mock.authenticateMember(anyString(), anyString()))
					.thenReturn(testArg_member);
			session = (MockHttpSession) mvc.perform(post("/lab3/confirmUpdate")
					.param("memberName", testArg_memberName)
					.param("gender", testArg_gender)
					.param("address", testArg_address)
					.param("phone", testArg_phone)
					.sessionAttr("updateMember", updateMemberForm))
					.andReturn().getRequest().getSession();

			// assert
			mvc.perform(post("/lab3/commitUpdate")
					.session(session))
					.andExpect(status().isBadRequest())
					.andExpect(view().name("/lab3/update-input"))
					.andExpect(model().attribute("message", BIZERR301))
					.andExpect(model().attribute("updateMember", new UpdateMemberForm("iida@fj.co.jp", "flm123", "飯田 哲夫", "M", "東京都 大田区 池上0-0-0", "03-1111-2222", 652)));
		}

		@Test
		@DisplayName("会員更新結果画面表示時にシステムエラーが発生する")
		void testCommitUpdate_SysExOccured() throws Exception {
			// setup
			String testArg_memberName = "富士通 太郎";
			String testArg_gender = "M";
			String testArg_address = "東京都港区港南2-13-34";
			String testArg_phone = "012-3456-7890";

			// when
			doThrow(new SystemException())
					.when(mock).setData(any());
			session = (MockHttpSession) mvc.perform(post("/lab3/confirmUpdate")
					.param("memberName", testArg_memberName)
					.param("gender", testArg_gender)
					.param("address", testArg_address)
					.param("phone", testArg_phone)
					.sessionAttr("updateMember", updateMemberForm))
					.andReturn().getRequest().getSession();

			// assert
			mvc.perform(post("/lab3/commitUpdate")
					.session(session))
					.andExpect(status().isInternalServerError())
					.andExpect(view().name("/error"))
					.andExpect(model().attribute("message", SYSERR000));
		}
	}
}
