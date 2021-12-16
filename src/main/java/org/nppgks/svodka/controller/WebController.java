package org.nppgks.svodka.controller;

import lombok.extern.slf4j.Slf4j;
import org.nppgks.svodka.entity.Config;
import org.nppgks.svodka.entity.Tags;
import org.nppgks.svodka.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
public class WebController {

    @Autowired
    private BaseService baseService;

    @GetMapping("/api/gettagsui")
    public List<Tags> getTagsForUi(@RequestParam int type) {
        log.info("getTagsForUi: type = " + type);
        return baseService.getTagsForUi(type);
    }

    @GetMapping("/api/getconfigui")
    public List<Config> getConfigsForUi(@RequestParam int type) {
        log.info("getConfigsForUi: type = " + type);
        return baseService.getConfigForUi(type);
    }

//    @GetMapping("/api/getdata",  produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/api/getdata", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDatasForUi(@RequestParam int type, @RequestParam String period, @RequestParam String ids) {
        log.info("getDatasForUi: type = " + type + ", period = " + period + ", ids = " + ids);
        return baseService.getDatasForUi(type, period, ids);
//        return "{'id': 8}";
    }


}
