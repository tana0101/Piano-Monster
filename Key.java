package piano;
import java.awt.event.KeyEvent;
public class Key {
    private static final int oneBasicKeyWhiteTotal = 14; // 一個basic的數量
    private static final int oneBasicKeyBlackTotal = 10; // 一個basic的數量
    private static int keyBasic = 2; // 預設的鍵位
    private static int whiteTransposition = oneBasicKeyWhiteTotal * keyBasic / 2;
    private static int blackTransposition = oneBasicKeyBlackTotal * keyBasic / 2;

    public static void setKeyBasic(int keyBasic) {
        Key.keyBasic = keyBasic;
        Key.whiteTransposition = oneBasicKeyWhiteTotal * keyBasic / 2;
        Key.blackTransposition = oneBasicKeyBlackTotal * keyBasic / 2;
    }

    public static int getKeyBasic() {
        return keyBasic;
    }

    // key type
    public static final int WHITE_KEY = 0, BLACK_KEY = 1;// 何 WHITEKEY改成WHITE_KEY
    // key status
    public static final int RELEASING = 0, PRESSING = 1;

    /*
     * public static int whiteKeyOrBlackKey(int keyCode){
     * switch (keyCode) {
     * case KeyEvent.VK_Z:
     * case KeyEvent.VK_X:
     * case KeyEvent.VK_C:
     * case KeyEvent.VK_V:
     * case KeyEvent.VK_B:
     * case KeyEvent.VK_N:
     * case KeyEvent.VK_M:
     * case KeyEvent.VK_Q:
     * case KeyEvent.VK_W:
     * case KeyEvent.VK_E:
     * case KeyEvent.VK_R:
     * case KeyEvent.VK_T:
     * case KeyEvent.VK_Y:
     * case KeyEvent.VK_U:
     * return Key.WHITEKEY;
     * //black key todo
     * default:
     * return -1;
     * }
     * }
     */
    public static int keyCodeToPositionInArray(int keyCode) {
        switch (keyCode) {
            /* white */
            // 1th row
            case KeyEvent.VK_Z:
                return 0 + Key.whiteTransposition;
            case KeyEvent.VK_X:
                return 1 + Key.whiteTransposition;
            case KeyEvent.VK_C:
                return 2 + Key.whiteTransposition;
            case KeyEvent.VK_V:
                return 3 + Key.whiteTransposition;
            case KeyEvent.VK_B:
                return 4 + Key.whiteTransposition;
            case KeyEvent.VK_N:
                return 5 + Key.whiteTransposition;
            case KeyEvent.VK_M:
                return 6 + Key.whiteTransposition;
            case KeyEvent.VK_COMMA:
                return 7 + Key.whiteTransposition;
            // 3th row
            case KeyEvent.VK_Q:
                return 7 + Key.whiteTransposition;
            case KeyEvent.VK_W:
                return 8 + Key.whiteTransposition;
            case KeyEvent.VK_E:
                return 9 + Key.whiteTransposition;
            case KeyEvent.VK_R:
                return 10 + Key.whiteTransposition;
            case KeyEvent.VK_T:
                return 11 + Key.whiteTransposition;
            case KeyEvent.VK_Y:
                return 12 + Key.whiteTransposition;
            case KeyEvent.VK_U:
                return 13 + Key.whiteTransposition;
            case KeyEvent.VK_I:
                return 14 + Key.whiteTransposition;

            /* black */
            // 2th row
            case KeyEvent.VK_S:
                return 0 + PianoMonsterFrame.getWhiteKeyTotal() + Key.blackTransposition;
            case KeyEvent.VK_D:
                return 1 + PianoMonsterFrame.getWhiteKeyTotal() + Key.blackTransposition;
            case KeyEvent.VK_G:
                return 2 + PianoMonsterFrame.getWhiteKeyTotal() + Key.blackTransposition;
            case KeyEvent.VK_H:
                return 3 + PianoMonsterFrame.getWhiteKeyTotal() + Key.blackTransposition;
            case KeyEvent.VK_J:
                return 4 + PianoMonsterFrame.getWhiteKeyTotal() + Key.blackTransposition;
            // 3th row
            case KeyEvent.VK_2:
                return 5 + PianoMonsterFrame.getWhiteKeyTotal() + Key.blackTransposition;
            case KeyEvent.VK_3:
                return 6 + PianoMonsterFrame.getWhiteKeyTotal() + Key.blackTransposition;
            case KeyEvent.VK_5:
                return 7 + PianoMonsterFrame.getWhiteKeyTotal() + Key.blackTransposition;
            case KeyEvent.VK_6:
                return 8 + PianoMonsterFrame.getWhiteKeyTotal() + Key.blackTransposition;
            case KeyEvent.VK_7:
                return 9 + PianoMonsterFrame.getWhiteKeyTotal() + Key.blackTransposition;
            /* basic */
            // case KeyEvent.VK_LEFT:
            // case KeyEvent.VK_RIGHT:

            /* no action */
            default:
                return -1; // 無效按鍵
        }

    }
}