import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.util.messages.impl.Message;
import org.apache.http.client.config.RequestConfig;
import org.json.JSONException;

/**
 * Created by shicaiD on 2016/5/20.
 */
public class DscTranslate extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        //获取编辑器
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (editor != null){
            SelectionModel model = editor.getSelectionModel();
            //获取选中文本
            String selectedText = model.getSelectedText().toString();
            if (selectedText!=null){
                selectedText = addBlanks(selectedText);
                try {
                    getTranslation(selectedText);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void getTranslation(String selectedText) throws JSONException {
            Messages.showMessageDialog(
                    new ToTranslate(NetApi.getJson(selectedText)).toString(),
                    "翻译结果",
                    Messages.getInformationIcon()
            );
    }
    private String addBlanks(String str) {
        String temp = str.replaceAll("_"," ");
        if (temp.equals(temp.toUpperCase())) {
            System.out.println("return");
            return temp;
        }
        String result = temp.replaceAll("([A-Z])", " $0");
        return result;
    }
}
