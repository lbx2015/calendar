package net.riking.service;

import net.riking.core.entity.EnumCustom;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.service.repo.ModelPropdictRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bing.xun on 2017/5/13.
 */
@Service("modelPropdictRepoService")
public class ModelPropdictRepoService {
    @Autowired
    private ModelPropdictRepo modelPropdictRepo;
    public List<EnumCustom> getEnumCustomInModelPropdict(String tableName, String filed, String prop){
        Set<String> set = new HashSet<String>();
        List<EnumCustom> enumKeyValues = new ArrayList<EnumCustom>();
        set.add(filed);
        List<ModelPropDict> list = modelPropdictRepo.getDatas(tableName, set);
        for (ModelPropDict dict : list) {
            EnumCustom enumCustom = new EnumCustom();
            enumCustom.setKey(dict.getKe());
            enumCustom.setValue(dict.getValu());
            enumCustom.setProp(prop);
            enumKeyValues.add(enumCustom);
        }
        return enumKeyValues;
    }
}
