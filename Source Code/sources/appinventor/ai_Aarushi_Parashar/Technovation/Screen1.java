package appinventor.ai_Aarushi_Parashar.Technovation;

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
import com.google.appinventor.components.runtime.TinyDB;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.RetValManager;
import com.google.appinventor.components.runtime.util.RuntimeErrorAlert;
import com.google.youngandroid.runtime;
import gnu.expr.Language;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.kawa.functions.Format;
import gnu.kawa.functions.GetNamedPart;
import gnu.kawa.reflect.Invoke;
import gnu.kawa.reflect.SlotGet;
import gnu.kawa.reflect.SlotSet;
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

/* compiled from: Screen1.yail */
public class Screen1 extends Form implements Runnable {
    static final SimpleSymbol Lit0 = ((SimpleSymbol) new SimpleSymbol("Screen1").readResolve());
    static final SimpleSymbol Lit1 = ((SimpleSymbol) new SimpleSymbol("getMessage").readResolve());
    static final SimpleSymbol Lit10 = ((SimpleSymbol) new SimpleSymbol("CloseScreenAnimation").readResolve());
    static final SimpleSymbol Lit11 = ((SimpleSymbol) new SimpleSymbol("OpenScreenAnimation").readResolve());
    static final SimpleSymbol Lit12 = ((SimpleSymbol) new SimpleSymbol("ScreenOrientation").readResolve());
    static final SimpleSymbol Lit13 = ((SimpleSymbol) new SimpleSymbol("Title").readResolve());
    static final FString Lit14 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit15 = ((SimpleSymbol) new SimpleSymbol("WelcomeLabel").readResolve());
    static final SimpleSymbol Lit16 = ((SimpleSymbol) new SimpleSymbol("FontBold").readResolve());
    static final SimpleSymbol Lit17 = ((SimpleSymbol) new SimpleSymbol(PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN).readResolve());
    static final SimpleSymbol Lit18 = ((SimpleSymbol) new SimpleSymbol("FontSize").readResolve());
    static final IntNum Lit19 = IntNum.make(36);
    static final SimpleSymbol Lit2 = ((SimpleSymbol) new SimpleSymbol("*the-null-value*").readResolve());
    static final SimpleSymbol Lit20 = ((SimpleSymbol) new SimpleSymbol("FontTypeface").readResolve());
    static final IntNum Lit21 = IntNum.make(2);
    static final SimpleSymbol Lit22 = ((SimpleSymbol) new SimpleSymbol("Text").readResolve());
    static final SimpleSymbol Lit23 = ((SimpleSymbol) new SimpleSymbol("TextAlignment").readResolve());
    static final IntNum Lit24 = IntNum.make(1);
    static final FString Lit25 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit26 = new FString("com.google.appinventor.components.runtime.Canvas");
    static final SimpleSymbol Lit27 = ((SimpleSymbol) new SimpleSymbol("Canvas1").readResolve());
    static final IntNum Lit28 = IntNum.make((int) Component.COLOR_NONE);
    static final SimpleSymbol Lit29 = ((SimpleSymbol) new SimpleSymbol("Height").readResolve());
    static final SimpleSymbol Lit3 = ((SimpleSymbol) new SimpleSymbol("AboutScreen").readResolve());
    static final IntNum Lit30 = IntNum.make(-2);
    static final SimpleSymbol Lit31 = ((SimpleSymbol) new SimpleSymbol("Width").readResolve());
    static final FString Lit32 = new FString("com.google.appinventor.components.runtime.Canvas");
    static final FString Lit33 = new FString("com.google.appinventor.components.runtime.ImageSprite");
    static final SimpleSymbol Lit34 = ((SimpleSymbol) new SimpleSymbol("ImageSprite1").readResolve());
    static final IntNum Lit35 = IntNum.make(250);
    static final SimpleSymbol Lit36 = ((SimpleSymbol) new SimpleSymbol("Picture").readResolve());
    static final SimpleSymbol Lit37 = ((SimpleSymbol) new SimpleSymbol("X").readResolve());
    static final IntNum Lit38 = IntNum.make(28);
    static final SimpleSymbol Lit39 = ((SimpleSymbol) new SimpleSymbol("Y").readResolve());
    static final SimpleSymbol Lit4;
    static final IntNum Lit40 = IntNum.make(-21);
    static final FString Lit41 = new FString("com.google.appinventor.components.runtime.ImageSprite");
    static final FString Lit42 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit43 = ((SimpleSymbol) new SimpleSymbol("Directions_Label").readResolve());
    static final SimpleSymbol Lit44 = ((SimpleSymbol) new SimpleSymbol("FontItalic").readResolve());
    static final IntNum Lit45 = IntNum.make(20);
    static final SimpleSymbol Lit46 = ((SimpleSymbol) new SimpleSymbol("TextColor").readResolve());
    static final IntNum Lit47;
    static final FString Lit48 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit49 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit5 = ((SimpleSymbol) new SimpleSymbol("AppName").readResolve());
    static final SimpleSymbol Lit50 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement1").readResolve());
    static final SimpleSymbol Lit51 = ((SimpleSymbol) new SimpleSymbol("AlignHorizontal").readResolve());
    static final IntNum Lit52 = IntNum.make(3);
    static final SimpleSymbol Lit53 = ((SimpleSymbol) new SimpleSymbol("AlignVertical").readResolve());
    static final FString Lit54 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit55 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit56 = ((SimpleSymbol) new SimpleSymbol("EnableButton").readResolve());
    static final IntNum Lit57;
    static final IntNum Lit58 = IntNum.make(18);
    static final SimpleSymbol Lit59 = ((SimpleSymbol) new SimpleSymbol("Shape").readResolve());
    static final SimpleSymbol Lit6 = ((SimpleSymbol) new SimpleSymbol("BackgroundColor").readResolve());
    static final IntNum Lit60;
    static final FString Lit61 = new FString("com.google.appinventor.components.runtime.Button");
    static final PairWithPosition Lit62 = PairWithPosition.make(Lit4, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen1.yail", 430158);
    static final SimpleSymbol Lit63 = ((SimpleSymbol) new SimpleSymbol("EnableButton$Click").readResolve());
    static final SimpleSymbol Lit64 = ((SimpleSymbol) new SimpleSymbol("Click").readResolve());
    static final FString Lit65 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit66 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement3").readResolve());
    static final FString Lit67 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit68 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit69 = ((SimpleSymbol) new SimpleSymbol("TextButton").readResolve());
    static final IntNum Lit7;
    static final IntNum Lit70;
    static final FString Lit71 = new FString("com.google.appinventor.components.runtime.Button");
    static final PairWithPosition Lit72 = PairWithPosition.make(Lit4, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen1.yail", 528464);
    static final SimpleSymbol Lit73 = ((SimpleSymbol) new SimpleSymbol("TextButton$Click").readResolve());
    static final FString Lit74 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit75 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement2").readResolve());
    static final FString Lit76 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit77 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit78 = ((SimpleSymbol) new SimpleSymbol("TrackYourPointsButton").readResolve());
    static final IntNum Lit79;
    static final SimpleSymbol Lit8 = ((SimpleSymbol) new SimpleSymbol("number").readResolve());
    static final IntNum Lit80;
    static final FString Lit81 = new FString("com.google.appinventor.components.runtime.Button");
    static final PairWithPosition Lit82;
    static final SimpleSymbol Lit83 = ((SimpleSymbol) new SimpleSymbol("TrackYourPointsButton$Click").readResolve());
    static final FString Lit84 = new FString("com.google.appinventor.components.runtime.TinyDB");
    static final SimpleSymbol Lit85 = ((SimpleSymbol) new SimpleSymbol("TinyDB1").readResolve());
    static final FString Lit86 = new FString("com.google.appinventor.components.runtime.TinyDB");
    static final SimpleSymbol Lit87 = ((SimpleSymbol) new SimpleSymbol("android-log-form").readResolve());
    static final SimpleSymbol Lit88 = ((SimpleSymbol) new SimpleSymbol("add-to-form-environment").readResolve());
    static final SimpleSymbol Lit89 = ((SimpleSymbol) new SimpleSymbol("lookup-in-form-environment").readResolve());
    static final SimpleSymbol Lit9 = ((SimpleSymbol) new SimpleSymbol("BackgroundImage").readResolve());
    static final SimpleSymbol Lit90 = ((SimpleSymbol) new SimpleSymbol("is-bound-in-form-environment").readResolve());
    static final SimpleSymbol Lit91 = ((SimpleSymbol) new SimpleSymbol("add-to-global-var-environment").readResolve());
    static final SimpleSymbol Lit92 = ((SimpleSymbol) new SimpleSymbol("add-to-events").readResolve());
    static final SimpleSymbol Lit93 = ((SimpleSymbol) new SimpleSymbol("add-to-components").readResolve());
    static final SimpleSymbol Lit94 = ((SimpleSymbol) new SimpleSymbol("add-to-global-vars").readResolve());
    static final SimpleSymbol Lit95 = ((SimpleSymbol) new SimpleSymbol("add-to-form-do-after-creation").readResolve());
    static final SimpleSymbol Lit96 = ((SimpleSymbol) new SimpleSymbol("send-error").readResolve());
    static final SimpleSymbol Lit97 = ((SimpleSymbol) new SimpleSymbol("dispatchEvent").readResolve());
    static final SimpleSymbol Lit98 = ((SimpleSymbol) new SimpleSymbol("lookup-handler").readResolve());
    public static Screen1 Screen1;
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
    public Label Directions_Label;
    public Button EnableButton;
    public final ModuleMethod EnableButton$Click;
    public HorizontalArrangement HorizontalArrangement1;
    public HorizontalArrangement HorizontalArrangement2;
    public HorizontalArrangement HorizontalArrangement3;
    public ImageSprite ImageSprite1;
    public Button TextButton;
    public final ModuleMethod TextButton$Click;
    public TinyDB TinyDB1;
    public Button TrackYourPointsButton;
    public final ModuleMethod TrackYourPointsButton$Click;
    public Label WelcomeLabel;
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

    /* compiled from: Screen1.yail */
    public class frame extends ModuleBody {
        Screen1 $main = this;

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
                    if (!(obj instanceof Screen1)) {
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
                    if (!(obj instanceof Screen1)) {
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
                    return Screen1.lambda2();
                case 16:
                    this.$main.$define();
                    return Values.empty;
                case 17:
                    return Screen1.lambda3();
                case 18:
                    return Screen1.lambda4();
                case 19:
                    return Screen1.lambda5();
                case 20:
                    return Screen1.lambda6();
                case 21:
                    return Screen1.lambda7();
                case 22:
                    return Screen1.lambda8();
                case 23:
                    return Screen1.lambda9();
                case 24:
                    return Screen1.lambda10();
                case 25:
                    return Screen1.lambda11();
                case 26:
                    return Screen1.lambda12();
                case 27:
                    return Screen1.lambda13();
                case 28:
                    return Screen1.lambda14();
                case 29:
                    return Screen1.lambda15();
                case 30:
                    return this.$main.EnableButton$Click();
                case 31:
                    return Screen1.lambda16();
                case 32:
                    return Screen1.lambda17();
                case 33:
                    return Screen1.lambda18();
                case 34:
                    return Screen1.lambda19();
                case 35:
                    return this.$main.TextButton$Click();
                case 36:
                    return Screen1.lambda20();
                case 37:
                    return Screen1.lambda21();
                case 38:
                    return Screen1.lambda22();
                case 39:
                    return Screen1.lambda23();
                case 40:
                    return this.$main.TrackYourPointsButton$Click();
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
                default:
                    return super.match0(moduleMethod, callContext);
            }
        }
    }

    static {
        SimpleSymbol simpleSymbol = (SimpleSymbol) new SimpleSymbol(PropertyTypeConstants.PROPERTY_TYPE_TEXT).readResolve();
        Lit4 = simpleSymbol;
        Lit82 = PairWithPosition.make(simpleSymbol, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen1.yail", 634962);
        int[] iArr = new int[2];
        iArr[0] = -1;
        Lit80 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = Component.COLOR_BLUE;
        Lit79 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = Component.COLOR_CYAN;
        Lit70 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit60 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = Component.COLOR_BLUE;
        Lit57 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = Component.COLOR_BLUE;
        Lit47 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -16777216;
        Lit7 = IntNum.make(iArr);
    }

    public Screen1() {
        ModuleInfo.register(this);
        ModuleBody frame = new frame();
        this.android$Mnlog$Mnform = new ModuleMethod(frame, 1, Lit87, 4097);
        this.add$Mnto$Mnform$Mnenvironment = new ModuleMethod(frame, 2, Lit88, 8194);
        this.lookup$Mnin$Mnform$Mnenvironment = new ModuleMethod(frame, 3, Lit89, 8193);
        this.is$Mnbound$Mnin$Mnform$Mnenvironment = new ModuleMethod(frame, 5, Lit90, 4097);
        this.add$Mnto$Mnglobal$Mnvar$Mnenvironment = new ModuleMethod(frame, 6, Lit91, 8194);
        this.add$Mnto$Mnevents = new ModuleMethod(frame, 7, Lit92, 8194);
        this.add$Mnto$Mncomponents = new ModuleMethod(frame, 8, Lit93, 16388);
        this.add$Mnto$Mnglobal$Mnvars = new ModuleMethod(frame, 9, Lit94, 8194);
        this.add$Mnto$Mnform$Mndo$Mnafter$Mncreation = new ModuleMethod(frame, 10, Lit95, 4097);
        this.send$Mnerror = new ModuleMethod(frame, 11, Lit96, 4097);
        this.process$Mnexception = new ModuleMethod(frame, 12, "process-exception", 4097);
        this.dispatchEvent = new ModuleMethod(frame, 13, Lit97, 16388);
        this.lookup$Mnhandler = new ModuleMethod(frame, 14, Lit98, 8194);
        PropertySet moduleMethod = new ModuleMethod(frame, 15, null, 0);
        moduleMethod.setProperty("source-location", "/tmp/runtime6140171982161511911.scm:542");
        lambda$Fn1 = moduleMethod;
        this.$define = new ModuleMethod(frame, 16, "$define", 0);
        lambda$Fn2 = new ModuleMethod(frame, 17, null, 0);
        lambda$Fn3 = new ModuleMethod(frame, 18, null, 0);
        lambda$Fn4 = new ModuleMethod(frame, 19, null, 0);
        lambda$Fn5 = new ModuleMethod(frame, 20, null, 0);
        lambda$Fn6 = new ModuleMethod(frame, 21, null, 0);
        lambda$Fn7 = new ModuleMethod(frame, 22, null, 0);
        lambda$Fn8 = new ModuleMethod(frame, 23, null, 0);
        lambda$Fn9 = new ModuleMethod(frame, 24, null, 0);
        lambda$Fn10 = new ModuleMethod(frame, 25, null, 0);
        lambda$Fn11 = new ModuleMethod(frame, 26, null, 0);
        lambda$Fn12 = new ModuleMethod(frame, 27, null, 0);
        lambda$Fn13 = new ModuleMethod(frame, 28, null, 0);
        lambda$Fn14 = new ModuleMethod(frame, 29, null, 0);
        this.EnableButton$Click = new ModuleMethod(frame, 30, Lit63, 0);
        lambda$Fn15 = new ModuleMethod(frame, 31, null, 0);
        lambda$Fn16 = new ModuleMethod(frame, 32, null, 0);
        lambda$Fn17 = new ModuleMethod(frame, 33, null, 0);
        lambda$Fn18 = new ModuleMethod(frame, 34, null, 0);
        this.TextButton$Click = new ModuleMethod(frame, 35, Lit73, 0);
        lambda$Fn19 = new ModuleMethod(frame, 36, null, 0);
        lambda$Fn20 = new ModuleMethod(frame, 37, null, 0);
        lambda$Fn21 = new ModuleMethod(frame, 38, null, 0);
        lambda$Fn22 = new ModuleMethod(frame, 39, null, 0);
        this.TrackYourPointsButton$Click = new ModuleMethod(frame, 40, Lit83, 0);
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
            Screen1 = null;
            this.form$Mnname$Mnsymbol = Lit0;
            this.events$Mnto$Mnregister = LList.Empty;
            this.components$Mnto$Mncreate = LList.Empty;
            this.global$Mnvars$Mnto$Mncreate = LList.Empty;
            this.form$Mndo$Mnafter$Mncreation = LList.Empty;
            find = require.find("com.google.youngandroid.runtime");
            try {
                ((Runnable) find).run();
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit3, "Drivie Off", Lit4);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit5, "DriveOff", Lit4);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit6, Lit7, Lit8);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit9, "minneapolisfantasymap__hero.png", Lit4);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit10, "slidehorizontal", Lit4);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit11, "zoom", Lit4);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit12, "sensor", Lit4);
                    Values.writeValues(runtime.setAndCoerceProperty$Ex(Lit0, Lit13, "Drive Off", Lit4), $result);
                } else {
                    addToFormDoAfterCreation(new Promise(lambda$Fn2));
                }
                this.WelcomeLabel = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit14, Lit15, lambda$Fn3), $result);
                } else {
                    addToComponents(Lit0, Lit25, Lit15, lambda$Fn4);
                }
                this.Canvas1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit26, Lit27, lambda$Fn5), $result);
                } else {
                    addToComponents(Lit0, Lit32, Lit27, lambda$Fn6);
                }
                this.ImageSprite1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit27, Lit33, Lit34, lambda$Fn7), $result);
                } else {
                    addToComponents(Lit27, Lit41, Lit34, lambda$Fn8);
                }
                this.Directions_Label = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit42, Lit43, lambda$Fn9), $result);
                } else {
                    addToComponents(Lit0, Lit48, Lit43, lambda$Fn10);
                }
                this.HorizontalArrangement1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit49, Lit50, lambda$Fn11), $result);
                } else {
                    addToComponents(Lit0, Lit54, Lit50, lambda$Fn12);
                }
                this.EnableButton = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit50, Lit55, Lit56, lambda$Fn13), $result);
                } else {
                    addToComponents(Lit50, Lit61, Lit56, lambda$Fn14);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit63, this.EnableButton$Click);
                } else {
                    addToFormEnvironment(Lit63, this.EnableButton$Click);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "EnableButton", "Click");
                } else {
                    addToEvents(Lit56, Lit64);
                }
                this.HorizontalArrangement3 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit65, Lit66, lambda$Fn15), $result);
                } else {
                    addToComponents(Lit0, Lit67, Lit66, lambda$Fn16);
                }
                this.TextButton = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit66, Lit68, Lit69, lambda$Fn17), $result);
                } else {
                    addToComponents(Lit66, Lit71, Lit69, lambda$Fn18);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit73, this.TextButton$Click);
                } else {
                    addToFormEnvironment(Lit73, this.TextButton$Click);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "TextButton", "Click");
                } else {
                    addToEvents(Lit69, Lit64);
                }
                this.HorizontalArrangement2 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit74, Lit75, lambda$Fn19), $result);
                } else {
                    addToComponents(Lit0, Lit76, Lit75, lambda$Fn20);
                }
                this.TrackYourPointsButton = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit75, Lit77, Lit78, lambda$Fn21), $result);
                } else {
                    addToComponents(Lit75, Lit81, Lit78, lambda$Fn22);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit83, this.TrackYourPointsButton$Click);
                } else {
                    addToFormEnvironment(Lit83, this.TrackYourPointsButton$Click);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "TrackYourPointsButton", "Click");
                } else {
                    addToEvents(Lit78, Lit64);
                }
                this.TinyDB1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit84, Lit85, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit86, Lit85, Boolean.FALSE);
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
        runtime.setAndCoerceProperty$Ex(Lit0, Lit3, "Drivie Off", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit5, "DriveOff", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit6, Lit7, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit9, "minneapolisfantasymap__hero.png", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit10, "slidehorizontal", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit11, "zoom", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit12, "sensor", Lit4);
        return runtime.setAndCoerceProperty$Ex(Lit0, Lit13, "Drive Off", Lit4);
    }

    static Object lambda4() {
        runtime.setAndCoerceProperty$Ex(Lit15, Lit16, Boolean.TRUE, Lit17);
        runtime.setAndCoerceProperty$Ex(Lit15, Lit18, Lit19, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit15, Lit20, Lit21, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit15, Lit22, "Welcome to Drive Off!", Lit4);
        return runtime.setAndCoerceProperty$Ex(Lit15, Lit23, Lit24, Lit8);
    }

    static Object lambda5() {
        runtime.setAndCoerceProperty$Ex(Lit15, Lit16, Boolean.TRUE, Lit17);
        runtime.setAndCoerceProperty$Ex(Lit15, Lit18, Lit19, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit15, Lit20, Lit21, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit15, Lit22, "Welcome to Drive Off!", Lit4);
        return runtime.setAndCoerceProperty$Ex(Lit15, Lit23, Lit24, Lit8);
    }

    static Object lambda6() {
        runtime.setAndCoerceProperty$Ex(Lit27, Lit6, Lit28, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit27, Lit29, Lit30, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit27, Lit31, Lit30, Lit8);
    }

    static Object lambda7() {
        runtime.setAndCoerceProperty$Ex(Lit27, Lit6, Lit28, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit27, Lit29, Lit30, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit27, Lit31, Lit30, Lit8);
    }

    static Object lambda8() {
        runtime.setAndCoerceProperty$Ex(Lit34, Lit29, Lit35, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit34, Lit31, Lit35, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit34, Lit36, "Logo(1).jpg", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit34, Lit37, Lit38, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit34, Lit39, Lit40, Lit8);
    }

    static Object lambda9() {
        runtime.setAndCoerceProperty$Ex(Lit34, Lit29, Lit35, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit34, Lit31, Lit35, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit34, Lit36, "Logo(1).jpg", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit34, Lit37, Lit38, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit34, Lit39, Lit40, Lit8);
    }

    static Object lambda10() {
        runtime.setAndCoerceProperty$Ex(Lit43, Lit16, Boolean.TRUE, Lit17);
        runtime.setAndCoerceProperty$Ex(Lit43, Lit44, Boolean.TRUE, Lit17);
        runtime.setAndCoerceProperty$Ex(Lit43, Lit18, Lit45, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit43, Lit22, "Press either Drive Off Mode or Easy TextMode Before Driving to get Started!", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit43, Lit23, Lit24, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit43, Lit46, Lit47, Lit8);
    }

    static Object lambda11() {
        runtime.setAndCoerceProperty$Ex(Lit43, Lit16, Boolean.TRUE, Lit17);
        runtime.setAndCoerceProperty$Ex(Lit43, Lit44, Boolean.TRUE, Lit17);
        runtime.setAndCoerceProperty$Ex(Lit43, Lit18, Lit45, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit43, Lit22, "Press either Drive Off Mode or Easy TextMode Before Driving to get Started!", Lit4);
        runtime.setAndCoerceProperty$Ex(Lit43, Lit23, Lit24, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit43, Lit46, Lit47, Lit8);
    }

    static Object lambda12() {
        runtime.setAndCoerceProperty$Ex(Lit50, Lit51, Lit52, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit50, Lit53, Lit21, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit50, Lit31, Lit30, Lit8);
    }

    static Object lambda13() {
        runtime.setAndCoerceProperty$Ex(Lit50, Lit51, Lit52, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit50, Lit53, Lit21, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit50, Lit31, Lit30, Lit8);
    }

    static Object lambda14() {
        runtime.setAndCoerceProperty$Ex(Lit56, Lit6, Lit57, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit56, Lit18, Lit58, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit56, Lit59, Lit24, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit56, Lit22, "Drive Off Mode", Lit4);
        return runtime.setAndCoerceProperty$Ex(Lit56, Lit46, Lit60, Lit8);
    }

    static Object lambda15() {
        runtime.setAndCoerceProperty$Ex(Lit56, Lit6, Lit57, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit56, Lit18, Lit58, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit56, Lit59, Lit24, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit56, Lit22, "Drive Off Mode", Lit4);
        return runtime.setAndCoerceProperty$Ex(Lit56, Lit46, Lit60, Lit8);
    }

    public Object EnableButton$Click() {
        runtime.setThisForm();
        return runtime.callYailPrimitive(runtime.open$Mnanother$Mnscreen, LList.list1("Screen2"), Lit62, "open another screen");
    }

    static Object lambda16() {
        runtime.setAndCoerceProperty$Ex(Lit66, Lit51, Lit52, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit66, Lit31, Lit30, Lit8);
    }

    static Object lambda17() {
        runtime.setAndCoerceProperty$Ex(Lit66, Lit51, Lit52, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit66, Lit31, Lit30, Lit8);
    }

    static Object lambda18() {
        runtime.setAndCoerceProperty$Ex(Lit69, Lit6, Lit70, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit69, Lit18, Lit58, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit69, Lit59, Lit24, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit69, Lit22, "Easy Text Mode", Lit4);
    }

    static Object lambda19() {
        runtime.setAndCoerceProperty$Ex(Lit69, Lit6, Lit70, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit69, Lit18, Lit58, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit69, Lit59, Lit24, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit69, Lit22, "Easy Text Mode", Lit4);
    }

    public Object TextButton$Click() {
        runtime.setThisForm();
        return runtime.callYailPrimitive(runtime.open$Mnanother$Mnscreen, LList.list1("Text_Page"), Lit72, "open another screen");
    }

    static Object lambda20() {
        runtime.setAndCoerceProperty$Ex(Lit75, Lit51, Lit52, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit75, Lit31, Lit30, Lit8);
    }

    static Object lambda21() {
        runtime.setAndCoerceProperty$Ex(Lit75, Lit51, Lit52, Lit8);
        return runtime.setAndCoerceProperty$Ex(Lit75, Lit31, Lit30, Lit8);
    }

    static Object lambda22() {
        runtime.setAndCoerceProperty$Ex(Lit78, Lit6, Lit79, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit78, Lit18, Lit58, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit78, Lit59, Lit24, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit78, Lit22, "Flames", Lit4);
        return runtime.setAndCoerceProperty$Ex(Lit78, Lit46, Lit80, Lit8);
    }

    static Object lambda23() {
        runtime.setAndCoerceProperty$Ex(Lit78, Lit6, Lit79, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit78, Lit18, Lit58, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit78, Lit59, Lit24, Lit8);
        runtime.setAndCoerceProperty$Ex(Lit78, Lit22, "Flames", Lit4);
        return runtime.setAndCoerceProperty$Ex(Lit78, Lit46, Lit80, Lit8);
    }

    public Object TrackYourPointsButton$Click() {
        runtime.setThisForm();
        return runtime.callYailPrimitive(runtime.open$Mnanother$Mnscreen, LList.list1("Points_Page"), Lit82, "open another screen");
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
        Screen1 = this;
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
        Screen1 closureEnv = this;
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
