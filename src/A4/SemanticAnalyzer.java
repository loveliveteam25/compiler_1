/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package A4;


/**
 *
 * @author javiergs
 */
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

/**
 *
 * @author FanjieLin
 */


public class SemanticAnalyzer {
	 
	//private static Gui gui;
	//private static Vector<Token> tokens;
  private static final Hashtable<String, Vector<SymbolTableItem>> symbolTable = new Hashtable<String, Vector<SymbolTableItem>>();
  private static final Stack stack = new Stack();
  
  // create here a data structure for the cube of types
  private static final int INT=0;
  private static final int FLOAT=1;
  private static final int CHAR=2;
  private static final int STRING=3;
  private static final int BOOLEAN=4;
  private static final int VOID=5;
  private static final int ERROR=6;
  private static final int OK=10;
  
  private static final int MINDIVMUL=0;
  private static final int PLUS=1;
  private static final int UNMINUS=2;
  private static final int LTGT=3;
  private static final int NEEE=4;
  private static final int ANDOR=5;
  private static final int NOT=6;
  private static final int EQUALS=7;
  
 static int a[][][]={
		  		{{INT,FLOAT,ERROR,ERROR,ERROR,ERROR,ERROR},    //a[0][0][]
		  		 {FLOAT,FLOAT,ERROR,ERROR,ERROR,ERROR,ERROR},
		  		 {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
		  		 {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
		  		 {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
		  		 {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
		  		 {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}}, //FOR {-,*,/}
		  		
		  		 {{INT,  FLOAT,ERROR,STRING,ERROR,ERROR,ERROR},//a[1][0][]
		  		  {FLOAT,FLOAT,ERROR,STRING,ERROR,ERROR,ERROR},
			  	  {ERROR,ERROR,ERROR,STRING,ERROR,ERROR,ERROR},
			      {STRING,STRING,STRING,STRING,STRING,ERROR,ERROR},
			  	  {ERROR,ERROR,ERROR,STRING,ERROR,ERROR,ERROR},
			  	  {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
			  	  {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}},//FOR +
		  		 
		  		 {{INT,FLOAT,ERROR,ERROR,ERROR,ERROR,ERROR}}, //FOR UNARY -  a[2][0][]
		  		 
		  		 {{BOOLEAN,BOOLEAN,ERROR,ERROR,ERROR,ERROR,ERROR}, //a[3][0][]
		  		  {BOOLEAN,BOOLEAN,ERROR,ERROR,ERROR,ERROR,ERROR},
		  		  {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
		  		  {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
		  		  {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
		  		  {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
		  		  {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}}, //FOR <,>
		  		  
		  		 {{BOOLEAN,BOOLEAN,ERROR,ERROR,ERROR,ERROR,ERROR}, //a[4][0][]
			  	  {BOOLEAN,BOOLEAN,ERROR,ERROR,ERROR,ERROR,ERROR}, 
			  	  {ERROR,ERROR,BOOLEAN,ERROR,ERROR,ERROR,ERROR},
		  		  {ERROR,ERROR,ERROR,BOOLEAN,ERROR,ERROR,ERROR},
		  		  {ERROR,ERROR,ERROR,ERROR,BOOLEAN,ERROR,ERROR},
		  		  {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
		  		  {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}}, //FOR !=,==
		  		  
		  		 {{ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},   //a[5][0][]
			  	  {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
			  	  {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
			  	  {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
			  	  {ERROR,ERROR,ERROR,ERROR,BOOLEAN,ERROR,ERROR},
			  	  {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
			  	  {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}},  //FOR &, |
		  		
			  	{{ERROR,ERROR,ERROR,ERROR,BOOLEAN,ERROR,ERROR}}, //FOR UNARY !  a[6][0][]
			  	
			  	{{OK,OK,ERROR,ERROR,ERROR,ERROR,ERROR},  //a[7][0][]
			  	 {ERROR,OK,ERROR,ERROR,ERROR,ERROR,ERROR},
			  	 {ERROR,ERROR,OK,ERROR,ERROR,ERROR,ERROR},
			  	 {ERROR,ERROR,ERROR,OK,ERROR,ERROR,ERROR},
			  	 {ERROR,ERROR,ERROR,ERROR,OK,ERROR,ERROR},
			  	 {ERROR,ERROR,ERROR,ERROR,ERROR,OK,ERROR},
			  	 {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}}  //FOR =
		  		 
		  		 
  };
  
  public static Stack getStack()
  {
	  return stack;
  }
  public static Hashtable<String, Vector<SymbolTableItem>> getSymbolTable() {
	 
    return symbolTable;
  }
  
  public static void checkVariable(String type, String id) {
	  
	
	  if(!symbolTable.containsKey(id))
	  {
		 
    // B. if !exist then insert: type, scope=global, value={0, false, "", '')
		  Vector v = new Vector();
		  v.add(new SymbolTableItem(type,"global", ""));
		  symbolTable.put(id, v);
	  }
	  else
		
		error(Parser.getgui(),1,Parser.getlineno(),id);
    // C. else error: â€œvariable id is already definedâ€?
	  
  }

  public static void pushStack(String type) {
  
	  stack.push(type);
	  System.out.println(stack);
    // push type in the stack
  }
  
  public static String popStack() {
    String result=(String) stack.pop();
    System.out.println(result);
    // pop a value from the stack
    return result;
  }
  
  
  public static String calculateCube(String type, String operator) {
    String result="";
    int op=0;
    int t1=0;
    if(operator.equals("-"))
    	op=UNMINUS;
    else if(operator.equals("!"))
    	op=NOT;
    
    if(type.equals("int"))
    	t1=INT;
    else if(type.equals("float"))
    	t1=FLOAT;
    else if(type.equals("string"))
    	t1=STRING;
    else if(type.equals("char"))
    	t1=CHAR;
    else if(type.equals("void"))
    	t1=VOID;
    else if(type.equals("boolean"))
    	t1=BOOLEAN;
    else
    	t1=ERROR;
    result=Integer.toString(a[op][0][t1]);
    if(result.equals("0")) result="int";
    else if(result.equals("1")) result="float";
    else if(result.equals("2")) result="char";
    else if(result.equals("3")) result="string";
    else if(result.equals("4")) result="boolean";
    else if(result.equals("5")) result="void";
    else if(result.equals("6")) result="ERROR";
    else if(result.equals("10"))result="OK";
    return result;
    // unary operator ( - and !)
    //return result;
  }

  public static String calculateCube(String type1, String type2, String operator) {
    String result="";
    int op=0;
    int t1=0;
    int t2=0;
    if(operator.equals("+"))
    	op=PLUS;
    else if(operator.equals("-")||operator.equals("*")||operator.equals("/"))
    	op=MINDIVMUL;
    else if(operator.equals("<")||operator.equals(">"))
    	op=LTGT;
    else if(operator.equals("==")||operator.equals("!="))
    	op=NEEE;
    else if(operator.equals("&")||operator.equals("|"))
    	op=ANDOR;
    else if(operator.equals("="))
    	op=EQUALS;
    			    	
    if(type1.equals("int"))
    	t1=INT;
    else if(type1.equals("float"))
    	t1=FLOAT;
    else if(type1.equals("string"))
    	t1=STRING;
    else if(type1.equals("char"))
    	t1=CHAR;
    else if(type1.equals("void"))
    	t1=VOID;
    else if(type1.equals("boolean"))
    	t1=BOOLEAN;
    else
    	t1=ERROR;
    
    if(type2.equals("int"))
    	t2=INT;
    else if(type2.equals("float"))
    	t2=FLOAT;
    else if(type2.equals("string"))
    	t2=STRING;
    else if(type2.equals("char"))
    	t2=CHAR;
    else if(type2.equals("void"))
    	t2=VOID;
    else if(type2.equals("boolean"))
    	t2=BOOLEAN;
    else
    	t2=ERROR;
    result=Integer.toString(a[op][t1][t2]);
    // binary operator ( - and !)
    if(result.equals("0")) result="int";
    else if(result.equals("1")) result="float";
    else if(result.equals("2")) result="char";
    else if(result.equals("3")) result="string";
    else if(result.equals("4")) result="boolean";
    else if(result.equals("5")) result="void";
    else if(result.equals("6")) result="ERROR";
    else if(result.equals("10"))result="OK";
    return result;
  }
  
  public static void error(Gui gui, int err, int n,String id) {
    switch (err) {
    case 0:
    	gui.writeConsole("Line" + n + ": variable " +"<" +id +"> " +"not found");
    	break;
      case 1: 
        gui.writeConsole("Line" + n + ": variable " +"<" +id +"> " +"is already defined"); 
        break;
      case 2: 
        gui.writeConsole("Line" + n + ": incompatible types: type mismatch"); 
        break;
      case 3: 
        gui.writeConsole("Line" + n + ": incompatible types: expected boolean"); 
        break;

    }
  }
  
}