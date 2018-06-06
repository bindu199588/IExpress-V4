package mvc.spring.dao.impl;

import org.springframework.stereotype.Repository;

import mvc.spring.dao.AbstractDao;
import mvc.spring.dao.XpressionDao;
import mvc.spring.model.Xpression;

@Repository("xpressionDao")
public class XpressionDaoImpl extends AbstractDao implements XpressionDao {

	@Override
	public void saveXpression(Xpression xpression) {
		getSession().save(xpression);
	}

}
