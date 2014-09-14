package model;
import java.io.*;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import parser.NonogramParser;
import solver.NonogramSolver;
public class NonogramFactory {

	private String filename;

	public NonogramFactory(String filename){
		this.filename= filename;
	}

	public Nonogram[] read(){
		try {
			// read the json file
			FileReader reader = new FileReader(filename);
			JSONParser jsonParser = new JSONParser();
			JSONObject root = (JSONObject) jsonParser.parse(reader);
			JSONArray puzzles = (JSONArray)root.get("puzzles");
			Iterator it = puzzles.iterator();
			// take each value from the json array separately
			List<Nonogram> result = new ArrayList<Nonogram>();
			while (it.hasNext()) {
				JSONObject puzzle = (JSONObject) it.next();
				NonogramParser nParser = new NonogramParser(puzzle);
				result.add(nParser.parse());
				
			}
			return result.toArray(new Nonogram[0]);
			/*
			// handle a structure into the json object
			JSONObject structure = (JSONObject) jsonObject.get("job");
			System.out.println("Into job structure, name: " + structure.get("name"));
			*/
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ParseException ex) {
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		NonogramFactory factory = new NonogramFactory("puzzles.json");
		Nonogram[] nonograms = factory.read();
		NonogramSolver solver = new NonogramSolver(nonograms[12]);
		
		solver.createPlFile();
		solver.solve();
	}

}
