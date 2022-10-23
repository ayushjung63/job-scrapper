package com.ayush.jobscrap.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@Builder
public class JobScrapPojo {
    private String jobTitle;
    private String company;
    private String location;
    private String description;
    private String salary;
    private String timeLeftToApply;
    private String skills;
    private String redirectLink;
    private String site;

    public String getSkills(){
        return skills==null ? "" : skills;
    }

    public String getSalary(){
        return salary==null ? "Not mentioned" : salary;
    }
}
