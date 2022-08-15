package com.seeyon.apps.vastdata.util;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.seeyon.ctp.common.AppContext;
import com.seeyon.ctp.common.ctpenumnew.manager.EnumManager;
import com.seeyon.ctp.common.po.ctpenumnew.CtpEnumItem;
import com.seeyon.ctp.organization.manager.OrgManager;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuwenping on 2020/11/10.
 */
public class WebUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");


    public static void processCrossOriginResponse(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type,Token,Accept, Connection, User-Agent, Cookie");
        response.setHeader("Access-Control-Max-Age", "3628800");
    }

    public static void responseJSON(Object data, HttpServletResponse response) {
        processCrossOriginResponse(response);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control",
                "no-store, max-age=0, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = null;
        //lamda;；
        try {
            out = response.getWriter();
            out.write(JSON.toJSONString(data, SerializerFeature.DisableCircularReferenceDetect));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } finally {

            }

        }
    }

    public static void responseJSONStriong(Object data, HttpServletResponse response) {
        processCrossOriginResponse(response);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control",
                "no-store, max-age=0, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = null;
        //lamda;；
        try {
            out = response.getWriter();
            out.write(JSON.toJSONString(data));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } finally {

            }

        }
    }

    /**
     * 下划线转驼峰法(默认小驼峰)
     *
     * @param line       源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰(驼峰，第一个字符是大写还是小写)
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line, boolean... smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        //匹配正则表达式
        while (matcher.find()) {
            String word = matcher.group();
            //当是true 或则是空的情况
            if ((smallCamel.length == 0 || smallCamel[0]) && matcher.start() == 0) {
                sb.append(Character.toLowerCase(word.charAt(0)));
            } else {
                sb.append(Character.toUpperCase(word.charAt(0)));
            }

            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰法转下划线
     *
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase()
                .concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    public static <T> T parseValueByFieldClassType(Object val, Class<T> type) {
        if (val == null) {
            return null;
        }
        String sVal = String.valueOf(val);
        //空串不处理
        try {
            if (type == String.class) {
                return (T) String.valueOf(val);
            }
            if (type == Long.class) {
                return (T) Long.valueOf(sVal);
            }
            if (type == Double.class) {
                return (T) Double.valueOf(sVal);
            }
            if (type == Float.class) {
                return (T) Float.valueOf(sVal);
            }
            if (type == Integer.class) {
                return (T) Integer.valueOf(sVal);
            }
            if (type == Short.class) {
                return (T) Short.valueOf(sVal);
            }
            if (type == Date.class) {
                return (T) parseDate(sVal);
            }
            if (type == BigDecimal.class) {
                return (T) new BigDecimal(sVal);
            }
            return JSON.parseObject(sVal, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public static boolean isEmpty(Collection collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(Collection collection) {

        return !isEmpty(collection);
    }

    public static boolean isEmpty(Map map) {
        if (map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(Object[] objs) {
        if (objs == null || objs.length == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(Object[] objs) {
        return isEmpty(objs);
    }

    public static Long paserLong(Object obj) {
        if (obj == null) {
            return null;
        }
        Long val = null;
        try {
            val = Long.parseLong(String.valueOf(obj));
        } finally {
            return val;
        }

    }

    public static Boolean getBoolean(Object obj) {
        String val = String.valueOf(obj);
        try {
            return Boolean.valueOf(val);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;

    }

    public static Long getLong(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).longValue();
        }
        Long val = null;
        try {
            val = Long.parseLong(String.valueOf(obj));
        } finally {
            return val;
        }

    }

    public static Double getDouble(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Double) {
            return (Double) obj;
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).doubleValue();
        }
        Double val = null;
        try {
            val = Double.parseDouble(String.valueOf(obj));
        } finally {
            return val;
        }

    }

    public static Float getFloat(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Float) {
            return (Float) obj;
        }
        if (obj instanceof Double) {
            return ((Double) obj).floatValue();
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).floatValue();
        }
        Float val = null;
        try {
            val = Float.parseFloat(String.valueOf(obj));
        } finally {
            return val;
        }

    }

    public static Integer getInteger(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).intValue();
        }
        Integer val = null;
        try {
            val = Integer.parseInt(String.valueOf(obj));
        } finally {
            return val;
        }

    }

    public static Date parseDate(String dateStr) {
        try {
            if (isEmpty(dateStr)) {
                return null;
            }
            if (dateStr.trim().length() == "yyyy-MM-dd".length()) {
                return sdf2.parse(dateStr);
            }
            Date dt = sdf.parse(dateStr);
            return dt;
        } catch (Exception e) {

        }
        return null;
    }

    public static String formatDate(Date date) {
        try {
            return sdf.format(date);
        } catch (Exception e) {

        }
        return null;
    }

    public static String formatDateSimple(Date date) {
        try {
            return sdf2.format(date);
        } catch (Exception e) {

        }
        return null;
    }

    public static String formatDateHourMinute(Date date) {
        try {
            return sdf1.format(date);
        } catch (Exception e) {

        }
        return null;
    }

    public static int getYear(String dtStr) {

        return getYear(parseDate(dtStr));
    }

    public static int getYear(Date dt) {
        Calendar calendar = Calendar.getInstance();
        if (dt != null) {
            calendar.setTime(dt);
        }
        return calendar.get(Calendar.YEAR);
    }

    public static int getYear() {

        return getYear(new Date());
    }


    public static String unicodeEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);

            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        System.out.println("unicodeBytes is: " + unicodeBytes);
        return unicodeBytes;

    }

    private static EnumManager enumManager;

    private static EnumManager getEnumManager() {
        if (enumManager == null) {
            enumManager = (EnumManager) AppContext.getBean("enumManagerNew");
            if (enumManager == null) {
                enumManager = (EnumManager) AppContext.getBean("enumManager");
            }
        }
        return enumManager;

    }



    public static Object getEnumShowValue(Object obj) {

        Long id = getLong(obj);
        if (id == null) {
            return obj;
        }
        try {
            CtpEnumItem item = getEnumManager().getCtpEnumItem(id);
            if (item != null) {
                return item.getShowvalue();
            }
        } catch (Exception e) {

        }
        return obj;
    }

    public static Object getOrgValueByDeptIdAndType(Object obj, int type) {

        Long id = getLong(obj);
        if (id == null) {
            return obj;
        }
        try {
////            V3xOrgDepartment department = getOrgManager().getDepartmentById(id);
////            if (department == null) {
////                return obj;
////            }
////            if(type==0){
////                String code = department.getCode();
////                if(CommonUtils.isEmpty(code)){
////                    return department.getName();
////                }
////                return code;
////            }else{
////                Long accountId = department.getOrgAccountId();
////                V3xOrgAccount account = getOrgManager().getAccountById(accountId);
////                if(account==null){
////                    return obj;
////                }
////                String code = account.getCode();
////                if(CommonUtils.isEmpty(code)){
////                    return account.getName();
////                }
////                return code;
//
//            }


        } catch (Exception e) {

        }
        return obj;
    }

    public static Map<String, String> genTableDataMapByColumns(List<Map> columnDataList) {
        Map<String, String> dataMap = new HashMap<String, String>();
        for (Map col : columnDataList) {
            dataMap.put("" + col.get("column_name"), "" + col.get("type_name"));
        }
        return dataMap;
    }

    public static OrgManager getOrgManager() {


        return (OrgManager) AppContext.getBean("orgManager");
    }

    public static <T> T copyProIfNotNullReturnSource(T source, T dest) {

        String sourceStr = JSON.toJSONString(source);
        Map sourceMap = JSON.parseObject(sourceStr, HashMap.class);
        String descStr = JSON.toJSONString(dest);
        Map destMap = JSON.parseObject(descStr, HashMap.class);
        for (Object key : destMap.keySet()) {
            Object dt = destMap.get(key);
            if (dt != null) {
                sourceMap.put(key, dt);
            }
        }
        return (T) JSON.parseObject(JSON.toJSONString(sourceMap), source.getClass());

    }


    public static String getGetMethodName(Field fd) {
        String preFix = "get";
        if (fd.getType() == Boolean.class) {

            preFix = "is";

        }
        String fdName = fd.getName();
        String sName = preFix + fdName.substring(0, 1).toUpperCase() + fdName.substring(1);

        return sName;

    }

    public static final char UNDERLINE = '_';

    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString().toLowerCase();
    }

    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String joinExtend(Collection objs, String token) {
        if (isEmpty(objs)) {
            return "";
        }
        StringBuilder stb = new StringBuilder();
        int index = 0;
        for (Object obj : objs) {
            if (index == 0) {
                stb.append(obj.toString());
            } else {
                stb.append(token).append(obj.toString());
            }
            index++;

        }
        return stb.toString();
    }

    public static String join(List<String> objs, String token) {
        return joinExtend(objs, token);
    }

    public static String joinSet(Set objs, String token) {
        return joinExtend(objs, token);
    }

    public static String joinArray(String[] objs, String token) {
        if (objs == null || objs.length == 0) {
            return "";
        }
        StringBuilder stb = new StringBuilder();
        int index = 0;
        for (String obj : objs) {
            if (index == 0) {
                stb.append(obj);
            } else {
                stb.append(token).append(obj);
            }
            index++;

        }
        return stb.toString();
    }

    public static <T> T parseObjectByRequest(HttpServletRequest request, Class<T> cls) {
        Map data = new HashMap();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            data.put(param, request.getParameter(param));
        }
        if (cls == Map.class || cls == HashMap.class) {
            return (T) data;
        }
        T item = JSON.parseObject(JSON.toJSONString(data), cls);
        return item;
    }

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }


}

