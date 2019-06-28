package gnu.kawa.android;

import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.kawa.lispexpr.LispLanguage;
import gnu.lists.Consumer;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.lists.PairWithPosition;
import gnu.mapping.CallContext;
import gnu.mapping.SimpleSymbol;
import kawa.lang.Macro;
import kawa.lang.Quote;
import kawa.lang.SyntaxPattern;
import kawa.lang.SyntaxTemplate;
import kawa.lang.TemplateScope;
import kawa.lib.lists;
import kawa.standard.syntax_case;

/* compiled from: defs.scm */
public class defs extends ModuleBody {
    public static final ModuleMethod $Pcprocess$Mnactivity;
    public static final defs $instance = new defs();
    static final SyntaxPattern Lit0 = new SyntaxPattern("<\f\u0002\r\u0007\u0000\b\b\u000b", new Object[]{(SimpleSymbol) new SimpleSymbol("on-create").readResolve()}, 2);
    static final SyntaxTemplate Lit1 = new SyntaxTemplate("\u0003\u0000", "\u0011\u0018\u0004\u0011\u0018\f\u0011\u0018\u0014\u0011\u0018\u001c\b\u0005\u0003", new Object[]{PairWithPosition.make(Lit19, PairWithPosition.make(PairWithPosition.make(Lit20, PairWithPosition.make(Lit17, PairWithPosition.make(Lit22, LList.Empty, "defs.scm", 16433), "defs.scm", 16430), "defs.scm", 16410), LList.Empty, "defs.scm", 16410), "defs.scm", 16400), Lit17, Lit23, PairWithPosition.make(Lit24, PairWithPosition.make(Lit18, PairWithPosition.make(PairWithPosition.make(Lit21, LList.Empty, "defs.scm", 20526), PairWithPosition.make(PairWithPosition.make(Lit25, PairWithPosition.make(Lit19, LList.Empty, "defs.scm", 20534), "defs.scm", 20534), PairWithPosition.make(Lit20, LList.Empty, "defs.scm", 20543), "defs.scm", 20533), "defs.scm", 20526), "defs.scm", 20505), "defs.scm", 20489)}, 1);
    static final SimpleSymbol Lit10 = ((SimpleSymbol) new SimpleSymbol("%process-activity").readResolve());
    static final SimpleSymbol Lit11 = ((SimpleSymbol) new SimpleSymbol("activity").readResolve());
    static final SyntaxPattern Lit12 = new SyntaxPattern("\f\u0007\f\u000f\u0013", new Object[0], 3);
    static final SyntaxTemplate Lit13 = new SyntaxTemplate("\u0001\u0001\u0000", "\u0011\u0018\u0004\b\u000b", new Object[]{(SimpleSymbol) new SimpleSymbol("define-simple-class").readResolve()}, 0);
    static final SyntaxTemplate Lit14 = new SyntaxTemplate("\u0001\u0001\u0000", "\u0018\u0004", new Object[]{PairWithPosition.make(Lit18, LList.Empty, "defs.scm", 86048)}, 0);
    static final SyntaxTemplate Lit15 = new SyntaxTemplate("\u0001\u0001\u0000", "\u0012", new Object[0], 0);
    static final SyntaxTemplate Lit16 = new SyntaxTemplate("\u0001\u0001\u0000", "\u0010", new Object[0], 0);
    static final SimpleSymbol Lit17 = ((SimpleSymbol) new SimpleSymbol("::").readResolve());
    static final SimpleSymbol Lit18 = ((SimpleSymbol) new SimpleSymbol("android.app.Activity").readResolve());
    static final SimpleSymbol Lit19 = ((SimpleSymbol) new SimpleSymbol("onCreate").readResolve());
    static final SyntaxTemplate Lit2 = new SyntaxTemplate("\u0003\u0000", "\n", new Object[0], 0);
    static final SimpleSymbol Lit20 = ((SimpleSymbol) new SimpleSymbol("savedInstanceState").readResolve());
    static final SimpleSymbol Lit21 = ((SimpleSymbol) new SimpleSymbol("this").readResolve());
    static final SimpleSymbol Lit22 = ((SimpleSymbol) new SimpleSymbol("android.os.Bundle").readResolve());
    static final SimpleSymbol Lit23 = ((SimpleSymbol) new SimpleSymbol("void").readResolve());
    static final SimpleSymbol Lit24 = ((SimpleSymbol) new SimpleSymbol("invoke-special").readResolve());
    static final SimpleSymbol Lit25 = ((SimpleSymbol) new SimpleSymbol(LispLanguage.quote_sym).readResolve());
    static final SyntaxPattern Lit3 = new SyntaxPattern("T\f\u0002\r\u0007\u0000\b\u0016\f\u000f\b\u0013", new Object[]{(SimpleSymbol) new SimpleSymbol("on-create-view").readResolve()}, 3);
    static final SyntaxTemplate Lit4 = new SyntaxTemplate("\u0003\u0001\u0000", "\u0011\u0018\u0004\u0011\u0018\f\u0011\u0018\u0014\u0011\u0018\u001c\u0011\u0005\u0003\b\u0011\u0018$\b\u000b", new Object[]{PairWithPosition.make(Lit19, PairWithPosition.make(PairWithPosition.make(Lit20, PairWithPosition.make(Lit17, PairWithPosition.make(Lit22, LList.Empty, "defs.scm", 36913), "defs.scm", 36910), "defs.scm", 36890), LList.Empty, "defs.scm", 36890), "defs.scm", 36880), Lit17, Lit23, PairWithPosition.make(Lit24, PairWithPosition.make(Lit18, PairWithPosition.make(PairWithPosition.make(Lit21, LList.Empty, "defs.scm", 41006), PairWithPosition.make(PairWithPosition.make(Lit25, PairWithPosition.make(Lit19, LList.Empty, "defs.scm", 41014), "defs.scm", 41014), PairWithPosition.make(Lit20, LList.Empty, "defs.scm", 41023), "defs.scm", 41013), "defs.scm", 41006), "defs.scm", 40985), "defs.scm", 40969), PairWithPosition.make((SimpleSymbol) new SimpleSymbol("$lookup$").readResolve(), Pair.make(PairWithPosition.make(Lit21, LList.Empty, "defs.scm", 49162), Pair.make(Pair.make((SimpleSymbol) new SimpleSymbol(LispLanguage.quasiquote_sym).readResolve(), Pair.make((SimpleSymbol) new SimpleSymbol("setContentView").readResolve(), LList.Empty)), LList.Empty)), "defs.scm", 49162)}, 1);
    static final SyntaxTemplate Lit5 = new SyntaxTemplate("\u0003\u0001\u0000", "\u0012", new Object[0], 0);
    static final SyntaxPattern Lit6 = new SyntaxPattern("\f\u0007\u000b", new Object[0], 2);
    static final SyntaxTemplate Lit7 = new SyntaxTemplate("\u0001\u0000", "\u0003", new Object[0], 0);
    static final SyntaxTemplate Lit8 = new SyntaxTemplate("\u0001\u0000", "\n", new Object[0], 0);
    static final SyntaxPattern Lit9 = new SyntaxPattern("\b", new Object[0], 0);
    public static final Macro activity;

