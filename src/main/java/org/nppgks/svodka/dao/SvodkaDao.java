package org.nppgks.svodka.dao;

import org.nppgks.svodka.entity.Config;
import org.nppgks.svodka.entity.Files;
import org.nppgks.svodka.entity.Tags;

import java.util.List;

public interface SvodkaDao {

    List<Files> getDownloadFiles();

    List<Tags> getDownloadTags();

    boolean insertTagDatas(String json);

    List<Tags> getTagsForUi(int type);

    List<Config> getConfigsForUi(int type);

    String getDatasForUi(int type, String period, String ids);
}
