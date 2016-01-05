package it.nikmark.datastructures;

import java.util.HashMap;
import java.util.LinkedList;

public class Function extends Term{

  
  /**
   * The arguments of the function.
   */
  private LinkedList<Term> args = new LinkedList<Term>();
  
  
  public Function(String symbol, int weight,LinkedList<Term> args){
	super(symbol,weight);
	this.args = args;
  }
  
  /**
   * @see Object#toString
   */
  public String toString(){
	String tmp = this.getSymbol()+"(";
	if(args.size()>0){
	  for(int i=0;i<args.size()-1;i++){
	  tmp = tmp + args.get(i).toString() +",";
	  }
	  tmp = tmp + args.getLast().toString();
	}
	return tmp + ")";
  }
  
  
  public LinkedList<Term> getArgs(){
	return args;
  }
  
  /**
   * Return the number of arguments.
   * 
   * @return number of arguments
   */
  public int getArgsCount(){
	return args.size();
  }
  
  public Term replaceNew (HashMap<Variable,Term> sigma,HashMap<Variable,Term> alreadyUsed){
	LinkedList<Term> list = new LinkedList<Term>();
	int weight = 1;
	for (Term t:this.getArgs()){
	  Term newTerm = t.replaceNew(sigma, alreadyUsed);
	  list.add(newTerm);
	  weight += newTerm.getWeight();
	}
	return new Function(this.getSymbol(),weight,list);
  }
  
}
