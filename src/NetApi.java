import com.google.gson.JsonObject;
import com.sun.javafx.fxml.builder.URLBuilder;
import net.sf.json.JSONObject;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ConnectTimeoutException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

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
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            if (conn.getResponseCode() == 200){//成功
                //获取返回数据
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                StringBuffer sb = new StringBuffer();
                while ((line=reader.readLine())!=null){
                    sb.append(line);
                }
                return sb.toString();
            }else {
                throw new RuntimeException("网络错误，请重试");

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
