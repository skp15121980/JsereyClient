package com.dellas.app.dao;

import com.dellas.app.dto.TaskDTO;
import com.dellas.app.model.Task;

public interface TaskDao {
	Task save(Task task);
}
