package org.nppgks.svodka.service;

import lombok.extern.slf4j.Slf4j;
import org.nppgks.svodka.dao.SvodkaDao;
import org.nppgks.svodka.entity.Files;
import org.nppgks.svodka.entity.Tags;
import org.nppgks.svodka.util.Constant;
import org.nppgks.svodka.util.RemoteFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FileServiceImpl implements FileService{

    @Autowired
    private SvodkaDao svodkaDAO;

    @Override
    public void setDataTags() {
        log.info("Начало процедуры вставки данных");
        RemoteFile remoteFile = new RemoteFile();
        List<Files> filesList = svodkaDAO.getDownloadFiles();
        if (filesList!=null && !filesList.isEmpty()) {
            List<Tags> tagsList = svodkaDAO.getDownloadTags();
            if (tagsList!=null && !tagsList.isEmpty()) {
                //получение данных
                for(Files file:filesList) {
                    Map<String, String> mapDatas = remoteFile.getFileContent(file.getFile(), file.getUsername(), file.getPassword());
                    tagsList.stream()
                            .filter(t->t.getIdFiles()== file.getId())
                            .forEach(t->{
                                String value = mapDatas.get(t.getName());
                                if (value!=null) {
                                    t.setData(value);
                                }
                            });
                }
                //вставка данных в БД
                String json = new Gson().toJson(tagsList);
                log.info("Вставка данных в БД: "+json+"");
                if (svodkaDAO.insertTagDatas(json)) {
                    log.info("Вставка произошла успешно");
                } else {
                    log.error("Ошибка при вставке данных "+json+"");
                }
            } else {
                log.warn("Теги для встаки в БД отсутсвуют");
            }
        } else {
            log.warn("Файлы для загрузки отсутсвуют");
        }
        log.info("Окончание процедуры вставки данных");
    }
}
