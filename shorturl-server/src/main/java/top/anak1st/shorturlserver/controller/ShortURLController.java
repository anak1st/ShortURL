package top.anak1st.shorturlserver.controller;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import top.anak1st.shorturlserver.model.GenerateCmd;
import top.anak1st.shorturlserver.model.R;
import top.anak1st.shorturlserver.service.ShortURLService;
import top.anak1st.shorturlserver.utils.URLCheck;

@Controller
public class ShortURLController {
    @Autowired
    private ShortURLService shortURLService;

    @GetMapping("/api")
    public RedirectView index() {
        return new RedirectView("http://anak1st.top/");
    }

    @PostMapping("/api/generate")
    @ResponseBody
    public R generate(@RequestBody GenerateCmd cmd) {
        String longURL = cmd.getLongURL();
        if (!URLCheck.check(longURL)) {
            return R.error("Invalid URL");
        }
        String shortURL = shortURLService.generateShortURL(longURL);
        return R.ok(shortURL);
    }

    @GetMapping("/api/urls/all")
    @ResponseBody
    public R all() {
        return R.ok(shortURLService.getAll());
    }

    @DeleteMapping("/api/urls/all")
    @ResponseBody
    public R deleteAll() {
        shortURLService.deleteAll();
        return R.ok("deleted");
    }

    @GetMapping("/api/urls/{shortURL}")
    @ResponseBody
    public R get(@PathVariable String shortURL) {
        return R.ok(shortURLService.getByShortURL(shortURL));
    }

    @DeleteMapping("/api/urls/{shortURL}")
    @ResponseBody
    public R delete(@PathVariable String shortURL) {
        shortURLService.deleteByShortURL(shortURL);
        return R.ok(shortURL);
    }

    @GetMapping("/s/{shortUrl}")
    public RedirectView redirect(@PathVariable String shortUrl) {
        String longURL = shortURLService.getLongURL(shortUrl);
        if (longURL == null) {
            return new RedirectView("http://anak1st.top/");
        }
        return new RedirectView(longURL);
    }
}


