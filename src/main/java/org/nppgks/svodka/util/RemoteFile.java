package org.nppgks.svodka.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class RemoteFile {

    public Map<String, String> getFileContent(String remoteFilePath, String username, String password) {
        Map<String, String> tagDatas = new HashMap<>();
        Scanner sc;
        try {
            URL url = new URL(remoteFilePath);
            URLConnection uc = url.openConnection();
            String userpass = username + ":" + password;
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            uc.setRequestProperty("Authorization", basicAuth);
            uc.setConnectTimeout(Constant.URL_CONN_TIME);
            InputStream in2 = uc.getInputStream();
            sc = new Scanner(in2);
        } catch (NullPointerException | IOException e) {
//            log.error("Ошибка при открытии файла из удаленной директории [" + remoteFilePath + "]", e);
            log.error("Ошибка при открытии файла из удаленной директории [" + remoteFilePath + "]");
            try {
                File file = new File(remoteFilePath);
                sc = new Scanner(file);
            } catch (NullPointerException | IOException ex) {
                log.error("Ошибка при открытии файла из локальной директории [" + remoteFilePath + "]", e);
                sc = null;
            }
        }

        if (sc != null) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.contains(Constant.FILE_TAG_DELIMETER)) {
                    String[] split = line.split(Constant.FILE_TAG_DELIMETER);
                    if (split != null && split.length > 1) {
                        if (split[0] != null && split[1] != null) {
                            tagDatas.put(split[0].trim(), split[1].trim());
                        }
                    }
                }
            }
        }
        return tagDatas;
    }
}
