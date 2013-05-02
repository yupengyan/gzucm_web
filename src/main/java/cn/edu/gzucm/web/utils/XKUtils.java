package cn.edu.gzucm.web.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

public class XKUtils {

    private static Logger _logger = Logger.getLogger(XKUtils.class);

    public static Date baseTime = XKUtils.getDate("1970-01-01 00:00:00");

    public static boolean isEmpty(final String a_String) {

        if (a_String == null || a_String.trim().equals("") || a_String.trim().equals("null")) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(final Collection<?> collection) {

        if (collection == null || collection.size() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(final Integer integer) {

        if (integer == null || integer == 0) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Object[] array) {

        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    public static String getFileExtension(final String fileName) {

        if (isEmpty(fileName)) {
            return "";
        }
        String ext = "";
        final int mid = fileName.lastIndexOf(".");
        ext = fileName.substring(mid + 1, fileName.length());
        return ext;
    }

    public static String getFilename(final String fileNameWithPath) {

        if (isEmpty(fileNameWithPath)) {
            return "";
        }
        String ext = "";
        final int mid = fileNameWithPath.lastIndexOf("/");
        ext = fileNameWithPath.substring(mid + 1, fileNameWithPath.length());
        return ext;
    }

    public static String removeFileExtension(final String fileName) {

        if (isEmpty(fileName)) {
            return "";
        }
        String ext = "";
        final int mid = fileName.lastIndexOf(".");
        ext = fileName.substring(0, mid);
        return ext;
    }

    /**
     * 将上传的文件存入指定的位置
     * @param multipartFile
     * @param filename
     * @throws IOException
     */
    public static void writeToFile(final MultipartFile multipartFile, final String filename) throws IOException {

        final File file = new File(filename);
        FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());

    }

    /**
    * 将上传的图片存入到系统配置的目录，并自动生成一个唯一的名称
    * @param multipartFile
    * @return
    * @throws IOException
    */
    public static String[] savePublicPicture(final MultipartFile multipartFile) throws IOException {

        String[] names = XKUtils.generateFilenames(multipartFile.getOriginalFilename());
        XKUtils.writeToFile(multipartFile, names[2]);
        XKUtils.makeThumbFixedWidth(names[3], names[2]);

        return names;
    }

    /**
     * 返回一个服务器的当前时间
     * @return
     */
    public static String getNow() {

        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    /**
     * 此日期用了生成目录名，不带时区
     * @return
     */
    public static String getToday() {

        final DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(new Date());

    }

    /**
     * yyyy-MM-dd HH:mm:ss
     * 
     * @param date
     * @return
     */
    public static String getFullDateInString(Date date) {

        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date == null) {
            date = new Date();
        }
        return df.format(date);

    }

    /**
     * yyyy-MM-dd HH:mm
     * @param date
     * @return
     */
    public static String getDatetimeInString(Date date) {

        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (date == null) {
            date = new Date();
        }
        return df.format(date);

    }

    /**
     * yyyy-MM-dd
     * @param date
     * @return
     */
    public static String getDateInString(Date date) {

        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (date == null) {
            date = new Date();
        }
        return df.format(date);

    }

    /**
     * HH:mm
     * @param date
     * @return
     */
    public static String getTimeInString(Date date) {

        final DateFormat df = new SimpleDateFormat("HH:mm");
        if (date == null) {
            date = new Date();
        }
        return df.format(date);

    }

    /**
     * HH
     * @param date
     * @return
     */
    public static String getHourInString(Date date) {

        final DateFormat df = new SimpleDateFormat("HH");
        if (date == null) {
            date = new Date();
        }
        return df.format(date);

    }

    /**
     * mm
     * @param date
     * @return
     */
    public static String getMinuteInString(Date date) {

        final DateFormat df = new SimpleDateFormat("mm");
        if (date == null) {
            date = new Date();
        }
        return df.format(date);

    }

    /**
     * 
     * @param date yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date getDate(final String date) {

        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return df.parse(date);
        } catch (final ParseException e) {
            return null;
        }

    }

    /**
     * 
     * @param date yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date getDate(final String date, final String format) {

        final DateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(date);
        } catch (final ParseException e) {
            return null;
        }

    }

    /**
     * 按指定格式返回时间
     * @param date 
     * @return format
     */
    public static String getDateStr(Date date, final String format) {

        final DateFormat df = new SimpleDateFormat(format);
        if (date == null) {
            date = new Date();
        }
        return df.format(date);
    }

    public static Date getStringInDate(final String str, final String format) {

        final DateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 这一周开始的一天
     * @param date 指定的时间
     * */
    public static Date getFristDayForWeek(final Date date) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return DateUtils.truncate(calendar.getTime(), Calendar.DATE);
    }

    /**
     * 获取的当前月份开始的第一天
     * @param date 指定的时间
     * */
    public static Date getFristDayForMonth(final Date date) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        return DateUtils.truncate(calendar.getTime(), Calendar.DATE);
    }

    /**
     * 
     * @param date yyyy-MM-dd HH:mm
     * @return
     */
    public static Date getDateTime(final String date) {

        if (isEmpty(date)) {
            return null;
        }

        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            return df.parse(date);
        } catch (final ParseException e) {
            //            LoggerUtil.error(_logger, e);
            return null;
        }

    }

    /**
     * 
     * @param date HH:mm
     * @return 
     */
    public static Date getTime(final String date) {

        return getTime(null, date);
    }

    public static Date getTime(final Date _date, final String date) {

        final DateFormat df = new SimpleDateFormat("HH:mm");

        try {
            final Date time = df.parse(date);
            final Calendar cal = Calendar.getInstance();
            cal.setTime(time);

            Date result = _date != null ? _date : new Date();
            result = DateUtils.setMinutes(result, cal.get(Calendar.MINUTE));
            result = DateUtils.setHours(result, cal.get(Calendar.HOUR_OF_DAY));

            return result;
        } catch (final ParseException e) {
            //            LoggerUtil.error(_logger, e);
            return null;
        }
    }

    /**
     * 返回一个基于客户端时区的时间
     * @param date yyyy-MM-dd
     * @return
     */
    public static Date getNowInClientZone(TimeZone clientZone) {

        //        Date time = TimeZoneUtils.convertToClientZone(new Date(), clientZone);
        //        return time;
        return null;
    }

    /**
     * 返回一个基于客户端时区的时间
     * @param date yyyy-MM-dd
     * @return
     */
    public static Date getSimpleDate(final String date, TimeZone clientZone) {

        //        if (isEmpty(date)) {
        //            return null;
        //        }
        //
        //        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //
        //        try {
        //            Date result = df.parse(date);
        //            Date time = TimeZoneUtils.convertToClientZone(new Date(), clientZone);
        //
        //            final Calendar cal = Calendar.getInstance();
        //            cal.setTime(time);
        //            result = DateUtils.setMinutes(result, cal.get(Calendar.MINUTE));
        //            result = DateUtils.setHours(result, cal.get(Calendar.HOUR_OF_DAY));
        //
        //            return result;
        //        } catch (final ParseException e) {
        //            return null;
        //        }
        return null;
    }

    /**
     * 
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date getSimpleDateTime(final String dateTime) {

        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return df.parse(dateTime);
        } catch (final ParseException e) {
            //            LoggerUtil.error(_logger, e);
            return null;
        }

    }

    /**
     * 
     * @return yyyy-MM-dd
     */
    public static String getSimpleDateInString() {

        return getSimpleDateInString(new Date());
    }

    /**
     * 
     * @param date
     * @return yyyy-MM-dd
     */
    public static String getSimpleDateInString(final Date date) {

        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public static HttpSession getHttpSession() {

        final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(false); // true == allow create
    }

    public static HttpServletRequest getHttpRequest() {

        final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        if (attr != null) {
            return attr.getRequest();
        }
        return null;
    }

    public static String getAccessDomain() {

        final HttpServletRequest request = XKUtils.getHttpRequest();
        if (request != null) {
            String name = request.getServerName();
            if (request.getServerPort() != 80) {
                name = name + ":" + request.getServerPort();
            }
            return name;
        }
        return null;
    }

    /**
     * 将一个对象转换成json格式的string
     * @param object
     * @return
     */
    public static String objectToJson(final Object object) {

        final ObjectMapper mapper = new ObjectMapper();
        try {
            final String uniJSON = mapper.writeValueAsString(object);
            return uniJSON;
        } catch (final Exception e) {
            //            LoggerUtil.error(_logger, e);
        }
        return null;
    }

    /**
     * 将json格式的string转成一个对象
     * @param object
     * @return
     */
    public static <T> T jsonToObject(final String json, final Class<T> a_Class) {

        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        try {
            final T uniJSON = mapper.readValue(json, a_Class);
            return uniJSON;
        } catch (final Exception e) {
            //            LoggerUtil.logging(_logger, Level.ERROR, e, "json:", json);
        }
        return null;
    }

    public static <T> T jsonToObject(final String json, final Class<T> a_Class, final Class mixinClass) {

        final ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.getSerializationConfig().addMixInAnnotations(a_Class, mixinClass);
            final T uniJSON = mapper.readValue(json, a_Class);
            return uniJSON;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json格式的结果，以text_html的方式返回
     * @param model
     * @param response
     * @return
     */
    public static ModelAndView renderPlanText(final Object model, final HttpServletResponse response) {

        final MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();

        final MediaType jsonMimeType = MediaType.TEXT_HTML;

        try {
            jsonConverter.write(model, jsonMimeType, new ServletServerHttpResponse(response));
        } catch (final HttpMessageNotWritableException e) {
            //            LoggerUtil.error(_logger, e);
        } catch (final IOException e) {
            //            LoggerUtil.error(_logger, e);
        }

        return null;
    }

    /**
     * 当json格式返回String时不知为缺省不是UTF-8的方式，此方法返回一个json格式的基于UTF-8编码的String
     * @param model
     * @param response
     * @return
     */
    public static ResponseEntity<String> renderUTF8StringJson(final String text, final HttpServletResponse response) {

        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=utf-8");

        return new ResponseEntity<String>(text, responseHeaders, HttpStatus.CREATED);
    }

    public static void writeTextFile(final String fFileName, final String text) throws IOException {

        final Writer out = new OutputStreamWriter(new FileOutputStream(fFileName), "UTF-8");
        try {
            out.write(text);
        } finally {
            out.close();
        }
    }

    /** Read the contents of the given file. */
    public static String readTextFile(final String fFileName) throws IOException {

        final StringBuilder text = new StringBuilder();
        final String NL = System.getProperty("line.separator");
        final Scanner scanner = new Scanner(new FileInputStream(fFileName), "UTF-8");
        try {
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine() + NL);
            }
        } finally {
            scanner.close();
        }

        return text.toString();
    }

    /** Read the contents of the given file. */
    public static String readTextFile(final InputStream file) throws IOException {

        final StringBuilder text = new StringBuilder();
        final String NL = System.getProperty("line.separator");
        final Scanner scanner = new Scanner(file, "UTF-8");
        try {
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine() + NL);
            }
        } finally {
            scanner.close();
        }

