/**
 * 
 */
package com.k99k.ij.resize;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ColorProcessor;
import ij.process.ShortProcessor;
import org.apache.log4j.*;
/**
 * @author keel
 *
 */
public class IJBuilderForFW  {
	
	private static final Logger log = Logger.getLogger(IJBuilderForFW.class);
	
	public final ImagePlus resizeJpg(String inPath,String title,String toWidth,String toHeight) throws IOException{
		ImagePlus imp = IJ.openImage(inPath);
		return this.resizeJpg(imp, title, toWidth, toHeight);
		//IJ.saveAs(impResult, "jpeg", outPath);
		
	}

	
	public final ImagePlus resizeJpg(ImagePlus imp,String title,String toWidth,String toHeight) throws IOException{
		int interpDegree = 3;
		int syntheDegree = 3;
	    int analyDegree = 3;
		double zoomX;
		double zoomY;
	    double sizeX = getDoubleValue(toWidth, 0.0D, 10.0D,
				1.7976931348623157E+308D);
		double sizeY = getDoubleValue(toHeight, 0.0D, 10.0D,
				1.7976931348623157E+308D);
		//String method = "Least-Squares";// this.choiceMethod.getSelectedItem();
		//String interpolation = "Cubic";// this.choiceInterpolation.getSelectedItem();
		//boolean UnitPixelsX = true;// this.choiceUnitX.getSelectedItem().equals("Pixels");
		//boolean UnitPixelsY = true;// this.choiceUnitY.getSelectedItem().equals("Pixels");
		
		int nx = imp.getWidth();
		int ny = imp.getHeight();
		//System.out.println("nx:" + nx + " ny:" + ny + " type:" + imp.getType());
		if (nx > 3000)
			analyDegree = 1;
		if (ny > 3000) {
			analyDegree = 1;
		}
		// if (this.interpolation.equalsIgnoreCase("Linear")) {
		// this.interpDegree = 1;
		// this.syntheDegree = 1;
		// this.analyDegree = 1;
		// }
		//
		// if (this.method.equalsIgnoreCase("Interpolation")) {
		// this.analyDegree = -1;
		// }
		// --------------
		// if (UnitPixelsX == true) {
		zoomX = (sizeX / nx);
		// }
		// else {
		// zoomX = (sizeX / 100.0D);
		// }

		// if (UnitPixelsY == true) {
		zoomY = (sizeY / ny);
		// }
		// else {
		// zoomY = (sizeY / 100.0D);
		// }

		sizeX = (int) Math.round(nx * zoomX);
		sizeY = (int) Math.round(ny * zoomY);

		ImageStack stack = new ImageStack((int) sizeX, (int) sizeY);
	    
	    //------------------------------
		Resize resize = new Resize();
		
		if (imp.getType() == 4) {

			ImageAccess[] out = new ImageAccess[3];
			for (int c = 0; c < 3; ++c) {
				ImageAccess in = new ImageAccess((ColorProcessor) imp
						.getProcessor(), c);
				out[c] = new ImageAccess((int) sizeX, (int) sizeY);
				resize.computeZoom(in, out[c], analyDegree,
						syntheDegree, interpDegree, zoomY,
						zoomX, 0.0D, 0.0D, false);
			}
			byte[] r = (byte[]) (byte[]) out[0].createByteProcessor()
					.getPixels();
			byte[] g = (byte[]) (byte[]) out[1].createByteProcessor()
					.getPixels();
			byte[] b = (byte[]) (byte[]) out[2].createByteProcessor()
					.getPixels();
			ColorProcessor cp = new ColorProcessor((int) sizeX,
					(int) sizeY);
			cp.setRGB(r, g, b);
			stack.addSlice("", cp);
		}else{
			ImageAccess in = new ImageAccess(imp.getProcessor());

			ImageAccess out = new ImageAccess((int) sizeX, (int) sizeY);
			resize.computeZoom(in, out, analyDegree, syntheDegree,
					interpDegree, zoomY, zoomX, 0.0D, 0.0D, false);  
		      switch (imp.getType())
		      {
		      case 0:
		        stack.addSlice("", out.createByteProcessor());
		        break;
		      case 1:
		        stack.addSlice("", createShortProcessor(out));
		        break;
		      case 2:
		        stack.addSlice("", out.createFloatProcessor());
		      }
		}

		    if (stack.getSize() == imp.getStack().getSize()) {
		      ImagePlus impResult = new ImagePlus(title+"_new", stack);
		      //System.out.println("Resize OK:"+title);
		      return impResult;
		      //IJ.saveAs(impResult, "jpeg", outPath);
		      //impResult.updateAndDraw();
		      //impResult.show();
		      
		    }else{
		    	log.info("no out. stack.getSize():"+stack.getSize() + " imp.getStack().getSize():"+imp.getStack().getSize());
		    	//System.out.println("no out. stack.getSize():"+stack.getSize() + " imp.getStack().getSize():"+imp.getStack().getSize());
		    	return null;
		    }
	}
	
	
	public final ImagePlus resizeKeepRadio(String inPath,String title,boolean keepWidth,String toWidth,String toHeight,boolean fix) throws IOException{
		ImagePlus ip = IJ.openImage(inPath);
		return this.resizeKeepRadio(ip, title, keepWidth, toWidth, toHeight,fix);
	}
	
