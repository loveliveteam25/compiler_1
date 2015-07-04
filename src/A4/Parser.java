/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package A4;

/**
 *
 * @author FanjieLin
 */


import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;



/**

 *

 * @author javiergs

 */

public class Parser {

  private static DefaultMutableTreeNode root;

  private static Vector<Token> tokens;

  private static int currentToken;

  private static Gui gui;
  
  private static int label_count=1;

  public static DefaultMutableTreeNode run(Vector<Token> t,Gui gui) {

	Parser.gui=gui;
    tokens = t;

    currentToken = 0;

    root = new DefaultMutableTreeNode("program");

    //

    rule_program(root);
    CodeGenerator.addInstruction("OPR", "0", "0");
    //
    gui.writeSymbolTable(SemanticAnalyzer.getSymbolTable());
    CodeGenerator.writeCode(gui);
    SemanticAnalyzer.getSymbolTable().clear();
    SemanticAnalyzer.getStack().clear();
    CodeGenerator.clear(gui);
    return root;

  }
  
  public static Gui getgui()
  {
	  return gui;
  }
  public static int getlineno()
  {
	  return tokens.get(currentToken).getLine();
  }

  

  private static boolean rule_program(DefaultMutableTreeNode parent) {
	  
	  boolean error=false;
	  DefaultMutableTreeNode node;      
	 
	  if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("{"))
	  {
		  node= new DefaultMutableTreeNode("{");
		  parent.add(node);
		  currentToken++;
	  }
	  else
	  {
		  error(1);
		  while(currentToken<tokens.size()&&!(tokens.get(currentToken).getWord().equalsIgnoreCase("print")||tokens.get(currentToken).getToken().equalsIgnoreCase("IDENTIFIER")||tokens.get(currentToken).getWord().equalsIgnoreCase("int")||tokens.get(currentToken).getWord().equalsIgnoreCase("float")||tokens.get(currentToken).getWord().equalsIgnoreCase("boolean")||tokens.get(currentToken).getWord().equalsIgnoreCase("void")||tokens.get(currentToken).getWord().equalsIgnoreCase("char")||tokens.get(currentToken).getWord().equalsIgnoreCase("string")||tokens.get(currentToken).getWord().equalsIgnoreCase("while")||tokens.get(currentToken).getWord().equalsIgnoreCase("if")||tokens.get(currentToken).getWord().equalsIgnoreCase("return")||tokens.get(currentToken).getWord().equals("}")))
		  {
			  currentToken++;
		  }
	  }
	  node = new DefaultMutableTreeNode("body");
	  parent.add(node);
	  error=rule_body(node);
	  //currentToken++;
	  
