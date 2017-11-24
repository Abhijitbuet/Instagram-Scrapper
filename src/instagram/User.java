package instagram;

public class User {
private String username;
private String numberOfFollowers;
private String numberOfFollowing;
public User(String username, String numberOfFollowers, String numberOfFollowing) {
	
	this.username = username;
	this.numberOfFollowers = numberOfFollowers;
	this.numberOfFollowing = numberOfFollowing;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getNumberOfFollowers() {
	return numberOfFollowers;
}
public void setNumberOfFollowers(String numberOfFollowers) {
	this.numberOfFollowers = numberOfFollowers;
}
public String getNumberOfFollowing() {
	return numberOfFollowing;
}
public void setNumberOfFollowing(String numberOfFollowing) {
	this.numberOfFollowing = numberOfFollowing;
}
@Override
public String toString() {
	return "User [username=" + username + ", numberOfFollowers=" + numberOfFollowers + ", numberOfFollowing="
			+ numberOfFollowing + "]";
}

}
