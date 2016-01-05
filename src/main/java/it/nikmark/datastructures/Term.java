package it.nikmark.datastructures;

import java.util.HashMap;

public abstract class Term extends Token{

  Term(String symbol, int weight) {
	super(symbol, weight);
  }

  public Term replaceNew (HashMap<Variable,Term> sigma,HashMap<Variable,Term> alreadyUsed){
	return this;
  }
  
}