	  if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("}"))
	  {
		  node=new DefaultMutableTreeNode("}");
		  parent.add(node);
		  
		  currentToken++;
	  }	  
	  else
	  {
		  currentToken--; //new addition
		  error(2);
		  currentToken++;
	  }
	  return error;
  }


  private static boolean rule_body(DefaultMutableTreeNode parent)
  {	  
	  boolean error=false;
	 
	  while(currentToken<tokens.size() && !tokens.get(currentToken).getWord().equals("}"))
	  {
		  if(tokens.get(currentToken).getToken().equalsIgnoreCase("identifier"))
		  {
			  DefaultMutableTreeNode node;
			  node=new DefaultMutableTreeNode("assignment");
			  parent.add(node);
			  error=rule_assignment(node);
			  if(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
			  {     	  
				  node=new DefaultMutableTreeNode(";");
				  parent.add(node);
				  currentToken++;
			  }
			  else error(3);
		  }		  
         
		  else if(tokens.get(currentToken).getWord().equalsIgnoreCase("INT")||tokens.get(currentToken).getWord().equalsIgnoreCase("FLOAT")||tokens.get(currentToken).getWord().equalsIgnoreCase("BOOLEAN")||tokens.get(currentToken).getWord().equalsIgnoreCase("CHAR")||tokens.get(currentToken).getWord().equalsIgnoreCase("STRING")||tokens.get(currentToken).getWord().equalsIgnoreCase("VOID"))
		  {
			  DefaultMutableTreeNode node;
              node=new DefaultMutableTreeNode("variable");
              parent.add(node);
              error=rule_variable(node);
              if(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
              {
            	  node=new DefaultMutableTreeNode(";");
            	  parent.add(node);
            	  currentToken++;
              }
              else 
				  error(3);
            	  
              

		  }

		  else if(tokens.get(currentToken).getWord().equalsIgnoreCase("print"))
		  {
			  DefaultMutableTreeNode node;
			  node=new DefaultMutableTreeNode("print");
			  parent.add(node);
			  error=rule_print(node);
			  if(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
			  {
				  node=new DefaultMutableTreeNode(";");
				  parent.add(node);
				  currentToken++;
			  }
			  else
			  {
				  if(currentToken<tokens.size())
				  error(3);
			  }
		  }
         
		  else if(tokens.get(currentToken).getWord().equalsIgnoreCase("return"))
		  {
			  
			  DefaultMutableTreeNode node;
			  node=new DefaultMutableTreeNode("return");
			  parent.add(node);
			  error=rule_return(node);
			  if(currentToken<tokens.size()&& tokens.get(currentToken).getWord().equals(";"))
			  {
				  node=new DefaultMutableTreeNode(";");
				  parent.add(node);
                  currentToken++;
			  }
			  else
				  error(3);
		  }

		  else if(tokens.get(currentToken).getWord().equalsIgnoreCase("while"))
		  {
			  DefaultMutableTreeNode node;
			  node=new DefaultMutableTreeNode("while");
			  parent.add(node);
			  error=rule_while(node);
			  //either do token++ here or inside the while rule
		  }

		  else if(tokens.get(currentToken).getWord().equals("if"))
		  {
			  DefaultMutableTreeNode node;
			  node=new DefaultMutableTreeNode("if");
			  parent.add(node);
			  error=rule_if(node);
			  //either do token++ here or inside the if rule
		  }


		  else 
		  {
			  error(4);			  
			  while(currentToken<tokens.size()&&!(tokens.get(currentToken).getWord().equalsIgnoreCase("print")||tokens.get(currentToken).getToken().equalsIgnoreCase("IDENTIFIER")||tokens.get(currentToken).getWord().equalsIgnoreCase("int")||tokens.get(currentToken).getWord().equalsIgnoreCase("float")||tokens.get(currentToken).getWord().equalsIgnoreCase("boolean")||tokens.get(currentToken).getWord().equalsIgnoreCase("void")||tokens.get(currentToken).getWord().equalsIgnoreCase("char")||tokens.get(currentToken).getWord().equalsIgnoreCase("string")||tokens.get(currentToken).getWord().equalsIgnoreCase("while")||tokens.get(currentToken).getWord().equalsIgnoreCase("if")||tokens.get(currentToken).getWord().equalsIgnoreCase("return")||tokens.get(currentToken).getWord().equals("}")))
			  {
				  currentToken++;
			  }
			  
		  }
	  }//while   
	  return error;
  }
  

  
  private static boolean rule_assignment(DefaultMutableTreeNode parent)
  {
	  
	  boolean error=false;
	  DefaultMutableTreeNode node;
	 
	  String iden=tokens.get(currentToken).getWord();
	  node=new DefaultMutableTreeNode("identifier" + "(" + tokens.get(currentToken).getWord() +")");
	  parent.add(node);
	  
	  String s=tokens.get(currentToken).getWord();
	  if(SemanticAnalyzer.getSymbolTable().get(s) != null)
		  SemanticAnalyzer.pushStack(SemanticAnalyzer.getSymbolTable().get(s).firstElement().getType());		  
	  else
	  {
		  SemanticAnalyzer.pushStack("ERROR");
		  SemanticAnalyzer.error(getgui(), 0, getlineno(),s);
	  }
	  
	  currentToken++;
	  if(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("="))
	  {
		  node=new DefaultMutableTreeNode("=");
		  parent.add(node);
		  currentToken++;		
	  }

	  else 
	  {
		  error(5);
		  while(currentToken<tokens.size()&&!(tokens.get(currentToken).getWord().equals("!")||tokens.get(currentToken).getWord().equals("-")||tokens.get(currentToken).getToken().equals("INTEGER")||tokens.get(currentToken).getToken().equals("OCTAL")||tokens.get(currentToken).getToken().equals("HEXADECIMAL")||tokens.get(currentToken).getToken().equals("BINARY")||tokens.get(currentToken).getToken().equals("STRING")||tokens.get(currentToken).getToken().equals("CHARACTER")||tokens.get(currentToken).getToken().equals("FLOAT")||tokens.get(currentToken).getToken().equals("IDENTIFIER")||tokens.get(currentToken).getWord().equals("true")||tokens.get(currentToken).getWord().equals("false")||tokens.get(currentToken).getWord().equals("(")||tokens.get(currentToken).getWord().equals(")")||tokens.get(currentToken).getWord().equals(";")))
		  {
			currentToken++;  
		  }
	  }
	  
	  node=new DefaultMutableTreeNode("expression");
	  parent.add(node);
	  error=rule_expression(node);
	  
	  String s1=SemanticAnalyzer.popStack();
	  String s2=SemanticAnalyzer.popStack();
	  String operator="=";
	  String res=SemanticAnalyzer.calculateCube(s1, s2, operator);
	  CodeGenerator.addInstruction("STO", iden, "0");
		
	  if(res.equals("ERROR"))
		SemanticAnalyzer.error(getgui(), 2, getlineno(),"");
	  
	  return error;
  }

  
  private static boolean rule_variable(DefaultMutableTreeNode parent)
  {
	  boolean error=false;
	  DefaultMutableTreeNode node;
	 
	  String type=tokens.get(currentToken).getWord();
	  String iden="";
	  node=new DefaultMutableTreeNode(tokens.get(currentToken).getWord());
	  parent.add(node);
	  currentToken++;
	  if(currentToken<tokens.size() && tokens.get(currentToken).getToken().equalsIgnoreCase("identifier"))
	  {
		  SemanticAnalyzer.checkVariable(tokens.get(currentToken-1).getWord(),tokens.get(currentToken).getWord()); 	 
		  node=new DefaultMutableTreeNode("identifier" + "(" + tokens.get(currentToken).getWord() +")");
		  iden=tokens.get(currentToken).getWord();
		  CodeGenerator.addVariable(type, iden);
		  parent.add(node);
		  currentToken++;
	  }

	  else 
	  {
	
		  error(6);
	  }

	  return error;
  }

  
  private static boolean rule_print(DefaultMutableTreeNode parent)
  {
	  boolean error=false;
	  DefaultMutableTreeNode node;
	
	  node=new DefaultMutableTreeNode(tokens.get(currentToken).getWord());
	  parent.add(node);
	  currentToken++;
	  if(currentToken<tokens.size() && tokens.get(currentToken).getWord().equals("("))
	  {
		  node=new DefaultMutableTreeNode("(");
		  parent.add(node);	  
		  currentToken++;	 
	  }
	  
	  else 
	  {
		  error(8);
		  while(currentToken<tokens.size()&&!(tokens.get(currentToken).getWord().equals("!")||tokens.get(currentToken).getWord().equals("-")||tokens.get(currentToken).getToken().equals("INTEGER")||tokens.get(currentToken).getToken().equals("OCTAL")||tokens.get(currentToken).getToken().equals("HEXADECIMAL")||tokens.get(currentToken).getToken().equals("BINARY")||tokens.get(currentToken).getToken().equals("STRING")||tokens.get(currentToken).getToken().equals("CHARACTER")||tokens.get(currentToken).getToken().equals("FLOAT")||tokens.get(currentToken).getToken().equals("IDENTIFIER")||tokens.get(currentToken).getWord().equals("true")||tokens.get(currentToken).getWord().equals("false")||tokens.get(currentToken).getWord().equals("(")||tokens.get(currentToken).getWord().equals(")")))
		  {
			  currentToken++;  
		  }
	  }
	  
	  node=new DefaultMutableTreeNode("expression");
	  parent.add(node);
	  error=rule_expression(node);
	  
	  if(currentToken<tokens.size()&& tokens.get(currentToken).getWord().equals(")"))
	  {
		  node=new DefaultMutableTreeNode(")");
		  parent.add(node);
		  CodeGenerator.addInstruction("OPR", "21", "0");
		  currentToken++;
	  }
  
	  else
		  error(7); 
	  return error;

  }

  
  private static boolean rule_return(DefaultMutableTreeNode parent)
  {
	  boolean error=false;
	  DefaultMutableTreeNode node;
	 
	  node=new DefaultMutableTreeNode(tokens.get(currentToken).getWord());
	  parent.add(node);
	  currentToken++;
	  CodeGenerator.addInstruction("OPR", "1", "0");
	  return error;
  }
  
  
  private static boolean rule_while(DefaultMutableTreeNode parent)
  {
	  
	  boolean error=false;
	  DefaultMutableTreeNode node;
	
	  node=new DefaultMutableTreeNode("while");
	  parent.add(node);
	  currentToken++;
	 
	  if(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("("))
	  {
		  node=new DefaultMutableTreeNode("(");
		  parent.add(node);
		  currentToken++;
	  }    
      
	  else 
	  {
		  error(8);
		  while(currentToken<tokens.size()&&!(tokens.get(currentToken).getWord().equals("!")||tokens.get(currentToken).getWord().equals("-")||tokens.get(currentToken).getToken().equals("INTEGER")||tokens.get(currentToken).getToken().equals("OCTAL")||tokens.get(currentToken).getToken().equals("HEXADECIMAL")||tokens.get(currentToken).getToken().equals("BINARY")||tokens.get(currentToken).getToken().equals("STRING")||tokens.get(currentToken).getToken().equals("CHARACTER")||tokens.get(currentToken).getToken().equals("FLOAT")||tokens.get(currentToken).getToken().equals("IDENTIFIER")||tokens.get(currentToken).getWord().equals("true")||tokens.get(currentToken).getWord().equals("false")||tokens.get(currentToken).getWord().equals("(")||tokens.get(currentToken).getWord().equals(")")))
		  {
			  currentToken++;  
		  }
	  }
      
	  node=new DefaultMutableTreeNode("expression");
	  parent.add(node);  
	  error=rule_expression(node);
	  String type=SemanticAnalyzer.popStack();
	  if(!type.equals("boolean"))
          SemanticAnalyzer.error(getgui(), 3, getlineno(),"");
	  if(tokens.get(currentToken).getWord().equals(")"))
	  {
		  node=new DefaultMutableTreeNode(")");
    	  parent.add(node);
    	  currentToken++;
      }
	  else
	  {
		  error(7);
		  while(currentToken<tokens.size()&&!tokens.get(currentToken).getWord().equals("{"))
		  {
			  currentToken++;
		  }
		  
	  }
	  CodeGenerator.addInstruction("JMC", "e"+label_count, "false");
	  label_count++;
	  node=new DefaultMutableTreeNode("program");
	  parent.add(node);
	  error=rule_program(node); 
	 
	  CodeGenerator.addInstruction("JMP", "e"+label_count, "0");
	  
	  label_count++;
	  return error;
  }
  
  
  private static boolean rule_if(DefaultMutableTreeNode parent)
  {
	  boolean error=false;
	  DefaultMutableTreeNode node;
	
	  node=new DefaultMutableTreeNode("if");
	  parent.add(node);
	  currentToken++;
	  if(tokens.get(currentToken).getWord().equals("("))
	  {
		  node=new DefaultMutableTreeNode("(");
		  parent.add(node);
		  currentToken++;
	  }
	  else 
	  {
		  error(8);
		  while(currentToken<tokens.size()&&!(tokens.get(currentToken).getWord().equals("!")||tokens.get(currentToken).getWord().equals("-")||tokens.get(currentToken).getToken().equals("INTEGER")||tokens.get(currentToken).getToken().equals("OCTAL")||tokens.get(currentToken).getToken().equals("HEXADECIMAL")||tokens.get(currentToken).getToken().equals("BINARY")||tokens.get(currentToken).getToken().equals("STRING")||tokens.get(currentToken).getToken().equals("CHARACTER")||tokens.get(currentToken).getToken().equals("FLOAT")||tokens.get(currentToken).getToken().equals("IDENTIFIER")||tokens.get(currentToken).getWord().equals("true")||tokens.get(currentToken).getWord().equals("false")||tokens.get(currentToken).getWord().equals("(")||tokens.get(currentToken).getWord().equals(")")))
		  {
			  currentToken++;  
		  }
	  }
         
	  node=new DefaultMutableTreeNode("expression");
	  parent.add(node);
	  error=rule_expression(node);
	  String type=SemanticAnalyzer.popStack();
	  if(!type.equals("boolean"))
		  SemanticAnalyzer.error(getgui(), 3, getlineno(),"");
	  
	  if(tokens.get(currentToken).getWord().equals(")"))   
	  {
		  node=new DefaultMutableTreeNode(")");    
		  parent.add(node);     
		  currentToken++;
	  }            
	  
	  else 
	  {
		  error(7);
		  while(currentToken<tokens.size()&&!(tokens.get(currentToken).getWord().equals("{")||tokens.get(currentToken).getWord().equals("else")))
			  currentToken++;
	  }
	  
	  CodeGenerator.addInstruction("JMC", "e"+label_count, "false");
	  label_count++;
	  node=new DefaultMutableTreeNode("PROGRAM");
	  parent.add(node);
	  error=rule_program(node);
	  
	  if(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equalsIgnoreCase("else"))      
	  {     
		  node=new DefaultMutableTreeNode("ELSE");         
		  parent.add(node);          
		  currentToken++;          
		  node=new DefaultMutableTreeNode("PROGRAM");
		  parent.add(node);
		  CodeGenerator.addInstruction("JMP", "e"+label_count, "0");
		  label_count++;
		  error=rule_program(node);         
	  }
	  return error;       
  }
             
  
  private static boolean rule_expression(DefaultMutableTreeNode parent)
  {
	  boolean error=false;
	  int flag=1;
	  DefaultMutableTreeNode node;
	 
	  node=new DefaultMutableTreeNode("X");
	  parent.add(node);
	  error=rule_X(node);
	
	  String operator="|";
	  while(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("|"))
	  {
		  node=new DefaultMutableTreeNode("|");
		  parent.add(node);
		  currentToken++;
		  node=new DefaultMutableTreeNode("X");
		  parent.add(node);
		  error=rule_X(node);
		  flag++;
		  
		  if(flag==2)
		  {  String type1=SemanticAnalyzer.popStack();
		  	 String type2=SemanticAnalyzer.popStack();
		  	 System.out.println("result: " +SemanticAnalyzer.calculateCube(type1,type2,operator));
		  	 SemanticAnalyzer.pushStack(SemanticAnalyzer.calculateCube(type1,type2,operator));
		  	 CodeGenerator.addInstruction("OPR", "8", "0");
		  	 flag=1;
		  	 
		  }
		  else flag=1;
		 
	  }
	  
	  return error;
  }

  
  private static boolean rule_X(DefaultMutableTreeNode parent)
  {
	 
	  int flag=1;
	  String operator="";
	  boolean error=false;
	  DefaultMutableTreeNode node;
	  node=new DefaultMutableTreeNode("Y");
	  parent.add(node);
	  error=rule_Y(node);
	 
	  
	  while(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("&"))
	  {
		  node=new DefaultMutableTreeNode("&");
		  parent.add(node);
		  currentToken++;
		  node=new DefaultMutableTreeNode("Y");
		  parent.add(node);
		  error=rule_Y(node);
		  flag++;
		  operator="&";
		  if(flag==2)
		  {  String type1=SemanticAnalyzer.popStack();
		  	 String type2=SemanticAnalyzer.popStack();
		  	 System.out.println("result: " +SemanticAnalyzer.calculateCube(type1,type2,operator));
		  	 SemanticAnalyzer.pushStack(SemanticAnalyzer.calculateCube(type1,type2,operator));
		  	CodeGenerator.addInstruction("OPR", "9", "0");
		  	 flag=1;
		  	 
		  }
		  else flag=1;
	  }
	  return error;
  }

  private static boolean rule_Y(DefaultMutableTreeNode parent)
  {
	 // System.out.println("Inside Y");
	  int flag=0;
	  String operator="";
	  boolean error=false;
	  DefaultMutableTreeNode node;
	  if(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("!"))
	  {
		  node=new DefaultMutableTreeNode("!");
		  parent.add(node);
		  currentToken++;
		  flag=1;
		  operator="!";
	  }
	  node=new DefaultMutableTreeNode("R");
	  parent.add(node);
	  error=rule_R(node);
	  if(flag==1)
	  {
		  String s=SemanticAnalyzer.popStack();
		  SemanticAnalyzer.pushStack(SemanticAnalyzer.calculateCube(s, operator));
		  CodeGenerator.addInstruction("OPR", "10", "0");
		  System.out.println("Here in Y");
	  }
	 
	  return error;
  }
  
  
  private static boolean rule_R(DefaultMutableTreeNode parent)
  {
	  int flag=1;
	  String operator="";
	  String operator_num="";
	
	  boolean error=false;
	  DefaultMutableTreeNode node;
	  node=new DefaultMutableTreeNode("E");
	  parent.add(node);
	  error=rule_E(node);
	 
	  while(currentToken<tokens.size()&&(tokens.get(currentToken).getWord().equals("<")||tokens.get(currentToken).getWord().equals(">")||tokens.get(currentToken).getWord().equals("==")||tokens.get(currentToken).getWord().equals("!=")))
	  {
		  if(tokens.get(currentToken).getWord().equals("<"))
		  {
			  node=new DefaultMutableTreeNode("<");
			  parent.add(node);
			  operator="<";
			  operator_num="12";
		  }
		  if(tokens.get(currentToken).getWord().equals(">"))
		  {
			  node=new DefaultMutableTreeNode(">");
			  parent.add(node);
			  operator=">";
			  operator_num="11";
		  }
		  if(tokens.get(currentToken).getWord().equals("=="))
		  {
			  node=new DefaultMutableTreeNode("==");
			  parent.add(node);
			  operator="==";
			  operator_num="15";
		  }
		  if(tokens.get(currentToken).getWord().equals("!="))
		  {
			  node=new DefaultMutableTreeNode("!=");
			  parent.add(node);
			  operator="!=";
			  operator_num="16";
		  }
  			
		  currentToken++;
		  node=new DefaultMutableTreeNode("E");
		  parent.add(node);
		  error=rule_E(node);
		  flag++;
		  if(flag==2)
		  {  String type1=SemanticAnalyzer.popStack();
		  	 String type2=SemanticAnalyzer.popStack();
		  	 System.out.println("result: " +SemanticAnalyzer.calculateCube(type1,type2,operator));
		  	 SemanticAnalyzer.pushStack(SemanticAnalyzer.calculateCube(type1,type2,operator));
		  	 CodeGenerator.addInstruction("OPR", operator_num, "0");
		  	 flag=1;
		  	 
		  }
		  else flag=1;
	  }
	  return error;
  }

  
  private static boolean rule_E(DefaultMutableTreeNode parent) {
    

	  boolean error;
	  DefaultMutableTreeNode node;
	  node = new DefaultMutableTreeNode("A");
	  parent.add(node);	
	  error = rule_A(node);
	
	  int flag=1;
	  
	  String operator = new String();
	  String operator_num="";
	  while (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("+") || currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("-")) {

		  if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("+")) {
			  operator=tokens.get(currentToken).getWord();
			  node = new DefaultMutableTreeNode("+");
			  parent.add(node);
			  currentToken++;
			  node = new DefaultMutableTreeNode("A");
			  parent.add(node);
			  error = rule_A(node);
			  operator="+";
			  operator_num="2";
			  flag++;
		  } 
		  
		  else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("-")) {
			  operator=tokens.get(currentToken).getWord();
			  node = new DefaultMutableTreeNode("-");
			  parent.add(node);
			  currentToken++;
			  node = new DefaultMutableTreeNode("A");
			  parent.add(node);
			  error = rule_A(node);
			  operator="-";
			  operator_num="3";
			  flag++;
		  }
		  
		  if(flag==2)
		  {  String type1=SemanticAnalyzer.popStack();
		  	 String type2=SemanticAnalyzer.popStack();
		  	 System.out.println("result: " +SemanticAnalyzer.calculateCube(type1,type2,operator));
		  	 SemanticAnalyzer.pushStack(SemanticAnalyzer.calculateCube(type1,type2,operator));
		  	 CodeGenerator.addInstruction("OPR", operator_num, "0");
		  	 flag=1;
		  	 
		  }
		  else flag=1;
	  }
	  return error;
  }

  
  private static boolean rule_A(DefaultMutableTreeNode parent) {
	  int flag=1;
	  String operator="";
	  String operator_num="";
    boolean error;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("B");
    parent.add(node);
    error = rule_B(node);

    while (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("*") || currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("/")) {
    	
      if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("*")) {
        node = new DefaultMutableTreeNode("*");
        parent.add(node);
        currentToken++;
        node = new DefaultMutableTreeNode("B");
        parent.add(node);
        error = rule_B(node);
        operator="*";
        operator_num="4";
        flag++;
      } 
      
      else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("/")) {

        node = new DefaultMutableTreeNode("/");
        parent.add(node);
        node = new DefaultMutableTreeNode("B");
        parent.add(node);
        currentToken++;
        error = rule_B(node);
        operator="/";
        operator_num="5";
        flag++;
      }
      
      if(flag==2)
	  {  String type1=SemanticAnalyzer.popStack();
	  	 String type2=SemanticAnalyzer.popStack();
	  	 System.out.println("result: " +SemanticAnalyzer.calculateCube(type1,type2,operator));
	  	 SemanticAnalyzer.pushStack(SemanticAnalyzer.calculateCube(type1,type2,operator));
	  	 CodeGenerator.addInstruction("OPR", operator_num, "0");
	  	 flag=1;
	  	 
	  }
	  else flag=1;
    }
    return error;
  }
  
  
  private static boolean rule_B(DefaultMutableTreeNode parent) {
    boolean error;
    DefaultMutableTreeNode node;
    int flag=0;
    String operator="";
    if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("-")) {
      node = new DefaultMutableTreeNode("-");
      parent.add(node);
      currentToken++;
      flag=1;
      operator="-";
      CodeGenerator.addInstruction("LIT", "0", "0");
    }

    node = new DefaultMutableTreeNode("C");
    parent.add(node);
    error = rule_C(node);
    if(flag==1)
	  {
		  String s=SemanticAnalyzer.popStack();
		  SemanticAnalyzer.pushStack(SemanticAnalyzer.calculateCube(s, operator));
		  CodeGenerator.addInstruction("OPR","3", "0");
		  System.out.println("here");
	  }
    return error;
  }
  
  
  private static boolean rule_C(DefaultMutableTreeNode parent) {
    boolean error = false;
    DefaultMutableTreeNode node;

    if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("INTEGER")) {
      node = new DefaultMutableTreeNode("integer" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
      SemanticAnalyzer.pushStack("int");
      CodeGenerator.addInstruction("LIT", tokens.get(currentToken).getWord(), "0");
      currentToken++;
    }
    
    else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("OCTAL")) {
        node = new DefaultMutableTreeNode("octal" + "(" + tokens.get(currentToken).getWord() + ")");
        parent.add(node);
        SemanticAnalyzer.pushStack("int");
        CodeGenerator.addInstruction("LIT", tokens.get(currentToken).getWord(), "0");
        currentToken++;
      }
    
    else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("HEXADECIMAL")) {
        node = new DefaultMutableTreeNode("hexadecimal" + "(" + tokens.get(currentToken).getWord() + ")");
        parent.add(node);
        SemanticAnalyzer.pushStack("int");
        CodeGenerator.addInstruction("LIT", tokens.get(currentToken).getWord(), "0");
        currentToken++;
      }

    else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("BINARY")) {
        node = new DefaultMutableTreeNode("binary" + "(" + tokens.get(currentToken).getWord() + ")");
        parent.add(node);
        SemanticAnalyzer.pushStack("int");
        CodeGenerator.addInstruction("LIT", tokens.get(currentToken).getWord(), "0");
        currentToken++;
      }

    else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("STRING")) {
        node = new DefaultMutableTreeNode("string" + "(" + tokens.get(currentToken).getWord() + ")");
        parent.add(node);
        SemanticAnalyzer.pushStack("string");
        CodeGenerator.addInstruction("LIT", tokens.get(currentToken).getWord(), "0");
        currentToken++;
      }

    else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("CHARACTER")) {
        node = new DefaultMutableTreeNode("character" + "(" + tokens.get(currentToken).getWord() + ")");
        parent.add(node);
        SemanticAnalyzer.pushStack("char");
        CodeGenerator.addInstruction("LIT", tokens.get(currentToken).getWord(), "0");
        currentToken++;
      }
    
    else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("FLOAT")) {
        node = new DefaultMutableTreeNode("float" + "(" + tokens.get(currentToken).getWord() + ")");
        parent.add(node);
        SemanticAnalyzer.pushStack("float");
        CodeGenerator.addInstruction("LIT", tokens.get(currentToken).getWord(), "0");
        currentToken++;
      }

    else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equalsIgnoreCase("true")) {
        node = new DefaultMutableTreeNode("boolean" + "(" + tokens.get(currentToken).getWord() + ")");
        parent.add(node);
        SemanticAnalyzer.pushStack("boolean");
        CodeGenerator.addInstruction("LIT", tokens.get(currentToken).getWord(), "0");
        currentToken++;
      }

    else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("false")) {
        node = new DefaultMutableTreeNode("boolean" + "(" + tokens.get(currentToken).getWord() + ")");
        parent.add(node);
        SemanticAnalyzer.pushStack("boolean");
        CodeGenerator.addInstruction("LIT", tokens.get(currentToken).getWord(), "0");
        currentToken++;
      }

    else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER")) {
      node = new DefaultMutableTreeNode("identifier" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
      if(SemanticAnalyzer.getSymbolTable().get(tokens.get(currentToken).getWord()) != null)
      SemanticAnalyzer.pushStack(SemanticAnalyzer.getSymbolTable().get(tokens.get(currentToken).getWord()).firstElement().getType());
      else
    	  {SemanticAnalyzer.error(getgui(), 0, getlineno(),tokens.get(currentToken).getWord());
    	   SemanticAnalyzer.pushStack("ERROR");
    	  }
      CodeGenerator.addInstruction("LOD", tokens.get(currentToken).getWord(), "0");
      currentToken++;
    } 
    
    else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("(")) {
      node = new DefaultMutableTreeNode("(");
      parent.add(node);
      currentToken++;
      //
      node = new DefaultMutableTreeNode("expression");
      parent.add(node);
      error = rule_expression(node);
      //
      if(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals(")"))
      {
    	  node = new DefaultMutableTreeNode(")");
    	  parent.add(node);	
    	  currentToken++;    
      }
      else
    	  error(7);
    } 
    
    
    else {
      error(9);
 
    }
    
    

    return false;
  }
  public static void error(int err)
  {
	  int n=0;
	  //if(currentToken<tokens.size()) 
	  if(err!=4 && currentToken!=0 && (tokens.get(currentToken-1).getLine()<tokens.get(currentToken).getLine()))
	  {
		  n=tokens.get(currentToken-1).getLine();
	  }
	  else
	  {
		  n=tokens.get(currentToken).getLine();
	  }
	  
	  switch(err)
	  {
	  case 1: 
		  gui.writeConsole("Line" +n+ ":expected {");
		  break;
	  case 2:
		  gui.writeConsole("Line" +n+ ":expected }");
		  break;
	  case 3:
		  gui.writeConsole("Line" +n+ ":expected ;");
		  break;
	  case 4:
		  gui.writeConsole("Line" +n+ ":expected identifier or keyword");
		  break;  
	  case 5:
		  gui.writeConsole("Line" +n+ ": expected ="); 
		  break;	  
	  case 6:
		  gui.writeConsole("Line" +n+": expected identifier"); 
		  break;
	  case 7:
		  gui.writeConsole("Line" +n+": expected )"); 
		  break;
	  case 8:
		  gui.writeConsole("Line" +n+": expected ("); 
		  break;
	  case 9:
		  gui.writeConsole("Line" +n+": expected value, identifier, (");
		  break;	  
		  
	  }
			  
  }
}