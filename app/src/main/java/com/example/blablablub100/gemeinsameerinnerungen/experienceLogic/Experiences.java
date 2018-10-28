package com.example.blablablub100.gemeinsameerinnerungen.experienceLogic;

import com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser.ExperienceReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Experiences {
    private List<Experience> experienceList = new ArrayList<Experience>();

    public Experiences() {
        intializeExperiences();
    }

    public void intializeExperiences() {
        experienceList = ExperienceReader.readExperiences();
        Collections.sort(experienceList);
    }

    public List<Experience> getExperiences() {
        return experienceList;
    }

    public void saveExperiences() {

    }

    public boolean isEmpty() {
        return (experienceList.size() == 0);
    }

    public Experience getExperience(int pos) {
        return experienceList.get(pos);
    }
}
