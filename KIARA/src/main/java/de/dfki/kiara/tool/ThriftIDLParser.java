/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.kiara.tool;

import de.dfki.kiara.idl.ThriftLexer;
import de.dfki.kiara.idl.ThriftParser;
import java.io.FileInputStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 *
 * @author Dmitri Rubinstein
 */
public class ThriftIDLParser {

    public static void main(String[] args) throws Exception {
        System.out.println("Parsing file: " + args[0]);
        // create a CharStream that reads from standard input
        ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(args[0]));
        // create a lexer that feeds off of input CharStream
        ThriftLexer lexer = new ThriftLexer(input);
        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // create a parser that feeds off the tokens buffer
        ThriftParser parser = new ThriftParser(tokens);
        ParseTree tree = parser.program(); // begin parsing at program rule
        System.out.println(tree.toStringTree(parser)); // print LISP-style tree
    }
}
