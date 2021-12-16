package org.nppgks.svodka.dao;

import lombok.extern.slf4j.Slf4j;
import org.nppgks.svodka.entity.Config;
import org.nppgks.svodka.entity.Files;
import org.nppgks.svodka.entity.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class SvodkaDaoImpl implements SvodkaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<Files> getDownloadFiles() {
        return callFunction(Files.class, "download$selectfiles()", null);
    }

    @Override
    public List<Tags> getDownloadTags() {
        return callFunction(Tags.class, "download$selecttags()", null);
    }

    @Override
    public List<Tags> getTagsForUi(int type) {
        Object[] inParamArr = {type};
        return callFunction(Tags.class, "ui$selecttags(?)", inParamArr);
    }

    @Override
    public List<Config> getConfigsForUi(int type) {
        Object[] inParamArr = {type};
        return callFunction(Config.class, "ui$getconfig(?)", inParamArr);
    }

    @Override
    public String getDatasForUi(int type, String period, String ids) {
        Object[] inParamArr = {type, period, ids};
        List<String> stringList = callFunction(String.class, "ui$getdataandconfiggraph(?,?,?)", inParamArr);
        if (stringList != null && stringList.size() > 0) {
            return stringList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean insertTagDatas(String json) {
        Object[] inParamArr = {json};
        return callFunctionForUpdate(String.class, "download$insertdatatags(?)", inParamArr);
    }

    private <T> List<T> callFunction(Class<T> tClass, String procedureName, Object[] inParamArr) {
        List<T> list = new ArrayList<>();
        int j = 2;
        try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {
            conn.setAutoCommit(false);
            CallableStatement proc = conn.prepareCall("{? = call " + procedureName + " }");
            if (inParamArr != null) {
                for (int i = 0; i < inParamArr.length; i++) {
                    if (inParamArr[i] instanceof String) {
                        proc.setString(j, (String) inParamArr[i]);
                        j++;
                    } else if (inParamArr[i] instanceof Integer) {
                        proc.setInt(j, (Integer) inParamArr[i]);
                        j++;
                    } else if (inParamArr[i] instanceof List) {
                        List inParamlist = (List) inParamArr[i];
                        String arrType = "text";
                        if (inParamlist.get(0) instanceof Integer) {
                            arrType = "integer";
                        }
                        final java.sql.Array sqlArray = conn.createArrayOf(arrType, ((List) inParamArr[i]).toArray());
                        proc.setArray(j, sqlArray);
                        j++;
                    }
                }
            }
            proc.registerOutParameter(1, Types.OTHER);
            proc.execute();
            ResultSet results = (ResultSet) proc.getObject(1);
            while (results.next()) {
                if (tClass.isAssignableFrom(String.class)) {
                    String result = results.getString(1);
                    list.add(tClass.cast(result));
                } else if (tClass.isAssignableFrom(Boolean.class)) {
                    Boolean result = results.getBoolean(1);
                    list.add(tClass.cast(result));
                } else if (tClass.isAssignableFrom(Files.class)) {
                    Files obj = new Files();
                    obj.setId(results.getInt(1));
                    obj.setFile(results.getString(2));
                    obj.setUsername(results.getString(3));
                    obj.setPassword(results.getString(4));
                    obj.setDescr(results.getString(5));
                    list.add(tClass.cast(obj));
                } else if (tClass.isAssignableFrom(Tags.class)) {
                    Tags obj = new Tags();
                    obj.setId(results.getInt(1));
                    obj.setName(results.getString(2));
                    obj.setDescr(results.getString(3));
                    obj.setIdFiles(results.getInt(4));
                    obj.setIsDefault(results.getBoolean(5));
                    list.add(tClass.cast(obj));
                } else if (tClass.isAssignableFrom(Config.class)) {
                    Config obj = new Config();
                    obj.setId(results.getInt(1));
                    obj.setName(results.getString(2));
                    obj.setValue(results.getString(3));
                    obj.setDescr(results.getString(4));
                    list.add(tClass.cast(obj));
                }
            }
            results.close();
            proc.close();
        } catch (SQLException ex) {
            log.error("Ошибка при выполнении функции " + procedureName, ex);
        }
        return list;
    }

    private <T> boolean callFunctionForUpdate(Class<T> tClass, String procedureName, Object[] inParamArr) {
        int j = 2;
        boolean result = false;
        try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {
            conn.setAutoCommit(true);
            CallableStatement proc = conn.prepareCall("{? = call " + procedureName + " }");
            if (inParamArr != null) {
                for (int i = 0; i < inParamArr.length; i++) {
                    if (inParamArr[i] instanceof String) {
                        proc.setString(j, (String) inParamArr[i]);
                        j++;
                    } else if (inParamArr[i] instanceof Integer) {
                        proc.setInt(j, (Integer) inParamArr[i]);
                        j++;
                    } else if (inParamArr[i] instanceof List) {
                        List inParamlist = (List) inParamArr[i];
                        String arrType = "text";
                        if (inParamlist.get(0) instanceof Integer) {
                            arrType = "integer";
                        } else if (inParamlist.get(0) instanceof Double) {
                            arrType = "numeric";
                        }
                        final java.sql.Array sqlArray = conn.createArrayOf(arrType, ((List) inParamArr[i]).toArray());
                        proc.setArray(j, sqlArray);
                        j++;
                    }
                }
            }
            proc.registerOutParameter(1, Types.BOOLEAN);
            proc.execute();
            result = (Boolean) proc.getObject(1);
            proc.close();
        } catch (SQLException ex) {
            log.error("Ошибка при выполнении функции " + procedureName, ex);
        }
        return result;
    }
}
