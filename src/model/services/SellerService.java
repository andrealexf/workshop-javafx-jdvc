package model.services;

import java.io.Serializable;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private SellerDao dao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll(){
		
		return dao.findAll();
	}
	
	public void saveOrUpdate(Seller obj) {
		
		if(obj.getId() == null) {
			
			dao.insert(obj);
		} else {
			
			dao.update(obj);
		}
	}
	
	public void remove(Seller obj) {
		
		dao.deleteById(obj.getId());
	}
	
	

}
