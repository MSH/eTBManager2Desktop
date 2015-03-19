/**
 * 
 */
package org.msh.etbm.desktop.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.InputStream;

import javax.swing.ImageIcon;

/**
 * Icon extension that generates an icon of a specific character from font awesome
 * 
 * @author Ricardo Memoria
 *
 */
public class AwesomeIcon extends ImageIcon {
    private static final long serialVersionUID = 4987414165016216026L;

    public static final int ICON_GLASS = 0xf000;
    public static final int ICON_MUSIC = 0xf001;
    public static final int ICON_SEARCH = 0xf002;
    public static final int ICON_ENVELOPE_ALT = 0xf003;
    public static final int ICON_HEART = 0xf004;
    public static final int ICON_STAR = 0xf005;
    public static final int ICON_STAR_EMPTY = 0xf006;
    public static final int ICON_USER = 0xf007;
    public static final int ICON_FILM = 0xf008;
    public static final int ICON_LARGE = 0xf009;
    public static final int ICON_TH = 0xf00a;
    public static final int ICON_TH_LIST = 0xf00b;
    public static final int ICON_OK = 0xf00c;
    public static final int ICON_REMOVE = 0xf00d;
    public static final int ICON_ZOOM_IN = 0xf00e;
    public static final int ICON_ZOOM_OUT = 0xf00f;
    public static final int ICON_POWER_OFF = 0xf011;
    public static final int ICON_SIGNAL = 0xf012;
    public static final int ICON_GEAR = 0xf013;
    public static final int ICON_COG = 0xf013;
    public static final int ICON_TRASH = 0xf014;
    public static final int ICON_HOME = 0xf015;
    public static final int ICON_FILE_ALT = 0xf016;
    public static final int ICON_TIME = 0xf017;
    public static final int ICON_ROAD = 0xf018;
    public static final int ICON_DOWNLOAD_ALT = 0xf019;
    public static final int ICON_DOWNLOAD = 0xf01a;
    public static final int ICON_UPLOAD = 0xf01b;
    public static final int ICON_INBOX = 0xf01C;
    public static final int ICON_PLAY_CIRCLE = 0xf01d;
    public static final int ICON_ROTATE_RIGHT = 0xf01e;
    public static final int ICON_REPEAT = 0xf01e;
    public static final int ICON_REFRESH = 0xf021;
    public static final int ICON_LIST_ALT = 0xf022;
    public static final int ICON_LOCK = 0xf023;
    public static final int ICON_BEFORE = 0xf024;
    public static final int ICON_HEADPHONES = 0xf025;
    public static final int ICON_VOLUME_OFF = 0xf026;
    public static final int ICON_VOLUME_DOWN = 0xf027;
    public static final int ICON_VOLUME_UP = 0xf028;
    public static final int ICON_QRCODE = 0xf029;
    public static final int ICON_BARCODE = 0xf02a;
    public static final int ICON_TAG = 0xf02b;
    public static final int ICON_TAGS = 0xf02c;
    public static final int ICON_BOOK = 0xf02d;
    public static final int ICON_BOOKMARK = 0xf02e;
    public static final int ICON_PRINT = 0xf02f;
    public static final int ICON_CAMERA = 0xf030;
    public static final int ICON_FONT = 0xf031;
    public static final int ICON_BOLD = 0xf032;
    public static final int ICON_ITALIC = 0xf033;
    public static final int ICON_TEXT_HEIGHT = 0xf034;
    public static final int ICON_TEXT_WIDTH = 0xf035;
    public static final int ICON_ALIGN_LEFT = 0xf036;
    public static final int ICON_ALIGN_CENTER = 0xf037;
    public static final int ICON_ALIGN_RIGHT = 0xf038;
    public static final int ICON_ALIGN_JUSTIFY = 0xf039;
    public static final int ICON_LIST = 0xf03a;
    public static final int ICON_INDENT_LEFT = 0xf03b;
    public static final int ICON_INDENT_RIGHT = 0xf03c;
    public static final int ICON_FACETIME_VIDEO = 0xf03d;
    public static final int ICON_PICTURE = 0xf03e;
    public static final int ICON_PENCIL = 0xf040;
    public static final int ICON_MAP_MARKER = 0xf041;
    public static final int ICON_ADJUST = 0xf042;
    public static final int ICON_TINT = 0xf043;
    public static final int ICON_EDIT = 0xf044;
    public static final int ICON_SHARE = 0xf045;
    public static final int ICON_CHECK = 0xf046;
    public static final int ICON_MOVE = 0xf047;
    public static final int ICON_STEP_BACKWARD = 0xf048;
    public static final int ICON_FAST_BACKWARD = 0xf049;
    public static final int ICON_BACKWARD = 0xf04a;
    public static final int ICON_PLAY = 0xf04b;
    public static final int ICON_PAUSE = 0xf04c;
    public static final int ICON_STOP = 0xf04d;
    public static final int ICON_FORWARD = 0xf04e;
    public static final int ICON_FAST_FORWARD = 0xf050;
    public static final int ICON_STEP_FORWARD = 0xf051;
    public static final int ICON_EJECT = 0xf052;
    public static final int ICON_CHEVRON_LEFT = 0xf053;
    public static final int ICON_CHEVRON_RIGHT = 0xf054;
    public static final int ICON_PLUS_SIGN = 0xf055;
    public static final int ICON_MINUS_SIGN = 0xf056;
    public static final int ICON_REMOVE_SIGN = 0xf057;
    public static final int ICON_OK_SIGN = 0xf058;
    public static final int ICON_QUESTION_SIGN = 0xf059;
    public static final int ICON_INFO_SIGN = 0xf05a;
    public static final int ICON_SCREENSHOT = 0xf05b;
    public static final int ICON_REMOVE_CIRCLE = 0xf05c;
    public static final int ICON_OK_CIRCLE = 0xf05d;
    public static final int ICON_BAN_CIRCLE = 0xf05E;
    public static final int ICON_ARROW_LEFT = 0xf060;
    public static final int ICON_ARROW_RIGHT = 0xf061;
    public static final int ICON_ARROW_UP = 0xf062;
    public static final int ICON_ARROW_DOWN = 0xf063;
    public static final int ICON_MAIL_FORWARD = 0xf064;
    public static final int ICON_SHARE_ALT = 0xf064;
    public static final int ICON_RESIZE_FULL = 0xf065;
    public static final int ICON_RESIZE_SMALL = 0xf066;
    public static final int ICON_PLUS = 0xf067;
    public static final int ICON_MINUS = 0xf068;
    public static final int ICON_ASTERISK = 0xf069;
    public static final int ICON_EXCLAMATION_SIGN = 0xf06a;
    public static final int ICON_GIFT = 0xf06b;
    public static final int ICON_LEAF = 0xf06c;
    public static final int ICON_FIRE = 0xf06d;
    public static final int ICON_EYE_OPEN = 0xf06e;
    public static final int ICON_EYE_CLOSE = 0xf070;
    public static final int ICON_WARNING_SIGN = 0xf071;
    public static final int ICON_PLANE = 0xf072;
    public static final int ICON_CALENDAR = 0xf073;
    public static final int ICON_RANDOM = 0xf074;
    public static final int ICON_COMMENT = 0xf075;
    public static final int ICON_MAGNET = 0xf076;
    public static final int ICON_CHEVRON_UP = 0xf077;
    public static final int ICON_CHEVRON_DOWN = 0xf078;
    public static final int ICON_RETWEET = 0xf079;
    public static final int ICON_SHOPPING_CART = 0xf07a;
    public static final int ICON_FOLDER_CLOSE = 0xf07b;
    public static final int ICON_FOLDER_OPEN = 0xf07c;
    public static final int ICON_RESIZE_VERTICAL = 0xf07d;
    public static final int ICON_RESIZE_HORIZONTAL = 0xf07e;
    public static final int ICON_BAR_CHART = 0xf080;
    public static final int ICON_TWITTER_SIGN = 0xf081;
    public static final int ICON_FACEBOOK_SIGN = 0xf082;
    public static final int ICON_CAMERA_RETRO = 0xf082;
    public static final int ICON_KEY = 0xf084;
    public static final int ICON_GEARS = 0xf085;
    public static final int ICON_COGS = 0xf085;
    public static final int ICON_COMMENTS = 0xf086;
    public static final int ICON_THUMBS_UP_ALT = 0xf087;
    public static final int ICON_THUMBS_DOWN_ALT = 0xf088;
    public static final int ICON_STAR_HALF = 0xf089;
    public static final int ICON_HEART_EMPTY = 0xf08a;
    public static final int ICON_SIGNOUT = 0xf08b;
    public static final int ICON_LINKEDIN_SIGN = 0xf08C;
    public static final int ICON_PUSHPIN = 0xf08d;
    public static final int ICON_EXTERNAL_LINK = 0xf08e;
    public static final int ICON_SIGNIN = 0xf090;
    public static final int ICON_TROPHY = 0xf091;
    public static final int ICON_GITHUB_SIGN = 0xf092;
    public static final int ICON_UPLOAD_ALT = 0xf093;
    public static final int ICON_LEMON = 0xf094;
    public static final int ICON_PHONE = 0xf095;
    public static final int ICON_UNCHECKED = 0xf096;
    public static final int ICON_CHECK_EMPTY = 0xf096;
    public static final int ICON_BOOKMARK_EMPTY = 0xf097;
    public static final int ICON_PHONE_SIGN = 0xf098;
    public static final int ICON_TWITTER = 0xf099;
    public static final int ICON_FACEBOOK = 0xf09a;
    public static final int ICON_GITHUB = 0xf09b;
    public static final int ICON_UNLOCK = 0xf09c;
    public static final int ICON_CREDIT_CARD = 0xf09d;
    public static final int ICON_RSS = 0xf09e;
    public static final int ICON_HDD = 0xf0a0;
    public static final int ICON_BULLHORN = 0xf0a1;
    public static final int ICON_BELL = 0xf0a2;
    public static final int ICON_CERTIFICATE = 0xf0a3;
    public static final int ICON_HAND_RIGHT = 0xf0a4;
    public static final int ICON_HAND_LEFT = 0xf0a5;
    public static final int ICON_HAND_UP = 0xf0a6;
    public static final int ICON_HAND_DOWN = 0xf0a7;
    public static final int ICON_CIRCLE_ARROW_LEFT = 0xf0a8;
    public static final int ICON_CIRCLE_ARROW_RIGHT = 0xf0a9;
    public static final int ICON_CIRCLE_ARROW_UP = 0xf0aa;
    public static final int ICON_CIRCLE_ARROW_DOWN = 0xf0ab;
    public static final int ICON_GLOBE = 0xf0ac;
    public static final int ICON_WRENCH = 0xf0ad;
    public static final int ICON_TASKS = 0xf0ae;
    public static final int ICON_FILTER = 0xf0b0;
    public static final int ICON_BRIEFCASE = 0xf0b1;
    public static final int ICON_FULLSCREEN = 0xf0b2;
    public static final int ICON_GROUP = 0xf0c0;
    public static final int ICON_LINK = 0xf0c1;
    public static final int ICON_CLOUD = 0xf0c2;
    public static final int ICON_BREAKER = 0xf0c3;
    public static final int ICON_CUT = 0xf0c4;
    public static final int ICON_COPY = 0xf0c5;
    public static final int ICON_PAPERCLIP = 0xf0c6;
    public static final int ICON_SAVE = 0xf0c7;
    public static final int ICON_SIGN_BLANK = 0xf0c8;
    public static final int ICON_REORDER = 0xf0c9;
    public static final int ICON_LIST_UL = 0xf0ca;
    public static final int ICON_LIST_OL = 0xf0cb;
    public static final int ICON_STRIKETHROUGH = 0xf0cc;
    public static final int ICON_UNDERLINE = 0xf0cd;
    public static final int ICON_TABLE = 0xf0ce;
    public static final int ICON_MAGIC = 0xf0d0;
    public static final int ICON_TRUCK = 0xf0d1;
    public static final int ICON_PINTEREST = 0xf0d2;
    public static final int ICON_PINTEREST_SIGN = 0xf0d3;
    public static final int ICON_GOOGLE_PLUS_SIGN = 0xf0d4;
    public static final int ICON_GOOGLE_PLUS = 0xf0d5;
    public static final int ICON_MONEY = 0xf0d6;
    public static final int ICON_CARET_DOWN = 0xf0d7;
    public static final int ICON_CARET_UP = 0xf0d8;
    public static final int ICON_CARET_LEFT = 0xf0d9;
    public static final int ICON_CARET_RIGHT = 0xf0da;
    public static final int ICON_COLUMNS = 0xf0db;
    public static final int ICON_SORT = 0xf0dc;
    public static final int ICON_SORT_DOWN = 0xf0dd;
    public static final int ICON_SORT_UP = 0xf0de;
    public static final int ICON_ENVELOPE = 0xf0e0;
    public static final int ICON_LINKEDIN = 0xf0e1;
    public static final int ICON_ROTATE_LEFT = 0xf0e2;
    public static final int ICON_UNDO = 0xf0e2;
    public static final int ICON_LEGAL = 0xf0e3;
    public static final int ICON_DASHBOARD = 0xf0e4;
    public static final int ICON_COMMENT_ALT = 0xf0e6;
    public static final int ICON_BOLT = 0xf0e7;
    public static final int ICON_SITEMAP = 0xf0e8;
    public static final int ICON_UMBRELLA = 0xf0e9;
    public static final int ICON_PASTE = 0xf0ea;
    public static final int ICON_LIGHTBULB = 0xf0eb;
    public static final int ICON_EXCHANGE = 0xf0ec;
    public static final int ICON_CLOUD_DOWNLOAD = 0xf0ed;
    public static final int ICON_CLOUD_UPLOAD = 0xf0ee;
    public static final int ICON_USER_MD = 0xf0f0;
    public static final int ICON_STETHOSCOPE = 0xf0f1;
    public static final int ICON_SUITCASE = 0xf0f2;
    public static final int ICON_BELL_ALT = 0xf0f3;
    public static final int ICON_COFFE = 0xf0f4;
    public static final int ICON_FOOD = 0xf0f5;
    public static final int ICON_FILE_TEXT_ALT = 0xf0f6;
    public static final int ICON_BUILDING = 0xf0f7;
    public static final int ICON_HOSPITAL = 0xf0f8;
    public static final int ICON_AMBULANCE = 0xf0f9;
    public static final int ICON_MEDKIT = 0xf0fa;
    public static final int ICON_FIGHTER_JET = 0xf0fb;
    public static final int ICON_BEER = 0xf0fc;
    public static final int ICON_H_SIGN = 0xf0fd;
    public static final int ICON_PLUS_SIGN_ALT = 0xf0fe;
    public static final int ICON_DOUBLE_ANGLE_LEFT = 0xf100;
    public static final int ICON_DOUBLE_ANGLE_RIGHT = 0xf101;
    public static final int ICON_DOUBLE_ANGLE_UP = 0xf102;
    public static final int ICON_DOUBLE_ANGLE_DOWN = 0xf103;
    public static final int ICON_ANGLE_LEFT = 0xf104;
    public static final int ICON_ANGLE_RIGHT = 0xf105;
    public static final int ICON_ANGLE_UP = 0xf106;
    public static final int ICON_ANGLE_DOWN = 0xf107;
    public static final int ICON_DESKTOP = 0xf108;
    public static final int ICON_LAPTOP = 0xf109;
    public static final int ICON_TABLET = 0xf10a;
    public static final int ICON_MOBILE_PHONE = 0xf10b;
    public static final int ICON_CIRCLE_BLANK = 0xf10c;
    public static final int ICON_QUOTE_LEFT = 0xf10d;
    public static final int ICON_QUOTE_RIGHT = 0xf10e;
    public static final int ICON_SPINNER = 0xf110;
    public static final int ICON_CIRCLE = 0xf111;
    public static final int ICON_MAIL_REPLY = 0xf112;
    public static final int ICON_REPLY = 0xf114;
    public static final int ICON_FOLDER_OPEN_ALT = 0xf115;
    public static final int ICON_EXPAND_ALT = 0xf116;
    public static final int ICON_COLLAPSE_ALT = 0xf117;
    public static final int ICON_SMILE = 0xf118;
    public static final int ICON_FROWN = 0xf119;
    public static final int ICON_MEH = 0xf11a;
    public static final int ICON_GAMEPAD = 0xf11b;
    public static final int ICON_KEYBOARD = 0xf11c;
    public static final int ICON_FLAG_ALT = 0xf11d;
    public static final int ICON_FLAG_CHECKERED = 0xf11e;
    public static final int ICON_TERMINAL = 0xf120;
    public static final int ICON_CODE = 0xf121;
    public static final int ICON_RELY_ALL = 0xf122;
    public static final int ICON_HALF_FULL = 0xf123;
    public static final int ICON_HALF_EMPTY = 0xf123;
    public static final int ICON_LOCATION_ARROW = 0xf124;
    public static final int ICON_CROP = 0xf125;
    public static final int ICON_CODE_FORK = 0xf126;
    public static final int ICON_UNLINK = 0xf127;
    public static final int ICON_QUESTION = 0xf128;
    public static final int ICON_INFO = 0xf129;
    public static final int ICON_EXCLAMATION = 0xf12a;
    public static final int ICON_SUPERSCRIPT = 0xf12b;
    public static final int ICON_SUBSCRIPT = 0xf12c;
    public static final int ICON_ERASER = 0xf12d;
    public static final int ICON_PUZZLE_PIECE = 0xf12e;
    public static final int ICON_MICROPHONE = 0xf130;
    public static final int ICON_MICROPHONE_OFF = 0xf131;
    public static final int ICON_SHIELD = 0xf132;
    public static final int ICON_CALENDAR_EMPTY = 0xf133;
    public static final int ICON_FIRE_EXTINGUISHER = 0xf134;
    public static final int ICON_ROCKET = 0xf135;
    public static final int ICON_MAXCDN = 0xf136;
    public static final int ICON_CHEVRON_SIGN_LEFT = 0xf137;
    public static final int ICON_CHEVRON_SIGN_RIGHT = 0xf138;
    public static final int ICON_CHEVRON_SIGN_UP = 0xf139;
    public static final int ICON_CHEVRON_SIGN_DOWN = 0xf13a;
    public static final int ICON_HTML5 = 0xf13b;
    public static final int ICON_CSS3 = 0xf13c;
    public static final int ICON_ANCHOR = 0xf13d;
    public static final int ICON_UNLOCK_ALT = 0xf13e;
    public static final int ICON_BULLSEYE = 0xf140;
    public static final int ICON_ELLIPSIS_HORIZONTAL = 0xf141;
    public static final int ICON_ELLIPSIS_VERTICAL = 0xf142;
    public static final int ICON_RSS_SIGN = 0xf143;
    public static final int ICON_PLAY_SIGN = 0xf144;
    public static final int ICON_TICKET = 0xf145;
    public static final int ICON_MINUS_SIGN_ALT = 0xf146;
    public static final int ICON_CHECK_MINUS = 0xf147;
    public static final int ICON_LEVEL_UP = 0xf148;
    public static final int ICON_LEVEL_DOWN = 0xf149;
    public static final int ICON_CHECK_SIGN = 0xf14a;
    public static final int ICON_EDIT_SIGN = 0xf14b;
    public static final int ICON_EXTERNAL_LINK_SIGN = 0xf14c;
    public static final int ICON_SHARE_SIGN = 0xf14d;
    public static final int ICON_COMPASS = 0xf14e;
    public static final int ICON_COLLAPSE = 0xf150;
    public static final int ICON_COLLAPSE_TOP = 0xf151;
    public static final int ICON_EXPAND = 0xf152;
    public static final int ICON_EURO = 0xf153;
    public static final int ICON_EUR = 0xf153;
    public static final int ICON_GPB = 0xf154;
    public static final int ICON_DOLLAR = 0xf155;
    public static final int ICON_USD = 0xf155;
    public static final int ICON_RUPEE = 0xf156;
    public static final int ICON_INR = 0xf156;
    public static final int ICON_YEN = 0xf157;
    public static final int ICON_JPY = 0xf157;
    public static final int ICON_RENMINBI = 0xf158;
    public static final int ICON_CNY = 0xf158;
    public static final int ICON_WON = 0xf159;
    public static final int ICON_KRW = 0xf159;
    public static final int ICON_BITCOIN = 0xf15a;
    public static final int ICON_BTC = 0xf15a;
    public static final int ICON_FILE = 0xf15b;
    public static final int ICON_FILE_TEXT = 0xf15c;
    public static final int ICON_SORT_BY_APLHABET = 0xf15d;
    public static final int ICON_SORT_BY_ALPHABET_ALT = 0xf15E;
    public static final int ICON_SORT_BY_ATTRIBUTES = 0xf160;
    public static final int ICON_SORT_BY_ATTRIBUTES_ALT = 0xf161;
    public static final int ICON_SORT_BY_ORDER = 0xf162;
    public static final int ICON_SORT_BY_ORDER_ALT = 0xf163;
    public static final int ICON_THUMBS_UP = 0xf164;
    public static final int ICON_THUMBS_DOWN = 0xf165;
    public static final int ICON_YOUTUBE_SIGN = 0xf166;
    public static final int ICON_YOUTUBE = 0xf167;
    public static final int ICON_XING = 0xf168;
    public static final int ICON_XING_SIGN = 0xf169;
    public static final int ICON_YOUTUBE_PLAY = 0xf16a;
    public static final int ICON_DROPBOX = 0xf16b;
    public static final int ICON_STACKEXCHANGE = 0xf16c;
    public static final int ICON_INSTAGRAM = 0xf16d;
    public static final int ICON_FLICKR = 0xf16e;
    public static final int ICON_ADN = 0xf170;
    public static final int ICON_BITBUCKET = 0xf171;
    public static final int ICON_BITBUCKET_SIGN = 0xf172;
    public static final int ICON_TUMBLR = 0xf173;
    public static final int ICON_TUMBLR_SIGN = 0xf174;
    public static final int ICON_LONG_ARROW_DOWN = 0xf175;
    public static final int ICON_LONG_ARROW_UP = 0xf176;
    public static final int ICON_LONG_ARROW_LEFT = 0xf177;
    public static final int ICON_LONG_ARROW_RIGHT = 0xf178;
    public static final int ICON_APPLE = 0xf179;
    public static final int ICON_WINDOWS = 0xf17a;
    public static final int ICON_ANDROID = 0xf17b;
    public static final int ICON_LINUX = 0xf17c;
    public static final int ICON_DRIBBLE = 0xf17d;
    public static final int ICON_SKYPE = 0xf17e;
    public static final int ICON_FOURSQUARE = 0xf180;
    public static final int ICON_TRELLO = 0xf181;
    public static final int ICON_FEMALE = 0xf182;
    public static final int ICON_MALE = 0xf183;
    public static final int ICON_GITTIP = 0xf184;
    public static final int ICON_SUN = 0xf185;
    public static final int ICON_MOON = 0xf186;
    public static final int ICON_ARCHIVE = 0xf187;
    public static final int ICON_BUG = 0xf188;
    public static final int ICON_VK = 0xf189;
    public static final int ICON_WEIBO = 0xf18a;
    public static final int ICON_RENREN = 0xf18b;
    
