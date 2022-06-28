package piano;

import piano.KeyButton;

public class Light {
    private final int LIGHT_ORIGINAL_Y_POS = 580;// 琴光原始y座標
    private final int LIGHT_ORIGINAL_WHITE_KEY_WIDTH = KeyButton.whiteKeyWidth;// 何 白鍵琴光原始寬度
    private final int LIGHT_ORIGINAL_BLACK_KEY_WIDTH = KeyButton.blackKeyWidth;// 何 黑鍵琴光原始寬度
    private final int LIGHT_ORIGINAL_HEIGHT = 2;// 琴光原始長度

    protected int x; // x座標
    protected int y; // y座標
    protected int width; // 寬
    protected int height; // 高
    private int speed = 2; // 移動的速度 可以用來調節拍

    // 建構子
    public Light(int x, int key) {
        this.x = x;
        this.y = LIGHT_ORIGINAL_Y_POS;
        if (key == Key.WHITE_KEY) {// 何 判斷黑鍵白鍵
            this.width = LIGHT_ORIGINAL_WHITE_KEY_WIDTH;
        } else if (key == Key.BLACK_KEY) {
            this.width = LIGHT_ORIGINAL_BLACK_KEY_WIDTH;
        }
        this.height = LIGHT_ORIGINAL_HEIGHT;
    }

    // 走一步
    public void step() {
        y -= speed;
    }

    // 越界處理
    public boolean outOfBounds() {
        return y < 0;
    }

    // 以下是各變量的getter與setter
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
