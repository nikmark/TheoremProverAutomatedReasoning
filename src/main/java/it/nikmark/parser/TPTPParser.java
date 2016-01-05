/* Generated By:JavaCC: Do not edit this line. TPTPParser.java */
package it.nikmark.parser;

import it.nikmark.datastructures.*;
import java.util.*;

public class TPTPParser implements TPTPParserConstants {
  /**
   * The priority queue of clauses to select from.
   */
  private static Collection<Clause> to_select;

  /**
   * The collection of selected clauses.
   */
  private static Collection<Clause> selected;

  /**
   * The Map of predicate alreary read of which I don't want different arity.
   */
  private static HashMap<String,Predicate > tmpPred = new HashMap<String,Predicate >();

  /**
   * The Map of predicate alreary read of which I don't want different arity.
   */
  private static HashMap<String,Function > tmpFun = new HashMap<String,Function >();

  /**
   * The Map of constant alreary read of which I don't want repetition.
   */
  private static HashMap<String,Constant > tmpConst = new HashMap<String,Constant >();

  /**
   * The variables read in the current clause.
   */
  private static HashMap<String,Variable > clauseVariables = new HashMap<String,Variable >();

  /**
   * The literals that compone a clause.
   */
  private static Set<Predicate > literals = new HashSet<Predicate >();

  public static Integer maxVar;

  /**
   * The conjecture.
   */
  private static boolean conjecture = false;

  /**
   * Set of Support
   */
  private static boolean directiveSos = false;

  public TPTPParser(String input,Queue<Clause> to_select,Collection<Clause > selected, boolean sos)
  {
    tmpPred.clear();
    tmpFun.clear();
    tmpConst.clear();
    clauseVariables.clear();
    literals.clear();
    to_select.clear();
    selected.clear();
    this.to_select = to_select;
    this.selected = selected;

    directiveSos=sos;

    try
    {
      new TPTPParser(new java.io.StringReader(input)).TPTP();
    }catch(Exception e)
    {
      //e.printStackTrace();
      System.out.println("Oops: syntax error, check the formula!"+e.getMessage());
      System.exit(-1);
      //e.printStackTrace();
    }

    maxVar = clauseVariables.size() + tmpConst.size();


  }

