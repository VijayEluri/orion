package com.k99k.app.orion;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.bson.types.BasicBSONList;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.*;

public class MongoConfig {
	
	
	private Mongo mongo;
	
	private DB db;
	
	/**
	 * 获取数据库连接并验证
	 * @return
	 */
	private boolean getDB(){
		try {
			if (this.db !=null && this.db.getName().equals("fwall")) {
				return true;
			}
			if (mongo == null) {
				mongo = new Mongo("127.0.0.1" , 27017 );
			}
			this.db = mongo.getDB("fwall");
			boolean auth = db.authenticate("sikewall009", "6667441".toCharArray());
			if (auth) {
				return true;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			mongo = null;
			return false;
		} catch (MongoException e) {
			e.printStackTrace();
			mongo = null;
			return false;
		}
		return false;
	}
	
	void testConfig(){
		
		if (!this.getDB()) {
			System.out.println("error:getDB");
			return;
		}
		DBCollection coll = db.getCollection("wallConfig");
		DBObject myDoc = coll.findOne();
		BasicBSONList l = (BasicBSONList) myDoc.get("index");
		DBObject o = (DBObject) l.get(0);
		String s = (String) ((BasicBSONList)((BasicBSONList) o.get("pics")).get(5)).get(2);
		System.out.println(s);
	}
	
	/**
	 * 建立索引表,结构同fw_index.htm
	 * @return
	 */
	boolean createWallConfig() {
		String s = "{ 'version':2, 'author':'Keel', 'state':1, 'update':'update:2010-07-25', 'updateCN':'图库更新于:2010-07-25', 'updateEN':'update:2010-07-25', 'updateJP':'ギャラリー更新:2010-07-25', 'updateTW':'圖庫更新於:2010-07-25', 'adkey':'game music movie video tool', 'server':'http://202.102.29.201/orion/', 'saveSdPath':'/sdcard/wallpaper/', 'index':[ {'cate':'Newest','cateCN':'最新','cateEN':'Newest','cateJP':'最新','cateTW':'最新', 'pics':[ ['abs#196','abs#195','abs#194','abs#193'], ['people#172','people#171','people#170','people#169'], ['people#168','people#167','people#166','people#165'], ['comic#56','comic#55','comic#54','comic#53'], ['mov#240','mov#239','mov#238','mov#237'], ['mov#236','mov#235','mov#234','mov#233'], ['mov#232','mov#231','mov#230','mov#229'], ['sce#196','sce#195','sce#194','sce#193'], ['sce#192','sce#191','sce#190','sce#189'], ['mov#228','mov#227','mov#226','mov#225'], ['mov#224','mov#223','mov#222','mov#221'], ['mov#220','mov#219','mov#218','mov#217'], ['sce#188','sce#187','sce#186','sce#185'], ['sce#184','sce#183','sce#182','sce#181'], ['sce#180','sce#179','sce#178','sce#177'], ['sce#176','sce#175','sce#174','sce#173'], ['car#92','car#91','car#90','car#89'], ['car#88','car#87','car#86','car#85'], ['car#84','car#83','car#82','car#81'], ['car#80','car#79','car#78','car#77'], ['car#76','car#75','car#74','car#73'], ['car#72','car#71','car#70','car#69'], ['game#96','game#95','game#94','game#93'], ['game#92','game#91','game#90','game#89'], ['game#88','game#87','game#86','game#85'], ['game#84','game#83','game#82','game#81'], ['game#80','game#79','game#78','game#77'], ['fifa#24','fifa#23','fifa#22','fifa#21'], ['fifa#20','fifa#19','fifa#18','fifa#17'], ['fifa#16','fifa#15','fifa#14','fifa#13'], ['fifa#12','fifa#11','fifa#10','fifa#9'], ['fifa#8','fifa#7','fifa#6','fifa#5'], ['fifa#4','fifa#3','fifa#2','fifa#1'], ['people#164','people#163','people#162','people#161'], ['people#160','people#159','people#158','people#157'] ]}, {'cate':'Abstract','cateCN':'抽象艺术','cateEN':'Abstract','cateJP':'抽象芸術','cateTW':'抽象藝術', 'maxPic':196,'picPre':'abs'}, {'cate':'Space','cateCN':'宇宙空间','cateEN':'Space','cateJP':'宇宙','cateTW':'宇宙', 'maxPic':224,'picPre':'space'}, {'cate':'People','cateCN':'人物','cateEN':'People','cateJP':'人','cateTW':'人物', 'maxPic':172,'picPre':'people'}, {'cate':'Scenic','cateCN':'美景','cateEN':'Scenic','cateJP':'美しい風景','cateTW':'美景', 'maxPic':196,'picPre':'sce'}, {'cate':'Movie','cateCN':'电影','cateEN':'Movie','cateJP':'映画','cateTW':'電影', 'maxPic':240,'picPre':'mov'}, {'cate':'Food','cateCN':'美食','cateEN':'Food','cateJP':'食品','cateTW':'美食', 'maxPic':44,'picPre':'food'}, {'cate':'City','cateCN':'城市','cateEN':'City','cateJP':'シティ','cateTW':'城市', 'maxPic':72,'picPre':'city'}, {'cate':'LOMO','cateCN':'LOMO','cateEN':'LOMO','cateJP':'LOMO','cateTW':'LOMO', 'maxPic':44,'picPre':'lomo'}, {'cate':'Creature','cateCN':'动物世界','cateEN':'Creature','cateJP':'動物','cateTW':'動物', 'maxPic':132,'picPre':'ani'}, {'cate':'Game','cateCN':'游戏','cateEN':'Game','cateJP':'ゲーム','cateTW':'遊戲', 'maxPic':96,'picPre':'game'}, {'cate':'Comic','cateCN':'唯美漫画','cateEN':'Comic','cateJP':'漫画','cateTW':'漫畫', 'maxPic':56,'picPre':'comic'}, {'cate':'Car','cateCN':'名车','cateEN':'Car','cateJP':'リムジン','cateTW':'名車', 'maxPic':92,'picPre':'car'}, {'cate':'Childhood','cateCN':'童年','cateEN':'Childhood','cateJP':'小児期','cateTW':'童年','maxPic':28,'picPre':'kids'}, {'cate':'Iceland','cateCN':'冰岛','cateEN':'Iceland','cateJP':'アイスランド','cateTW':'冰島', 'maxPic':36,'picPre':'iceland'}, {'cate':'Still life','cateCN':'静物','cateEN':'Still life','cateJP':'静物','cateTW':'靜物', 'maxPic':104,'picPre':'still'}, {'cate':'Chinese style','cateCN':'中国风','cateEN':'Chinese style','cateJP':'中国風','cateTW':'中國風', 'maxPic':44,'picPre':'cn'}, {'cate':'Flower','cateCN':'精品花卉','cateEN':'Flower','cateJP':'ファイン花','cateTW':'精品花卉', 'maxPic':68,'picPre':'flower'}, {'cate':'Avatar','cateCN':'阿凡达','cateEN':'Avatar','cateJP':'Avatar','cateTW':'Avatar', 'maxPic':36,'picPre':'ava'}, {'cate':'WorldCup2010','cateCN':'WorldCup2010','cateEN':'WorldCup2010','cateJP':'WorldCup2010','cateTW':'WorldCup2010', 'maxPic':24,'picPre':'fifa'} ], 'backup':[ {'cate':'Easter Day','cateCN':'复活节','cateEN':'Easter Day','cateJP':'イースター','cateTW':'復活節', 'maxPic':28,'picPre':'easter'} ], 'starmenu':{ 'ver':1, 'show1': [ {'txt':'Show pictures:','txtEN':'Show pictures:','txtCN':'浏览方式:','txtTW':'瀏覽方式:','txtJP':'陳列:','type':'title'}, {'txt':'Shuffle all categories','txtEN':'Shuffle all categories','txtCN':'随机混和','txtTW':'Shuffle 隨機','txtJP':'シャッフル','ison':false,'type':'order','sort':'shuffle','asc':0}, {'txt':'Last position','txtEN':'Last position','txtCN':'最后的位置','txtTW':'最後的位置','txtJP':'最終位置','ison':false,'type':'order','sort':'last','asc':0}, {'txt':'Stars:','txtEN':'Stars:','txtCN':'星星:','txtTW':'星星:','txtJP':'星:','type':'title'}, {'txt':'My star pictures','txtEN':'My star pictures','txtCN':'我加星的图','txtTW':'我加星的圖','txtJP':'私の星','type':'mystars'} ], 'show2': [ {'txt':'Order by:','txtEN':'Order by:','txtCN':'排序:','txtTW':'排序:','txtJP':'ソート:','type':'title'}, {'txt':'Newest','txtEN':'Newest','txtCN':'最新','txtTW':'最新','txtJP':'最新','ison':true,'type':'order','sort':'time','asc':0}, {'txt':'Downloads','txtEN':'Downloads','txtCN':'下载量','txtTW':'下載量','txtJP':'ダウンロード','ison':false,'type':'order','sort':'down','asc':0}, {'txt':'Lucky','txtEN':'Lucky','txtCN':'幸运','txtTW':'幸運','txtJP':'ラッキー','ison':false,'type':'order','sort':'random','asc':0}, {'txt':'Stars','txtEN':'Stars','txtCN':'加星数','txtTW':'加星數','txtJP':'星','ison':false,'type':'order','sort':'star','asc':0,'noshow':1}, {'txt':'Stars:','txtEN':'Stars:','txtCN':'星星:','txtTW':'星星:','txtJP':'星:','type':'title'}, {'txt':'My star pictures','txtEN':'My star pictures','txtCN':'我加星的图','txtTW':'我加星的圖','txtJP':'私の星','type':'mystars'} ], 'show3': [ {'txt':'Stars:','txtEN':'Stars:','txtCN':'星星:','txtTW':'星星:','txtJP':'星:','type':'title'}, {'txt':'Add a star for this pic#Remove this star','txtEN':'Add a star for this pic#Remove this star','txtCN':'加星#取消加星','txtTW':'加星#取消加星','txtJP':'星を追加する#キャンセルの星','type':'addstar'} ], 'close':{'txt':'[ Close ]','txtEN':'[ Close ]','txtCN':'[ 关闭 ]','txtTW':'[ 關閉 ]','txtJP':'[ クローズ ]'} } }";
		return this.createColl("wallConfig", s);
	}
	
	/**
	 * 由json字符串创建一个Collection
	 * @param collName
	 * @param json 
	 * @return
	 */
	boolean createColl(String collName,String json){
		try {
			if (!this.getDB()) {
				System.out.println("error:getDB");
				return false;
			}
			DBCollection coll = db.getCollection(collName);
			coll.drop();
			System.out.println("droped:"+collName);
			coll = db.getCollection(collName);
			BasicDBObject ob = (BasicDBObject) JSON.parse(json);
			coll.insert(ob);
			System.out.println("creat ok:"+collName);
		} catch (MongoException e) {
			e.printStackTrace();
			return false;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	boolean createUser(){
		String s = "{'userName':'test', 'imei':'abcde', 'imsi':'', 'screen':{'width':480,'height':854}, 'handset': { 'display':'ERD79', 'board':'desirec', 'brand':'verizon', 'fingerprint':'verizon/htc_desirec/desirec/desirec:2.1/ERD79/165907:user/release-keys', 'device':'desirec', 'host':'Android-X03', 'id':'ERD79', 'model':'Eris', 'product':'htc_desirec', 'tags':'release-keys', 'type':'user', 'user':'u70000', 'user-agent':'Dalvik/1.1.0 (Linux; U; Android 2.1; Eris Build/ERD79)' },'star':[], 'info':{ }, 'regTime':'', 'lastLogin':'', 'loginTimes':223, 'appVersion':'cn-2.1','state':1, 'point':0}";
		if(this.createColl("wallUser", s)){
			DBCollection coll = db.getCollection("wallUser");
			BasicDBObject query = new BasicDBObject();
			query.put("userName", "test");
			BasicDBObject ss = new BasicDBObject();
			ss.append("$set", new BasicDBObject().append("regTime", new Date()).append("lastLogin", new Date()));
			coll.update(query, ss);
		}else{
			return false;
		}
		return true;
		
	}
	
	/**
	 * 根据当前的分类创建pic对象
	 * @param catePre
	 * @param picMax
	 * @param ifClear
	 * @param topId
	 * @return
	 */
	boolean createPicFromCate(String catePre,int picMax,boolean ifClear,int topId){
		//
		String pathPre = "/orion/";
		try {
			if (!this.getDB()) {
				System.out.println("error:getDB");
				return false;
			}
			DBCollection piccoll = db.getCollection("wallPic");

			if (ifClear) {
				piccoll.remove(new BasicDBObject("cate",catePre));
			}
			
			
        	System.out.println("--------cate:"+catePre+ " max:"+picMax);
        	for (int i = 0; i <picMax; i++) {
        		int num = i+1;
        		BasicDBObject pic = new BasicDBObject();
        		pic.put("picId", num);//自然数序列号
        		BasicBSONList paths = new BasicBSONList();
        		paths.put("0", pathPre+catePre+"/"+catePre+num+".jpg");
        		paths.put("1", pathPre+catePre+"/"+catePre+num+"_l.jpg");
        		paths.put("2", pathPre+catePre+"/b_"+catePre+num+".jpg");
        		paths.put("3", pathPre+catePre+"/b_"+catePre+num+"_l.jpg");
        		pic.put("picPath", paths);
        		pic.put("picSource", "");
        		pic.put("picName", catePre+"#"+num);//后期可以是真正的名称
//	        		DBObject time = new BasicDBObject("addTime", new Date());
//	    			pic.putAll(time);
    			pic.put("addTime", new Date());
        		pic.put("click", 0);
        		pic.put("download", 0);
        		pic.put("setWall", 0);
        		pic.put("stars", 0);
        		pic.put("starInfo", new BasicDBList());
        		pic.put("cate", catePre);
        		pic.put("info", "");
        		pic.put("state", 1);//0为未发布状态,1为正常发布状态,2为新发布或其他含义
        		pic.put("topId", topId);//用于置顶排序等
        		/*
        		//加星后如下
        		"starInfo":[
        		        	{"userId":ObjectId(),"msg":"","good":32,"bad":2},
        		        	{"userId":ObjectId()},
        		        	{"userId":ObjectId()}
        		]
        		*/
        		
        		piccoll.save(pic);
        		//System.out.println("pic:"+pic.getString("picId"));
			}
			System.out.println("wallPic create ok:"+catePre+" max:"+picMax);
		} catch (MongoException e) {
			e.printStackTrace();
			return false;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
	/**
	 * 根据当前的分类创建pic对象
	 * @return
	 */
	boolean createPicFromCate(){
		//
		String pathPre = "/orion/";
		try {
			if (!this.getDB()) {
				System.out.println("error:getDB");
				return false;
			}
			DBCollection coll = db.getCollection("wallCate");
			DBCollection piccoll = db.getCollection("wallPic");
			piccoll.drop();
			System.out.println("wallPic droped.");
			piccoll = db.getCollection("wallPic");
			DBCursor cur = coll.find();
	        while(cur.hasNext()) {
	        	DBObject o = cur.next();
	        	//String cate = (String) o.get("cateName");
	        	String catePre = (String)o.get("catePre");
	        	int max = ((Integer) o.get("max")).intValue();
	        	System.out.println("--------cate:"+catePre+ " max:"+max);
	        	for (int i = 0; i <max; i++) {
	        		int num = i+1;
	        		BasicDBObject pic = new BasicDBObject();
	        		pic.put("picId", num);//自然数序列号
	        		BasicBSONList paths = new BasicBSONList();
	        		paths.put("0", pathPre+catePre+"/"+catePre+num+".jpg");
	        		paths.put("1", pathPre+catePre+"/"+catePre+num+"_l.jpg");
	        		paths.put("2", pathPre+catePre+"/b_"+catePre+num+".jpg");
	        		paths.put("3", pathPre+catePre+"/b_"+catePre+num+"_l.jpg");
	        		pic.put("picPath", paths);
	        		pic.put("picSource", "");
	        		pic.put("picName", catePre+"#"+num);//后期可以是真正的名称
//	        		DBObject time = new BasicDBObject("addTime", new Date());
//	    			pic.putAll(time);
	    			pic.put("addTime", new Date());
	        		pic.put("click", 0);
	        		pic.put("download", 0);
	        		pic.put("setWall", 0);
	        		pic.put("stars", 0);
	        		pic.put("cate", catePre);
	        		pic.put("info", "");
	        		pic.put("state", 1);//0为未发布状态,1为正常发布状态,2为新发布或其他含义
	        		pic.put("topId", 0);//用于置顶排序等
	        		/*
	        		//加星后如下
	        		"starInfo":[
	        		        	{"userId":ObjectId(),"msg":"","good":32,"bad":2},
	        		        	{"userId":ObjectId()},
	        		        	{"userId":ObjectId()}
	        		]
	        		*/
	        		
	        		piccoll.save(pic);
	        		//System.out.println("pic:"+pic.getString("picId"));
				}
	        	
	        }
			System.out.println("wallPic create ok");
		} catch (MongoException e) {
			e.printStackTrace();
			return false;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
	void createIndexs(){
		try {
			if (!this.getDB()) {
				System.out.println("error:getDB");
				return;
			}
			DBCollection coll = db.getCollection("wallUser");
//			coll.createIndex(new BasicDBObject("imei", 1),);
			coll.ensureIndex(new BasicDBObject("imei", 1), "imei", true);
			//coll.ensureIndex(new BasicDBObject("regTime", 1), "regTime", false);
			coll.ensureIndex(new BasicDBObject("state", 1), "userState", false);
			System.out.println("create index:wallUser ok");
			//
			coll = db.getCollection("wallCate");
			coll.ensureIndex(new BasicDBObject("catePre", 1), "catePre", true);
			coll.ensureIndex(new BasicDBObject("state", 1), "cateState", false);
			System.out.println("create index:wallCate ok");
			//
			coll = db.getCollection("wallPic");
			coll.ensureIndex(new BasicDBObject("cate", 1), "picCate", false);
			coll.ensureIndex(new BasicDBObject("picId", 1), "picPicId", false);
			coll.ensureIndex(new BasicDBObject("download", 1), "picDownload", false);
			coll.ensureIndex(new BasicDBObject("stars", 1), "picStars", false);
			
			System.out.println("create index:wallPic ok");
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	void testIndex(){
		if (!this.getDB()) {
			System.out.println("error:getDB");
			return;
		}
		DBCollection coll = db.getCollection("wallPic");
		List<DBObject> list = coll.getIndexInfo();

        for (DBObject o : list) {
            System.out.println(o);
        }
	}
	
	/**
	 * 创建一个类别
	 * @param maxId
	 * @param name
	 * @param pre
	 * @param cn
	 * @param en
	 * @param jp
	 * @param tw
	 * @param sub
	 * @param info
	 * @param style
	 * @param state
	 * @param sortId
	 * @return
	 */
	boolean createCate(int maxId,String name,String pre,String cn,String en,String jp,String tw,String sub,String info,String style,int state,int sortId){
		try {
			if (!this.getDB()) {
				System.out.println("error:getDB");
				return false;
			}
			DBCollection coll = db.getCollection("wallCate");
			coll.remove(new BasicDBObject("catePre",pre));
			BasicDBObject doc = new BasicDBObject();
			doc.put("cateName", name);
			doc.put("catePre", pre);
			doc.put("cn", cn);
			doc.put("en", en);
			doc.put("jp", jp);
			doc.put("tw", tw);
			doc.put("max", maxId);
			doc.put("sub", "");
			doc.put("info", info);
			doc.put("style", style);
			doc.put("state", state);
			doc.put("sortId", sortId);
			
			DBObject time = new BasicDBObject("addTime", new Date());
			doc.putAll(time);
			coll.insert(doc);
			System.out.println("ceate cate ok:"+name);
		} catch (MongoException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 清空类别
	 * @param maxId
	 * @param name
	 * @param pre
	 * @param cn
	 * @param en
	 * @param jp
	 * @param tw
	 * @return
	 */
	boolean clearCate(){
		try {
			if (!this.getDB()) {
				System.out.println("error:getDB");
				return false;
			}
			DBCollection coll = db.getCollection("wallCate");
			coll.drop();
			System.out.println("drop cate ok.");
		} catch (MongoException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	void showAllCate(){
		if (!this.getDB()) {
			System.out.println("error:getDB");
			return;
		}
		DBCollection coll = db.getCollection("wallCate");
		DBCursor cur = coll.find();

        while(cur.hasNext()) {
            System.out.println(cur.next());
        }
		
	}
	
	
	
	public boolean createDB(){
		try {
			this.createWallConfig();
			/*
			this.testConfig();
			//创建类别
			this.clearCate();
			this.createCate(196, "Abstract", "abs", "抽象艺术", "Abstract", "抽象芸術", "抽象藝術","","","",1);
			this.createCate(224, "Space", "space", "宇宙空间", "Space", "宇宙", "宇宙","","","",1);
			this.createCate(24, "WorldCup2010", "fifa", "WorldCup2010 - new", "WorldCup2010 - new", "WorldCup2010 - new", "WorldCup2010 - new","","","",1);
			this.createCate(44, "Food", "food", "美食", "Food", "食品", "美食","","","",1);
			this.createCate(172, "People", "people", "人物", "People", "人", "人物","","","",1);
			this.createCate(196, "Scenic", "sce", "美景", "Scenic", "美しい風景", "美景","","","",1);
			this.createCate(72, "City", "city", "城市", "City", "シティ", "城市","","","",1);
			this.createCate(44, "LOMO", "lomo", "LOMO", "LOMO", "LOMO", "LOMO","","","",1);
			this.createCate(240, "Movie", "mov", "电影", "Movie", "映画", "電影","","","",1);
			this.createCate(132, "Creature", "ani", "动物世界", "Creature", "動物", "動物","","","",1);
			this.createCate(96, "Game", "game", "游戏", "Game", "ゲーム", "遊戲","","","",1);
			this.createCate(208, "Car", "car", "名车", "Car", "リムジン", "名車","","","",1);
			this.createCate(28, "Childhood", "kids", "童年", "Childhood", "小児期", "童年","","","",1);
			this.createCate(36, "Iceland", "iceland", "冰岛", "Iceland", "アイスランド", "冰島","","","",1);
			this.createCate(104, "Still life", "still", "静物", "Still life", "静物", "靜物","","","",1);
			this.createCate(56, "Comic", "comic", "唯美漫画", "Comic", "漫画", "漫畫","","","",1);
			this.createCate(44, "Chinese style", "cn", "中国风", "Chinese style", "中国風", "中國風","","","",1);
			this.createCate(36, "Avatar", "ava", "阿凡达", "Avatar", "Avatar", "Avatar","","","",1);
			this.createCate(68, "Flower", "flower", "精品花卉", "Flower", "ファイン花", "精品花卉","","","",1);
			this.createCate(28, "Easter Day", "easter", "复活节", "Easter Day", "イースター", "復活節","","","",0);
			//显示类别
			//this.showAllCate();
			//创建wallUser
			this.createUser();
			//根据分类创建各个pic
			this.createPicFromCate();
			//创建index
			this.createIndexs();
			//测试indexs，注意wallPic已经有记录了，显示所有index会有延迟
			//this.testIndex();
		*/
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
//	
//	void trans(){
//		try {
////			Mongo mongo1 = new Mongo("202.102.113.204" , 27017 );
////			DB db1 = mongo1.getDB("fwall");
////			boolean auth = db1.authenticate("sikewall009", "6667441".toCharArray());
////			if (auth) {
////				System.out.println("db1 created OK.");
////			}
//			Mongo mongo2 = new Mongo("202.102.40.43" , 27017 );
//			DB db2 = mongo2.getDB("fwall");
//			boolean auth2 = db2.authenticate("sikewall009", "6667441".toCharArray());
//			if (auth2) {
//				System.out.println("db2 created OK.");
//			}
////			DBCollection coll = db1.getCollection("wallPic");
//			DBCollection coll2 = db2.getCollection("wallPic");
//			
//			BasicDBObject pic = new BasicDBObject();
//    		pic.put("picId", 219);//自然数序列号
//    		String pathPre = "/orion/";
//    		String catePre = "car";
//    		BasicBSONList paths = new BasicBSONList();
//    		paths.put("0", pathPre+catePre+"/"+catePre+219+".jpg");
//    		paths.put("1", pathPre+catePre+"/"+catePre+219+"_l.jpg");
//    		paths.put("2", pathPre+catePre+"/b_"+catePre+219+".jpg");
//    		paths.put("3", pathPre+catePre+"/b_"+catePre+219+"_l.jpg");
//    		pic.put("picPath", paths);
//    		pic.put("picSource", "");
//    		pic.put("picName", catePre+"#"+219);//后期可以是真正的名称
////        		DBObject time = new BasicDBObject("addTime", new Date());
////    			pic.putAll(time);
//    		Calendar c = Calendar.getInstance();
//    		c.set(Calendar.YEAR, 2010);
//    		c.set(Calendar.MONTH, 8);
//    		c.set(Calendar.DATE, 8);
//			pic.put("addTime", c.getTime());
//    		pic.put("click", 0);
//    		pic.put("download", 550);
//    		pic.put("setWall", 0);
//    		pic.put("stars", 0);
//    		pic.put("starInfo", new BasicDBList());
//    		pic.put("cate", catePre);
//    		pic.put("info", "");
//    		pic.put("state", 1);//0为未发布状态,1为正常发布状态,2为新发布或其他含义
//    		pic.put("topId", 1);//用于置顶排序等
//    		coll2.save(pic);
//			//先删除
////			coll2.remove(new BasicDBObject("cate","car"));
//			//再加入
////			DBCursor cur1 = coll.find(new BasicDBObject("cate","car"));
////			while (cur1.hasNext()) {
////				DBObject dbobj = (DBObject) cur1.next();
////				
////				coll2.save(dbobj);
////				System.out.println(dbobj);
////				System.out.println("=========");
////			}
//			
//			System.out.println("Done!");
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MongoException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//MongoConfig t = new MongoConfig();
		//t.createCate(36, "Japan Earthquake & Tsunami", "japan", "日本大地震和海啸", "Japan Earthquake & Tsunami", "東日本巨大地震&津波", "日本大地震和海嘯","","","",1,9);
		//t.createPicFromCate("japan2",219);
		
		//t.trans();
		//t.createWallConfig();
		//t.testConfig();
//创建类别
//		t.createCate(192, "Abstract", "abs", "抽象艺术", "Abstract", "抽象芸術", "抽象藝術","","","",1);
//		t.createCate(224, "Space", "space", "宇宙空间", "Space", "宇宙", "宇宙","","","",1);
//		t.createCate(24, "WorldCup2010", "fifa", "WorldCup2010 - new", "WorldCup2010 - new", "WorldCup2010 - new", "WorldCup2010 - new","","","",1);
//		t.createCate(44, "Food", "food", "美食", "Food", "食品", "美食","","","",1);
//		t.createCate(164, "People", "people", "人物", "People", "人", "人物","","","",1);
//		t.createCate(188, "Scenic", "sce", "美景", "Scenic", "美しい風景", "美景","","","",1);
//		t.createCate(72, "City", "city", "城市", "City", "シティ", "城市","","","",1);
//		t.createCate(44, "LOMO", "lomo", "LOMO", "LOMO", "LOMO", "LOMO","","","",1);
//		t.createCate(216, "Movie", "mov", "电影", "Movie", "映画", "電影","","","",1);
//		t.createCate(132, "Creature", "ani", "动物世界", "Creature", "動物", "動物","","","",1);
//		t.createCate(96, "Game", "game", "游戏", "Game", "ゲーム", "遊戲","","","",1);
//		t.createCate(208, "Car", "car", "名车", "Car", "リムジン", "名車","","","",1);
//		t.createCate(28, "Childhood", "kids", "童年", "Childhood", "小児期", "童年","","","",1);
//		t.createCate(36, "Iceland", "iceland", "冰岛", "Iceland", "アイスランド", "冰島","","","",1);
//		t.createCate(104, "Still life", "still", "静物", "Still life", "静物", "靜物","","","",1);
//		t.createCate(52, "Comic", "comic", "唯美漫画", "Comic", "漫画", "漫畫","","","",1);
//		t.createCate(44, "Chinese style", "cn", "中国风", "Chinese style", "中国風", "中國風","","","",1);
//		t.createCate(36, "Avatar", "ava", "阿凡达", "Avatar", "Avatar", "Avatar","","","",1);
//		t.createCate(68, "Flower", "flower", "精品花卉", "Flower", "ファイン花", "精品花卉","","","",1);
//		t.createCate(28, "Easter Day", "easter", "复活节", "Easter Day", "イースター", "復活節","","","",0);
		//显示类别
//		t.showAllCate();
		//创建wallUser
//		t.createUser();
		//根据分类创建各个pic
//		t.createPicFromCate();
		//创建index
//		t.createIndexs();
		//测试indexs，注意wallPic已经有记录了，显示所有index会有延迟
//		t.testIndex();
		
//		t.createPicFromCate("car",208);
		//t.mongo.close();
	}

}
