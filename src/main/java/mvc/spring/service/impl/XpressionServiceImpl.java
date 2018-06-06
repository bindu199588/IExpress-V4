package mvc.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mvc.spring.dao.XpressionDao;
import mvc.spring.model.Xpression;
import mvc.spring.service.XpressionService;

@Service("xpressionService")
@Transactional
public class XpressionServiceImpl implements XpressionService {

	@Autowired 
	private XpressionDao dao;

	@Override
	public void saveXpression(Xpression xpression) {
		dao.saveXpression(xpression);
	}
}
