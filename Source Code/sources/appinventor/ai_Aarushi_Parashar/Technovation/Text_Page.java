package appinventor.ai_Aarushi_Parashar.Technovation;

import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Canvas;
import com.google.appinventor.components.runtime.CheckBox;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.ImageSprite;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.LocationSensor;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.TextToSpeech;
import com.google.appinventor.components.runtime.Texting;
import com.google.appinventor.components.runtime.TinyDB;
import com.google.appinventor.components.runtime.VerticalArrangement;
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

/* compiled from: Text_Page.yail */
public class Text_Page extends Form implements Runnable {
    static final SimpleSymbol Lit0 = ((SimpleSymbol) new SimpleSymbol("Text_Page").readResolve());
    static final SimpleSymbol Lit1 = ((SimpleSymbol) new SimpleSymbol("getMessage").readResolve());
    static final SimpleSymbol Lit10 = ((SimpleSymbol) new SimpleSymbol("TinyDB1").readResolve());
    static final FString Lit100 = new FString("com.google.appinventor.components.runtime.Texting");
    static final FString Lit101 = new FString("com.google.appinventor.components.runtime.Texting");
    static final SimpleSymbol Lit102 = ((SimpleSymbol) new SimpleSymbol("PhoneNumber").readResolve());
    static final SimpleSymbol Lit103 = ((SimpleSymbol) new SimpleSymbol("Message").readResolve());
    static final PairWithPosition Lit104 = PairWithPosition.make(Lit16, PairWithPosition.make(Lit16, PairWithPosition.make(Lit16, PairWithPosition.make(Lit16, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 573777), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 573772), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 573767), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 573761);
    static final SimpleSymbol Lit105 = ((SimpleSymbol) new SimpleSymbol("SendMessage").readResolve());
    static final PairWithPosition Lit106;
    static final SimpleSymbol Lit107 = ((SimpleSymbol) new SimpleSymbol("TextToSpeech1").readResolve());
    static final SimpleSymbol Lit108 = ((SimpleSymbol) new SimpleSymbol("Speak").readResolve());
    static final PairWithPosition Lit109 = PairWithPosition.make(Lit16, PairWithPosition.make(Lit16, PairWithPosition.make(Lit16, PairWithPosition.make(Lit16, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 574193), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 574188), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 574183), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 574177);
    static final SimpleSymbol Lit11 = ((SimpleSymbol) new SimpleSymbol("StoreValue").readResolve());
    static final PairWithPosition Lit110;
    static final SimpleSymbol Lit111 = ((SimpleSymbol) new SimpleSymbol("Texting1$MessageReceived").readResolve());
    static final SimpleSymbol Lit112 = ((SimpleSymbol) new SimpleSymbol("MessageReceived").readResolve());
    static final FString Lit113 = new FString("com.google.appinventor.components.runtime.TinyDB");
    static final FString Lit114 = new FString("com.google.appinventor.components.runtime.TinyDB");
    static final FString Lit115 = new FString("com.google.appinventor.components.runtime.TextToSpeech");
    static final FString Lit116 = new FString("com.google.appinventor.components.runtime.TextToSpeech");
    static final FString Lit117 = new FString("com.google.appinventor.components.runtime.LocationSensor");
    static final SimpleSymbol Lit118 = ((SimpleSymbol) new SimpleSymbol("MyLocationSensor").readResolve());
    static final FString Lit119 = new FString("com.google.appinventor.components.runtime.LocationSensor");
    static final PairWithPosition Lit12 = PairWithPosition.make(Lit16, PairWithPosition.make(Lit138, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 45287), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 45281);
    static final SimpleSymbol Lit120 = ((SimpleSymbol) new SimpleSymbol("CurrentAddress").readResolve());
    static final SimpleSymbol Lit121 = ((SimpleSymbol) new SimpleSymbol("MyLocationSensor$LocationChanged").readResolve());
    static final SimpleSymbol Lit122 = ((SimpleSymbol) new SimpleSymbol("LocationChanged").readResolve());
    static final FString Lit123 = new FString("com.google.appinventor.components.runtime.Notifier");
    static final SimpleSymbol Lit124 = ((SimpleSymbol) new SimpleSymbol("Notifier1").readResolve());
    static final FString Lit125 = new FString("com.google.appinventor.components.runtime.Notifier");
    static final SimpleSymbol Lit126 = ((SimpleSymbol) new SimpleSymbol("android-log-form").readResolve());
    static final SimpleSymbol Lit127 = ((SimpleSymbol) new SimpleSymbol("add-to-form-environment").readResolve());
    static final SimpleSymbol Lit128 = ((SimpleSymbol) new SimpleSymbol("lookup-in-form-environment").readResolve());
    static final SimpleSymbol Lit129 = ((SimpleSymbol) new SimpleSymbol("is-bound-in-form-environment").readResolve());
    static final PairWithPosition Lit13 = PairWithPosition.make(Lit25, PairWithPosition.make(Lit25, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 45172), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 45164);
    static final SimpleSymbol Lit130 = ((SimpleSymbol) new SimpleSymbol("add-to-global-var-environment").readResolve());
    static final SimpleSymbol Lit131 = ((SimpleSymbol) new SimpleSymbol("add-to-events").readResolve());
    static final SimpleSymbol Lit132 = ((SimpleSymbol) new SimpleSymbol("add-to-components").readResolve());
    static final SimpleSymbol Lit133 = ((SimpleSymbol) new SimpleSymbol("add-to-global-vars").readResolve());
    static final SimpleSymbol Lit134 = ((SimpleSymbol) new SimpleSymbol("add-to-form-do-after-creation").readResolve());
    static final SimpleSymbol Lit135 = ((SimpleSymbol) new SimpleSymbol("send-error").readResolve());
    static final SimpleSymbol Lit136 = ((SimpleSymbol) new SimpleSymbol("dispatchEvent").readResolve());
    static final SimpleSymbol Lit137 = ((SimpleSymbol) new SimpleSymbol("lookup-handler").readResolve());
    static final SimpleSymbol Lit138 = ((SimpleSymbol) new SimpleSymbol("any").readResolve());
    static final PairWithPosition Lit14 = PairWithPosition.make(Lit16, PairWithPosition.make(Lit138, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 45287), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 45281);
    static final SimpleSymbol Lit15 = ((SimpleSymbol) new SimpleSymbol("AppName").readResolve());
    static final SimpleSymbol Lit16;
    static final SimpleSymbol Lit17 = ((SimpleSymbol) new SimpleSymbol("ScreenOrientation").readResolve());
    static final SimpleSymbol Lit18 = ((SimpleSymbol) new SimpleSymbol("Title").readResolve());
    static final PairWithPosition Lit19 = PairWithPosition.make(Lit16, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 81998);
    static final SimpleSymbol Lit2 = ((SimpleSymbol) new SimpleSymbol("*the-null-value*").readResolve());
    static final SimpleSymbol Lit20 = ((SimpleSymbol) new SimpleSymbol("Text_Page$BackPressed").readResolve());
    static final SimpleSymbol Lit21 = ((SimpleSymbol) new SimpleSymbol("BackPressed").readResolve());
    static final SimpleSymbol Lit22 = ((SimpleSymbol) new SimpleSymbol("Texting1").readResolve());
    static final SimpleSymbol Lit23 = ((SimpleSymbol) new SimpleSymbol("ReceivingEnabled").readResolve());
    static final IntNum Lit24 = IntNum.make(3);
    static final SimpleSymbol Lit25;
    static final SimpleSymbol Lit26 = ((SimpleSymbol) new SimpleSymbol("Receiving_Enabled").readResolve());
    static final SimpleSymbol Lit27 = ((SimpleSymbol) new SimpleSymbol("Checked").readResolve());
    static final SimpleSymbol Lit28 = ((SimpleSymbol) new SimpleSymbol("GoogleVoiceEnabled").readResolve());
    static final SimpleSymbol Lit29 = ((SimpleSymbol) new SimpleSymbol(PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN).readResolve());
    static final SimpleSymbol Lit3 = ((SimpleSymbol) new SimpleSymbol("g$last_known_location").readResolve());
    static final PairWithPosition Lit30 = PairWithPosition.make(Lit138, PairWithPosition.make(Lit138, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 90402), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 90397);
    static final SimpleSymbol Lit31 = ((SimpleSymbol) new SimpleSymbol("GetValue").readResolve());
    static final PairWithPosition Lit32 = PairWithPosition.make(Lit16, PairWithPosition.make(Lit138, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 90690), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 90684);
    static final PairWithPosition Lit33 = PairWithPosition.make(Lit16, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 90821);
    static final PairWithPosition Lit34 = PairWithPosition.make(Lit25, PairWithPosition.make(Lit25, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 90850), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 90842);
    static final SimpleSymbol Lit35 = ((SimpleSymbol) new SimpleSymbol("myResponseTextBox").readResolve());
    static final SimpleSymbol Lit36 = ((SimpleSymbol) new SimpleSymbol("Text").readResolve());
    static final SimpleSymbol Lit37 = ((SimpleSymbol) new SimpleSymbol("Text_Page$Initialize").readResolve());
    static final SimpleSymbol Lit38 = ((SimpleSymbol) new SimpleSymbol("Initialize").readResolve());
    static final FString Lit39 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit4 = ((SimpleSymbol) new SimpleSymbol("g$response").readResolve());
    static final SimpleSymbol Lit40 = ((SimpleSymbol) new SimpleSymbol("Home").readResolve());
    static final SimpleSymbol Lit41 = ((SimpleSymbol) new SimpleSymbol("FontBold").readResolve());
    static final SimpleSymbol Lit42 = ((SimpleSymbol) new SimpleSymbol("Shape").readResolve());
    static final FString Lit43 = new FString("com.google.appinventor.components.runtime.Button");
    static final PairWithPosition Lit44 = PairWithPosition.make(Lit16, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 143438);
    static final SimpleSymbol Lit45 = ((SimpleSymbol) new SimpleSymbol("Home$Click").readResolve());
    static final SimpleSymbol Lit46 = ((SimpleSymbol) new SimpleSymbol("Click").readResolve());
    static final FString Lit47 = new FString("com.google.appinventor.components.runtime.VerticalArrangement");
    static final SimpleSymbol Lit48 = ((SimpleSymbol) new SimpleSymbol("VerticalArrangement1").readResolve());
    static final SimpleSymbol Lit49 = ((SimpleSymbol) new SimpleSymbol("AlignHorizontal").readResolve());
    static final SimpleSymbol Lit5 = ((SimpleSymbol) new SimpleSymbol("g$tscore").readResolve());
    static final SimpleSymbol Lit50 = ((SimpleSymbol) new SimpleSymbol("AlignVertical").readResolve());
    static final SimpleSymbol Lit51 = ((SimpleSymbol) new SimpleSymbol("BackgroundColor").readResolve());
    static final IntNum Lit52;
    static final SimpleSymbol Lit53 = ((SimpleSymbol) new SimpleSymbol("Height").readResolve());
    static final IntNum Lit54 = IntNum.make(-2);
    static final SimpleSymbol Lit55 = ((SimpleSymbol) new SimpleSymbol("Width").readResolve());
    static final FString Lit56 = new FString("com.google.appinventor.components.runtime.VerticalArrangement");
    static final FString Lit57 = new FString("com.google.appinventor.components.runtime.Canvas");
    static final SimpleSymbol Lit58 = ((SimpleSymbol) new SimpleSymbol("Canvas1").readResolve());
    static final IntNum Lit59;
    static final IntNum Lit6 = IntNum.make(0);
    static final FString Lit60 = new FString("com.google.appinventor.components.runtime.Canvas");
    static final FString Lit61 = new FString("com.google.appinventor.components.runtime.ImageSprite");
    static final SimpleSymbol Lit62 = ((SimpleSymbol) new SimpleSymbol("ImageSprite1").readResolve());
    static final IntNum Lit63 = IntNum.make(250);
    static final IntNum Lit64 = IntNum.make(400);
    static final SimpleSymbol Lit65 = ((SimpleSymbol) new SimpleSymbol("Picture").readResolve());
    static final SimpleSymbol Lit66 = ((SimpleSymbol) new SimpleSymbol("X").readResolve());
    static final IntNum Lit67 = IntNum.make(-39);
    static final SimpleSymbol Lit68 = ((SimpleSymbol) new SimpleSymbol("Y").readResolve());
    static final IntNum Lit69 = IntNum.make(-15);
    static final SimpleSymbol Lit7 = ((SimpleSymbol) new SimpleSymbol("p$procedure").readResolve());
    static final FString Lit70 = new FString("com.google.appinventor.components.runtime.ImageSprite");
    static final FString Lit71 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit72 = ((SimpleSymbol) new SimpleSymbol("PromptLabel").readResolve());
    static final IntNum Lit73;
    static final SimpleSymbol Lit74 = ((SimpleSymbol) new SimpleSymbol("TextAlignment").readResolve());
    static final SimpleSymbol Lit75 = ((SimpleSymbol) new SimpleSymbol("TextColor").readResolve());
    static final IntNum Lit76;
    static final FString Lit77 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit78 = new FString("com.google.appinventor.components.runtime.TextBox");
    static final SimpleSymbol Lit79 = ((SimpleSymbol) new SimpleSymbol("Hint").readResolve());
    static final IntNum Lit8 = IntNum.make(1);
    static final FString Lit80 = new FString("com.google.appinventor.components.runtime.TextBox");
    static final FString Lit81 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit82 = ((SimpleSymbol) new SimpleSymbol("SubmitButton").readResolve());
    static final FString Lit83 = new FString("com.google.appinventor.components.runtime.Button");
    static final PairWithPosition Lit84 = PairWithPosition.make(Lit16, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 438459);
    static final PairWithPosition Lit85 = PairWithPosition.make(Lit138, PairWithPosition.make(Lit138, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 438485), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 438480);
    static final PairWithPosition Lit86 = PairWithPosition.make(Lit16, PairWithPosition.make(Lit138, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 438635), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 438629);
    static final SimpleSymbol Lit87 = ((SimpleSymbol) new SimpleSymbol("SubmitButton$Click").readResolve());
    static final FString Lit88 = new FString("com.google.appinventor.components.runtime.CheckBox");
    static final SimpleSymbol Lit89 = ((SimpleSymbol) new SimpleSymbol("FontItalic").readResolve());
    static final PairWithPosition Lit9 = PairWithPosition.make(Lit25, PairWithPosition.make(Lit25, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 45172), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 45164);
    static final IntNum Lit90;
    static final FString Lit91 = new FString("com.google.appinventor.components.runtime.CheckBox");
    static final PairWithPosition Lit92 = PairWithPosition.make(Lit138, PairWithPosition.make(Lit138, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 491635), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 491630);
    static final SimpleSymbol Lit93 = ((SimpleSymbol) new SimpleSymbol("Receiving_Enabled$Changed").readResolve());
    static final SimpleSymbol Lit94 = ((SimpleSymbol) new SimpleSymbol("Changed").readResolve());
    static final FString Lit95 = new FString("com.google.appinventor.components.runtime.CheckBox");
    static final SimpleSymbol Lit96 = ((SimpleSymbol) new SimpleSymbol("Google_Voice").readResolve());
    static final IntNum Lit97;
    static final FString Lit98 = new FString("com.google.appinventor.components.runtime.CheckBox");
    static final SimpleSymbol Lit99 = ((SimpleSymbol) new SimpleSymbol("Google_Voice$Changed").readResolve());
    public static Text_Page Text_Page;
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
    public CheckBox Google_Voice;
    public final ModuleMethod Google_Voice$Changed;
    public Button Home;
    public final ModuleMethod Home$Click;
    public ImageSprite ImageSprite1;
    public LocationSensor MyLocationSensor;
    public final ModuleMethod MyLocationSensor$LocationChanged;
    public Notifier Notifier1;
    public Label PromptLabel;
    public CheckBox Receiving_Enabled;
    public final ModuleMethod Receiving_Enabled$Changed;
    public Button SubmitButton;
    public final ModuleMethod SubmitButton$Click;
    public TextToSpeech TextToSpeech1;
    public final ModuleMethod Text_Page$BackPressed;
    public final ModuleMethod Text_Page$Initialize;
    public Texting Texting1;
    public final ModuleMethod Texting1$MessageReceived;
    public TinyDB TinyDB1;
    public VerticalArrangement VerticalArrangement1;
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
    public TextBox myResponseTextBox;
    public final ModuleMethod process$Mnexception;
    public final ModuleMethod send$Mnerror;

