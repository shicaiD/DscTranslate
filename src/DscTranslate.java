import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.Messages;

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
            String selectedText = model.getSelectedText();
            if (selectedText!=null){
                getTranslation(selectedText);
            }
        }
    }

    private void getTranslation(String selectedText) {
            Messages.showMessageDialog(
                    new ToTranslate(NetApi.getJson(selectedText)).toString(),
                    "翻译结果",
                    Messages.getInformationIcon()
            );
    }
}
