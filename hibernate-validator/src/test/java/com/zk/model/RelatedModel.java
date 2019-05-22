package com.zk.model;

import com.zk.annotation.RelatedValidate;
import com.zk.annotation.sub.IsNullOnConditionFieldValue;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor (access = AccessLevel.PUBLIC)
@RelatedValidate
public class RelatedModel {

    private String category;

    // category为单证时，可为空
    @IsNullOnConditionFieldValue(fieldName = "category", fieldValue = "单证")
    private String lossType;

}
