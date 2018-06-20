import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Review {
	private static final int REVIEW_COUNT_PER_PAGE = 10;
	
	private  String[] reviewID;
	private  String[] reviewerID;
	private  int[] rating;
	private  LocalDate[] dateReviewed;
	private  int[] commentCount;
	private  String[] reviewContent;
	
	private  boolean[] purchaseVerified;
	private  String[] helpfulVote;
	
	private Document reviewDoc;
	private  int index = 0;

	
	
	public Review(String filePath) {
		//initialize amount of reviews to a certain size
		reviewID = new String[REVIEW_COUNT_PER_PAGE];
		reviewerID = new String[REVIEW_COUNT_PER_PAGE];
		rating = new int[REVIEW_COUNT_PER_PAGE];
		dateReviewed = new LocalDate[REVIEW_COUNT_PER_PAGE];
		commentCount = new int[REVIEW_COUNT_PER_PAGE];
		reviewContent = new String[REVIEW_COUNT_PER_PAGE];
		
		File reviewFile = new File(filePath);
		
		try {
			reviewDoc =  Jsoup.parse(reviewFile, "UTF-8");
			
			//select product reviews table for parsing
			Elements Reviews = reviewDoc.select("table#productReviews");

			//parse for review ID
			index = 0;
			Elements ReviewIDs = Reviews.select("a[name]").not("a[style]");
			for (Element r : ReviewIDs){
				if (!r.attr("name").contains("oc")){
					reviewID[index] = r.attr("name");
					index++;
				}	
			}	
			//parse for reviewer IDs
			index = 0;
			Elements ReviewerIDs = Reviews.select("div[style=float:left;] a[href]");
			for (Element e : ReviewerIDs){
				if (e.attr("href").contains("gp/pdp/profile/")){
					String ReviewerUrl = e.attr("href");
					reviewerID[index] = ReviewerUrl.substring(ReviewerUrl.indexOf("profile/") + 8, ReviewerUrl.indexOf("/ref"));
					index++;
				}
			}
			//parse for ratings
			index = 0;
			Elements Ratings = Reviews.select("div[style=margin-bottom:0.5em;] span span span");
			for (Element r : Ratings){
				rating[index] = Integer.parseInt(r.text().substring(0, 1).trim());
				index++;
			}
			
			//parse for review dates
			index=0;
			Elements reviewDates = Reviews.select("span[style=vertical-align:middle;] nobr");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
			for (Element r : reviewDates){
				LocalDate reviewDate = LocalDate.parse(r.text(), formatter);
				dateReviewed[index] = reviewDate;
				index++;
			}
			
			//parse for review content
			index=0;
			Elements reviewContentRaw = Reviews.select("div[class=reviewText]");
			for (Element r : reviewContentRaw) {
				reviewContent[index] = r.text();
				//get comment count, by splitting string by white space
				String[] count = reviewContent[index].split("\\s+");
				commentCount[index] = count.length;
				index++;			
			}
		}
		 catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String getReviewID(int i) {
		return reviewID[i];
	}
	public String getReviewerID(int i) {
		return reviewerID[i];
	}
	public int getRating(int i) {
		return rating[i];
	}
	public LocalDate getDate(int i) {
		return dateReviewed[i];
	}
	public int getCommentCount(int i) {
		return commentCount[i];
	}
	public String getreviewContent(int i) {
		return reviewContent[i];
	}
}
