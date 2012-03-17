package org.sabot.shared.beans;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;


import com.google.appengine.api.datastore.Email;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class SystemConfiguration extends DatastoreObject<SystemConfiguration> {

	private static final long serialVersionUID = 1760662706365695361L;
	@Embedded private ClientActivityInfoConfiguration statisticsConfiguration;
	private String bccEmails;
	private boolean compactGmailAddresses;
	private String overrideRecaptchaValue;
	private String preferredHostUrl;
	private String preferredHostScheme;
	private Integer preferredPort;

	public SystemConfiguration(){
	}
	
	public void setStatisticsConfiguration(ClientActivityInfoConfiguration statisticsConfiguration) {
		this.statisticsConfiguration = statisticsConfiguration;
	}

	public ClientActivityInfoConfiguration getStatisticsConfiguration() {
		return statisticsConfiguration;
	}

	public void setBccEmails(String bccEmails) {
		this.bccEmails = bccEmails;
	}

	public String getBccEmails() {
		return bccEmails;
	}
	
	public List<Email> getBccEmailsList(){
		ArrayList<Email> bccAddresses = new ArrayList<Email>();
		for(String email : bccEmails.split(";")){
			bccAddresses.add(new Email(email));
		}
		return bccAddresses;
	}

	public void setCompactGmailAddresses(boolean compactGmailAddresses) {
		this.compactGmailAddresses = compactGmailAddresses;
	}

	public boolean isCompactGmailAddresses() {
		return compactGmailAddresses;
	}

	public void setOverrideRecaptchaValue(String overrideRecaptchaValue) {
		this.overrideRecaptchaValue = overrideRecaptchaValue;
	}

	public String getOverrideRecaptchaValue() {
		return overrideRecaptchaValue;
	}

	public void setPreferredHostUrl(String preferredHostUrl) {
		this.preferredHostUrl = preferredHostUrl;
	}

	//This equates to a servername such as vetlinq.com
	public String preferredHostUrl(String suggestedHostUrl) {
		return preferredHostUrl == null ? suggestedHostUrl : preferredHostUrl;
	}

	public void setPreferredHostScheme(String preferredHostScheme) {
		this.preferredHostScheme = preferredHostScheme;
	}

	//This is the protocol like http
	public String preferredHostScheme(String suggestedProtocol) {
		return preferredHostScheme == null ? suggestedProtocol : preferredHostScheme; 
	}

	//This is the port
	public String preferedPort(int localPort) {
		int port = preferredPort == null ? localPort : preferredPort.intValue();
		if(port == 80 || port == 443 || port == 0){
			return "";
		}else{
			return ":" + port;
		}
	}

	@Override
	protected Class<SystemConfiguration> getClassForKey() {
		return SystemConfiguration.class;
	}
}
