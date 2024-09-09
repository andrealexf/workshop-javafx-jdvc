package model.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.entites.Department;

public class DepartmentService implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public List<Department> findAll(){
		
		List<Department> list = new ArrayList<>();
		list.add(new Department(1, "Books"));
		list.add(new Department(2, "Computers"));
		list.add(new Department(3, "Electronics"));
		return list;
	}

}
