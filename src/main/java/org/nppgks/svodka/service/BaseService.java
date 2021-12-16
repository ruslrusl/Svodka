package org.nppgks.svodka.service;

import org.nppgks.svodka.entity.Config;
import org.nppgks.svodka.entity.Tags;

import java.util.List;

public interface BaseService {

    List<Tags> getTagsForUi(int type);

    List<Config> getConfigForUi(int type);

    String getDatasForUi(int type, String period, String ids);
}
