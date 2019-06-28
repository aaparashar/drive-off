package gnu.q2.lang;

import gnu.expr.Keyword;
import gnu.expr.QuoteExp;
import gnu.kawa.lispexpr.LispReader;
import gnu.kawa.xml.MakeAttribute;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.lists.PairWithPosition;
import gnu.lists.Sequence;
import gnu.mapping.InPort;
import gnu.text.SourceMessages;
import gnu.text.SyntaxException;
import java.io.IOException;
import kawa.standard.begin;

public class Q2Read extends LispReader {
    int curIndentation;
    int expressionStartColumn;
    String expressionStartFile;
    int expressionStartLine;

    void init() {
        ((InPort) this.port).readState = ' ';
    }

    public Q2Read(InPort port) {
        super(port);
        init();
    }

    public Q2Read(InPort port, SourceMessages messages) {
        super(port, messages);
        init();
    }

    int skipIndentation() throws IOException, SyntaxException {
        int numTabs = 0;
        int numSpaces = 0;
        int ch = this.port.read();
        while (ch == 9) {
            numTabs++;
            ch = this.port.read();
        }
        while (ch == 32) {
            numSpaces++;
            ch = this.port.read();
        }
        if (ch < 0) {
            return -1;
        }
        this.port.unread();
        return (numTabs << 16) + numSpaces;
    }

    Object readIndentCommand() throws IOException, SyntaxException {
        int startIndentation = this.curIndentation;
        LList rresult = LList.Empty;
        LList obj = LList.Empty;
        while (true) {
            int ch = read();
            if (ch < 0) {
                break;
            } else if (!(ch == 32 || ch == 9)) {
                unread();
                if (ch == 41) {
                    break;
                } else if (ch != 13 && ch != 10) {
                    rresult = makePair(readObject(), rresult, this.port.getLineNumber(), this.port.getColumnNumber());
                }
            }
        }
        if (!singleLine()) {
            ch = read();
            this.port.mark(Integer.MAX_VALUE);
            int subIndentation = skipIndentation();
            LList qresult = LList.Empty;
            this.curIndentation = subIndentation;
            while (this.curIndentation != -1 && subIndentation == this.curIndentation) {
                int comparedIndent = Q2.compareIndentation(subIndentation, startIndentation);
                if (comparedIndent != Integer.MIN_VALUE) {
                    if (comparedIndent != -1 && comparedIndent != 1) {
                        if (comparedIndent <= 0) {
                            break;
                        }
                        qresult = makePair(readIndentCommand(), qresult, this.port.getLineNumber(), this.port.getColumnNumber());
                    } else {
                        error('e', "indentation must differ by 2 or more");
                        break;
                    }
                }
                error('e', "cannot compare indentation - mix of tabs and spaces");
                break;
            }
            if (qresult != LList.Empty) {
                rresult = new Pair(new Pair(begin.begin, LList.reverseInPlace(qresult)), rresult);
            }
        }
        return LList.reverseInPlace(rresult);
    }

    boolean singleLine() {
        return this.interactive && this.nesting == 0;
    }

    public Object readCommand() throws IOException, SyntaxException {
        int indent = skipIndentation();
        if (indent < 0) {
            return Sequence.eofValue;
        }
        this.curIndentation = indent;
        Object result = readIndentCommand();
        if (this.interactive) {
            return result;
        }
        this.port.reset();
        return result;
    }

    public Object readCommand(boolean forceList) throws IOException, SyntaxException {
        int line = this.port.getLineNumber();
        int startColumn = this.port.getColumnNumber();
        int lastColumn = startColumn;
        Object obj = LList.Empty;
        PairWithPosition pair = null;
        PairWithPosition last = null;
        while (true) {
            int ch = read();
            if (ch >= 0) {
                if (!(ch == 32 || ch == 9)) {
                    unread();
                    if (ch == 41) {
                        break;
                    }
                    line = this.port.getLineNumber();
                    int column = this.port.getColumnNumber();
                    do {
                        if (ch != 13 && ch != 10) {
                            break;
                        } else if (singleLine()) {
                            return obj;
                        } else {
                            ch = read();
                            skipIndentation();
                            column = this.port.getColumnNumber();
                            ch = peek();
                        }
                    } while (column > startColumn);
                    if (column <= startColumn && last != null) {
                        break;
                    }
                    Object next;
                    if (column == lastColumn && last != null) {
                        next = readCommand();
                    } else if (column >= lastColumn || last == null) {
                        next = readObject();
                    } else {
                        PairWithPosition np;
                        PairWithPosition p = pair;
                        while (true) {
                            LList n = p.getCdr();
                            if (n == LList.Empty) {
                                break;
                            }
                            np = (PairWithPosition) n;
                            int pColumn = np.getColumnNumber() - 1;
                            if (pColumn >= column) {
                                if (pColumn > column) {
                                    error('e', "some tokens on previous line indented more than current line");
                                }
                                n = np.getCdr();
                                if (n != LList.Empty) {
                                    if (((PairWithPosition) n).getColumnNumber() - 1 != column) {
                                        break;
                                    }
                                    p = (PairWithPosition) n;
                                } else {
                                    break;
                                }
                            }
                            p = np;
                        }
                        last = (PairWithPosition) makePair(np, this.port.getLineNumber(), column);
                        p.setCdrBackdoor(last);
                        next = readCommand();
                    }
                    if (next == Sequence.eofValue) {
                        break;
                    }
                    lastColumn = column;
                    PairWithPosition cur = PairWithPosition.make(next, LList.Empty, this.port.getName(), line + 1, column + 1);
                    if (last == null) {
                        pair = cur;
                        obj = cur;
                    } else if (last.getCar() instanceof Keyword) {
                        QuoteExp name = new QuoteExp(((Keyword) last.getCar()).getName());
                        last.setCar(new PairWithPosition(last, MakeAttribute.makeAttribute, new PairWithPosition(last, name, cur)));
                    } else {
                        last.setCdrBackdoor(cur);
                    }
                    last = cur;
                }
            } else {
                break;
            }
        }
        if (forceList) {
            return obj;
        }
        if (obj == last) {
            return last.getCar();
        }
        if (last == null) {
            return QuoteExp.voidExp;
        }
        return obj;
    }

    public static Object readObject(InPort port) throws IOException, SyntaxException {
        return new Q2Read(port).readObject();
    }

    void saveExpressionStartPosition() {
        this.expressionStartFile = this.port.getName();
        this.expressionStartLine = this.port.getLineNumber();
        this.expressionStartColumn = this.port.getColumnNumber();
    }
}
