<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" import="com.k99k.app.orion.*,com.mongodb.*,org.bson.types.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>FWS - addNewPicsTask</title>
</head>
<body>
<%
String oid = "";
String re = "task id error.";
if(request.getParameter("oid") != null && request.getParameter("oid").length()>5){
	oid = request.getParameter("oid").trim();
	DBCollection coll = FWall.getMongoCol().getColl("wallTask");
	DBCursor cur = coll.find(new BasicDBObject("_id",new ObjectId(oid)));
	if(cur.hasNext()){
		int state  = (Integer)((DBObject)cur.next()).get("state");
		switch(state){
		case 1:
			re = "Task is ready or doing now.";
			break;
		case 2:
			re = "Task is done! [ <a href='/orion/initfwall.jsp'>ReBuild index</a> ]";
			break;
		case 3:
			re = "Task is failed!";
			break;
		default:
			break;
		}
		
	}
}
out.print(re);
%>
</body>
</html>