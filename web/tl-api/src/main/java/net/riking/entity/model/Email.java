package net.riking.entity.model;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

/**
 * 发送邮件必要字段
 */
public class Email extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6032956463780926056L;

	@Comment("收件人邮箱")
	private String receiveMail;

	@Comment("发件人名称")
	private String sender;

	@Comment("发件人邮箱账号")
	private String myAccount;

	@Comment("发件人邮箱密码")
	private String myPassword;

	@Comment("发件人邮箱的 SMTP 服务器地址")
	private String mySMTPHost;

	@Comment("收件人名称")
	private String receiver;

	@Comment("主题")
	private String theme;

	@Comment("邮件正文")
	private String content;

	public Email() {
		super();
	}

	public Email(String myAccount, String myPassword, String mySMTPHost, String sender) {
		super();
		this.myAccount = myAccount;
		this.myPassword = myPassword;
		this.mySMTPHost = mySMTPHost;
		this.sender = sender;
	}

	public Email(String receiveMail, String sender, String myAccount, String myPassword, String mySMTPHost,
			String receiver, String theme, String content) {
		super();
		this.receiveMail = receiveMail;
		this.sender = sender;
		this.myAccount = myAccount;
		this.myPassword = myPassword;
		this.mySMTPHost = mySMTPHost;
		this.receiver = receiver;
		this.theme = theme;
		this.content = content;
	}

	public String getMyAccount() {
		return myAccount;
	}

	public void setMyAccount(String myAccount) {
		this.myAccount = myAccount;
	}

	public String getMyPassword() {
		return myPassword;
	}

	public void setMyPassword(String myPassword) {
		this.myPassword = myPassword;
	}

	public String getMySMTPHost() {
		return mySMTPHost;
	}

	public void setMySMTPHost(String mySMTPHost) {
		this.mySMTPHost = mySMTPHost;
	}

	public String getReceiveMail() {
		return receiveMail;
	}

	public void setReceiveMail(String receiveMail) {
		this.receiveMail = receiveMail;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Email [receiveMail=" + receiveMail + ", sender=" + sender + ", myAccount=" + myAccount + ", myPassword="
				+ myPassword + ", mySMTPHost=" + mySMTPHost + ", receiver=" + receiver + ", theme=" + theme
				+ ", content=" + content + "]";
	}

}
