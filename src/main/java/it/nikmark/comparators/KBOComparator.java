package it.nikmark.comparators;

import java.util.Comparator;
import java.util.HashSet;

import it.nikmark.datastructures.Constant;
import it.nikmark.datastructures.Function;
import it.nikmark.datastructures.Predicate;
import it.nikmark.datastructures.Term;
import it.nikmark.datastructures.Token;
import it.nikmark.datastructures.Variable;

public class KBOComparator implements Comparator<Token> {


	public KBOComparator(){

	}

	/*
	 * KBOOOOOOOO
	 * varCheck(F(f,ss), F(g,ts)) & [phi(F(f,ss)) > phi(F(g,ts)) v phi(F(f,ss))=phi(F(g,ts)) & f>g v phi(F(f,ss))=phi(F(g,ts)) & f=g & kbolex(ss, ts)]
	 */
	public int compare(Token arg0, Token arg1) {
//		System.out.println("1: "+arg0.toString());
//		System.out.println("2: "+arg1.toString());

		//		System.out.println("comp: "+i);

		return kbo(arg0, arg1);
	}
	
	private int kbo(Token arg0, Token arg1) {
	if(kbo_cond(arg0,arg1)){
		return 1;
	}else if(kbo_cond(arg1, arg0)){
		return -1;
	}
	return 0;
} 

	private int phi(Token p){
		int weight=0;

		if(p instanceof Predicate){
			int weightLit = 1;
			weight+= weightLit;
			for(Term e : ((Predicate) p).getArgs()){
				weight+=phi(e);
			}
		}

		if(p instanceof Function){
			int weightFun = 1;
			weight+= weightFun;
			if(((Function) p).getArgsCount() == 0){
				weight+=0;
				return weight;
			}
			for(Term e : ((Function) p).getArgs()){
				weight+=phi(e);
			}
		}

		if(p instanceof Variable){
			int weightVar = 1;
			weight+= weightVar;
		}

		if(p instanceof Constant){
			int weightConst = 2;
			weight+= weightConst;
		}

		return weight;

	}

	
	private boolean kbo_cond(Token p, Token e) {


		if (phi(p) > phi(e) && occur(p, e)) {
			return true;
		}
		return phi(p) == phi(e) && occur(p, e) && ((kbo2a(p, e) || kbo2b(p, e) || kbo2c(p, e)));

	}

	private boolean kbo2c(Token p, Token e) {
		
		if(p instanceof Predicate && e instanceof Predicate){
			if(p.getSymbol().equals(e.getSymbol()) ){
				
				for(int i=0; i< ((Predicate)p).getArgsCount(); i++){
					if(!(((Predicate)p).getArgs().get(i).toString().equals(((Predicate)e).getArgs().get(i)))){
						return kbo_cond(((Predicate)p).getArgs().get(i), ((Predicate)e).getArgs().get(i));
					}
				}
			}
		}
		
		if(p instanceof Function && e instanceof Function){
			if(p.getSymbol().equals(e.getSymbol()) ){
				
				for(int i=0; i< ((Function)p).getArgsCount(); i++){
					if(!(((Function)p).getArgs().get(i).toString().equals(((Function)e).getArgs().get(i)))){
						return kbo_cond(((Function)p).getArgs().get(i), ((Function)e).getArgs().get(i));
					}
				}
			}
		}
		
		
		
		return false;
		
	}

	
	private boolean kbo2a(Token p, Token e){
		
		
		if(p instanceof Function && e instanceof Variable && ((Function)p).getArgsCount() == 1) {
			Term t = (Term)p;
			do{
				t = ((Function) t).getArgs().get(0);
			}while(p.getSymbol().equals(t.getSymbol()));
			
	        return t.getSymbol().equals(e.getSymbol());			
		}
		return false;
	}
	

	/**
	 *  private boolean checkFnToXKBO(Function a, Variable b) {
        // so già che a ha arietà 1
        Term arg = a;
        do {
            arg = ((Function) arg).getArgs().get(0);
        } while (((Term) a).getSymbol().equals(arg.getSymbol()));
        
        return arg.equals(b);
            
    }

	 */


	private boolean kbo2b(Token p, Token e) {
		
		if(p instanceof Function && e instanceof Function){
			return p.getSymbol().compareTo(e.getSymbol()) < 0;
		}
		
		if(p instanceof Predicate && e instanceof Predicate){

			return p.getSymbol().compareTo(e.getSymbol()) < 0;
		}

		return false;
	}

	private boolean occur(Token p, Token e) {
		HashSet<Variable> var = new HashSet<Variable>();
		int occur = 0;
		int tmp = 0;
		
		if(p instanceof Predicate && e instanceof Predicate){
			
			for(Term a : ((Predicate) p).getArgs()){
				if(a instanceof Variable){
					var.add((Variable) a);
				}
			}
			for(Term a : ((Predicate) e).getArgs()){
				if(a instanceof Variable){
					var.add((Variable) a);
				}
			}
			
			for(Variable a : var){
				for(Term l : ((Predicate) p).getArgs()){
					tmp+= occurCheck(l, a);
				}
				for(Term l : ((Predicate) e).getArgs()){
					occur+= occurCheck(l, a);
				}
				
				if(!(occur >= tmp)){
					return false;
				}
				occur = tmp = 0;
			}
			
		}else{
			if(p instanceof Function){
		
			
				for(Term a : ((Function) p).getArgs()){
					if(a instanceof Variable){
						var.add((Variable) a);
					}
				}
			}
			if(p instanceof Variable){
				var.add((Variable)p);
			}
			if(e instanceof Function){
				
				
				for(Term a : ((Function) e).getArgs()){
					if(a instanceof Variable){
						var.add((Variable) a);
					}
				}
			}
			if(e instanceof Variable){
				var.add((Variable)e);
			}
			
			for(Variable a : var){
				if(p instanceof Function){
					for(Term l : ((Function) p).getArgs()){
						tmp+= occurCheck(l, a);
					}
				}
				if(p instanceof Variable){
						tmp+= occurCheck((Term)p, a);
				}
				if(e instanceof Function){
					for(Term l : ((Function) e).getArgs()){
						tmp+= occurCheck(l, a);
					}
				}
				if(e instanceof Variable){
						tmp+= occurCheck((Term)e, a);
				}
				if(!(occur >= tmp)){
					return false;
				}
				occur = tmp = 0;

			}
				
					
		}
		

		return false;
	}

	
	private int occurCheck(Term e, Variable a){
		
		int res = 0; 
		if(e instanceof Variable){
			if(e.getSymbol().equals(a.getSymbol())){
				return 1;
			}
		}
		
		if(e instanceof Function){
			for(Term m : ((Function) e).getArgs()){
				res += occurCheck(m, a);
			}
			return res;

		}
		return 0;
	}

}
