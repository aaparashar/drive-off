package gnu.kawa.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class PreProcess {
    static final String JAVA4_FEATURES = "+JAVA2 +use:java.util.IdentityHashMap +use:java.lang.CharSequence +use:java.lang.Throwable.getCause +use:java.net.URI +use:java.util.regex +SAX2 +use:java.nio";
    static final String JAVA5_FEATURES = "+JAVA5 +JAVA2 +use:java.util.IdentityHashMap +use:java.lang.CharSequence +use:java.lang.Throwable.getCause +use:java.net.URI +use:java.util.regex +SAX2 +use:java.nio +use:org.w3c.dom.Node +use:javax.xml.transform +JAXP-1.3 -JAXP-QName";
    static final String NO_JAVA4_FEATURES = "-JAVA5 -use:java.util.IdentityHashMap -use:java.lang.CharSequence -use:java.lang.Throwable.getCause -use:java.net.URI -use:java.util.regex -use:org.w3c.dom.Node -JAXP-1.3 -use:javax.xml.transform -JAVA5 -JAVA6 -JAVA6COMPAT5 -JAXP-QName -use:java.text.Normalizer -SAX2 -use:java.nio -Android";
    static final String NO_JAVA6_FEATURES = "-JAVA6 -JAVA7 -use:java.dyn -use:java.text.Normalizer";
    static String[] version_features = new String[]{"java1", "-JAVA2 -JAVA5 -use:java.util.IdentityHashMap -use:java.lang.CharSequence -use:java.lang.Throwable.getCause -use:java.net.URI -use:java.util.regex -use:org.w3c.dom.Node -JAXP-1.3 -use:javax.xml.transform -JAVA5 -JAVA6 -JAVA6COMPAT5 -JAXP-QName -use:java.text.Normalizer -SAX2 -use:java.nio -Android -JAVA6 -JAVA7 -use:java.dyn -use:java.text.Normalizer", "java2", "+JAVA2 -JAVA5 -use:java.util.IdentityHashMap -use:java.lang.CharSequence -use:java.lang.Throwable.getCause -use:java.net.URI -use:java.util.regex -use:org.w3c.dom.Node -JAXP-1.3 -use:javax.xml.transform -JAVA5 -JAVA6 -JAVA6COMPAT5 -JAXP-QName -use:java.text.Normalizer -SAX2 -use:java.nio -Android -JAVA6 -JAVA7 -use:java.dyn -use:java.text.Normalizer", "java4", "-JAVA5 +JAVA2 +use:java.util.IdentityHashMap +use:java.lang.CharSequence +use:java.lang.Throwable.getCause +use:java.net.URI +use:java.util.regex +SAX2 +use:java.nio -use:org.w3c.dom.Node -JAXP-1.3 -use:javax.xml.transform -JAXP-QName -JAVA6COMPAT5 -Android -JAVA6 -JAVA7 -use:java.dyn -use:java.text.Normalizer", "java4x", "-JAVA5 +JAVA2 +use:java.util.IdentityHashMap +use:java.lang.CharSequence +use:java.lang.Throwable.getCause +use:java.net.URI +use:java.util.regex +SAX2 +use:java.nio +use:org.w3c.dom.Node +JAXP-1.3 +use:javax.xml.transform -JAXP-QName -JAVA6COMPAT5 -Android -JAVA6 -JAVA7 -use:java.dyn -use:java.text.Normalizer", "java5", "+JAVA5 +JAVA2 +use:java.util.IdentityHashMap +use:java.lang.CharSequence +use:java.lang.Throwable.getCause +use:java.net.URI +use:java.util.regex +SAX2 +use:java.nio +use:org.w3c.dom.Node +use:javax.xml.transform +JAXP-1.3 -JAXP-QName -JAVA6COMPAT5 -Android -JAVA6 -JAVA7 -use:java.dyn -use:java.text.Normalizer", "java6compat5", "+JAVA5 +JAVA2 +use:java.util.IdentityHashMap +use:java.lang.CharSequence +use:java.lang.Throwable.getCause +use:java.net.URI +use:java.util.regex +SAX2 +use:java.nio +use:org.w3c.dom.Node +use:javax.xml.transform +JAXP-1.3 -JAXP-QName -JAVA6 -JAVA7 +JAVA6COMPAT5 +use:java.text.Normalizer -use:java.dyn -Android", "java6", "+JAVA5 +JAVA2 +use:java.util.IdentityHashMap +use:java.lang.CharSequence +use:java.lang.Throwable.getCause +use:java.net.URI +use:java.util.regex +SAX2 +use:java.nio +use:org.w3c.dom.Node +use:javax.xml.transform +JAXP-1.3 -JAXP-QName +JAVA6 -JAVA7 -JAVA6COMPAT5 +use:java.text.Normalizer -use:java.dyn -Android", "java7", "+JAVA5 +JAVA2 +use:java.util.IdentityHashMap +use:java.lang.CharSequence +use:java.lang.Throwable.getCause +use:java.net.URI +use:java.util.regex +SAX2 +use:java.nio +use:org.w3c.dom.Node +use:javax.xml.transform +JAXP-1.3 -JAXP-QName +JAVA6 +JAVA7 -JAVA6COMPAT5 +use:java.text.Normalizer +use:java.dyn -Android", "android", "+JAVA5 +JAVA2 +use:java.util.IdentityHashMap +use:java.lang.CharSequence +use:java.lang.Throwable.getCause +use:java.net.URI +use:java.util.regex +SAX2 +use:java.nio +use:org.w3c.dom.Node +JAXP-1.3 -JAXP-QName -use:javax.xml.transform -JAVA6 -JAVA6COMPAT5 +Android -JAVA6 -JAVA7 -use:java.dyn -use:java.text.Normalizer"};
    String filename;
    Hashtable keywords = new Hashtable();
    int lineno;
    byte[] resultBuffer;
    int resultLength;

    void error(String str) {
        System.err.println(this.filename + ':' + this.lineno + ": " + str);
        System.exit(-1);
    }

    public void filter(String str) throws Throwable {
        if (filter(str, new BufferedInputStream(new FileInputStream(str)))) {
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            fileOutputStream.write(this.resultBuffer, 0, this.resultLength);
            fileOutputStream.close();
            System.err.println("Pre-processed " + str);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean filter(java.lang.String r20, java.io.BufferedInputStream r21) throws java.lang.Throwable {
        /*
        r19 = this;
        r0 = r20;
        r1 = r19;
        r1.filename = r0;
        r13 = 0;
        r2 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
        r12 = new byte[r2];
        r11 = 0;
        r10 = 0;
        r5 = -1;
        r3 = 0;
        r2 = 1;
        r0 = r19;
        r0.lineno = r2;
        r8 = -1;
        r7 = 0;
        r9 = 0;
        r6 = 0;
        r4 = 0;
        r2 = 0;
        r14 = r13;
        r18 = r3;
        r3 = r2;
        r2 = r18;
    L_0x0020:
        r15 = r21.read();
        if (r15 >= 0) goto L_0x004f;
    L_0x0026:
        r13 = r12;
        r12 = r11;
    L_0x0028:
        if (r9 == 0) goto L_0x0046;
    L_0x002a:
        r0 = r19;
        r0.lineno = r2;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "unterminated ";
        r2 = r2.append(r3);
        r2 = r2.append(r4);
        r2 = r2.toString();
        r0 = r19;
        r0.error(r2);
    L_0x0046:
        r0 = r19;
        r0.resultBuffer = r13;
        r0 = r19;
        r0.resultLength = r12;
        return r14;
    L_0x004f:
        r13 = r11 + 10;
        r0 = r12.length;
        r16 = r0;
        r0 = r16;
        if (r13 < r0) goto L_0x030f;
    L_0x0058:
        r13 = r11 * 2;
        r13 = new byte[r13];
        r16 = 0;
        r17 = 0;
        r0 = r16;
        r1 = r17;
        java.lang.System.arraycopy(r12, r0, r13, r1, r11);
    L_0x0067:
        r12 = 10;
        if (r15 != r12) goto L_0x007f;
    L_0x006b:
        if (r11 <= 0) goto L_0x007f;
    L_0x006d:
        r12 = r11 + -1;
        r12 = r13[r12];
        r16 = 13;
        r0 = r16;
        if (r12 != r0) goto L_0x007f;
    L_0x0077:
        r12 = r11 + 1;
        r15 = (byte) r15;
        r13[r11] = r15;
        r11 = r12;
        r12 = r13;
        goto L_0x0020;
    L_0x007f:
        if (r8 < 0) goto L_0x030b;
    L_0x0081:
        if (r5 >= 0) goto L_0x030b;
    L_0x0083:
        if (r3 > 0) goto L_0x030b;
    L_0x0085:
        r12 = 13;
        if (r15 == r12) goto L_0x030b;
    L_0x0089:
        r12 = 10;
        if (r15 == r12) goto L_0x030b;
    L_0x008d:
        if (r8 == r7) goto L_0x0097;
    L_0x008f:
        r12 = 32;
        if (r15 == r12) goto L_0x030b;
    L_0x0093:
        r12 = 9;
        if (r15 == r12) goto L_0x030b;
    L_0x0097:
        r12 = 47;
        if (r15 != r12) goto L_0x0142;
    L_0x009b:
        r12 = 100;
        r0 = r21;
        r0.mark(r12);
        r12 = r21.read();
        r16 = 47;
        r0 = r16;
        if (r12 != r0) goto L_0x011d;
    L_0x00ac:
        r12 = 0;
    L_0x00ad:
        r21.reset();
    L_0x00b0:
        if (r12 == 0) goto L_0x030b;
    L_0x00b2:
        r3 = r11 + 1;
        r12 = 47;
        r13[r11] = r12;
        r12 = r3 + 1;
        r11 = 47;
        r13[r3] = r11;
        r11 = r12 + 1;
        r3 = 32;
        r13[r12] = r3;
        r3 = 1;
        r14 = 1;
        r12 = r11;
        r11 = r3;
    L_0x00c8:
        r3 = 32;
        if (r15 == r3) goto L_0x0305;
    L_0x00cc:
        r3 = 9;
        if (r15 == r3) goto L_0x0305;
    L_0x00d0:
        if (r5 >= 0) goto L_0x0305;
    L_0x00d2:
        if (r9 <= 0) goto L_0x02fe;
    L_0x00d4:
        if (r8 == r7) goto L_0x02fe;
    L_0x00d6:
        r3 = 47;
        if (r15 != r3) goto L_0x02fe;
    L_0x00da:
        r3 = r21.read();
        if (r3 < 0) goto L_0x0028;
    L_0x00e0:
        r5 = 47;
        if (r3 == r5) goto L_0x0145;
    L_0x00e4:
        r5 = r12 + 1;
        r15 = 47;
        r13[r12] = r15;
        r18 = r3;
        r3 = r11;
        r11 = r5;
        r5 = r12;
        r12 = r14;
        r14 = r18;
    L_0x00f2:
        r15 = (byte) r14;
        r13[r11] = r15;
        r11 = r11 + 1;
        r15 = 13;
        if (r14 == r15) goto L_0x00ff;
    L_0x00fb:
        r15 = 10;
        if (r14 != r15) goto L_0x02d6;
    L_0x00ff:
        r5 = -1;
        r3 = 0;
        r18 = r10;
        r10 = r5;
        r5 = r18;
    L_0x0106:
        r14 = r11 + -1;
        if (r5 >= r14) goto L_0x0167;
    L_0x010a:
        r14 = r13[r5];
        r15 = 32;
        if (r14 == r15) goto L_0x011a;
    L_0x0110:
        r14 = r13[r5];
        r15 = 9;
        if (r14 == r15) goto L_0x011a;
    L_0x0116:
        if (r10 >= 0) goto L_0x02f1;
    L_0x0118:
        r3 = r5;
        r10 = r5;
    L_0x011a:
        r5 = r5 + 1;
        goto L_0x0106;
    L_0x011d:
        r16 = 42;
        r0 = r16;
        if (r12 != r0) goto L_0x013f;
    L_0x0123:
        r12 = r21.read();
        r16 = 32;
        r0 = r16;
        if (r12 == r0) goto L_0x0123;
    L_0x012d:
        r16 = 9;
        r0 = r16;
        if (r12 == r0) goto L_0x0123;
    L_0x0133:
        r16 = 35;
        r0 = r16;
        if (r12 == r0) goto L_0x013c;
    L_0x0139:
        r12 = 1;
        goto L_0x00ad;
    L_0x013c:
        r12 = 0;
        goto L_0x00ad;
    L_0x013f:
        r12 = 1;
        goto L_0x00ad;
    L_0x0142:
        r12 = 1;
        goto L_0x00b0;
    L_0x0145:
        r3 = r21.read();
        if (r3 < 0) goto L_0x0028;
    L_0x014b:
        r5 = -1;
        r14 = 1;
        r11 = 32;
        if (r3 != r11) goto L_0x02f4;
    L_0x0151:
        r3 = r21.read();
        r11 = 32;
        if (r3 == r11) goto L_0x015d;
    L_0x0159:
        r11 = 9;
        if (r3 != r11) goto L_0x02f4;
    L_0x015d:
        r11 = -1;
        r18 = r3;
        r3 = r5;
        r5 = r11;
        r11 = r12;
        r12 = r14;
        r14 = r18;
        goto L_0x00f2;
    L_0x0167:
        r5 = r3 - r10;
        r14 = 4;
        if (r5 < r14) goto L_0x021e;
    L_0x016c:
        r5 = r13[r10];
        r14 = 47;
        if (r5 != r14) goto L_0x021e;
    L_0x0172:
        r5 = r10 + 1;
        r5 = r13[r5];
        r14 = 42;
        if (r5 != r14) goto L_0x021e;
    L_0x017a:
        r5 = r3 + -1;
        r5 = r13[r5];
        r14 = 42;
        if (r5 != r14) goto L_0x021e;
    L_0x0182:
        r5 = r13[r3];
        r14 = 47;
        if (r5 != r14) goto L_0x021e;
    L_0x0188:
        r5 = r10 + 2;
        r10 = r5;
    L_0x018b:
        if (r10 >= r3) goto L_0x0197;
    L_0x018d:
        r5 = r13[r10];
        r14 = 32;
        if (r5 != r14) goto L_0x0197;
    L_0x0193:
        r5 = r10 + 1;
        r10 = r5;
        goto L_0x018b;
    L_0x0197:
        r3 = r3 + -2;
    L_0x0199:
        if (r3 <= r10) goto L_0x01a4;
    L_0x019b:
        r5 = r13[r3];
        r14 = 32;
        if (r5 != r14) goto L_0x01a4;
    L_0x01a1:
        r3 = r3 + -1;
        goto L_0x0199;
    L_0x01a4:
        r5 = r13[r10];
        r14 = 35;
        if (r5 != r14) goto L_0x021e;
    L_0x01aa:
        r5 = new java.lang.String;
        r2 = r3 - r10;
        r2 = r2 + 1;
        r3 = "ISO-8859-1";
        r5.<init>(r13, r10, r2, r3);
        r2 = 32;
        r2 = r5.indexOf(r2);
        r0 = r19;
        r10 = r0.lineno;
        if (r2 <= 0) goto L_0x023a;
    L_0x01c1:
        r3 = 0;
        r4 = r5.substring(r3, r2);
        r2 = r5.substring(r2);
        r3 = r2.trim();
        r0 = r19;
        r2 = r0.keywords;
        r2 = r2.get(r3);
    L_0x01d6:
        r14 = "#ifdef";
        r14 = r14.equals(r4);
        if (r14 != 0) goto L_0x01e6;
    L_0x01de:
        r14 = "#ifndef";
        r14 = r14.equals(r4);
        if (r14 == 0) goto L_0x0259;
    L_0x01e6:
        if (r2 != 0) goto L_0x031d;
    L_0x01e8:
        r2 = java.lang.System.err;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r0 = r20;
        r5 = r5.append(r0);
        r14 = ":";
        r5 = r5.append(r14);
        r0 = r19;
        r14 = r0.lineno;
        r5 = r5.append(r14);
        r14 = ": warning - undefined keyword: ";
        r5 = r5.append(r14);
        r3 = r5.append(r3);
        r3 = r3.toString();
        r2.println(r3);
        r2 = java.lang.Boolean.FALSE;
        r5 = r2;
    L_0x0217:
        r3 = r9 + 1;
        if (r6 <= 0) goto L_0x023f;
    L_0x021b:
        r9 = r3;
        r8 = r7;
        r2 = r10;
    L_0x021e:
        r5 = -1;
        r7 = 0;
        r0 = r19;
        r3 = r0.lineno;
        r3 = r3 + 1;
        r0 = r19;
        r0.lineno = r3;
        r3 = 0;
        r10 = r11;
        r18 = r2;
        r2 = r3;
        r3 = r18;
    L_0x0231:
        r14 = r12;
        r12 = r13;
        r18 = r3;
        r3 = r2;
        r2 = r18;
        goto L_0x0020;
    L_0x023a:
        r3 = "";
        r2 = 0;
        r4 = r5;
        goto L_0x01d6;
    L_0x023f:
        r2 = 3;
        r2 = r4.charAt(r2);
        r9 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        if (r2 != r9) goto L_0x0255;
    L_0x0248:
        r2 = 1;
    L_0x0249:
        r9 = java.lang.Boolean.FALSE;
        if (r5 != r9) goto L_0x0257;
    L_0x024d:
        r5 = 1;
    L_0x024e:
        if (r2 == r5) goto L_0x0319;
    L_0x0250:
        r6 = r3;
        r9 = r3;
        r8 = r7;
        r2 = r10;
        goto L_0x021e;
    L_0x0255:
        r2 = 0;
        goto L_0x0249;
    L_0x0257:
        r5 = 0;
        goto L_0x024e;
    L_0x0259:
        r2 = "#else";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0289;
    L_0x0261:
        if (r9 != 0) goto L_0x027d;
    L_0x0263:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "unexpected ";
        r2 = r2.append(r3);
        r2 = r2.append(r4);
        r2 = r2.toString();
        r0 = r19;
        r0.error(r2);
        r2 = r10;
        goto L_0x021e;
    L_0x027d:
        if (r9 != r6) goto L_0x0283;
    L_0x027f:
        r8 = -1;
        r6 = 0;
        r2 = r10;
        goto L_0x021e;
    L_0x0283:
        if (r6 != 0) goto L_0x0315;
    L_0x0285:
        r6 = r9;
        r8 = r7;
        r2 = r10;
        goto L_0x021e;
    L_0x0289:
        r2 = "#endif";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x02bb;
    L_0x0291:
        if (r9 != 0) goto L_0x02ab;
    L_0x0293:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "unexpected ";
        r2 = r2.append(r3);
        r2 = r2.append(r4);
        r2 = r2.toString();
        r0 = r19;
        r0.error(r2);
    L_0x02ab:
        if (r9 != r6) goto L_0x02b6;
    L_0x02ad:
        r2 = 0;
        r3 = -1;
    L_0x02af:
        r9 = r9 + -1;
        r6 = r2;
        r8 = r3;
        r2 = r10;
        goto L_0x021e;
    L_0x02b6:
        if (r6 <= 0) goto L_0x0312;
    L_0x02b8:
        r2 = r6;
        r3 = r7;
        goto L_0x02af;
    L_0x02bb:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "unknown command: ";
        r2 = r2.append(r3);
        r2 = r2.append(r5);
        r2 = r2.toString();
        r0 = r19;
        r0.error(r2);
        r2 = r10;
        goto L_0x021e;
    L_0x02d6:
        if (r5 >= 0) goto L_0x02ea;
    L_0x02d8:
        r15 = 9;
        if (r14 != r15) goto L_0x02e7;
    L_0x02dc:
        r7 = r7 + 8;
        r7 = r7 & -8;
    L_0x02e0:
        r18 = r3;
        r3 = r2;
        r2 = r18;
        goto L_0x0231;
    L_0x02e7:
        r7 = r7 + 1;
        goto L_0x02e0;
    L_0x02ea:
        r18 = r3;
        r3 = r2;
        r2 = r18;
        goto L_0x0231;
    L_0x02f1:
        r3 = r5;
        goto L_0x011a;
    L_0x02f4:
        r11 = r12;
        r18 = r5;
        r5 = r12;
        r12 = r14;
        r14 = r3;
        r3 = r18;
        goto L_0x00f2;
    L_0x02fe:
        r3 = r11;
        r5 = r12;
        r11 = r12;
        r12 = r14;
        r14 = r15;
        goto L_0x00f2;
    L_0x0305:
        r3 = r11;
        r11 = r12;
        r12 = r14;
        r14 = r15;
        goto L_0x00f2;
    L_0x030b:
        r12 = r11;
        r11 = r3;
        goto L_0x00c8;
    L_0x030f:
        r13 = r12;
        goto L_0x0067;
    L_0x0312:
        r2 = r6;
        r3 = r8;
        goto L_0x02af;
    L_0x0315:
        r8 = r7;
        r2 = r10;
        goto L_0x021e;
    L_0x0319:
        r9 = r3;
        r2 = r10;
        goto L_0x021e;
    L_0x031d:
        r5 = r2;
        goto L_0x0217;
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.util.PreProcess.filter(java.lang.String, java.io.BufferedInputStream):boolean");
    }

    void handleArg(String str) {
        int i = 0;
        String substring;
        if (str.charAt(0) == '%') {
            substring = str.substring(1);
            while (true) {
                if (i >= version_features.length) {
                    System.err.println("Unknown version: " + substring);
                    System.exit(-1);
                }
                if (substring.equals(version_features[i])) {
                    break;
                }
                i += 2;
            }
            String str2 = version_features[i + 1];
            System.err.println("(variant " + substring + " maps to: " + str2 + ")");
            StringTokenizer stringTokenizer = new StringTokenizer(str2);
            while (stringTokenizer.hasMoreTokens()) {
                handleArg(stringTokenizer.nextToken());
            }
        } else if (str.charAt(0) == '+') {
            this.keywords.put(str.substring(1), Boolean.TRUE);
        } else if (str.charAt(0) == '-') {
            int indexOf = str.indexOf(61);
            if (indexOf > 1) {
                substring = str.substring(str.charAt(1) == '-' ? 2 : 1, indexOf);
                String substring2 = str.substring(indexOf + 1);
                Object obj = Boolean.FALSE;
                if (substring2.equalsIgnoreCase("true")) {
                    obj = Boolean.TRUE;
                } else if (!substring2.equalsIgnoreCase("false")) {
                    System.err.println("invalid value " + substring2 + " for " + substring);
                    System.exit(-1);
                }
                this.keywords.put(substring, obj);
                return;
            }
            this.keywords.put(str.substring(1), Boolean.FALSE);
        } else {
            try {
                filter(str);
            } catch (Throwable th) {
                System.err.println("caught " + th);
                th.printStackTrace();
                System.exit(-1);
            }
        }
    }

    public static void main(String[] strArr) {
        PreProcess preProcess = new PreProcess();
        preProcess.keywords.put("true", Boolean.TRUE);
        preProcess.keywords.put("false", Boolean.FALSE);
        for (String handleArg : strArr) {
            preProcess.handleArg(handleArg);
        }
    }
}
