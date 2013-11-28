package com.pourbaix.creature.editor.dao.impl;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.pourbaix.creature.editor.dao.CategoryDao;
import com.pourbaix.creature.editor.domain.Category;

@Repository
public class CategoryDaoImpl extends AbstractDaoImpl<Category, Integer> implements CategoryDao {

    protected CategoryDaoImpl() {
        super(Category.class);
    }

    @SuppressWarnings("unchecked")
	public List<Category> getList() {
    	List<Category> result = getCurrentSession().createCriteria(Category.class)
    	.setFetchMode("parent", FetchMode.JOIN)
        .setCacheable(true)
        .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
        .addOrder(Order.asc("name"))
        .list();
    	//Query query = getCurrentSession().createQuery("select c from Category c join fetch c.children where c.parent is null");
    	return result;
    }
    
    public List<Category> findCategories(String name) {
        return findByCriteria(Restrictions.like("name", name, MatchMode.START));
    }
    
}
