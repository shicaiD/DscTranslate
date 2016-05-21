

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by shicaiD on 2016/5/20.
 * 请求翻译封装的Api
 */
public class NetApi {
    public static final String TAG = "NetApi";
    private static final String URL = "http://fanyi.youdao.com/openapi.do";
    private static final String KEYFROM = "DscTranslate";
    private static final String KEY = "478546458";
    private static final String TYPE = "data";
    private static final String DOCTYPE = "json";
    private static final String VERSION = "1.1";

    private NetApi(){}

    /**
     * 封装url
     * @param toTranslate
     * @return
     */
    private static String getRequstUrl(String toTranslate){
        return URL+"?keyfrom="+KEYFROM+"&key="+KEY+"&type="+TYPE+"&doctype="+DOCTYPE+"&version="+VERSION+"&q="+toTranslate;
    }

    public static String getJson(String toTranslate){
        try {
            URL url = new URL(getRequstUrl(toTranslate));
            System.out.println("TAG: url= "+url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //config
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "text/html");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            if (conn.getResponseCode() == 200){//成功
                //获取返回数据
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                String line = "";
                StringBuilder sb = new StringBuilder();
                while ((line=reader.readLine())!=null){
                    sb.append(line);
                }
                String result = sb.toString();
                return result;
            }else {
                System.out.println("网络错误，请重试");
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
