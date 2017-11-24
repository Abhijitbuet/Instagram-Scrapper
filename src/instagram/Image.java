package instagram;

public class Image {
	private String text;
	private String username;
	private String numberOfFollowers;
	private String numberOfFollowing;
	private String type;
	private String dateTime;
	private String favourites;
	private String url;
	private String replyToURL;
	private String searchKey;
	
	public Image(String text, String username, String numberOfFollowers, String numberOfFollowing, String type,
			String dateTime, String favourites, String url, String replyToURL, String searchKey) {
		super();
		this.text = text;
		this.username = username;
		this.numberOfFollowers = numberOfFollowers;
		this.numberOfFollowing = numberOfFollowing;
		this.type = type;
		this.dateTime = dateTime;
		this.favourites = favourites;
		this.url = url;
		this.replyToURL = replyToURL;
		this.searchKey = searchKey;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getFavourites() {
		return favourites;
	}
	public void setFavourites(String favourites) {
		this.favourites = favourites;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getReplyToURL() {
		return replyToURL;
	}
	public void setReplyToURL(String replyToURL) {
		this.replyToURL = replyToURL;
	}
	public String getSearchKey() {
		return searchKey;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	
}
