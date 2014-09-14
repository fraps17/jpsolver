package parser;
import model.Nonogram;

import org.json.simple.*;


public class NonogramParser {

	private JSONObject nonogram;

	
	public NonogramParser(JSONObject nonogram){
		this.nonogram = nonogram;
	}
	
	private int[][] parseArray(JSONArray array){
		int[][] rows = new int[array.size()][];
		for(int i=0;i<array.size();i++){
			JSONArray jRow = (JSONArray)array.get(i);

			rows[i]=new int[jRow.size()];
			for(int j=0;j<jRow.size();j++){
				rows[i][j]=(int)(long)jRow.get(j);
			}
		}
		return rows;
	}
	
	public Nonogram parse(){
		String name = (String)nonogram.get("name");
		JSONArray jRows = (JSONArray)nonogram.get("rows");
		int[][] rows = this.parseArray(jRows);		
		JSONArray jCols = (JSONArray)nonogram.get("cols");
		int[][] cols = this.parseArray(jCols);
		return new Nonogram(name,rows,cols);
	}
}
