package model.services;

import java.io.Serializable;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){
		
		return dao.findAll();
	}
	
	public void saveOrUpdate(Department obj) {
		
		if(obj.getId() == null) {
			
			dao.insert(obj);
		} else {
			
			dao.update(obj);
		}
	}

}
