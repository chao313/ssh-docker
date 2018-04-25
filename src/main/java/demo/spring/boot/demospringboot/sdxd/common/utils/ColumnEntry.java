package demo.spring.boot.demospringboot.sdxd.common.utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 类的描述
 * 读取数据库之后，记录表中每一元组每一字段的信息
 */
public class ColumnEntry implements Comparable<ColumnEntry> {
	
	private String id;
	
	//列名 大写
	private String columnName;
	
	//属性名 驼峰
	private String propertyName;
	
	//注释
	private String remarks;
	
	//数据类型
	private String typeName;
	
	//数据长度
	private String columnSize;
	
	//是否为空
	private String isNullable;
	
	//是否主键
	private boolean isPrimary = false;
	
	//主键列名
	private String primaryKeyName;
	
	//是否生成
	private boolean isCreate = true;
	
	//小数位
	private Integer digits = 0;
	
	@XmlTransient
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement
	public String getColumnName() {
		return columnName;
	}
	
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	@XmlElement
	public String getTypeName() {
		return typeName;
	}
	
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	@XmlElement
	public String getColumnSize() {
		return columnSize;
	}
	
	public void setColumnSize(String columnSize) {
		this.columnSize = columnSize;
	}
	
	@XmlElement
	public String getIsNullable() {
		return isNullable;
	}
	
	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}
	
	@XmlElement
	public boolean getIsPrimary() {
		return isPrimary;
	}
	
	public void setIsPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}
	
	@XmlElement
	public String getPrimaryKeyName() {
		return primaryKeyName;
	}
	
	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}
	
	@XmlElement
	public String getPropertyName() {
		return propertyName;
	}
	
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public int compareTo(ColumnEntry o) {
//		return this.isPrimary.compareTo(o.isPrimary);
		return 0;
	}

	@XmlElement
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@XmlElement
	public boolean getIsCreate() {
		return isCreate;
	}

	public void setIsCreate(boolean isCreate) {
		this.isCreate = isCreate;
	}

	@XmlElement
	public Integer getDigits() {
		return digits;
	}

	public void setDigits(Integer digits) {
		this.digits = digits;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnEntry other = (ColumnEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
