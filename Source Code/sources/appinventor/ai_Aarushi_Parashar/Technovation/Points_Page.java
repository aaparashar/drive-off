package appinventor.ai_Aarushi_Parashar.Technovation;

import com.google.api.client.http.HttpStatusCodes;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Canvas;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.ImageSprite;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.Sharing;
import com.google.appinventor.components.runtime.TinyDB;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.RetValManager;
import com.google.appinventor.components.runtime.util.RuntimeErrorAlert;
import com.google.youngandroid.runtime;
import gnu.expr.Language;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.kawa.functions.AddOp;
import gnu.kawa.functions.Format;
import gnu.kawa.functions.GetNamedPart;
import gnu.kawa.reflect.Invoke;
import gnu.kawa.reflect.SlotGet;
import gnu.kawa.reflect.SlotSet;
import gnu.kawa.xml.XDataType;
import gnu.lists.Consumer;
import gnu.lists.FString;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.lists.PairWithPosition;
import gnu.lists.VoidConsumer;
import gnu.mapping.CallContext;
import gnu.mapping.Environment;
import gnu.mapping.Procedure;
import gnu.mapping.PropertySet;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Symbol;
import gnu.mapping.Values;
import gnu.mapping.WrongType;
import gnu.math.IntNum;
import kawa.lang.Promise;
import kawa.lib.lists;
import kawa.lib.misc;
import kawa.lib.strings;
import kawa.standard.Scheme;
import kawa.standard.require;