	public final ImagePlus resizeKeepRadio(ImagePlus ip,String title,boolean keepWidth,String toWidth,String toHeight,boolean fix) throws IOException{
		int w = ip.getWidth();
		int h = ip.getHeight();
		if (keepWidth) {
			String newH = keepRadioWidth(w, h, toWidth, toHeight,fix);
			return this.resizeJpg(ip, title, toWidth, newH);
		}else{
			String newW = keepRadioHeight(w, h, toWidth, toHeight,fix);
			return this.resizeJpg(ip, title, newW, toHeight);
		}
	}
	
	/**
	 * 比例不变,保证宽度符合，返回新的高度
	 * @param orgW 原宽
	 * @param orgH 原高
	 * @param toWidth
	 * @param toHeight
	 * @param fix 为true时,当新高度不够时，使用目标高度
	 * @return newHeight
	 */
	public static final String keepRadioWidth(int orgW,int orgH,String toWidth,String toHeight,boolean fix){
		double x = getDoubleValue(toWidth, 0.0D, orgW, 1.7976931348623157E+308D);
	    double y = Math.round(x * orgH / orgW);
	    if (fix && y<getDoubleValue(toHeight, 0.0D, orgH, 1.7976931348623157E+308D)) {
			return toHeight;
		}
		String newH = "" + Math.round(y);
		//System.out.println("newH:"+newH);
		return newH;
	}
	
	/**
	 * 比例不变,保证高度符合，返回新的宽度
	 * @param orgW 原宽
	 * @param orgH 原高
	 * @param toWidth
	 * @param toHeight
	 * @param fix 为true时,当新宽度不够时，使用目标宽度
	 * @return newWidth
	 */
	public static final String keepRadioHeight(int orgW,int orgH,String toWidth,String toHeight,boolean fix){
		double y = getDoubleValue(toHeight, 0.0D, orgH, 1.7976931348623157E+308D);
		double x = Math.round(y * orgW / orgH);
		if (fix && x < getDoubleValue(toWidth, 0.0D, orgW, 1.7976931348623157E+308D)) {
			return toWidth;
		}
		String newW = "" + Math.round(x);
		//System.out.println("newW:"+newW);
		return newW;
	}

	private final static ShortProcessor createShortProcessor(ImageAccess image) {
		double[] pixels = image.getPixels();

		int nx = image.getWidth();
		int ny = image.getHeight();
		ShortProcessor sp = new ShortProcessor(nx, ny);
		short[] ssrc = new short[pixels.length];

		for (int k = 0; k < pixels.length; ++k) {
			double p = pixels[k];
			if (p < 0.0D)
				p = 0.0D;
			if (p > 32767.0D)
				p = 32767.0D;
			ssrc[k] = (short) (int) p;
		}
		sp.setPixels(ssrc);
		return sp;
	}
	
