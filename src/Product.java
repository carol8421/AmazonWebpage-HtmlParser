import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Product {

	private  String title;
	private  String location;
	private  String productASINID;
	private  String ProductUrl;
	
	private  int reviewCount = 0;
	private  int productReviewCount;
	
	private  ArrayList<Review> reviewObjects = new ArrayList<Review>();
	
	private Document productPage;
	
	
	public Product(String filePath) throws ParseException, FileNotFoundException, UnsupportedEncodingException{
		
		setLocation(filePath);
		File product = new File(filePath);

		try {
			
			productPage = Jsoup.parse(product, "UTF-8");
			title = productPage.title();
			
			//get the number of reviews for this product
			String reviewCountLine = productPage.select("span[class=a-size-base]").text();
			String reviewCountAlpha = reviewCountLine.substring(0, reviewCountLine.indexOf("customer")).trim();
			reviewCountAlpha = reviewCountAlpha.replace(",", "");
			productReviewCount = Integer.parseInt(reviewCountAlpha);
			
			//get the product ID for this product, note some pages have a different ASIN then the folder names
			productASINID = filePath.split("\\\\")[3];
			
			//product url for this product
			ProductUrl = "http://www.amazon.com/dp/" + productASINID;

			//format the product filePath string to get the review folder filepath
			String reviewFilePath = filePath.replace(productASINID + "." + "html", "reviews\\");
			
			System.out.println("productID: " + productASINID);
			System.out.println("Reveiw Folder filePath:" + reviewFilePath);
			
			//create a writer for this specific product folder
			PrintWriter outputWriter;	
			outputWriter = new PrintWriter("Output\\" + productASINID +  "_Output.txt", "UTF-8");
			
			//write the product information to the specified file.
			outputWriter.println("Title: "+ title);
			outputWriter.println("Product ID: " +  productASINID);
			outputWriter.println("Product url: " +  ProductUrl);
			outputWriter.println("Review Count: "+ productReviewCount);
			outputWriter.println("---------------------------------------------------");
			
			//create review objects for every single review file for the product
			
			Files.walk(Paths.get(reviewFilePath)).forEach(fp -> {
			    if (Files.isRegularFile(fp)) {
			    	reviewObjects.add(new Review(fp.toString()));
			    }
			});
			
			outputWriter.println("Reviews: ");
			outputWriter.println("Review_ID          " + "Reviewer_ID  " + "   Rating " + 
								 "Date_Reviewed " + "Comment_Count " + " Comment      ");
					
			int i = 0;
			while (i <  reviewObjects.size()){
				int j = 0;
				while (j < 10){
					outputWriter.println(reviewObjects.get(i).getReviewID(j)   + "   " + 
										 reviewObjects.get(i).getReviewerID(j) + "      " +
										 reviewObjects.get(i).getRating(j)     + "      " +
										 reviewObjects.get(i).getDate(j)       + "            " +
										 reviewObjects.get(i).getCommentCount(j)+"    " +
										 reviewObjects.get(i).getreviewContent(j));
					j++;
				}
				i++;
			}
			
			outputWriter.close();
		} 
		
			catch (IOException e) {
			e.printStackTrace();
		}

	}



	private void setLocation(String location) {
		this.location = location;
	}

	public String getProductID() {
		return productASINID;
	}


	private void setProductID(String productID) {
		this.productASINID = productID;
	}


	public Document getProductPage() {
		return productPage;
	}


	public static void setProductPage(Document productPage) {
		productPage = productPage;
	}


	public String getTitle() {
		return title;
	}


	public static void setTitle(String title) {
		title = title;
	}
	

	
}
