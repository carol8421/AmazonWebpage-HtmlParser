import java.io.File;




import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HtmlParser {
	
private static final int NUMBER_OF_PRODUCTS = 5;

private static int ProductIndex = 0;

private static Path[] PathsArray;
private static ArrayList<Product> productObjects = new ArrayList<Product>();

/**
 * Parser to extract Product and Review related information from locally stored Amazon Web Pages
 * @param args
 * @author Vishnu Govindaraj
 * @throws ParseException 
 * 
 */
	public static void main(String[] args) throws ParseException {
	
		PathsArray = new Path[NUMBER_OF_PRODUCTS];
		
		//get filepaths for the different products only, excluding the review filePaths
		try {
			Files.walk(Paths.get("Assets/products/")).forEach(filePath -> {
			    if (Files.isRegularFile(filePath)) {
			    	if (!filePath.toString().contains("reviews")){
			    		PathsArray[ProductIndex] = filePath;
			    		System.out.println("productIndex: " + (ProductIndex + 1 )
			    				+ " " + PathsArray[ProductIndex]);
			    		ProductIndex++;
			    	}
			    }
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			
			int i = 0;
			while(i < NUMBER_OF_PRODUCTS) {
				//create the product objects
				productObjects.add(new Product(PathsArray[i].toString()));
				i++;
			}
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}
	
	
}
