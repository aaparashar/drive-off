package gnu.q2.lang;

import gnu.bytecode.Type;
import gnu.expr.Special;
import gnu.kawa.reflect.Invoke;
import gnu.lists.Consumable;
import gnu.mapping.CallContext;
import gnu.mapping.MethodProc;
import gnu.mapping.Procedure;
import gnu.mapping.Values;
import java.util.Vector;

public class Q2Apply extends MethodProc {
    public static Q2Apply q2Apply = new Q2Apply();

    public void apply(CallContext ctx) throws Throwable {
        Special endMarker = Special.dfault;
        Special arg = ctx.getNextArg(endMarker);
        if ((arg instanceof Procedure) || (arg instanceof Type) || (arg instanceof Class)) {
            Procedure proc;
            Vector vec = new Vector();
            if (arg instanceof Procedure) {
                proc = (Procedure) arg;
            } else {
                vec.add(arg);
                proc = Invoke.make;
            }
            while (true) {
                arg = ctx.getNextArg(endMarker);
                if (arg == endMarker) {
                    break;
                } else if (arg instanceof Values) {
                    Object[] vals = ((Values) arg).getValues();
                    for (Object add : vals) {
                        vec.add(add);
                    }
                } else {
                    vec.add(arg);
                }
            }
            Object arg2 = proc.applyN(vec.toArray());
            if (arg2 instanceof Consumable) {
                ((Consumable) arg2).consume(ctx.consumer);
                return;
            } else {
                ctx.writeValue(arg2);
                return;
            }
        }
        while (arg != endMarker) {
            if (arg instanceof Consumable) {
                ((Consumable) arg).consume(ctx.consumer);
            } else {
                ctx.writeValue(arg);
            }
            arg = ctx.getNextArg(endMarker);
        }
    }
}
