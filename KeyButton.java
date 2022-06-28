package piano;
import javax.swing.*;
import java.awt.*;
@SuppressWarnings("serial")
// 設定琴鍵編號
public class KeyButton extends JButton {
    private static final int nullKey = -1;
    public static int WHITEKEY=1,BLACKKEY=2,whiteKeyWidth=26,whiteKeyHeight=200,blackKeyWidth=22,blackKeyHeight=120;
    int key;
    public KeyButton() {
        this.key = nullKey;
    }
    public KeyButton(int type){
        if(type==KeyButton.WHITEKEY){
            this.setSize(KeyButton.whiteKeyWidth,KeyButton.whiteKeyHeight);
        }
        else if(type==KeyButton.BLACKKEY){
            this.setSize(KeyButton.whiteKeyWidth,KeyButton.whiteKeyHeight);
        }
    }
    public int getKey() {
        return key;
    }
    public void setKey(int key) {
        this.key = key;
    }
}