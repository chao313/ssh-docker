package demo.spring.boot.demospringboot.sdxd.common.utils;

import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class TableBeanUtil {
	
	public Connection getConnection() throws ClassNotFoundException, SQLException{
		
		/*String drivename = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://192.168.3.225:3306/aneshop?useUnicode=true&characterEncoding=utf8";
		String user = "aneshop";
		String password = "aneshop";*/
		
		String drivename = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@101.231.237.70:62000:orcl";
		String user = "aneng";
		String password = "123456";
		
		Properties properties = new Properties();
		properties.put("user", user);
		properties.put("password", password);
		properties.put("remarksReporting", "true");
		
		Class.forName(drivename);
		return DriverManager.getConnection(url,properties);
	}

	public List<ColumnEntry> getTableColumns(String tableName) throws SQLException, ClassNotFoundException{
		String sql = "select * from " + tableName.toUpperCase() +"where 1=2";

		Connection connection = this.getConnection();
//		java.sql.PreparedStatement statement  = connection.prepareStatement(sql);
		DatabaseMetaData databaseMetaData = connection.getMetaData();
//		java.sql.ResultSet rs = ps.executeQuery();
		//获得指定tableName对应的列
		ResultSet rs =databaseMetaData.getColumns(null,null,tableName.toUpperCase(),"%");
		ColumnEntry e = null;
		List<ColumnEntry> colList = new ArrayList<ColumnEntry>();
		while(rs.next()){
			e = new ColumnEntry();
			e.setColumnName(rs.getString("COLUMN_NAME"));
			e.setTypeName(rs.getString("TYPE_NAME"));
			e.setRemarks(rs.getString("REMARKS"));
			colList.add(e);
		}
		return colList;
	}
	public void createJavaFile(String tableName) throws SQLException, ClassNotFoundException{
		StringBuffer java = new StringBuffer("import java.io.Serializable;\n");
		java.append("import com.ane.framework.common.annotation.EntrtyNamespace;\n");
	//	java.append("com.openwork.common.framework.beans.SuperBean;\n");
		StringBuffer m = new StringBuffer();
		StringBuffer str = new StringBuffer("\"{");
		java.append("@EntrtyNamespace(nameSapce = \"\")\n");
		java.append("public class ").append(this.disJavaName(tableName)).append("Entity").append(" implements Serializable").append(" {").append("\n");
		java.append("\tprivate static final long serialVersionUID = 1L;\n");
		List<ColumnEntry> colList = this.getTableColumns(tableName);
		for (ColumnEntry e : colList){
			
		//	System.out.println(e.getTypeName() + "---------" + this.getProType(e.getTypeName()));
			
			
			java.append("\t/**\n").append("\t*").append(e.getRemarks()).append("\n\t*/\n");
			java.append("\t").append("private ").append(this.getProType(e.getTypeName())).append(" ").append(this.disAttributeName(e.getColumnName())).append(";\n");
			
//			m.append("\t/**").append("\n\t*").append("获取").append(e.getRemarks()).append("\n");
//			m.append("\t* @MethodName: ").append("get").append(this.disJavaName(e.getColumnName())).append("\n");
//			m.append("\t* @author：柳发勇 \n").append("\t* @date：").append("").append("\n");
//			m.append("\t* @return ").append(this.getProType(e.getTypeName())).append("\n\t*/\n");
//			m.append("\t").append("public ").append(this.getProType(e.getTypeName())).append(" ").append("get");
//			m.append(this.disJavaName(e.getColumnName())).append("()").append("{\n");
//			m.append("\t\t").append("return ").append(this.disAttributeName(e.getColumnName())).append(";\n").append("\t}\n");
//
//			m.append("\t/**").append("\n\t*").append("设置").append(e.getRemarks()).append("\n");
//			m.append("\t* @MethodName: ").append("set").append(this.disJavaName(e.getColumnName())).append("\n");
//			m.append("\t* @author：柳发勇 \n").append("\t* @date：").append("").append("\n");
//			m.append("\t* @return ").append(this.getProType(e.getTypeName())).append("\n\t*/\n");
//			m.append("\t").append("public void ").append("set");
//			m.append(this.disJavaName(e.getColumnName())).append("(").append(this.getProType(e.getTypeName())).append(" ");
//			m.append("").append(this.disAttributeName(e.getColumnName())).append(")").append("{\n");
//			m.append("\t\t").append("this.").append(this.disAttributeName(e.getColumnName())).append(" = ").append(this.disAttributeName(e.getColumnName()));
//			m.append(";\n").append("\t}\n");
			
			str.append(this.disAttributeName(e.getColumnName())).append(":");
			if(this.getProType(e.getTypeName()) == "int" || "int".equals(this.getProType(e.getTypeName()))){
				str.append("\" + ").append("get").append(this.disJavaName(e.getColumnName())).append("() + \"").append(",");
			}else if(this.getProType(e.getTypeName()) == "java.util.Date" || "java.util.Date".equals(this.getProType(e.getTypeName()))){
				str.append("'\" + com.openwork.common.util.DateUtil.getDateFormat(").append("get").append(this.disJavaName(e.getColumnName())).append("() ,\"yyyy-MM-dd HH:mm:ss\") + \"'").append(",");
			}else{
				str.append("'\" + ").append("get").append(this.disJavaName(e.getColumnName())).append("() + \"'").append(",");
			}
		}
		java.append(m);
		java.append("\t").append("public String toString(){").append("\n\t\treturn ").append(str.substring(0, str.length() -1)).append("}\";").append("\n\t}");
		java.append("\n}");
		System.out.println(java.toString());
	}
	/**
	 * 设置类的名称
	* @MethodName: disJavaName 
	* @description : TODO
	* @author：柳发勇 
	* @date： 2014-4-8 下午5:17:37
	* @param tableName
	* @return String
	 */
	public String disJavaName(String tableName){
		StringBuffer name = new StringBuffer("");
		if(tableName == null || "".equals(tableName))
			return tableName;
		else{
			String[] str = tableName.split("_");
			if(tableName.indexOf("_") != -1){
				for(int i = 0 ; i < str.length ; i++)
					name.append(StringUtils.capitalize(str[i].toLowerCase()));
			}else{
				name.append(StringUtils.capitalize(tableName.toLowerCase()));
			}
		}
		return name.toString();
	}
	/**
	 * 将字段名称转换成类属性名
	* @MethodName: disAttributeName 
	* @description : TODO
	* @author：柳发勇 
	* @date： 2014-4-8 下午5:34:52
	* @param attb
	* @return String
	 */
	public String disAttributeName(String attb){
		StringBuffer name = new StringBuffer("");
		if(attb == null || "".equals(attb))
			return attb;
		else{
			String[] str = attb.split("_");
			name.append(StringUtils.uncapitalize(str[0].toLowerCase()));
			for(int i = 1 ; i < str.length ; i++)
				name.append(StringUtils.capitalize(str[i].toLowerCase()));
		}
		return name.toString();
	}
	/**
	 * 将表字段类型转换成类属性类型
	* @MethodName: getProType 
	* @description : TODO
	* @author：柳发勇 
	* @date： 2014-4-8 下午5:17:04
	* @param columnType
	* @return String
	 */
	public String getProType(String columnType){
		String type = "String";
		if(columnType != null && !"".equals(columnType)){
			if(columnType.indexOf("(") != -1){
				columnType = columnType.substring(0,columnType.indexOf("("));
			}
			if("int".equals(columnType)){
				type = "int";
			}else if("varchar".equals(columnType)){
				type = "String";
			}else if("char".equals(columnType)){
				type = "String";
			}else if("datetime".equals(columnType)){
				type = "java.util.Date";
			}
		}
		return type;
	}
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		TableBeanUtil t = new TableBeanUtil();
		
		t.createJavaFile("TAB_sign");
	}
	
}
