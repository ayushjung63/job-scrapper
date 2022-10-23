package com.ayush.jobscrap.service.impl;

import com.ayush.jobscrap.pojo.JobScrapPojo;
import com.ayush.jobscrap.service.JobScrapService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobScrapServiceImpl implements JobScrapService {

    @Value("${kumarijobs.search.url}")
    public String kumariJobUrl;

    @Value("${merojob.search.url}")
    public String meroJobUrl;

    @Override
    public List<JobScrapPojo> scrapByKeyword(String keyword) throws IOException {
        List<JobScrapPojo> overallScrapData=new ArrayList<>();

        List<JobScrapPojo> scrapFromKumariJob = scrapFromKumariJob(keyword);
        overallScrapData.addAll(scrapFromKumariJob);

        List<JobScrapPojo> scrapFromMeroJob = scrapFromMeroJob(keyword);
        overallScrapData.addAll(scrapFromMeroJob);

        return overallScrapData;
    }

    public List<JobScrapPojo> scrapFromMeroJob(String keyword) throws IOException {
        String meroJobFullSearchURL =meroJobUrl+keyword;
        List<JobScrapPojo> meroJobSearchResult=new ArrayList<>();
        Document document = Jsoup.connect(meroJobFullSearchURL).get();
        Element searchResult = document.getElementsByClass("search-results").first();
        Element searchJob = searchResult.getElementById("search_job");

        Elements jobCards= searchJob.getElementsByClass("card mt-3 hover-shadow");

        for (Element jobCard : jobCards) {
            String companyName = jobCard.getElementsByTag("img").first().attr("alt");
            String jobTitle = jobCard.getElementsByTag("h1").first().getElementsByTag("a").first().attr("title");
            String redirectLink = jobCard.getElementsByTag("h1").first().getElementsByTag("a").first().attr("href");
            String location=null;
            if (jobCard.getElementsByClass("location font-12").first() !=null) {
                location = jobCard.getElementsByClass("location font-12").first().getElementsByTag("span").get(2).ownText();
            }
            JobScrapPojo jobScrapPojo = JobScrapPojo.builder()
                    .company(companyName)
                    .jobTitle(jobTitle)
                    .location(location)
                    .site("Mero Job")
                    .build();
            Elements skills=null;
            if (location==null) {
                skills = jobCard.getElementsByClass("media").first().getElementsByClass("badge");
            }else{
                System.out.println();
                if (jobCard.getElementsByClass("media").size()>=2) {
                    skills = jobCard.getElementsByClass("media").get(1).getElementsByClass("badge");
                }
            }
            StringBuilder skillBuffer = new StringBuilder();
            if (skills!=null) {
                for (Element skill : skills) {
                    skillBuffer.append(" " + skill.ownText());
                }
                jobScrapPojo.setSkills(String.valueOf(skillBuffer));
            }


            String timeLeftToApply = jobCard.getElementsByClass("card-footer").first().getElementsByTag("p").attr("title");
            jobScrapPojo.setTimeLeftToApply(timeLeftToApply);
            jobScrapPojo.setRedirectLink("https://merojob.com"+redirectLink);
            meroJobSearchResult.add(jobScrapPojo);
        }
        return meroJobSearchResult;
    }

    public List<JobScrapPojo> scrapFromKumariJob(String keyword) throws IOException {
        String kumariFullSearchURL = kumariJobUrl + keyword;
        List<JobScrapPojo> kumariSearchResultList=new ArrayList<>();
        Document document = Jsoup.connect(kumariFullSearchURL).get();
        Elements jobListingFrame = document.getElementsByClass("job-listing-frame");
        for (Element jobFrame: jobListingFrame) {
            Elements jobListingContent = jobFrame.getElementsByClass("job-listing-content");
            for (Element jobContent : jobListingContent){
                Element jobDetail = jobContent.getElementsByClass("job-detail").first();
                String companyName = jobDetail.getElementsByTag("a").get(1).ownText();
                String skills = jobDetail.getElementsByTag("p").get(1).ownText();
                String jobTitle = jobDetail.getElementsByTag("a").first().ownText();

                String timeLeftToApply = jobDetail.getElementsByClass("show-mobile").first().ownText();
                String description = jobDetail.getElementsByClass("hide-mobile").first().ownText();

                Element ul = jobDetail.getElementsByTag("ul").first();
                Elements lis = ul.getElementsByTag("li");
                String location = lis.first().getElementsByTag("span").first().ownText();
                JobScrapPojo scrapPojo = JobScrapPojo.builder()
                        .company(companyName)
                        .jobTitle(jobTitle)
                        .location(location)
                        .description(description)
                        .timeLeftToApply(timeLeftToApply)
                        .skills(skills)
                        .site("Kumari Job")
                        .build();
                String salary=null;
                if (lis.size()>=2) {
                    salary= lis.get(1).getElementsByTag("span").first().ownText();
                    scrapPojo.setSalary(salary);
                }
                Element rightContent = jobFrame.getElementsByClass("right-content").first();
                String jobLink = rightContent.getElementsByTag("a").first().attr("href");
                scrapPojo.setRedirectLink(jobLink);
                kumariSearchResultList.add(scrapPojo);
            }
        }
        return kumariSearchResultList;
    }
}
