package com.inse.service;

import com.inse.model.Bundle;
import com.inse.scheduler.GeneticAlgorithm;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileParserService {
	
	private static Map<Integer,ArrayList<Bundle>> bundlesForNurse = new HashMap<Integer, ArrayList<Bundle>>();

  //  private static final String FILE_NAME = "/E:/Book1.xls";


    public void parseFile(String csvFile) throws ParseException {
		/*
		String csvFile = "/Users/klajdi/IdeaProjects/Scheduling/resources/data-fp.csv";*/
		CSVReader reader = null;
		System.out.println("csvfile >>> "+csvFile);
		try {
			reader = new CSVReader(new FileReader(csvFile));
			reader.readNext();reader.readNext();
			parseCsvToNurseBundles(reader);
			printBundlesPerNurse();
			GeneticAlgorithm GA = new GeneticAlgorithm();
			GA.setBundlesForNurse(bundlesForNurse);
			GA.setDomain();
			GA.printDomain();
			int[] solution = {3, 5, 1, 7, 8, 9};
			GA.costf(solution);
			//GA.assignMapToArray(bundlesForNurse);
			//GA.printArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private  void parseCsvToNurseBundles(CSVReader reader) throws IOException, ParseException {
		String[] line;
		while ((line = reader.readNext()) != null) {
			int nurseNo = 0;            	
			for(int i=1; i < line.length; i=i+2){
				nurseNo++;
				String nurseVisit = line[i];          	
				// Empty value in CSV gives numberformat error
				if(!nurseVisit.equals("") || nurseVisit != ""  || !nurseVisit.isEmpty() || nurseVisit != null || nurseVisit.length() > 0 ){
					String nurseCost = line[i+1];
					Bundle b = initializeBundle(nurseCost, nurseVisit);                        
					assignBundlesToNurse( nurseNo, b);	
		    	}
				            		
			}                
		}
	}

	private  void printBundlesPerNurse() {
		for(int nurse : bundlesForNurse.keySet()){
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println("Nurse :"+nurse);
			for(Bundle b : bundlesForNurse.get(nurse)){
				System.out.println(b.getVisitSequence() + " -- "+ b.getCostOfVisit());
			}
		}
	}

	private  void assignBundlesToNurse(int nurseNo, Bundle b) {
		if(bundlesForNurse.containsKey(nurseNo)){
			bundlesForNurse.get(nurseNo).add(b);
		}else{
			ArrayList<Bundle> bundles = new ArrayList<Bundle>();
			bundles.add(b);
			bundlesForNurse.put(nurseNo, bundles);
		}
	}

    private  Bundle initializeBundle(String nurseCost, String nurseVisit) throws ParseException {
		NumberFormat nf = NumberFormat.getInstance();                			
		double value = nf.parse(nurseCost).doubleValue();
		Bundle b = new Bundle(nurseVisit, value);
		return b;
    }
    
    


}