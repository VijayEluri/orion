/**
 * 
 */
package com.k99k.fws;

import java.io.IOException;
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
	
	public final static void resizeKeepRadio(int width,int height,int gravity,String srcImg,String dstImg){
		resizeKeepRadio(width,height,gravityMap.get(gravity),srcImg,dstImg);
	}
	
	public final static void resizeKeepRadio(int width,int height,String gravity,String srcImg,String dstImg){
		if(!checkInit()){
			return;
		}
		try {
			GMOperation op = new GMOperation();
			op.addImage(srcImg);
			op.resize(width,height,'^');
			op.gravity(gravity);
			op.crop(width, height,0,0);
			op.sharpen(1D,0.3D);
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
	
		return true;
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
		resizeKeepRadio(960,854,0,picPath+"wallpaper-2937817.jpg",picPathTo+"wall7.jpg");
	}

}
