/*
 * Created on 16 nov. 2013 ( Time 09:59:22 )
 * Generated by Telosys Tools Generator ( version 2.0.6 )
 */
package com.pourbaix.script.creature.dao.impl;


import com.pourbaix.script.creature.model.Trigger ;
import com.pourbaix.script.creature.dao.TriggerDao;

import org.springframework.stereotype.Repository;

/**
 * JPA implementation for basic persistence operations ( entity "Trigger" )
 * 
 * @author Telosys Tools Generator
 *
 */
@Repository
public class TriggerDaoImpl extends AbstractDaoImpl<Trigger, String> implements TriggerDao {

    protected TriggerDaoImpl() {
        super(Trigger.class);
    }
    
}