    /* compiled from: Text_Page.yail */
    public class frame extends ModuleBody {
        Text_Page $main = this;

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 49 ? this.$main.MyLocationSensor$LocationChanged(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 49) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            switch (moduleMethod.selector) {
                case 15:
                    return Text_Page.lambda2();
                case 16:
                    this.$main.$define();
                    return Values.empty;
                case 17:
                    return Text_Page.lambda3();
                case 18:
                    return Text_Page.lambda4();
                case 19:
                    return Text_Page.lambda5();
                case 20:
                    return Text_Page.lambda6();
                case 21:
                    return Text_Page.lambda8();
                case 22:
                    return Text_Page.lambda7();
                case 23:
                    return Text_Page.lambda9();
                case 24:
                    return this.$main.Text_Page$BackPressed();
                case 25:
                    return this.$main.Text_Page$Initialize();
                case 26:
                    return Text_Page.lambda10();
                case 27:
                    return Text_Page.lambda11();
                case 28:
                    return this.$main.Home$Click();
                case 29:
                    return Text_Page.lambda12();
                case 30:
                    return Text_Page.lambda13();
                case 31:
                    return Text_Page.lambda14();
                case 32:
                    return Text_Page.lambda15();
                case 33:
                    return Text_Page.lambda16();
                case 34:
                    return Text_Page.lambda17();
                case 35:
                    return Text_Page.lambda18();
                case 36:
                    return Text_Page.lambda19();
                case 37:
                    return Text_Page.lambda20();
                case 38:
                    return Text_Page.lambda21();
                case 39:
                    return Text_Page.lambda22();
                case 40:
                    return Text_Page.lambda23();
                case 41:
                    return this.$main.SubmitButton$Click();
                case XDataType.NMTOKEN_TYPE_CODE /*42*/:
                    return Text_Page.lambda24();
                case XDataType.NAME_TYPE_CODE /*43*/:
                    return Text_Page.lambda25();
                case XDataType.NCNAME_TYPE_CODE /*44*/:
                    return this.$main.Receiving_Enabled$Changed();
                case XDataType.ID_TYPE_CODE /*45*/:
                    return Text_Page.lambda26();
                case XDataType.IDREF_TYPE_CODE /*46*/:
                    return Text_Page.lambda27();
                case XDataType.ENTITY_TYPE_CODE /*47*/:
                    return this.$main.Google_Voice$Changed();
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
                    if (!(obj instanceof Text_Page)) {
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
                case 48:
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
                    if (!(obj instanceof Text_Page)) {
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
                case 48:
                    return this.$main.Texting1$MessageReceived(obj, obj2);
                default:
                    return super.apply2(moduleMethod, obj, obj2);
            }
        }
    }

