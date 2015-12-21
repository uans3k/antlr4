package antlr4

import (
	"fmt"
			)

type TraceListener struct {
	ParseTreeListener
	parser *Parser
}

func NewTraceListener(parser *Parser) *TraceListener {
	tl := &TraceListener{ParseTreeListener{}}
    tl.parser = parser
	return tl
}

func (this *TraceListener) enterEveryRule(ctx *ParserRuleContext) {
	fmt.Println("enter   " + this.parser.getRuleNames()[ctx.ruleIndex] + ", LT(1)=" + this.parser._input.LT(1).text())
}

func (this *TraceListener) visitTerminal( node TerminalNode ) {
	fmt.Println("consume " + node.getSymbol() + " rule " + this.parser.getRuleNames()[this.parser._ctx.ruleIndex])
}

func (this *TraceListener) exitEveryRule(ctx *ParserRuleContext) {
	fmt.Println("exit    " + this.parser.getRuleNames()[ctx.ruleIndex] + ", LT(1)=" + this.parser._input.LT(1).text())
}


type Parser struct {
	Recognizer

	_input TokenStream
	_errHandler ErrorStrategy
	_precedenceStack IntStack
	_ctx ParserRuleContext
	buildParseTrees bool
	_tracer bool
	_parseListeners []*ParseTreeListener
	_syntaxErrors int

	literalNames []string
	symbolicNames []string
}

// p.is all the parsing support code essentially most of it is error
// recovery stuff.//
func NewParser(input *TokenStream) *Parser {

	p := new(Parser)

	p.InitRecognizer()

	// The input stream.
	p._input = nil
	// The error handling strategy for the parser. The default value is a new
	// instance of {@link DefaultErrorStrategy}.
	p._errHandler = NewDefaultErrorStrategy()
	p._precedenceStack = make([]int, 0)
	p._precedenceStack.Push(0)
	// The {@link ParserRuleContext} object for the currently executing rule.
	// p.is always non-nil during the parsing process.
	p._ctx = nil
	// Specifies whether or not the parser should construct a parse tree during
	// the parsing process. The default value is {@code true}.
	p.buildParseTrees = true
	// When {@link //setTrace}{@code (true)} is called, a reference to the
	// {@link TraceListener} is stored here so it can be easily removed in a
	// later call to {@link //setTrace}{@code (false)}. The listener itself is
	// implemented as a parser listener so p.field is not directly used by
	// other parser methods.
	p._tracer = nil
	// The list of {@link ParseTreeListener} listeners registered to receive
	// events during the parse.
	p._parseListeners = nil
	// The number of syntax errors reported during parsing. p.value is
	// incremented each time {@link //notifyErrorListeners} is called.
	p._syntaxErrors = 0
	p.setInputStream(input)

	return p
}

// p.field maps from the serialized ATN string to the deserialized {@link
// ATN} with
// bypass alternatives.
//
// @see ATNDeserializationOptions//isGenerateRuleBypassTransitions()
//
var bypassAltsAtnCache = make(map[string]int)

// reset the parser's state//
func (p *Parser) reset() {
	if (p._input != nil) {
		p._input.seek(0)
	}
	p._errHandler.reset()
	p._ctx = nil
	p._syntaxErrors = 0
	p.setTrace(false)
	p._precedenceStack = make([]int, 0)
	p._precedenceStack.Push(0)
	if (p._interp != nil) {
		p._interp.reset()
	}
}

// Match current input symbol against {@code ttype}. If the symbol type
// matches, {@link ANTLRErrorStrategy//reportMatch} and {@link //consume} are
// called to complete the match process.
//
// <p>If the symbol type does not match,
// {@link ANTLRErrorStrategy//recoverInline} is called on the current error
// strategy to attempt recovery. If {@link //getBuildParseTree} is
// {@code true} and the token index of the symbol returned by
// {@link ANTLRErrorStrategy//recoverInline} is -1, the symbol is added to
// the parse tree by calling {@link ParserRuleContext//addErrorNode}.</p>
//
// @param ttype the token type to match
// @return the matched symbol
// @panics RecognitionException if the current input symbol did not match
// {@code ttype} and the error strategy could not recover from the
// mismatched symbol

