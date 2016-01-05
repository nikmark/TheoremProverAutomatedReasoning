package it.nikmark.datastructures;

public abstract class Token {

  /**
   * The symbol of the token.
   */
  private String symbol = null;
  /**
   * The weight of the token.
   */
  private int weight = 0;
  
  Token(String symbol, int weight){
	this.symbol = symbol;
	this.weight = weight;
  }
  
  /**
   * Get the symbol of the token.
   * 
   * @return token symbol
   */
  public String getSymbol(){
	return symbol;
  }
  /**
   * Get the weight of the token.
   * 
   * @return token weight
   */
  public int getWeight(){
	return weight;
  }
  /**
   * Set the weight of the token.
   * 
   * @param w the weight of the token
   */
  public void setWeight(int w){
	weight = w;
  }
  
  /**
   * @see Object#toString
   */
  public String toString(){
	return symbol;
  }
}
