package gnu.kawa.models;

import java.io.Serializable;

public class Column extends Box implements Viewable, Serializable {
    public int getAxis() {
        return 1;
    }
}
