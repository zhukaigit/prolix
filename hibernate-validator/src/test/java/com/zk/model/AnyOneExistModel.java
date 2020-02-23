package com.zk.model;

import com.zk.annotation.AnyoneExist;
import com.zk.annotation.FieldGroup;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@AnyoneExist
public class AnyOneExistModel {

    @FieldGroup("a")
    private String a1 = "";
    @FieldGroup("a")
    private String a2;
    @FieldGroup("a")
    private String a3;
    @FieldGroup("b")
    private String b1 = "1";
    @FieldGroup("b")
    private String b2 = "2";

    @FieldGroup("c")
    private String c1;
    @FieldGroup("c")
    private String c2;

    @NotEmpty
    private String c = "";


}
