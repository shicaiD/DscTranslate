import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by shicaiD on 2016/5/20.
 * 将翻译结果得到的json转为所需的格式
 */
public class ToTranslate {
    public static final String TAG = "ToTranslate";

    //json首层
    private final static String TRANSLATION = "translation";
    private final static String BASIC = "basic";
    private final static String QUERY = "query";
    private final static String ERRORCODE = "errorCode";
    private final static String WEB = "web";

    //basic层
    private final static String US_PHONETIC = "us-phonetic";
    private final static String UK_PHONETIC = "uk-phonetic";
    private final static String EXPLAINS = "explains";

    //web的第二层
    private final static String VALUE = "value";
    private final static String KEY = "key";

    private static final int CODE_SUCCESS = 0; //成功
    private static final int CODE_TOO_LONG = 20; //太长
    private static final int CODE_CANT = 30; //无法进行有效的翻译
    private static final int CODE_UNSUPPORTED_LANGUAGE = 40; //不支持语言
    private static final int CODE_INVALID_KEY = 50; //key失效
    private static final int CODE_NO_RESULT = 60; //无结果

    private String query = "";
    private String translation = "";//"xxx,xxx"的形式返回
    private String us_phonetic = "";//[træns'leʃən]的形式放回
    private String uk_phonetic = "";//[træns'leʃən]的形式放回
    private String explains;   //全部意译
    private List<Map<String,String>> web = new ArrayList<>();   //全部网络意译

    private JSONObject jsonObject = null;
    public ToTranslate(String translateResult) throws JSONException {
        if (translateResult == null){
            System.out.println(TAG+":translateResult=null");
        }else {
            System.out.println(TAG+":translateResult="+translateResult);
            jsonObject = new JSONObject(translateResult);
        }
    }

    /**
     * 查询的单词
     * @return
     */
    private String getQuery() throws JSONException {
        query = (String) jsonObject.get(QUERY);
        return query;
    }

    /**
     * 意译结果
     * @return
     */
    private String getTranslation() throws JSONException {
        translation = jsonObject.getJSONArray(TRANSLATION).toString();
        return translation;
    }

    /**
     * 美式发音
     * @return
     */
    private String getUs_phonetic() throws JSONException {
        if (!jsonObject.has(BASIC)){
            return null;
        }
        JSONObject json = jsonObject.getJSONObject(BASIC);
        if (!json.has(US_PHONETIC)){
            return null;
        }
        us_phonetic = "["+json.get(US_PHONETIC)+"]";
        System.out.println("us_phonetic="+us_phonetic);
        return us_phonetic;
    }

    /**
     * 英式发音
     * @return
     */
    private String getUk_phonetic() throws JSONException {
        if (!jsonObject.has(BASIC)){
            return null;
        }
        JSONObject json = jsonObject.getJSONObject(BASIC);
        if (!json.has(UK_PHONETIC)){
            return null;
        }
        uk_phonetic = "["+json.get(UK_PHONETIC)+"]";
        return uk_phonetic;
    }

    /**
     * 更多意译结果
     * @return
     */
    private String getExplains() throws JSONException {
        if (!jsonObject.has(BASIC)){
            return null;
        }
        JSONObject json = jsonObject.getJSONObject(BASIC);
        if (!json.has(EXPLAINS)){
            return null;
        }
        JSONArray array = json.getJSONArray(EXPLAINS);
        explains = array.toString();
        explains = explains.replaceAll("\",\"","\n");
        explains = explains.replaceAll("\"]","");
        explains = explains.replaceAll("\\[\"","");
        return explains;
    }

    /**
     * 网络意译
     * @return
     */
    private List<Map<String,String>> getWeb() throws JSONException {
        if (!jsonObject.has(WEB)){
            return null;
        }
        JSONArray arrays = jsonObject.getJSONArray(WEB);
        for (int i = 0; i < arrays.length(); i++) {
            JSONObject json = arrays.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put((String) json.get(KEY)+i,json.getJSONArray(VALUE).toString());//+i确保重复的key不会覆盖
            web.add(map);
            json = null;
            map = null;
        }

        return web;
    }

    /**
     * 检查返回码
     * @param errorCode
     * @throws Exception
     *          返回码不为0，则throw Exception，附带详细信息
     */
    private void getResponseCode(int errorCode) throws Exception {
        switch (errorCode){
            case CODE_SUCCESS:
                //nothing to do
                break;
            case CODE_TOO_LONG:
                throw new Exception("要翻译的文本过长");
            case CODE_CANT:
                throw new Exception("无法进行有效的翻译");
            case CODE_UNSUPPORTED_LANGUAGE:
                throw new Exception("不支持的语言类型");
            case CODE_INVALID_KEY:
                throw new Exception("无效的key");
            case CODE_NO_RESULT:
                throw new Exception("无词典结果，仅在获取词典结果生效");
            default:
                break;
        }
    }

    @Override
    public String toString() {
        String result = null;
        if (jsonObject == null){
            result = "未知错误";
        }else {
            int responseCode = 0;//获取返回码
            try {
                responseCode = (int)jsonObject.get(ERRORCODE);
                getResponseCode(responseCode);
                result = getQuery()+" : "+getTranslation()+"\n";
                String temp = null;
                if ((temp = getUs_phonetic()) != null){
                    result += "美式: "+temp+" ；";
                    temp = null;
                }
                if ((temp = getUk_phonetic()) != null){
                    result += "英式: "+temp+"\n";
                    temp = null;
                }
                if ((temp = getExplains())!=null){
                    result += temp+"\n";
                    temp = null;
                }
                Object[] objects = null;
                List<Map<String,String>> list = null;
                if ((list = getWeb()) != null){
                    result += "网络意译：\n";
                    for (Map<String, String> map : list) {
                        Set<String> keys = map.keySet();
                        for (String key : keys) {
                            result += key.substring(0,key.length()-1)+": ";
                            String objects1 = map.get(key);
                            result += objects1+"\n";
                        }
                    }
                }
            } catch (Exception e) {
                result = "错误代码："+responseCode+"\n "+e.toString();
            }
        }
        return result;
    }
}
