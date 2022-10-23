package com.ayush.jobscrap;

import com.ayush.jobscrap.pojo.JobScrapPojo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.io.IOException;

public class KumariJob {
    public static void main(String[] args) throws IOException {

        String kumariFullSearchURL = "https://www.kumarijob.com/search?keywords=Java";

        List<JobScrapPojo> kumariSearch=new ArrayList<>();
            Document document = Jsoup.connect(kumariFullSearchURL).get();
            Elements jobListingFrame = document.getElementsByClass("job-listing-frame");
            for (Element jobFrame: jobListingFrame) {
                Elements jobListingContent = jobFrame.getElementsByClass("job-listing-content");
                for (Element jobContent : jobListingContent){
                    Element jobDetail = jobContent.getElementsByClass("job-detail").first();
                    String companyName = jobDetail.getElementsByTag("a").get(1).ownText();
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
                            .build();
                    String salary=null;
                    if (lis.size()>=2) {
                        salary= lis.get(1).getElementsByTag("span").first().ownText();
                        scrapPojo.setSalary(salary);
                    }

                    kumariSearch.add(scrapPojo);
                }
            }

    }
}
