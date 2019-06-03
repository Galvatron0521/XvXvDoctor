package cn.com.xxdoctor.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * wubj 创建于 2017/3/13 0010.
 */
public class JSONUtil {

    /**
     * 把一个map变成json字符串
     *
     * @param map 传入要解析的map
     * @return 解析成功返回json字符串, 失败返回null
     */
    public static String parseMapToJson(Map<?, ?> map) {
        try {
            Gson gson = new Gson();
            return gson.toJson(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把一个json字符串变成对象
     *
     * @param json json字符串
     * @param cls  javaBean的class
     * @return 封装后的对象
     */
    public static <T> T parseJsonToBean(String json, Class<T> cls) {
        Gson gson = new Gson();
        T t = null;
        try {
            t = gson.fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 把json字符串变成map
     *
     * @param json json字符串
     * @return 封装后的集合
     */
    public static HashMap<String, Object> parseJsonToMap(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        HashMap<String, Object> map = null;
        try {
            map = gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 把json字符串变成集合
     * params: new TypeToken<List<yourBean>>(){}.getType()
     *
     * @param json json字符串
     * @param type 类型
     * @return 封装后的集合
     */
    public static <T> List<T> parseJsonToList(String json, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    public static boolean isPrintException = true;

    public static Long getLong(JSONObject jsonObject, String key, Long defaultValue) {
        if (jsonObject != null && !StringUtils.isEmpty(key)) {
            try {
                return jsonObject.getLong(key);
            } catch (JSONException var4) {
                if (isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static Long getLong(String jsonData, String key, Long defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject e = new JSONObject(jsonData);
                return getLong(e, key, defaultValue);
            } catch (JSONException var4) {
                if (isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        }
    }

    public static long getLong(JSONObject jsonObject, String key, long defaultValue) {
        return getLong(jsonObject, key, Long.valueOf(defaultValue));
    }

    public static long getLong(String jsonData, String key, long defaultValue) {
        return getLong(jsonData, key, Long.valueOf(defaultValue));
    }

    public static Integer getInt(JSONObject jsonObject, String key, Integer defaultValue) {
        if (jsonObject != null && !StringUtils.isEmpty(key)) {
            try {
                return jsonObject.getInt(key);
            } catch (JSONException var4) {
                if (isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static Integer getInt(String jsonData, String key, Integer defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject e = new JSONObject(jsonData);
                return getInt(e, key, defaultValue);
            } catch (JSONException var4) {
                if (isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        }
    }

    public static int getInt(JSONObject jsonObject, String key, int defaultValue) {
        return getInt(jsonObject, key, Integer.valueOf(defaultValue));
    }

    public static int getInt(String jsonData, String key, int defaultValue) {
        return getInt(jsonData, key, Integer.valueOf(defaultValue));
    }

    public static Double getDouble(JSONObject jsonObject, String key, Double defaultValue) {
        if (jsonObject != null && !StringUtils.isEmpty(key)) {
            try {
                return jsonObject.getDouble(key);
            } catch (JSONException var4) {
                if (isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static Double getDouble(String jsonData, String key, Double defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject e = new JSONObject(jsonData);
                return getDouble(e, key, defaultValue);
            } catch (JSONException var4) {
                if (isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        }
    }

    public static double getDouble(JSONObject jsonObject, String key, double defaultValue) {
        return getDouble(jsonObject, key, Double.valueOf(defaultValue));
    }

    public static double getDouble(String jsonData, String key, double defaultValue) {
        return getDouble(jsonData, key, Double.valueOf(defaultValue));
    }

    public static String getString(JSONObject jsonObject, String key, String defaultValue) {
        if (jsonObject != null && !StringUtils.isEmpty(key)) {
            try {
                return jsonObject.getString(key);
            } catch (JSONException var4) {
                if (isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static String getString(String jsonData, String key, String defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject e = new JSONObject(jsonData);
                return getString(e, key, defaultValue);
            } catch (JSONException var4) {
                if (isPrintException) {
                    var4.printStackTrace();
                }
                return defaultValue;
            }
        }
    }

    public static String[] getStringArray(JSONObject jsonObject, String key, String[]
            defaultValue) {
        if (jsonObject != null && !StringUtils.isEmpty(key)) {
            try {
                JSONArray e = jsonObject.getJSONArray(key);
                if (e == null) {
                    return defaultValue;
                } else {
                    String[] value = new String[e.length()];

                    for (int i = 0; i < e.length(); ++i) {
                        value[i] = e.getString(i);
                    }

                    return value;
                }
            } catch (JSONException var6) {
                if (isPrintException) {
                    var6.printStackTrace();
                }

                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static String[] getStringArray(String jsonData, String key, String[] defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject e = new JSONObject(jsonData);
                return getStringArray(e, key, defaultValue);
            } catch (JSONException var4) {
                if (isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        }
    }

    public static JSONObject getJSONObject(JSONObject jsonObject, String key, JSONObject
            defaultValue) {
        if (jsonObject != null && !StringUtils.isEmpty(key)) {
            try {
                return jsonObject.getJSONObject(key);
            } catch (JSONException var4) {
                if (isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static JSONObject getJSONObject(String jsonData, String key, JSONObject defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject e = new JSONObject(jsonData);
                return getJSONObject(e, key, defaultValue);
            } catch (JSONException var4) {
                if (isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        }
    }

    public static JSONArray getJSONArray(JSONObject jsonObject, String key, JSONArray
            defaultValue) {
        if (jsonObject != null && !StringUtils.isEmpty(key)) {
            try {
                return jsonObject.getJSONArray(key);
            } catch (JSONException var4) {
                if (isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static JSONArray getJSONArray(String jsonData, String key, JSONArray defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject e = new JSONObject(jsonData);
                return getJSONArray(e, key, defaultValue);
            } catch (JSONException var4) {
                if (isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        }
    }

    public static boolean getBoolean(JSONObject jsonObject, String key, Boolean defaultValue) {
        if (jsonObject != null && !StringUtils.isEmpty(key)) {
            try {
                return jsonObject.getBoolean(key);
            } catch (JSONException var4) {
                if (isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static boolean getBoolean(String jsonData, String key, Boolean defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject e = new JSONObject(jsonData);
                return getBoolean(e, key, defaultValue);
            } catch (JSONException var4) {
                if (isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        }
    }

    public static Map<String, String> getMap(JSONObject jsonObject, String key) {
        return parseKeyAndValueToMap(getString(jsonObject, key, null));
    }

    public static Map<String, String> getMap(String jsonData, String key) {
        if (jsonData == null) {
            return null;
        } else if (jsonData.length() == 0) {
            return new HashMap<>();
        } else {
            try {
                JSONObject e = new JSONObject(jsonData);
                return getMap(e, key);
            } catch (JSONException var3) {
                if (isPrintException) {
                    var3.printStackTrace();
                }

                return null;
            }
        }
    }

    public static Map<String, String> parseKeyAndValueToMap(JSONObject sourceObj) {
        if (sourceObj == null) {
            return null;
        } else {
            HashMap<String, String> keyAndValueMap = new HashMap<>();
            Iterator iter = sourceObj.keys();

            while (iter.hasNext()) {
                String key = (String) iter.next();
                putMapNotEmptyKey(keyAndValueMap, key, getString(sourceObj, key, ""));
            }

            return keyAndValueMap;
        }
    }

    private static boolean putMapNotEmptyKey(Map<String, String> map, String key, String value) {
        if (map != null && !StringUtils.isEmpty(key)) {
            map.put(key, value);
            return true;
        } else {
            return false;
        }
    }

    public static Map<String, String> parseKeyAndValueToMap(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        } else {
            try {
                JSONObject e = new JSONObject(source);
                return parseKeyAndValueToMap(e);
            } catch (JSONException var2) {
                if (isPrintException) {
                    var2.printStackTrace();
                }

                return null;
            }
        }
    }

    public static final String EMPTY_JSON = "{}";
    public static final String EMPTY_JSON_ARRAY = "[]";
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
    public static final double SINCE_VERSION_10 = 1.0D;
    public static final double SINCE_VERSION_11 = 1.1D;
    public static final double SINCE_VERSION_12 = 1.2D;
    public static final double UNTIL_VERSION_10 = 1.0D;
    public static final double UNTIL_VERSION_11 = 1.1D;
    public static final double UNTIL_VERSION_12 = 1.2D;


    public static String toJson(Object target, Type targetType, boolean isSerializeNulls, Double
            version, String datePattern, boolean excludesFieldsWithoutExpose) {
        if (target == null) {
            return "{}";
        } else {
            GsonBuilder builder = new GsonBuilder();
            if (isSerializeNulls) {
                builder.serializeNulls();
            }

            if (version != null) {
                builder.setVersion(version.doubleValue());
            }

            if (isBlank(datePattern)) {
                datePattern = "yyyy-MM-dd HH:mm:ss SSS";
            }

            builder.setDateFormat(datePattern);
            if (excludesFieldsWithoutExpose) {
                builder.excludeFieldsWithoutExposeAnnotation();
            }

            return toJson(target, targetType, builder);
        }
    }

    public static String toJson(Object target) {
        return toJson(target, (Type) null, false, (Double) null, (String) null, true);
    }

    public static String toJson(Object target, String datePattern) {
        return toJson(target, (Type) null, false, (Double) null, datePattern, true);
    }

    public static String toJson(Object target, Double version) {
        return toJson(target, (Type) null, false, version, (String) null, true);
    }

    public static String toJson(Object target, boolean excludesFieldsWithoutExpose) {
        return toJson(target, (Type) null, false, (Double) null, (String) null,
                excludesFieldsWithoutExpose);
    }

    public static String toJson(Object target, Double version, boolean
            excludesFieldsWithoutExpose) {
        return toJson(target, (Type) null, false, version, (String) null,
                excludesFieldsWithoutExpose);
    }

    public static String toJson(Object target, Type targetType) {
        return toJson(target, targetType, false, (Double) null, (String) null, true);
    }

    public static String toJson(Object target, Type targetType, Double version) {
        return toJson(target, targetType, false, version, (String) null, true);
    }

    public static String toJson(Object target, Type targetType, boolean
            excludesFieldsWithoutExpose) {
        return toJson(target, targetType, false, (Double) null, (String) null,
                excludesFieldsWithoutExpose);
    }

    public static String toJson(Object target, Type targetType, Double version, boolean
            excludesFieldsWithoutExpose) {
        return toJson(target, targetType, false, version, (String) null,
                excludesFieldsWithoutExpose);
    }

    public static <T> T fromJson(String json, TypeToken<T> token, String datePattern) {
        if (isBlank(json)) {
            return null;
        } else {
            GsonBuilder builder = new GsonBuilder();
            if (isBlank(datePattern)) {
                datePattern = "yyyy-MM-dd HH:mm:ss SSS";
            }

            Gson gson = builder.create();

            try {
                return gson.fromJson(json, token.getType());
            } catch (Exception var6) {
                var6.printStackTrace();
                Log.e(json + " 无法转换为 " + token.getRawType().getName() + " 对象!", var6.getMessage());
                return null;
            }
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz, String datePattern) {
        if (isBlank(json)) {
            return null;
        } else {
            GsonBuilder builder = new GsonBuilder();
            if (isBlank(datePattern)) {
                datePattern = "yyyy-MM-dd HH:mm:ss SSS";
            }

            Gson gson = builder.create();

            try {
                return gson.fromJson(json, clazz);
            } catch (Exception var6) {
                Log.e(json + " 无法转换为 " + clazz.getName() + " 对象!", var6.getMessage());
                return null;
            }
        }
    }



    public static String toJson(Object target, Type targetType, GsonBuilder builder) {
        if (target == null) {
            return "{}";
        } else {
            Gson gson = null;
            if (builder == null) {
                gson = new Gson();
            } else {
                gson = builder.create();
            }

            String result = "{}";

            try {
                if (targetType == null) {
                    result = gson.toJson(target);
                } else {
                    result = gson.toJson(target, targetType);
                }
            } catch (Exception var6) {
                if (target instanceof Collection || target instanceof Iterator || target
                        instanceof Enumeration || target.getClass().isArray()) {
                    result = "[]";
                }
            }

            return result;
        }
    }

    private static boolean isBlank(String text) {
        return text == null || "".equals(text.trim());
    }
}