        return text.toString();
    }

    /**
     * 
     * @param date1
     * @param date2
     * @return -1 if date1 is before date2
     *         -2 if date1 is after date2
     *         0 if equals
     */
    public static int compareDates(final Date date1, final Date date2) {

        final Calendar cal1 = Calendar.getInstance();
        final Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        if (cal1.before(cal2)) {
            return -1;
        } else if (cal1.after(cal2)) {
            return 1;
        } else {
            return 0;
        }
    }

    public static boolean makeThumbFixedWidth(final String outputFile, final String inputFile) {

        //        try {
        //            return XKPictureMerger.resizeByFixedWidth(outputFile, inputFile, 160);
        //        } catch (final IOException e) {
        //            LoggerUtil.error(_logger, e);
        //            return false;
        //        }
        return false;
    }

    public static boolean makeThumb(final String outputFile, final String inputFile) {

        //        try {
        //            return XKPictureMerger.makeThumb(outputFile, inputFile);
        //        } catch (final IOException e) {
        //            LoggerUtil.error(_logger, e);
        //            return false;
        //        }
        return false;
    }

    /**
     * 图片网址转换
     * @param isUser true 用户；false 微博
     * @param url
     * @param size 0 缩略图  1中图  2 大图
     * @return
     */
    public static String translateImageUrl(final boolean isUser, final String url, final int size) {

        //腾讯    在返回的头像地址后面加上 /20 /30 /40 /50 /100 返回相应大小的图片。
        //     返回图片地址请在后面加上 /120 /160 /460 /2000。 

        if (isEmpty(url)) {
            return url;
        }

        if (url.indexOf("sinaimg") > 0) {
            //新浪
            //  http://tp1.sinaimg.cn/1911041220/50/1300263394/1
            final String User_Thumb = "/30/";
            final String User_Flag = "/50/";
            final String User_Large = "/180/";
            //  http://ww1.sinaimg.cn/large/62062d82jw6dflw3vv7guj.jpg
            final String Blog_Thumb = "/thumbnail/";
            final String Blog_Middle = "/bmiddle/";
            final String Blog_Flag = "/large/";

            String oldFlag = null;
            String newFlag = null;

            if (isUser) {
                oldFlag = User_Flag;
                if (size == 0) {
                    newFlag = User_Thumb;
                } else if (size == 1) {
                    newFlag = User_Flag;
                } else {
                    newFlag = User_Large;
                }
            } else {
                oldFlag = Blog_Thumb;
                if (size == 0) {
                    newFlag = Blog_Thumb;
                } else if (size == 1) {
                    newFlag = Blog_Middle;
                } else {
                    newFlag = Blog_Flag;
                }
            }

            return url.replace(oldFlag, newFlag);
        } else if (url.indexOf("qlogo.") > 0 || url.indexOf("qpic.") > 0) {
            //腾讯
            String closeText = null;
            if (isUser) {
                if (size == 0) {
                    closeText = "30";
                } else if (size == 1) {
                    closeText = "50";
                } else {
                    closeText = "100";
                }
            } else if (size == 0) {
                closeText = "120";
            } else if (size == 1) {
                closeText = "460";
            } else {
                closeText = "2000";
            }
            return url + "/" + closeText;
        }
        return url;

    }

    public static String emailToMailBox(final String email) {

        if (isEmpty(email)) {
            return email;
        }

        final int atLocation = email.indexOf("@");
        return "mail." + email.substring(atLocation + 1, email.length());
    }

    /**
     * 添加一个timestamp,一般用来测试性能是计算时间，接着用calculateTime
     * @return
     */
    public static long addTimeStamp() {

        return new Date().getTime();
    }

    /**
     * 先用addTimeStamp添加一个timestamp,一般用来测试性能是计算时间
     * @return
     */
    public static float calculateTime(final long oldTimeStamp) {

        final long mSeconds = new Date().getTime() - oldTimeStamp;
        return mSeconds / 1000;
    }

    /**
     * 
     * @param originalFilename  faoiefao-iefupaoiu-fewaef.jpg
     * @return 0:/UploadFiles/Temp/20120202/faoiefao-iefupaoiu-fewaef.jpg
     *         1:/UploadFiles/Temp/20120202/thum_faoiefao-iefupaoiu-fewaef.jpg
     *         2:c:/xuanker/config/UploadFiles/Temp/20120202/faoiefao-iefupaoiu-fewaef.jpg
     *         3:c:/xuanker/config/UploadFiles/Temp/20120202/thum_faoiefao-iefupaoiu-fewaef.jpg
     */
    public static String[] generateFilenames(final String originalFilename) {

        final String path = MyProperties.getConfigPath();
        final String basicName = UUID.randomUUID() + "." + XKUtils.getFileExtension(originalFilename);
        final String today = XKUtils.getToday();
        //        final String relativePath = XKProperties.PUBLIC_PICTURE_PREFIX + "/" + today + "/" + basicName;
        //        final String relativeThumb = XKProperties.PUBLIC_PICTURE_PREFIX + "/" + today + "/thum_" + basicName;

        //        return new String[] { relativePath, relativeThumb, path + relativePath, path + relativeThumb };
        return null;
    }

    public static String[] splitStringToArray(final String stringVar, final String splitTag) {

        String[] str = null;

        final int index = stringVar.indexOf(splitTag);

        if (index > 0) {
            str = stringVar.split(splitTag);
        }

        return str;
    }

    public static String arrayToString(final String[] strings, final String splitTag) {

        String result = null;
        for (String str : strings) {
            if (result == null) {
                result = str;
            } else {
                result = result + splitTag + str;
            }
        }

        return result;
    }

    public static boolean isNumeric(final String str) {

        final Pattern pattern = Pattern.compile("[0-9]*");
        final Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static HashMap<String, Object> jsonToMap(String str) throws IOException {

        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
        };
        HashMap<String, Object> result = mapper.readValue(str, typeRef);
        return result;
    }

    public static String addToCSV(String baseString, String newValue) {

        if (isEmpty(baseString)) {
            baseString = newValue;
        } else {
            if (baseString.equals(newValue) || baseString.indexOf(newValue + ",") >= 0 || baseString.indexOf("," + newValue) >= 0) {
                return baseString;
            } else {
                baseString = baseString + "," + newValue;
            }
        }
        return baseString;
    }

    public static String addToCSVAsFirst(String baseString, String newValue) {

        if (isEmpty(baseString)) {
            baseString = newValue;
        } else {
            if (baseString.equals(newValue) || baseString.indexOf(newValue + ",") >= 0 || baseString.indexOf("," + newValue) >= 0) {
                return baseString;
            } else {
                baseString = newValue + "," + baseString;
            }
        }
        return baseString;
    }

    public static boolean valueInCSV(String baseString, String value) {

        if (baseString == null || baseString.length() == 0) {
            return false;
        } else {
            if (baseString.equals(value) || baseString.indexOf(value + ",") > -1 || baseString.indexOf("," + value) > -1) {
                return true;
            }
        }
        return false;
    }

    public static String indexInCSV(String baseString, int index) {

        if (baseString == null || baseString.length() == 0) {
            return null;
        }

        String[] strs = baseString.split(",");
        if (strs.length <= index) {
            return null;
        }
        return strs[index];
    }

    public static String removeFromCSV(String baseString, String removeStr) {

        if (isEmpty(baseString)) {
            return baseString;
        }
        if (baseString.equals(removeStr)) {
            return "";
        }
        baseString = baseString.replaceAll(removeStr + ",", "");
        baseString = baseString.replaceAll("," + removeStr, "");
        if (isEmpty(baseString)) {
            return null;
        }
        return baseString;
    }

    /**
     * 获取随机数
     * */
    public static int getRandom(int min, int max) {

        Random r = new Random();
        return r.nextInt(max + 1 - min) + min;

    }

    public static List<String> extractUrls(String input) {

        List<String> result = new ArrayList<String>();

        Pattern pattern = Pattern.compile("\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)" + "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov" + "|mil|biz|info|mobi|name|aero|jobs|museum" + "|travel|[a-z]{2}))(:[\\d]{1,5})?" + "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" + "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)" + "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*" + "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b");

        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }

    public static Map<String, String> getUrlParams(String url) {

        Map<String, String> result = new HashMap<String, String>();
        if (url != null && url.indexOf("?") >= 0) {
            String[] strs = url.split("\\?");
            if (strs.length > 1) {
                for (String str : strs[1].split("&")) {
                    String[] pair = str.split("=");
                    if (pair.length == 2) {
                        result.put(pair[0], pair[1]);
                    }
                }
            }
        }
        return result;
    }

    public static <T> T ajaxNormalCall(String serviceUrl, Class<T> resultClass, String... params) {

        return ajaxNormalCall(serviceUrl, null, resultClass, params);

    }

    public static <T> T ajaxNormalCall(String serviceUrl, Class<T> resultClass, int timeOutSeconds, String... params) {

        return ajaxNormalCall(serviceUrl, null, resultClass, timeOutSeconds, params);

    }

    public static <T> T ajaxNormalCall(String serviceUrl, String authInfo, Class<T> resultClass, String... params) {

        return ajaxNormalCall(serviceUrl, authInfo, resultClass, 0, params);
    }

    public static <T> T ajaxNormalCall(String serviceUrl, String authInfo, Class<T> resultClass, int timeOutSeconds, String... params) {

        String result = new String();

        try {
            if (params != null && params.length > 0) {
                int i = 0;
                for (String param : params) {
                    if (i == 0) {
                        serviceUrl = serviceUrl + "?" + param;
                    } else {
                        serviceUrl = serviceUrl + "&" + param;
                    }
                    i++;
                }
            }
            URL url = new URL(serviceUrl);

            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true); // Let the run-time system (RTS) know that we want input.
            urlConn.setUseCaches(false); // No caching, we want the real thing.
            urlConn.setRequestProperty("Content-Type", "application/json;charset=utf-8"); // Specify the content type.
            if (timeOutSeconds > 0) {
                urlConn.setConnectTimeout(timeOutSeconds * 1000);
                urlConn.setReadTimeout(timeOutSeconds * 1000);
            }
            if (authInfo != null) {

                String encoding = Base64.encodeBase64String(authInfo.getBytes()).replaceAll("\r\n", "");
                urlConn.setRequestProperty("Authorization", "Basic " + encoding);
            }
            DataInputStream input = new DataInputStream(urlConn.getInputStream()); // Get response data.

            Scanner sc = new Scanner(input, "UTF-8");
            while (sc.hasNext()) {
                result += sc.next();
            }

            if (resultClass == null) {
                return null;
            } else if (resultClass.equals(String.class)) {
                return (T) result;
            } else {
                T gooGlResult = jsonToObject(result, resultClass);
                return gooGlResult;
            }
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }

    public static <T> T ajaxJsonCall(String serviceUrl, Object param, Class<T> resultClass) {

        String result = new String();

        try {
            URL url = new URL(serviceUrl);

            URLConnection urlConn = url.openConnection();
            urlConn.setDoInput(true); // Let the run-time system (RTS) know that we want input.
            urlConn.setDoOutput(true); // Let the RTS know that we want to do output.
            urlConn.setUseCaches(false); // No caching, we want the real thing.
            urlConn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConn.setRequestProperty("Accept", "*/*");
            urlConn.setRequestProperty("Accept-Encoding", "gzip,deflate");
            //urlConn.setRequestProperty("Accept-Language", "*/*"); 

            DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream()); // Send POST output.
            String content = XKUtils.objectToJson(param);
            //printout.writeBytes(content);
            printout.write(content.getBytes("utf-8"));
            printout.flush();
            printout.close();

            DataInputStream input = new DataInputStream(urlConn.getInputStream()); // Get response data.

            Scanner sc = new Scanner(input, "UTF-8");
            while (sc.hasNext()) {
                result += sc.next();
            }

            if (resultClass == null) {
                return null;
            } else {
                T gooGlResult = jsonToObject(result, resultClass);
                return gooGlResult;
            }
        } catch (Exception ex) {
            _logger.error("", ex);
            return null;
        }
    }

    public static boolean isInternalAccess() {

        String address = getHttpRequest().getRemoteAddr();
        if (address != null && ("127.0.0.1".equals(address) || address.startsWith("192.168."))) {
            return true;
        }
        return false;
    }

    public static Date getUnixTime(long unixTime) {

        long timeStamp = unixTime * 1000;
        return new Date(timeStamp);
    }

    public static long getUnixTime(Date timeStamp) {

        return timeStamp.getTime() / 1000;
    }

    public static int daysBetween(Date earlyDate, Date laterDate) {

        return (int) ((laterDate.getTime() - earlyDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static String getEditionName(Integer edition) {

        if (edition == 0) {
            return "免费版";
        } else if (edition == 1) {
            return "企业标准版";
        } else if (edition == 2) {
            return "企业增强版";
        } else if (edition == 3) {
            return "企业豪华版";
        } else if (edition == 4) {
            return "企业旗舰版";
        } else if (edition == 5) {
            return "企业定制版";
        }

        return null;
    }

    public static BigDecimal addCommission(BigDecimal cost, BigDecimal commissionFee) {

        if (cost == null) {
            return null;
        }

        return cost.add(cost.multiply(commissionFee));
    }

    public static boolean userExistRemotely(String email) {

        //        String server = null;
        //        if (AppConstants.isEnterprise()) {
        //            server = XKProperties.DOMAIN_FREE_INT;
        //        } else {
        //            server = XKProperties.DOMAIN_ENTERPRISE_INT;
        //        }
        //        try {
        //            return XKUtils.ajaxNormalCall("http://" + server + XKRmiService.userExists, Boolean.class, 5, "email=" + email);
        //        } catch (Exception e) {
        //            _logger.error("", e);
        //            return false;
        //        }
        return false;
    }
}
