package solver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import jpl.Atom;
import jpl.Compound;
import jpl.Query;
import jpl.Term;
import jpl.Variable;
import model.Nonogram;

public class NonogramSolver {
	private Nonogram nonogram;
	
	public NonogramSolver(Nonogram nonogram){
		this.nonogram=nonogram;
	}
	
	private String writeVars(){
		int m = nonogram.getRows().length;
		int n = nonogram.getCols().length;
		StringBuilder str = new StringBuilder();
		str.append("solve(Xvars):-\n");
	    String xvars = "Xvars=[";
        for(int i=0;i<m;i++){
        	for(int j=0;j<n;j++){
        		if(i==m-1 && j==n-1){
        			xvars+="Xi"+i+"j"+j;
        		}
        		else{
        			xvars+="Xi"+i+"j"+j+",";
        		}
        	}
        }
        str.append(xvars +"],");
        String hvars="Hvars=[";
        for(int i=0;i<m;i++){
        	for(int k=0;k<nonogram.getRows()[i].length;k++){
        		if(i==m-1 && k==nonogram.getRows()[i].length-1){
        			hvars+="Hi"+i+"k"+k;
        		}
        		else{
        			hvars+="Hi"+i+"k"+k+",";
        		}
        	}
        }
        str.append(hvars+"],");
        String vvars="Vvars=[";
        for(int j=0;j<n;j++){
        	for(int k=0;k<nonogram.getCols()[j].length;k++){
        		if(j==n-1 && k==nonogram.getCols()[j].length-1){
        			vvars+="Vj"+j+"k"+k;
        			
        		}
        		else{
        			vvars+="Vj"+j+"k"+k+",";
        		}
        	}
        }
        str.append(vvars+"],\n");
        return str.toString();
	}
	
	private String writeLineConstraints(){
		StringBuilder str = new StringBuilder();
		int m = nonogram.getRows().length;
		int n = nonogram.getCols().length;
		for(int i=0;i<m;i++){
        	for(int j=0;j<n;j++){
        		String xconst="Xi"+i+"j"+j+"#=1 #<==>";
        		for(int k=0;k<nonogram.getRows()[i].length;k++){
        			if(k!=nonogram.getRows()[i].length-1){
        				xconst+="Hi"+i+"k"+k+"#=<"+j+"#/\\"+j+"#< Hi"+i+"k"+k+"+" + nonogram.getRows()[i][k] +"#\\/";
        			}
        			else{
        				xconst+="Hi"+i+"k"+k+"#=<"+j+"#/\\"+j+"#< Hi"+i+"k"+k+"+" + nonogram.getRows()[i][k];
        			}
        		}
        		str.append(xconst+",\n");
        	}
        }
        
        for(int j=0;j<n;j++){
        	for(int i=0;i<m;i++){
        		String xconst="Xi"+i+"j"+j+"#=1 #<==>";
        		for(int k=0;k<nonogram.getCols()[j].length;k++){
        			if(k!=nonogram.getCols()[j].length-1){
        				xconst+="Vj"+j+"k"+k+"#=<"+i+"#/\\"+i+"#< Vj"+j+"k"+k+"+" + nonogram.getCols()[j][k] +"#\\/";
        			}
        			else{
        				xconst+="Vj"+j+"k"+k+"#=<"+i+"#/\\"+i+"#< Vj"+j+"k"+k+"+" + nonogram.getCols()[j][k];
        			}
        		}
        		str.append(xconst+",\n");
        	}
        }
        return str.toString();
	}
	
	private String writeDistanceConstraints(){
		StringBuilder str = new StringBuilder();
		int m = nonogram.getRows().length;
		int n = nonogram.getCols().length;
		for(int i=0;i<m;i++){
        	for(int k=0;k<nonogram.getRows()[i].length-1;k++){
        		str.append("Hi"+i+"k"+k+"+"+nonogram.getRows()[i][k]+"#<Hi"+i+"k"+(k+1)+",\n");
        	}
        }
        for(int i=0;i<n;i++){
        	for(int k=0;k<nonogram.getCols()[i].length-1;k++){
        		str.append("Vj"+i+"k"+k+"+"+nonogram.getCols()[i][k]+"#<Vj"+i+"k"+(k+1)+",\n");
        	}
        }
        return str.toString();
	}
	
	private String writeSumConstraint(){
		StringBuilder str = new StringBuilder();
		int m = nonogram.getRows().length;
        int n = nonogram.getCols().length;
		for(int i=0;i<m;i++){
        	int sum = 0;
        	for(int k=0;k<nonogram.getRows()[i].length;k++){
        		sum+=nonogram.getRows()[i][k];
        	}
        	String constr="sum([";
        	for(int j=0;j<n-1;j++){
        		constr+="Xi"+i+"j"+j+",";
        	}
        	constr+="Xi"+i+"j"+(n-1)+"],#=,"+sum+"),\n";
        	str.append(constr);
        }
        
        for(int j=0;j<n;j++){
        	int sum = 0;
        	for(int k=0;k<nonogram.getCols()[j].length;k++){
        		sum+=nonogram.getCols()[j][k];
        	}
        	String constr="sum([";
        	for(int i=0;i<m-1;i++){
        		constr+="Xi"+i+"j"+j+",";
        	}
        	constr+="Xi"+(m-1)+"j"+j+"],#=,"+sum+"),\n";
        	str.append(constr);
        }
        return str.toString();
	}
	
	public void createPlFile(){
		BufferedWriter writer = null;
        try {
            //create a temporary file
            int m = nonogram.getRows().length;
            int n = nonogram.getCols().length;
            File logFile = new File(nonogram.getName() +".pl");
            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(":-use_module(library(clpfd)).\n");
            writer.write(this.writeVars());
            //Domini
            writer.write("Xvars ins 0..1, Hvars ins 0.."+(n-1)+", Vvars ins 0.."+(m-1)+",\n");
            //Vincoli
            writer.write(this.writeLineConstraints());
            writer.write(this.writeDistanceConstraints());
            writer.write(this.writeSumConstraint());
            writer.write("append(Hvars,Vvars,Vars1),append(Vars1,Xvars,Vars),label(Vars),write(Xvars).");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
	}
	
	public void solve(){
		Query q1 = 
			    new Query( 
			        "consult", 
			        new Term[] {new Atom(nonogram.getName()+".pl")} 
			    );
		System.out.println( "consult " + (q1.query() ? "succeeded" : "failed"));
		
		Variable X = new Variable();
		Query q4 = new Query(new Compound("solve", new Term[] { new Variable("X")}));
		long startTime = System.currentTimeMillis();
		java.util.Hashtable solution=q4.oneSolution();
		long endTime = System.currentTimeMillis();
		System.out.println("Execution time: " + (endTime-startTime) + " milliseconds");
	
		Compound a = (Compound)solution.get("X");
		
		int m = nonogram.getRows().length;
        int n = nonogram.getCols().length;
        
		for(int i=0;i<m;i++){
			for(int j=0;j<n;j++){
				Term[] terms = a.args();
				if(terms[0].intValue()==1){
					System.out.print('#');
				}
				else{
					System.out.print('-');
				}
				a = (Compound)terms[1];
			}
			System.out.println();
		}
		
		
	}
}
