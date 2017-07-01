package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class BaseDAO { // TEST
	
	//Constructor 
	//@Qualifier(value="Oracle") would get the oracle configuration template 
	//because we set the name there as @Bean(name="Oracle")
	@Autowired
	@Qualifier(value="MySQL")
	JdbcTemplate template; // JdbcTemplate MySQL is also another way of specifying.

	private Integer pageNo = 0;
	private Integer pageSize = 10;

	public abstract List<?> extractData(ResultSet rs) throws SQLException;

	/**
	 * @return the pageNo
	 */
	public Integer getPageNo() {
		return pageNo;
	}

	/**
	 * @param pageNo
	 *            the pageNo to set
	 */
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
