package org.sabot.server.utility;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.Email;

/*
 * Library Sabot: a library for accelerating GWT and AppEngine development
 * 
 * Copyright (C) 2011  Phil Craven, Stephen McLaughry
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

public class MailUtility {
 	
	private static final String GMAIL_COM = "@gmail.com";

	public static class Recipient{
		private final RecipientType recipientType;
		private final Email address;
		private final String name;

		public Recipient(Message.RecipientType recipientType, Email address){
			this(recipientType, address, null);
		}
		
		public Recipient(Message.RecipientType recipientType, Email address, String name){
			this.recipientType = recipientType;
			this.address = address;
			this.name = name;			
		}

		public RecipientType getRecipientType() {
			return recipientType;
		}

		public Email getAddress() {
			return address;
		}

		public String getName() {
			return name;
		}
	}

	public void sendMail(Email senderAddress, String senderName, 
			 String subject, String messageBody, Email replyAddress,
			 Recipient... recipients) throws UnsupportedEncodingException, MessagingException{
		sendMail(senderAddress, senderName, null, subject, messageBody, replyAddress, recipients);
	}
	
	public void sendMail(Email senderAddress, String senderName, List<Email> bccAddresses,
						 String subject, String messageBody, Email replyAddress,
						 Recipient... recipients) throws UnsupportedEncodingException, MessagingException{
		MimeMessage msg = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
		if(senderName == null){
			msg.setFrom(new InternetAddress(senderAddress.getEmail()));			
		}else{
			msg.setFrom(new InternetAddress(senderAddress.getEmail(), senderName));			
		}
		if (null != replyAddress) {
			Address[] replyTo = {(new InternetAddress(replyAddress.getEmail()))};
			msg.setReplyTo(replyTo);
		}

		if (bccAddresses != null) {
			for(Email bccAddress : bccAddresses){
				msg.addRecipient(RecipientType.BCC, new InternetAddress(bccAddress.getEmail()));
			}
		}
		
		for(Recipient recipient : recipients){
			InternetAddress address;
			if(recipient.getName() == null){
				address = new InternetAddress(recipient.getAddress().getEmail());
			}else{
				address = new InternetAddress(recipient.getAddress().getEmail(), recipient.getName());
			}
			msg.addRecipient(recipient.getRecipientType(), address);
		}

		msg.setSubject(subject);
		msg.setContent(messageBody, "text/html");

		Transport.send(msg);
	}

	public String getServer(HttpServletRequest request){
		String server = request.getServerName();
		if(request.getServerPort() != 80 && request.getServerPort() != 443){
			return server + ":" + request.getServerPort();
		}
		return server;
	}
	
	public static Email compactGmailEmail(Email email){
		String emailStr = email.getEmail();
		if(emailStr.endsWith(GMAIL_COM)){
			emailStr = emailStr.substring(0, emailStr.indexOf(GMAIL_COM));
			return new Email(emailStr.replace("." , "") + GMAIL_COM);
		}
		return email;
	}
}
