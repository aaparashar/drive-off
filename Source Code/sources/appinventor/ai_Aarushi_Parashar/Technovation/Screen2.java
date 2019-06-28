package appinventor.ai_Aarushi_Parashar.Technovation;

import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.ActivityStarter;
import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Clock;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Image;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.LocationSensor;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.TableArrangement;
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
import gnu.kawa.functions.MultiplyOp;
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
import gnu.math.DFloNum;
import gnu.math.IntNum;
import gnu.text.PrettyWriter;
import kawa.lang.Promise;
import kawa.lib.lists;
import kawa.lib.misc;
import kawa.lib.numbers;
import kawa.lib.strings;
import kawa.standard.Scheme;
import kawa.standard.expt;
import kawa.standard.require;

/* compiled from: Screen2.yail */
public class Screen2 extends Form implements Runnable {
    static final SimpleSymbol Lit0 = ((SimpleSymbol) new SimpleSymbol("Screen2").readResolve());
    static final SimpleSymbol Lit1 = ((SimpleSymbol) new SimpleSymbol("getMessage").readResolve());
    static final PairWithPosition Lit10 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 32882), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 32874);
    static final SimpleSymbol Lit100 = ((SimpleSymbol) new SimpleSymbol("DistanceDisplay").readResolve());
    static final SimpleSymbol Lit101 = ((SimpleSymbol) new SimpleSymbol("TimerEnabled").readResolve());
    static final SimpleSymbol Lit102 = ((SimpleSymbol) new SimpleSymbol(PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN).readResolve());
    static final SimpleSymbol Lit103 = ((SimpleSymbol) new SimpleSymbol("Screen2$Initialize").readResolve());
    static final SimpleSymbol Lit104 = ((SimpleSymbol) new SimpleSymbol("Initialize").readResolve());
    static final FString Lit105 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit106 = ((SimpleSymbol) new SimpleSymbol("hbar1").readResolve());
    static final IntNum Lit107;
    static final SimpleSymbol Lit108 = ((SimpleSymbol) new SimpleSymbol("Height").readResolve());
    static final IntNum Lit109 = IntNum.make(5);
    static final PairWithPosition Lit11 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 32995), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 32989);
    static final SimpleSymbol Lit110 = ((SimpleSymbol) new SimpleSymbol("Width").readResolve());
    static final IntNum Lit111 = IntNum.make(-1100);
    static final SimpleSymbol Lit112 = ((SimpleSymbol) new SimpleSymbol("Visible").readResolve());
    static final FString Lit113 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit114 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit115 = ((SimpleSymbol) new SimpleSymbol("Home").readResolve());
    static final SimpleSymbol Lit116 = ((SimpleSymbol) new SimpleSymbol("FontBold").readResolve());
    static final SimpleSymbol Lit117 = ((SimpleSymbol) new SimpleSymbol("Shape").readResolve());
    static final FString Lit118 = new FString("com.google.appinventor.components.runtime.Button");
    static final PairWithPosition Lit119 = PairWithPosition.make(Lit69, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 221262);
    static final SimpleSymbol Lit12 = ((SimpleSymbol) new SimpleSymbol("g$r").readResolve());
    static final SimpleSymbol Lit120 = ((SimpleSymbol) new SimpleSymbol("Home$Click").readResolve());
    static final SimpleSymbol Lit121 = ((SimpleSymbol) new SimpleSymbol("Click").readResolve());
    static final FString Lit122 = new FString("com.google.appinventor.components.runtime.VerticalArrangement");
    static final SimpleSymbol Lit123 = ((SimpleSymbol) new SimpleSymbol("VerticalArrangement1").readResolve());
    static final SimpleSymbol Lit124 = ((SimpleSymbol) new SimpleSymbol("AlignHorizontal").readResolve());
    static final IntNum Lit125 = IntNum.make(3);
    static final SimpleSymbol Lit126 = ((SimpleSymbol) new SimpleSymbol("AlignVertical").readResolve());
    static final IntNum Lit127 = IntNum.make(-2);
    static final FString Lit128 = new FString("com.google.appinventor.components.runtime.VerticalArrangement");
    static final FString Lit129 = new FString("com.google.appinventor.components.runtime.Image");
    static final DFloNum Lit13 = DFloNum.make(3963.19d);
    static final SimpleSymbol Lit130 = ((SimpleSymbol) new SimpleSymbol("Image1").readResolve());
    static final IntNum Lit131 = IntNum.make(-1025);
    static final IntNum Lit132 = IntNum.make(-1025);
    static final SimpleSymbol Lit133 = ((SimpleSymbol) new SimpleSymbol("Picture").readResolve());
    static final SimpleSymbol Lit134 = ((SimpleSymbol) new SimpleSymbol("ScalePictureToFit").readResolve());
    static final FString Lit135 = new FString("com.google.appinventor.components.runtime.Image");
    static final FString Lit136 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit137 = ((SimpleSymbol) new SimpleSymbol("Label1").readResolve());
    static final SimpleSymbol Lit138 = ((SimpleSymbol) new SimpleSymbol("FontSize").readResolve());
    static final DFloNum Lit139 = DFloNum.make((double) 20);
    static final SimpleSymbol Lit14 = ((SimpleSymbol) new SimpleSymbol("p$c").readResolve());
    static final SimpleSymbol Lit140 = ((SimpleSymbol) new SimpleSymbol("TextColor").readResolve());
    static final IntNum Lit141;
    static final FString Lit142 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit143 = new FString("com.google.appinventor.components.runtime.Label");
    static final IntNum Lit144;
    static final IntNum Lit145 = IntNum.make(18);
    static final FString Lit146 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit147 = new FString("com.google.appinventor.components.runtime.Label");
    static final IntNum Lit148;
    static final DFloNum Lit149 = DFloNum.make((double) 12);
    static final SimpleSymbol Lit15 = ((SimpleSymbol) new SimpleSymbol("p$a").readResolve());
    static final SimpleSymbol Lit150 = ((SimpleSymbol) new SimpleSymbol("TextAlignment").readResolve());
    static final FString Lit151 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit152 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit153 = ((SimpleSymbol) new SimpleSymbol("OldAddress").readResolve());
    static final IntNum Lit154;
    static final FString Lit155 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit156 = new FString("com.google.appinventor.components.runtime.TableArrangement");
    static final SimpleSymbol Lit157 = ((SimpleSymbol) new SimpleSymbol("GPSTable").readResolve());
    static final SimpleSymbol Lit158 = ((SimpleSymbol) new SimpleSymbol("Columns").readResolve());
    static final FString Lit159 = new FString("com.google.appinventor.components.runtime.TableArrangement");
    static final PairWithPosition Lit16 = PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 41093);
    static final FString Lit160 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit161 = ((SimpleSymbol) new SimpleSymbol("Latitude_Label").readResolve());
    static final IntNum Lit162;
    static final SimpleSymbol Lit163 = ((SimpleSymbol) new SimpleSymbol("Column").readResolve());
    static final SimpleSymbol Lit164 = ((SimpleSymbol) new SimpleSymbol("Row").readResolve());
    static final FString Lit165 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit166 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit167 = ((SimpleSymbol) new SimpleSymbol("Longititude_Label").readResolve());
    static final IntNum Lit168;
    static final FString Lit169 = new FString("com.google.appinventor.components.runtime.Label");
    static final IntNum Lit17 = IntNum.make(1);
    static final FString Lit170 = new FString("com.google.appinventor.components.runtime.Label");
    static final IntNum Lit171;
    static final FString Lit172 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit173 = new FString("com.google.appinventor.components.runtime.Label");
    static final IntNum Lit174;
    static final FString Lit175 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit176 = new FString("com.google.appinventor.components.runtime.Label");
    static final IntNum Lit177;
    static final FString Lit178 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit179 = new FString("com.google.appinventor.components.runtime.Label");
    static final PairWithPosition Lit18 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 41228), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 41220);
    static final IntNum Lit180;
    static final FString Lit181 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit182 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit183 = ((SimpleSymbol) new SimpleSymbol("hbar2").readResolve());
    static final IntNum Lit184;
    static final IntNum Lit185 = IntNum.make(-1100);
    static final FString Lit186 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit187 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit188 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement4").readResolve());
    static final FString Lit189 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final PairWithPosition Lit19 = PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 41243);
    static final FString Lit190 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit191 = ((SimpleSymbol) new SimpleSymbol("Distance_Label").readResolve());
    static final IntNum Lit192;
    static final FString Lit193 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit194 = new FString("com.google.appinventor.components.runtime.Label");
    static final IntNum Lit195;
    static final FString Lit196 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit197 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit198 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement5").readResolve());
    static final FString Lit199 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit2 = ((SimpleSymbol) new SimpleSymbol("*the-null-value*").readResolve());
    static final PairWithPosition Lit20 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 41270), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 41262);
    static final FString Lit200 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit201 = ((SimpleSymbol) new SimpleSymbol("Interval_Label").readResolve());
    static final IntNum Lit202;
    static final FString Lit203 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit204 = new FString("com.google.appinventor.components.runtime.Label");
    static final IntNum Lit205;
    static final FString Lit206 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit207 = new FString("com.google.appinventor.components.runtime.LocationSensor");
    static final SimpleSymbol Lit208 = ((SimpleSymbol) new SimpleSymbol("DistanceInterval").readResolve());
    static final FString Lit209 = new FString("com.google.appinventor.components.runtime.LocationSensor");
    static final PairWithPosition Lit21 = PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 41093);
    static final PairWithPosition Lit210 = PairWithPosition.make(Lit249, PairWithPosition.make(Lit69, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1290463), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1290448);
    static final PairWithPosition Lit211 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1290701), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1290695);
    static final PairWithPosition Lit212 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1290814), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1290808);
    static final PairWithPosition Lit213 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1290925), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1290919);
    static final PairWithPosition Lit214 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1291059), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1291053);
    static final PairWithPosition Lit215 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1291202), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1291196);
    static final DFloNum Lit216 = DFloNum.make(0.4d);
    static final PairWithPosition Lit217;
    static final SimpleSymbol Lit218 = ((SimpleSymbol) new SimpleSymbol("DriverNotifier").readResolve());
    static final SimpleSymbol Lit219 = ((SimpleSymbol) new SimpleSymbol("ShowAlert").readResolve());
    static final PairWithPosition Lit22 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 41228), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 41220);
    static final PairWithPosition Lit220 = PairWithPosition.make(Lit69, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1291709);
    static final PairWithPosition Lit221 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1291840), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1291834);
    static final PairWithPosition Lit222 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1291965), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1291959);
    static final PairWithPosition Lit223 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1292118), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1292112);
    static final PairWithPosition Lit224;
    static final SimpleSymbol Lit225 = ((SimpleSymbol) new SimpleSymbol("DriverLocation$LocationChanged").readResolve());
    static final SimpleSymbol Lit226 = ((SimpleSymbol) new SimpleSymbol("LocationChanged").readResolve());
    static final FString Lit227 = new FString("com.google.appinventor.components.runtime.Clock");
    static final FString Lit228 = new FString("com.google.appinventor.components.runtime.Clock");
    static final FString Lit229 = new FString("com.google.appinventor.components.runtime.ActivityStarter");
    static final PairWithPosition Lit23 = PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 41243);
    static final SimpleSymbol Lit230 = ((SimpleSymbol) new SimpleSymbol("ActivityStarterLock").readResolve());
    static final FString Lit231 = new FString("com.google.appinventor.components.runtime.ActivityStarter");
    static final FString Lit232 = new FString("com.google.appinventor.components.runtime.TinyDB");
    static final FString Lit233 = new FString("com.google.appinventor.components.runtime.TinyDB");
    static final FString Lit234 = new FString("com.google.appinventor.components.runtime.Notifier");
    static final FString Lit235 = new FString("com.google.appinventor.components.runtime.Notifier");
    static final SimpleSymbol Lit236 = ((SimpleSymbol) new SimpleSymbol("android-log-form").readResolve());
    static final SimpleSymbol Lit237 = ((SimpleSymbol) new SimpleSymbol("add-to-form-environment").readResolve());
    static final SimpleSymbol Lit238 = ((SimpleSymbol) new SimpleSymbol("lookup-in-form-environment").readResolve());
    static final SimpleSymbol Lit239 = ((SimpleSymbol) new SimpleSymbol("is-bound-in-form-environment").readResolve());
    static final PairWithPosition Lit24 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 41270), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 41262);
    static final SimpleSymbol Lit240 = ((SimpleSymbol) new SimpleSymbol("add-to-global-var-environment").readResolve());
    static final SimpleSymbol Lit241 = ((SimpleSymbol) new SimpleSymbol("add-to-events").readResolve());
    static final SimpleSymbol Lit242 = ((SimpleSymbol) new SimpleSymbol("add-to-components").readResolve());
    static final SimpleSymbol Lit243 = ((SimpleSymbol) new SimpleSymbol("add-to-global-vars").readResolve());
    static final SimpleSymbol Lit244 = ((SimpleSymbol) new SimpleSymbol("add-to-form-do-after-creation").readResolve());
    static final SimpleSymbol Lit245 = ((SimpleSymbol) new SimpleSymbol("send-error").readResolve());
    static final SimpleSymbol Lit246 = ((SimpleSymbol) new SimpleSymbol("dispatchEvent").readResolve());
    static final SimpleSymbol Lit247 = ((SimpleSymbol) new SimpleSymbol("lookup-handler").readResolve());
    static final SimpleSymbol Lit248 = ((SimpleSymbol) new SimpleSymbol("any").readResolve());
    static final SimpleSymbol Lit249 = ((SimpleSymbol) new SimpleSymbol("InstantInTime").readResolve());
    static final SimpleSymbol Lit25 = ((SimpleSymbol) new SimpleSymbol("p$dlat").readResolve());
    static final SimpleSymbol Lit26 = ((SimpleSymbol) new SimpleSymbol("GetValue").readResolve());
    static final PairWithPosition Lit27 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 45197), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 45191);
    static final PairWithPosition Lit28 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 45284), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 45278);
    static final PairWithPosition Lit29 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 45300), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 45292);
    static final SimpleSymbol Lit3 = ((SimpleSymbol) new SimpleSymbol("p$procedure").readResolve());
    static final PairWithPosition Lit30 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 45197), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 45191);
    static final PairWithPosition Lit31 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 45284), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 45278);
    static final PairWithPosition Lit32 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 45300), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 45292);
    static final SimpleSymbol Lit33 = ((SimpleSymbol) new SimpleSymbol("p$dlon").readResolve());
    static final PairWithPosition Lit34 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 49294), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 49288);
    static final PairWithPosition Lit35 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 49382), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 49376);
    static final PairWithPosition Lit36 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 49398), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 49390);
    static final PairWithPosition Lit37 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 49294), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 49288);
    static final PairWithPosition Lit38 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 49382), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 49376);
    static final PairWithPosition Lit39 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 49398), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 49390);
    static final SimpleSymbol Lit4 = ((SimpleSymbol) new SimpleSymbol("g$score").readResolve());
    static final SimpleSymbol Lit40 = ((SimpleSymbol) new SimpleSymbol("p$d").readResolve());
    static final PairWithPosition Lit41 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 53346), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 53338);
    static final PairWithPosition Lit42 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 53346), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 53338);
    static final PairWithPosition Lit43 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57584), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57576);
    static final PairWithPosition Lit44 = PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57609);
    static final PairWithPosition Lit45 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57637), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57629);
    static final PairWithPosition Lit46 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57829), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57823);
    static final PairWithPosition Lit47 = PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57837);
    static final PairWithPosition Lit48 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57987), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57981);
    static final PairWithPosition Lit49 = PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57995);
    static final IntNum Lit5 = IntNum.make(2);
    static final PairWithPosition Lit50 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58195), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58187);
    static final PairWithPosition Lit51 = PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58220);
    static final PairWithPosition Lit52 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58248), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58240);
    static final PairWithPosition Lit53 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58282), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58275), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58267);
    static final PairWithPosition Lit54 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58307), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58299);
    static final PairWithPosition Lit55 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57584), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57576);
    static final PairWithPosition Lit56 = PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57609);
    static final PairWithPosition Lit57 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57637), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57629);
    static final PairWithPosition Lit58 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57829), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57823);
    static final PairWithPosition Lit59 = PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57837);
    static final PairWithPosition Lit6 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 32882), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 32874);
    static final PairWithPosition Lit60 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57987), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57981);
    static final PairWithPosition Lit61 = PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 57995);
    static final PairWithPosition Lit62 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58195), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58187);
    static final PairWithPosition Lit63 = PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58220);
    static final PairWithPosition Lit64 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58248), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58240);
    static final PairWithPosition Lit65 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58282), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58275), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58267);
    static final PairWithPosition Lit66 = PairWithPosition.make(Lit73, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58307), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 58299);
    static final IntNum Lit67 = IntNum.make(0);
    static final SimpleSymbol Lit68 = ((SimpleSymbol) new SimpleSymbol("AboutScreen").readResolve());
    static final SimpleSymbol Lit69;
    static final SimpleSymbol Lit7 = ((SimpleSymbol) new SimpleSymbol("TinyDB1").readResolve());
    static final SimpleSymbol Lit70 = ((SimpleSymbol) new SimpleSymbol("AppName").readResolve());
    static final SimpleSymbol Lit71 = ((SimpleSymbol) new SimpleSymbol("BackgroundColor").readResolve());
    static final IntNum Lit72 = IntNum.make((int) Component.COLOR_NONE);
    static final SimpleSymbol Lit73;
    static final SimpleSymbol Lit74 = ((SimpleSymbol) new SimpleSymbol("ScreenOrientation").readResolve());
    static final SimpleSymbol Lit75 = ((SimpleSymbol) new SimpleSymbol("Title").readResolve());
    static final PairWithPosition Lit76 = PairWithPosition.make(Lit69, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 106574);
    static final SimpleSymbol Lit77 = ((SimpleSymbol) new SimpleSymbol("Screen2$BackPressed").readResolve());
    static final SimpleSymbol Lit78 = ((SimpleSymbol) new SimpleSymbol("BackPressed").readResolve());
    static final SimpleSymbol Lit79 = ((SimpleSymbol) new SimpleSymbol("Address").readResolve());
    static final SimpleSymbol Lit8 = ((SimpleSymbol) new SimpleSymbol("StoreValue").readResolve());
    static final SimpleSymbol Lit80 = ((SimpleSymbol) new SimpleSymbol("Text").readResolve());
    static final SimpleSymbol Lit81 = ((SimpleSymbol) new SimpleSymbol("DriverLocation").readResolve());
    static final SimpleSymbol Lit82 = ((SimpleSymbol) new SimpleSymbol("CurrentAddress").readResolve());
    static final SimpleSymbol Lit83 = ((SimpleSymbol) new SimpleSymbol("TimeInterval_Display").readResolve());
    static final SimpleSymbol Lit84 = ((SimpleSymbol) new SimpleSymbol("DriverClock").readResolve());
    static final SimpleSymbol Lit85 = ((SimpleSymbol) new SimpleSymbol("FormatDateTime").readResolve());
    static final SimpleSymbol Lit86 = ((SimpleSymbol) new SimpleSymbol("Now").readResolve());
    static final PairWithPosition Lit87 = PairWithPosition.make(Lit249, PairWithPosition.make(Lit69, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 115022), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 115007);
    static final PairWithPosition Lit88 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 115165), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 115159);
    static final SimpleSymbol Lit89 = ((SimpleSymbol) new SimpleSymbol("Latitude").readResolve());
    static final PairWithPosition Lit9 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 32995), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 32989);
    static final PairWithPosition Lit90 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 115290), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 115284);
    static final SimpleSymbol Lit91 = ((SimpleSymbol) new SimpleSymbol("Longitude").readResolve());
    static final PairWithPosition Lit92 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 115417), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 115411);
    static final PairWithPosition Lit93 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 115570), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 115564);
    static final SimpleSymbol Lit94 = ((SimpleSymbol) new SimpleSymbol("LatitudeDisplay").readResolve());
    static final PairWithPosition Lit95 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 115705), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 115699);
    static final SimpleSymbol Lit96 = ((SimpleSymbol) new SimpleSymbol("LatitudeDisplay2").readResolve());
    static final SimpleSymbol Lit97 = ((SimpleSymbol) new SimpleSymbol("LongitudeDisplay").readResolve());
    static final PairWithPosition Lit98 = PairWithPosition.make(Lit69, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 115945), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 115939);
    static final SimpleSymbol Lit99 = ((SimpleSymbol) new SimpleSymbol("LongitudeDisplay2").readResolve());
    public static Screen2 Screen2;
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
    static final ModuleMethod lambda$Fn29 = null;
    static final ModuleMethod lambda$Fn3 = null;
    static final ModuleMethod lambda$Fn30 = null;
    static final ModuleMethod lambda$Fn31 = null;
    static final ModuleMethod lambda$Fn32 = null;
    static final ModuleMethod lambda$Fn33 = null;
    static final ModuleMethod lambda$Fn34 = null;
    static final ModuleMethod lambda$Fn35 = null;
    static final ModuleMethod lambda$Fn36 = null;
    static final ModuleMethod lambda$Fn37 = null;
    static final ModuleMethod lambda$Fn38 = null;
    static final ModuleMethod lambda$Fn39 = null;
    static final ModuleMethod lambda$Fn4 = null;
    static final ModuleMethod lambda$Fn40 = null;
    static final ModuleMethod lambda$Fn41 = null;
    static final ModuleMethod lambda$Fn42 = null;
    static final ModuleMethod lambda$Fn43 = null;
    static final ModuleMethod lambda$Fn44 = null;
    static final ModuleMethod lambda$Fn45 = null;
    static final ModuleMethod lambda$Fn46 = null;
    static final ModuleMethod lambda$Fn47 = null;
    static final ModuleMethod lambda$Fn48 = null;
    static final ModuleMethod lambda$Fn49 = null;
    static final ModuleMethod lambda$Fn5 = null;
    static final ModuleMethod lambda$Fn50 = null;
    static final ModuleMethod lambda$Fn51 = null;
    static final ModuleMethod lambda$Fn52 = null;
    static final ModuleMethod lambda$Fn53 = null;
    static final ModuleMethod lambda$Fn54 = null;
    static final ModuleMethod lambda$Fn55 = null;
    static final ModuleMethod lambda$Fn56 = null;
    static final ModuleMethod lambda$Fn57 = null;
    static final ModuleMethod lambda$Fn58 = null;
    static final ModuleMethod lambda$Fn59 = null;
    static final ModuleMethod lambda$Fn6 = null;
    static final ModuleMethod lambda$Fn60 = null;
    static final ModuleMethod lambda$Fn61 = null;
    static final ModuleMethod lambda$Fn62 = null;
    static final ModuleMethod lambda$Fn63 = null;
    static final ModuleMethod lambda$Fn64 = null;
    static final ModuleMethod lambda$Fn7 = null;
    static final ModuleMethod lambda$Fn8 = null;
    static final ModuleMethod lambda$Fn9 = null;
    public Boolean $Stdebug$Mnform$St;
    public final ModuleMethod $define;
    public ActivityStarter ActivityStarterLock;
    public Label Address;
    public Label DistanceDisplay;
    public Label Distance_Label;
    public Clock DriverClock;
    public LocationSensor DriverLocation;
    public final ModuleMethod DriverLocation$LocationChanged;
    public Notifier DriverNotifier;
    public TableArrangement GPSTable;
    public Button Home;
    public final ModuleMethod Home$Click;
    public HorizontalArrangement HorizontalArrangement4;
    public HorizontalArrangement HorizontalArrangement5;
    public Image Image1;
    public Label Interval_Label;
    public Label Label1;
    public Label LatitudeDisplay;
    public Label LatitudeDisplay2;
    public Label Latitude_Label;
    public Label Longititude_Label;
    public Label LongitudeDisplay;
    public Label LongitudeDisplay2;
    public Label Now;
    public Label OldAddress;
    public final ModuleMethod Screen2$BackPressed;
    public final ModuleMethod Screen2$Initialize;
    public Label TimeInterval_Display;
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
    public HorizontalArrangement hbar1;
    public HorizontalArrangement hbar2;
    public final ModuleMethod is$Mnbound$Mnin$Mnform$Mnenvironment;
    public final ModuleMethod lookup$Mnhandler;
    public final ModuleMethod lookup$Mnin$Mnform$Mnenvironment;
    public final ModuleMethod process$Mnexception;
    public final ModuleMethod send$Mnerror;

    /* compiled from: Screen2.yail */
    public class frame extends ModuleBody {
        Screen2 $main = this;

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 83 ? this.$main.DriverLocation$LocationChanged(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 83) {
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
                    return Screen2.lambda2();
                case 16:
                    this.$main.$define();
                    return Values.empty;
                case 17:
                    return Screen2.lambda3();
                case 18:
                    return Screen2.lambda5();
                case 19:
                    return Screen2.lambda4();
                case 20:
                    return Screen2.lambda6();
                case 21:
                    return Screen2.lambda7();
                case 22:
                    return Screen2.lambda9();
                case 23:
                    return Screen2.lambda8();
                case 24:
                    return Screen2.lambda10();
                case 25:
                    return Screen2.lambda12();
                case 26:
                    return Screen2.lambda11();
                case 27:
                    return Screen2.lambda13();
                case 28:
                    return Screen2.lambda15();
                case 29:
                    return Screen2.lambda14();
                case 30:
                    return Screen2.lambda16();
                case 31:
                    return Screen2.lambda18();
                case 32:
                    return Screen2.lambda17();
                case 33:
                    return Screen2.lambda19();
                case 34:
                    return Screen2.lambda21();
                case 35:
                    return Screen2.lambda20();
                case 36:
                    return Screen2.lambda22();
                case 37:
                    return Screen2.lambda23();
                case 38:
                    return this.$main.Screen2$BackPressed();
                case 39:
                    return this.$main.Screen2$Initialize();
                case 40:
                    return Screen2.lambda24();
                case 41:
                    return Screen2.lambda25();
                case XDataType.NMTOKEN_TYPE_CODE /*42*/:
                    return Screen2.lambda26();
                case XDataType.NAME_TYPE_CODE /*43*/:
                    return Screen2.lambda27();
                case XDataType.NCNAME_TYPE_CODE /*44*/:
                    return this.$main.Home$Click();
                case XDataType.ID_TYPE_CODE /*45*/:
                    return Screen2.lambda28();
                case XDataType.IDREF_TYPE_CODE /*46*/:
                    return Screen2.lambda29();
                case XDataType.ENTITY_TYPE_CODE /*47*/:
                    return Screen2.lambda30();
                case 48:
                    return Screen2.lambda31();
                case 49:
                    return Screen2.lambda32();
                case 50:
                    return Screen2.lambda33();
                case 51:
                    return Screen2.lambda34();
                case 52:
                    return Screen2.lambda35();
                case 53:
                    return Screen2.lambda36();
                case 54:
                    return Screen2.lambda37();
                case 55:
                    return Screen2.lambda38();
                case 56:
                    return Screen2.lambda39();
                case 57:
                    return Screen2.lambda40();
                case 58:
                    return Screen2.lambda41();
                case 59:
                    return Screen2.lambda42();
                case 60:
                    return Screen2.lambda43();
                case 61:
                    return Screen2.lambda44();
                case 62:
                    return Screen2.lambda45();
                case 63:
                    return Screen2.lambda46();
                case 64:
                    return Screen2.lambda47();
                case 65:
                    return Screen2.lambda48();
                case 66:
                    return Screen2.lambda49();
                case 67:
                    return Screen2.lambda50();
                case 68:
                    return Screen2.lambda51();
                case 69:
                    return Screen2.lambda52();
                case PrettyWriter.NEWLINE_FILL /*70*/:
                    return Screen2.lambda53();
                case 71:
                    return Screen2.lambda54();
                case 72:
                    return Screen2.lambda55();
                case 73:
                    return Screen2.lambda56();
                case 74:
                    return Screen2.lambda57();
                case 75:
                    return Screen2.lambda58();
                case 76:
                    return Screen2.lambda59();
                case PrettyWriter.NEWLINE_MISER /*77*/:
                    return Screen2.lambda60();
                case PrettyWriter.NEWLINE_LINEAR /*78*/:
                    return Screen2.lambda61();
                case 79:
                    return Screen2.lambda62();
                case 80:
                    return Screen2.lambda63();
                case 81:
                    return Screen2.lambda64();
                case PrettyWriter.NEWLINE_MANDATORY /*82*/:
                    return Screen2.lambda65();
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
                case 48:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 49:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 50:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 51:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 52:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 53:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 54:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 55:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 56:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 57:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 58:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 59:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 60:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 61:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 62:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 63:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 64:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 65:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 66:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 67:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 68:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 69:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case PrettyWriter.NEWLINE_FILL /*70*/:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 71:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 72:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 73:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 74:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 75:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 76:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case PrettyWriter.NEWLINE_MISER /*77*/:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case PrettyWriter.NEWLINE_LINEAR /*78*/:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 79:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 80:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 81:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case PrettyWriter.NEWLINE_MANDATORY /*82*/:
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
                    if (!(obj instanceof Screen2)) {
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
                    if (!(obj instanceof Screen2)) {
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
    }

    static {
        SimpleSymbol simpleSymbol = (SimpleSymbol) new SimpleSymbol(PropertyTypeConstants.PROPERTY_TYPE_TEXT).readResolve();
        Lit69 = simpleSymbol;
        Lit224 = PairWithPosition.make(simpleSymbol, PairWithPosition.make(Lit248, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1292253), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1292247);
        simpleSymbol = (SimpleSymbol) new SimpleSymbol("number").readResolve();
        Lit73 = simpleSymbol;
        Lit217 = PairWithPosition.make(simpleSymbol, PairWithPosition.make(Lit73, LList.Empty, "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1291531), "/tmp/1461280886464_0.6325097682996035-0/youngandroidproject/../src/appinventor/ai_Aarushi_Parashar/Technovation/Screen2.yail", 1291523);
        int[] iArr = new int[2];
        iArr[0] = -1;
        Lit205 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit202 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit195 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit192 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit184 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit180 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit177 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit174 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit171 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit168 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit162 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit154 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit148 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit144 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = Component.COLOR_CYAN;
        Lit141 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -1;
        Lit107 = IntNum.make(iArr);
    }

    public Screen2() {
        ModuleInfo.register(this);
        ModuleBody frame = new frame();
        this.android$Mnlog$Mnform = new ModuleMethod(frame, 1, Lit236, 4097);
        this.add$Mnto$Mnform$Mnenvironment = new ModuleMethod(frame, 2, Lit237, 8194);
        this.lookup$Mnin$Mnform$Mnenvironment = new ModuleMethod(frame, 3, Lit238, 8193);
        this.is$Mnbound$Mnin$Mnform$Mnenvironment = new ModuleMethod(frame, 5, Lit239, 4097);
        this.add$Mnto$Mnglobal$Mnvar$Mnenvironment = new ModuleMethod(frame, 6, Lit240, 8194);
        this.add$Mnto$Mnevents = new ModuleMethod(frame, 7, Lit241, 8194);
        this.add$Mnto$Mncomponents = new ModuleMethod(frame, 8, Lit242, 16388);
        this.add$Mnto$Mnglobal$Mnvars = new ModuleMethod(frame, 9, Lit243, 8194);
        this.add$Mnto$Mnform$Mndo$Mnafter$Mncreation = new ModuleMethod(frame, 10, Lit244, 4097);
        this.send$Mnerror = new ModuleMethod(frame, 11, Lit245, 4097);
        this.process$Mnexception = new ModuleMethod(frame, 12, "process-exception", 4097);
        this.dispatchEvent = new ModuleMethod(frame, 13, Lit246, 16388);
        this.lookup$Mnhandler = new ModuleMethod(frame, 14, Lit247, 8194);
        PropertySet moduleMethod = new ModuleMethod(frame, 15, null, 0);
        moduleMethod.setProperty("source-location", "/tmp/runtime6140171982161511911.scm:542");
        lambda$Fn1 = moduleMethod;
        this.$define = new ModuleMethod(frame, 16, "$define", 0);
        lambda$Fn2 = new ModuleMethod(frame, 17, null, 0);
        lambda$Fn4 = new ModuleMethod(frame, 18, null, 0);
        lambda$Fn3 = new ModuleMethod(frame, 19, null, 0);
        lambda$Fn5 = new ModuleMethod(frame, 20, null, 0);
        lambda$Fn6 = new ModuleMethod(frame, 21, null, 0);
        lambda$Fn8 = new ModuleMethod(frame, 22, null, 0);
        lambda$Fn7 = new ModuleMethod(frame, 23, null, 0);
        lambda$Fn9 = new ModuleMethod(frame, 24, null, 0);
        lambda$Fn11 = new ModuleMethod(frame, 25, null, 0);
        lambda$Fn10 = new ModuleMethod(frame, 26, null, 0);
        lambda$Fn12 = new ModuleMethod(frame, 27, null, 0);
        lambda$Fn14 = new ModuleMethod(frame, 28, null, 0);
        lambda$Fn13 = new ModuleMethod(frame, 29, null, 0);
        lambda$Fn15 = new ModuleMethod(frame, 30, null, 0);
        lambda$Fn17 = new ModuleMethod(frame, 31, null, 0);
        lambda$Fn16 = new ModuleMethod(frame, 32, null, 0);
        lambda$Fn18 = new ModuleMethod(frame, 33, null, 0);
        lambda$Fn20 = new ModuleMethod(frame, 34, null, 0);
        lambda$Fn19 = new ModuleMethod(frame, 35, null, 0);
        lambda$Fn21 = new ModuleMethod(frame, 36, null, 0);
        lambda$Fn22 = new ModuleMethod(frame, 37, null, 0);
        this.Screen2$BackPressed = new ModuleMethod(frame, 38, Lit77, 0);
        this.Screen2$Initialize = new ModuleMethod(frame, 39, Lit103, 0);
        lambda$Fn23 = new ModuleMethod(frame, 40, null, 0);
        lambda$Fn24 = new ModuleMethod(frame, 41, null, 0);
        lambda$Fn25 = new ModuleMethod(frame, 42, null, 0);
        lambda$Fn26 = new ModuleMethod(frame, 43, null, 0);
        this.Home$Click = new ModuleMethod(frame, 44, Lit120, 0);
        lambda$Fn27 = new ModuleMethod(frame, 45, null, 0);
        lambda$Fn28 = new ModuleMethod(frame, 46, null, 0);
        lambda$Fn29 = new ModuleMethod(frame, 47, null, 0);
        lambda$Fn30 = new ModuleMethod(frame, 48, null, 0);
        lambda$Fn31 = new ModuleMethod(frame, 49, null, 0);
        lambda$Fn32 = new ModuleMethod(frame, 50, null, 0);
        lambda$Fn33 = new ModuleMethod(frame, 51, null, 0);
        lambda$Fn34 = new ModuleMethod(frame, 52, null, 0);
        lambda$Fn35 = new ModuleMethod(frame, 53, null, 0);
        lambda$Fn36 = new ModuleMethod(frame, 54, null, 0);
        lambda$Fn37 = new ModuleMethod(frame, 55, null, 0);
        lambda$Fn38 = new ModuleMethod(frame, 56, null, 0);
        lambda$Fn39 = new ModuleMethod(frame, 57, null, 0);
        lambda$Fn40 = new ModuleMethod(frame, 58, null, 0);
        lambda$Fn41 = new ModuleMethod(frame, 59, null, 0);
        lambda$Fn42 = new ModuleMethod(frame, 60, null, 0);
        lambda$Fn43 = new ModuleMethod(frame, 61, null, 0);
        lambda$Fn44 = new ModuleMethod(frame, 62, null, 0);
        lambda$Fn45 = new ModuleMethod(frame, 63, null, 0);
        lambda$Fn46 = new ModuleMethod(frame, 64, null, 0);
        lambda$Fn47 = new ModuleMethod(frame, 65, null, 0);
        lambda$Fn48 = new ModuleMethod(frame, 66, null, 0);
        lambda$Fn49 = new ModuleMethod(frame, 67, null, 0);
        lambda$Fn50 = new ModuleMethod(frame, 68, null, 0);
        lambda$Fn51 = new ModuleMethod(frame, 69, null, 0);
        lambda$Fn52 = new ModuleMethod(frame, 70, null, 0);
        lambda$Fn53 = new ModuleMethod(frame, 71, null, 0);
        lambda$Fn54 = new ModuleMethod(frame, 72, null, 0);
        lambda$Fn55 = new ModuleMethod(frame, 73, null, 0);
        lambda$Fn56 = new ModuleMethod(frame, 74, null, 0);
        lambda$Fn57 = new ModuleMethod(frame, 75, null, 0);
        lambda$Fn58 = new ModuleMethod(frame, 76, null, 0);
        lambda$Fn59 = new ModuleMethod(frame, 77, null, 0);
        lambda$Fn60 = new ModuleMethod(frame, 78, null, 0);
        lambda$Fn61 = new ModuleMethod(frame, 79, null, 0);
        lambda$Fn62 = new ModuleMethod(frame, 80, null, 0);
        lambda$Fn63 = new ModuleMethod(frame, 81, null, 0);
        lambda$Fn64 = new ModuleMethod(frame, 82, null, 0);
        this.DriverLocation$LocationChanged = new ModuleMethod(frame, 83, Lit225, 12291);
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
            Screen2 = null;
            this.form$Mnname$Mnsymbol = Lit0;
            this.events$Mnto$Mnregister = LList.Empty;
            this.components$Mnto$Mncreate = LList.Empty;
            this.global$Mnvars$Mnto$Mncreate = LList.Empty;
            this.form$Mndo$Mnafter$Mncreation = LList.Empty;
            find = require.find("com.google.youngandroid.runtime");
            try {
                ((Runnable) find).run();
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addGlobalVarToCurrentFormEnvironment(Lit3, lambda$Fn2), $result);
                } else {
                    addToGlobalVars(Lit3, lambda$Fn3);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addGlobalVarToCurrentFormEnvironment(Lit12, Lit13), $result);
                } else {
                    addToGlobalVars(Lit12, lambda$Fn5);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addGlobalVarToCurrentFormEnvironment(Lit14, lambda$Fn6), $result);
                } else {
                    addToGlobalVars(Lit14, lambda$Fn7);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addGlobalVarToCurrentFormEnvironment(Lit25, lambda$Fn9), $result);
                } else {
                    addToGlobalVars(Lit25, lambda$Fn10);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addGlobalVarToCurrentFormEnvironment(Lit33, lambda$Fn12), $result);
                } else {
                    addToGlobalVars(Lit33, lambda$Fn13);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addGlobalVarToCurrentFormEnvironment(Lit40, lambda$Fn15), $result);
                } else {
                    addToGlobalVars(Lit40, lambda$Fn16);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addGlobalVarToCurrentFormEnvironment(Lit15, lambda$Fn18), $result);
                } else {
                    addToGlobalVars(Lit15, lambda$Fn19);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addGlobalVarToCurrentFormEnvironment(Lit4, Lit67), $result);
                } else {
                    addToGlobalVars(Lit4, lambda$Fn21);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit68, "Drive Off Mode", Lit69);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit70, "Technovation", Lit69);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit71, Lit72, Lit73);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit74, "user", Lit69);
                    Values.writeValues(runtime.setAndCoerceProperty$Ex(Lit0, Lit75, "Entering Drive Off Mode", Lit69), $result);
                } else {
                    addToFormDoAfterCreation(new Promise(lambda$Fn22));
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit77, this.Screen2$BackPressed);
                } else {
                    addToFormEnvironment(Lit77, this.Screen2$BackPressed);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Screen2", "BackPressed");
                } else {
                    addToEvents(Lit0, Lit78);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit103, this.Screen2$Initialize);
                } else {
                    addToFormEnvironment(Lit103, this.Screen2$Initialize);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Screen2", "Initialize");
                } else {
                    addToEvents(Lit0, Lit104);
                }
                this.hbar1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit105, Lit106, lambda$Fn23), $result);
                } else {
                    addToComponents(Lit0, Lit113, Lit106, lambda$Fn24);
                }
                this.Home = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit114, Lit115, lambda$Fn25), $result);
                } else {
                    addToComponents(Lit0, Lit118, Lit115, lambda$Fn26);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit120, this.Home$Click);
                } else {
                    addToFormEnvironment(Lit120, this.Home$Click);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Home", "Click");
                } else {
                    addToEvents(Lit115, Lit121);
                }
                this.VerticalArrangement1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit122, Lit123, lambda$Fn27), $result);
                } else {
                    addToComponents(Lit0, Lit128, Lit123, lambda$Fn28);
                }
                this.Image1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit123, Lit129, Lit130, lambda$Fn29), $result);
                } else {
                    addToComponents(Lit123, Lit135, Lit130, lambda$Fn30);
                }
                this.Label1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit123, Lit136, Lit137, lambda$Fn31), $result);
                } else {
                    addToComponents(Lit123, Lit142, Lit137, lambda$Fn32);
                }
                this.Now = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit123, Lit143, Lit86, lambda$Fn33), $result);
                } else {
                    addToComponents(Lit123, Lit146, Lit86, lambda$Fn34);
                }
                this.Address = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit123, Lit147, Lit79, lambda$Fn35), $result);
                } else {
                    addToComponents(Lit123, Lit151, Lit79, lambda$Fn36);
                }
                this.OldAddress = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit152, Lit153, lambda$Fn37), $result);
                } else {
                    addToComponents(Lit0, Lit155, Lit153, lambda$Fn38);
                }
                this.GPSTable = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit156, Lit157, lambda$Fn39), $result);
                } else {
                    addToComponents(Lit0, Lit159, Lit157, lambda$Fn40);
                }
                this.Latitude_Label = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit157, Lit160, Lit161, lambda$Fn41), $result);
                } else {
                    addToComponents(Lit157, Lit165, Lit161, lambda$Fn42);
                }
                this.Longititude_Label = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit157, Lit166, Lit167, lambda$Fn43), $result);
                } else {
                    addToComponents(Lit157, Lit169, Lit167, lambda$Fn44);
                }
                this.LatitudeDisplay2 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit157, Lit170, Lit96, lambda$Fn45), $result);
                } else {
                    addToComponents(Lit157, Lit172, Lit96, lambda$Fn46);
                }
                this.LatitudeDisplay = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit157, Lit173, Lit94, lambda$Fn47), $result);
                } else {
                    addToComponents(Lit157, Lit175, Lit94, lambda$Fn48);
                }
                this.LongitudeDisplay = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit157, Lit176, Lit97, lambda$Fn49), $result);
                } else {
                    addToComponents(Lit157, Lit178, Lit97, lambda$Fn50);
                }
                this.LongitudeDisplay2 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit157, Lit179, Lit99, lambda$Fn51), $result);
                } else {
                    addToComponents(Lit157, Lit181, Lit99, lambda$Fn52);
                }
                this.hbar2 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit182, Lit183, lambda$Fn53), $result);
                } else {
                    addToComponents(Lit0, Lit186, Lit183, lambda$Fn54);
                }
                this.HorizontalArrangement4 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit187, Lit188, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit189, Lit188, Boolean.FALSE);
                }
                this.Distance_Label = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit188, Lit190, Lit191, lambda$Fn55), $result);
                } else {
                    addToComponents(Lit188, Lit193, Lit191, lambda$Fn56);
                }
                this.DistanceDisplay = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit188, Lit194, Lit100, lambda$Fn57), $result);
                } else {
                    addToComponents(Lit188, Lit196, Lit100, lambda$Fn58);
                }
                this.HorizontalArrangement5 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit197, Lit198, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit199, Lit198, Boolean.FALSE);
                }
                this.Interval_Label = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit198, Lit200, Lit201, lambda$Fn59), $result);
                } else {
                    addToComponents(Lit198, Lit203, Lit201, lambda$Fn60);
                }
                this.TimeInterval_Display = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit198, Lit204, Lit83, lambda$Fn61), $result);
                } else {
                    addToComponents(Lit198, Lit206, Lit83, lambda$Fn62);
                }
                this.DriverLocation = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit207, Lit81, lambda$Fn63), $result);
                } else {
                    addToComponents(Lit0, Lit209, Lit81, lambda$Fn64);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit225, this.DriverLocation$LocationChanged);
                } else {
                    addToFormEnvironment(Lit225, this.DriverLocation$LocationChanged);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "DriverLocation", "LocationChanged");
                } else {
                    addToEvents(Lit81, Lit226);
                }
                this.DriverClock = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit227, Lit84, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit228, Lit84, Boolean.FALSE);
                }
                this.ActivityStarterLock = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit229, Lit230, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit231, Lit230, Boolean.FALSE);
                }
                this.TinyDB1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit232, Lit7, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit233, Lit7, Boolean.FALSE);
                }
                this.DriverNotifier = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit234, Lit218, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit235, Lit218, Boolean.FALSE);
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
        runtime.addGlobalVarToCurrentFormEnvironment(Lit4, runtime.callYailPrimitive(AddOp.$Pl, LList.list2(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit4, runtime.$Stthe$Mnnull$Mnvalue$St), Lit5), Lit6, "+"));
        return runtime.callComponentMethod(Lit7, Lit8, LList.list2("Score", runtime.lookupGlobalVarInCurrentFormEnvironment(Lit4, runtime.$Stthe$Mnnull$Mnvalue$St)), Lit9);
    }

    static Procedure lambda4() {
        return lambda$Fn4;
    }

    static Object lambda5() {
        runtime.addGlobalVarToCurrentFormEnvironment(Lit4, runtime.callYailPrimitive(AddOp.$Pl, LList.list2(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit4, runtime.$Stthe$Mnnull$Mnvalue$St), Lit5), Lit10, "+"));
        return runtime.callComponentMethod(Lit7, Lit8, LList.list2("Score", runtime.lookupGlobalVarInCurrentFormEnvironment(Lit4, runtime.$Stthe$Mnnull$Mnvalue$St)), Lit11);
    }

    static DFloNum lambda6() {
        return Lit13;
    }

    static Object lambda7() {
        return runtime.callYailPrimitive(runtime.atan2$Mndegrees, LList.list2(runtime.callYailPrimitive(numbers.sqrt, LList.list1(Scheme.applyToArgs.apply1(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit15, runtime.$Stthe$Mnnull$Mnvalue$St))), Lit16, "sqrt"), runtime.callYailPrimitive(numbers.sqrt, LList.list1(runtime.callYailPrimitive(AddOp.$Mn, LList.list2(Lit17, Scheme.applyToArgs.apply1(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit15, runtime.$Stthe$Mnnull$Mnvalue$St))), Lit18, "-")), Lit19, "sqrt")), Lit20, "atan2");
    }

    static Procedure lambda8() {
        return lambda$Fn8;
    }

    static Object lambda9() {
        return runtime.callYailPrimitive(runtime.atan2$Mndegrees, LList.list2(runtime.callYailPrimitive(numbers.sqrt, LList.list1(Scheme.applyToArgs.apply1(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit15, runtime.$Stthe$Mnnull$Mnvalue$St))), Lit21, "sqrt"), runtime.callYailPrimitive(numbers.sqrt, LList.list1(runtime.callYailPrimitive(AddOp.$Mn, LList.list2(Lit17, Scheme.applyToArgs.apply1(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit15, runtime.$Stthe$Mnnull$Mnvalue$St))), Lit22, "-")), Lit23, "sqrt")), Lit24, "atan2");
    }

    static Object lambda10() {
        return runtime.callYailPrimitive(AddOp.$Mn, LList.list2(runtime.callComponentMethod(Lit7, Lit26, LList.list2("lat2", "0"), Lit27), runtime.callComponentMethod(Lit7, Lit26, LList.list2("lat", "0"), Lit28)), Lit29, "-");
    }

    static Procedure lambda11() {
        return lambda$Fn11;
    }

    static Object lambda12() {
        return runtime.callYailPrimitive(AddOp.$Mn, LList.list2(runtime.callComponentMethod(Lit7, Lit26, LList.list2("lat2", "0"), Lit30), runtime.callComponentMethod(Lit7, Lit26, LList.list2("lat", "0"), Lit31)), Lit32, "-");
    }

    static Object lambda13() {
        return runtime.callYailPrimitive(AddOp.$Mn, LList.list2(runtime.callComponentMethod(Lit7, Lit26, LList.list2("long2", "0"), Lit34), runtime.callComponentMethod(Lit7, Lit26, LList.list2("long", "0"), Lit35)), Lit36, "-");
    }

    static Procedure lambda14() {
        return lambda$Fn14;
    }

    static Object lambda15() {
        return runtime.callYailPrimitive(AddOp.$Mn, LList.list2(runtime.callComponentMethod(Lit7, Lit26, LList.list2("long2", "0"), Lit37), runtime.callComponentMethod(Lit7, Lit26, LList.list2("long", "0"), Lit38)), Lit39, "-");
    }

    static Object lambda16() {
        return runtime.callYailPrimitive(MultiplyOp.$St, LList.list2(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit12, runtime.$Stthe$Mnnull$Mnvalue$St), Scheme.applyToArgs.apply1(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit14, runtime.$Stthe$Mnnull$Mnvalue$St))), Lit41, "*");
    }

    static Procedure lambda17() {
        return lambda$Fn17;
    }

    static Object lambda18() {
        return runtime.callYailPrimitive(MultiplyOp.$St, LList.list2(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit12, runtime.$Stthe$Mnnull$Mnvalue$St), Scheme.applyToArgs.apply1(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit14, runtime.$Stthe$Mnnull$Mnvalue$St))), Lit42, "*");
    }

    static Object lambda19() {
        return runtime.callYailPrimitive(AddOp.$Pl, LList.list2(runtime.callYailPrimitive(expt.expt, LList.list2(runtime.callYailPrimitive(runtime.sin$Mndegrees, LList.list1(runtime.callYailPrimitive(runtime.yail$Mndivide, LList.list2(Scheme.applyToArgs.apply1(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit25, runtime.$Stthe$Mnnull$Mnvalue$St)), Lit5), Lit43, "yail-divide")), Lit44, "sin"), Lit5), Lit45, "expt"), runtime.callYailPrimitive(MultiplyOp.$St, LList.list3(runtime.callYailPrimitive(runtime.cos$Mndegrees, LList.list1(runtime.callComponentMethod(Lit7, Lit26, LList.list2("lat", ""), Lit46)), Lit47, "cos"), runtime.callYailPrimitive(runtime.cos$Mndegrees, LList.list1(runtime.callComponentMethod(Lit7, Lit26, LList.list2("lat2", ""), Lit48)), Lit49, "cos"), runtime.callYailPrimitive(expt.expt, LList.list2(runtime.callYailPrimitive(runtime.sin$Mndegrees, LList.list1(runtime.callYailPrimitive(runtime.yail$Mndivide, LList.list2(Scheme.applyToArgs.apply1(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit33, runtime.$Stthe$Mnnull$Mnvalue$St)), Lit5), Lit50, "yail-divide")), Lit51, "sin"), Lit5), Lit52, "expt")), Lit53, "*")), Lit54, "+");
    }

    static Procedure lambda20() {
        return lambda$Fn20;
    }

    static Object lambda21() {
        return runtime.callYailPrimitive(AddOp.$Pl, LList.list2(runtime.callYailPrimitive(expt.expt, LList.list2(runtime.callYailPrimitive(runtime.sin$Mndegrees, LList.list1(runtime.callYailPrimitive(runtime.yail$Mndivide, LList.list2(Scheme.applyToArgs.apply1(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit25, runtime.$Stthe$Mnnull$Mnvalue$St)), Lit5), Lit55, "yail-divide")), Lit56, "sin"), Lit5), Lit57, "expt"), runtime.callYailPrimitive(MultiplyOp.$St, LList.list3(runtime.callYailPrimitive(runtime.cos$Mndegrees, LList.list1(runtime.callComponentMethod(Lit7, Lit26, LList.list2("lat", ""), Lit58)), Lit59, "cos"), runtime.callYailPrimitive(runtime.cos$Mndegrees, LList.list1(runtime.callComponentMethod(Lit7, Lit26, LList.list2("lat2", ""), Lit60)), Lit61, "cos"), runtime.callYailPrimitive(expt.expt, LList.list2(runtime.callYailPrimitive(runtime.sin$Mndegrees, LList.list1(runtime.callYailPrimitive(runtime.yail$Mndivide, LList.list2(Scheme.applyToArgs.apply1(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit33, runtime.$Stthe$Mnnull$Mnvalue$St)), Lit5), Lit62, "yail-divide")), Lit63, "sin"), Lit5), Lit64, "expt")), Lit65, "*")), Lit66, "+");
    }

    static IntNum lambda22() {
        return Lit67;
    }

    static Object lambda23() {
        runtime.setAndCoerceProperty$Ex(Lit0, Lit68, "Drive Off Mode", Lit69);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit70, "Technovation", Lit69);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit71, Lit72, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit74, "user", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit0, Lit75, "Entering Drive Off Mode", Lit69);
    }

    public Object Screen2$BackPressed() {
        runtime.setThisForm();
        return runtime.callYailPrimitive(runtime.open$Mnanother$Mnscreen, LList.list1("Screen1"), Lit76, "open another screen");
    }

    public Object Screen2$Initialize() {
        runtime.setThisForm();
        runtime.setAndCoerceProperty$Ex(Lit79, Lit80, runtime.getProperty$1(Lit81, Lit82), Lit69);
        runtime.setAndCoerceProperty$Ex(Lit83, Lit80, runtime.callComponentMethod(Lit84, Lit85, LList.list2(runtime.callComponentMethod(Lit84, Lit86, LList.Empty, LList.Empty), "MM/dd/yyyy hh:mm:ss a"), Lit87), Lit69);
        runtime.callComponentMethod(Lit7, Lit8, LList.list2("address", runtime.getProperty$1(Lit81, Lit82)), Lit88);
        runtime.callComponentMethod(Lit7, Lit8, LList.list2("lat", runtime.getProperty$1(Lit81, Lit89)), Lit90);
        runtime.callComponentMethod(Lit7, Lit8, LList.list2("long", runtime.getProperty$1(Lit81, Lit91)), Lit92);
        runtime.callComponentMethod(Lit7, Lit8, LList.list2("stime", runtime.callComponentMethod(Lit84, Lit86, LList.Empty, LList.Empty)), Lit93);
        runtime.setAndCoerceProperty$Ex(Lit94, Lit80, runtime.callComponentMethod(Lit7, Lit26, LList.list2("lat", "0"), Lit95), Lit69);
        runtime.setAndCoerceProperty$Ex(Lit96, Lit80, runtime.getProperty$1(Lit81, Lit89), Lit69);
        runtime.setAndCoerceProperty$Ex(Lit97, Lit80, runtime.callComponentMethod(Lit7, Lit26, LList.list2("long", ""), Lit98), Lit69);
        runtime.setAndCoerceProperty$Ex(Lit99, Lit80, runtime.getProperty$1(Lit81, Lit89), Lit69);
        runtime.setAndCoerceProperty$Ex(Lit100, Lit80, Lit67, Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit84, Lit101, Boolean.TRUE, Lit102);
    }

    static Object lambda24() {
        runtime.setAndCoerceProperty$Ex(Lit106, Lit71, Lit107, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit106, Lit108, Lit109, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit106, Lit110, Lit111, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit106, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda25() {
        runtime.setAndCoerceProperty$Ex(Lit106, Lit71, Lit107, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit106, Lit108, Lit109, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit106, Lit110, Lit111, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit106, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda26() {
        runtime.setAndCoerceProperty$Ex(Lit115, Lit116, Boolean.TRUE, Lit102);
        runtime.setAndCoerceProperty$Ex(Lit115, Lit117, Lit17, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit115, Lit80, "Home", Lit69);
    }

    static Object lambda27() {
        runtime.setAndCoerceProperty$Ex(Lit115, Lit116, Boolean.TRUE, Lit102);
        runtime.setAndCoerceProperty$Ex(Lit115, Lit117, Lit17, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit115, Lit80, "Home", Lit69);
    }

    public Object Home$Click() {
        runtime.setThisForm();
        return runtime.callYailPrimitive(runtime.open$Mnanother$Mnscreen, LList.list1("Screen1"), Lit119, "open another screen");
    }

    static Object lambda28() {
        runtime.setAndCoerceProperty$Ex(Lit123, Lit124, Lit125, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit123, Lit126, Lit5, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit123, Lit108, Lit127, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit123, Lit110, Lit127, Lit73);
    }

    static Object lambda29() {
        runtime.setAndCoerceProperty$Ex(Lit123, Lit124, Lit125, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit123, Lit126, Lit5, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit123, Lit108, Lit127, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit123, Lit110, Lit127, Lit73);
    }

    static Object lambda30() {
        runtime.setAndCoerceProperty$Ex(Lit130, Lit108, Lit131, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit130, Lit110, Lit132, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit130, Lit133, "hour-glass-clipart-1.png", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit130, Lit134, Boolean.TRUE, Lit102);
    }

    static Object lambda31() {
        runtime.setAndCoerceProperty$Ex(Lit130, Lit108, Lit131, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit130, Lit110, Lit132, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit130, Lit133, "hour-glass-clipart-1.png", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit130, Lit134, Boolean.TRUE, Lit102);
    }

    static Object lambda32() {
        runtime.setAndCoerceProperty$Ex(Lit137, Lit116, Boolean.TRUE, Lit102);
        runtime.setAndCoerceProperty$Ex(Lit137, Lit138, Lit139, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit137, Lit80, "Entering Drive Off Mode", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit137, Lit140, Lit141, Lit73);
    }

    static Object lambda33() {
        runtime.setAndCoerceProperty$Ex(Lit137, Lit116, Boolean.TRUE, Lit102);
        runtime.setAndCoerceProperty$Ex(Lit137, Lit138, Lit139, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit137, Lit80, "Entering Drive Off Mode", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit137, Lit140, Lit141, Lit73);
    }

    static Object lambda34() {
        runtime.setAndCoerceProperty$Ex(Lit86, Lit71, Lit144, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit86, Lit116, Boolean.TRUE, Lit102);
        runtime.setAndCoerceProperty$Ex(Lit86, Lit138, Lit145, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit86, Lit80, "Time", Lit69);
    }

    static Object lambda35() {
        runtime.setAndCoerceProperty$Ex(Lit86, Lit71, Lit144, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit86, Lit116, Boolean.TRUE, Lit102);
        runtime.setAndCoerceProperty$Ex(Lit86, Lit138, Lit145, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit86, Lit80, "Time", Lit69);
    }

    static Object lambda36() {
        runtime.setAndCoerceProperty$Ex(Lit79, Lit71, Lit148, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit79, Lit138, Lit149, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit79, Lit80, "Address", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit79, Lit150, Lit17, Lit73);
    }

    static Object lambda37() {
        runtime.setAndCoerceProperty$Ex(Lit79, Lit71, Lit148, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit79, Lit138, Lit149, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit79, Lit80, "Address", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit79, Lit150, Lit17, Lit73);
    }

    static Object lambda38() {
        runtime.setAndCoerceProperty$Ex(Lit153, Lit71, Lit154, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit153, Lit80, "Original address", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit153, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda39() {
        runtime.setAndCoerceProperty$Ex(Lit153, Lit71, Lit154, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit153, Lit80, "Original address", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit153, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda40() {
        return runtime.setAndCoerceProperty$Ex(Lit157, Lit158, Lit125, Lit73);
    }

    static Object lambda41() {
        return runtime.setAndCoerceProperty$Ex(Lit157, Lit158, Lit125, Lit73);
    }

    static Object lambda42() {
        runtime.setAndCoerceProperty$Ex(Lit161, Lit71, Lit162, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit161, Lit163, Lit67, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit161, Lit164, Lit67, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit161, Lit80, "Latitude", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit161, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda43() {
        runtime.setAndCoerceProperty$Ex(Lit161, Lit71, Lit162, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit161, Lit163, Lit67, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit161, Lit164, Lit67, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit161, Lit80, "Latitude", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit161, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda44() {
        runtime.setAndCoerceProperty$Ex(Lit167, Lit71, Lit168, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit167, Lit163, Lit67, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit167, Lit164, Lit17, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit167, Lit80, "Longitude", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit167, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda45() {
        runtime.setAndCoerceProperty$Ex(Lit167, Lit71, Lit168, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit167, Lit163, Lit67, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit167, Lit164, Lit17, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit167, Lit80, "Longitude", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit167, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda46() {
        runtime.setAndCoerceProperty$Ex(Lit96, Lit71, Lit171, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit96, Lit163, Lit5, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit96, Lit164, Lit67, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit96, Lit80, "Text for Latitude2", Lit69);
        runtime.setAndCoerceProperty$Ex(Lit96, Lit150, Lit5, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit96, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda47() {
        runtime.setAndCoerceProperty$Ex(Lit96, Lit71, Lit171, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit96, Lit163, Lit5, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit96, Lit164, Lit67, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit96, Lit80, "Text for Latitude2", Lit69);
        runtime.setAndCoerceProperty$Ex(Lit96, Lit150, Lit5, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit96, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda48() {
        runtime.setAndCoerceProperty$Ex(Lit94, Lit71, Lit174, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit94, Lit163, Lit17, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit94, Lit164, Lit67, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit94, Lit80, "Text for Latitude", Lit69);
        runtime.setAndCoerceProperty$Ex(Lit94, Lit150, Lit5, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit94, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda49() {
        runtime.setAndCoerceProperty$Ex(Lit94, Lit71, Lit174, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit94, Lit163, Lit17, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit94, Lit164, Lit67, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit94, Lit80, "Text for Latitude", Lit69);
        runtime.setAndCoerceProperty$Ex(Lit94, Lit150, Lit5, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit94, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda50() {
        runtime.setAndCoerceProperty$Ex(Lit97, Lit71, Lit177, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit97, Lit163, Lit17, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit97, Lit164, Lit17, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit97, Lit80, "Text for Longitude", Lit69);
        runtime.setAndCoerceProperty$Ex(Lit97, Lit150, Lit5, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit97, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda51() {
        runtime.setAndCoerceProperty$Ex(Lit97, Lit71, Lit177, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit97, Lit163, Lit17, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit97, Lit164, Lit17, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit97, Lit80, "Text for Longitude", Lit69);
        runtime.setAndCoerceProperty$Ex(Lit97, Lit150, Lit5, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit97, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda52() {
        runtime.setAndCoerceProperty$Ex(Lit99, Lit71, Lit180, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit99, Lit163, Lit5, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit99, Lit164, Lit17, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit99, Lit80, "Text for Longitude2", Lit69);
        runtime.setAndCoerceProperty$Ex(Lit99, Lit150, Lit5, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit99, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda53() {
        runtime.setAndCoerceProperty$Ex(Lit99, Lit71, Lit180, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit99, Lit163, Lit5, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit99, Lit164, Lit17, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit99, Lit80, "Text for Longitude2", Lit69);
        runtime.setAndCoerceProperty$Ex(Lit99, Lit150, Lit5, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit99, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda54() {
        runtime.setAndCoerceProperty$Ex(Lit183, Lit71, Lit184, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit183, Lit108, Lit109, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit183, Lit110, Lit185, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit183, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda55() {
        runtime.setAndCoerceProperty$Ex(Lit183, Lit71, Lit184, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit183, Lit108, Lit109, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit183, Lit110, Lit185, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit183, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda56() {
        runtime.setAndCoerceProperty$Ex(Lit191, Lit71, Lit192, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit191, Lit80, "DIstance Travelled(mi)", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit191, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda57() {
        runtime.setAndCoerceProperty$Ex(Lit191, Lit71, Lit192, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit191, Lit80, "DIstance Travelled(mi)", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit191, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda58() {
        runtime.setAndCoerceProperty$Ex(Lit100, Lit71, Lit195, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit100, Lit80, "Text for Distance", Lit69);
        runtime.setAndCoerceProperty$Ex(Lit100, Lit150, Lit5, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit100, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda59() {
        runtime.setAndCoerceProperty$Ex(Lit100, Lit71, Lit195, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit100, Lit80, "Text for Distance", Lit69);
        runtime.setAndCoerceProperty$Ex(Lit100, Lit150, Lit5, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit100, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda60() {
        runtime.setAndCoerceProperty$Ex(Lit201, Lit71, Lit202, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit201, Lit80, "Time Interval", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit201, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda61() {
        runtime.setAndCoerceProperty$Ex(Lit201, Lit71, Lit202, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit201, Lit80, "Time Interval", Lit69);
        return runtime.setAndCoerceProperty$Ex(Lit201, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda62() {
        runtime.setAndCoerceProperty$Ex(Lit83, Lit71, Lit205, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit83, Lit80, "Text for Time Interval", Lit69);
        runtime.setAndCoerceProperty$Ex(Lit83, Lit150, Lit5, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit83, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda63() {
        runtime.setAndCoerceProperty$Ex(Lit83, Lit71, Lit205, Lit73);
        runtime.setAndCoerceProperty$Ex(Lit83, Lit80, "Text for Time Interval", Lit69);
        runtime.setAndCoerceProperty$Ex(Lit83, Lit150, Lit5, Lit73);
        return runtime.setAndCoerceProperty$Ex(Lit83, Lit112, Boolean.FALSE, Lit102);
    }

    static Object lambda64() {
        return runtime.setAndCoerceProperty$Ex(Lit81, Lit208, Lit67, Lit73);
    }

    static Object lambda65() {
        return runtime.setAndCoerceProperty$Ex(Lit81, Lit208, Lit67, Lit73);
    }

    public Object DriverLocation$LocationChanged(Object $latitude, Object $longitude, Object $altitude) {
        $latitude = runtime.sanitizeComponentData($latitude);
        $longitude = runtime.sanitizeComponentData($longitude);
        runtime.sanitizeComponentData($altitude);
        runtime.setThisForm();
        runtime.setAndCoerceProperty$Ex(Lit86, Lit80, runtime.callComponentMethod(Lit84, Lit85, LList.list2(runtime.callComponentMethod(Lit84, Lit86, LList.Empty, LList.Empty), "MM/dd/yyyy hh:mm:ss a"), Lit210), Lit69);
        runtime.setAndCoerceProperty$Ex(Lit79, Lit80, runtime.getProperty$1(Lit81, Lit82), Lit69);
        runtime.callComponentMethod(Lit7, Lit8, LList.list2("address2", runtime.getProperty$1(Lit81, Lit82)), Lit211);
        runtime.callComponentMethod(Lit7, Lit8, LList.list2("long2", $longitude), Lit212);
        runtime.callComponentMethod(Lit7, Lit8, LList.list2("lat2", $latitude), Lit213);
        runtime.setAndCoerceProperty$Ex(Lit94, Lit80, runtime.callComponentMethod(Lit7, Lit26, LList.list2("lat", ""), Lit214), Lit69);
        runtime.setAndCoerceProperty$Ex(Lit97, Lit80, runtime.callComponentMethod(Lit7, Lit26, LList.list2("long", ""), Lit215), Lit69);
        runtime.setAndCoerceProperty$Ex(Lit96, Lit80, $latitude, Lit69);
        runtime.setAndCoerceProperty$Ex(Lit99, Lit80, $longitude, Lit69);
        runtime.setAndCoerceProperty$Ex(Lit100, Lit80, Scheme.applyToArgs.apply1(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit40, runtime.$Stthe$Mnnull$Mnvalue$St)), Lit69);
        if (runtime.callYailPrimitive(Scheme.numGEq, LList.list2(Scheme.applyToArgs.apply1(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit40, runtime.$Stthe$Mnnull$Mnvalue$St)), Lit216), Lit217, ">=") != Boolean.FALSE) {
            Scheme.applyToArgs.apply1(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit3, runtime.$Stthe$Mnnull$Mnvalue$St));
            runtime.callComponentMethod(Lit218, Lit219, LList.list1("Driving Mode is detected ! Refrain from using the Device"), Lit220);
        }
        runtime.callComponentMethod(Lit7, Lit8, LList.list2("long", runtime.getProperty$1(Lit81, Lit91)), Lit221);
        runtime.callComponentMethod(Lit7, Lit8, LList.list2("lat", runtime.getProperty$1(Lit81, Lit89)), Lit222);
        runtime.callComponentMethod(Lit7, Lit8, LList.list2("stime", runtime.callComponentMethod(Lit84, Lit86, LList.Empty, LList.Empty)), Lit223);
        return runtime.callComponentMethod(Lit7, Lit8, LList.list2("address", runtime.getProperty$1(Lit81, Lit82)), Lit224);
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
        Screen2 = this;
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
        Screen2 closureEnv = this;
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