    static {
        SimpleSymbol simpleSymbol = (SimpleSymbol) new SimpleSymbol(PropertyTypeConstants.PROPERTY_TYPE_TEXT).readResolve();
        Lit16 = simpleSymbol;
        Lit110 = PairWithPosition.make(simpleSymbol, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 574210);
        simpleSymbol = (SimpleSymbol) new SimpleSymbol("number").readResolve();
        Lit25 = simpleSymbol;
        Lit106 = PairWithPosition.make(simpleSymbol, PairWithPosition.make(Lit25, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 573963), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Text_Page.yail", 573955);
        int[] iArr = new int[2];
        iArr[0] = Component.COLOR_CYAN;
        Lit97 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = Component.COLOR_CYAN;
        Lit90 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit76 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -16777216;
        Lit73 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -16777216;
        Lit59 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -16777216;
        Lit52 = IntNum.make(iArr);
    }

    public Text_Page() {
        ModuleInfo.register(this);
        ModuleBody frame = new frame();
        this.android$Mnlog$Mnform = new ModuleMethod(frame, 1, Lit126, 4097);
        this.add$Mnto$Mnform$Mnenvironment = new ModuleMethod(frame, 2, Lit127, 8194);
        this.lookup$Mnin$Mnform$Mnenvironment = new ModuleMethod(frame, 3, Lit128, 8193);
        this.is$Mnbound$Mnin$Mnform$Mnenvironment = new ModuleMethod(frame, 5, Lit129, 4097);
        this.add$Mnto$Mnglobal$Mnvar$Mnenvironment = new ModuleMethod(frame, 6, Lit130, 8194);
        this.add$Mnto$Mnevents = new ModuleMethod(frame, 7, Lit131, 8194);
        this.add$Mnto$Mncomponents = new ModuleMethod(frame, 8, Lit132, 16388);
        this.add$Mnto$Mnglobal$Mnvars = new ModuleMethod(frame, 9, Lit133, 8194);
        this.add$Mnto$Mnform$Mndo$Mnafter$Mncreation = new ModuleMethod(frame, 10, Lit134, 4097);
        this.send$Mnerror = new ModuleMethod(frame, 11, Lit135, 4097);
        this.process$Mnexception = new ModuleMethod(frame, 12, "process-exception", 4097);
        this.dispatchEvent = new ModuleMethod(frame, 13, Lit136, 16388);
        this.lookup$Mnhandler = new ModuleMethod(frame, 14, Lit137, 8194);
        PropertySet moduleMethod = new ModuleMethod(frame, 15, null, 0);
        moduleMethod.setProperty("source-location", "/tmp/runtime6140171982161511911.scm:542");
        lambda$Fn1 = moduleMethod;
        this.$define = new ModuleMethod(frame, 16, "$define", 0);
        lambda$Fn2 = new ModuleMethod(frame, 17, null, 0);
        lambda$Fn3 = new ModuleMethod(frame, 18, null, 0);
        lambda$Fn4 = new ModuleMethod(frame, 19, null, 0);
        lambda$Fn5 = new ModuleMethod(frame, 20, null, 0);
        lambda$Fn7 = new ModuleMethod(frame, 21, null, 0);
        lambda$Fn6 = new ModuleMethod(frame, 22, null, 0);
        lambda$Fn8 = new ModuleMethod(frame, 23, null, 0);
        this.Text_Page$BackPressed = new ModuleMethod(frame, 24, Lit20, 0);
        this.Text_Page$Initialize = new ModuleMethod(frame, 25, Lit37, 0);
        lambda$Fn9 = new ModuleMethod(frame, 26, null, 0);
        lambda$Fn10 = new ModuleMethod(frame, 27, null, 0);
        this.Home$Click = new ModuleMethod(frame, 28, Lit45, 0);
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
        this.SubmitButton$Click = new ModuleMethod(frame, 41, Lit87, 0);
        lambda$Fn23 = new ModuleMethod(frame, 42, null, 0);
        lambda$Fn24 = new ModuleMethod(frame, 43, null, 0);
        this.Receiving_Enabled$Changed = new ModuleMethod(frame, 44, Lit93, 0);
        lambda$Fn25 = new ModuleMethod(frame, 45, null, 0);
        lambda$Fn26 = new ModuleMethod(frame, 46, null, 0);
        this.Google_Voice$Changed = new ModuleMethod(frame, 47, Lit99, 0);
        this.Texting1$MessageReceived = new ModuleMethod(frame, 48, Lit111, 8194);
        this.MyLocationSensor$LocationChanged = new ModuleMethod(frame, 49, Lit121, 12291);
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
            Text_Page = null;
            this.form$Mnname$Mnsymbol = Lit0;
            this.events$Mnto$Mnregister = LList.Empty;
            this.components$Mnto$Mncreate = LList.Empty;
            this.global$Mnvars$Mnto$Mncreate = LList.Empty;
            this.form$Mndo$Mnafter$Mncreation = LList.Empty;
            find = require.find("com.google.youngandroid.runtime");
            try {
                ((Runnable) find).run();
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addGlobalVarToCurrentFormEnvironment(Lit3, "unknown"), $result);
                } else {
                    addToGlobalVars(Lit3, lambda$Fn2);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addGlobalVarToCurrentFormEnvironment(Lit4, ""), $result);
                } else {
                    addToGlobalVars(Lit4, lambda$Fn3);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addGlobalVarToCurrentFormEnvironment(Lit5, Lit6), $result);
                } else {
                    addToGlobalVars(Lit5, lambda$Fn4);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addGlobalVarToCurrentFormEnvironment(Lit7, lambda$Fn5), $result);
                } else {
                    addToGlobalVars(Lit7, lambda$Fn6);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit15, "Technovation", Lit16);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit17, "user", Lit16);
                    Values.writeValues(runtime.setAndCoerceProperty$Ex(Lit0, Lit18, "Text Page", Lit16), $result);
                } else {
                    addToFormDoAfterCreation(new Promise(lambda$Fn8));
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit20, this.Text_Page$BackPressed);
                } else {
                    addToFormEnvironment(Lit20, this.Text_Page$BackPressed);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Text_Page", "BackPressed");
                } else {
                    addToEvents(Lit0, Lit21);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit37, this.Text_Page$Initialize);
                } else {
                    addToFormEnvironment(Lit37, this.Text_Page$Initialize);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Text_Page", "Initialize");
                } else {
                    addToEvents(Lit0, Lit38);
                }
                this.Home = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit39, Lit40, lambda$Fn9), $result);
                } else {
                    addToComponents(Lit0, Lit43, Lit40, lambda$Fn10);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit45, this.Home$Click);
                } else {
                    addToFormEnvironment(Lit45, this.Home$Click);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Home", "Click");
                } else {
                    addToEvents(Lit40, Lit46);
                }
                this.VerticalArrangement1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit47, Lit48, lambda$Fn11), $result);
                } else {
                    addToComponents(Lit0, Lit56, Lit48, lambda$Fn12);
                }
                this.Canvas1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit48, Lit57, Lit58, lambda$Fn13), $result);
                } else {
                    addToComponents(Lit48, Lit60, Lit58, lambda$Fn14);
                }
                this.ImageSprite1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit58, Lit61, Lit62, lambda$Fn15), $result);
                } else {
                    addToComponents(Lit58, Lit70, Lit62, lambda$Fn16);
                }
                this.PromptLabel = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit48, Lit71, Lit72, lambda$Fn17), $result);
                } else {
                    addToComponents(Lit48, Lit77, Lit72, lambda$Fn18);
                }
                this.myResponseTextBox = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit48, Lit78, Lit35, lambda$Fn19), $result);
                } else {
                    addToComponents(Lit48, Lit80, Lit35, lambda$Fn20);
                }
                this.SubmitButton = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit48, Lit81, Lit82, lambda$Fn21), $result);
                } else {
                    addToComponents(Lit48, Lit83, Lit82, lambda$Fn22);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit87, this.SubmitButton$Click);
                } else {
                    addToFormEnvironment(Lit87, this.SubmitButton$Click);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "SubmitButton", "Click");
                } else {
                    addToEvents(Lit82, Lit46);
                }
                this.Receiving_Enabled = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit48, Lit88, Lit26, lambda$Fn23), $result);
                } else {
                    addToComponents(Lit48, Lit91, Lit26, lambda$Fn24);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit93, this.Receiving_Enabled$Changed);
                } else {
                    addToFormEnvironment(Lit93, this.Receiving_Enabled$Changed);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Receiving_Enabled", "Changed");
                } else {
                    addToEvents(Lit26, Lit94);
                }
                this.Google_Voice = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit48, Lit95, Lit96, lambda$Fn25), $result);
                } else {
                    addToComponents(Lit48, Lit98, Lit96, lambda$Fn26);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit99, this.Google_Voice$Changed);
                } else {
                    addToFormEnvironment(Lit99, this.Google_Voice$Changed);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Google_Voice", "Changed");
                } else {
                    addToEvents(Lit96, Lit94);
                }
                this.Texting1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit100, Lit22, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit101, Lit22, Boolean.FALSE);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit111, this.Texting1$MessageReceived);
                } else {
                    addToFormEnvironment(Lit111, this.Texting1$MessageReceived);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Texting1", "MessageReceived");
                } else {
                    addToEvents(Lit22, Lit112);
                }
                this.TinyDB1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit113, Lit10, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit114, Lit10, Boolean.FALSE);
                }
                this.TextToSpeech1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit115, Lit107, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit116, Lit107, Boolean.FALSE);
                }
                this.MyLocationSensor = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit117, Lit118, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit119, Lit118, Boolean.FALSE);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit121, this.MyLocationSensor$LocationChanged);
                } else {
                    addToFormEnvironment(Lit121, this.MyLocationSensor$LocationChanged);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "MyLocationSensor", "LocationChanged");
                } else {
                    addToEvents(Lit118, Lit122);
                }
                this.Notifier1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit123, Lit124, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit125, Lit124, Boolean.FALSE);
                }
                runtime.initRuntime();
            } catch (ClassCastException e) {
                throw new WrongType(e, "java.lang.Runnable.run()", 1, find);
            }
        } catch (ClassCastException e2) {
            throw new WrongType(e2, "java.lang.Runnable.run()", 1, find);
        }
    }

    static String lambda3() {
        return "unknown";
    }

    static String lambda4() {
        return "";
    }

    static IntNum lambda5() {
        return Lit6;
    }

    static Object lambda6() {
        runtime.addGlobalVarToCurrentFormEnvironment(Lit5, runtime.callYailPrimitive(AddOp.$Pl, LList.list2(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit5, runtime.$Stthe$Mnnull$Mnvalue$St), Lit8), Lit9, "+"));
        return runtime.callComponentMethod(Lit10, Lit11, LList.list2("TScore", runtime.lookupGlobalVarInCurrentFormEnvironment(Lit5, runtime.$Stthe$Mnnull$Mnvalue$St)), Lit12);
    }

    static Procedure lambda7() {
        return lambda$Fn7;
    }

    static Object lambda8() {
        runtime.addGlobalVarToCurrentFormEnvironment(Lit5, runtime.callYailPrimitive(AddOp.$Pl, LList.list2(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit5, runtime.$Stthe$Mnnull$Mnvalue$St), Lit8), Lit13, "+"));
        return runtime.callComponentMethod(Lit10, Lit11, LList.list2("TScore", runtime.lookupGlobalVarInCurrentFormEnvironment(Lit5, runtime.$Stthe$Mnnull$Mnvalue$St)), Lit14);
    }

    static Object lambda9() {
        runtime.setAndCoerceProperty$Ex(Lit0, Lit15, "Technovation", Lit16);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit17, "user", Lit16);
        return runtime.setAndCoerceProperty$Ex(Lit0, Lit18, "Text Page", Lit16);
    }

    public Object Text_Page$BackPressed() {
        runtime.setThisForm();
        return runtime.callYailPrimitive(runtime.open$Mnanother$Mnscreen, LList.list1("Screen1"), Lit19, "open another screen");
    }

    public Object Text_Page$Initialize() {
        runtime.setThisForm();
        runtime.setAndCoerceProperty$Ex(Lit22, Lit23, Lit24, Lit25);
        runtime.setAndCoerceProperty$Ex(Lit26, Lit27, runtime.getProperty$1(Lit22, Lit28), Lit29);
        if (runtime.callYailPrimitive(runtime.yail$Mnnot$Mnequal$Qu, LList.list2(runtime.getProperty$1(Lit22, Lit23), Lit8), Lit30, "=") != Boolean.FALSE) {
            runtime.setAndCoerceProperty$Ex(Lit26, Lit27, Boolean.TRUE, Lit29);
        } else {
            runtime.setAndCoerceProperty$Ex(Lit26, Lit27, Boolean.FALSE, Lit29);
        }
        runtime.addGlobalVarToCurrentFormEnvironment(Lit4, runtime.callComponentMethod(Lit10, Lit31, LList.list2("ResponseMessage", "No Response"), Lit32));
        return runtime.callYailPrimitive(Scheme.numGrt, LList.list2(runtime.callYailPrimitive(strings.string$Mnlength, LList.list1(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit4, runtime.$Stthe$Mnnull$Mnvalue$St)), Lit33, "length"), Lit6), Lit34, ">") != Boolean.FALSE ? runtime.setAndCoerceProperty$Ex(Lit35, Lit36, runtime.lookupGlobalVarInCurrentFormEnvironment(Lit4, runtime.$Stthe$Mnnull$Mnvalue$St), Lit16) : Values.empty;
    }

    static Object lambda10() {
        runtime.setAndCoerceProperty$Ex(Lit40, Lit41, Boolean.TRUE, Lit29);
        runtime.setAndCoerceProperty$Ex(Lit40, Lit42, Lit8, Lit25);
        return runtime.setAndCoerceProperty$Ex(Lit40, Lit36, "Home", Lit16);
    }

    static Object lambda11() {
        runtime.setAndCoerceProperty$Ex(Lit40, Lit41, Boolean.TRUE, Lit29);
        runtime.setAndCoerceProperty$Ex(Lit40, Lit42, Lit8, Lit25);
        return runtime.setAndCoerceProperty$Ex(Lit40, Lit36, "Home", Lit16);
    }

    public Object Home$Click() {
        runtime.setThisForm();
        return runtime.callYailPrimitive(runtime.open$Mnanother$Mnscreen, LList.list1("Screen1"), Lit44, "open another screen");
    }

    static Object lambda12() {
        runtime.setAndCoerceProperty$Ex(Lit48, Lit49, Lit24, Lit25);
        runtime.setAndCoerceProperty$Ex(Lit48, Lit50, Lit24, Lit25);
        runtime.setAndCoerceProperty$Ex(Lit48, Lit51, Lit52, Lit25);
        runtime.setAndCoerceProperty$Ex(Lit48, Lit53, Lit54, Lit25);
        return runtime.setAndCoerceProperty$Ex(Lit48, Lit55, Lit54, Lit25);
    }

    static Object lambda13() {
        runtime.setAndCoerceProperty$Ex(Lit48, Lit49, Lit24, Lit25);
        runtime.setAndCoerceProperty$Ex(Lit48, Lit50, Lit24, Lit25);
        runtime.setAndCoerceProperty$Ex(Lit48, Lit51, Lit52, Lit25);
        runtime.setAndCoerceProperty$Ex(Lit48, Lit53, Lit54, Lit25);
        return runtime.setAndCoerceProperty$Ex(Lit48, Lit55, Lit54, Lit25);
    }

    static Object lambda14() {
        runtime.setAndCoerceProperty$Ex(Lit58, Lit51, Lit59, Lit25);
        runtime.setAndCoerceProperty$Ex(Lit58, Lit53, Lit54, Lit25);
        return runtime.setAndCoerceProperty$Ex(Lit58, Lit55, Lit54, Lit25);
    }

    static Object lambda15() {
        runtime.setAndCoerceProperty$Ex(Lit58, Lit51, Lit59, Lit25);
        runtime.setAndCoerceProperty$Ex(Lit58, Lit53, Lit54, Lit25);
        return runtime.setAndCoerceProperty$Ex(Lit58, Lit55, Lit54, Lit25);
    }

    static Object lambda16() {
        runtime.setAndCoerceProperty$Ex(Lit62, Lit53, Lit63, Lit25);
        runtime.setAndCoerceProperty$Ex(Lit62, Lit55, Lit64, Lit25);
        runtime.setAndCoerceProperty$Ex(Lit62, Lit65, "TextMode.jpg", Lit16);
        runtime.setAndCoerceProperty$Ex(Lit62, Lit66, Lit67, Lit25);
        return runtime.setAndCoerceProperty$Ex(Lit62, Lit68, Lit69, Lit25);
    }

    static Object lambda17() {
        runtime.setAndCoerceProperty$Ex(Lit62, Lit53, Lit63, Lit25);
        runtime.setAndCoerceProperty$Ex(Lit62, Lit55, Lit64, Lit25);
        runtime.setAndCoerceProperty$Ex(Lit62, Lit65, "TextMode.jpg", Lit16);
        runtime.setAndCoerceProperty$Ex(Lit62, Lit66, Lit67, Lit25);
        return runtime.setAndCoerceProperty$Ex(Lit62, Lit68, Lit69, Lit25);
    }

    static Object lambda18() {
        runtime.setAndCoerceProperty$Ex(Lit72, Lit51, Lit73, Lit25);
        runtime.setAndCoerceProperty$Ex(Lit72, Lit41, Boolean.TRUE, Lit29);
        runtime.setAndCoerceProperty$Ex(Lit72, Lit36, "Automatic Text Response", Lit16);
        runtime.setAndCoerceProperty$Ex(Lit72, Lit74, Lit8, Lit25);
        return runtime.setAndCoerceProperty$Ex(Lit72, Lit75, Lit76, Lit25);
    }

    static Object lambda19() {
        runtime.setAndCoerceProperty$Ex(Lit72, Lit51, Lit73, Lit25);
        runtime.setAndCoerceProperty$Ex(Lit72, Lit41, Boolean.TRUE, Lit29);
        runtime.setAndCoerceProperty$Ex(Lit72, Lit36, "Automatic Text Response", Lit16);
        runtime.setAndCoerceProperty$Ex(Lit72, Lit74, Lit8, Lit25);
        return runtime.setAndCoerceProperty$Ex(Lit72, Lit75, Lit76, Lit25);
    }

    static Object lambda20() {
        return runtime.setAndCoerceProperty$Ex(Lit35, Lit79, "Enter custom response", Lit16);
    }

    static Object lambda21() {
        return runtime.setAndCoerceProperty$Ex(Lit35, Lit79, "Enter custom response", Lit16);
    }

    static Object lambda22() {
        return runtime.setAndCoerceProperty$Ex(Lit82, Lit36, "Submit", Lit16);
    }

    static Object lambda23() {
        return runtime.setAndCoerceProperty$Ex(Lit82, Lit36, "Submit", Lit16);
    }

    public Object SubmitButton$Click() {
        runtime.setThisForm();
        Scheme.applyToArgs.apply1(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit7, runtime.$Stthe$Mnnull$Mnvalue$St));
        return runtime.callYailPrimitive(runtime.yail$Mnnot$Mnequal$Qu, LList.list2(runtime.callYailPrimitive(strings.string$Mnlength, LList.list1(runtime.getProperty$1(Lit35, Lit36)), Lit84, "length"), Lit6), Lit85, "=") != Boolean.FALSE ? runtime.callComponentMethod(Lit10, Lit11, LList.list2("ResponseMessage", runtime.getProperty$1(Lit35, Lit36)), Lit86) : Values.empty;
    }

    static Object lambda24() {
        runtime.setAndCoerceProperty$Ex(Lit26, Lit89, Boolean.TRUE, Lit29);
        runtime.setAndCoerceProperty$Ex(Lit26, Lit36, "Receiving Enabled", Lit16);
        return runtime.setAndCoerceProperty$Ex(Lit26, Lit75, Lit90, Lit25);
    }

    static Object lambda25() {
        runtime.setAndCoerceProperty$Ex(Lit26, Lit89, Boolean.TRUE, Lit29);
        runtime.setAndCoerceProperty$Ex(Lit26, Lit36, "Receiving Enabled", Lit16);
        return runtime.setAndCoerceProperty$Ex(Lit26, Lit75, Lit90, Lit25);
    }

    public Object Receiving_Enabled$Changed() {
        runtime.setThisForm();
        return runtime.callYailPrimitive(runtime.yail$Mnequal$Qu, LList.list2(runtime.getProperty$1(Lit26, Lit27), Boolean.TRUE), Lit92, "=") != Boolean.FALSE ? runtime.setAndCoerceProperty$Ex(Lit22, Lit23, Lit24, Lit25) : runtime.setAndCoerceProperty$Ex(Lit22, Lit23, Lit8, Lit25);
    }

    static Object lambda26() {
        runtime.setAndCoerceProperty$Ex(Lit96, Lit89, Boolean.TRUE, Lit29);
        runtime.setAndCoerceProperty$Ex(Lit96, Lit36, "Google Voice Enabled", Lit16);
        return runtime.setAndCoerceProperty$Ex(Lit96, Lit75, Lit97, Lit25);
    }

    static Object lambda27() {
        runtime.setAndCoerceProperty$Ex(Lit96, Lit89, Boolean.TRUE, Lit29);
        runtime.setAndCoerceProperty$Ex(Lit96, Lit36, "Google Voice Enabled", Lit16);
        return runtime.setAndCoerceProperty$Ex(Lit96, Lit75, Lit97, Lit25);
    }

    public Object Google_Voice$Changed() {
        runtime.setThisForm();
        return runtime.setAndCoerceProperty$Ex(Lit22, Lit28, runtime.getProperty$1(Lit96, Lit27), Lit29);
    }

    public Object Texting1$MessageReceived(Object $number, Object $messageText) {
        $number = runtime.sanitizeComponentData($number);
        $messageText = runtime.sanitizeComponentData($messageText);
        runtime.setThisForm();
        runtime.setAndCoerceProperty$Ex(Lit22, Lit102, $number, Lit16);
        runtime.setAndCoerceProperty$Ex(Lit22, Lit103, runtime.callYailPrimitive(strings.string$Mnappend, LList.list4(runtime.getProperty$1(Lit72, Lit36), runtime.getProperty$1(Lit35, Lit36), "My last known location is", runtime.lookupGlobalVarInCurrentFormEnvironment(Lit3, runtime.$Stthe$Mnnull$Mnvalue$St)), Lit104, "join"), Lit16);
        runtime.callComponentMethod(Lit22, Lit105, LList.Empty, LList.Empty);
        runtime.addGlobalVarToCurrentFormEnvironment(Lit5, runtime.callYailPrimitive(AddOp.$Pl, LList.list2(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit5, runtime.$Stthe$Mnnull$Mnvalue$St), Lit8), Lit106, "+"));
        return runtime.callComponentMethod(Lit107, Lit108, LList.list1(runtime.callYailPrimitive(strings.string$Mnappend, LList.list4("Message from ", $number, "reads", $messageText), Lit109, "join")), Lit110);
    }

    public Object MyLocationSensor$LocationChanged(Object $latitude, Object $longitude, Object $altitude) {
        runtime.sanitizeComponentData($latitude);
        runtime.sanitizeComponentData($longitude);
        runtime.sanitizeComponentData($altitude);
        runtime.setThisForm();
        return runtime.addGlobalVarToCurrentFormEnvironment(Lit3, runtime.getProperty$1(Lit118, Lit120));
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
        Text_Page = this;
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
        Text_Page closureEnv = this;
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
