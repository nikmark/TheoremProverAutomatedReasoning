package it.nikmark.datastructures;

import java.util.HashMap;

public class Variable extends Term {

  public Variable(String symbol) {
	super(symbol, 1);
  }


  public Term replaceNew (HashMap<Variable,Term> sigma,HashMap<Variable,Term> alreadyUsed){

		if ( alreadyUsed.containsKey(this) ) {
			return alreadyUsed.get(this);
		}
		if ( sigma.containsKey(this) ){
		  Term newTerm = (sigma.get(this)).replaceNew(sigma, alreadyUsed);
		  alreadyUsed.put(this,newTerm);
		  return newTerm;
		}
		Variable newVar = new Variable(this.getSymbol());
		alreadyUsed.put(this, newVar);
		return newVar;
	  }
  
}
