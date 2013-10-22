/**
 * 
 */
package com.k99k.fws;


import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.im4java.core.GMOperation;
import org.im4java.core.GraphicsMagickCmd;
import org.im4java.core.IM4JavaException;

/**
 * @author keel
 *
 */
public class ImgByGM {

	/**
	 * 
	 */
	public ImgByGM(String gmPath) {
		cmd.setSearchPath(gmPath);
	}
	
	private static final Logger log = Logger.getLogger(ImgByGM.class);
	
	private final static GraphicsMagickCmd cmd = new GraphicsMagickCmd("convert");
	
	private static boolean isInit = false;
	
	public final static void init(String gmPath){
		cmd.setSearchPath(gmPath);
		isInit=true;
	}
	
//	static String picPath = "/Users/keel/Documents/workspace/testJava/pics/";
	
	private final static boolean checkInit(){
		if(!isInit){
			log.error("ImgByGM is not inited.");
		}
		return isInit;
	}
	
	private final static HashMap<Integer,String> gravityMap = new HashMap<Integer, String>();
	
	static{
		gravityMap.put(0, "Center");
		gravityMap.put(1, "North");
		gravityMap.put(2, "NorthEast");
		gravityMap.put(3, "East");
		gravityMap.put(4, "SouthEast");
		gravityMap.put(5, "South");
		gravityMap.put(6, "SouthWest");
		gravityMap.put(7, "West");
		gravityMap.put(8, "NorthWest");
	}
	
	public final static void resizeKeepRadio(int width,int height,int gravity,String srcImg,String dstImg,boolean isSharp){
		resizeKeepRadio(width,height,gravityMap.get(gravity),srcImg,dstImg,isSharp);
	}
	
	public final static void resizeKeepRadio(int width,int height,String gravity,String srcImg,String dstImg,boolean isSharp){
		if(!checkInit()){
			return;
		}
		try {
			GMOperation op = new GMOperation();
			op.addImage(srcImg);
			op.resize(width,height,'^');
			op.gravity(gravity);
			op.crop(width, height,0,0);
			if (isSharp) {
				op.sharpen(1D,0.3D);
			}
			op.quality(80D);
			op.addImage(dstImg);
			cmd.run(op);
		} catch (IOException e) {
			log.error("resizeKeepRadio failed.", e);
		} catch (InterruptedException e) {
			log.error("resizeKeepRadio failed.", e);
		} catch (IM4JavaException e) {
			log.error("resizeKeepRadio failed.", e);
		}
	}
	
	public final static void resizeScale(int width,int height,String srcImg,String dstImg,boolean isSharp){
		if(!checkInit()){
			return;
		}
		try {
			GMOperation op = new GMOperation();
			op.addImage(srcImg);
			op.resize(width,height,'!');
			if (isSharp) {
				op.sharpen(1D,0.3D);
			}
			op.quality(80D);
			op.addImage(dstImg);
			cmd.run(op);
		} catch (IOException e) {
			log.error("resizeKeepRadio failed.", e);
		} catch (InterruptedException e) {
			log.error("resizeKeepRadio failed.", e);
		} catch (IM4JavaException e) {
			log.error("resizeKeepRadio failed.", e);
		}
	}
	
