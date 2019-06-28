package gnu.kawa.models;

import java.awt.Composite;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class WithComposite implements Paintable {
    Composite[] composite;
    Paintable[] paintable;

    public static WithComposite make(Paintable paintable, Composite composite) {
        WithComposite comp = new WithComposite();
        comp.paintable = new Paintable[]{paintable};
        comp.composite = new Composite[]{composite};
        return comp;
    }

    public static WithComposite make(Paintable[] paintable, Composite[] composite) {
        WithComposite comp = new WithComposite();
        comp.paintable = paintable;
        comp.composite = composite;
        return comp;
    }

    public static WithComposite make(Object[] arguments) {
        int n = 0;
        int i = arguments.length;
        while (true) {
            i--;
            if (i < 0) {
                break;
            } else if (arguments[i] instanceof Paintable) {
                n++;
            }
        }
        Paintable[] paintable = new Paintable[n];
        Composite[] composite = new Composite[n];
        Composite comp = null;
        int j = 0;
        for (i = 0; i < arguments.length; i++) {
            Composite arg = arguments[i];
            if (arg instanceof Paintable) {
                paintable[j] = (Paintable) arguments[i];
                composite[j] = comp;
                j++;
            } else {
                comp = arg;
            }
        }
        return make(paintable, composite);
    }

    public void paint(java.awt.Graphics2D r7) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Assign predecessor not found for B:4:0x000b from B:21:?
	at jadx.core.dex.visitors.ssa.EliminatePhiNodes.replaceMerge(EliminatePhiNodes.java:102)
	at jadx.core.dex.visitors.ssa.EliminatePhiNodes.replaceMergeInstructions(EliminatePhiNodes.java:68)
	at jadx.core.dex.visitors.ssa.EliminatePhiNodes.visit(EliminatePhiNodes.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/815992954.run(Unknown Source)
*/
        /*
        r6 = this;
        r4 = r7.getComposite();
        r3 = r4;
        r5 = r6.paintable;	 Catch:{ all -> 0x0027 }
        r2 = r5.length;	 Catch:{ all -> 0x0027 }
        r1 = 0;	 Catch:{ all -> 0x0027 }
    L_0x0009:
        if (r1 >= r2) goto L_0x0021;	 Catch:{ all -> 0x0027 }
    L_0x000b:
        r5 = r6.composite;	 Catch:{ all -> 0x0027 }
        r0 = r5[r1];	 Catch:{ all -> 0x0027 }
        if (r0 == 0) goto L_0x0017;	 Catch:{ all -> 0x0027 }
    L_0x0011:
        if (r0 == r3) goto L_0x0017;	 Catch:{ all -> 0x0027 }
    L_0x0013:
        r7.setComposite(r0);	 Catch:{ all -> 0x0027 }
        r3 = r0;	 Catch:{ all -> 0x0027 }
    L_0x0017:
        r5 = r6.paintable;	 Catch:{ all -> 0x0027 }
        r5 = r5[r1];	 Catch:{ all -> 0x0027 }
        r5.paint(r7);	 Catch:{ all -> 0x0027 }
        r1 = r1 + 1;
        goto L_0x0009;
    L_0x0021:
        if (r3 == r4) goto L_0x0026;
    L_0x0023:
        r7.setComposite(r4);
    L_0x0026:
        return;
    L_0x0027:
        r5 = move-exception;
        if (r3 == r4) goto L_0x002d;
    L_0x002a:
        r7.setComposite(r4);
    L_0x002d:
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.models.WithComposite.paint(java.awt.Graphics2D):void");
    }

    public Rectangle2D getBounds2D() {
        int n = this.paintable.length;
        if (n == 0) {
            return null;
        }
        Rectangle2D bounds = this.paintable[0].getBounds2D();
        for (int i = 1; i < n; i++) {
            bounds = bounds.createUnion(this.paintable[i].getBounds2D());
        }
        return bounds;
    }

    public Paintable transform(AffineTransform tr) {
        int n = this.paintable.length;
        Paintable[] transformed = new Paintable[n];
        for (int i = 0; i < n; i++) {
            transformed[i] = this.paintable[i].transform(tr);
        }
        return make(transformed, this.composite);
    }
}
