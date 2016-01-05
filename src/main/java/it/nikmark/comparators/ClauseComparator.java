package it.nikmark.comparators;

import java.util.Comparator;

import it.nikmark.datastructures.Clause;
import it.nikmark.GivenClause;

public class ClauseComparator implements Comparator<Clause> {

  public int compare(Clause c1, Clause c2) {
	if (GivenClause.queue == 1)
	  return c1.getWeight() - c2.getWeight();
	else
	  return c2.getWeight() - c1.getWeight();
  }

}
