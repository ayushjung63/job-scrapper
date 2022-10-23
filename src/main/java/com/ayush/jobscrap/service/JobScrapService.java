
package com.ayush.jobscrap.service;

import com.ayush.jobscrap.pojo.JobScrapPojo;

import java.io.IOException;
import java.util.List;

public interface JobScrapService {

    List<JobScrapPojo> scrapByKeyword(String keyword) throws IOException;
}