    public static Object $PcProcessActivity(Object form) {
        Object[] allocVars = SyntaxPattern.allocVars(3, null);
        if (Lit0.match(form, allocVars, 0)) {
            return lists.cons(Lit1.execute(allocVars, TemplateScope.make()), $PcProcessActivity(Lit2.execute(allocVars, TemplateScope.make())));
        } else if (Lit3.match(form, allocVars, 0)) {
            return lists.cons(Lit4.execute(allocVars, TemplateScope.make()), $PcProcessActivity(Lit5.execute(allocVars, TemplateScope.make())));
        } else if (!Lit6.match(form, allocVars, 0)) {
            return Lit9.match(form, allocVars, 0) ? LList.Empty : syntax_case.error("syntax-case", form);
        } else {
            return lists.cons(Lit7.execute(allocVars, TemplateScope.make()), $PcProcessActivity(Lit8.execute(allocVars, TemplateScope.make())));
        }
    }

    static {
        ModuleBody moduleBody = $instance;
        $Pcprocess$Mnactivity = new ModuleMethod(moduleBody, 1, Lit10, 4097);
        activity = Macro.make(Lit11, new ModuleMethod(moduleBody, 2, null, 4097), $instance);
        $instance.run();
    }

    public defs() {
        ModuleInfo.register(this);
    }

    public Object apply1(ModuleMethod moduleMethod, Object obj) {
        switch (moduleMethod.selector) {
            case 1:
                return $PcProcessActivity(obj);
            case 2:
                return lambda1(obj);
            default:
                return super.apply1(moduleMethod, obj);
        }
    }

    public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 1:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 2:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            default:
                return super.match1(moduleMethod, obj, callContext);
        }
    }

    public final void run(CallContext $ctx) {
        Consumer $result = $ctx.consumer;
    }

    static Object lambda1(Object form) {
        Object[] allocVars = SyntaxPattern.allocVars(3, null);
        if (!Lit12.match(form, allocVars, 0)) {
            return syntax_case.error("syntax-case", form);
        }
        TemplateScope make = TemplateScope.make();
        Object[] objArr = new Object[2];
        objArr[0] = Lit13.execute(allocVars, make);
        objArr[1] = Pair.make(Lit14.execute(allocVars, make), Quote.append$V(new Object[]{$PcProcessActivity(Lit15.execute(allocVars, make)), Lit16.execute(allocVars, make)}));
        return Quote.append$V(objArr);
    }
}
