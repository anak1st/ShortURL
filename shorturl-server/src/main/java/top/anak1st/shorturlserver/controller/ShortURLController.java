package top.anak1st.shorturlserver.controller;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import top.anak1st.shorturlserver.model.R;
import top.anak1st.shorturlserver.service.ShortURLService;

@Controller
public class ShortURLController {
    @Autowired
    private ShortURLService shortURLService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/generate")
    @ResponseBody
    public R generate(@RequestParam String longURL) {
        String shortURL = shortURLService.generateShortURL(longURL);
        return R.ok(shortURL);
    }

    @GetMapping("/urls/all")
    @ResponseBody
    public R all() {
        return R.ok(shortURLService.getAll());
    }

    @DeleteMapping("/urls/all")
    @ResponseBody
    public R deleteAll() {
        shortURLService.deleteAll();
        return R.ok("deleted");
    }

    @GetMapping("/urls/{shortURL}")
    @ResponseBody
    public R get(@PathVariable String shortURL) {
        return R.ok(shortURLService.getByShortURL(shortURL));
    }

    @DeleteMapping("/urls/{shortURL}")
    @ResponseBody
    public R delete(@PathVariable String shortURL) {
        shortURLService.deleteByShortURL(shortURL);
        return R.ok(shortURL);
    }

    @GetMapping("/{shortUrl}")
    public RedirectView redirect(@PathVariable String shortUrl) {
        String longURL = shortURLService.getLongURL(shortUrl);
        if (longURL == null) {
            return new RedirectView("http://anak1st.top/");
        }
        return new RedirectView(longURL);
    }

}


