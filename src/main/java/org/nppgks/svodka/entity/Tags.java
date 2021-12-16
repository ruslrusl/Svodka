package org.nppgks.svodka.entity;

import lombok.Getter;
import lombok.Setter;

public class Tags {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String descr;

    @Getter
    @Setter
    private int idFiles;

    @Getter
    @Setter
    private String data;

    @Getter
    @Setter
    private Boolean isDefault;

}
