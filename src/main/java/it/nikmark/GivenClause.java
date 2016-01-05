package it.nikmark;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

import it.nikmark.datastructures.Clause;
import it.nikmark.rules.ContractionRules;
import it.nikmark.rules.ExpansionRules;

public class GivenClause {

	public static boolean rPlan = false;

	static boolean sos = false;

	public static int queue = 1;

	protected static HashSet<Clause> sussunte = new HashSet<Clause>();

	protected static ArrayList<Clause> new_to_select = new ArrayList<Clause>();
	protected static ArrayList<Clause> new_selected = new ArrayList<Clause>();

	private static final ArrayList<Clause> tmp = new ArrayList<Clause>();
	private static final ArrayList<Clause> result = new ArrayList<Clause>();

	private static Collection<Clause> selected;
	private static Queue<Clause> to_select;

	public static int subsumption = 1;

	public enum ResearchPlan {
		OTTER, E
	}

	public static void parseArguments(String args[]) {
		for (String e : args) {
			parseArguments(e);
		}
	}

	public static boolean givenClauseAlgorithm(Queue<Clause> to_select,	ArrayList<Clause> selected) {

		if (rPlan) {
			return researchPlan(to_select, selected, ResearchPlan.OTTER);
		} else {
			return researchPlan(to_select, selected, ResearchPlan.E);
		}
	}

	private static void parseArguments(String arg) {

		if (arg.toLowerCase().equals("otter")) {
			rPlan = true;
		}
		if (arg.toLowerCase().equals("e")) {
			rPlan = false;
		}
		if (arg.toLowerCase().equals("fifo")) {
			queue = 0;
		}
		if (arg.toLowerCase().equals("sos")) {
			sos = true;
		}
		if (arg.toLowerCase().equals("min")) {
			queue = 1;
		}
		if (arg.toLowerCase().equals("max")) {
			queue = 2;
		}

		if (arg.toLowerCase().length() > 5) {
			StringTokenizer st = new StringTokenizer(arg.toLowerCase(), "=");
			String t = st.nextToken();
			if (t.equals("peak")) {
				int peakN = Integer.parseInt(st.nextToken());
				int peak = 1;
			}
			if(t.equals("subs")){
				String s = st.nextToken();
				if(s.toLowerCase().equals("st")){
					subsumption =1;
				}
				
				if(s.toLowerCase().equals("cl")){
					subsumption=0;
				}
			}
			if(t.equals("timeout")){
				String s = st.nextToken();
				int timeout = Integer.parseInt(s);
			}
		}

	}
	
	private static boolean researchPlan(Queue<Clause> to, ArrayList<Clause> sel, ResearchPlan rp) {
		to_select = to;
		selected = sel;
	
		int i = 0;
	
		while (!to_select.isEmpty()) {
				
			System.out.println("to sel start: "+to_select.toString());
			System.out.println("sel start: "+selected.toString());
			
			i++;
//			getGivenClause(peakN, i);
			
			result.clear();
			tmp.clear();

			Clause givenClause = to_select.poll();
			
			if (rp == ResearchPlan.E) {
				
                if ((givenClause = forwardContraction(givenClause, rp)) == null) {
                	
//                	if(empty){
//            			System.out.println("empty: ");
//                		return false;
//                	}
                    continue;
                }

                Set<Clause> betaprimo = backwardContraction(givenClause, rp);
                if(betaprimo == null){
                	return false;
                }
//                if (!betaprimo.isEmpty()) {
                    selected.addAll(betaprimo);
//                }
            }
			
			result.clear();
			result.addAll(ExpansionRules.factorization(givenClause));
			for(Clause c : selected){
				result.addAll(ExpansionRules.binaryResolution(givenClause, c));
			}
			
			if(checkEmptyClaus()){
				return false;
			}
			
          selected.add(givenClause);
			
			 if (!result.isEmpty()) {

	                Set<Clause> betaprimo = new LinkedHashSet<Clause>();
	                Set<Clause> copy = new LinkedHashSet<Clause>();
	                
	                copy.addAll(result);

	                for (Clause a : copy) {
	                	if (a.toString().equals("cnf()")) {
	                        return false; // insoddisfacibile
	                    }

	                    Clause as;
	                    if ((as = forwardContraction(a, rp)) == null) {

	                        result.remove(a);
	                        continue;
	                    	
	                    } else {
	                        if (a != as) {
	                            result.remove(a);
	                            result.add(as);
	                        }
	                    }

	                    if (rp == ResearchPlan.E) {
	                        Set<Clause> betaprimoAdd = backwardContraction(as, rp);
	                        
	                        if(betaprimo == null){
	                        	return false;
	                        }
	                        betaprimo.addAll(betaprimoAdd);
	                    }

	                }

	                to_select.addAll(result);
	                if (!betaprimo.isEmpty()) {
	                    to_select.addAll(betaprimo);
	                }

	            }

				System.out.println("to sel: "+to_select.toString());
				System.out.println("sel: "+selected.toString());
		}


		return true;
		
}

