package com.lagrandee.kinMel.helper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

public class StaticPaths {

//    public static final String profilePath = new ClassPathResource("static\\images").getPath();
public static String getProfilePath() throws IOException {
    Resource resource = new ClassPathResource("static/images");
    File file = resource.getFile();
    System.out.println(file.getAbsolutePath());
    return file.getAbsolutePath();
}

    public static String getCategoryPath() throws IOException {
        Resource resource = new ClassPathResource("static/category");
        File file = resource.getFile();
        System.out.println(file.getAbsolutePath());
        return file.getAbsolutePath();
    }



    public static String getProfileDefaultPath(){
        return "images/default.png";
    }

//    public static final String defaultPath = new ClassPathResource("static\\images\\default.png").getPath();
}
