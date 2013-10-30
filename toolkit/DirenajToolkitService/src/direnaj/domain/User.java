package direnaj.domain;

public class User {

	private String userId;
	private String userScreenName;
	
	public User(String userId, String screenName) {
		setUserId(userId);
		setUserScreenName(screenName);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserScreenName() {
		return userScreenName;
	}

	public void setUserScreenName(String userScreenName) {
		this.userScreenName = userScreenName;
	}

	@Override
	public boolean equals(Object obj) {
		try {
			boolean isEqual = false;
			if (obj instanceof User) {
				User user = (User) obj;
				if (this.userId.equalsIgnoreCase(user.getUserId())) {
					isEqual = true;
				}
			}
			return isEqual;
		} catch (Exception e) {
			return false;
		}
	}
	
	public int hashCode() {
	    return (int) (Long.valueOf(userId)%1000);
	}

}
