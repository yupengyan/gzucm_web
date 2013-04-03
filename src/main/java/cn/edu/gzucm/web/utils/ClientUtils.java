package cn.edu.gzucm.web.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class ClientUtils {
    private static final Logger _logger = Logger.getLogger(ClientUtils.class);

    public static <T> HttpResponse<T> ajaxPostCall(String serviceUrl, String sessionId, Class<T> resultClass, String... params) {

        String result = new String();
        String p = null;
        try {
            if (params != null && params.length > 0) {
                int i = 0;
                for (String param : params) {
                    if (i == 0) {
                        p = param;
                    } else {
                        p = p + "&" + param;
                    }
                    i++;
                }
            }
            URL url = new URL(serviceUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            if (p != null) {
                connection.setRequestProperty("Content-Length", "" + Integer.toString(p.getBytes("UTF-8").length));
            }
            if (sessionId != null) {
                connection.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
            }

            connection.setUseCaches(false);

            DataOutputStream printout = new DataOutputStream(connection.getOutputStream()); // Send POST output.
            if (p != null) {
                printout.writeBytes(p);
            }

            printout.flush();

            DataInputStream input = new DataInputStream(connection.getInputStream()); // Get response data.
            Scanner sc = new Scanner(input, "UTF-8");
            while (sc.hasNext()) {
                result += sc.next();
            }
            printout.close();

            HttpResponse response = new HttpResponse();
            response.setHeaders(connection.getHeaderFields());
            response.setResponseCode(connection.getResponseCode());
            if (resultClass == null) {

            } else if (resultClass.equals(String.class)) {
                response.setBody(result);
            } else {
                T gooGlResult = jsonToObject(result, resultClass);
                response.setBody(gooGlResult);
            }
            return response;
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }

    public static HashMap<String, Object> jsonToMap(String str) throws IOException {

        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
        };
        HashMap<String, Object> result = mapper.readValue(str, typeRef);
        return result;
    }

    public static <T> HttpResponse<T> ajaxNormalCall(String serviceUrl, String sessionId, Class<T> resultClass, String... params) {

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

            URLConnection urlConn = url.openConnection();
            urlConn.setDoInput(true); // Let the run-time system (RTS) know that we want input.
            urlConn.setUseCaches(false); // No caching, we want the real thing.
            urlConn.setRequestProperty("Content-Type", "application/json;charset=utf-8"); // Specify the content type.
            if (sessionId != null) {
                urlConn.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
            }

            DataInputStream input = new DataInputStream(urlConn.getInputStream()); // Get response data.

            Scanner sc = new Scanner(input, "UTF-8");
            while (sc.hasNext()) {
                result += sc.next();
            }

            HttpResponse response = new HttpResponse();
            response.setHeaders(urlConn.getHeaderFields());
            response.setUrl(urlConn.getURL().toString());
            if (resultClass == null) {

            } else if (resultClass.equals(String.class)) {
                response.setBody(result);
            } else {
                T gooGlResult = jsonToObject(result, resultClass);
                response.setBody(gooGlResult);
            }

            return response;
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
            String content = objectToJson(param);
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

    /**
     * 将json格式的string转成一个对象
     * @param json json字符串
     * @param a_Class 返回类型
     * @return
     */
    public static <T> T jsonToObject(final String json, final Class<T> a_Class) {

        if (null == a_Class) {
            return null;
        }
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        try {
            final T uniJSON = mapper.readValue(json, a_Class);
            return uniJSON;
        } catch (final Exception e) {
            _logger.error("", e);
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
            _logger.error("", e);
        }
        return null;
    }

    public static Date getDate(final String date) {

        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return df.parse(date);
        } catch (final ParseException e) {
            return null;
        }

    }

    /**
     * @param date  日期
     * @param format    日期格式
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

    /**
     * 比较两个时间
     * @param date1
     * @param date2
     * @return 
     * date1时间早于date2，返回-1
     * date1时间晚于date2，返回1
     * date1时间等于date2，返回0
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

    public static String GetTextLengthStr(String text) {

        int textLength = GetTextLength(text);
        int textRemainLength = 140 - textLength;
        String template = "";
        if (textRemainLength >= 0) {
            template = "还可以输入%s字";
        } else {
            template = "已经超过了%s字";
        }
        return String.format(template, String.valueOf(Math.abs(textRemainLength)));
    }

    public static int GetTextLength(String text) {

        text = text.trim();
        text = text.replaceAll("\r\n", "\n");
        int textLength = 0;
        if (text.length() > 0) {
            Pattern regex = Pattern.compile("[\\u4e00-\\u9fa5]");
            Matcher regexMatcher = regex.matcher(text);
            int _ch = 0;
            while (regexMatcher.find()) {
                _ch++;
            }
            textLength = (int) Math.ceil((text.length() + _ch) / 2);
        }
        return textLength;
    }

    public static boolean isNumeric(final String str) {

        final Pattern pattern = Pattern.compile("[0-9]*");
        final Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
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

        if (ClientUtils.isEmpty(url)) {
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

    public static <T> void doReflection(final Class<T> a_class) throws Exception {

        Method m[] = a_class.getMethods();
        for (int i = 0; i < m.length; i++)
            _logger.info(m[i].toString());
        Field f[] = a_class.getDeclaredFields();
        for (int i = 0; i < f.length; i++)
            _logger.info(f[i].toString());
    }

    public static int getDayOfYear(int year, int month, int day) {

        if (year < 0 || month > 12 || month < 0 || day > 31 || day < 0) {
            throw new IllegalArgumentException("Illegal Argument.");
        }
        int dayOfYear = 0;
        boolean isLeapYear = ClientUtils.isLeapYear(year);
        int[] daysPerMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        for (int i = 0; i < month - 1; i++) {
            if (i == 1) {
                if (isLeapYear) {
                    dayOfYear += daysPerMonth[i] + 1;
                } else {
                    dayOfYear += daysPerMonth[i];
                }
            } else {
                dayOfYear += daysPerMonth[i];
            }
        }
        dayOfYear += day;
        return dayOfYear;
    }

    public static boolean isLeapYear(int year) {

        return year % 4 == 0;
    }

    public static void main(String[] args) {

        int year = 2012;
        //        int month = 1;
        //        int day = 1;
        int dayOfYear = 0;
        Calendar calendar = Calendar.getInstance();
        int calendar_dayOfYear = 0;
        StringBuilder getDayOfYear = new StringBuilder();
        StringBuilder calendar_DAY_OF_YEAR = new StringBuilder();
        for (int month = 1; month <= 12; month++) {
            for (int day = 1; day <= 31; day++) {
                dayOfYear = ClientUtils.getDayOfYear(year, month, day);
                calendar.set(year, month - 1, day);
                calendar_dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
                System.out.println(getDayOfYear.append(year).append("-").append(month).append("-").append(day).append("getDayOfYear:").append(dayOfYear).toString());
                System.out.println(calendar_DAY_OF_YEAR.append(year).append("-").append(month).append("-").append(day).append("Calendar.DAY_OF_YEAR:").append(calendar_dayOfYear).toString());
                getDayOfYear = null;
                getDayOfYear = new StringBuilder();
                calendar_DAY_OF_YEAR = null;
                calendar_DAY_OF_YEAR = new StringBuilder();
            }
        }
    }
}
