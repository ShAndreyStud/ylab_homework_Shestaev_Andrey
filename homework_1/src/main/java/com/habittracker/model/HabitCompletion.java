package com.habittracker.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class HabitCompletion {
    private LocalDate markDate;
    private LocalDate nextMarkDate;


    public HabitCompletion(Habit habit) {
        this.markDate = LocalDate.now();
        if (habit.getFrequency().equals("daily")) {
            this.nextMarkDate = markDate.plusDays(1);
        } else {
            LocalDate today = LocalDate.now();
            LocalDate start = habit.getCreateDate();
            int weeksToAdd = (int) ChronoUnit.WEEKS.between(start, today) + 1;
            this.nextMarkDate = start.plusWeeks(weeksToAdd);
        }
    }


    public LocalDate getMarkDate() {
        return markDate;
    }

    public LocalDate getNextMarkDate() {
        return nextMarkDate;
    }

    public void setNextMarkDate(LocalDate nextMarkDate) {
        this.nextMarkDate = nextMarkDate;
    }

    public void setMarkDate(LocalDate markDate){
        this.markDate = markDate;
    }
}
