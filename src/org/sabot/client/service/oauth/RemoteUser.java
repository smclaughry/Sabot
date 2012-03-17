package org.sabot.client.service.oauth;

public class RemoteUser {
	private final String firstName;
	private final String lastName;
	private final String email;
	private final String remoteId;
	private final OauthProvider provider;
	
	public RemoteUser(String firstName, String lastName, String email, String remoteId, OauthProvider provider) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.remoteId = remoteId;
		this.provider = provider;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getRemoteId() {
		return remoteId;
	}

	public OauthProvider getProvider() {
		return provider;
	}
}