    private static Font fontAwesome;
    
    private int icon;
    private Color color;
    private float size;
    private Component component;
    
    /**
     * Default constructor
     * @param icon is the character code representing the icon in the font awesome charset 
     * @param color is the color of the icon
     * @param size is the size of the icon
     */
    public AwesomeIcon(int icon, Color color, float size) {
	super();
	this.icon = icon;
	this.color = color;
	this.size = size;
    }


    /**
     * Create a new font awesome icon using the icon image and a component to define color and size
     * @param icon the char code in the font awesome charset 
     * @param component the component that will provide color and size
     */
    public AwesomeIcon(int icon, Component component) {
	super();
	this.icon = icon;
	this.component = component;
    }


    
    /**
     * Return the font to draw the icon
     * @return instance of the {@link Font} class
     */
    protected Font getFont() {
	if (component != null)
	    return getFontAwesome().deriveFont((float)component.getFont().getSize() + 4); // small adjustment of the icon size
	else return getFontAwesome().deriveFont(size);
    }
    

    /** {@inheritDoc}
     */
    @Override
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
	super.paintIcon(c, g, x, y);
	Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
          RenderingHints.VALUE_RENDER_QUALITY);
        
	Font prevFont = g.getFont();
	Font iconFont = getFont();
	g.setFont(iconFont);
	if (component != null) {
//		g.setColor(new Color(51, 102, 153));
	    g.setColor(component.getForeground());
	}
	else {
	    if (color != null)
		g.setColor(color);
	}
	String s = String.valueOf((char)icon);
	int size = iconFont.getSize();
	g.drawString(s, x, y + size - (int)size/7);
	g.setFont(prevFont);
    }


    /** {@inheritDoc}
     */
    @Override
    public int getIconWidth() {
	return getFont().getSize();
    }


    /** {@inheritDoc}
     */
    @Override
    public int getIconHeight() {
	return getFont().getSize();
    }
    
    
    /**
     * Initialize the font awesome indicating its name and path in the application resource
     */
    public static void initialize() { 
	if (fontAwesome != null)
	    throw new IllegalArgumentException("Font awesome already was initialized");

	InputStream is = AwesomeIcon.class.getResourceAsStream("/resources/fonts/fontawesome-webfont.ttf");
	try {
	    fontAwesome = Font.createFont(Font.TRUETYPE_FONT, is);
	} catch (Exception e) {
	    throw new IllegalArgumentException(e);
	}
    }
    
    /**
     * Return the font awesome registered in the application resources
     * @return instance of {@link Font}
     */
    public static final Font getFontAwesome() {
    	if (fontAwesome == null) {
    		initialize();
//    		throw new IllegalAccessError("Font awesome was not initialized");
    	}
    	return fontAwesome;
    }


	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}


	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}


	/**
	 * @return the size
	 */
	public float getSize() {
		return size;
	}


	/**
	 * @param size the size to set
	 */
	public void setSize(float size) {
		this.size = size;
	}
}
