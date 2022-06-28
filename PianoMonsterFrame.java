package piano;

import java.io.File;
import java.io.IOException;

import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Desktop;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Icon;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;

import java.lang.Thread;
import java.lang.ArrayIndexOutOfBoundsException;
import java.lang.Object;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.net.URI;
import java.net.URISyntaxException;
@SuppressWarnings("serial")
public class PianoMonsterFrame extends JFrame {
    // array
    private static ArrayList<String> audioList = new ArrayList<String>();
    private static ArrayList<KeyButton> keyButtonList = new ArrayList<KeyButton>();
    private static ArrayList<controlState> controlStateList = new ArrayList<controlState>();
    // 琴光陣列
    private ArrayList<Light> lights= new ArrayList<Light>();

    // frame
    public static int width;
    public static int height;

    // 物件
    private static JButton teachModeOnButton;
    private static JComboBox<String> songSelectList;
    private static JButton startTeachingButton;// 開啟教學按鈕
    private static JButton stopTeachingButton;// 關閉教學按鈕
    private static JLabel teachInstructionLabel;// 教學時教導使用者如何彈奏的標籤
    private JButton reflectionSwitchButton;//開啟/關閉鍵盤投射按鈕
    private JLabel whiteKeyboardReflectionLabel;
    private JLabel blackKeyboardReflectionLabel;
    
    private static JButton shareFacebookButton;// 選擇標籤
    private static JButton shareTwitterButton;// 選擇標籤
    private RecordButton recordButton;

    // 鍵盤參數
    private static final int whiteKeyTotal = 43;// 白鍵數量
    private static final int blackKeyTotal = 30;
    // 事件處理
    private int timePeriod = 5; // 時間間隔(毫秒) 何 琴光速度太快 1改成7
    private int delay = 1; // 延遲(毫秒)
    private static final int RELEASING = 0;
    private static final int PRESSING = 1;
    private static int[] stateKey = new int[whiteKeyTotal + blackKeyTotal];
    private static int[] teachStateKey = new int[whiteKeyTotal + blackKeyTotal];// 教學琴鍵狀態
    private boolean reflection=false;

    public PianoMonsterFrame() {

        /* -------------設定JFrame------------- */
        PianoMonsterFrame.width = 1920;
        PianoMonsterFrame.height = 1080;
        this.setSize(PianoMonsterFrame.width / 4 * 3, PianoMonsterFrame.height / 4 * 3);

        // set location
        //this.setLocation(width / 8, height / 8);
        this.setLocation(10, 10);
        // set Layout
        setLayout(null);

        // set Icon
        Image img = new ImageIcon("image/applicationIcon.png").getImage();
        this.setIconImage(img);
        this.setTitle("PianoMonster");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true); // display frame
        this.setFocusable(true);

        /* -------------處理主程序------------- */
        // button
        // state = RELEASING;
        // this.action();

        /* -------------建立音效位置------------- */
        CreateAudioList audio = new CreateAudioList();
        audioList = audio.getList();

        /* -------------建立鍵盤------------- */
        // 鍵盤面板
        JPanel whitePanel;
        JPanel blackPanel;

        // 事件
        MouseAdapter mouseAdapter = new MyMouseAdapter();
        addKeyListener(new KeyboardListener());

