package core;

import java.io.*;
import java.util.Hashtable;

public class CheckCount {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	
	static int NumberOfMessages = 50;
	
	public static void main_output(String args) throws IOException {
		// TODO Auto-generated method stub
		Hashtable<Integer, Integer> Messages = new Hashtable<Integer, Integer>();
		
		String FileName = "./"+args; 
		String FileNameWrite = "./reports/"+args; 
		
		BufferedReader BR = new BufferedReader(new FileReader(FileName));
		BufferedWriter BW = new BufferedWriter(new FileWriter(FileNameWrite));
		
		String line;
		int tempcount;
		int linecount = 0;
		
		
		while((line = BR.readLine()) != null)
		{
			linecount++;
			if (linecount == 1) continue;
			
			String[] tmp = line.split("\t");
			
			if (tmp.length != 3) continue;
			
			String[] m = tmp[0].split("_");	
			int num = Integer.parseInt(m[0].substring(1));
			
			if ( Messages.containsKey(num) )
			{
				tempcount = Messages.get(num);
				tempcount++;
				Messages.put(num, tempcount);
			}
			
			else Messages.put(num, 1);
			
		}
		BR.close();
		
			
		for( int i = 1; i <= NumberOfMessages; i++ )   
		{ 
			if (Messages.containsKey(i))
			{
			//	String prt = "Message " + i + "\t Received: " + Messages.get(i) + "\t Percentage: " + Messages.get(i)/0.01 + "%";
				String prt = Integer.toString(Messages.get(i));
				System.out.println(prt);
				BW.write(prt);
				BW.newLine();
			}
			else
			{
			//	String prt = "Message " + i + "\t  Received: " + 0 + "\t Percentage: " + 0 + "%";
				String prt = Integer.toString(0);
				System.out.println(prt);
				BW.write(prt);
				BW.newLine();				
			}
		}
		
		BW.close();
	}

}
