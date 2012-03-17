package org.sabot.client.twitter;

public class UserInfo {
	private final String screenName;
	private final String name;
	private final String profileImageUrl;
	private final String friendsCount;
	private final String followersCount;
	private final String description;
	private final String url;
	private final String id;
	private final String statusesCount;

	public UserInfo(String screenName, 
					String name, 
					String profileImageUrl, 
					String friendsCount, 
					String followersCount, 
					String description, 
					String url,
					String id, 
					String statusesCount) 
	{
		this.screenName = screenName;
		this.name = name;
		this.profileImageUrl = profileImageUrl;
		this.friendsCount = friendsCount;
		this.followersCount = followersCount;
		this.description = description;
		this.url = url;
		this.id = id;
		this.statusesCount = statusesCount;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getName() {
		return name;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public String getFriendsCount() {
		return friendsCount;
	}

	public String getFollowersCount() {
		return followersCount;
	}

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}

	public String getId() {
		return id;
	}

	public String getStatusesCount() {
		return statusesCount;
	}

}
