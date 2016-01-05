package it.nikmark.rules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import it.nikmark.comparators.KBOComparator;
import it.nikmark.datastructures.Clause;
import it.nikmark.datastructures.Predicate;
import it.nikmark.datastructures.Term;
import it.nikmark.datastructures.Variable;

public class ExpansionRules {
	

	private static int weight;

	private static LinkedList<Predicate> getMaximal(Clause c){
		
		LinkedList<Predicate> max = new LinkedList<Predicate>();
		
		if(c.getPredicates().size()==1){
			max.addAll(c.getPredicates());
			return max;
		}
		
		KBOComparator kbo = new KBOComparator();
		boolean m = true;
		for(Predicate e : c.getPredicates()){
			for(Predicate t : c.getPredicates()){
				if(!e.equals(t)){
					if(kbo.compare(e, t) < 1){
						m = false;
						break;
					}
				}
			}
			if(m){
				max.add(e);
			}
			m=true;
		}
		
		if(max.isEmpty()){
			max.addAll(c.getPredicates());
		}

		return max;
	}
	
//	public static LinkedList<Clause> binaryResolution(Clause C, Clause D) {
//		LinkedList<Clause> list = new LinkedList<Clause>();
//		HashMap<Variable, Term> sigma;
//		HashMap<Variable, Term> alreadyUsed = new HashMap<Variable, Term>();
//		for (Predicate P : C.getPredicates()) {
//		  for (Predicate Q : D.getPredicates()) {
//			if (P.getSign() == !Q.getSign() && P.getSymbol().equals(Q.getSymbol())
//				&& (sigma = UnificationRules.mostGeneralUnifier(P.getArgs(), Q.getArgs())) != null) {
////			   System.out.print("MGU:"+sigma); //DEGUN
////			   System.out.println("\tC:"+C); //DEGUN
//			  Set<Predicate> preds = new HashSet<Predicate>();
//			  int weight = 0;
//			  alreadyUsed.clear();
//			  for (Predicate PP : C.getPredicates()) {
//				if (!PP.equals(P)) {
//				  if (PP.getArgs().size() != 0) {
//					Predicate newPred = PP.replaceNew(sigma, alreadyUsed);
//					weight += newPred.getWeight();
//					preds.add(newPred);
//				  } else {
//					weight += PP.getWeight();
//					preds.add(PP);
//				  }
//				}
//			  }
////			   alreadyUsed.clear();
//			  for (Predicate PP : D.getPredicates()) {
//				if (!PP.equals(Q)) {
//				  if (PP.getArgs().size() != 0) {
//					Predicate newPred = PP.replaceNew(sigma, alreadyUsed);
//					weight += newPred.getWeight();
//					preds.add(newPred);
//				  } else {
//					weight += PP.getWeight();
//					preds.add(PP);
//				  }
//				}
//			  }
//			  list.add(new Clause(preds, weight));
//			  sigma.clear();
//			}
//		  }
//		}
//		return list;
//	  }

	  public static LinkedList<Clause> binaryResolution(Clause c1, Clause c2){
		
		LinkedList<Predicate> max1 = getMaximal(c1);
		LinkedList<Predicate> max2 = getMaximal(c2);
		
//		System.out.println("max res 1= "+max1.toString());
//		System.out.println("max res 2= "+max2.toString());


		LinkedList<Clause> list = new LinkedList<Clause>();
		
		HashMap<Variable, Term> sigma;
		HashMap<Variable, Term> alreadyUsed = new HashMap<Variable, Term>();
		Set<Predicate> preds = new HashSet<Predicate>();
		
		weight = 0;
		
		for(Predicate e : max1){
			for(Predicate t : max2){
				if(e.getSign() == !t.getSign() && e.getSymbol().equals(t.getSymbol()) 
						&& (sigma = UnificationRules.mostGeneralUnifier(e.getArgs(), t.getArgs())) != null){

					alreadyUsed.clear();

					weight=0;
					preds.addAll(ContractionRules.replacing(c1, e, sigma, alreadyUsed));
					preds.addAll(ContractionRules.replacing(c2, t, sigma, alreadyUsed));
					
					list.add(new Clause(preds, weight));
					
					
				}

			}

		}
		return list;
		  
	  }
//
//	  private static Set<Predicate> replacing(Clause c, Predicate e, HashMap<Variable, Term> sigma, HashMap<Variable, Term> alreadyUsed){
//
//		  Set<Predicate> preds = new HashSet<Predicate>();
//
//		  for (Predicate p : c.getPredicates()) {
//			if (!p.equals(e)) {
//			  if (p.getArgs().size() != 0) {
//				Predicate newPred = p.replaceNew(sigma, alreadyUsed);
//				weight += newPred.getWeight();
//				preds.add(newPred);
//			  } else {
//				weight += p.getWeight();
//				preds.add(p);
//			  }
//			}
//		  }
//		  return preds;
//	  }

	  public static LinkedList<Clause> factorization(Clause c){
		  
		  	LinkedList<Clause> res = new LinkedList<Clause>();
			LinkedList<Predicate> max = getMaximal(c);
			
//			System.out.println("max facto= "+max.toString());
			
			HashMap<Variable, Term> sigma = new HashMap<Variable, Term>();
			HashMap<Variable, Term> alreadyUsed = new HashMap<Variable, Term>();
			
			Set<Predicate> preds = new HashSet<Predicate>();
			weight = 0;
			
			for(Predicate e: max){
				for(Predicate t: max){
					// && max.indexOf(e)< max.indexOf(t)
					if(!e.equals(t) && e.getSign() == t.getSign() && e.getSymbol().equals(t.getSymbol())){
						if((sigma = UnificationRules.mostGeneralUnifier(e.getArgs(), t.getArgs())) != null){
					
						alreadyUsed.clear();
						preds.addAll(ContractionRules.replacing(c, e, sigma, alreadyUsed));
						
						res.add(new Clause(preds, weight));
						sigma.clear();
					}
					
					}
				}
				
			}
						
			return res;
		  
	  }
	  
//	  public static LinkedList<Clause> factorization(Clause C) {
//			LinkedList<Clause> list = new LinkedList<Clause>();
//
//			Predicate[] ciao = new Predicate[C.getPredicates().size()];
//			Predicate[] p1 = (Predicate[]) C.getPredicates().toArray(ciao);
//
//			for (int i = 0; i < p1.length; i++) {
//			  for (int j = i + 1; j < p1.length; j++) {
//				HashMap<Variable, Term> sigma;
//				if (!p1[i].equals(p1[j])
//					&& p1[i].getSymbol().equals(p1[j].getSymbol())
//					&& p1[i].getSign() == p1[j].getSign()
//					&& (sigma = UnificationRules.mostGeneralUnifier(p1[i].getArgs(), p1[j].getArgs())) != null) {
//				  Set<Predicate> preds = new HashSet<Predicate>();
//				  int weight = 0;
//				  HashMap<Variable, Term> alreadyUsed = new HashMap<Variable, Term>();
//				  for (Predicate PP : C.getPredicates()) {
//					if (!PP.equals(p1[i])) {
//					  // HashMap<Var, Term> alreadyUsed = new HashMap<Var, Term>();
//					  Predicate newPred = PP.replaceNew(sigma, alreadyUsed);
//					  weight += PP.getWeight();
//					  preds.add(newPred);
//					}
//				  }
//				  list.add(new Clause(preds, weight));
//				}
//			  }
//			}
//			return list;
//		  }
	  
}
