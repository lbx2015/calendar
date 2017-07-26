package net.riking.web.controller;

import net.riking.core.entity.Resp;
import net.riking.service.repo.impl.BusinessDayRepoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bing.xun on 2017/5/18.
 */
@RestController
@RequestMapping(value = "/businessDay")
public class BusinessDayController {

    @Autowired
    BusinessDayRepoImpl businessDayRepo;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Resp getBusinessDay(@RequestParam Date date,@RequestParam Integer count) throws Exception {
        return new Resp(businessDayRepo.getBusinessDayList(date,count));
    }
}
