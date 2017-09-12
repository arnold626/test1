package com.saccl.test1;

import android.content.Intent;

/**
 * Created by user on 12/09/2017.
 */

public class DemoObject {

    public DemoObject(int imageId, int score_9_min_run, int score_burpee, int score_hand_grip, int score_plank, int score_push_up, int score_single_leg_stand) {
        this.ImageId = imageId;
        this.score_9_min_run = score_9_min_run;
        this.score_burpee = score_burpee;
        this.score_hand_grip = score_hand_grip;
        this.score_plank = score_plank;
        this.score_push_up = score_push_up;
        this.score_single_leg_stand = score_single_leg_stand;
    }

    public DemoObject()
    {

    }

    public DemoObject(String englishName, String chineseName, int imageId, String youtubeURL, String category, String description, int score_9_min_run, int score_burpee, int score_hand_grip, int score_plank, int score_push_up, int score_single_leg_stand, int score_sit_and_reach, int score_sit_up, int score_stand_long_jump, int score_t_test) {
        this.EnglishName = englishName;
        this.ChineseName = chineseName;
        this.ImageId = imageId;
        this.YoutubeURL = youtubeURL;
        this.Category = category;
        this.Description = description;
        this.score_9_min_run = score_9_min_run;
        this.score_burpee = score_burpee;
        this.score_hand_grip = score_hand_grip;
        this.score_plank = score_plank;
        this.score_push_up = score_push_up;
        this.score_single_leg_stand = score_single_leg_stand;
        this.score_sit_and_reach = score_sit_and_reach;
        this.score_sit_up = score_sit_up;
        this.score_stand_long_jump = score_stand_long_jump;
        this.score_t_test = score_t_test;
    }

    public String getEnglishName() {
        return EnglishName;
    }

    public void setEnglishName(String englishName) {
        EnglishName = englishName;
    }

    public String getChineseName() {
        return ChineseName;
    }

    public void setChineseName(String chineseName) {
        ChineseName = chineseName;
    }

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public String getYoutubeURL() {
        return YoutubeURL;
    }

    public void setYoutubeURL(String youtubeURL) {
        YoutubeURL = youtubeURL;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getScore_9_min_run() {
        return score_9_min_run;
    }

    public void setScore_9_min_run(int score_9_min_run) {
        this.score_9_min_run = score_9_min_run;
    }

    public int getScore_burpee() {
        return score_burpee;
    }

    public void setScore_burpee(int score_burpee) {
        this.score_burpee = score_burpee;
    }

    public int getScore_hand_grip() {
        return score_hand_grip;
    }

    public void setScore_hand_grip(int score_hand_grip) {
        this.score_hand_grip = score_hand_grip;
    }

    public int getScore_plank() {
        return score_plank;
    }

    public void setScore_plank(int score_plank) {
        this.score_plank = score_plank;
    }

    public int getScore_push_up() {
        return score_push_up;
    }

    public void setScore_push_up(int score_push_up) {
        this.score_push_up = score_push_up;
    }

    public int getScore_single_leg_stand() {
        return score_single_leg_stand;
    }

    public void setScore_single_leg_stand(int score_single_leg_stand) {
        this.score_single_leg_stand = score_single_leg_stand;
    }

    public int getScore_sit_and_reach() {
        return score_sit_and_reach;
    }

    public void setScore_sit_and_reach(int score_sit_and_reach) {
        this.score_sit_and_reach = score_sit_and_reach;
    }

    public int getScore_sit_up() {
        return score_sit_up;
    }

    public void setScore_sit_up(int score_sit_up) {
        this.score_sit_up = score_sit_up;
    }

    public int getScore_stand_long_jump() {
        return score_stand_long_jump;
    }

    public void setScore_stand_long_jump(int score_stand_long_jump) {
        this.score_stand_long_jump = score_stand_long_jump;
    }

    public int getScore_t_test() {
        return score_t_test;
    }

    public void setScore_t_test(int score_t_test) {
        this.score_t_test = score_t_test;
    }

    String EnglishName;
    String ChineseName;
    int ImageId;
    String YoutubeURL;
    String Category;
    String Description;
    int score_9_min_run;
    int score_burpee;
    int score_hand_grip;
    int score_plank;
    int score_push_up;
    int score_single_leg_stand;
    int score_sit_and_reach;
    int score_sit_up;
    int score_stand_long_jump;
    int score_t_test;


}
