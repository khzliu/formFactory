package org.me.Videoviewapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static org.me.Videoviewapp.Videoview.i;
import static org.me.Videoviewapp.Videoview.progressbar;

 class ConvertVideo {
     
     static String inputPath = "";
     
     static String outputPath = "";
     
     static String ffmpegPath = "";
     
     static String line = ""; 
     static int result = 0;
    // static String ss = "";
     
     
     static void getPath() { // �Ȼ�ȡ��ǰ��Ŀ·�����ڻ��Դ�ļ���Ŀ���ļ���ת������·��
         File diretory = new File("");
         try {
             String currPath = diretory.getAbsolutePath();  
             //inputPath = currPath + "\\input\\";
             outputPath = "D:\\ffmpeg\\output\\";
             ffmpegPath = currPath + "\\ffmpeg\\";
             System.out.println(currPath);
         }
         catch (Exception e) {
             System.out.println("getPath����");
         }
     }
     
     static String process() {
         int type = checkContentType();
         String status = "";
         if (type == 0) {
             System.out.println("直接转换");
             status = processMulti(inputPath);// ֱ��ת��flv��ʽ
             //String avifilepath = processAVI(type);
         } else if (type == 1) {
             String avifilepath = processAVI(type);
             if (avifilepath == null)
                 return null;// û�еõ�avi��ʽ
             status = processMulti(avifilepath);// ��aviת��flv��ʽ
         }
         return status;
     }
 
     static int checkContentType() {
         String type = inputPath.substring(inputPath.lastIndexOf(".") + 1, inputPath.length())
                 .toLowerCase();
         // ffmpeg�ܽ����ĸ�ʽ����asx��asf��mpg��wmv��3gp��mp4��mov��avi��flv�ȣ�
         if (type.equals("avi")) {
             return 0;
         } else if (type.equals("mpg")) {
             return 0;
         } else if (type.equals("wmv")) {
             return 0;
         } else if (type.equals("3gp")) {
             return 0;
         } else if (type.equals("mov")) {
             return 0;
         } else if (type.equals("mp4")) {
             return 0;
         } else if (type.equals("asf")) {
             return 0;
         } else if (type.equals("asx")) {
             return 0;
         } else if (type.equals("flv")) {
             return 0;
         }
         // ��ffmpeg�޷��������ļ���ʽ(wmv9��rm��rmvb��),
         // �������ñ�Ĺ��ߣ�mencoder��ת��Ϊavi(ffmpeg�ܽ�����)��ʽ.
         else if (type.equals("wmv9")) {
             return 1;
         } else if (type.equals("rm")) {
             return 1;
         } else if (type.equals("rmvb")) {
             return 1;
         }
         return 9;
     }
 
     static boolean checkfile(String path) {
         File file = new File(path);
         if (!file.isFile()) {
             return false;
         }
         return true;
     }
     //mencoder Jessica.rmvb -oac lavc -lavcopts acodec=mp3:abitrate=64 -ovc xvid -xvidencopts bitrate=600 -of avi -o tt.avi
     // ��ffmpeg�޷��������ļ���ʽ(wmv9��rm��rmvb��), �������ñ�Ĺ��ߣ�mencoder��ת��Ϊavi(ffmpeg�ܽ�����)��ʽ.
     static String processAVI(int type) {
         List<String> commend = new ArrayList<String>();
         commend.add(ffmpegPath + "mencoder");
         commend.add(inputPath);
         commend.add("-oac");
         commend.add("mp3lame");
         commend.add("-lameopts");
         commend.add("preset=64");
         commend.add("-ovc");
         commend.add("xvid");
         commend.add("-xvidencopts");
         commend.add("bitrate=600");
         commend.add("-of");
         commend.add("avi");
         commend.add("-o");
         commend.add(outputPath + "a.avi");
        
         try {
             ProcessBuilder builder = new ProcessBuilder();
             Process process = builder.command(commend).redirectErrorStream(true).start();
             
             final InputStream is1 = process.getInputStream();  
             final InputStream is2 = process.getErrorStream();
             final int fesultAVI =0;
             //new PrintStream(process.getInputStream());
             //new PrintStream(process.getErrorStream());
             new Thread() {  
                public void run() {  
                    BufferedReader br = new BufferedReader( new InputStreamReader(is1));  
                    try {  
                        String lineB = null;  
                        while ((lineB = br.readLine()) != null ){  
                            if(lineB != null)
                                //System.out.println(lineB);
                            if(Videoview.flag)
                                Runtime.getRuntime().exec("taskkill /f /im mencoder.exe"); //“暂停”标志位“真”时，杀死mencoder进程
                            else{
                                if ( (lineB!=null) && (lineB.indexOf("%)")!=-1) ){  
                                //根据时长百分比，设置进度条
                                String persent = lineB.substring(lineB.indexOf("(")+1,lineB.indexOf(")")-1);
                                //System.out.println(persent);
                                if(persent.indexOf(" ")==0)
                                    persent = persent.substring(1);
                                result = Integer.parseInt(persent)/2;
                                progressbar.setValue(result);
                                
                    }
                }
                        }  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }.start();   
            new Thread() {  
                public void run() {  
                    BufferedReader br2 = new BufferedReader( new InputStreamReader(is2));  
                    try {  
                        String lineC = null;  
                        while ( (lineC = br2.readLine()) != null){  
                            //if(lineC != null)
                                //System.out.println(lineC);  
                        }  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }.start();
            //progressbar.setValue(0);
             process.waitFor();
             System.out.println("who cares");
             return outputPath + "a.avi";
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
     }
 
     // ffmpeg�ܽ����ĸ�ʽ����asx��asf��mpg��wmv��3gp��mp4��mov��avi��flv�ȣ�
     static String processMulti(String oldfilepath) {
         
         int d = 0;
         String time= "";
         String st1 = "";
         String st2 = "";
         int resultMulti = 0;
         int ct = 0;
         String[] r = inputPath.split("\\\\");
         st1 = r[r.length - 2];  //视频上层目录
         st2 = r[r.length - 1];  //视频名
         st2 = st2.substring(0, st2.lastIndexOf("."));  //视频名去后缀
         outputPath +=st1+" "+Buttton2_1.ly_time+"\\"+st2+"\\";
         File f= new File(outputPath);  
	 f.mkdirs(); //创建文件夹
         if (!checkfile(inputPath)) {
             System.out.println(oldfilepath + " is not file");
             return null;
         }
         List<String> command = new ArrayList<String>();
         command.add(ffmpegPath + "ffmpeg");
         command.add("-i"); //输入文件
         command.add(oldfilepath); 
         command.add("-vcodec");
         command.add("libx264");
         command.add("-acodec");
         command.add("copy");
         command.add("-s"); //指定分辨率
         command.add(changeSize(Videoview.sizeall[i],Videoview.height));
         command.add("-f");
         command.add("mp4");
         command.add(outputPath +"video.mp4");
         try {
             
            
             Process videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();

             BufferedReader buf = null; // 保存ffmpeg的输出结果流  
              
             //read the standard output  

             buf = new BufferedReader(new InputStreamReader(videoProcess.getInputStream()));  

             //StringBuffer sb= new StringBuffer(); 
             
             while ((line = buf.readLine()) != null) {   
                //sb.append(line);  
                if(Videoview.flag)
                    Runtime.getRuntime().exec("taskkill /f /im ffmpeg.exe"); //“暂停”标志位“真”时，杀死ffmpeg进程
                else{
                    if ( (line!=null) && (line.indexOf("time=")!=-1) ){  
                      //根据时长百分比，设置进度条
                      time = line.substring(line.indexOf("time=")+5,line.indexOf("bitrate")-4);
                      d = timeToSec(time);
                      //d = Double.parseDouble(time);
                      if(result==0)
                          resultMulti = ((int) (100*d/Videoview.timeall[Videoview.i]));
                      else
                          resultMulti = result + ((int) (100*d/Videoview.timeall[Videoview.i])/2);
                      progressbar.setValue(resultMulti);
                    }
                }
             }  
            progressbar.setValue(0);
            result = 0; 
            videoProcess.waitFor();//这里线程阻塞，将等待外部转换进程运行成功运行结束后，才往下执行  
             return "ok";
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
     }
     
     static String analysis(String oldfilepath) {
            if (!checkfile(inputPath)) {
                System.out.println(oldfilepath + " is not file");
                return null;
            }
            //System.out.println(oldfilepath+"analysis");
            List<String> command = new ArrayList<String>();
            command.add(ffmpegPath + "ffmpeg");
            command.add("-i");
            command.add(oldfilepath);
            try {

                Process videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();

                //videoProcess.waitFor();
                BufferedReader buf = null; // 保存ffmpeg的输出结果流  
                String line = null;  
                //read the standard output  
  
                buf = new BufferedReader(new InputStreamReader(videoProcess.getInputStream()));  

                StringBuffer sb= new StringBuffer();  
                while ((line = buf.readLine()) != null) {  
                    //System.out.println(line);  
                    sb.append(line);  
                    continue;  
                }  
                videoProcess.waitFor();//这里线程阻塞，将等待外部转换进程运行成功运行结束后，才往下执行  
                //System.out.println(sb.toString());
                return sb.toString();
                //return null;
              
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
         }
         
         static void screenshot(String name,int time,int timeall) {
            if (!checkfile(name)) {
                System.out.println(name + " is not file");
                return;
            }
            List<String> command = new ArrayList<String>();
            command.add(ffmpegPath + "ffmpeg");
            command.add("-i");  //输入文件
            command.add(name);
            command.add("-y");   //覆盖输出文件
            command.add("-f");
            command.add("image2");
            command.add("-ss");  //搜索到指定的时间
            command.add(secToTime(timeall));
            command.add("-vframes");
            command.add("1");
            command.add(outputPath+time+".jpg");
            try {
                Process videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();
                
                BufferedReader buf = null; // 保存ffmpeg的输出结果流  
                String line = null;  
                //read the standard output  
  
                buf = new BufferedReader(new InputStreamReader(videoProcess.getInputStream()));  

                //StringBuffer sb= new StringBuffer();  
                while ((line = buf.readLine()) != null) {  
                   // sb.append(line);  
                    continue;  
                }  
                videoProcess.waitFor();
                //System.out.println(sb.toString());
               // return sb.toString();
                //return null;
              
            } catch (Exception e) {
                e.printStackTrace();
               // return null;
            }
         }
         
    //将整数型时间转换为“00:00:00”形式
    static String secToTime(int time) {
           String timeStr = null;
           int hour = 0;
           int minute = 0;
           int second = 0;
           if (time <= 0)
               return "00:00";
           else {
               minute = time / 60;
               if (minute < 60) {
                   second = time % 60;
                   timeStr = "00:"+unitFormat(minute) + ":" + unitFormat(second);
               } else {
                   hour = minute / 60;
                   if (hour > 99)
                       return "99:59:59";
                   minute = minute % 60;
                   second = time - hour * 3600 - minute * 60;
                   timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
               }
           }
           return timeStr;
       }
       
       //个位数前补0
       static String unitFormat(int i) {
           String retStr = null;
           if (i >= 0 && i < 10)
               retStr = "0" + Integer.toString(i);
           else
               retStr = "" + i;
           return retStr;
       }
       
       //将“00:00:00.00”形式转换为整数型时间
       static int timeToSec(String str) {
         int index1 = 0;
         int index2 = 0;
         int hh = 0;
         int mi = 0;
         int ss = 0;
         index1=str.indexOf(":");
         index2=str.indexOf(":",index1+1);
         hh=Integer.parseInt(str.substring(0,index1));
         mi=Integer.parseInt(str.substring(index1+1,index2));
         ss=Integer.parseInt(str.substring(index2+1));
         return hh*60*60+mi*60+ss;
       }
       
       //根据复选框改变生成视频的分辨率
       static String changeSize(String s,int i) {
           String s1 = s.substring(s.indexOf("x")+1);
           int i1 = Integer.parseInt(s1.trim());
           if(i1>i){
               String s0 = s.substring(0,s.indexOf("x"));
               int i0 = Integer.parseInt(s0.trim());
               i0 = (int) (i0/(i1/(i*1.0)));
               i1 = i;
               return String.valueOf(i0)+"x"+String.valueOf(i1);
           }else 
               return s;
       }
 }
 
 
