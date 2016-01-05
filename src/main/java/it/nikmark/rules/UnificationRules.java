package it.nikmark.rules;

import java.util.HashMap;
import java.util.LinkedList;

import it.nikmark.datastructures.Function;
import it.nikmark.datastructures.Term;
import it.nikmark.datastructures.Variable;

class UnificationRules {

	@SuppressWarnings("unchecked")
	public static HashMap<Variable, Term> mostGeneralUnifier(LinkedList<Term> list1, LinkedList<Term> list2) {
		 return MGU(((LinkedList<Term>) list1.clone()), (LinkedList<Term>) list2.clone(), new HashMap<Variable, Term>());
		  }
	
	  private static HashMap<Variable, Term> MGU(LinkedList<Term> list1, LinkedList<Term> list2, HashMap<Variable, Term> sigma) {
		  if (sigma == null)
			  return null;
			else if (list1.size() > 1 && list2.size() > 1)
			  return MGU(list1, list2, unify(list1.poll(), list2.poll(), sigma));
			else if (list1.size() == 0 && list2.size() == 0)
			  return new HashMap<Variable, Term>();
			return unify(list1.getFirst(), list2.getFirst(), sigma);
	  }

	  private static HashMap<Variable, Term> unify(Term x, Term y, HashMap<Variable, Term> sigma) {
			if (x.equals(y))
			  return sigma;
			else if (x instanceof Variable)
			  return unify_var(x, y, sigma);
			else if (y instanceof Variable)
			  return unify_var(y, x, sigma);
			else if (x instanceof Function && y instanceof Function && x.getSymbol().equals(y.getSymbol()))
			  return MGU(((Function) x).getArgs(), ((Function) y).getArgs(), sigma);
			else
			  return null;
		  }
	  
	  private static HashMap<Variable, Term> unify_var(Term var, Term x, HashMap<Variable, Term> sigma) {
			if (sigma.containsKey(var))
			  return unify(sigma.get(var), x, sigma);
			else if (sigma.containsKey(x))
			  return unify(var, sigma.get(x), sigma);
			else if (occurCheck((Variable) var, x, sigma))
			  return null;
			else {
			  sigma.put((Variable) var, x);
			  return sigma;
			}
		  }
	  
	  private static boolean occurCheck(Variable x, Term t, HashMap<Variable, Term> sigma) {
		  
			if (t instanceof Variable && x.equals(t))
			  return true;
			if ( sigma.containsKey(t) )
			  return occurCheck(x,sigma.get(t),sigma);
			if (t instanceof Function ){
			  Function fun = (Function)t;
			  for(Term tm:fun.getArgs()){
				if(occurCheck(x,tm,sigma)) return true;
			  }
			  return false;
			}
			return false;
		  }

}
