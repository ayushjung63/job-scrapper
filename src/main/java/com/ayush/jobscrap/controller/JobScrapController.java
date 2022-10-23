package com.ayush.jobscrap.controller;

import com.ayush.jobscrap.pojo.JobScrapPojo;
import com.ayush.jobscrap.service.JobScrapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/job-scrap")
public class JobScrapController {

    private final JobScrapService jobScrapService;

    public JobScrapController(JobScrapService jobScrapService) {
        this.jobScrapService = jobScrapService;
    }

    @GetMapping("/keyword/{keyword}")
    public ResponseEntity fetchJobList(@PathVariable("keyword") String keyword) throws IOException {
        List<JobScrapPojo> jobScrapPojos = jobScrapService.scrapByKeyword(keyword);
        return ResponseEntity.ok(jobScrapPojos);
    }
}
