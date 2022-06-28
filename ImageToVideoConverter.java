package piano;
import ch.randelshofer.media.avi.AVIOutputStream;
import piano.RecordButton;

import java.io.*;
import java.awt.image.BufferedImage ;
import javax.imageio.ImageIO;
public class ImageToVideoConverter
{
    private static File[] imageFile;
    public static void convert(int videoAmount,int imageWidth,int imageHeight,String videoName)
    {
        imageFile=new File[videoAmount];
        for(int i=0; i<videoAmount; i++){
            imageFile[i]=new File("image/shot/screenShot"+i+".jpg");
            
        }

        if( imageFile.length < 2 )
        {
            System.out.println( "錄影時間太短，" );
        }
        AVIOutputStream out = null;
        try{
            out = new AVIOutputStream( new File(videoName) , AVIOutputStream.VideoFormat.JPG );
        }
        catch(IOException i){
            System.out.println(i);
        }
       
        out.setVideoDimension​(imageWidth,imageHeight);
        //影片壓縮，壓縮越多，影片大小越小，但是畫面也越糊，參數範圍從0~1。 0:壓縮最多 1:畫面最清晰
        out.setVideoCompressionQuality( 1f );

        //幾秒tick一次
        out.setTimeScale(1);

        //一tick幾張圖
        out.setFrameRate(10);

        try{
            //轉換成影片並輸出影片檔
            for( int i=0; i <imageFile.length ; ++i )
            {
                out.writeFrame(ImageIO.read(imageFile[i]));
            }

            //關閉影片檔
            out.close();
        }
        catch(IOException i){
            System.out.println(i);
        }

        //刪除為了錄製影片產生的截圖
        for(int i=0; i<imageFile.length; i++){
            imageFile[i].delete();
        }

        //刪除為了錄製影片產生的截圖儲存資料夾
        File screenShotFolder=new File(RecordButton.screenShotFolderPath);
        screenShotFolder.delete();
        RecordButton.imageAmount=-1;      
    }
}