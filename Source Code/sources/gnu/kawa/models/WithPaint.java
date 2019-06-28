package gnu.kawa.models;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class WithPaint implements Paintable {
    Paint paint;
    Paintable paintable;

    public WithPaint(Paintable paintable, Paint paint) {
        this.paintable = paintable;
        this.paint = paint;
    }

    public void paint(Graphics2D graphics) {
        Paint saved = graphics.getPaint();
        try {
            graphics.setPaint(this.paint);
            this.paintable.paint(graphics);
        } finally {
            graphics.setPaint(saved);
        }
    }

    public Rectangle2D getBounds2D() {
        return this.paintable.getBounds2D();
    }

    public Paintable transform(AffineTransform tr) {
        return new WithPaint(this.paintable.transform(tr), this.paint);
    }
}
