package com.dellas.app.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.dellas.app.dao.TaskDao;
import com.dellas.app.dto.TaskDTO;
import com.dellas.app.model.Task;

public class TaskDaoImpl implements TaskDao{
 @Autowired
 JdbcTemplate jdbcTemplate;
	@Override
	public Task save(Task task) {
		 String insertSql="insert into student values(?,?,?)";
		 jdbcTemplate.update(insertSql,new Object[]{"HIBERNATE_SEQUENCE.NEXTVAL"+task.getCreationTime(),task.getName(),task.getStartDate()});
		return task;
	}

}
