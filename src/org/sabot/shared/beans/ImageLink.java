package org.sabot.shared.beans;


import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class ImageLink extends DatastoreObject<ImageLink> {
	private static final long serialVersionUID = 4760563059194281036L;
	private String blobKey;
	private String imageUrl;
	private Key<? extends SabotUser> userKey;

	public ImageLink(){
		//for gwt
	}
	
	public ImageLink(Key<? extends SabotUser> userKey, String blobKey, String imageUrl) {
		this.userKey = userKey;
		this.blobKey = blobKey;
		this.imageUrl = imageUrl;
	}

	@Override
	public boolean equals(Object other){
		if (other == null) { return false; }
		if (other == this) { return true; }
		if (other.getClass() != getClass()) {return false;}
		
		ImageLink otherUser = (ImageLink) other;
		
		return new EqualsBuilder().append(getId(), otherUser.getId()).isEquals();
	}
	
	@Override
	public int hashCode(){
		return new HashCodeBuilder(113, 59).append(getId())
										   .append(blobKey)
										   .append(imageUrl)
										   .toHashCode();
	}

	@Override
	protected Class<ImageLink> getClassForKey() {
		return ImageLink.class;
	}

	public String getBlobKey() {
		return blobKey;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public Key<? extends SabotUser> getUserKey() {
		return userKey;
	}
}