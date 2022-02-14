import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import data.S1;
import data.S2;
import p1.P1;
import p2.P2;
import p3.P3;

public class application {
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		Scanner inputScanner = new Scanner(System.in);
		S1 s1 = new S1(3000);
		s1.generateExampleSets();
		S2 s2 = new S2();
		s2.generateExampleSetS2();
		
		do {
			String option;
			do {
				System.out.println("\n-> Select one from the following options by typing :\n"
						+ "\n(1) For MLP program"
						+ "\n(2) For k-means program"
						+ "\n(3) For LVQ program"
						+ "\n(x) To exit");
				System.out.print("\n-> ");
				String userInput = inputScanner.nextLine();
				option = userInput.trim();	
			}while(checkInput(option));
			if(option.equals("x")) {
				break;
			}
			//inputScanner.close();
			int numberOfProgram = Integer.parseInt(option);
			
			if(numberOfProgram == 1) {
				//inputScanner = new Scanner(System.in);
				String option2;
				do {
					System.out.println("\n-> Please select one from the following by typing:\n"
							+ "\n(1) For standard execution"
							+ "\n(2) For multiple execution training");
					System.out.print("\n-> ");
					String userInput = inputScanner.nextLine();
					option2 = userInput.trim();
				}while(!(option2.equals("1") || option2.equals("2")));
				int mode = Integer.parseInt(option2);
				P1 p1 = new P1();
				p1.executeModeOfMLP(mode);
				
			}else if (numberOfProgram == 2) {
				String option2;
				do {
					System.out.println("\n-> Please select one from the following by typing:\n"
							+ "\n(1) For standard execution"
							+ "\n(2) For multiple executions");
					System.out.print("\n-> ");
					String userInput = inputScanner.nextLine();
					option2 = userInput.trim();
				}while(!(option2.equals("1") || option2.equals("2")));
				int mode = Integer.parseInt(option2);
				P2 p2 = new P2();
				p2.executeModeOfKmeans(mode);
			}else if(numberOfProgram == 3) {
				String option2;
				do {
					System.out.println("\n-> Please select one from the following by typing:\n"
							+ "\n(1) For standard execution"
							+ "\n(2) For multiple execution training");
					System.out.print("\n-> ");
					String userInput = inputScanner.nextLine();
					option2 = userInput.trim();
				}while(!(option2.equals("1") || option2.equals("2")));
				int mode = Integer.parseInt(option2);
				P3 p3 = new P3();
				p3.executeModeOfLVQ(mode);
			}
			//System.out.println();
			//inputScanner.nextLine();
		}while(true);
		inputScanner.close();
		System.exit(0);
	}
	
	public static boolean checkInput(String option) {
		if(option.contentEquals("1") || option.contentEquals("2") || option.contentEquals("3") || option.contentEquals("x")) {
			return false;
		}
		return true;
		
	}

}
