/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
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
