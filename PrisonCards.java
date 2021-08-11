import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;


public class PrisonCards {
/**
 * Zach Moeller
 * 
 * 8/8/21
 * 
 * There are 10 cards and 10 prisoners each numbered 1 to 10. Each prisoner can look at 5 cards. If Each prisoner finds the card that shares their number they all get to go free.
 * However, if even one prisoner doesn't find the card that shows their number they all die. MWUHAHAHAHAHAHAHAHA. 
 * 
 * 
 * The strategy this program uses, ups their chances from .01% to about 31% chance.
 * 
 * So lets say each card is just an integer 0 to 9(This will work for any number of cards, prisoners or checks), and each card is placed randomly in a drawer marked 1 to 10.
 * Each prisoner will start with the drawer that matches their number, and what ever the card value inside that drawer is the next drawer they will check.
 * This will make all the cards into a cycle and if that cycle is less or equal to 5 then that prisoner will find their number.
 * If all cycles that make up the cards are less than or equal to 5 then all prisoners will find their number and live.
 */
	static ArrayList<Integer> masterList = new ArrayList<Integer>();
	public static void main(String[] args) throws Exception {
		int lives = 0;
		int dies= 0;
		for (int run = 0; run < 1000; run++) {
			boolean check = false; // If the prisoner found their card.
			int size = 10; // Number of Cards
			int checks = 5; //Card Checks each prisoner gets
			ArrayList<Integer> cards = new ArrayList<Integer>();// The Cards
			ArrayList<Integer> cardsInCycle = new ArrayList<Integer>();
			//Populate the List with one of Each
			for(int i = 0; i < size; i++) {
				cards.add(i);
			}
		
			Collections.shuffle(cards);
			checkCycles(cards);
//			System.out.print(Arrays.toString(cards)); //Testing to see if the convertIntegers() method works
			
		/**
		 * In this for loop:
		 * i is the prisoners number
		 * x is the amount of checks they have had so far. 
		 * if they find their number you break from the inner loop and go on to the next prisoner
		 * if you get to last loop and no one has failed increment the lives variable
		 * and if anyone ever gets it wrong add to the dies variable and restart the loop
		 * after each set of checks if he lived reset the check to false for the next prisoner. 
		 */
			for(int i = 0; i < size; i++) {
				int temp = i;
			
				for(int x = 0; x < checks; x++) {
					if(cards.get(temp).intValue() == i) {
						check = true;
						break;
					}
					else {
						temp = cards.get(temp).intValue();
					}	
				}
				if(i==size-1 && check) {
					lives++;
				}
				if(!check) {
					dies++;
					break;
				}
				else {
					check = false;
				}
			}
			
			
		}
		System.out.println("Live: " + lives + "\t Die: " + dies);
		writeToExcel(masterList);
	}
	
	/**
	 * 
	 * @param list. The cards in the drawers
	 * This method will check how many cycles there are in any given set, it will also record how long each cycle is. 
	 */
	public static void checkCycles(ArrayList<Integer> list) {
		int currentSize = 0; //Counter for the size of the Cycle
		ArrayList<Integer> cycleSizes = new ArrayList<Integer>(); // Stores each cycle size
		ArrayList<Integer> badNumbers = new ArrayList<Integer>(); // Numbers that have already been used in a cycle.
		
		
		for(int i =0; i < list.size(); i++) {
			int temp = i;
			
			if(!badNumbers.contains(i)) { //Check if the number has been used before so we dont do the same cycle twice
				do {
					badNumbers.add(temp);
					temp = list.get(temp).intValue();
					currentSize++;
				}while(temp != i);
			cycleSizes.add(currentSize);
			currentSize = 0;
			}
		}
//		System.out.println("There were " + cycleSizes.size() + " Cycle(s) for this set, with size(s) of: ");
		for(int z = 0; z < cycleSizes.size(); z++) {
//			System.out.print(cycleSizes.get(z) + " ");
		}
//		System.out.println("\n");
		masterList.add(cycleSizes.size());
	}
	/**
	 * 
	 * @param list an Array list of Integers
	 * This will output the list of Cycles to an Excel sheet for analysis
	 */
	private static void writeToExcel(ArrayList<Integer> list) throws Exception{
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Data");
		
		int cellid = 0;
		int rowid = 0;
		XSSFRow row = sheet.createRow(rowid);
		for(Integer i : list) {
			Cell cell = row.createCell(cellid++);
			if(rowid == 1048576) {
				rowid=0;
				cellid++;
			}
			cell.setCellValue(i);
		}
		FileOutputStream out = new FileOutputStream("C:/Users/MOELLER/Desktop/Data.xlsx"); //C:/Users/MOELLER/Desktop/data.xlsx C:\\Users\\MOELLER\\Desktop\\data.xlsx
		workbook.write(out);
		out.close();
		workbook.close();
		System.out.println("Wrote to workbook succesfully");
	}
	
}

