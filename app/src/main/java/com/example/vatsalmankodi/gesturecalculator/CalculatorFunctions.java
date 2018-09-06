package com.example.vatsalmankodi.gesturecalculator;

import java.util.Stack;
import java.util.StringTokenizer;

/**
 * Created by Vatsal Mankodi on 15-12-2016.
 */
public class CalculatorFunctions {

    public static boolean isOperator(char op){
        switch (op){
            case '+':
            case '-':
            case 'X':
            case '/':
                return true;
            default:return false;
        }
    }

    public static double operate(double first, double second, String op){
        switch (op){
            case "+": return (first) + (second);
            case "-": return (first) - (second);
            case "X": return (first) * (second);
            case "/":
                try{
                    return (first) / (second);
                }catch (Exception e){
                    e.printStackTrace();
                }
            default: return -1;
        }
    }

    public static int getWeight(char op){
        switch (op){
            case '+':
            case '-':
                return 0;
            case 'X':
            case '/':
                return 1;

            default:return 0;

        }

    }

    public static String toPostfix(String infix){
        Stack operatorStack = new Stack();
        StringBuilder postfix = new StringBuilder();
        for(char c : infix.toCharArray()){
            if(isOperator(c)){
                while(!operatorStack.isEmpty() && getWeight((char) operatorStack.peek())>= getWeight(c)){
                    postfix.append('|');
                    postfix.append(operatorStack.pop());
                }
                postfix.append('|');
                operatorStack.push(c);
            }else{
                postfix.append(c);
            }
        }
        while(!operatorStack.isEmpty()){
            postfix.append('|');
            postfix.append(operatorStack.pop());
        }
        return postfix.toString();
    }

    public static double evaluatePostfix(String postfix){
        Stack operandStack = new Stack();
        StringTokenizer st = new StringTokenizer(postfix,"|");
        int answer;
        while(st.hasMoreTokens()){
            String currentToken = st.nextToken();
            if(!operandStack.isEmpty() && isOperator(currentToken.charAt(0))){
                double temp = (double)operandStack.pop();
                operandStack.push((double)operate((double)operandStack.pop(),temp,currentToken));
            }else{
                operandStack.push(Double.parseDouble(currentToken));
            }
        }
        return (double)operandStack.pop();
    }


}
