package com.ayush.jobscrap;

import com.ayush.jobscrap.pojo.JobScrapPojo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MeroJobScrap {
    public static void main(String[] args) throws IOException {
        String meroJobFullSearchURL = "https://merojob.com/search/?q=java";
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
                 skills = jobCard.getElementsByClass("media").get(1).getElementsByClass("badge");
            }
            StringBuilder skillBuffer = new StringBuilder();
            for (Element skill : skills) {
                skillBuffer.append(" " + skill.ownText());
            }
            jobScrapPojo.setSkills(String.valueOf(skillBuffer));

            String timeLeftToApply = jobCard.getElementsByClass("card-footer").first().getElementsByTag("p").attr("title");
            jobScrapPojo.setTimeLeftToApply(timeLeftToApply);
            jobScrapPojo.setRedirectLink("https://merojob.com"+redirectLink);
            meroJobSearchResult.add(jobScrapPojo);
        }

        for (JobScrapPojo pojo: meroJobSearchResult){
            System.out.println(pojo);
        }

    }
}