/* compiled from: Points_Page.yail */
public class Points_Page extends Form implements Runnable {
    static final SimpleSymbol Lit0 = ((SimpleSymbol) new SimpleSymbol("Points_Page").readResolve());
    static final SimpleSymbol Lit1 = ((SimpleSymbol) new SimpleSymbol("getMessage").readResolve());
    static final SimpleSymbol Lit10 = ((SimpleSymbol) new SimpleSymbol("Title").readResolve());
    static final SimpleSymbol Lit100 = ((SimpleSymbol) new SimpleSymbol("Share").readResolve());
    static final IntNum Lit101;
    static final SimpleSymbol Lit102 = ((SimpleSymbol) new SimpleSymbol("FontTypeface").readResolve());
    static final IntNum Lit103;
    static final FString Lit104 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit105 = ((SimpleSymbol) new SimpleSymbol("Sharing1").readResolve());
    static final SimpleSymbol Lit106 = ((SimpleSymbol) new SimpleSymbol("ShareMessage").readResolve());
    static final PairWithPosition Lit107 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit4, PairWithPosition.make(Lit4, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Points_Page.yail", 807167), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Points_Page.yail", 807162), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Points_Page.yail", 807156);
    static final PairWithPosition Lit108;
    static final SimpleSymbol Lit109 = ((SimpleSymbol) new SimpleSymbol("Share$Click").readResolve());
    static final PairWithPosition Lit11 = PairWithPosition.make(Lit4, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Points_Page.yail", 73806);
    static final FString Lit110 = new FString("com.google.appinventor.components.runtime.TinyDB");
    static final FString Lit111 = new FString("com.google.appinventor.components.runtime.TinyDB");
    static final FString Lit112 = new FString("com.google.appinventor.components.runtime.Sharing");
    static final FString Lit113 = new FString("com.google.appinventor.components.runtime.Sharing");
    static final SimpleSymbol Lit114 = ((SimpleSymbol) new SimpleSymbol("android-log-form").readResolve());
    static final SimpleSymbol Lit115 = ((SimpleSymbol) new SimpleSymbol("add-to-form-environment").readResolve());
    static final SimpleSymbol Lit116 = ((SimpleSymbol) new SimpleSymbol("lookup-in-form-environment").readResolve());
    static final SimpleSymbol Lit117 = ((SimpleSymbol) new SimpleSymbol("is-bound-in-form-environment").readResolve());
    static final SimpleSymbol Lit118 = ((SimpleSymbol) new SimpleSymbol("add-to-global-var-environment").readResolve());
    static final SimpleSymbol Lit119 = ((SimpleSymbol) new SimpleSymbol("add-to-events").readResolve());
    static final SimpleSymbol Lit12 = ((SimpleSymbol) new SimpleSymbol("Points_Page$BackPressed").readResolve());
    static final SimpleSymbol Lit120 = ((SimpleSymbol) new SimpleSymbol("add-to-components").readResolve());
    static final SimpleSymbol Lit121 = ((SimpleSymbol) new SimpleSymbol("add-to-global-vars").readResolve());
    static final SimpleSymbol Lit122 = ((SimpleSymbol) new SimpleSymbol("add-to-form-do-after-creation").readResolve());
    static final SimpleSymbol Lit123 = ((SimpleSymbol) new SimpleSymbol("send-error").readResolve());
    static final SimpleSymbol Lit124 = ((SimpleSymbol) new SimpleSymbol("dispatchEvent").readResolve());
    static final SimpleSymbol Lit125 = ((SimpleSymbol) new SimpleSymbol("lookup-handler").readResolve());
    static final SimpleSymbol Lit126 = ((SimpleSymbol) new SimpleSymbol("any").readResolve());
    static final SimpleSymbol Lit13 = ((SimpleSymbol) new SimpleSymbol("BackPressed").readResolve());
    static final SimpleSymbol Lit14 = ((SimpleSymbol) new SimpleSymbol("Score_displaed").readResolve());
    static final SimpleSymbol Lit15 = ((SimpleSymbol) new SimpleSymbol("Text").readResolve());
    static final SimpleSymbol Lit16 = ((SimpleSymbol) new SimpleSymbol("TinyDB1").readResolve());
    static final SimpleSymbol Lit17 = ((SimpleSymbol) new SimpleSymbol("GetValue").readResolve());
    static final PairWithPosition Lit18 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit126, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Points_Page.yail", 82056), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Points_Page.yail", 82050);
    static final SimpleSymbol Lit19 = ((SimpleSymbol) new SimpleSymbol("TextingScoreDisplayed").readResolve());
    static final SimpleSymbol Lit2 = ((SimpleSymbol) new SimpleSymbol("*the-null-value*").readResolve());
    static final PairWithPosition Lit20 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit126, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Points_Page.yail", 82207), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Points_Page.yail", 82201);
    static final SimpleSymbol Lit21 = ((SimpleSymbol) new SimpleSymbol("OverallScoreDisplayed").readResolve());
    static final PairWithPosition Lit22 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit126, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Points_Page.yail", 82400), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Points_Page.yail", 82394);
    static final PairWithPosition Lit23 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit126, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Points_Page.yail", 82490), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Points_Page.yail", 82484);
    static final PairWithPosition Lit24;
    static final SimpleSymbol Lit25 = ((SimpleSymbol) new SimpleSymbol("Points_Page$Initialize").readResolve());
    static final SimpleSymbol Lit26 = ((SimpleSymbol) new SimpleSymbol("Initialize").readResolve());
    static final FString Lit27 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit28 = ((SimpleSymbol) new SimpleSymbol("Home").readResolve());
    static final SimpleSymbol Lit29 = ((SimpleSymbol) new SimpleSymbol("FontBold").readResolve());
    static final SimpleSymbol Lit3 = ((SimpleSymbol) new SimpleSymbol("AboutScreen").readResolve());
    static final SimpleSymbol Lit30 = ((SimpleSymbol) new SimpleSymbol(PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN).readResolve());
    static final SimpleSymbol Lit31 = ((SimpleSymbol) new SimpleSymbol("Shape").readResolve());
    static final IntNum Lit32 = IntNum.make(1);
    static final FString Lit33 = new FString("com.google.appinventor.components.runtime.Button");
    static final PairWithPosition Lit34 = PairWithPosition.make(Lit4, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Points_Page.yail", 135246);
    static final SimpleSymbol Lit35 = ((SimpleSymbol) new SimpleSymbol("Home$Click").readResolve());
    static final SimpleSymbol Lit36 = ((SimpleSymbol) new SimpleSymbol("Click").readResolve());
    static final FString Lit37 = new FString("com.google.appinventor.components.runtime.Canvas");
    static final SimpleSymbol Lit38 = ((SimpleSymbol) new SimpleSymbol("Canvas1").readResolve());
    static final IntNum Lit39;
    static final SimpleSymbol Lit4;
    static final SimpleSymbol Lit40 = ((SimpleSymbol) new SimpleSymbol("Height").readResolve());
    static final IntNum Lit41 = IntNum.make(-2);
    static final SimpleSymbol Lit42 = ((SimpleSymbol) new SimpleSymbol("Width").readResolve());
    static final FString Lit43 = new FString("com.google.appinventor.components.runtime.Canvas");
    static final FString Lit44 = new FString("com.google.appinventor.components.runtime.ImageSprite");
    static final SimpleSymbol Lit45 = ((SimpleSymbol) new SimpleSymbol("ImageSprite1").readResolve());
    static final IntNum Lit46 = IntNum.make((int) HttpStatusCodes.STATUS_CODE_MULTIPLE_CHOICES);
    static final IntNum Lit47 = IntNum.make(500);
    static final SimpleSymbol Lit48 = ((SimpleSymbol) new SimpleSymbol("Picture").readResolve());
    static final SimpleSymbol Lit49 = ((SimpleSymbol) new SimpleSymbol("Speed").readResolve());
    static final SimpleSymbol Lit5 = ((SimpleSymbol) new SimpleSymbol("AppName").readResolve());
    static final IntNum Lit50 = IntNum.make(0);
    static final SimpleSymbol Lit51 = ((SimpleSymbol) new SimpleSymbol("X").readResolve());
    static final IntNum Lit52 = IntNum.make(-94);
    static final SimpleSymbol Lit53 = ((SimpleSymbol) new SimpleSymbol("Y").readResolve());
    static final IntNum Lit54 = IntNum.make(-15);
    static final FString Lit55 = new FString("com.google.appinventor.components.runtime.ImageSprite");
    static final FString Lit56 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit57 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement4").readResolve());
    static final SimpleSymbol Lit58 = ((SimpleSymbol) new SimpleSymbol("AlignHorizontal").readResolve());
    static final IntNum Lit59 = IntNum.make(3);
    static final SimpleSymbol Lit6 = ((SimpleSymbol) new SimpleSymbol("BackgroundColor").readResolve());
    static final SimpleSymbol Lit60 = ((SimpleSymbol) new SimpleSymbol("AlignVertical").readResolve());
    static final IntNum Lit61;
    static final FString Lit62 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit63 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit64 = ((SimpleSymbol) new SimpleSymbol("Score").readResolve());
    static final SimpleSymbol Lit65 = ((SimpleSymbol) new SimpleSymbol("FontSize").readResolve());
    static final IntNum Lit66 = IntNum.make(18);
    static final SimpleSymbol Lit67 = ((SimpleSymbol) new SimpleSymbol("TextAlignment").readResolve());
    static final SimpleSymbol Lit68 = ((SimpleSymbol) new SimpleSymbol("TextColor").readResolve());
    static final IntNum Lit69;
    static final IntNum Lit7;
    static final FString Lit70 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit71 = new FString("com.google.appinventor.components.runtime.Label");
    static final IntNum Lit72;
    static final FString Lit73 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit74 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit75 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement5").readResolve());
    static final IntNum Lit76;
    static final FString Lit77 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit78 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit79 = ((SimpleSymbol) new SimpleSymbol("TScore").readResolve());
    static final SimpleSymbol Lit8;
    static final IntNum Lit80;
    static final FString Lit81 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit82 = new FString("com.google.appinventor.components.runtime.Label");
    static final IntNum Lit83;
    static final FString Lit84 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit85 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit86 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement6").readResolve());
    static final IntNum Lit87 = IntNum.make(2);
    static final IntNum Lit88;
    static final FString Lit89 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit9 = ((SimpleSymbol) new SimpleSymbol("ScreenOrientation").readResolve());
    static final FString Lit90 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit91 = ((SimpleSymbol) new SimpleSymbol("GrandTotal").readResolve());
    static final IntNum Lit92;
    static final IntNum Lit93;
    static final FString Lit94 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit95 = new FString("com.google.appinventor.components.runtime.Label");
    static final IntNum Lit96;
    static final IntNum Lit97;
    static final FString Lit98 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit99 = new FString("com.google.appinventor.components.runtime.Button");
    public static Points_Page Points_Page;
    static final ModuleMethod lambda$Fn1 = null;
    static final ModuleMethod lambda$Fn10 = null;
    static final ModuleMethod lambda$Fn11 = null;
    static final ModuleMethod lambda$Fn12 = null;
    static final ModuleMethod lambda$Fn13 = null;
    static final ModuleMethod lambda$Fn14 = null;
    static final ModuleMethod lambda$Fn15 = null;
    static final ModuleMethod lambda$Fn16 = null;
    static final ModuleMethod lambda$Fn17 = null;
    static final ModuleMethod lambda$Fn18 = null;
    static final ModuleMethod lambda$Fn19 = null;
    static final ModuleMethod lambda$Fn2 = null;
    static final ModuleMethod lambda$Fn20 = null;
    static final ModuleMethod lambda$Fn21 = null;
    static final ModuleMethod lambda$Fn22 = null;
    static final ModuleMethod lambda$Fn23 = null;
    static final ModuleMethod lambda$Fn24 = null;
    static final ModuleMethod lambda$Fn25 = null;
    static final ModuleMethod lambda$Fn26 = null;
    static final ModuleMethod lambda$Fn27 = null;
    static final ModuleMethod lambda$Fn28 = null;
    static final ModuleMethod lambda$Fn3 = null;
    static final ModuleMethod lambda$Fn4 = null;
    static final ModuleMethod lambda$Fn5 = null;
    static final ModuleMethod lambda$Fn6 = null;
    static final ModuleMethod lambda$Fn7 = null;
    static final ModuleMethod lambda$Fn8 = null;
    static final ModuleMethod lambda$Fn9 = null;
    public Boolean $Stdebug$Mnform$St;
    public final ModuleMethod $define;
    public Canvas Canvas1;
    public Label GrandTotal;
    public Button Home;
    public final ModuleMethod Home$Click;
    public HorizontalArrangement HorizontalArrangement4;
    public HorizontalArrangement HorizontalArrangement5;
    public HorizontalArrangement HorizontalArrangement6;
    public ImageSprite ImageSprite1;
    public Label OverallScoreDisplayed;
    public final ModuleMethod Points_Page$BackPressed;
    public final ModuleMethod Points_Page$Initialize;
    public Label Score;
    public Label Score_displaed;
    public Button Share;
    public final ModuleMethod Share$Click;
    public Sharing Sharing1;
    public Label TScore;
    public Label TextingScoreDisplayed;
    public TinyDB TinyDB1;
    public final ModuleMethod add$Mnto$Mncomponents;
    public final ModuleMethod add$Mnto$Mnevents;
    public final ModuleMethod add$Mnto$Mnform$Mndo$Mnafter$Mncreation;
    public final ModuleMethod add$Mnto$Mnform$Mnenvironment;
    public final ModuleMethod add$Mnto$Mnglobal$Mnvar$Mnenvironment;
    public final ModuleMethod add$Mnto$Mnglobal$Mnvars;
    public final ModuleMethod android$Mnlog$Mnform;
    public LList components$Mnto$Mncreate;
    public final ModuleMethod dispatchEvent;
    public LList events$Mnto$Mnregister;
    public LList form$Mndo$Mnafter$Mncreation;
    public Environment form$Mnenvironment;
    public Symbol form$Mnname$Mnsymbol;
    public Environment global$Mnvar$Mnenvironment;
    public LList global$Mnvars$Mnto$Mncreate;
    public final ModuleMethod is$Mnbound$Mnin$Mnform$Mnenvironment;
    public final ModuleMethod lookup$Mnhandler;
    public final ModuleMethod lookup$Mnin$Mnform$Mnenvironment;
    public final ModuleMethod process$Mnexception;
    public final ModuleMethod send$Mnerror;

    /* compiled from: Points_Page.yail */
    public class frame extends ModuleBody {
        Points_Page $main = this;

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 1:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 3:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 5:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 10:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 11:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 12:
                    if (!(obj instanceof Points_Page)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                default:
                    return super.match1(moduleMethod, obj, callContext);
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 2:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 3:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 6:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 7:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 9:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 14:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                default:
                    return super.match2(moduleMethod, obj, obj2, callContext);
            }
        }

        public int match4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 8:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.value3 = obj3;
                    callContext.value4 = obj4;
                    callContext.proc = moduleMethod;
                    callContext.pc = 4;
                    return 0;
                case 13:
                    if (!(obj instanceof Points_Page)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    if (!(obj2 instanceof Component)) {
                        return -786430;
                    }
                    callContext.value2 = obj2;
                    if (!(obj3 instanceof String)) {
                        return -786429;
                    }
                    callContext.value3 = obj3;
                    if (!(obj4 instanceof String)) {
                        return -786428;
                    }
                    callContext.value4 = obj4;
                    callContext.proc = moduleMethod;
                    callContext.pc = 4;
                    return 0;
                default:
                    return super.match4(moduleMethod, obj, obj2, obj3, obj4, callContext);
            }
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            switch (moduleMethod.selector) {
                case 1:
                    this.$main.androidLogForm(obj);
                    return Values.empty;
                case 3:
                    try {
                        return this.$main.lookupInFormEnvironment((Symbol) obj);
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "lookup-in-form-environment", 1, obj);
                    }
                case 5:
                    try {
                        return this.$main.isBoundInFormEnvironment((Symbol) obj) ? Boolean.TRUE : Boolean.FALSE;
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "is-bound-in-form-environment", 1, obj);
                    }
                case 10:
                    this.$main.addToFormDoAfterCreation(obj);
                    return Values.empty;
                case 11:
                    this.$main.sendError(obj);
                    return Values.empty;
                case 12:
                    this.$main.processException(obj);
                    return Values.empty;
                default:
                    return super.apply1(moduleMethod, obj);
            }
        }

        public Object apply4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4) {
            switch (moduleMethod.selector) {
                case 8:
                    this.$main.addToComponents(obj, obj2, obj3, obj4);
                    return Values.empty;
                case 13:
                    try {
                        try {
                            try {
                                try {
                                    return this.$main.dispatchEvent((Component) obj, (String) obj2, (String) obj3, (Object[]) obj4) ? Boolean.TRUE : Boolean.FALSE;
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "dispatchEvent", 4, obj4);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "dispatchEvent", 3, obj3);
                            }
                        } catch (ClassCastException e22) {
                            throw new WrongType(e22, "dispatchEvent", 2, obj2);
                        }
                    } catch (ClassCastException e222) {
                        throw new WrongType(e222, "dispatchEvent", 1, obj);
                    }
                default:
                    return super.apply4(moduleMethod, obj, obj2, obj3, obj4);
            }
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            switch (moduleMethod.selector) {
                case 2:
                    try {
                        this.$main.addToFormEnvironment((Symbol) obj, obj2);
                        return Values.empty;
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "add-to-form-environment", 1, obj);
                    }
                case 3:
                    try {
                        return this.$main.lookupInFormEnvironment((Symbol) obj, obj2);
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "lookup-in-form-environment", 1, obj);
                    }
                case 6:
                    try {
                        this.$main.addToGlobalVarEnvironment((Symbol) obj, obj2);
                        return Values.empty;
                    } catch (ClassCastException e22) {
                        throw new WrongType(e22, "add-to-global-var-environment", 1, obj);
                    }
                case 7:
                    this.$main.addToEvents(obj, obj2);
                    return Values.empty;
                case 9:
                    this.$main.addToGlobalVars(obj, obj2);
                    return Values.empty;
                case 14:
                    return this.$main.lookupHandler(obj, obj2);
                default:
                    return super.apply2(moduleMethod, obj, obj2);
            }
        }

        public Object apply0(ModuleMethod moduleMethod) {
            switch (moduleMethod.selector) {
                case 15:
                    return Points_Page.lambda2();
                case 16:
                    this.$main.$define();
                    return Values.empty;
                case 17:
                    return Points_Page.lambda3();
                case 18:
                    return this.$main.Points_Page$BackPressed();
                case 19:
                    return this.$main.Points_Page$Initialize();
                case 20:
                    return Points_Page.lambda4();
                case 21:
                    return Points_Page.lambda5();
                case 22:
                    return this.$main.Home$Click();
                case 23:
                    return Points_Page.lambda6();
                case 24:
                    return Points_Page.lambda7();
                case 25:
                    return Points_Page.lambda8();
                case 26:
                    return Points_Page.lambda9();
                case 27:
                    return Points_Page.lambda10();
                case 28:
                    return Points_Page.lambda11();
                case 29:
                    return Points_Page.lambda12();
                case 30:
                    return Points_Page.lambda13();
                case 31:
                    return Points_Page.lambda14();
                case 32:
                    return Points_Page.lambda15();
                case 33:
                    return Points_Page.lambda16();
                case 34:
                    return Points_Page.lambda17();
                case 35:
                    return Points_Page.lambda18();
                case 36:
                    return Points_Page.lambda19();
                case 37:
                    return Points_Page.lambda20();
                case 38:
                    return Points_Page.lambda21();
                case 39:
                    return Points_Page.lambda22();
                case 40:
                    return Points_Page.lambda23();
                case 41:
                    return Points_Page.lambda24();
                case XDataType.NMTOKEN_TYPE_CODE /*42*/:
                    return Points_Page.lambda25();
                case XDataType.NAME_TYPE_CODE /*43*/:
                    return Points_Page.lambda26();
                case XDataType.NCNAME_TYPE_CODE /*44*/:
                    return Points_Page.lambda27();
                case XDataType.ID_TYPE_CODE /*45*/:
                    return Points_Page.lambda28();
                case XDataType.IDREF_TYPE_CODE /*46*/:
                    return Points_Page.lambda29();
                case XDataType.ENTITY_TYPE_CODE /*47*/:
                    return this.$main.Share$Click();
                default:
                    return super.apply0(moduleMethod);
            }
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 15:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 16:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 17:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 18:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 19:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 20:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 21:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 22:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 23:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 24:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 25:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 26:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 27:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 28:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 29:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 30:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 31:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 32:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 33:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 34:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 35:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 36:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 37:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 38:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 39:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 40:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 41:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case XDataType.NMTOKEN_TYPE_CODE /*42*/:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case XDataType.NAME_TYPE_CODE /*43*/:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case XDataType.NCNAME_TYPE_CODE /*44*/:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case XDataType.ID_TYPE_CODE /*45*/:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case XDataType.IDREF_TYPE_CODE /*46*/:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case XDataType.ENTITY_TYPE_CODE /*47*/:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                default:
                    return super.match0(moduleMethod, callContext);
            }
        }
    }

    static {
        SimpleSymbol simpleSymbol = (SimpleSymbol) new SimpleSymbol(PropertyTypeConstants.PROPERTY_TYPE_TEXT).readResolve();
        Lit4 = simpleSymbol;
        Lit108 = PairWithPosition.make(simpleSymbol, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Points_Page.yail", 807184);
        int[] iArr = new int[2];
        iArr[0] = -16777216;
        Lit103 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit101 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = Component.COLOR_GREEN;
        Lit97 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -16777216;
        Lit96 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = Component.COLOR_GREEN;
        Lit93 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -16777216;
        Lit92 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -16777216;
        Lit88 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = Component.COLOR_CYAN;
        Lit83 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = Component.COLOR_CYAN;
        Lit80 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -16777216;
        Lit76 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = Component.COLOR_CYAN;
        Lit72 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = Component.COLOR_CYAN;
        Lit69 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -16777216;
        Lit61 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -16777216;
        Lit39 = IntNum.make(iArr);
        simpleSymbol = (SimpleSymbol) new SimpleSymbol("number").readResolve();
        Lit8 = simpleSymbol;
        Lit24 = PairWithPosition.make(simpleSymbol, PairWithPosition.make(Lit8, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Points_Page.yail", 82507), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Points_Page.yail", 82499);
        iArr = new int[2];
        iArr[0] = -16777216;
        Lit7 = IntNum.make(iArr);
    }

    public Points_Page() {
        ModuleInfo.register(this);
        ModuleBody frame = new frame();
        this.android$Mnlog$Mnform = new ModuleMethod(frame, 1, Lit114, 4097);
        this.add$Mnto$Mnform$Mnenvironment = new ModuleMethod(frame, 2, Lit115, 8194);
        this.lookup$Mnin$Mnform$Mnenvironment = new ModuleMethod(frame, 3, Lit116, 8193);
        this.is$Mnbound$Mnin$Mnform$Mnenvironment = new ModuleMethod(frame, 5, Lit117, 4097);
        this.add$Mnto$Mnglobal$Mnvar$Mnenvironment = new ModuleMethod(frame, 6, Lit118, 8194);
        this.add$Mnto$Mnevents = new ModuleMethod(frame, 7, Lit119, 8194);
        this.add$Mnto$Mncomponents = new ModuleMethod(frame, 8, Lit120, 16388);
        this.add$Mnto$Mnglobal$Mnvars = new ModuleMethod(frame, 9, Lit121, 8194);
        this.add$Mnto$Mnform$Mndo$Mnafter$Mncreation = new ModuleMethod(frame, 10, Lit122, 4097);
        this.send$Mnerror = new ModuleMethod(frame, 11, Lit123, 4097);
        this.process$Mnexception = new ModuleMethod(frame, 12, "process-exception", 4097);
        this.dispatchEvent = new ModuleMethod(frame, 13, Lit124, 16388);
        this.lookup$Mnhandler = new ModuleMethod(frame, 14, Lit125, 8194);
        PropertySet moduleMethod = new ModuleMethod(frame, 15, null, 0);
        moduleMethod.setProperty("source-location", "/tmp/runtime6140171982161511911.scm:542");
        lambda$Fn1 = moduleMethod;
        this.$define = new ModuleMethod(frame, 16, "$define", 0);
        lambda$Fn2 = new ModuleMethod(frame, 17, null, 0);
        this.Points_Page$BackPressed = new ModuleMethod(frame, 18, Lit12, 0);
        this.Points_Page$Initialize = new ModuleMethod(frame, 19, Lit25, 0);
        lambda$Fn3 = new ModuleMethod(frame, 20, null, 0);
        lambda$Fn4 = new ModuleMethod(frame, 21, null, 0);
        this.Home$Click = new ModuleMethod(frame, 22, Lit35, 0);
        lambda$Fn5 = new ModuleMethod(frame, 23, null, 0);
        lambda$Fn6 = new ModuleMethod(frame, 24, null, 0);
        lambda$Fn7 = new ModuleMethod(frame, 25, null, 0);
        lambda$Fn8 = new ModuleMethod(frame, 26, null, 0);
        lambda$Fn9 = new ModuleMethod(frame, 27, null, 0);
        lambda$Fn10 = new ModuleMethod(frame, 28, null, 0);
        lambda$Fn11 = new ModuleMethod(frame, 29, null, 0);
        lambda$Fn12 = new ModuleMethod(frame, 30, null, 0);
        lambda$Fn13 = new ModuleMethod(frame, 31, null, 0);
        lambda$Fn14 = new ModuleMethod(frame, 32, null, 0);
        lambda$Fn15 = new ModuleMethod(frame, 33, null, 0);
        lambda$Fn16 = new ModuleMethod(frame, 34, null, 0);
        lambda$Fn17 = new ModuleMethod(frame, 35, null, 0);
        lambda$Fn18 = new ModuleMethod(frame, 36, null, 0);
        lambda$Fn19 = new ModuleMethod(frame, 37, null, 0);
        lambda$Fn20 = new ModuleMethod(frame, 38, null, 0);
        lambda$Fn21 = new ModuleMethod(frame, 39, null, 0);
        lambda$Fn22 = new ModuleMethod(frame, 40, null, 0);
        lambda$Fn23 = new ModuleMethod(frame, 41, null, 0);
        lambda$Fn24 = new ModuleMethod(frame, 42, null, 0);
        lambda$Fn25 = new ModuleMethod(frame, 43, null, 0);
        lambda$Fn26 = new ModuleMethod(frame, 44, null, 0);
        lambda$Fn27 = new ModuleMethod(frame, 45, null, 0);
        lambda$Fn28 = new ModuleMethod(frame, 46, null, 0);
        this.Share$Click = new ModuleMethod(frame, 47, Lit109, 0);
    }

    public Object lookupInFormEnvironment(Symbol symbol) {
        return lookupInFormEnvironment(symbol, Boolean.FALSE);
    }

    public void run() {
        Throwable th;
        CallContext instance = CallContext.getInstance();
        Consumer consumer = instance.consumer;
        instance.consumer = VoidConsumer.instance;
        try {
            run(instance);
            th = null;
        } catch (Throwable th2) {
            th = th2;
        }
        ModuleBody.runCleanup(instance, th, consumer);
    }

    public final void run(CallContext $ctx) {
        Consumer $result = $ctx.consumer;
        Object find = require.find("com.google.youngandroid.runtime");
        try {
            String str;
            ((Runnable) find).run();
            this.$Stdebug$Mnform$St = Boolean.FALSE;
            this.form$Mnenvironment = Environment.make(misc.symbol$To$String(Lit0));
            FString stringAppend = strings.stringAppend(misc.symbol$To$String(Lit0), "-global-vars");
            if (stringAppend == null) {
                str = null;
            } else {
                str = stringAppend.toString();
            }
            this.global$Mnvar$Mnenvironment = Environment.make(str);
            Points_Page = null;
            this.form$Mnname$Mnsymbol = Lit0;
            this.events$Mnto$Mnregister = LList.Empty;
            this.components$Mnto$Mncreate = LList.Empty;
            this.global$Mnvars$Mnto$Mncreate = LList.Empty;
            this.form$Mndo$Mnafter$Mncreation = LList.Empty;
            find = require.find("com.google.youngandroid.runtime");
            try {
                ((Runnable) find).run();
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit3, "Points Tracker", Lit4);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit5, "Technovation", Lit4);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit6, Lit7, Lit8);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit9, "sensor", Lit4);
                    Values.writeValues(runtime.setAndCoerceProperty$Ex(Lit0, Lit10, "Flame Score Tracker", Lit4), $result);
                } else {
                    addToFormDoAfterCreation(new Promise(lambda$Fn2));
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit12, this.Points_Page$BackPressed);
                } else {
                    addToFormEnvironment(Lit12, this.Points_Page$BackPressed);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Points_Page", "BackPressed");
                } else {
                    addToEvents(Lit0, Lit13);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit25, this.Points_Page$Initialize);
                } else {
                    addToFormEnvironment(Lit25, this.Points_Page$Initialize);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Points_Page", "Initialize");
                } else {
                    addToEvents(Lit0, Lit26);
                }
                this.Home = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit27, Lit28, lambda$Fn3), $result);
                } else {
                    addToComponents(Lit0, Lit33, Lit28, lambda$Fn4);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit35, this.Home$Click);
                } else {
                    addToFormEnvironment(Lit35, this.Home$Click);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Home", "Click");
                } else {
                    addToEvents(Lit28, Lit36);
                }
                this.Canvas1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit37, Lit38, lambda$Fn5), $result);
                } else {
                    addToComponents(Lit0, Lit43, Lit38, lambda$Fn6);
                }
                this.ImageSprite1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit38, Lit44, Lit45, lambda$Fn7), $result);
                } else {
                    addToComponents(Lit38, Lit55, Lit45, lambda$Fn8);
                }
                this.HorizontalArrangement4 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit56, Lit57, lambda$Fn9), $result);
                } else {
                    addToComponents(Lit0, Lit62, Lit57, lambda$Fn10);
                }
                this.Score = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit57, Lit63, Lit64, lambda$Fn11), $result);
                } else {
                    addToComponents(Lit57, Lit70, Lit64, lambda$Fn12);
                }
                this.Score_displaed = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit57, Lit71, Lit14, lambda$Fn13), $result);
                } else {
                    addToComponents(Lit57, Lit73, Lit14, lambda$Fn14);
                }
                this.HorizontalArrangement5 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit74, Lit75, lambda$Fn15), $result);
                } else {
                    addToComponents(Lit0, Lit77, Lit75, lambda$Fn16);
                }
                this.TScore = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit75, Lit78, Lit79, lambda$Fn17), $result);
                } else {
                    addToComponents(Lit75, Lit81, Lit79, lambda$Fn18);
                }
                this.TextingScoreDisplayed = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit75, Lit82, Lit19, lambda$Fn19), $result);
                } else {
                    addToComponents(Lit75, Lit84, Lit19, lambda$Fn20);
                }
                this.HorizontalArrangement6 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit85, Lit86, lambda$Fn21), $result);
                } else {
                    addToComponents(Lit0, Lit89, Lit86, lambda$Fn22);
                }
                this.GrandTotal = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit86, Lit90, Lit91, lambda$Fn23), $result);
                } else {
                    addToComponents(Lit86, Lit94, Lit91, lambda$Fn24);
                }
                this.OverallScoreDisplayed = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit86, Lit95, Lit21, lambda$Fn25), $result);
                } else {
                    addToComponents(Lit86, Lit98, Lit21, lambda$Fn26);
                }
                this.Share = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit99, Lit100, lambda$Fn27), $result);
                } else {
                    addToComponents(Lit0, Lit104, Lit100, lambda$Fn28);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit109, this.Share$Click);
                } else {
                    addToFormEnvironment(Lit109, this.Share$Click);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Share", "Click");
                } else {
                    addToEvents(Lit100, Lit36);
                }
                this.TinyDB1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit110, Lit16, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit111, Lit16, Boolean.FALSE);
                }
                this.Sharing1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit112, Lit105, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit113, Lit105, Boolean.FALSE);
                }
                runtime.initRuntime();
            } catch (ClassCastException e) {
                throw new WrongType(e, "java.lang.Runnable.run()", 1, find);
            }
        } catch (ClassCastException e2) {
            throw new WrongType(e2, "java.lang.Runnable.run()", 1, find);
        }
    }

    static Object lambda3() {
        runtime.setAndCoerceProperty$Ex(Lit0, Lit3, "Points Tracker", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit5, "Technovation", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit6, Lit7, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit9, "sensor", Lit4);
        return runtime.setAndCoerceProperty$Ex(Lit0, Lit10, "Flame Score Tracker", Lit4);
    }

    public Object Points_Page$BackPressed() {
        runtime.setThisForm();
        return runtime.callYailPrimitive(runtime.open$Mnanother$Mnscreen, LList.list1("Screen1"), Lit11, "open another screen");
    }

    public Object Points_Page$Initialize() {
        runtime.setThisForm();
        runtime.setAndCoerceProperty$Ex(Lit14, Lit15, runtime.callComponentMethod(Lit16, Lit17, LList.list2("Score", "0"), Lit18), Lit4);
        runtime.setAndCoerceProperty$Ex(Lit19, Lit15, runtime.callComponentMethod(Lit16, Lit17, LList.list2("TScore", "0"), Lit20), Lit4);
        return runtime.setAndCoerceProperty$Ex(Lit21, Lit15, runtime.callYailPrimitive(AddOp.$Pl, LList.list2(runtime.callComponentMethod(Lit16, Lit17, LList.list2("Score", "0"), Lit22), runtime.callComponentMethod(Lit16, Lit17, LList.list2("TScore", "0"), Lit23)), Lit24, "+"), Lit4);
    }

    static Object lambda4() {
        runtime.setAndCoerceProperty$Ex(Lit28, Lit29, Boolean.TRUE, Lit30);
        runtime.setAndCoerceProperty$Ex(Lit28, Lit31, Lit32, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit28, Lit15, "Home", Lit4);
    }

    static Object lambda5() {
        runtime.setAndCoerceProperty$Ex(Lit28, Lit29, Boolean.TRUE, Lit30);
        runtime.setAndCoerceProperty$Ex(Lit28, Lit31, Lit32, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit28, Lit15, "Home", Lit4);
    }

    public Object Home$Click() {
        runtime.setThisForm();
        return runtime.callYailPrimitive(runtime.open$Mnanother$Mnscreen, LList.list1("Screen1"), Lit34, "open another screen");
    }

    static Object lambda6() {
        runtime.setAndCoerceProperty$Ex(Lit38, Lit6, Lit39, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit38, Lit40, Lit41, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit38, Lit42, Lit41, Lit8);
    }

    static Object lambda7() {
        runtime.setAndCoerceProperty$Ex(Lit38, Lit6, Lit39, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit38, Lit40, Lit41, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit38, Lit42, Lit41, Lit8);
    }

    static Object lambda8() {
        runtime.setAndCoerceProperty$Ex(Lit45, Lit40, Lit46, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit45, Lit42, Lit47, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit45, Lit48, "FlameScore.jpg", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit45, Lit49, Lit50, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit45, Lit51, Lit52, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit45, Lit53, Lit54, Lit8);
    }

    static Object lambda9() {
        runtime.setAndCoerceProperty$Ex(Lit45, Lit40, Lit46, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit45, Lit42, Lit47, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit45, Lit48, "FlameScore.jpg", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit45, Lit49, Lit50, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit45, Lit51, Lit52, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit45, Lit53, Lit54, Lit8);
    }

    static Object lambda10() {
        runtime.setAndCoerceProperty$Ex(Lit57, Lit58, Lit59, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit57, Lit60, Lit59, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit57, Lit6, Lit61, Lit8);
    }

    static Object lambda11() {
        runtime.setAndCoerceProperty$Ex(Lit57, Lit58, Lit59, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit57, Lit60, Lit59, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit57, Lit6, Lit61, Lit8);
    }

    static Object lambda12() {
        runtime.setAndCoerceProperty$Ex(Lit64, Lit65, Lit66, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit64, Lit15, "Drive Off Score:", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit64, Lit67, Lit32, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit64, Lit68, Lit69, Lit8);
    }

    static Object lambda13() {
        runtime.setAndCoerceProperty$Ex(Lit64, Lit65, Lit66, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit64, Lit15, "Drive Off Score:", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit64, Lit67, Lit32, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit64, Lit68, Lit69, Lit8);
    }

    static Object lambda14() {
        runtime.setAndCoerceProperty$Ex(Lit14, Lit65, Lit66, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit14, Lit15, "Text for DriveOff Score", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit14, Lit67, Lit32, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit14, Lit68, Lit72, Lit8);
    }

    static Object lambda15() {
        runtime.setAndCoerceProperty$Ex(Lit14, Lit65, Lit66, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit14, Lit15, "Text for DriveOff Score", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit14, Lit67, Lit32, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit14, Lit68, Lit72, Lit8);
    }

    static Object lambda16() {
        runtime.setAndCoerceProperty$Ex(Lit75, Lit58, Lit59, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit75, Lit60, Lit59, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit75, Lit6, Lit76, Lit8);
    }

    static Object lambda17() {
        runtime.setAndCoerceProperty$Ex(Lit75, Lit58, Lit59, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit75, Lit60, Lit59, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit75, Lit6, Lit76, Lit8);
    }

    static Object lambda18() {
        runtime.setAndCoerceProperty$Ex(Lit79, Lit65, Lit66, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit79, Lit15, "Texting Score:", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit79, Lit67, Lit32, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit79, Lit68, Lit80, Lit8);
    }

    static Object lambda19() {
        runtime.setAndCoerceProperty$Ex(Lit79, Lit65, Lit66, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit79, Lit15, "Texting Score:", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit79, Lit67, Lit32, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit79, Lit68, Lit80, Lit8);
    }

    static Object lambda20() {
        runtime.setAndCoerceProperty$Ex(Lit19, Lit65, Lit66, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit19, Lit15, "Text for Texting Score", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit19, Lit67, Lit32, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit19, Lit68, Lit83, Lit8);
    }

    static Object lambda21() {
        runtime.setAndCoerceProperty$Ex(Lit19, Lit65, Lit66, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit19, Lit15, "Text for Texting Score", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit19, Lit67, Lit32, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit19, Lit68, Lit83, Lit8);
    }

    static Object lambda22() {
        runtime.setAndCoerceProperty$Ex(Lit86, Lit58, Lit59, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit86, Lit60, Lit87, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit86, Lit6, Lit88, Lit8);
    }

    static Object lambda23() {
        runtime.setAndCoerceProperty$Ex(Lit86, Lit58, Lit59, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit86, Lit60, Lit87, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit86, Lit6, Lit88, Lit8);
    }

    static Object lambda24() {
        runtime.setAndCoerceProperty$Ex(Lit91, Lit6, Lit92, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit91, Lit65, Lit66, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit91, Lit15, "Overall Flame Score:", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit91, Lit67, Lit32, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit91, Lit68, Lit93, Lit8);
    }

    static Object lambda25() {
        runtime.setAndCoerceProperty$Ex(Lit91, Lit6, Lit92, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit91, Lit65, Lit66, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit91, Lit15, "Overall Flame Score:", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit91, Lit67, Lit32, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit91, Lit68, Lit93, Lit8);
    }

    static Object lambda26() {
        runtime.setAndCoerceProperty$Ex(Lit21, Lit6, Lit96, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit21, Lit65, Lit66, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit21, Lit15, "Text for Total", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit21, Lit67, Lit32, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit21, Lit68, Lit97, Lit8);
    }

    static Object lambda27() {
        runtime.setAndCoerceProperty$Ex(Lit21, Lit6, Lit96, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit21, Lit65, Lit66, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit21, Lit15, "Text for Total", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit21, Lit67, Lit32, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit21, Lit68, Lit97, Lit8);
    }

    static Object lambda28() {
        runtime.setAndCoerceProperty$Ex(Lit100, Lit6, Lit101, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit100, Lit29, Boolean.TRUE, Lit30);
        runtime.setAndCoerceProperty$Ex(Lit100, Lit102, Lit32, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit100, Lit42, Lit41, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit100, Lit31, Lit32, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit100, Lit15, "Share", Lit4);
        return runtime.setAndCoerceProperty$Ex(Lit100, Lit68, Lit103, Lit8);
    }

    static Object lambda29() {
        runtime.setAndCoerceProperty$Ex(Lit100, Lit6, Lit101, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit100, Lit29, Boolean.TRUE, Lit30);
        runtime.setAndCoerceProperty$Ex(Lit100, Lit102, Lit32, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit100, Lit42, Lit41, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit100, Lit31, Lit32, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit100, Lit15, "Share", Lit4);
        return runtime.setAndCoerceProperty$Ex(Lit100, Lit68, Lit103, Lit8);
    }

    public Object Share$Click() {
        runtime.setThisForm();
        return runtime.callComponentMethod(Lit105, Lit106, LList.list1(runtime.callYailPrimitive(strings.string$Mnappend, LList.list3("Try to beat my flame score of ", runtime.getProperty$1(Lit21, Lit15), "on Drive Off: A safer way to drive"), Lit107, "join")), Lit108);
    }

    public void androidLogForm(Object message) {
    }

    public void addToFormEnvironment(Symbol name, Object object) {
        androidLogForm(Format.formatToString(0, "Adding ~A to env ~A with value ~A", name, this.form$Mnenvironment, object));
        this.form$Mnenvironment.put(name, object);
    }

    public Object lookupInFormEnvironment(Symbol name, Object default$Mnvalue) {
        boolean x = ((this.form$Mnenvironment == null ? 1 : 0) + 1) & 1;
        if (x) {
            if (!this.form$Mnenvironment.isBound(name)) {
                return default$Mnvalue;
            }
        } else if (!x) {
            return default$Mnvalue;
        }
        return this.form$Mnenvironment.get(name);
    }

    public boolean isBoundInFormEnvironment(Symbol name) {
        return this.form$Mnenvironment.isBound(name);
    }

    public void addToGlobalVarEnvironment(Symbol name, Object object) {
        androidLogForm(Format.formatToString(0, "Adding ~A to env ~A with value ~A", name, this.global$Mnvar$Mnenvironment, object));
        this.global$Mnvar$Mnenvironment.put(name, object);
    }

    public void addToEvents(Object component$Mnname, Object event$Mnname) {
        this.events$Mnto$Mnregister = lists.cons(lists.cons(component$Mnname, event$Mnname), this.events$Mnto$Mnregister);
    }

    public void addToComponents(Object container$Mnname, Object component$Mntype, Object component$Mnname, Object init$Mnthunk) {
        this.components$Mnto$Mncreate = lists.cons(LList.list4(container$Mnname, component$Mntype, component$Mnname, init$Mnthunk), this.components$Mnto$Mncreate);
    }

    public void addToGlobalVars(Object var, Object val$Mnthunk) {
        this.global$Mnvars$Mnto$Mncreate = lists.cons(LList.list2(var, val$Mnthunk), this.global$Mnvars$Mnto$Mncreate);
    }

    public void addToFormDoAfterCreation(Object thunk) {
        this.form$Mndo$Mnafter$Mncreation = lists.cons(thunk, this.form$Mndo$Mnafter$Mncreation);
    }

    public void sendError(Object error) {
        RetValManager.sendError(error == null ? null : error.toString());
    }

    public void processException(Object ex) {
        Object apply1 = Scheme.applyToArgs.apply1(GetNamedPart.getNamedPart.apply2(ex, Lit1));
        RuntimeErrorAlert.alert(this, apply1 == null ? null : apply1.toString(), ex instanceof YailRuntimeError ? ((YailRuntimeError) ex).getErrorType() : "Runtime Error", "End Application");
    }

    public boolean dispatchEvent(Component componentObject, String registeredComponentName, String eventName, Object[] args) {
        SimpleSymbol registeredObject = misc.string$To$Symbol(registeredComponentName);
        if (!isBoundInFormEnvironment(registeredObject)) {
            EventDispatcher.unregisterEventForDelegation(this, registeredComponentName, eventName);
            return false;
        } else if (lookupInFormEnvironment(registeredObject) != componentObject) {
            return false;
        } else {
            try {
                Scheme.apply.apply2(lookupHandler(registeredComponentName, eventName), LList.makeList(args, 0));
                return true;
            } catch (Throwable exception) {
                androidLogForm(exception.getMessage());
                exception.printStackTrace();
                processException(exception);
                return false;
            }
        }
    }

    public Object lookupHandler(Object componentName, Object eventName) {
        String str = null;
        String obj = componentName == null ? null : componentName.toString();
        if (eventName != null) {
            str = eventName.toString();
        }
        return lookupInFormEnvironment(misc.string$To$Symbol(EventDispatcher.makeFullEventName(obj, str)));
    }

    public void $define() {
        Language.setDefaults(Scheme.getInstance());
        try {
            run();
        } catch (Exception exception) {
            androidLogForm(exception.getMessage());
            processException(exception);
        }
        Points_Page = this;
        addToFormEnvironment(Lit0, this);
        Object obj = this.events$Mnto$Mnregister;
        while (obj != LList.Empty) {
            try {
                Pair arg0 = (Pair) obj;
                Object event$Mninfo = arg0.getCar();
                Object apply1 = lists.car.apply1(event$Mninfo);
                String obj2 = apply1 == null ? null : apply1.toString();
                Object apply12 = lists.cdr.apply1(event$Mninfo);
                EventDispatcher.registerEventForDelegation(this, obj2, apply12 == null ? null : apply12.toString());
                obj = arg0.getCdr();
            } catch (ClassCastException e) {
                throw new WrongType(e, "arg0", -2, obj);
            }
        }
        addToGlobalVars(Lit2, lambda$Fn1);
        Points_Page closureEnv = this;
        obj = lists.reverse(this.global$Mnvars$Mnto$Mncreate);
        while (obj != LList.Empty) {
            try {
                arg0 = (Pair) obj;
                Object var$Mnval = arg0.getCar();
                Object var = lists.car.apply1(var$Mnval);
                addToGlobalVarEnvironment((Symbol) var, Scheme.applyToArgs.apply1(lists.cadr.apply1(var$Mnval)));
                obj = arg0.getCdr();
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "arg0", -2, obj);
            } catch (ClassCastException e22) {
                throw new WrongType(e22, "arg0", -2, obj);
            } catch (ClassCastException e222) {
                throw new WrongType(e222, "add-to-form-environment", 0, component$Mnname);
            } catch (ClassCastException e3) {
                throw new WrongType(e3, "lookup-in-form-environment", 0, apply1);
            } catch (ClassCastException e2222) {
                throw new WrongType(e2222, "arg0", -2, obj);
            } catch (ClassCastException e22222) {
                throw new WrongType(e22222, "arg0", -2, obj);
            } catch (ClassCastException e222222) {
                throw new WrongType(e222222, "add-to-global-var-environment", 0, var);
            } catch (ClassCastException e2222222) {
                throw new WrongType(e2222222, "arg0", -2, obj);
            } catch (YailRuntimeError exception2) {
                processException(exception2);
                return;
            }
        }
        obj = lists.reverse(this.form$Mndo$Mnafter$Mncreation);
        while (obj != LList.Empty) {
            arg0 = (Pair) obj;
            misc.force(arg0.getCar());
            obj = arg0.getCdr();
        }
        LList component$Mndescriptors = lists.reverse(this.components$Mnto$Mncreate);
        closureEnv = this;
        obj = component$Mndescriptors;
        while (obj != LList.Empty) {
            arg0 = (Pair) obj;
            Object component$Mninfo = arg0.getCar();
            Object component$Mnname = lists.caddr.apply1(component$Mninfo);
            lists.cadddr.apply1(component$Mninfo);
            Object component$Mnobject = Invoke.make.apply2(lists.cadr.apply1(component$Mninfo), lookupInFormEnvironment((Symbol) lists.car.apply1(component$Mninfo)));
            SlotSet.set$Mnfield$Ex.apply3(this, component$Mnname, component$Mnobject);
            addToFormEnvironment((Symbol) component$Mnname, component$Mnobject);
            obj = arg0.getCdr();
        }
        obj = component$Mndescriptors;
        while (obj != LList.Empty) {
            arg0 = (Pair) obj;
            component$Mninfo = arg0.getCar();
            lists.caddr.apply1(component$Mninfo);
            Boolean init$Mnthunk = lists.cadddr.apply1(component$Mninfo);
            if (init$Mnthunk != Boolean.FALSE) {
                Scheme.applyToArgs.apply1(init$Mnthunk);
            }
            obj = arg0.getCdr();
        }
        obj = component$Mndescriptors;
        while (obj != LList.Empty) {
            arg0 = (Pair) obj;
            component$Mninfo = arg0.getCar();
            component$Mnname = lists.caddr.apply1(component$Mninfo);
            lists.cadddr.apply1(component$Mninfo);
            callInitialize(SlotGet.field.apply2(this, component$Mnname));
            obj = arg0.getCdr();
        }
    }

    public static SimpleSymbol lambda1symbolAppend$V(Object[] argsArray) {
        Object car;
        LList symbols = LList.makeList(argsArray, 0);
        Procedure procedure = Scheme.apply;
        ModuleMethod moduleMethod = strings.string$Mnappend;
        Pair result = LList.Empty;
        Object arg0 = symbols;
        while (arg0 != LList.Empty) {
            try {
                Pair arg02 = (Pair) arg0;
                Object arg03 = arg02.getCdr();
                car = arg02.getCar();
                try {
                    result = Pair.make(misc.symbol$To$String((Symbol) car), result);
                    arg0 = arg03;
                } catch (ClassCastException e) {
                    throw new WrongType(e, "symbol->string", 1, car);
                }
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "arg0", -2, arg0);
            }
        }
        car = procedure.apply2(moduleMethod, LList.reverseInPlace(result));
        try {
            return misc.string$To$Symbol((CharSequence) car);
        } catch (ClassCastException e3) {
            throw new WrongType(e3, "string->symbol", 1, car);
        }
    }

    static Object lambda2() {
        return null;
    }
}
