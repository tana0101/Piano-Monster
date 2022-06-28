package piano;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.text.DefaultStyledDocument.ElementSpec;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.Point;
import java.awt.Rectangle;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
@SuppressWarnings("serial")
public class RecordButton extends JButton{
    public static boolean capturing=false;
    private static String videofileName="abcSongForYoutube.mp4";
    public static int imageAmount=-1;
    public static String screenShotFolderPath;
    public static Icon startRecordIcon = new ImageIcon("image/startRecord.PNG");
    public static Icon stopRecordIcon = new ImageIcon("image/stopRecord.PNG");
    public RecordButton(){
        this.setIcon(startRecordIcon);
    }
    public static void record(Point topLeftCorner){
        videofileName = "videos//"+JOptionPane.showInputDialog(null, "請輸入影片名稱")+".mp4";
        System.out.println("start capturing");
        Rectangle screenRectangle = new Rectangle();
        int xError=7,heightOfIconTitleMinMaxClose=27,widthOfButtons=315,heightOfBottom=50;
        screenRectangle.setLocation(topLeftCorner.x+xError, topLeftCorner.y+heightOfIconTitleMinMaxClose);
        Dimension imageDimension=new Dimension(PianoMonsterFrame.width/4*3-widthOfButtons, PianoMonsterFrame.height/4*3-heightOfBottom);
        screenRectangle.setSize(imageDimension);
        
        ArrayList<File> files = new ArrayList<File>();

        //建立存放截圖的資料夾
        File screenShotFolder=new File("image//shot");
        screenShotFolder.mkdir();

        //截圖的資料夾的路徑(作為截圖之後的存檔路徑)
        screenShotFolderPath=screenShotFolder.getPath();

        TimerTask task = new TimerTask() {
            public void run() {
                if(capturing){
                    try{
                        Robot robot = new Robot();
                        BufferedImage image = robot.createScreenCapture(screenRectangle);
                        imageAmount++;
                        
                        File newScreenShot = new File(screenShotFolderPath+"//screenShot"+imageAmount+".jpg");
                        files.add(newScreenShot);
                        ImageIO.write(image, "jpg", newScreenShot);
                    }catch(AWTException a){
                        System.out.println(a);
                    }
                    catch(IOException i){
                        System.out.println(i);
                    }
                }
                else{
                    System.out.println("stop capturing");
                    cancel();
                    ImageToVideoConverter.convert(files.size(),imageDimension.width,imageDimension.height, videofileName);
                    JOptionPane.showMessageDialog(null, "影片錄製完成!", "提醒", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
        Timer timer = new Timer("Timer");
        long delay = 0L;
        long period = 100L;
        timer.schedule(task, delay,period);
    }
}