func (p *Parser) match(ttype int) *Token {
	var t = p.getCurrentToken()
	if (t.tokenType == ttype) {
		p._errHandler.reportMatch(p)
		p.consume()
	} else {
		t = p._errHandler.recoverInline(p)
		if (p.buildParseTrees && t.tokenIndex == -1) {
			// we must have conjured up a Newtoken during single token
			// insertion
			// if it's not the current symbol
			p._ctx.addErrorNode(t)
		}
	}
	return t
}
// Match current input symbol as a wildcard. If the symbol type matches
// (i.e. has a value greater than 0), {@link ANTLRErrorStrategy//reportMatch}
// and {@link //consume} are called to complete the match process.
//
// <p>If the symbol type does not match,
// {@link ANTLRErrorStrategy//recoverInline} is called on the current error
// strategy to attempt recovery. If {@link //getBuildParseTree} is
// {@code true} and the token index of the symbol returned by
// {@link ANTLRErrorStrategy//recoverInline} is -1, the symbol is added to
// the parse tree by calling {@link ParserRuleContext//addErrorNode}.</p>
//
// @return the matched symbol
// @panics RecognitionException if the current input symbol did not match
// a wildcard and the error strategy could not recover from the mismatched
// symbol

func (p *Parser) matchWildcard() *Token {
	var t = p.getCurrentToken()
	if (t.tokenType > 0) {
		p._errHandler.reportMatch(p)
		p.consume()
	} else {
		t = p._errHandler.recoverInline(p)
		if (p.buildParseTrees && t.tokenIndex == -1) {
			// we must have conjured up a Newtoken during single token
			// insertion
			// if it's not the current symbol
			p._ctx.addErrorNode(t)
		}
	}
	return t
}

func (p *Parser) getParseListeners() []*ParseTreeListener {
	if (p._parseListeners == nil){
		return make([]*ParseTreeListener)
	}
	return p._parseListeners
}

// Registers {@code listener} to receive events during the parsing process.
//
// <p>To support output-preserving grammar transformations (including but not
// limited to left-recursion removal, automated left-factoring, and
// optimized code generation), calls to listener methods during the parse
// may differ substantially from calls made by
// {@link ParseTreeWalker//DEFAULT} used after the parse is complete. In
// particular, rule entry and exit events may occur in a different order
// during the parse than after the parser. In addition, calls to certain
// rule entry methods may be omitted.</p>
//
// <p>With the following specific exceptions, calls to listener events are
// <em>deterministic</em>, i.e. for identical input the calls to listener
// methods will be the same.</p>
//
// <ul>
// <li>Alterations to the grammar used to generate code may change the
// behavior of the listener calls.</li>
// <li>Alterations to the command line options passed to ANTLR 4 when
// generating the parser may change the behavior of the listener calls.</li>
// <li>Changing the version of the ANTLR Tool used to generate the parser
// may change the behavior of the listener calls.</li>
// </ul>
//
// @param listener the listener to add
//
// @panics nilPointerException if {@code} listener is {@code nil}
//
func (p *Parser) addParseListener(listener *ParseTreeListener) {
	if (listener == nil) {
		panic("listener")
	}
	if (p._parseListeners == nil) {
		p._parseListeners = new([]ParseTreeListener)
	}
	p._parseListeners = append(p._parseListeners, listener)
}

//
// Remove {@code listener} from the list of parse listeners.
//
// <p>If {@code listener} is {@code nil} or has not been added as a parse
// listener, p.method does nothing.</p>
// @param listener the listener to remove
//
func (p *Parser) removeParseListener(listener *ParseTreeListener) {
	panic("Not implemented!")
//	if (p._parseListeners != nil) {
//		var idx = p._parseListeners.indexOf(listener)
//		if (idx >= 0) {
//			p._parseListeners.splice(idx, 1)
//		}
//		if (len(p._parseListeners) == 0) {
//			p._parseListeners = nil
//		}
//	}
}

// Remove all parse listeners.
func (p *Parser) removeParseListeners() {
	p._parseListeners = nil
}

// Notify any parse listeners of an enter rule event.
func (p *Parser) triggerEnterRuleEvent() {
	if (p._parseListeners != nil) {
        var ctx = p._ctx
		for _,listener := range p._parseListeners {
			(*listener).enterEveryRule(ctx)
			ctx.enterRule(listener)
		}
	}
}

//
// Notify any parse listeners of an exit rule event.
//
// @see //addParseListener
//
func (p *Parser) triggerExitRuleEvent() {
	if (p._parseListeners != nil) {
		// reverse order walk of listeners
        ctx := p._ctx
		l := len(p._parseListeners) - 1

		for i := range p._parseListeners {
			listener := p._parseListeners[l-i]
			ctx.exitRule(listener)
			(*listener).exitEveryRule(ctx)
		}
	}
}

