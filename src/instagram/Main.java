package instagram;

import java.io.*;

import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {
	public static void main(final String[] args) throws Exception {

		Capabilities capability = new DesiredCapabilities();
		((DesiredCapabilities) capability).setJavascriptEnabled(true);
		((DesiredCapabilities) capability).setCapability("takesScreenshot", true);
		((DesiredCapabilities) capability).setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "phantomjs.exe");

		WebDriver driver = new PhantomJSDriver(capability);

		HashMap<String, User> userMap = new HashMap<String, User>();
		String imageLink;
		String userName;


		

		Scanner scanner = new Scanner(new File("instagram.txt"));

		ArrayList<String> keys = getKeysFromFile(scanner);

		for (String key : keys) {
			System.out.println(key);
			String connectURL = "https://www.instagram.com/explore/tags/" + key + "/";

			driver.get(connectURL);

			if (driver.findElements(By.linkText("Load more")).size() > 0) {
				WebElement element = driver.findElement(By.linkText("Load more"));
				element.click();

				WebDriverWait wait = new WebDriverWait(driver, 200000);

				wait.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver wdriver) {
						int firstCount = driver.getPageSource().length();
						try {

							JavascriptExecutor jse = (JavascriptExecutor) driver;
							jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
							Thread.sleep(1500);
						} catch (InterruptedException e) { // TODO

						}
						int secondCount = driver.getPageSource().length();
						return firstCount == secondCount;
					}
				});
				String pageContent = driver.getPageSource();

				Document doc = Jsoup.parse(pageContent);

				Elements images = doc.getElementsByTag("img");

				System.out.println("Number of images found: " + images.size());

			}

			String pageContent = driver.getPageSource();
			System.out.println(driver.findElement(By.tagName("img")).getSize());

			Document doc = Jsoup.parse(pageContent);

			Elements images = doc.getElementsByTag("img");
	
			System.out.println("Number of images found: " + images.size());
			int count = 0;
			ArrayList<Image> imageList = new ArrayList<Image>();
			int imageCount =0;
			for (Element imageObject : images) {

				imageCount++;

				Element node = imageObject.parent().parent().parent();

				String link = node.attr("href");

				if (link.length() == 0)
					continue;
				String captionString = imageObject.attr("alt");
			
				
				driver.get("https://www.instagram.com" + link);
				int counter =0;
				while (driver.findElements(By.xpath("//button[contains(.,'comments')]")).size() > 0) {
					driver.findElements(By.xpath("//button[contains(.,'comments')]")).get(0).click();
					System.out.println(" found link");
					if(counter==5)break;
					counter++;
				}
			
				Document imagePage = Jsoup.parse(driver.getPageSource());
				
				String shortenedLink = link;
				if(link.contains("?"))
					shortenedLink= link.substring(0, link.indexOf('?'));

				Element timeLink = imagePage.getElementsByAttributeStarting("datetime").first();
				
		
				String dateTimeValue = timeLink.attr("datetime");
			
				String likes = "0";
				if (driver.findElements(By.className("_tf9x3")).size() != 0) {
					WebElement likeElement = driver.findElement(By.className("_tf9x3"));

					if (likeElement != null) {
						likes = likeElement.getText().split(" ")[0];

					}
				}
				List<?> allImagesOfProfile = imagePage.getElementsByTag("img");
				Element profilePic = (Element) allImagesOfProfile.get(0);
				Element nodeForProfile = (Element) profilePic.parent().nextSibling();

				userName = nodeForProfile.children().get(0).text();

				imageLink = "https://www.instagram.com" + shortenedLink;

				Elements elements = imagePage.getElementsByClass("_nk46a");
				elements.remove(0);
				System.out.println("Image Count: "+imageCount+" and total comments found: " + elements.size());

				User user = null;
				if (userMap.containsKey(userName))
					user = userMap.get(userName);
				else {
					user = getUserFromName(userName, driver);
					if(user!=null)
					userMap.put(userName, user);
				}
				if(user!=null)
				imageList.add(
						new Image(captionString, userName, user.getNumberOfFollowers(), user.getNumberOfFollowing(),
								"status", dateTimeValue.replaceAll("T", " "), likes, imageLink, "", key));

				for (Element element : elements) {
					String commentUserName = element.child(0).text();

					User userOfComment = null;
					if (userMap.containsKey(commentUserName))
						userOfComment = userMap.get(commentUserName);
					else {
						userOfComment = getUserFromName(commentUserName, driver);
						if(userOfComment!=null)
						userMap.put(commentUserName, userOfComment);
					}

					String comment = element.child(1).text();
					if(userOfComment!=null)
					imageList.add(new Image(comment, commentUserName, userOfComment.getNumberOfFollowers(),
							userOfComment.getNumberOfFollowing(), "comment", dateTimeValue.replaceAll("T", " "), "0",
							"", imageLink, key));

				}

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(key);
			createFormat(workbook, sheet, key);
			for (Image image : imageList) {
				count++;
				writeData(workbook, sheet, image, count);
			}
		}

	}

	public static void writeData(XSSFWorkbook workbook, XSSFSheet sheet, Image image, int rowIndex) throws IOException {
		System.out.println(rowIndex);
		Row row = sheet.createRow(rowIndex);

		int columnCount = 0;

		row.createCell(++columnCount).setCellValue(image.getUsername());

		row.createCell(++columnCount).setCellValue(image.getNumberOfFollowers());
		row.createCell(++columnCount).setCellValue(image.getNumberOfFollowing());

		row.createCell(++columnCount).setCellValue(image.getText());

		row.createCell(++columnCount).setCellValue(image.getType());

		row.createCell(++columnCount).setCellValue(image.getDateTime());

		row.createCell(++columnCount).setCellValue(image.getFavourites());

		row.createCell(++columnCount).setCellValue(image.getSearchKey());

		row.createCell(++columnCount).setCellValue(image.getUrl());

		row.createCell(++columnCount).setCellValue(image.getReplyToURL());

		try (FileOutputStream outputStream = new FileOutputStream("Instagram" + image.getSearchKey() + ".xlsx")) {
			workbook.write(outputStream);

		}

	}

	public static void createFormat(XSSFWorkbook workbook, XSSFSheet sheet, String key) throws IOException {

		Row row = sheet.createRow(0);

		int columnCount = 0;

		row.createCell(++columnCount).setCellValue("Username");
		row.createCell(++columnCount).setCellValue("Number of followers");
		row.createCell(++columnCount).setCellValue("Number of people following");

		row.createCell(++columnCount).setCellValue("Text");

		row.createCell(++columnCount).setCellValue("Type");
		row.createCell(++columnCount).setCellValue("Date and time");

		row.createCell(++columnCount).setCellValue("Favourites");

		row.createCell(++columnCount).setCellValue("Key");

		row.createCell(++columnCount).setCellValue("URL");

		row.createCell(++columnCount).setCellValue("Source Status URL");

		try (FileOutputStream outputStream = new FileOutputStream("Instagram" + key + ".xlsx")) {
			workbook.write(outputStream);

		}

	}

	private static ArrayList<String> getKeysFromFile(Scanner scanner) {
		ArrayList<String> keys = new ArrayList<String>();
		while (scanner.hasNext()) {
			String key = scanner.next();
			keys.add(key.substring(1));
		}
		return keys;
	}

	private static User getUserFromName(String username, WebDriver driver) {
		if(username.contains(" comments"))return null;
		driver.get("https://www.instagram.com/" + username + "/");

		String profileContent = driver.getPageSource();

		Document profileDocument = Jsoup.parse(profileContent);
	//	System.out.println("https://www.instagram.com/" + username + "/");
		if (profileDocument.getElementsByClass("_bkw5z").size() < 3)
			return null;
		Element followersLink = profileDocument.getElementsByClass("_bkw5z").get(1);

		String numberOfFollowers = followersLink.attr("title");

		Element followingLink = profileDocument.getElementsByClass("_bkw5z").get(2);
		String numberOfFollowing = followingLink.text();

		return new User(username, numberOfFollowers, numberOfFollowing);
	}

}
