package com.hzm.lczs.db;

public class MyDbInfo {
	
	private static String TableNames[] = {
		"TBL_EXPENDITURE_CATEGORY",       ////消费类别（一级）目录名称表
		"TBL_EXPENDITURE_SUB_CATEGORY",  //消费子类别（二级）目录名称表
		"TBL_INCOME_CATEGORY",           //收入类别（一级）目录名称表
		"TBL_INCOME_SUB_CATEGORY",       //收入子类别（二级）目录名称表
		"TBL_ACCOUNT_TYPE",
		"TBL_ACCOUNT_SUB_TYPE",
		"TBL_ACCOUNT",
		"TBL_STORE",
		"TBL_ITEM",
		"TBL_EXPENDITURE",          // //消费详情表 （记一笔 收入）
		"TBL_INCOME",              //收入详情表 （记一笔 收入）
		"TBL_TRANSFER",//转账
		"TBL_BANK_TYPE",  //银行名称表
		"TBL_BANK_CARD_INFO",  //银行卡信息表
		"TBL_BANK_CARD_TYPE",   //银行卡种类
		"TBL_BANK_BACK_DATE_TYPE" //还款日类型
	};//表名
	

	private static String FieldNames[][] = {
			{"ID","NAME","BUDGET"},
			{"ID","NAME","PARENT_CATEGORY_ID"},
			{"ID","NAME"},
			{"ID","NAME","PARENT_CATEGORY_ID"},
			{"ID","NAME","POSTIVE"},
			{"ID","NAME","PARENT_TYPE_ID"},
			{"ID","NAME","TYPE_ID","SUB_TYPE_ID","ACCOUNT_BALANCE"},
			{"ID","NAME"},
			{"ID","NAME"},
			{"ID", "AMOUNT", "EXPENDITURE_CATEGORY_ID", "EXPENDITURE_SUB_CATEGORY_ID", "ACCOUNT_ID", "STORE_ID", "ITEM_ID", "DATE", "MEMO"},
			{"ID", "AMOUNT", "INCOME_CATEGORY_ID", "INCOME_SUB_CATEGORY_ID", "ACCOUNT_ID", "ITEM_ID", "DATE", "MEMO"},
			{"ID", "AMOUNT", "ACCOUNT_ID", "ITEM_ID", "DATE", "MEMO"},
			{"ID","NAME","SHORTNAME"},
			{"ID","CARD_NUM","BANK_ID","CARD_TYPE","BILL_DATE","REPAYMENT_TYPE","REPAYMENT_DATE","CREDIT_LIMIT","REMARK","ALARM_ID"},
			{"ID","NAME"},
			{"ID","NAME"}
	};//字段名
	
	private static String FieldTypes[][] = {
			{"INTEGER PRIMARY KEY AUTOINCREMENT","text","DOUBLE"},
			{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","INTEGER"},
			{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT"},
			{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","INTEGER"},
			{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","INTEGER","DOUBLE"},
			{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","INTEGER"},
			{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","INTEGER","INTEGER","DOUBLE"},
			{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT"},
			{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT"},
			{"INTEGER PRIMARY KEY AUTOINCREMENT","DOUBLE","INTEGER","INTEGER","INTEGER","INTEGER","INTEGER","TEXT","TEXT"},
			{"INTEGER PRIMARY KEY AUTOINCREMENT","DOUBLE","INTEGER","INTEGER","INTEGER","INTEGER","TEXT","TEXT"},
			{"INTEGER PRIMARY KEY AUTOINCREMENT","DOUBLE","INTEGER","INTEGER","TEXT","TEXT"},
			{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","TEXT"},
			{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","INTEGER","INTEGER","INTEGER","INTEGER","INTEGER","DOUBLE","TEXT","INTEGER"},
			{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT"},
			{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT"}
	};//字段类型
	
	public MyDbInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public static String[] getTableNames() {
		return TableNames;
	}
	
	public static String[][] getFieldNames() {
		return FieldNames;
	}
	
	public static String[][] getFieldTypes() {
		return FieldTypes;
	}
	
}