func (this *Parser) getATN() *ATN {
	return this._interp.atn
}

func (p *Parser) getTokenFactory() *TokenFactory {
	return (*p._input.getTokenSource()).getTokenFactory()
}

// Tell our token source and error strategy about a Newway to create tokens.//
func (p *Parser) setTokenFactory(factory *TokenFactory) {
	(*p._input.getTokenSource()).setTokenFactory( factory )
}

// The ATN with bypass alternatives is expensive to create so we create it
// lazily.
//
// @panics UnsupportedOperationException if the current parser does not
// implement the {@link //getSerializedATN()} method.
//
func (p *Parser) getATNWithBypassAlts() {

	// TODO
	panic("Not implemented!")

//	var serializedAtn = p.getSerializedATN()
//	if (serializedAtn == nil) {
//		panic("The current parser does not support an ATN with bypass alternatives.")
//	}
//	var result = p.bypassAltsAtnCache[serializedAtn]
//	if (result == nil) {
//		var deserializationOptions = NewATNDeserializationOptions(nil)
//		deserializationOptions.generateRuleBypassTransitions = true
//		result = NewATNDeserializer(deserializationOptions).deserialize(serializedAtn)
//		p.bypassAltsAtnCache[serializedAtn] = result
//	}
//	return result
}

// The preferred method of getting a tree pattern. For example, here's a
// sample use:
//
// <pre>
// ParseTree t = parser.expr()
// ParseTreePattern p = parser.compileParseTreePattern("&ltID&gt+0",
// MyParser.RULE_expr)
// ParseTreeMatch m = p.match(t)
// String id = m.get("ID")
// </pre>

func (p *Parser) compileParseTreePattern(pattern, patternRuleIndex, lexer *Lexer) {

	if (lexer == nil) {
		if (p.getTokenStream() != nil) {
			var tokenSource = (*p.getTokenStream()).getTokenSource()
			if _, ok := tokenSource.(Lexer); ok {
				lexer = tokenSource
			}
		}
	}
	if (lexer == nil) {
		panic("Parser can't discover a lexer to use")
	}

	panic("NewParseTreePatternMatcher not implemented!")
//	var m = NewParseTreePatternMatcher(lexer, p)
//	return m.compile(pattern, patternRuleIndex)
}

func (p *Parser) getInputStream() *InputStream {
	return p.getTokenStream()
}

func (p *Parser) setInputStream(input *TokenStream) {
	p.setTokenStream(input)
}

func (p *Parser) getTokenStream() *TokenStream {
	return p._input
}

// Set the token stream and reset the parser.//
func (p *Parser) setTokenStream(input *TokenStream) {
	p._input = nil
	p.reset()
	p._input = input
}

// Match needs to return the current input symbol, which gets put
// into the label for the associated token ref e.g., x=ID.
//
func (p *Parser) getCurrentToken() *Token {
	return p._input.LT(1)
}

func (p *Parser) notifyErrorListeners(msg string, offendingToken *Token, err *RecognitionException) {
	offendingToken = offendingToken || nil
	err = err || nil
	if (offendingToken == nil) {
		offendingToken = p.getCurrentToken()
	}
	p._syntaxErrors += 1
	var line = offendingToken.line
	var column = offendingToken.column
	listener := p.getErrorListenerDispatch()
	listener.syntaxError(p, offendingToken, line, column, msg, err)
}

//
// Consume and return the {@linkplain //getCurrentToken current symbol}.
//
// <p>E.g., given the following input with {@code A} being the current
// lookahead symbol, p.func moves the cursor to {@code B} and returns
// {@code A}.</p>
//
// <pre>
// A B
// ^
// </pre>
//
// If the parser is not in error recovery mode, the consumed symbol is added
// to the parse tree using {@link ParserRuleContext//addChild(Token)}, and
// {@link ParseTreeListener//visitTerminal} is called on any parse listeners.
// If the parser <em>is</em> in error recovery mode, the consumed symbol is
// added to the parse tree using
// {@link ParserRuleContext//addErrorNode(Token)}, and
// {@link ParseTreeListener//visitErrorNode} is called on any parse
// listeners.
//
func (p *Parser) consume() {
	var o = p.getCurrentToken()
	if (o.tokenType != TokenEOF) {
		p.getInputStream().consume()
	}
	var hasListener = p._parseListeners != nil && len(p._parseListeners) > 0
	if (p.buildParseTrees || hasListener) {
		var node *ErrorNodeImpl
		if (p._errHandler.inErrorRecoveryMode(p)) {
			node = p._ctx.addErrorNode(o)
		} else {
			node = p._ctx.addTokenNode(o)
		}
        node.invokingState = p.state
		if (hasListener) {
			for _, l := range p._parseListeners {
				l.visitTerminal(node)
			}
		}
	}
	return o
}

