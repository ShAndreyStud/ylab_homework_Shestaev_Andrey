package com.habittracker.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Habit {
    private String name;
    private String description;
    private String frequency;
    private LocalDate createDate;
    private List<HabitCompletion> completions;

    public Habit(String name, String description, String frequency) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.createDate = LocalDate.now();
        this.completions = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public void setCompletions(List<HabitCompletion> completions) {
        this.completions = completions;
    }

    public List<HabitCompletion> getCompletions() {
        return completions;
    }

    public List<HabitCompletion> getCompletionsByStartDate(LocalDate startDate){
        List<HabitCompletion> completionsByStartDate = new ArrayList<>();
        List<HabitCompletion> reverseCompletions = new ArrayList<>(this.completions);
        Collections.reverse(reverseCompletions);
        for (HabitCompletion completion : reverseCompletions){
            LocalDate nextMark = completion.getNextMarkDate();
            if(startDate.isBefore(nextMark)){
                completionsByStartDate.add(completion);
            }
        }

        return completionsByStartDate;
    }


    public void addCompletion(HabitCompletion completion) {
        this.completions.add(completion);
    }
}