        // white鍵
        whitePanel = new JPanel();
        whitePanel.setOpaque(false);
        whitePanel.setBounds(0, 0, 2000, 800);
        whitePanel.setBackground(null);
        whitePanel.setLayout(null);
        for (int i = 0; i < whiteKeyTotal; i++) { // 生成白鍵
            KeyButton Btn = new KeyButton(KeyButton.WHITEKEY);
            // 琴鍵編號
            Btn.setKey(i); // 設定編號
            Btn.addMouseListener(mouseAdapter); // 加入滑鼠事件
            controlStateList.add(new controlState(i)); // 加入事件狀態
            // 按钮大小位置
            Btn.setBackground(Color.white);
            Btn.setBounds(26 * i + 4, 550, 26, 200);
            whitePanel.add((JButton) Btn);
            keyButtonList.add(Btn);
        }
        // black鍵
        blackPanel = new JPanel();
        blackPanel.setOpaque(false);
        blackPanel.setBounds(0, 0, 2000, 800);
        blackPanel.setBackground(null);
        blackPanel.setLayout(null);
        int jump = 0, j = 0; // 黑鍵的位移
        for (int i = 0; i < blackKeyTotal; i++) { // 生成黑鍵
            KeyButton Btn = new KeyButton(KeyButton.BLACKKEY);
            // 黑鍵跳躍的位移 2 6 9 13
            if (i == 2)
                jump++;
            if (jump > 0) {
                if (j == 3 && jump % 2 == 1) {
                    jump++;
                    j = 0;
                }
                if (j == 2 && jump % 2 == 0) {
                    jump++;
                    j = 0;
                }
                j++;
            }
            // 琴鍵編號
            Btn.setKey(i + whiteKeyTotal); // 黑鍵的編號為黑鍵i加白鍵數量
            Btn.addMouseListener(mouseAdapter); // 加入滑鼠事件
            controlStateList.add(new controlState(i + whiteKeyTotal)); // 加入事件狀態
            // 琴鍵位置與大小
            Btn.setBackground(Color.black);
            Btn.setBounds(26 * (i + jump) + 19, 550, 22, 120);
            blackPanel.add((JButton) Btn);
            keyButtonList.add(Btn);
        }
        add(whitePanel);
        add(blackPanel, 0);
        this.getLayeredPane().add(blackPanel, Integer.valueOf(Integer.MAX_VALUE));
        // 教學按鈕
        teachModeOnButton = new JButton("開始教學模式");
        startTeachingButton = new JButton("開始教學");
        stopTeachingButton = new JButton("停止教學");
        Icon teachIcon = new ImageIcon("image/followLabel.PNG");
        teachInstructionLabel = new JLabel(teachIcon);

