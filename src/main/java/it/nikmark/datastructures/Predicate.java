package it.nikmark.datastructures;

import java.util.HashMap;
import java.util.LinkedList;

public class Predicate extends Token {

  /**
   * The arguments of the Predicate.
   */
  private LinkedList<Term> args = new LinkedList<Term>();
  
  /**
   * The sign of the Predicate, true is positive, false negative.
   */
  private boolean sign = true;
  
  
  public Predicate(String symbol, int weight,LinkedList<Term> args, boolean sign){
	super(symbol,weight);
	this.args = args;
	this.sign = sign;
  }
  
  
  /**
   * Set the list arguments of the Predicate.
   * 
   * @param arg the list argument
   */
  public void setArgs(LinkedList<Term> arg){
	args = arg;
  }
  
  
  /**
   * Return the list arguments of the Predicate.
   * 
   * @return Predicate arguments
   */
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
  
  /**
   * Set the sign of the Predicate.
   * 
   * @param s sign of the predicate
   */
  public void setSign(boolean s){
	sign = s;
  }
  
  /**
   * Return the sign of the Predicate.
   * 
   * @return the sign of the Predicate
   */
  public boolean getSign(){
	return sign;
  }
  
  
  public String toAtom(){
	String tmp = "";
	tmp = tmp +this.getSymbol()+"(";
	if(args.size()>0){
	  for(int i=0;i<args.size()-1;i++){
	  tmp = tmp + args.get(i).toString() +",";
	  }
	  tmp = tmp + args.getLast().toString();
	}
	return tmp + ")";
  }
  
  /**
   * @see Object#toString
   */
  public String toString(){
	String tmp = "";
	if (this.sign)
	 tmp = "~";
	tmp = tmp +this.getSymbol()+"(";
	if(args.size()>0){
	  for(int i=0;i<args.size()-1;i++){
	  tmp = tmp + args.get(i).toString() +",";
	  }
	  tmp = tmp + args.getLast().toString();
	}
	return tmp + ")";
  }
  
  public Predicate replaceNew (HashMap<Variable,Term> sigma,HashMap<Variable,Term> alreadyUsed){
	LinkedList<Term> list = new LinkedList<Term>();
	int weight = 1;
	for (Term t:this.getArgs()){
	  Term newTerm = t.replaceNew(sigma, alreadyUsed);
	  list.add(newTerm);
	  weight += newTerm.getWeight();
	}
	return new Predicate(this.getSymbol(),weight,list,this.getSign());
  }
}
