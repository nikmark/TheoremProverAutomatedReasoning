package it.nikmark.datastructures;

import java.util.HashSet;
import java.util.Set;

public class Clause  {

  /**
   * The Predicate that compose the clause.
   */
  private Set<Predicate> predicates = new HashSet<Predicate>();

  /**
   * The weight of the clasue.
   */
  private int weight;

  public Clause(){
	this.predicates = new HashSet<Predicate>();
	this.weight = 0;
  }

  public Clause(Set<Predicate> pred, int weight) {
	this.predicates = pred;
	this.weight = weight;
  }

  public Set<Predicate> getPredicates() {
	return predicates;
  }
  
  public boolean isUnitary(){
	  return this.predicates.size() == 1;
  }

  
  public void addPredicates(Set<Predicate> preds,int weight){
	this.predicates.addAll(preds);
	this.weight += weight;
  }
  
  /**
   * Return the weight of the clause
   * 
   * @return the weight of the clause
   */
  public int getWeight() {
	return weight;
  }
  /**
   * @see Object#toString
   */
  public String toString() {
	String tmp = "cnf(";
	for (Predicate p : predicates) {
	  tmp = tmp + p.toString() + "|";
	}
	return tmp + ")";
  }
}