	private final static double getDoubleValue(String text, double mini, double defaut,
			double maxi) {
		double d;
		try {
			String s = text;
			if (s.charAt(0) == '-') {
				s = s.substring(1);
			}
			d = new Double(s).doubleValue();
			if (d < mini) {
				return mini;
				// text.setText("" + mini);
			}

			if (d > maxi) {
				return maxi;
				// text.setText("" + maxi);
			}

		} catch (Exception e) {
			if (e instanceof NumberFormatException) {
				// text.setText("" + defaut);
			}
			return defaut;
		}
		return d;
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
	public final boolean dealOnePic(String picPath,String cate,int picId,String outPath,boolean fix,int position,boolean isSharp,boolean isScale) {
		try {
			//先生成输出的类别目录
			outPath = outPath+"/"+cate;
			(new File(outPath)).mkdirs();
			
			ImagePlus ip1 = IJ.openImage(picPath);
			ImagePlus ip2 = new ImagePlus(ip1.getTitle(), ip1.getImage());
			IJ.run("Input/Output...",
					"jpeg=88 gif=-1 file=.txt copy_row save_column save_row");
			//---------
			//是否整体直接缩放，不考虑等比缩放
			if (!isScale) {
				//需要进行等比缩放
				ip1 = this.resizeKeepRadio(ip1, "to854-KeepRadio", false,
						"960", "854",fix);
				String canvasPosition = "Center";
				if (position >0 ) {
					switch (position) {
					case 1:
						canvasPosition = "Top-Center";
						break;
					case 2:
						canvasPosition = "Top-Right";
						break;
					case 3:
						canvasPosition = "Center-Right";
						break;
					case 4:
						canvasPosition = "Bottom-Right";
						break;
					case 5:
						canvasPosition = "Bottom-Center";
						break;
					case 6:
						canvasPosition = "Bottom-Left";
						break;
					case 7:
						canvasPosition = "Center-Left";
						break;
					case 8:
						canvasPosition = "Top-Left";
						break;
					default:
						break;
					}
				}
				IJ.run(ip1,"Canvas Size...", "width=960 height=854 position="+canvasPosition);
			}else{
				//直接无等比例缩放
				ip1 = this.resizeJpg(ip1, "to854", 
						"960", "854");
			}
			if (isSharp) {
				IJ.run(ip1,"Unsharp Mask...", "radius=1 mask=0.30");
			}
			IJ.save(ip1,outPath+"/b_"+cate+picId+".jpg");
			
			//WindowManager.setTempCurrentImage(ip2);
			// String newW = t.keepRadioHeight(ip2.getWidth(), ip2.getHeight(),
			// "960", "854");
			// IJ.run("Size...",
			// "width="+newW+" height=854 constrain interpolation=Bilinear");
		
			//WindowManager.setTempCurrentImage();
			ImagePlus ip1_l = this.resizeJpg(ip1, ip1.getTitle(),
					"160", "225");
			IJ.run(ip1_l,"Unsharp Mask...", "radius=1 mask=0.20");
			IJ.save(ip1_l,outPath+"/"+cate+picId+".jpg");
			
			// ---- 640x480
			
			if (!isScale) {
				//需要进行等比缩放
				ip2 = this.resizeKeepRadio(ip2, "to640-KeepRadio", false,
						"640", "480",fix);
				String canvasPosition = "Center";
				if (position >0 ) {
					switch (position) {
					case 1:
						canvasPosition = "Top-Center";
						break;
					case 2:
						canvasPosition = "Top-Right";
						break;
					case 3:
						canvasPosition = "Center-Right";
						break;
					case 4:
						canvasPosition = "Bottom-Right";
						break;
					case 5:
						canvasPosition = "Bottom-Center";
						break;
					case 6:
						canvasPosition = "Bottom-Left";
						break;
					case 7:
						canvasPosition = "Center-Left";
						break;
					case 8:
						canvasPosition = "Top-Left";
						break;
					default:
						break;
					}
				}
				IJ.run(ip2,"Canvas Size...", "width=640 height=480 position="+canvasPosition);
			}else{
				//直接无等比例缩放
				ip2 = this.resizeJpg(ip2, "to640", 
						"640", "480");
			}
			if (isSharp) {
				IJ.run(ip2,"Unsharp Mask...", "radius=1 mask=0.30");
			}
			IJ.save(ip2,outPath+"/b_"+cate+picId+"_l.jpg");
			ImagePlus ip2_l = this.resizeJpg(ip2, ip2.getTitle(),
					"160", "180");
			IJ.run(ip2_l,"Unsharp Mask...", "radius=1 mask=0.20");
			IJ.save(ip2_l,outPath+"/"+cate+picId+"_l.jpg");
		} catch (IOException e) {
			log.error("dealOnePic error!", e);
			return false;
		}
		return true;
	}
	
	FilenameFilter jpgFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(".jpg") && (name.indexOf("[skip]")<0);
        }
    };
    
    FileFilter dirFilter = new FileFilter() {
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
	public final ArrayList<String> buildNewPics(String inPath,String outPath,HashMap<String,Integer> iniIdMap,String donePath){
		ArrayList<String> picList = new ArrayList<String>();


	    boolean moveToDonePath = false;
	    if (donePath != null && donePath.length()>2) {
	    	(new File(donePath)).mkdirs();
	    	moveToDonePath = true;//(new File(donePath)).mkdirs();
	    }
	    if (outPath != null && outPath.length()>2) {
	    	(new File(outPath)).mkdirs();
	    }
	    
		File srcdir = new File(inPath);
		File[] dirs = srcdir.listFiles(dirFilter);
	    if (dirs == null) {
	    	log.error("path not exist:"+srcdir);
	       //System.out.println("path not exist:"+srcdir);
	    } else {
	        for (int i=0; i<dirs.length; i++) {
	            String dirname = dirs[i].getName();
	            log.info("-----------["+dirname+"]------------");
	            //System.out.println("-----------["+dirname+"]------------");
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
					//识别特殊处理,文件名前添加标记用#分开,r和l表示位置,f表示无fix,s表示无sharp,c表示无等比缩放
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
					boolean re = this.dealOnePic(dirPath+"/"+picName, dirname, pId, outPath,fix,posi,isSharp,isScale);
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
	 * 生成jpg缩略图到指定路径下的pre目录
	 * @param inPath 
	 * @param width 
	 * @param height 
	 */
	public final boolean makePreviews(String inPath,int width,int height){
		File dir = new File(inPath);
		String[] pics = dir.list(jpgFilter);
		String outPath = inPath+"/pre/";
		if(!(new File(outPath)).mkdirs()){
			return false;
		}
		
		IJ.run("Input/Output...","jpeg=88 gif=-1 file=.txt copy_row save_column save_row");

		try {
			for (int i = 0; i < pics.length; i++) {
				ImagePlus ip = this.resizeKeepRadio(inPath+"/"+pics[i], "pre", false, width+"", height+"", false);
				IJ.save(ip, outPath+pics[i]);
				log.info("make pre done:"+pics[i]);
			}
		} catch (IOException e) {
			log.error("makePreviews error!", e);
			return false;
		}
		return true;
		
	}

//	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		IJBuilderForFW t = new IJBuilderForFW();
//		
//		String inPath = "F:/testPics";
//		String outPath = "F:/testPicsOut";
//		HashMap<String, Integer> initIdMap = new HashMap<String, Integer>();
//		initIdMap.put("abs", 289);
//		initIdMap.put("car", 56);
//		initIdMap.put("people", 255);
//		
//		t.buildNewPics(inPath, outPath,initIdMap,"F:/testPicsRe");
//		
//		//t.makePreviews("F:/testPics/sce", 300, 200);
//		
//	}



}
