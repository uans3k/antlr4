/* This file is generated by TestGenerator, any edits will be overwritten by the next generation. */
package org.antlr.v4.test.runtime.go;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("unused")
public class TestParserErrors extends BaseTest {

	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testConjuringUpToken() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(63);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' x='b' {console.log(\"conjured=\" + $x);} 'c' ;");
		String grammar = grammarBuilder.toString();
		String input ="ac";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("conjured=[@-1,-1:-1='<missing 'b'>',<2>,1:1]\n", found);

		assertEquals("line 1:1 missing 'b' at 'c'\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testConjuringUpTokenFromSet() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(69);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' x=('b'|'c') {console.log(\"conjured=\" + $x);} 'd' ;");
		String grammar = grammarBuilder.toString();
		String input ="ad";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("conjured=[@-1,-1:-1='<missing 'b'>',<2>,1:1]\n", found);

		assertEquals("line 1:1 missing {'b', 'c'} at 'd'\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testContextListGetters() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(211);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("@parser::members{\n");
		grammarBuilder.append("	function foo() {\n");
		grammarBuilder.append("		var s = new SContext();\n");
		grammarBuilder.append("	    var a = s.a();\n");
		grammarBuilder.append("	    var b = s.b();\n");
		grammarBuilder.append("    };\n");
		grammarBuilder.append("}\n");
		grammarBuilder.append("s : (a | b)+;\n");
		grammarBuilder.append("a : 'a' {process.stdout.write('a');};\n");
		grammarBuilder.append("b : 'b' {process.stdout.write('b');};");
		String grammar = grammarBuilder.toString();
		String input ="abab";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "s", input, true);
		assertEquals("abab\n", found);
		assertNull(this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testDuplicatedLeftRecursiveCall_1() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(63);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("start : expr EOF;\n");
		grammarBuilder.append("expr : 'x'\n");
		grammarBuilder.append("     | expr expr\n");
		grammarBuilder.append("     ;");
		String grammar = grammarBuilder.toString();
		String input ="x";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "start", input, true);
		assertEquals("", found);
		assertNull(this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testDuplicatedLeftRecursiveCall_2() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(63);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("start : expr EOF;\n");
		grammarBuilder.append("expr : 'x'\n");
		grammarBuilder.append("     | expr expr\n");
		grammarBuilder.append("     ;");
		String grammar = grammarBuilder.toString();
		String input ="xx";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "start", input, true);
		assertEquals("", found);
		assertNull(this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testDuplicatedLeftRecursiveCall_3() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(63);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("start : expr EOF;\n");
		grammarBuilder.append("expr : 'x'\n");
		grammarBuilder.append("     | expr expr\n");
		grammarBuilder.append("     ;");
		String grammar = grammarBuilder.toString();
		String input ="xxx";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "start", input, true);
		assertEquals("", found);
		assertNull(this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testDuplicatedLeftRecursiveCall_4() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(63);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("start : expr EOF;\n");
		grammarBuilder.append("expr : 'x'\n");
		grammarBuilder.append("     | expr expr\n");
		grammarBuilder.append("     ;");
		String grammar = grammarBuilder.toString();
		String input ="xxxx";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "start", input, true);
		assertEquals("", found);
		assertNull(this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testInvalidATNStateRemoval() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(98);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("start : ID ':' expr;\n");
		grammarBuilder.append("expr : primary expr? {} | expr '->' ID;\n");
		grammarBuilder.append("primary : ID;\n");
		grammarBuilder.append("ID : [a-z]+;");
		String grammar = grammarBuilder.toString();
		String input ="x:x";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "start", input, false);
		assertEquals("", found);
		assertNull(this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testInvalidEmptyInput() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(36);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("start : ID+;\n");
		grammarBuilder.append("ID : [a-z]+;");
		String grammar = grammarBuilder.toString();
		String input ="";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "start", input, true);
		assertEquals("", found);

		assertEquals("line 1:0 missing ID at '<EOF>'\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testLL1ErrorInfo() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(301);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("start : animal (AND acClass)? service EOF;\n");
		grammarBuilder.append("animal : (DOG | CAT );\n");
		grammarBuilder.append("service : (HARDWARE | SOFTWARE) ;\n");
		grammarBuilder.append("AND : 'and';\n");
		grammarBuilder.append("DOG : 'dog';\n");
		grammarBuilder.append("CAT : 'cat';\n");
		grammarBuilder.append("HARDWARE: 'hardware';\n");
		grammarBuilder.append("SOFTWARE: 'software';\n");
		grammarBuilder.append("WS : ' ' -> skip ;\n");
		grammarBuilder.append("acClass\n");
		grammarBuilder.append("@init\n");
		grammarBuilder.append("{console.log(this.getExpectedTokens().toString(this.literalNames));}\n");
		grammarBuilder.append("  : ;");
		String grammar = grammarBuilder.toString();
		String input ="dog and software";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "start", input, false);
		assertEquals("{'hardware', 'software'}\n", found);
		assertNull(this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testLL2() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(46);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' 'b'\n");
		grammarBuilder.append("  | 'a' 'c'\n");
		grammarBuilder.append(";\n");
		grammarBuilder.append("q : 'e' ;");
		String grammar = grammarBuilder.toString();
		String input ="ae";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals("line 1:1 no viable alternative at input 'ae'\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testLL3() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(55);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' 'b'* 'c'\n");
		grammarBuilder.append("  | 'a' 'b' 'd'\n");
		grammarBuilder.append(";\n");
		grammarBuilder.append("q : 'e' ;");
		String grammar = grammarBuilder.toString();
		String input ="abe";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals("line 1:2 no viable alternative at input 'abe'\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testLLStar() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(48);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a'+ 'b'\n");
		grammarBuilder.append("  | 'a'+ 'c'\n");
		grammarBuilder.append(";\n");
		grammarBuilder.append("q : 'e' ;");
		String grammar = grammarBuilder.toString();
		String input ="aaae";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals("line 1:3 no viable alternative at input 'aaae'\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testMultiTokenDeletionBeforeLoop() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(28);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' 'b'* 'c';");
		String grammar = grammarBuilder.toString();
		String input ="aacabc";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals("line 1:1 extraneous input 'a' expecting {'b', 'c'}\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testMultiTokenDeletionBeforeLoop2() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(36);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' ('b'|'z'{})* 'c';");
		String grammar = grammarBuilder.toString();
		String input ="aacabc";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals("line 1:1 extraneous input 'a' expecting {'b', 'z', 'c'}\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testMultiTokenDeletionDuringLoop() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(29);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' 'b'* 'c' ;");
		String grammar = grammarBuilder.toString();
		String input ="abaaababc";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals(
			"line 1:2 extraneous input 'a' expecting {'b', 'c'}\n" +
			"line 1:6 extraneous input 'a' expecting {'b', 'c'}\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testMultiTokenDeletionDuringLoop2() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(37);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' ('b'|'z'{})* 'c' ;");
		String grammar = grammarBuilder.toString();
		String input ="abaaababc";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals(
			"line 1:2 extraneous input 'a' expecting {'b', 'z', 'c'}\n" +
			"line 1:6 extraneous input 'a' expecting {'b', 'z', 'c'}\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testNoViableAltAvoidance() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(83);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("s : e '!' ;\n");
		grammarBuilder.append("e : 'a' 'b'\n");
		grammarBuilder.append("  | 'a'\n");
		grammarBuilder.append("  ;\n");
		grammarBuilder.append("DOT : '.' ;\n");
		grammarBuilder.append("WS : [ \\t\\r\\n]+ -> skip;");
		String grammar = grammarBuilder.toString();
		String input ="a.";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "s", input, false);
		assertEquals("", found);

		assertEquals("line 1:1 mismatched input '.' expecting '!'\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testSingleSetInsertion() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(34);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' ('b'|'c') 'd' ;");
		String grammar = grammarBuilder.toString();
		String input ="ad";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals("line 1:1 missing {'b', 'c'} at 'd'\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testSingleSetInsertionConsumption() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(82);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("myset: ('b'|'c') ;\n");
		grammarBuilder.append("a: 'a' myset 'd' {console.log(\"\" + $myset.stop);} ; ");
		String grammar = grammarBuilder.toString();
		String input ="ad";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("[@0,0:0='a',<3>,1:0]\n", found);

		assertEquals("line 1:1 missing {'b', 'c'} at 'd'\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testSingleTokenDeletion() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(24);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' 'b' ;");
		String grammar = grammarBuilder.toString();
		String input ="aab";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals("line 1:1 extraneous input 'a' expecting 'b'\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testSingleTokenDeletionBeforeAlt() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(38);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : ('b' | 'c')\n");
		grammarBuilder.append(";\n");
		grammarBuilder.append("q : 'a'\n");
		grammarBuilder.append(";");
		String grammar = grammarBuilder.toString();
		String input ="ac";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals("line 1:0 extraneous input 'a' expecting {'b', 'c'}\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testSingleTokenDeletionBeforeLoop() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(25);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' 'b'* ;");
		String grammar = grammarBuilder.toString();
		String input ="aabc";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals(
			"line 1:1 extraneous input 'a' expecting {<EOF>, 'b'}\n" +
			"line 1:3 token recognition error at: 'c'\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testSingleTokenDeletionBeforeLoop2() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(32);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' ('b'|'z'{})*;");
		String grammar = grammarBuilder.toString();
		String input ="aabc";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals(
			"line 1:1 extraneous input 'a' expecting {<EOF>, 'b', 'z'}\n" +
			"line 1:3 token recognition error at: 'c'\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testSingleTokenDeletionBeforePredict() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(48);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a'+ 'b'\n");
		grammarBuilder.append("  | 'a'+ 'c'\n");
		grammarBuilder.append(";\n");
		grammarBuilder.append("q : 'e' ;");
		String grammar = grammarBuilder.toString();
		String input ="caaab";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals("line 1:0 extraneous input 'c' expecting 'a'\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testSingleTokenDeletionConsumption() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(82);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("myset: ('b'|'c') ;\n");
		grammarBuilder.append("a: 'a' myset 'd' {console.log(\"\" + $myset.stop);} ; ");
		String grammar = grammarBuilder.toString();
		String input ="aabd";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("[@2,2:2='b',<1>,1:2]\n", found);

		assertEquals("line 1:1 extraneous input 'a' expecting {'b', 'c'}\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testSingleTokenDeletionDuringLoop() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(29);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' 'b'* 'c' ;");
		String grammar = grammarBuilder.toString();
		String input ="ababbc";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals("line 1:2 extraneous input 'a' expecting {'b', 'c'}\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testSingleTokenDeletionDuringLoop2() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(37);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' ('b'|'z'{})* 'c' ;");
		String grammar = grammarBuilder.toString();
		String input ="ababbc";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals("line 1:2 extraneous input 'a' expecting {'b', 'z', 'c'}\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testSingleTokenDeletionExpectingSet() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(30);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' ('b'|'c') ;");
		String grammar = grammarBuilder.toString();
		String input ="aab";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals("line 1:1 extraneous input 'a' expecting {'b', 'c'}\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testSingleTokenInsertion() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(28);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' 'b' 'c' ;");
		String grammar = grammarBuilder.toString();
		String input ="ac";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals("line 1:1 missing 'b' at 'c'\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testTokenMismatch() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(24);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("a : 'a' 'b' ;");
		String grammar = grammarBuilder.toString();
		String input ="aa";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "a", input, false);
		assertEquals("", found);

		assertEquals("line 1:1 mismatched input 'a' expecting 'b'\n", this.stderrDuringParse);

	}
	/* This file and method are generated by TestGenerator, any edits will be overwritten by the next generation. */
	@Test
	public void testTokenMismatch2() throws Exception {
		mkdir(tmpdir);
		StringBuilder grammarBuilder = new StringBuilder(165);
		grammarBuilder.append("grammar T;\n");
		grammarBuilder.append("\n");
		grammarBuilder.append("stat:   ( '(' expr? ')' )? EOF ;\n");
		grammarBuilder.append("expr:   ID '=' STR ;\n");
		grammarBuilder.append("\n");
		grammarBuilder.append("ERR :   '~FORCE_ERROR~' ;\n");
		grammarBuilder.append("ID  :   [a-zA-Z]+ ;\n");
		grammarBuilder.append("STR :   '\"' ~[\"]* '\"' ;\n");
		grammarBuilder.append("WS  :   [ \\t\\r\\n]+ -> skip ;");
		String grammar = grammarBuilder.toString();
		String input ="( ~FORCE_ERROR~ ";
		String found = execParser("T.g4", grammar, "TParser", "TLexer",
		                          "TListener", "TVisitor",
		                          "stat", input, false);
		assertEquals("", found);

		assertEquals("line 1:2 mismatched input '~FORCE_ERROR~' expecting ')'\n", this.stderrDuringParse);

	}

}
