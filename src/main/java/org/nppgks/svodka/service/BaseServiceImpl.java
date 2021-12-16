package org.nppgks.svodka.service;

import org.nppgks.svodka.dao.SvodkaDao;
import org.nppgks.svodka.entity.Config;
import org.nppgks.svodka.entity.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseServiceImpl implements BaseService{


    @Autowired
    private SvodkaDao svodkaDAO;

    @Override
    public List<Tags> getTagsForUi(int type) {
        return svodkaDAO.getTagsForUi(type);
    }

    @Override
    public List<Config> getConfigForUi(int type) {
        return svodkaDAO.getConfigsForUi(type);
    }

    @Override
    public String getDatasForUi(int type, String period, String ids) {
        return svodkaDAO.getDatasForUi(type, period, ids);
    }
}
