/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.kiara.tool;

import de.dfki.kiara.idl.IDLWriter;
import de.dfki.kiara.idl.KiaraKTDConstructor;
import de.dfki.kiara.idl.KiaraLexer;
import de.dfki.kiara.idl.KiaraParser;
import de.dfki.kiara.ktd.Module;
import de.dfki.kiara.ktd.World;
import java.io.FileInputStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class KiaraIDLParser {

    public static void main(String[] args) throws Exception {
        System.out.println("Parsing file: " + args[0]);
        // create a CharStream that reads from standard input
        ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(args[0]));
        // create a lexer that feeds off of input CharStream
        KiaraLexer lexer = new KiaraLexer(input);
        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // create a parser that feeds off the tokens buffer
        KiaraParser parser = new KiaraParser(tokens);
        ParseTree tree = parser.program(); // begin parsing at program rule

        ParseTreeWalker walker = new ParseTreeWalker();

        World world = new World();
        Module module = new Module(world, "kiara");
        KiaraKTDConstructor ktdConstructor = new KiaraKTDConstructor(module, args[0]);
        walker.walk(ktdConstructor, tree);
        System.out.println(tree.toStringTree(parser)); // print LISP-style tree

        if (!ktdConstructor.getParserErrors().isEmpty()) {
            System.err.println("Parser errors:");
            for (String error : ktdConstructor.getParserErrors()) {
                    System.err.print("ERROR: ");
                    System.err.println(error);
            }
        }

        IDLWriter idlWriter = new IDLWriter(module);
        idlWriter.write(System.out);
    }

}
