package gnu.q2.lang;

import gnu.kawa.lispexpr.ReaderDispatchMisc;
import gnu.text.Lexer;
import gnu.text.SyntaxException;
import java.io.IOException;

/* compiled from: Q2Read */
class Q2ReaderParens extends ReaderDispatchMisc {
    Q2ReaderParens() {
    }

    public Object read(Lexer in, int ch, int count) throws IOException, SyntaxException {
        Q2Read reader = (Q2Read) in;
        char saveReadState = reader.pushNesting('(');
        try {
            Object result = reader.readCommand(true);
            if (reader.getPort().read() != 41) {
                reader.error("missing ')'");
            }
            reader.popNesting(saveReadState);
            return result;
        } catch (Throwable th) {
            reader.popNesting(saveReadState);
        }
    }
}
