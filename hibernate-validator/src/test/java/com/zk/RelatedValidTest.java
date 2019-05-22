package com.zk;

import com.zk.model.RelatedModel;
import com.zk.utils.ValidatorUtil;
import org.junit.Test;

public class RelatedValidTest extends ValidateBaseTest {

    @Test
    public void testError () {
        RelatedModel model = new RelatedModel();
        model.setCategory("影像");
        result = ValidatorUtil.validate(model);
    }

    @Test
    public void testTrue () {
        RelatedModel model = new RelatedModel();
        model.setCategory("单证");
        result = ValidatorUtil.validate(model);
    }
}
