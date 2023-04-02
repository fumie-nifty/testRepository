/**
 * MessageList.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package jp.co.flm.common.util;

/**
 * メッセージを管理するユーティリティクラス
 * @author FLM
 * @version 1.0 yyyy/mm/dd
 */
public final class MessageList {

	/**
	 * システムエラー時のエラーメッセージ
	 * <ul>
	 * <li>メッセージID：SYSERR000</li>
	 * <li>メッセージ本文：{@value}</li>
	 * </ul>
	 */
	public static final String SYSERR000 = "システムエラーです。システム管理者に連絡してください。";

	/**
	 * 業務エラー（認証失敗）時のエラーメッセージ
	 * <ul>
	 * <li>メッセージID：BIZERR101</li>
	 * <li>メッセージ本文：{@value}</li>
	 * </ul>
	 */
	public static final String BIZERR101 = "会員IDまたはパスワードが違います。";

	/**
	 * 業務エラー（更新失敗）時のエラーメッセージ
	 * <ul>
	 * <li>メッセージID：BIZERR301</li>
	 * <li>メッセージ本文：{@value}</li>
	 * </ul>
	 */
	public static final String BIZERR301 = "会員情報の更新に失敗しました。再度操作をやりなおしてください。";

	/**
	 * 業務エラー（セッション無効）時のエラーメッセージ
	 * <ul>
	 * <li>メッセージID：BIZERR302</li>
	 * <li>メッセージ本文：{@value}</li>
	 * </ul>
	 */
	public static final String BIZERR302 = "セッションが無効になりました。再度操作をやりなおしてください。";

	/**
	 * 業務エラー（検索結果0件）時のエラーメッセージ
	 * <ul>
	 * <li>メッセージID：BIZERR401</li>
	 * <li>メッセージ本文：{@value}</li>
	 * </ul>
	 */
	public static final String BIZERR401 = "対象の商品は現在ありません。";

	/**
	 * 業務エラー（一意性制約違反）時のエラーメッセージ
	 * <ul>
	 * <li>メッセージID：BIZERR601</li>
	 * <li>メッセージ本文：{@value}</li>
	 * </ul>
	 */
	public static final String BIZERR601 = "そのメールアドレスはすでに登録されています。";

	/**
	 * 業務エラー（PW不一致）時のエラーメッセージ
	 * <ul>
	 * <li>メッセージID：BIZERR602</li>
	 * <li>メッセージ本文：{@value }</li>
	 * </ul>
	 */
	public static final String BIZERR602 = "パスワードと確認用パスワードが一致しません。";

	/**
	 * 業務エラー（一意性制約違反）時のエラーメッセージ
	 * <ul>
	 * <li>メッセージID：BIZERR611</li>
	 * <li>メッセージ本文：{@value}</li>
	 * </ul>
	 */
	public static final String BIZERR611 = "商品の登録に失敗しました。再度操作をやりなおしてください。";

	/**
	 * ログアウト時の業務メッセージ
	 * <ul>
	 * <li>メッセージID：BIZMSG101</li>
	 * <li>メッセージ本文：{@value}</li>
	 * </ul>
	 */
	public static final String BIZMSG101 = "ログアウトしました。";

	/** プライベート・コンストラクター */
	private MessageList() {}
}