	/**
	 * 由一张源图生成四张图到指定路径下
	 * @param picPath 图片文件全路径
	 * @param cate 类别简称
	 * @param picId 图片id
	 * @param outPath 输出总路径
	 * @param fix 长宽等比缩放某一值不够时是否自动适应到目标值,默认为是
	 * @param position 中间为0,1为顶部，其他值顺时针方向推,默认为0
	 * @param isSharp 是否sharp化，默认为是
	 * @param isScale 是否等比缩放，默认为否
	 * @return 是否成功生成四张图
	 */
	public final static boolean dealOnePic(String picPath,String cate,int picId,String outPath,boolean fix,int position,boolean isSharp,boolean isScale) {
		
		try {
			//先生成输出的类别目录
			outPath = outPath+"/"+cate;
			(new File(outPath)).mkdirs();
			
			if(position<0 || position >8){
				position = 0;
			}
			String dstPicPath = outPath+"/b_"+cate+picId+".jpg";
			//---------
			//是否整体直接缩放，不考虑等比缩放
			if (!isScale) {
				//需要进行等比缩放
				resizeKeepRadio(960,854,position,picPath,dstPicPath,isSharp);
			}else{
				//直接无等比例缩放
				resizeScale(960,854,picPath,dstPicPath,isSharp);
			}
		
			String pre1 = outPath+"/"+cate+picId+".jpg";
			resizeScale(160,225,dstPicPath,pre1,isSharp);
			
			// ---- 640x480
			dstPicPath = outPath+"/b_"+cate+picId+"_l.jpg";
			if (!isScale) {
				//需要进行等比缩放
				resizeKeepRadio(640,480,position,picPath,dstPicPath,isSharp);
			}else{
				//直接无等比例缩放
				resizeScale(640,480,picPath,dstPicPath,isSharp);
			}
			
			String pre2 = outPath+"/"+cate+picId+"_l.jpg";
			resizeScale(160,180,dstPicPath,pre2,isSharp);
			
		} catch (Exception e) {
			log.error("dealOnePic error!", e);
			return false;
		}
		return true;
	}
	
	
	static FilenameFilter jpgFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return name.substring(name.length()-4).toLowerCase().matches("[jpg|png|gif]") && (name.indexOf("[skip]")<0);
        }
    };
    
    static FileFilter dirFilter = new FileFilter() {
        public boolean accept(File file) {
            return file.isDirectory();
        }
    };
	
	/**
	 * 处理所有新图片.<br />
	 * 文件名识别特殊处理：文件名前添加标记用#分开,p及后一位数字表示位置,f表示无fix,s表示无sharp,c表示无等比缩放
	 * 如“abc.jpg”，添加为无fix,无sharp,中间偏左后为"fsp7#abc.jpg" <br />
	 * 注意图片按文件名#号后的字母顺序生成,文件名加入[skip]可跳过处理
	 * @param inPath 
	 * @param outPath 
	 * @param iniIdMap 初始id值:&lt;cate,maxId&gt;
	 * @param donePath 处理完后将源图移动到的另一路径,为空时不移动
	 * @return ArrayList 每项格式为catePre#picId
	 */
	public final static ArrayList<String> buildNewPics(String inPath,String outPath,HashMap<String,Integer> iniIdMap,String donePath){
		ArrayList<String> picList = new ArrayList<String>();


	    boolean moveToDonePath = false;
	    if (donePath != null && donePath.length()>2) {
	    	(new File(donePath)).mkdirs();
	    	moveToDonePath = true;
	    }
	    if (outPath != null && outPath.length()>2) {
	    	(new File(outPath)).mkdirs();
	    }
	    
		File srcdir = new File(inPath);
		File[] dirs = srcdir.listFiles(dirFilter);
	    if (dirs == null) {
	    	log.error("path not exist:"+srcdir);
	    } else {
	        for (int i=0; i<dirs.length; i++) {
	            String dirname = dirs[i].getName();
	            log.info("-----------["+dirname+"]------------");
	            int initId = 0;
	            if (iniIdMap!= null && iniIdMap.containsKey(dirname)) {
					initId += iniIdMap.get(dirname);
				}
	            String dirPath = dirs[i].getAbsolutePath();
	            File dir = new File(dirPath);
	            String[] files = dir.list(jpgFilter);
	            //图片文字名按字母顺序排序,忽略#号前的标记
	            Arrays.sort(files,new Comparator<String>(){
					@Override
					public int compare(String s1, String s2) {
						return s1.substring(s1.indexOf("#")+1).compareToIgnoreCase(s1.substring(s2.indexOf("#")+1));
					}});
	            
	            for (int j = 0; j < files.length; j++) {
					String picName = files[j];
					//识别特殊处理,文件名前添加标记用#分开,f表示无fix,s表示无sharp,c表示无等比缩放
					int posi = 0;
					boolean isSharp = true;
					boolean fix = true;
					String[] sarr = picName.split("#");
					boolean isScale = false;
					if (sarr.length > 1) {
						String s = sarr[0];
						int p = s.indexOf("p");
						if (p >= 0 && (p+1 < s.length()) && s.substring(p+1,p+2).matches("[0-8]")) {
							posi = Integer.parseInt(s.substring(p+1,p+2));
						}
						if (s.indexOf("f") >= 0) {
							fix = false;
						}
						if (s.indexOf("s") >= 0) {
							isSharp = false;
						}
						if (s.indexOf("c") >= 0) {
							isScale = true;
						}
					}
					int pId = initId+j+1;
					boolean re = dealOnePic(dirPath+"/"+picName, dirname, pId, outPath,fix,posi,isSharp,isScale);
					if (re) {
						boolean move = false;
						if (moveToDonePath) {
							File f = new File(inPath+"/"+dirname+"/"+picName);
							String done = donePath+"/"+dirname;
							(new File(done)).mkdirs();
							//TODO 注意当目标文件已存在时，无法移动，原文件会仍然存在
							move = f.renameTo(new File(done,picName));
						}
						//保存成功处理的pic到list，格式为catePre#picId
						picList.add(dirname+"#"+pId);
						log.info(picName+" done! moved:"+move+" "+dirname+"#"+pId);
					}else{
						log.error(picName+" failed!");
					}
				}
	        }
	    }
	    return picList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String picPath = "/Users/keel/Pictures/used/";
		String picPathTo = "/Users/keel/Pictures/";
		ImgByGM.init("/usr/local/Cellar/graphicsmagick/1.3.18/bin");
		//wallpaper-2937817
		//wallpaper-2810122.png
		//resizeKeepRadio(960,854,0,picPath+"wallpaper-2937817.jpg",picPathTo+"wall8.jpg");
		resizeScale(960,854,picPath+"wallpaper-2937817.jpg",picPathTo+"wall8.jpg",true);
		System.out.println("end");
	}

}