  final public void TPTP() throws ParseException {
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 27:
          ;
          break;
        default:
          jj_la1[0] = jj_gen;
          break label_1;
      }
      CNF_A();
    }
    jj_consume_token(0);
  }

  final public void CNF_A() throws ParseException {
    Clause f;
    jj_consume_token(27);
    NAME();
    jj_consume_token(28);
    NAME();
    jj_consume_token(28);
    f = CNF();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 28:
        jj_consume_token(28);
        NAME();
        break;
      default:
        jj_la1[1] = jj_gen;
        ;
    }
    jj_consume_token(29);
    if( directiveSos )
    {
      if ( conjecture ) to_select.add(f);
      else selected.add(f);
      conjecture = false;
    } else to_select.add(f);
  }

  /* I don't need this for the correct behaviour, is just for completness. */
  final public void NAME() throws ParseException {
    Token con;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LOWER_WORD:
        con = jj_consume_token(LOWER_WORD);
        break;
      case SINGLE_QUOTED:
        con = jj_consume_token(SINGLE_QUOTED);
        break;
      case INTEGER:
        con = jj_consume_token(INTEGER);
        break;
      case UPPER_WORD:
        con = jj_consume_token(UPPER_WORD);
        break;
      case DECIMAL:
        con = jj_consume_token(DECIMAL);
        break;
      case SIGNED_INTEGER:
        con = jj_consume_token(SIGNED_INTEGER);
        break;
      default:
        jj_la1[2] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
    }
    if(con!=null)
    {
      String str = con.image;
      if( directiveSos && (str.equals("negated_conjecture") | str.equals("conjecture")))
      {
        conjecture = true;
        //System.out.println("negazione");
      }
    }
  }

  final public Clause CNF() throws ParseException {
    Predicate l=null;
    int clause_weight=0;
    clauseVariables = new HashMap<String,Variable >();
    literals = new HashSet<Predicate >();
    jj_consume_token(30);
    l = LITERAL();
    clause_weight += l.getWeight();literals.add(l);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case VLINE:
          ;
          break;
        default:
          jj_la1[3] = jj_gen;
          break label_2;
      }
      jj_consume_token(VLINE);
      l = LITERAL();
      clause_weight += l.getWeight();literals.add(l);
    }
    jj_consume_token(31);
    {if (true) return new Clause(literals,clause_weight);}
    throw new Error("Missing return statement in function");
  }

  final public Predicate LITERAL() throws ParseException {
    Token sign=null;
    Token p;
    LinkedList<Term> args=new LinkedList<Term >();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NOT:
        sign = jj_consume_token(NOT);
        break;
      default:
        jj_la1[4] = jj_gen;
        ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LOWER_WORD:
        p = jj_consume_token(LOWER_WORD);
        break;
      case SINGLE_QUOTED:
        p = jj_consume_token(SINGLE_QUOTED);
        break;
      default:
        jj_la1[5] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 30:
        jj_consume_token(30);
        args = ARGUMENTS();
        jj_consume_token(31);
        break;
      default:
        jj_la1[6] = jj_gen;
        ;
    }
    int weight=1;
    //it is a predicate with arguments, check if I've already read it with
    //a different number of arguments
    Predicate p2 = tmpPred.get(p.image);

    if( p2 != null && p2.getArgsCount() != args.size() )
    {
      {if (true) throw new ParseException("The predicate \u005c"" + p.image
              + "\u005c" has been already read with " + p2.getArgsCount() + " argument(s)");}
      }

      for(Term t:args) // retrive the weight of my arguments
      {
        weight += t.getWeight();
      }
      Predicate newPred = new Predicate(p.image,weight,args, (sign==null) ? false:true);
      tmpPred.put(p.image,newPred);
      {if (true) return newPred;}
      throw new Error("Missing return statement in function");
    }

    final public LinkedList<Term> ARGUMENTS() throws ParseException {
      LinkedList<Term> args = new LinkedList<Term>();
      Term t;
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LOWER_WORD:
        case UPPER_WORD:
        case SINGLE_QUOTED:
          t = TERM();
          args.add(t);
          break;
        default:
          jj_la1[7] = jj_gen;
          ;
      }
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case 28:
            ;
            break;
          default:
            jj_la1[8] = jj_gen;
            break label_3;
        }
        jj_consume_token(28);
        t = TERM();
        args.add(t);
      }
      {if (true) return args;}
      throw new Error("Missing return statement in function");
    }

  final public Term TERM() throws ParseException {
    Token t;
    LinkedList<Term > args = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case UPPER_WORD:
        t = jj_consume_token(UPPER_WORD);
        Variable v = clauseVariables.get(t.toString());
        if(v == null)
        {// if is the first time I read this vaariable in this clause, create a new one.
          v = new Variable(t.image,1);
          clauseVariables.put(t.toString(),v);
        }
      {if (true) return v;}
      break;
      case LOWER_WORD:
      case SINGLE_QUOTED:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case LOWER_WORD:
            t = jj_consume_token(LOWER_WORD);
            break;
          case SINGLE_QUOTED:
            t = jj_consume_token(SINGLE_QUOTED);
            break;
          default:
            jj_la1[9] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case 30:
            jj_consume_token(30);
            args = ARGUMENTS();
            jj_consume_token(31);
            break;
          default:
            jj_la1[10] = jj_gen;
            ;
        }
        if(args == null)
        { // contant
          Constant c = tmpConst.get(t.image);
          if(c == null)
          { //first time I read this constant
            c = new Constant(t.image,1);
            tmpConst.put(t.image,c);
          }
          {if (true) return c;}
        }else{
          // function
          Function f = tmpFun.get(t.image);
          if (f!=null && f.getArgsCount() != args.size() ){
            {if (true) throw new ParseException("The function \u005c"" + t.image
                    + "\u005c" has been already read with " + f.getArgsCount() + " argument(s)");}
            }
            int weight=1;
            for(Term t1:args) // retrive the weight of my arguments
            {
              weight += t1.getWeight();
            }
            f = new Function(t.image,weight,args);
            tmpFun.put(t.image,f);
            {if (true) return f;}

          }
          break;
          default:
            jj_la1[11] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
        throw new Error("Missing return statement in function");
    }

    /** Generated Token Manager. */
    public TPTPParserTokenManager token_source;
    SimpleCharStream jj_input_stream;
    /** Current token. */
    public Token token;
    /** Next token. */
    public Token jj_nt;
    private int jj_ntk;
    private int jj_gen;
    final private int[] jj_la1 = new int[12];
    static private int[] jj_la1_0;
    static {
      jj_la1_init_0();
    }
  private static void jj_la1_init_0() {
    jj_la1_0 = new int[] {0x8000000,0x10000000,0x700700,0x40,0x80,0x500,0x40000000,0x700,0x10000000,0x500,0x40000000,0x700,};
  }

  /** Constructor with InputStream. */
  public TPTPParser(java.io.InputStream stream) {
    this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public TPTPParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new TPTPParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 12; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
    ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 12; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public TPTPParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new TPTPParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 12; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 12; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public TPTPParser(TPTPParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 12; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(TPTPParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 12; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


  /** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  /** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[32];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 12; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 32; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}