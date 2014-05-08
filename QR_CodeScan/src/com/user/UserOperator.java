package com.user;

import com.sqlite3.db.DatabaseHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserOperator {
private String uasno;
private String uapass;
private Context context;

public UserOperator(Context context) {
	super();
	this.context = context;
}
public Context getContext() {
	return context;
}
public void setContext(Context context) {
	this.context = context;
}
public String getUasno() {
	return uasno;
}
public void setUasno(String uasno) {
	this.uasno = uasno;
}
public String getUapass() {
	return uapass;
}
public void setUapass(String uapass) {
	this.uapass = uapass;
}

public boolean CreateDatabase(){
	try {
		//创建一个DatabaseHelper对象
		DatabaseHelper dbHelper = new DatabaseHelper(this.getContext(),"a_qra_db");
		//只有调用了DatabaseHelper对象的getReadableDatabase()方法，或者是getWritableDatabase()方法之后，才会创建，或打开一个数据库
		dbHelper.getReadableDatabase();
		return true;
	} catch (Exception e) {
		return false;
	}
}

public boolean UpdateDatabase(int version){
	try {
		DatabaseHelper dbHelper = new DatabaseHelper(this.getContext(),"a_qra_db",version);
		dbHelper.getReadableDatabase();
		return true;
	} catch (Exception e) {
		return false;
	}
}
public boolean Insert(int version,String uasno,String uapass){
	try {
		ContentValues values = new ContentValues();
		values.put("id", 1);
		values.put("uid",0);
		values.put("uasno",uasno);
		values.put("uapass",uapass);
		
		DatabaseHelper dbHelper = new DatabaseHelper(this.getContext(),"a_qra_db",version);
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		db.insert("local_user", null, values);
		return true;
	} catch (Exception e) {
		return false;
	}
}

public boolean InsertOnly(int version,String uasno,String uapass){
	try {
		ContentValues values = new ContentValues();
		values.put("id", 1);
		values.put("uid",0);
		values.put("uasno",uasno);
		values.put("uapass",uapass);
		
		DatabaseHelper dbHelper = new DatabaseHelper(this.getContext(),"a_qra_db",version);
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		db.delete("local_user", "id=?", new String[]{"1"});
		db.insert("local_user", null, values);
		return true;
	} catch (Exception e) {
		return false;
	}
}
public boolean Update(int version,int id,String uasno,String uapass){
	try {
		ContentValues values = new ContentValues();
		values.put("uid",0);
		values.put("uasno",uasno);
		values.put("uapass",uapass);
		DatabaseHelper dbHelper = new DatabaseHelper(this.getContext(),"a_qra_db",version);
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		db.update("local_user", values, "id=?", new String[]{"1"});//XXX
		return true;
	} catch (Exception e) {
		return false;
	}
}

public UserOperator Query(int id){
	try {
		DatabaseHelper dbHelper = new DatabaseHelper(this.getContext(),"a_qra_db");
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor cursor = db.query("local_user", new String[]{"id","uid","uasno","uapass"}, "id=?", new String[]{"1"}, null, null, null);
		while(cursor.moveToNext()){
			String sno = cursor.getString(cursor.getColumnIndex("uasno"));
			String pass = cursor.getString(cursor.getColumnIndex("uapass"));
			UserOperator user=new UserOperator(context);
			user.setUasno(sno);
			user.setUapass(pass);
			return user;
		}
		return null;
	} catch (Exception e) {
		return null;
	}
}
}
