package com.johnsoft.library.swing.component.filetree;

public class JohnFileTreeConstants
{
	public enum FilterOption{
		FOLDER_BOOLEAN,EXECUTE_BOOLEAN,
		HIDDEN_BOOLEAN,READONLY_BOOLEAN,
		SIZE_LONG,SIZE_OPERATE_INTEGER,
		DATE_LONG,DATE_OPERATE_INTEGER,
		NAME_STRING,NAME_OPERATE_INTEGER,
		CONTENT_STRING,CASCADE_BOOLEAN;
	}
	
	public static final int REFRESH_ALL_TREE=587160165;
	public static final int REFRESH_SELECTED_NODES=1893966094;
	
	public static final int BROWSE_WITH_NOTEPAD=1277366407;
	public static final int BROWSE_WITH_DEFAULT=1714301940;
	
	public static final int ORDER_BY_NAME=404235056;
	public static final int ORDER_BY_TYPE=637319831;
	public static final int ORDER_BY_DATE=565026280;
	public static final int ORDER_BY_SIZE=702006687;
	
	public static final int ADD_FILE=1688307337;
	public static final int ADD_FOLDER=1680639528;
	
	public static final int DATE_OPERATE_GREATER=1;
	public static final int DATE_OPERATE_LESS=-1;
	public static final int DATE_OPERATE_EQUAL=0;
	public static final int SIZE_OPERATE_GREATER=1;
	public static final int SIZE_OPERATE_LESS=-1;
	public static final int SIZE_OPERATE_EQUAL=0;
	public static final int NAME_OPERATE_PREFIX=-1;
	public static final int NAME_OPERATE_SUFFIX=1;
	public static final int NAME_OPERATE_EQUAL=0;
	public static final int NAME_OPERATE_CONTAIN=-9;
	public static final int NAME_OPERATE_EXPAND=9;
}
