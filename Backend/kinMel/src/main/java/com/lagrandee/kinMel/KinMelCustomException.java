package com.lagrandee.kinMel;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KinMelCustomException {
    private int status;
    private String message;
    private long timeStamp;

}
