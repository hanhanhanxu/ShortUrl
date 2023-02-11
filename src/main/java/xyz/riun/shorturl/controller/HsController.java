package xyz.riun.shorturl.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HsController {

    @RequestMapping("hs")
    public String hs() {
        return "ok";
    }
}