	private static Clause forwardContraction(Clause a, ResearchPlan rp) {
		if (ContractionRules.isTautology(a))
            return null;

		for (Clause c : selected) {
			if (ContractionRules.subsumption(c, a))
				return null;
		}
		
		if(rp == ResearchPlan.E){
			for (Clause c : to_select) {
				if (ContractionRules.subsumption(c, a))
					return null;
			}
		}
		
		for(Clause e : selected){
			Clause sempl = ContractionRules.clauseSimplification(a, e);
			if(sempl != null){
				return sempl;
			}else{
				if(sempl == null){
					return null;
				}
			}
		}

		if(rp == ResearchPlan.E){
			for(Clause e : to_select){
				Clause sempl = ContractionRules.clauseSimplification(a, e);
				if(sempl != null){
					return sempl;
				}else{
					if(sempl == null){
						boolean empty = true;
						return null;
					}
				}
			}
		}

        return a;
	}

	private static Set<Clause> backwardContraction(Clause as, ResearchPlan rp) {
		
		Set<Clause> set = new LinkedHashSet<Clause>();
		
		Iterator<Clause> it = selected.iterator();
		while(it.hasNext()){
			if(ContractionRules.subsumption(as, it.next()))
				it.remove();
		}

		if(rp == ResearchPlan.E){
			it = to_select.iterator();
			while(it.hasNext()){
				if(ContractionRules.subsumption(as, it.next()))
					it.remove();
			}
			
		}
		
		for(Clause e : selected){
			Clause sempl = ContractionRules.clauseSimplification(e, as);
			if(sempl != null){
				set.add(e);
//			}else{
//				if(sempl == null){
//
//					return null;
//				}
//
			}
		}
		
		if(rp == ResearchPlan.E){
			
			for(Clause e : to_select){
				System.out.println("e:"+e.toString());
				System.out.println("as:"+as.toString());

				Clause sempl = ContractionRules.clauseSimplification(e, as);
				if(sempl != null){
					set.add(e);
//				}else{
//					if(sempl.equals("cnf()")){
//						System.out.println("qui");
//
//						return null;
//					}
				}
			}
			
		}
		return set;
	}


//	public static boolean researchPlan(Queue<Clause> to, ArrayList<Clause> sel, ResearchPlan rp) {
//		to_select = to;
//		selected = sel;
//
//		int i = 0;
//
//		while (!to_select.isEmpty()) {
//
//			result.clear();
//			tmp.clear();
//			
//			i++;
////			getGivenClause(peakN, i);
//			givenClause = to_select.poll();
//
//			System.out.println("given: "+givenClause.toString());
//			
//			if (rp == ResearchPlan.OTTER) {
//
//				
//				if (!expansion()){
//					System.out.println("exp");
//					return false;
//				}
//				
//				if (!semplRes()){
//					System.out.println("sempl res");
//					return false;
//				}
//				
//				if (!semplOtterToSel()){
//					return false;
//				}
//				
//				if (!semplSelect()){
//					return false;
//
//				}
//
//				if (!semplOnlyOtter()){
//					return false;
//
//				}
//		
//				if (!checkEmptyClaus()){
//					return false;
//
//				}
//				sussCommonRes();
//
//				sussOtterToSel();
//
//				sussCommonSel();
//				
//				if (!semplSelToResOtter()){
//					return false;
//
//				}
//
//				sussOtterSelToSel();
//
//				to_select.addAll(result);
//
//			} else {
//				
//				if (!preExpansionE()){
//					return false;
//				}
//				
//				if (!expansion())
//					return false;
//				
////				if (!semplRes()){
////					System.out.println("sempl res");
////					return false;
////				}
//
//				if (!semplSelect())
//					return false;
//				
//				if (!checkEmptyClaus()){
//					return false;
//
//				}
//
//				sussCommonRes();
//
//				sussCommonSel();
//
//				to_select.addAll(result);
//
//			}
//
//		}
//		return true;
//
//	}
//
//	private static void sussOtterSelToSel() {
//		boolean subsume;
//		long inizio;
//		long fine;
//		subsume = false;
//		new_to_select = new ArrayList<Clause>();
//		inizio = System.currentTimeMillis();
//		while (!to_select.isEmpty() && !result.isEmpty()) {
//			subsume = false;
//			Clause c1 = to_select.poll();
//			for (Clause c2 : result) {
//				if (ContractionRules.subsumption(c2, c1)) {
//					subsume = true;
//					break;
//				}
//			}
//			if (!subsume)
//				new_to_select.add(c1);
//		}
//		fine = System.currentTimeMillis();
//		if (!result.isEmpty())
//			to_select.addAll(new_to_select);
//
//		Iterator<Clause> it_selected = selected.iterator();
//		subsume = false;
//		inizio = System.currentTimeMillis();
//		while (it_selected.hasNext()) {
//			Clause c1 = it_selected.next();
//			subsume = false;
//			for (Clause c2 : result) {
//				if (ContractionRules.subsumption(c2, c1)) {
//					subsume = true;
//					break;
//				}
//			}
//			if (subsume)
//				it_selected.remove();
//		}
//		fine = System.currentTimeMillis();
//	}
//
//	private static void sussCommonSel() {
//		boolean subsume;
//		long inizio;
//		long fine;
//
//		tmp.clear();
//
//		inizio = System.currentTimeMillis();
//		for (Clause c1 : result) {
//			subsume = false;
//			for (Clause c2 : selected) {
//				if (ContractionRules.subsumption(c2, c1)) {
//					subsume = true;
//					break;
//				}
//			}
//
//			if (!subsume)
//				tmp.add(c1);
//		}
//		fine = System.currentTimeMillis();
//		result.clear();
//		result.addAll(tmp);
//	}
//
//	private static void sussOtterToSel() {
//		boolean subsume;
//		long inizio;
//		long fine;
//		tmp.clear();
//		inizio = System.currentTimeMillis();
//		for (Clause c1 : result) {
//			subsume = false;
//			for (Clause c2 : to_select) {
//				if (ContractionRules.subsumption(c2, c1)) {
//					subsume = true;
//					break;
//				}
//			}
//			if (!subsume)
//				tmp.add(c1);
//		}
//		fine = System.currentTimeMillis();
//		result.clear();
//		result.addAll(tmp);
//	}
//
//	private static void sussCommonRes() {
//		boolean subsume;
//		long inizio;
//		long fine;
//		sussunte.clear();
//		tmp.clear();
//		inizio = System.currentTimeMillis();
//		for (Clause c1 : result) {
//			subsume = false;
//			for (Clause c2 : result) {
//				if (!c1.equals(c2) && !sussunte.contains(c1)
//						&& !sussunte.contains(c2)
//						&& ContractionRules.subsumption(c2, c1)) {
//					subsume = true;
//					sussunte.add(c1);
//					break;
//				}
//			}
//			if (!subsume)
//				tmp.add(c1);
//		}
//		fine = System.currentTimeMillis();
//		result.clear();
//		result.addAll(tmp);
//	}
//
//	private static boolean semplSelToResOtter() {
//		boolean semplificato;
//		long inizio;
//		long fine;
//		tmp.clear();
//		new_selected.clear();
//		inizio = System.currentTimeMillis();
//
//		for (Clause c1 : selected) {
//			semplificato = false;
//			for (Clause c2 : result) {
//				Clause sempl = ContractionRules.clauseSimplification(c1, c2);
//				if (sempl != null && sempl.getWeight() == 0) {
//					return false;
//				}
//				if (sempl != null) {
//					tmp.add(sempl);
//					semplificato = true;
//				}
//			}
//			if (!semplificato)
//				new_selected.add(c1);
//		}
//		fine = System.currentTimeMillis();
//		result.addAll(tmp);
//		selected.clear();
//		selected.addAll(new_selected);
//
//		return true;
//	}
//
//	private static boolean semplOnlyOtter() {
//		boolean semplificato;
//		long inizio;
//		long fine;
//		new_to_select = new ArrayList<Clause>();
//		inizio = System.currentTimeMillis();
//		while (!to_select.isEmpty()) {
//			semplificato = false;
//			Clause c1 = to_select.poll();
//			for (Clause c2 : result) {
//				Clause sempl = ContractionRules.clauseSimplification(c1, c2);
//				if (sempl != null && sempl.getWeight() == 0) {
//					return false;
//				}
//				if (sempl != null) {
//					new_to_select.add(sempl);
//					semplificato = true;
//				}
//			}
//			if (!semplificato)
//				new_to_select.add(c1);
//		}
//		fine = System.currentTimeMillis();
//		to_select.addAll(new_to_select);
//
//		return true;
//	}
//
//	private static boolean semplSelect() {
//		boolean semplificato;
//		long inizio;
//		long fine;
//		tmp.clear();
//		inizio = System.currentTimeMillis();
//		for (Clause c1 : result) {
//			semplificato = false;
//			for (Clause c2 : selected) {
//				Clause sempl = ContractionRules.clauseSimplification(c1, c2);
//				if (sempl != null && sempl.getWeight() == 0) {
//					return false;
//				}
//				if (sempl != null) {
//					tmp.add(sempl);
//					semplificato = true;
//				}
//			}
//			if (!semplificato)
//				tmp.add(c1);
//		}
//		fine = System.currentTimeMillis();
//		result.clear();
//		result.addAll(tmp);
//
//		return true;
//	}
//
//	private static boolean semplOtterToSel() {
//		boolean semplificato;
//		long inizio;
//		long fine;
//		tmp.clear();
//		inizio = System.currentTimeMillis();
//		for (Clause c1 : result) {
//			semplificato = false;
//			for (Clause c2 : to_select) {
//				Clause sempl = ContractionRules.clauseSimplification(c1, c2);
//				if (sempl != null && sempl.getWeight() == 0) {
//					return false;
//				}
//				if (sempl != null) {
//					tmp.add(sempl);
//					semplificato = true;
//				}
//			}
//			if (!semplificato)
//				tmp.add(c1);
//		}
//		fine = System.currentTimeMillis();
//		result.clear();
//		result.addAll(tmp);
//		return true;
//	}
//
//	private static boolean expansion() {
//		int statistics;
//		boolean semplificato;
//		long inizio;
//		long fine;
//		inizio = System.currentTimeMillis();
//		statistics = result.size();
//		result.addAll(ExpansionRules.factorization(givenClause));
//		fine = System.currentTimeMillis();
//
//		for (Clause c : selected) {
//			statistics = result.size();
//
//			inizio = System.currentTimeMillis();
//			result.addAll(ExpansionRules.binaryResolution(givenClause, c));
//			System.out.println("sel res bin : "+ selected.toString());
//
//			System.out.println("result res bin : "+ result.toString());
//
//			fine = System.currentTimeMillis();
//
//			for (Clause empy : result) {
//				if (empy.getWeight() == 0) {
//					return false;
//				}
//			}
//		}
//		selected.add(givenClause);
//		tmp.clear();
//		inizio = System.currentTimeMillis();
//		for (Clause c : result) {
//			if (!ContractionRules.isTautology(c))
//				tmp.add(c);
//		}
//		fine = System.currentTimeMillis();
//		result.clear();
//		result.addAll(tmp);
//
//		return true;
//	}
//
//	private static boolean semplRes() {
//		boolean semplificato;
//		long inizio;
//		long fine;
//		tmp.clear();
//		inizio = System.currentTimeMillis();
//		for (Clause c1 : result) {
//			semplificato = false;
//			for (Clause c2 : result) {
//				Clause sempl = ContractionRules.clauseSimplification(c1, c2);
//				if (sempl != null && sempl.getWeight() == 0) {
//					return false;
//				}
//				if (sempl != null) {
//					tmp.add(sempl);
//					semplificato = true;
//				}
//			}
//			if (!semplificato)
//				tmp.add(c1);
//		}
//		fine = System.currentTimeMillis();
//		result.clear();
//		result.addAll(tmp);
//		
//		return true;
//	}
//
//	private static boolean preExpansionE() {
//		long inizio;
//		long fine;
//		tmp.clear();
//		if (givenClause.getPredicates().size() == 1) {
//			inizio = System.currentTimeMillis();
//			Iterator<Clause> iter = selected.iterator();
//			while (iter.hasNext()) {
//				Clause c = iter.next();
////				System.out.println("c weiht=" + c.getWeight());
////				System.out.println("c =" + c.toString());
////
////				System.out.println("given weiht= "+givenClause.getWeight());
////				System.out.println("given = "+givenClause.toString());
//
//				Clause sempl = ContractionRules.clauseSimplification(c, givenClause);
////				System.out.println("sempl= "+sempl);
//
//				if (sempl != null && sempl.getWeight() == 0) {
//					
////						System.out.println("simpl preexp1");
//
//					
//					return false;
//				} else if (sempl != null) {
//					tmp.add(sempl);
//					iter.remove();
//				}
//			}
//			fine = System.currentTimeMillis();
//			result.addAll(tmp);
//		}
//		tmp.clear();
//		inizio = System.currentTimeMillis();
//		for (Clause c : selected) {
//			if (!ContractionRules.subsumption(givenClause, c)) {
//				tmp.add(c);
//			}
//		}
//		fine = System.currentTimeMillis();
//		selected.clear();
//		selected.addAll(tmp);
//
//		return true;
//	}
//	
	private static boolean checkEmptyClaus(){
		for(Clause e : result){
			if(e.getWeight() == 0 || e.toString().equals("cnf()")){
				return true;
			}
		}
		for(Clause e : selected){
			if(e.getWeight() == 0 && e.toString().equals("cnf()")){
				return true;
			}
		}
		for(Clause e : to_select){
			if(e.getWeight() == 0 && e.toString().equals("cnf()")){
				return true;
			}
		}
		
		return false;
	}

//	private static void getGivenClause(int peakGivenRatio, int i) {
//
//		if (peakGivenRatio > 0 && i % peakGivenRatio == 0 && peak == 1) {
//			givenClause = (Clause) Arrays.asList(to_select).get(to_select.size());
//			Arrays.asList(to_select).remove(to_select.size());
//
//		} else {
//			givenClause = to_select.poll();
//		}
//	}
}
