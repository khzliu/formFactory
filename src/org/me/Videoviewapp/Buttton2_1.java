/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.Videoviewapp;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.me.Videoviewapp.ConvertVideo.outputPath;
import static org.me.Videoviewapp.Videoview.bitrate;
import static org.me.Videoviewapp.Videoview.datasize;
import static org.me.Videoviewapp.Videoview.duration;
import static org.me.Videoviewapp.Videoview.i;
import static org.me.Videoviewapp.Videoview.jButton1;
import static org.me.Videoviewapp.Videoview.jButton2;
import static org.me.Videoviewapp.Videoview.name;
import static org.me.Videoviewapp.Videoview.name1;
import static org.me.Videoviewapp.Videoview.sb;
import static org.me.Videoviewapp.Videoview.size;
import static org.me.Videoviewapp.Videoview.timeall;

/**
 *
 * @author Administrator
 */
class Buttton2_1 implements Runnable{
    String info = "";
    String n = "";
    String s = "";
    double l = 0;
    String dr = "";
    String sz = "";
    String bt = "";
    static String ly_time = "";
    double d = 0.0;
    double d1 = 0.0;
    double d2 = 0.0;
    SimpleDateFormat sdf = null;
    boolean again = false;  //重新转换当前视频标志，为“真”时重新转换
    
    public void run() {
        
            jButton1.setEnabled(false);  //“浏览”按钮变灰
            jButton2.setEnabled(false);  //“开始”按钮变灰
            if(name.size() != 0) {
                OutputStream out = null;
                try {   
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        ly_time = sdf.format(new java.util.Date());  //获取当前时间
                        ly_time = ly_time.replace(':', '时');
                        ly_time = ly_time + "分";
                        for(i=0;i<name.size();i++){
                            ConvertVideo.getPath();
                            ConvertVideo.inputPath = name.get(i);
                            if (! ConvertVideo.checkfile( ConvertVideo.inputPath)) {
                                 System.out.println( ConvertVideo.inputPath + " is not file");
                                 return;
                             }
                            System.out.println(ConvertVideo.inputPath);
                            ConvertVideo.process();
                            while(Videoview.flag) //“暂停”标志为“真”时，停在此处
                                again = true;  
                            if(again == true){
                                i-=1;
                                again = false;
                                continue;
                            }
                            Videoview.jTable1.setRowSelectionInterval(0,i);  //选中表格0到i行
                            Videoview.jTable1.setSelectionBackground(Color.pink);  //改变选中行的颜色
                            n = outputPath+"video.mp4";
                            System.out.println(n);
                            //截取三张图
                            for(int m=0;m<3;m++){
                                ConvertVideo.screenshot(n,m+1, (int) (Math.random()*timeall[i])); 
                            }
                            info = ConvertVideo.analysis(n);
                            File f = new File(n);
                            l = f.length(); 
                            l = ((int)((l*100.0/1024/1024))*1.0/100);
                            System.out.println(info+"pppp");
                            dr = info.substring(info.indexOf("Duration")+10,info.indexOf(", start")-3);
                            int temp = info.indexOf("yuv");
                            sz = info.substring(info.indexOf("x",temp)-4,info.indexOf("x",temp)+5);
                            if(!Character.isDigit(sz.charAt(sz.length()-1)))
                                sz = sz.substring(0,sz.length()-1);
                            bt = info.substring(info.lastIndexOf("bitrate")+9,info.indexOf("kb/s")-1); 
                           // System.out.println("777"+sz+"777");  
                            System.out.println(sz);
                            d1 = Double.parseDouble(sz.substring(0,sz.indexOf("x")))*100;
                            d2 = Double.parseDouble(sz.substring(sz.indexOf("x")+1,sz.length()));
                            d = (int)(d1/d2)/100.0;

                            String[] str = {n,l+"M",dr,sz,String.valueOf(d),bt+"kb/s"};
                            out = new FileOutputStream(outputPath+"information.txt",true);
                            for(int m =0; m<str.length; m++){
                                    out.write(str[m].getBytes()); //向文件中写入数据
                                    out.write(' ');
                                    out.write(' ');
                                    out.write(' ');

                            }
                            out.write('\r'); // \r\n表示换行
                            out.write('\n'); 
                         }      
                         out.close();	//关闭输出流
                         System.out.println("写入成功！");
                         System.out.println("ok");

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Buttton2_1.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Buttton2_1.class.getName()).log(Level.SEVERE, null, ex);
                    }
        }else return;
        System.out.println("all done");
        //视频全部转换结束的后续处理
        jButton1.setEnabled(true);  
        jButton2.setEnabled(true);
        Videoview.start = false;
    }
    
}