func (p *Parser) addContextToParseTree() {
	// add current context to parent if we have a parent
	if (p._ctx.parentCtx != nil) {
		p._ctx.parentCtx.children = append(p._ctx.parentCtx.children, p._ctx)
	}
}

// Always called by generated parsers upon entry to a rule. Access field
// {@link //_ctx} get the current context.

func (p *Parser) enterRule(localctx *ParserRuleContext, state, ruleIndex int) {
	p.state = state
	p._ctx = localctx
	p._ctx.start = p._input.LT(1)
	if (p.buildParseTrees) {
		p.addContextToParseTree()
	}
	if (p._parseListeners != nil) {
		p.triggerEnterRuleEvent()
	}
}

func (p *Parser) exitRule() {
	p._ctx.stop = p._input.LT(-1)
	// trigger event on _ctx, before it reverts to parent
	if (p._parseListeners != nil) {
		p.triggerExitRuleEvent()
	}
	p.state = p._ctx.invokingState
	p._ctx = p._ctx.parentCtx
}

func (p *Parser) enterOuterAlt(localctx *ParserRuleContext, altNum int) {
	// if we have Newlocalctx, make sure we replace existing ctx
	// that is previous child of parse tree
	if (p.buildParseTrees && p._ctx != localctx) {
		if (p._ctx.parentCtx != nil) {
			p._ctx.parentCtx.removeLastChild()
			p._ctx.parentCtx.addChild(localctx)
		}
	}
	p._ctx = localctx
}

// Get the precedence level for the top-most precedence rule.
//
// @return The precedence level for the top-most precedence rule, or -1 if
// the parser context is not nested within a precedence rule.

func (p *Parser) getPrecedence() int {
	if ( len(p._precedenceStack) == 0) {
		return -1
	} else {
		return p._precedenceStack[ len(p._precedenceStack) -1]
	}
}

func (p *Parser) enterRecursionRule(localctx *ParserRuleContext, state, ruleIndex, precedence int) {
	p.state = state
	p._precedenceStack.Push(precedence)
	p._ctx = localctx
	p._ctx.start = p._input.LT(1)
	if (p._parseListeners != nil) {
		p.triggerEnterRuleEvent() // simulates rule entry for
										// left-recursive rules
	}
}

//
// Like {@link //enterRule} but for recursive rules.

func (p *Parser) pushNewRecursionContext(localctx *ParserRuleContext, state, ruleIndex int) {
	var previous = p._ctx
	previous.parentCtx = localctx
	previous.invokingState = state
	previous.stop = p._input.LT(-1)

	p._ctx = localctx
	p._ctx.start = previous.start
	if (p.buildParseTrees) {
		p._ctx.addChild(previous)
	}
	if (p._parseListeners != nil) {
		p.triggerEnterRuleEvent() // simulates rule entry for
										// left-recursive rules
	}
}

func (p *Parser) unrollRecursionContexts(parentCtx *ParserRuleContext) {
	p._precedenceStack.Pop()
	p._ctx.stop = p._input.LT(-1)
	var retCtx = p._ctx // save current ctx (return value)
	// unroll so _ctx is as it was before call to recursive method
	if (p._parseListeners != nil) {
		for (p._ctx != parentCtx) {
			p.triggerExitRuleEvent()
			p._ctx = p._ctx.parentCtx
		}
	} else {
		p._ctx = parentCtx
	}
	// hook into tree
	retCtx.parentCtx = parentCtx
	if (p.buildParseTrees && parentCtx != nil) {
		// add return ctx into invoking rule's tree
		parentCtx.addChild(retCtx)
	}
}

func (p *Parser) getInvokingContext(ruleIndex int) *ParserRuleContext {
	var ctx = p._ctx
	for (ctx != nil) {
		if (ctx.ruleIndex == ruleIndex) {
			return ctx
		}
		ctx = ctx.parentCtx
	}
	return nil
}

func (p *Parser) precpred(localctx, precedence int) {
	return precedence >= p._precedenceStack[ len(p._precedenceStack) -1]
}

func (p *Parser) inContext(context *ParserRuleContext) bool {
	// TODO: useful in parser?
	return false
}

