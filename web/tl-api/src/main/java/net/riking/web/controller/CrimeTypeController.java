package net.riking.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.riking.core.entity.EnumCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.core.log.InFunLog;
import net.riking.core.log.InModLog;
import net.riking.entity.PageQuery;
import net.riking.entity.model.CrimeType;
import net.riking.service.repo.CrimeTypeRepo;

/**
 * Created by bing.xun on 2017/5/24.
 */
@InModLog(modName = "涉罪类型")
@RestController
@RequestMapping(value = "/crimeType")
public class CrimeTypeController {

    @Autowired
    CrimeTypeRepo crimeTypeRepo;
    
    
    @InFunLog(funName = "添加或者更新涉罪类型",args = { 0 })
    @ApiOperation(value = "添加或者更新涉罪类型", notes = "POST-@CrimeType")
    @RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
    public Resp add_(@RequestBody CrimeType crimeType) {
        CrimeType ct = crimeTypeRepo.save(crimeType);
        return new Resp(ct, CodeDef.SUCCESS);
    }

    @InFunLog(funName = "删除涉罪类型",argNames = { "ID" })
    @ApiOperation(value = "删除涉罪类型", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(name = "id", value = "员工ID", required = true, dataType = "Long")
    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public Resp del_(@RequestParam("id") Long id) {
        crimeTypeRepo.delete(id);
        return new Resp(1);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Resp get_(@RequestParam("id") Long id) {
        CrimeType crimeType = crimeTypeRepo.findOne(id);
        return new Resp(crimeType, CodeDef.SUCCESS);
    }
    
    @InFunLog(funName = "删除", args = { 0 }, argNames = { "ID" })
    @RequestMapping(value = "/delMore", method = RequestMethod.POST)
    public Resp delMore(@RequestBody Set<Long> ids) {
        Integer count = 0;
        count = crimeTypeRepo.deleteByIds(ids);
        return new Resp(count);
    }
    
    @RequestMapping(value = "/getMore", method = RequestMethod.GET)
    public Resp getMore(@ModelAttribute PageQuery query, @ModelAttribute CrimeType ct) {
        PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
        Example<CrimeType> example = Example.of(ct, ExampleMatcher.matchingAll());
        Page<CrimeType> page = crimeTypeRepo.findAll(example, pageable);
        return new Resp(page);
    }

    @RequestMapping(value = "/getAllCode", method = RequestMethod.GET)
    public Resp getAllCode(@RequestParam(value = "prop", required = false) String prop){
        List<CrimeType> list = crimeTypeRepo.findAll();
        List<EnumCustom> enumKeyValues = new ArrayList<EnumCustom>();
        EnumCustom enumCustom;
        for(CrimeType ct : list){
            enumCustom = new EnumCustom();
            enumCustom.setKey(ct.getSzkyjyxwdm());
            enumCustom.setValue(ct.getSzkyjyxwdm()+"-"+ct.getSzkyjyxw());
            // 为了区分多个枚举，此字段为框架自动传入
            enumCustom.setProp(prop);
            enumKeyValues.add(enumCustom);
        }
        return new Resp(enumKeyValues);
    }
}
