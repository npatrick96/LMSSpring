package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.lms.entity.Branch;
import com.mysql.jdbc.Statement;


public class BranchDAO extends BaseDAO implements ResultSetExtractor<List<Branch>>{

	public void addBranch(Branch branch) throws SQLException{
		template.update("insert into tbl_library_branch(branchName) values (?)", new Object[] {branch.getBranchName()});
	}
	
	public void addBranchWithAddress(Branch branch) throws SQLException{
		template.update("insert into tbl_library_branch(branchName, branchAddress) values (?, ?)", new Object[] {branch.getBranchName(), branch.getBranchAddress()});
	}
	
	public Integer addBranchWithID(Branch branch) throws SQLException{
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "insert into tbl_library_branch(branchName) values (?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, branch.getBranchName());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}
	
	public void updateBranchName(Branch branch) throws SQLException{
		template.update("update tbl_library_branch set branchName =? where branchId = ?", new Object[] {branch.getBranchName(), branch.getBranchId()});
	}
	
	public void updateBranchAddress(Integer branchId, String branchAddress) throws SQLException{
		template.update("update tbl_library_branch set branchAddress =? where branchId = ?", new Object[] {branchAddress, branchId});
	}
	
	public void updateBranchNameAndAddress(Branch branch) throws SQLException{
		template.update("update tbl_library_branch set branchName =?, branchAddress =? where branchId = ?", new Object[] {branch.getBranchName(), branch.getBranchAddress(),branch.getBranchId()});
	}
	
	public Integer getBranchesCount() throws SQLException{
		return template.queryForObject("select count(*) as COUNT from tbl_library_branch",Integer.class);
	}
	
	public Integer getBranchesCount(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		return template.queryForObject("select count(*) as COUNT from tbl_library_branch where branchName like ?",new Object[]{searchString},Integer.class);
	}
	
	public List<Branch> readAllBranches(Integer pageNo) throws SQLException{
		setPageNo(pageNo);
		return  template.query("select * from tbl_library_branch", this);
	}
	
	public List<Branch> readAllBranchesByName(Integer pageNo, String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		setPageNo(pageNo);
		return (List<Branch>) template.query("select * from tbl_library_branch where branchName like ?", new Object[]{searchString}, this);
	}
	
	public void deleteBranch(Branch branch) throws SQLException{
		template.update("delete from tbl_library_branch where branchId = ?", new Object[] {branch.getBranchId()});
	}
	
	public List<Branch> readAllBranchs() throws SQLException{
		return (List<Branch>) template.query("select * from tbl_library_branch", this);
	}
	
	public List<Branch> readAllBranchsByBranchName(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		return (List<Branch>) template.query("select * from tbl_library_branch where branchName like ?", new Object[]{searchString}, this);
	}
	
	public Branch readBranchByPK(Integer branchId) throws SQLException{
		List<Branch> branchs = (List<Branch>) template.query("select * from tbl_library_branch where branchId = ?", new Object[]{branchId}, this);
		if(branchs!=null){
			return branchs.get(0);
		}
		return null;
	}

	@Override
	public List<Branch> extractData(ResultSet rs) throws SQLException {
		List<Branch> branchs = new ArrayList<>();
		while(rs.next()){
			Branch b = new Branch();
			b.setBranchId(rs.getInt("branchId"));
			b.setBranchName(rs.getString("branchName"));
			b.setBranchAddress(rs.getString("branchAddress"));
			branchs.add(b);
		}
		return branchs;
	}
}