//
// Checks whether or not {@code symbol} can follow the current state in the
// ATN. The behavior of p.method is equivalent to the following, but is
// implemented such that the complete context-sensitive follow set does not
// need to be explicitly constructed.
//
// <pre>
// return getExpectedTokens().contains(symbol)
// </pre>
//
// @param symbol the symbol type to check
// @return {@code true} if {@code symbol} can follow the current state in
// the ATN, otherwise {@code false}.

func (p *Parser) isExpectedToken(symbol *Token) bool {
	var atn *ATN = p._interp.atn
	var ctx = p._ctx
	var s = atn.states[p.state]
	var following = atn.nextTokens(s,nil)
	if (following.contains(symbol)) {
		return true
	}
	if (!following.contains(TokenEpsilon)) {
		return false
	}
	for (ctx != nil && ctx.invokingState >= 0 && following.contains(TokenEpsilon)) {
		var invokingState = atn.states[ctx.invokingState]
		var rt = invokingState.transitions[0]
		following = atn.nextTokens(rt.(*RuleTransition).followState,nil)
		if (following.contains(symbol)) {
			return true
		}
		ctx = ctx.parentCtx
	}
	if (following.contains(TokenEpsilon) && symbol == TokenEOF) {
		return true
	} else {
		return false
	}
}

// Computes the set of input symbols which could follow the current parser
// state and context, as given by {@link //getState} and {@link //getContext},
// respectively.
//
// @see ATN//getExpectedTokens(int, RuleContext)
//
func (p *Parser) getExpectedTokens() *IntervalSet {
	return p._interp.atn.getExpectedTokens(p.state, p._ctx)
}

func (p *Parser) getExpectedTokensWithinCurrentRule() *IntervalSet {
	var atn = p._interp.atn
	var s = atn.states[p.state]
	return atn.nextTokens(s,nil)
}

// Get a rule's index (i.e., {@code RULE_ruleName} field) or -1 if not found.//
func (p *Parser) getRuleIndex(ruleName string) int {
	var ruleIndex = p.getRuleIndexMap()[ruleName]
	if (ruleIndex != nil) {
		return ruleIndex
	} else {
		return -1
	}
}

// Return List&ltString&gt of the rule names in your parser instance
// leading up to a call to the current rule. You could override if
// you want more details such as the file/line info of where
// in the ATN a rule is invoked.
//
// this very useful for error messages.

func (this *Parser) getRuleInvocationStack(p *ParserRuleContext) []string {
	if (p == nil) {
		p = this._ctx;
	}
	var stack = make([]string)
	for (p != nil) {
		// compute what follows who invoked us
		var ruleIndex = p.ruleIndex;
		if (ruleIndex < 0) {
			stack = append(stack, "n/a")
		} else {
			stack = append(stack, this.getRuleNames()[ruleIndex]);
		}
		p = p.parentCtx;
	}
	return stack;
};

// For debugging and other purposes.//
func (p *Parser) getDFAStrings() {
	panic("dumpDFA Not implemented!")
//	return p._interp.decisionToDFA.toString()
}

// For debugging and other purposes.//
func (p *Parser) dumpDFA() {
	panic("dumpDFA Not implemented!")

//	var seenOne = false
//	for i := 0; i < p._interp.decisionToDFA.length; i++ {
//		var dfa = p._interp.decisionToDFA[i]
//		if ( len(dfa.states) > 0) {
//			if (seenOne) {
//				fmt.Println()
//			}
//			p.printer.println("Decision " + dfa.decision + ":")
//			p.printer.print(dfa.toString(p.literalNames, p.symbolicNames))
//			seenOne = true
//		}
//	}
}

/*
"			printer = function() {\r\n" +
"				p.println = function(s) { document.getElementById('output') += s + '\\n' }\r\n" +
"				p.print = function(s) { document.getElementById('output') += s }\r\n" +
"			}\r\n" +
*/

func (p *Parser) getSourceName() string {
	return p._input.getSourceName()
}

// During a parse is sometimes useful to listen in on the rule entry and exit
// events as well as token matches. p.is for quick and dirty debugging.
//
func (p *Parser) setTrace(trace *TraceListener) {
	if (trace == nil) {
		p.removeParseListener(p._tracer)
		p._tracer = nil
	} else {
		if (p._tracer != nil) {
			p.removeParseListener(p._tracer)
		}
		p._tracer = NewTraceListener(p)
		p.addParseListener(p._tracer)
	}
}

