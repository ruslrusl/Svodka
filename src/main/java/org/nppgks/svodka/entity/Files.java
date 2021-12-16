package org.nppgks.svodka.entity;

import lombok.Getter;
import lombok.Setter;

public class Files {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String file;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String descr;
}