        //reflection
        reflectionSwitchButton=new JButton("開啟鍵盤投射");
        reflectionSwitchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (reflection){
                    reflection=false;
                    whiteKeyboardReflectionLabel.setVisible(reflection);
                    blackKeyboardReflectionLabel.setVisible(reflection);
                    
                    PianoMonsterFrame.this.reflectionSwitchButton.setText("開啟鍵盤投射");
                }
                else{
                    reflection=true;
                    whiteKeyboardReflectionLabel.setVisible(reflection);
                    blackKeyboardReflectionLabel.setVisible(reflection);
                    PianoMonsterFrame.this.reflectionSwitchButton.setText("關閉鍵盤投射");
                }
                requestFocus();
            }
        });
        //reflection
        Icon whiteIcon = new ImageIcon("image/white.PNG");
        whiteKeyboardReflectionLabel=new JLabel(whiteIcon);
        whiteKeyboardReflectionLabel.setVisible(false);
        Icon blackIcon = new ImageIcon("image/black.PNG");
        blackKeyboardReflectionLabel=new JLabel(blackIcon);
        blackKeyboardReflectionLabel.setVisible(false);


        //教學歌曲清單和下拉式選單
        String[] songs={"小星星","小蜜蜂","兩隻老虎","月亮代表我的心"};
        songSelectList=new JComboBox<String>(songs);
        
        
        //分享到臉書的按鈕
        Icon facebookIcon = new ImageIcon("image/facebook.PNG");
        shareFacebookButton = new JButton(facebookIcon);
        //分享到推特的按鈕
        Icon twitterIcon = new ImageIcon("image/twitter.PNG");
        shareTwitterButton = new JButton(twitterIcon);

        //錄影按鈕
        recordButton=new RecordButton();
        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if(RecordButton.capturing==false){
                    RecordButton.capturing=true;
                    recordButton.setIcon(RecordButton.stopRecordIcon);
                    RecordButton.record(getTopLeftPoint());
                }
                else{
                    RecordButton.capturing=false;
                    recordButton.setIcon(RecordButton.startRecordIcon);
                }
                requestFocus();
            }
        });
        
        keyButtonListener teachListener = new keyButtonListener();
        teachModeOnButton.addMouseListener(teachListener);
        startTeachingButton.addMouseListener(teachListener);
        stopTeachingButton.addMouseListener(teachListener);
        shareFacebookButton.addMouseListener(teachListener);
        shareTwitterButton.addMouseListener(teachListener);

        //設定button位置&大小
        reflectionSwitchButton.setBounds(1270, 450, 150, 50);//reflection
        whiteKeyboardReflectionLabel.setBounds(360, 630, 700, 700);//reflection
        blackKeyboardReflectionLabel.setBounds(370, 430, 700, 700);//reflection
        teachModeOnButton.setBounds(1200, 20, 150,50);
        songSelectList.setBounds(1175, 110,100,30);
        startTeachingButton.setBounds(1300, 100, 100, 50);
        stopTeachingButton.setBounds(1200, 250, 150, 50);
        teachInstructionLabel.setBounds(300, 100, 500, 200);//300,100
        shareFacebookButton.setBounds(1300, 600, 100, 100);
        shareTwitterButton.setBounds(1150, 600, 100, 100);
        recordButton.setBounds(1200, 450, 50, 50);

        //隱藏按鈕(這些按鈕在教學模式中才會出現)
        songSelectList.setVisible(false);
        startTeachingButton.setVisible(false);
        stopTeachingButton.setVisible(false);
        
        

        add(teachModeOnButton,0);
        this.getLayeredPane().add(teachModeOnButton, Integer.valueOf(Integer.MAX_VALUE));
        add(startTeachingButton, 0);
        this.getLayeredPane().add(startTeachingButton, Integer.valueOf(Integer.MAX_VALUE));
        add(stopTeachingButton, 0);
        this.getLayeredPane().add(stopTeachingButton, Integer.valueOf(Integer.MAX_VALUE));
        add(teachInstructionLabel, 0);
        this.getLayeredPane().add(teachInstructionLabel, Integer.valueOf(Integer.MAX_VALUE));
        teachInstructionLabel.setVisible(false);
        add(shareFacebookButton, 0);
        this.getLayeredPane().add(shareFacebookButton, Integer.valueOf(Integer.MAX_VALUE));
        add(shareTwitterButton, 0);
        this.getLayeredPane().add(shareTwitterButton, Integer.valueOf(Integer.MAX_VALUE));
        add(recordButton,0);
        this.getLayeredPane().add(recordButton, Integer.valueOf(Integer.MAX_VALUE));
        add(songSelectList,0);
        this.getLayeredPane().add(songSelectList, Integer.valueOf(Integer.MAX_VALUE));
        add(reflectionSwitchButton,0);//reflection
        this.getLayeredPane().add(reflectionSwitchButton, Integer.valueOf(Integer.MAX_VALUE));//reflection
        add(whiteKeyboardReflectionLabel,0);//reflection
        this.getLayeredPane().add(whiteKeyboardReflectionLabel, Integer.valueOf(Integer.MAX_VALUE));//reflection
        add(blackKeyboardReflectionLabel,0);//reflection
        this.getLayeredPane().add(blackKeyboardReflectionLabel, Integer.valueOf(Integer.MAX_VALUE));//reflection

        for (int i = 0; i < whiteKeyTotal + blackKeyTotal; i++) {
            stateKey[i] = RELEASING;
            teachStateKey[i] = RELEASING;
        }
        action();

        new Thread() {// 何 按多個鍵才能沒有延遲發出聲音
            @Override
            public void run() {
                System.out.println("聲音延遲更正");
                stateKey[0] = PRESSING;
                stateKey[whiteKeyTotal - 1] = PRESSING;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
                stateKey[0] = RELEASING;
                stateKey[whiteKeyTotal - 1] = RELEASING;
            }
        }.start();
    }

    // 琴鍵聲音
    public static class AudioPlay {
        private static Clip clip;
        private static File file;
        private static AudioInputStream audioIn;

        public AudioPlay(String path) {
            try {
                file = new File(path);
                audioIn = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(audioIn);
            } catch (UnsupportedAudioFileException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (LineUnavailableException ex) {
                ex.printStackTrace();
            }
        }

        public void play() {
            clip.start();
        }

        public void stop() {
            //this.clip.close();
            clip.close();
        }
    }

    // 獨立處理不同事件的狀態
    private class controlState {
        private int timePeriod = 1; // 時間間隔(毫秒)
        private int delay = 0; // 延遲(毫秒)
        private KeyButton touchButton;// 當下觸發的按鍵
        private AudioPlay voice;// 當下觸發的聲音
        private Color touchButtonColor = Color.pink;// 觸發後的顏色
        private boolean finish = false;

        public controlState(int key) {
            Timer timer = new Timer(); // 單事件流程控制
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (stateKey[key] == PRESSING && finish == false) { // 不同鍵才觸發
                        finish = true;
                        voice = new AudioPlay(audioList.get(key).toString());
                        voice.play();
                        touchButton = keyButtonList.get(key);
                        touchButton.setBackground(touchButtonColor); // 觸發後的顏色
                    } else if (stateKey[key] == RELEASING && finish == true) {
                        finish = false;
                        voice.stop();
                        if (key >= whiteKeyTotal) // 黑鍵改為黑色
                            touchButton.setBackground(Color.black);
                        else
                            touchButton.setBackground(Color.white); // 白鍵改為白色
                    }
                }
            }, delay, timePeriod);// 從現在起delay毫秒後 每過timePeriod毫秒執行這個任務(Task) timer.schedule(task, delay, period)
        }
    }

    // 滑鼠事件
    private class MyMouseAdapter extends MouseAdapter {
        private KeyButton touchButton;// 當下觸發的按鍵
        // 按壓

        public void mousePressed(MouseEvent e) {
            //System.out.println("*滑鼠開始壓*");
            touchButton = (KeyButton) e.getSource();
            stateKey[touchButton.getKey()] = PRESSING;// 按鍵狀態預設PRESSING
            requestFocus(); // 重新設置面板焦點
        }

        // 釋放
        public void mouseReleased(MouseEvent e) {
            //println("*滑鼠放掉*");
            touchButton = (KeyButton) e.getSource();
            stateKey[touchButton.getKey()] = RELEASING;// 按鍵狀態預設RELEASING
            // 何 教學狀態加完美等待時間
            teachStateKey[touchButton.getKey()] = PRESSING;
            try {
                Thread.sleep(25);
            } catch (InterruptedException ex) {
                ////("Thread interrupted.");
            }
            teachStateKey[touchButton.getKey()] = RELEASING;
        }
    }

    //按鈕點擊
    private class keyButtonListener extends MouseAdapter {
        private final int ON = 1;
        private final int OFF = 0;
        private int teachFlag;
        private int currentSheetMusicNumber;
        private Thread thread;

        public void mousePressed(MouseEvent e) {// 何 未解決問題 少有的琴光閃爍
            Object clickedObject=e.getSource();
            if(clickedObject == teachModeOnButton){
                teachModeOnButton.setVisible(false);
                songSelectList.setVisible(true);
                startTeachingButton.setVisible(true);
            }
            else if (clickedObject == startTeachingButton) {
                int song=songSelectList.getSelectedIndex();
                 
                if (teachFlag == ON) {// 檢查教學模式是否在進行
                } else if (teachFlag == OFF) {
                    teachInstructionLabel.setVisible(true);
                    startTeachingButton.setVisible(false);
                    stopTeachingButton.setVisible(true);
                    teachFlag = ON;
                    SheetMusicHandler sheetMusicHandler = new SheetMusicHandler();
                    int[] sheetMusic = sheetMusicHandler.chooseSheetMusic(song);
                    currentSheetMusicNumber = 0;
                    System.out.println("教學模式開啟");
                    // 何 照簡譜打 所以sheetMusic[currentSheetMusicNumber]都-1
                    KeyButton currentKeyButton = keyButtonList.get(sheetMusic[currentSheetMusicNumber] - 1);
                    currentKeyButton.setBackground(Color.YELLOW);
                    thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                while (teachFlag == ON && currentSheetMusicNumber < sheetMusic.length) {
                                    // //教學中");// 沒有這行 不行
                                    try {
                                        Thread.sleep(5);
                                    } catch (InterruptedException ex) {
                                        System.out.println("Thread interrupted.");
                                    }
                                    if (teachStateKey[sheetMusic[currentSheetMusicNumber] - 1] == Key.PRESSING) {
                                        KeyButton currentKeyButton = keyButtonList
                                                .get(sheetMusic[currentSheetMusicNumber] - 1);
                                        currentKeyButton.setBackground(Color.WHITE);
                                        currentSheetMusicNumber++;
                                        try {
                                            sleep(25);
                                        } catch (InterruptedException e) {
                                            System.out.println("Thread interrupted.");
                                        }
                                        currentKeyButton = keyButtonList
                                                .get(sheetMusic[currentSheetMusicNumber] - 1);
                                        currentKeyButton.setBackground(Color.YELLOW);
                                        // try {
                                        // sleep(25);
                                        // } catch (InterruptedException e) {
                                        // System.out.println("Thread interrupted.");
                                        // }
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                                //歌曲教學完成
                                System.out.println("教學模式關閉");
                                for (int i = 0; i < 36; i++) {
                                    KeyButton Btn = keyButtonList.get(i);
                                    Btn.setBackground(Color.WHITE);
                                }
                                teachInstructionLabel.setVisible(false);
                                songSelectList.setVisible(false);
                                stopTeachingButton.setVisible(false);
                                teachModeOnButton.setVisible(true);
                                teachFlag = OFF;
                            }
                        }
                    };
                    thread.start();
                }
            }

            else if (clickedObject == stopTeachingButton) {
                if(teachFlag==ON){
                    thread.interrupt();
                    teachFlag = OFF;
                    System.out.println("教學模式關閉");
                    for (int i = 0; i < 36; i++) {
                        KeyButton Btn = keyButtonList.get(i);
                        Btn.setBackground(Color.WHITE);
                    }
                    teachInstructionLabel.setVisible(false);
                    
                    teachModeOnButton.setVisible(true);
                    songSelectList.setVisible(false);
                    stopTeachingButton.setVisible(false);
                }
                
            }
            else if (clickedObject == shareFacebookButton) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    URI uri = new URI(
                            "https://www.facebook.com/sharer/sharer.php?u=https%3A%2F%2Fwww.facebook.com%2FPIANOMONSTERJAVA&amp%3Bsrc=sdkpreparse");
                    desktop.browse(uri);
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            else if (clickedObject == shareTwitterButton) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    URI uri = new URI(
                            "http://twitter.com/intent/tweet?url=https://twitter.com/PianoMonster_");
                    desktop.browse(uri);
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            requestFocus(); // 何 重新設置面板焦點 ActionListener改成MouseAdapter
        }
    }

    // 鍵盤事件
    class KeyboardListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            //System.out.println("*鍵盤開始壓*");
            int keyCode = e.getKeyCode();
            // 另外處理左右鍵的轉調功能
            int sevenDistance=182;
            if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
                if (keyCode == KeyEvent.VK_LEFT) {
                    if (Key.getKeyBasic() > 0){
                        Key.setKeyBasic(Key.getKeyBasic() - 1);
                        int whiteNewX=whiteKeyboardReflectionLabel.getX()-sevenDistance;
                        int whiteY=whiteKeyboardReflectionLabel.getY();
                        whiteKeyboardReflectionLabel.setLocation(whiteNewX, whiteY);
                        int blackNewX=blackKeyboardReflectionLabel.getX()-sevenDistance;
                        int blackY=blackKeyboardReflectionLabel.getY();
                        blackKeyboardReflectionLabel.setLocation(blackNewX, blackY);
                    }
                        
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    if (Key.getKeyBasic() < 4){
                        Key.setKeyBasic(Key.getKeyBasic() + 1);
                        int whiteNewX=whiteKeyboardReflectionLabel.getX()+sevenDistance;
                        int whiteY=whiteKeyboardReflectionLabel.getY();
                        whiteKeyboardReflectionLabel.setLocation(whiteNewX, whiteY);
                        int blackNewX=blackKeyboardReflectionLabel.getX()+sevenDistance;
                        int blackY=blackKeyboardReflectionLabel.getY();
                        blackKeyboardReflectionLabel.setLocation(blackNewX, blackY);
                    }
                        
                }
                System.out.println("*Basic = " + Key.getKeyBasic() + "*");
            }
            // 琴鍵按壓
            else
                try { // 處理按鍵無效的例外
                    stateKey[Key.keyCodeToPositionInArray(keyCode)] = PRESSING;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.out.println("此按鍵無效*");
                }
        }

        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            // 另外處理左右鍵
            if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT)
                return;
            try { // 處理按鍵無效的例外
                stateKey[Key.keyCodeToPositionInArray(keyCode)] = RELEASING;// 按鍵狀態預設RELEASING
                // 何 教學狀態加完美等待時間
                teachStateKey[Key.keyCodeToPositionInArray(keyCode)] = PRESSING;
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ex) {
                    System.out.println("Thread interrupted.");
                }
                teachStateKey[Key.keyCodeToPositionInArray(keyCode)] = RELEASING;
            } catch (ArrayIndexOutOfBoundsException ex) {
                System.out.println("此按鍵無效*");
            }
            //System.out.println("*鍵盤放掉*");
        }
    }

    public static int getWhiteKeyTotal() {
        return whiteKeyTotal;
    }

    public static int getBlackKeyTotal() {
        return blackKeyTotal;
    }

    // 畫
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintLight(g); // 畫光
    }

    // 畫光
    public void paintLight(Graphics g) {
        // System.out.println("畫光");
        for (int i = 0; i < lights.size(); i++) {
            Light currentLight = lights.get(i);
            g.setColor(Color.red);// 設置顏色 設為紅色
            g.fillRect(currentLight.getX(), currentLight.getY(), currentLight.getWidth(), currentLight.getHeight());// 畫填滿長方形並設置位置與大小
        }
    }

    // 琴光向上移動一步
    public void stepAction() {
        for (int i = 0; i < lights.size(); i++) { // 每個琴光走一步
            Light currentLight = lights.get(i);
            currentLight.step();// currentLight.y -= speed;
        }
    }

    // 發射琴光
    public void shootAction() {
        for (int j = 0; j < whiteKeyTotal + blackKeyTotal; j++) {
            if (stateKey[j] == Key.PRESSING) {
                Light newLight;// 新琴光陣列
                // 何 黑鍵白建琴光寬度不一樣
                if (j < whiteKeyTotal) {// 白鍵
                    newLight = new Light(11 + j * KeyButton.whiteKeyWidth, Key.WHITE_KEY);// 初始化
                } else {// 黑鍵
                    int jump = 0, blackKeyNumber = j - whiteKeyTotal, iBlackKey = blackKeyNumber;
                    while (iBlackKey / 5 >= 1) {
                        jump += 2;
                        iBlackKey -= 5;
                    }
                    if (iBlackKey > 1) {
                        jump++;
                    }
                    //黑鍵輸出
                    //System.out.println(blackKeyNumber + jump);

                    newLight = new Light(26 + (blackKeyNumber + jump) * 26, Key.BLACK_KEY);// 初始化
                }
                lights.add(newLight);
            }
        }
    }

    // 越界處理
    public void outOfBoundsAction() {
        int index = 0; // 索引為0
        for (int i = 0; i < lights.size(); i++) {
            Light currentLight = lights.get(i);
            if (currentLight.outOfBounds()) {
                lights.remove(currentLight);
            }
        }
    }

    //回傳JFrame的左上角
    private Point getTopLeftPoint(){
        return this.getLocation();
    }
    
    public void action() {
        System.out.println("琴光thread啟動");
        Timer timer = new Timer(); // 主流程控制
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                shootAction(); // 發射琴光
                stepAction();// 走一步 不知道和重繪誰先
                outOfBoundsAction(); // 刪除越界琴光
                repaint(); // 重繪，呼叫paint()方法
                
                
            }
        }, delay, timePeriod);// 從現在起delay毫秒後 每過timePeriod毫秒執行這個任務(Task) timer.schedule(task, delay, period)
    }
}