package it.nikmark.rules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import it.nikmark.GivenClause;
import it.nikmark.datastructures.Clause;
import it.nikmark.datastructures.Constant;
import it.nikmark.datastructures.Function;
import it.nikmark.datastructures.Predicate;
import it.nikmark.datastructures.Term;
import it.nikmark.datastructures.Variable;

public class ContractionRules {

	static Predicate[] pc1; 
	static Predicate[] pc2;

	private static Integer weight;
	private static HashMap<Variable, Term> sigmaSub;

	/**
	 * Stillman algorithm
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	/** */
	
	  public static boolean subsumption(Clause c1, Clause c2){
		   if(GivenClause.subsumption == 0){
				return subsumptionCL(c1, c2);

		   }
		   return subsumptionStillman(c1, c2);

          
  }
	  private static boolean subsumptionStillman(Clause c1, Clause c2){
		  
		  sigmaSub = new HashMap<Variable, Term>();
		  sigmaSub.clear();
          
          return stillmanFunction(c1, c2, 0, 0);
          
  }

	  private static boolean stillmanFunction(Clause c1, Clause c2, int i, int j){
          int a = 0;
          Predicate[] pc1 = c1.getPredicates().toArray(new Predicate[c1.getPredicates().size()]);
          Predicate[] pc2 = c2.getPredicates().toArray(new Predicate[c2.getPredicates().size()]);

          if(j >= pc1.length){
                  return false;
          }else{
                  a=j;

                  do{
                          a++; 
                  }while((( a < pc2.length && (!(isUnificable(pc1[i], pc2[a]) != null)))));
                  
                  if(a >= pc2.length){
                          return false;
                  }else {
					  sigmaSub = isUnificable(pc1[i], pc2[a]);

					  return i == pc1.length - 1 || stillmanFunction(c1, c2, i + 1, 0) || stillmanFunction(c1, c2, i, a + 1);
				  }
          }

  }
  
  private static HashMap<Variable, Term> isUnificable(Predicate pc1, Predicate pc2) {
          if(pc1.getSymbol().equals(pc2.getSymbol()) && (pc1.getSign() == pc2.getSign())){
//                  return sigmaSub = UnificationRules.mostGeneralUnifier(pc1.getArgs(), pc2.getArgs());
        	  
        	  try {
				sigmaSub.putAll(UnificationRules.mostGeneralUnifier(pc1.getArgs(), pc2.getArgs()));
			} catch (Exception ignored) {
			}
        	  return sigmaSub;
          }
          return null;
  }
	/** */

	  
	  public static Clause clauseSimplification(Clause c1, Clause c2) {
		  
			HashMap<Variable, Term> sigma1 = new HashMap<Variable, Term>();
			Set<Predicate> tm;

			if ((c2.getPredicates().size() != 1 || c1.equals(c2)) && c1.getPredicates().size() < 2){
				return null;
			}
			tm = c1.getPredicates();
			int weight = 0;
			for (Predicate L : c2.getPredicates()) {
			  for (Predicate P : c1.getPredicates()) {
				  
				  if(P.getSymbol().equals(L.getSymbol()) && P.getSign() == L.getSign() && (sigma1 = UnificationRules.mostGeneralUnifier(L.getArgs(), P.getArgs())) != null){
					  tm.remove(P);

					  return new Clause(tm, c1.getWeight()-P.getWeight());
				  }
				  
			  }
			}

			return null;
		  }

	  public static boolean isTautology(Clause c) {
		for (Predicate P : c.getPredicates()) {
		  for (Predicate PP : c.getPredicates()) {
			if (P.toAtom().equals(PP.toAtom()) && P.getSign() != PP.getSign()) {
			  return true;
			}
		  }
		}
		return false;
	  }
	  
	  public static Set<Predicate> replacing(Clause c, Predicate e, HashMap<Variable, Term> sigma, HashMap<Variable, Term> alreadyUsed){
		  
		  Set<Predicate> preds = new HashSet<Predicate>();

		  for (Predicate p : c.getPredicates()) {
			if (!p.equals(e)) {
			  if (p.getArgs().size() != 0) {
				Predicate newPred = p.replaceNew(sigma, alreadyUsed);
				weight += newPred.getWeight();
				preds.add(newPred);
			  } else {
				weight += p.getWeight();
				preds.add(p);
			  }
			}
		  }
		  return preds;
	  }
	  
	  /* -------------------------------------------------------------------------- */
	  
	  private static boolean subsumptionCL(Clause C, Clause D) {
			if (C.getPredicates().size() > D.getPredicates().size())
			  return false;
			HashMap<Variable, Term> teta = findGroundSubstitution(D);
			LinkedList<Clause> W = new LinkedList<Clause>();
			LinkedList<Clause> U = new LinkedList<Clause>();
			boolean subsume = false;
			for (Predicate L : D.getPredicates()) {
			  Predicate newPred = L.replaceNew(teta, new HashMap<Variable, Term>());
			  newPred.setSign(!newPred.getSign());
			  HashSet<Predicate> preds = new HashSet<Predicate>();
			  preds.add(newPred);
			  W.add(new Clause(preds, newPred.getWeight()));
			}
			U.add(C);

			while (!subsume) {
			  LinkedList<Clause> tmpU = new LinkedList<Clause>();
			  for (Clause C1 : W) {
				for (Clause C2 : U) {
				  tmpU.addAll(ExpansionRules.binaryResolution(C1, C2));
				}
			  }
			  U = tmpU;
			  if (U.isEmpty())
				return false;
			  for (Clause u : U) {
				if (u.getWeight() == 0)
				  subsume = true;
			  }
			}
			return subsume;
		  }
	  
	  private static HashMap<Variable,Term> findGroundSubstitution(Clause C){
			return findGroundSubstitution(C,new HashMap<Variable,Term>());
		  }
		  /**
		   * Support method which find a ground substitution for a clause.
		   * 
		   * @param C - the clause for which to find the ground substitution
		   * @param teta - the substitution found at the current iteration
		   * @return the ground substitution
		   */
		  private static HashMap<Variable,Term> findGroundSubstitution(Clause C,HashMap<Variable,Term> teta){
			for (Predicate P:C.getPredicates()){
			  for (Term t:P.getArgs()){
				findGroundSubstitutionTerm(t,teta);
			  }
			}
			return teta;
		  }
		  
		  /**
		   * Support method which find a ground substitution fot a term.
		   * 
		   * @param T - the term for which to find the ground substitution
		   * @param teta - the substitution generate at the current iteration
		   * @return the ground substitution
		   */
		  private static HashMap<Variable,Term> findGroundSubstitutionTerm(Term T,HashMap<Variable,Term> teta){
			if ( T instanceof Variable )
			  if ( !teta.containsKey(T) ){
				Term c = new Constant("@");
				teta.put((Variable)T,c);
			  }
			if ( T instanceof Function ){
			  for(Term A:((Function) T).getArgs()){
				findGroundSubstitutionTerm(A,teta);
			  }
			}
			return teta;
		  }

}
