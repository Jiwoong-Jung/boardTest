package com.study.springboot.controller;

import com.study.springboot.entity.Notice;
import com.study.springboot.repository.NoticeRepository;
import com.study.springboot.service.DetailService;
import com.study.springboot.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class MyController {

    private static final Logger log = LoggerFactory.getLogger(MyController.class);
    private final NoticeRepository noticeRepository;
    private final DetailService detailService;
    private final NoticeService noticeService;

    //@PostConstruct
    public void init() {
        Notice notice = Notice.builder().seq(30000L)
                .title("제목3").content("내용3").build();
        //noticeRepository.saveAndFlush(notice);
        noticeRepository.insertNotice(notice);
    }

    @GetMapping("/")
    public String root() {
        return "view/index";
    }

    @GetMapping("/page2")
    public String listAction(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        //List<Notice> list = noticeRepository.findAll();
        List<Notice> list = noticeRepository.findAllByOrderBySeqDesc();
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), list.size());
        final Page<Notice> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
        model.addAttribute("list", page);
        return "view/list";
    }

    @GetMapping("/insert1")
    public String insert1() {
        Notice notice1 = Notice.builder().seq(1L)
                .title("제목1").content("내용1").parent(0L).build();
        noticeRepository.insertNotice(notice1);
        return "redirect:/page2";
    }
    @GetMapping("/insert2")
    public String insert2() {
        Long seq = noticeRepository.selectMaxSeq();
        Notice notice2 = Notice.builder().seq(seq+2L)
                .title("제목2").content("내용2").parent(0L).build();
        noticeRepository.insertNotice(notice2);
        return "redirect:/page2";
    }

    @GetMapping("/detail")
    public String detail(Long seq, Model model) {
        Notice notice = detailService.detail(seq);
        log.info("arg1: {}, arg2: {}", notice.getSeq(), notice.getTitle());
        model.addAttribute("notice", notice);
        return "view/detail";
    }

    @PostMapping("/addNotice")
    @ResponseBody
    public Notice addNotice(Notice notice) {
        return noticeService.saveReply(notice);
    }
}
